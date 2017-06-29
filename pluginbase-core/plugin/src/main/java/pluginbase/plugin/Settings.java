package pluginbase.plugin;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import pluginbase.config.annotation.Comment;
import pluginbase.config.annotation.SerializeWith;
import pluginbase.config.annotation.ValidateWith;
import pluginbase.config.field.DependentField;
import pluginbase.config.field.PropertyVetoException;
import pluginbase.config.field.Validator;
import pluginbase.config.field.Validators;
import pluginbase.config.properties.PropertiesWrapper;
import pluginbase.config.serializers.Serializer;
import pluginbase.config.serializers.SerializerSet;
import pluginbase.logging.PluginLogger;
import pluginbase.messages.Message;
import pluginbase.messages.MessageProvider;
import pluginbase.plugin.command.builtin.DebugCommand;

import java.util.Locale;

public class Settings extends PropertiesWrapper {

    @Comment("0 = off, 1-3 display debug info with increasing granularity.")
    @ValidateWith(DebugLevelValidator.class)
    private final VirtualDebug debugLevel = new VirtualDebug();

    @Comment("Will make the plugin perform tasks only done on a first run (if any.)")
    private boolean firstRun = true;

    @Comment({"This is the locale you wish to use for messages.", "The general format is a 2 character language code followed by an underscore and a 2 character capitalized country code"})
    @SerializeWith(LocaleSerializer.class)
    @NotNull
    private Locale locale = MessageProvider.DEFAULT_LOCALE.getValue();

    public Settings(@NotNull PluginBase plugin) {
        this.debugLevel.setLogger(plugin.getLog());
    }

    protected Settings() { }

    public int getDebugLevel() {
        Integer level = debugLevel.get();
        return level != null ? level : 0;
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

    @NotNull
    public Locale getLocale() {
        return locale;
    }

    public void setLocale(@NotNull Locale locale) {
        this.locale = locale;
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
                throw new PropertyVetoException(DebugCommand.INVALID_DEBUG.bundle());
            }
        }
    }

    private static class VirtualDebug extends DependentField<Integer, PluginLogger> {

        private PluginLogger logger = null;

        VirtualDebug() {
            super(0);
        }

        public void setLogger(PluginLogger logger) {
            this.logger = logger;
        }

        protected PluginLogger getDependency() {
            return logger;
        }

        @Override
        protected Integer getDependentValue() {
            return getDependency().getDebugLevel();
        }

        @Override
        protected void setDependentValue(@Nullable Integer value) {
            getDependency().setDebugLevel(value != null ? value : 0);
        }
    }

    public static class LocaleSerializer implements Serializer<Locale> {

        private LocaleSerializer() { }

        @Nullable
        @Override
        public Object serialize(@Nullable Locale locale, @NotNull SerializerSet serializerSet) {
            return locale != null ? locale.toString() : MessageProvider.DEFAULT_LOCALE.getValue().toString();
        }

        @Nullable
        @Override
        public Locale deserialize(@Nullable Object object, @NotNull Class wantedType, @NotNull SerializerSet serializerSet) throws IllegalArgumentException {
            if (object == null) {
                return MessageProvider.DEFAULT_LOCALE.getValue();
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
