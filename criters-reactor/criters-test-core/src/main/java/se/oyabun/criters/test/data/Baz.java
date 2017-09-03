package se.oyabun.criters.test.data;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;

@Entity
public class Baz {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @OneToOne
    private Bar bar;

    public long getId() {

        return id;
    }

    public void setId(final long id) {

        this.id = id;
    }

    public Bar getBar() {

        return bar;

    }

    public void setBar(Bar bar) {

        this.bar = bar;

    }

    @Override
    public boolean equals(Object o) {

        if (this == o) {
            return true;
        }

        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Baz baz = (Baz) o;

        return new EqualsBuilder()
                .append(id, baz.id)
                .isEquals();

    }

    @Override
    public int hashCode() {

        return new HashCodeBuilder(17,37)
                .append(id)
                .toHashCode();

    }

}
