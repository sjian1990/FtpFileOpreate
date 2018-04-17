package com.pydata.csindex.schelude;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.pydata.csindex.controller.ZhongZhengDataController;

@Component
public class ZhongZhengDataSchelude {
	
	@Autowired
	private ZhongZhengDataController rediectTo;

	@Scheduled(cron = "0 0 18 ? * *")
	public void ZhongZhengDataTransSchelude() {
		rediectTo.getCSIndexBondValuation(null);
		rediectTo.getCSIndexBondTermstructure(null);
	}
	
}
