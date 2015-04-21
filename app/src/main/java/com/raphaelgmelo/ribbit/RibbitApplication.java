package com.raphaelgmelo.ribbit;

import android.app.Application;

import com.parse.Parse;
import com.parse.ParseObject;

/**
 * Created by raphaelgmelo on 19/04/15.
 */

// Applications classes are loaded when the App is opened
// this must be in Manifest too
public class RibbitApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        // Enable Local Datastore.
        Parse.enableLocalDatastore(this);
        Parse.initialize(this, "dAgaQ0ljDR1aAxVz9GIlJ3u8jigTeU5CDGXxLkmV", "UFUBpOqPM7GBSOrtnH0Bm8T52BkkMjRb4fqJI1FR");

    }
}
