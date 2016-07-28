// ===========================================================================
//
// <copyright
// notice="oco-source"
// pids=""
// years="2014"
// crc="3150929008" >
// IBM Confidential
//
// OCO Source Materials
//
//
//
// (C) Copyright IBM Corp. 2014
//
// The source code for the program is not published
// or otherwise divested of its trade secrets,
// irrespective of what has been deposited with the
// U.S. Copyright Office.
// </copyright>
//
// ===========================================================================
package com.rsmaxwell.utilities.http.returndatahandler;

/**
 * 
 */
public interface ReturnDataHandler {

	/**
	 * @return response
	 */
	public String getUrlExtension();

	/**
	 * @param response
	 * @return response
	 * @throws Exception
	 */
	public Object checkResponse(String response) throws Exception;
}
