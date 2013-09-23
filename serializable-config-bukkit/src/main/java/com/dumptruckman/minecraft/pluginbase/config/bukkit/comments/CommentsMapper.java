package com.dumptruckman.minecraft.pluginbase.config.bukkit.comments;

import com.dumptruckman.minecraft.pluginbase.comments.yaml.YamlFileCommentInstrumenter;
import com.dumptruckman.minecraft.pluginbase.config.SerializationRegistrar;
import com.dumptruckman.minecraft.pluginbase.config.bukkit.YamlConfiguration;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

public class CommentsMapper {

    @NotNull
    private String currentPath;
    @NotNull
    private Map<String, Object> config;
    @NotNull
    private YamlFileCommentInstrumenter commentInstrumenter;

    YamlFileCommentInstrumenter getCommentInstrumenter(@NotNull YamlConfiguration config) {
        return new CommentsMapper("",
                config.getValues(true),
                YamlFileCommentInstrumenter.createCommentInstrumenter(config.options().indent()))
                        .getCommentInstrumenter();
    }

    private CommentsMapper(@NotNull String currentPath, @NotNull Map<String, Object> config, YamlFileCommentInstrumenter commentInstrumenter) {
        this.currentPath = currentPath;
        this.config = config;
        this.commentInstrumenter = commentInstrumenter;
    }

    private YamlFileCommentInstrumenter getCommentInstrumenter() {
        for (Map.Entry<String, Object> entry : config.entrySet()) {
            if (SerializationRegistrar.isClassRegistered(entry.getValue().getClass())) {

            }
        }
        return commentInstrumenter;
    }
}
