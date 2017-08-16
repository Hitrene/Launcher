package mingorto.launcher;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import org.askerov.dynamicgrid.BaseDynamicGridAdapter;

import java.util.List;

public class GridViewAdapter extends BaseDynamicGridAdapter {
    private List<AppDetail> appList;

    public GridViewAdapter(Context context, List<AppDetail> items, int columnCount) {
        super(context, items, columnCount);
        this.appList = items;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_item, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.build(appList.get(position).label.toString(), appList.get(position).icon);
        return convertView;
    }

    private class ViewHolder {
        private TextView letterText;
        private ImageButton imageButton;

        private ViewHolder(View view) {
            letterText = (TextView) view.findViewById(R.id.item_app_label);
            imageButton = (ImageButton) view.findViewById(R.id.item_app_icon);
        }

        void build(String title, Drawable drawable) {
            letterText.setText(title);
            imageButton.setImageDrawable(drawable);


        }
    }
}
