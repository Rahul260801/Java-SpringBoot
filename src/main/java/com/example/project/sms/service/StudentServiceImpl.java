package com.example.project.sms.service;

import com.example.project.sms.dto.AddressResponseDto;
import com.example.project.sms.dto.CourseResponseDto;
import com.example.project.sms.dto.StudentRequestDto;
import com.example.project.sms.dto.StudentResponseDto;
import com.example.project.sms.entity.Address;
import com.example.project.sms.entity.Course;
import com.example.project.sms.entity.Department;
import com.example.project.sms.entity.Student;
import com.example.project.sms.exception.DuplicateEmailException;
import com.example.project.sms.exception.ResourceNotFoundException;
import com.example.project.sms.exception.StudentNotFoundException;
import com.example.project.sms.repository.CourseRepository;
import com.example.project.sms.repository.DepartmentRepository;
import com.example.project.sms.repository.StudentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

import static java.util.Arrays.stream;

@RequiredArgsConstructor
@Service
public class StudentServiceImpl implements StudentService{
    @Autowired
    private final StudentRepository studentRepository;
    private final DepartmentRepository departmentRepository;
    private final CourseRepository courseRepository;


    @Override
    public StudentResponseDto getId(Long id) {
        Student student = studentRepository.findById(id).orElseThrow(()-> new StudentNotFoundException("student not found by id"));
        return mapToResponse(student);
    }

    @Override
    public List<StudentResponseDto> getAll() {
        return studentRepository.findAll().stream().map(this::mapToResponse).collect(Collectors.toList());
    }

    @Override
    public StudentResponseDto saveStud(StudentRequestDto dto) {
        if(studentRepository.existsByEmail(dto.getEmail())){
            throw new DuplicateEmailException("Student already exist");
        }
        //Address entity
        Address address =new Address();
        address.setCity(dto.getAddressRequestDto().getCity());
        address.setState(dto.getAddressRequestDto().getState());
        address.setCountry(dto.getAddressRequestDto().getCountry());
        // Dept entity
        Department dept =departmentRepository.findById(dto.getDepartmentId()).orElseThrow(()->
                new ResourceNotFoundException("dept not found")
        );
        //Course entity
        List<Course> courses = courseRepository.findAllById(dto.getCourseId());

        //Student entity
        Student student = new Student();
        student.setFirstName(dto.getFirstName());
        student.setLastName(dto.getLastName());
        student.setNumber(dto.getNumber());
        student.setEmail(dto.getEmail());
        student.setAddress(address);

        student.setDepartment(dept);

        student.setCourses(courses);
        Student saveStu = studentRepository.save(student);
      return   mapToResponse(saveStu);
    }

    @Override
    public StudentResponseDto updateStud(Long id , StudentRequestDto dto) {
        Student exsStu = studentRepository.findById(id).orElseThrow(() -> new StudentNotFoundException("student not found"));

            exsStu.setFirstName(dto.getFirstName());
            exsStu.setLastName(dto.getLastName());
            exsStu.setEmail(dto.getEmail());
            exsStu.setNumber(dto.getNumber());
            Address address = exsStu.getAddress();
            if (address == null){
                address = new Address();
            }
            address.setCity(dto.getAddressRequestDto().getCity());
            address.setState(dto.getAddressRequestDto().getState());
            address.setCountry(dto.getAddressRequestDto().getCountry());

            Department dept = departmentRepository.findById(dto.getDepartmentId()).orElseThrow(()-> new ResourceNotFoundException("id not found"));
            exsStu.setDepartment(dept);
            exsStu.setAddress(address);

            Student updStudent = studentRepository.save(exsStu);
            return mapToResponse(updStudent);
    }

    @Override
    public void deleteById(Long id) {
        Student student = studentRepository.findById(id).orElseThrow(()->new StudentNotFoundException("student not found"));
        studentRepository.deleteById(id);
    }

