package de.turtle_exception.core.core.net;

/** The current status of a remove connection. */
public enum ConnectionStatus {
    /** The socket connection has been established. */
    CONNECTED,
    /** The server has requested version & login and the client should respond accordingly. */
    LOGIN,
    /** The provided version & login are valid. All traffic is encrypted in this stage. */
    LOGGED_IN,
    /** The socket connection has been closed. */
    DISCONNECTED
}
