package eu.thedarken.wldonate.main.core;

import android.content.Context;
import android.content.SharedPreferences;

import java.io.File;

import javax.inject.Inject;

import eu.thedarken.wldonate.AppComponent;
import eu.thedarken.wldonate.ApplicationContext;
import eu.thedarken.wldonate.SharedPrefsDir;
import timber.log.Timber;

@AppComponent.Scope
public class WidgetSettingsRepo {
    private final Context context;
    private final File sharedPrefsDir;

    @Inject
    public WidgetSettingsRepo(@SharedPrefsDir File sharedPrefsDir, @ApplicationContext Context context) {
        this.sharedPrefsDir = sharedPrefsDir;
        this.context = context;
    }

    private static String getWidgetPrefsFileName(int appWidgetId) {
        return "widget_" + appWidgetId + "_prefs";
    }

    public SharedPreferences getSettings(int appWidgetId) {
        return context.getSharedPreferences(getWidgetPrefsFileName(appWidgetId), Context.MODE_PRIVATE);
    }

    public void deleteSettings(int appWidgetId) {
        File prefs = new File(sharedPrefsDir, getWidgetPrefsFileName(appWidgetId) + ".xml");
        if (prefs.exists() && !prefs.delete()) Timber.e("Failed to delete prefs: %s", prefs.getPath());
    }
}
