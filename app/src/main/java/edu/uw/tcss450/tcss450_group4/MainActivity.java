package edu.uw.tcss450.tcss450_group4;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity implements VerifyFragment.OnFragmentInteractionListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Intent intent = new Intent(this, HomeActivity.class);
        startActivity(intent);
    }
    @Override
    public void onVerifySuccess() {
        HomeActivity homeActivity = new HomeActivity();
        Intent intent = new Intent(this, homeActivity.getClass());
//        homeActivity.setArguments(args);
//        FragmentTransaction transaction = getSupportFragmentManager()
//                .beginTransaction()
//                .replace(R.id.frame_main_container, homeActivity)
//                .addToBackStack(null);
        // Commit the transaction
//        transaction.commit();
        this.startActivity(intent);
    }
}
