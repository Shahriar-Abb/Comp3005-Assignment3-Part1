import java.util.Scanner;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class Main {
    public static void main(String[] args){
        String url = "jdbc:postgresql://localhost:5432/Students";
        String user = "postgres";
        String password = "postgres";
        Scanner sc = new Scanner(System.in);
        int decision;
        boolean run = true;

        try{
            Class.forName("org.postgresql.Driver");
            Connection c = DriverManager.getConnection(url, user, password);

            //This while loop processes the user interface/decision-making
            while(run){
                //This section informs the user of the different functions
                System.out.println("What would you like to do?");
                System.out.println("\t1. Retrieve all student information");
                System.out.println("\t2. Add student to database");
                System.out.println("\t3. Update a students email");
                System.out.println("\t4. Delete a students information");
                System.out.println("\t5. Exit");
                System.out.print("Decision: ");
                decision = sc.nextInt(); //Sets the users decision to a variable
                sc.nextLine();
                switch(decision){ //This switch statement handles all the different choices the user can make
                    case 1: //Displays entire students table
                        getAllStudents(c);
                        break;
                    case 2: //Asks the user for the information of the user they would like to add and sends it to the function handling the addition of it to the database
                        System.out.print("First name: ");
                        String first_name = sc.nextLine();
                        System.out.print("Last name: ");
                        String last_name = sc.nextLine();
                        System.out.print("Email: ");
                        String email = sc.nextLine();
                        System.out.print("Enrollment date (Ex: 2000-09-31): ");
                        String enrollment_date = sc.nextLine();
                        addStudent(c, first_name, last_name, email, enrollment_date);
                        break;
                    case 3: //Asks the user for the id of the student they would like to update the email of as well as the new email
                        System.out.print("Student id: ");
                        int replacement_id = sc.nextInt();
                        sc.nextLine();
                        System.out.print("New email: ");
                        String new_email = sc.nextLine();
                        updateStudentEmail(c, replacement_id, new_email);
                        break;
                    case 4: //Asks the user for the id of the student they would like to remove from the database
                        System.out.print("ID of the student: ");
                        int id = sc.nextInt();
                        sc.nextLine();
                        deleteStudent(c, id);
                        break;
                    default: //Any input other than 1-4 causes the application to stop running
                        run = false;
                }
            }
        }
        catch(Exception e){// Handles errors related with the connection to the postgres server
            System.out.println(e);
        }
    }
    public static void getAllStudents(Connection c){
        try{
            //Creates a new statement where the sql code for selecting the student information
            Statement s = c.createStatement();
            s.executeQuery("SELECT * FROM students ORDER BY student_id");
            ResultSet r = s.getResultSet();
            System.out.println("\nStudents table: (student_id, first_name, last_name, email, enrollment_date)"); //Shows the intended format to the user
            while(r.next()){ //Outputs all student information
                System.out.println("\t" + r.getInt("student_id") + " " + r.getString("first_name") + " " + r.getString("last_name") + " " + r.getString("email") + " " + r.getString("enrollment_date"));
            }
            System.out.println();
        }
        catch(Exception e){
            System.out.println(e);
        }
    }
    public static void addStudent(Connection c, String first_name, String last_name, String email, String enrollment_date){
        try{
            //Formats the java variables into sql code to insert the new students information
            Statement s = c.createStatement();
            s.execute("INSERT INTO students (first_name, last_name, email, enrollment_date) VALUES " +
                    "('" + first_name + "', '" + last_name + "', '" + email + "', '" + enrollment_date + "');");
        }
        catch(Exception e){ //Tells the user of the possible reasons for the exception
            System.out.println("Either a student is already using that email or the formatting of the date inputted was incorrect");
        }
    }
    public static void updateStudentEmail(Connection c, int student_id, String new_email){
        try{
            //Formats the java variables into sql code to change a specific students email
            Statement s = c.createStatement();
            s.execute("UPDATE students SET email = '" + new_email + "' WHERE student_id = " + student_id);
        }
        catch(Exception e){ //Tells the user of the possible reasons for the exception
            System.out.println("Either a student with that id does not exist or that email has already been used");
        }
    }
    public static void deleteStudent(Connection c, int student_id){
        try{
            //Formats the java variables into sql code to delete a student
            Statement s = c.createStatement();
            s.execute("DELETE FROM students WHERE student_id = " + student_id);
        }
        catch(Exception e){ //Tells the user of the possible reasons for the exception
            System.out.println("A student with that id does not exist");
        }
    }
}

