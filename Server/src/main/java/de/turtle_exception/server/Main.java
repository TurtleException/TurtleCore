package de.turtle_exception.server;

import de.turtle_exception.client.internal.util.version.IllegalVersionException;
import de.turtle_exception.client.internal.util.version.Version;

/**
 * Technical main class used to initialize {@link TurtleServer} and to prevent accidentally initializing multiple
 * instances. In that case {@link Main#main(String[])} throws an {@link IllegalStateException}. While multiple
 * instances might not necessarily be a problem they are not permitted for simplification purposes.
 */
public class Main {
    static {
        System.out.print("Starting TurtleServer");
    }

    public static TurtleServer singleton;
    public static final Version VERSION = Version.retrieveFromResources(Main.class, "server");

    /**
     * Called by the JVM when starting this program.
     */
    public static void main(String[] args) throws Exception {
        if (singleton != null)
            throw new IllegalStateException("Cannot initialize multiple times.");

        /* VERSION*/
        if (VERSION == null)
            throw new IllegalVersionException("Version may not be null.");
        System.out.printf(" version %s...%n", VERSION);

        singleton = new TurtleServer();
        singleton.run();
    }
}