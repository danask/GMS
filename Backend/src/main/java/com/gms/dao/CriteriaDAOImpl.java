package com.gms.dao;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.gms.model.Criteria;


@Repository
public class CriteriaDAOImpl implements CriteriaDAO
{
	@Autowired
	private SessionFactory sessionFactory;
	
	@Override
	public Criteria getCriteria() {
		List<Criteria> criteria = new ArrayList<Criteria>();
		
		criteria = sessionFactory.getCurrentSession()
			.createQuery("from Criteria")
			.list();

		if (criteria.size() > 0) 
		{
			return criteria.get(0);
		} 
		else 
		{
			return null;
		}	
	}

	@Override
	public void updateCriteria(Criteria criteria) {

		sessionFactory.getCurrentSession().update(criteria);
	}

	@Override
	public String getEmailNotification()
	{
		List<Criteria> criteria = new ArrayList<Criteria>();
		
		criteria = sessionFactory.getCurrentSession()
			.createQuery("from Criteria")
			.list();

		if (criteria.size() > 0) 
		{
			return criteria.get(0).getEmailNotification();
		} 
		else 
		{
			return null;
		}	
	}
}
