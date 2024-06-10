package com.example.StudentDetails.Helper;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import com.example.StudentDetails.Model.ClassModel;
import com.example.StudentDetails.Model.Student;

@Component
public class ExcelHelper {
	
	public static String TYPE = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";
	static String[] HEADERs = { "studentId", "name", "registerNo", "gender", "age", "emailId", "phoneNo", "currentStatus", "course", "batch", "fees", "classId" };
	static String[] CLASS_HEADERs = {"classId","studentId","name","registerNo","examId","examName"};
	static String CLASS_SHEET = "class";
	static String SHEET = "students";

	public static ByteArrayInputStream studentsToExcel(List<Student> students) {

		try (Workbook workbook = new XSSFWorkbook(); 
			 ByteArrayOutputStream out = new ByteArrayOutputStream();) {
			
			Sheet sheet = (Sheet) workbook.createSheet(SHEET);

			Row headerRow = ((XSSFSheet) sheet).createRow(0);

			for (int col = 0; col < HEADERs.length; col++) {
				Cell cell = headerRow.createCell(col);
				cell.setCellValue(HEADERs[col]);
			}

			int rowIdx = 1;
			for (Student student : students) {
				Row row = ((XSSFSheet) sheet).createRow(rowIdx++);

				row.createCell(0).setCellValue(student.getStudentId());
				row.createCell(1).setCellValue(student.getName());
				row.createCell(2).setCellValue(student.getRegisterNo());
				row.createCell(3).setCellValue(student.getGender());
				row.createCell(4).setCellValue(student.getAge());
				row.createCell(5).setCellValue(student.getEmailId());
				row.createCell(6).setCellValue(student.getPhoneNo());
				row.createCell(7).setCellValue(student.getCurrentStatus());
				row.createCell(8).setCellValue(student.getCourse());
				row.createCell(9).setCellValue(student.getBatch());
				row.createCell(10).setCellValue(student.getFees());
				row.createCell(11).setCellValue(student.getClassId());
			}

			workbook.write(out);
			return new ByteArrayInputStream(out.toByteArray());
		
		}catch (IOException e) {
		
			throw new RuntimeException("fail to import data to Excel file: " + e.getMessage());
		}
	}
	
	public static boolean hasExcelFormat(MultipartFile file) {
		
	    if (!TYPE.equals(file.getContentType())) {
	    	return false;
	    }

	    return true;
	}

	public static List<Student> excelToStudent(InputStream is) {
		try {
			
			DataFormatter formatter = new DataFormatter();

			Workbook workbook = new XSSFWorkbook(is);

			Sheet sheet = workbook.getSheet(SHEET);
			Iterator<Row> rows = sheet.iterator();

			List<Student> students = new ArrayList<Student>();

			int rowNumber = 0;
			while (rows.hasNext()) {
				Row currentRow = rows.next();

				if (rowNumber == 0) {
					rowNumber++;
					continue;
				}

				Iterator<Cell> cellsInRow = currentRow.iterator();

				Student student = new Student();

				int cellIdx = 0;
				while (cellsInRow.hasNext()) {
					Cell currentCell = cellsInRow.next();

					switch (cellIdx) {
					case 0:
						String name = formatter.formatCellValue(currentCell);
						student.setName(name);
						break;

					case 1:
						String registerNoAsString = formatter.formatCellValue(currentCell);
				        int registerNo = Integer.parseInt(registerNoAsString);
						student.setRegisterNo(registerNo);
						break;

					case 2:
						String gender = formatter.formatCellValue(currentCell);
						student.setGender(gender);
						break;
	            
					case 3:
						String ageAsString = formatter.formatCellValue(currentCell);
				        int age = Integer.parseInt(ageAsString);
						student.setAge(age);
						break;
						
					case 4:
						String emailId = formatter.formatCellValue(currentCell);
						student.setEmailId(emailId);
						break;

					case 5:
						String phoneNo = formatter.formatCellValue(currentCell);
						student.setPhoneNo(phoneNo);
						break;

					case 6:
						String currentStatus = formatter.formatCellValue(currentCell);
						student.setCurrentStatus(currentStatus);
						break;

					case 7:
						String course = formatter.formatCellValue(currentCell);
						student.setCourse(course);
						break;
	            
					case 8:
						String batch = formatter.formatCellValue(currentCell);
						student.setBatch(batch);
						break;
						
					case 9:
						String feesAsString = formatter.formatCellValue(currentCell);
				        int fees = Integer.parseInt(feesAsString);
						student.setFees(fees);
						break;

					case 10:
						String classIdAsString = formatter.formatCellValue(currentCell);
				        int classId = Integer.parseInt(classIdAsString);
						student.setClassId(classId);
						break;

					default:
						break;
					}

					cellIdx++;
				}

				students.add(student);
			}

			workbook.close();
			return students;
	    
	    } catch (IOException e) {
	    	
	    	throw new RuntimeException("fail to parse Excel file: " + e.getMessage());
	    }
	}
	
