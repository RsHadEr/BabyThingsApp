package com.example.babythings;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.widget.TintableCheckedTextView;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.babythings.data.DatabaseHandler;
import com.example.babythings.model.Item;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import java.util.List;

public class MainActivity extends AppCompatActivity {
    private AlertDialog.Builder builder;
    private AlertDialog dialog;
    private Button saveButton;
    private EditText babyItem;
    private EditText itemQuantity;
    private EditText itemColor;
    private EditText itemSize;
    private DatabaseHandler databaseHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        databaseHandler = new DatabaseHandler(this);
        bypassActivity();

        List<Item> items = databaseHandler.getAllItems();

        for(Item item:items)
        {
            Log.d("main", "onCreate: "+item.getItemName());
        }





        FloatingActionButton fab=findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createPopupDialog();
            }
        });
    }

    private void bypassActivity() {
        if(databaseHandler.getItemCount()>0)
        {
            startActivity(new Intent(MainActivity.this,ListActivity.class));
            finish();

        }
    }

    private void saveButton(View view) {
        Item item = new Item();

        String newItem = babyItem.getText().toString().trim();
        String newColor= itemColor.getText().toString().trim();
        int quantity = Integer.parseInt(itemQuantity.getText().toString().trim());
        int size = Integer.parseInt(itemSize.getText().toString().trim());

        item.setItemName(newItem);
        item.setItemColor(newColor);
        item.setItemQuantity(quantity);
        item.setItemSize(size);

        databaseHandler.addItem(item);

        Snackbar.make(view,"Item Saved",Snackbar.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                dialog.dismiss();

                startActivity(new Intent(MainActivity.this,ListActivity.class));
            }
        },1200);

    }

    private void createPopupDialog() {
        builder = new AlertDialog.Builder(this);
        View view = getLayoutInflater().inflate(R.layout.popup, null);

        babyItem = view.findViewById(R.id.babyItem);
        itemQuantity = view.findViewById(R.id.itemQuantity);
        itemColor = view.findViewById(R.id.itemColor);
        itemSize = view.findViewById(R.id.itemSize);
        saveButton=view.findViewById(R.id.save_button);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!babyItem.getText().toString().isEmpty()&&
                !itemColor.getText().toString().isEmpty()
                        &&!itemQuantity.getText().toString().isEmpty()
                        &&!itemSize.getText().toString().isEmpty())
                    saveButton(view);
                else{
                    Snackbar.make(view,"fill ALL values",Snackbar.LENGTH_SHORT).show();
                }
            }
        });

        builder.setView(view);
        dialog=builder.create();
        dialog.show();

    }


}