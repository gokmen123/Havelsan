package com.frontend;

import io.grpc.Channel;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.StatusRuntimeException;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

import protocomp.GreeterGrpc;
import protocomp.TrackRequest;
import protocomp.TrackMessage;

public class GreetClient {
   private Track tracker;
   private static final Logger logger = Logger.getLogger(GreetClient.class.getName());
   private final GreeterGrpc.GreeterBlockingStub blockingStub;
   
   public GreetClient(Channel channel,Track track) {
      blockingStub = GreeterGrpc.newBlockingStub(channel);
      this.tracker=track;
   }
 
   public void makeGreeting(String greeting) {

      logger.info("Sending greeting to server: " + greeting);

      TrackRequest request = TrackRequest.newBuilder()
      .setMessage("Update Tracks")
      .build();
      
      logger.info("Sending to server: " + request);

      TrackMessage response;

      try {
         response = blockingStub.greet(request);
      } 
      catch (StatusRuntimeException e) {
         logger.log(Level.WARNING, "RPC failed: {0}", e.getStatus());
         return;
      }

      //logger.info("Got following from the server: " + response.getTrackLongitude());
      int trackId=response.getTrackID();
      int trackHead=response.getTrackHeading();
      int trackSpeed=response.getTrackSpeed();
      short trackLatitude=(short)response.getTrackLatitude();
      short trackLongtitude=(short)response.getTrackLongitude();
      tracker= new Track(trackId, trackHead, trackSpeed,trackLatitude,trackLongtitude);
      setTrack(tracker);
      logger.info("Got following from the server: " + tracker.toString());
   }
   public Track geTrack(){
      return tracker;
   }
   public void setTrack(Track track){
      this.tracker=track;
   }
   

/*    public static void main(String[] args) throws Exception {

      String greeting = "Hello";
      String serverAddress = "localhost:50051";

	   ManagedChannel channel = ManagedChannelBuilder.forTarget(serverAddress)
         .usePlaintext()
         .build();

      try {
         GreetClient client = new GreetClient(channel);
         client.makeGreeting(greeting);
         
      } 
      finally {
         channel.shutdownNow().awaitTermination(5, TimeUnit.SECONDS);
      }
   } */
}