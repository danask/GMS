package com.gms.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gms.dao.CriteriaDAO;
import com.gms.model.Criteria;

@Service("CriteriaService")
@Transactional
public class CriteriaServiceImpl implements CriteriaService
{

	@Autowired
	private CriteriaDAO criteriaDAO;
	
	@Override
	public Criteria getCriteria() {
		return criteriaDAO.getCriteria();
	}

	@Override
	public void updateCriteria(Criteria criteria) { 
		criteriaDAO.updateCriteria(criteria);
	}

}
