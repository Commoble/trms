package com.revature.trms.util;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

public class HibernateUtils
{
	private static final SessionFactory SESSION_FACTORY = new Configuration()
		.configure()
		.buildSessionFactory();

	public static void bootstrap()
	{
		// NOOP, just static inits the session factory // TODO reevaluate
	}

	public static Session getSession()
	{
		return SESSION_FACTORY.openSession();
	}
}
