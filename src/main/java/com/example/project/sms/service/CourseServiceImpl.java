package com.example.project.sms.service;

import com.example.project.sms.dto.CourseRequestDto;
import com.example.project.sms.dto.CourseResponseDto;
import com.example.project.sms.entity.Course;
import com.example.project.sms.exception.ResourceNotFoundException;
import com.example.project.sms.repository.CourseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CourseServiceImpl implements CourseService {
    private final CourseRepository courseRepository;
    @Override
    public CourseResponseDto createCourse(CourseRequestDto requestDto) {
        Course course = new Course();
        course.setCourseName(requestDto.getCourseName());
        course.setDuration(requestDto.getDuration());
        course.setFees(requestDto.getFees());
        course.setInstructorName(requestDto.getInstructorName());
        Course saveCourse = courseRepository.save(course);
        return mapToResponseDto(saveCourse);
    }

    @Override
    public List<CourseResponseDto> getAll() {
        return courseRepository.findAll().stream()
                .map(this::mapToResponseDto)
                .toList();
    }

    @Override
    public CourseResponseDto getById(Long id) {
        Course course = courseRepository.findById(id).orElseThrow(()-> new ResourceNotFoundException("Course not found"));
        return mapToResponseDto(course);
    }

    private CourseResponseDto mapToResponseDto(Course course){
        CourseResponseDto responseDto =new CourseResponseDto();
        responseDto.setId(course.getId());
        responseDto.setCourseName(course.getCourseName());
        responseDto.setDuration(course.getDuration());
        responseDto.setFees(course.getFees());
        responseDto.setInstructorName(course.getInstructorName());
        return responseDto;
    }
}
