package com.loopwiki.notes.repositories;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.os.AsyncTask;
import android.support.annotation.NonNull;

import com.loopwiki.notes.database.Daos.NoteDao;
import com.loopwiki.notes.database.NoteDatabase;
import com.loopwiki.notes.database.models.Note;

import java.util.List;
//Notes repository
public class NotesRepository {
    //Live Data of List of all notes
    private LiveData<List<Note>> mAllNotes;
    //Define Notes Dao
    NoteDao mNoteDao;

    public NotesRepository(@NonNull Application application) {
        NoteDatabase noteDatabase = NoteDatabase.getDatabase(application);
        //init Notes Dao
        mNoteDao = noteDatabase.noteDao();
        //get all notes
        mAllNotes = mNoteDao.getAllNotes();
    }
    //method to get all notes
    public LiveData<List<Note>> getAllNotes() {
        return mAllNotes;
    }

    //method to add note
    public void addNote(Note note) {
        new AddNote().execute(note);
    }

    //Async task to add note
    public class AddNote extends AsyncTask<Note, Void, Void> {
        @Override
        protected Void doInBackground(Note... notes) {
            mNoteDao.insertNote(notes[0]);
            return null;
        }
    }

    //method to delete note
    public void deleteNote(Note note) {
        new DeleteNote().execute(note);
    }

    //Async task to delete note
    public class DeleteNote extends AsyncTask<Note, Void, Void> {
        @Override
        protected Void doInBackground(Note... notes) {
            mNoteDao.deleteNote(notes[0]);
            return null;
        }
    }

    //method to update note
    public void updateNote(Note note) {
        new UpdateNote().execute(note);
    }

    //Async task to update note
    public class UpdateNote extends AsyncTask<Note, Void, Void> {
        @Override
        protected Void doInBackground(Note... notes) {
            mNoteDao.update(notes[0]);
            return null;
        }
    }
}
