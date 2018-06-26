package de.wirecard.pdfbox.layout.elements;

import com.tom_roush.pdfbox.pdmodel.PDDocument;
import com.tom_roush.pdfbox.pdmodel.PDPageContentStream;

import java.io.IOException;

import de.wirecard.pdfbox.layout.text.Alignment;
import de.wirecard.pdfbox.layout.text.DrawListener;
import de.wirecard.pdfbox.layout.text.Position;
import de.wirecard.pdfbox.layout.text.TextFlow;
import de.wirecard.pdfbox.layout.text.TextSequenceUtil;
import de.wirecard.pdfbox.layout.text.WidthRespecting;

/**
 * A paragraph is used as a container for {@link TextFlow text} that is drawn as
 * one element. A paragraph has a {@link #setAlignment(Alignment) (text-)
 * alignment}, and {@link WidthRespecting respects a given width} by applying
 * word-wrap.
 */
public class Paragraph extends TextFlow implements Drawable, Element,
        WidthRespecting, Dividable {

    private Position absolutePosition;
    private Alignment alignment = Alignment.Left;

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

    /**
     * @return the text alignment to apply. Default is left.
     */
    public Alignment getAlignment() {
        return alignment;
    }

    /**
     * Sets the alignment to apply.
     *
     * @param alignment the text alignment.
     */
    public void setAlignment(Alignment alignment) {
        this.alignment = alignment;
    }

    @Override
    public void draw(PDDocument pdDocument, PDPageContentStream contentStream,
                     Position upperLeft, DrawListener drawListener) throws IOException {
        drawText(contentStream, upperLeft, getAlignment(), drawListener);
    }

    @Override
    public Divided divide(float remainingHeight, final float pageHeight)
            throws IOException {
        return TextSequenceUtil.divide(this, getMaxWidth(), remainingHeight);
    }

    @Override
    public Paragraph removeLeadingEmptyVerticalSpace() throws IOException {
        return removeLeadingEmptyLines();
    }

    @Override
    public Paragraph removeLeadingEmptyLines() throws IOException {
        Paragraph result = (Paragraph) super.removeLeadingEmptyLines();
        result.setAbsolutePosition(this.getAbsolutePosition());
        result.setAlignment(this.getAlignment());
        return result;
    }

    @Override
    protected Paragraph createInstance() {
        return new Paragraph();
    }

    @Override
    public void drawText(PDPageContentStream contentStream, Position upperLeft, Alignment alignment, DrawListener drawListener) {

    }
}
