package de.wirecard.pdfbox.layout.elements;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;

import java.io.IOException;

import de.wirecard.pdfbox.layout.text.DrawListener;
import de.wirecard.pdfbox.layout.text.Position;
import de.wirecard.pdfbox.layout.text.WidthRespecting;

public class ImageElement implements Element, Drawable, Dividable,
        WidthRespecting {

    /**
     * Set this to {@link #setWidth(float)} resp. {@link #setHeight(float)}
     * (usually both) in order to respect the {@link WidthRespecting width}.
     */
    public final static float SCALE_TO_RESPECT_WIDTH = -1f;

    private PDImageXObject image;
    private float width;
    private float height;
    private float maxWidth = -1;
    private Position absolutePosition;

    public ImageElement(final PDImageXObject image) {
        this.image = image;
        this.width = image.getWidth();
        this.height = image.getHeight();
    }

    @Override
    public float getWidth() {
        if (width == SCALE_TO_RESPECT_WIDTH) {
            if (getMaxWidth() > 0 && image.getWidth() > getMaxWidth()) {
                return getMaxWidth();
            }
            return image.getWidth();
        }
        return width;
    }

    /**
     * Sets the width. Default is the image width. Set to
     * {@link #SCALE_TO_RESPECT_WIDTH} in order to let the image
     * {@link WidthRespecting respect any given width}.
     *
     * @param width the width to use.
     */
    public void setWidth(float width) {
        this.width = width;
    }

    @Override
    public float getHeight() {
        if (height == SCALE_TO_RESPECT_WIDTH) {
            if (getMaxWidth() > 0 && image.getWidth() > getMaxWidth()) {
                return getMaxWidth() / (float) image.getWidth()
                        * (float) image.getHeight();
            }
            return image.getHeight();
        }
        return height;
    }

    /**
     * Sets the height. Default is the image height. Set to
     * {@link #SCALE_TO_RESPECT_WIDTH} in order to let the image
     * {@link WidthRespecting respect any given width}. Usually this makes only
     * sense if you also set the width to {@link #SCALE_TO_RESPECT_WIDTH}.
     *
     * @param height the height to use.
     */
    public void setHeight(float height) {
        this.height = height;
    }

    @Override
    public Divided divide(float remainingHeight, float nextPageHeight)
            throws IOException {
        if (getHeight() <= nextPageHeight) {
            return new Divided(new VerticalSpacer(remainingHeight), this);
        }
        return new Cutter(this).divide(remainingHeight, nextPageHeight);
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
    public Position getAbsolutePosition() {
        return absolutePosition;
    }

    /**
     * Sets the absolute position to render at.
     *
     * @param absolutePosition the absolute position.
     */
    public void setAbsolutePosition(Position absolutePosition) {
        this.absolutePosition = absolutePosition;
    }

    @Override
    public void draw(PDDocument pdDocument, PDPageContentStream contentStream, Position upperLeft, DrawListener drawListener) throws IOException {
        contentStream.drawImage(image, upperLeft.getX(), upperLeft.getY() - image.getHeight());
        if (drawListener != null) {
            drawListener.drawn(this, upperLeft, getWidth(), getHeight());
        }
    }

    @Override
    public Drawable removeLeadingEmptyVerticalSpace() {
        return this;
    }
}
