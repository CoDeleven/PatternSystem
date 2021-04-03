package com.codeleven.core.transfom;


import com.codeleven.common.constants.TransformOperation;
import com.codeleven.core.transform.TransformReceiver;
import com.codeleven.core.transform.command.ITransformCommand;
import com.codeleven.core.transform.command.TranslateYCommand;
import org.junit.Test;
import org.mockito.Mockito;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;


/**
 * Test TranslateYCommand
 * {@link TranslateYCommand}
 */
public class TranslateYCommandTest {

    @Test
    public void testCorrectDependency() {
        TransformReceiver mock = mock(TransformReceiver.class);
        ITransformCommand command = new TranslateYCommand(mock, 10);
        command.execute();
        Mockito.verify(mock, times(1)).receive(TransformOperation.MOVE_Y, 10);
    }
}
