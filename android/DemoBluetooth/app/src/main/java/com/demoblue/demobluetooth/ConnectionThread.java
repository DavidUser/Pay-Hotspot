package com.demoblue.demobluetooth;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.os.Bundle;
import android.os.Message;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.UUID;

public class ConnectionThread extends Thread{

    BluetoothSocket btSocket = null;
    BluetoothServerSocket btServerSocket = null;
    InputStream input = null;
    OutputStream output = null;
    String btDevAddress = null;
    String myUUID = "00001401-0000-9000-8000-00805F9B34FF";
    boolean server;
    boolean running = false;

    public ConnectionThread() {

        this.server = true;
    }

    public ConnectionThread(String btDevAddress) {

        this.server = false;
        this.btDevAddress = btDevAddress;
    }

    public void run() {

        this.running = true;
        BluetoothAdapter btAdapter = BluetoothAdapter.getDefaultAdapter();

        if(this.server) {

            try {

                btServerSocket = btAdapter.listenUsingRfcommWithServiceRecord("Super Bluetooth", UUID.fromString(myUUID));
                btSocket = btServerSocket.accept();

                if(btSocket != null) {

                    btServerSocket.close();
                }

            } catch (IOException e) {
                e.printStackTrace();
                toMainActivity("---N".getBytes());
            }


        } else {
            try {
                BluetoothDevice btDevice = btAdapter.getRemoteDevice(btDevAddress);
                btSocket = btDevice.createRfcommSocketToServiceRecord(UUID.fromString(myUUID));
                btAdapter.cancelDiscovery();
                if (btSocket != null)
                    btSocket.connect();

            } catch (IOException e) {
                e.printStackTrace();
                toMainActivity("---N".getBytes());
            }

        }
        if(btSocket != null) {

            toMainActivity("---S".getBytes());
            try {

                input = btSocket.getInputStream();
                output = btSocket.getOutputStream();
                byte[] buffer = new byte[1024];
                int bytes;
                while(running) {

                    bytes = input.read(buffer);
                    toMainActivity(Arrays.copyOfRange(buffer, 0, bytes));

                }

            } catch (IOException e) {

                e.printStackTrace();
                toMainActivity("---N".getBytes());
            }
        }

    }
    private void toMainActivity(byte[] data) {

        Message message = new Message();
        Bundle bundle = new Bundle();
        bundle.putByteArray("data", data);
        message.setData(bundle);
        ClienteActivity.handler.sendMessage(message);
    }

    public void write(byte[] data) {

        if(output != null) {
            try {
                output.write(data);

            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            toMainActivity("---N".getBytes());
        }
    }

    public void cancel() {

        try {

            running = false;
            btServerSocket.close();
            btSocket.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
        running = false;
    }
}