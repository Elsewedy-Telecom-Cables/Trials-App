package com.elsewedyt.trialsapp.PDF;

import com.itextpdf.text.BadElementException;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Image;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ItemsReprot {

    long millis = System.currentTimeMillis();
    java.sql.Date date = new java.sql.Date(millis);

    DateFormat sdf = new SimpleDateFormat("dd/MM/yyyy - hh:mm:ss aa");
    Date resultdate = new Date(millis);
    String dateFormat = sdf.format(resultdate);

    public PdfWriter pdfWriter;
    private Document document;
    File pdfFile;

    BaseFont ArialBase;

    {
        try {
            ArialBase = BaseFont.createFont(
                    "c:/windows/fonts/Arial.ttf", BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
        } catch (DocumentException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    Font font = new Font(Font.FontFamily.HELVETICA, 18, Font.BOLD, BaseColor.RED);

    Font ArialFont = new Font(ArialBase, 14);
    Font ArialFontRed = new Font(ArialBase, 20, 0, BaseColor.DARK_GRAY);
    Font ArialFontBlue = new Font(ArialBase, 16, 0, BaseColor.BLUE);
    Font ArialFontSmall = new Font(ArialBase, 10);

    public void closeDocument() {
        document.close();
    }

    public void openDocument(String pdfName, String location) throws DocumentException, FileNotFoundException, BadElementException, IOException {

        File folder = new File(location);
        if (!folder.exists()) {
            folder.mkdirs();
        }
        pdfFile = new File(folder, pdfName);
        document = new Document();

        // left , right , top , bottom
        document.setMargins(36, 36, 160, 50);
        document.setMarginMirroring(false);

        pdfWriter = PdfWriter.getInstance(document, new FileOutputStream(pdfFile));

              // add header and footer
        // add header and footer
        HeaderFooterPageEvent event = new HeaderFooterPageEvent();
        pdfWriter.setPageEvent(event);

        document.open();

        //  addTopHeaderIMG();
        addTopHeader();

    }

    public void addTopHeader() {

        // Table
        PdfPTable table = new PdfPTable(3); // 3 columns.

        table.setWidthPercentage(100);

        try {
            table.setWidths(new int[]{180, 0, 180});

        } catch (DocumentException e) {
            e.printStackTrace();
        }

        table.setRunDirection(PdfWriter.RUN_DIRECTION_RTL);
        table.setHorizontalAlignment(Element.ALIGN_CENTER);

        PdfPCell cell7 = new PdfPCell(new Phrase(" تــقــريــر  الاصــــــنـاف ", ArialFontRed));
        cell7.setBorder(Rectangle.NO_BORDER);
        cell7.setPadding(5);
        cell7.setColspan(3);
        cell7.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(cell7);

        try {

            //   table.setSpacingBefore(20F);
            table.setSpacingBefore(10F);
            document.add(table);

        } catch (DocumentException e) {
            e.printStackTrace();
        }

    }

    public void addTopHeaderIMG() throws BadElementException, IOException {

        PdfPTable tableTop = new PdfPTable(1); // 3 columns.

        tableTop.setWidthPercentage(100);

        tableTop.setRunDirection(PdfWriter.RUN_DIRECTION_RTL);
        tableTop.setHorizontalAlignment(Element.ALIGN_CENTER);

        // Image image = Image.getInstance("src/images/test.png");
        Image image = Image.getInstance("C:\\test.png");
        image.setAlignment(Element.ALIGN_CENTER);
        // image.scaleAbsolute(350f, 840f);
        document.getPageSize();
        image.scaleToFit(document.getPageSize().getWidth(), 78);

        float scaler = ((document.getPageSize().getWidth() - document.leftMargin()
                - document.rightMargin()) / image.getWidth()) * 100;

        image.scalePercent(scaler);

        PdfPCell cell1 = new PdfPCell();
        cell1.addElement(image);
        cell1.setBorder(Rectangle.NO_BORDER);
        tableTop.addCell(cell1);

        try {

         //   table.setSpacingBefore(20F);
            //   table.setSpacingBefore(30F);
            document.add(tableTop);

        } catch (DocumentException e) {
            e.printStackTrace();
        }

    }

    public void addImage() throws BadElementException, IOException, DocumentException {

        Image image = Image.getInstance("src/images/elnoor.jpg");
        image.setAlignment(Element.ALIGN_CENTER);
        image.scaleAbsolute(250f, 740f);
        image.scaleToFit(850, 78);
        //Add to document
        document.add(image);

    }

    public void addTable(String[] header, ArrayList<String[]> items) {

        Paragraph paragraph = new Paragraph();

        // Table
        PdfPTable table = new PdfPTable(header.length); // # of columns.
        table.setWidthPercentage(100);

        table.setHeaderRows(1);

        try {
            table.setWidths(new int[]{50, 50, 50, 150, 50});
        } catch (DocumentException e) {
            e.printStackTrace();
        }
        table.setRunDirection(PdfWriter.RUN_DIRECTION_RTL);

        PdfPCell pdfPCell;
        int indexC = 0;
        while (indexC < header.length) {
            pdfPCell = new PdfPCell(new Phrase(header[indexC++], ArialFont));
            pdfPCell.setHorizontalAlignment(Element.ALIGN_CENTER);
            pdfPCell.setVerticalAlignment(Element.ALIGN_CENTER);
            pdfPCell.setBackgroundColor(BaseColor.LIGHT_GRAY);
            pdfPCell.setFixedHeight(30);
            table.addCell(pdfPCell);
        }

        for (int indexR = 0; indexR < items.size(); indexR++) {
            String[] row = items.get(indexR);
            for (indexC = 0; indexC < header.length; indexC++) {

                Phrase phrase = new Phrase(row[indexC], ArialFontSmall);

                Paragraph paragraph1 = new Paragraph(row[indexC], ArialFontSmall);

                paragraph1.setSpacingBefore(5);

                pdfPCell = new PdfPCell(paragraph1);
                pdfPCell.setHorizontalAlignment(Element.ALIGN_CENTER);
                pdfPCell.setPadding(5);
                // pdfPCell.setFixedHeight(20);
                table.addCell(pdfPCell);
            }
        }

        try {
            //  table.setSpacingAfter(70f);
            table.setSpacingBefore(30F);
            document.add(table);

        } catch (DocumentException e) {
            e.printStackTrace();
        }

    }

}
