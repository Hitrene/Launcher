package mingorto.launcher;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.jar.Manifest;


public class FirstFirstRow extends Activity {
    public static final String USER_SETTINGS = "user_settings";
    public static final String USER_SETTINGS_LAUNCHER_TYPE = "launcher_type";
    public static final String FINAL_SETTING = "FINAL_SETTING";
    public static final String[] launcherType = {"Один экран", "Классический"};
    private SharedPreferences settings;
    private android.content.SharedPreferences.Editor editor;
    private AlertDialog.Builder alertDialog;
    private Context context;
    private MainMenu mainMenu;

    ListView listView;
    private String[] list = {"Тип главного меню", "Лаунчер по умолчанию", "Порядок приложений в списке"};
    ArrayList<Object> activities = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.first_first_row);

        listView = (ListView) findViewById(R.id.secondSettingsList);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, list);
        addActivities();

        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent i = new Intent(FirstFirstRow.this, (Class) activities.get(position));
                if (position == 0)
                    chooseAppType();
                else
                    startActivity(i);
            }
        });
    }

    public void addActivities() {
        activities.add(FirstFirstRow.class);
    }

    public void chooseAppType() {
        context = FirstFirstRow.this;
        settings = getSharedPreferences(USER_SETTINGS, Context.MODE_PRIVATE);

        alertDialog = new AlertDialog.Builder(context);
        alertDialog.setTitle("Тип меню приложений").setCancelable(false)
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                }).setSingleChoiceItems(launcherType, settings.getInt(USER_SETTINGS_LAUNCHER_TYPE, 0), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        editor = settings.edit();
                        editor.putInt(USER_SETTINGS_LAUNCHER_TYPE, which);

                        if (which == 0) {
                            editor.putInt(FINAL_SETTING, R.layout.one_screen);
                        } else
                            editor.putInt(FINAL_SETTING, R.layout.classic_screen_home);

                        editor.apply();
                        Log.v("New launcher type : ", String.valueOf(settings.getInt(USER_SETTINGS_LAUNCHER_TYPE, 0)));
//                        restartApp();
                    }
                });
        alertDialog.create();
        alertDialog.show();
    }

/*    public void restartApp() {
        AlertDialog.Builder restartDialog = new AlertDialog.Builder(FirstFirstRow.this);
        restartDialog.setTitle("Лаунчер будет перезагружен").setCancelable(false).setPositiveButton("Ok" , new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                mainMenu = new MainMenu();
                mainMenu.onRestart();
            }
        }).setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int which) {
                dialogInterface.cancel();
            }
        });
        restartDialog.create();
        restartDialog.show();
    }*/
}

