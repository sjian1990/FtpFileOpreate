package com.pydata.csindex.util;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import com.google.common.base.CharMatcher;

public class FileUtil {
	private static Logger logger = LoggerFactory.getLogger(FileUtil.class);
	
	/**
	 * @describe 将压缩文件中的某一文件解压到指定文件夹
	 * @param file
	 *            压缩包路径
	 * @param saveRootDirectory
	 *            写入文件夹路径
	 * @param fileName
	 *            文件名
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	public static void writeZipFile(String file, String saveRootDirectory, String fileName) {
		try {
			int len = 0;
			ZipFile zf = new ZipFile(file);
			ZipEntry ze = zf.getEntry(fileName);
			InputStream read = zf.getInputStream(ze);
			File writeFile = new File(saveRootDirectory + fileName);
			File rootDirectoryFile = new File(saveRootDirectory);
			// 创建目录
			if (!rootDirectoryFile.exists()) {
				rootDirectoryFile.mkdirs();
			}
			// 创建文件
			writeFile.createNewFile();

			BufferedOutputStream write = new BufferedOutputStream(new FileOutputStream(writeFile));
			try {
				// 写入文件内容
				while ((len = read.read()) != -1) {
					write.write(len);
				}
			}catch (Exception e) {
				logger.error("解压失败，失败信息message:",e);
			}
			
			write.flush();
			write.close();
			read.close();
		} catch (Exception e) {
			e.printStackTrace();
		} 
	}

	public static List<String> readTXTByLine(String pathname) {
		List<String> returnList = new ArrayList<>();
		String line = "";
		try {
			File file = new File(pathname); // 要读取以上路径的input.txt文件
			InputStreamReader reader = new InputStreamReader(new FileInputStream(file)); // 建立一个输入流对象reader
			BufferedReader br = new BufferedReader(reader); // 建立一个对象，它把文件内容转成计算机能读懂的语言
			line = br.readLine();
			while (line != null) {
				line = br.readLine(); // 一次读入一行数据
				if( !StringUtils.isEmpty(line)) {
					returnList.add(CharMatcher.WHITESPACE.replaceFrom(line, " "));
				}				
			}
			br.close();
			file.delete();
		} catch (Exception e) {
			logger.error("错误信息:", e);
		}
		return returnList;
	}
	
}
