/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMain.java to edit this template
 */
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javax.swing.JOptionPane;

/**
 *
 * @author Admin
 */

public class TableController  implements Initializable {

    @FXML
    private ToggleButton light;
    
    @FXML
    private ToggleButton dark;
  
    @FXML
    private TextField input_age;

    @FXML
    private TextField input_fname;

    @FXML
    private ChoiceBox<String> input_gender;

    @FXML
    private TextField input_id;

    @FXML
    private TextField input_lname;

    @FXML
    private TextField input_mname;
    
    @FXML
    private TableColumn<Employee, Integer>view_id;
    
    @FXML
    private TableColumn<Employee, String>view_fname;
    
    @FXML
    private TableColumn<Employee, String>view_mname;
    
    @FXML
    private TableColumn<Employee, String>view_lname;
    
    @FXML
    private TableColumn<Employee, Integer>view_age;
    
    @FXML
    private TableColumn<Employee, String>view_gender;
    
    @FXML
    private TableView<Employee>view_table;
 
    private SQLconnector sqlConnector;    
    private ObservableList<Employee> list;
    private Connection conn = null;
    private PreparedStatement ps = null;
    private ResultSet rs = null;
    private String[] genderBox = {"Male","Female"};
    int index = -1;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
        // Initialize the ObservableList
        list = FXCollections.observableArrayList();
        
        // Set up your TableView columns and connect them to the Employee properties
        view_id.setCellValueFactory(new PropertyValueFactory <Employee, Integer> ("id"));
        view_fname.setCellValueFactory(new PropertyValueFactory <Employee, String> ("fname"));
        view_mname.setCellValueFactory(new PropertyValueFactory <Employee, String> ("mname"));
        view_lname.setCellValueFactory(new PropertyValueFactory <Employee, String> ("lname"));
        view_age.setCellValueFactory(new PropertyValueFactory <Employee, Integer> ("age"));
        view_gender.setCellValueFactory(new PropertyValueFactory <Employee, String> ("gender"));
        
        input_gender.getItems().addAll(genderBox);        
        // Call a method to load data from your database
       sqlConnector = new SQLconnector();
        
        // Call a method to load data from your database
        loadDataFromDatabase();
    }

    
    
    private void loadDataFromDatabase() {
        try {
            // Fetch employee data from the SQLconnector
            ObservableList<Employee> employees = sqlConnector.getEmployee();
            
            // Set the data into the TableView
            view_table.setItems(employees);
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    @FXML
    public void switch_mode(ActionEvent e){
       Scene scene = input_age.getScene(); // Get a reference to the current scene.

    if (e.getSource() == dark) {
        // Apply dark mode CSS
        scene.getStylesheets().clear(); // Clear any existing stylesheets
        scene.getStylesheets().add(getClass().getResource("Dark_mode.css").toExternalForm());
         }

    if (e.getSource() == light) {
        // Apply light mode CSS
        scene.getStylesheets().clear(); // Clear any existing stylesheets
         scene.getStylesheets().add(getClass().getResource("text.css").toExternalForm());
          }
    }
       
   @FXML
   public void add_data(ActionEvent e) {
    if (input_id.getText().isEmpty() || input_fname.getText().isEmpty() || input_lname.getText().isEmpty() || input_age.getText().isEmpty() || input_gender.getValue() == null) {
        JOptionPane.showMessageDialog(null, "Must Input all needed data");
    } else {
        int id = Integer.parseInt(input_id.getText());
        String fname = input_fname.getText();
        String lname = input_lname.getText();
        // Check if the employee already exists in the database based on both id and fname/lname
        if (isEmployeeExists(id, fname, lname)) {
            JOptionPane.showMessageDialog(null, "ID, first name, and last name already exists.");
        } else {
            Employee newEmployee = new Employee(
                Integer.parseInt(input_id.getText()),
                input_fname.getText(),
                input_mname.getText(),
                input_lname.getText(),
                Integer.parseInt(input_age.getText()),
                input_gender.getValue().toString()
            );

            sqlConnector.addEmployee(newEmployee);

            // Refresh the TableView after adding data
            loadDataFromDatabase();

            // Clear input fields
            clearInputFields();
        }
    }
}

    @FXML
    public void delete_data(ActionEvent e) {
        if (input_id.getText().isEmpty()) {
        JOptionPane.showMessageDialog(null, "Must Input all needed data");
    } else {
         int employeeId = Integer.parseInt(input_id.getText());
         sqlConnector.deleteEmployee(employeeId);
    
         // Refresh the TableView after deleting data
         loadDataFromDatabase();
    
         // Clear input fields
         clearInputFields();
        }
    }

    @FXML
    public void edit_data(ActionEvent e) {
        // Get the selected index
    if (input_id.getText().isEmpty() || input_fname.getText().isEmpty() || input_lname.getText().isEmpty() || input_age.getText().isEmpty() || input_gender.getValue() == null) {
        JOptionPane.showMessageDialog(null, "Must Input all needed data");
    } else {
    int selectedIndex = view_table.getSelectionModel().getSelectedIndex();
    
    if (selectedIndex >= 0) {
        // Get the selected Employee object from the TableView
        Employee selectedEmployee = view_table.getItems().get(selectedIndex);
        
        // Update the selected Employee object with the new data from input fields
        selectedEmployee.setFname(input_fname.getText());
        selectedEmployee.setMname(input_mname.getText());
        selectedEmployee.setLname(input_lname.getText());
        selectedEmployee.setAge(Integer.parseInt(input_age.getText()));
        selectedEmployee.setGender(input_gender.getValue().toString());
        
        // Update the employee in the database
        sqlConnector.editEmployee(selectedEmployee);

        // Refresh the TableView after editing data
        loadDataFromDatabase();

        // Clear input fields
        clearInputFields();
               }
         }
    }
    
     @FXML
    public void click_data(MouseEvent event) {
        index = view_table.getSelectionModel().getSelectedIndex();
        if(index >= 0){
            input_id.setText(view_id.getCellData(index).toString());
            input_fname.setText(view_fname.getCellData(index).toString());
            input_mname.setText(view_mname.getCellData(index).toString());
            input_lname.setText(view_lname.getCellData(index).toString());
            input_age.setText(view_age.getCellData(index).toString());
            input_gender.setValue(view_gender.getCellData(index).toString());
            
            
        }
    }
    
    private boolean isEmployeeExists(int id, String fname, String lname) {
    // Implement the logic to check if the ID already exists in the database
    // You'll need to query your database and return true if the ID exists, false otherwise.
    // You can use the 'sqlConnector' for this purpose.
    // For demonstration purposes, I'll assume that 'sqlConnector' has a method 'isIdExists'.
    return sqlConnector.isEmployeeAlreadyExists(id,fname,lname);
}
    
   private void clearInputFields() {
    input_id.clear();
    input_fname.clear();
    input_mname.clear();
    input_lname.clear();
    input_age.clear();
    input_gender.setValue(null);
      }
}

