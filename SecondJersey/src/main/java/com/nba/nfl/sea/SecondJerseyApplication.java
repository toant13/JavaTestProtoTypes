package com.nba.nfl.sea;

import java.util.HashSet;
import java.util.Set;

import javax.ws.rs.core.Application;

import org.glassfish.jersey.media.multipart.MultiPartFeature;
import org.glassfish.jersey.media.sse.SseFeature;

public class SecondJerseyApplication extends Application{
	public Set<Class<?>> getClasses() {
        final Set<Class<?>> classes = new HashSet<Class<?>>();

        classes.add(MultiPartFeature.class);
        classes.add(SecondJersResource.class);
        classes.add(SseFeature.class);
        return classes;
    }
}
