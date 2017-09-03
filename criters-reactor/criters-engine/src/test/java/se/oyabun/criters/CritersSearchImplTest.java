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

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import se.oyabun.criters.criteria.Filter;
import se.oyabun.criters.exception.InvalidCritersFilteringException;
import se.oyabun.criters.extraction.ParameterExtractor;
import se.oyabun.criters.extraction.RelationExtractor;
import se.oyabun.criters.test.data.Foo;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import static org.mockito.Matchers.isA;
import static org.mockito.Mockito.validateMockitoUsage;
import static org.mockito.Mockito.when;


@RunWith(MockitoJUnitRunner.class)
public class CritersSearchImplTest {

    private CritersSearchImpl<Foo, Filter<Foo>> critersSearch;

    @Mock
    private Root<Foo> rootMock;

    @Mock
    private CriteriaBuilder criteriaBuilderMock;

    @Mock
    private CriteriaQuery<Foo> criteriaQueryMock;

    @Mock
    private Filter<Foo> filterMock;

    @Mock
    private ParameterExtractor parameterExtractorMock;

    @Mock
    private RelationExtractor relationExtractorMock;

    @Mock
    private Predicate predicateMock;

    @Before
    public void before() {

        this.critersSearch =
                new CritersSearchImpl<>(
                        criteriaBuilderMock,
                        criteriaQueryMock,
                        rootMock,
                        filterMock);

        this.critersSearch.using(relationExtractorMock);
        this.critersSearch.using(parameterExtractorMock);

    }

    @Test
    public void testRestrictions()
            throws InvalidCritersFilteringException {

        prepareRestrictionExpectations();
        final Predicate restrictions = critersSearch.restrictions();

    }

    @Test
    public void testCriteria()
            throws InvalidCritersFilteringException {

        prepareRestrictionExpectations();
        when(criteriaQueryMock.where(isA(Predicate.class))).thenReturn(criteriaQueryMock);
        final CriteriaQuery<Foo> restrictions = critersSearch.criteria();

    }

    @SuppressWarnings("unchecked")
    public void prepareRestrictionExpectations()
            throws InvalidCritersFilteringException {

        when(parameterExtractorMock.generatePredicate(isA(Filter.class),
                                                      isA(CriteriaBuilder.class),
                                                      isA(Root.class)))
                .thenReturn(predicateMock);

        when(relationExtractorMock.generatePredicate(isA(Filter.class),
                                                     isA(CriteriaBuilder.class),
                                                     isA(Root.class)))
                .thenReturn(predicateMock);


    }

    @After
    public void after() {

        validateMockitoUsage();

    }

}
