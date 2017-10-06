package pl.palonek.interview.course.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.palonek.interview.course.model.CourseSession;
import pl.palonek.interview.course.model.CourseSessionDTO;
import pl.palonek.interview.course.model.SessionStatus;
import pl.palonek.interview.course.repository.CourseRepository;

import java.time.ZonedDateTime;


@Service
@RequiredArgsConstructor
public class CourseStatusService {

    private final CourseRepository courseRepository;

    @Transactional
    public void saveCourseStatus(CourseSessionDTO courseSessionDTO) {
        String userId = courseSessionDTO.getUserId();
        String courseId = courseSessionDTO.getCourseId();
        ZonedDateTime timestamp = courseSessionDTO.getTimestamp();
        switch (courseSessionDTO.getSessionStatus()) {
            case INIT:
                initializeSession(timestamp, userId, courseId);
                break;
            case SAVE:
                updateSession(courseSessionDTO.getScore(), userId, courseId);
                break;
            case FINISH:
                closeSession(timestamp, userId, courseId);
                break;
        }
    }

    private void closeSession(ZonedDateTime timestamp, String userId, String courseId) {
        CourseSession courseSession;
        courseSession = courseRepository.findByUserIdAndCourseIdAndSessionStatusNot(userId, courseId, SessionStatus.FINISH);
        courseSession.setSessionStatus(SessionStatus.FINISH);
        courseSession.setEndDate(timestamp);
    }

    private void updateSession(int newScore, String userId, String courseId) {
        CourseSession courseSession;
        courseSession = courseRepository.findByUserIdAndCourseIdAndSessionStatusNot(userId, courseId, SessionStatus.FINISH);
        Integer oldScore = courseSession.getScore();
        if (oldScore == null || oldScore < newScore) {
            courseSession.setScore(newScore);
        }
    }

    private void initializeSession(ZonedDateTime timestamp, String userId, String courseId) {
        CourseSession courseSession = CourseSession.builder()
                .courseId(courseId)
                .userId(userId)
                .sessionStatus(SessionStatus.INIT)
                .startDate(timestamp)
                .build();
        courseRepository.save(courseSession);
    }
}
