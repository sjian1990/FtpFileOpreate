package com.pydata.csindex.controller;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.pydata.csindex.entity.BondZhongzhengTeEntity;
import com.pydata.csindex.entity.BondZhongzhengVaEntity;
import com.pydata.csindex.service.ZhongZhengDataService;
import com.pydata.csindex.util.FileUtil;
import com.pydata.csindex.util.FtpUtil;

@RestController
public class ZhongZhengDataController {

	private static Logger logger = LoggerFactory.getLogger(ZhongZhengDataController.class);

	@Value("${ftp.file.desPath}")
	private String desPath;

	@Autowired
	private ZhongZhengDataService service;
	
	/**
	 * 获取中证数据，获取bond_termstructure.zip的数据
	 * @return
	 */
	@GetMapping("/getCSIndexBondTermstructure")
	public void getCSIndexBondTermstructure(HttpServletRequest request) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		Calendar cal = Calendar.getInstance();
		String date = sdf.format(cal.getTime());
		if(request != null && !StringUtils.isEmpty(request.getParameter("date"))) {
			date = request.getParameter("date");
		}
		
		String fileName = date + "bond_termstructure.zip";
		try {
			String fromPath = "/" + fileName;
			String downloadPath = desPath + fileName;

			// 下载FTP文件
			boolean b = FtpUtil.getFTPFile(fromPath, downloadPath);
			// 如果下载成功，获取指定文件
			if (b) {
				FileUtil.writeZipFile(downloadPath, desPath, date + "bond_termstructure.txt");
			}

			// 按行读取文件
			List<String> list = FileUtil.readTXTByLine(desPath + date + "bond_termstructure.txt");
			//List<String> list = FileUtil.readTXTByLine(desPath + "unzip/20180411bond_termstructure.txt");
			
			List<BondZhongzhengTeEntity> tees = new ArrayList<>();
			
			list.stream().filter(p -> {
				String[] splits = p.split("\\|");
				if (splits.length < 3) {
					return false;
				} else {
					return true;
				}
			}).collect(Collectors.toList()).forEach(p -> {
				String[] str = p.split("\\|");
				BondZhongzhengTeEntity tee = new BondZhongzhengTeEntity();
				tee.setS1(str[0].trim());
				tee.setS2(str[1].trim());
				tee.setS3(str[2].trim());
				tee.setS4(str[3].trim());
				tee.setS5(str[4].trim());
				tee.setS6(str[5].trim());
				tee.setS7(str[6].trim());
				tee.setOpdateTime(new Date());
				tees.add(tee);
				//service.addBondZhongzhengTe(tee);
			});

			service.addBondZhongzhengTes(tees);
		} catch (Exception e) {
			logger.error("从中证获取数据失败，失败信息message：",e);
		}
	}

	/**
	 * 获取中证数据，获取bond_valuation.zip的数据
	 * @return
	 */
	@GetMapping("/getCSIndexBondValuation")
	public void getCSIndexBondValuation(HttpServletRequest request) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		Calendar cal = Calendar.getInstance();
		String date = sdf.format(cal.getTime());
		if(request != null && !StringUtils.isEmpty(request.getParameter("date"))) {
			date = request.getParameter("date");
		}
		String fileName = date + "bond_valuation.zip";
		try {
			String fromPath = "/" + fileName;
			String downloadPath = desPath + fileName;

			// 下载FTP文件
			boolean b = FtpUtil.getFTPFile(fromPath, downloadPath);
			// 如果下载成功，获取指定文件
			if (b) {
				FileUtil.writeZipFile(downloadPath, desPath , date + "bond_valuation.txt");
			}

			// 按行读取文件
			List<String> list = FileUtil.readTXTByLine(desPath + date + "bond_valuation.txt");
			//List<String> list = FileUtil.readTXTByLine(desPath + "unzip/20180411bond_termstructure.txt");
			
			List<BondZhongzhengVaEntity> vaes = new ArrayList<>();
			
			list.stream().filter(p -> {
				String[] splits = p.split("\\|");
				if (splits.length < 3) {
					return false;
				} else {
					return true;
				}
			}).collect(Collectors.toList()).forEach(p -> {
				String[] str = p.split("\\|");
				BondZhongzhengVaEntity vae = new BondZhongzhengVaEntity();
				vae.setS1(str[0].trim());
				vae.setS2(str[1].trim());
				vae.setS3(str[2].trim());
				vae.setS4(str[3].trim());
				vae.setS5(str[4].trim());
				vae.setS6(str[5].trim());
				vae.setS7(str[6].trim());
				vae.setS8(str[7].trim());
				vae.setS9(str[8].trim());
				vae.setS10(str[9].trim());
				vae.setS11(str[10].trim());
				vae.setOpdateTime(new Date());
				vaes.add(vae);
			});
			service.addBondZhongzhengVas(vaes);
		} catch (Exception e) {
			logger.error("从中证获取数据失败，失败信息message：",e);
		}
	}

}
