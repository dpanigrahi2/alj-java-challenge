package jp.co.axa.apidemo.controllers;

import jp.co.axa.apidemo.dto.EmployeeRequest;
import jp.co.axa.apidemo.entities.Employee;
import jp.co.axa.apidemo.error.EmployeeNotFoundException;
import jp.co.axa.apidemo.services.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/v1")
public class EmployeeController {

    @Autowired
    private EmployeeService employeeService;

    public void setEmployeeService(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    @GetMapping("/employees")
    public ResponseEntity<List<Employee>> getEmployees() {
        return ResponseEntity.ok(employeeService.retrieveEmployees());
    }

    @GetMapping("/employees/{employeeId}")
    public ResponseEntity<Employee> getEmployee(@PathVariable(name="employeeId")Long employeeId) throws EmployeeNotFoundException {
        return ResponseEntity.ok(employeeService.getEmployee(employeeId));
    }

    @PostMapping("/employees")
    public ResponseEntity<Employee>  saveEmployee(@Valid @RequestBody EmployeeRequest request){
        return new ResponseEntity<>(employeeService.saveEmployee(request), HttpStatus.CREATED);
    }

    @DeleteMapping("/employees/{employeeId}")
    public ResponseEntity<Void> deleteEmployee(@PathVariable(name="employeeId")Long employeeId) throws EmployeeNotFoundException {
        employeeService.deleteEmployee(employeeId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PutMapping("/employees/{employeeId}")
    public ResponseEntity<Void> updateEmployee(@RequestBody EmployeeRequest request,
                                               @PathVariable(name="employeeId")Long employeeId) throws EmployeeNotFoundException {
        employeeService.updateEmployee(request, employeeId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
