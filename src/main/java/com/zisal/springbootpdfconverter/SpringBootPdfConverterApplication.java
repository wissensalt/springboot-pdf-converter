package com.zisal.springbootpdfconverter;

import com.zisal.springbootpdfconverter.service.IHTMLConverter;
import com.zisal.springbootpdfconverter.service.IImageConverter;
import com.zisal.springbootpdfconverter.service.ITextConverter;
import com.zisal.springbootpdfconverter.service.IWordConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class SpringBootPdfConverterApplication {

	@Autowired
	private IHTMLConverter htmlConverter;

	@Autowired
	private IWordConverter wordConverter;

	@Autowired
	private IImageConverter imageConverter;

	@Autowired
	private ITextConverter textConverter;

	public static void main(String[] args) {
		SpringApplication.run(SpringBootPdfConverterApplication.class, args);
	}

	private final String STORAGE_PATH = "/home/fauzi/pdf/";
	private String SOURCE_PDF = STORAGE_PATH+"EXAMPLE_PDF.pdf";
	private String OUTPUT_HTML = STORAGE_PATH+"output.html";
	private String OUTPUT_PDF = STORAGE_PATH+"output.pdf";
	private String OUTPUT_TEXT = STORAGE_PATH+"output.txt";
	private String OUTPUT_WORD = STORAGE_PATH+"output.doc";
	private String OUTPUT_IMAGE = STORAGE_PATH+"output";
	private String CHARSET = "utf-8";

	@Bean
	CommandLineRunner runnerHTML() {
		return strings -> {
			htmlConverter.convertToParticularForm(SOURCE_PDF, OUTPUT_HTML, CHARSET);
			htmlConverter.convertToPDF(OUTPUT_HTML, OUTPUT_PDF);
		};
	}

	@Bean
	CommandLineRunner runnerImage() {
		return strings -> {
			imageConverter.convertToParticularForm(SOURCE_PDF, OUTPUT_IMAGE, "");
			imageConverter.convertToPDF(OUTPUT_IMAGE.concat(".jpg"), OUTPUT_PDF);
		};
	}

	@Bean
	CommandLineRunner runnerText() {
		return strings -> {
			textConverter.convertToParticularForm(SOURCE_PDF, OUTPUT_TEXT, "");
			textConverter.convertToPDF(OUTPUT_TEXT, OUTPUT_PDF);
		};
	}

	@Bean
	CommandLineRunner runnerWord() {
		return strings -> wordConverter.convertToParticularForm(SOURCE_PDF, OUTPUT_WORD, "utf-8");
	}
}
