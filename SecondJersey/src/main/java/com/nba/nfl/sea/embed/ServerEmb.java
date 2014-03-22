package com.nba.nfl.sea.embed;

import java.net.URI;

import javax.ws.rs.core.UriBuilder;

import org.eclipse.jetty.server.Server;
import org.glassfish.jersey.jetty.JettyHttpContainerFactory;
import org.glassfish.jersey.server.ResourceConfig;

import com.nba.nfl.sea.SecondJerseyApplication;

public class ServerEmb {
	public static void main(String[] args) {
		URI baseUri = UriBuilder.fromUri("http://localhost/").port(8081).build();
		ResourceConfig config = ResourceConfig.forApplicationClass(SecondJerseyApplication.class);
	
		Server server = JettyHttpContainerFactory.createServer(baseUri, config);

	}
}
