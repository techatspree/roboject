package de.akquinet.android.roboject.tutorial.activities;

import android.app.Activity;
import android.app.Service;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import de.akquinet.android.roboject.tutorial.R;
import de.akquinet.android.roboject.tutorial.services.TestService;

public class AdderActivity extends Activity {
    
    private Button addNumbers;
    private EditText number1Input;
    private EditText number2Input;
    private TextView resultText;

    private TestService.AdderService adderService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.adder_layout);
        
        number1Input = (EditText) findViewById(R.id.number1Input);
        number2Input = (EditText) findViewById(R.id.number2Input);
        addNumbers = (Button) findViewById(R.id.addNumbersButton);
        resultText = (TextView) findViewById(R.id.addResultText);
        
        addNumbers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (adderService != null) {
                    try {
                        int number1 = Integer.parseInt(number1Input.getText().toString());
                        int number2 = Integer.parseInt(number2Input.getText().toString());
                        resultText.setText("Result is " + adderService.add(number1, number2));
                    } catch (NumberFormatException e) {
                        Toast toast = Toast.makeText(getApplicationContext(), "Please enter two integer values", 3);
                        toast.show();
                    }
                }
            }
        });
        
        Intent intent = new Intent();
        intent.setClassName(getApplicationContext(), TestService.class.getName());
        getApplicationContext().bindService(intent, new AdderConnection(), Service.BIND_AUTO_CREATE);
    }


    private class AdderConnection implements ServiceConnection {

        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            adderService = (TestService.AdderService) iBinder;
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            adderService = null;
        }
    }
}
