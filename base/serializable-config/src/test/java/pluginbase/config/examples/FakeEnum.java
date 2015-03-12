package pluginbase.config.examples;

import pluginbase.config.annotation.FauxEnum;

@FauxEnum
public class FakeEnum {

    public static final FakeEnum FAKE_1 = new FakeEnum("FAKE_1");
    public static final FakeEnum FAKE_2 = new FakeEnum("FAKE_2");

    public static FakeEnum valueOf(String fakeEnum) {
        if (fakeEnum.equalsIgnoreCase("FAKE_1")) {
            return FAKE_1;
        } else if (fakeEnum.equalsIgnoreCase("FAKE_2")) {
            return FAKE_2;
        }
        return null;
    }

    private String name;

    private FakeEnum(String name) {
        this.name = name;
    }

    public String name() {
        return name;
    }
}
