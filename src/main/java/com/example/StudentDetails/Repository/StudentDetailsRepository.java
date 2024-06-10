package com.example.StudentDetails.Repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.lang.NonNull;

import com.example.StudentDetails.Model.Student;

import jakarta.annotation.Nullable;

public interface StudentDetailsRepository  extends JpaRepository<Student, Integer>, JpaSpecificationExecutor<Student>{

	Page<Student> findAll(@Nullable Specification<Student> spec, @NonNull Pageable pageable);
	
	List<Student> findByNameContains(String name, Pageable pageable);
	
	List<Student> findByGenderContains(String gender, Pageable pageable);
	
	List<Student> findByRegisterNoEquals(int registerNo, Pageable pageable);
	
	List<Student> findByEmailIdContains(String email, Pageable pageable);
	
	List<Student> findByPhoneNoContains(String phoneNo, Pageable pageable);
	
	List<Student> findByCurrentStatusContains(String status, Pageable pageable);
	
	List<Student> findByCourseContains(String course, Pageable pageable);
	
	List<Student> findByBatchContains(String batch, Pageable pageable);

	List<Student> findByAgeEquals(int age, Pageable pageable);
	
	List<Student> findByFeesEquals(int fees, Pageable pageable);
	
	List<Student> findByClassIdEquals(int classId, Pageable pageable);

	Student findByStudentId(int studentId);
	
}
