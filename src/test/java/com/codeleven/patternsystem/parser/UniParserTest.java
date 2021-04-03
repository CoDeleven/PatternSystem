package com.codeleven.patternsystem.parser;

import com.codeleven.parser.IParserStrategy;
import com.codeleven.parser.UniParser;
import com.codeleven.parser.shangyi.SystemTopParserStrategy;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class UniParserTest {
    @Test
    public void testGetTargetParserStrategyError() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Method getTargetParserStrategyMethod = UniParser.class.getDeclaredMethod("getTargetParserStrategy", new byte[]{}.getClass());
        UniParser uniParser = new UniParser();
        IParserStrategy invokeResult = (IParserStrategy) getTargetParserStrategyMethod.invoke(uniParser, new byte[]{0x1, 0x1, 0x1, 0x1, 0x1});
        Assertions.assertNull(invokeResult);
    }

    @Test
    public void testGetTargetParserStrategyForSystemTop() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Method getTargetParserStrategyMethod = UniParser.class.getDeclaredMethod("getTargetParserStrategy", new byte[]{}.getClass());
        UniParser uniParser = new UniParser();
        IParserStrategy invokeResult = (IParserStrategy) getTargetParserStrategyMethod.invoke(uniParser, new byte[]{(byte) 0xAA, 0x55, 0x08, 0x00, 0x11});
        Assertions.assertNotNull(invokeResult);
        Assertions.assertEquals(SystemTopParserStrategy.class, invokeResult.getClass());
    }
}
