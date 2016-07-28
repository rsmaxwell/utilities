
package com.rsmaxwell.utilities.basic;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

/**
 * A class that provides some primitive formating capabilities: allows strings to be left or right
 * justified within some specified field width (fill character may also be specified).
 */
public class StringUtilities {

    /** */
    public static final String newline = System.getProperty("line.separator");

    /**
     * Return a String padding with spaces to the given length
     * 
     * @param width
     *            the field width to pad
     * @return the justified string.
     */
    public static String pad(final int width) {
        return StringUtilities.fill(width, ' ');
    }

    /**
     * Return a String padding with spaces to the given length
     * 
     * @param width
     *            the field width to pad
     * @param ch
     * @return the justified string.
     */
    public static String fill(final int width, final char ch) {
        final StringBuffer sb = new StringBuffer();
        for (int i = 0; i < width; i++) {
            sb.append(ch);
        }
        return sb.toString();
    }

    /**
     * Left justify a string, padding with spaces.
     * 
     * @param s
     *            the string to justify
     * @param width
     *            the field width to justify within
     * @return the justified string.
     */
    public static String left(final String s, final int width) {
        return StringUtilities.left(s, width, ' ');
    }

    /**
     * Left justify a string.
     * 
     * @param s
     *            the string to justify
     * @param width
     *            the field width to justify within
     * @param fillChar
     *            the character to fill with
     * @return the justified string.
     */
    public static String left(final String s, final int width, final char fillChar) {
        if (s.length() >= width) {
            return s;
        }
        final StringBuffer sb = new StringBuffer(width);
        sb.append(s);
        for (int i = width - s.length(); --i >= 0;) {
            sb.append(fillChar);
        }
        return sb.toString();
    }

    /**
     * Right justify a string, padding with spaces.
     * 
     * @param s
     *            the string to justify
     * @param width
     *            the field width to justify within
     * @return the justified string.
     */
    public static String right(final String s, final int width) {
        return StringUtilities.right(s, width, ' ');
    }

    /**
     * Right justify a string.
     * 
     * @param s
     *            the string to justify
     * @param width
     *            the field width to justify within
     * @param fillChar
     *            the character to fill with
     * @return the justified string.
     */
    public static String right(final String s, final int width, final char fillChar) {
        if (s.length() >= width) {
            return s;
        }
        final StringBuffer sb = new StringBuffer(width);
        for (int i = width - s.length(); --i >= 0;) {
            sb.append(fillChar);
        }
        sb.append(s);
        return sb.toString();
    }

    /**
     * Helper method to convert a byte[] array (such as a MsgId) to a hex string
     * 
     * @param array
     * @return hex string
     */
    public static String arrayToHexString(final byte[] array) {
        return StringUtilities.arrayToHexString(array, 0, array.length);
    }

    /**
     * Helper method to convert a byte[] array (such as a MsgId) to a hex string
     * 
     * @param array
     * @param offset
     * @param limit
     * @return hex string
     */
    public static String arrayToHexString(final byte[] array, final int offset, final int limit) {
        String retVal;
        if (array != null) {
            final StringBuffer hexString = new StringBuffer(array.length);
            int hexVal;
            char hexChar;
            final int length = Math.min(limit, array.length);
            for (int i = offset; i < length; i++) {
                hexVal = (array[i] & 0xF0) >> 4;
                hexChar = (char) ((hexVal > 9) ? ('A' + (hexVal - 10)) : ('0' + hexVal));
                hexString.append(hexChar);
                hexVal = array[i] & 0x0F;
                hexChar = (char) ((hexVal > 9) ? ('A' + (hexVal - 10)) : ('0' + hexVal));
                hexString.append(hexChar);
            }
            retVal = hexString.toString();
        }
        else {
            retVal = "<null>";
        }
        return retVal;
    }

