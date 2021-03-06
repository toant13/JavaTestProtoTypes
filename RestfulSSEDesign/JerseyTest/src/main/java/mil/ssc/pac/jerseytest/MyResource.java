
package mil.ssc.pac.jerseytest;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URI;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import javax.ws.rs.Consumes;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;

import org.glassfish.jersey.media.multipart.BodyPart;
import org.glassfish.jersey.media.multipart.FormDataContentDisposition;
import org.glassfish.jersey.media.multipart.FormDataMultiPart;
import org.glassfish.jersey.media.multipart.FormDataParam;
import org.glassfish.jersey.media.multipart.MultiPart;
import org.glassfish.jersey.media.multipart.file.FileDataBodyPart;
import org.glassfish.jersey.media.sse.EventOutput;
import org.glassfish.jersey.media.sse.OutboundEvent;
import org.glassfish.jersey.media.sse.SseBroadcaster;
import org.glassfish.jersey.media.sse.SseFeature;
import org.glassfish.jersey.server.ChunkedOutput;




/** Example resource class hosted at the URI path "/myresource"
 */
@Path("/myresource")
public class MyResource {
	private static final Map<Integer, Process> processes = new ConcurrentHashMap<Integer, Process>();
	
	
	
    /** Method processing HTTP GET requests, producing "text/plain" MIME media
     * type.
     * @return String that will be send back as a response of type "text/plain".
     */
    @GET 
    @Path("/msg")
    @Produces("text/plain")
    public String getIt() {
        return "Hi therdfasdfse!";
    }
    
    @POST
    @Path("/save")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces("text/plain")
    public String upload(@FormDataParam("file") InputStream is, @FormDataParam("file") FormDataContentDisposition header) throws IOException
    {
    	OutputStream out = new FileOutputStream(new File("src/main/resources/output/" + header.getFileName()));
    	
    	int read = 0;
    	byte[] bytes = new byte[1024];
    	
    	while((read = is.read(bytes)) != -1){
    		out.write(bytes, 0, read);
    	}
    	out.close();
    	return "successful";
    }
    
    @GET 
    @Path("/getfile")
    @Produces("multipart/mixed")
    public MultiPart download() throws FileNotFoundException {
    	File data = new File("src/main/resources/serverFile/servFile.xml");
    	InputStream in = new FileInputStream(data);
    	FileDataBodyPart fileDataBodyPart = new FileDataBodyPart("file", data, MediaType.APPLICATION_OCTET_STREAM_TYPE);
        MultiPart entity = new FormDataMultiPart().bodyPart(fileDataBodyPart);
        BodyPart bp = new BodyPart();
        bp.setEntity("filenametest");
        entity.bodyPart(bp);
        System.out.println("created entity");
        return entity;
    }
    
    @GET 
    @Path("/getfilestream")
    @Produces("multipart/mixed")
    public MultiPart downloadStream() throws FileNotFoundException {
    	File data = new File("/Users/toantran/Documents/Java_development/workspace/JerseyTest/src/main/resources/serverFile/servFile.xml");
    	InputStream in = new FileInputStream(data);
    	FileDataBodyPart fileDataBodyPart = new FileDataBodyPart("file", data, MediaType.APPLICATION_OCTET_STREAM_TYPE);
        MultiPart entity = new FormDataMultiPart().bodyPart(fileDataBodyPart);
        
        BodyPart bp = new BodyPart();
        bp.setEntity("filenametest");
        entity.bodyPart(bp);
        System.out.println("created entity");
        return entity;
    }
    
    
    @POST
    @Path("/stream")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces("text/plain")
    public String uploadStream(@FormDataParam("file") InputStream is, @FormDataParam("file") FormDataContentDisposition header) throws IOException
    {
    	OutputStream out = new FileOutputStream(new File("src/main/resources/output/outputStream.xml"));
    	
    	int read = 0;
    	byte[] bytes = new byte[1024];
    	
    	while((read = is.read(bytes)) != -1){
    		out.write(bytes, 0, read);
    	}
    	out.close();
    	return "streamsuccessful";
    }
    
