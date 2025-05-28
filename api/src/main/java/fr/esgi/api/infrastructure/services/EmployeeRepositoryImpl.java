package fr.esgi.api.infrastructure.services;

import fr.esgi.api.infrastructure.jpa.entities.EmployeeEntity;
import fr.esgi.api.infrastructure.jpa.repositories.EmployeeJpaRepository;
import fr.esgi.api.infrastructure.mappers.EmployeeMapper;
import fr.esgi.api.model.manage_employees.Employee;
import fr.esgi.api.model.manage_employees.EmployeeRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EmployeeRepositoryImpl implements EmployeeRepository {
    private final EmployeeJpaRepository employeeJpaRepository;
    private final EmployeeMapper employeeMapper;

    public EmployeeRepositoryImpl(EmployeeJpaRepository employeeJpaRepository, EmployeeMapper employeeMapper) {
        this.employeeJpaRepository = employeeJpaRepository;
        this.employeeMapper = employeeMapper;
    }

    @Override
    public List<Employee> getAll() {
        List<EmployeeEntity> employees = this.employeeJpaRepository.findAll();
        return employees.stream().map(this.employeeMapper::toDomain).toList();
    }
}
