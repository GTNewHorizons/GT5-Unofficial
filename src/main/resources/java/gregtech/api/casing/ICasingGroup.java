package gregtech.api.casing;

import com.github.bsideup.jabel.Desugar;

/**
 * This is just a marker interface that represents a group of casings in a structure. It is generally used as a map key,
 * so it has no new methods. Implementations must define {@link Object#hashCode()} and {@link Object#equals(Object)}.
 */
public interface ICasingGroup {

    static ICasingGroup ofCasing(ICasing casing) {
        return new AllCasingsOfType(casing);
    }

    static ICasingGroup ofCharacter(char c) {
        return new SpecificCasingChar(c);
    }

    @Desugar
    record AllCasingsOfType(ICasing casing) implements ICasingGroup {

        @Override
        public boolean equals(Object obj) {
            if (!(obj instanceof AllCasingsOfType other)) return false;

            return this.casing == other.casing;
        }
    }

    @Desugar
    record SpecificCasingChar(char c) implements ICasingGroup {

        @Override
        public int hashCode() {
            return Character.hashCode(c);
        }

        @Override
        public boolean equals(Object obj) {
            if (!(obj instanceof SpecificCasingChar other)) return false;

            return c == other.c;
        }
    }
}
