package kubatech.api.arcfurnace;

import gregtech.api.logic.ProcessingLogic;
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

        public final ProcessingLogic logic;
        public final GTRecipe recipe;
        public final ParallelHelper helper;
        public final OverclockCalculator calculator;
        public final CheckRecipeResult result;
        public final boolean processingPhase;
        public long eut;

        public EventPostRecipeCheck(final ArcFurnaceContext arcFurnace, final ProcessingLogic logic,
            final GTRecipe recipe, final ParallelHelper helper, final OverclockCalculator calculator,
            final CheckRecipeResult result, final boolean processingPhase, long eut) {
            super(arcFurnace);
            this.logic = logic;
            this.recipe = recipe;
            this.helper = helper;
            this.calculator = calculator;
            this.result = result;
            this.processingPhase = processingPhase;
            this.eut = eut;
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

    public static class EventConfigureProcessing extends ArcFurnaceProcessingEvent {

        public final ProcessingLogic processingLogic;

        public EventConfigureProcessing(final ArcFurnaceContext arcFurnace, final ProcessingLogic processingLogic) {
            super(arcFurnace);
            this.processingLogic = processingLogic;
        }
    }

    public static class EventCreateParallelHelper extends ArcFurnaceProcessingEvent {

        public final ParallelHelper parallelHelper;

        public EventCreateParallelHelper(final ArcFurnaceContext arcFurnace, final ParallelHelper parallelHelper) {
            super(arcFurnace);
            this.parallelHelper = parallelHelper;
        }
    }

    public static class EventConfigureOverclock extends ArcFurnaceProcessingEvent {

        public final OverclockCalculator overclockCalculator;

        public EventConfigureOverclock(final ArcFurnaceContext arcFurnace, OverclockCalculator overclockCalculator) {
            super(arcFurnace);
            this.overclockCalculator = overclockCalculator;
        }
    }

    public static class EventRunCompleted extends ArcFurnaceProcessingEvent {

        public EventRunCompleted(final ArcFurnaceContext arcFurnace) {
            super(arcFurnace);
        }
    }

    public static class EventReset extends ArcFurnaceProcessingEvent {

        public EventReset(final ArcFurnaceContext arcFurnace) {
            super(arcFurnace);
        }
    }

}
