import android.os.StrictMode;
import android.support.v4.widget.TextViewCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.StringReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;
import java.util.stream.Stream;

public class MainActivity extends AppCompatActivity {

    private String serverHost = "127.0.0.1";
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
                text.setText("Waiting mac...");

                try {

                    int SDK_INT = android.os.Build.VERSION.SDK_INT;
                    
                    if (SDK_INT > 8) {
                        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                                .permitAll().build();
                        StrictMode.setThreadPolicy(policy);

                        //your codes here
                        ServerSocket server = new ServerSocket(1337);
                        Socket socket = server.accept();

                        InputStreamReader serverStreamReader = new InputStreamReader(socket.getInputStream());
                        Scanner scanner = new Scanner(serverStreamReader);

                        MainActivity.mac = scanner.nextLine();

                        text.setText(MainActivity.mac);
                    }
/*
                    Socket proxySocket = new Socket(serverHost, serverPort);
                    OutputStreamWriter proxyStreamWriter = new OutputStreamWriter(proxySocket.getOutputStream());

                    int character;
                    do {
                        character = clientStreamReader.read();
                        proxyStreamWriter.write(character);
                    } while (character != -1);
                    */

                } catch (IOException e) {
                    Log.i("x", e.getMessage());
                    e.printStackTrace();

                }

            }
        });
    }
}