package com.codeleven.core.transfom;

import com.codeleven.common.constants.TransformOperation;
import com.codeleven.core.transform.TransformReceiver;
import com.codeleven.core.transform.command.ChangeFirstSewingCommand;
import com.codeleven.core.transform.command.ITransformCommand;
import org.junit.Test;
import org.mockito.Mockito;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;

public class ChangeFirstSewingCommandTest {

    @Test
    public void execute() {
        TransformReceiver mock = mock(TransformReceiver.class);
        ITransformCommand command = new ChangeFirstSewingCommand(mock, 10);
        command.execute();
        Mockito.verify(mock, times(1)).receive(TransformOperation.CHANGE_FIRST_SEWING, 10);
    }
}