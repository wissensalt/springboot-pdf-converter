package com.zisal.springbootpdfconverter.service.impl;

import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.parser.PdfReaderContentParser;
import com.itextpdf.text.pdf.parser.SimpleTextExtractionStrategy;
import com.itextpdf.text.pdf.parser.TextExtractionStrategy;
import com.zisal.springbootpdfconverter.service.IWordConverter;
import org.apache.poi.xwpf.usermodel.BreakType;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created on 4/15/18.
 *
 * @author <a href="mailto:fauzi.knightmaster.achmad@gmail.com">Achmad Fauzi</a>
 */
@Service
public class WordConverterImpl implements IWordConverter {

    private static final Logger LOGGER = LoggerFactory.getLogger(WordConverterImpl.class);

    @Override
    public void convertToPDF(String p_SourcePath, String p_OutputPath) {
        File wordFile = new File(p_SourcePath);
        File targetFile = new File(p_OutputPath);
        /*IConverter converter = new Local
        Future<Boolean> conversion =*/
    }

    @Override
    public void convertToParticularForm(String p_SourcePath, String p_OutputPath, String p_CharSet) {
        XWPFDocument document = new XWPFDocument();
        PdfReader pdfReader = null;
        try {
            pdfReader = new PdfReader(p_SourcePath);
        } catch (IOException e) {
            LOGGER.error("Error Reading PDF File {}", e.toString());
        }
        PdfReaderContentParser pdfReaderContentParser = new PdfReaderContentParser(pdfReader);
        if (pdfReader != null) {
            for (int a=1; a<= pdfReader.getNumberOfPages(); a++) {
                TextExtractionStrategy extractionStrategy = null;
                try {
                    extractionStrategy = pdfReaderContentParser.processContent(a, new SimpleTextExtractionStrategy());
                } catch (IOException e) {
                    LOGGER.error("Error Extracting PDF Document {}", e.toString());
                }
                String text = null;
                if (extractionStrategy != null) {
                    text = extractionStrategy.getResultantText();
                }
                XWPFParagraph xwpfParagraph = document.createParagraph();
                XWPFRun xwpfRun = xwpfParagraph.createRun();
                xwpfRun.setText(text);
                xwpfRun.addBreak(BreakType.PAGE);
            }
        }
        FileOutputStream fileOutputStream = null;
        try {
            fileOutputStream = new FileOutputStream(p_OutputPath);
        } catch (FileNotFoundException e) {
            LOGGER.error("Error create output stream {}",e.toString());
        }
        try {
            document.write(fileOutputStream);
        } catch (IOException e) {
            LOGGER.error("Error create document {} ",e.toString());
        }
        try {
            if (fileOutputStream != null) {
                fileOutputStream.close();
            }
        } catch (IOException e) {
            LOGGER.error("Error closing output stream {}", e.toString());
        }
        pdfReader.close();
        try {
            document.close();
        } catch (IOException e) {
            LOGGER.error("Error closing document {}", e.toString());
        }
    }
}
