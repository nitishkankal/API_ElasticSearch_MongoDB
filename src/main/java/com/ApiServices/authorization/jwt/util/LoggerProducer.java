package com.ApiServices.authorization.jwt.util;

import javax.enterprise.inject.Produces;
import javax.enterprise.inject.spi.InjectionPoint;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**

* @author nitish.k
 *         --
 */
public class LoggerProducer {

    // ======================================
    // =              Producers             =
    // ======================================

	@Produces
	  public Logger producer(InjectionPoint ip){
	    return LoggerFactory.getLogger(
	      ip.getMember().getDeclaringClass().getName());
	  }
}
