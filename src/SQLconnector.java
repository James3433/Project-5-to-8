/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
import java.sql.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;


/**
 *
 * @author Admin
 */
 public class SQLconnector{
   private static final String DB_URL = "jdbc:mysql://localhost/workplace";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "";
    
    public Connection getConnection(){
        Connection connection;
        try{
            connection = DriverManager.getConnection(DB_URL,DB_USER,DB_PASSWORD);
            return connection;
        }catch(Exception e){
            e.printStackTrace();
            return null;
        }
    }
    
       public void executeQuery(String query){
        Connection con = getConnection();
        Statement st;
        try{
            st = con.createStatement();
            st.executeUpdate(query);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
       
           public ObservableList<Employee> getEmployee(){
        ObservableList<Employee> employees = FXCollections.observableArrayList();
        try (Connection connection = getConnection();
            Statement statement = connection.createStatement()){
            ResultSet rs = statement.executeQuery("SELECT * FROM employee");

            
            while (rs.next()) {
                  employees.add(new Employee(
                           rs.getInt("id"),
                           rs.getString("fname"),
                          rs.getString("mname"),
                          rs.getString("lname"),
                          rs.getInt("age"),
                           rs.getString("gender")                          
                  ));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return employees;
           }  
           
    public void addEmployee(Employee employee) {
    try (Connection connection = getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO employee (id, fname, mname, lname, age, gender) VALUES (?, ?, ?, ?, ?, ?)")) {
        preparedStatement.setInt(1, employee.getId());
        preparedStatement.setString(2, employee.getFname());
        preparedStatement.setString(3, employee.getMname());
        preparedStatement.setString(4, employee.getLname());
        preparedStatement.setInt(5, employee.getAge());
        preparedStatement.setString(6, employee.getGender());
        preparedStatement.executeUpdate();
        
    } catch (SQLException e) {
        e.printStackTrace();
    }
   }
   public void editEmployee(Employee employee) {
    try (Connection connection = getConnection();
         PreparedStatement preparedStatement = connection.prepareStatement("UPDATE employee SET fname = ?, mname = ?, lname = ?, age = ?, gender = ? WHERE id = ?")) {
        preparedStatement.setString(1, employee.getFname());
        preparedStatement.setString(2, employee.getMname());
        preparedStatement.setString(3, employee.getLname());
        preparedStatement.setInt(4, employee.getAge());
        preparedStatement.setString(5, employee.getGender());
        preparedStatement.setInt(6, employee.getId());
        preparedStatement.executeUpdate();
    } catch (SQLException e) {
        e.printStackTrace();
    }
   }
   public void deleteEmployee(int employeeId) {
    try (Connection connection = getConnection();
         PreparedStatement preparedStatement = connection.prepareStatement("DELETE FROM employee WHERE id = ?")) {
        preparedStatement.setInt(1, employeeId);
        preparedStatement.executeUpdate();
    } catch (SQLException e) {
        e.printStackTrace();
    }
}
   
   public boolean isEmployeeAlreadyExists(int id, String fname, String lname) {
    boolean exists = false;
    String query = "SELECT id FROM employee WHERE id = ? OR (fname = ? AND lname = ?)";
    try (Connection connection = getConnection();
         PreparedStatement preparedStatement = connection.prepareStatement(query)) {
        preparedStatement.setInt(1, id);
        preparedStatement.setString(2, fname);
        preparedStatement.setString(3, lname);
        ResultSet resultSet = preparedStatement.executeQuery();
        exists = resultSet.next();
    } catch (SQLException e) {
        e.printStackTrace();
    }
    return exists;
}
 }
