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

import java.lang.reflect.ParameterizedType;

/**
 * Basic filter class, enforcing typing of filter.
 *
 * @param <E> type of entity
 * @author Daniel Sundberg
 */
public abstract class Filter<E> {

    private final Class<E> filterClass;

    public Filter() {

        filterClass = (Class<E>) ((ParameterizedType) getClass()
                .getGenericSuperclass()).getActualTypeArguments()[0];

    }

    public final Class<E> getEntityClass() {

        return filterClass;

    }

}
