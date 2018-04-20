package com.gslab.jndi;

import java.util.Scanner;

import javax.naming.NamingException;


/**
 * @author GS-1708
 * Class to perform operations on Ldap
 * Uses {@link LdapUtility} to perform operations*/
public class LdapOperationMenu {

	private static Scanner sc;
	/**
	 * Method to show list of operations that can be performed
	 * @return choice made by user
	 */
	public static int menu()
	{	
		try 
		{
			sc = new Scanner(System.in);
			int choice;
			System.out.println("==============================================================");
			System.out.println("List of Operations");
			System.out.println("0.EXIT");
			System.out.println("1.Display all the users");
			System.out.println("2.Add new User");
			System.out.println("3.ADD or UPDATE new Attributte to user");
			System.out.println("4.Delete user");
			System.out.println("5.Delete attribute of user");
			System.out.println("6.Search");
			System.out.println("Please enter your choice");
			choice=sc.nextInt();
			return choice;
		} 
		catch (Exception e)
		{
			System.out.println("Please enter valid input");
		}
		return 255;
	}
	/**
	 * @param args,command line agruments
	 */
	public static void main(String[] args) {
		int choice;
		LdapUtility operations= new LdapUtility();
		while((choice=menu())!=0)
		{
			switch(choice)
			{
				case 1:
					operations.listUsers();
					break;
				case 2:
					operations.addUser();
					break;
				case 3:
					operations.addOrUpdateAttribute();
					break;
				case 4:
					operations.deleteEntry();
					break;
				case 5:
					operations.deleteAttribute();
					break;
				case 6:
					operations.search();
					break;
				case 255:
					System.out.println("You have entered value other then INTEGER");
					break;
				default:
					System.out.println("Invalid Choice try Again!!!");
			}
		}
		/*
		 * Block to close the connection
		 * Print the good bye message*/
		try 
		{
			//connection is closed here
			TdsConnection.getConnection().close();
			System.out.println("Connection closed Successfully!!!");
		} 
		catch (NamingException e)
		{
			System.err.println("Connection closing Failed!!!");
			e.printStackTrace();
		}
		finally
		{
			//scanner resource is closed here
			sc.close();
			System.out.println("Thank you...Have a nice day!!!!");
		}
	}
}
