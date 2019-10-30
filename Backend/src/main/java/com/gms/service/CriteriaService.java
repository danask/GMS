package com.gms.service;

import java.util.List;

import com.gms.model.Criteria;

public interface CriteriaService 
{
	public Criteria getCriteria();
	
	public void updateCriteria(Criteria criteria);
	
	public String getEmailNotification();
}
