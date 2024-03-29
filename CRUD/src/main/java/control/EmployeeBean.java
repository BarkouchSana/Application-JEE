package control;



import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;

import DAO.EmployeeDAOImpl;
import DAO.MyConnexion;
import model.Employee;

@ManagedBean
@SessionScoped
public class EmployeeBean implements Serializable {
    private static final long serialVersionUID = 455659950717243338L;
    private Employee employee = new Employee();
    private EmployeeDAOImpl employeeDAO = new EmployeeDAOImpl();
    private List<Employee> employees = new ArrayList<>();
    
  
   
    private int startIndex = 0;
    private int endIndex = 4; 
    private int totalEmployees;
    
   


    public EmployeeBean() throws ClassNotFoundException {
        employees = employeeDAO.readAll();
    }
   
 
    private void fetchEmployees() {
        try {
            employees = employeeDAO.readRange(startIndex, endIndex); 
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    // Méthode pour afficher les lignes suivantes
    public void nextPage() {
        if (endIndex < totalEmployees - 1) {
            startIndex += 5;
            endIndex += 5;
            fetchEmployees();
        }
    }

    
    public void previousPage() {
        if (startIndex > 0) {
            startIndex -= 5;
            endIndex -= 5;
            fetchEmployees();
        }
    }
    
    
    public void cancelEdit(Employee emp) {
        emp.setEditMode(false);
    }
    
    
    public String goToIndexPage() {
        return "index?faces-redirect=true";
    }
    public void insert(ActionEvent evt) throws ClassNotFoundException {
        employeeDAO.create(employee);
        fetchEmployees(); 
        showMessage("Employee " + employee.getName() + " created successfully!");
        clear(); 
    }

   
    public void update() throws ClassNotFoundException {
        employeeDAO.update(employee);
        fetchEmployees();
        showMessage("Employee " + employee.getName() + " updated successfully!");
    }
    
    @PostConstruct
    public void init() {
        try {
            totalEmployees = employeeDAO.getTotalEmployees();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public boolean hasPreviousPage() {
        return startIndex > 0;
    }

    public boolean hasNextPage() {
        return endIndex < totalEmployees - 1;
    }

  

    
    
    public void remove(Employee emp) throws ClassNotFoundException {
        employeeDAO.delete(emp.getEmployeeId());
        employees = employeeDAO.readAll();
        showMessage("Employee " + emp.getName() + " est supprimee!");
    }

    public void clear() {
        employee = new Employee();
    }

    private void showMessage(String summary) {
        FacesContext context = FacesContext.getCurrentInstance();
        context.addMessage(null, new FacesMessage(summary));
    }
    
    
     
    private void loadEmployees() {
        employees = new ArrayList<>();
        try (Connection conn = MyConnexion.getConnection()) {
            String sql = "SELECT * FROM employee ";
            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                try (ResultSet rs = pstmt.executeQuery()) {
                    while (rs.next()) {
                        Employee emp = new Employee();
                        emp.setEmployeeId(rs.getInt("employee_id"));
                        emp.setName(rs.getString("name"));
                        emp.setDepartment(rs.getString("department"));
                        emp.setBirthDate(rs.getString("birth_date"));
                        
                        employees.add(emp);
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
           
        }
    }
    
    
    
    public void saveChanges() {
        try (Connection conn = MyConnexion.getConnection()) {
            for (Employee emp : employees) {
                if (emp.isEditMode()) {
                    String sql = "UPDATE employee SET name = ?, department = ? WHERE employee_id = ?";
                    try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                        pstmt.setString(1, emp.getName());
                        pstmt.setString(2, emp.getDepartment());
                        pstmt.setInt(3, emp.getEmployeeId());
                        pstmt.executeUpdate();
                    }
                }
            }
            
            loadEmployees();
        } catch (SQLException e) {
            e.printStackTrace();
            
        }
    }
    
    

    public Employee getEmployee() {
        return employee;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
    }

    public List<Employee> getEmployees() {
        return employees;
    }

    public void setEmployees(List<Employee> employees) {
        this.employees = employees;
    }

    public Employee getNullEmployee() {
        return new Employee();
    }
    
    
    public String goToAddPage() {
        return "addEmployee?faces-redirect=true";
    }
    
    public void toggleEditMode(Employee employee) {
        employee.setEditMode(!employee.isEditMode());
    }

}
