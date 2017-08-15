package mingorto.launcher;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.TextView;

import java.sql.SQLOutput;
import java.util.ArrayList;
import java.util.List;

import mingorto.launcher.ClassicScreen.ClassicAppMenu;
import mingorto.launcher.SettingScreens.SettingsList;

import static mingorto.launcher.FirstFirstRow.FINAL_SETTING;
import static mingorto.launcher.FirstFirstRow.USER_SETTINGS;
import static mingorto.launcher.FirstFirstRow.USER_SETTINGS_LAUNCHER_TYPE;

public class MainMenu extends Activity {
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private PackageManager manager;
    private List<AppDetail> appList;

    private GridView grid;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onStart() {
        super.onStart();

        if (getSharedPreferences(USER_SETTINGS, Context.MODE_PRIVATE).getInt(FINAL_SETTING, 0) == 0)
            getSharedPreferences(USER_SETTINGS, Context.MODE_PRIVATE).edit().putInt(FINAL_SETTING, R.layout.one_screen).apply();

        setContentView(getSharedPreferences(USER_SETTINGS, Context.MODE_PRIVATE).getInt(FINAL_SETTING, 0));
        loadApps();
        loadListView();
    }

    private void loadApps() {
        manager = getPackageManager();
        appList = new ArrayList<AppDetail>();

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

    private void loadListView() {
        if (getSharedPreferences(USER_SETTINGS, Context.MODE_PRIVATE).getInt(USER_SETTINGS_LAUNCHER_TYPE, 0) == 0) {
            System.out.println("loadList menu type 0");
            grid = (GridView) findViewById(R.id.apps_list);
        } else {
            grid = (GridView) findViewById(R.id.home_screen);
        }
        ArrayAdapter<AppDetail> adapter = new ArrayAdapter<AppDetail>(this, R.layout.list_item, appList) {
            @Override
            public View getView(final int position, View convertView, ViewGroup parent) {
                if (convertView == null) {
                    convertView = getLayoutInflater().inflate(R.layout.list_item, null);
                }

                ImageButton appIcon = (ImageButton) convertView.findViewById(R.id.item_app_icon);
                appIcon.setImageDrawable(appList.get(position).icon);

                appIcon.setOnClickListener(new View.OnClickListener() { //Short action
                    @Override
                    public void onClick(View v) { //Установка обчного нажатия
                        Intent i = manager.getLaunchIntentForPackage(appList.get(position).name.toString());
                        if (i.getPackage().equals("mingorto.launcher")) { //Если кликнул на иконку настроек
                            Log.v("Name of Activity", i.getPackage());
                            i = new Intent(MainMenu.this, SettingsList.class);
                        }
                        MainMenu.this.startActivity(i);
                    }
                });

                appIcon.setOnLongClickListener(new View.OnLongClickListener() { //Long action
                    @Override
                    public boolean onLongClick(View v) { //Установка долгого нажатия на иконку
                        Intent intent = new Intent(Intent.ACTION_UNINSTALL_PACKAGE);
                        intent.setData(Uri.parse("package:" + appList.get(position).name));
                        intent.putExtra(Intent.EXTRA_RETURN_RESULT, true);
                        startActivityForResult(intent, 1);
                        return false;
                    }
                });

                TextView appLabel = (TextView) convertView.findViewById(R.id.item_app_label);
                appLabel.setText(appList.get(position).label);

                return convertView;
            }
        };
        grid.setAdapter(adapter);
    }

    public void show_alt_grid(View v) {
        Intent i = new Intent(MainMenu.this, ClassicAppMenu.class);
        startActivity(i);
        Log.d("Current screen :", "Button on main menu has been pressed");
    }

    @Override
    public void onBackPressed() {

    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void onRestart() {
        super.onRestart();
    }
}
