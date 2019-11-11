package com.mejesticpay.eventstore.executor;

import com.mejesticpay.events.Event;

public interface EventExecutor
{
    public void handleEvent(Event event);
}
