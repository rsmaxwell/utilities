// ===========================================================================
//
// <copyright
// notice="oco-source"
// pids=""
// years="2013,2015"
// crc="3301759056" >
// IBM Confidential
//
// OCO Source Materials
//
//
//
// (C) Copyright IBM Corp. 2013, 2015
//
// The source code for the program is not published
// or otherwise divested of its trade secrets,
// irrespective of what has been deposited with the
// U.S. Copyright Office.
// </copyright>
//
// ===========================================================================

package com.rsmaxwell.utilities.process;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.rsmaxwell.utilities.basic.ObjectFormatter;

/**
 * General purpose test utilities
 */
public class Utility {

    private final static String className = Utility.class.getName();
    private final static Logger logger = Logger.getLogger(className);

    /**
     * @return the current method name for the caller.
     */
    public static String getMethodName() {
        final StackTraceElement[] stack = (new Throwable()).getStackTrace();
        String methodName = stack[1].getMethodName();

        // Skip over synthetic accessor methods
        if (methodName.equals("access$0")) {
            methodName = stack[2].getMethodName();
        }

        return methodName;
    }

    /**
     * @return 'true' is running on Windows
     */
    public static boolean isWindows() {
        final String osName = System.getProperty("os.name");
        if (osName.startsWith("Windows")) {
            return true;
        }
        return false;
    }

    /**
     * @param name
     * @return executable name
     */
    public static String programName(final String name) {
        final StringBuilder sb = new StringBuilder();
        sb.append(name);
        if (Utility.isWindows()) {
            sb.append(".exe");
        }
        return sb.toString();
    }

    /**
     * @param program
     * @param directory
     * @param options
     * @return result
     * @throws Exception
     */
    public static ProcessBuilder buildCommand(final String program, final File directory, final List<String> options) throws Exception {
        final ProcessBuilder pb = new ProcessBuilder();

        final List<String> command = new ArrayList<String>();
        command.add(program);
        for (final String option : options) {
            command.add(option);
        }
        pb.command(command);
        pb.directory(directory);

        return pb;
    }

    /**
     * @param scriptName
     * @param workingDirectory
     * @param args
     * @param environment
     * @return result
     * @throws ProcessFailedException
     * @throws InterruptedException
     */
    public static ProcessResult runScript(final String scriptName, final File workingDirectory, final List<String> args, final Map<String, String> environment)
                    throws ProcessFailedException, InterruptedException {

        final List<String> command = new ArrayList<String>();

        String extension = null;
        final boolean isWindows = Utility.isWindows();
        if (isWindows) {
            command.add("cmd.exe");
            command.add("/C");
            extension = ".bat";
        }
        else {
            command.add("bash");
            extension = ".sh";
        }
        command.add(scriptName + extension);
        command.addAll(args);

        final ProcessBuilder pb = new ProcessBuilder();
        pb.command(command);
        pb.directory(workingDirectory);

        for (final Entry<String, String> var : environment.entrySet()) {
            pb.environment().put(var.getKey(), var.getValue());
        }

        return Utility.executeAndWait(pb, scriptName);
    }

    private static void logOutput(Level logLevel, String program, ByteArrayOutputStream stdout, ByteArrayOutputStream stderr) {
        if (logger.isLoggable(logLevel))
            try {
                BufferedReader br = new BufferedReader(new InputStreamReader(new ByteArrayInputStream(stdout.toByteArray())));
                String line;
                do {
                    line = br.readLine();
                    if (line != null)
                        logger.log(logLevel, "{0} STDOUT: {1}", new Object[] { program, line });
                }
                while (line != null);
                br.close();
                br = new BufferedReader(new InputStreamReader(new ByteArrayInputStream(stderr.toByteArray())));
                do {
                    line = br.readLine();
                    if (line != null)
                        logger.log(logLevel, "{0} STDERR: {1}", new Object[] { program, line });
                }
                while (line != null);
                br.close();
            }
            catch (Exception e) {
                // Ignore as it's just logging
            }
    }

    /**
     * @param program
     * @param directory
     * @param input
     * @param options
     * @return result
     * @throws Exception
     */
    public static ProcessResult buildExecuteAndWait(final String program, final File directory, final String input, final List<String> options) throws Exception {
        final ProcessBuilder pb = Utility.buildCommand(program, directory, options);
        ByteArrayOutputStream stdout = new ByteArrayOutputStream();
        ByteArrayOutputStream stderr = new ByteArrayOutputStream();
        ProcessResult retval = Utility.executeAndWait(pb, program, input, stdout, stderr);
        logOutput(Level.FINE, program, stdout, stderr);
        return retval;
    }

