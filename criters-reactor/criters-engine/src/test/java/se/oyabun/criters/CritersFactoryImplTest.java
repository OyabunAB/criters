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
package se.oyabun.criters;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import se.oyabun.criters.criteria.Filter;
import se.oyabun.criters.criteria.Parameter;
import se.oyabun.criters.criteria.Restriction;
import se.oyabun.criters.exception.InvalidCritersTargetException;
import se.oyabun.criters.test.data.Foo;

import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import jakarta.persistence.metamodel.EntityType;
import jakarta.persistence.metamodel.Metamodel;
import java.util.Collections;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.when;

/**
 * Criters factory verjfication tests
 *
 * @author Daniel Sundberg
 */
@ExtendWith(MockitoExtension.class)
public class CritersFactoryImplTest {

    private static final Integer EQUALS_VALUE = 1337;
    private static final Integer NOT_EQUALS_VALUE = -1337;

    private CritersFactoryImpl<Foo, Filter<Foo>> critersFactory;

    @Mock
    private EntityManager entityManagerMock;

    @Mock
    private Metamodel metamodelMock;

    @Mock
    private EntityType<Foo> entityTypeMock;

    @Mock
    private Root<Foo> rootMock;

    @Mock
    private CriteriaBuilder criteriaBuilderMock;

    @Mock
    private CriteriaQuery<Foo> criteriaQueryMock;

    private Filter<Foo> filterStubb = new Filter<Foo>() {

        @Parameter(restriction = Restriction.EQUALS,
                   name = "value")
        public Integer equalsValue() {
            return EQUALS_VALUE;
        }

        @Parameter(restriction = Restriction.NOT_EQUALS,
                   name = "value")
        public Integer notEquals() {
            return NOT_EQUALS_VALUE;
        }

    };

    @BeforeEach
    public void before() {

        critersFactory = new CritersFactoryImpl<>();

    }

    @Test
    public void testUseEntityManager()
            throws InvalidCritersTargetException {

        expectEntityManager();
        assertThat(critersFactory.use(entityManagerMock).isSearchable(Foo.class), is(true));


    }

    @Test
    public void testUseComponents()
            throws InvalidCritersTargetException {

        expectComponents();
        assertThat(critersFactory.use(rootMock,
                                      criteriaQueryMock,
                                      criteriaBuilderMock).isSearchable(Foo.class), is(true));

    }

    @Test
    public void testPrepare()
            throws InvalidCritersTargetException {

        expectEntityManager();
        critersFactory.use(entityManagerMock).prepare(filterStubb);

    }

    public void testCriteria() {

    }

    private void expectEntityManager() {

        when(entityManagerMock.getMetamodel()).thenReturn(metamodelMock);
        when(metamodelMock.getEntities()).thenReturn(Collections.singleton(entityTypeMock));
        when(entityTypeMock.getJavaType()).thenReturn(Foo.class);

    }

    private void expectComponents() {

        when(rootMock.getModel()).thenReturn(entityTypeMock);
        when(entityTypeMock.getJavaType()).thenReturn(Foo.class);

    }

}
