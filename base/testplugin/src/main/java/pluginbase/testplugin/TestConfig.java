package pluginbase.testplugin;

import pluginbase.testplugin.pie.Pie;
import pluginbase.testplugin.pie.PieListSerializer;
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
