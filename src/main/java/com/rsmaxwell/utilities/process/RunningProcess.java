package com.rsmaxwell.utilities.process;

/**
 *
 */
public class RunningProcess {

    private final ProcessBuilder processBuilder;
    private final Process process;
    private final String title;
    private final InputThread standardInputThread;
    private final OutputThread standardOutputThread;
    private final OutputThread standardErrorThread;

    /**
     * @param processBuilder
     * @param process
     * @param title
     * @param standardInputThread
     * @param standardOutputThread
     * @param standardErrorThread
     */
    public RunningProcess(
        final ProcessBuilder processBuilder,
        final Process process,
        final String title,
        final InputThread standardInputThread,
        final OutputThread standardOutputThread,
        final OutputThread standardErrorThread) {

        this.processBuilder = processBuilder;
        this.process = process;
        this.title = title;
        this.standardInputThread = standardInputThread;
        this.standardOutputThread = standardOutputThread;
        this.standardErrorThread = standardErrorThread;
    }

    /**
     * @return the process
     */
    public ProcessBuilder getProcessBuilder() {
        return processBuilder;
    }

    /**
     * @return the process
     */
    public Process getProcess() {
        return process;
    }

    /**
     * @return the process
     */
    public String getTitle() {
        return title;
    }

    /**
     * @return the standardInputThread
     */
    public InputThread getStandardInputThread() {
        return standardInputThread;
    }

    /**
     * @return the standardOutputThread
     */
    public OutputThread getStandardOutputThread() {
        return standardOutputThread;
    }

    /**
     * @return the standardErrorThread
     */
    public OutputThread getStandardErrorThread() {
        return standardErrorThread;
    }

    /**
   * 
   */
    public void destroy() {
        process.destroy();
    }

}