    /**
     * @param program
     * @param directory
     * @param input
     * @param options
     * @param environment
     * @return result
     * @throws Exception
     */
    public static ProcessResult buildExecuteAndWaitWithEnvSetting(final String program, final File directory, final String input, final List<String> options,
                    final Map<String, String> environment) throws Exception {
        final ProcessBuilder pb = Utility.buildCommand(program, directory, options);
        pb.environment().putAll(environment);
        return Utility.executeAndWait(pb, program, input, new ByteArrayOutputStream(), new ByteArrayOutputStream());

    }

    /**
     * Build and execute a command without waiting for it to finish
     * 
     * @param program
     * @param directory
     * @param input
     * @param options
     * @return process
     * @throws Exception
     */
    public static RunningProcess buildAndExecute(final String program, final File directory, final String input, final List<String> options) throws Exception {
        final ProcessBuilder pb = Utility.buildCommand(program, directory, options);
        InputStream inputStream = null;
        if (input != null) {
            inputStream = new ByteArrayInputStream(input.getBytes());
        }
        return Utility.executeProcess(pb, program, inputStream, new ByteArrayOutputStream(), new ByteArrayOutputStream());
    }

    /**
     * Execute a command in an operating system process.
     * 
     * @param pb
     * @param title
     * @return result.
     * @throws ProcessFailedException
     */
    public static ProcessResult executeAndWait(final ProcessBuilder pb, final String title) throws ProcessFailedException {
        final InputStream input = null;
        return Utility.executeAndWait(pb, title, input, new ByteArrayOutputStream(), new ByteArrayOutputStream());
    }

    /**
     * @param pb
     * @param title
     * @param input
     * @return result
     * @throws ProcessFailedException
     */
    public static ProcessResult executeAndWait(final ProcessBuilder pb, final String title, final String input) throws ProcessFailedException {
        return Utility.executeAndWait(pb, title, input, new ByteArrayOutputStream(), new ByteArrayOutputStream());
    }

    /**
     * Execute a command in an operating system process.
     * 
     * @param pb
     * @param title
     * @param input
     * @param standardOutputStream
     * @param standardErrorStream
     * @return result.
     * @throws ProcessFailedException
     */
    public static ProcessResult executeAndWait(final ProcessBuilder pb, final String title, final String input, final OutputStream standardOutputStream,
                    final OutputStream standardErrorStream) throws ProcessFailedException {

        InputStream inputStream = null;
        if (input != null) {
            inputStream = new ByteArrayInputStream(input.getBytes());
        }

        return Utility.executeAndWait(pb, title, inputStream, standardOutputStream, standardErrorStream);
    }

    /**
     * Execute a command in an operating system process.
     * 
     * @param pb
     * @param inputStream
     * @param title
     * @param standardOutputStream
     * @param standardErrorStream
     * @return RunningProcess
     * @throws ProcessFailedException
     */
    public static ProcessResult executeAndWait(final ProcessBuilder pb, final String title, final InputStream inputStream, final OutputStream standardOutputStream,
                    final OutputStream standardErrorStream) throws ProcessFailedException {

        final RunningProcess runningProcess = Utility.executeProcess(pb, title, inputStream, standardOutputStream, standardErrorStream);
        return Utility.waitForProcess(runningProcess);
    }

    /**
     * Execute a command in an operating system process.
     * 
     * @param pb
     * @param inputStream
     * @param title
     * @param standardOutputStream
     * @param standardErrorStream
     * @return RunningProcess
     * @throws ProcessFailedException
     */
    public static RunningProcess executeProcess(final ProcessBuilder pb, final String title, final InputStream inputStream, final OutputStream standardOutputStream,
                    final OutputStream standardErrorStream) throws ProcessFailedException {

        logger.log(Level.INFO, "command: {0}", ObjectFormatter.format(pb.command()));

        final File workingDirectory = pb.directory();
        if (workingDirectory != null) {
            logger.log(Level.INFO, "workingDirectory: {0}", workingDirectory.getAbsolutePath());
        }

        RunningProcess runningProcess = null;
        try {
            final Process process = pb.start();
            final OutputThread standardOutputThread = new OutputThread(process.getInputStream(), standardOutputStream);
            standardOutputThread.start();
            final OutputThread standardErrorThread = new OutputThread(process.getErrorStream(), standardErrorStream);
            standardErrorThread.start();

            InputThread standardInputThread = null;
            if (inputStream != null) {
                standardInputThread = new InputThread(process.getOutputStream(), inputStream);
                standardInputThread.start();
            }

            runningProcess = new RunningProcess(pb, process, title, standardInputThread, standardOutputThread, standardErrorThread);
        }
        catch (final Exception e) {
            // e.printStackTrace();
            logger.log(Level.SEVERE, "caught exception:", e);
            final ProcessFailedException exception2 = new ProcessFailedException(pb, e);
            throw exception2;
        }

        return runningProcess;
    }

