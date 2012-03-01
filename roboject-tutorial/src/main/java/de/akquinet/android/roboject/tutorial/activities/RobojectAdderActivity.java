package de.akquinet.android.roboject.tutorial.activities;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import de.akquinet.android.roboject.RobojectActivity;
import de.akquinet.android.roboject.annotations.InjectLayout;
import de.akquinet.android.roboject.annotations.InjectService;
import de.akquinet.android.roboject.annotations.InjectView;
import de.akquinet.android.roboject.tutorial.services.TestService;

@InjectLayout("adder_layout")
public class RobojectAdderActivity extends RobojectActivity {

    @InjectView private Button addNumbersButton;
    @InjectView private EditText number1Input;
    @InjectView private EditText number2Input;
    @InjectView private TextView addResultText;
    @InjectService(clazz = TestService.class)
    private TestService.AdderService adderService;

    @Override
    public void onReady() {
        super.onReady();

        addNumbersButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    int number1 = Integer.parseInt(number1Input.getText().toString());
                    int number2 = Integer.parseInt(number2Input.getText().toString());
                    addResultText.setText("Result is " + adderService.add(number1, number2));
                } catch (NumberFormatException e) {
                    Toast toast = Toast.makeText(getApplicationContext(), "Please enter two integer values", 3);
                    toast.show();
                }
            }
        });
    }
}
