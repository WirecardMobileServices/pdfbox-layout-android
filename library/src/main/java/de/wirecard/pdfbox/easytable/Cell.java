package de.wirecard.pdfbox.easytable;

import android.graphics.Color;
import android.support.annotation.ColorInt;

import org.apache.pdfbox.pdmodel.font.PDFont;


public class Cell {

    public enum HorizontalAlignment {
        LEFT, CENTER, RIGHT
    }

    private Row row;

    private HorizontalAlignment alignment = HorizontalAlignment.LEFT;
    private final String text;
    private Integer backgroundColor;
    private Integer textColor = Color.BLACK;
    private PDFont font;

    private float paddingLeft = 4;
    private float paddingRight = 4;
    private float paddingTop = 2;
    private float paddingBottom = 4;

    private float borderWidthTop = 0;
    private float borderWidthLeft = 0;
    private float borderWidthRight = 0;
    private float borderWidthBottom = 0;

    private Integer borderColor;

    private Cell(Object object) {
        this.text = String.valueOf(object);
    }

    public static Cell withText(Object object) {
        return new Cell(object);
    }

    public Cell withAllBorders() {
        int borderWith = 1;
        return withAllBorders(borderWith);
    }

    public Cell withAllBorders(int borderWith) {
        return this
                .setBorderWidthBottom(borderWith)
                .setBorderWidthLeft(borderWith)
                .setBorderWidthRight(borderWith)
                .setBorderWidthTop(borderWith);
    }

    public PDFont getFont() {
        return font;
    }

    public Cell setFont(PDFont font) {
        this.font = font;
        return this;
    }

    public Row getRow() {
        return row;
    }

    public void setRow(final Row row) {
        this.row = row;
    }

    public String getText() {
        return text;
    }

    public boolean hasBackgroundColor() {
        return backgroundColor != null;
    }

    public Integer getBackgroundColor() {
        return backgroundColor;
    }

    public Cell setBackgroundColor(@ColorInt final int backgroundColor) {
        this.backgroundColor = backgroundColor;
        return this;
    }

    public boolean hasText() {
        return text != null;
    }

    public float getPaddingLeft() {
        return paddingLeft;
    }

    public Cell setPaddingLeft(final float paddingLeft) {
        this.paddingLeft = paddingLeft;
        return this;
    }

    public float getPaddingRight() {
        return paddingRight;
    }

    public Cell setPaddingRight(final float paddingRight) {
        this.paddingRight = paddingRight;
        return this;
    }

    public float getPaddingTop() {
        return paddingTop;
    }

    public Cell setPaddingTop(final float paddingTop) {
        this.paddingTop = paddingTop;
        return this;
    }

    public float getPaddingBottom() {
        return paddingBottom;
    }

    public Cell setPaddingBottom(final float paddingBottom) {
        this.paddingBottom = paddingBottom;
        return this;
    }

    public HorizontalAlignment getHorizontalAlignment() {
        return alignment;
    }

    public Cell setHorizontalAlignment(final HorizontalAlignment alignment) {
        this.alignment = alignment;
        return this;
    }

    public boolean hasBorderTop() {
        return getBorderWidthTop() > 0;
    }

    public boolean hasBorderBottom() {
        return getBorderWidthBottom() > 0;
    }

    public boolean hasBorderLeft() {
        return getBorderWidthLeft() > 0;
    }

    public boolean hasBorderRight() {
        return getBorderWidthRight() > 0;
    }

    public float getBorderWidthTop() {
        return borderWidthTop;
    }

    public Cell setBorderWidthTop(float borderWidthTop) {
        this.borderWidthTop = borderWidthTop;
        return this;
    }

    public float getBorderWidthLeft() {
        return borderWidthLeft;
    }

    public Cell setBorderWidthLeft(float borderWidthLeft) {
        this.borderWidthLeft = borderWidthLeft;
        return this;
    }

    public float getBorderWidthRight() {
        return borderWidthRight;
    }

    public Cell setBorderWidthRight(float borderWidthRight) {
        this.borderWidthRight = borderWidthRight;
        return this;
    }

    public float getBorderWidthBottom() {
        return borderWidthBottom;
    }

    public Cell setBorderWidthBottom(float borderWidthBottom) {
        this.borderWidthBottom = borderWidthBottom;
        return this;
    }

    public float getHeightWithoutFontSize() {
        return this.paddingBottom + this.paddingTop;
    }

    public Integer getBorderColor() {
        if (borderColor != null) {
            return borderColor;
        } else {
            return getRow().getBorderColor();
        }
    }

    public Cell setBorderColor(@ColorInt Integer borderColor) {
        this.borderColor = borderColor;
        return this;
    }

    public Integer getParentBorderColor() {
        return getRow().getBorderColor();
    }

    public Integer getTextColor() {
        return textColor;
    }

    public Cell setTextColor(@ColorInt Integer textColor) {
        this.textColor = textColor;
        return this;
    }

}
