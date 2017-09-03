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

import se.oyabun.criters.criteria.Filter;
import se.oyabun.criters.exception.InvalidCritersFilteringException;
import se.oyabun.criters.extraction.ParameterExtractor;
import se.oyabun.criters.extraction.RelationExtractor;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.Objects;
import java.util.Optional;

/**
 * Basic Criters search implementation.
 *
 * @param <E> type of entity
 * @param <S> type of filter
 * @author Daniel Sundberg
 */
public class CritersSearchImpl<E, S extends Filter<E>>
        implements CritersSearch<E> {

    private final S searchCriteria;
    private final CriteriaBuilder criteriaBuilder;
    private final CriteriaQuery<E> criteriaQuery;
    private final Root<E> root;

    private ParameterExtractor parameterExtractor = new ParameterExtractor();
    private RelationExtractor relationExtractor = new RelationExtractor();

    private Predicate finalRestrictions;

    CritersSearchImpl(final CriteriaBuilder criteriaBuilder,
                      final CriteriaQuery<E> criteriaQuery,
                      final Root<E> root,
                      final S searchCriteria) {

        this.criteriaBuilder = criteriaBuilder;
        this.criteriaQuery = criteriaQuery;
        this.root = root;
        this.searchCriteria = searchCriteria;
    }

    void using(final ParameterExtractor parameterExtractor) {

        this.parameterExtractor = parameterExtractor;

    }

    void using(final RelationExtractor relationExtractor) {

        this.relationExtractor = relationExtractor;

    }

    /**
     * ${@inheritDoc}
     */
    @Override
    public CriteriaQuery<E> criteria()
            throws InvalidCritersFilteringException {

        return criteriaQuery.where(restrictions());

    }

    /**
     * ${@inheritDoc}
     */
    @Override
    public Predicate restrictions()
            throws InvalidCritersFilteringException {

        final Predicate propertyRestrictions = parameterExtractor.generatePredicate(searchCriteria,
                                                                                    criteriaBuilder,
                                                                                    root);

        final Predicate relationRestrictions = relationExtractor.generatePredicate(searchCriteria,
                                                                                   criteriaBuilder,
                                                                                   root);

        finalRestrictions =
                Objects.nonNull(propertyRestrictions) &&
                Objects.nonNull(relationRestrictions) ?
                    criteriaBuilder.and(propertyRestrictions, relationRestrictions) :
                    Objects.nonNull(propertyRestrictions) ?
                        propertyRestrictions :
                        Optional.ofNullable(relationRestrictions)
                                .orElseThrow(IllegalStateException::new);

        return finalRestrictions;

    }

}
