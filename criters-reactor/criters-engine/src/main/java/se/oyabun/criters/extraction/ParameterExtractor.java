package se.oyabun.criters.extraction;

import se.oyabun.criters.criteria.Filter;
import se.oyabun.criters.criteria.ParameterFilter;
import se.oyabun.criters.exception.InvalidCritersFilteringException;
import se.oyabun.criters.util.FilterUtil;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Objects;

/**
 * Parameter extractor producing predicates for filters
 *
 * @author Daniel Sundberg
 */
public class ParameterExtractor
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

        for(final Method method : FilterUtil.parameterMethods(filter)) {

            FilterUtil.validatePropertyParameter(method, filter.getEntityClass());

            final ParameterFilter parameterFilter = method.getAnnotation(ParameterFilter.class);

            final Predicate currentPredicate;

            switch (parameterFilter.restriction()) {

                case EQUALS: {

                    try {

                        currentPredicate =
                                criteriaBuilder.equal(
                                        root.get(parameterFilter.sourceParameterName()),
                                        method.invoke(filter));

                    } catch (IllegalAccessException | InvocationTargetException e) {

                        throw new InvalidCritersFilteringException("Failed to access criteria parameter.", e);

                    }

                    break;

                }

                case NOT_EQUALS: {


                    try {

                        currentPredicate =
                                criteriaBuilder.notEqual(
                                        root.get(parameterFilter.sourceParameterName()),
                                        method.invoke(filter));

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

}
