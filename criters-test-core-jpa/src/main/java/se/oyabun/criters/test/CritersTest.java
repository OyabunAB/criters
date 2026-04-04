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

import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaQuery;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import se.oyabun.criters.Criters;
import se.oyabun.criters.CritersFactory;
import se.oyabun.criters.criteria.Filter;
import se.oyabun.criters.exception.CritersException;
import se.oyabun.criters.exception.CritersSearchCriteriaException;
import se.oyabun.criters.exception.InvalidCritersTargetException;
import se.oyabun.criters.test.data.Bar;
import se.oyabun.criters.test.data.Baz;
import se.oyabun.criters.test.data.Foo;
import se.oyabun.criters.test.filter.FooBazLikeFilter;
import se.oyabun.criters.test.filter.FooInFilter;
import se.oyabun.criters.test.filter.FooPropertyFilter;
import se.oyabun.criters.test.filter.FooRelationFilter;
import se.oyabun.criters.test.filter.FooValueIsNotNullFilter;
import se.oyabun.criters.test.filter.FooValueIsNullFilter;
import se.oyabun.criters.test.filter.InvalidFooFilter;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import java.util.Arrays;
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
public abstract class CritersTest {

    private static final Integer TEST_VALUE = 2;
    private static final Integer OTHER_VALUE = 1234;
    private static final String BAZ_VALUE = "value";

    private EntityManagerFactory entityManagerFactory;
    private EntityManager entityManager;

    private Baz baz;
    private Foo foo, fooToo;
    private Bar bar;

    private CritersFactory<Foo, Filter<Foo>> critersFactory;

    /** Creates a new {@code CritersTest} instance. */
    public CritersTest() {}

