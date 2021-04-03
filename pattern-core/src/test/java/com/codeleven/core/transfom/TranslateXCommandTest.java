package com.codeleven.core.transfom;


import com.codeleven.common.constants.TransformOperation;
import com.codeleven.core.transform.TransformReceiver;
import com.codeleven.core.transform.command.ITransformCommand;
import com.codeleven.core.transform.command.TranslateXCommand;
import org.junit.Test;
import org.mockito.Mockito;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;


/**
 * TransformHelper
 */
public class TranslateXCommandTest {

    @Test
    public void testCorrectDependency() {
        TransformReceiver mock = mock(TransformReceiver.class);
        ITransformCommand command = new TranslateXCommand(mock, 10);
        command.execute();
        Mockito.verify(mock, times(1)).receive(TransformOperation.MOVE_X, 10);
    }

}
