package com.gslab.jndi;

import java.util.Properties;

import javax.naming.Context;
import javax.naming.NamingException;
import javax.naming.directory.DirContext;
import javax.naming.directory.InitialDirContext;

/**
 * @author GS-1708
 *Singleton Class for creating connection to TDS server
 */
public class TdsConnection {
	Properties intialProperties;
	static  TdsConnection instance;
	static DirContext context;
	private TdsConnection() throws NamingException {
		this.intialProperties=new Properties();
		this.initialConnection();
		createConnection();
	}
	
	/**
	 * Method to create connection with Server
	 * @throws NamingException, If connection failed to established
	 */
	private void createConnection() throws NamingException
	{
		//Connection is established here
		context=new InitialDirContext(this.intialProperties);
		System.out.println("Connection Established Successfully!!!!");
	}
	
	/**
	 * Method to initialize the connection 
	 * Map the all required attributes to establish connection
	 */
	private void initialConnection()
	{
		this.intialProperties.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.ldap.LdapCtxFactory");
		this.intialProperties.put(Context.PROVIDER_URL, "ldap://192.168.31.149:2389");
		this.intialProperties.put(Context.SECURITY_PRINCIPAL, "cn=root");
		this.intialProperties.put(Context.SECURITY_CREDENTIALS, "Passw0rd");
	}	
	/**
	 * Create the instance of the class on first call
	 * @return  DirContext object
	 * @throws NamingException,delegated exceptions
	 */
	public static DirContext getConnection() throws NamingException {
			if(instance==null)
				instance=new TdsConnection();
		return context;
	}
}
