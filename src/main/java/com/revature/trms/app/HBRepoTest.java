package com.revature.trms.app;

import com.revature.trms.models.Department;
import com.revature.trms.models.EventType;
import com.revature.trms.repositories.CrudDao;
import com.revature.trms.repositories.HibernateCrudDao;
import com.revature.trms.util.HibernateUtils;
import org.hibernate.SessionFactory;

import java.util.List;

public class HBRepoTest
{
	public static void main(String[] args)
	{
		SessionFactory sf = HibernateUtils.getSession().getSessionFactory();
		CrudDao<Department> departmentRepo = new HibernateCrudDao<>(Department.class, "departmentID", Department::setDepartmentID);
		CrudDao<EventType> eventTypeRepo = new HibernateCrudDao<>(EventType.class, "eventTypeID", EventType::setEventTypeID);
////		DepartmentRecord dept = new DepartmentRecord("Silly Walks");
////		repo.addDepartment(dept);
////		System.out.println(dept);
//
		List<Department> depts = departmentRepo.getAll();
		List<EventType> eventTypes = eventTypeRepo.getAll();
		System.out.println(depts);
		System.out.println(eventTypes);
//
//		DepartmentRecord d = repo.getDepartment(6);
//		System.out.println(d);
//
//		d.setName("Fine Cheeses");
//		repo.updateDepartment(d);
//		System.out.println(d);
//
//		boolean deleted = repo.deleteDepartment(d.getDepartmentID());
//		System.out.println(deleted);
	}
}
