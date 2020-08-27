package ru.mobilap.firebaseremoteconfig;

import android.app.Activity;
import android.util.Log;
import android.view.View;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.HashMap;
import org.godotengine.godot.Godot;
import org.godotengine.godot.plugin.GodotPlugin;
import org.godotengine.godot.plugin.SignalInfo;
import org.godotengine.godot.Dictionary;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
//import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings;

public class FirebaseRemoteConfig extends GodotPlugin {

    private final String TAG = FirebaseRemoteConfig.class.getName();
    final private SignalInfo loadedSignal = new SignalInfo("loaded");
    private Godot activity = null;
    private com.google.firebase.remoteconfig.FirebaseRemoteConfig mFirebaseRemoteConfig = null;
    private boolean _loaded = false;

    public FirebaseRemoteConfig(Godot godot) {
        super(godot);
        activity = godot;
        mFirebaseRemoteConfig = com.google.firebase.remoteconfig.FirebaseRemoteConfig.getInstance();
        init();
    }

    @Override
    public String getPluginName() {
        return "FirebaseRemoteConfig";
    }

    @Override
    public List<String> getPluginMethods() {
        return Arrays.asList("get_boolean",
                             "get_double",
                             "get_int",
                             "get_string",
                             "loaded",
                             "setDefaults");
    }

    @Override
    public Set<SignalInfo> getPluginSignals() {
        return Collections.singleton(loadedSignal);
    }

    @Override
    public View onMainCreate(Activity activity) {
        return null;
    }

    private void init() {
        mFirebaseRemoteConfig.fetchAndActivate()
        .addOnCompleteListener(activity, new OnCompleteListener<Boolean>() {
                @Override
                public void onComplete(Task<Boolean> task) {
                    if (task.isSuccessful()) {
                        boolean updated = task.getResult();
                        Log.d(TAG, "RemoteConfig params updated: " + updated);
                        _loaded = true;
                        emitSignal(loadedSignal.getName());
                    } else {
                        Log.w(TAG, "RemoteConfig update failed!");
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

    public void setDefaults(final Dictionary defs) {
        HashMap<String, Object> map = new HashMap<>();
        for(String key: defs.get_keys()) {
            map.put(key, defs.get(key));
        }
        
        mFirebaseRemoteConfig.setDefaultsAsync(map);
    }
}
