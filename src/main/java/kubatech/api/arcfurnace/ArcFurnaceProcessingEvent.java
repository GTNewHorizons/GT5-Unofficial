package kubatech.api.arcfurnace;

import gregtech.api.recipe.check.CheckRecipeResult;
import gregtech.api.util.GTRecipe;
import gregtech.api.util.OverclockCalculator;
import gregtech.api.util.ParallelHelper;

public class ArcFurnaceProcessingEvent {

    public final ArcFurnaceContext arcFurnace;

    ArcFurnaceProcessingEvent(final ArcFurnaceContext arcFurnace) {
        this.arcFurnace = arcFurnace;
    }

    public static class EventPostRecipeCheck extends ArcFurnaceProcessingEvent {

        public final GTRecipe recipe;
        public final ParallelHelper helper;
        public final OverclockCalculator calculator;
        public final CheckRecipeResult result;

        public EventPostRecipeCheck(final ArcFurnaceContext arcFurnace, final GTRecipe recipe,
            final ParallelHelper helper, final OverclockCalculator calculator, final CheckRecipeResult result) {
            super(arcFurnace);
            this.recipe = recipe;
            this.helper = helper;
            this.calculator = calculator;
            this.result = result;

        }
    }

    public static class EventStartShutdown extends ArcFurnaceProcessingEvent {

        public int duration;

        public EventStartShutdown(final ArcFurnaceContext arcFurnace, int duration) {
            super(arcFurnace);
            this.duration = duration;
        }
    }

    public static class EventStartIgnition extends ArcFurnaceProcessingEvent {

        public int duration;
        public int eut;

        public EventStartIgnition(final ArcFurnaceContext arcFurnace, int duration, int eut) {
            super(arcFurnace);
            this.duration = duration;
            this.eut = eut;
        }
    }

}
