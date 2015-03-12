package pluginbase.sponge.config.comments;

import pluginbase.comments.yaml.YamlFileCommentInstrumenter;
import pluginbase.config.SerializationRegistrar;
import pluginbase.sponge.config.YamlConfiguration;
import pluginbase.config.field.Field;
import pluginbase.config.field.FieldMap;
import pluginbase.config.field.FieldMapper;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

public class CommentsMapper {

    @NotNull
    private String currentPath;
    private Map<?, ?> configMap = null;
    private FieldMap fieldMap = null;
    @NotNull
    private YamlFileCommentInstrumenter commentInstrumenter;

    public static YamlFileCommentInstrumenter getCommentInstrumenter(@NotNull YamlConfiguration config) {
        return new CommentsMapper("",
                config.getValues(true),
                YamlFileCommentInstrumenter.createCommentInstrumenter(config.options().indent()))
                        .getCommentInstrumenter();
    }

    private CommentsMapper(@NotNull String currentPath, @NotNull Map<?, ?> configMap, YamlFileCommentInstrumenter commentInstrumenter) {
        this.currentPath = currentPath;
        this.configMap = configMap;
        this.commentInstrumenter = commentInstrumenter;
    }

    private CommentsMapper(@NotNull String currentPath, @NotNull FieldMap fieldMap, YamlFileCommentInstrumenter commentInstrumenter) {
        this.currentPath = currentPath;
        this.fieldMap = fieldMap;
        this.commentInstrumenter = commentInstrumenter;
    }

    private YamlFileCommentInstrumenter getCommentInstrumenter() {
        if (configMap != null) {
            processConfigMap();
        } else if (fieldMap != null) {
            processFieldMap();
        }
        return commentInstrumenter;
    }

    private YamlFileCommentInstrumenter processConfigMap() {
        for (Map.Entry<?, ?> entry : configMap.entrySet()) {
            if (entry.getValue() == null) {
                continue;
            }
            Class entryClass = entry.getValue().getClass();
            if (Map.class.isAssignableFrom(entryClass)) {
                Map<?, ?> configMap = (Map<?, ?>) entry.getValue();
                commentInstrumenter = new CommentsMapper(getNewPath(entry.getKey()), configMap, commentInstrumenter).getCommentInstrumenter();
            } else if (SerializationRegistrar.isClassRegistered(entryClass)) {
                FieldMap fieldMap = FieldMapper.getFieldMap(entryClass);
                commentInstrumenter = new CommentsMapper(getNewPath(entry.getKey()), fieldMap, commentInstrumenter).getCommentInstrumenter();
            }
        }
        return commentInstrumenter;
    }

    private String getNewPath(Object key) {
        return currentPath + (currentPath.isEmpty() ? "" : ".") + key.toString();
    }

    private YamlFileCommentInstrumenter processFieldMap() {
        for (Field field : fieldMap) {
            String newPath = null;
            if (SerializationRegistrar.isClassRegistered(field.getType())) {
                newPath = getNewPath(field);
                commentInstrumenter = new CommentsMapper(newPath, field, commentInstrumenter).getCommentInstrumenter();
            }
            String[] comments = field.getComments();
            if (comments != null) {
                if (newPath == null) {
                    newPath = getNewPath(field);
                }
                commentInstrumenter.setCommentsForPath(newPath, comments);
            }
        }
        return commentInstrumenter;
    }

    private String getNewPath(Field field) {
        return currentPath + (currentPath.isEmpty() ? "" : ".") + field.getName();
    }

}
