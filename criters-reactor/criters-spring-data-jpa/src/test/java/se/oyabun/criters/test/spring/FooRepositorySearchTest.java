package se.oyabun.criters.test.spring;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import se.oyabun.criters.test.data.Bar;
import se.oyabun.criters.test.data.Foo;
import se.oyabun.criters.test.filter.FooRelationFilter;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.is;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = SpringTestConfiguration.class)
public class FooRepositorySearchTest {

    private static long NON_EXISTING_BAR_ID = -123L;

    @Autowired
    private FooRepository fooRepository;

    @Autowired
    private BarRepository barRepository;

    private Foo foo;
    private Bar bar;

    @Before
    public void before() {

        foo = new Foo();
        bar = new Bar();
        bar.setFoo(foo);

        fooRepository.save(foo);
        barRepository.save(bar);

    }

    @Test
    public void verifyCriters() {

        FooRelationFilter noResultsExpectedFilter = new FooRelationFilter(NON_EXISTING_BAR_ID);
        FooRelationFilter resultExpectedFilter = new FooRelationFilter(bar.getId());

        assertThat(fooRepository.findAll(new FooSpecification(noResultsExpectedFilter)), is(empty()));
        assertThat(fooRepository.findAll(new FooSpecification(resultExpectedFilter)), contains(foo));

    }

}
