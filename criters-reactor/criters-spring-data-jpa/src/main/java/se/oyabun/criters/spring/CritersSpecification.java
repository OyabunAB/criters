package se.oyabun.criters.spring;

import org.springframework.data.jpa.domain.Specification;
import se.oyabun.criters.Criters;
import se.oyabun.criters.criteria.Filter;
import se.oyabun.criters.exception.InvalidCritersFilteringException;
import se.oyabun.criters.exception.InvalidCritersTargetException;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

/**
 * Connects the Criters Criteria Automation Engine to Spring data by
 * implementing the {@link Specification} interface, thus getting injected
 * into {@link org.springframework.data.jpa.repository.JpaSpecificationExecutor} implementations.
 *
 * @param <E> type of entity specification is intended to produce
 * @param <S> type of filter used for criters criteria generation
 * @author Daniel Sundberg
 */
public abstract class CritersSpecification<E, S extends Filter<E>>
       implements Specification<E> {

    private final S searchFilter;

    public CritersSpecification(final S searchFilter) {

        this.searchFilter = searchFilter;

    }

    /**
     * ${@inheritDoc}
     */
    @Override
    public Predicate toPredicate(final Root<E> root,
                                 final CriteriaQuery<?> criteriaQuery,
                                 final CriteriaBuilder criteriaBuilder) {

        try {

            return Criters. <E, Filter<E>> factory()
                          .use(root, criteriaQuery, criteriaBuilder)
                          .prepare(searchFilter)
                          .build().restrictions();

        } catch (InvalidCritersTargetException | InvalidCritersFilteringException e) {

            throw new IllegalStateException("Failed to prepare predicate for search filter.", e);

        }

    }

}
