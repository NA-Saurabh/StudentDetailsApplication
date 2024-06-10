package com.example.StudentDetails.Helper;

import java.io.IOException;
import java.util.List;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.springframework.web.multipart.MultipartFile;

import com.example.StudentDetails.Model.Student;
import com.lowagie.text.BadElementException;
import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Element;
import com.lowagie.text.Font;
import com.lowagie.text.FontFactory;
import com.lowagie.text.Image;
import com.lowagie.text.PageSize;
import com.lowagie.text.Phrase;
import com.lowagie.text.Rectangle;
import com.lowagie.text.pdf.CMYKColor;
import com.lowagie.text.pdf.ColumnText;
import com.lowagie.text.pdf.PdfContentByte;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfPageEventHelper;
import com.lowagie.text.pdf.PdfWriter;

import jakarta.servlet.http.HttpServletResponse;

public class PDFGenerator extends PdfPageEventHelper {

    private int pageNumber = 0;
//    private String userPassword = "123456";
//    private String ownerPassword = "owner";
    
    @Override
    public void onStartPage(PdfWriter writer, Document document) {
    	
        PdfContentByte canvas = writer.getDirectContent();
        Rectangle pageSize = document.getPageSize();
        Image img;
		try {
			img = Image.getInstance("/home/saurabhpandey/Downloads/download.png");
			img.scaleAbsolute(50, 50);
	        img.setAbsolutePosition(250, 500);
	        document.add(img);
		} catch (BadElementException | IOException e) {
			
			e.printStackTrace();
		}
		Font font = FontFactory.getFont(FontFactory.TIMES_ROMAN, 16, Font.BOLD | Font.UNDERLINE);
        Phrase header = new Phrase(String.format("LIST OF STUDENTS"), font);
        ColumnText.showTextAligned(canvas, Element.ALIGN_CENTER, header, 
                                    (pageSize.getLeft() + pageSize.getRight()) / 2, 
                                    pageSize.getTop() - 50, 0);
    }

    public void generate(List<Student> studentList, HttpServletResponse response) throws DocumentException, IOException {

        Document document = new Document(PageSize.A4,  36, 36, 72, 36);
        PdfWriter writer = PdfWriter.getInstance(document, response.getOutputStream());
        writer.setPageEvent(this);
//        writer.setEncryption(
//                userPassword.getBytes(),
//                ownerPassword.getBytes(),
//                PdfWriter.ALLOW_PRINTING | PdfWriter.ALLOW_COPY,
//                PdfWriter.ENCRYPTION_AES_128
//        );
        document.open();

        Font fontTitle = FontFactory.getFont(FontFactory.TIMES_ROMAN);
        fontTitle.setSize(16);

        PdfPTable table = new PdfPTable(12);

        table.setWidthPercentage(100f);
        table.setWidths(new int[]{1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1});
        table.setSpacingBefore(30f);
        table.setSpacingAfter(10f);
        table.setHeaderRows(0);

        PdfPCell cell = new PdfPCell();
        cell.setBackgroundColor(CMYKColor.gray);
        cell.setPadding(5);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);

        Font font = FontFactory.getFont(FontFactory.TIMES_ROMAN);
        font.setColor(CMYKColor.WHITE);

        cell.setPhrase(new Phrase("studentId", font));
        table.addCell(cell);
        cell.setPhrase(new Phrase("name", font));
        table.addCell(cell);
        cell.setPhrase(new Phrase("registerNo", font));
        table.addCell(cell);
        cell.setPhrase(new Phrase("gender", font));
        table.addCell(cell);
        cell.setPhrase(new Phrase("age", font));
        table.addCell(cell);
        cell.setPhrase(new Phrase("emailId", font));
        table.addCell(cell);
        cell.setPhrase(new Phrase("phoneNo", font));
        table.addCell(cell);
        cell.setPhrase(new Phrase("currentStatus", font));
        table.addCell(cell);
        cell.setPhrase(new Phrase("course", font));
        table.addCell(cell);
        cell.setPhrase(new Phrase("batch", font));
        table.addCell(cell);
        cell.setPhrase(new Phrase("fees", font));
        table.addCell(cell);
        cell.setPhrase(new Phrase("classId", font));
        table.addCell(cell);
        
        table.setHeaderRows(1);

        for (Student student : studentList) {

            table.addCell(String.valueOf(student.getStudentId()));
            table.addCell(student.getName());
            table.addCell(String.valueOf(student.getRegisterNo()));
            table.addCell(student.getGender());
            table.addCell(String.valueOf(student.getAge()));
            table.addCell(student.getEmailId());
            table.addCell(student.getPhoneNo());
            table.addCell(student.getCurrentStatus());
            table.addCell(student.getCourse());
            table.addCell(student.getBatch());
            table.addCell(String.valueOf(student.getFees()));
            table.addCell(String.valueOf(student.getClassId()));
        }

        document.add(table);
        document.close();
    }
    
    @Override
    public void onEndPage(PdfWriter writer, Document document) {
        pageNumber++;
        PdfContentByte canvas = writer.getDirectContent();
        Rectangle pageSize = document.getPageSize();
        Phrase footer = new Phrase(String.format("Page %d", pageNumber));
        ColumnText.showTextAligned(canvas, Element.ALIGN_CENTER, footer, 
                                    (pageSize.getLeft() + pageSize.getRight()) / 2, 
                                    pageSize.getBottom() + 20, 0);
    }

    public String extractTextFromPDF(MultipartFile multipartFile, String password) throws IOException {
        try (PDDocument document = PDDocument.load(multipartFile.getInputStream(), password)) {

            if (document.isEncrypted()) {
                document.setAllSecurityToBeRemoved(true);
            }

            PDFTextStripper pdfTextStripper = new PDFTextStripper();
            return pdfTextStripper.getText(document);
        }
    }
}