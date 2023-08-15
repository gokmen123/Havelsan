package com.frontend;

import java.util.ArrayList;
import java.util.List;

public class TrackList {

    List <Track> list_tracks;

    public TrackList() {
        list_tracks = new ArrayList<Track>();
    }

    public void addTrack(Track track) {
        if (!hasTrack(track)) {
            list_tracks.add(track);
        }
    }

    public boolean hasTrack(Track new_track) {
        for(Track track_ele : list_tracks) {
            if(new_track.get_trackID() == track_ele.get_trackID()) return true;
        }
        return false;
    }

    public int getSize() {
        return list_tracks.size();
    }

    public List<Track> getList() {
        return list_tracks;
    }
    public boolean replaceTrack(Track track){
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
    public boolean isEmpty(){
        if(list_tracks.size()==0){
            return true;
        }
        return false;
    }
}
    

