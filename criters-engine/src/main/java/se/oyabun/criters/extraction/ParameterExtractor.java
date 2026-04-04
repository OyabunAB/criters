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
import se.oyabun.criters.criteria.Parameter;
import se.oyabun.criters.exception.InvalidCritersFilteringException;
import se.oyabun.criters.util.FilterUtil;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.From;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

/**
 * Extractor producing predicates for filter parameters
 * implementing {@link se.oyabun.criters.criteria.Restriction} conversion to predicates.
 *
 * @author Daniel Sundberg
 */
public class ParameterExtractor
        implements Extractor {

    /** Creates a new {@code ParameterExtractor}. */
    public ParameterExtractor() {}

    private static final String INVALID_RESTRICTION = "No parameter prepared restriction found for '%s'.";

    private static final String METHOD_INVOCATION = "Failed to access criteria parameter.";

    /**
     * ${@inheritDoc}
     */
    @Override
    public <E, S extends Filter<E>> Optional<Predicate> generatePredicate(final S filter,
                                                                          final CriteriaBuilder criteriaBuilder,
                                                                          final Root<E> root)
            throws InvalidCritersFilteringException {

        final Map<String, Predicate> predicates = new HashMap<>();

        for(final Method method : FilterUtil.parameterMethods(filter)) {

            final Parameter parameter = method.getAnnotation(Parameter.class);

            FilterUtil.validateParameter(method, parameter.name(), filter.getEntityClass(), parameter.restriction());

            final Predicate currentPredicate =
                    ParameterExtractor.produce(filter,
                                               criteriaBuilder,
                                               root,
                                               parameter,
                                               method);

            predicates.compute(parameter.combinate().group(),
                               (key, combinedPredicates) ->
                                       Objects.nonNull(combinedPredicates) ?
                                       FilterUtil.combine(parameter.combinate().combine(),
                                                          criteriaBuilder,
                                                          combinedPredicates,
                                                          currentPredicate) :
                                       currentPredicate);

        }

        return predicates.values().stream().reduce(criteriaBuilder::and);

    }

    /**
     * Produce a predicate from given method and filter values.
     *
     * <p>The restriction type on the {@link Parameter} annotation controls which JPA predicate is
     * built:
     * <ul>
     *   <li>{@link se.oyabun.criters.criteria.Restriction#EQUALS} — field = value</li>
     *   <li>{@link se.oyabun.criters.criteria.Restriction#NOT_EQUALS} — field &lt;&gt; value</li>
     *   <li>{@link se.oyabun.criters.criteria.Restriction#GREATER_THAN} — field &gt; value</li>
     *   <li>{@link se.oyabun.criters.criteria.Restriction#GREATER_THAN_OR_EQUALS} — field &gt;= value</li>
     *   <li>{@link se.oyabun.criters.criteria.Restriction#LESS_THAN} — field &lt; value</li>
     *   <li>{@link se.oyabun.criters.criteria.Restriction#LESS_THAN_OR_EQUALS} — field &lt;= value</li>
     *   <li>{@link se.oyabun.criters.criteria.Restriction#LIKE} — field LIKE pattern (String fields only)</li>
     *   <li>{@link se.oyabun.criters.criteria.Restriction#IS_NULL} — field IS NULL (getter return value ignored)</li>
     *   <li>{@link se.oyabun.criters.criteria.Restriction#IS_NOT_NULL} — field IS NOT NULL (getter return value ignored)</li>
     *   <li>{@link se.oyabun.criters.criteria.Restriction#IN} — field IN collection (getter must return {@link Collection})</li>
     * </ul>
     *
     * @param filter to produce predicates for
     * @param criteriaBuilder to create predicates and combinations with
     * @param from root of parameter
     * @param parameter annotation instance to parse predicate restriction from
     * @param method containing values when invoked
     * @param <S> type of filter
     * @return annotation configured predicate
     * @throws InvalidCritersFilteringException when not matching any restriction or failing to invoke method
     */
    static <S> Predicate produce(final S filter,
                                 final CriteriaBuilder criteriaBuilder,
                                 final From from,
                                 final Parameter parameter,
                                 final Method method)

            throws InvalidCritersFilteringException {

        try {

            switch (parameter.restriction()) {

                case EQUALS:
                    return criteriaBuilder.equal(from.get(parameter.name()),
                                                 method.invoke(filter));

                case NOT_EQUALS:
                    return criteriaBuilder.notEqual(from.get(parameter.name()),
                                                    method.invoke(filter));

                case GREATER_THAN_OR_EQUALS:
                    return criteriaBuilder.greaterThanOrEqualTo(from.get(parameter.name()),
                                                                (Comparable) method.invoke(filter));

                case GREATER_THAN:
                    return criteriaBuilder.greaterThan(from.get(parameter.name()),
                                                       (Comparable) method.invoke(filter));

                case LESS_THAN_OR_EQUALS:
                    return criteriaBuilder.lessThanOrEqualTo(from.get(parameter.name()),
                                                             (Comparable) method.invoke(filter));

                case LESS_THAN:
                    return criteriaBuilder.lessThan(from.get(parameter.name()),
                                                    (Comparable) method.invoke(filter));

                case LIKE:
                    return criteriaBuilder.like(from.get(parameter.name()),
                                                (String) method.invoke(filter));

                case IS_NULL:
                    return criteriaBuilder.isNull(from.get(parameter.name()));

                case IS_NOT_NULL:
                    return criteriaBuilder.isNotNull(from.get(parameter.name()));

                case IN:
                    return from.get(parameter.name()).in((Collection<?>) method.invoke(filter));

            }

            throw new InvalidCritersFilteringException(String.format(INVALID_RESTRICTION, method.getName()));

        } catch (IllegalAccessException | InvocationTargetException e) {

            throw new InvalidCritersFilteringException(METHOD_INVOCATION, e);

        }

    }

}
