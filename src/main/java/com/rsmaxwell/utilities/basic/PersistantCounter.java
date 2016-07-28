package com.rsmaxwell.utilities.basic;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 *
 */
public class PersistantCounter {

    private File file;

    /**
     * @param afile
     */
    public PersistantCounter(File directory) {
        this.file = new File(directory, "counter.id");
    }

    /**
     * @throws IOException
     * @throws FileNotFoundException
     */
    public synchronized int incrementAndGet() throws FileNotFoundException, IOException {

        int id = 0;
        try (InputStream in = new FileInputStream(file); DataInputStream is = new DataInputStream(in)) {
            id = 1 + is.readInt();
        }
        catch (FileNotFoundException e) {
            // ignore
        }

        try (OutputStream out = new FileOutputStream(file); DataOutputStream os = new DataOutputStream(out)) {
            os.writeInt(id);
        }

        return id;
    }

}
