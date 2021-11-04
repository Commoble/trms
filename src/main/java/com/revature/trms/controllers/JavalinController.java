package com.revature.trms.controllers;

import io.javalin.Javalin;
import io.javalin.core.JavalinConfig;

public interface JavalinController
{
	public void configureJavalin(JavalinConfig config);
	public void registerEndpoints(Javalin javalin);
}