    /**
     * @param text
     * @return a Java string
     */
    public static String toJavaString(final String text) {

        String string = text;
        if (string != null) {
            string = string.replaceAll("\n", "\\\\n");
            string = string.replaceAll("\r", "\\\\r");
            string = string.replaceAll("\"", "\\\\\"");
        }

        final StringBuilder sb = new StringBuilder();
        sb.append("\"");
        sb.append(string);
        sb.append("\"");

        return sb.toString();
    }

    /**
     * @param object
     * @return a non-null string based on the given string
     */
    public static String safeString(final Object object) {
        return (object == null) ? "<null>" : object.toString();
    }

    /**
     * @param string
     * @return the given string encoded for output
     */
    public static String xmlEncode(final String string) {
        final StringBuffer sb = new StringBuffer();

        for (int i = 0; i < string.length(); i++) {
            final char ch = string.charAt(i);
            switch (ch) {
            case '&':
                sb.append("&amp;");
                break;
            case '<':
                sb.append("&lt;");
                break;
            case '>':
                sb.append("&gt;");
                break;
            case '"':
                sb.append("&quot;");
                break;
            case '\'':
                sb.append("&apos;");
                break;
            case '\0':
                sb.append("\\0");
                break;
            default:
                sb.append(ch);
            }
        }

        return sb.toString();
    }

    /**
     * @param string
     * @return the given string decoded for output
     */
    public static String xmlDecode(final String string) {
        final StringBuffer sb = new StringBuffer();

        // Add the characters - escaping binary zeros as we go
        int i = 0;
        while (i < (string.length() - 1)) {
            final char ch1 = string.charAt(i);
            final char ch2 = string.charAt(i + 1);
            switch (ch1) {
            case '\\':
                switch (ch2) {
                case '0':
                    sb.append("\0");
                    break;
                default:
                    sb.append(ch1);
                    sb.append(ch2);
                    break;
                }
                i++;
                break;
            default:
                sb.append(ch1);
            }
            i++;
        }

        // Add the last character (which should NOT be an escape character)
        if (i < string.length()) {
            final char ch = string.charAt(i);
            sb.append(ch);
        }
        return sb.toString();
    }

    /**
     * Returns a string with all trailing blanks from the right end of its string parameter
     * 
     * @param s
     *            the string to trim
     * @return the right trimmed string.
     */
    public static String trimRight(final String s) {

        int index = s.length() - 1;
        while (index >= 0) {
            final char ch = s.charAt(index);
            if (Character.isWhitespace(ch)) {
                index--;
            }
            else {
                break;
            }
        }

        return s.substring(0, index + 1);
    }

    /**
     * @param systemId
     * @return string
     */
    public static String systemIdToString(final String systemId) {
        String result = systemId;

        if (systemId == null) {
            result = "";
        }
        else {
            try {
                final URI uri = new URI(systemId);

                final String scheme = uri.getScheme();
                if ("file".equals(scheme)) {
                    File f;
                    try {
                        f = new File(uri);
                    }
                    catch (final IllegalArgumentException e2) {
                        f = new File(uri.getPath());
                    }
                    result = f.getCanonicalPath();
                }
                else {
                    result = uri.toString();
                }
            }
            catch (final URISyntaxException e) {
                // do nothing
            }
            catch (final IOException e) {
                // do nothing
            }
        }

        return result;
    }

