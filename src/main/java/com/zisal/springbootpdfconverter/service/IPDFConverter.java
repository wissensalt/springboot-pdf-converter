package com.zisal.springbootpdfconverter.service;

/**
 * Created on 4/13/18.
 *
 * @author <a href="mailto:fauzi.knightmaster.achmad@gmail.com">Achmad Fauzi</a>
 */
public interface IPDFConverter {

    void convertToPDF(String p_SourcePath, String p_OutputPath);

    void convertToParticularForm(String p_SourcePath, String p_OutputPath, String p_CharSet);
}
