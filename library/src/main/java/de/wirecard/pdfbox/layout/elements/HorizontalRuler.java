package de.wirecard.pdfbox.layout.elements;

import android.support.annotation.ColorInt;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPageContentStream;

import java.io.IOException;

import de.wirecard.pdfbox.layout.shape.Stroke;
import de.wirecard.pdfbox.layout.text.DrawListener;
import de.wirecard.pdfbox.layout.text.Position;
import de.wirecard.pdfbox.layout.text.WidthRespecting;
import de.wirecard.pdfbox.layout.util.ColorConverter;

/**
 * A horizontal ruler that adjust its width to the given
 * {@link WidthRespecting#getMaxWidth() max width}.
 */
public class HorizontalRuler implements Drawable, Element, WidthRespecting {

    private Stroke stroke;
    @ColorInt
    private Integer color;
    private float maxWidth = -1f;

    public HorizontalRuler(Stroke stroke, int color) {
        super();
        this.stroke = stroke;
        this.color = color;
    }

    /**
     * @return the stroke to draw the ruler with.
     */
    public Stroke getStroke() {
        return stroke;
    }

    /**
     * @return the color to draw the ruler with.
     */
    public int getColor() {
        return color;
    }

    @Override
    public float getMaxWidth() {
        return maxWidth;
    }

    @Override
    public void setMaxWidth(float maxWidth) {
        this.maxWidth = maxWidth;
    }

    @Override
    public float getWidth() {
        return getMaxWidth();
    }

    @Override
    public float getHeight() {
        if (getStroke() == null) {
            return 0f;
        }
        return getStroke().getLineWidth();
    }

    @Override
    public Position getAbsolutePosition() {
        return null;
    }

    @Override
    public void draw(PDDocument pdDocument, PDPageContentStream contentStream,
                     Position upperLeft, DrawListener drawListener) throws IOException {
        if (color != null) {
            contentStream.setStrokingColor(ColorConverter.convert(color));
        }
        if (getStroke() != null) {
            getStroke().applyTo(contentStream);
            float x = upperLeft.getX();
            float y = upperLeft.getY() - getStroke().getLineWidth() / 2;
            contentStream.moveTo(x, y);
            contentStream.lineTo(x + getWidth(), y);
            contentStream.stroke();
        }
        if (drawListener != null) {
            drawListener.drawn(this, upperLeft, getWidth(), getHeight());
        }
    }

    @Override
    public Drawable removeLeadingEmptyVerticalSpace() {
        return this;
    }

}
