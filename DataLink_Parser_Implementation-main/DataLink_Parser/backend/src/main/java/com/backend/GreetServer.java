package com.backend;

import io.grpc.Server;
import io.grpc.ServerBuilder;
import io.grpc.stub.StreamObserver;
import java.io.IOException;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;
import protocomp.GreeterGrpc;
import protocomp.TrackRequest;
import protocomp.TrackMessage;

public class GreetServer {

   private static final Logger logger = Logger.getLogger(GreetServer.class.getName());
   private Server server;
   

   public void start(Track track) throws IOException {
      
      int port = 50051;

      server = ServerBuilder.forPort(port).addService(new GreeterImpl(track)).build().start();
       
      logger.info("Server started, listening on " + port);
 
      Runtime.getRuntime().addShutdownHook(new Thread() {
         @Override
         public void run() {
            System.err.println("Shutting down gRPC server");
            try {
               server.shutdown().awaitTermination(30, TimeUnit.SECONDS);
            } catch (InterruptedException e) {
               e.printStackTrace(System.err);
            }
         }
      });
   }
   public Server getServer(){
      return server;
   }
   static class GreeterImpl extends GreeterGrpc.GreeterImplBase {
      private Track track;
      public GreeterImpl(Track track){
         this.track=track;
      }
      @Override
      public void greet(TrackRequest req, StreamObserver<TrackMessage> responseObserver) {
         logger.info("Got request from client: ");
         
         //Track track1 = new Track(4,2,3,(short)15,(short)15);        
         
         TrackMessage reply = TrackMessage.newBuilder()
                              .setTrackID(track.get_trackID())
                              .setTrackHeading(track.get_trackHeading())
                              .setTrackSpeed(track.get_trackSpeed())
                              .setTrackLatitude(track.get_trackLatitude())
                              .setTrackLongitude(track.get_trackLongitude())
                              .build();

         responseObserver.onNext(reply);
         responseObserver.onCompleted();
      }
   }
   
  /*  public static void main(String[] args) throws IOException, InterruptedException {
      final GreetServer greetServer = new GreetServer();
      
      greetServer.start();
      greetServer.server.awaitTermination();
   } */
} 