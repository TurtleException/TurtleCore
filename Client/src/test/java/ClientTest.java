import de.turtle_exception.client.api.TurtleClient;
import de.turtle_exception.client.api.TurtleClientBuilder;
import de.turtle_exception.client.internal.util.logging.ConsoleHandler;
import de.turtle_exception.client.internal.util.logging.SimpleFormatter;

import java.util.UUID;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ClientTest {
    private final static String HOST  = "localhost";
    private final static int    PORT  = 8346;
    private final static String LOGIN = "login123";
    private final static String PASS  = "!hq&*&wP3q*CTy6R";

    public static void main(String[] args) throws Exception {
        Logger logger = Logger.getLogger("CLIENT");
        logger.setUseParentHandlers(false);
        logger.addHandler(new ConsoleHandler(new SimpleFormatter()));
        logger.setLevel(Level.ALL);
        for (Handler handler : logger.getHandlers())
            handler.setLevel(Level.ALL);

        TurtleClient client = TurtleClientBuilder.createDefault(HOST, PORT, LOGIN, PASS)
                .setLogger(logger)
                .build();

        System.out.println("OK! Creating a user...");

        client.createUser()
                .setMinecraftIds(UUID.randomUUID(), UUID.randomUUID())
                .setDiscordIds(12345L, 74557L, 5436L)
                .setName("helo")
                .complete();

        System.out.println("OK! Shutting down...");

        client.shutdown();

        System.out.println("DONE!");
    }
}
