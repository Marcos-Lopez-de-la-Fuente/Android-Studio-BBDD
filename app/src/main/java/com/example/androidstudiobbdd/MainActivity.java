package com.example.androidstudiobbdd;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {


    // ! Attributes
    private DatabaseHelper databaseHelper;


    private Spinner spinner;

    private TextInputEditText inputCommentTitle;
    private TextInputEditText inputCommentDescription;
    private TextInputEditText outputCommentTitle;
    private TextInputEditText outputCommentDescription;

    private AppCompatButton buttonCreate;
    private AppCompatButton buttonShow;
    private AppCompatButton buttonDelete;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.spinner = findViewById(R.id.spinnerComments);
        this.inputCommentTitle = findViewById(R.id.inputCommentTitle);
        this.inputCommentDescription = findViewById(R.id.inputCommentDescription);
        this.outputCommentTitle = findViewById(R.id.outputCommentTitle);
        this.outputCommentDescription = findViewById(R.id.outputCommentDescription);
        this.buttonCreate = findViewById(R.id.buttonCreate);
        this.buttonShow = findViewById(R.id.buttonShow);
        this.buttonDelete = findViewById(R.id.buttonDelete);


        this.databaseHelper = new DatabaseHelper(this);

        this.refreshComments();



        this.buttonCreate.setOnClickListener(view -> {
            String title = String.valueOf(this.inputCommentTitle.getText());
            String description = String.valueOf(this.inputCommentDescription.getText());

            this.inputCommentTitle.setText("");
            this.inputCommentDescription.setText("");

            if (!title.equals("") && !description.equals("")) {

                Comment comment = new Comment(title, description);
                this.databaseHelper.addComment(comment);

                this.refreshComments();


            }

        });


        this.buttonShow.setOnClickListener(view -> {
            if (this.spinner.getSelectedItem() != null) {
                ArrayList<Comment> comments = this.databaseHelper.getComments();

                int counter = 0;
                boolean finish = false;
                while (!finish) {

                    if (comments.get(counter).getTitle().equals(this.spinner.getSelectedItem())) {

                        this.outputCommentTitle.setText(comments.get(counter).getTitle());
                        this.outputCommentDescription.setText(comments.get(counter).getDescription());

                        finish = true;
                    }

                    counter++;
                }
            }
        });


        this.buttonDelete.setOnClickListener(view -> {
            if (this.spinner.getSelectedItem() != null) {
                this.databaseHelper.removeComment((String) this.spinner.getSelectedItem());
                this.refreshComments();
            }
        });


    }


    private void refreshComments() {


        ArrayList<Comment> comments = this.databaseHelper.getComments();
        ArrayList<String> titles = new ArrayList<String>();
        for (int i = 0; i < comments.size(); i++) {
            titles.add(comments.get(i).getTitle());
        }

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, titles);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(arrayAdapter);


    }
}