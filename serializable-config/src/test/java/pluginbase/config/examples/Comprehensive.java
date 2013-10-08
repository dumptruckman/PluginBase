package pluginbase.config.examples;

import org.jetbrains.annotations.NotNull;
import pluginbase.config.annotation.Comment;
import pluginbase.config.annotation.Description;
import pluginbase.config.annotation.Immutable;
import pluginbase.config.annotation.SerializeWith;
import pluginbase.config.annotation.Stringify;
import pluginbase.config.annotation.ValidateWith;
import pluginbase.config.field.PropertyVetoException;
import pluginbase.config.field.Validator;
import pluginbase.config.field.VirtualField;
import pluginbase.config.properties.PropertiesWrapper;
import pluginbase.config.properties.PropertyAliases;
import pluginbase.config.properties.Stringifier;
import pluginbase.config.serializers.CustomSerializer2;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;

public class Comprehensive extends PropertiesWrapper {

    public static final int A_INT = 200;
    public static final String A_INT_DESCRIPTION = "Just some int";
    public static final String A_INT_COMMENT_1 = "Just some int";
    public static final String A_INT_COMMENT_2 = "Really.";
    public static final String[] A_INT_COMMENTS = {A_INT_COMMENT_1, A_INT_COMMENT_2};
    public static final int T_INT = 5;
    public static final String NAME = "Comprehensive";
    public static final List<String> WORD_LIST = new ArrayList<String>();
    public static final List<String> WORD_LIST_2 = new CopyOnWriteArrayList<String>();
    public static final List<List<String>> LIST_LIST = new ArrayList<List<String>>();
    public static final Child CHILD = new Child(true);
    public static final Parent PARENT = new Parent(CHILD);
    public static final List<Object> RANDOM_LIST = new ArrayList<Object>();
    public static final Map<String, Object> STRING_OBJECT_MAP = new HashMap<String, Object>();
    public static final Custom CUSTOM = new Custom("Custom");

    static {
        WORD_LIST.add("test");
        WORD_LIST.add("lol");
        WORD_LIST_2.add("omg");
        WORD_LIST_2.add("words");
        LIST_LIST.add(WORD_LIST);
        LIST_LIST.add(WORD_LIST_2);
        RANDOM_LIST.add(PARENT);
        RANDOM_LIST.add(CHILD);
        RANDOM_LIST.add(false);
        STRING_OBJECT_MAP.put("parent", PARENT);
        STRING_OBJECT_MAP.put("child", CHILD);
        STRING_OBJECT_MAP.put("String", "String");
        STRING_OBJECT_MAP.put("list", WORD_LIST);
        RANDOM_LIST.add(STRING_OBJECT_MAP);
        PropertyAliases.createAlias(Comprehensive.class, "cname", "custom", "name");
    }

    public Comprehensive() {
        super(".");
    }

    public static class NameValidator implements Validator<String> {
        @Nullable
        @Override
        public String validateChange(@Nullable String newValue, @Nullable String oldValue) throws PropertyVetoException {
            if (newValue != null && newValue.length() >= 4) {
                return newValue;
            } else {
                return oldValue;
            }
        }
    }

    public static class SimpleStringifier implements Stringifier {
        @NotNull
        @Override
        public String toString(@NotNull Object value) {
            if (value instanceof Simple) {
                return ((Simple) value).string;
            } else if (value instanceof List) {
                StringBuilder buffer = new StringBuilder();
                List<Simple> values = (List<Simple>) value;
                for (Simple simple : values) {
                    if (buffer.length() != 0) {
                        buffer.append(",");
                    }
                    buffer.append(simple.string);
                }
                return buffer.toString();
            }
            return value.toString();
        }
        @NotNull
        @Override
        public Object valueOf(@NotNull String value, @NotNull Class desiredType) throws PropertyVetoException {
            String[] values = value.split(",");
            List<Simple> list = new ArrayList<Simple>(values.length);
            for (String s : values) {
                list.add(new Simple(s));
            }
            return list;
        }
    }

