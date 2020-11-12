package yongs.temp.dao;

import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.reactive.ReactiveSortingRepository;

import reactor.core.publisher.Flux;
import yongs.temp.model.Employee;

public interface EmployeePageRepository extends ReactiveSortingRepository<Employee, String> {
	Flux<Employee> findAllByIdNotNullOrderBySalaryDesc(final Pageable page);
}
