package yongs.temp.dao;

import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

import reactor.core.publisher.Flux;
import yongs.temp.model.Employee;

public interface EmployeeRepository extends ReactiveMongoRepository<Employee, String> {	
	@Query("{ 'name': ?0 }")
	Flux<Employee> findByName(final String name);
}