    /**
     * @param runningProcess
     * @return ProcessResult
     * @throws ProcessFailedException
     */
    public static ProcessResult waitForProcess(final RunningProcess runningProcess) throws ProcessFailedException {
        final Process process = runningProcess.getProcess();
        final OutputThread standardOutputThread = runningProcess.getStandardOutputThread();
        final OutputThread standardErrorThread = runningProcess.getStandardErrorThread();
        final InputThread standardInputThread = runningProcess.getStandardInputThread();

        ProcessResult processResult = null;
        try {
            final int exitCode = process.waitFor();
            standardOutputThread.join();
            standardErrorThread.join();

            if (standardInputThread != null) {
                standardInputThread.join();
            }

            processResult = new ProcessResult(exitCode, standardOutputThread, standardErrorThread, standardInputThread);
        }
        catch (final InterruptedException e) {
            logger.log(Level.SEVERE, "caught exception:", e);
            throw new ProcessFailedException(runningProcess.getProcessBuilder(), e);
        }

        return processResult;
    }

    /**
     * @param name
     * @return executable name
     */
    public static String executableName(final String name) {
        final StringBuilder sb = new StringBuilder();
        sb.append(name);
        if (Utility.isWindows()) {
            sb.append(".exe");
        }
        return sb.toString();
    }

    /**
     * Delete files - as a user
     * 
     * @param filename
     * @param username
     * @return completion code
     * @throws Exception
     */
    public static int deleteFilesAsUser(final String filename, final String username) throws Exception {

        String program = null;
        final List<String> argumentList = new ArrayList<String>();

        if (Utility.isWindows()) {
            program = "cmd.exe";
            argumentList.add("/C");
            argumentList.add("del");
        }
        else {
            program = "/usr/bin/sudo";
            argumentList.add("-u");
            argumentList.add(username);
            argumentList.add("rm");
            argumentList.add("-rf");
        }
        argumentList.add(filename);

        final ProcessResult result = Utility.buildExecuteAndWait(program, null, null, argumentList);

        return result.getCompletionCode();
    }

    /**
     * Delete all the files in a directory - as a user
     * <p>
     * NOTE: We run the delete command in a process with the working directory set to prevent the
     * directory itself being deleted
     * 
     * @param directoryname
     * @param username
     * @return completion code
     * @throws Exception
     */
    public static int emptyDirectoryAsUser(final String directoryname, final String username) throws Exception {

        String program = null;
        final List<String> argumentList = new ArrayList<String>();

        if (Utility.isWindows()) {
            program = "cmd.exe";
            argumentList.add("/C");
            argumentList.add("rd");
            argumentList.add("/S");
            argumentList.add("/Q");
        }
        else {
            program = "/usr/bin/sudo";
            argumentList.add("-u");
            argumentList.add(username);
            argumentList.add("rm");
            argumentList.add("-rf");
        }
        argumentList.add(directoryname);

        final File workingDirectory = new File(directoryname);

        /* final ProcessResult result = */Utility.buildExecuteAndWait(program, workingDirectory, null, argumentList);

        return 0; /* Ignore the error due to not deleting the current directory! */
    }

    /**
     * Kill a process
     * 
     * @param pids
     * @throws ProcessFailedException
     */
    public static void killProcess(final List<Integer> pids) throws ProcessFailedException {
        logger.log(Level.FINE, "killProcess({0})", new Object[] { pids });

        if (Utility.isWindows()) {
            for (Integer pid : pids) {
                final List<String> command = new ArrayList<String>();
                command.add("taskkill");
                command.add("/f");
                command.add("/pid");
                command.add(Integer.toString(pid));

                final ProcessBuilder pb = new ProcessBuilder();
                pb.command(command);
                ProcessResult result = Utility.executeAndWait(pb, "killProcess");
                // System.out.println(result.toString());
            }
        }
        else {
            final List<String> command = new ArrayList<String>();
            command.add("/usr/bin/sudo");
            command.add("kill");
            command.add("-9");

            for (Integer pid : pids) {
                command.add(Integer.toString(pid));
            }

            final ProcessBuilder pb = new ProcessBuilder();
            pb.command(command);
            ProcessResult result = Utility.executeAndWait(pb, "killProcess");
            // System.out.println(result.toString());
        }
    }

    /**
     * @param arguments
     * @return converted path
     * @throws Exception
     */
    public static String cygpath(final String... arguments) throws Exception {

        final List<String> args = new ArrayList<String>();
        for (final String argument : arguments) {
            args.add(argument);
        }

        String program = "C:/cygwin/bin/cygpath.exe";
        ProcessResult result = Utility.buildExecuteAndWait(program, null, null, args);

        int compCode = result.getCompletionCode();
        if (compCode != 0) {
            throw new Exception("cygpath " + ObjectFormatter.format(args) + " returned " + result.toString());
        }

        return result.getStandardOut().toString().trim();
    }

}
