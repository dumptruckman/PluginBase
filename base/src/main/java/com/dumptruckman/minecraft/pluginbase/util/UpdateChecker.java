package com.dumptruckman.minecraft.pluginbase.util;

import com.dumptruckman.minecraft.pluginbase.logging.Logging;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilderFactory;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;

public class UpdateChecker {

    private static volatile boolean updateAvailable = false;
    private static volatile String latestVersion = null;
    private static volatile String link = null;
    private static volatile String jarLink = null;

    public static void checkForUpdates(final String version, final String url) {
        try{
            new UpdateCheckerThread(version, new URL(url)).start();
        }catch (MalformedURLException e){
            Logging.warning("Could not check for updates: " + e.getMessage());
        }
    }

    private static class UpdateCheckerThread extends Thread {

        private final String currentVersion;
        private final URL filesFeed;

        UpdateCheckerThread(final String version, final URL filesFeed) {
            this.currentVersion = version;
            this.filesFeed = filesFeed;
        }

        @Override
        public void run() {
            try{
                InputStream input = filesFeed.openConnection().getInputStream();
                Document document = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(input);

                Node latestFile = document.getElementsByTagName("item").item(0);
                NodeList children = latestFile.getChildNodes();

                UpdateChecker.latestVersion = children.item(1).getTextContent().replaceAll("[a-zA-Z ]", "");
                UpdateChecker.link = children.item(3).getTextContent();

                input.close();

                input = (new URL(UpdateChecker.link)).openConnection().getInputStream();

                BufferedReader reader = new BufferedReader(new InputStreamReader(input));
                String line;

                while ((line = reader.readLine()) != null) {
                    if (line.trim().startsWith("<li class=\"user-action user-action-download\">")) {
                        UpdateChecker.jarLink = line.substring(line.indexOf("href=\"") + 6, line.lastIndexOf("\""));
                        break;
                    }
                }

                reader.close();
                input.close();

                if (!currentVersion.equals(UpdateChecker.latestVersion)) {
                    UpdateChecker.updateAvailable = true;
                    Logging.info("A new version is available: " + getLatestVersion());
                    Logging.info("Get it from: " + getLink());
                    Logging.info("Direct Link: " + getJarLink());
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            UpdateChecker.updateAvailable = false;
        }
    }

    public static boolean isUpdateAvailable() {
        return updateAvailable;
    }

    public static String getLatestVersion(){
        return latestVersion;
    }

    public static String getLink(){
        return link;
    }

    public static String getJarLink(){
        return jarLink;
    }
}
