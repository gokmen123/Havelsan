package com.frontend;

import java.util.ArrayList;
import java.util.List;

public class TrackList {

    static List <Track>  list_tracks;

    public TrackList() {
        list_tracks = new ArrayList<Track>();
    }

    public static void addTrack(Track track) {
        if (!hasTrack(track)) {
            list_tracks.add(track);
        }
    }

    public static boolean hasTrack(Track new_track) {
        for(Track track_ele : list_tracks) {
            if(new_track.get_trackID() == track_ele.get_trackID()) return true;
        }
        return false;
    }

    public  static int getSize() {
        return  list_tracks.size();
    }

    public static List<Track> getList() {
        return list_tracks;
    }
    public static boolean replaceTrack(Track track){
        if(list_tracks.size()!=0){
                for(int j=0;j<list_tracks.size();j++){
                if(list_tracks.get(j).get_trackID()==track.get_trackID()){
                    list_tracks.set(j, track);
                    return true;
                }
            }
            
        }
        
        return false;
    }
    @Override
    public String toString() {
        // TODO Auto-generated method stub
        return super.toString();
    }
    public static boolean isEmpty(){
        if(list_tracks.size()==0){
            return true;
        }
        return false;
    }
}
    

