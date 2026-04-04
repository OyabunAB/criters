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
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;

/**
 * Test entity Bar
 * Neutrally compliant with javax persistence API to enable reuse for different implementations
 *
 * @author Daniel Sundberg
 */
@Entity
public class Bar {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne
    private Foo foo;

    @OneToOne
    private Baz baz;

    /** Creates a new {@code Bar} entity with default field values. */
    public Bar() {}

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
     * Returns the parent Foo entity.
     *
     * @return the foo
     */
    public Foo getFoo() {

        return foo;

    }

    /**
     * Sets the parent Foo entity.
     *
     * @param foo the foo to set
     */
    public void setFoo(Foo foo) {

        this.foo = foo;

    }

    /**
     * Returns the associated Baz entity.
     *
     * @return the baz
     */
    public Baz getBaz() {

        return baz;
    }

    /**
     * Sets the associated Baz entity.
     *
     * @param baz the baz to set
     */
    public void setBaz(final Baz baz) {

        this.baz = baz;

    }

    @Override
    public boolean equals(final Object o) {

        if (this == o) {
            return true;
        }

        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        final Bar bar = (Bar) o;

        return new EqualsBuilder().append(id,
                                          bar.id)
                                  .isEquals();
    }

    @Override
    public int hashCode() {

        return new HashCodeBuilder(17,
                                   37).append(id)
                                      .toHashCode();
    }
}
