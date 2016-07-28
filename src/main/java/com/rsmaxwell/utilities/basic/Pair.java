// ===========================================================================
//
// <copyright
// notice="oco-source"
// pids=""
// years="2013,2014"
// crc="3907455194" >
// IBM Confidential
//
// OCO Source Materials
//
//
//
// (C) Copyright IBM Corp. 2013, 2014
//
// The source code for the program is not published
// or otherwise divested of its trade secrets,
// irrespective of what has been deposited with the
// U.S. Copyright Office.
// </copyright>
//
// ===========================================================================

package com.rsmaxwell.utilities.basic;

/**
 * A class encapsulating a pair of objects.
 * 
 * @param <K>
 * @param <V>
 */
public class Pair<K, V> {

    private final K element0;
    private final V element1;

    /**
     * Method for creating a new Pair of objects.
     * 
     * @param element0
     *            - one of two objects in this Pair
     * @param element1
     *            - one of two objects in this Pair
     * @return - a new pair of objects
     */
    public static <K, V> Pair<K, V> createPair(final K element0, final V element1) {
        return new Pair<K, V>(element0, element1);
    }

    /**
     * Public constructor of class Pair.
     * 
     * @param element0
     *            - one of two objects in this Pair
     * @param element1
     *            - one of two objects in this Pair
     */
    public Pair(final K element0, final V element1) {
        this.element0 = element0;
        this.element1 = element1;
    }

    /**
     * Accessor method for one of the elements of class Pair.
     * 
     * @return element0 - the object which was the first element specified when the Pair was
     *         created.
     */
    public K getElement0() {
        return element0;
    }

    /**
     * Accessor method for one of the elements of class Pair.
     * 
     * @return element1 - the object which was the second element specified when the Pair was
     *         created.
     */
    public V getElement1() {
        return element1;
    }

}
