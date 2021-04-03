package com.codeleven.core.transfom;

import com.codeleven.common.constants.TransformOperation;
import com.codeleven.core.transform.TransformReceiver;
import com.codeleven.core.transform.command.ChangeStartEndCommand;
import com.codeleven.core.transform.command.ITransformCommand;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;

/**
 * 测试 ExchangeSECommand
 * {@link ChangeStartEndCommand}
 */
class ChangeStartEndCommandTest {

    @Test
    void execute() {
        TransformReceiver mock = mock(TransformReceiver.class);
        ITransformCommand command = new ChangeStartEndCommand(mock);
        command.execute();
        Mockito.verify(mock, times(1)).receive(TransformOperation.CHANGE_START_END);
    }
}