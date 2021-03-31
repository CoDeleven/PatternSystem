package com.codeleven.patternsystem.parser.transform;


import cn.hutool.core.io.IoUtil;
import com.codeleven.patternsystem.entity.UniPattern;
import com.codeleven.patternsystem.parser.UniParser;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.io.InputStream;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;


/**
 * Test TranslateYCommand
 *  {@link com.codeleven.patternsystem.parser.transform.TranslateYCommand}
 */
public class TranslateYCommandTest {

    @Test
    public void testCorrectDependency() {
        TransformReceiver mock = mock(TransformReceiver.class);
        ITransformCommand command = new TranslateYCommand(mock, 10);
        command.execute();
        Mockito.verify(mock, times(1)).receive(TransformReceiver.Operation.MOVE_Y, 10);
    }
}
