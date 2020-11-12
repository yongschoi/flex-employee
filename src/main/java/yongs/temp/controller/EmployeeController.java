package yongs.temp.controller;

import java.util.Map;
import java.util.Random;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import yongs.temp.dao.EmployeePageRepository;
import yongs.temp.dao.EmployeeRepository;
import yongs.temp.model.Employee;

@RestController
@RequestMapping("/employee")
public class EmployeeController {
	private Logger logger = LoggerFactory.getLogger(EmployeeController.class);
	@Autowired
    EmployeeRepository repo;
	@Autowired
    EmployeePageRepository pageRepo;
	@Autowired
	private MongoOperations mongoOperation;
    
    @GetMapping("/all")
    public Flux<Employee> findAll() {
    	logger.debug("flex-employee|EmployeeController|findAll()");
    	// id 순으로 sort
    	return repo.findAll().sort( (a, b) -> new Integer(a.getId()) - new Integer(b.getId()) );
    }
 
    @GetMapping("/all-paged")
    public Flux<Employee> findAllPaged(final @RequestParam(name = "page", defaultValue = "0") int page,
    								   final @RequestParam(name = "size", defaultValue = "50") int size) {
    	logger.debug("flex-employee|EmployeeController|findAllPaged()");
    	// salary를 기준으로 order by
        return pageRepo.findAllByIdNotNullOrderBySalaryDesc(PageRequest.of(page, size));
    }
    
    /* Employee 데이터 생성을 위한 임시 method */
    @PostMapping("/init")
    public Mono<Void> init(@RequestBody Map<String, Long> numOfEmp) {
    	logger.debug("flex-employee|EmployeeController|init({})", numOfEmp.get("num")); 	
 	
    	long[] salary = {4000, 5000, 6000, 7000, 8000, 9000};
    	String[] sex = {"남", "여"};
    	
    	Random r = new Random();
    	int R_salary;
    	int R_sex;
    	
    	for(int idx=0; idx <numOfEmp.get("num"); idx++) {
    		R_salary = r.nextInt(salary.length);
    		R_sex = r.nextInt(sex.length);
    				
    		Employee e = new Employee();
    		e.setId(new Integer(idx).toString());
    		e.setName("User-"+idx);
    		e.setSalary(salary[R_salary]);
    		e.setSex(sex[R_sex]);
    		repo.save(e).subscribe();
    		
    		logger.debug(e.getId());
    	}
    	return Mono.empty();
    }
    
    @DeleteMapping("/clean")
    public Mono<Void> clean() {
    	logger.debug("flex-employee|EmployeeController|clean()");
    	mongoOperation.dropCollection("employee");
    	return Mono.empty();
    }
}
