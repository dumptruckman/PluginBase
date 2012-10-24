package com.dumptruckman.minecraft.pluginbase.util;

import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarInputStream;

/**
 *
 * List all classes within a package.
 *
 * @author gravypod
 *
 */
public class PackageUtil {

    /**
     * Lists all of the classes in a package.
     *
     * @param jarName
     *            the name of the jar file. (Its file path)
     * @param packageName
     *            The package we want to list from.
     * @return list of classes in the package.
     *
     */
    public static List<String> getClassNamesInPackage(String jarName, String packageName) {
        ArrayList<String> classList = new ArrayList<String>();
        packageName = packageName.replaceAll("\\.", "/");
        try {
            JarInputStream jarFile = new JarInputStream(new FileInputStream(jarName));
            JarEntry jarEntry;
            while (true) {

                jarEntry = jarFile.getNextJarEntry();

                if (jarEntry == null) {
                    break;
                }

                if ((jarEntry.getName().startsWith(packageName)) && (jarEntry.getName().endsWith(".class")) && !(jarEntry.getName().contains("$"))) {
                    classList.add(jarEntry.getName().replaceAll("/", "\\."));
                }

            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return classList;
    }

}