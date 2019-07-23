package tool;

import org.junit.Test;

import java.util.List;
import java.util.StringJoiner;

import static org.junit.Assert.*;


public class MyTranslatorTest {

    @Test
    public void testPlainText(){
        String s = "How are you?";
        String expected = "How are you?";
        List<String> result = MyTranslator.translate(s);
        assertEquals(expected, print(result));
    }

    @Test
    public void testSimpleOr(){
        String s = "I know when John|Jim is coming|leaving";
        String expected = "I know when John is coming; I know when John is leaving; I know when Jim is coming; I know when Jim is leaving";
        List<String> result = MyTranslator.translate(s);
        assertEquals(expected, print(result));
    }

    @Test
    public void testOrWithBrackets(){
        String s = "{{It is}|It's} up to you|him|her";
        String expected = "It is up to you; It is up to him; It is up to her; It's up to you; It's up to him; It's up to her";
        List<String> result = MyTranslator.translate(s);
        assertEquals(expected, print(result));
    }

    @Test
    public void testTwoParallelBrackets(){
        String s = "{I'm good}|{It is fabulous|fantastic}";
        String expected = "I'm good; It is fabulous; It is fantastic";
        List<String> result = MyTranslator.translate(s);
        assertEquals(expected, print(result));
    }

    @Test
    public void testWithNestedBrackets(){
        String s = "{Jim|Kate} helped {{Mr Huang}|{Ms Huang}} a lot {{removing snow}|{cutting grass}}";
        String expected = "Jim helped Mr Huang a lot removing snow; Jim helped Mr Huang a lot cutting grass; Jim helped Ms Huang a lot removing snow; Jim helped Ms Huang a lot cutting grass; Kate helped Mr Huang a lot removing snow; Kate helped Mr Huang a lot cutting grass; Kate helped Ms Huang a lot removing snow; Kate helped Ms Huang a lot cutting grass";
        List<String> result = MyTranslator.translate(s);
        assertEquals(expected, print(result));
    }

    @Test
    public void testWithChinese(){
        String s="好 楞|鬼 劲|犀利|sharp";
        String expected = "好楞劲; 好楞犀利; 好楞 sharp; 好鬼劲; 好鬼犀利; 好鬼 sharp";
        List<String> result = MyTranslator.translate(s);
        assertEquals(expected, print(result));
    }

    private String print(List<String> list){
        StringJoiner sj = new StringJoiner("; ");
        list.forEach(e->sj.add(e));
        return sj.toString();
    }
}