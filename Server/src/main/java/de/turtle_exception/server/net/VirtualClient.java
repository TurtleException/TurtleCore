package de.turtle_exception.server.net;

import de.turtle_exception.core.net.ConnectionStatus;
import de.turtle_exception.core.net.NetworkAdapter;
import de.turtle_exception.core.net.route.Routes;
import de.turtle_exception.core.util.AsyncLoopThread;
import de.turtle_exception.core.util.logging.NestedLogger;
import de.turtle_exception.server.net.handlers.GroupHandler;
import de.turtle_exception.server.net.handlers.TicketHandler;
import de.turtle_exception.server.net.handlers.UserHandler;
import org.jetbrains.annotations.NotNull;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.logging.Level;

/** A virtual client on the server-side of the application. Used as a communication interface. */
public class VirtualClient extends NetworkAdapter {
    private final InternalServer internalServer;
    private final Socket socket;
    private final PrintWriter out;
    private final BufferedReader in;

    VirtualClient(InternalServer internalServer, Socket socket, @NotNull String login, @NotNull String pass) throws IOException {
        super(internalServer.getServer(), new NestedLogger("Client#" + login, internalServer.getLogger()), login, pass);
        this.internalServer = internalServer;
        this.socket = socket;

        this.out = new PrintWriter(socket.getOutputStream(), true);
        this.in = new BufferedReader(new InputStreamReader(socket.getInputStream()));


        /* --- GROUP */
        GroupHandler groupHandler = new GroupHandler(this);
        this.registerHandler(Routes.Group.GET,     groupHandler);
        this.registerHandler(Routes.Group.GET_ALL, groupHandler);
        this.registerHandler(Routes.Group.DEL,     groupHandler);
        this.registerHandler(Routes.Group.CREATE,  groupHandler);
        this.registerHandler(Routes.Group.MODIFY,  groupHandler);
        this.registerHandler(Routes.Group.ADD_USER, groupHandler);
        this.registerHandler(Routes.Group.DEL_USER, groupHandler);

        /* --- USER */
        UserHandler userHandler = new UserHandler(this);
        this.registerHandler(Routes.User.GET,     userHandler);
        this.registerHandler(Routes.User.GET_ALL, userHandler);
        this.registerHandler(Routes.User.DEL,     userHandler);
        this.registerHandler(Routes.User.CREATE,  userHandler);
        this.registerHandler(Routes.User.MODIFY,  userHandler);
        this.registerHandler(Routes.User.ADD_DISCORD, userHandler);
        this.registerHandler(Routes.User.DEL_DISCORD, userHandler);
        this.registerHandler(Routes.User.ADD_MINECRAFT, userHandler);
        this.registerHandler(Routes.User.DEL_MINECRAFT, userHandler);

        /* --- TICKET */
        TicketHandler ticketHandler = new TicketHandler(this);
        this.registerHandler(Routes.Ticket.GET,     ticketHandler);
        this.registerHandler(Routes.Ticket.GET_ALL, ticketHandler);
        this.registerHandler(Routes.Ticket.DEL,     ticketHandler);
        this.registerHandler(Routes.Ticket.CREATE,  ticketHandler);
        this.registerHandler(Routes.Ticket.MODIFY,  ticketHandler);
        this.registerHandler(Routes.Ticket.ADD_TAG, ticketHandler);
        this.registerHandler(Routes.Ticket.DEL_TAG, ticketHandler);
        this.registerHandler(Routes.Ticket.ADD_USER, ticketHandler);
        this.registerHandler(Routes.Ticket.DEL_USER, ticketHandler);


        this.receiver = new AsyncLoopThread(() -> status != ConnectionStatus.DISCONNECTED, () -> {
            try {
                this.handleInbound(in.readLine());
            } catch (IOException e) {
                logger.log(Level.WARNING, "Could not read input from client.", e);
            }
        });
    }

    /* - - - */
    @Override
    public void stop() throws IOException {
        // TODO

        this.quit();
    }

    @Override
    protected void quit() throws IOException {
        // TODO
    }

    @Override
    protected void send(@NotNull String msg) {
        this.out.println(msg);
    }

    /* - - - */

    public @NotNull String getIdentifier() {
        return login;
    }

    void closeSocket() throws IOException {
        status = ConnectionStatus.DISCONNECTED;
        this.in.close();
        this.out.close();
        this.socket.close();
    }

    public @NotNull ConnectionStatus getStatus() {
        return status;
    }

    public InternalServer getInternalServer() {
        return internalServer;
    }
}
