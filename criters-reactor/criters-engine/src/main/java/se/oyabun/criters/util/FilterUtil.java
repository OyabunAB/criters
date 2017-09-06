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

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import se.oyabun.criters.criteria.Combination;
import se.oyabun.criters.criteria.Filter;
import se.oyabun.criters.criteria.Parameter;
import se.oyabun.criters.criteria.Relation;
import se.oyabun.criters.criteria.Relations;
import se.oyabun.criters.exception.InvalidCritersFilteringException;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Predicate;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.Collection;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Filtering util containing externalized class handling methods
 *
 * @author Daniel Sundberg
 */
public class FilterUtil {

    private static final Logger logger = LoggerFactory.getLogger(FilterUtil.class);

    private static final String GETTER_PREFIX = "get";

    private static final String VALIDATION_TRACE_MESSAGE = "Validated '{}' method on '{}'.";

    private static final String ILLEGAL_COMBINATION = "Illegal combination.";

    private static final String MISMATCHING_GETTERS = "No matching getter method '%s.%s()' for filter." ;

    private static final String NON_PREPARED_METHOD = "Method '%s' not indicated as prepared.";

    private static final String MISMATCHING_RETURN_TYPE = "Return type '%s' not matching target.";

    private static final String INVALID_ITERABLE = "Indicated collection '%s' not valid iterable.";

    /**
     * Introspect criteria for get methods with parameter annotations.
     *
     * @param searchCriteria to introspect
     * @param <E> type of target class
     * @param <S> type of prepare criteria
     * @return collection of get methods for parameters
     */
    public static <E, S extends Filter<E>> Collection<Method> parameterMethods(final S searchCriteria) {

        return Arrays.stream(searchCriteria.getClass().getDeclaredMethods())
                     .filter(method -> method.isAnnotationPresent(Parameter.class))
                     .filter(method -> method.getParameterCount() == 0)
                     .collect(Collectors.toList());

    }

    /**
     * Introspect criteria for get methods with relation annotations.
     *
     * @param searchCriteria to introspect
     * @param <E> type of target class
     * @param <S> type of prepare criteria
     * @return collection of get methods for parameters
     */
    public static <E, S extends Filter<E>> Collection<Method> relationalMethods(final S searchCriteria) {

        return Arrays.stream(searchCriteria.getClass().getDeclaredMethods())
                     .filter(method -> method.isAnnotationPresent(Relations.class))
                     .filter(method -> method.getParameterCount() == 0)
                     .collect(Collectors.toList());

    }

    /**
     * Validate method is corresponding to target type method
     *
     * @param searchCriteriaMethod which should match target type
     * @param type on which getter should match
     * @param <E> target type
     * @throws InvalidCritersFilteringException indicating issues with matching
     */
    public static <E> void validateParameter(final Method searchCriteriaMethod,
                                             final String parameterName,
                                             final Class<E> type)
            throws InvalidCritersFilteringException {

        if(searchCriteriaMethod.isAnnotationPresent(Parameter.class)) {

            validateRelationalParameter(getterOf(parameterName),
                                        searchCriteriaMethod.getReturnType(),
                                        type);

        } else {

            throw new InvalidCritersFilteringException(
                    String.format(NON_PREPARED_METHOD,
                                  searchCriteriaMethod.getName()));

        }

    }

    /**
     * Validate method is corresponding to similar on target type
     *
     * @param method which should match target type
     * @param type on which getter should match
     * @throws InvalidCritersFilteringException indicating issues with matching
     */
    public static void validateRelations(final Method method,
                                         final Class<?> type)
            throws InvalidCritersFilteringException {

        //
        // Assert that there exists an relations annotation
        //
        if(method.isAnnotationPresent(Relations.class)) {

            final Relations relations = method.getDeclaredAnnotation(Relations.class);

            Class<?> currentType = null;

            //
            // Iterate all relation on parent annotation, each iteration representing a join on
            // the previous starting with the initial type as root.
            //
            for(final Relation relation : relations.value()) {

                final String expectedMethodName = getterOf(relation.name());

                final Optional<Method> optionalTargetMethod =
                        Arrays.stream(Objects.nonNull(currentType) ?
                                      currentType.getMethods() :
                                      type.getMethods())
                              .filter(typeMethod -> typeMethod.getName().equals(getterOf(relation.name())))
                          .findFirst();

                if(optionalTargetMethod.isPresent()) {

                    final Method targetMethod = optionalTargetMethod.get();

                    //
                    // Validate getters for each parameter
                    //
                    for(final Parameter parameter : relation.parameters()) {

                        Class<?> targetType = targetMethod.getReturnType();

                        if(Iterable.class.isAssignableFrom(targetType)) {

                            Type returnType = targetMethod.getGenericReturnType();

                            if (returnType instanceof ParameterizedType) {

                                final ParameterizedType paramType = (ParameterizedType) returnType;
                                targetType = (Class<?>) paramType.getActualTypeArguments()[ 0 ];

                            }

                        }

                        validateRelationalParameter(getterOf(parameter.name()),
                                                    method.getReturnType(),
                                                    targetType);

                    }

                    currentType = extractTargetType(relation, targetMethod);

                } else {

                    throw new InvalidCritersFilteringException(
                            String.format(MISMATCHING_GETTERS,
                                          type.getSimpleName(),
                                          expectedMethodName));

                }

            }

        } else {

            throw new InvalidCritersFilteringException(
                    String.format(NON_PREPARED_METHOD,
                                  method.getName()));

        }


    }

