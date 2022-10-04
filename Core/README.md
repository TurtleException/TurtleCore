# Core
tbd

## Messages and Routes
The main difference between a message and a route is that a message is a representation of a route, including several
meta attributes, while the route itself is only an instruction with parameters & arguments. The message is basically a
wrapper object that can also be used to parse conversations, handle encryption and provide a few helper methods to send
and receive data. A route can be re-used across connections and instances while a message is unique and can only ever be
used by its designated NetworkAdapter.