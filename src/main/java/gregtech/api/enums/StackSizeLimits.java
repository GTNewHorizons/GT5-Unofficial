package gregtech.api.enums;

import gregtech.api.util.GTUtility;
import gregtech.common.config.Gregtech;

/// The configured stack ceilings for the bulk item forms, clamped to a usable range.
public class StackSizeLimits {

    public static final int ORE_STACK_SIZE = GTUtility.clamp(Gregtech.features.maxOreStackSize, 1, 64);
    public static final int PLANK_STACK_SIZE = GTUtility.clamp(Gregtech.features.maxPlankStackSize, 16, 64);
    public static final int LOG_STACK_SIZE = GTUtility.clamp(Gregtech.features.maxLogStackSize, 16, 64);
    public static final int OTHER_STACK_SIZE = GTUtility.clamp(Gregtech.features.maxOtherBlocksStackSize, 16, 64);

    private StackSizeLimits() {}
}
