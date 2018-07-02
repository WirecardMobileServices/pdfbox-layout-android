package de.wirecard.pdfbox.layout.text;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;

/**
 * Provides the current page and document to draw to.
 */
public interface DrawContext {

    /**
     * @return the document to draw to.
     */
    PDDocument getPdDocument();

    /**
     * @return the current page to draw to.
     */
    PDPage getCurrentPage();

    /**
     * @return the current page content stream.
     */
    PDPageContentStream getCurrentPageContentStream();
}
