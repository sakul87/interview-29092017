package pl.palonek.interview.course.model;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

import java.time.ZonedDateTime;

@Data
@Builder
public class CourseSummary {

    @JsonProperty("started_date")
    private ZonedDateTime startedDate;

    @JsonProperty("total_session_time")
    private long totalSessionTime;

    @JsonProperty("end_date")
    private ZonedDateTime endDate;

    @JsonProperty("best_score")
    private int bestScore;

    @JsonProperty("average_score")
    private double averageScore;
}
