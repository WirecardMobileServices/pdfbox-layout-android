package de.wirecard.pdfbox.layout.elements.render;

import com.tom_roush.pdfbox.pdmodel.PDDocument;
import com.tom_roush.pdfbox.pdmodel.PDPage;
import com.tom_roush.pdfbox.pdmodel.PDPageContentStream;
import com.tom_roush.pdfbox.pdmodel.common.PDRectangle;
import com.tom_roush.pdfbox.util.Matrix;

import java.io.Closeable;
import java.io.IOException;

import de.wirecard.pdfbox.layout.elements.ControlElement;
import de.wirecard.pdfbox.layout.elements.Document;
import de.wirecard.pdfbox.layout.elements.Element;
import de.wirecard.pdfbox.layout.elements.Orientation;
import de.wirecard.pdfbox.layout.elements.PageFormat;
import de.wirecard.pdfbox.layout.elements.PositionControl;
import de.wirecard.pdfbox.layout.elements.PositionControl.MarkPosition;
import de.wirecard.pdfbox.layout.elements.PositionControl.MovePosition;
import de.wirecard.pdfbox.layout.elements.PositionControl.SetPosition;
import de.wirecard.pdfbox.layout.text.DrawContext;
import de.wirecard.pdfbox.layout.text.DrawListener;
import de.wirecard.pdfbox.layout.text.Position;
import de.wirecard.pdfbox.layout.text.annotations.AnnotationDrawListener;

/**
 * The render context is a container providing all state of the current
 * rendering process.
 */
public class RenderContext implements Renderer, Closeable, DrawContext, DrawListener {

    private final Document document;
    private final PDDocument pdDocument;
    private PDPage page;
    private int pageIndex = 0;
    private PDPageContentStream contentStream;
    private Position currentPosition;
    private Position markedPosition;
    private Position maxPositionOnPage;
    private Layout layout = new VerticalLayout();

    private PageFormat nextPageFormat;
    private PageFormat pageFormat;

    private AnnotationDrawListener annotationDrawListener;

    /**
     * Creates a render context.
     *
     * @param document   the document to render.
     * @param pdDocument the underlying pdfbox document.
     * @throws IOException by pdfbox.
     */
    public RenderContext(Document document, PDDocument pdDocument)
            throws IOException {
        this.document = document;
        this.pdDocument = pdDocument;
        this.pageFormat = document.getPageFormat();
        this.annotationDrawListener = new AnnotationDrawListener(this);
        newPage();
    }

    /**
     * @return the current {@link Layout} used for rendering.
     */
    public Layout getLayout() {
        return layout;
    }

    /**
     * Sets the current {@link Layout} used for rendering.
     *
     * @param layout the new layout.
     */
    public void setLayout(Layout layout) {
        this.layout = layout;
        resetPositionToLeftEndOfPage();
    }

    /**
     * @return the orientation to use for the page. If no special
     * {@link #setPageFormat(PageFormat) page format} is set, the
     * {@link Document#getOrientation() document orientation} is used.
     * @deprecated use {@link #getPageFormat()} instead.
     */
    @Deprecated
    public Orientation getOrientation() {
        return getPageFormat().getOrientation();
    }

    /**
     * @return the media box to use for the page. If no special
     * {@link #setPageFormat(PageFormat) page format} is set, the
     * {@link Document#getMediaBox() document media box} is used.
     * @deprecated use {@link #getPageFormat()} instead.
     */
    @Deprecated
    public PDRectangle getMediaBox() {
        return getPageFormat().getMediaBox();
    }

    public void setPageFormat(final PageFormat pageFormat) {
        if (pageFormat == null) {
            this.pageFormat = document.getPageFormat();
        } else {
            this.pageFormat = pageFormat;
        }
    }

    public PageFormat getPageFormat() {
        return pageFormat;
    }

    /**
     * @return the upper left position in the document respecting the
     * {@link Document document} margins.
     */
    public Position getUpperLeft() {
        return new Position(getPageFormat().getMarginLeft(), getPageHeight()
                - getPageFormat().getMarginTop());
    }

