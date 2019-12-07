package edu.uw.tcss450.tcss450_group4;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import edu.uw.tcss450.tcss450_group4.ui.HomeFragment;
import me.pushy.sdk.Pushy;

/**
 * The activity for when you login
 * @author Abraham Lee abe2016@uw.edu
 */
public class MainActivity extends AppCompatActivity {

    /**
     * Creates the Activity. Checks for any notifications to forward throughout app
     * @param savedInstanceState the state of the app
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Pushy.listen(this);
        setContentView(R.layout.activity_main);

        if (getIntent().getExtras() != null) {
            if (getIntent().getExtras().containsKey("type")) {
                Navigation.findNavController(this, R.id.nav_host_fragment)
                        .setGraph(R.navigation.login_navigation, getIntent().getExtras());
            }
        }
    }

    /**
     * When back is pressed at Home page, app closes
     */
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
