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

import se.oyabun.criters.criteria.Combination;
import se.oyabun.criters.criteria.Filter;
import se.oyabun.criters.exception.InvalidCritersFilteringException;
import se.oyabun.criters.extraction.Extractor;
import se.oyabun.criters.extraction.ParameterExtractor;
import se.oyabun.criters.extraction.RelationExtractor;
import se.oyabun.criters.util.FilterUtil;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.Arrays;
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

    private Iterable<Extractor> extractors =
            Arrays.asList(new ParameterExtractor(),
                          new RelationExtractor());

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

    /**
     * Configure current instance extractors to be used for extracting predicates
     *
     * @param extractors to be used for restrictions
     */
    void using(final Iterable<Extractor> extractors) {

        this.extractors = extractors;

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

        for(final Extractor extractor : extractors) {

            extractor.generatePredicate(searchCriteria, criteriaBuilder, root)
                     .ifPresent(this::addPredicate);

        }

        return Optional.ofNullable(finalRestrictions)
                       .orElseThrow(() -> new IllegalStateException("No predicates generated."));

    }

    /**
     * Combine all predicates we have, starting with non null predicate
     *
     * @param predicate to be combined with final restrictions
     */
    private void addPredicate(final Predicate predicate) {

        finalRestrictions = Objects.isNull(finalRestrictions) ?
                            predicate :
                            FilterUtil.combine(Combination.Combine.AND,
                                               criteriaBuilder,
                                               finalRestrictions,
                                               predicate);

    }

}
