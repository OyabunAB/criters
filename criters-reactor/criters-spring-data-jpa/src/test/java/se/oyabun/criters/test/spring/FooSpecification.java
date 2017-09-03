package se.oyabun.criters.test.spring;

import se.oyabun.criters.criteria.Filter;
import se.oyabun.criters.spring.CritersSpecification;
import se.oyabun.criters.test.data.Foo;

public class FooSpecification
        extends CritersSpecification<Foo, Filter<Foo>> {

    public FooSpecification(final Filter<Foo> searchFilter) {

        super(searchFilter);

    }

}
