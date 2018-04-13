package com.pydata.csindex.controller;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.pydata.csindex.util.FileUtil;
import com.pydata.csindex.util.FtpUtil;

@RestController
public class FileDownloadController {
	
	@Value("${ftp.file.desPath}")
	private String desPath;

	@GetMapping("/getCSIndexFile")
	public Object getCSIndexFile() {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DATE, -1);
		String date = sdf.format(cal.getTime());
		String fileName = date + "bond_termstructure.zip";
		try {
			String fromPath = "/"+fileName;
			String downloadPath = desPath + fileName;
			
			//下载FTP文件
			//boolean b = FtpUtil.getFTPFile(fromPath, downloadPath);
			//如果下载成功，获取指定文件
			//if(b) {
				//FileUtil.writeZipFile(downloadPath, desPath + "unzip/", date + "bond_termstructure.txt");
			//}
			
			//按行读取文件，并打印
            readTXTByLine(desPath + "unzip/" + date + "bond_termstructure.txt");
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	
	private void readTXTByLine(String pathname) {
		String line = "";  
		try {
	        File filename = new File(pathname); // 要读取以上路径的input。txt文件  
	        InputStreamReader reader = new InputStreamReader(  
	                new FileInputStream(filename)); // 建立一个输入流对象reader  
	        BufferedReader br = new BufferedReader(reader); // 建立一个对象，它把文件内容转成计算机能读懂的语言  
	        
	        line = br.readLine();  
	        while (line != null) {  
	            line = br.readLine(); // 一次读入一行数据  
	            System.out.println(line);
	        } 
	        br.close();
		}catch (Exception e) {
			e.printStackTrace();
		}
	}
	
}
