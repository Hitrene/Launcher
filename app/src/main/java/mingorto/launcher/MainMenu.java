package mingorto.launcher;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.annotations.SerializedName;
import com.google.gson.reflect.TypeToken;

import org.askerov.dynamicgrid.DynamicGridView;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import mingorto.launcher.ClassicScreen.ClassicAppMenu;
import mingorto.launcher.SettingScreens.SettingsList;

import static mingorto.launcher.FirstFirstRow.FINAL_SETTING;
import static mingorto.launcher.FirstFirstRow.USER_SETTINGS;
import static mingorto.launcher.FirstFirstRow.USER_SETTINGS_LAUNCHER_TYPE;

public class MainMenu extends Activity implements ExclusionStrategy {
    private PackageManager manager;

    private static List<AppDetail> appList;

    public static final String TAG = "ARRAY_LIST";
    public static final String CONDITION = "Activity condition : ";
    public static final String ONE_SCREEN_ACTIVITY = "ONE_SCREEN_ACTIVITY";
    public static final String CLASSIC_HOME = "CLASSIC_HOME";
    public static final String CLASSIC_MAIN = "CLASSIC_MAIN";
    private int menuType;
    private String userSettings;

    public DynamicGridView grid;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.v(CONDITION, "On Create");


    }

    @Override
    public void onStart() {
        super.onStart();

        if (getSharedPreferences(USER_SETTINGS, Context.MODE_PRIVATE).getInt(FINAL_SETTING, 0) == 0) {
            getSharedPreferences(USER_SETTINGS, Context.MODE_PRIVATE).edit().putInt(FINAL_SETTING, R.layout.one_screen).apply();
        }

        menuType = getSharedPreferences(USER_SETTINGS, Context.MODE_PRIVATE).getInt(FINAL_SETTING, 0);
        setContentView(menuType);

        if (getSharedPreferences(USER_SETTINGS, Context.MODE_PRIVATE).getInt(USER_SETTINGS_LAUNCHER_TYPE, 0) == 0) {
            userSettings = ONE_SCREEN_ACTIVITY;
            grid = (DynamicGridView) findViewById(R.id.apps_list);
        } else {
            userSettings = CLASSIC_HOME;
            grid = (DynamicGridView) findViewById(R.id.home_screen);
        }

        appList = getArrayList();
        if (appList == null || appList.isEmpty()) {
            Log.v("LIST CONDITION", "OUT");
            loadApps();
        } else {
            Log.v("LIST CONDITION", "FULL");
            Log.v("List CONDITION", String.valueOf(appList));
        }

        loadListView();
        Log.v(CONDITION, "On Start");
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
        GridViewAdapter adapter = new GridViewAdapter(this, appList, 3) {
            @Override
            public View getView(final int position, View convertView, ViewGroup parent) {
                if (convertView == null) {
                    convertView = getLayoutInflater().inflate(R.layout.list_item, null);
                }

                ImageButton appIcon = (ImageButton) convertView.findViewById(R.id.item_app_icon);
                appIcon.setImageDrawable(appList.get(position).icon);

                TextView appLabel = (TextView) convertView.findViewById(R.id.item_app_label);
                appLabel.setText(appList.get(position).label);

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
                        /*Intent intent = new Intent(Intent.ACTION_UNINSTALL_PACKAGE);
                        intent.setData(Uri.parse("package:" + appList.get(position).name));
                        intent.putExtra(Intent.EXTRA_RETURN_RESULT, true);
                        startActivityForResult(intent, 1);*/
                        grid.startEditMode(position);
                        return true;
                    }
                });

                grid.setOnDragListener(new DynamicGridView.OnDragListener() {
                    @Override
                    public void onDragStarted(int position) {
                        Log.d(TAG, "drag started at position " + position);
                    }

                    @Override
                    public void onDragPositionsChanged(int oldPosition, int newPosition) {
                        Log.d(TAG, String.format("drag item position changed from %d to %d", oldPosition, newPosition));
                        AppDetail oldApp = appList.get(oldPosition);
                        AppDetail newApp = appList.get(newPosition);
                        appList.set(newPosition, oldApp);
                        appList.set(oldPosition, newApp);
                    }
                });

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
        if (grid.isEditMode()) {
            grid.stopEditMode();
            saveArrayList(appList);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public void onStop() {
        saveArrayList(appList);
        Log.v(CONDITION, "On Stop");
        super.onStop();
    }

    @Override
    public void onRestart() {
        Log.v(CONDITION, "On Restart");
/*        saveArrayList(MainMenu.this, appList);*/
        super.onRestart();
    }

    @Override
    public void onDestroy() {
        saveArrayList(appList);
        Log.v(CONDITION, "On Destroy");
        super.onDestroy();
    }

    @Override
    public void onPause() {
        saveArrayList(appList);
        Log.v(CONDITION, "On Pause");
        super.onPause();
    }

    public void saveArrayList(List<AppDetail> list) {
        SharedPreferences sharedPreferences = this.getSharedPreferences(userSettings, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Exclude ex = new Exclude();
        Gson gson = new GsonBuilder().addDeserializationExclusionStrategy(ex).addSerializationExclusionStrategy(ex).create();
        manager = getPackageManager();

        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_LAUNCHER);

        ArrayList<String> list1 = new ArrayList<>();
        for (AppDetail app : appList) {
            list1.add(app.label.toString());
        }
        String json = gson.toJson(list1);
        System.out.println(json);
        editor.putString(userSettings, json);
        editor.apply();
    }

    public List<AppDetail> getArrayList() {
        SharedPreferences sharedPreferences = this.getSharedPreferences(userSettings, Context.MODE_PRIVATE);
        Exclude ex = new Exclude();
        Gson gson = new GsonBuilder().addDeserializationExclusionStrategy(ex).addSerializationExclusionStrategy(ex).create();
        String json = sharedPreferences.getString(userSettings, null);

        System.out.println(json);

        Type type = new TypeToken<ArrayList<String>>() {}.getType();
        manager = getPackageManager();

        ArrayList<String> gsonList = gson.fromJson(json, type);

        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_LAUNCHER);

        ArrayList<AppDetail> resultList = new ArrayList<>();

        List<ResolveInfo> availableActivities = manager.queryIntentActivities(intent, 0);
        if (gsonList != null) {
            System.out.println("WTF");
            for (String app : gsonList) {
                for (ResolveInfo ri : availableActivities) {
                    if (app.equals(ri.loadLabel(manager))) {
                        AppDetail appInfo = new AppDetail();

                        appInfo.label = ri.loadLabel(manager);
                        appInfo.name = ri.activityInfo.packageName;
                        appInfo.icon = ri.activityInfo.loadIcon(manager);
                        resultList.add(appInfo);
                    }
                }
            }
        }

        System.out.println(resultList);
        return resultList;
    }

    @Override
    public boolean shouldSkipClass(Class<?> arg0) {
        return false;
    }

    @Override
    public boolean shouldSkipField(FieldAttributes field) {
        SerializedName ns = field.getAnnotation(SerializedName.class);
        if (ns != null)
            return false;
        return true;
    }

    class Exclude implements ExclusionStrategy {
        @Override
        public boolean shouldSkipClass(Class<?> arg0) {
            return false;
        }

        @Override
        public boolean shouldSkipField(FieldAttributes field) {
            SerializedName ns = field.getAnnotation(SerializedName.class);
            if (ns != null)
                return false;
            return true;
        }
    }
}