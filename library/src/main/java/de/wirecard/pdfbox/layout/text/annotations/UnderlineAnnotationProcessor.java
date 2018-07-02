package de.wirecard.pdfbox.layout.text.annotations;

import android.support.annotation.ColorInt;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPageContentStream;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import de.wirecard.pdfbox.layout.shape.Stroke;
import de.wirecard.pdfbox.layout.text.DrawContext;
import de.wirecard.pdfbox.layout.text.Position;
import de.wirecard.pdfbox.layout.text.StyledText;
import de.wirecard.pdfbox.layout.text.annotations.Annotations.UnderlineAnnotation;
import de.wirecard.pdfbox.layout.util.ColorConverter;

/**
 * This annotation processor handles the {@link UnderlineAnnotation}s, and adds
 * the needed hyperlink metadata to the PDF document.
 */
public class UnderlineAnnotationProcessor implements AnnotationProcessor {

    private List<Line> linesOnPage = new ArrayList<Line>();

    @Override
    public void annotatedObjectDrawn(Annotated drawnObject,
                                     DrawContext drawContext, Position upperLeft, float width,
                                     float height) {

        if (!(drawnObject instanceof StyledText)) {
            return;
        }

        StyledText drawnText = (StyledText) drawnObject;
        for (UnderlineAnnotation underlineAnnotation : drawnObject
                .getAnnotationsOfType(UnderlineAnnotation.class)) {
            float fontSize = drawnText.getFontDescriptor().getSize();
            float ascent = fontSize
                    * drawnText.getFontDescriptor().getFont()
                    .getFontDescriptor().getAscent() / 1000;

            float baselineOffset = fontSize * underlineAnnotation.getBaselineOffsetScale();
            float thickness = (0.01f + fontSize * 0.05f)
                    * underlineAnnotation.getLineWeight();

            Position start = new Position(upperLeft.getX(), upperLeft.getY()
                    - ascent + baselineOffset);
            Position end = new Position(start.getX() + width, start.getY());
            Stroke stroke = Stroke.builder().lineWidth(thickness).build();
            Line line = new Line(start, end, stroke, drawnText.getColor());
            linesOnPage.add(line);
        }
    }

    @Override
    public void beforePage(DrawContext drawContext) {
        linesOnPage.clear();
    }

    @Override
    public void afterPage(DrawContext drawContext) throws IOException {
        for (Line line : linesOnPage) {
            line.draw(drawContext.getCurrentPageContentStream());
        }
        linesOnPage.clear();
    }

    @Override
    public void afterRender(PDDocument document) {
        linesOnPage.clear();
    }

    private static class Line {

        private Position start;
        private Position end;
        private Stroke stroke;
        private Integer color;

        public Line(Position start, Position end, Stroke stroke, @ColorInt int color) {
            super();
            this.start = start;
            this.end = end;
            this.stroke = stroke;
            this.color = color;
        }

        public void draw(PDPageContentStream contentStream) throws IOException {
            if (color != null) {
                contentStream.setStrokingColor(ColorConverter.convert(color));
            }
            if (stroke != null) {
                stroke.applyTo(contentStream);
            }
            contentStream.moveTo(start.getX(), start.getY());
            contentStream.lineTo(end.getX(), end.getY());
        }

    }
}
