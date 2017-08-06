package mingorto.launcher;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;

import mingorto.launcher.ClassicScreen.ClassicMainScreen;
import mingorto.launcher.OneScreen.OneScreen;

import static mingorto.launcher.SettingScreens.FirstRow.FirstFirstRow.USER_SETTINGS;
import static mingorto.launcher.SettingScreens.FirstRow.FirstFirstRow.USER_SETTINGS_LAUNCHER_TYPE;

public class Switcher extends Activity {
    private SharedPreferences settings;
    private int menuType;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        settings = getSharedPreferences(USER_SETTINGS, Context.MODE_PRIVATE);
        menuType = settings.getInt(USER_SETTINGS_LAUNCHER_TYPE, 0);
        Intent intent;

        if (menuType == 0) {
            intent = new Intent(Switcher.this, ClassicMainScreen.class);
        } else {
            intent = new Intent(Switcher.this, OneScreen.class);
        }

        Log.d("AAAAAAAAAAAAAAAAA", "AAAAAAA");

        startActivity(intent);
    }
}
