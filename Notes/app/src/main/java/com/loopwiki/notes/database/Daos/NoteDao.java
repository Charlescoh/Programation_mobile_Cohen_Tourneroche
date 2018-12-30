package com.loopwiki.notes.database.Daos;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.TypeConverters;
import android.arch.persistence.room.Update;

import com.loopwiki.notes.utils.DateConverter;
import com.loopwiki.notes.database.models.Note;

import java.util.List;

import static android.arch.persistence.room.OnConflictStrategy.REPLACE;

//note dao(data access object)
@Dao
@TypeConverters(DateConverter.class)
public interface NoteDao {
    // Dao method to get all notes
    @Query("SELECT * FROM Note")
    LiveData<List<Note>> getAllNotes();

    // Dao method to insert note
    @Insert(onConflict = REPLACE)
    void insertNote(Note note);

    // Dao method to delete note
    @Delete
    void deleteNote(Note note);

    // Dao method to Update note
    @Update
    void update(Note note);
}

