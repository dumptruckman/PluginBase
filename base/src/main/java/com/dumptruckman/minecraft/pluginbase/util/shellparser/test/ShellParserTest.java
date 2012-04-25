package com.dumptruckman.minecraft.pluginbase.util.shellparser.test;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import com.dumptruckman.minecraft.pluginbase.util.shellparser.ShellParser;
import junit.framework.Assert;

import org.junit.Test;

public class ShellParserTest {
    @Test
    public void testEmpty() {
        Assert.assertEquals(new ArrayList<String>(), ShellParser.safeParseString(""));
    }
    
    @Test
    public void testWord() {
        Assert.assertEquals(new ArrayList<String>() {{ add("test"); }}, ShellParser.safeParseString("test"));
    }
    
    @Test
    public void testTwoWords() {
        Assert.assertEquals(new ArrayList<String>() {{ add("a"); add("b"); }}, ShellParser.safeParseString("a b"));
    }
    
    @Test
    public void testManyWords() {
        List<String> expected = new ArrayList<String>() {{
            add("a");
            add("b");
            add("c");
            add("d");
            add("e");
        }};
        
        Assert.assertEquals(expected, ShellParser.safeParseString("a b c d e"));
    }
    
    @Test
    public void testEscapedLiteral() {
        Assert.assertEquals(new ArrayList<String>() {{ add("test"); }}, ShellParser.safeParseString("\\test"));
    }
    
    @Test
    public void testDoubleQuotes() {
        Assert.assertEquals(new ArrayList<String>() {{ add("test test"); }}, ShellParser.safeParseString("\"test test\""));
    }
    
    @Test
    public void testMixedDoubleQuotes() {
        Assert.assertEquals(new ArrayList<String>() {{ add("test"); add("test test"); add("test"); }}, ShellParser.safeParseString("test \"test test\" test"));
    }
    
    @Test
    public void testSingleQuotes() {
        Assert.assertEquals(new ArrayList<String>() {{ add("test test"); }}, ShellParser.safeParseString("'test test'"));
    }
    
    @Test
    public void testMixedSingleQuotes() {
        Assert.assertEquals(new ArrayList<String>() {{ add("test"); add("test test"); add("test"); }}, ShellParser.safeParseString("test 'test test' test"));
    }
    
    @Test
    public void testMixedQuotes() {
        Assert.assertEquals(new ArrayList<String>() {{ add("test test"); add("test test"); }}, ShellParser.safeParseString("\"test test\" 'test test'"));
    }
    
    @Test
    public void testNestedQuotes() {
        Assert.assertEquals(new ArrayList<String>() {{ add("test 'test test'"); }}, ShellParser.safeParseString("\"test 'test test'\""));
    }
    
    @Test(expected=ParseException.class)
    public void testMismatchedDoubleQuote() throws ParseException {
        ShellParser.parseString("\"");
    }
    
    @Test(expected=ParseException.class)
    public void testMismatchedSingleQuote() throws ParseException {
        ShellParser.parseString("'");
    }
    
    @Test(expected=ParseException.class)
    public void testBadEscape() throws ParseException {
        ShellParser.parseString("\\");
    }
}
