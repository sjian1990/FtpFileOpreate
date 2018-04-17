package com.pydata.csindex.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.pydata.csindex.dao.mybatis.ZhongZhengDataDao;
import com.pydata.csindex.entity.BondZhongzhengTeEntity;
import com.pydata.csindex.entity.BondZhongzhengVaEntity;
import com.pydata.csindex.service.ZhongZhengDataService;

@Service
public class ZhongZhengDataServiceImpl implements ZhongZhengDataService {

	@Autowired
	private ZhongZhengDataDao dao;
	
	@Override
	public void addBondZhongzhengTes(List<BondZhongzhengTeEntity> tees) throws Exception {
		dao.addBondZhongzhengTes(tees);
	}

	@Override
	public void addBondZhongzhengTe(BondZhongzhengTeEntity tee) {
		try {
			dao.addBondZhongzhengTe(tee);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void addBondZhongzhengVas(List<BondZhongzhengVaEntity> vees) throws Exception {
		dao.addBondZhongzhengVas(vees);
	}
	
}
