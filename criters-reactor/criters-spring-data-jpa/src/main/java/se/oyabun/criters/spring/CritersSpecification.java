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
package se.oyabun.criters.spring;

import org.springframework.data.jpa.domain.Specification;
import se.oyabun.criters.Criters;
import se.oyabun.criters.criteria.Filter;
import se.oyabun.criters.exception.InvalidCritersFilteringException;
import se.oyabun.criters.exception.InvalidCritersTargetException;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.lang.reflect.ParameterizedType;

/**
 * Connects the Criters Criteria Automation Engine to Spring data by
 * implementing the {@link Specification} interface, thus getting injected
 * into {@link org.springframework.data.jpa.repository.JpaSpecificationExecutor} implementations.
 *
 * @param <E> type of entity specification is intended to produce
 * @param <S> type of filter used for criters criteria generation
 * @author Daniel Sundberg
 */
public abstract class CritersSpecification<E, S extends Filter<E>>
       implements Specification<E> {

    private static final String FAILED_PREDICATE_PREPARATION =
            "Failed to prepare predicate for search filter.";

    private static final String INVALID_CRITERIA_TYPE =
            "CriteriaQuery type '%s' must correspond to Criters search filter type '%s'.";

    private final S searchFilter;

    public CritersSpecification(final S searchFilter) {

        this.searchFilter = searchFilter;

    }

    /**
     * ${@inheritDoc}
     */
    @Override
    @SuppressWarnings("unchecked")
    public Predicate toPredicate(final Root<E> root,
                                 final CriteriaQuery<?> criteriaQuery,
                                 final CriteriaBuilder criteriaBuilder) {

        try {

            final String criteriaQueryType =
                    ((Class<E>)(((ParameterizedType) getClass().getGenericSuperclass())
                                        .getActualTypeArguments()[0])).getName();

            final String searchFilterType = searchFilter.getEntityClass().getName();

            if(!criteriaQueryType.equals(searchFilterType)) {

                throw new InvalidCritersFilteringException(
                        String.format(INVALID_CRITERIA_TYPE, criteriaQueryType, searchFilterType));

            }

            //
            // Unsafe cast verified by type parameter check on criteria query above
            //
            return Criters. <E, Filter<E>> factory()
                          .use(root, (CriteriaQuery<E>) criteriaQuery, criteriaBuilder)
                          .prepare(searchFilter)
                          .build().restrictions();

        } catch (InvalidCritersTargetException | InvalidCritersFilteringException e) {

            throw new IllegalStateException(FAILED_PREDICATE_PREPARATION, e);

        }

    }

}
