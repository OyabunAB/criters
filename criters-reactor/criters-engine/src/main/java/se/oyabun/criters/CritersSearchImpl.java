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
import se.oyabun.criters.criteria.ParameterFilter;
import se.oyabun.criters.criteria.RelationFilter;
import se.oyabun.criters.exception.InvalidCritersFilteringException;
import se.oyabun.criters.util.FilterUtil;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Objects;
import java.util.Optional;

public class CritersSearchImpl<E, S extends Filter<E>>
        implements CritersSearch {

    private final S searchCriteria;
    private final CriteriaBuilder criteriaBuilder;
    private final CriteriaQuery<?> criteriaQuery;
    private final Root<E> root;

    private Predicate finalRestrictions;

    CritersSearchImpl(final CriteriaBuilder criteriaBuilder,
                      final CriteriaQuery<?> criteriaQuery,
                      final Root<E> root,
                      final S searchCriteria) {

        this.criteriaBuilder = criteriaBuilder;
        this.criteriaQuery = criteriaQuery;
        this.root = root;
        this.searchCriteria = searchCriteria;
    }

    /**
     * ${@inheritDoc}
     */
    @Override
    public CriteriaQuery<?> criteria()
            throws InvalidCritersFilteringException {

        return criteriaQuery.where(restrictions());

    }

    /**
     * ${@inheritDoc}
     */
    @Override
    public Predicate restrictions()
            throws InvalidCritersFilteringException {

        final Predicate propertyRestrictions = parameterRestriction(searchCriteria, criteriaBuilder, root);
        final Predicate relationRestrictions = relationRestrictions(searchCriteria, criteriaBuilder, root);

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

    Predicate parameterRestriction(final S searchCriteria,
                                   final CriteriaBuilder criteriaBuilder,
                                   final Root<E> root)
            throws InvalidCritersFilteringException {

        Predicate combinedPredicates = null;

        for(final Method method : FilterUtil.parameterMethods(searchCriteria)) {

            FilterUtil.validatePropertyParameter(method, searchCriteria.getEntityClass());

            final ParameterFilter parameterFilter = method.getAnnotation(ParameterFilter.class);

            final Predicate currentPredicate;

            switch (parameterFilter.restriction()) {

                case EQUALS: {

                    try {

                         currentPredicate =
                                criteriaBuilder.equal(
                                        root.get(parameterFilter.sourceParameter()),
                                        method.invoke(searchCriteria));

                    } catch (IllegalAccessException | InvocationTargetException e) {

                        throw new InvalidCritersFilteringException("Failed to access criteria parameter.", e);

                    }

                    break;

                }

                case NOT_EQUALS: {


                    try {

                        currentPredicate =
                                criteriaBuilder.notEqual(
                                        root.get(parameterFilter.sourceParameter()),
                                        method.invoke(searchCriteria));

                    } catch (IllegalAccessException | InvocationTargetException e) {

                        throw new InvalidCritersFilteringException("Failed to access criteria parameter.", e);

                    }

                    break;

                }

                default: {

                    throw new InvalidCritersFilteringException("No parameter prepared restriction found for '" +
                                                               method.getName() +
                                                               "'.");
                }

            }

            combinedPredicates =
                    Objects.nonNull(combinedPredicates) ?
                        criteriaBuilder.and(combinedPredicates, currentPredicate) :
                        currentPredicate;

        }

        return combinedPredicates;

    }

    Predicate relationRestrictions(final S searchCriteria,
                                   final CriteriaBuilder criteriaBuilder,
                                   final Root<E> root)
            throws InvalidCritersFilteringException {

        Predicate combinedPredicates = null;

        for(final Method method : FilterUtil.relationalMethods(searchCriteria)) {

            FilterUtil.validatePropertyRelation(method, searchCriteria.getEntityClass());

            final RelationFilter filterRelation = method.getAnnotation(RelationFilter.class);

            final Predicate currentPredicate;

            switch (filterRelation.restriction()) {

                case IN: {

                    try {

                        currentPredicate =
                                root.join(filterRelation.relationSourceParameter())
                                    .get(filterRelation.relationTargetParameter())
                                    .in(method.invoke(searchCriteria));

                    } catch (IllegalAccessException | InvocationTargetException e) {

                        throw new InvalidCritersFilteringException("Failed to access prepare parameter.", e);

                    }

                    break;

                }

                case NOT_IN: {

                    try {

                        currentPredicate =
                            criteriaBuilder.not(
                                    root.join(filterRelation.relationSourceParameter())
                                        .get(filterRelation.relationTargetParameter())
                                        .in(method.invoke(searchCriteria)));

                    } catch (IllegalAccessException | InvocationTargetException e) {

                        throw new InvalidCritersFilteringException("Failed to access prepare parameter.", e);

                    }

                    break;

                }

                default: {

                    throw new InvalidCritersFilteringException("No parameter prepare restriction found for '" +
                                                               method.getName() + "'.");

                }

            }

            combinedPredicates =
                    Objects.nonNull(combinedPredicates) ?
                    criteriaBuilder.and(combinedPredicates, currentPredicate) :
                    currentPredicate;

        }

        return combinedPredicates;

    }

}
