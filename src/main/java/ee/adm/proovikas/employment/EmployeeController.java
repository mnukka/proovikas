package ee.adm.proovikas.employment;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("api")
public class EmployeeController {
    private final Logger LOG = LoggerFactory.getLogger(EmployeeController.class);

    private EmployeeCRUDService employeeCRUDService;
    private EmployeeDepartmentService employeeDepartmentService;

    EmployeeController(EmployeeCRUDService employeeCRUDService, EmployeeDepartmentService employeeDepartmentService) {
        this.employeeCRUDService = employeeCRUDService;
        this.employeeDepartmentService = employeeDepartmentService;
    }

    @PostMapping("/employee")
    public ResponseEntity<Employee> createEmployee(@Valid @RequestBody Employee employee) {
        LOG.info("REST request to create Employee : {}", employee.getId());
        if (employee.getId() != null) {
            Employee newEmployee = employeeCRUDService.createEmployee(employee);
            return ResponseEntity.ok(newEmployee);
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
    }

    @PutMapping("/employee")
    public ResponseEntity<Employee> updateEmployee(@Valid @RequestBody Employee employee) {
        LOG.info("REST request to update Employee : {}", employee.getId());
        try {
            Employee updatedEmployee = employeeCRUDService.updateEmployee(employee);
            return ResponseEntity.ok(updatedEmployee);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }

    @DeleteMapping("/employee/{id}")
    public ResponseEntity<Void> deleteEmployeeById(@PathVariable Long id) {
        LOG.info("REST request to delete Employee : {}", id);

        try {
            employeeCRUDService.deleteEmployee(id);
            return ResponseEntity.status(HttpStatus.OK).body(null);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }

    @GetMapping("/employee/{id}")
    public ResponseEntity<Employee> getEmployeeById(@PathVariable Long id) {
        LOG.info("REST request to get Employee : {}", id);
        try {
            Employee employee = employeeCRUDService.getEmployeeById(id);
            return ResponseEntity.ok(employee);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }

    @RequestMapping(value = "/employee/subordinates/{id}", method = RequestMethod.GET)
    public ResponseEntity<Set<Employee>> getSubordinatesByEmployeeId(@PathVariable Long id) {
        LOG.info("REST request to get Employee subordinates : {}", id);
        try {
            Set<Employee> subordinates = employeeDepartmentService.getSubordinatesOfEmployee(id);
            return ResponseEntity.ok(subordinates);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }
}
