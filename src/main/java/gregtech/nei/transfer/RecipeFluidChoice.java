package gregtech.nei.transfer;

import java.util.List;
import java.util.function.Predicate;

/** Picks the fluid of a recipe that takes any of several. */
final class RecipeFluidChoice {

    private RecipeFluidChoice() {}

    /**
     * The first alternative already in the machine tank, since the tank cannot take another fluid; otherwise the first
     * one available; otherwise the first.
     *
     * @return an index into the alternatives, which must not be empty
     */
    static <T> int choose(List<T> alternatives, Predicate<T> inTank, Predicate<T> available) {
        for (int i = 0; i < alternatives.size(); i++) {
            if (inTank.test(alternatives.get(i))) return i;
        }
        for (int i = 0; i < alternatives.size(); i++) {
            if (available.test(alternatives.get(i))) return i;
        }
        return 0;
    }
}
