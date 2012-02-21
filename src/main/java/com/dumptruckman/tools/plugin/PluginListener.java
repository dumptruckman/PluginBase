package com.dumptruckman.tools.plugin;

import com.dumptruckman.tools.api.circuit.Circuit;
import com.dumptruckman.tools.api.circuit.Circuits;
import com.dumptruckman.tools.util.Perm;
import org.bukkit.block.Sign;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockRedstoneEvent;
import org.bukkit.event.block.SignChangeEvent;

/**
 * Class that handles all bukkit events for this Plugin.
 */
public class PluginListener implements Listener {

    private DPlugin plugin;
    private CircuitManager circuitManager;

    public PluginListener(DPlugin plugin) {
        this.plugin = plugin;
        this.circuitManager = plugin.getCircuitManager();
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void signChanged(SignChangeEvent event) {
        Class<? extends Circuit> circuit = Circuits.lookup(event.getLine(0));
        if (circuit != null && 
                !Perm.CIRCUITS_FREE_PLACE.has(event.getPlayer())) {
            System.out.println(event.getPlayer().getName()
                    + " is not allowed to place circuits directly as signs");
            event.setCancelled(true);
        }
    }
    
    @EventHandler
    public void blockRedstone(BlockRedstoneEvent event) {
        if (!(event.getBlock().getState() instanceof Sign)) {
            return;
        }
        Circuit circuit = this.circuitManager.getCircuit(event.getBlock());
        if (circuit != null) {
            circuit.handleEvent(event);
        }
    }
}
