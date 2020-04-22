package drm.godot.plugin;

import android.app.Activity;
import android.util.Log;
import android.view.View;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import org.godotengine.godot.Godot;
import org.godotengine.godot.plugin.GodotPlugin;
import org.godotengine.godot.plugin.SignalInfo;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings;

public class FirebaseRemoteConfigPlugin extends GodotPlugin {

    final private SignalInfo loadedSignal = new SignalInfo("loaded");
    private Godot activity = null;
    private FirebaseRemoteConfig mFirebaseRemoteConfig = null;
    private boolean _loaded = false;

    public FirebaseRemoteConfigPlugin(Godot godot) {
        super(godot);
        activity = godot;
        mFirebaseRemoteConfig = FirebaseRemoteConfig.getInstance();
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
                             "loaded");
    }

    @Override
    public Set<SignalInfo> getPluginSignals() {
        return Collections.singleton(loadedSignal);
    }

    @Override
    public View onMainCreateView(Activity activity) {
        return null;
    }

    private void init() {
        mFirebaseRemoteConfig.fetchAndActivate()
        .addOnCompleteListener(activity, new OnCompleteListener<Boolean>() {
                @Override
                public void onComplete(Task<Boolean> task) {
                    if (task.isSuccessful()) {
                        boolean updated = task.getResult();
                        Log.d("godot", "RemoteConfig params updated: " + updated);
                        _loaded = true;
                        emitSignal(loadedSignal.getName());
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
}
