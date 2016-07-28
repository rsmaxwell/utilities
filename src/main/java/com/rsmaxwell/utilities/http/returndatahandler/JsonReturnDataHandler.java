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

import java.util.logging.Logger;

import com.google.gson.JsonParser;

/**
 * 
 */
public class JsonReturnDataHandler implements ReturnDataHandler {

	private final static String className = JsonReturnDataHandler.class.getName();
	private final static Logger logger = Logger.getLogger(className);

	/**
	 * @return response
	 */
	@Override
	public String getUrlExtension() {
		return ".json";
	}

	/**
	 * Attempts to parse a String as a JsonElement.
	 * 
	 * @param response
	 * @return response
	 */
	@Override
	public Object checkResponse(final String response) throws Exception {
		final String methodName = "checkResponse";
		logger.entering(className, methodName);

		Object objectResponse = new JsonParser().parse(response);

		logger.exiting(className, methodName, objectResponse);
		return objectResponse;
	}
}
