package main;



import java.io.IOException;
import java.sql.*;
import java.util.*;

import login.registe;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

public class ticketbooking{


    static Scanner sc=new Scanner(System.in);
    static Connection con;

    static{
        try{
            Class.forName("com.mysql.cj.jdbc.Driver");
            con=DriverManager.getConnection("jdbc:mysql://localhost:3306/book","root","prasanabalaji@90");
        }
       catch(Exception e){
            System.out.println("Exception occured "+e.getMessage());
        }
        
    }

    public static boolean login() throws SQLException{
        System.out.println("***************************LOGIN TO MOVIE TICKET BOOKING***************************");
        Scanner sc = new Scanner(System.in);
        System.out.println("Enter Username");
        String username =sc.nextLine();
        System.out.println("Enter Password");
        String password =sc.nextLine();
        if(username.equals("admin") && password.equals("password")){
            return true;
        }
        else{
            return false;
        }
    }


    @SuppressWarnings("deprecation")
	public static void BookTicket() throws SQLException{
        try{
            int num=ShowMovieList();
            int Id;
            if(num==0){
                System.out.println("Sorry There is no show going on!");
                return;
            }
            while(true){
                System.out.println("Enter movie Id(0 to Exit):");
                Id=sc.nextInt();
                if(Id==0){
                    return;
                }
                PreparedStatement pst=con.prepareStatement("select * from movies where columnid=?");
                pst.setInt(1,Id);
                ResultSet rs=pst.executeQuery();
                if(rs.next()){
                    System.out.println("Enter User Name: ");
                    sc.nextLine();
                    String Uname=sc.nextLine();
                    System.out.println("Enter Phone no.: ");
                    String Pno=sc.nextLine();
                    System.out.println("Enter seats: ");
                    int seat=sc.nextInt();
                    int RemainingSeats;
                    java.util.Date d;
                    java.util.Date t;
                    double price;
                    String Mname,format;
                    Mname=rs.getString(2);
                    format=rs.getString(3);
                    d=rs.getDate(4);
                    java.sql.Date date=new java.sql.Date(d.getYear(),d.getMonth(),d.getDate());
                    t=rs.getTime(5);
                    java.sql.Time time=new Time(t.getHours(),t.getMinutes(),0);
                    price=rs.getDouble(6);
                    RemainingSeats=rs.getInt(7)-seat;
                    if(RemainingSeats<=0){
                        System.out.println("Insufficient seats.!!");
                        System.out.println("Booking is cancelled.!!");
                        return;
                    }
                    PreparedStatement pst1=con.prepareStatement("insert into customer (name,phoneNo,columnid,Mname,format,date,time,price,seat) values(?,?,?,?,?,?,?,?,?)");
                    pst1.setString(1,Uname);
                    pst1.setString(2,Pno);
                    pst1.setInt(3,Id);
                    pst1.setString(4,Mname);
                    pst1.setString(5,format);
                    pst1.setDate(6,date);
                    pst1.setTime(7,time);
                    pst1.setDouble(8,price*seat);
                    pst1.setInt(9,seat);
                    if(pst1.executeUpdate()>0){
                        System.out.println("Ticket Booked");
                    }
                    else{
                        System.out.println("Something went Wrong");
                        return;
                    }
                    pst1=con.prepareStatement("update movies set seat=? where columnId=?");
                    pst1.setInt(1,RemainingSeats);
                    pst1.setInt(2,Id);
                    if(pst1.executeUpdate()<=0){
                        System.out.println("Something went Wrong");
                        return;
                    }
                    System.out.print("Press any key");
                    sc.next();
                    pst1=con.prepareStatement("select * from customer where name=? and phoneNo=?");
                    pst1.setString(1,Uname);
                    pst1.setString(2,Pno);
                    ResultSet rs1=pst1.executeQuery();
                    if(rs1.next()){
                    
                    showMyTicket(rs1.getInt(10));
                    }
                    else{
                        System.out.println("Something went Wrong");
                    }
                    return;
                }
                else{
                    System.out.println("Please Enter a valid Id");
                }

            }
        }
       catch(Exception e){
            System.out.println("Exception occured "+e.getMessage());
        }
    }


