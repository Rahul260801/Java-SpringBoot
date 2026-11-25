package com.example.project.sms.service;

import com.example.project.sms.dto.*;
import com.example.project.sms.entity.Address;
import com.example.project.sms.entity.Course;
import com.example.project.sms.entity.Department;
import com.example.project.sms.entity.Student;
import com.example.project.sms.exception.DuplicateEmailException;
import com.example.project.sms.exception.ResourceNotFoundException;
import com.example.project.sms.exception.StudentNotFoundException;
import com.example.project.sms.projection.StudentProjection;
import com.example.project.sms.repository.CourseRepository;
import com.example.project.sms.repository.DepartmentRepository;
import com.example.project.sms.repository.StudentRepository;
import com.example.project.sms.specification.StudentSpecification;
import jakarta.persistence.OptimisticLockException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

import static java.util.Arrays.stream;


@Slf4j
@RequiredArgsConstructor
@Service
public class StudentServiceImpl implements StudentService{
    @Autowired
    private final StudentRepository studentRepository;
    private final DepartmentRepository departmentRepository;
    private final CourseRepository courseRepository;


    @Override
    public StudentResponseDto getId(Long id) {
        log.debug("Fetching student with id {}", id);

        Student student = studentRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Student not found with id {}", id);
                    return new StudentNotFoundException("Student not found by id: " + id);
                });
        log.info("Student fetch successfully with id {}",id);
        return mapToResponse(student);
    }


    @Override
    public List<StudentResponseDto> getAll() {
        log.info("Fetching all stud");
        List<StudentResponseDto> students = studentRepository.findALLWithDetails()
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
        log.info("successfully fetched {} students",students);
        return students;
    }

    @Transactional(rollbackFor = Exception.class)
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
        try {
            sendConfirmationEmail();
        } catch (Exception e) {

          //  System.out.println(e.getMessage());
            log.error("Failed to send",e);
        }
      return   mapToResponse(saveStu);
    }

    private void sendConfirmationEmail(){
        throw new RuntimeException("somthing went wrong");
    }

@Transactional
    @Override
    public StudentResponseDto updateStud(Long id , StudentRequestDto dto) {
        Student exsStu = studentRepository.findById(id)
                .orElseThrow(() ->
                        new StudentNotFoundException("student not found"));

            if (!exsStu.getVersion().equals(dto.getVersion())){
                throw new OptimisticLockException("student was already updated by another user");
            }
            exsStu.setFirstName(dto.getFirstName());
            exsStu.setLastName(dto.getLastName());
            exsStu.setEmail(dto.getEmail());
            exsStu.setNumber(dto.getNumber());

            if (dto.getAddressRequestDto() !=null) {
                Address address = exsStu.getAddress();
                if (address == null) {
                    address = new Address();
                }
                address.setCity(dto.getAddressRequestDto().getCity());
                address.setState(dto.getAddressRequestDto().getState());
                address.setCountry(dto.getAddressRequestDto().getCountry());
                exsStu.setAddress(address);
            }
            Department dept = departmentRepository.findById(dto.getDepartmentId()).orElseThrow(()-> new ResourceNotFoundException("id not found"));
            exsStu.setDepartment(dept);

            Student updStudent = studentRepository.save(exsStu);

            List<Course> courses = courseRepository.findAllById(dto.getCourseId());
            exsStu.setCourses(courses);
            return mapToResponse(updStudent);
    }


    @Override
    public void deleteById(Long id) {
        Student student = studentRepository.findById(id).orElseThrow(()->new StudentNotFoundException("student not found"));
      student.setDeleted(true);
        studentRepository.save(student);
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

    //    public Page<StudentResponseDto> getStudent(int page, int size) {
//        Pageable pageable = PageRequest.of(page,size,Sort.by("firstName").descending());
//        Page<Student> studentPage = studentRepository.findAll(pageable);
//        return studentPage.map(this::mapToResponse);
//    }
    @Override
    public Page<StudentResponseDto> getStudent(Pageable pageable) {
        Page<Student> studentPage = studentRepository.findAll(pageable);
        return studentPage.map(this::mapToResponse);
    }

    @Override
    public List<StudentProjection> getStudentProjection() {
        return studentRepository.findBy();
    }


    @Override
    public List<StudentProjectionDto> getProjectionDto() {
        return studentRepository.getStudentProjection();
    }


    @Override
    public List<StudentResponseDto> searchStud(String firstName, String email, String courses) {
        Specification<Student> spec = Specification.allOf(
                StudentSpecification.hasFirstName(firstName),
                StudentSpecification.hasEmail(email),
                StudentSpecification.hasCourse(courses)
        );
        List<Student> students =studentRepository.findAll(spec);
        return students.stream().map(this::mapToResponse).toList();
    }

    @Override
    public List<StudentResponseDto> getAllStudentCustom() {
        List<Student> students = studentRepository.findStudentCustom();
        return students.stream().map(this::mapToResponse).toList();
    }

    @Override
    public List<StudentResponseDto> getStudentByDeptCustom(String departmentName) {
        List<Student> students = studentRepository.findStudentByDept(departmentName);
        return students.stream().map(this::mapToResponse).toList();
    }


    private StudentResponseDto mapToResponse (Student student){
        StudentResponseDto dto =new StudentResponseDto();
        dto.setId(student.getId());
        dto.setFirstName(student.getFirstName());
        dto.setLastName(student.getLastName());
        dto.setEmail(student.getEmail());

        dto.setCreatedAt(student.getCreatedAt());
        dto.setUpdatedAt(student.getUpdatedAt());
        dto.setCreatedBy(student.getCreatedBy());
        dto.setLastModifiedBy(student.getLastModifiedBy());

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

        dto.setVersion(student.getVersion());
        return dto;
    }
}
