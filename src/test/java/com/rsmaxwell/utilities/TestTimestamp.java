package com.rsmaxwell.utilities;

import static org.junit.Assert.assertEquals;

import java.text.ParseException;
import java.util.Date;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.rsmaxwell.utilities.basic.Timestamp;

/**
 *
 */
public class TestTimestamp {

    private static final Logger logger = LoggerFactory.getLogger(TestTimestamp.class);

    @Test
    public void timestampIsSensible() {
        String string = Timestamp.now();
        logger.info(string);
        assertEquals("Unexpected timestamp", string.length(), 23);
    }

    @Test
    public void parseKnownTimeWorks() throws ParseException {
        long now = new Date().getTime();
        String string = Timestamp.format(now);
        long now2 = Timestamp.parse(string);
        assertEquals("Unexpected timestamp", now, now2);
    }
}
