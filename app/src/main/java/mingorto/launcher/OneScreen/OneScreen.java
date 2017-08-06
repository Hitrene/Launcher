package mingorto.launcher.OneScreen;

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

import java.util.ArrayList;
import java.util.List;

import mingorto.launcher.AppDetail;
import mingorto.launcher.ClassicScreen.ClassicMainScreen;
import mingorto.launcher.R;
import mingorto.launcher.SettingScreens.SettingsList;

import static mingorto.launcher.SettingScreens.FirstRow.FirstFirstRow.USER_SETTINGS;
import static mingorto.launcher.SettingScreens.FirstRow.FirstFirstRow.USER_SETTINGS_LAUNCHER_TYPE;

public class OneScreen extends Activity {
    private static PackageManager manager;
    private List<AppDetail> apps;
    private GridView grid;
    private SharedPreferences settings;
    int menuType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        settings = getSharedPreferences(USER_SETTINGS, Context.MODE_PRIVATE);
        menuType = settings.getInt(USER_SETTINGS_LAUNCHER_TYPE, 0);

        if (menuType == 0) {
            setContentView(R.layout.one_screen);
            loadApps();
            loadListView();
        } else if (menuType == 1) {
            setContentView(R.layout.classic_screen_home);
        }
    }

    private void loadApps() {
        manager = getPackageManager();
        apps = new ArrayList<AppDetail>();

        Intent i = new Intent(Intent.ACTION_MAIN);
        i.addCategory(Intent.CATEGORY_LAUNCHER);

        List<ResolveInfo> availableActivities = manager.queryIntentActivities(i, 0);
        for (ResolveInfo ri : availableActivities) {
            AppDetail app = new AppDetail();
            app.label = ri.loadLabel(manager);
            app.name = ri.activityInfo.packageName;
            app.icon = ri.activityInfo.loadIcon(manager);
            apps.add(app);
        }
    }

    private void loadListView() {
        grid = (GridView) findViewById(R.id.apps_list);
        ArrayAdapter<AppDetail> adapter = new ArrayAdapter<AppDetail>(this, R.layout.list_item, apps) {
            @Override
            public View getView(final int position, View convertView, ViewGroup parent) {
                if (convertView == null) {
                    convertView = getLayoutInflater().inflate(R.layout.list_item, null);
                }

                ImageButton appIcon = (ImageButton) convertView.findViewById(R.id.item_app_icon);
                appIcon.setImageDrawable(apps.get(position).icon);

                appIcon.setOnClickListener(new View.OnClickListener() { //Short action
                    @Override
                    public void onClick(View v) { //Установка обчного нажатия
                        Intent i = manager.getLaunchIntentForPackage(apps.get(position).name.toString());
                        if (i.getPackage().equals("mingorto.launcher")) { //Если кликнул на иконку настроек
                            Log.v("Name of Activity", i.getPackage());
                            i = new Intent(OneScreen.this, SettingsList.class);
                        }
                        OneScreen.this.startActivity(i);
                    }
                });

                appIcon.setOnLongClickListener(new View.OnLongClickListener() { //Long action
                    @Override
                    public boolean onLongClick(View v) { //Установка долгого нажатия на иконку
                        Intent intent = new Intent(Intent.ACTION_UNINSTALL_PACKAGE);
                        intent.setData(Uri.parse("package:" + apps.get(position).name));
                        intent.putExtra(Intent.EXTRA_RETURN_RESULT, true);
                        startActivityForResult(intent, 1);
                        return false;
                    }
                });

                TextView appLabel = (TextView) convertView.findViewById(R.id.item_app_label);
                appLabel.setText(apps.get(position).label);

                return convertView;
            }
        };
        grid.setAdapter(adapter);
    }

    public void show_alt_grid(View v) {
        v.setClickable(false);
        v.setVisibility(View.GONE);
        Intent i = new Intent(OneScreen.this, ClassicMainScreen.class);
        startActivity(i);
    }
}