	public static ByteArrayInputStream classStudentsToExcel(List<ClassModel> classModels) {

		try (Workbook workbook = new XSSFWorkbook(); ByteArrayOutputStream out = new ByteArrayOutputStream();) {
			Sheet sheet = (Sheet) workbook.createSheet(SHEET);

			// Header
			Row headerRow = ((XSSFSheet) sheet).createRow(0);

			for (int col = 0; col < CLASS_HEADERs.length; col++) {
				Cell cell = headerRow.createCell(col);
				cell.setCellValue(CLASS_HEADERs[col]);
			}

			int rowIdx = 1;
			for (ClassModel classModel : classModels) {
				
				Row row = ((XSSFSheet) sheet).createRow(rowIdx++);
				
				row.createCell(0).setCellValue(classModel.getClassId());
				row.createCell(1).setCellValue(classModel.getStudentId());
				row.createCell(2).setCellValue(classModel.getName());
				row.createCell(3).setCellValue(classModel.getRegisterNo());
				row.createCell(4).setCellValue(classModel.getExamId());
				row.createCell(5).setCellValue(classModel.getExamName());
			}

			workbook.write(out);
			return new ByteArrayInputStream(out.toByteArray());
		
		}catch (IOException e) {
		
			throw new RuntimeException("fail to import data to Excel file: " + e.getMessage());
		}
	}
	
	public static ByteArrayInputStream addErrorColumnToExcel(InputStream is) {
        try (Workbook workbook = new XSSFWorkbook(is);
             ByteArrayOutputStream out = new ByteArrayOutputStream()) {

            Sheet sheet = workbook.getSheet(SHEET);

            Row headerRow = sheet.getRow(0);
            headerRow.createCell(HEADERs.length).setCellValue("Error");

            Iterator<Row> rowIterator = sheet.iterator();
            int rowIndex = 0;
            while (rowIterator.hasNext()) {
                Row row = rowIterator.next();
                if (rowIndex == 0) { 
                    rowIndex++;
                    continue;
                }
                Cell errorCell = row.createCell(HEADERs.length);
                String errorMessage = validateRow(row); 
                errorCell.setCellValue(errorMessage);
                rowIndex++;
            }

            workbook.write(out);
            return new ByteArrayInputStream(out.toByteArray());

        } catch (IOException e) {
            throw new RuntimeException("Failed to add error column to Excel file: " + e.getMessage());
        }
    }

    private static String validateRow(Row row) {
    	
    	DataFormatter formatter = new DataFormatter();
        StringBuilder errorMessage = new StringBuilder();

        Cell genderCell = row.getCell(3); 
        String gender = genderCell == null ? "" : genderCell.getStringCellValue();
        

        Cell phoneCell = row.getCell(6);
        String phoneNo = phoneCell == null ? "" : formatter.formatCellValue(phoneCell);
       
        if (!gender.equalsIgnoreCase("male") && !gender.equalsIgnoreCase("female")) {
            errorMessage.append("Invalid gender. ");
        }else if (phoneNo.length() != 10) {
            errorMessage.append("Invalid phone number length. ");
        }

        return errorMessage.toString().trim();
    }
	
}
