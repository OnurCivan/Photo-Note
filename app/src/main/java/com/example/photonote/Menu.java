package com.example.photonote;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

public class Menu extends AppCompatActivity {

    static ArrayList<Bitmap> objectImage;


    @Override
    public boolean onCreateOptionsMenu(android.view.Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.add_object, menu);


        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == R.id.add_note){

            Intent intent = new Intent(getApplicationContext(),Liste.class );
            intent.putExtra("info","new");

            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        ListView listView = (ListView) findViewById(R.id.listView);

        final ArrayList<String> objectName = new ArrayList<String>();
        objectImage = new ArrayList<Bitmap>();

        ArrayAdapter arrayAdapter = new ArrayAdapter(this,android.R.layout.simple_list_item_1, objectName);
        listView.setAdapter(arrayAdapter);

        try {

            Liste.database = this.openOrCreateDatabase("Objects", MODE_PRIVATE,null);
            Liste.database.execSQL("CREATE TABLE IF NOT EXISTS objects (name VARCHAR, image BLOB)");

            Cursor cursor = Liste.database.rawQuery("SELECT * FROM objects" , null);

            int nameIx = cursor.getColumnIndex("name");
            int imageIx = cursor.getColumnIndex("image");

            cursor.moveToFirst();

            while (cursor != null){

                objectName.add(cursor.getString(nameIx));

                byte[] byteArray = cursor.getBlob(imageIx);
                Bitmap image = BitmapFactory.decodeByteArray(byteArray,0,byteArray.length);
                objectImage.add(image);

                cursor.moveToNext();

                arrayAdapter.notifyDataSetChanged();
            }

        }catch (Exception e){

            e.printStackTrace();

        }

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Intent intent = new Intent(getApplicationContext(), Liste.class);
                intent.putExtra("info", "old");
                intent.putExtra("name", objectName.get(position));
                intent.putExtra("position", position);

                startActivity(intent);

            }
        });


    }


}
