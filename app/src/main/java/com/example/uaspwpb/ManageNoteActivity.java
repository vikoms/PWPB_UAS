package com.example.uaspwpb;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.Date;

public class ManageNoteActivity extends AppCompatActivity {

    EditText edtTitle,edtDesc;
    Button btnSimpan;
    DatabaseReference databaseNotes;
    String idNote;
    TextView tvToolbar;
    ImageView imgToolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_managa_note);

        edtTitle = (EditText) findViewById(R.id.edtJudul);
        edtDesc = (EditText) findViewById(R.id.edtDesc);
        btnSimpan = (Button) findViewById(R.id.btnSimpan);
        idNote = getIntent().getStringExtra("id");
        databaseNotes = FirebaseDatabase.getInstance().getReference("Notes");
        tvToolbar = findViewById(R.id.titleToolbarManage);
        imgToolbar = findViewById(R.id.imgBack);

        imgToolbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ManageNoteActivity.this, HomeActivity.class));
                finish();
            }
        });
        if(!TextUtils.isEmpty(idNote)) {
            String title = getIntent().getStringExtra("title");
            String date = getIntent().getStringExtra("date");
            String desc = getIntent().getStringExtra("desc");
            tvToolbar.setText("Edit Data");
            edtTitle.setText(title);
            edtDesc.setText(desc);
        } else {
            tvToolbar.setText("Tambah Data");

        }


        btnSimpan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(TextUtils.isEmpty(idNote)) {
                    addData();
                }else if(!TextUtils.isEmpty(idNote)) {
                    String title = getIntent().getStringExtra("title");
                    String date = getIntent().getStringExtra("date");
                    String desc = getIntent().getStringExtra("desc");
                    updateData(idNote,title,desc,date);
                }
            }
        });

    }


    public void updateData(String idnote, String title, String desc, String date) {
        String titleUpdate = edtTitle.getText().toString();
        String descUpdate = edtDesc.getText().toString();

//
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Notes").child(idnote);
        Notes note= new Notes(idnote,titleUpdate,descUpdate,date);

        databaseReference.setValue(note);

        Toast.makeText(this, "Artist Update Successfully", Toast.LENGTH_SHORT).show();
        startActivity(new Intent(ManageNoteActivity.this, HomeActivity.class));
        finish();


    }

    public void addData() {
        String title = edtTitle.getText().toString();
        String desc = edtDesc.getText().toString();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd ' ' HH:mm:ss");
        String date = sdf.format(new Date());

        if(!TextUtils.isEmpty(title) && !TextUtils.isEmpty(desc) ) {
            String id =databaseNotes.push().getKey();

            Notes notes =new Notes(id,title,desc,date);

            databaseNotes.child(id).setValue(notes);
            Toast.makeText(this, "Note added", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(ManageNoteActivity.this, HomeActivity.class));
            finish();

        }else {
            Toast.makeText(this, "please fill in all fields", Toast.LENGTH_SHORT).show();
        }

    }
}
