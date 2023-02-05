# Android-Studio-BBDD

# AndroidStudio-Lists_Cards_Colors

<details>

**<summary>Application Images</summary>**

<img src="resForReadme/mobile.gif">

</details>

## **Code**

<ul>

### <li>**Java files**

<ul>

<li>

<details>

**<summary>`Comment.java`</summary>**

```java
package com.example.androidstudiobbdd;

public class Comment {

    // ! Attributes
    private String title;
    private String description;

    // ! Constructors
    public Comment() {
    }

    public Comment(String title, String description) {
        this.setTitle(title);
        this.setDescription(description);
    }


    // ! Getters and Setters
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
```

</details>

</li>

<li>

<details>

**<summary>`DatabaseHelper.java`</summary>**

```java
package com.example.androidstudiobbdd;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

public class DatabaseHelper extends SQLiteOpenHelper {
    public static String DATABASE_NAME = "androidadvancesqlite";
    public static String COMMENT_TABLE = "comments";

    private ArrayList<Comment> cartList = new ArrayList<Comment>();
    Context c;

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, 33);
        c = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE if not exists comments(" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, "
                + "title TEXT,"
                + "description TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int
            newVersion) {
        db.execSQL("DROP TABLE IF EXISTS" + COMMENT_TABLE);
        onCreate(db);
    }

    public void addComment(Comment comment) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("title", comment.getTitle());
        contentValues.put("description", comment.getDescription());
        db.insert(COMMENT_TABLE, null, contentValues);
        db.close();
    }


    public void removeComment(String title) {
        try {
            String[] args = {title};

            SQLiteDatabase db = this.getWritableDatabase();
            db.execSQL("delete from " + COMMENT_TABLE + " where title=?", args);
            db.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public ArrayList<Comment> getComments() {
        cartList.clear();
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("select * from " + COMMENT_TABLE, null);

        if (cursor.getCount() != 0) {
            if (cursor.moveToFirst()) {
                do {
                    Comment comment = new Comment();
                    comment.setTitle(cursor.getString(cursor.getColumnIndexOrThrow("title")));
                    comment.setDescription(cursor.getString(cursor.getColumnIndexOrThrow("description")));
                    cartList.add(comment);
                } while (cursor.moveToNext());
            }
        }

        cursor.close();
        db.close();
        return cartList;
    }
}
```

</details>

</li>

<li>

<details>

**<summary>`MainActivity.java`</summary>**

```java
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
```

</details>

</li>

</ul>

</li>

### <li>**XML files**

<ul>

#### <li>**`layout`**

<ul>

<li>

<details>

**<summary>`activity_main.xml`</summary>**

```xml
<?xml version="1.0" encoding="utf-8"?>
<LinearLayout xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    xmlns:tools="http://schemas.android.com/tools"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    android:orientation="vertical"
    android:padding="20dp"
    tools:context=".MainActivity">

    <com.google.android.material.textfield.TextInputLayout
        style="@style/Widget.Design.TextInputLayout"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:hint="New comment">


        <com.google.android.material.textfield.TextInputEditText
            android:id="@+id/inputCommentTitle"
            android:layout_width="match_parent"
            android:layout_height="wrap_content"
            android:inputType="text" />

    </com.google.android.material.textfield.TextInputLayout>


    <com.google.android.material.textfield.TextInputLayout
        style="@style/Widget.Design.TextInputLayout"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:hint="Text new comment">


        <com.google.android.material.textfield.TextInputEditText
            android:id="@+id/inputCommentDescription"
            android:layout_width="match_parent"
            android:layout_height="150dp"
            android:inputType="text" />

    </com.google.android.material.textfield.TextInputLayout>


    <androidx.appcompat.widget.AppCompatButton
        android:id="@+id/buttonCreate"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:text="Create" />


    <Spinner
        android:id="@+id/spinnerComments"
        android:layout_width="match_parent"
        android:layout_height="wrap_content" />


    <androidx.appcompat.widget.AppCompatButton
        android:id="@+id/buttonShow"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:text="Show" />


    <com.google.android.material.textfield.TextInputLayout
        style="@style/Widget.Design.TextInputLayout"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:hint="Show comment">


        <com.google.android.material.textfield.TextInputEditText
            android:id="@+id/outputCommentTitle"
            android:layout_width="match_parent"
            android:layout_height="wrap_content"
            android:clickable="false"
            android:cursorVisible="false"
            android:focusable="false"
            android:focusableInTouchMode="false"
            android:inputType="text" />

    </com.google.android.material.textfield.TextInputLayout>


    <com.google.android.material.textfield.TextInputLayout
        style="@style/Widget.Design.TextInputLayout"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:hint="Show text comment">


        <com.google.android.material.textfield.TextInputEditText
            android:id="@+id/outputCommentDescription"
            android:layout_width="match_parent"
            android:layout_height="150dp"
            android:clickable="false"
            android:cursorVisible="false"
            android:focusable="false"
            android:focusableInTouchMode="false"
            android:inputType="text" />

    </com.google.android.material.textfield.TextInputLayout>


    <androidx.appcompat.widget.AppCompatButton
        android:id="@+id/buttonDelete"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:text="Delete" />


</LinearLayout>
```

</details>

</li>

</ul>

</ul>

</li>

</ul>

</ul>
