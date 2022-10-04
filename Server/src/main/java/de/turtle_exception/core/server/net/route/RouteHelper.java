package de.turtle_exception.core.server.net.route;

import de.turtle_exception.core.core.net.message.InboundMessage;
import de.turtle_exception.core.core.net.route.Errors;
import de.turtle_exception.core.core.util.ResponseHelper;
import de.turtle_exception.core.core.util.StringUtil;
import de.turtle_exception.core.server.Main;
import org.jetbrains.annotations.NotNull;

class RouteHelper {
    public static void modifyGroup(@NotNull InboundMessage in, @NotNull String key) {
        try {
            String content = in.getRoute().content();
            String groupSt = StringUtil.getToken(content, " ", 0);
            Long   groupId = StringUtil.parseLong(groupSt);

            if (groupId == null)
                throw new NullPointerException();

            String value = content.substring(groupSt.length() + 1);

            ResponseHelper.tryOK(in, () -> {
                Main.singleton.getDataService().modifyGroup(groupId, jsonObject -> {
                    jsonObject.addProperty(key, value);
                    return jsonObject;
                });
                return null;
            }, Errors.DATA_SERVICE_INTERNAL);
        } catch (NullPointerException e) {
            ResponseHelper.respondError(in, Errors.BAD_REQUEST);
        }
    }

    public static void modifyUser(@NotNull InboundMessage in, @NotNull String key) {
        try {
            String content = in.getRoute().content();
            String userSt  = StringUtil.getToken(content, " ", 0);
            Long   userId  = StringUtil.parseLong(userSt);

            if (userId == null)
                throw new NullPointerException();

            String value = content.substring(userSt.length() + 1);

            ResponseHelper.tryOK(in, () -> {
                Main.singleton.getDataService().modifyUser(userId, jsonObject -> {
                    jsonObject.addProperty(key, value);
                    return jsonObject;
                });
                return null;
            }, Errors.DATA_SERVICE_INTERNAL);
        } catch (NullPointerException e) {
            ResponseHelper.respondError(in, Errors.BAD_REQUEST);
        }
    }
}
