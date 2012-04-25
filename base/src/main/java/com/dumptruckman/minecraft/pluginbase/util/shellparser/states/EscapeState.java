package com.dumptruckman.minecraft.pluginbase.util.shellparser.states;

import java.text.ParseException;
import java.util.List;

public class EscapeState extends State {

    @Override
    public List<String> parse(String parsing, String accumulator, List<String> parsed, State referrer) throws ParseException {
        if(parsing.length() == 0) {
            throw new ParseException("Unexpected end of string after escape character", 0);
        }
        
        return referrer.parse(parsing.substring(1), accumulator + (char)(parsing.getBytes()[0]), parsed, this);
    }

}
