package de.turtle_exception.client.internal.net;

import de.turtle_exception.client.internal.util.version.Version;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.security.auth.login.LoginException;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

public abstract class Handshake extends Thread {
    protected PrintWriter out;
    protected BufferedReader in;

    protected Version version;

    protected final AtomicBoolean done = new AtomicBoolean(false);
    protected final AtomicReference<LoginException> error = new AtomicReference<>();

    protected Handshake(@NotNull Version version) {
        super();
        this.version = version;
        this.setDaemon(true);
    }

    public void setIO(@NotNull PrintWriter out, @NotNull BufferedReader in) {
        this.out = out;
        this.in  = in;
    }

    @Override
    public final void run() {
        try {
            this.onRun();
            String buffer;
            while ((buffer = in.readLine()) != null)
                if (this.onInput(buffer)) break;
        } catch (LoginException e) {
            this.error.set(e);
        } catch (IOException e) {
            this.error.set(new LoginException("Unexpected IO error: " + e.getMessage()));
        } catch (IndexOutOfBoundsException e) {
            this.error.set(new LoginException("Unexpected message substring error: " + e.getMessage()));
        } catch (Exception e) {
            this.error.set(new LoginException("Unknown problem: " + e.getMessage()));
        }
    }

    public void onRun() { }

    public abstract boolean onInput(@NotNull String str) throws LoginException;

    public void await(long timeout, @NotNull TimeUnit unit) throws LoginException {
        final long deadline = unit.toMillis(timeout);

        while (System.currentTimeMillis() < deadline) {
            if (error.get() != null)
                throw error.get();

            if (done.get())
                return;
        }
        this.interrupt();
        throw new LoginException("Timed out while attempting to log in.");
    }

    public @Nullable String getPass() {
        return null;
    }
}
