package de.turtle_exception.core.server;

import de.turtle_exception.core.netcore.TurtleCore;
import de.turtle_exception.core.netcore.net.route.Routes;
import de.turtle_exception.core.netcore.util.logging.SimpleFormatter;
import de.turtle_exception.core.server.net.InternalServer;
import de.turtle_exception.core.server.util.LogUtil;
import de.turtle_exception.core.server.util.Status;

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

    public TurtleServer() throws Exception {
        this.logger = Logger.getLogger("SERVER");
        this.logger.addHandler(LogUtil.getFileHandler(new SimpleFormatter()));

        this.config.load(new FileReader(new File(DIR, "server.properties")));
    }

    @SuppressWarnings("StatementWithEmptyBody")
    public void run() throws Exception {
        status.set(Status.INIT);

        this.logger.log(Level.INFO, "Registering route finalizers...");
        this.routeManager.setLog(logger::log);
        this.routeManager.setRouteFinalizer(Routes.ERROR, inboundMessage -> {
            // this is for debug purposes (relevant errors should be reported in processing)
            logger.log(Level.FINE, "Received error: " + inboundMessage.getRoute().content());
        });
        this.routeManager.setRouteFinalizer(Routes.QUIT, inboundMessage -> {
            // TODO: missing some sort of #getVirtualClient() in InboundMessage
        });
        /* --- NOT HANDLED BY FINALIZERS */
        this.routeManager.setEmptyFinalizer(
                Routes.OK
        );
        /* --- CONTENT / USER */
        this.routeManager.setRouteFinalizer(Routes.Content.User.GET_ALL, inboundMessage -> {
            // TODO
        });
        this.routeManager.setRouteFinalizer(Routes.Content.User.GET, inboundMessage -> {
            // TODO
        });
        this.routeManager.setRouteFinalizer(Routes.Content.User.DEL, inboundMessage -> {
            // TODO
        });
        this.routeManager.setRouteFinalizer(Routes.Content.User.MOD_NAME, inboundMessage -> {
            // TODO
        });
        this.routeManager.setRouteFinalizer(Routes.Content.User.MOD_NAME, inboundMessage -> {
            // TODO
        });
        this.routeManager.setRouteFinalizer(Routes.Content.User.GROUP_JOIN, inboundMessage -> {
            // TODO
        });
        this.routeManager.setRouteFinalizer(Routes.Content.User.GROUP_LEAVE, inboundMessage -> {
            // TODO
        });
        this.routeManager.setRouteFinalizer(Routes.Content.User.DISCORD_GET, inboundMessage -> {
            // TODO
        });
        this.routeManager.setRouteFinalizer(Routes.Content.User.DISCORD_SET, inboundMessage -> {
            // TODO
        });
        this.routeManager.setRouteFinalizer(Routes.Content.User.MINECRAFT_GET, inboundMessage -> {
            // TODO
        });
        this.routeManager.setRouteFinalizer(Routes.Content.User.MINECRAFT_SET, inboundMessage -> {
            // TODO
        });
        /* --- CONTENT / GROUP */
        this.routeManager.setRouteFinalizer(Routes.Content.Group.GET_ALL, inboundMessage -> {
            // TODO
        });
        this.routeManager.setRouteFinalizer(Routes.Content.Group.GET, inboundMessage -> {
            // TODO
        });
        this.routeManager.setRouteFinalizer(Routes.Content.Group.DEL, inboundMessage -> {
            // TODO
        });
        this.routeManager.setRouteFinalizer(Routes.Content.Group.MOD_NAME, inboundMessage -> {
            // TODO
        });

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
    private void shutdown() {
        if (status.get() <= Status.RUNNING)
            throw new IllegalStateException("Cannot shutdown while main loop is still running. Call exit() first!");

        logger.log(Level.INFO, "Shutting down...");

        // TODO

        logger.log(Level.ALL, "OK bye.");
    }

    public Logger getLogger() {
        return logger;
    }
}
