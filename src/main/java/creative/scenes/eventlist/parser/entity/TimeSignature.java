package creative.scenes.eventlist.parser.entity;

import lombok.Getter;
import lombok.Setter;
import org.slf4j.helpers.MessageFormatter;

@Getter
@Setter
public class TimeSignature {
    private int numerator;
    private int denominator;

    public TimeSignature(int numerator, int denominator) {
        this.numerator = numerator;
        this.denominator = denominator;
    }

    public int getBeatValue() {
        return this.numerator;
    }

    public int getBeatsPerMeasure() {
        return this.denominator;
    }

    @Override
    public String toString() {
        return MessageFormatter.basicArrayFormat("{}/{}", new Object[]{numerator, denominator});
    }
}
