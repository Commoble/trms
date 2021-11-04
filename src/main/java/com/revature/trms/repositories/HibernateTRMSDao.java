package com.revature.trms.repositories;

import com.revature.trms.models.Approval;
import com.revature.trms.models.Attachment;
import com.revature.trms.models.Department;
import com.revature.trms.models.Employee;
import com.revature.trms.models.EventType;
import com.revature.trms.models.GradingFormat;
import com.revature.trms.models.ReimbursementRequest;
import com.revature.trms.models.Role;
import com.revature.trms.util.HibernateUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Session;
import org.jetbrains.annotations.Nullable;

import javax.persistence.PersistenceException;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Root;
import javax.persistence.criteria.Subquery;
import java.util.Collections;
import java.util.List;

public class HibernateTRMSDao implements TRMSDao
{
	public static final Logger LOGGER = LogManager.getLogger();

	private final CrudDao<Approval> approvalDao;
	private final CrudDao<Attachment> attachmentDao;
	private final CrudDao<Department> departmentDao;
	private final CrudDao<Employee> employeeDao;
	private final CrudDao<EventType> eventTypeDao;
	private final CrudDao<GradingFormat> gradingFormatDao;
	private final CrudDao<ReimbursementRequest> requestDao;
	private final CrudDao<Role> getRoleDao;

	public HibernateTRMSDao(CrudDao<Approval> approvalDao, CrudDao<Attachment> attachmentDao, CrudDao<Department> departmentDao, CrudDao<Employee> employeeDao, CrudDao<EventType> eventTypeDao, CrudDao<GradingFormat> gradingFormatDao, CrudDao<ReimbursementRequest> requestDao, CrudDao<Role> getRoleDao)
	{
		this.approvalDao = approvalDao;
		this.attachmentDao = attachmentDao;
		this.departmentDao = departmentDao;
		this.employeeDao = employeeDao;
		this.eventTypeDao = eventTypeDao;
		this.gradingFormatDao = gradingFormatDao;
		this.requestDao = requestDao;
		this.getRoleDao = getRoleDao;
	}

	@Override
	public CrudDao<Approval> getApprovalDao()
	{
		return this.approvalDao;
	}

	@Override
	public CrudDao<Attachment> getAttachmentDao()
	{
		return this.attachmentDao;
	}

	@Override
	public CrudDao<Department> getDepartmentDao()
	{
		return this.departmentDao;
	}

	@Override
	public CrudDao<Employee> getEmployeeDao()
	{
		return this.employeeDao;
	}

	@Override
	public CrudDao<EventType> getEventTypeDao()
	{
		return this.eventTypeDao;
	}

	@Override
	public CrudDao<GradingFormat> getGradingFormatDao()
	{
		return this.gradingFormatDao;
	}

	@Override
	public CrudDao<ReimbursementRequest> getRequestDao()
	{
		return this.requestDao;
	}

	@Override
	public CrudDao<Role> getRoleDao()
	{
		return this.getRoleDao;
	}

	@Nullable
	@Override
	public Employee getEmployee(String username, String password)
	{
		LOGGER.debug("Looking up employee from credentials");
		try (Session session = HibernateUtils.getSession())
		{
			Class<Employee> tableClass = Employee.class;
			CriteriaBuilder builder = session.getCriteriaBuilder();
			CriteriaQuery<Employee> criteriaQuery = builder.createQuery(tableClass);
			Root<Employee> root = criteriaQuery.from(tableClass);
			return session.createQuery(
				criteriaQuery.select(root)
					.where(builder.and(
						builder.equal(root.get("username"), username),
						builder.equal(root.get("password"), password))))
				.getSingleResult();
		}
		catch (PersistenceException e)
		{
			LOGGER.debug("Encountered persistence exception trying to get employee from credentials:", e);
			return null;
		}
	}

	@Override
	public List<ReimbursementRequest> getRequestsOwnedBy(int employeeID)
	{
		LOGGER.debug("Getting reimbursement requests submitted by employee {}", employeeID);
		try (Session session = HibernateUtils.getSession())
		{
			Class<ReimbursementRequest> tableClass = ReimbursementRequest.class;
			CriteriaBuilder builder = session.getCriteriaBuilder();
			CriteriaQuery<ReimbursementRequest> criteriaQuery = builder.createQuery(tableClass);
			Root<ReimbursementRequest> root = criteriaQuery.from(tableClass);

			return session.createQuery(
					criteriaQuery.where(
						builder.equal(
							root.get("employee"), employeeID)))
				.getResultList();

		}
		catch (PersistenceException e)
		{
			LOGGER.debug("Encountered persistence exception trying to get employee's own requests:", e);
			return Collections.emptyList();
		}
	}

	public List<Approval> getApprovalsForRequests(int requestID)
	{
		try(Session session = HibernateUtils.getSession())
		{
			CriteriaBuilder builder = session.getCriteriaBuilder();

			Class<Approval> tableClass = Approval.class;
			CriteriaQuery<Approval> criteriaQuery = builder.createQuery(tableClass);
			Root<Approval> root = criteriaQuery.from(tableClass);

			return session.createQuery(
					criteriaQuery.where(
						builder.equal(root.get("request"), requestID)
					)
				)
				.getResultList();

//			// count denied approvals
//
//			Subquery<Long> subQuery = criteriaQuery.subquery(Long.class);
//			Root<Approval> subRoot = subQuery.from(Approval.class);
//			Join<Approval,ReimbursementRequest> join = subRoot.join("request");
//			subQuery = subQuery.select(builder.count(join))
//					.where(builder.and(
//						builder.equal(join.get("requestID"), subRoot.get("request")),
//						builder.equal(subRoot.get("approved"), false)
//					));
//
//			return session.createQuery(
//				criteriaQuery.where(
//					builder.and(
//						builder.equal(root.get("employee"), employeeID),
//						builder.or(
//							builder.equal(root.get("awarded"), true),
//							builder.equal(subQuery, 0L)
//						)
//					)))
//				.getResultList();
		}
		catch(PersistenceException e)
		{
			LOGGER.debug("Encountered persistence exception getting pending requests for employee:", e);
			return Collections.emptyList();
		}
	}
}
