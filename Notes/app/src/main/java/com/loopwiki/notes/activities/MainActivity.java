package com.loopwiki.notes.activities;

import android.app.AlertDialog;
import android.app.Dialog;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.EditText;
import android.widget.TextView;
import com.loopwiki.notes.R;
import com.loopwiki.notes.adapters.NotesAdapter;
import com.loopwiki.notes.database.models.Note;
import com.loopwiki.notes.utils.Space;
import com.loopwiki.notes.viewModels.NotesListViewModel;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    NotesListViewModel mNotesListViewModel;
    FloatingActionButton fab;
    List<Note> __notes ;
    int __position ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Initialize floating action button
        fab = (FloatingActionButton) findViewById(R.id.fab);
        //show add notes dialogue

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog();
            }
        });
        // bind recyclerview to object
        RecyclerView mNotesRecyclerView = findViewById(R.id.recyclerViewNotes);
        // set layout manager
        mNotesRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        // create new notes adapter
        final NotesAdapter notesAdapter = new NotesAdapter(this);
        // set adapter to recyclerview
        mNotesRecyclerView.setAdapter(notesAdapter);
        // add decoration to recyclerview
        mNotesRecyclerView.addItemDecoration(new Space(20));
        // get ViewModel of this activity using ViewModelProviders
        mNotesListViewModel = ViewModelProviders.of(this).get(NotesListViewModel.class);
        // observe for notes data changes
        mNotesListViewModel.getAllNotes().observe(this, new Observer<List<Note>>() {
            @Override
            public void onChanged(@Nullable List<Note> notes) {
                //add notes to adapter
                notesAdapter.addNotes(notes);
            }
        });


    }

    public void showDialog() {
        fab.hide();
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.add_note_dialog);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        final EditText editTextTitle = dialog.findViewById(R.id.editTextTitle);
        final EditText editTextDescription = dialog.findViewById(R.id.editTextDescription);
        TextView textViewAdd = dialog.findViewById(R.id.textViewAdd);
        TextView textViewCancel = dialog.findViewById(R.id.textViewCancel);
        textViewAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String Title = editTextTitle.getText().toString();
                String Description = editTextDescription.getText().toString();
                Date createdAt = Calendar.getInstance().getTime();
                //add note
                mNotesListViewModel.addNote(new Note(Title, Description, createdAt));
                fab.show();
                dialog.dismiss();
            }
        });
        textViewCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                fab.show();
            }
        });

        dialog.show();

    }

    /**
     * get NotesListViewModel
     * @return NotesListViewModel
     */
    public NotesListViewModel getmNotesListViewModel() {
        return mNotesListViewModel;
    }

    /**
     * show dialog window to make update in specific note
     * @param note
     */
    public void showDialogToUpdate(final Note note) {
        fab.hide();
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.add_note_dialog);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        TextView textView = dialog.findViewById(R.id.textViewAdd);
        textView.setText("Save");
        final EditText editTextTitle = dialog.findViewById(R.id.editTextTitle);
        final EditText editTextDescription = dialog.findViewById(R.id.editTextDescription);
        editTextTitle.setText(note.getNoteTitle());
        editTextDescription.setText(note.getNoteDescription());
        TextView textViewAdd = dialog.findViewById(R.id.textViewAdd);
        TextView textViewCancel = dialog.findViewById(R.id.textViewCancel);
        textViewAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String Title = editTextTitle.getText().toString();
                String Description = editTextDescription.getText().toString();
                Date createdAt = Calendar.getInstance().getTime();
                note.setNoteTitle(Title);
                note.setNoteDescription(Description);
                note.setCreatedAt(createdAt);
                //update note
                mNotesListViewModel.updateNote(note);
                fab.show();
                dialog.dismiss();
            }
        });
        textViewCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                fab.show();
            }
        });

        dialog.show();

    }

    /**
     * show dialog window befor we make delete to make sure that we want to delete specific note
     * @param notes
     * @param position
     */
    public void showDialogBeforDelete(List<Note> notes ,int position) {
        __notes = notes ;
        __position = position ;
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage("Are you sure,You want to delete this note");
        alertDialogBuilder.setPositiveButton("yes",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        getmNotesListViewModel().deleteNote(__notes.get(__position));
                        __notes.remove(__position);
                    }
                });

        alertDialogBuilder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();

    }

}
