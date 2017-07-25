
import android.os.StrictMode;
import android.support.v4.widget.TextViewCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.StringReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;
import java.util.stream.Stream;

public class MainActivity extends AppCompatActivity {

    private String serverHost = "192.168.43.42";
    private int serverPort = 1337;

    private static String mac;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final TextView text = (TextView) findViewById(R.id.text);
        Button button = (Button) findViewById(R.id.pressButton);

        button.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                text.setText("Proxing...");

                try {
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
                        text.setText("Data sended! ");
                        Thread.sleep(200);

                        character = proxyStreamReader.read();
                        while (character != -1) {
                            outputStreamWriter.write(character);
                            character = proxyStreamReader.read();
                        }

                        outputStreamWriter.flush();
                    }

                    text.setText("Data received: " + new String(outputStream.toByteArray(), StandardCharsets.UTF_8));

                } catch (IOException e) {
                    Log.i("x", e.getMessage());
                    e.printStackTrace();

                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            }
        });
    }
}