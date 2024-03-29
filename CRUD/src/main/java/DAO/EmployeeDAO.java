package DAO;

import java.util.List;

import model.Employee;

public interface EmployeeDAO {
	 public void create(Employee employee) throws ClassNotFoundException;
	 public Employee read(int id) throws ClassNotFoundException;
	 public List<Employee> readAll() throws ClassNotFoundException;
	 public void update(Employee employee) throws ClassNotFoundException;
	 public void delete(int id) throws ClassNotFoundException;
}
	
