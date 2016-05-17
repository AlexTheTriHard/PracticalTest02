package practicaltest02.pdsd.systems.cs.pub.ro.practicaltest02;

import android.util.Log;

import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.HashMap;

/**
 * Created by Songbird on 5/17/2016.
 */
public class CommunicationThread extends Thread {

    private ServerThread serverThread;
    private Socket socket;

    public CommunicationThread(ServerThread serverThread, Socket socket) {
        this.serverThread = serverThread;
        this.socket       = socket;
    }

    @Override
    public void run() {
        if (socket != null) {
            try {
                BufferedReader bufferedReader = Utilities.getReader(socket);
                PrintWriter printWriter    = Utilities.getWriter(socket);
                if (bufferedReader != null && printWriter != null) {
                    Log.i(Constants.TAG, "[COMMUNICATION THREAD] Waiting for parameters from client (Key/value information type)!");
                    String informationType = bufferedReader.readLine();
                    String key            = bufferedReader.readLine();
                    String value          = bufferedReader.readLine();

                    HashMap<String, Timeapi> data = serverThread.getData();
                    Timeapi timeInformation = null;
                    if (key != null && !key.isEmpty()&& value != null && !value.isEmpty()
                            && informationType != null && !informationType.isEmpty()) {
                        if (data.containsKey(key)) {
                            Log.i(Constants.TAG, "[COMMUNICATION THREAD] Getting the information from the cache...");
                            timeInformation = data.get(key);
                            String result = timeInformation.toString();
                            printWriter.println(result);
                            printWriter.flush();
                            serverThread.setData(key, timeInformation);
                        } else {
                            Log.i(Constants.TAG, "[COMMUNICATION THREAD] Getting the information from the webservice...");
                            HttpClient httpClient = new DefaultHttpClient();
                            HttpGet httpget = new HttpGet("http://www.timeapi.org/utc/now");
                            ResponseHandler<String> responseHandler = new BasicResponseHandler();
                            String pageSourceCode = httpClient.execute(httpget, responseHandler);
                            
                            if (pageSourceCode != null) {
                                timeInformation = new Timeapi(pageSourceCode);
                                printWriter.println(pageSourceCode);
                                printWriter.flush();
                                serverThread.setData(key, timeInformation);
                            } else {
                                Log.e(Constants.TAG, "[COMMUNICATION THREAD] Error getting the information from the webservice!");
                            }
                        }



                    } else {
                        Log.e(Constants.TAG, "[COMMUNICATION THREAD] Error receiving parameters from client (city / information type)!");
                    }
                } else {
                    Log.e(Constants.TAG, "[COMMUNICATION THREAD] BufferedReader / PrintWriter are null!");
                }
                socket.close();
            } catch (IOException ioException) {
                Log.e(Constants.TAG, "[COMMUNICATION THREAD] An exception has occurred: " + ioException.getMessage());
                if (Constants.DEBUG) {
                    ioException.printStackTrace();
                }
            }
        } else {
            Log.e(Constants.TAG, "[COMMUNICATION THREAD] Socket is null!");
        }
    }

}
