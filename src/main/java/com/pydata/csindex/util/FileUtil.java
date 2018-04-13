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
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

public class FileUtil {
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
				System.out.println("解压时写入文件失败");
			}
			write.flush();
			write.close();
			read.close();
		} catch (Exception e) {
			e.printStackTrace();
		} 
	}

	
}
