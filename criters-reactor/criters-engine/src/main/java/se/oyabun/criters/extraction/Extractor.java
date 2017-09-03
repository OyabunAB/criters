package se.oyabun.criters.extraction;

import se.oyabun.criters.criteria.Filter;
import se.oyabun.criters.exception.InvalidCritersFilteringException;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

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
    <E, S extends Filter<E>> Predicate generatePredicate(final S filter,
                                                         final CriteriaBuilder criteriaBuilder,
                                                         final Root<E> root)
            throws InvalidCritersFilteringException;

}
