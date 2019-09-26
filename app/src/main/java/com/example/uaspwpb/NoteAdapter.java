package com.example.uaspwpb;

import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class NoteAdapter extends RecyclerView.Adapter<NoteAdapter.MyViewHolder> {


    List<Notes> noteList;
    OnUserClickListener listener;
    Context mContext;

    public interface  OnUserClickListener {
        void UserClickListener(Notes notes, String act);
    }

    public NoteAdapter(List<Notes> noteList, OnUserClickListener listener) {
        this.noteList = noteList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_notes,parent,false);
        MyViewHolder vHolder = new MyViewHolder(view);
        mContext = parent.getContext();

        return vHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        final Notes currentNote =noteList.get(position);

        holder.txtNoteTitle.setText(currentNote.getTitle());
        holder.txtNoteDesc.setText(currentNote.getDesc());
        holder.txtDate.setText(currentNote.getDate());

        holder.cvNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                View dialogView = LayoutInflater.from(mContext).inflate(R.layout.alert_dialog,null);
                builder.setView(dialogView);

                TextView edit = dialogView.findViewById(R.id.editData);
                TextView delete = dialogView.findViewById(R.id.deleteData);


                final AlertDialog alertDialog = builder.create();
                alertDialog.show();


                edit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        listener.UserClickListener(currentNote, "Edit");
                        alertDialog.dismiss();
                    }
                });

                delete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        listener.UserClickListener(currentNote,"Delete");
                        alertDialog.dismiss();
                    }
                });


            }
        });
    }

    @Override
    public int getItemCount() {
        return noteList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        CardView cvNote;
        TextView txtDate,txtNoteTitle,txtNoteDesc;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            cvNote = itemView.findViewById(R.id.carViewNote);
            txtDate = itemView.findViewById(R.id.txtDate);
            txtNoteTitle= itemView.findViewById(R.id.txtNoteTitle);
            txtNoteDesc = itemView.findViewById(R.id.txtDesc);
        }
    }

}
