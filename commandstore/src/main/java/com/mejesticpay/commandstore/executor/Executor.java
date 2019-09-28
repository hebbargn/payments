package com.mejesticpay.commandstore.executor;

import com.mejesticpay.commands.Command;
import com.mejesticpay.commandstore.model.CommandModel;

public interface Executor
{
    public CommandModel execute(Command command);
}
