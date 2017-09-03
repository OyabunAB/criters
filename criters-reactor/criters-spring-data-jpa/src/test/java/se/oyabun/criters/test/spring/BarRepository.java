package se.oyabun.criters.test.spring;

import org.springframework.data.repository.CrudRepository;
import se.oyabun.criters.test.data.Bar;

public interface BarRepository extends CrudRepository<Bar, Long> {

}
