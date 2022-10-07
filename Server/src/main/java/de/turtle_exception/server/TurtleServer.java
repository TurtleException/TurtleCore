package de.turtle_exception.server;

import de.turtle_exception.core.TurtleCore;
import de.turtle_exception.core.util.logging.SimpleFormatter;
import de.turtle_exception.server.data.DataService;
import de.turtle_exception.server.data.DataServiceProvider;
import de.turtle_exception.server.net.InternalServer;
import de.turtle_exception.server.util.LogUtil;
import de.turtle_exception.server.util.Status;

import java.io.File;
import java.io.FileReader;
import java.net.URISyntaxException;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

public class TurtleServer extends TurtleCore {
    private final Status status = new Status();

    /** The root logger of this server */
    private final Logger logger;

    /** Directory in which the JAR is located. */
    public static final File DIR;
    static {
        File f = null;
        try {
            f = new File(Main.class.getProtectionDomain().getCodeSource().getLocation().toURI()).getParentFile();
        } catch (URISyntaxException e) {
            System.out.println("Failed to declare directory.");
            e.printStackTrace();
        }
        DIR = f;
    }

    private final Properties config = new Properties();

    private InternalServer internalServer;
    private DataService    dataService;

    public TurtleServer() throws Exception {
        this.logger = Logger.getLogger("SERVER");
        this.logger.addHandler(LogUtil.getFileHandler(new SimpleFormatter()));

        this.config.load(new FileReader(new File(DIR, "server.properties")));
    }

    @SuppressWarnings("StatementWithEmptyBody")
    public void run() throws Exception {
        status.set(Status.INIT);

        logger.log(Level.INFO, "Initializing InternalServer...");
        this.internalServer = new InternalServer(this, getPort());

        logger.log(Level.INFO, "Initializing DataService...");
        this.dataService = new DataServiceProvider(this).get();

        logger.log(Level.INFO, "Starting InternalServer...");
        this.internalServer.start();

        /* RUNNING */

        status.set(Status.RUNNING);
        logger.log(Level.INFO, "Startup done.");

        while (status.get() == Status.RUNNING) {
            // TODO: CLI
        }

        logger.log(Level.WARNING, "Main loop has been interrupted.");

        this.shutdown();
    }

    /**
     * Await execution of final tasks, proper shutdown of all active tasks and suspend all active threads.
     */
    private void shutdown() throws Exception {
        if (status.get() <= Status.RUNNING)
            throw new IllegalStateException("Cannot shutdown while main loop is still running. Call exit() first!");

        logger.log(Level.INFO, "Shutting down...");

        logger.log(Level.INFO, "Stopping InternalServer...");
        this.internalServer.stop();

        logger.log(Level.INFO, "Stopping DataService...");
        this.dataService.stop();

        logger.log(Level.ALL, "OK bye.");
    }

    /* - - - */

    private int getPort() throws IllegalArgumentException {
        String str = config.getProperty("port");
        try {
            return Integer.parseInt(str);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Invalid port: " + str);
        }
    }

    /* - - - */

    public Logger getLogger() {
        return logger;
    }

    public Properties getConfig() {
        return config;
    }

    public DataService getDataService() {
        return dataService;
    }
}
