package com.rsmaxwell.utilities.version;

public class Version {

	// buildID function
	public static String buildID() {		
		return "<BUILD_ID>";
	}

	// buildDate function
	public static String buildDate() {
		return "<TIMESTAMP>";
	}

	// gitCommit function
	public static String  gitCommit() {
		return "<GIT_COMMIT>";
	}

	// gitBranch function
	public static String  gitBranch() {
		return "<GIT_BRANCH>";
	}

	// gitURL function
	public static String  gitURL() {
		return "<GIT_URL>";
	}
}