    /**
     * Validating relational parameter expectations on given type
     *
     * @param expectedMethodName verified against method on given type
     * @param expectedReturnType verified against method on given type
     * @param type to verify methods on
     * @param <E> inferred type of class
     * @throws InvalidCritersFilteringException if validation fails
     */
    private static <E> void validateRelationalParameter(final String expectedMethodName,
                                                        final Class<?> expectedReturnType,
                                                        final Class<E> type)
            throws InvalidCritersFilteringException {

        Optional<Method> optionalTargetMethod =
                Arrays.stream(type.getMethods())
                      .filter(method -> method.getName().equals(expectedMethodName))
                      .filter(method -> method.getReturnType().equals(expectedReturnType))
                      .findFirst();

        if(optionalTargetMethod.isPresent()) {

            final Method targetMethod = optionalTargetMethod.get();

            if(targetMethod.getReturnType().equals(expectedReturnType)) {

                if(logger.isTraceEnabled()) {

                    logger.trace(VALIDATION_TRACE_MESSAGE,
                                 expectedMethodName,
                                 type.getSimpleName());

                }

            } else {

                throw new InvalidCritersFilteringException(
                        String.format(MISMATCHING_RETURN_TYPE,
                                      expectedReturnType.getSimpleName()));

            }

        } else {

            throw new InvalidCritersFilteringException(
                    String.format(MISMATCHING_GETTERS,
                                  type.getSimpleName(),
                                  expectedMethodName));

        }

    }

    /**
     * Extract target class type
     *
     * @param relation of method
     * @param targetMethod to be called
     * @return class type returned from method call
     * @throws InvalidCritersFilteringException if relations iterable indication is wrong
     */
    private static Class<?> extractTargetType(final Relation relation,
                                              final Method targetMethod)
            throws InvalidCritersFilteringException {

        Class<?> currentType = targetMethod.getReturnType();

        boolean indicationFail = false;

        if(relation.iterable()) {

            if(Iterable.class.isAssignableFrom(currentType)) {

                Type returnType = targetMethod.getGenericReturnType();

                if (returnType instanceof ParameterizedType) {

                    final ParameterizedType paramType = (ParameterizedType) returnType;
                    currentType = (Class<?>) paramType.getActualTypeArguments()[ 0 ];

                }

            } else {

                indicationFail = true;

            }

        } else {

            if(Iterable.class.isAssignableFrom(currentType)) {

                indicationFail = true;

            }

        }

        if(indicationFail) {

            throw new InvalidCritersFilteringException(
                    String.format(INVALID_ITERABLE,
                                  currentType.getSimpleName()));

        }

        return currentType;

    }

    /**
     * Combine predicates with given criteria builder based on combination logic.
     *
     * @param combination of parameter
     * @param criteriaBuilder to combine with
     * @param firstPredicate to combine
     * @param secondPredicate to combine
     * @return combinated predicates
     */
    public static Predicate combine(final Combination.Combine combination,
                                    final CriteriaBuilder criteriaBuilder,
                                    final Predicate firstPredicate,
                                    final Predicate secondPredicate) {

        switch (combination) {

            case OR: return criteriaBuilder.or(firstPredicate, secondPredicate);

            case AND: return criteriaBuilder.and(firstPredicate, secondPredicate);

        }

        throw new IllegalStateException(ILLEGAL_COMBINATION);

    }

    /**
     * Implements getter convention.
     * Parameters are expected to follow the getParameterName() convention.
     *
     * @param propertName to produce convention get method name for
     * @return conventional getter method name for property
     */
    private static String getterOf(final String propertName) {

        return GETTER_PREFIX + StringUtils.capitalize(propertName);

    }

    private FilterUtil() {}

}
