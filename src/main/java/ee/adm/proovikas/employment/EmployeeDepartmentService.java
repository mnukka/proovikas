package ee.adm.proovikas.employment;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class EmployeeDepartmentService {
    private final Logger LOG = LoggerFactory.getLogger(EmployeeDepartmentService.class);

    private EmployeeRepository employeeRepository;
    private EmployeeCRUDService employeeCRUDService;

    public EmployeeDepartmentService(EmployeeRepository employeeRepository, EmployeeCRUDService employeeCRUDService) {
        this.employeeRepository = employeeRepository;
        this.employeeCRUDService = employeeCRUDService;
    }

    @PostConstruct
    @Transactional
    public void init() throws Exception {
        Employee employee1 = new Employee("Lennart", "Meri", "President");
        Employee employee2 = new Employee("Kenno", "Juske", "Poliitik");
        Employee employee3 = new Employee("Maire", "Aunaste", "Poliitik");
        Employee employee4 = new Employee("Jaak", "Aaviksoo", "Poliitik");
        employeeCRUDService.createEmployee(employee1);
        employeeCRUDService.createEmployee(employee2);
        employeeCRUDService.createEmployee(employee3);
        employeeCRUDService.createEmployee(employee4);

        Set<Employee> subordinates = new HashSet<>(Arrays.asList(employee2, employee3, employee4));

        subordinates.forEach(p -> p.setManager(employee1));

        employee1.setSubordinates(subordinates);
        try {
            employeeCRUDService.updateEmployee(employee1);
        } catch (Exception e) {
            LOG.info("PostConstruct failure: " + e.getMessage());
        }
    }

    @Transactional
    public Set<Employee> getSubordinatesOfEmployee(Long id) throws Exception {
        Optional<Employee> employeeResult = employeeRepository.findById(id);
        if (!employeeResult.isPresent()) {
            throw new Exception("No subordinates");
        }

        return employeeResult.get().getSubordinates();
    }
}
