package pluginbase.sponge.config;

import com.google.common.io.CharSink;
import com.google.common.io.CharSource;
import com.typesafe.config.ConfigParseOptions;
import com.typesafe.config.ConfigRenderOptions;
import ninja.leaping.configurate.ConfigurationNode;
import ninja.leaping.configurate.ConfigurationOptions;
import ninja.leaping.configurate.commented.CommentedConfigurationNode;
import ninja.leaping.configurate.commented.SimpleCommentedConfigurationNode;
import ninja.leaping.configurate.hocon.HoconConfigurationLoader;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.Writer;
import java.net.URL;

public class HoconSerializableConfigLoader extends HoconConfigurationLoader {

    public HoconSerializableConfigLoader(CharSource source, CharSink sink, ConfigRenderOptions render, ConfigParseOptions parse, boolean preservesHeader) {
        super(source, sink, render, parse, preservesHeader);
    }

    public static class Builder extends HoconConfigurationLoader.Builder {

        private ConfigRenderOptions render = ConfigRenderOptions.defaults()
                .setOriginComments(false)
                .setJson(false);
        private ConfigParseOptions parse = ConfigParseOptions.defaults();

        protected Builder() {
        }

        public ConfigRenderOptions getRenderOptions() {
            return render;
        }

        public ConfigParseOptions getParseOptions() {
            return parse;
        }

        @Override
        public Builder setFile(File file) {
            return (Builder) super.setFile(file);
        }

        @Override
        public Builder setURL(URL url) {
            return (Builder) super.setURL(url);
        }

        @Override
        public Builder setRenderOptions(ConfigRenderOptions options) {
            return (Builder) super.setRenderOptions(options);
        }

        @Override
        public Builder setParseOptions(ConfigParseOptions options) {
            return (Builder) super.setParseOptions(options);
        }

        @Override
        public Builder setSource(CharSource source) {
            return (Builder) super.setSource(source);
        }

        @Override
        public Builder setSink(CharSink sink) {
            return (Builder) super.setSink(sink);
        }

        @Override
        public Builder setPreservesHeader(boolean preservesHeader) {
            return (Builder) super.setPreservesHeader(preservesHeader);
        }

        @Override
        public HoconSerializableConfigLoader build() {
            return new HoconSerializableConfigLoader(source, sink, render, parse, preserveHeader);
        }
    }

    public static Builder builder() {
        return new Builder();
    }

    @Override
    public void loadInternal(CommentedConfigurationNode node, BufferedReader reader) throws IOException {
        super.loadInternal(node, reader);
    }

    @Override
    protected void saveInternal(ConfigurationNode node, Writer writer) throws IOException {
        super.saveInternal(node, writer);
    }

    @Override
    public CommentedConfigurationNode createEmptyNode(ConfigurationOptions options) {
        return SimpleCommentedConfigurationNode.root(options);
    }

}
