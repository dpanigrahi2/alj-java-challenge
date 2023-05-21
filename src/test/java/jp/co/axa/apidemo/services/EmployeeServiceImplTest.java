package jp.co.axa.apidemo.services;

import jp.co.axa.apidemo.entities.Employee;
import jp.co.axa.apidemo.error.EmployeeNotFoundException;
import jp.co.axa.apidemo.repositories.EmployeeRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;


class EmployeeServiceImplTest {

    private EmployeeServiceImpl employeeService;

    private EmployeeRepository employeeRepository;

    @BeforeEach
    public void beforeEach() {
        employeeService = new EmployeeServiceImpl();
        employeeRepository = mock(EmployeeRepository.class);
        employeeService.setEmployeeRepository(employeeRepository);
    }

    @Test
    void retrieveEmployees_successfulScenario() {
        List<Employee> employees = Arrays.asList(
                Employee.build(100L,"Arya", 10000, "DEV"),
                Employee.build(200L,"Sansa", 11000, "QA"),
                Employee.build(200L,"Tyrion", 12000, "HR"),
                Employee.build(200L,"John Snow", 9000, "Admin")
                );
        when(employeeRepository.findAll()).thenReturn(employees);
        List<Employee> allEmployees = employeeService.retrieveEmployees();
        Assertions.assertEquals(4, allEmployees.size());
    }

    @Test
    void getEmployee_successfulScenario() throws EmployeeNotFoundException {
        Employee employee = Employee.build(100L,"Arya", 10000, "DEV");
        when(employeeRepository.findById(anyLong())).thenReturn(Optional.of(employee));
        Employee e = employeeService.getEmployee(100L);
        Assertions.assertNotNull(e);
    }

    @Test
    void getEmployee_unsuccessfulScenario() throws EmployeeNotFoundException {
        when(employeeRepository.findById(anyLong())).thenReturn(Optional.empty());
        Assertions.assertThrows(EmployeeNotFoundException.class, () -> employeeService.getEmployee(1L));
    }

    @Test
    void deleteEmployee_successfulScenario() {
        Employee employee = Employee.build(100L,"Arya", 10000, "DEV");

        when(employeeRepository.findById(anyLong())).thenReturn(Optional.of(employee));
        Assertions.assertDoesNotThrow(() -> employeeService.deleteEmployee(100L));
        verify(employeeRepository, times(1)).deleteById(any());
    }

    @Test
    void deleteEmployee_unsuccessfulScenario() {
        when(employeeRepository.findById(anyLong())).thenReturn(Optional.empty());
        Assertions.assertThrows(EmployeeNotFoundException.class, () -> employeeService.deleteEmployee(1L));
    }

}
