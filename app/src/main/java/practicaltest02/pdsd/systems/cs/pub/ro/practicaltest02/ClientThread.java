package practicaltest02.pdsd.systems.cs.pub.ro.practicaltest02;

import android.util.Log;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

/**
 * Created by Songbird on 5/17/2016.
 */
public class ClientThread extends Thread  {


    private String   address;
    private int      port;
    private String   key, value;
    private String   informationType;
    private TextView weatherForecastTextView;


    private Socket socket;

    public ClientThread(
            String address,
            int port,
            String key,
            String value,
            String informationType,
            TextView weatherForecastTextView) {
        this.address                 = address;
        this.port                    = port;
        this.key                     = key;
        this.value                   = value;
        this.informationType         = informationType;
        this.weatherForecastTextView = weatherForecastTextView;
    }

    @Override
    public void run() {
        try {
            socket = new Socket(address, port);
            if (socket == null) {
                Log.e(Constants.TAG, "[CLIENT THREAD] Could not create socket!");
            }

            BufferedReader bufferedReader = Utilities.getReader(socket);
            PrintWriter printWriter    = Utilities.getWriter(socket);
            if (bufferedReader != null && printWriter != null) {
                printWriter.println(informationType);
                printWriter.flush();
                printWriter.println(key);
                printWriter.flush();
                printWriter.println(value);
                printWriter.flush();

                String timeInformation;
                while ((timeInformation = bufferedReader.readLine()) != null) {
                    final String finalizedTimeInformation = timeInformation;
                    weatherForecastTextView.post(new Runnable() {
                        @Override
                        public void run() {
                            weatherForecastTextView.append(finalizedTimeInformation + "\n");
                        }
                    });
                }
            } else {
                Log.e(Constants.TAG, "[CLIENT THREAD] BufferedReader / PrintWriter are null!");
            }
            socket.close();
        } catch (IOException ioException) {
            Log.e(Constants.TAG, "[CLIENT THREAD] An exception has occurred: " + ioException.getMessage());
            if (Constants.DEBUG) {
                ioException.printStackTrace();
            }
        }
    }


}
