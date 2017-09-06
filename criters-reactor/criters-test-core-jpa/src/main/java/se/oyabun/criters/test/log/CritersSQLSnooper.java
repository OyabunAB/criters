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
package se.oyabun.criters.test.log;

import com.p6spy.engine.common.P6Util;
import com.p6spy.engine.spy.appender.MessageFormattingStrategy;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * SQL generation snooper, using {@link com.p6spy.engine.spy.P6SpyDriver} to generate logs.
 * Shamelessly re-logging to criters class logger.
 *
 * @author Daniel Sundberg
 */
public class CritersSQLSnooper
       implements MessageFormattingStrategy {

    private static final Logger log = LoggerFactory.getLogger(CritersSQLSnooper.class);

    /**
     * ${@inheritDoc}
     */
    @Override
    public String formatMessage(final int connectionId,
                                final String now,
                                final long elapsed,
                                final String category,
                                final String prepared,
                                final String sql) {

        final String singleLineSql = P6Util.singleLine(sql);

        if(log.isDebugEnabled() && StringUtils.isNotBlank(singleLineSql)) {

            log.debug(singleLineSql);

        }

        return singleLineSql;

    }

}