    /**
     * @return the lower right position in the document respecting the
     * {@link Document document} margins.
     */
    public Position getLowerRight() {
        return new Position(getPageWidth() - getPageFormat().getMarginRight(),
                getPageFormat().getMarginBottom());
    }

    /**
     * @return the current rendering position in pdf coord space (origin in
     * lower left corner).
     */
    public Position getCurrentPosition() {
        return currentPosition;
    }

    /**
     * @return the {@link PositionControl#MARKED_POSITION}.
     */
    public Position getMarkedPosition() {
        return markedPosition;
    }

    protected void setMarkedPosition(Position markedPosition) {
        this.markedPosition = markedPosition;
    }

    /**
     * Moves the {@link #getCurrentPosition() current position} relatively by
     * the given offset.
     *
     * @param x to move horizontally.
     * @param y to move vertically.
     */
    public void movePositionBy(final float x, final float y) {
        currentPosition = currentPosition.add(x, y);
    }

    /**
     * Resets the position to {@link #getUpperLeft()}.
     */
    public void resetPositionToUpperLeft() {
        currentPosition = getUpperLeft();
    }

    /**
     * Resets the position to the x of {@link #getUpperLeft()} while keeping the
     * current y.
     */
    public void resetPositionToLeft() {
        currentPosition = new Position(getUpperLeft().getX(),
                currentPosition.getY());
    }

    /**
     * Resets the position to the x of {@link #getUpperLeft()} and the
     * y of {@link #getMaxPositionOnPage()}.
     */
    protected void resetPositionToLeftEndOfPage() {
        currentPosition = new Position(getUpperLeft().getX(),
                getMaxPositionOnPage().getY());
    }

    /**
     * @return the orientation of the current page
     */
    protected Orientation getPageOrientation() {
        if (getPageWidth() > getPageHeight()) {
            return Orientation.Landscape;
        }
        return Orientation.Portrait;
    }

    /**
     * @return <code>true</code> if the page is rotated by 90/270 degrees.
     */
    public boolean isPageTilted() {
        return page.getRotation() == 90 || page.getRotation() == 270;
    }

    /**
     * @return the page' width, or - if {@link #isPageTilted() rotated} - the
     * height.
     */
    public float getPageWidth() {
        if (isPageTilted()) {
            return page.getMediaBox().getHeight();
        }
        return page.getMediaBox().getWidth();
    }

    /**
     * @return the page' height, or - if {@link #isPageTilted() rotated} - the
     * width.
     */
    public float getPageHeight() {
        if (isPageTilted()) {
            return page.getMediaBox().getWidth();
        }
        return page.getMediaBox().getHeight();
    }

    /**
     * @return the {@link #getPageWidth() width of the page} respecting the
     * margins.
     */
    public float getWidth() {
        return getPageWidth() - getPageFormat().getMarginLeft()
                - getPageFormat().getMarginRight();
    }

    /**
     * @return the {@link #getPageHeight() height of the page} respecting the
     * margins.
     */
    public float getHeight() {
        return getPageHeight() - getPageFormat().getMarginTop()
                - getPageFormat().getMarginBottom();
    }

    /**
     * @return the remaining height on the page.
     */
    public float getRemainingHeight() {
        return getCurrentPosition().getY() - getPageFormat().getMarginBottom();
    }

    /**
     * @return the document.
     */
    public Document getDocument() {
        return document;
    }

    /**
     * @return the PDDocument.
     */
    @Override
    public PDDocument getPdDocument() {
        return pdDocument;
    }

    @Override
    public PDPage getCurrentPage() {
        return page;
    }

    @Override
    public PDPageContentStream getCurrentPageContentStream() {
        return getContentStream();
    }

    /**
     * @return the current PDPage.
     */
    @Deprecated
    public PDPage getPage() {
        return getCurrentPage();
    }

    /**
     * @return the current PDPageContentStream.
     */
    public PDPageContentStream getContentStream() {
        return contentStream;
    }

    /**
     * @return the current page index (starting from 0).
     */
    public int getPageIndex() {
        return pageIndex;
    }

