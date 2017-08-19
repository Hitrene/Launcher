package mingorto.launcher;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.TextView;

import com.google.gson.InstanceCreator;
import com.google.gson.annotations.SerializedName;

import java.lang.reflect.Type;

public class AppDetail {
    @SerializedName("label")
    public CharSequence label;
    @SerializedName("name")
    public CharSequence name;
    @SerializedName("icon")
    public Drawable icon;

    public CharSequence getLabel() {
        return label;
    }

    public void setLabel(CharSequence label) {
        this.label = label;
    }

    public CharSequence getName() {
        return name;
    }

    public void setName(CharSequence name) {
        this.name = name;
    }

    public Drawable getIcon() {
        return icon;
    }

    public void setIcon(Drawable icon) {
        this.icon = icon;
    }
}
