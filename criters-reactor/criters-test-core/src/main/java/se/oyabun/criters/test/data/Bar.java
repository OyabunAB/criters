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

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;

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


    public long getId() {

        return id;
    }

    public void setId(final long id) {

        this.id = id;
    }

    public Foo getFoo() {

        return foo;

    }

    public void setFoo(Foo foo) {

        this.foo = foo;

    }

    public Baz getBaz() {

        return baz;
    }

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
