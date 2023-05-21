package jp.co.axa.apidemo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;


@Data
@AllArgsConstructor(staticName = "build")
@NoArgsConstructor
public class EmployeeRequest {
    @NotBlank
    private String name;
    @Min(0)
    @Max(1000000)
    private Integer salary;
    @NotBlank
    private String department;

}