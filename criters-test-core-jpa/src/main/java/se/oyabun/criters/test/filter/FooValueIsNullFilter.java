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
package se.oyabun.criters.test.filter;

import se.oyabun.criters.criteria.Filter;
import se.oyabun.criters.criteria.Parameter;
import se.oyabun.criters.criteria.Restriction;
import se.oyabun.criters.test.data.Foo;

/**
 * IS NULL restriction filter for {@link Foo} entities, matching where {@code value} is null.
 * The getter return value is not used when building the predicate.
 *
 * @author Daniel Sundberg
 */
public class FooValueIsNullFilter
        extends Filter<Foo> {

    /** Creates a new {@code FooValueIsNullFilter}. */
    public FooValueIsNullFilter() {}

    /**
     * Marker method supplying the field name for the IS NULL predicate.
     * The returned value is not used when building the predicate.
     *
     * @return {@code null}
     */
    @Parameter(restriction = Restriction.IS_NULL,
               name = "value")
    public Integer getValue() {

        return null;

    }

}
