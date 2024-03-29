package DAO;



import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.List;

import model.Employee;

public class EmployeeDAOImpl implements EmployeeDAO{
    

    private Connection connection;
    private Statement statement;

    public void connect() throws ClassNotFoundException {
        try {
            connection = MyConnexion.getConnection();
            statement = connection.createStatement();
            System.out.println("Connected");
        } catch (SQLException e) {
            System.err.println("Error connecting: " + e.getMessage());
        }
    }

    public void close() {
        try {
            statement.close();
            connection.close();
            System.out.println("Disconnected");
        } catch (SQLException e) {
            System.err.println("Error disconnecting: " + e.getMessage());
        }
    }

    public void create(Employee employee) throws ClassNotFoundException {
        connect();
        try {
            statement.execute("INSERT INTO employee(employee_id, name, department, birth_date) VALUES('"
                    + employee.getEmployeeId()
                    + "', '"
                    + employee.getName()
                    + "', '"
                    + employee.getDepartment() + "', '" + employee.getBirthDate() + "')");
        } catch (SQLException e) {
            System.err.println("Error inserting employee: " + e.getMessage());
        } finally {
            close();
        }
    }

    public Employee read(int id) throws ClassNotFoundException {
        connect();
        ResultSet rs;
        Employee temp = null;
        try {
            rs = statement.executeQuery("SELECT * FROM employee WHERE employee_id = '" + id + "';");
            while (rs.next()) {
                temp = new Employee(rs.getInt("employee_id"), rs.getString("name"),
                        rs.getString("department"), rs.getString("birth_date"));
            }
            return temp;

        } catch (SQLException e) {
            System.err.println("Error fetching employee: " + e.getMessage());
        } finally {
            close();
        }
        return null;
    }
    
    public void update(Employee employee) throws ClassNotFoundException {
        connect();
        try {
            StringBuilder queryBuilder = new StringBuilder("UPDATE employee SET ");
            boolean hasUpdate = false;

            if (employee.getName() != null) {
                queryBuilder.append("name = ?, ");
                hasUpdate = true;
            }
            if (employee.getDepartment() != null) {
                queryBuilder.append("department = ?, ");
                hasUpdate = true;
            }
            if (employee.getBirthDate() != null) {
                queryBuilder.append("birth_date = ?, ");
                hasUpdate = true;
            }

           
            if (hasUpdate) {
                queryBuilder.delete(queryBuilder.length() - 2, queryBuilder.length());
                queryBuilder.append(" WHERE employee_id = ?");

                
                PreparedStatement preparedStatement = connection.prepareStatement(queryBuilder.toString());
                int parameterIndex = 1;
                if (employee.getName() != null) {
                    preparedStatement.setString(parameterIndex++, employee.getName());
                }
                if (employee.getDepartment() != null) {
                    preparedStatement.setString(parameterIndex++, employee.getDepartment());
                }
                if (employee.getBirthDate() != null) {
                    preparedStatement.setDate(parameterIndex++, Date.valueOf(employee.getBirthDate()));
                }

                
                preparedStatement.setInt(parameterIndex, employee.getEmployeeId());

               
                int rowsUpdated = preparedStatement.executeUpdate();
                System.out.println("Rows updated: " + rowsUpdated);
            } else {
                System.out.println("No changes to update.");
            }
        } catch (SQLException e) {
            System.err.println("Error updating employee: " + e.getMessage());
        } finally {
            close();
        }
    }

    
    public List<Employee> readRange(int startIndex, int endIndex) throws ClassNotFoundException {
        connect();
        List<Employee> liste = new ArrayList<>();
        ResultSet rs;
        Employee temp;
        try {
            
            String query = "SELECT * FROM employee LIMIT ?, ?";
            PreparedStatement pstmt = connection.prepareStatement(query);
            pstmt.setInt(1, startIndex);
            pstmt.setInt(2, endIndex - startIndex + 1);
            rs = pstmt.executeQuery();

            while (rs.next()) {
                temp = new Employee(rs.getInt("employee_id"), rs.getString("name"),
                        rs.getString("department"), rs.getString("birth_date"));
                liste.add(temp);
            }
            return liste;
        } catch (SQLException e) {
            System.err.println("Error fetching employees: " + e.getMessage());
            return null;
        } finally {
            close();
        }
    }
    
    
    public int getTotalEmployees() throws ClassNotFoundException {
        connect();
        int totalEmployees = 0;
        try {
            ResultSet rs = statement.executeQuery("SELECT COUNT(*) AS total FROM employee");
            if (rs.next()) {
                totalEmployees = rs.getInt("total");
            }
        } catch (SQLException e) {
            System.err.println("Error fetching total number of employees: " + e.getMessage());
        } finally {
            close();
        }
        return totalEmployees;
    }


    
    
    
    public List<Employee> readAll() throws ClassNotFoundException {
        connect();
        List<Employee> employees = new ArrayList<>();
        try {
            ResultSet resultSet = statement.executeQuery("SELECT * FROM employee");
            while (resultSet.next()) {
                Employee employee = new Employee(resultSet.getInt("employee_id"), 
                                                  resultSet.getString("name"),
                                                  resultSet.getString("department"),
                                                  resultSet.getString("birth_date"));
                employees.add(employee);
            }
        } catch (SQLException e) {
            System.err.println("Error fetching employees: " + e.getMessage());
        } finally {
            close();
        }
        return employees;
    }


   


    public void delete(int id) throws ClassNotFoundException {
        connect();
        try {
            statement.execute("DELETE FROM employee WHERE employee_id = '" + id + "';");
        } catch (SQLException e) {
            System.err.println("Error deleting employee: " + e.getMessage());
        } finally {
            close();
        }
    }
}
