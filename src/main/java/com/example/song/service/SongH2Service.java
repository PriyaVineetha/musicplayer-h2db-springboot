/*
 * 
 * You can use the following import statements
 * import org.springframework.beans.factory.annotation.Autowired;
 * import org.springframework.http.HttpStatus;
 * import org.springframework.jdbc.core.JdbcTemplate;
 * import org.springframework.stereotype.Service;
 * import org.springframework.web.server.ResponseStatusException;
 * import java.util.ArrayList;
 * 
 */

// Write your code here

package com.example.song.service;

import com.example.song.repository.SongRepository;
import com.example.song.model.Song;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;

import com.example.song.model.SongRowMapper;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.*;

@Service
public class SongH2Service implements SongRepository {

    @Autowired
    private JdbcTemplate db;

    @Override 
    public ArrayList<Song> getSongs() {
        List<Song> songList = db.query("SELECT * FROM PLAYLIST",new SongRowMapper());
        ArrayList<Song> songs = new ArrayList<>(songList);
        return songs;
    }

    @Override 
    public Song getSongById(int songId) {
        try {
            Song song = db.queryForObject("SELECT * FROM PLAYLIST WHERE songId = ?",new SongRowMapper(),songId);
            return song;
        } catch(Exception e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }

    @Override 
    public Song addSong(Song song) {
        
        db.update("INSERT INTO PLAYLIST(songName,lyricist,singer,musicDirector) VALUES(?,?,?,?)",song.getSongName(),song.getLyricist(),
        song.getSinger(),song.getMusicDirector());
        
        return db.queryForObject("SELECT * FROM SONG WHERE songName =? and lyricist = ? and singer = ? and musicDirector = ?",
        new SongRowMapper(),song.getSongName(),song.getLyricist(),song.getSinger(),song.getMusicDirector());
        
    }

    @Override 
    public Song updateSong(int songId,Song song) {
        if(song.getSongName() != null) {
            db.update("UPDATE PLAYLIST SET songName = ? WHERE songId = ?",song.getSongName(),songId);
        }
        if(song.getLyricist() != null) {
            db.update("UPDATE PLAYLIST SET lyricist = ? WHERE songId = ?",song.getLyricist(),songId);
        }
        if(song.getSinger() != null) {
            db.update("UPDATE PLAYLIST SET singer = ? WHERE songId = ?",song.getSinger(),songId);
        }
        if(song.getMusicDirector() != null) {
            db.update("UPDATE PLAYLIST SET musicDirector = ? WHERE songId = ?",song.getMusicDirector(),songId);
        }
        return getSongById(songId);
    }

    @Override 
    public void deleteSong(int songId) {
        db.update("DELETE FROM PLAYLIST WHERE songId = ?",songId);
    }

}