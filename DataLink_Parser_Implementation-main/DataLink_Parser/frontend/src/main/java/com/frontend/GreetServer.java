package com.frontend;

import io.grpc.Server;
import io.grpc.ServerBuilder;
import io.grpc.stub.StreamObserver;
import javafx.application.Application;
import javafx.application.Platform;

import java.io.IOException;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;
import protocomp.GreeterGrpc;
import protocomp.TrackResponse;
import protocomp.TrackMessage;

public class GreetServer {

   private static final Logger logger = Logger.getLogger(GreetServer.class.getName());
   private Server server;
   private App app;
   

   private void start() throws IOException {
      
      int port = 50051;

      server = ServerBuilder.forPort(port).addService(new GreeterImpl(app)).build().start();
       
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

   static class GreeterImpl extends GreeterGrpc.GreeterImplBase {
      private TrackList trackList= new TrackList();
      App app;
      public GreeterImpl(App app2) {
         this.app=app2;
      }
      @Override
      public void greet(TrackMessage req, StreamObserver<TrackResponse> responseObserver) {
         logger.info("Got request from client: ");
         Track newTrack = new Track((int)req.getTrackID(), (int)req.getTrackHeading(),(int)req.getTrackSpeed(),(short)req.getTrackLatitude(),(short) req.getTrackLongitude());
         if(trackList.replaceTrack(newTrack)){

         }
         else{
            trackList.addTrack(newTrack);
         }
         Application.launch(app.getClass());
         app.updateLabels(trackList);
      
         TrackResponse reply = TrackResponse.newBuilder()
         .setMessage("got it")
         .build();
         responseObserver.onNext(reply);
         responseObserver.onCompleted();
         
      }
   }
   
   public static void main(String[] args) throws IOException, InterruptedException {
       final GreetServer greetServer = new GreetServer();

      // Thread 1: Start the gRPC server
      Thread grpcServerThread = new Thread(() -> {
         try {
            greetServer.app= new App();
            greetServer.start();
            greetServer.server.awaitTermination();
         } catch (IOException | InterruptedException e) {
            e.printStackTrace();
         }
      });

     
      // Start both threads
      grpcServerThread.start();
      

      // Wait for both threads to complete
      grpcServerThread.join();
     
   
   }
} 