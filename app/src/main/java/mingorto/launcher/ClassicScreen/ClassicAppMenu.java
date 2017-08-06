package mingorto.launcher.ClassicScreen;

import android.app.Activity;
import android.content.Intent;
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
import mingorto.launcher.R;
import mingorto.launcher.SettingScreens.SettingsList;

public class ClassicAppMenu extends Activity {
    private PackageManager manager;
    private List<AppDetail> apps;
    private GridView grid;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.classic_app_menu);

        loadApps();
        loadListView();
        Log.d("Current screen: ", "You are in classic app List");
    }

    private void loadApps() {
        manager = getPackageManager();
        apps = new ArrayList<AppDetail>();

        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_LAUNCHER);

        List<ResolveInfo> availableActivities = manager.queryIntentActivities(intent, 0);
        for (ResolveInfo ri : availableActivities) {
            AppDetail app = new AppDetail();
            app.label = ri.loadLabel(manager);
            app.name = ri.loadLabel(manager);
            app.name = ri.activityInfo.packageName;
            app.icon = ri.activityInfo.loadIcon(manager);
            apps.add(app);
        }
    }

    private void loadListView() {
        grid = (GridView) findViewById(R.id.classic_menu);
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
                            i = new Intent(ClassicAppMenu.this, SettingsList.class);
                        }
                        ClassicAppMenu.this.startActivity(i);
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
}
