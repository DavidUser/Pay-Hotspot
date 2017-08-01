package com.demoblue.demobluetooth;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.StrictMode;
import android.support.annotation.RequiresApi;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.AbstractQueue;
import java.util.Scanner;
import java.util.Set;


public class ClienteActivity extends Activity {

    public static int ENABLE_BLUETOOTH = 1;
    public static int SELECT_PAIRED_DEVICE = 2;
    public static int SELECT_DISCOVERED_DEVICE = 3;
    static TextView statusMessage;
    ConnectionThread connect;
    static TextView textSpace;
    public static final int PORT = 1337;
    public static String mac;
    private String serverHost = "192.168.0.9";
    private int serverPort = 1337;
    private Button enviarMac;
    private Button pegarMac;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cliente);

        statusMessage = (TextView) findViewById(R.id.statusMessage);
        textSpace = (TextView) findViewById(R.id.textSpace);
        enviarMac = (Button) findViewById(R.id.btnEnviarMac);
        pegarMac = (Button) findViewById(R.id.getMac);

        BluetoothAdapter btAdapter = BluetoothAdapter.getDefaultAdapter();
        if (btAdapter == null) {
            statusMessage.setText("Que pena! Hardware Bluetooth não está funcionando");
        } else {
            statusMessage.setText("Ótimo! Hardware Bluetooth está funcionando");
            if(!btAdapter.isEnabled()) {
                Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                startActivityForResult(enableBtIntent, ENABLE_BLUETOOTH);
                statusMessage.setText("Solicitando ativação do Bluetooth...");
            } else {
                statusMessage.setText("Bluetooth já ativado");
            }
        }

        pegarMac.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getMac();
                textSpace.setText(mac);
            }
        });

        enviarMac.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onClick(View view) {
                try {
                    textSpace.setText("Proxing...");
                    final String textFromBluetooth = "dados da transacao dados da transacao\r\n dados da transacao dados da transacao\n";
                    InputStream inputStream = new ByteArrayInputStream(textFromBluetooth.getBytes(StandardCharsets.UTF_8));
                    InputStreamReader clientStreamReader = new InputStreamReader(inputStream);

                    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                    OutputStreamWriter outputStreamWriter = new OutputStreamWriter(outputStream);

                    int SDK_INT = android.os.Build.VERSION.SDK_INT;
                    if (SDK_INT > 8) {
                        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                                .permitAll().build();
                        StrictMode.setThreadPolicy(policy);
                        //your codes here


                        Socket proxySocket = new Socket(serverHost, serverPort);
                        OutputStreamWriter proxyStreamWriter = new OutputStreamWriter(proxySocket.getOutputStream());
                        InputStreamReader proxyStreamReader = new InputStreamReader(proxySocket.getInputStream());

                        int character = clientStreamReader.read();
                        while (character != -1) {
                            proxyStreamWriter.write(character);
                            character = clientStreamReader.read();
                        }

                        proxyStreamWriter.flush();
                        textSpace.setText("Data sended! ");
                        Thread.sleep(200);

                        character = proxyStreamReader.read();
                        while (character != -1) {
                            outputStreamWriter.write(character);
                            character = proxyStreamReader.read();
                        }

                        outputStreamWriter.flush();
                    }

                    textSpace.setText("Data received: " + new String(outputStream.toByteArray(), StandardCharsets.UTF_8));

                } catch (IOException e) {
                    Log.i("x", e.getMessage());
                    e.printStackTrace();

                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main_bluetooth, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if(requestCode == ENABLE_BLUETOOTH) {
            if(resultCode == RESULT_OK) {
                statusMessage.setText("Bluetooth ativado");
            }
            else {
                statusMessage.setText("Bluetooth não ativado");
            }
        }
        else if(requestCode == SELECT_PAIRED_DEVICE || requestCode == SELECT_DISCOVERED_DEVICE) {
            if(resultCode == RESULT_OK) {
                statusMessage.setText("Você selecionou " + data.getStringExtra("btDevName") + "\n"
                        + data.getStringExtra("btDevAddress"));

                connect = new ConnectionThread(data.getStringExtra("btDevAddress"));
                connect.start();
            }
            else {
                statusMessage.setText("Nenhum dispositivo selecionado");
            }
        }
    }

    public void searchPairedDevices(View view) {

        Intent searchPairedDevicesIntent = new Intent(this, PairedDevices.class);
        startActivityForResult(searchPairedDevicesIntent, SELECT_PAIRED_DEVICE);
    }

    public void discoverDevices(View view) {

        Intent searchPairedDevicesIntent = new Intent(this, DiscoveredDevices.class);
        startActivityForResult(searchPairedDevicesIntent, SELECT_DISCOVERED_DEVICE);
    }

    public void enableVisibility(View view) {

        Intent discoverableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
        discoverableIntent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 30);
        startActivity(discoverableIntent);
    }

    public void waitConnection(View view) {

        connect = new ConnectionThread();
        connect.start();
    }

    public void sendMessage(View view) {

        EditText messageBox = (EditText) findViewById(R.id.editText_MessageBox);
        String messageBoxString = messageBox.getText().toString();
        byte[] data =  messageBoxString.getBytes();
        connect.write(data);
    }

    public static Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {

            Bundle bundle = msg.getData();
            byte[] data = bundle.getByteArray("data");
            String dataString= new String(data);

            if(dataString.equals("---N"))
                statusMessage.setText("Ocorreu um erro durante a conexão");
            else if(dataString.equals("---S"))
                statusMessage.setText("Conectado");
            else {

                textSpace.setText(new String(data));
            }
        }
    };

    private void getMac() {
        try {

            int SDK_INT = android.os.Build.VERSION.SDK_INT;

            if (SDK_INT > 8) {
                StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                        .permitAll().build();
                StrictMode.setThreadPolicy(policy);

                ServerSocket server = new ServerSocket(PORT);
                Socket socket = server.accept();

                InputStreamReader serverStreamReader = new InputStreamReader(socket.getInputStream());
                Scanner scanner = new Scanner(serverStreamReader);

                ClienteActivity.mac = scanner.nextLine();
                Log.i("info", "mac = " + ClienteActivity.mac);

            }

        } catch (IOException e) {
            Log.i("x", e.getMessage());
            e.printStackTrace();
        }
    }

}


