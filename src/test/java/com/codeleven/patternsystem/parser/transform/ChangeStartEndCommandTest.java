package com.codeleven.patternsystem.parser.transform;

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
        Mockito.verify(mock, times(1)).receive(TransformReceiver.Operation.CHANGE_START_END);
    }
}