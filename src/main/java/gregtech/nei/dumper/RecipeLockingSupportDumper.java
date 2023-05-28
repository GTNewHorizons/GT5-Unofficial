package gregtech.nei.dumper;

import gregtech.api.interfaces.modularui.ControllerWithOptionalFeatures;

public class RecipeLockingSupportDumper extends MultiBlockFeatureSupportDumper {

    public RecipeLockingSupportDumper() {
        super("recipe_locking", ControllerWithOptionalFeatures::supportsSingleRecipeLocking);
    }
}
