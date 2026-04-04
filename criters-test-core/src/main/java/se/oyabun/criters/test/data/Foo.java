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
package se.oyabun.criters.test.data;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import java.util.ArrayList;
import java.util.Collection;

/**
 * Test entity Foo
 * Neutrally compliant with javax persistence API to enable reuse for different implementations
 *
 * @author Daniel Sundberg
 */
@Entity
public class Foo{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private Integer value;

    @OneToMany(mappedBy = "foo")
    private Collection<Bar> bars = new ArrayList<>();

    /** Creates a new {@code Foo} entity with default field values. */
    public Foo() {}

    /**
     * Returns the entity identifier.
     *
     * @return the id
     */
    public long getId() {

        return id;
    }

    /**
     * Sets the entity identifier.
     *
     * @param id the id to set
     */
    public void setId(final long id) {

        this.id = id;
    }

    /**
     * Returns the integer value.
     *
     * @return the value
     */
    public Integer getValue() {

        return value;

    }

    /**
     * Sets the integer value.
     *
     * @param value the value to set
     */
    public void setValue(Integer value) {

        this.value = value;

    }

    /**
     * Returns the associated Bar entities.
     *
     * @return the bars collection
     */
    public Collection<Bar> getBars() {

        return bars;

    }

    /**
     * Sets the associated Bar entities.
     *
     * @param bars the bars collection to set
     */
    public void setBars(Collection<Bar> bars) {

        this.bars = bars;

    }

    @Override
    public boolean equals(final Object o) {

        if (this == o) {
            return true;
        }

        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        final Foo foo = (Foo) o;

        return new EqualsBuilder().append(id,
                                          foo.id)
                                  .append(value,
                                          foo.value)
                                  .append(bars,
                                          foo.bars)
                                  .isEquals();
    }

    @Override
    public int hashCode() {

        return new HashCodeBuilder(17,
                                   37).append(id)
                                      .append(value)
                                      .append(bars)
                                      .toHashCode();
    }
}
