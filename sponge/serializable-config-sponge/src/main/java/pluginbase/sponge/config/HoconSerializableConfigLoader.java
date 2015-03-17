package pluginbase.sponge.config;

import com.google.common.base.Optional;
import com.google.common.collect.ImmutableList;
import com.google.common.io.CharSink;
import com.google.common.io.CharSource;
import com.typesafe.config.ConfigList;
import com.typesafe.config.ConfigObject;
import com.typesafe.config.ConfigOrigin;
import com.typesafe.config.ConfigParseOptions;
import com.typesafe.config.ConfigRenderOptions;
import com.typesafe.config.ConfigValue;
import com.typesafe.config.ConfigValueFactory;
import ninja.leaping.configurate.ConfigurationNode;
import ninja.leaping.configurate.ConfigurationOptions;
import ninja.leaping.configurate.commented.CommentedConfigurationNode;
import ninja.leaping.configurate.commented.SimpleCommentedConfigurationNode;
import ninja.leaping.configurate.hocon.HoconConfigurationLoader;
import pluginbase.config.ConfigSerializer;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.Writer;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

public class HoconSerializableConfigLoader extends HoconConfigurationLoader {

    public static final Pattern CRLF_MATCH = Pattern.compile("\r\n?");
    private final ConfigRenderOptions render;
    private final ConfigParseOptions parse;

    public HoconSerializableConfigLoader(CharSource source, CharSink sink, ConfigRenderOptions render, ConfigParseOptions parse, boolean preservesHeader) {
        super(source, sink, render, parse, preservesHeader);
        this.render = render;
        this.parse = parse;
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
        Object value = node.getValue();
        if (value != null && node.hasMapChildren()) {
            node.setValue(ConfigSerializer.deserialize(node.getValue()));
        }
    }

    @Override
    protected void saveInternal(ConfigurationNode node, Writer writer) throws IOException {
        Object value = node.getValue();
        if (value != null) {
            final ConfigValue newValue = ConfigValueFactory.fromAnyRef(ConfigSerializer.serialize(node.getValue()), "serializable-config-hocon");
            traverseForComments(newValue, node);
            final String renderedValue = newValue.render(render);
            writer.write(renderedValue);
        } else {
            writer.write(LINE_SEPARATOR);
            return;
        }
        /*
        if (!node.hasMapChildren()) {
            if (node.getValue() == null) {

            } else {
                System.out.println(node.getValue());
                throw new IOException("HOCON cannot write nodes not in map format!");
            }
        }
        */


    }

    @Override
    public CommentedConfigurationNode createEmptyNode(ConfigurationOptions options) {
        return SimpleCommentedConfigurationNode.root(options);
    }

    private void traverseForComments(ConfigValue value, ConfigurationNode node) throws IOException {
        potentialComment(value, node);
        switch (value.valueType()) {
            case OBJECT:
                for (Map.Entry<Object, ? extends ConfigurationNode> ent : node.getChildrenMap().entrySet()) {
                    traverseForComments(((ConfigObject) value).get(ent.getKey().toString()), ent.getValue());
                }
                break;
            case LIST:
                List<? extends ConfigurationNode> nodes = node.getChildrenList();
                for (int i = 0; i < nodes.size(); ++i) {
                    traverseForComments(((ConfigList) value).get(i), nodes.get(i));
                }
                break;
        }
    }

    // -- Comment handling -- this might have to be updated as the hocon dep changes (But tests should detect this
    // breakage
    private static final Class<? extends ConfigValue> VALUE_CLASS;
    private static final Class<? extends ConfigOrigin> ORIGIN_CLASS;
    private static final Field VALUE_ORIGIN;
    private static final Method ORIGIN_SET_COMMENTS;
    static {
        try {
            VALUE_CLASS = Class.forName("com.typesafe.config.impl.AbstractConfigValue").asSubclass(ConfigValue.class);
        } catch (ClassNotFoundException e) {
            throw new ExceptionInInitializerError(e);
        }

        ORIGIN_CLASS = ConfigValueFactory.fromAnyRef("a").origin().getClass();
        try {
            VALUE_ORIGIN = VALUE_CLASS.getDeclaredField("origin");
            ORIGIN_SET_COMMENTS = ORIGIN_CLASS.getDeclaredMethod("setComments", List.class);
            VALUE_ORIGIN.setAccessible(true);
            ORIGIN_SET_COMMENTS.setAccessible(true);
        } catch (NoSuchFieldException | NoSuchMethodException e) {
            throw new ExceptionInInitializerError(e);
        }

    }
    private ConfigValue potentialComment(ConfigValue value, ConfigurationNode node) throws IOException {
        if (!(node instanceof CommentedConfigurationNode)) {
            return value;
        }
        CommentedConfigurationNode commentedNode = (CommentedConfigurationNode) node;
        Optional<String> comment = commentedNode.getComment();
        if (!comment.isPresent()) {
            return value;
        }
        try {
            Object o = ORIGIN_SET_COMMENTS.invoke(value.origin(), ImmutableList.copyOf(LINE_SPLITTER.split(comment.get())));
            VALUE_ORIGIN.set(value, o);
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new IOException("Unable to set comments for config value" + value);
        }
        return value;
    }
}
