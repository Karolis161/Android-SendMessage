package com.example.egzopvz;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    String[] products = {"Duona", "Pienas", "Sviestas", "Makaronai"};
    String item, price, number;
    ArrayList<String> productList;
    ArrayAdapter<String> arrayAdapter;
    ArrayAdapter<String> arrayAdapter1;
    Spinner spin;
    Button btn1, btn2, btn3;
    ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.SEND_SMS}, 1);

        spin = findViewById(R.id.spinner);
        btn1 = findViewById(R.id.button1);
        btn2 = findViewById(R.id.button2);
        btn3 = findViewById(R.id.button3);
        listView = findViewById(R.id.listView);

        spin.setOnItemSelectedListener(this);
        arrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, products);
        spin.setAdapter(arrayAdapter);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        productList = new ArrayList<>(Arrays.asList());
        arrayAdapter1 = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, productList);
        listView.setAdapter(arrayAdapter1);

        addItemToList();
        showProductPrice();
        moveToSecondActivity();
        sendMessage();
    }

    @Override
    public void onItemSelected(AdapterView<?> arg0, View arg1, int position, long id) {
        item = spin.getSelectedItem().toString();

        switch (item) {
            case "Duona":
                price = "2";
                break;
            case "Pienas":
                price = "3";
                break;
            case "Sviestas":
                price = "4";
                break;
            case "Makaronai":
                price = "5";
                break;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> arg0) {

    }

    public void addItemToList() {
        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                productList.add(item);
                arrayAdapter1.notifyDataSetChanged();
            }
        });
    }

    public void showProductPrice() {
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Toast.makeText(getApplicationContext(), price + " eurai", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void moveToSecondActivity() {
        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, SecondActivity.class);
                startActivityForResult(intent, 2);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 2) {
            number = data.getStringExtra("number");
        }
    }

    public void sendMessage() {
        btn3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    SmsManager smsManager = SmsManager.getDefault();
                    smsManager.sendTextMessage(number, null, String.valueOf(productList), null, null);
                    Toast.makeText(getApplicationContext(), "Message Send successfully", Toast.LENGTH_SHORT).show();

                } catch (Exception e) {
                    Toast.makeText(MainActivity.this, "SMS Failed to Send, Please try again", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
