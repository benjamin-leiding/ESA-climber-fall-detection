package com.lha.falldetection;

import android.app.Application;

/**
 * Created by lucahernandezacosta on 29.07.18.
 */

public class SetDBonStartChecker extends Application {


    public SetDBonStartChecker(){
        MainActivity.firstCall = true;
    }
}
