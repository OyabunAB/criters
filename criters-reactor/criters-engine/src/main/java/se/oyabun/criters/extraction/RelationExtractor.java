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
import se.oyabun.criters.criteria.Relation;
import se.oyabun.criters.criteria.Relations;
import se.oyabun.criters.exception.InvalidCritersFilteringException;
import se.oyabun.criters.util.FilterUtil;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.From;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

/**
 * Relations extractor producing predicates for filters
 * converting {@link Relations} references into joins and reusing {@link ParameterExtractor} for
 * restrictions on each {@link Relation} level.
 *
 * @author Daniel Sundberg
 */
public class RelationExtractor
        implements Extractor {

    /**
     * ${@inheritDoc}
     */
    @Override
    public <E, S extends Filter<E>> Optional<Predicate> generatePredicate(final S filter,
                                                                          final CriteriaBuilder criteriaBuilder,
                                                                          final Root<E> root)
            throws InvalidCritersFilteringException {

        final Map<String, Predicate> predicates = new HashMap<>();

        for(final Method method : FilterUtil.relationalMethods(filter)) {

            FilterUtil.validateRelations(method, root.getJavaType());

            From<?, ?> currentFrom = null;

            final Relations relations = method.getAnnotation(Relations.class);

            for(final Relation relation : relations.value()) {

                currentFrom = Objects.nonNull(currentFrom) ?
                              currentFrom.join(relation.name()) :
                              root.join(relation.name());

                for(final Parameter parameter : relation.parameters()) {

                    final Predicate currentPredicate =
                            ParameterExtractor.produce(filter,
                                                       criteriaBuilder,
                                                       currentFrom,
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

            }

        }

        return predicates.values().stream().reduce(criteriaBuilder::and);

    }

}
