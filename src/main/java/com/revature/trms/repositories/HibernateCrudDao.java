package com.revature.trms.repositories;

import com.revature.trms.util.HibernateUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import org.jetbrains.annotations.Nullable;

import javax.persistence.PersistenceException;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaDelete;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.Collections;
import java.util.List;
import java.util.function.ObjIntConsumer;

public class HibernateCrudDao<T> implements CrudDao<T>
{
	private static final Logger LOGGER = LogManager.getLogger();

	private final Class<T> tableClass;
	private final String idField;
	private final ObjIntConsumer<T> idSetter;

	public HibernateCrudDao(Class<T> tableClass, String idField, ObjIntConsumer<T> idSetter)
	{
		this.tableClass = tableClass;
		this.idField = idField;
		this.idSetter = idSetter;
	}

	public Class<T> getTableClass()
	{
		return this.tableClass;
	}

	public String getIdField()
	{
		return this.idField;
	}

	public ObjIntConsumer<T> getIdSetter()
	{
		return this.idSetter;
	}


	@Nullable
	@Override
	public T add(T data)
	{
		Session session = HibernateUtils.getSession();
		try
		{
			session.beginTransaction();
			int id = (int)session.save(data); // calls 'insert into' sql
			session.getTransaction().commit();
			this.getIdSetter().accept(data, id);
			return data;
		}
		catch (PersistenceException e)
		{
			LOGGER.error("Encountered persistence exception trying to add new record:", e);
			session.getTransaction().rollback();
			return null;
		}
		finally
		{
			session.close();
		}
	}

	@Override
	public List<T> getAll()
	{
		try(Session session = HibernateUtils.getSession())
		{
			Class<T> tableClass = this.getTableClass();
			CriteriaBuilder builder = session.getCriteriaBuilder();
			CriteriaQuery<T> criteriaQuery = builder.createQuery(tableClass);
			Root<T> root = criteriaQuery.from(tableClass);
			criteriaQuery.select(root);
			Query<T> query = session.createQuery(criteriaQuery);
			return query.getResultList();
		}
		catch(PersistenceException e)
		{
			LOGGER.error("Encountered persistence exception getting records:", e);
			return Collections.emptyList();
		}
	}

	@Nullable
	@Override
	public T get(int id)
	{
		try(Session session = HibernateUtils.getSession())
		{
			return session.get(this.getTableClass(), id);
		}
		catch(PersistenceException e)
		{
			LOGGER.debug("Encountered persistence exception getting record:", e);
			return null;
		}
	}

	@Nullable
	@Override
	public T update(T data)
	{
		Session session = HibernateUtils.getSession();
		Transaction transaction = session.beginTransaction();
		try
		{
			session.update(data);
			transaction.commit();
			return data;
		}
		catch(PersistenceException e)
		{
			LOGGER.error("Encountered persistence exception updating record:", e);
			transaction.rollback();
			return null;
		}
		finally
		{
			session.close();
		}
	}

	@Override
	public boolean delete(int id)
	{
		Class<T> tableClass = this.getTableClass();
		Session session = HibernateUtils.getSession();
		Transaction transaction = session.beginTransaction();
		try
		{
			CriteriaBuilder builder = session.getCriteriaBuilder();
			CriteriaDelete<T> deleteCriteria =
				builder.createCriteriaDelete(tableClass);
			Root<T> root = deleteCriteria.from(tableClass);
			Path<T> idPath = root.get(this.getIdField()); // java field name
			Predicate equalsPred = builder.equal(idPath, id);
			deleteCriteria = deleteCriteria.where(equalsPred);
			Query query = session.createQuery(deleteCriteria);
			query.executeUpdate();
			transaction.commit();
			return true;
		}
		catch(PersistenceException e)
		{
			LOGGER.error("Encountered persistence exception deleting record:", e);
			transaction.rollback();
			return false;
		}
		finally
		{
			session.close();
		}
	}
}
