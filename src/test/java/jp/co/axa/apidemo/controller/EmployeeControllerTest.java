package jp.co.axa.apidemo.controller;

import jp.co.axa.apidemo.dto.EmployeeRequest;
import jp.co.axa.apidemo.services.EmployeeService;
import jp.co.axa.apidemo.entities.Employee;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.Arrays;
import java.util.List;

import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.times;
import static org.mockito.ArgumentMatchers.any;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class EmployeeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private EmployeeService employeeService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void getEmployees_successfulScenario() throws Exception {
        List<Employee> employees = Arrays.asList(
                Employee.build(100L,"Arya", 10000, "DEV"),
                Employee.build(200L,"Sansa", 11000, "QA"),
                Employee.build(200L,"Tyrion", 12000, "HR"),
                Employee.build(200L,"John Snow", 9000, "Admin")
        );

        given(employeeService.retrieveEmployees()).willReturn(employees);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/employees")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].name").value("Arya"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].name").value("Sansa"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[2].name").value("Tyrion"));
    }

    @Test
    public void getEmployee_successfulScenario() throws Exception {
        Employee employee = Employee.build(100L,"Arya", 10000, "DEV");
        given(employeeService.getEmployee(100L)).willReturn(employee);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/employees/100")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Arya"))
                .andExpect(jsonPath("$.salary").value(10000))
                .andExpect(jsonPath("$.department").value("DEV"));

    }

    @Test
    public void saveEmployee_successfulScenario() throws Exception {
        EmployeeRequest request = EmployeeRequest.build("Arya", 10000, "DEV");
        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/employees")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated());

        then(employeeService).should(times(1)).saveEmployee(any(EmployeeRequest.class));
    }

    @Test
    public void deleteEmployee_successfulScenario() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete("/api/v1/employees/100")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

        then(employeeService).should(times(1)).deleteEmployee(100L);
    }

    @Test
    public void updateEmployee_successfulScenario() throws Exception {
        EmployeeRequest request = EmployeeRequest.build("Arya", 12000, "SRE");

        mockMvc.perform(MockMvcRequestBuilders.put("/api/v1/employees/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNoContent());

        given(employeeService.getEmployee(anyLong())).willReturn(Employee.build(100L,"Arya", 12000, "SRE"));
        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/employees/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Arya"))
                .andExpect(jsonPath("$.salary").value(12000))
                .andExpect(jsonPath("$.department").value("SRE"));
    }
}
