package pluginbase.bukkit;

import pluginbase.bukkit.pie.Pie;
import pluginbase.bukkit.pie.PieListSerializer;
import pluginbase.config.annotation.SerializeWith;
import pluginbase.plugin.Settings;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class TestConfig extends Settings {

    private UUID testUUID = UUID.randomUUID();

    @SerializeWith(PieListSerializer.class)
    private List<Pie> pieList = new ArrayList<Pie>();

    public List<Pie> getPieList() {
        return pieList;
    }
}
