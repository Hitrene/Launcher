package mingorto.launcher.SettingScreens;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import mingorto.launcher.R;

public class FirstRow extends Activity {
    public static final String USER_SETTINGS = "user_settings";
    public static final String USER_SETTINGS_LAUNCHER_TYPE = "launcher_type";
    private final String[] launcherType = {"Один экран", "Классический"};
    private SharedPreferences settings;
    private SharedPreferences.Editor editor;
    private AlertDialog.Builder alertDialog;
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.first_row);

        context = FirstRow.this;
        settings = getSharedPreferences(USER_SETTINGS, Context.MODE_PRIVATE);

        Button button = (Button) findViewById(R.id.defLauncher);
        button.setOnClickListener(new View.OnClickListener() { //Настройка диалогового окна для выбора типа отображения рабочего стола
            public void onClick(View v) {
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
                        editor.apply();
                        Log.v("New launcher type : ", String.valueOf(settings.getInt(USER_SETTINGS_LAUNCHER_TYPE, 0)));
                    }
                });
                alertDialog.create();
                alertDialog.show();
            }
        });
    }
}
