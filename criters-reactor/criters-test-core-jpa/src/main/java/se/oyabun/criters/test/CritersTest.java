/*
 * Copyright 2017 Oyabun AB
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package se.oyabun.criters.test;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import se.oyabun.criters.Criters;
import se.oyabun.criters.CritersFactory;
import se.oyabun.criters.criteria.Filter;
import se.oyabun.criters.exception.CritersException;
import se.oyabun.criters.exception.CritersSearchCriteriaException;
import se.oyabun.criters.exception.InvalidCritersTargetException;
import se.oyabun.criters.test.data.Bar;
import se.oyabun.criters.test.data.Baz;
import se.oyabun.criters.test.data.Foo;
import se.oyabun.criters.test.filter.FooPropertyFilter;
import se.oyabun.criters.test.filter.FooRelationFilter;
import se.oyabun.criters.test.filter.InvalidFooFilter;

import javax.persistence.EntityManager;
import javax.persistence.Persistence;
import java.util.List;
import java.util.stream.Collectors;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * Criters abstract JPA tests written to be reused with all compatible JPA implementations
 * for verification of compliance.
 *
 * @author Daniel Sundberg
 */
@RunWith(JUnit4.class)
public abstract class CritersTest {

    private static final Integer TEST_VALUE = 2;
    private static final Integer OTHER_VALUE = 1234;
    private static final String BAZ_VALUE = "value";

    private EntityManager entityManager;

    private Baz baz;
    private Foo foo, fooToo;
    private Bar bar;

    private CritersFactory<Foo, Filter<Foo>> critersFactory;

    @Before
    public void before()
            throws CritersException {

        //
        // Start JPA provider and get entity manager
        //
        entityManager = Persistence.createEntityManagerFactory("criterstest")
                                   .createEntityManager();

        critersFactory = generateCritersFactory();

        prepareData(entityManager);

    }

    /**
     * This can be overridden by extending classes to change init method
     */
    private CritersFactory<Foo, Filter<Foo>> generateCritersFactory()
            throws CritersException {

        return Criters.<Foo, Filter<Foo>> factory().use(entityManager);

    }

    private void prepareData(final EntityManager entityManager) {

        //
        // Prepare data in entityManager
        //
        entityManager.getTransaction().begin();

        foo = new Foo();
        foo.setValue(TEST_VALUE);
        entityManager.persist(foo);

        bar = new Bar();
        bar.setFoo(foo);
        foo.getBars().add(bar);
        entityManager.persist(bar);

        fooToo = new Foo();
        fooToo.setValue(OTHER_VALUE);
        entityManager.persist(fooToo);

        baz = new Baz();
        baz.setValue(BAZ_VALUE);
        baz.setBar(bar);
        bar.setBaz(baz);
        entityManager.persist(baz);

        entityManager.flush();

    }

    @Test
    public void testIsFilterable() {

        assertThat(critersFactory.isSearchable(Foo.class), is(true));

    }

    @Test
    public void testValidCritersStream()
            throws CritersSearchCriteriaException,
                   InvalidCritersTargetException {

        final List<Foo> testEntities =
                entityManager.createQuery(
                        critersFactory.prepare(new FooPropertyFilter(TEST_VALUE))
                                      .build().criteria())
                             .getResultStream()
                             .collect(Collectors.toList());

        assertThat(testEntities.size(), is(1));
        assertThat(testEntities.iterator().next().getValue(), is(TEST_VALUE));

    }

    @Test(expected = CritersSearchCriteriaException.class)
    public void testInvalidCritersStream()
            throws CritersSearchCriteriaException,
                   InvalidCritersTargetException {

        final InvalidFooFilter invalidTestEntityCriteria = new InvalidFooFilter();
        invalidTestEntityCriteria.setInvalidValue(0L);

        critersFactory.prepare(invalidTestEntityCriteria)
                      .build().criteria();

    }

    @Test
    public void testValidCritersUniqueCriteria()
            throws InvalidCritersTargetException,
                   CritersSearchCriteriaException {

        entityManager.createQuery(
                critersFactory.prepare(new FooPropertyFilter(TEST_VALUE))
                              .build().criteria()).getSingleResult();

    }

    @Test
    public void testRelationCritersUnique()
            throws InvalidCritersTargetException,
                   CritersSearchCriteriaException {

        entityManager.createQuery(
                critersFactory.prepare(new FooRelationFilter(bar.getId(), BAZ_VALUE))
                              .build().criteria()).getSingleResult();

    }

    @After
    public void after() {

        if(entityManager != null) {

            entityManager.getTransaction().rollback();

        }

    }

}
