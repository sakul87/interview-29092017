package pl.palonek.interview.course.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

import java.time.ZonedDateTime;

@Data
@Builder
public final class CourseSessionDTO {

    private final String userId;

    private final String courseId;

    @JsonProperty("type")
    private final SessionStatus sessionStatus;

    private Integer score;

    private ZonedDateTime timestamp;

}
