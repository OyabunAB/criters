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

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import se.oyabun.criters.test.data.Bar;
import se.oyabun.criters.test.data.Baz;
import se.oyabun.criters.test.data.Foo;
import se.oyabun.criters.test.filter.FooPropertyFilter;
import se.oyabun.criters.test.filter.FooRelationFilter;
import se.oyabun.criters.test.spring.BarRepository;
import se.oyabun.criters.test.spring.BazRepository;
import se.oyabun.criters.test.spring.FooRepository;
import se.oyabun.criters.test.spring.FooSpecification;
import se.oyabun.criters.test.spring.SpringTestConfiguration;

import javax.transaction.Transactional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;

/**
 * Test verification of Criters Spring Data JPA compatibility
 *
 * @author Daniel Sundberg
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = SpringTestConfiguration.class)
public class FooRepositorySearchTest {

    private static long NON_EXISTING_BAR_ID = -123L;
    private static int FOO_VALUE = 1337;
    private static int NON_EXISTING_FOO_VALUE = -321;
    private static String BAZ_VALUE = "value";

    @Autowired
    private FooRepository fooRepository;

    @Autowired
    private BarRepository barRepository;

    @Autowired
    private BazRepository bazRepository;

    private Foo foo, fooToo;
    private Bar bar;
    private Baz baz;

    @Before
    public void before() {

        foo = new Foo();
        foo.setValue(FOO_VALUE);
        fooRepository.save(foo);

        fooToo = new Foo();
        fooToo.setValue(FOO_VALUE);
        fooRepository.save(fooToo);

        bar = new Bar();
        bar.setFoo(foo);
        foo.getBars().add(bar);
        barRepository.save(bar);

        baz = new Baz();
        baz.setBar(bar);
        baz.setValue(BAZ_VALUE);
        bar.setBaz(baz);
        bazRepository.save(baz);

    }

    @Test
    @Transactional
    public void testRelationalFilteringSpecification() {

        FooRelationFilter noResultsExpectedFilter = new FooRelationFilter(NON_EXISTING_BAR_ID, BAZ_VALUE);
        FooRelationFilter resultExpectedFilter = new FooRelationFilter(bar.getId(), baz.getValue());

        assertThat(fooRepository.findAll(new FooSpecification(noResultsExpectedFilter)), is(empty()));
        assertThat(fooRepository.findAll(new FooSpecification(resultExpectedFilter)), is(not(empty())));
        assertThat(fooRepository.findAll(new FooSpecification(resultExpectedFilter)), contains(foo));

    }

    @Test
    @Transactional
    public void testPropertyFilteringSpecification() {

        FooPropertyFilter noResultsExpectedFilter = new FooPropertyFilter(NON_EXISTING_FOO_VALUE);
        FooPropertyFilter resultExpectedFilter = new FooPropertyFilter(FOO_VALUE);

        assertThat(fooRepository.findAll(new FooSpecification(noResultsExpectedFilter)), is(empty()));
        assertThat(fooRepository.findAll(new FooSpecification(resultExpectedFilter)), is(not(empty())));
        assertThat(fooRepository.findAll(new FooSpecification(resultExpectedFilter)), contains(foo, fooToo));

    }

}
