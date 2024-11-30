package com.azzam.electric;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Set up the toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Initialize UI elements
        EditText etUnits = findViewById(R.id.etUnits);
        EditText etRebate = findViewById(R.id.etRebate);
        TextView tvBillBeforeRebate = findViewById(R.id.tvBillBeforeRebate);
        TextView tvBillAfterRebate = findViewById(R.id.tvBillAfterRebate);
        Button btnCalculate = findViewById(R.id.btnCalculate);
        Button btnClear = findViewById(R.id.btnClear); // Added Clear button

        // Set button click listener for Calculate
        btnCalculate.setOnClickListener(view -> {
            try {
                // Get and validate inputs
                String unitsText = etUnits.getText().toString();
                String rebateText = etRebate.getText().toString();

                if (unitsText.isEmpty() || rebateText.isEmpty()) {
                    Toast.makeText(MainActivity.this, "Please fill in all fields!", Toast.LENGTH_SHORT).show();
                    return;
                }

                double units = Double.parseDouble(unitsText);
                double rebatePercentage = Double.parseDouble(rebateText);

                if (units < 0) {
                    Toast.makeText(MainActivity.this, "Units cannot be negative!", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (rebatePercentage < 0 || rebatePercentage > 5) {
                    Toast.makeText(MainActivity.this, "Rebate percentage must be between 0 and 5!", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Calculate the bills
                double[] bills = calculateBill(units, rebatePercentage);

                // Display the results
                tvBillBeforeRebate.setText(String.format("Bill Before Rebate: RM %.2f", bills[0]));
                tvBillAfterRebate.setText(String.format("Bill After Rebate: RM %.2f", bills[1]));
            } catch (NumberFormatException e) {
                // Show error message for invalid input format
                Toast.makeText(MainActivity.this, "Invalid input! Please enter valid numbers.", Toast.LENGTH_SHORT).show();
            }
        });

        // Set button click listener for Clear
        btnClear.setOnClickListener(view -> {
            // Clear input fields
            etUnits.setText("");
            etRebate.setText("");

            // Clear output results
            tvBillBeforeRebate.setText("");
            tvBillAfterRebate.setText("");

            // Show feedback to user
            Toast.makeText(MainActivity.this, "Inputs and results cleared!", Toast.LENGTH_SHORT).show();
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.about_developer) {
            // Navigate to AboutDeveloperActivity
            Intent intent = new Intent(this, AboutDeveloperActivity.class);
            startActivity(intent);
            return true;
        } else if (id == R.id.info) {
            // Navigate to InfoActivity
            Intent intent = new Intent(this, InfoActivity.class);
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    // Method to calculate the electricity bill
    private double[] calculateBill(double units, double rebatePercentage) {
        double billBeforeRebate = 0;
        double remainingUnits = units;

        // Block charges
        if (remainingUnits > 0) {
            double block1 = Math.min(remainingUnits, 200);
            billBeforeRebate += block1 * 0.218;
            remainingUnits -= block1;
        }

        if (remainingUnits > 0) {
            double block2 = Math.min(remainingUnits, 100);
            billBeforeRebate += block2 * 0.334;
            remainingUnits -= block2;
        }

        if (remainingUnits > 0) {
            double block3 = Math.min(remainingUnits, 300);
            billBeforeRebate += block3 * 0.516;
            remainingUnits -= block3;
        }

        if (remainingUnits > 0) {
            billBeforeRebate += remainingUnits * 0.546;
        }

        // Bill after rebate
        double billAfterRebate = billBeforeRebate - (billBeforeRebate * (rebatePercentage / 100));

        return new double[]{billBeforeRebate, billAfterRebate};
    }
}
