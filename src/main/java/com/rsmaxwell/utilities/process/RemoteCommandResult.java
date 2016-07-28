
package com.rsmaxwell.utilities.process;

/**
 *
 */
public class RemoteCommandResult {

    private final int exitStatus;
    private final String stdout;
    private final String stderr;

    /**
     * @param exitStatus
     * @param stdout
     * @param stderr
     */
    public RemoteCommandResult(int exitStatus, String stdout, String stderr) {
        this.exitStatus = exitStatus;
        this.stdout = stdout;
        this.stderr = stderr;
    }

    /**
     * @return the exitStatus
     */
    public int getExitStatus() {
        return exitStatus;
    }

    /**
     * @return the stdout
     */
    public String getStdout() {
        return stdout;
    }

    /**
     * @return the stderr
     */
    public String getStderr() {
        return stderr;
    }

    /**
     * 
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("{ exitStatus: ");
        sb.append(exitStatus);
        sb.append(", stdout: ");
        sb.append(stdout.trim());
        sb.append(" }");
        return sb.toString();
    }

}
