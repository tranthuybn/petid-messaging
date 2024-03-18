package vn.supperapp.apigw.messaging.clients;

import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;

public class IdleConnectionMonitor implements Runnable {

    final static Logger logger = LoggerFactory.getLogger(IdleConnectionMonitor.class);

    private final PoolingHttpClientConnectionManager cm;
    // Use a BlockingQueue to stop everything.
    private final BlockingQueue<Stop> stopSignal = new ArrayBlockingQueue<>(1);

    public IdleConnectionMonitor(PoolingHttpClientConnectionManager cm) {
        this.cm = cm;
    }

    public void run() {
        try {
            logger.info("#run - check idle");
            // Holds the stop request that stopped the process.
            Stop stopRequest;
            // Every 5 seconds.
            while ((stopRequest = stopSignal.poll(5, TimeUnit.SECONDS)) == null) {
                logger.info("#run - close expired and idle connection");
                // Close expired connections
                cm.closeExpiredConnections();
                // Optionally, close connections that have been idle too long.
                cm.closeIdleConnections(60, TimeUnit.SECONDS);
            }
            // Acknowledge the stop request.
            stopRequest.stopped();
        } catch (InterruptedException ex) {
            // terminate
        }
    }

    // Pushed up the queue.
    private static class Stop {

        // The return queue.

        private final BlockingQueue<Stop> stop = new ArrayBlockingQueue<Stop>(1);

        // Called by the process that is being told to stop.
        public void stopped() {
            // Push me back up the queue to indicate we are now stopped.
            stop.add(this);
        }

        // Called by the process requesting the stop.
        public void waitForStopped() throws InterruptedException {
            // Wait until the callee acknowledges that it has stopped.
            stop.take();
        }

    }

    public void shutdown() throws InterruptedException, IOException {
        // Signal the stop to the thread.
        Stop stop = new Stop();
        stopSignal.add(stop);
        // Wait for the stop to complete.
        stop.waitForStopped();
        // Close the connection manager.
        cm.close();
    }
}
