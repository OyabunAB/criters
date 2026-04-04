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
package se.oyabun.criters.criteria;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Filtering parameter annotation, used on direct parameters.
 *
 * @author Daniel Sundberg
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Parameter {

    /**
     * Combination strategy controlling how this parameter is joined with others.
     *
     * @return the combination annotation, defaults to ungrouped AND
     */
    Combination combinate() default @Combination;

    /**
     * The comparison restriction applied when building the predicate.
     *
     * @return the restriction type, defaults to {@link Restriction#EQUALS}
     */
    Restriction restriction() default Restriction.EQUALS;

    /**
     * The entity field name this parameter maps to.
     *
     * @return the field name
     */
    String name();

}
