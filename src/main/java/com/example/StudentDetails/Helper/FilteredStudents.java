package com.example.StudentDetails.Helper;

import org.springframework.data.jpa.domain.Specification;

import com.example.StudentDetails.Model.Student;
import com.example.StudentDetails.Model.StudentFilter;

public class FilteredStudents {
	
    public static final String COURSE = "course";
    public static final String BATCH = "batch";
    public static final String NAME = "name";
    public static final String PHONE_NO = "phoneNo";

    private FilteredStudents() {
        
    }

    public static Specification<Student> filterBy(StudentFilter studentFilter) {
        return Specification
                .where(hasCourse(studentFilter.getCourse()))
                .and(hasBatch(studentFilter.getBatch()))
                .and(hasName(studentFilter.getName()))
                .and(hasPhoneNo(studentFilter.getPhoneNo()));
    }

    private static Specification<Student> hasCourse(String course) {
        return ((root, query, cb) -> course == null || course.isEmpty() ? cb.conjunction() : cb.equal(root.get(COURSE), course));
    }

    private static Specification<Student> hasBatch(String batch) {
        return (root, query, cb) -> batch == null ? cb.conjunction() : cb.equal(root.get(BATCH), batch);
    }

    private static Specification<Student> hasName(String name) {
        return (root, query, cb) -> name == null ? cb.conjunction() : cb.equal(root.get(NAME), name);
    }

    private static Specification<Student> hasPhoneNo(String phoneNo) {
        return (root, query, cb) -> phoneNo == null ? cb.conjunction() : cb.equal(root.get(PHONE_NO), phoneNo);
    }
}