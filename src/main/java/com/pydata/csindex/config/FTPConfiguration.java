package com.pydata.csindex.config;
import javax.annotation.PreDestroy;

import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.pool2.ObjectPool;
import org.apache.commons.pool2.PooledObject;
import org.apache.commons.pool2.PooledObjectFactory;
import org.apache.commons.pool2.impl.DefaultPooledObject;
import org.apache.commons.pool2.impl.GenericObjectPool;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import com.pydata.csindex.util.FtpUtil;

/**
 * FTP配置类
 * 
 * @author wxyh
 */
@Configuration
@ConditionalOnClass({GenericObjectPool.class, FTPClient.class})
@ConditionalOnProperty(value = "ftp.enabled", havingValue = "true")
@EnableConfigurationProperties(FTPConfiguration.FtpConfigProperties.class)
public class FTPConfiguration {

    private ObjectPool<FTPClient> pool;

    public FTPConfiguration(FtpConfigProperties props) {
        // 默认最大连接数与最大空闲连接数都为8，最小空闲连接数为0
        // 其他未设置属性使用默认值，可根据需要添加相关配置
        GenericObjectPoolConfig poolConfig = new GenericObjectPoolConfig();
        poolConfig.setTestOnBorrow(true);
        poolConfig.setTestOnReturn(true);
        poolConfig.setTestWhileIdle(true);
        poolConfig.setMinEvictableIdleTimeMillis(60000);
        poolConfig.setSoftMinEvictableIdleTimeMillis(50000);
        poolConfig.setTimeBetweenEvictionRunsMillis(30000);
        pool = new GenericObjectPool<>(new FtpClientPooledObjectFactory(props), poolConfig);
        preLoadingFtpClient(props.getInitialSize(), poolConfig.getMaxIdle());
        // 初始化ftp工具类中的ftpClientPool
        FtpUtil.init(pool);
    }

    /**
     * 预先加载FTPClient连接到对象池中
     * @param initialSize 初始化连接数
     * @param maxIdle 最大空闲连接数
     */
    private void preLoadingFtpClient(Integer initialSize, int maxIdle) {
        if (initialSize == null || initialSize <= 0) {
            return;
        }

        int size = Math.min(initialSize.intValue(), maxIdle);
        for (int i = 0; i < size; i++) {
            try {
                pool.addObject();
            } catch (Exception e) {
                //
            }
        }
    }

    @PreDestroy
    public void destroy() {
        if (pool != null) {
            pool.close();
           //
        }
    }

    /**
     * Ftp配置属性类，建立ftpClient时使用
     */
    @ConfigurationProperties(prefix = "ftp")
    static class FtpConfigProperties {

        private String host ;

        private int port ;

        private String username;

        private String password;

        private int bufferSize = 8096;

        /**
         * 初始化连接数
         */
        private Integer initialSize = 0;

		public String getHost() {
			return host;
		}

		public void setHost(String host) {
			this.host = host;
		}

		public int getPort() {
			return port;
		}

		public void setPort(int port) {
			this.port = port;
		}

		public String getUsername() {
			return username;
		}

		public void setUsername(String username) {
			this.username = username;
		}

		public String getPassword() {
			return password;
		}

		public void setPassword(String password) {
			this.password = password;
		}

		public int getBufferSize() {
			return bufferSize;
		}

		public void setBufferSize(int bufferSize) {
			this.bufferSize = bufferSize;
		}

		public Integer getInitialSize() {
			return initialSize;
		}

		public void setInitialSize(Integer initialSize) {
			this.initialSize = initialSize;
		}
        
    }

    /**
     * FtpClient对象工厂类
     */
    static class FtpClientPooledObjectFactory implements PooledObjectFactory<FTPClient> {

        private FtpConfigProperties props;

        public FtpClientPooledObjectFactory(FtpConfigProperties props) {
            this.props = props;
        }

        @Override
        public PooledObject<FTPClient> makeObject() throws Exception {
            FTPClient ftpClient = new FTPClient();
            try {
                ftpClient.connect(props.getHost(), props.getPort());
                ftpClient.login(props.getUsername(), props.getPassword());
                //System.out.println("连接FTP服务器返回码:"+ ftpClient.getReplyCode() + ftpClient.getReply());
                ftpClient.setBufferSize(props.getBufferSize());
                //ftpClient.setControlEncoding(props.getEncoding());
                ftpClient.setFileType(FTPClient.BINARY_FILE_TYPE);
                ftpClient.enterLocalPassiveMode();
                return new DefaultPooledObject<>(ftpClient);
            } catch (Exception e) {
                //log.error("建立FTP连接失败", e);
                if (ftpClient.isAvailable()) {
                    ftpClient.disconnect();
                }
                ftpClient = null;
                throw new Exception("建立FTP连接失败", e);
            }
        }

        @Override
        public void destroyObject(PooledObject<FTPClient> p) throws Exception {
            FTPClient ftpClient = getObject(p);
            if (ftpClient != null && ftpClient.isConnected()) {
                ftpClient.disconnect();
            }
        }

        @Override
        public boolean validateObject(PooledObject<FTPClient> p) {
            FTPClient ftpClient = getObject(p);
            if (ftpClient == null || !ftpClient.isConnected()) {
                return false;
            }
            try {
                ftpClient.changeWorkingDirectory("/");
                return true;
            } catch (Exception e) {
                //log.error("验证FTP连接失败::{}", ExceptionUtils.getStackTrace(e));
                return false;
            }
        }

        @Override
        public void activateObject(PooledObject<FTPClient> p) throws Exception {
        }

        @Override
        public void passivateObject(PooledObject<FTPClient> p) throws Exception {
        }

        private FTPClient getObject(PooledObject<FTPClient> p) {
            if (p == null || p.getObject() == null) {
                return null;
            }
            return p.getObject();
        }

    }

}