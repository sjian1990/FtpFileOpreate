package com.pydata.csindex;

import javax.sql.DataSource;

import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.scheduling.annotation.EnableScheduling;

import com.alibaba.druid.pool.DruidDataSource;

@EnableScheduling
@MapperScan("com.pydata.csindex.dao.mybatis")
@SpringBootApplication
public class CSIndexDataTRansApplication {

	@Bean
	@ConfigurationProperties(prefix = "spring.datasource")
	public DataSource dataSource() {
		return new DruidDataSource();
	}
	
	@Bean
	public SqlSessionFactory sqlSessionFactory() throws Exception{
		SqlSessionFactoryBean sqlSessionFactory = new SqlSessionFactoryBean();
		sqlSessionFactory.setDataSource(dataSource());
		
		Configuration configuration = new Configuration();
		configuration.setLogPrefix("dao.");//控制台输出sql语句
		configuration.setMapUnderscoreToCamelCase(true);//用于驼峰法显示结果
		sqlSessionFactory.setConfiguration(configuration);
		
		PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
		sqlSessionFactory.setMapperLocations(resolver.getResources("classpath:/mybatis/*.xml"));
		sqlSessionFactory.setTypeAliasesPackage("com.pydata.csindex.entity");
		return sqlSessionFactory.getObject();
	}
	
	public static void main(String[] args) {
		SpringApplication.run(CSIndexDataTRansApplication.class, args);
	}

}
