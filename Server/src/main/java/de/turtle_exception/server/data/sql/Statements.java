package de.turtle_exception.server.data.sql;

class Statements {
    private Statements() { }

    // CREATE TABLE
    public static final String CT_CREDENTIALS    = "CREATE TABLE IF NOT EXISTS `credentials` (`login` VARCHAR(256) NOT NULL , `pass` VARCHAR(256) NOT NULL , PRIMARY KEY (`login`));";
    public static final String CT_GROUPS         = "CREATE TABLE IF NOT EXISTS `groups` (`id` BIGINT NOT NULL , `name` TINYTEXT NOT NULL , `permissions` BIGINT NOT NULL , PRIMARY KEY (`id`));";
    public static final String CT_MEMBERS        = "CREATE TABLE IF NOT EXISTS `members` (`user` BIGINT NOT NULL , `group` BIGINT NOT NULL , PRIMARY KEY (`user`, `group`));";
    public static final String CT_USERS          = "CREATE TABLE IF NOT EXISTS `users` (`id` BIGINT NOT NULL , `name` TINYTEXT NOT NULL , `permissions` BIGINT NOT NULL , PRIMARY KEY (`id`));";
    public static final String CT_USER_DISCORD   = "CREATE TABLE IF NOT EXISTS `user_discord` (`user` BIGINT NOT NULL , `discord` BIGINT NOT NULL , PRIMARY KEY (`user`, `discord`));";
    public static final String CT_USER_MINECRAFT = "CREATE TABLE IF NOT EXISTS `user_minecraft` (`user` BIGINT NOT NULL , `minecraft` VARCHAR(36) NOT NULL , PRIMARY KEY (`user`, `minecraft`));";
    public static final String CT_TICKETS        = "CREATE TABLE IF NOT EXISTS `tickets` (`id` BIGINT NOT NULL , `state` TINYINT NOT NULL , `title` TINYTEXT NULL , `category` TINYTEXT NOT NULL , `discord_channel` BIGINT NOT NULL , PRIMARY KEY (`id`));";
    public static final String CT_TICKET_TAGS    = "CREATE TABLE IF NOT EXISTS `ticket_tags` (`ticket` BIGINT NOT NULL , `tag` VARCHAR(256) NOT NULL , PRIMARY KEY (`ticket`, `tag`));";
    public static final String CT_TICKET_USERS   = "CREATE TABLE IF NOT EXISTS `ticket_users` (`ticket` BIGINT NOT NULL , `user` BIGINT NOT NULL , PRIMARY KEY (`ticket`, `user`));";

    // CREDENTIALS
    public static final String GET_PASS = "SELECT `pass` FROM `credentials` WHERE `name` = '{0}';";

    // GROUPS
    public static final String GET_GROUP     = "SELECT * FROM `groups` WHERE `id` = '{0}';";
    public static final String GET_GROUP_IDS = "SELECT `id` FROM `groups`;";
    public static final String SET_GROUP     = "INSERT INTO `groups` (`id`, `name`, `permissions`) VALUES ('{0}', '{1}', '{2}') ON DUPLICATE KEY UPDATE `name` = '{1}';";
    public static final String DEL_GROUP     = "DELETE FROM `groups` WHERE `id` = {0};";

    // MEMBERS
    public static final String GET_MEMBERS  = "SELECT `user` FROM `members` WHERE `group` = '{0}';";
    public static final String MEMBER_JOIN  = "INSERT INTO `members`(`group`, `user`) VALUES ('{0}', '{1}');";
    public static final String MEMBER_LEAVE = "DELETE FROM `members` WHERE `group` = '{0}' AND `user` = '{1}';";

    // USERS
    public static final String GET_USER     = "SELECT * FROM `users` WHERE `id` = '{0}';";
    public static final String GET_USER_IDS = "SELECT `id` FROM `users`;";
    public static final String SET_USER     = "INSERT INTO `users` (`id`, `name`, `permissions`) VALUES ('{0}', '{1}', '{2}') ON DUPLICATE KEY UPDATE `name` = '{1}';";
    public static final String DEL_USER     = "DELETE FROM `users` WHERE `id` = {0};";

    // USERS & DISCORD
    public static final String GET_USER_DISCORD = "SELECT `discord` FROM `user_discord` WHERE `user` = '{0}';";
    public static final String ADD_USER_DISCORD = "INSERT INTO `user_discord`(`user`, `discord`) VALUES ('{0}','{1}');";
    public static final String DEL_USER_DISCORD = "DELETE FROM `user_discord` WHERE `user` = '{0}' AND `discord` = '{1}';";

    // USERS & MINECRAFT
    public static final String GET_USER_MINECRAFT = "SELECT `minecraft` FROM `user_minecraft` WHERE `user` = '{0}';";
    public static final String ADD_USER_MINECRAFT = "INSERT INTO `user_minecraft`(`user`, `minecraft`) VALUES ('{0}','{1}');";
    public static final String DEL_USER_MINECRAFT = "DELETE FROM `user_minecraft` WHERE `user` = '{0}' AND `minecraft` = '{1}';";

    // TICKETS
    public static final String GET_TICKET     = "SELECT * FROM `ticket` WHERE `id` = '{0}';";
    public static final String GET_TICKET_IDS = "SELECT `id` FROM `ticket`;";
    // TODO: check NULL on title
    public static final String SET_TICKET     = "INSERT INTO `tickets` (`id`, `state`, `title`, `category`, `discord_channel`) VALUES ('{0}', '{1}', {2}, '{3}', '{4}');";
    public static final String DEL_TICKET     = "DELETE FROM `ticket` WHERE `id` = {0};";

    // TICKETS & TAGS
    public static final String GET_TICKET_TAGS = "SELECT `tag` FROM `ticket_tags` WHERE `ticket` = '{0}';";
    public static final String ADD_TICKET_TAG  = "INSERT INTO `ticket_tags`(`ticket`, `tag`) VALUES ('{0}','{1}');";
    public static final String DEL_TICKET_TAG  = "DELETE FROM `ticket_tags` WHERE `ticket` = '{0}' AND `tag` = '{1}';";

    // TICKETS & USERS
    public static final String GET_TICKET_USERS = "SELECT `user` FROM `ticket_users` WHERE `ticket` = '{0}';";
    public static final String ADD_TICKET_USER  = "INSERT INTO `ticket_users`(`ticket`, `user`) VALUES ('{0}','{1}');";
    public static final String DEL_TICKET_USER  = "DELETE FROM `ticket_users` WHERE `ticket` = '{0}' AND `user` = '{1}';";
}
