package com.demoblue.demobluetooth;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.AbstractQueue;
import java.util.Set;

public class MainActivity extends Activity {

    private ImageView btnSer;
    private ImageView btnCli;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnCli = (ImageView) findViewById(R.id.btnCliente);
        btnSer = (ImageView) findViewById(R.id.btnServidor);
        btnCli.setImageResource(R.drawable.cle);
        btnSer.setImageResource(R.drawable.pos);

        btnCli.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, ClienteActivity.class));
            }
        });

        btnSer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, ServidorActivity.class));
            }
        });

    }
}
