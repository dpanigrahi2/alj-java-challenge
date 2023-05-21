package jp.co.axa.apidemo.services;

import jp.co.axa.apidemo.dto.EmployeeRequest;
import jp.co.axa.apidemo.entities.Employee;
import jp.co.axa.apidemo.error.EmployeeNotFoundException;

import java.util.List;

public interface EmployeeService {

    public List<Employee> retrieveEmployees();

    public Employee getEmployee(Long employeeId) throws EmployeeNotFoundException;

    public Employee saveEmployee(EmployeeRequest request);

    public void deleteEmployee(Long employeeId) throws EmployeeNotFoundException;

    public void updateEmployee(EmployeeRequest request, Long employeeId) throws EmployeeNotFoundException;
}