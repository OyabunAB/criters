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
 * Parameter restricting Foo typed filter.
 *
 * @author Daniel Sundberg
 */
public class FooPropertyFilter
        extends Filter<Foo> {

    private Integer value;

    /**
     * Constructs a filter that matches {@link se.oyabun.criters.test.data.Foo} entities with the given value.
     *
     * @param value the integer value to filter by
     */
    public FooPropertyFilter(final Integer value) {

        this.value = value;

    }

    /**
     * Returns the integer value used as the EQUALS filter criterion.
     *
     * @return the filter value
     */
    @Parameter(restriction = Restriction.EQUALS,
               name ="value")
    public Integer getValue() {

        return value;

    }

    /**
     * Sets the integer value used as the filter criterion.
     *
     * @param value the value to set
     */
    public void setValue(Integer value) {

        this.value = value;

    }

}
