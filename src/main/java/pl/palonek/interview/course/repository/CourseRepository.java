package pl.palonek.interview.course.repository;

import org.springframework.data.repository.CrudRepository;
import pl.palonek.interview.course.model.CourseSession;
import pl.palonek.interview.course.model.SessionStatus;

import java.util.List;

public interface CourseRepository extends CrudRepository<CourseSession, Long> {

    CourseSession findByUserIdAndCourseIdAndSessionStatusNot(String userId, String courseId, SessionStatus sessionStatus);

    List<CourseSession> findByUserIdAndCourseId(String userId, String courseId);
}