    @Override
    public boolean render(RenderContext renderContext, Element element,
                          LayoutHint layoutHint) throws IOException {
        boolean success = getLayout()
                .render(renderContext, element, layoutHint);
        if (success) {
            return true;
        }
        if (element == ControlElement.NEWPAGE) {
            newPage();
            return true;
        }
        if (element instanceof PositionControl) {
            return render((PositionControl) element);
        }
        if (element instanceof PageFormat) {
            nextPageFormat = (PageFormat) element;
            return true;
        }
        if (element instanceof Layout) {
            setLayout((Layout) element);
            return true;
        }
        return false;
    }

    protected boolean render(final PositionControl positionControl) {
        if (positionControl instanceof MarkPosition) {
            setMarkedPosition(getCurrentPosition());
            return true;
        }
        if (positionControl instanceof SetPosition) {
            SetPosition setPosition = (SetPosition) positionControl;
            Float x = setPosition.getX();
            if (x == PositionControl.MARKED_POSITION) {
                x = getMarkedPosition().getX();
            }
            if (x == null) {
                x = getCurrentPosition().getX();
            }
            Float y = setPosition.getY();
            if (y == PositionControl.MARKED_POSITION) {
                y = getMarkedPosition().getY();
            }
            if (y == null) {
                y = getCurrentPosition().getY();
            }
            Position newPosition = new Position(x, y);
            currentPosition = newPosition;
            return true;
        }
        if (positionControl instanceof MovePosition) {
            MovePosition movePosition = (MovePosition) positionControl;
            movePositionBy(movePosition.getX(), movePosition.getY());
            return true;
        }
        return false;
    }

    /**
     * Triggers a new page.
     *
     * @throws IOException by pdfbox
     */
    public void newPage() throws IOException {
        if (closePage()) {
            ++pageIndex;
        }
        if (nextPageFormat != null) {
            setPageFormat(nextPageFormat);
        }

        this.page = new PDPage(getPageFormat().getMediaBox());
        this.pdDocument.addPage(page);
        this.contentStream = new PDPageContentStream(pdDocument, page);

        // fix orientation
        if (getPageOrientation() != getPageFormat().getOrientation()) {
            if (isPageTilted()) {
                page.setRotation(0);
            } else {
                page.setRotation(90);
            }
        }

        if (isPageTilted()) {
            contentStream.transform(new Matrix(0, 1, -1, 0,
                    getPageHeight(), 0));
        }

        resetPositionToUpperLeft();
        resetMaxPositionOnPage();
        document.beforePage(this);
        annotationDrawListener.beforePage(this);
    }

    /**
     * Closes the current page.
     *
     * @return <code>true</code> if the current page has not been closed before.
     * @throws IOException by pdfbox
     */
    public boolean closePage() throws IOException {
        if (contentStream != null) {

            annotationDrawListener.afterPage(this);
            document.afterPage(this);

            if (getPageFormat().getRotation() != 0) {
                int currentRotation = getCurrentPage().getRotation();
                getCurrentPage().setRotation(
                        currentRotation + getPageFormat().getRotation());
            }

            contentStream.close();
            contentStream = null;
            return true;
        }
        return false;
    }

    @Override
    public void close() throws IOException {
        closePage();
        annotationDrawListener.afterRender();
    }

    @Override
    public void drawn(Object drawnObject, Position upperLeft, float width,
                      float height) {
        updateMaxPositionOnPage(upperLeft, width, height);
        annotationDrawListener.drawn(drawnObject, upperLeft, width, height);
    }

    /**
     * Updates the maximum right resp. bottom position on the page.
     *
     * @param upperLeft
     * @param width
     * @param height
     */
    protected void updateMaxPositionOnPage(Position upperLeft, float width,
                                           float height) {
        maxPositionOnPage = new Position(Math.max(maxPositionOnPage.getX(),
                upperLeft.getX() + width), Math.min(maxPositionOnPage.getY(),
                upperLeft.getY() - height));
    }

    /**
     * Resets the maximumn position to upper left.
     */
    protected void resetMaxPositionOnPage() {
        maxPositionOnPage = getUpperLeft();
    }

    /**
     * @return the maximum right and bottom position of all
     * objects rendered on this page so far.
     */
    protected Position getMaxPositionOnPage() {
        return maxPositionOnPage;
    }

}
