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

import se.oyabun.criters.exception.InvalidCritersFilteringException;

import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;

public interface CritersSearch {

    /**
     * Return search generated criteria
     *
     * @throws InvalidCritersFilteringException if restrictions fails to calculate
     * @return criteria with calculated restrictions
     */
    CriteriaQuery<?> criteria()
            throws InvalidCritersFilteringException;

    /**
     * Return calculated restrictions
     *
     * @throws InvalidCritersFilteringException if restrictions fails to calculate
     * @return restrictions based on search criteria
     */
    Predicate restrictions()
            throws InvalidCritersFilteringException;

}
