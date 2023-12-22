package me.refracdevelopment.simplegems.utilities;

import lombok.experimental.UtilityClass;
import me.refracdevelopment.simplegems.SimpleGems;
import net.byteflux.libby.BukkitLibraryManager;
import net.byteflux.libby.Library;

@UtilityClass
public class DownloadUtil {

    public void downloadAndEnable() {
        BukkitLibraryManager libraryManager = new BukkitLibraryManager(SimpleGems.getInstance());
        Library lib = Library.builder()
                .groupId("org{}mariadb{}jdbc") // "{}" is replaced with ".", useful to avoid unwanted changes made by maven-shade-plugin
                .artifactId("mariadb-java-client")
                .version("3.3.1")
                .build();
        Library lib2 = Library.builder()
                .groupId("org{}mongodb") // "{}" is replaced with ".", useful to avoid unwanted changes made by maven-shade-plugin
                .artifactId("mongodb-driver-sync")
                .version("4.11.1")
                .build();
        Library lib3 = Library.builder()
                .groupId("org{}mongodb") // "{}" is replaced with ".", useful to avoid unwanted changes made by maven-shade-plugin
                .artifactId("bson")
                .version("4.11.1")
                .build();
        Library lib4 = Library.builder()
                .groupId("org{}mongodb") // "{}" is replaced with ".", useful to avoid unwanted changes made by maven-shade-plugin
                .artifactId("mongodb-driver-core")
                .version("4.11.1")
                .build();
        Library lib5 = Library.builder()
                .groupId("org{}slf4j") // "{}" is replaced with ".", useful to avoid unwanted changes made by maven-shade-plugin
                .artifactId("slf4j-reload4j")
                .version("2.0.9")
                .build();
        Library lib6 = Library.builder()
                .groupId("org{}xerial") // "{}" is replaced with ".", useful to avoid unwanted changes made by maven-shade-plugin
                .artifactId("sqlite-jdbc")
                .version("3.44.1.0")
                .build();
        Library lib7 = Library.builder()
                .groupId("com{}zaxxer") // "{}" is replaced with ".", useful to avoid unwanted changes made by maven-shade-plugin
                .artifactId("HikariCP")
                .version("4.0.3")
                .build();

        libraryManager.addMavenCentral();
        libraryManager.loadLibrary(lib);
        libraryManager.loadLibrary(lib2);
        libraryManager.loadLibrary(lib3);
        libraryManager.loadLibrary(lib4);
        libraryManager.loadLibrary(lib5);
        libraryManager.loadLibrary(lib6);
        libraryManager.loadLibrary(lib7);
    }
}