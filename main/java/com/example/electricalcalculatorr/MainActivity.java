package com.example.electricalcalculatorr;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import java.math.RoundingMode;
import java.text.DecimalFormat;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    EditText numElectric, rebate;
    Button btnCalc;
    TextView electricOutput, rebateOutput, finalCostOutput;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Electricity Calculate");

        numElectric = findViewById(R.id.numElectric);
        rebate = findViewById(R.id.rebate);
        btnCalc = findViewById(R.id.btnCalc);

        electricOutput = findViewById(R.id.electricOutput);
        rebateOutput = findViewById(R.id.rebateOutput);
        finalCostOutput = findViewById(R.id.finalCostOutput);

        btnCalc.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btnCalc) {
            calculateElectricBill();
        }
    }

    private void calculateElectricBill() {
        String inputElectric = numElectric.getText().toString();
        String inputRebate = rebate.getText().toString();

        if (TextUtils.isEmpty(inputElectric) || TextUtils.isEmpty(inputRebate)) {
            Toast.makeText(this, "Please enter values for electricity and rebate", Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            double electricity = Double.parseDouble(inputElectric);
            int rebateValue = Integer.parseInt(inputRebate);

            // Check if the rebate value is within the allowed range
            if (rebateValue < 1 || rebateValue > 5) {
                Toast.makeText(this, "Rebate must be between 1 and 5", Toast.LENGTH_SHORT).show();
                return;
            }

            double totalCharge = 0.0;

            if (electricity <= 200) {
                totalCharge = electricity * 21.8;
            } else if (electricity <= 300) {
                totalCharge = (200 * 21.8) + ((electricity - 200) * 33.4);
            } else if (electricity <= 600) {
                totalCharge = (200 * 21.8) + (100 * 33.4) + ((electricity - 300) * 51.6);
            } else if (electricity > 600) {
                totalCharge = (200 * 21.8) + (100 * 33.4) + (300 * 51.6) + ((electricity - 600) * 54.6);
            }

            double finalCost = totalCharge - (totalCharge * (rebateValue / 100.0));

            DecimalFormat decimalFormat = new DecimalFormat("#,##0.000");
            decimalFormat.setRoundingMode(RoundingMode.DOWN);

            String formattedElectricityResult = decimalFormat.format(totalCharge / 100.0);
            String formattedFinalCost = decimalFormat.format(finalCost / 100.0);

            electricOutput.setText("Total Charges: RM " + formattedElectricityResult);
            rebateOutput.setText("Rebate: " + inputRebate + "%");
            finalCostOutput.setText("Final Cost: RM " + formattedFinalCost);

            Toast.makeText(this, "Final Cost: RM " + formattedFinalCost, Toast.LENGTH_SHORT).show();
        } catch (NumberFormatException e) {
            Toast.makeText(this, "Invalid input. Please enter valid numbers", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        return onClickMenuItem(item) || super.onOptionsItemSelected(item);
    }

    private boolean onClickMenuItem(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.profile) {
            Intent intent1 = new Intent(this, ProfileActivity.class);
            startActivity(intent1);
            return true;

        } else if (id == R.id.about) {
            Intent intent2 = new Intent(this, AboutActivity.class);
            startActivity(intent2);
            return true;
        }
        return false;
    }
}
