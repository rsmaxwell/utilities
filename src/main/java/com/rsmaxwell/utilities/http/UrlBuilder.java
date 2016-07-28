package com.rsmaxwell.utilities.http;

import java.util.ArrayList;
import java.util.List;

/**
 *
 */
public class UrlBuilder {

    private String base;
    private final List<String> paths = new ArrayList<String>();
    private String extension = null;
    private final List<Parameter> parameters = new ArrayList<Parameter>();
    private String fragment;

    /**
    *
    */
    static class Parameter {

        private final String key;
        private final String value;

        /**
         * @param key
         * @param value
         */
        public Parameter(final String key, final String value) {
            this.key = key;
            this.value = value;
        }

        /**
         * @return the key
         */
        public String getKey() {
            return key;
        }

        /**
         * @return the value
         */
        public String getValue() {
            return value;
        }
    }

    /**
     * @param base
     */
    public UrlBuilder(final String base) {
        this.base = base;
    }

    /**
     * @param base
     */
    public void setBase(final String base) {
        this.base = base;
    }

    /**
    * 
    */
    public void clearPath() {
        paths.clear();
    }

    /**
     * @param path
     */
    public void addPath(final Object path) {
        paths.add(path.toString());
    }

    /**
     * @param extension
     */
    public void setExtension(final String extension) {
        this.extension = extension;
    }

    /**
    * 
    */
    public void clearParameters() {
        parameters.clear();
    }

    /**
     * @param key
     * @param value
     */
    public void addParameter(final String key, final String value) {
        parameters.add(new Parameter(key, value));
    }

    /**
     * @param fragment
     */
    public void setFragment(final String fragment) {
        this.fragment = fragment;
    }

    /**
     * @return result
     */
    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();

        // **************************************************************
        // * Start off with the base
        // **************************************************************
        sb.append(base);

        // **************************************************************
        // * Add the paths and the extension
        // **************************************************************
        for (final String path : paths) {
            sb.append(path);
        }

        if (extension != null) {
            sb.append(extension);
        }

        // **************************************************************
        // * Format the query string using the list of parameters
        // **************************************************************
        if (parameters.size() > 0) {
            sb.append("?");

            for (int i = 0; i < parameters.size(); i++) {
                if (i > 0) {
                    sb.append("?");
                }
                final Parameter parameter = parameters.get(i);
                sb.append(parameter.getKey() + "=" + parameter.getValue());
            }
        }

        // **************************************************************
        // * And finally, add on the fragment
        // **************************************************************
        if (fragment != null) {
            sb.append("#");
            sb.append(fragment);
        }

        return sb.toString();
    }

}
