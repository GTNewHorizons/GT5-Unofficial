package gregtech.api.items.armor.behaviors;

import net.minecraft.util.StatCollector;

public class StepAssistBehavior implements IArmorBehavior {

    public static final StepAssistBehavior INSTANCE = new StepAssistBehavior();

    protected StepAssistBehavior() {/**/}

    @Override
    public BehaviorName getName() {
        return BehaviorName.StepAssist;
    }
}
