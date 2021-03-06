package com.test.client;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;

import org.glassfish.jersey.media.multipart.FormDataMultiPart;
import org.glassfish.jersey.media.multipart.MultiPart;
import org.glassfish.jersey.media.multipart.MultiPartFeature;
import org.glassfish.jersey.media.multipart.file.FileDataBodyPart;
import org.glassfish.jersey.media.multipart.file.StreamDataBodyPart;
import org.glassfish.jersey.media.sse.EventInput;
import org.glassfish.jersey.media.sse.EventListener;
import org.glassfish.jersey.media.sse.EventSource;
import org.glassfish.jersey.media.sse.InboundEvent;
import org.glassfish.jersey.media.sse.SseFeature;

public class ClientJersey2 {

	
	public static void main(String[] args) throws IOException, InterruptedException {
		System.out.println("starting");
		ClientJersey2 cj = new ClientJersey2();
		cj.getTest();
//		cj.getSse();	//sse without thread (getSse and getStart can't be ran together)
		cj.getStart(); //sse  with threads
//		cj.save();
//		cj.saveStream();
//		
//		cj.getFile();
		
		System.out.println("done");
	}
	
	
	
	
	public void getStart() throws InterruptedException
	{

		
		Client client = ClientBuilder.newBuilder()
		        .register(SseFeature.class).build();
		WebTarget target = client.target(getBaseURI());
		 
		Response response = target.path("myresource").path("start").queryParam("testSources", "1").request().post(Entity.text("data"), Response.class);
		
		System.out.println(Response.Status.CREATED.getStatusCode());
		System.out.println(response.getLocation().toString());
		String processUriString = target.getUri().relativize(response.getLocation()).toString();
		System.out.println(processUriString);
		

		WebTarget target2 = client.target(getBaseURI());
		 
		EventInput eventInput = target2.path(processUriString).queryParam("testSource", "true").request().get(EventInput.class);
		while (!eventInput.isClosed()) {
		    final InboundEvent inboundEvent = eventInput.read();
		    if (inboundEvent == null) {
		        // connection has been closed
		        break;
		    }
		    System.out.println(inboundEvent.getName() + "; "
		        + inboundEvent.readData(String.class));
		}
	}
	
	public void getTest()
	{	
		Client client = ClientBuilder.newClient();
		
		WebTarget target = client.target(getBaseURI());
		String result = target.path("myresource").path("msg").request().get(String.class);
		System.out.println(result);			
	}
	
	
	public void save()
	{
		Client client = ClientBuilder.newClient().register(MultiPartFeature.class);	
		WebTarget target = client.target(getBaseURI());
		
		File data = new File("src/main/resources/input/input.xml");
        FileDataBodyPart fileDataBodyPart = new FileDataBodyPart("file", data, MediaType.APPLICATION_OCTET_STREAM_TYPE);
        MultiPart entity = new FormDataMultiPart().bodyPart(fileDataBodyPart);
        String response = target.path("myresource").path("save").request().post(Entity.entity(entity, MediaType.MULTIPART_FORM_DATA_TYPE), String.class);
        System.out.println(response);
	}
	
	public void saveStream() throws FileNotFoundException
	{
		Client client = ClientBuilder.newClient().register(MultiPartFeature.class);	
		WebTarget target = client.target(getBaseURI());
		
		File data = new File("src/main/resources/input/input.xml");
//        FileDataBodyPart fileDataBodyPart = new FileDataBodyPart("file", data, MediaType.APPLICATION_OCTET_STREAM_TYPE);
        
        InputStream fStream = new FileInputStream(data);
        StreamDataBodyPart sb = new StreamDataBodyPart("file",fStream,data.getName(),MediaType.APPLICATION_OCTET_STREAM_TYPE);
        
        MultiPart entity = new FormDataMultiPart().bodyPart(sb);
        String response = target.path("myresource").path("stream").request().post(Entity.entity(entity, MediaType.MULTIPART_FORM_DATA_TYPE), String.class);
        System.out.println(response);
	}
	
	public void getFile() throws IOException
	{
		Client client = ClientBuilder.newClient().register(MultiPartFeature.class);	
		WebTarget target = client.target(getBaseURI());
        MultiPart response = target.path("myresource").path("getfile").request().get(MultiPart.class);
        
        
        File file = response.getBodyParts().get(0).getEntityAs(File.class);
      
        System.out.println("bodypart: " +response.getBodyParts().get(1).getEntityAs(String.class));
//        System.out.println(response.getBodyParts().get(0).getContentDisposition().getFileName());
        BufferedReader br = new BufferedReader(new FileReader(file));
        String line = null;
        while ((line = br.readLine()) != null) {
          System.out.println(line);
        }
	}
	
	public void getSse(){
		Client client = ClientBuilder.newBuilder()
		        .register(SseFeature.class).build();
		WebTarget target = client.target(getBaseURI());
		 
		EventInput eventInput = target.path("myresource").path("sse").request().get(EventInput.class);
		while (!eventInput.isClosed()) {
		    final InboundEvent inboundEvent = eventInput.read();
		    if (inboundEvent == null) {
		        // connection has been closed
		        break;
		    }
		    System.out.println(inboundEvent.getName() + "; "
		        + inboundEvent.readData(String.class));
		}
	}
	
	private URI getBaseURI()
	{
		return UriBuilder.fromUri("http://localhost:8080/").build();
	}
	
	
	
}
