//import java.io.*;
//import java.net.*;
//
//public class WebWorker extends Thread {
///*
//  //This is the core web/download i/o code...
// 	void download() {
//        InputStream input = null;
//        StringBuilder contents = null;
//        try {
//            URL url = new URL(urlString);
//            URLConnection connection = url.openConnection();
//
//            // Set connect() to throw an IOException
//            // if connection does not succeed in this many msecs.
//            connection.setConnectTimeout(5000);
//
//            connection.connect();
//            input = connection.getInputStream();
//
//            BufferedReader reader = new BufferedReader(new InputStreamReader(input));
//
//            char[] array = new char[1000];
//            int len;
//            contents = new StringBuilder(1000);
//            while ((len = reader.read(array, 0, array.length)) > 0) {
//                contents.append(array, 0, len);
//                Thread.sleep(100);
//            }
//
//            // Successful download if we get here
//
//        }
//        // Otherwise control jumps to a catch...
//        catch (MalformedURLException ignored) {
//        } catch (InterruptedException exception) {
//            // YOUR CODE HERE
//            // deal with interruption
//        } catch (IOException ignored) {
//        }
//        // "finally" clause, to close the input stream
//        // in any case
//        finally {
//            try {
//                if (input != null) input.close();
//            } catch (IOException ignored) {
//            }
//        }
//    }
//*/
//}

import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.Semaphore;

public class WebWorker extends Thread {
    private final String url;
    private final int rowIndex;
    private final WebFrame frame;
    private final Semaphore gate;

    public WebWorker(String url, int rowIndex, WebFrame frame, Semaphore gate) {
        this.url = url;
        this.rowIndex = rowIndex;
        this.frame = frame;
        this.gate = gate;
    }
    /*
     * The main execution method for the thread.
     * Attempts to download content from the specified URL.
     * If successful or an error/interrupt occurs, reports the result to the frame.
     * Releases a semaphore permit to signal that this worker has completed.
     */

    @Override
    public void run() {
        String result;
        try {
            result = download();
        } catch (InterruptedException e) {
            result = "interrupted";
        } catch (Exception e) {
            result = "err";
        }
        frame.reportFinished(result, rowIndex);
        gate.release();
    }

    /*
     * Downloads content from the specified URL.
     * Measures time taken and total bytes read.
     * Simulates latency by sleeping after each read.
     * Throws InterruptedException if the thread is interrupted.
     */
    private String download() throws Exception {
        if (isInterrupted()) throw new InterruptedException();

        long startTime = System.currentTimeMillis();
        URL target = new URL(url);
        URLConnection connection = target.openConnection();
        connection.setConnectTimeout(5000);
        connection.connect();

        int totalBytes = 0;
        try (InputStream input = connection.getInputStream()) {
            byte[] buffer = new byte[1024];
            int bytesRead;

            while ((bytesRead = input.read(buffer)) > 0) {
                if (isInterrupted()) throw new InterruptedException();
                totalBytes += bytesRead;
                Thread.sleep(100); // Simulate latency
            }
        }

        long endTime = System.currentTimeMillis();
        String time = new SimpleDateFormat("HH:mm:ss").format(new Date(startTime));
        return time + "   " + (endTime - startTime) + "ms   " + totalBytes + "bytes";
    }
}
