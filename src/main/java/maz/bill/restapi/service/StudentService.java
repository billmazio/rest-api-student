package maz.bill.restapi.service;


import maz.bill.restapi.entity.Address;
import maz.bill.restapi.entity.Student;
import maz.bill.restapi.entity.Subject;
import maz.bill.restapi.repository.AddressRepository;
import maz.bill.restapi.repository.StudentRepository;
import maz.bill.restapi.repository.SubjectRepository;
import maz.bill.restapi.request.CreateStudentRequest;
import maz.bill.restapi.request.CreateSubjectRequest;
import maz.bill.restapi.request.InQueryRequest;
import maz.bill.restapi.request.UpdateStudentRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;


import java.util.ArrayList;
import java.util.List;

@Service
public class StudentService {

    @Autowired
    StudentRepository studentRepository;

    @Autowired
    AddressRepository addressRepository;

    @Autowired
    SubjectRepository subjectRepository;

    public List<Student> getAllStudents () {
        return studentRepository.findAll();
    }

    public Student createStudent (CreateStudentRequest createStudentRequest) {
        Student student = new Student(createStudentRequest);

        Address address = new Address();
        address.setStreet(createStudentRequest.getStreet());
        address.setCity(createStudentRequest.getCity());

        address = addressRepository.save(address);

        student.setAddress(address);
        student = studentRepository.save(student);

        List<Subject> subjectsList = new ArrayList<Subject>();

        if(createStudentRequest.getSubjectsLearning() != null) {
            for (CreateSubjectRequest createSubjectRequest :
                    createStudentRequest.getSubjectsLearning()) {
                Subject subject = new Subject();
                subject.setSubjectName(createSubjectRequest.getSubjectName());
                subject.setMarksObtained(createSubjectRequest.getMarksObtained());
                subject.setStudent(student);

                subjectsList.add(subject);
            }

            subjectRepository.saveAll(subjectsList);

        }

        student.setLearningSubjects(subjectsList);

        return student;
    }

    public Student updateStudent(UpdateStudentRequest updateStudentRequest) {
        Student student = studentRepository.findById(updateStudentRequest.getId()).orElse(null);

        if (student != null) {
            if (updateStudentRequest.getFirstName() != null && !updateStudentRequest.getFirstName().isEmpty()) {
                student.setFirstName(updateStudentRequest.getFirstName());
            }

            if (updateStudentRequest.getLastName() != null && !updateStudentRequest.getLastName().isEmpty()) {
                student.setLastName(updateStudentRequest.getLastName());
            }

            if (updateStudentRequest.getEmail() != null && !updateStudentRequest.getEmail().isEmpty()) {
                student.setEmail(updateStudentRequest.getEmail());
            }

            student = studentRepository.save(student);
        }

        return student;
    }

    public String deleteStudent (long id) {
        studentRepository.deleteById(id);
        return "Student has been deleted successfully";
    }

    public List<Student> getByFirstName (String firstName) {
        return studentRepository.findByFirstName(firstName);
    }

    public Student getByFirstNameAndLastName (String firstName, String lastName) {
        //return studentRepository.findByLastNameAndFirstName(lastName, firstName);
        return studentRepository.getByLastNameAndFirstName(lastName, firstName);
    }

    public List<Student> getByFirstNameOrLastName (String firstName, String lastName) {
        return studentRepository.findByFirstNameOrLastName(firstName, lastName);
    }

    public List<Student> getByFirstNameIn (InQueryRequest inQueryRequest) {
        return studentRepository.findByFirstNameIn(inQueryRequest.getFirstNames());
    }

    public List<Student> getAllStudentsWithPagination (int pageNo, int pageSize) {
        Pageable pageable = PageRequest.of(pageNo - 1, pageSize);

        return studentRepository.findAll(pageable).getContent();
    }

    public List<Student> getAllStudentsWithSorting () {
        Sort sort = Sort.by(Sort.Direction.ASC, "firstName", "lastName", "email");

        return studentRepository.findAll(sort);
    }

    public Integer updateStudentWithJpql (Long id, String firstName) {
        return studentRepository.updateFirstName(id, firstName);
    }

    public Integer deleteStudent (String firstName) {
        return studentRepository.deleteByFirstName(firstName);
    }

    public List<Student> getByCity (String city) {
        return studentRepository.findByAddressCity(city);
    }
}
