package model;

public class Employee {
    private int employeeId;
    private String name;
    private String department;
    private String birthDate;
    
    private boolean editMode;

    
    public Employee() {
    }

    
    public Employee(int employeeId, String name, String department, String birthDate) {
        this.employeeId = employeeId;
        this.name = name;
        this.department = department;
        this.birthDate = birthDate;
    }

   
    public int getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(int employeeId) {
        this.employeeId = employeeId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(String birthDate) {
        this.birthDate = birthDate;
    }
    
    
    public boolean isEditMode() {
        return editMode;
    }

    public void setEditMode(boolean editMode) {
        this.editMode = editMode;
    }
}
