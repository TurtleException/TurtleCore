package de.turtle_exception.server;

import de.turtle_exception.client.api.TurtleClient;
import de.turtle_exception.client.api.TurtleClientBuilder;
import de.turtle_exception.client.internal.util.logging.ConsoleHandler;
import de.turtle_exception.client.internal.util.logging.NestedLogger;
import de.turtle_exception.client.internal.util.logging.SimpleFormatter;
import de.turtle_exception.server.cli.ServerCLI;
import de.turtle_exception.server.data.DatabaseProvider;
import de.turtle_exception.server.data.LoginHandler;
import de.turtle_exception.server.event.EntityUpdateListener;
import de.turtle_exception.server.net.NetServer;
import de.turtle_exception.server.util.LogUtil;
import de.turtle_exception.server.util.StatusView;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Properties;
import java.util.Scanner;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;

public class TurtleServer {
    private final StatusView statusView = new StatusView();

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

    private final File configFile = new File(DIR, "server.properties");
    private final Properties config = new Properties();

    private final LoginHandler loginHandler;

    private final ServerCLI cli;

    private TurtleClient turtleClient;

    @SuppressWarnings("ResultOfMethodCallIgnored")
    public TurtleServer() throws Exception {
        this.logger = Logger.getLogger("SERVER");
        this.logger.setUseParentHandlers(false);
        this.logger.addHandler(new ConsoleHandler(new SimpleFormatter()));
        this.logger.addHandler(LogUtil.getFileHandler(new SimpleFormatter()));

        for (Handler handler : this.logger.getHandlers())
            handler.setLevel(Level.ALL);

        configFile.createNewFile();
        this.config.load(new FileReader(configFile));

        // get log level from config or use INFO as default
        this.logger.setLevel(Level.parse(config.getProperty("logLevel", "INFO")));

        this.loginHandler = new LoginHandler(this);

        this.cli = new ServerCLI(this);
    }

    public void run() throws Exception {
        statusView.set(StatusView.INIT);

        logger.log(Level.INFO, "Initializing TurtleClient...");
        NetServer netServer = new NetServer(this, getPort());
        this.turtleClient = new TurtleClientBuilder()
                .setNetworkAdapter(netServer)
                .setProvider(new DatabaseProvider(new File(DIR, "data")))
                .setLogger(new NestedLogger("TurtleClient", logger))
                .addListeners(new EntityUpdateListener(netServer))
                .setAutoFillCache(true)
                .build();

        logger.log(Level.INFO, "Cached " + turtleClient.getTurtles().size() + " turtles.");

        /* RUNNING */

        statusView.set(StatusView.RUNNING);
        logger.log(Level.INFO, "Startup done.");

        Scanner scanner = new Scanner(System.in);
        while (statusView.get() == StatusView.RUNNING)
            if (scanner.hasNextLine())
                cli.handle(scanner.nextLine());
        scanner.close();

        logger.log(Level.WARNING, "Main loop has been interrupted.");

        this.shutdown();
    }

    /**
     * Await execution of final tasks, proper shutdown of all active tasks and suspend all active threads.
     */
    private void shutdown() throws Exception {
        if (statusView.get() <= StatusView.RUNNING)
            throw new IllegalStateException("Cannot shutdown while main loop is still running. Call exit() first!");

        logger.log(Level.INFO, "Shutting down...");

        logger.log(Level.INFO, "Stopping TurtleClient...");
        this.turtleClient.shutdown();

        logger.log(Level.ALL, "OK bye.");
    }

    /* - - - */

    private int getPort() throws IllegalArgumentException {
        String str = config.getProperty("port");

        if (str == null) {
            config.setProperty("port", "null");
            try {
                config.store(new FileWriter(configFile, true), null);
            } catch (IOException ignored) { }
            throw new IllegalArgumentException("Invalid port: null. Please change the server port in server.properties.");
        }

        try {
            return Integer.parseInt(str);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Invalid port: " + str);
        }
    }

    /* - - - */

    public StatusView getStatus() {
        return statusView;
    }

    public Logger getLogger() {
        return logger;
    }

    public Properties getConfig() {
        return config;
    }

    public TurtleClient getClient() {
        return turtleClient;
    }

    public LoginHandler getLoginHandler() {
        return loginHandler;
    }
}
