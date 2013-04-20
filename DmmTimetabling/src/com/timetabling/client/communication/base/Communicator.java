package com.timetabling.client.communication.base;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.event.shared.SimpleEventBus;

public class Communicator {
	
    /** main event bus for application **/
    public final EventBus eventBus = new SimpleEventBus();

    public final ObjectifyRequestFactory requestFactory = GWT.create( ObjectifyRequestFactory.class );

    private Communicator() {
        requestFactory.initialize( eventBus );
    }
    
    
    private static Communicator instance = new Communicator();
    
    public static Communicator get() {
    	return instance;
    }
    
}