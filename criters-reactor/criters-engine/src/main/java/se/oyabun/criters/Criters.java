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
package se.oyabun.criters;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import se.oyabun.criters.criteria.Filter;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Criters convenience builder.
 *
 * @author Daniel Sundberg
 */
public class Criters {

    private static final Logger logger = LoggerFactory.getLogger(Criters.class);

    private static final String LINEBREAK = "\n\r";

    static {

        BufferedReader bufferedReader = null;
        try {

            bufferedReader =
                    new BufferedReader(
                            new InputStreamReader(
                                    Criters.class.getResourceAsStream(
                                            "/critersbanner.txt")));

            if(logger.isInfoEnabled()) {

                logger.info(
                        LINEBREAK.concat(
                                bufferedReader.lines()
                                              .collect(Collectors.joining(LINEBREAK))));

            }

        } finally {

            if(Objects.nonNull(bufferedReader)) {

                try {

                    bufferedReader.close();

                } catch (IOException e) {

                    //
                    // Ignore reader issue.
                    //

                }

            }

        }

    }

    /**
     * Produce an instance of a criters factory.
     *
     * @param <E> type of entity
     * @param <S> type of filter
     * @return criters factory
     */
    public static <E, S extends Filter<E>> CritersFactory<E, S> factory() {

        return new CritersFactoryImpl<>();

    }

}