    public static void showMyTicket(int num) throws SQLException{
        try{
            PreparedStatement pst=con.prepareStatement("select * from customer where cid=?");
            pst.setInt(1,num);
            ResultSet rs=pst.executeQuery();
            if(rs.next()){
                System.out.println("MOVIE TICKET");
                System.out.println("Name-"+rs.getString(1));
                System.out.println("Phone No.-"+rs.getString(2));
                System.out.println("Movie Name-"+rs.getString(4));
                System.out.println("Type-"+rs.getString(5));
                System.out.println("date-"+rs.getDate(6));
                System.out.println("Time-"+rs.getString(7));
                System.out.println("Seat(s)-"+rs.getInt(9));
                System.out.println("Price-"+rs.getDouble(8));
                System.out.println("Movie Id-"+rs.getInt(10));

            }
            else{
                System.out.println("No Booking available!");
            }
            
        }
        catch(Exception e){
            System.out.println("Exception occured "+e.getMessage());
        }
    }


    public static void CancleTicket() throws SQLException{
        try{
            while(true){
                System.out.print("Enter Movie Id(0 for exit): ");
                int num=sc.nextInt();
                if(num==0){
                    return;
                }
                int seat,Mid;
                PreparedStatement pst=con.prepareStatement("select * from customer where Cid=?");
                pst.setInt(1,num);
                ResultSet rs=pst.executeQuery();
                if(rs.next()){
                    seat=rs.getInt(9);
                    Mid=rs.getInt(3);
                }
                else{
                    
                    System.out.println("The Movie Id doesn\'t Exist!");
                    System.out.println("Please enter correct Movie Id");
                    System.out.println("Please enter any key to continue");
                    sc.next();
                    
                    continue;
                }
                pst=con.prepareStatement("select * from movies where columnid=?");
                pst.setInt(1,Mid);
                rs=pst.executeQuery();
                if(rs.next()){
                    showMyTicket(num);
                    System.out.println("Press \'c\' to cancle ticket");
                    char t=sc.next().charAt(0);
                    if(t!='c'&& t!='C'){
                        System.out.println("Ticket is not cancelled");
                        System.out.println("Please enter any key to continue");
                        sc.next();
                        return;
                    }
                    PreparedStatement pst1=con.prepareStatement("update movies set seat=? where columnid=?");
                    pst1.setInt(1,seat+rs.getInt(7));
                    pst1.setInt(2,Mid);
                    if(pst1.executeUpdate()<=0){
                        System.out.println("Exception occured");
                    }
                }
                pst=con.prepareStatement("delete from customer where Cid=?");
                pst.setInt(1,num);
                if(pst.executeUpdate()>0){
                    System.out.println("The ticket is cancelled");
                    return;
                }
                else{
                    System.out.println("The Movie Id doesn\'t Exist");
                    System.out.println("Plese enter correct Movie Id...!!\n");
                    System.out.println("Please enter any key to continue");
                    sc.next();
                    
                }
            }
        }
        catch(Exception e){
            System.out.println("Exception occured "+e.getMessage());
        }
    }


    public static void CheckSeat() throws SQLException{
        try{
            PreparedStatement pst=con.prepareStatement("Select * from movies");
            ResultSet rs=pst.executeQuery();
            System.out.println("CHECK SEATS");
            //System.out.println("\t\t\t\t| MOVIE NAME\t| SEATS\t|");
            while(rs.next()){
                System.out.println("MOVIE NAME:  "+rs.getString(2)+"SEATS  "+rs.getInt(7)+"\t|");
            } 
        }
        catch(Exception e){
            System.out.println("Exception occured "+e.getMessage());
        }
    }