    @Description(A_INT_DESCRIPTION)
    @Comment({A_INT_COMMENT_1, A_INT_COMMENT_2})
    public int aInt = A_INT;
    public transient int tInt = T_INT;
    @ValidateWith(NameValidator.class)
    public String name = NAME;
    public List<String> wordList = new ArrayList<String>(WORD_LIST);
    public List<String> wordList2 = new ArrayList<String>(WORD_LIST_2);
    public List<List<String>> listList = new ArrayList<List<String>>(LIST_LIST);
    public List<Object> randomList = new ArrayList<Object>(RANDOM_LIST);
    public Map<String, Object> stringObjectMap = new HashMap<String, Object>(STRING_OBJECT_MAP);
    public final Custom custom = new Custom(CUSTOM.name);
    @SerializeWith(CustomSerializer2.class)
    public Custom custom2 = new Custom(CUSTOM.name);
    @Immutable
    public String immutableString = NAME;
    public final Simple simple = new Simple();
    public final String finalString = NAME;
    public final VirtualField<Anum> virtualEnum = new VirtualField<Anum>() {
        private Anum actual = Anum.A;
        @Override
        public Anum get() {
            return actual;
        }
        @Override
        public void set(final Anum newValue) {
            actual = newValue;
        }
    };

    private VirtualField<List<?>> testWildCardListVirtualProp;
    private VirtualField<?> testWildCardVirtualProp;
    private VirtualField<List<String>> testTypedVirtualProp;
    private List<?> genericList = new ArrayList();

    @Stringify(withClass = SimpleStringifier.class)
    public List<Simple> simpleList = new ArrayList<Simple>();
    {
        simpleList.add(new Simple("test"));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Comprehensive)) return false;

        final Comprehensive that = (Comprehensive) o;

        if (aInt != that.aInt) return false;
        if (tInt != that.tInt) return false;
        if (!custom.equals(that.custom)) return false;
        if (!custom2.equals(that.custom2)) return false;
        if (!listList.equals(that.listList)) return false;
        if (!name.equals(that.name)) return false;
        if (!randomList.equals(that.randomList)) return false;
        if (!stringObjectMap.equals(that.stringObjectMap)) return false;
        if (!wordList.equals(that.wordList)) return false;
        if (!wordList2.equals(that.wordList2)) return false;
        if (!immutableString.equals(that.immutableString)) return false;
        if (!finalString.equals(that.finalString)) return false;
        if (!virtualEnum.get().equals(that.virtualEnum.get())) return false;
        if (!simple.equals(that.simple)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = aInt;
        result = 31 * result + tInt;
        result = 31 * result + name.hashCode();
        result = 31 * result + wordList.hashCode();
        result = 31 * result + wordList2.hashCode();
        result = 31 * result + listList.hashCode();
        result = 31 * result + randomList.hashCode();
        result = 31 * result + stringObjectMap.hashCode();
        result = 31 * result + custom.hashCode();
        result = 31 * result + custom2.hashCode();
        result = 31 * result + immutableString.hashCode();
        result = 31 * result + finalString.hashCode();
        result = 31 * result + virtualEnum.hashCode();
        result = 31 * result + simple.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "Comprehensive{" +
                "aInt=" + aInt +
                ", tInt=" + tInt +
                ", name='" + name + '\'' +
                ", wordList=" + wordList +
                ", wordList2=" + wordList2 +
                ", listList=" + listList +
                ", randomList=" + randomList +
                ", stringObjectMap=" + stringObjectMap +
                ", custom=" + custom +
                ", custom2=" + custom2 +
                ", immutableString=" + immutableString +
                ", finalString='" + finalString + '\'' +
                ", virtualEnum=" + virtualEnum.get() +
                ", simple=" + simple +
                '}';
    }
}
