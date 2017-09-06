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
package se.oyabun.criters.extraction;

import se.oyabun.criters.criteria.Filter;
import se.oyabun.criters.exception.InvalidCritersFilteringException;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.Optional;

/**
 * Criters extractors are responsible for producing predicates later combined and set on
 * criteria query as 'where' parameter.
 *
 * @author Daniel Sundberg
 */
public interface Extractor {

    /**
     * Generate restriction predicate
     *
     * @param filter to use for predicate generation
     * @param criteriaBuilder to use for predicate generation
     * @param root to use for predicate generation
     * @param <E> type of entity
     * @param <S> type of search filter
     * @return combined parameter predicate
     * @throws InvalidCritersFilteringException if predicate generation fails
     * @author Daniel Sundberg
     */
    <E, S extends Filter<E>> Optional<Predicate> generatePredicate(final S filter,
                                                                   final CriteriaBuilder criteriaBuilder,
                                                                   final Root<E> root)
            throws InvalidCritersFilteringException;

}