    @Override
    public StudentResponseDto partUpdate(StudentRequestDto dto,Long id) {
        Student exsStu = studentRepository.findById(id).orElseThrow(() -> new StudentNotFoundException("not found"));
      
        if (dto.getNumber() !=null){
            exsStu.setNumber(dto.getNumber());
        }
        if (dto.getEmail()!=null){
            exsStu.setEmail(dto.getEmail());
        }
        if (dto.getAddressRequestDto()!=null){
           Address address = exsStu.getAddress();
            if (address == null){
                address = new Address();
            }
           if (dto.getAddressRequestDto().getCity() !=null){
               address.setCity(dto.getAddressRequestDto().getCity());
           }
            if (dto.getAddressRequestDto().getState() !=null){
                address.setState(dto.getAddressRequestDto().getState());
            }
            if (dto.getAddressRequestDto().getCountry() !=null){
                address.setCountry(dto.getAddressRequestDto().getCountry());
            }
            exsStu.setAddress(address);
        }

        if (dto.getDepartmentId()!=null) {
            Department dept = departmentRepository.findById(dto.getDepartmentId()).orElseThrow(() -> new StudentNotFoundException("not found"));
            exsStu.setDepartment(dept);
        }

        Student parUpd = studentRepository.save(exsStu);
        return mapToResponse(parUpd);
    }

    @Override
    public StudentResponseDto getStudentByEmail(String email) {
        Student student =studentRepository.findByEmail(email).orElseThrow(()->
             new ResourceNotFoundException("Student not found")
        );
            return mapToResponse(student);
    }

    @Override
    public Long getStudentCountByCourse(String courseName) {
        return studentRepository.countStudentByCourse(courseName);
    }

    @Override
    public List<StudentResponseDto> getStudentByFirstNameAndCourse(String firstName, String course) {
        List<Student> students = studentRepository.findByFirstNameAndCourse(firstName,course);
     return students.stream().map(this::mapToResponse).toList();
    }

    @Override
    public StudentResponseDto getStudentByEmailNative(String email) {

        Student student = studentRepository.findByEmailNative(email)
                .orElseThrow(() -> new StudentNotFoundException("Student not found"));

        return mapToResponse(student);
    }

    @Override
    public List<StudentResponseDto> getAllStudentSortByFirstName() {
        Sort sort = Sort.by("firstName").ascending().and(Sort.by("lastName"));
        List<Student> students = studentRepository.findAll(sort);
            //    Sort.by("firstName").descending());
        return students.stream().map(this::mapToResponse).toList();
    }

    @Override
    public Page<StudentResponseDto> getStudent(Pageable pageable) {
        Page<Student> studentPage = studentRepository.findAll(pageable);
        return studentPage.map(this::mapToResponse);
    }
//    public Page<StudentResponseDto> getStudent(int page, int size) {
//        Pageable pageable = PageRequest.of(page,size,Sort.by("firstName").descending());
//        Page<Student> studentPage = studentRepository.findAll(pageable);
//        return studentPage.map(this::mapToResponse);
//    }


    private StudentResponseDto mapToResponse (Student student){
        StudentResponseDto dto =new StudentResponseDto();
        dto.setId(student.getId());
        dto.setFirstName(student.getFirstName());
        dto.setLastName(student.getLastName());
        dto.setEmail(student.getEmail());
        AddressResponseDto addressResponseDto = new AddressResponseDto();
        addressResponseDto.setCity(student.getAddress().getCity());
        addressResponseDto.setState(student.getAddress().getState());
        addressResponseDto.setCountry(student.getAddress().getCountry());
        dto.setAddressResponseDto(addressResponseDto);
        dto.setDepartmentName(student.getDepartment().getDeptName());
        dto.setCourses(student.getCourses().stream().map(course -> new CourseResponseDto(
                course.getId(),
                course.getCourseName(),
                course.getDuration(),
                course.getFees(),
                course.getInstructorName()
        )).toList()
        );

        return dto;
    }
}
