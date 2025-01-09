package gregtech.api.util.recipe;

import java.util.Objects;

import org.apache.commons.lang3.builder.ToStringBuilder;

public class Scanning {

    public final int time;
    public final long voltage;

    public Scanning(int time, long voltage) {
        this.time = time;
        this.voltage = voltage;
    }

    public int hashCode() {
        return Objects.hash(time, voltage);
    }

    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Scanning that = (Scanning) o;

        if (time != that.time) return false;
        return voltage == that.voltage;
    }

    public String toString() {
        return new ToStringBuilder(this).append("time", time)
            .append("voltage", voltage)
            .toString();
    }
}
