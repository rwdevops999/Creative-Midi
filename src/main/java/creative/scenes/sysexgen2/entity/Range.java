package creative.scenes.sysexgen2.entity;

import lombok.Getter;

public class Range {
    @Getter
    private Integer min;
    @Getter
    private Integer max;

    public Range() {
        this.min = 0;
        this.max = 0;
    }

    public Range(Range other) {
        this.min = other.min;
        this.max = other.max;
    }

    public Range(Integer min, Integer max) {
        this();

        this.min = min;
        this.max = max;
    }
}
