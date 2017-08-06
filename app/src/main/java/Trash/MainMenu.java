package Trash;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Bundle;

import java.util.ArrayList;
import java.util.List;

import mingorto.launcher.AppDetail;
import mingorto.launcher.R;

import static mingorto.launcher.SettingScreens.FirstRow.FirstFirstRow.USER_SETTINGS;
import static mingorto.launcher.SettingScreens.FirstRow.FirstFirstRow.USER_SETTINGS_LAUNCHER_TYPE;

public class MainMenu extends Activity {
    private SharedPreferences sharedPreferences;
    private PackageManager manager;
    private List<AppDetail> appList;
    private int menuType;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        sharedPreferences = getSharedPreferences(USER_SETTINGS, Context.MODE_PRIVATE);
        menuType = sharedPreferences.getInt(USER_SETTINGS_LAUNCHER_TYPE, 0);

        if (menuType == 0) {
            setContentView(R.layout.one_screen);
        } else if (menuType == 1) {
            setContentView(R.layout.classic_screen_home);
        }
    }

    private void loadApps() {
        manager = getPackageManager();
        appList = new ArrayList<>();

        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_LAUNCHER);

        List<ResolveInfo> availableActivities = manager.queryIntentActivities(intent, 0);
        for (ResolveInfo ri : availableActivities) {
            AppDetail app = new AppDetail();
            app.label = ri.loadLabel(manager);
            app.name = ri.activityInfo.packageName;
            app.icon = ri.activityInfo.loadIcon(manager);
            appList.add(app);
        }
    }




}
