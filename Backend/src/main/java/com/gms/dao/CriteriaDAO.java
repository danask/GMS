package com.gms.dao;

import java.util.List;

import com.gms.model.Criteria;

public interface CriteriaDAO 
{
	public Criteria getCriteria();
	
	public void updateCriteria(Criteria criteria);
	
	public String getEmailNotification();
}
