package de.turtle_exception.core.core.util.version;

import de.turtle_exception.core.core.util.StringUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.util.Properties;

public record Version(int[] versions, @Nullable String EXTRA) {
    public static Version parse(String raw) throws IllegalVersionException {
        try {
            return doParse(raw);
        } catch (Exception e) {
            throw new IllegalVersionException(raw, e);
        }
    }

    private static Version doParse(String raw) throws ArrayIndexOutOfBoundsException, NumberFormatException {
        String[] parts = raw.split("-");
        String[] versions = parts[0].split("\\.");
        String extra = parts.length > 1 ? parts[1] : null;

        int[] versionInts = new int[versions.length];
        for (int i = 0; i < versions.length; i++)
            versionInts[i] = Integer.parseInt(versions[i]);

        return new Version(versionInts, extra);
    }

    /**
     * Retrieves the version from <code>resources/version.properties</code>. If the file does not exist, does not
     * contain a valid version or could not be read <code>null</code> is returned and an {@link Exception} will be
     * printed to the console. It is recommended to check whether the version is <code>null</code> while constructing
     * the main class.
     * @return Version stored in resources.
     */
    public static Version retrieveFromResources(@NotNull Class<?> clazz) {
        try {
            Properties properties = new Properties();
            properties.load(clazz.getClassLoader().getResourceAsStream("version.properties"));
            return parse(properties.getProperty("version"));
        } catch (NullPointerException | IOException | IllegalVersionException e) {
            System.out.println("Unable to retrieve version from resources.");
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Compares another {@link Version} object and returns whether it represents a more recent version. If both versions
     * are equal this will return {@code false}.
     * <p> This ignores version extras and only compares major, minor and build.
     * @param v The {@link Version} to compare.
     * @return {@code true} if v represents a more recent Version.
     */
    public boolean isMoreRecent(Version v) {
        for (int i = 0; i < Math.min(versions.length, v.versions.length); i++) {
            if (versions[i] > v.versions[i])
                return false;
            if (versions[i] < v.versions[i])
                return true;
        }
        return false;
    }

    /**
     * Returns a string representation of the version.
     * <p>
     *     Schema: (depending on whether <code>EXTRA</code> is defined)
     *     <li><code>MAJOR.MINOR-BUILD</code></li>
     *     <li><code>MAJOR.MINOR-BUILD_EXTRA</code></li>
     * </p>
     * @return a string representation of the object.
     */
    @Override
    public @NotNull String toString() {
        return StringUtil.join(".", versions) + ((EXTRA != null) ? ("-" + EXTRA) : "");
    }
}