package com.codeleven.patternsystem.parser.transform;

import org.junit.Test;
import org.mockito.Mockito;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;


public class ChangePatternSewingSeqCommandTest {

    @Test
    public void execute() {
        TransformReceiver mock = mock(TransformReceiver.class);
        ITransformCommand command = new ChangePatternSewingSeqCommand(mock, 3, 0);
        command.execute();
        Mockito.verify(mock, times(1)).receive(TransformReceiver.Operation.CHANGE_PATTERN_SEWING_SEQ, 3, 0);
    }
}