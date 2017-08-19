package mingorto.launcher;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.annotations.SerializedName;
import com.google.gson.reflect.TypeToken;

import org.askerov.dynamicgrid.BaseDynamicGridAdapter;
import org.askerov.dynamicgrid.DynamicGridView;
import org.json.JSONArray;

import java.lang.reflect.Type;
import java.sql.SQLOutput;
import java.util.ArrayList;
import java.util.List;

import mingorto.launcher.ClassicScreen.ClassicAppMenu;
import mingorto.launcher.SettingScreens.SettingsList;

import static android.content.ContentValues.TAG;
import static mingorto.launcher.FirstFirstRow.FINAL_SETTING;
import static mingorto.launcher.FirstFirstRow.USER_SETTINGS;
import static mingorto.launcher.FirstFirstRow.USER_SETTINGS_LAUNCHER_TYPE;

public class MainMenu extends Activity implements ExclusionStrategy {
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private PackageManager manager;

    private static List<AppDetail> appList;

    public static final String TAG = "ARRAY_LIST";
    public static final String CONDITION = "Activity condition : ";
    public static final String ONE_SCREEN_ACTIVITY = "ONE_SCREEN_ACTIVITY";
    public static final String CLASSIC_HOME = "CLASSIC_HOME";
    public static final String CLASSIC_MAIN = "CLASSIC_MAIN";

    public DynamicGridView grid;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.v(CONDITION, "On Create");

        appList = getArrayList();
        if (appList == null || appList.isEmpty()) {
            Log.v("LIST CONDITION", "OUT");
            loadApps();
        } else {
            Log.v("LIST CONDITION", "FULL");
            Log.v("List CONDITION", String.valueOf(appList));
        }
    }

    @Override
    public void onStart() {
        super.onStart();

        if (getSharedPreferences(USER_SETTINGS, Context.MODE_PRIVATE).getInt(FINAL_SETTING, 0) == 0) {
            getSharedPreferences(USER_SETTINGS, Context.MODE_PRIVATE).edit().putInt(FINAL_SETTING, R.layout.one_screen).apply();
        }

        setContentView(getSharedPreferences(USER_SETTINGS, Context.MODE_PRIVATE).getInt(FINAL_SETTING, 0));

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
        if (getSharedPreferences(USER_SETTINGS, Context.MODE_PRIVATE).getInt(USER_SETTINGS_LAUNCHER_TYPE, 0) == 0) {
            System.out.println("loadList menu type 0");
            grid = (DynamicGridView) findViewById(R.id.apps_list);
        } else {
            grid = (DynamicGridView) findViewById(R.id.home_screen);
        }
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
        SharedPreferences sharedPreferences = this.getSharedPreferences(ONE_SCREEN_ACTIVITY, Context.MODE_PRIVATE);
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
        editor.putString(ONE_SCREEN_ACTIVITY, json);
        editor.apply();
    }

    public List<AppDetail> getArrayList() {
        SharedPreferences sharedPreferences = this.getSharedPreferences(ONE_SCREEN_ACTIVITY, Context.MODE_PRIVATE);
        Exclude ex = new Exclude();
        Gson gson = new GsonBuilder().addDeserializationExclusionStrategy(ex).addSerializationExclusionStrategy(ex).create();
        String json = sharedPreferences.getString(ONE_SCREEN_ACTIVITY, null);

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
                        System.out.println("JSON app label " + appInfo.label);
                        System.out.println("List name " + ri.loadLabel(manager));

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
        // TODO Auto-generated method stub
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
            // TODO Auto-generated method stub
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