package com.pydata.csindex.dao.mybatis;

import java.util.List;

import com.pydata.csindex.entity.BondZhongzhengTeEntity;
import com.pydata.csindex.entity.BondZhongzhengVaEntity;

public interface ZhongZhengDataDao {

	void addBondZhongzhengTes(List<BondZhongzhengTeEntity> tees) throws Exception;

	void addBondZhongzhengTe(BondZhongzhengTeEntity tee);

	void addBondZhongzhengVas(List<BondZhongzhengVaEntity> vees)  throws Exception;

}
