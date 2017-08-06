package mingorto.launcher.ClassicScreen;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import mingorto.launcher.AppDetail;
import mingorto.launcher.R;
import mingorto.launcher.SettingScreens.SettingsList;

import static mingorto.launcher.SettingScreens.FirstRow.FirstFirstRow.USER_SETTINGS;
import static mingorto.launcher.SettingScreens.FirstRow.FirstFirstRow.USER_SETTINGS_LAUNCHER_TYPE;

public class ClassicMainScreen extends Activity {
    private GridView grid;
    private SharedPreferences settings;
    private int menuType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.classic_screen_home);

        settings = getSharedPreferences(USER_SETTINGS, Context.MODE_PRIVATE);
        menuType = settings.getInt(USER_SETTINGS_LAUNCHER_TYPE, 0);

        Log.d("Current screen :", "You are on ClassicMainScreen");

        if (menuType == 0) {
            setContentView(R.layout.one_screen);
        } else if (menuType == 1) {
            setContentView(R.layout.classic_screen_home);
        }
    }

    public void show_alt_grid(View v) {
        Intent i = new Intent(ClassicMainScreen.this, ClassicAppMenu.class);
        i.addCategory(Intent.ACTION_MAIN);
        i.addCategory(Intent.CATEGORY_LAUNCHER);
        startActivity(i);
        Log.d("Current screen :", "Button on main menu has been pressed");
    }

    @Override
    public void onBackPressed() {

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        return super.onKeyDown(keyCode, event);
    }
}
