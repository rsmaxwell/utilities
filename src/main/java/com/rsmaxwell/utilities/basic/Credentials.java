package com.rsmaxwell.utilities.basic;

import org.apache.commons.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Credentials {

    private static final Logger logger = LoggerFactory.getLogger(Credentials.class);

    private String username;
    private String password;

    public Credentials(String prefixEncodedApikey) throws InvalidHeaderException {

        if (prefixEncodedApikey == null) {
            throw new InvalidHeaderException("Missing authorisation header");
        }

        logger.info("prefixEncodedApikey = " + prefixEncodedApikey);
        String prefix = "Basic ";

        if (prefixEncodedApikey.startsWith(prefix) == false) {
            throw new InvalidHeaderException("Bad authorisation header prefix");
        }

        String encodedApikey = prefixEncodedApikey.substring(prefix.length());
        logger.info("encodedApikey = " + encodedApikey);

        String apikey = new String(Base64.decodeBase64(encodedApikey.getBytes()));
        logger.info("apikey = " + apikey);

        int colonPos = apikey.indexOf(':');
        if (colonPos < 0) {
            throw new InvalidHeaderException("Bad authorisation header separator");
        }

        username = apikey.substring(0, colonPos);
        password = apikey.substring(colonPos + 1);

        logger.info("username = " + username + ", password = " + password);
    }

    /**
     * @return the username
     */
    public String getUsername() {
        return username;
    }

    /**
     * @param username
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * @return the password
     */
    public String getPassword() {
        return password;
    }

    /**
     * @param password
     */
    public void setPassword(String password) {
        this.password = password;
    }
}
