package mingorto.launcher;

import android.app.Activity;
import android.media.session.PlaybackState;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

public class SettingsList extends Activity {
    ListView listView;
    String[] settingsText = {"Point 1", "Point 2", "Point 3", "Point 4"};
    Integer[] settingsImages = {R.drawable.image_001_team, R.drawable.image_002_handshake, R.drawable.image_003_bar_chart, R.drawable.image_004_startup};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_list);

        ImgTextAdapter adapter = new ImgTextAdapter(this, settingsText, settingsImages);
        listView = (ListView) findViewById(R.id.settings_list);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                // TODO Auto-generated method stub
                String Slecteditem = settingsText[+position];
                Toast.makeText(getApplicationContext(), Slecteditem, Toast.LENGTH_SHORT).show();

            }
        });
    }
}
