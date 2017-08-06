package mingorto.launcher;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import mingorto.launcher.ClassicScreen.ClassicMainScreen;
import mingorto.launcher.OneScreen.OneScreen;

import static mingorto.launcher.SettingScreens.FirstRow.FirstFirstRow.USER_SETTINGS;
import static mingorto.launcher.SettingScreens.FirstRow.FirstFirstRow.USER_SETTINGS_LAUNCHER_TYPE;

public class Switcher extends AppCompatActivity {
    private SharedPreferences settings;
    private int menuType;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.bitch);
        settings = getSharedPreferences(USER_SETTINGS, Context.MODE_PRIVATE);
        menuType = settings.getInt(USER_SETTINGS_LAUNCHER_TYPE, 0);

        Intent i;
        Log.d("DFFGFGDFG", String.valueOf(menuType));
        if (menuType == 1) {
            i = new Intent(Switcher.this, ClassicMainScreen.class);
            Log.d("AAAAAAAAAAAAAAAAA", "AAAAAAA");
        } else {
            i = new Intent(Switcher.this, OneScreen.class);
            Log.d("AAAAAAAAAAAAAAAAA", "AAAAAAA");
        }

        i.addCategory(Intent.CATEGORY_LAUNCHER);
        i.addCategory(Intent.CATEGORY_HOME);
        i.addCategory(Intent.CATEGORY_DEFAULT);

        startActivity(i);

        finish();

    }
}
