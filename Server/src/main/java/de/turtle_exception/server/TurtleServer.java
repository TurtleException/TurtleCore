package de.turtle_exception.server;

import de.turtle_exception.client.api.TurtleClient;
import de.turtle_exception.client.api.TurtleClientBuilder;
import de.turtle_exception.client.internal.util.logging.NestedLogger;
import de.turtle_exception.client.internal.util.logging.SimpleFormatter;
import de.turtle_exception.server.data.DatabaseProvider;
import de.turtle_exception.server.event.EntityGroupListener;
import de.turtle_exception.server.event.EntityTicketListener;
import de.turtle_exception.server.event.EntityUserListener;
import de.turtle_exception.server.net.NetServer;
import de.turtle_exception.server.util.LogUtil;
import de.turtle_exception.server.util.Status;

import java.io.File;
import java.io.FileReader;
import java.net.URISyntaxException;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

public class TurtleServer {
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

    private TurtleClient turtleClient;

    public TurtleServer() throws Exception {
        this.logger = Logger.getLogger("SERVER");
        this.logger.addHandler(LogUtil.getFileHandler(new SimpleFormatter()));

        this.config.load(new FileReader(new File(DIR, "server.properties")));
    }

    @SuppressWarnings("StatementWithEmptyBody")
    public void run() throws Exception {
        status.set(Status.INIT);

        logger.log(Level.INFO, "Initializing TurtleClient...");
        NetServer netServer = new NetServer(this, getPort());
        this.turtleClient = new TurtleClientBuilder()
                .setNetworkAdapter(netServer)
                .setProvider(new DatabaseProvider(new File(DIR, "data")))
                .setLogger(new NestedLogger("TurtleClient", logger))
                .addListeners(
                        new EntityGroupListener(netServer),
                        new EntityTicketListener(netServer),
                        new EntityUserListener(netServer)
                )
                .build();

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

        logger.log(Level.INFO, "Stopping TurtleClient...");
        this.turtleClient.shutdown();

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

    public TurtleClient getClient() {
        return turtleClient;
    }
}
