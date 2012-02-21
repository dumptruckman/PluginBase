package com.dumptruckman.tools;

import com.dumptruckman.tools.api.circuit.Circuit;
import com.dumptruckman.tools.plugin.DPlugin;
import com.dumptruckman.tools.api.circuit.Circuits;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;

import java.util.HashMap;
import java.util.Map;

final class DefaultCircuitManager implements CircuitManager {

    DPlugin plugin;
    
    DefaultCircuitManager(DPlugin plugin) {
        this.plugin = plugin;
    }
    
    private Map<BlockLocation, Circuit> circuitMap = new HashMap<BlockLocation, Circuit>();
    
  /*  public Circuit newCircuit(Block block) {
        BlockLocation blockLocation = BlockLocation.get(block);
        Circuit circuit = this.circuitMap.get(blockLocation);
        if (circuit != null) {
            this.plugin.getData().removeCircuit(circuit);
        }
        circuit = new DefaultCircuit(block);
        this.circuitMap.put(blockLocation, circuit);
        return circuit;
    }*/

    @Override
    public Circuit getCircuit(Block block) {
        if (!(block.getState() instanceof Sign)) {
            return null;
        }
        Sign sign = (Sign) block.getState();
        Circuit circuit = getCircuit(sign);
        return circuit;
    }

    //@Override
    private Circuit getCircuit(Sign sign) {
        return Circuits.getCircuit(Circuits.lookup(sign.getLine(0)), sign);
    }
}