    @GET
    @Path("/sse")
    @Produces(SseFeature.SERVER_SENT_EVENTS)
    public EventOutput getServerSentEvents() {
        final EventOutput eventOutput = new EventOutput();
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    for (int i = 0; i < 10; i++) {
                        // ... code that waits 1 second
                        final OutboundEvent.Builder eventBuilder
                        = new OutboundEvent.Builder();
                        eventBuilder.name("message-to-client");
                        eventBuilder.data(String.class,
                            "Hello world " + i + "!");
                        final OutboundEvent event = eventBuilder.build();
                        eventOutput.write(event);
                    }
                } catch (IOException e) {
                    throw new RuntimeException(
                        "Error when writing the event.", e);
                } finally {
                    try {
                        eventOutput.close();
                    } catch (IOException ioClose) {
                        throw new RuntimeException(
                            "Error when closing the event output.", ioClose);
                    }
                }
            }
        }).start();
        return eventOutput;
    }
    
    /****************************SSE TEST***********************************/
    
    @Path("/start")
    @POST
    public Response post(@DefaultValue("0") @QueryParam("testSources") int testSources) {
    	System.out.println("start");
        final Process process = new Process(testSources);
        processes.put(process.getId(), process);

        Executors.newSingleThreadExecutor().execute(process);

        final URI processIdUri = UriBuilder.fromResource(MyResource.class).path("process/{id}").build(process.getId());
        return Response.created(processIdUri).build();
    }

    @Path("/process/{id}")
    @Produces(SseFeature.SERVER_SENT_EVENTS)
    @GET
    public EventOutput getProgress(@PathParam("id") int id,
                                    @DefaultValue("false") @QueryParam("testSource") boolean testSource) {
        final Process process = processes.get(id);

        if (process != null) {
        	System.out.println("found");
            if (testSource) {
                process.release();
            }
            final EventOutput eventOutput = new EventOutput();
            process.getBroadcaster().add(eventOutput);
            return eventOutput;
        } else {
            throw new NotFoundException();
        }
    }

    
    
    
    static class Process implements Runnable {

        private static final AtomicInteger counter = new AtomicInteger(0);

        private final int id;
        private final CountDownLatch latch;
        private final SseBroadcaster broadcaster = new SseBroadcaster() {
            @Override
            public void onException(ChunkedOutput<OutboundEvent> outboundEventChunkedOutput, Exception exception) {
                exception.printStackTrace();
            }
        };

        public Process(int testReceivers) {
            id = counter.incrementAndGet();
            latch = testReceivers > 0 ? new CountDownLatch(testReceivers) : null;
        }

        public int getId() {
            return id;
        }

        public SseBroadcaster getBroadcaster() {
            return broadcaster;
        }

        public boolean release() {
            if (latch == null) {
                return false;
            }

            latch.countDown();
            return true;
        }

        public void run() {
            try {
                if (latch != null) {
                    // wait for all test EventSources to be registered
                    latch.await(5, TimeUnit.SECONDS);
                }

                broadcaster.broadcast(new OutboundEvent.Builder().data(String.class, "starting domain " + id + " ...").build());
                Thread.sleep(500);
                broadcaster.broadcast(new OutboundEvent.Builder().name("domain-progress").data(String.class, "50%").build());
                Thread.sleep(500);
                broadcaster.broadcast(new OutboundEvent.Builder().name("domain-progress").data(String.class, "60%").build());
                Thread.sleep(500);
                broadcaster.broadcast(new OutboundEvent.Builder().name("domain-progress").data(String.class, "70%").build());
                Thread.sleep(500);
                broadcaster.broadcast(new OutboundEvent.Builder().name("domain-progress").data(String.class, "99%").build());
                Thread.sleep(500);
                broadcaster.broadcast(new OutboundEvent.Builder().name("domain-progress").data(String.class, "done").build());
                Thread.sleep(500);
                broadcaster.closeAll();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
    
}