    /**
     * Initializes the entity manager and prepares test data before each test.
     *
     * @throws CritersException if the Criters factory cannot be initialised
     */
    @BeforeEach
    public void before() throws CritersException {
        //
        // Start JPA provider and get entity manager
        //
        var persistenceUnitName = "criterstest";
        entityManagerFactory = Persistence.createEntityManagerFactory(persistenceUnitName);
        entityManager = entityManagerFactory.createEntityManager();
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

    /** Verifies that {@link Foo} is recognized as a searchable entity. */
    @Test
    public void testIsFilterable() {

        assertThat(critersFactory.isSearchable(Foo.class), is(true));

    }

    /**
     * Verifies that a valid criteria stream returns the expected filtered entity.
     *
     * @throws CritersSearchCriteriaException when search criteria fails
     * @throws InvalidCritersTargetException  when target is invalid
     */
    @Test
    public void testValidCritersStream()
            throws CritersSearchCriteriaException,
                   InvalidCritersTargetException {
        final Filter<Foo> testFilter = new FooPropertyFilter(TEST_VALUE);
        final CriteriaQuery<Foo> criteriaQuery = critersFactory.prepare(testFilter).build().criteria();
        final TypedQuery<Foo> typedQuery = entityManager.createQuery(criteriaQuery);
        final List<Foo> testEntities = typedQuery.getResultStream().toList();
        assertThat(testEntities.size(), is(1));
        assertThat(testEntities.getFirst().getValue(), is(TEST_VALUE));
    }

    /** Verifies that an invalid filter raises {@link se.oyabun.criters.exception.CritersSearchCriteriaException}. */
    @Test
    public void testInvalidCritersStream() {
        assertThrows(CritersSearchCriteriaException.class, () -> {
            final InvalidFooFilter invalidTestEntityCriteria = new InvalidFooFilter();
            invalidTestEntityCriteria.setInvalidValue(0L);
            critersFactory.prepare(invalidTestEntityCriteria).build().criteria();
        });
    }

    /**
     * Verifies that a valid criteria query returns exactly one result.
     *
     * @throws InvalidCritersTargetException  when target is invalid
     * @throws CritersSearchCriteriaException when search criteria fails
     */
    @Test
    public void testValidCritersUniqueCriteria()
            throws InvalidCritersTargetException,
                   CritersSearchCriteriaException {
        final Filter<Foo> testFilter = new FooPropertyFilter(TEST_VALUE);
        final CriteriaQuery<Foo> criteriaQuery = critersFactory.prepare(testFilter).build().criteria();
        final TypedQuery<Foo> testQuery = entityManager.createQuery(criteriaQuery);
        assertNotNull(testQuery.getSingleResult());
    }

    /**
     * Verifies that a relational criteria query returns exactly one result.
     * @throws InvalidCritersTargetException when target is invalid
     * @throws CritersSearchCriteriaException when search criteria fails
     */
    @Test
    public void testRelationCritersUnique()
            throws InvalidCritersTargetException,
                   CritersSearchCriteriaException {
        final Filter<Foo> testFilter = new FooRelationFilter(bar.getId(), BAZ_VALUE);
        final CriteriaQuery<Foo> criteriaQuery = critersFactory.prepare(testFilter).build().criteria();
        final TypedQuery<Foo> testQuery = entityManager.createQuery(criteriaQuery);
        assertNotNull(testQuery.getSingleResult());
    }

    /**
     * Verifies that an IN criteria query returns all entities whose value is in the given collection.
     *
     * @throws InvalidCritersTargetException  when target is invalid
     * @throws CritersSearchCriteriaException when search criteria fails
     */
    @Test
    public void testInCritersStream()
            throws InvalidCritersTargetException,
                   CritersSearchCriteriaException {
        final Filter<Foo> testFilter = new FooInFilter(Arrays.asList(TEST_VALUE, OTHER_VALUE));
        final CriteriaQuery<Foo> criteriaQuery = critersFactory.prepare(testFilter).build().criteria();
        final List<Foo> results = entityManager.createQuery(criteriaQuery).getResultStream().toList();
        assertThat(results.size(), is(2));
    }

    /**
     * Verifies that an IS NULL criteria query returns no entities when no values are null.
     *
     * @throws InvalidCritersTargetException  when target is invalid
     * @throws CritersSearchCriteriaException when search criteria fails
     */
    @Test
    public void testIsNullCritersStream()
            throws InvalidCritersTargetException,
                   CritersSearchCriteriaException {
        final Filter<Foo> testFilter = new FooValueIsNullFilter();
        final CriteriaQuery<Foo> criteriaQuery = critersFactory.prepare(testFilter).build().criteria();
        final List<Foo> results = entityManager.createQuery(criteriaQuery).getResultStream().toList();
        assertThat(results.size(), is(0));
    }

    /**
     * Verifies that an IS NOT NULL criteria query returns all entities with non-null values.
     *
     * @throws InvalidCritersTargetException  when target is invalid
     * @throws CritersSearchCriteriaException when search criteria fails
     */
    @Test
    public void testIsNotNullCritersStream()
            throws InvalidCritersTargetException,
                   CritersSearchCriteriaException {
        final Filter<Foo> testFilter = new FooValueIsNotNullFilter();
        final CriteriaQuery<Foo> criteriaQuery = critersFactory.prepare(testFilter).build().criteria();
        final List<Foo> results = entityManager.createQuery(criteriaQuery).getResultStream().toList();
        assertThat(results.size(), is(2));
    }

    /**
     * Verifies that a LIKE criteria query via a nested relation returns the expected entity.
     *
     * @throws InvalidCritersTargetException  when target is invalid
     * @throws CritersSearchCriteriaException when search criteria fails
     */
    @Test
    public void testLikeRelationCriters()
            throws InvalidCritersTargetException,
                   CritersSearchCriteriaException {
        final Filter<Foo> testFilter = new FooBazLikeFilter("%alu%");
        final CriteriaQuery<Foo> criteriaQuery = critersFactory.prepare(testFilter).build().criteria();
        final List<Foo> results = entityManager.createQuery(criteriaQuery).getResultStream().toList();
        assertThat(results.size(), is(1));
        assertThat(results.getFirst().getValue(), is(TEST_VALUE));
    }

    /** Rolls back the transaction and releases resources after each test. */
    @AfterEach
    public void after() {
        if(entityManager != null) {
            entityManager.getTransaction().rollback();
            entityManager.close();
        }
        if(entityManagerFactory != null) {
            entityManagerFactory.close();
        }
    }

}
