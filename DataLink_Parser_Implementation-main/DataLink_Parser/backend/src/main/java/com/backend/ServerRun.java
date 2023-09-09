package com.backend;

import java.io.*;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.bson.Document;
import org.bson.codecs.jsr310.LocalDateCodec;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;

public class ServerRun {
    public static void main(String[] args) throws IOException, InterruptedException {
        String uri="mongodb://127.0.0.1:27017/?directConnection=true&serverSelectionTimeoutMS=2000&appName=mongosh+1.10.6";
        MongoClient mongoClient =  MongoClients.create(uri);
        String databaseName = "tracks";

        // Check if the database exists
        boolean exists = databaseExists(mongoClient, databaseName);
        if(exists){
            MongoDatabase db = mongoClient.getDatabase("tracks");
            db.getCollection("track").drop();
        }
        
        MongoDatabase db = mongoClient.getDatabase("tracks");
        MongoCollection collection= db.getCollection("track");
        
        

        
        Server server = new Server();
        String binaryData;
        DataParser dataParser = new DataParser();
        TrackCredentials credentials = new TrackCredentials();
        List <Track> list_tracks = new ArrayList<Track>();
    
        server.start(5000);
        
        while(true) {

            try {
                binaryData = server.listen(); // Awaiting for simulator

                if (binaryData.equals("0000000000000000000000000000000000000000000000000000000000000000")) {
                    break;
                }
                credentials = dataParser.parse(binaryData);
                Date currentDate = new Date();

                // Define a date format with hour and minute
                SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");

                // Format the current date as a string
                String formattedDate = dateFormat.format(currentDate);

                Document document = new Document("trackId", credentials.get_trackID());
                document.append("trackHeading", credentials.get_trackHeading());
                document.append("trackSpeed", credentials.get_trackSpeed());
                document.append("trackLatitude", credentials.get_trackLatitude());
                document.append("trackLongtitude", credentials.get_trackLongitude());
                document.append("time", formattedDate.toString());
                collection.insertOne(document);
                System.out.println("connection success"); 
                
                
                
                

                Track track = new Track(
                    credentials.get_trackID(),
                    credentials.get_trackHeading(),
                    credentials.get_trackSpeed(),
                    credentials.get_trackLatitude(),
                    credentials.get_trackLongitude());
                    String serverAddress = "localhost:50051";

                        ManagedChannel channel = ManagedChannelBuilder.forTarget(serverAddress)
                            .usePlaintext()
                            .build();

                        try {
                            GreetClient client = new GreetClient(channel);
                            client.makeGreeting(track);
                        } 
                        finally {
                            channel.shutdownNow().awaitTermination(5, TimeUnit.SECONDS);
                        }
                list_tracks.add(track);
            } catch (java.net.SocketTimeoutException e) {
                System.out.println(e);
            }

        }
        
        
        server.stop();
    }
    public static boolean databaseExists(MongoClient client, String databaseName) {
        for (String dbName : client.listDatabaseNames()) {
            if (dbName.equals(databaseName)) {
                return true;
            }
        }
        return false;
    }
}
