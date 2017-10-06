package pl.palonek.interview;

import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;
import pl.palonek.interview.course.model.CourseSessionDTO;
import pl.palonek.interview.course.model.CourseSummary;
import pl.palonek.interview.course.model.SessionStatus;

import java.time.ZoneId;
import java.time.ZonedDateTime;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
        classes = InterviewApplication.class)
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class InterviewApplicationTests {

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    public void shouldReturnSummaryForGivenSingleSession() {
        //given
        CourseSessionDTO initRequest = prepareRequest(SessionStatus.INIT, "2017-06-28T14:21:45.375+02:00");
        CourseSessionDTO saveRequest = prepareRequest(SessionStatus.SAVE,"2017-06-28T14:22:45.375+02:00",10);
        CourseSessionDTO secondSaveRequest = prepareRequest(SessionStatus.SAVE,"2017-06-28T14:23:45.375+02:00",50);
        CourseSessionDTO finishRequest = prepareRequest(SessionStatus.FINISH, "2017-06-28T14:24:45.375+02:00");

        CourseSummary expectedResult = CourseSummary.builder()
                .averageScore(50.0)
                .bestScore(50)
                .startedDate(ZonedDateTime.ofInstant(ZonedDateTime.parse("2017-06-28T14:21:45.375+02:00").toInstant(), ZoneId.of("UTC")))
                .endDate(ZonedDateTime.ofInstant(ZonedDateTime.parse("2017-06-28T14:24:45.375+02:00").toInstant(), ZoneId.of("UTC")))
                .totalSessionTime(180)
                .build();

        //when
        restTemplate.postForObject("/status/course", initRequest, HttpEntity.class);
        restTemplate.postForObject("/status/course", saveRequest, HttpEntity.class);
        restTemplate.postForObject("/status/course", secondSaveRequest, HttpEntity.class);
        restTemplate.postForObject("/status/course", finishRequest, HttpEntity.class);


        CourseSummary actualResult = restTemplate.getForEntity("/session/user/1/course/1/state", CourseSummary.class)
                .getBody();


        //then
        Assertions.assertThat(actualResult).isEqualTo(expectedResult);
    }

	@Test
	public void shouldReturnSummaryForGivenMultipleSessions() {
        //given

        CourseSessionDTO initRequest = prepareRequest(SessionStatus.INIT, "2017-06-28T14:21:45.375+02:00");
        CourseSessionDTO saveRequest = prepareRequest(SessionStatus.SAVE,"2017-06-28T14:22:45.375+02:00",10);
        CourseSessionDTO secondSaveRequest = prepareRequest(SessionStatus.SAVE,"2017-06-28T14:23:45.375+02:00",50);
        CourseSessionDTO finishRequest = prepareRequest(SessionStatus.FINISH, "2017-06-28T14:24:45.375+02:00");

        CourseSessionDTO initRequest2 = prepareRequest(SessionStatus.INIT, "2017-06-28T15:21:45.375+02:00");
        CourseSessionDTO saveRequest2 = prepareRequest(SessionStatus.SAVE,"2017-06-28T15:22:45.375+02:00",80);
        CourseSessionDTO secondSaveRequest2 = prepareRequest(SessionStatus.SAVE,"2017-06-28T15:23:45.375+02:00",50);
        CourseSessionDTO finishRequest2 = prepareRequest(SessionStatus.FINISH, "2017-06-28T15:24:45.375+02:00");

        CourseSummary expectedResult = CourseSummary.builder()
                .averageScore(65.0)
                .bestScore(80)
                .startedDate(ZonedDateTime.ofInstant(ZonedDateTime.parse("2017-06-28T14:21:45.375+02:00").toInstant(), ZoneId.of("UTC")))
                .endDate(ZonedDateTime.ofInstant(ZonedDateTime.parse("2017-06-28T15:24:45.375+02:00").toInstant(), ZoneId.of("UTC")))
                .totalSessionTime(3780)
                .build();

        //when
        restTemplate.postForObject("/status/course", initRequest, HttpEntity.class);
        restTemplate.postForObject("/status/course", saveRequest, HttpEntity.class);
        restTemplate.postForObject("/status/course", secondSaveRequest, HttpEntity.class);
        restTemplate.postForObject("/status/course", finishRequest, HttpEntity.class);
        restTemplate.postForObject("/status/course", initRequest2, HttpEntity.class);
        restTemplate.postForObject("/status/course", saveRequest2, HttpEntity.class);
        restTemplate.postForObject("/status/course", secondSaveRequest2, HttpEntity.class);
        restTemplate.postForObject("/status/course", finishRequest2, HttpEntity.class);


        CourseSummary actualResult = restTemplate.getForEntity("/session/user/1/course/1/state", CourseSummary.class)
                .getBody();


        //then
        Assertions.assertThat(actualResult).isEqualTo(expectedResult);
    }


    private CourseSessionDTO prepareRequest(SessionStatus sessionStatus, String timestamp) {
        return prepareCourseSessionDTOBuilder(sessionStatus, timestamp)
                .build();
    }

    private CourseSessionDTO prepareRequest(SessionStatus sessionStatus, String timestamp, int score) {
        return prepareCourseSessionDTOBuilder(sessionStatus, timestamp)
                .score(score)
                .build();
    }

    private CourseSessionDTO.CourseSessionDTOBuilder prepareCourseSessionDTOBuilder(SessionStatus sessionStatus,
                                                                                    String timestamp) {
        return CourseSessionDTO.builder()
                .courseId("1")
                .userId("1")
                .sessionStatus(sessionStatus)
                .timestamp(ZonedDateTime.parse(timestamp));
    }


}
