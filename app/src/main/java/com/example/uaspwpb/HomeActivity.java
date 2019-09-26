package com.example.uaspwpb;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class HomeActivity extends AppCompatActivity implements NoteAdapter.OnUserClickListener {
    FloatingActionButton FabBtnAdd;
    RecyclerView recyclerView;
    DatabaseReference databaseNote;
    List<Notes> noteList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);


        FabBtnAdd = (FloatingActionButton) findViewById(R.id.btnAddNote);
        FabBtnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(HomeActivity.this, ManageNoteActivity.class));
            }
        });
        recyclerView = (RecyclerView) findViewById(R.id.container);

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        noteList = new ArrayList<>();
        databaseNote = FirebaseDatabase.getInstance().getReference("Notes");

    }


    @Override
    protected void onStart() {
        super.onStart();

        databaseNote.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                noteList.clear();
                for (DataSnapshot noteSnapshot : dataSnapshot.getChildren()) {
                    Notes notes = noteSnapshot.getValue(Notes.class);
                    noteList.add(notes);
                }

                NoteAdapter adapter = new NoteAdapter(noteList,HomeActivity.this);
                recyclerView.setAdapter(adapter);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void UserClickListener(final Notes notes, String act) {
        if(act.equals("Delete")) {
            AlertDialog.Builder builder = new AlertDialog.Builder(HomeActivity.this);
            builder.setMessage("Are you sure to deleted it??")
                    .setCancelable(false)
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                          deleteData(notes.getId());
                        }
                    })
                    .setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.cancel();
                        }
                    });

            AlertDialog alertDialog = builder.create();
            alertDialog.show();
        }else if(act.equals("Edit")) {
            String id = notes.getId();
            String title= notes.getTitle();
            String desc= notes.getDesc();
            String date = notes.getDate();

            Intent intent = new Intent(HomeActivity.this, ManageNoteActivity.class);

            intent.putExtra("id",id);
            intent.putExtra("title",title);
            intent.putExtra("desc",desc);
            intent.putExtra("date",date);

            startActivity(intent);
        }
    }

    public void deleteData(String id) {
        DatabaseReference notes = FirebaseDatabase.getInstance().getReference("Notes").child(id);

        notes.removeValue();
        Toast.makeText(this, "Notes Removed", Toast.LENGTH_SHORT).show();
    }
}
