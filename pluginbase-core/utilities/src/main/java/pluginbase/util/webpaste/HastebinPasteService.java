package pluginbase.util.webpaste;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Pastes to {@code hastebin.com}.
 */
public class HastebinPasteService implements PasteService {

    /**
     * {@inheritDoc}
     */
    @Override
    public URL getPostURL() {
        try {
            return new URL("http://hastebin.com/documents");
        } catch (MalformedURLException e) {
            return null; // should never hit here
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String encodeData(String data) {
        return data;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String postData(String encodedData, URL url) throws PasteFailedException {
        try {
            HttpURLConnection con = (HttpURLConnection) url.openConnection();

            con.setRequestMethod("POST");
            con.setRequestProperty("User-Agent", "PostRequester2000");
            con.setDoOutput(true);
            DataOutputStream out = new DataOutputStream(con.getOutputStream());
            out.writeBytes(encodedData);
            out.close();

            int code = con.getResponseCode(); // Maybe use later...

            BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
            StringBuffer buffer = new StringBuffer();

            String input;
            while ((input = in.readLine()) != null) {
                buffer.append(input);
            }
            in.close();
            con.disconnect();

            String pasteUrl;
            if (code >= 200 && code < 300) {
                pasteUrl = buffer.toString().split(":")[1];
                pasteUrl = pasteUrl.split("\"")[1];
                pasteUrl = "http://hastebin.com/" + pasteUrl;
            } else {
                throw new RuntimeException("Could not retrieve paste URL. Error code: " + code);
            }

            return pasteUrl;
        } catch (Exception e) {
            throw new PasteFailedException(e);
        }
    }
}
