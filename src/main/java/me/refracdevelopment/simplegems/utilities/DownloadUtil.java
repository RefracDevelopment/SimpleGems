package me.refracdevelopment.simplegems.utilities;

import com.alessiodp.libby.BukkitLibraryManager;
import com.alessiodp.libby.Library;
import org.bukkit.plugin.java.JavaPlugin;

public class DownloadUtil {

    public static void downloadAndEnable(JavaPlugin plugin) {
        try {
            plugin.getLogger().info("Downloading dependencies...");

            BukkitLibraryManager libraryManager = new BukkitLibraryManager(plugin);
            Library lib = Library.builder()
                    .groupId("org{}mariadb{}jdbc") // "{}" is replaced with ".", useful to avoid unwanted changes made by maven-shade-plugin
                    .artifactId("mariadb-java-client")
                    .version("3.5.6")
                    .build();
            Library lib2 = Library.builder()
                    .groupId("org{}slf4j") // "{}" is replaced with ".", useful to avoid unwanted changes made by maven-shade-plugin
                    .artifactId("slf4j-reload4j")
                    .version("2.0.17")
                    .build();
            Library lib3 = Library.builder()
                    .groupId("org{}xerial") // "{}" is replaced with ".", useful to avoid unwanted changes made by maven-shade-plugin
                    .artifactId("sqlite-jdbc")
                    .version("3.51.0.0")
                    .build();
            Library lib4 = Library.builder()
                    .groupId("com{}zaxxer") // "{}" is replaced with ".", useful to avoid unwanted changes made by maven-shade-plugin
                    .artifactId("HikariCP")
                    .version("5.1.0")
                    .build();

            libraryManager.addMavenCentral();
            libraryManager.loadLibrary(lib);
            libraryManager.loadLibrary(lib2);
            libraryManager.loadLibrary(lib3);
            libraryManager.loadLibrary(lib4);
        } catch (Throwable throwable) {
            plugin.getLogger().severe("Failed to download one or more dependencies: " + throwable.getMessage());
            throwable.printStackTrace();
        } finally {
            plugin.getLogger().info("Successfully loaded all dependencies!");
        }
    }

}
