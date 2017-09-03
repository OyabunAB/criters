package se.oyabun.criters.extraction;

import se.oyabun.criters.criteria.Filter;
import se.oyabun.criters.criteria.RelationFilter;
import se.oyabun.criters.exception.InvalidCritersFilteringException;
import se.oyabun.criters.util.FilterUtil;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Objects;

/**
 * Relation extractor producing predicates for filters
 *
 * @author Daniel Sundberg
 */
public class RelationExtractor
        implements Extractor {


    /**
     * ${@inheritDoc}
     */
    @Override
    public <E, S extends Filter<E>> Predicate generatePredicate(final S filter,
                                                                final CriteriaBuilder criteriaBuilder,
                                                                final Root<E> root)
            throws InvalidCritersFilteringException {

        Predicate combinedPredicates = null;

        for(final Method method : FilterUtil.relationalMethods(filter)) {

            FilterUtil.validatePropertyRelation(method, filter.getEntityClass());

            final RelationFilter filterRelation = method.getAnnotation(RelationFilter.class);

            final Predicate currentPredicate;

            switch (filterRelation.restriction()) {

                case IN: {

                    try {

                        currentPredicate =
                                root.join(filterRelation.sourceParameterName())
                                    .get(filterRelation.relationTargetParameter())
                                    .in(method.invoke(filter));

                    } catch (IllegalAccessException | InvocationTargetException e) {

                        throw new InvalidCritersFilteringException("Failed to access prepare parameter.", e);

                    }

                    break;

                }

                case NOT_IN: {

                    try {

                        currentPredicate =
                                criteriaBuilder.not(
                                        root.join(filterRelation.sourceParameterName())
                                            .get(filterRelation.relationTargetParameter())
                                            .in(method.invoke(filter)));

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
