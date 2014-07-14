package pluginbase.bukkit.pie;

import pluginbase.config.annotation.SerializableAs;

@SerializableAs("Pie")
public class PieProperties {

    private String name = "";

    private int number = 0;

    public PieProperties() {

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }
}
