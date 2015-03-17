package pluginbase.testplugin.pie;

import pluginbase.config.annotation.SerializeWith;

@SerializeWith(PieSerializer.class)
public class Pie {

    PieProperties properties;

    private long time = 0;

    public Pie(PieProperties properties) {
        this.properties = properties;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public String getName() {
        return properties.getName();
    }

    public void setNumber(int number) {
        properties.setNumber(number);
    }

    public void setName(String name) {
        properties.setName(name);
    }

    public int getNumber() {
        return properties.getNumber();
    }
}