    /**
     * Format time into a human readable format
     * 
     * @param millis
     * @return the time formatted in milli-seconds
     */
    public static String formatTime(final long millis) {
        long milliseconds = millis;
        final long hours = milliseconds / 3600000;
        milliseconds %= 3600000;
        final long minutes = milliseconds / 60000;
        milliseconds %= 60000;
        final long seconds = milliseconds / 1000;
        milliseconds %= 1000;

        final StringBuffer buffer = new StringBuffer();
        if (hours > 0) {
            buffer.append(hours);
            if (hours == 1) {
                buffer.append(" hour ");
            }
            else {
                buffer.append(" hours ");
            }
            buffer.append(minutes);
            if (minutes == 1) {
                buffer.append(" minute");
            }
            else {
                buffer.append(" minutes");
            }
        }
        else if (minutes > 0) {
            buffer.append(minutes);
            if (minutes == 1) {
                buffer.append(" minute ");
            }
            else {
                buffer.append(" minutes ");
            }
            buffer.append(seconds);
            if (seconds == 1) {
                buffer.append(" second");
            }
            else {
                buffer.append(" seconds");
            }
        }
        else if (seconds > 0) {
            final double double_seconds = millis / 1000.0;
            buffer.append(double_seconds);
            buffer.append(" seconds");
        }
        else {
            buffer.append(milliseconds);
            buffer.append(" millis");
        }

        return buffer.toString();
    }

    /**
     * @param stream
     * @param charsetName
     * @return string
     * @throws Exception
     */
    public static String toString(final InputStream stream, final String charsetName) throws Exception {

        final char[] buffer = new char[0x10000];
        final StringBuilder out = new StringBuilder();
        final Reader in = new InputStreamReader(stream, charsetName);
        try {
            int read;
            do {
                read = in.read(buffer, 0, buffer.length);
                if (read > 0) {
                    out.append(buffer, 0, read);
                }
            }
            while (read >= 0);
        }
        finally {
            in.close();
        }
        return out.toString();
    }

    // Pick from some letters that won't be easily mistaken for each other.
    // So, for example, omit o O and 0, 1 l and L.
    //
    // Also omit the "pound symbol" which is 00A3 when typed in with an editor
    // or C2A3 when typed in with eclipse, which is beyond the 128 ascii range
    // and so will display differently on the BlueMix console in some
    // arbitrary encoding
    //
    // Also omit the "dollar" and "double quote" characters which are mangled when
    // an app queries the password from VCAP_SERVICES
    private static final String LOWERCASE = "abcdefghjkmnpqrstuvwxyz";
    private static final String UPPERCASE = "ABCDEFGHJKMNPQRSTUVWXYZ";
    private static final String ALPHA = LOWERCASE + UPPERCASE;
    private static final String DIGIT = "23456789";
    private static final String PUNCTUATION = "!%^*()_+-={}[]:@~;'#?,./'";

    private static final String userIdCharacterSet = ALPHA + DIGIT;
    private static final String saltCharacterSet = ALPHA + DIGIT + PUNCTUATION;
    private static final String passwordCharacterSet = ALPHA + DIGIT + PUNCTUATION;

    /**
     * @return random char array
     */
    public static String generateSalt() {
        return new String(StringUtilities.randomChars(16, saltCharacterSet));
    }

    /**
     * @return random char array
     */
    public static String generateUserid() {
        return new String(StringUtilities.randomChars(12, userIdCharacterSet));
    }

    /**
     * @return random char array
     */
    public static String generatePassword() {
        return new String(StringUtilities.randomChars(12, passwordCharacterSet));
    }

    /**
     * @param length
     * @param characterSet
     * @return random char array
     */
    public static char[] randomChars(final int length, final String characterSet) {

        final SecureRandom random = new SecureRandom();

        final char[] randomChars = new char[length];
        for (int i = 0; i < length; i++) {

            final int index = (int) (random.nextDouble() * characterSet.length());
            randomChars[i] += characterSet.charAt(index);
        }

        return randomChars;
    }

    /**
     * @param password
     * @param salt
     * @return hash
     * @throws NoSuchAlgorithmException
     * @throws UnsupportedEncodingException
     */
    public static String generateHashString(final String password, final String salt, final String algorithm) throws NoSuchAlgorithmException, UnsupportedEncodingException {
        final MessageDigest md = MessageDigest.getInstance(algorithm);

        final String passwordSalt = password + salt;

        md.update(passwordSalt.getBytes("UTF-8"));
        final byte[] digest = md.digest();

        return ObjectFormatter.format(digest);
    }
}
