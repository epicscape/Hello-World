package org.dementhium.tools;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.LinkedList;
import java.util.List;

public class LineCounter {

    private static final String PROJECT_PATH = "./";

    private static final String SECURITY_KEY = "4dcd00bd258e72b18d2c737cf36e7494";

    private static final int PROJECT_ID = 13;

    public static void main(String[] args) {
        System.out.println("Loading files...");
        List<File> files = listRecursive(new File(PROJECT_PATH), new Filter<File>() {
            @Override
            public boolean accept(File t) {
                return t.getName().endsWith(".java") || t.getName().endsWith(".lua") || t.getName().endsWith(".rb") || t.getName().endsWith(".js");
            }
        });
        System.out.println("Parsing line count...");
        int lineCount = 0;
        for (File file : files) {
            lineCount += lineCount(file);
        }
        System.out.println(lineCount + " lines in " + files.size() + " files");
        doUpdate(files.size(), lineCount);
    }

    public static void doUpdate(int fileCount, int lineCount) {
        try {
            URL url = new URL("http://nikkii.us/projectsig/projectinfo.php?set&projectid=" + PROJECT_ID + "&securitykey=" + SECURITY_KEY + "&value=" + fileCount + "," + lineCount);
            BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream()));
            String resp = reader.readLine();
            if (resp.equals("SUCCESS")) {
                System.out.println("Successfully updated.");
            } else {
                System.out.println("Failed to update, check your security key");
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public interface Filter<T> {
        public boolean accept(T t);
    }

    public static LinkedList<File> listRecursive(File file, Filter<File> filter) {
        LinkedList<File> files = new LinkedList<File>();
        for (File f : file.listFiles()) {
            if (f.isDirectory()) {
                files.addAll(listRecursive(f, filter));
            } else {
                if (filter.accept(f))
                    files.add(f);
            }
        }
        return files;
    }

    public static int lineCount(File file) {
        int count = 0;
        try {
            LineNumberReader ln = new LineNumberReader(new FileReader(file));
            while (true) {
                String line = ln.readLine();
                if (line == null)
                    break;
                if (!line.trim().equals(""))
                    count++;
            }
            ln.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return count;
    }
}
