package de.wirecard.pdfbox.layout.util;

import android.graphics.Color;
import android.support.annotation.ColorInt;

import com.tom_roush.pdfbox.pdmodel.graphics.color.PDColor;
import com.tom_roush.pdfbox.pdmodel.graphics.color.PDDeviceRGB;

public class ColorConverter {

    public static PDColor convert(@ColorInt int color) {
        float[] components = new float[]{Math.abs(Color.red(color)) / 255f, Math.abs(Color.green(color)) / 255f, Math.abs(Color.blue(color)) / 255f};
        return new PDColor(components, PDDeviceRGB.INSTANCE);
    }

}
