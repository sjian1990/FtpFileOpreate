package com.pydata.csindex.service;

import java.util.List;

import com.pydata.csindex.entity.BondZhongzhengTeEntity;
import com.pydata.csindex.entity.BondZhongzhengVaEntity;

public interface ZhongZhengDataService {

	void addBondZhongzhengTes(List<BondZhongzhengTeEntity> tees) throws Exception;

	void addBondZhongzhengTe(BondZhongzhengTeEntity tee);

	void addBondZhongzhengVas(List<BondZhongzhengVaEntity> vees) throws Exception;

}
