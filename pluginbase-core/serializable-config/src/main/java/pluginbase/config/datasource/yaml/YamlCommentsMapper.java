package pluginbase.config.datasource.yaml;

import pluginbase.config.field.Field;
import pluginbase.config.field.FieldMap;
import pluginbase.config.field.FieldMapper;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

class YamlCommentsMapper {

    @NotNull
    private String currentPath;
    private Map<?, ?> configMap = null;
    private FieldMap fieldMap = null;
    @NotNull
    private YamlFileCommentInstrumenter commentInstrumenter;

    public static YamlFileCommentInstrumenter createYamlCommentInstrumenter(@NotNull Map<?, ?> configMap, int indentAmount) {
        return new YamlCommentsMapper("", configMap, YamlFileCommentInstrumenter.createCommentInstrumenter(indentAmount)).getCommentInstrumenter();
    }

    private YamlCommentsMapper(@NotNull String currentPath, @NotNull Map<?, ?> configMap, @NotNull YamlFileCommentInstrumenter commentInstrumenter) {
        this.currentPath = currentPath;
        this.configMap = configMap;
        this.commentInstrumenter = commentInstrumenter;
    }

    private YamlCommentsMapper(@NotNull String currentPath, @NotNull FieldMap fieldMap, @NotNull YamlFileCommentInstrumenter commentInstrumenter) {
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
                commentInstrumenter = new YamlCommentsMapper(getNewPath(entry.getKey()), configMap, commentInstrumenter).getCommentInstrumenter();
            } else {
                FieldMap fieldMap = FieldMapper.getFieldMap(entryClass);
                commentInstrumenter = new YamlCommentsMapper(getNewPath(entry.getKey()), fieldMap, commentInstrumenter).getCommentInstrumenter();
            }
        }
        return commentInstrumenter;
    }

    private String getNewPath(Object key) {
        return currentPath + (currentPath.isEmpty() ? "" : ".") + key.toString();
    }

    private YamlFileCommentInstrumenter processFieldMap() {
        for (Field field : fieldMap) {
            String newPath = getNewPath(field);
            commentInstrumenter = new YamlCommentsMapper(newPath, field, commentInstrumenter).getCommentInstrumenter();
            String[] comments = field.getComments();
            if (comments != null) {
                newPath = getNewPath(field);
                commentInstrumenter.setCommentsForPath(newPath, comments);
            }
        }
        return commentInstrumenter;
    }

    private String getNewPath(Field field) {
        return currentPath + (currentPath.isEmpty() ? "" : ".") + field.getName();
    }

}
