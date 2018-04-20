package com.zisal.springbootpdfconverter.service.impl;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.tool.xml.XMLWorkerHelper;
import com.zisal.springbootpdfconverter.service.IHTMLConverter;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.fit.pdfdom.PDFDomTree;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.xml.parsers.ParserConfigurationException;
import java.io.*;

/**
 * Created on 4/13/18.
 *
 * @author <a href="mailto:fauzi.knightmaster.achmad@gmail.com">Achmad Fauzi</a>
 */
@Service
public class HTMLConverterImpl implements IHTMLConverter {

    private static final Logger LOGGER = LoggerFactory.getLogger(HTMLConverterImpl.class);

    @Override
    public void convertToPDF(String p_SourcePath, String p_OutputPath) {
        Document document = new Document();
        PdfWriter writer = null;
        try {
            writer = PdfWriter.getInstance(document, new FileOutputStream(p_OutputPath));
        } catch (DocumentException | FileNotFoundException e) {
            LOGGER.error("Error Instantiating Writer ".concat(e.toString()));
        }
        document.open();
        if (writer != null) {
            try {
                XMLWorkerHelper.getInstance().parseXHtml(writer, document, new FileInputStream(p_SourcePath));
            } catch (IOException e) {
                LOGGER.error("Error Parsing XML ".concat(e.toString()));
            }
        }
        document.close();
    }

    @Override
    public void convertToParticularForm(String p_SourcePath, String p_OutputPath, String p_CharSet) {
        PDDocument pdf = null;
        try {
            pdf = PDDocument.load(new File(p_SourcePath));
        } catch (IOException e) {
            LOGGER.error("Error Loading Document ".concat(e.toString()));
        }
        Writer output = null;
        try {
            output = new PrintWriter(p_OutputPath, p_CharSet);
        } catch (FileNotFoundException | UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        if (pdf != null && output != null) {
            try {
                new PDFDomTree().writeText(pdf, output);
            } catch (IOException | ParserConfigurationException e) {
                LOGGER.error("Error Create Output ".concat(e.toString()));
            }
        }
        if (output != null) {
            try {
                output.close();
            } catch (IOException e) {
                LOGGER.error("Error closing Output IO ".concat(e.toString()));
            }
        }
        if (pdf != null) {
            try {
                pdf.close();
            } catch (IOException e) {
                LOGGER.error("Error Closing Output IO {}", e.toString());
            }
        }
    }
}
