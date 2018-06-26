package de.wirecard.pdfbox.layout.shape;

import com.tom_roush.pdfbox.pdmodel.PDDocument;
import com.tom_roush.pdfbox.pdmodel.PDPageContentStream;

import java.io.IOException;

import de.wirecard.pdfbox.layout.text.Position;

/**
 * A simple rectangular shape.
 */
public class Rect extends AbstractShape {

    @Override
    public void add(PDDocument pdDocument, PDPageContentStream contentStream,
                    Position upperLeft, float width, float height) throws IOException {
        contentStream.addRect(upperLeft.getX(), upperLeft.getY() - height,
                width, height);
    }

}
