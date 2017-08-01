package com.demoblue.demobluetooth;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.AbstractQueue;

public class ServidorActivity extends Activity {

    private ImageView btnPagar;
    private ImageView pizza;
    private ImageView hum;
    private ImageView batata;
    private ImageView hotDog;
    private ImageView Pedidos;
    private ImageView x;
    private ImageView cake;
    private ImageView mik;
    private TextView texo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_servidor);

        btnPagar = (ImageView) findViewById(R.id.btnPagar);
        pizza = (ImageView) findViewById(R.id.pizza);
        hum = (ImageView) findViewById(R.id.ham);
        batata = (ImageView) findViewById(R.id.batata);
        hotDog = (ImageView) findViewById(R.id.hot);
        Pedidos = (ImageView) findViewById(R.id.pedidos);
        x = (ImageView) findViewById(R.id.x);
        cake = (ImageView) findViewById(R.id.cake);
        mik = (ImageView) findViewById(R.id.mik);
        texo = (TextView) findViewById(R.id.textPedidos);
        texo.setText("Pedidos");
        mik.setImageResource(R.drawable.milkshake);
        cake.setImageResource(R.drawable.cake);
        x.setImageResource(R.drawable.x);
        Pedidos.setImageResource(R.drawable.pedidos);
        hotDog.setImageResource(R.drawable.hotdog);
        batata.setImageResource(R.drawable.frenchfries);
        hum.setImageResource(R.drawable.hamburger);
        pizza.setImageResource(R.drawable.pizza);
        btnPagar.setImageResource(R.drawable.ok);

        btnPagar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ServidorActivity.this, ClienteActivity.class));
            }
        });

        x.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ServidorActivity.this, MainActivity.class));
            }
        });

    }
}
