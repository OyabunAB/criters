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
package se.oyabun.criters.util;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;
import se.oyabun.criters.criteria.Filter;
import se.oyabun.criters.criteria.Parameter;
import se.oyabun.criters.criteria.Relation;
import se.oyabun.criters.criteria.Relations;
import se.oyabun.criters.criteria.Restriction;
import se.oyabun.criters.test.data.Foo;

import java.lang.reflect.Method;
import java.util.Collection;
import java.util.stream.Collectors;

import static org.hamcrest.CoreMatchers.hasItem;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * Filter utility functionality verification
 *
 * @author Daniel Sundberg
 */
@RunWith(MockitoJUnitRunner.class)
public class FilterUtilTest {

    private static final String SOURCE_PARAMETER = "SOURCE_PARAMETER";
    private static final String TARGET_PARAMETER = "TARGET_PARAMETER";
    private static final String VALUE = "VALUE";

    private TestFilter testFilter;

    private Method annotatedParameterMethod, annotatedRelationalMethod;

    @Before
    public void before()
            throws NoSuchMethodException {

        testFilter = new TestFilter();

        annotatedParameterMethod = testFilter.getClass().getMethod("getAnnotatedParameterMethod");
        annotatedRelationalMethod = testFilter.getClass().getMethod("getAnnotatedRelationalMethod");

    }

    @Test
    public void testFindParameterMethods() {

        final Collection<String> parameterFilterAnnotatedMethods =
                FilterUtil.parameterMethods(testFilter).stream().map(Method::getName)
                          .collect(Collectors.toList());

        assertThat(parameterFilterAnnotatedMethods, hasItem(annotatedParameterMethod.getName()));
        assertThat(parameterFilterAnnotatedMethods, not(hasItem(annotatedRelationalMethod.getName())));

    }

    @Test
    public void testFindRelationalMethods() {

        final Collection<String> relationFilterAnnotatedMethods =
                FilterUtil.relationalMethods(testFilter).stream().map(Method::getName)
                          .collect(Collectors.toList());

        assertThat(relationFilterAnnotatedMethods, not(hasItem(annotatedParameterMethod.getName())));
        assertThat(relationFilterAnnotatedMethods, hasItem(annotatedRelationalMethod.getName()));

    }

    public class TestFilter
           extends Filter<Foo> {

        @Parameter(restriction = Restriction.EQUALS,
                   name = SOURCE_PARAMETER)
        public String getAnnotatedParameterMethod() {
            return VALUE;
        }

        @Relations({
                @Relation(name = SOURCE_PARAMETER,
                          iterable = true,
                          parameters = {
                            @Parameter(name = TARGET_PARAMETER,
                                       restriction = Restriction.EQUALS)
                          })
        })
        public String getAnnotatedRelationalMethod() {
            return VALUE;
        }

        public String getNoAnnotationMethod() {
            return VALUE;
        }

        private void noReturnValueMethod() {

        }

        protected String getProtectedMethod() {
            return VALUE;
        }

        String getPackagePRivateMethod() {
            return VALUE;
        }

        public void setAnnotatedParameterMethod(final String value) {}

    }

}
