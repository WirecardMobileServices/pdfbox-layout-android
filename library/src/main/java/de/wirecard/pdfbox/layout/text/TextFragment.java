package de.wirecard.pdfbox.layout.text;

import android.support.annotation.ColorInt;

/**
 * A text fragment describes a drawable piece of text associated with a font,
 * size and color.
 */
public interface TextFragment extends Area {

    /**
     * @return the text.
     */
    String getText();

    /**
     * @return the font and size to use to draw the text.
     */
    FontDescriptor getFontDescriptor();

    /**
     * @return the color to use to draw the text.
     */
    @ColorInt
    Integer getColor();
}
