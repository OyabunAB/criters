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
import se.oyabun.criters.criteria.Filter;
import se.oyabun.criters.criteria.ParameterFilter;
import se.oyabun.criters.criteria.RelationFilter;
import se.oyabun.criters.exception.InvalidCritersFilteringException;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collection;
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
                     .filter(method -> method.isAnnotationPresent(ParameterFilter.class))
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
                     .filter(method -> method.isAnnotationPresent(RelationFilter.class))
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
    public static <E> void validatePropertyParameter(final Method searchCriteriaMethod,
                                                     final Class<E> type)
            throws InvalidCritersFilteringException {

        if(searchCriteriaMethod.isAnnotationPresent(ParameterFilter.class)) {

            Optional<Method> optionalTargetMethod =
                    Arrays.stream(type.getMethods())
                          .filter(method ->
                                  method.getName()
                                        .equals(searchCriteriaMethod.getName()))
                  .findFirst();

            if(optionalTargetMethod.isPresent()) {

                Method targetMethod = optionalTargetMethod.get();

                if(targetMethod.getReturnType().equals(searchCriteriaMethod.getReturnType())) {

                    if(logger.isTraceEnabled()) {

                        logger.trace("Validated " + searchCriteriaMethod.getName() + ".");

                    }

                } else {

                    throw new InvalidCritersFilteringException(
                            "Return type " + searchCriteriaMethod.getReturnType() +
                            " not matching target.");

                }

            } else {

                throw new InvalidCritersFilteringException(
                        "No matching getter method for " + searchCriteriaMethod.getName() +
                        " on type " + type.getSimpleName() + " found.");

            }

        } else {

            throw new InvalidCritersFilteringException(
                    "Method " + searchCriteriaMethod.getName() +
                    " not indicated as prepare parameter.");
        }


    }

    /**
     * Validate method is corresponding to similar on target type
     *
     * @param searchCriteriaMethod which should match target type
     * @param type on which getter should match
     * @param <E> target type
     * @throws InvalidCritersFilteringException indicating issues with matching
     */
    public static < E > void validatePropertyRelation(final Method searchCriteriaMethod,
                                                      final Class<E> type)
            throws InvalidCritersFilteringException {

        if(searchCriteriaMethod.isAnnotationPresent(RelationFilter.class)) {

            final RelationFilter filterRelation = searchCriteriaMethod.getDeclaredAnnotation(RelationFilter.class);

            final Optional<Method> optionalTargetMethod =
                    Arrays.stream(type.getMethods())
                          .filter(method -> method.getName().equals(
                                  GETTER_PREFIX + StringUtils.capitalize(
                                          filterRelation.sourceParameterName())))
                          .findFirst();

            if(optionalTargetMethod.isPresent()) {

                final String getterMethodName =
                        GETTER_PREFIX + StringUtils.capitalize(
                                filterRelation.relationTargetParameter());

                final Method targetMethod = optionalTargetMethod.get();
                Class targetClass = targetMethod.getReturnType();

                if(filterRelation.relationSourceCollection() &&
                   Iterable.class.isAssignableFrom(targetClass)) {

                    targetClass = filterRelation.relationTargetType();

                } else {

                    throw new InvalidCritersFilteringException(
                            "Indicated collection '" + targetClass.getSimpleName() + "' not valid iterable.");

                }

                final Optional<Method> optionalTargetRelationParameterMethod =
                        Arrays.stream(targetClass.getDeclaredMethods())
                              .filter(method -> method.getName().equals(getterMethodName))
                              .findFirst();

                if(optionalTargetRelationParameterMethod.isPresent()) {

                    final Method targetRelationParameterMethod = optionalTargetRelationParameterMethod.get();

                    if(targetRelationParameterMethod.getReturnType().equals(searchCriteriaMethod.getReturnType())) {

                        if(logger.isTraceEnabled()) {

                            logger.trace("Validated " + searchCriteriaMethod.getName() + ".");

                        }

                    } else {

                        throw new InvalidCritersFilteringException(
                                "Return type " + searchCriteriaMethod.getReturnType() +
                                " not matching target.");

                    }

                } else {

                    throw new InvalidCritersFilteringException(
                            "Failed to identify filtering method '" + getterMethodName +
                            "' on relational object '"+targetClass.getSimpleName()+"'.");

                }

            } else {

                throw new InvalidCritersFilteringException(
                        "No matching getter method for " + searchCriteriaMethod.getName() +
                        " on type " + type.getSimpleName() + " found.");

            }

        } else {

            throw new InvalidCritersFilteringException(
                    "Method " + searchCriteriaMethod.getName() +
                    " not indicated as prepare relation.");

        }


    }

    private FilterUtil() {}

}
