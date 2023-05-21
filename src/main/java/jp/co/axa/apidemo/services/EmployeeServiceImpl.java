package jp.co.axa.apidemo.services;

import jp.co.axa.apidemo.dto.EmployeeRequest;
import jp.co.axa.apidemo.entities.Employee;
import jp.co.axa.apidemo.error.EmployeeNotFoundException;
import jp.co.axa.apidemo.repositories.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class EmployeeServiceImpl implements EmployeeService{

    @Autowired
    private EmployeeRepository employeeRepository;

    public void setEmployeeRepository(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    @Cacheable(cacheNames = "employees")
    public List<Employee> retrieveEmployees() {
        List<Employee> employees = employeeRepository.findAll();
        return employees;
    }

    @Cacheable(cacheNames = "employees", key = "#employeeId")
    public Employee getEmployee(Long employeeId) throws EmployeeNotFoundException {
        Optional<Employee> optEmp = employeeRepository.findById(employeeId);
        if(!optEmp.isPresent()) {
            throw new EmployeeNotFoundException("No Employee details found for id "+ employeeId);
        }
        return optEmp.get();
    }

    public Employee saveEmployee(EmployeeRequest request){
        Employee employee = Employee.
                build( 0L, request.getName(), request.getSalary(),
                        request.getDepartment());
        return employeeRepository.save(employee);
    }

    @CacheEvict(cacheNames =  "employees", key = "#employeeId")
    public void deleteEmployee(Long employeeId) throws EmployeeNotFoundException{
        Optional<Employee> optEmp = employeeRepository.findById(employeeId);
        if(!optEmp.isPresent()) {
            throw new EmployeeNotFoundException("No Employee details found for id "+ employeeId);
        }
        employeeRepository.deleteById(employeeId);
    }

    @CachePut(cacheNames = "employees", key = "#employeeId")
    public void updateEmployee(EmployeeRequest request, Long employeeId) throws EmployeeNotFoundException {
        Optional<Employee> optEmp = employeeRepository.findById(employeeId);
        if(!optEmp.isPresent()) {
            throw new EmployeeNotFoundException("No Employee details found for id "+ employeeId);
        }
        Employee employee = Employee.build(employeeId,request.getName(), request.getSalary(),
                request.getDepartment());

        employeeRepository.save(employee);
    }
}
