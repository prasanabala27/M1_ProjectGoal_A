package login;




import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;
import java.util.Scanner;

public class registe {
	 static Register register = new Register();

	public void reg()
	{

	         Scanner scanner = new Scanner(System.in);
	            System.out.print(" Enter firstName => ");
	            String firstName = scanner.nextLine();
	            register.setFirstName(firstName);

	            System.out.print(" Enter lastName => ");
	            String lastName = scanner.nextLine();
	            register.setLastName(lastName);

	            System.out.print(" Enter userName => ");
	            String userName = scanner.nextLine();
	            register.setUserName(userName);

	            System.out.print(" Enter password => ");
	            String password = scanner.nextLine();
	            register.setPassword(password);

	            System.out.print(" Enter emailId => ");
	            String emailId = scanner.nextLine();
	            register.setEmailId(emailId);

	            System.out.print(" Enter phoneNo => ");
	            long phoneNo = scanner.nextLong();
	            register.setPhoneNo(phoneNo);
	            try{
	                File file = new File("userAge.txt"); 
	                PrintWriter writer = new PrintWriter(file);
	                writer.write(firstName + " " + lastName+" "+userName+" "+emailId+" "+password+" "+phoneNo);
	                writer.close();
					System.out.println("sucessfully registered");
				
					
				     System.out.println("Email:"+emailId);
				   String  e=scanner.nextLine();
				   
				     System.out.println("Password :");
				    String p =scanner.nextLine();
								System.out.println("sucessfully logged In");

	            } catch(IOException e){
	                e.printStackTrace();

	            }
	        }
	    }
	

	class Register {
	    private String firstName;
	    private String lastName;
	    private String userName;
	    private String password;
	    private String emailId;
	    private long phoneNo;
	    public String getFirstName() {
	        return firstName;
	    }
	    public void setFirstName(String firstName) {
	        this.firstName = firstName;
	    }
	    public String getLastName() {
	        return lastName;
	    }
	    public void setLastName(String lastName) {
	        this.lastName = lastName;
	    }
	    public String getUserName() {
	        return userName;
	    }
	    public void setUserName(String userName) {
	        this.userName = userName;
	    }
	    public String getPassword() {
	        return password;
	    }
	    public void setPassword(String password) {
	        this.password = password;
	    }
	    public String getEmailId() {
	        return emailId;
	    }
	    public void setEmailId(String emailId) {
	        this.emailId = emailId;
	    }
	    public long getPhoneNo() {
	        return phoneNo;
	    }
	    public void setPhoneNo(long phoneNo) {
	        this.phoneNo = phoneNo;
	    }
	
	}



