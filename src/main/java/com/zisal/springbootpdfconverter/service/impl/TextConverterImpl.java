package com.zisal.springbootpdfconverter.service.impl;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;
import com.zisal.springbootpdfconverter.service.ITextConverter;
import org.apache.pdfbox.cos.COSDocument;
import org.apache.pdfbox.io.RandomAccessFile;
import org.apache.pdfbox.pdfparser.PDFParser;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.*;

/**
 * Created on 4/15/18.
 *
 * @author <a href="mailto:fauzi.knightmaster.achmad@gmail.com">Achmad Fauzi</a>
 */
@Service
public class TextConverterImpl implements ITextConverter {

    private static final Logger LOGGER = LoggerFactory.getLogger(TextConverterImpl.class);

    @Override
    public void convertToPDF(String p_SourcePath, String p_OutputPath) {
        Document pdfDoc = new Document();
        try {
            PdfWriter.getInstance(pdfDoc, new FileOutputStream(p_OutputPath));
        } catch (DocumentException | FileNotFoundException e) {
            LOGGER.error("Error handling pdf writer {} ",e.toString());
        }
        pdfDoc.open();

        BufferedReader bufferedReader = null;
        try {
            bufferedReader = new BufferedReader(new FileReader(p_SourcePath));
        } catch (FileNotFoundException e) {
            LOGGER.error("Error handling buffered reader");
        }
        String line;
        try {
            if (bufferedReader != null) {
                while ((line = bufferedReader.readLine()) != null) {
                    Paragraph paragraph = new Paragraph(line.concat("\n"));
                    paragraph.setAlignment(Element.ALIGN_JUSTIFIED);
                    try {
                        pdfDoc.add(paragraph);
                    } catch (DocumentException e) {
                        LOGGER.error("Error adding paragraph {}", e.toString());
                    }
                }
            }
        } catch (IOException e) {
            LOGGER.error("Error reading line {}", e.toString());
        }
        pdfDoc.close();
        try {
            bufferedReader.close();
        } catch (IOException e) {
            LOGGER.error("Error closing buffered reader {}", e.toString());
        }

    }

    @Override
    public void convertToParticularForm(String p_SourcePath, String p_OutputPath, String p_CharSet) {
        File file = new File(p_SourcePath);
        String parsedText = null;
        PDFParser pdfParser = null;
        try {
            pdfParser = new PDFParser(new RandomAccessFile(file, "r"));
        } catch (IOException e) {
            LOGGER.error("Error Parsing Text {}", e.toString());
        }
        COSDocument cosDocument = null;
        try {
            if (pdfParser != null) {
                cosDocument = pdfParser.getDocument();
            }
        } catch (IOException e) {
            LOGGER.error("Error getting cosdocument ", e.toString());
        }
        PDFTextStripper pdfTextStripper = null;
        try {
            pdfTextStripper = new PDFTextStripper();
        } catch (IOException e) {
            LOGGER.error("error initiating pdf stripper {} ", e.toString());
        }
        PDDocument pdDocument = new PDDocument(cosDocument);
        try {
            if (pdfTextStripper != null) {
                parsedText = pdfTextStripper.getText(pdDocument);
            }
        } catch (IOException e) {
            LOGGER.error("Error getting parsed text {}", e.toString());
        }
        if (cosDocument != null) {
            try {
                cosDocument.close();
            } catch (IOException e) {
                LOGGER.error("Error closing cos document {}", e.toString());
            }
        }
        try {
            pdDocument.close();
        } catch (IOException e) {
            LOGGER.error("Error closing pd document {}", e.toString());
        }
        PrintWriter printWriter = null;
        try {
            printWriter = new PrintWriter(p_OutputPath);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        if (printWriter != null) {
            printWriter.print(parsedText);
        }
        if (printWriter != null) {
            printWriter.close();
        }
    }
}
