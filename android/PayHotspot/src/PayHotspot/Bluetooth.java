package PayHotspot;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashSet;
import java.util.Set;

import android.app.Activity;
import android.bluetooth.*;
import android.content.*;

public class Bluetooth {
  private String mac;
  private static BluetoothAdapter adapter = BluetoothAdapter.getDefaultAdapter();

  public static Set<String> discover(Activity activity, int milliseconds) {
    final Set<String> devicesMacAdress = new HashSet<String>();

    IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
    BroadcastReceiver receiver = new BroadcastReceiver() {
      public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        if (BluetoothDevice.ACTION_FOUND.equals(action)) {
          BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
          String deviceHardwareAdress = device.getAddress();
          devicesMacAdress.add(deviceHardwareAdress);
        }
      }
    };

    activity.registerReceiver(receiver, filter);
    Bluetooth.adapter.startDiscovery();

    try {
      Thread.sleep(1000);
    } catch (InterruptedException e){
      e.printStackTrace();
    }

    Bluetooth.adapter.cancelDiscovery();
    activity.unregisterReceiver(receiver);
    
    return devicesMacAdress;
  }

  public Bluetooth(String mac) {
    this.mac = mac;
  }

  public InputStream getInputStream() {
    return null;
  }

  public OutputStream getOutputStream() {
    return null;
  }

  public void open() {}
  public void close() {}
}
