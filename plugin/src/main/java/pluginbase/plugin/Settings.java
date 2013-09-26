package pluginbase.plugin;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import pluginbase.config.SerializationRegistrar;
import pluginbase.config.annotation.Comment;
import pluginbase.config.annotation.SerializeWith;
import pluginbase.config.annotation.ValidateWith;
import pluginbase.config.field.PropertyVetoException;
import pluginbase.config.field.Validator;
import pluginbase.config.field.Validators;
import pluginbase.config.field.VirtualProperty;
import pluginbase.config.properties.PropertiesWrapper;
import pluginbase.config.serializers.Serializer;
import pluginbase.logging.PluginLogger;
import pluginbase.messages.Message;
import pluginbase.messages.MessageProvider;
import pluginbase.plugin.command.builtin.DebugCommand;

import java.util.Locale;

public class Settings extends PropertiesWrapper {

    static {
        SerializationRegistrar.registerClass(Settings.class);
        SerializationRegistrar.registerClass(Language.class);
    }

    @Comment("Settings related to language/locale.")
    @NotNull
    private Language language = new Language();

    @Comment("0 = off, 1-3 display debug info with increasing granularity.")
    @ValidateWith(DebugLevelValidator.class)
    private VirtualProperty<Integer> debugLevel;

    @Comment("Will make the plugin perform tasks only done on a first run (if any.)")
    private boolean firstRun = true;

    public Settings(@NotNull PluginBase plugin) {
        this.debugLevel = new VirtualDebug(plugin.getLog());
    }

    private Settings() { }

    @NotNull
    public Language getLanguageSettings() {
        return language;
    }

    public int getDebugLevel() {
        return debugLevel.get();
    }

    public void setDebugLevel(int debugLevel) throws PropertyVetoException {
        Integer value = Validators.getValidator(DebugLevelValidator.class).validateChange(debugLevel, getDebugLevel());
        this.debugLevel.set(value != null ? value : 0);
    }

    public boolean isFirstRun() {
        return firstRun;
    }

    public void setFirstRun(boolean firstRun) {
        this.firstRun = firstRun;
    }

    public static class Language {

        @Comment("This is the locale you wish to use for messages.")
        @SerializeWith(LocaleSerializer.class)
        @NotNull
        private Locale locale = MessageProvider.DEFAULT_LOCALE;

        @Comment("The language file that contains localized messages.")
        @NotNull
        private String languageFile = MessageProvider.DEFAULT_LANGUAGE_FILE_NAME;

        protected Language() { }

        @NotNull
        public Locale getLocale() {
            return locale;
        }

        public void setLocale(@NotNull Locale locale) {
            this.locale = locale;
        }

        @NotNull
        public String getLanguageFile() {
            return languageFile;
        }

        public void setLanguageFile(@NotNull String languageFile) {
            this.languageFile = languageFile;
        }

        public static class LocaleSerializer implements Serializer<Locale> {

            private LocaleSerializer() { }

            @Nullable
            @Override
            public Object serialize(@Nullable Locale locale) {
                return locale != null ? locale.toString() : MessageProvider.DEFAULT_LOCALE.toString();
            }

            @Nullable
            @Override
            public Locale deserialize(@Nullable Object object, @NotNull Class wantedType) throws IllegalArgumentException {
                if (object == null) {
                    return MessageProvider.DEFAULT_LOCALE;
                }
                String[] split = object.toString().split("_");
                switch (split.length) {
                    case 1:
                        return new Locale(split[0]);
                    case 2:
                        return new Locale(split[0], split[1]);
                    case 3:
                        return new Locale(split[0], split[1], split[2]);
                    default:
                        return new Locale(object.toString());
                }
            }
        }
    }

    private static class DebugLevelValidator implements Validator<Integer> {
        @Nullable
        @Override
        public Integer validateChange(@Nullable Integer newValue, @Nullable Integer oldValue) throws PropertyVetoException {
            if (newValue == null) {
                return oldValue == null ? 0 : oldValue;
            } else if (newValue >= 0 && newValue <= 3) {
                return newValue;
            } else {
                throw new PropertyVetoException(Message.bundleMessage(DebugCommand.INVALID_DEBUG));
            }
        }
    }

    private static class VirtualDebug implements VirtualProperty<Integer> {

        private PluginLogger logger;

        VirtualDebug(PluginLogger logger) {
            this.logger = logger;
        }

        @Override
        public Integer get() {
            return logger.getDebugLevel();
        }

        @Override
        public void set(Integer newValue) {
            logger.setDebugLevel(newValue);
        }
    }
}