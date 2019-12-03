package edu.uw.tcss450.tcss450_group4;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import edu.uw.tcss450.tcss450_group4.ui.HomeFragment;

public class MainActivity extends AppCompatActivity {

    /**
     *
     * @param savedInstanceState the sta
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        Intent intent = new Intent(this, HomeActivity.class);
//        startActivity(intent);
    }


    @Override
    public void onBackPressed() {
        Fragment f = getSupportFragmentManager().findFragmentById(R.id.nav_home);
        if (f instanceof HomeFragment) {

            this.finish();
        } else {
            super.onBackPressed();
        }

    }
}
