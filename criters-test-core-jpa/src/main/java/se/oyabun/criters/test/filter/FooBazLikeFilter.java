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
import se.oyabun.criters.criteria.Relation;
import se.oyabun.criters.criteria.Relations;
import se.oyabun.criters.criteria.Restriction;
import se.oyabun.criters.test.data.Foo;

/**
 * LIKE restriction filter for {@link Foo} entities, matching via the nested
 * {@code bars -> baz.value} relation path using a SQL LIKE pattern.
 *
 * @author Daniel Sundberg
 */
public class FooBazLikeFilter
        extends Filter<Foo> {

    private final String bazValuePattern;

    /**
     * Constructs a filter that matches {@link Foo} entities whose related {@code Baz.value}
     * matches the given SQL LIKE pattern.
     *
     * @param bazValuePattern the SQL LIKE pattern (e.g. {@code "%alu%"})
     */
    public FooBazLikeFilter(final String bazValuePattern) {

        this.bazValuePattern = bazValuePattern;

    }

    /**
     * Returns the SQL LIKE pattern used to filter on the nested {@code Baz.value} field.
     *
     * @return the LIKE pattern
     */
    @Relations({
            @Relation(name = "bars",
                      iterable = true),
            @Relation(name = "baz",
                      parameters = {
                    @Parameter(name = "value",
                               restriction = Restriction.LIKE)
            })
    })
    public String getBazValuePattern() {

        return bazValuePattern;

    }

}
