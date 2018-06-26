package de.wirecard.pdfbox.layout.util;

import android.support.annotation.ColorInt;

import com.tom_roush.harmony.awt.geom.AffineTransform;
import com.tom_roush.pdfbox.pdmodel.PDPage;
import com.tom_roush.pdfbox.pdmodel.common.PDRectangle;
import com.tom_roush.pdfbox.pdmodel.interactive.action.PDActionGoTo;
import com.tom_roush.pdfbox.pdmodel.interactive.action.PDActionURI;
import com.tom_roush.pdfbox.pdmodel.interactive.annotation.PDAnnotation;
import com.tom_roush.pdfbox.pdmodel.interactive.annotation.PDAnnotationLink;
import com.tom_roush.pdfbox.pdmodel.interactive.annotation.PDBorderStyleDictionary;
import com.tom_roush.pdfbox.pdmodel.interactive.documentnavigation.destination.PDDestination;

import de.wirecard.pdfbox.layout.text.annotations.Annotations.HyperlinkAnnotation.LinkStyle;

public class CompatibilityHelper {
    private static PDBorderStyleDictionary noBorder;
    private final static String BULLET = "\u2022";
    private final static String DOUBLE_ANGLE = "\u00bb";

    /**
     * Returns the bullet character for the given level. Actually only two
     * bullets are used for odd and even levels. For odd levels the
     * {@link #BULLET bullet} character is used, for even it is the
     * {@link #DOUBLE_ANGLE double angle}. You may customize this by setting the
     * system properties <code>pdfbox.layout.bullet.odd</code> and/or
     * <code>pdfbox.layout.bullet.even</code>.
     *
     * @param level the level to return the bullet for.
     * @return the bullet character for the leve.
     */
    public static String getBulletCharacter(final int level) {
        if (level % 2 == 1) {
            return System.getProperty("pdfbox.layout.bullet.odd", BULLET);
        }
        return System.getProperty("pdfbox.layout.bullet.even", DOUBLE_ANGLE);
    }

    public static PDAnnotationLink createLink(PDPage page, PDRectangle rect, @ColorInt int color,
                                              LinkStyle linkStyle, final String uri) {
        PDAnnotationLink pdLink = createLink(page, rect, color, linkStyle);

        PDActionURI actionUri = new PDActionURI();
        actionUri.setURI(uri);
        pdLink.setAction(actionUri);
        return pdLink;
    }

    public static PDAnnotationLink createLink(PDPage page, PDRectangle rect, @ColorInt int color,
                                              LinkStyle linkStyle, final PDDestination destination) {
        PDAnnotationLink pdLink = createLink(page, rect, color, linkStyle);

        PDActionGoTo gotoAction = new PDActionGoTo();
        gotoAction.setDestination(destination);
        pdLink.setAction(gotoAction);
        return pdLink;
    }

    private static PDAnnotationLink createLink(PDPage page, PDRectangle rect, @ColorInt int color,
                                               LinkStyle linkStyle) {
        PDAnnotationLink pdLink = new PDAnnotationLink();
        pdLink.setBorderStyle(toBorderStyle(linkStyle));
        PDRectangle rotatedRect = transformToPageRotation(rect, page);
        pdLink.setRectangle(rotatedRect);
        setAnnotationColor(pdLink, color);
        return pdLink;
    }

    private static PDBorderStyleDictionary toBorderStyle(
            final LinkStyle linkStyle) {
        if (linkStyle == LinkStyle.none) {
            return getNoBorder();
        }
        PDBorderStyleDictionary borderStyle = new PDBorderStyleDictionary();
        borderStyle.setStyle(PDBorderStyleDictionary.STYLE_UNDERLINE);
        return borderStyle;
    }

    /**
     * Sets the color in the annotation.
     *
     * @param annotation the annotation.
     * @param color      the color to set.
     */
    public static void setAnnotationColor(final PDAnnotation annotation,
                                          @ColorInt int color) {
        annotation.setColor(ColorConverter.convert(color));
    }


    private static PDBorderStyleDictionary getNoBorder() {
        if (noBorder == null) {
            noBorder = new PDBorderStyleDictionary();
            noBorder.setWidth(0);
        }
        return noBorder;
    }

    /**
     * Transform the quad points in order to match the page rotation
     *
     * @param quadPoints the quad points.
     * @param page       the page.
     * @return the transformed quad points.
     */
    public static float[] transformToPageRotation(
            final float[] quadPoints, final PDPage page) {
        AffineTransform transform = transformToPageRotation(page);
        if (transform == null) {
            return quadPoints;
        }
        float[] rotatedPoints = new float[quadPoints.length];
        transform.transform(quadPoints, 0, rotatedPoints, 0, 4);
        return rotatedPoints;
    }

    /**
     * Transform the rectangle in order to match the page rotation
     *
     * @param rect the rectangle.
     * @param page the page.
     * @return the transformed rectangle.
     */
    public static PDRectangle transformToPageRotation(
            final PDRectangle rect, final PDPage page) {
        AffineTransform transform = transformToPageRotation(page);
        if (transform == null) {
            return rect;
        }
        float[] points = new float[]{rect.getLowerLeftX(), rect.getLowerLeftY(), rect.getUpperRightX(), rect.getUpperRightY()};
        float[] rotatedPoints = new float[4];
        transform.transform(points, 0, rotatedPoints, 0, 2);
        PDRectangle rotated = new PDRectangle();
        rotated.setLowerLeftX(rotatedPoints[0]);
        rotated.setLowerLeftY(rotatedPoints[1]);
        rotated.setUpperRightX(rotatedPoints[2]);
        rotated.setUpperRightY(rotatedPoints[3]);
        return rotated;
    }

    private static AffineTransform transformToPageRotation(final PDPage page) {
        int pageRotation = page.getRotation();
        if (pageRotation == 0) {
            return null;
        }
        float pageWidth = page.getMediaBox().getHeight();
        float pageHeight = page.getMediaBox().getWidth();
        AffineTransform transform = new AffineTransform();
        transform.rotate(pageRotation * Math.PI / 180, pageHeight / 2,
                pageWidth / 2);
        double offset = Math.abs(pageHeight - pageWidth) / 2;
        transform.translate(-offset, offset);
        return transform;
    }

}
