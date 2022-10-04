package de.turtle_exception.core.server.net.route;

import de.turtle_exception.core.core.net.message.InboundMessage;
import de.turtle_exception.core.core.net.route.Errors;
import de.turtle_exception.core.core.net.route.Routes;
import de.turtle_exception.core.core.util.ResponseHelper;
import de.turtle_exception.core.core.util.StringUtil;
import de.turtle_exception.core.server.Main;
import de.turtle_exception.core.server.TurtleServer;

import java.util.UUID;
import java.util.function.Consumer;
import java.util.logging.Level;

public class ServerRouteFinalizers {
    public static final Consumer<InboundMessage> OK    = in -> { /* terminating */ };
    public static final Consumer<InboundMessage> ERROR = in -> {
        // this is for debug purposes (relevant errors should be reported in processing)
        server().getLogger().log(Level.FINE, "Received error: " + in.getRoute().content());
    };
    public static final Consumer<InboundMessage> QUIT  = in -> {
        // TODO: missing some sort of #getVirtualClient() in InboundMessage
    };

    public static class Content {
        public static class User {
            public static final Consumer<InboundMessage> GET_ALL = in -> {
                ResponseHelper.tryResponse(in, Routes.Content.User.GET_ALL_R, () -> server().getDataService().getUsers().getAsString(), Errors.DATA_SERVICE_INTERNAL);
            };
            public static final Consumer<InboundMessage> GET     = in -> {
                Long id = ResponseHelper.parseLong(in);
                if (id != null)
                    ResponseHelper.tryResponse(in, Routes.Content.User.GET_R, () -> server().getDataService().getUser(id).getAsString(), Errors.DATA_SERVICE_INTERNAL);
            };
            public static final Consumer<InboundMessage> DEL     = in -> {
                Long id = ResponseHelper.parseLong(in);
                if (id != null)
                    ResponseHelper.tryOK(in, () -> {
                        server().getDataService().deleteUser(id);
                        return null;
                    }, Errors.EXCEPTION);
            };

            public static final Consumer<InboundMessage> MOD_NAME = in -> {
                RouteHelper.modifyUser(in, "name");
            };

            public static final Consumer<InboundMessage> GROUP_JOIN  = in -> {
                String str = in.getRoute().content();
                Long user  = StringUtil.parseLong(StringUtil.getToken(str, " ", 0));
                Long group = StringUtil.parseLong(StringUtil.getToken(str, " ", 1));

                if (user == null || group == null) {
                    ResponseHelper.respondError(in, Errors.BAD_REQUEST);
                    return;
                }

                ResponseHelper.tryOK(in, () -> {
                    server().getDataService().addUserGroup(user, group);
                    return null;
                }, Errors.DATA_SERVICE_INTERNAL);
            };
            public static final Consumer<InboundMessage> GROUP_LEAVE = in -> {
                String str = in.getRoute().content();
                Long user  = StringUtil.parseLong(StringUtil.getToken(str, " ", 0));
                Long group = StringUtil.parseLong(StringUtil.getToken(str, " ", 1));

                if (user == null || group == null) {
                    ResponseHelper.respondError(in, Errors.BAD_REQUEST);
                    return;
                }

                ResponseHelper.tryOK(in, () -> {
                    server().getDataService().delUserGroup(user, group);
                    return null;
                }, Errors.DATA_SERVICE_INTERNAL);
            };

            public static final Consumer<InboundMessage> DISCORD_GET = in -> {
                String str   = in.getRoute().content();
                Long user    = StringUtil.parseLong(StringUtil.getToken(str, " ", 0));
                Long discord = StringUtil.parseLong(StringUtil.getToken(str, " ", 1));

                if (user == null || discord == null) {
                    ResponseHelper.respondError(in, Errors.BAD_REQUEST);
                    return;
                }

                ResponseHelper.tryOK(in, () -> {
                    server().getDataService().addUserDiscord(user, discord);
                    return null;
                }, Errors.DATA_SERVICE_INTERNAL);
            };
            public static final Consumer<InboundMessage> DISCORD_SET = in -> {
                String str   = in.getRoute().content();
                Long user    = StringUtil.parseLong(StringUtil.getToken(str, " ", 0));
                Long discord = StringUtil.parseLong(StringUtil.getToken(str, " ", 1));

                if (user == null || discord == null) {
                    ResponseHelper.respondError(in, Errors.BAD_REQUEST);
                    return;
                }

                ResponseHelper.tryOK(in, () -> {
                    server().getDataService().delUserDiscord(user, discord);
                    return null;
                }, Errors.DATA_SERVICE_INTERNAL);
            };

            public static final Consumer<InboundMessage> MINECRAFT_GET = in -> {
                String str     = in.getRoute().content();
                Long user      = StringUtil.parseLong(StringUtil.getToken(str, " ", 0));
                UUID minecraft = StringUtil.parseUUID(StringUtil.getToken(str, " ", 1));

                if (user == null || minecraft == null) {
                    ResponseHelper.respondError(in, Errors.BAD_REQUEST);
                    return;
                }

                ResponseHelper.tryOK(in, () -> {
                    server().getDataService().addUserMinecraft(user, minecraft);
                    return null;
                }, Errors.DATA_SERVICE_INTERNAL);
            };
            public static final Consumer<InboundMessage> MINECRAFT_SET = in -> {
                String str     = in.getRoute().content();
                Long user      = StringUtil.parseLong(StringUtil.getToken(str, " ", 0));
                UUID minecraft = StringUtil.parseUUID(StringUtil.getToken(str, " ", 1));

                if (user == null || minecraft == null) {
                    ResponseHelper.respondError(in, Errors.BAD_REQUEST);
                    return;
                }

                ResponseHelper.tryOK(in, () -> {
                    server().getDataService().delUserMinecraft(user, minecraft);
                    return null;
                }, Errors.DATA_SERVICE_INTERNAL);
            };
        }

        public static class Group {
            public static final Consumer<InboundMessage> GET_ALL = in -> {
                ResponseHelper.tryResponse(in, Routes.Content.User.GET_ALL_R, () -> server().getDataService().getUsers().getAsString(), Errors.DATA_SERVICE_INTERNAL);
            };
            public static final Consumer<InboundMessage> GET     = in -> {
                Long id = ResponseHelper.parseLong(in);
                if (id != null)
                    ResponseHelper.tryResponse(in, Routes.Content.User.GET_R, () -> server().getDataService().getUser(id).getAsString(), Errors.DATA_SERVICE_INTERNAL);
            };
            public static final Consumer<InboundMessage> DEL     = in -> {
                Long id = ResponseHelper.parseLong(in);
                if (id != null)
                    ResponseHelper.tryOK(in, () -> {
                        server().getDataService().deleteGroup(id);
                        return null;
                    }, Errors.EXCEPTION);
            };

            public static final Consumer<InboundMessage> MOD_NAME = in -> {
                RouteHelper.modifyGroup(in, "name");
            };
        }
    }

    private static TurtleServer server() {
        return Main.singleton;
    }
}
