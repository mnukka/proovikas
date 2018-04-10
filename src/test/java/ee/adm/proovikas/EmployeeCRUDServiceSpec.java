package ee.adm.proovikas;

import ee.adm.proovikas.employment.Employee;
import ee.adm.proovikas.employment.EmployeeCRUDService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
public class EmployeeCRUDServiceSpec {

    @Autowired
    EmployeeCRUDService employeeCRUDService;

    @PersistenceContext
    private EntityManager entityManager;

    @Test
    public void serviceInitializedCorrectly() {
        assertThat(employeeCRUDService).isNotNull();
    }

    @Test
    public void validateEmployeeCreation() {
        Employee employee = createEmployee("Taavi", "Taukar");
        assertThat(employee.getId()).isNotNull();
    }

    // This test fails
    // currently entity does not supporting updating Employee's manager_id by assigning an Employee to a manager Entity
    @Test
    public void validateEmployeeSubordinateUpdate() throws Exception {
        Employee subordinate = createEmployee("Miko", "Nukka");
        Employee manager = createEmployee("David", "Jop");
        Set<Employee> subordinates = Stream.of(subordinate).collect(Collectors.toSet());
        manager.setSubordinates(subordinates);
        employeeCRUDService.updateEmployee(manager);

        Employee subordinateVerify = entityManager.find(Employee.class, subordinate.getId());
        assertThat(subordinateVerify.getManager().getId()).isEqualTo(manager.getId());
    }

    @Test
    public void validateEmployeeUpdate() throws Exception {
        Employee oldEmployee = createEmployee("Miko", "Nukka");
        Employee employee = new Employee();
        employee.setFirstName("Miko");
        employee.setLastName("SomethingElse");
        employee.setId(oldEmployee.getId());
        employeeCRUDService.updateEmployee(employee);

        Employee employeeVerify = entityManager.find(Employee.class, oldEmployee.getId());
        assertThat(employeeVerify.getLastName()).isEqualTo(employee.getLastName());
    }

    @Test
    public void getSubordinatesForEmployee() throws Exception {
        Set<Employee> employeeList = new HashSet<Employee>();
        Employee manager = createEmployee("Keegi", "Boss");
        for (int i = 0; i <= 25; i++) {
            Employee employee = createEmployeeM("Mati" + i, "Laan", manager);
            employeeList.add(employee);
        }
        manager.setSubordinates(employeeList);
        employeeCRUDService.updateEmployee(manager);
        Employee boss = employeeCRUDService.getEmployeeById(manager.getId());

        assertThat(boss.getSubordinates()).isEqualTo(employeeList);
    }

    @Transactional
    public Employee createEmployeeM(String firstName, String lastName, Employee manager) {
        Employee employee = new Employee();
        employee.setFirstName(firstName);
        employee.setLastName(lastName);
        employee.setManager(manager);
        return employeeCRUDService.createEmployee(employee);
    }

    @Transactional
    public Employee createEmployee(String firstName, String lastName) {
        Employee employee = new Employee();
        employee.setFirstName(firstName);
        employee.setLastName(lastName);
        return employeeCRUDService.createEmployee(employee);
    }
}
