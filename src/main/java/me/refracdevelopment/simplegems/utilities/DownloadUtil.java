package me.refracdevelopment.simplegems.utilities;

import me.refracdevelopment.simplegems.SimpleGems;
import net.byteflux.libby.BukkitLibraryManager;
import net.byteflux.libby.Library;

public class DownloadUtil {

    public static void downloadAndEnable() {
        BukkitLibraryManager libraryManager = new BukkitLibraryManager(SimpleGems.getInstance());
        Library lib = Library.builder()
                .groupId("org{}mongodb") // "{}" is replaced with ".", useful to avoid unwanted changes made by maven-shade-plugin
                .artifactId("mongodb-driver-sync")
                .version("4.10.2")
                .build();
        Library lib2 = Library.builder()
                .groupId("org{}mongodb") // "{}" is replaced with ".", useful to avoid unwanted changes made by maven-shade-plugin
                .artifactId("bson")
                .version("4.10.2")
                .build();
        Library lib3 = Library.builder()
                .groupId("org{}mongodb") // "{}" is replaced with ".", useful to avoid unwanted changes made by maven-shade-plugin
                .artifactId("mongodb-driver-core")
                .version("4.10.2")
                .build();
        Library lib4 = Library.builder()
                .groupId("org{}json") // "{}" is replaced with ".", useful to avoid unwanted changes made by maven-shade-plugin
                .artifactId("json")
                .version("20220320")
                .build();

        libraryManager.addMavenCentral();
        libraryManager.loadLibrary(lib);
        libraryManager.loadLibrary(lib2);
        libraryManager.loadLibrary(lib3);
        libraryManager.loadLibrary(lib4);
    }
}