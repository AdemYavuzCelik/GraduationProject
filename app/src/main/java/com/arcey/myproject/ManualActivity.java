package com.arcey.myproject;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Vibrator;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.io.IOException;
import java.util.UUID;

public class ManualActivity extends AppCompatActivity{
    String adres = null;
    Button butonRotaCiz,butonRotaBaslat;
    private ProgressDialog progressDialog;
    BluetoothAdapter bluetoothAdapter = null;
    public static BluetoothSocket bluetoothSocket = null;
    public String positionTilt;
    public String positionPant;
    private TextView panText;
    private TextView tiltText;
    private byte[] mmBuffer; // mmBuffer store for the stream
    private SeekBar pan,tilt;
    private ImageButton butonIleri, butonGeri, butonSagIleri, butonSolIleri, butonSol, butonSag, butonSagGeri, butonSolGeri;
    private boolean btBaglimi = false;
    static final UUID uuid = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
    private ImageView imageTank;
    public  static boolean pathCiziliMi = false;
    public String newString;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manual);
        Vibrator vibe = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        Intent intent = getIntent();
        butonRotaCiz = findViewById(R.id.butonRotaCiz);
        butonRotaBaslat = findViewById(R.id.butonRotaBaslat);
        if(!pathCiziliMi){
            butonRotaBaslat.setEnabled(false);
        }
        adres = intent.getStringExtra(MainActivity.CIHAZ_ADRES);
        new BTBaglan().execute();

        if (savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();
            if(extras == null) {
                pathCiziliMi = false;
            } else {
                newString= extras.getString("ROTA");
            }
        } else {
            newString= (String) savedInstanceState.getSerializable("ROTA");
        }
        butonRotaCiz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ManualActivity.this, MoveActivity.class);
                startActivity(intent);
            }
        });

        panText=findViewById(R.id.textPan);
        tiltText=findViewById(R.id.textTilt);


        imageTank = findViewById(R.id.imageTank);
        imageTank.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    bluetoothSocket.getOutputStream().write("X!".getBytes());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        butonSolIleri = findViewById(R.id.buton_sol_ileri);
        butonIleri = findViewById(R.id.buton_ileri);
        butonSagIleri = findViewById(R.id.buton_sag_ileri);
        butonSol = findViewById(R.id.buton_sol);
        butonSag = findViewById(R.id.buton_sag);
        butonSolGeri = findViewById(R.id.buton_sol_geri);
        butonGeri = findViewById(R.id.buton_geri);
        butonSagGeri = findViewById(R.id.buton_sag_geri);

        pan = findViewById(R.id.pan);
        tilt=findViewById(R.id.tilt);

        tilt.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                i=i+10;
                if(i>170){
                    i=i-10;
                }
                tiltText.setText("Y: "+i+"°");
                positionTilt = tiltText.getText().toString();
                positionTilt = positionTilt.substring(3,positionTilt.length()-1);

                try {
                    bluetoothSocket.getOutputStream().write(("T"+positionTilt+"!").getBytes());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });



        pan.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int j, boolean b) {
                j=j+10;
                if(j>170){
                    j=j-10;
                }
                panText.setText("X: "+j+"°");
                positionPant = panText.getText().toString();
                positionPant = positionPant.substring(3,positionPant.length()-1);

                try {
                    bluetoothSocket.getOutputStream().write(("P"+positionPant+"!").getBytes());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {


            }
        });
        butonRotaBaslat.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                try {

                    bluetoothSocket.getOutputStream().write(newString.getBytes());
                    System.out.println(newString);
                    MoveActivity.path=null;
                    PaintView.moveCords.clear();

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        butonIleri.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (bluetoothSocket != null) {
                    switch (motionEvent.getAction()) {
                        case MotionEvent.ACTION_DOWN:
                            vibe.vibrate(100);
                            try {
                                bluetoothSocket.getOutputStream().write("F!".getBytes());
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            break;
                        case MotionEvent.ACTION_UP:
                            try {
                                bluetoothSocket.getOutputStream().write("S!".getBytes());
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            break;
                    }
                }
                return false;
            }
        });



        butonSolIleri.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (bluetoothSocket != null) {
                    switch (motionEvent.getAction()) {
                        case MotionEvent.ACTION_DOWN:
                            vibe.vibrate(100);
                            try {
                                bluetoothSocket.getOutputStream().write("G!".toString().getBytes());
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            break;
                        case MotionEvent.ACTION_UP:
                            try {
                                bluetoothSocket.getOutputStream().write("S!".toString().getBytes());
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            break;
                    }
                }
                return false;
            }
        });



        butonSagIleri.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {

                if (bluetoothSocket != null) {
                    switch (motionEvent.getAction()) {
                        case MotionEvent.ACTION_DOWN:
                            vibe.vibrate(100);
                            try {
                                bluetoothSocket.getOutputStream().write("I!".toString().getBytes());
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            break;
                        case MotionEvent.ACTION_UP:
                            try {
                                bluetoothSocket.getOutputStream().write("S!".toString().getBytes());
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            break;
                    }
                }
                return false;
            }
        });

        butonSol.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {

                if (bluetoothSocket != null) {
                    switch (motionEvent.getAction()) {
                        case MotionEvent.ACTION_DOWN:
                            vibe.vibrate(100);
                            try {
                                bluetoothSocket.getOutputStream().write("L!".toString().getBytes());
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            break;
                        case MotionEvent.ACTION_UP:
                            try {
                                bluetoothSocket.getOutputStream().write("S!".toString().getBytes());
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            break;
                    }
                }
                return false;
            }
        });



        butonSag.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {

                if (bluetoothSocket != null) {
                    switch (motionEvent.getAction()) {
                        case MotionEvent.ACTION_DOWN:
                            vibe.vibrate(100);
                            try {
                                bluetoothSocket.getOutputStream().write("R!".toString().getBytes());
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            break;
                        case MotionEvent.ACTION_UP:
                            try {
                                bluetoothSocket.getOutputStream().write("S!".toString().getBytes());
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            break;
                    }
                }
                return false;
            }
        });
        butonSolGeri.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {

                if (bluetoothSocket != null) {
                    switch (motionEvent.getAction()) {
                        case MotionEvent.ACTION_DOWN:
                            vibe.vibrate(100);
                            try {
                                bluetoothSocket.getOutputStream().write("H!".toString().getBytes());
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            break;
                        case MotionEvent.ACTION_UP:
                            try {
                                bluetoothSocket.getOutputStream().write("S!".toString().getBytes());
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            break;
                    }
                }
                return false;
            }
        });


        butonGeri.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {

                if (bluetoothSocket != null) {
                    switch (motionEvent.getAction()) {
                        case MotionEvent.ACTION_DOWN:
                            vibe.vibrate(100);
                            try {
                                bluetoothSocket.getOutputStream().write("B!".toString().getBytes());
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            break;
                        case MotionEvent.ACTION_UP:
                            try {
                                bluetoothSocket.getOutputStream().write("S!".toString().getBytes());
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            break;
                    }
                }
                return false;
            }
        });
        butonSagGeri.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {

                if (bluetoothSocket != null) {
                    switch (motionEvent.getAction()) {
                        case MotionEvent.ACTION_DOWN:
                            vibe.vibrate(100);
                            try {
                                bluetoothSocket.getOutputStream().write("J!".toString().getBytes());
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            break;
                        case MotionEvent.ACTION_UP:
                            try {
                                bluetoothSocket.getOutputStream().write("S!".toString().getBytes());
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            break;
                    }
                }
                return false;
            }
        });

    }

    /*private void baglantiKes() {
        if (bluetoothSocket != null) {
            try {
                bluetoothSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        finish();
    }*/

    @Override
    public void onBackPressed() {
        super.onBackPressed();
       //baglantiKes();
    }
    private class BTBaglan extends AsyncTask<Void, Void, Void> {
        private boolean baglantiBasarili = true;

        @Override
        protected void onPreExecute() {
            progressDialog = ProgressDialog.show(ManualActivity.this, "Baglaniliyor...", "Lutfen Bekleyiniz");
        }

        @SuppressLint("MissingPermission")
        @Override
        protected Void doInBackground(Void... voids) {
            try {
                if (bluetoothSocket == null || btBaglimi) {
                    bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
                    BluetoothDevice bluetoothDevice = bluetoothAdapter.getRemoteDevice(adres);
                    bluetoothSocket = bluetoothDevice.createInsecureRfcommSocketToServiceRecord(uuid);
                    BluetoothAdapter.getDefaultAdapter().cancelDiscovery();
                    bluetoothSocket.connect();
                }
            } catch (IOException e) {
                baglantiBasarili = false;
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void unused) {
            super.onPostExecute(unused);
            if (!baglantiBasarili) {
                Toast.makeText(getApplicationContext(), "Baglanti Hatasi Tekrar Deneyin", Toast.LENGTH_SHORT).show();
                finish();
            } else {
               // Toast.makeText(getApplicationContext(), "Baglanti Basarili", Toast.LENGTH_SHORT).show();
                btBaglimi = true;
            }
            progressDialog.dismiss();
        }
    }
}
