package com.goose.androidtest;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.goose.androidtest.database.DBManager;
import com.goose.androidtest.database.Problem;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private DBManager dbManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        loadList();
    }

    @Override
    protected void onRestart(){
        super.onRestart();
        setContentView(R.layout.activity_main);
        loadList();
    }

    public void onResume() {
        super.onResume();
    }

    public void onStop(){
        super.onStop();
        dbManager.close();
    }


    private void loadList() {

        dbManager = new DBManager(this);
        dbManager.open();
        ArrayList<Problem> items = dbManager.fetch();
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        ImageButton addButton = findViewById(R.id.addButton);
        ImageButton refresh = findViewById(R.id.refresh);

        if (items.size() > 0) {
            for (Problem item : items) {
                if (inflater == null) {
                    continue;
                }
                View v = inflater.inflate(R.layout.container, null);

                if(v!=null){
                    TextView titleView = v.findViewById(R.id.title);
                    TextView descView = v.findViewById(R.id.description);
                    TextView yearView = v.findViewById(R.id.year);
                    ImageButton delButton = v.findViewById(R.id.delButton);

                    titleView.setText(item.getTitle());
                    descView.setText(item.getDescription());
                    yearView.setText(item.getExpDate());

                    delButton.setOnClickListener(v1 -> {
                        dbManager.delete(item.getId());
                        onRestart();
                    });

                }

                LinearLayout items_container = findViewById(R.id.list_items);
                if(items_container != null){
                    items_container.addView(v);
                }
            }
        }

        addButton.setOnClickListener(v1 -> {
            final Dialog dialog = new Dialog(MainActivity.this, R.style.DialogTheme);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setContentView(R.layout.add_item_dialog);

            dialog.setCancelable(false);

            EditText newTitle = dialog.findViewById(R.id.EditTextTitle);
            EditText newDesc = dialog.findViewById(R.id.EditTextDescription);
            EditText newYear = dialog.findViewById(R.id.EditTextYear);
            Button btnAdd = dialog.findViewById(R.id.btn_add);
            Button btnCancel = dialog.findViewById(R.id.btn_cancel);

            btnAdd.setOnClickListener(v2 -> {
                String title = newTitle.getText().toString();
                String desc = newDesc.getText().toString();
                String year = newYear.getText().toString();
                if(year.contains(" ")) year.replaceAll(" ", "");
                String[] validYear = year.split("-");
                Problem newItem = new Problem();
                if(validYear.length == 3){
                    newItem.setTitle(title);
                    newItem.setDescription(desc);
                    newItem.setExpDate(year);
                    dbManager.insert(newItem);
                    dialog.dismiss();
                    onRestart();
                }
                else{
                    Toast toast = Toast.makeText(getApplicationContext(), "year is not a date", Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.BOTTOM, 0, 0);
                    toast.show();
                    dialog.dismiss();
                }
            });

            btnCancel.setOnClickListener(v2 -> {
                dialog.dismiss();
            });

            dialog.show();
        });
        refresh.setOnClickListener(v1 -> {
            onRestart();
        });
    }
}