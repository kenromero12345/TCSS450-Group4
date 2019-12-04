package edu.uw.tcss450.tcss450_group4;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Context;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import edu.uw.tcss450.tcss450_group4.ui.VerifyFragment;
import edu.uw.tcss450.tcss450_group4.ui.HomeFragment;
import me.pushy.sdk.Pushy;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Pushy.listen(this);
        setContentView(R.layout.activity_main);
//        Intent intent = new Intent(this, HomeActivity.class);
//        startActivity(intent);
    }

    private void closeKeyboard() {
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
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
