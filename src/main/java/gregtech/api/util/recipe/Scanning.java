package gregtech.api.util.recipe;

import org.apache.commons.lang3.builder.ToStringBuilder;

public record Scanning(int time, long voltage) {

    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Scanning(int otherTime, long otherVolt))) return false;

        if (time != otherTime) return false;
        return voltage == otherVolt;
    }

    public String toString() {
        return new ToStringBuilder(this).append("time", time)
            .append("voltage", voltage)
            .toString();
    }
}