    @SuppressWarnings("deprecation")
	public static void AddNewMovie() throws SQLException{
        try{
            System.out.println("Enter Movie name: ");
            sc.nextLine();
            String name=sc.nextLine();
            System.out.println("Format: ");
            String format=sc.nextLine();
            System.out.println("Date(YYYY/MM/DD): ");
            String date=sc.nextLine();
            System.out.println("Time(HH:MM:SS): ");
            String time=sc.nextLine();
            System.out.println("Price: ");
            Double price=sc.nextDouble();
            System.out.println("Seats: ");
            int seat=sc.nextInt();
            DateFormat df=new SimpleDateFormat("HH:MM:SS");
            java.util.Date t=df.parse(time);
            java.util.Date d=new java.util.Date(date);
            java.sql.Date d1=new java.sql.Date(d.getYear(),d.getMonth(),d.getDate());
            java.sql.Time t1=new java.sql.Time(t.getHours(),t.getMinutes(),t.getSeconds());
            PreparedStatement pst=con.prepareStatement("insert into movies (name,format,showdate,showtime,price,seat) values (?,?,?,?,?,?)");
            pst.setString(1,name);
            pst.setString(2,format);
            pst.setDate(3,d1);
            pst.setTime(4,t1);
            pst.setDouble(5,price);
            pst.setInt(6,seat);
            if(pst.executeUpdate()>0){
                System.out.println("Movie Added Successfully");
            }
            else{
                System.out.println("Movie NOT added!");
            }
        }
        catch(Exception e){
            System.out.println("Exception occured "+e.getMessage());
        }
    }


    public static void DeleteMovie(){
        try{
            System.out.println("Enter Movie ID(0 to exit): ");
            int num=sc.nextInt();
            if(num==0){
                return;
            }
            PreparedStatement pst=con.prepareStatement("select * from movies where columnid=?");
            pst.setInt(1,num);
            ResultSet rs=pst.executeQuery();
            if(rs.next()){
                System.out.println("  \t|ID\t|\tNAME\t|\tFORMAT\t| SHOW DATE\t| SHOW TIME\t| PRICE\t| AVL.SEAT(s)\t|");
                System.out.println("  \t| "+rs.getInt(1)+"\t| "+rs.getString(2)+"\t| "+rs.getString(3)+"\t| "+rs.getDate(4)+"\t| "+rs.getTime(5)+"\t| "+rs.getDouble(6)+"\t|\t"+rs.getInt(7)+"\t|");
            }
            else{
                System.out.println("Invalid Movie ID");
                System.out.println("Please enter any key to continue");
                sc.next();
                return;
            }
            System.out.println("Press \'d\' to delete");
            char c=sc.next().charAt(0);
            if(c=='d'||c=='D'){
                pst=con.prepareStatement("delete from movies where columnid=?");
                pst.setInt(1,num);
                if(pst.executeUpdate()>0){
                    System.out.println("Movie deleted");
                }
                
                else{
                    System.out.println("Movie NOT deleted");
                    System.out.println("Please enter any key to continue");
                    sc.next();
                }
            }
            else{
                System.out.println("Movie NOT deleted");
                System.out.println("Please enter any key to continue");
                sc.next(); 
                return;
            }
        }
        catch(Exception e){
            System.out.println("Exception occured "+e.getMessage());
        }
    }


    public static int ShowMovieList() throws SQLException{
        int num=0;
        try{
            PreparedStatement pst=con.prepareStatement("select * from movies");
            ResultSet rs=pst.executeQuery();
            System.out.println("DETAILS OF ALL MOVIE");
            //System.out.println("\t| SNO.\t|ID\t|\tNAME\t|\tFORMAT\t| SHOW DATE\t| SHOW TIME\t| PRICE\t| AVL.SEAT(s)\t|");
            //int i=1;
            while(rs.next()){
                //System.out.println("  \t| "+i+"\t| "+rs.getInt(1)+"\t| "+rs.getString(2)+"\t| "+rs.getString(3)+"\t| "+rs.getDate(4)+"\t| "+rs.getTime(5)+"\t| "+rs.getDouble(6)+"\t|\t"+rs.getInt(7)+"\t|");
            	System.out.println("MOVIE NAME: "+rs.getString(2)+"- ID: "+rs.getInt(1)+" ,FORMAT: "+rs.getString(3)+" ,SHOW DATE: "+rs.getDate(4)+" ,SHOW TIME: "+rs.getTime(5)+"  , PRICE: "+rs.getDouble(6)+" , SEATS AVAILABLE: "+rs.getInt(7));
                num++;
                //i++;
            }
        }
        catch(Exception e){
            System.out.println("Exception occured "+e.getMessage());
        }
        return num;
    }


