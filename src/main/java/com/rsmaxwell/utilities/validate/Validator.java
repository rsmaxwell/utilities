package com.rsmaxwell.utilities.validate;

import java.io.File;
import java.io.InvalidObjectException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Validator {

    private static final Logger logger = LoggerFactory.getLogger(Validator.class);

    private String fieldName;
    private String string;
    private Object value;

    public Validator(String fieldName, String string) {
        this.fieldName = fieldName;
        this.string = string;
    }

    public static String boundedString(String fieldname, String string, int minimum, int maximum) throws InvalidObjectException {
        return new Validator(fieldname, string).notnull().minimumLength(minimum).maximumLength(maximum).getString();
    }

    public static String boundedSafeString(String fieldName, String string, int minimum, int maximum) throws InvalidObjectException {
        return new Validator(fieldName, string).notnull().minimumLength(minimum).maximumLength(maximum).pattern("[0-9a-zA-Z]*").getString();
    }

    public static String username(String fieldName, String string) throws InvalidObjectException {
        return new Validator(fieldName, string).notnull().pattern("[0-9a-zA-Z]{6,64}").getString();
    }

    public static String password(String fieldName, String string) throws InvalidObjectException {
        return new Validator(fieldName, string).notnull().pattern("[0-9a-zA-Z]{8,64}").getString();
    }

    public static int integerRange(String fieldName, String string, int min, int max) throws InvalidObjectException {
        return new Validator(fieldName, string).notnull().number().minimumValue(min).maximumValue(max).getInteger();
    }

    public static String uuid(String fieldName, String string) throws InvalidObjectException {
        return new Validator(fieldName, string).notnull().pattern("[0-9a-fA-F-]{36}").getString();
    }

    public static File file(String fieldName, String string) throws InvalidObjectException {
        return new Validator(fieldName, string).notnull().file().getFile();
    }

    public static File fileExists(String fieldName, String string) throws InvalidObjectException {
        return new Validator(fieldName, string).notnull().fileExists().getFile();
    }

    public static File existingDirectory(String fieldName, String string) throws InvalidObjectException {
        return new Validator(fieldName, string).notnull().fileExists().directory().getFile();
    }

    private Validator file() throws InvalidObjectException {
        value = new File(string);
        return this;
    }

    private Validator fileExists() throws InvalidObjectException {
        File file = new File(string);
        if (file.exists() == false) {
            throw new InvalidObjectException(fieldName + " value " + string + " is not an existing file");
        }
        value = file;
        return this;
    }

    private Validator directory() throws InvalidObjectException {
        File file = (File) value;
        if (file.isDirectory() == false) {
            throw new InvalidObjectException(fieldName + " value " + string + " is not a directory");
        }
        return this;
    }

    private File getFile() {
        return (File) value;
    }

    private String getString() {
        return string;
    }

    private int getInteger() {
        return ((Long) value).intValue();
    }

    public static Validator email(String fieldName, String string) throws InvalidObjectException {
        Validator validator = new Validator(fieldName, string);
        try {
            InternetAddress emailAddr = new InternetAddress(string);
            emailAddr.validate();
        }
        catch (AddressException e) {
            throw new InvalidObjectException("Bad email address: " + e.getClass().getName() + ": " + e.getMessage());
        }
        return validator;
    }

    public Validator number() throws InvalidObjectException {
        try {
            this.value = Long.parseLong(string);
        }
        catch (NumberFormatException e) {
            throw new InvalidObjectException(fieldName + " value " + string + " is not a number");
        }
        return this;
    }

    public Validator notnull() throws InvalidObjectException {
        if (string == null) {
            throw new InvalidObjectException("Missing field: " + fieldName);
        }
        return this;
    }

    public Validator minimumValue(long min) throws InvalidObjectException {
        if ((Long) value < min) {
            throw new InvalidObjectException(fieldName + " too small: (value: " + string + ", minimum: " + min + ")");
        }
        return this;
    }

    public Validator maximumValue(long max) throws InvalidObjectException {
        if ((Long) value > max) {
            throw new InvalidObjectException(fieldName + " too large: (value: " + string + ", maximum: " + max + ")");
        }
        return this;
    }

    public Validator minimumLength(int min) throws InvalidObjectException {
        if (string.length() < min) {
            throw new InvalidObjectException(fieldName + " too short: (value: " + string + ", length: " + string.length() + ", minimum: " + min + ")");
        }
        return this;
    }

    public Validator maximumLength(int max) throws InvalidObjectException {
        if (string.length() > max) {
            throw new InvalidObjectException(fieldName + " too long: (value: " + string + ", length: " + string.length() + ", maximum: " + max + ")");
        }
        return this;
    }

    public Validator pattern(String pattern) throws InvalidObjectException {
        Pattern p = Pattern.compile(pattern);
        Matcher m = p.matcher(string);
        if (m.matches() == false) {
            throw new InvalidObjectException(fieldName + " does not match pattern: (value: " + string + ", pattern:" + pattern + ")");
        }
        return this;
    }
}
