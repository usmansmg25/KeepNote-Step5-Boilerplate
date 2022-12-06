package com.stackroute.keepnote.aspectj;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

/* Annotate this class with @Aspect and @Component */

@Aspect
@Component
public class LoggingAspect {
	/*
	 * Write loggers for each of the methods of Category controller, any particular method
	 * will have all the four aspectJ annotation
	 * (@Before, @After, @AfterReturning, @AfterThrowing).
	 */
	
	private Log log = LogFactory.getLog(getClass());
	
	 @Before("execution(* com.stackroute.keepnote.controller.CategoryController.*(..))")
	    public void logBefore(JoinPoint point) {
	        log.info(point.getSignature().getName() + " before called...");
	    }
	 
	 @After("execution(* com.stackroute.keepnote.controller.CategoryController.*(..))")
	    public void logAfter(JoinPoint point) {
	        log.info(point.getSignature().getName() + " after called...");
	    }
	 
	 @AfterReturning("execution(* com.stackroute.keepnote.controller.CategoryController.*(..))")
	    public void logAfterReturning(JoinPoint point) {
	        log.info(point.getSignature().getName() + " after returning called...");
	    }
	 
	 @AfterThrowing("execution(* com.stackroute.keepnote.controller.CategoryController.*(..))")
	    public void afterThrowing(JoinPoint point) {
	        log.info(point.getSignature().getName() + " afterThrowing called...");
	      
	    }
}
