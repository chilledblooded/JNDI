package com.gslab.jndi;
import java.util.Scanner;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.Attribute;
import javax.naming.directory.Attributes;
import javax.naming.directory.BasicAttribute;
import javax.naming.directory.BasicAttributes;
import javax.naming.directory.DirContext;
import javax.naming.directory.ModificationItem;
import javax.naming.directory.SearchControls;
import javax.naming.directory.SearchResult;

/**
 * @author GS-1708
 *Performs the LDAP operations
 *Contains the utility methods related to LDAP  
 */
public class LdapUtility {
	private Scanner scanner=new Scanner(System.in);
	/**
	 * Method for printing all the users available in LDAP schema
	 */
	public void listUsers()
	{
		try 
		{
				//Creating connection to TDS
				DirContext context = TdsConnection.getConnection();
				printUsersDetails(context);
		} 
		catch (NamingException e)
		{
			System.err.println("Connection failed!!!....");
			System.err.println(e.getMessage());
		}
	}
	private void printUsersDetails(DirContext context)
	{
		//Setting required Attributes for displaying
		String searchFilter = "(objectClass=inetOrgPerson)";
		String[] requiredAttributes = { "employeeNumber", "cn","sn","mobile","localityName","mail" };

		SearchControls controls = new SearchControls();
		controls.setSearchScope(SearchControls.SUBTREE_SCOPE); 
		controls.setReturningAttributes(requiredAttributes);

		try {
				//Data is extracted here of all users 
				NamingEnumeration<SearchResult>	users = context.search("ou=users,o=GSLab,DC=COM", searchFilter, controls);
				System.out.println("Existing users in ou=users , o=GSLab, DC=COM");
				SearchResult searchResult = null;
				String commonName = null;
				String empNumber = null;
				String mobileNumber = null;
				String sn=null;
				String locality=null;
				String mail=null;
				//Printing of details of all users
				while (users.hasMore()) 
				{
					searchResult = (SearchResult) users.next();
					Attributes attr = searchResult.getAttributes();
					commonName = attr.get("cn").get(0).toString();
					empNumber = attr.get("employeeNumber").get(0).toString();
					mobileNumber = attr.get("mobile").get(0).toString();
					sn = attr.get("sn").get(0).toString();
					locality = attr.get("localityName").get(0).toString();
					mail = attr.get("mail").get(0).toString();
					System.out.println("Name = " + commonName+" "+sn);
					System.out.println("Employee Number = " + empNumber);
					System.out.println("Mobile Number = " + mobileNumber);
					System.out.println("Locality = " + locality);
					System.out.println("Email = " + mail);
					System.out.println("--------------------------------");
			
				}
			} 
			//printing possible error messages
			catch (NamingException e)
			{
				System.err.println("Possible Reasons....");
				System.err.println("Incorrect DN or Dn may not exist");
				System.err.println("Attribute may not be found in some entry..!!!");
				System.err.println(e.getMessage());
			}
		}
	/**
	 * method to add the user to the LDAP
	 */
	public void addUser()
	{
		try
		{	scanner = new Scanner(System.in);
			String commonName = null;
			String empNumber = null;
			String mobileNumber = null;
			String sn=null;
			String locality=null;
			String mail=null;
			String practice=null;
			String uid=null;
			System.out.println("Enter the Practice (IBM/CIS)::");
			practice=scanner.nextLine();
			practice=practice.toUpperCase();
			//Checking if user have entered valid practice 
			if(practice.equals("IBM") || practice.equals("CIS"))
			{
				System.out.println("Enter the Comman Name ::");
				commonName=scanner.nextLine();
				System.out.println("Enter the Surname ::");
				sn=scanner.nextLine();
				System.out.println("Enter the EmployeeNumber ::");
				empNumber=scanner.nextLine();
				System.out.println("Enter the Mobile Number ::");
				mobileNumber=scanner.nextLine();
				System.out.println("Enter the Locality Name ::");
				locality=scanner.nextLine();
				System.out.println("Enter the Email ::");
				mail=scanner.nextLine();
				System.out.println("Enter the UID ::");
				uid=scanner.nextLine();
				//creating attributes here
				Attributes attributes=new BasicAttributes();
				attributes.put("objectClass", "inetOrgPerson");
				attributes.put("cn", commonName);
				attributes.put("sn", sn);
				attributes.put("localityName", locality);
				attributes.put("mobile", mobileNumber);
				attributes.put("mail", mail);
				attributes.put("employeeNumber", empNumber);
				attributes.put("uid", uid);
				try {
						//Connection is established here with TDS
					DirContext context = TdsConnection.getConnection();
						//User is added here
						context.createSubcontext("cn="+commonName+",ou="+practice+",ou=users,o=GSLab,DC=COM", attributes);
						System.out.println("User added successfully!!!!!");
					} 
				catch (NamingException e) 
				{
					System.err.println(e.getMessage());
				}
			}
			//If user have entered wrong Practice name
			else
			{
				System.err.println("Invalid Input..Returning to MAIN MENU");
				return;
			}
		} catch (Exception e) {
			System.err.println(e.getMessage());
		}
		
	}
	/**
	 * Method for adding or updating the attribute of the LDAP
	 */
	public void addOrUpdateAttribute()
	{
		try
		{
			//Connection is established here with TDS
			DirContext context = TdsConnection.getConnection();
			String cn=null;
			String practice=null;
			System.out.println("Enter the Comman Name");
			cn=scanner.nextLine();
			System.out.println("Enter the Practice(IBM/CIS)");
			practice=scanner.nextLine().toUpperCase();
			//Checking if user have entered valid practice name 
			if(practice.equals("IBM") || practice.equals("CIS"))
			{
				//Attributes are retrieved here
				Attributes attributes=context.getAttributes("cn="+cn+",ou="+practice+",ou=users,o=GSLab,DC=COM");
				//checking if user exist or not
				if(attributes==null)
				{
					System.out.println("Invalid DN..returning to main menu...");
					return;
				}
				String attributeName=null;
				String attributeValue=null;
				System.out.println("Enter the attribute name");
				attributeName=scanner.nextLine();
				System.out.println("Enter the attribute value");
				attributeValue=scanner.nextLine();
				Attribute attribute=new BasicAttribute(attributeName);
				attribute.add(attributeValue);
				ModificationItem[] items=new ModificationItem[1];
				//adding attribute for modification
				items[0]= new ModificationItem(DirContext.ADD_ATTRIBUTE, attribute);
				attributes.put(attribute);
				try {
						//Modification is taking place here
						context.modifyAttributes("cn="+cn+",ou="+practice+",ou=users,o=GSLab,DC=COM", items);
						System.out.println("Attribute Updated successfully!!!");
					} 
					catch (Exception e) 
					{
						System.err.println(e.getMessage());
					}
			}
			else
			{
				System.err.println("Invalid Input..Returning to MAIN MENU");
				return;
			}
			
		} 
		catch (Exception e)
		{
			System.err.println(e.getMessage());
		}
		
	}
	/**
	 * Method to delete the entry from the LDAP
	 */
	public void deleteEntry()
	{
		try
		{
			//Connection is established here with TDS
			DirContext context = TdsConnection.getConnection();
			String cn=null;
			String practice=null;
			System.out.println("Enter the Comman Name");
			cn=scanner.nextLine();
			System.out.println("Enter the Practice(IBM/CIS)");
			practice=scanner.nextLine().toUpperCase();
			//checking if valid practice name is entered
			if(practice.equals("IBM") || practice.equals("CIS"))
			{
				//Retrieving all the attributes
				Attributes attributes=context.getAttributes("cn="+cn+",ou="+practice+",ou=users,o=GSLab,DC=COM");
				//checking if user exist
				if(attributes==null)
				{
					System.out.println("Invalid DN..returning to main menu...");
					return;
				}
				//User is deleted here
				context.destroySubcontext("cn="+cn+",ou="+practice+",ou=users,o=GSLab,DC=COM");
				System.out.println("User deleted successfully!!!!");
			}
			else
			{
				//if user have entred invalid practice name
				System.err.println("Invalid Input..Returning to MAIN MENU");
				return;
			}
			
		} 
		catch (Exception e)
		{
			System.err.println(e.getMessage());
		}
	}
	/**
	 * Method to delete the attribute from the LDAP
	 */
	public void deleteAttribute()
	{
		try
		{
			//Connection is established here with TDS
			DirContext context = TdsConnection.getConnection();
			String cn=null;
			String practice=null;
			System.out.println("Enter the Comman Name");
			cn=scanner.nextLine();
			System.out.println("Enter the Practice(IBM/CIS)");
			practice=scanner.nextLine().toUpperCase();
			//checking if valid practice name is entered
			if(practice.equals("IBM") || practice.equals("CIS"))
			{
				//Retrieving all the attributes
				Attributes attributes=context.getAttributes("cn="+cn+",ou="+practice+",ou=users,o=GSLab,DC=COM");
				if(attributes==null)
				{
					//checking if user exist
					System.err.println("Invalid DN..returning to main menu...");
					return;
				}
				String attributeName=null;
				String attributeValue=null;
				System.out.println("Enter the attribute name");
				attributeName=scanner.nextLine();
				System.out.println("Enter the attribute value");
				attributeValue=scanner.nextLine();
				Attribute attribute=new BasicAttribute(attributeName);
				attribute.add(attributeValue);
				ModificationItem[] items=new ModificationItem[1];
				//attributes are added for deletion
				items[0]= new ModificationItem(DirContext.REMOVE_ATTRIBUTE, attribute);
				attributes.put(attribute);
				try {
					//User attribute  is deleted here
					context.modifyAttributes("cn="+cn+",ou="+practice+",ou=users,o=GSLab,DC=COM", items);
					System.out.println("Attribute Deleted successfully!!!");
				} 
				catch (Exception e) 
				{
					System.err.println(e.getMessage());
				}
			}
			else
			{
				//if user have entered invalid practice name
				System.err.println("Invalid Input..Returning to MAIN MENU");
				return;
			}
			
		} 
		catch (Exception e)
		{
			System.err.println(e.getMessage());
		}
	}
	
