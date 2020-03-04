package org.godotengine.godot;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.Context;
import android.os.Bundle;
import android.os.Build;
import android.os.Environment;
import android.os.StatFs;
import android.util.Log;
import android.util.DisplayMetrics;
import android.telephony.TelephonyManager;
import android.view.WindowManager;
import android.view.Display;
import java.math.BigDecimal;
import java.io.IOException;
import java.io.File;
import java.util.Currency;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;
import java.util.Locale;
import java.util.Date;
import java.lang.Exception;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings;

public class GodotFirebaseRemoteConfig extends Godot.SingletonBase {

    private Godot activity = null;
    private FirebaseRemoteConfig mFirebaseRemoteConfig = null;
    private boolean _loaded = false;

    static public Godot.SingletonBase initialize(Activity p_activity) 
    { 
        return new GodotFirebaseRemoteConfig(p_activity); 
    } 

    public GodotFirebaseRemoteConfig(Activity p_activity) 
    {
        registerClass("FirebaseRemoteConfig", new String[]{
                "get_boolean",
                "get_double",
                "get_int",
                "get_string",
                "loaded"
            });
        // it will work event without argument types
        addSignal("FirebaseRemoteConfig", "loaded", new String[] {});
        activity = (Godot)p_activity;
        mFirebaseRemoteConfig = FirebaseRemoteConfig.getInstance();
        init();
    }

    // Public methods

    /*
    public void init(final String key)
    {
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                try {
                } catch (Exception e) {
                    Log.e("godot", "Exception: " + e.getMessage());  
                }
            }
        });
    }
    */

    private void init() {
        mFirebaseRemoteConfig.fetchAndActivate()
        .addOnCompleteListener(activity, new OnCompleteListener<Boolean>() {
                @Override
                public void onComplete(Task<Boolean> task) {
                    if (task.isSuccessful()) {
                        boolean updated = task.getResult();
                        Log.d("godot", "RemoteConfig params updated: " + updated);
                        _loaded = true;
                        emitSignal("FirebaseRemoteConfig", "loaded", new Object[] {});
                    } else {
                        Log.w("godot", "RemoteConfig update failed!");
                    }
                }
            });
    }
    
    public boolean get_boolean(final String pname) {
        return mFirebaseRemoteConfig.getBoolean(pname);
    }

    public float get_double(final String pname) {
        return (float)mFirebaseRemoteConfig.getDouble(pname);
    }

    public int get_int(final String pname) {
        return (int)mFirebaseRemoteConfig.getLong(pname);
    }

    public String get_string(final String pname) {
        return mFirebaseRemoteConfig.getString(pname);
    }

    public boolean loaded() {
        return _loaded;
    }

    // Internal methods

    public void callbackSuccess(String ticket, String signature, String sku) {
		//GodotLib.callobject(facebookCallbackId, "purchase_success", new Object[]{ticket, signature, sku});
        //GodotLib.calldeferred(purchaseCallbackId, "consume_fail", new Object[]{});
	}
    @Override protected void onMainActivityResult (int requestCode, int resultCode, Intent data)
    {
    }
}
