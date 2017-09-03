package se.oyabun.criters.test.spring;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;
import se.oyabun.criters.test.data.Foo;

public interface FooRepository extends CrudRepository<Foo, Long>,
                                       JpaSpecificationExecutor<Foo> {

}
