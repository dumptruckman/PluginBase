package com.dumptruckman.minecraft.pluginbase.util.webpaste;

import java.io.IOException;

/**
 * An {@link URLShortener} using {@code bit.ly}.
 */
public class BitlyURLShortener extends HttpAPIClient implements URLShortener {
    private static final String GENERIC_BITLY_REQUEST_FORMAT = "https://api-ssl.bitly.com/v3/shorten?format=txt&apiKey=%s&login=%s&longUrl=%s";

    // I think it's no problem that these are public
    private static final String USERNAME = "dumptruckman";
    private static final String API_KEY = "R_a231a189ca5232f0e17f3c202dec0a3f";

    public BitlyURLShortener() {
        super(String.format(GENERIC_BITLY_REQUEST_FORMAT, API_KEY, USERNAME, "%s"));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String shorten(String longUrl) {
        try {
            String result = this.exec(longUrl);
            if (!result.startsWith("http://bit.ly/")) // ... then it's failed :/
                throw new IOException(result);
            return result;
        } catch (IOException e) {
            e.printStackTrace();
            return longUrl; // sorry ...
        }
    }
}
