package mingorto.launcher.SettingScreens;

import android.app.Activity;
import android.content.pm.PackageManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import mingorto.launcher.R;

public class ImgTextAdapter extends ArrayAdapter<String> {
    private PackageManager manager;
    private final Activity context;
    private final String[] text;
    private final Integer[] img;

    public ImgTextAdapter(Activity context, String[] text, Integer[] img) {
        super(context, R.layout.settings_item_img, text);
        this.context = context;
        this.text = text;
        this.img = img;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View rowView = inflater.inflate(R.layout.settings_item_img, null, true);

        TextView txt = (TextView) rowView.findViewById(R.id.setting_text);
        ImageView imageView = (ImageView) rowView.findViewById(R.id.setting_image);

        txt.setText(text[position]);
        imageView.setImageResource(img[position]);

        return rowView;
    }
}