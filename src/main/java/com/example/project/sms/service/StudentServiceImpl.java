package com.example.project.sms.service;

import com.example.project.sms.dto.AddressResponseDto;
import com.example.project.sms.dto.StudentRequestDto;
import com.example.project.sms.dto.StudentResponseDto;
import com.example.project.sms.entity.Address;
import com.example.project.sms.entity.Student;
import com.example.project.sms.exception.DuplicateEmailException;
import com.example.project.sms.exception.StudentNotFoundException;
import com.example.project.sms.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class StudentServiceImpl implements StudentService{
    @Autowired
    public final StudentRepository studentRepository;

    public StudentServiceImpl(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

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
        Address address =new Address();
        address.setCity(dto.getAddressRequestDto().getCity());
        address.setState(dto.getAddressRequestDto().getState());
        address.setCountry(dto.getAddressRequestDto().getCountry());

        Student student = new Student();
        student.setFirstName(dto.getFirstName());
        student.setLastName(dto.getLastName());
        student.setNumber(dto.getNumber());
        student.setEmail(dto.getEmail());
        student.setAddress(address);
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
        Student parUpd = studentRepository.save(exsStu);
        return mapToResponse(parUpd);
    }

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
        return dto;
    }
}
