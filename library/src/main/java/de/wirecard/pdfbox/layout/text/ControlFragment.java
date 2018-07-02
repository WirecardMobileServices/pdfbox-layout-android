package de.wirecard.pdfbox.layout.text;

import android.graphics.Color;
import android.support.annotation.ColorInt;

import org.apache.pdfbox.pdmodel.font.PDType1Font;

import java.io.IOException;

/**
 * A control fragment has no drawable representation but is meant to control the
 * text rendering.
 */
public class ControlFragment implements TextFragment {

    protected final static FontDescriptor DEFAULT_FONT_DESCRIPTOR = new FontDescriptor(
            PDType1Font.HELVETICA, 11);

    private String name;
    private String text;
    private FontDescriptor fontDescriptor;
    private Integer color;

    protected ControlFragment(final String text,
                              final FontDescriptor fontDescriptor) {
        this(null, text, fontDescriptor, Color.BLACK);
    }

    protected ControlFragment(final String name, final String text,
                              final FontDescriptor fontDescriptor, @ColorInt final int color) {
        this.name = name;
        if (this.name == null) {
            this.name = getClass().getSimpleName();
        }
        this.text = text;
        this.fontDescriptor = fontDescriptor;
        this.color = color;
    }

    @Override
    public float getWidth() throws IOException {
        return 0;
    }

    @Override
    public float getHeight() {
        return getFontDescriptor() == null ? 0 : getFontDescriptor().getSize();
    }

    @Override
    public FontDescriptor getFontDescriptor() {
        return fontDescriptor;
    }

    protected String getName() {
        return name;
    }

    @Override
    public String getText() {
        return text;
    }

    @Override
    public Integer getColor() {
        return color;
    }

    @Override
    public String toString() {
        return "ControlFragment [" + name + "]";
    }

}
