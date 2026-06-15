package autotests.payloads.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter
@Setter
@Accessors(fluent = true)
@JsonInclude(JsonInclude.Include.NON_NULL)

public class DuckProperties {
    @JsonProperty("color")
    String color;
    @JsonProperty("height")
    Double height;
    @JsonProperty("material")
    String material;
    @JsonProperty("sound")
    String sound;
    @JsonProperty("wingsState")
    WingsState wingsState;

    public enum WingsState {
        ACTIVE, FIXED, UNDEFINED
    }
}