    public static int SeeTotalBookings() throws SQLException{
        int num=0;
        try{
            num=ShowMovieList();
            PreparedStatement pst=con.prepareStatement("select * from customer");
            ResultSet rs=pst.executeQuery();
            num=0;
            System.out.println("\n");
            System.out.println("DETAILS OF ALL BOOKINGS");
            //System.out.println("| SNO.\t| ID\t|\tNAME\t|\t Phone No.\t| Movie ID\t| SEAT(s)\t|");
            int i=1;
            while(rs.next()){
                System.out.println("ID: "+rs.getInt(10)+"  NAME:  "+rs.getString(1)+"  PHONE NO: "+rs.getString(2)+"  MOVIE ID:  "+rs.getInt(3)+"  SEAT(S):  "+rs.getInt(9));
                num++;
            }
        }
        catch(Exception e){
            System.out.println("Exception occured "+e.getMessage());
        }
        return num;
    }
    


    

    public static void main(String[] arg) throws SQLException{
  

    	
        
    
            int ch;
	     System.out.println("********************WELCOME TO MOVIE TICKET BOOKING APPLICATION********************");
                registe a = new registe();
                a.reg();
           
            do
            {
                
                
                System.out.println("\t\t\t\tMAINMENU");

                System.out.println("\t\t\t\t1.USER INTERFACE");
                System.out.println("\t\t\t\t2.ADMIN");
                System.out.println("\t\t\t\t0.Exit");
                System.out.println("Enter your Choice:");
                ch=sc.nextInt();
                switch(ch)
                {
                    case 1:
                        int ch1;
                        do
                        {
                           
                            System.out.println("********************WELCOME TO MOVIE TICKET BOOKING APPLICATION********************");
                            System.out.println("\t\t\t\t1. Book Ticket");
                            System.out.println("\t\t\t\t2. Show my Ticket");
                            System.out.println("\t\t\t\t3. Cancle Ticket");
                            System.out.println("\t\t\t\t4. Check Seat");
                            System.out.println("\t\t\t\t5. Show Movie List");
                            System.out.println("\t\t\t\t6. Back");
                            System.out.println("\t\t\t\t0. Exit");
                            System.out.println("Enter your Choice:");
                            ch1=sc.nextInt();
                            switch(ch1)
                            {
                                case 1:
                                   
                                    BookTicket();
                                    break;
                                case 2:
                                   
                                    char c='x';
                                    while(c=='x')
                                    {
                                        System.out.print("Do you have your movie ID(enter \'Y\' for yes or \'N' for No: ");
                                        c=sc.next().charAt(0);
                                        if(c=='Y'||c=='y')
                                        {
                                            System.out.print("Enter movie Id: ");
                                            int num=sc.nextInt();
                                            showMyTicket(num);
                                        }
                                        else if(c=='N'||c=='n')
                                        {
                                            System.out.println("Enter your name: ");
                                            sc.nextLine();
                                            String str=sc.nextLine();
                                            System.out.println("Enter Mobile no.: ");
                                            //System.out.print("\t\t\t\t");
                                            String ph=sc.nextLine();
                                            PreparedStatement pst=con.prepareStatement("select * from customer where name=? and phoneNo=?");
                                            pst.setString(1,str);
                                            pst.setString(2,ph);
                                            ResultSet rs=pst.executeQuery();
                                            if(rs.next())
                                            {
                                                int num=rs.getInt(10);
                                                showMyTicket(num);
                                            }
                                            else
                                            {
                                                System.out.println("No Booking available");
                                            }
                                        }
                                        else
                                        {
                                            c='x';
                                            
                                        }
                                    }
                                    break;
                                case 3:
                                    
                                    CancleTicket();
                                    break;
                                case 4:
                                   
                                    CheckSeat();
                                    break;
                                case 5:
                                    
                                    int num=ShowMovieList();
                                    if(num==0)
                                    {
                                        System.out.println("List is EMPTY");
                                    }
                                    break;
                                case 6:
                                    break;
                                case 0:
                                   break;
                                default:
                                   
                                    System.out.println("Enter a VALID CHOICE");
                                    System.out.print("Enter any key");
                                    sc.next();
                                    ch1=9;
                            }
                            if(ch1!=0 && ch1!=9 && ch1!=6){
                                System.out.println("Press any key to back to main menu");
                                System.out.println("0. Exit");
                                System.out.println("Enter your Choice:");
                                char c1=sc.next().charAt(0);
                                if(c1!='0')
                                    ch1=9;
                                else
                                    ch1=0;
                            }
                            if(ch1==0){
                                
                                System.out.println("********************THANK YOU FOR USING MOVIE-TICKET-BOOKING-APPLICATION********************");
                                
                                return;
                            }
                            if(ch1==6){
                                ch=9;
                                break;
                            }
                            
                        }while(ch1==9);
                        break;
                    case 2:
                        
                        if(login()){
                            int ch2;
                            do{
                               
                                System.out.println("************************************ADMIN PANEL************************************");
                                System.out.println("\t\t\t\t1. Add New Movie");
                                System.out.println("\t\t\t\t2. Delete Movie");
                                System.out.println("\t\t\t\t3. View Movie List");
                                System.out.println("\t\t\t\t4. See Total Bookings");
                                System.out.println("\t\t\t\t5. Back");
                                System.out.println("\t\t\t\t0. Exit");
                                System.out.println("Enter your Choice:");
                                ch2=sc.nextInt();
                                switch(ch2){
                                    case 1:
                                        
                                        AddNewMovie();
                                        break;
                                    case 2:
                                       
                                        DeleteMovie();
                                        break;
                                    case 3:
                                        
                                        int num=ShowMovieList();
                                        if(num==0)
                                        {
                                            System.out.println("List is EMPTY");
                                        }
                                        break;
                                    case 4:
                                        
                                        num=SeeTotalBookings();
                                        if(num==0){
                                            System.out.println("No booking Available");
                                        }
                                        break;
                                    case 5:
                                        break;
                                    case 0:
                                        break;
                                    default:
                                       
                                        System.out.println("Enter a VALID CHOICE");
                                        System.out.print("Enter any key");
                                        sc.next();
                                        ch2=9;
                                }
                                if(ch2!=0 && ch2!=9 && ch2!=5){
                                    System.out.println("Press any key Back to main menu");
                                    System.out.println("0. Exit");
                                    System.out.println("Enter your Choice: ");
                                    char c2=sc.next().charAt(0);
                                    if(c2!='0')
                                        ch2=9;
                                    else
                                        ch2=0;
                                }
                                if(ch2==0){
                                    
                                    System.out.println("********************THANK YOU FOR USING MOVIE TICKET BOOKING APPLICATION********************");                                
                                    return;
                                }
                                if(ch2==5){
                                    ch=9;
                                    break;
                                }
                            }while(ch2==9);
                        }
                        else{
                           
                            System.out.println("INCORRECT USERNAME PASSWORD");
                            System.out.println("Press any key to go back");
                            sc.next();
                            ch=9;
                        }
                        break;
                    case 0:
                       
                        System.out.println("********************THANK YOU FOR USING MOVIE TICKET BOOKING APPLICATION********************");
                        return;
                    default:
                        
                        System.out.println("Enter a VALID CHOICE");
                        System.out.print("Enter any key");
                        sc.next();
                        ch=9;
                }
            }while(ch==9);
        }
      
    
}
    


