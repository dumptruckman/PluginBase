package com.dumptruckman.minecraft.pluginbase.bukkit.properties;

import com.google.common.io.Files;
import org.jetbrains.annotations.NotNull;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.nio.charset.IllegalCharsetNameException;

class EncodedYamlConfiguration extends CommentedYamlConfiguration {

    @NotNull
    private final Charset charset;

    EncodedYamlConfiguration(@NotNull final Charset charset, final boolean doComments) throws UnsupportedEncodingException {
        super(doComments);
        this.charset = charset;
    }

    EncodedYamlConfiguration(@NotNull final String charset, final boolean doComments) throws UnsupportedEncodingException, IllegalCharsetNameException {
        this(Charset.forName(charset), doComments);
    }

    @Override
    public void save(@NotNull final File file) throws IOException {
        Files.createParentDirs(file);

        final String data = saveToString();

        BufferedWriter writer = null;
        try {
            writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file), charset));
            writer.write(data);
        } finally {
            if (writer != null) {
                writer.close();
            }
        }
    }
}
