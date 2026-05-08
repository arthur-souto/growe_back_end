package br.com.growe.growe_backend.service;

import br.com.growe.growe_backend.exceptions.BusinessException;
import lombok.RequiredArgsConstructor;
import org.apache.pdfbox.Loader;
import org.apache.pdfbox.io.RandomAccessReadBuffer;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.apache.poi.xwpf.extractor.XWPFWordExtractor;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

@Service
@RequiredArgsConstructor
public class ExtractService {

    public String extractTextFromFile(MultipartFile file) {

        if(file == null) {
            throw new BusinessException("File is null", HttpStatus.BAD_REQUEST, "NULL_EXCEPTION");
        }

        var filename = file.getOriginalFilename();

        if(filename == null) {
            throw new BusinessException("File Name is null", HttpStatus.BAD_REQUEST, "NULL_EXCEPTION");
        }

        try {
            if (filename.toLowerCase().endsWith(".pdf")) {
                try (PDDocument doc = Loader.loadPDF(
                        new RandomAccessReadBuffer(file.getBytes()))) {
                    PDFTextStripper stripper = new PDFTextStripper();
                    stripper.setSortByPosition(true);
                    return stripper.getText(doc);
                }
            } else if (filename.toLowerCase().endsWith(".docx")) {
                try (XWPFDocument doc = new XWPFDocument(file.getInputStream());
                     XWPFWordExtractor extractor = new XWPFWordExtractor(doc)) {
                    return extractor.getText();
                }
            } else {
                return new String(file.getBytes(), StandardCharsets.UTF_8);
            }
        } catch (IOException e) {
            throw new BusinessException("Failed to read file", HttpStatus.BAD_REQUEST, "ERROR_FILE");
        }
    }
}
