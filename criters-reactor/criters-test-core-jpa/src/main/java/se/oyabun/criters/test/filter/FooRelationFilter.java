package se.oyabun.criters.test.filter;

import se.oyabun.criters.criteria.Filter;
import se.oyabun.criters.criteria.RelationFilter;
import se.oyabun.criters.test.data.Bar;
import se.oyabun.criters.test.data.Foo;

import static se.oyabun.criters.criteria.RelationFilter.Restriction;

public class FooRelationFilter
        extends Filter<Foo> {

    private long barId;

    public FooRelationFilter(final long barId) {

        this.barId = barId;
    }

    @RelationFilter(restriction = Restriction.IN,
                    relationSourceParameter = "bars",
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
