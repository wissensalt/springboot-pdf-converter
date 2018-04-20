package com.zisal.springbootpdfconverter.service.impl;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Image;
import com.itextpdf.text.pdf.PdfWriter;
import com.zisal.springbootpdfconverter.service.IImageConverter;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.ImageType;
import org.apache.pdfbox.rendering.PDFRenderer;
import org.apache.pdfbox.tools.imageio.ImageIOUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.awt.image.BufferedImage;
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
public class ImageConverterImpl implements IImageConverter {

    private static final Logger LOGGER = LoggerFactory.getLogger(ImageConverterImpl.class);

    @Override
    public void convertToPDF(String p_SourcePath, String p_OutputPath) {
        Document document = new Document();
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(p_OutputPath);
        } catch (FileNotFoundException e) {
            LOGGER.error("Error Output Stream {}", e.toString());
        }
        PdfWriter writer = null;
        try {
            writer = PdfWriter.getInstance(document, fos);
        } catch (DocumentException e) {
            LOGGER.error("Error instantiating PDF {}", e.toString());
        }
        if (writer != null) {
            writer.open();
        }
        document.open();
        try {
            document.add(Image.getInstance(p_SourcePath));
        } catch (DocumentException | IOException e) {
            LOGGER.error("Error Manipulating Document Image {}", e.toString());
        }
        document.close();
        if (writer != null) {
            writer.close();
        }
    }

    @Override
    public void convertToParticularForm(String p_SourcePath, String p_OutputPath, String p_CharSet) {
        PDDocument document = null;
        try {
            document = PDDocument.load(new File(p_SourcePath));
        } catch (IOException e) {
            LOGGER.error("Can not load PDF Document {}", e.toString());
        }
        if (document != null) {
            PDFRenderer pdfRenderer = new PDFRenderer(document);
            for (int a=0; a<document.getNumberOfPages(); a++) {
                BufferedImage bufferedImage = null;
                try {
                    bufferedImage = pdfRenderer.renderImageWithDPI(a, 300, ImageType.RGB);
                } catch (IOException e) {
                    LOGGER.error("Error Buffering Image {}", e.toString());
                }
                if (bufferedImage != null) {
                    try {
                        ImageIOUtil.writeImage(bufferedImage,
                                String.format(p_OutputPath.concat(".%s"), "jpg"),
                                300
                        );
                    } catch (IOException e) {
                        LOGGER.error("Error Writing Image {}", e.toString());
                    }
                }
            }
        }
    }
}
