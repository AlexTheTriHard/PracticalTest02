package practicaltest02.pdsd.systems.cs.pub.ro.practicaltest02;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class PracticalTest02MainActivity extends Activity {
    // Server widgets
    private EditText serverPortEditText       = null;
    private Button       connectButton            = null;

    // Client widgets
    private EditText     clientAddressEditText    = null;
    private EditText     clientPortEditText       = null;
    private EditText     keyEditText             = null;
    private EditText     valueEditText             = null;
    private Spinner      informationTypeSpinner   = null;
    private Button       ForecastButton = null;
    private TextView weatherForecastTextView  = null;




    private ServerThread serverThread             = null;
    private ClientThread clientThread             = null;

    private ConnectButtonClickListener connectButtonClickListener = new ConnectButtonClickListener();
    private class ConnectButtonClickListener implements Button.OnClickListener {

        @Override
        public void onClick(View view) {
            String serverPort = serverPortEditText.getText().toString();
            if (serverPort == null || serverPort.isEmpty()) {
                Toast.makeText(
                        getApplicationContext(),
                        "Server port should be filled!",
                        Toast.LENGTH_SHORT
                ).show();
                return;
            }

            serverThread = new ServerThread(Integer.parseInt(serverPort));
            if (serverThread.getServerSocket() != null) {
                serverThread.start();
            } else {
                Log.e(Constants.TAG, "[MAIN ACTIVITY] Could not creat server thread!");
            }

        }
    }
    @Override
    protected void onDestroy() {
        if (serverThread != null) {
            serverThread.stopThread();
        }
        super.onDestroy();
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_practical_test02_main);

        serverPortEditText = (EditText)findViewById(R.id.server_port_edit_text);
        connectButton = (Button)findViewById(R.id.connect_button);
        connectButton.setOnClickListener(connectButtonClickListener);

        clientAddressEditText = (EditText)findViewById(R.id.client_address_edit_text);
        clientPortEditText = (EditText)findViewById(R.id.client_port_edit_text);
        keyEditText = (EditText)findViewById(R.id.key_text);
        valueEditText = (EditText)findViewById(R.id.value_text);
        informationTypeSpinner = (Spinner)findViewById(R.id.information_type_spinner);
        ForecastButton = (Button)findViewById(R.id.get_weather_forecast_button);
        ForecastButton.setOnClickListener(getWeatherForecastButtonClickListener);
        weatherForecastTextView = (TextView)findViewById(R.id.weather_forecast_text_view);


    }

    private GetWeatherForecastButtonClickListener getWeatherForecastButtonClickListener = new GetWeatherForecastButtonClickListener();
    private class GetWeatherForecastButtonClickListener implements Button.OnClickListener {

        @Override
        public void onClick(View view) {
            String clientAddress = clientAddressEditText.getText().toString();
            String clientPort    = clientPortEditText.getText().toString();
            if (clientAddress == null || clientAddress.isEmpty() ||
                    clientPort == null || clientPort.isEmpty()) {
                Toast.makeText(
                        getApplicationContext(),
                        "Client connection parameters should be filled!",
                        Toast.LENGTH_SHORT
                ).show();
                return;
            }

            if (serverThread == null || !serverThread.isAlive()) {
                Log.e(Constants.TAG, "[MAIN ACTIVITY] There is no server to connect to!");
                return;
            }

            String key = keyEditText.getText().toString();
            String value = valueEditText.getText().toString();
            String informationType = informationTypeSpinner.getSelectedItem().toString();
            if(informationType == null || informationType.isEmpty()){
                Toast.makeText(
                        getApplicationContext(),
                        "Parameters from client get/put should be filled!",
                        Toast.LENGTH_SHORT
                ).show();
                return;

            }
            if (informationType.equals("Put")){
                if (value == null || value.isEmpty() || key == null || key.isEmpty()) {
                    Toast.makeText(
                            getApplicationContext(),
                            "[PUT]Parameters from client (key/value information type) should be filled!",
                            Toast.LENGTH_SHORT
                    ).show();
                    return;
                }
                clientThread = new ClientThread(
                        clientAddress,
                        Integer.parseInt(clientPort),
                        key,
                        value,
                        informationType,
                        weatherForecastTextView);


            }
            if (informationType.equals("Get")){
                if (key == null || key.isEmpty()) {
                    Toast.makeText(
                            getApplicationContext(),
                            "[GET]Parameters from client (key information type) should be filled!",
                            Toast.LENGTH_SHORT
                    ).show();
                    return;
                }
                clientThread = new ClientThread(
                        clientAddress,
                        Integer.parseInt(clientPort),
                        key,
                        value,
                        informationType,
                        weatherForecastTextView);


            }
            weatherForecastTextView.setText(Constants.EMPTY_STRING);
            clientThread.start();
        }
    }
}
