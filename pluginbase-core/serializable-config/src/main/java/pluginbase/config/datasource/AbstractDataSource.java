package pluginbase.config.datasource;

import ninja.leaping.configurate.ConfigurationNode;
import ninja.leaping.configurate.ConfigurationOptions;
import ninja.leaping.configurate.commented.CommentedConfigurationNode;
import ninja.leaping.configurate.commented.SimpleCommentedConfigurationNode;
import ninja.leaping.configurate.loader.AbstractConfigurationLoader;
import ninja.leaping.configurate.loader.AtomicFiles;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import pluginbase.config.SerializableConfig;
import pluginbase.config.annotation.Comment;
import pluginbase.config.field.Field;
import pluginbase.config.field.FieldMap;
import pluginbase.config.field.FieldMapper;
import pluginbase.config.serializers.SerializerSet;
import pluginbase.messages.Message;
import pluginbase.messages.Messages;
import pluginbase.messages.messaging.SendablePluginBaseException;

import java.io.*;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;
import java.util.concurrent.Callable;

import static java.nio.charset.StandardCharsets.UTF_8;

/**
 * A nearly complete implementation of DataSource that implements the main features of Serializable-Config and acts as
 * a wrapper for the zml's Configurate lib.
 */
public abstract class AbstractDataSource implements DataSource {

    protected static final String LINE_SEPARATOR = System.lineSeparator();

    @NotNull
    private final AbstractConfigurationLoader loader;
    @NotNull
    private final SerializerSet serializerSet;

    /**
     * This builder class is used to properly configure and create a DataSource object.
     * <p/>
     * You may specify both a source and a sink for the storage medium or simply specify a File which counts as both.
     * <br/>
     * Once all the options are set for the storage medium, simply call {@link #build()} to create a DataSource tied
     * to the configured storage medium.
     */
    public static abstract class Builder<T extends Builder> {

        protected Callable<BufferedReader> source;
        protected Callable<BufferedWriter> sink;
        protected final SerializerSet serializerSet;

        protected Builder(SerializerSet serializerSet) {
            this.serializerSet = serializerSet;
        }

        @NotNull
        @SuppressWarnings("unchecked")
        private T self() {
            return (T) this;
        }

        /**
         * A File can be set to be used as a data source and sink.
         *
         * @param file the file to be used as the data source and sink.
         * @return this builder.
         */
        @NotNull
        public T setFile(@NotNull File file) {
            return setPath(file.toPath());
        }

        /**
         * A Path can be set to be used as a data source and sink.
         *
         * @param path the path to be used as the data source and sink.
         * @return this builder.
         */
        @NotNull
        public T setPath(@NotNull Path path) {
            this.source = () -> Files.newBufferedReader(path, UTF_8);
            this.sink = AtomicFiles.createAtomicWriterFactory(path, UTF_8);
            return self();
        }

        /**
         * A URL can be set to be used as a data source.
         *
         * @param url A url to be used as the data source.
         * @return this builder.
         */
        @NotNull
        public T setURL(@NotNull URL url) {
            this.source = () -> new BufferedReader(new InputStreamReader(url.openConnection().getInputStream()));
            return self();
        }

        @NotNull
        public T setSource(@NotNull Callable<BufferedReader> source) {
            this.source = source;
            return self();
        }

        @NotNull
        public T setSink(@NotNull Callable<BufferedWriter> sink) {
            this.sink = sink;
            return self();
        }

        /**
         * Creates the data source using the options of this builder.
         *
         * @return a new DataSource object usings the options specified in this builder.
         */
        @NotNull
        public abstract AbstractDataSource build();
    }

    protected AbstractDataSource(@NotNull AbstractConfigurationLoader loader, @NotNull SerializerSet serializerSet) {
        this.loader = loader;
        this.serializerSet = serializerSet;
    }

    /** {@inheritDoc} */
    @Nullable
    @Override
    public Object load() throws SendablePluginBaseException {
        try {
            ConfigurationNode node = getLoader().load();
            return SerializableConfig.deserialize(node.getValue(), serializerSet);
        } catch (IOException e) {
            throw new SendablePluginBaseException(Message.bundleMessage(Messages.EXCEPTION, e), e);
        }
    }

    /** {@inheritDoc} */
    @Nullable
    @Override
    public <ObjectType> ObjectType load(Class<ObjectType> wantedType) throws SendablePluginBaseException {
        try {
            ConfigurationNode node = getLoader().load();
            return SerializableConfig.deserializeAs(node.getValue(), wantedType, serializerSet);
        } catch (IOException e) {
            throw new SendablePluginBaseException(Message.bundleMessage(Messages.EXCEPTION, e), e);
        }
    }

    /** {@inheritDoc} */
    @Nullable
    @SuppressWarnings("unchecked")
    public <ObjectType> ObjectType loadToObject(@NotNull ObjectType destination) throws SendablePluginBaseException {
        try {
            Object value = getLoader().load().getValue();
            if (value == null) {
                return null;
            }
            ObjectType source = SerializableConfig.deserializeAs(value, (Class<ObjectType>) destination.getClass(), serializerSet);
            if (destination.equals(source)) {
                return destination;
            }
            if (source != null) {
                destination = FieldMapper.mapFields(source, destination);
                return destination;
            } else {
                return null;
            }
        } catch (IOException e) {
            throw new SendablePluginBaseException(Message.bundleMessage(Messages.EXCEPTION, e), e);
        }
    }

    /** {@inheritDoc} */
    @Override
    public void save(@NotNull Object object) throws SendablePluginBaseException {
        try {
            String header = getComments(object.getClass(), true);

            CommentedConfigurationNode node = SimpleCommentedConfigurationNode.root(ConfigurationOptions.defaults().setHeader(header));
            Object serialized = SerializableConfig.serialize(object, serializerSet);
            node = node.setValue(serialized);

            node = addComments(FieldMapper.getFieldMap(object.getClass()), node);

            getLoader().save(node);
        } catch (IOException e) {
            throw new SendablePluginBaseException(Message.bundleMessage(Messages.EXCEPTION, e), e);
        }
    }

    @NotNull
    protected AbstractConfigurationLoader getLoader() {
        return loader;
    }

    @Nullable
    protected String getComments(@NotNull Class clazz, boolean header) {
        Comment comment = (Comment) clazz.getAnnotation(Comment.class);
        if (comment != null) {
            return joinComments(comment.value(), header);
        }
        return null;
    }

    protected String joinComments(String[] comments, boolean header) {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < comments.length; i++) {
            if (i > 0) {
                if (header) {
                    builder.append("\n");
                } else {
                    builder.append(LINE_SEPARATOR);
                }
            }
            builder.append(comments[i]);
        }
        return builder.toString();
    }

    protected CommentedConfigurationNode addComments(@NotNull FieldMap fieldMap, @NotNull CommentedConfigurationNode node) {
        for (Map.Entry<Object, ? extends CommentedConfigurationNode> entry : node.getChildrenMap().entrySet()) {
            Field field = fieldMap.getField(entry.getKey().toString());
            if (field != null) {
                String[] comments = field.getComments();
                if (comments != null) {
                    entry.getValue().setComment(joinComments(comments, false));
                }
                digDeeper(field, entry.getValue());
            }
        }
        return node;
    }

    protected void digDeeper(@NotNull Field field, @NotNull CommentedConfigurationNode node) {
        if (node.hasMapChildren()) {
            addComments(field, node);
        } else if (node.hasListChildren()) {
            for (CommentedConfigurationNode item : node.getChildrenList()) {
                digDeeper(field, item);
            }
        }
    }
}
