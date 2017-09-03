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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import se.oyabun.criters.criteria.Filter;
import se.oyabun.criters.exception.InvalidCritersTargetException;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.Objects;
import java.util.Optional;

/**
 * Criters basic search factory implementation.
 *
 * @param <E> type of entity
 * @param <S> type of filter
 * @author Daniel Sundberg
 */
public class CritersFactoryImpl<E, S extends Filter<E>>
            implements CritersFactory<E, S> {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    static final String INVALID_TARGET = "Class is an invalid criters target.";
    static final String NULL_PARAMETERS = "Null parameters is not allowed.";
    static final String ROOT_BUILDER_OR_QUERY_MISSING = "Can't determine search root, builder or query.";
    static final String SEARCH_FILTER_MISSING = "No search filter configured.";
    static final String ENTITY_MANAGER_OR_ROOT_REQUIRED = "Entity manager or root required to validate class.";

    private EntityManager entityManager;
    private Root<E> root;
    private CriteriaQuery<E> criteriaQuery;
    private CriteriaBuilder criteriaBuilder;
    private S searchFilter;

    CritersFactoryImpl() {}

    /**
     * ${@inheritDoc}
     */
    @Override
    public CritersFactory<E, S> use(final EntityManager entityManager)
            throws InvalidCritersTargetException {

        this.entityManager = Optional.ofNullable(entityManager)
                                     .orElseThrow(IllegalArgumentException::new);

        if(Objects.nonNull(searchFilter) &&
           !isSearchable(searchFilter.getEntityClass())) {

            throw new InvalidCritersTargetException(INVALID_TARGET);

        }

        if(logger.isDebugEnabled()) {

            logger.debug("Configured entity manager '{}'.",
                         entityManager);

        }


        return this;

    }

    /**
     * ${@inheritDoc}
     */
    @Override
    public CritersFactory<E, S> use(final Root<E> root,
                                    final CriteriaQuery<E> criteriaQuery,
                                    final CriteriaBuilder criteriaBuilder)
            throws InvalidCritersTargetException {

        if(Objects.isNull(root) ||
           Objects.isNull(criteriaQuery) ||
           Objects.isNull(criteriaBuilder)) {

            throw new IllegalArgumentException(NULL_PARAMETERS);
        }

        if(Objects.nonNull(searchFilter) &&
           !isSearchable(searchFilter.getEntityClass())) {

            throw new InvalidCritersTargetException(INVALID_TARGET);
        }

        this.root = root;
        this.criteriaBuilder = criteriaBuilder;
        this.criteriaQuery = criteriaQuery;

        if(logger.isDebugEnabled()) {

            logger.debug("Configured root '{}', criteriaBuilder '{}' and criteriaQuery '{}'.",
                         root, criteriaBuilder, criteriaQuery);

        }

        return this;

    }

    /**
     * ${@inheritDoc}
     */
    @Override
    public CritersFactory<E, S> prepare(S searchFilter)
            throws InvalidCritersTargetException {

        this.searchFilter = Optional.ofNullable(searchFilter)
                                    .orElseThrow(IllegalArgumentException::new);

        if(!isSearchable(searchFilter.getEntityClass())) {

            throw new InvalidCritersTargetException(INVALID_TARGET);

        }

        if(logger.isDebugEnabled()) {

            logger.debug("Prepared search filter '{}'.",
                         searchFilter);

        }

        return this;

    }

    /**
     * ${@inheritDoc}
     */
    @Override
    public CritersSearch<E> build() {

        if(Objects.nonNull(searchFilter)) {

            if(Objects.isNull(root) &&
               Objects.isNull(criteriaBuilder) &&
               Objects.isNull(criteriaQuery) &&
               Objects.nonNull(entityManager)) {

                try {

                    final CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
                    final CriteriaQuery<E> criteriaQuery = criteriaBuilder.createQuery(searchFilter.getEntityClass());
                    final Root<E> root = criteriaQuery.from(searchFilter.getEntityClass());
                    this.use(root, criteriaQuery, criteriaBuilder);

                } catch (InvalidCritersTargetException e) {

                    throw new IllegalStateException(INVALID_TARGET);

                }

            } else if(Objects.nonNull(root) &&
                      Objects.nonNull(criteriaBuilder) &&
                      Objects.nonNull(criteriaQuery)) {

                if(logger.isDebugEnabled()) {

                    logger.debug("Using pre-configured components for search.");

                }

            } else {

                throw new IllegalStateException(ROOT_BUILDER_OR_QUERY_MISSING);

            }

            return new CritersSearchImpl<>(criteriaBuilder,
                                           criteriaQuery,
                                           root,
                                           searchFilter);

        } else {

            throw new IllegalStateException(SEARCH_FILTER_MISSING);

        }

    }

    /**
     * ${@inheritDoc}
     */
    @Override
    public boolean isSearchable(final Class<E> targetClass) {

        if(Objects.nonNull(entityManager)) {

            return entityManager.getMetamodel()
                                .getEntities()
                                .stream()
                                .anyMatch(type -> type.getJavaType().equals(targetClass));

        } else if(Objects.nonNull(root)) {

            return root.getModel().getJavaType().equals(targetClass);

        } else {

            throw new IllegalStateException(ENTITY_MANAGER_OR_ROOT_REQUIRED);

        }

    }

}
