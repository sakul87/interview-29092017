package pl.palonek.interview.course.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public enum SessionStatus {

    @JsonProperty("init")
    INIT,
    @JsonProperty("save")
    SAVE,
    @JsonProperty("finish")
    FINISH
}
