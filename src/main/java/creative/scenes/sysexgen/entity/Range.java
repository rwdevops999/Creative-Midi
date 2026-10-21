package creative.scenes.sysexgen.entity;

import lombok.Getter;

public class Range {
    @Getter
    private Integer min;
    @Getter
    private Integer max;

    @Getter
    private boolean discrete;

    public Range() {
        this.min = 0;
        this.max = 0;
        this.discrete = false;
    }

    public Range(Range other) {
        this.min = other.min;
        this.max = other.max;
        this.discrete = other.discrete;
    }

    public Range(Integer min, Integer max) {
        this();

        this.min = min;
        this.max = max;
    }

    public Range(Integer min, Integer max, boolean discrete) {
        this();

        this.min = min;
        this.max = max;
        this.discrete = discrete;
    }
}
