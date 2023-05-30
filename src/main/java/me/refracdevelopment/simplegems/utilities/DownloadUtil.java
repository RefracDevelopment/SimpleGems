package me.refracdevelopment.simplegems.utilities;

import me.refracdevelopment.simplegems.SimpleGems;
import net.byteflux.libby.BukkitLibraryManager;
import net.byteflux.libby.Library;

public class DownloadUtil {

    public static void downloadAndEnable() {
        BukkitLibraryManager libraryManager = new BukkitLibraryManager(SimpleGems.getInstance());
        Library lib = Library.builder()
                .groupId("org{}mongodb") // "{}" is replaced with ".", useful to avoid unwanted changes made by maven-shade-plugin
                .artifactId("mongo-java-driver")
                .version("3.12.13")
                .build();

        libraryManager.addMavenCentral();
        libraryManager.loadLibrary(lib);
    }
}