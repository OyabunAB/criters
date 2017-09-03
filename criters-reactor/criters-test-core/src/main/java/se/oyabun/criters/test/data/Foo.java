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
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.Collection;

@Entity
public class Foo{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private Integer value;

    @OneToMany(mappedBy = "foo")
    private Collection<Bar> bars = new ArrayList<>();

    public long getId() {

        return id;
    }

    public void setId(final long id) {

        this.id = id;
    }

    public Integer getValue() {

        return value;

    }

    public void setValue(Integer value) {

        this.value = value;

    }

    public Collection<Bar> getBars() {

        return bars;

    }

    public void setBars(Collection<Bar> bars) {

        this.bars = bars;

    }

    @Override
    public boolean equals(Object o) {

        if (this == o) {
            return true;
        }

        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Foo that = (Foo) o;

        return new EqualsBuilder()
                .append(id, that.id)
                .isEquals();

    }

    @Override
    public int hashCode() {

        return new HashCodeBuilder(17,37)
                .append(id)
                .toHashCode();

    }

}
