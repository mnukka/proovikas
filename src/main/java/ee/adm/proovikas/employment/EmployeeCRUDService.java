package ee.adm.proovikas.employment;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.Optional;

@Service
public class EmployeeCRUDService {

    @PersistenceContext
    private EntityManager entityManager;

    private EmployeeRepository employeeRepository;

    public EmployeeCRUDService(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    @Transactional
    public Employee createEmployee(Employee employee) {
        return employeeRepository.save(employee);
    }

    @Transactional
    public Employee updateEmployee(Employee updateValues) throws Exception {
        Optional<Employee> employeeResult = employeeRepository.findById(updateValues.getId());

        if (!employeeResult.isPresent()) {
            throw new Exception("No ID found");
        }

        Employee employee = employeeResult.get();
        employee.setFirstName(updateValues.getFirstName());
        employee.setLastName(updateValues.getLastName());
        employee.setJobTitle(updateValues.getJobTitle());
        employee.setSubordinates(updateValues.getSubordinates());
        employee = employeeRepository.save(employee);
        return employee;
    }

    @Transactional
    public void deleteEmployee(Long id) throws Exception {
        Optional<Employee> employeeResult =  employeeRepository.findById(id);

        if (!employeeResult.isPresent()) {
            throw new Exception("No ID found");
        }
        employeeResult.get().getSubordinates().forEach(p -> p.setManager(null));
        entityManager.remove(employeeResult.get());
    }

    @Transactional
    public Employee getEmployeeById(Long id) throws Exception {
        Optional<Employee> employeeResult = employeeRepository.findById(id);

        if (!employeeResult.isPresent()) {
            throw new Exception("No ID found");
        }
        return employeeResult.get();
    }
}
