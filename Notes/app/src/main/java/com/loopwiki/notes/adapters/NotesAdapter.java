package com.loopwiki.notes.adapters;

import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.loopwiki.notes.activities.MainActivity;
import com.loopwiki.notes.utils.DateConverter;
import com.loopwiki.notes.R;
import com.loopwiki.notes.database.models.Note;
import com.loopwiki.notes.viewModels.NotesListViewModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class NotesAdapter extends RecyclerView.Adapter {
    //Create list of notes
    List<Note> notes = new ArrayList<>();
    private MainActivity mainActivity ;
    NotesListViewModel mNotesListViewModel;

    public  NotesAdapter (MainActivity mainActivity){
        this.mainActivity = mainActivity;
    }
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //Get layout inflater
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        //Inflate layout
        View row = inflater.inflate(R.layout.custom_row_note, parent, false);
        //return notes holder and pass row inside
        return new NoteHolder(row);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        //Get current note
        Note currentNote = notes.get(position);
        //cast notes holder
        NoteHolder noteHolder = (NoteHolder) holder;
        //set title description and created at
        noteHolder.mNoteTitle.setText(currentNote.getNoteTitle());
        noteHolder.mNoteDescription.setText(currentNote.getNoteDescription());
        noteHolder.createdAt.setText(DateConverter.getDayMonth(currentNote.getCreatedAt()));
        //create random color and set it
        Random rnd = new Random();
        int color = Color.argb(255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256));
        noteHolder.backStrip.setBackgroundColor(color);

        ((NoteHolder) holder).setItemclickListner(new ItemClickListner() {
            @Override
            public void onClick(View v, int position, boolean isLongClick) {
                if(isLongClick) {
                    mainActivity.showDialogBeforDelete(notes ,position );
                }
                else {
                    mainActivity.showDialogToUpdate(notes.get(position));
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return notes.size();
    }


    public class NoteHolder extends RecyclerView.ViewHolder implements View.OnClickListener ,View.OnLongClickListener  {
        TextView mNoteTitle, mNoteDescription, createdAt;
        FrameLayout backStrip;
        private ItemClickListner  itemClickListner ;
        public NoteHolder(View itemView) {
            super(itemView);
            mNoteTitle = itemView.findViewById(R.id.noteTitle);
            mNoteDescription = itemView.findViewById(R.id.noteDescription);
            createdAt = itemView.findViewById(R.id.createdAt);
            backStrip = itemView.findViewById(R.id.backStrip);
            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);
        }

        public void setItemclickListner(ItemClickListner itemClickListner){
            this.itemClickListner = itemClickListner ;
        }

        @Override
        public void onClick(View v) {
            itemClickListner.onClick(v, getAdapterPosition() ,false);

        }

        @Override
        public boolean onLongClick(View v) {
            itemClickListner.onClick(v, getAdapterPosition() ,true);

            return true;
        }
    }

    public void addNotes(List<Note> notes) {
        this.notes = notes;
        notifyDataSetChanged();
    }



}
