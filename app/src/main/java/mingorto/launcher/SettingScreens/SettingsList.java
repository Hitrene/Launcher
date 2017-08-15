package mingorto.launcher.SettingScreens;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;

import mingorto.launcher.R;
import mingorto.launcher.FirstFirstRow;

public class SettingsList extends Activity {
    ListView listView;
    String[] settingsText = {"Point 1", "Point 2", "Point 3", "Point 4"};
    Integer[] settingsImages = {R.drawable.image_001_team, R.drawable.image_002_handshake, R.drawable.image_003_bar_chart, R.drawable.image_004_startup};
    ArrayList<Object> activities = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_list);

        addActivities();

        ImgTextAdapter adapter = new ImgTextAdapter(this, settingsText, settingsImages);
        listView = (ListView) findViewById(R.id.settings_list);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent i = new Intent(SettingsList.this, (Class) activities.get(position));
                startActivity(i);
            }
        });
    }

    public void addActivities() {
        activities.add(FirstFirstRow.class);
        activities.add(SecondRow.class);
    }
}
