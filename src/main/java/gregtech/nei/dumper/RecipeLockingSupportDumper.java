package gregtech.nei.dumper;

import gregtech.api.interfaces.modularui.IControllerWithOptionalFeatures;

public class RecipeLockingSupportDumper extends MultiBlockFeatureSupportDumper {

    public RecipeLockingSupportDumper() {
        super("recipe_locking", IControllerWithOptionalFeatures::supportsSingleRecipeLocking);
    }
}
