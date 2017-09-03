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
import se.oyabun.criters.criteria.RelationFilter;
import se.oyabun.criters.test.data.Bar;
import se.oyabun.criters.test.data.Foo;

import static se.oyabun.criters.criteria.RelationFilter.Restriction;

/**
 * Relation restriction focused Foo typed filter.
 *
 * @author Daniel Sundberg
 */
public class FooRelationFilter
        extends Filter<Foo> {

    private long barId;

    public FooRelationFilter(final long barId) {

        this.barId = barId;
    }

    @RelationFilter(restriction = Restriction.IN,
                    sourceParameterName = "bars",
                    relationSourceCollection = true,
                    relationTargetType = Bar.class,
                    relationTargetParameter = "id")
    public long getBarId() {

        return barId;
    }

    public void setBarId(long barId) {

        this.barId = barId;
    }

}
