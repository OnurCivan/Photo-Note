package com.example.photonote;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class Liste extends AppCompatActivity {

    ImageView imageView;
    EditText editText2;
    static SQLiteDatabase database;
    Bitmap saveImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_liste);

        imageView = (ImageView) findViewById(R.id.imageView);

        editText2 = (EditText) findViewById(R.id.editText2);
        Button button2 = (Button) findViewById(R.id.button2);
        Button remove = (Button) findViewById(R.id.remove);

        Intent intent = getIntent();

        String info = intent.getStringExtra("info");

        if (info.equalsIgnoreCase("new")){

            Bitmap background = BitmapFactory.decodeResource(getApplicationContext().getResources(), R.drawable.background2);
            imageView.setImageBitmap(background);
            button2.setVisibility(View.VISIBLE);
            editText2.setText(" ");
            remove.setVisibility(View.INVISIBLE);

        }else {

            String name = intent.getStringExtra("name");
            editText2.setText(name);
            int position = intent.getIntExtra("position",0);
            imageView.setImageBitmap(Menu.objectImage.get(position));
            button2.setVisibility(View.INVISIBLE);
            remove.setVisibility(View.VISIBLE);
        }
    }


    public void save(View view){

        String objectName = editText2.getText().toString();

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        saveImage.compress(Bitmap.CompressFormat.PNG,50,outputStream);
        byte[] byteArray = outputStream.toByteArray();


        try {

            database = this.openOrCreateDatabase("Objects", MODE_PRIVATE,null);
            database.execSQL("CREATE TABLE IF NOT EXISTS objects (name VARCHAR, image BLOB)");

            String sqlString = "INSERT INTO objects (name,image) VALUES (?,?)";
            SQLiteStatement statement = database.compileStatement(sqlString);
            statement.bindString(1,objectName);
            statement.bindBlob(2,byteArray);
            statement.execute();

        }catch (Exception e){

            e.printStackTrace();
        }

        Intent intent = new Intent(getApplicationContext(), Menu.class);
        startActivity(intent);

    }

    public void remove(View view){

        try {

            database = this.openOrCreateDatabase("Objects",MODE_PRIVATE,null);
            database.execSQL("CREATE TABLE IF NOT EXISTS objects (name VARCHAR, image BLOB)");

            String sqlString = ("DELETE FROM objects WHERE name = ?");

            SQLiteStatement statement = database.compileStatement(sqlString);
            statement.bindString(1, editText2.getText().toString());
            statement.execute();

        }catch (Exception e){

            e.printStackTrace();
        }

        Intent intent = new Intent(getApplicationContext(), Menu.class);
        startActivity(intent);

    }


    public void select(View view) {

        if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){

            requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},2);


        }else {

            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(intent,1);

        }


    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        if (requestCode == 2) {

            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent,1);
            }
        }


        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        if (requestCode == 1 && resultCode == RESULT_OK && data != null){

            Uri selectedImage = data.getData();

            saveImage = null;
            try {
                saveImage = MediaStore.Images.Media.getBitmap(this.getContentResolver(),selectedImage);
            } catch (IOException e) {
                e.printStackTrace();
            }

            imageView.setImageBitmap(saveImage);
        }


        super.onActivityResult(requestCode, resultCode, data);
    }
}
