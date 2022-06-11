package com.arcey.myproject;

import android.annotation.SuppressLint;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Set;


public class MainActivity extends AppCompatActivity {
    BluetoothAdapter bluetoothAdapter;
    Button butonBTAcKapat, butonCihazListele;
    ListView cihazListesi;
    private Set<BluetoothDevice> eslesmisCihazlar;
    public static String CIHAZ_ADRES = "Cihaz Adresi";
    ArrayAdapter<String> adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        butonCihazListele = findViewById(R.id.butonCihazListele);
        cihazListesi = findViewById(R.id.cihazListesi);
        butonBTAcKapat = findViewById(R.id.butonBTAcKapat);
        butonBTAcKapat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                butonBTAcKapat();
            }
        });

        butonCihazListele.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cihazlariListele();
            }
        });

    }


    @SuppressLint("MissingPermission")
    private void butonBTAcKapat() {
        if (bluetoothAdapter == null)
            Toast.makeText(getApplicationContext(), "Bluetooth Cihazi Yok", Toast.LENGTH_SHORT).show();
        if (!bluetoothAdapter.isEnabled()) {
            Intent enableBTIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivity(enableBTIntent);
        }
        if (bluetoothAdapter.isEnabled()) {
            bluetoothAdapter.disable();
        }
    }

    @SuppressLint("MissingPermission")
    private void cihazlariListele() {

        eslesmisCihazlar = bluetoothAdapter.getBondedDevices();
        ArrayList list = new ArrayList();
        if (eslesmisCihazlar.size() > 0) {
            for (BluetoothDevice bluetoothDevice : eslesmisCihazlar) {
                list.add(bluetoothDevice.getName() + "\n" + bluetoothDevice.getAddress());
            }
        } else {
            Toast.makeText(getApplicationContext(), "Eslesmis Cihaz Yok", Toast.LENGTH_SHORT).show();
        }

        final  ArrayAdapter arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, list);
        if(!bluetoothAdapter.isEnabled()){
            arrayAdapter.clear();
        }
        cihazListesi.setAdapter(arrayAdapter);
        cihazListesi.setOnItemClickListener(secilenCihaz);
    }

    public AdapterView.OnItemClickListener secilenCihaz = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
            String bilgi = ((TextView) view).getText().toString();
            String cihazAdres = bilgi.substring(bilgi.length() - 17);
            Intent intent = new Intent(MainActivity.this, ManualActivity.class);
            intent.putExtra(CIHAZ_ADRES, cihazAdres);
            startActivity(intent);
        }
    };


}