package com.fjrotgerl.checkinonline.services;

import com.itextpdf.html2pdf.HtmlConverter;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.io.FileOutputStream;
import java.io.IOException;

import org.primeframework.transformer.domain.Document;
import org.primeframework.transformer.service.BBCodeParser;
import org.primeframework.transformer.service.BBCodeToHTMLTransformer;
import org.primeframework.transformer.service.Transformer;

@Service
public class BBCodeEncoder {

    private String message;

    public void exportToPdf(String pdfName) {

        Document document = new BBCodeParser().buildDocument(this.message, null);
        String html = new BBCodeToHTMLTransformer().transform(document, (node) -> {
            // transform predicate, returning false will cause this node to not be transformed
            return true;
        }, new Transformer.TransformFunction.HTMLTransformFunction(), null);

        try {
            HtmlConverter.convertToPdf(html, new FileOutputStream("src/main/resources/static/pdfs/" + pdfName + ".pdf"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
