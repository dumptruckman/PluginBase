package com.dumptruckman.minecraft.pluginbase.util.shellparser.states;

import java.text.ParseException;
import java.util.List;

public abstract class State {
    public abstract List<String> parse(String parsing, String accumulator, List<String> parsed, State referrer) throws ParseException;
}
