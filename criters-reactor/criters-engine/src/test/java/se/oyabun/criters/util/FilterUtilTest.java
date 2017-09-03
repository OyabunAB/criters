package se.oyabun.criters.util;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;
import se.oyabun.criters.criteria.Filter;
import se.oyabun.criters.criteria.ParameterFilter;
import se.oyabun.criters.criteria.RelationFilter;
import se.oyabun.criters.test.data.Bar;
import se.oyabun.criters.test.data.Foo;

import java.lang.reflect.Method;
import java.util.Collection;
import java.util.stream.Collectors;

import static org.hamcrest.CoreMatchers.hasItem;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.MatcherAssert.assertThat;

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

        @ParameterFilter(restriction = ParameterFilter.Restriction.EQUALS,
                         sourceParameterName = SOURCE_PARAMETER)
        public String getAnnotatedParameterMethod() {
            return VALUE;
        }

        @RelationFilter(restriction = RelationFilter.Restriction.IN,
                        sourceParameterName = SOURCE_PARAMETER,
                        relationTargetParameter = TARGET_PARAMETER,
                        relationTargetType = Bar.class)
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