	/**
	 * Method to perform the search operations on the LDAP
	 * Uses the filter to perform the search
	 */
	public void search()
	{
		try
		{
			System.out.println("Please Enter the filter(ObjectClass=*)::");
			
			//Filter is taken from the user
			String searchFilter =scanner.nextLine();
			DirContext context = TdsConnection.getConnection();
			
			//Required attributes is specified here
			String[] requiredAttributes = { "employeeNumber", "cn","sn","mobile","localityName","mail" };
			SearchControls controls = new SearchControls();
			
			//Scope of the search is specified here
			controls.setSearchScope(SearchControls.SUBTREE_SCOPE); 
			controls.setReturningAttributes(requiredAttributes);

			try {
					//Data is extracted here of all users 
					NamingEnumeration<SearchResult>	users = context.search("ou=users,o=GSLab,DC=COM", searchFilter, controls);
					System.out.println("SEARCH RESULT");
					SearchResult searchResult = null;
					String commonName = null;
					String empNumber = null;
					String mobileNumber = null;
					String sn=null;
					String locality=null;
					String mail=null;
					//Printing of details of all users
					while (users.hasMore()) 
					{
						searchResult = (SearchResult) users.next();
						Attributes attr = searchResult.getAttributes();
						commonName = attr.get("cn").get(0).toString();
						empNumber = attr.get("employeeNumber").get(0).toString();
						mobileNumber = attr.get("mobile").get(0).toString();
						sn = attr.get("sn").get(0).toString();
						locality = attr.get("localityName").get(0).toString();
						mail = attr.get("mail").get(0).toString();
						System.out.println("Name = " + commonName+" "+sn);
						System.out.println("Employee Number = " + empNumber);
						System.out.println("Mobile Number = " + mobileNumber);
						System.out.println("Locality = " + locality);
						System.out.println("Email = " + mail);
						System.out.println("--------------------------------");
				
					}
				} 
				//printing possible error messages
				catch (NamingException e)
				{
					System.err.println("Possible Reasons....");
					System.err.println("Incorrect DN or Dn may not exist");
					System.err.println("Attribute may not be found in some entry..!!!");
					System.err.println(e.getMessage());
				}			
		} 
		catch (Exception e)
		{
			System.err.println(e.getMessage());
		}
	}
}
