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

import java.util.Collection;

/**
 * IN restriction filter for {@link Foo} entities, matching {@code value} against a collection.
 *
 * @author Daniel Sundberg
 */
public class FooInFilter
        extends Filter<Foo> {

    private final Collection<Integer> values;

    /**
     * Constructs a filter that matches {@link Foo} entities whose value is contained in the given collection.
     *
     * @param values the collection of values to match against
     */
    public FooInFilter(final Collection<Integer> values) {

        this.values = values;

    }

    /**
     * Returns the collection of values used as the IN filter criterion.
     *
     * @return the values collection
     */
    @Parameter(restriction = Restriction.IN,
               name = "value")
    public Collection<Integer> getValues() {

        return values;

    }

}
