
package com.rsmaxwell.utilities.basic;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.reflect.Array;
import java.util.Collection;

import org.xml.sax.Attributes;

/**
 * Utility class to help format different types of object into a structured string format. Typically
 * used for Outputting values to trace or logs
 */
public class ObjectFormatter {

    private final StringBuffer buffer;
    private final int width1;
    private final int width2;
    private final String separator;

    /**
     * @param width1
     * @param width2
     * @param separator
     */
    public ObjectFormatter(final int width1, final int width2, final String separator) {
        buffer = new StringBuffer();
        this.width1 = width1;
        this.width2 = width2;
        this.separator = separator;
    }

    private void addField(final String lquote, final String rquote, final String name, final String value) {
        buffer.append(StringUtilities.left(name, width1));
        buffer.append(StringUtilities.left(":", width2));
        buffer.append(lquote);
        buffer.append(value);
        buffer.append(rquote);
        buffer.append(separator);
    }

    private void addField(final String lquote, final String rquote, final int name, final String value) {
        buffer.append(StringUtilities.right(Integer.toString(name), width1));
        buffer.append(StringUtilities.left(":", width2));
        buffer.append(lquote);
        buffer.append(value);
        buffer.append(rquote);
        buffer.append(separator);
    }

    /**
     * @param name
     * @param value
     */
    public void add(final String name, final int value) {
        addField("", "", name, Integer.toString(value));
    }

    /**
     * @param name
     * @param value
     */
    public void add(final String name, final long value) {
        addField("", "", name, Long.toString(value));
    }

    /**
     * @param name
     * @param value
     */
    public void add(final String name, final boolean value) {
        addField("", "", name, Boolean.toString(value));
    }

    /**
     * @param name
     * @param value
     */
    public void add(final String name, final String value) {
        addField("'", "'", name, StringUtilities.safeString(value));
    }

    /**
     * @param name
     * @param value
     */
    public void add(final String name, final String[] value) {
        if (value != null) {
            buffer.append(StringUtilities.left(name, width1));
            buffer.append(":");
            buffer.append(separator);
            for (int i = 0; i < value.length; i++) {
                addField("(", ")", i, StringUtilities.safeString(value[i]));
            }
        }
    }

    /**
     * @param name
     * @param value
     */
    public void add(final String name, final int[] value) {
        buffer.append(StringUtilities.left(name, width1));
        buffer.append(StringUtilities.left(":", width2));
        buffer.append("(");
        for (int i = 0; i < value.length; i++) {
            if (i > 0) {
                buffer.append(',');
            }
            buffer.append(value[i]);
        }
        buffer.append(")");
    }

    /**
     * @param name
     * @param value
     */
    public void add(final String name, final Collection<?> value) {
        buffer.append(StringUtilities.left(name, width1));
        buffer.append(StringUtilities.left(":", width2));
        buffer.append("(");

        int index = 0;
        for (final Object object : value) {
            if (index++ > 0) {
                buffer.append(',');
            }
            buffer.append(object);
        }
        buffer.append(")");
    }

    /**
     * @param name
     * @param value
     */
    public void add(final String name, final byte[] value) {
        addField("", "", name, StringUtilities.arrayToHexString(value));
    }

    /**
     * @param name
     * @param value
     */
    public void add(final String name, final Object value) {
        addField("[", "]", name, StringUtilities.safeString(value));
    }

    /**
     * @param value
     * @return this instance
     */
    public ObjectFormatter append(final String value) {
        buffer.append(value);
        buffer.append(separator);
        return this;
    }

    /**
     * @return string
     */
    @Override
    public String toString() {
        return buffer.toString();
    }

    /**
     * @param object
     * @return string
     */
    public static String format(final Object object) {
        final StringBuilder sb = new StringBuilder();

        if (object == null) {
            sb.append("(null)");
        }
        else if (object instanceof Attributes) {
            sb.append(format((Attributes) object));
        }
        else if (object instanceof String) {
            sb.append(format((String) object));
        }
        else if (object instanceof StringBuffer) {
            sb.append(format((StringBuffer) object));
        }
        else if (object instanceof StringBuilder) {
            sb.append(format((StringBuilder) object));
        }
        else if (object instanceof Throwable) {
            sb.append(format((Throwable) object));
        }
        else if (object instanceof Byte) {
            sb.append(format((Byte) object));
        }
        else if (object instanceof Collection) {
            final Collection<?> collection = (Collection<?>) object;
            final int length = collection.size();
            if (length > 1) {
                sb.append("{");
            }
            int index = 0;
            for (final Object element : collection) {
                if (index++ > 0) {
                    sb.append(", ");
                }
                sb.append(format(element));
            }
            if (length > 1) {
                sb.append("}");
            }
        }
        else {
            final Class<?> cclass = object.getClass();

            if (cclass == byte[].class) {
                sb.append(bytesToHex((byte[]) object));
            }
            else if (cclass.isArray()) {
                final int length = Array.getLength(object);
                if (length > 1) {
                    sb.append("[");
                }
                for (int index = 0; index < length; index++) {
                    final Object element = Array.get(object, index);
                    if (index > 0) {
                        sb.append(", ");
                    }
                    sb.append(format(element));
                }
                if (length > 1) {
                    sb.append("]");
                }
            }
            else {
                sb.append(object);
            }
        }
        return sb.toString();
    }

    /**
     * @param attributes
     * @return string
     */
    public static String format(final Attributes attributes) {

        final StringBuilder sb = new StringBuilder();
        for (int i = 0; i < attributes.getLength(); i++) {
            if (i > 0) {
                sb.append(" ");
            }
            sb.append("[");
            final String name = attributes.getQName(i);
            final String value = attributes.getValue(i);
            sb.append(name);
            sb.append("=\"");
            sb.append(value);
            sb.append("\"");
            sb.append("]");
        }

        return sb.toString();
    }

    /**
     * @param text
     * @return string
     */
    public static String format(final String text) {
        return StringUtilities.toJavaString(text);
    }

    /**
     * @param sb
     * @return string
     */
    public static String format(final StringBuffer sb) {
        return format(sb.toString());
    }

    /**
     * @param sb
     * @return string
     */
    public static String format(final StringBuilder sb) {
        return format(sb.toString());
    }

    final static char[] hexArray = "0123456789abcdef".toCharArray();

    /**
     * @param bytes
     * @return string
     */
    public static String bytesToHex(final byte[] bytes) {
        final char[] hexChars = new char[bytes.length * 2];
        int v;
        for (int j = 0; j < bytes.length; j++) {
            v = bytes[j] & 0xFF;
            hexChars[j * 2] = hexArray[v >>> 4];
            hexChars[(j * 2) + 1] = hexArray[v & 0x0F];
        }
        return new String(hexChars);
    }

    /**
     * @param sb
     * @return string
     */
    public static String format(final Byte sb) {
        final int v = sb & 0xFF;
        final char[] hexChars = new char[2];
        hexChars[0] = hexArray[v >>> 4];
        hexChars[1] = hexArray[v & 0x0F];
        return new String(hexChars);
    }

    /**
     * @param t
     * @return string
     */
    public static String format(final Throwable t) {
        final StringWriter sw = new StringWriter();
        final PrintWriter pw = new PrintWriter(sw, true);
        t.printStackTrace(pw);
        pw.flush();
        sw.flush();
        return sw.toString();
    }

}
