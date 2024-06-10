package com.example.StudentDetails.Controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.example.StudentDetails.Helper.PDFGenerator;
import com.example.StudentDetails.Model.Student;
import com.example.StudentDetails.Service.PdfService;
import com.lowagie.text.DocumentException;

import jakarta.servlet.http.HttpServletResponse;

@RestController
public class PdfController {

	@Autowired
	private PdfService service;
  
    @GetMapping("/getstudentdetail/pdf")
	public void generatePdf(HttpServletResponse response) throws DocumentException, IOException {
		
		response.setContentType("application/pdf");
		String headerkey = "Content-Disposition";
		String headervalue = "attachment; filename=pdf_" + "Students" + ".pdf";
		response.setHeader(headerkey, headervalue);
		
		List<Student> studentList = service.findAllStudents();
		
		PDFGenerator generator = new PDFGenerator();
		generator.generate(studentList,response);
		
	}
    
    @GetMapping("/getstudentdetail/pdf/{student_id}")
	public void generatePdf(@PathVariable int student_id, HttpServletResponse response) throws DocumentException, IOException {
		
		response.setContentType("application/pdf");
		String headerkey = "Content-Disposition";
		String headervalue = "attachment; filename=" + "Student" + ".pdf";
		response.setHeader(headerkey, headervalue);
		
		Student student = service.findStudentById(student_id);
		List<Student> studentList = new ArrayList<>();
		studentList.add(student);
		
		PDFGenerator generator = new PDFGenerator();
		generator.generate(studentList,response);
		
	}   
    
    @PostMapping(path= "/pdf/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> classify( @RequestParam("file") final MultipartFile pdfFile){
        
    	final String password = "owner";
    	try {
    		PDFGenerator generator = new PDFGenerator();
			return ResponseEntity.ok().body(generator.extractTextFromPDF(pdfFile,password));
		} catch (IOException e) {
			
			return ResponseEntity.badRequest().body(e.getMessage());
		}
    }
}