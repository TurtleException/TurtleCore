package de.turtle_exception.server.net;

import de.turtle_exception.client.internal.net.NetworkAdapter;
import de.turtle_exception.client.internal.util.version.IllegalVersionException;
import de.turtle_exception.client.internal.util.version.Version;
import de.turtle_exception.server.Main;
import org.jetbrains.annotations.NotNull;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.logging.Level;

class LoginHandler extends Thread {
    private final InternalServer internalServer;
    private final Socket client;

    private final PrintWriter    out;
    private final BufferedReader in;

    public LoginHandler(InternalServer internalServer, Socket client) throws IOException {
        super();

        this.internalServer = internalServer;
        this.client = client;

        this.out = new PrintWriter(client.getOutputStream(), true);
        this.in  = new BufferedReader(new InputStreamReader(client.getInputStream()));

        this.setDaemon(true);
        this.start();
    }

    @Override
    public void run() {
        try {
            String rawVersion = this.get("VERSION");
            Version version;
            try {
                version = Version.parse(rawVersion);
            } catch (IllegalVersionException e) {
                this.out.println("ERROR VERSION ILLEGAL");
                throw e;
            }
            if (!version.equals(Main.VERSION)) {
                this.out.println("ERROR VERSION INCOMPATIBLE");
                throw new IllegalVersionException("Incompatible versions: Server " + Main.VERSION + ", Client " + version);
            }


            String login = this.get("LOGIN");
            String pass  = internalServer.getPass(login);

            if (pass == null) {
                this.out.println("ERROR LOGIN ILLEGAL");
                throw new IllegalArgumentException("Illegal login");
            }

            if (!internalServer.registerLogin(login)) {
                this.out.println("ERROR LOGIN TAKEN");
                throw new IllegalArgumentException("Already logged in");
            }

            this.internalServer.clients.add(new VirtualClient(internalServer, client, login, pass));

            this.out.println(NetworkAdapter.LOGGED_IN);
        } catch (IOException | IllegalArgumentException | IllegalVersionException e) {
            internalServer.getLogger().log(Level.WARNING, "Client login failed: " + client.getInetAddress(), e);

            try {
                this.in.close();
                this.out.close();
                this.client.close();
            } catch (IOException e1) {
                internalServer.getLogger().log(Level.WARNING, "Could not close connection: " + client.getInetAddress(), e1);
            }
        }

        // TODO: necessary?
        this.interrupt();
    }

    private @NotNull String get(@NotNull String command) throws IOException, IllegalArgumentException {
        this.out.println(command);
        String s = this.in.readLine();

        if (!s.startsWith(command + " "))
            throw new IllegalArgumentException("Expected " + command + " but received: " + s);

        return s.substring(command.length() + 1);
    }
}
