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

/**
 * Relations type definition
 */
public enum Restriction {

    /** Field value must equal the filter value. */
    EQUALS,

    /** Field value must not equal the filter value. */
    NOT_EQUALS,

    /** Field value must be strictly greater than the filter value. */
    GREATER_THAN,

    /** Field value must be greater than or equal to the filter value. */
    GREATER_THAN_OR_EQUALS,

    /** Field value must be strictly less than the filter value. */
    LESS_THAN,

    /** Field value must be less than or equal to the filter value. */
    LESS_THAN_OR_EQUALS,

    /** Field value must match the given SQL LIKE pattern. */
    LIKE,

    /** Field value must be null. */
    IS_NULL,

    /** Field value must not be null. */
    IS_NOT_NULL,

    /** Field value must be contained in the given collection. */
    IN

}
