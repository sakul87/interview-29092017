package pl.palonek.interview.course;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import pl.palonek.interview.course.model.CourseSessionDTO;
import pl.palonek.interview.course.model.CourseSummary;
import pl.palonek.interview.course.service.CourseStatusService;
import pl.palonek.interview.course.service.CourseSummaryService;

@RestController
@Slf4j
@RequiredArgsConstructor
public class CourseConstroller {

    private final CourseStatusService courseStatusService;

    private final CourseSummaryService courseSummaryService;

    @PostMapping("/status/course")
    public void postCourseStatus(@RequestBody CourseSessionDTO courseSessionDTO) {
        log.info("Saving session status for user {} and course {}", courseSessionDTO.getUserId(), courseSessionDTO.getCourseId());
        courseStatusService.saveCourseStatus(courseSessionDTO);
    }

    @GetMapping("/session/user/{userId}/course/{courseId}/state")
    public CourseSummary getCourseSummary(@PathVariable String userId, @PathVariable String courseId) {
        log.info("Getting sessions summary for user {} and course {}", userId, courseId);
        return courseSummaryService.getCourseSummary(userId, courseId);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity generalException(Exception e) {
        log.error(e.getMessage(), e);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }
}
