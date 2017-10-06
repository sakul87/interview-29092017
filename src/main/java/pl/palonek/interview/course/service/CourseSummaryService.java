package pl.palonek.interview.course.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.palonek.interview.course.model.CourseSession;
import pl.palonek.interview.course.model.CourseSummary;
import pl.palonek.interview.course.repository.CourseRepository;

import java.time.Duration;
import java.time.ZonedDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CourseSummaryService {

    private final CourseRepository courseRepository;

    public CourseSummary getCourseSummary(String userId, String courseId) {
        List<CourseSession> sessions = courseRepository.findByUserIdAndCourseId(userId, courseId);

        int maxScore = getMaxScore(sessions);
        double average = getAverage(sessions);
        ZonedDateTime startDate = getSessionsStartDate(sessions);
        ZonedDateTime endDate = getSessionsEndDate(sessions);
        long totalSessionTime = getTotalTimeBetweenSessions(startDate, endDate);

        return CourseSummary.builder()
                .averageScore(average)
                .bestScore(maxScore)
                .endDate(endDate)
                .startedDate(startDate)
                .totalSessionTime(totalSessionTime)
                .build();


    }

    private long getTotalTimeBetweenSessions(ZonedDateTime startDate, ZonedDateTime endDate) {
        return Duration.between(startDate, endDate).getSeconds();
    }

    private ZonedDateTime getSessionsEndDate(List<CourseSession> sessions) {
        return sessions.stream()
                    .map(CourseSession::getEndDate)
                    .max(ZonedDateTime::compareTo)
                    .get();
    }

    private ZonedDateTime getSessionsStartDate(List<CourseSession> sessions) {
        return sessions.stream()
                    .map(CourseSession::getStartDate)
                    .min(ZonedDateTime::compareTo)
                    .get();
    }

    private double getAverage(List<CourseSession> sessions) {
        return sessions.stream().mapToInt(CourseSession::getScore).average().getAsDouble();
    }

    private int getMaxScore(List<CourseSession> sessions) {
        return sessions.stream().mapToInt(CourseSession::getScore).max().getAsInt();
    }
}
