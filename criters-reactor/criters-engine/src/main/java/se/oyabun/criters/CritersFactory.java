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
import se.oyabun.criters.exception.InvalidCritersTargetException;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

public interface CritersFactory<E, S extends Filter<E>> {

    /**
     * Prepare factory instance to use given entity manager
     *
     * @param entityManager to use for preparing search
     * @throws InvalidCritersTargetException if target not found on entity manager
     * @return configured factory
     */
    CritersFactory<E, S> use(final EntityManager entityManager)
            throws InvalidCritersTargetException;

    /**
     * Prepare factory instance to use given JPA parameters
     *
     * @param root to use while preparing the search
     * @param criteriaQuery to use while preparing the search
     * @param criteriaBuilder to use while preparing the search
     * @throws InvalidCritersTargetException if target not found on entity manager
     * @return configured factory
     */
    CritersFactory<E, S> use(final Root<?> root,
                             final CriteriaQuery<?> criteriaQuery,
                             final CriteriaBuilder criteriaBuilder)
            throws InvalidCritersTargetException;

    /**
     * Hunt for a specific criters based on given criteria
     * @param searchFilter to be used for search
     * @throws InvalidCritersTargetException if target not found on entity manager
     * @return generated criters
     */
    CritersFactory<E, S>  prepare(final S searchFilter)
            throws InvalidCritersTargetException;

    /**
     * Produce a critters search based on configured search filter and entity manager
     * @return new critters search
     */
    CritersSearch build();

    /**
     * Can this bounty hunter find criters for given type
     * @param targetClass type that feeds criters
     * @return true if hunter can find criters of given type
     */
    boolean isSearchable(final Class<E> targetClass);

}
