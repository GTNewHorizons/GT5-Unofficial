package gregtech.nei.dumper;

import gregtech.api.interfaces.modularui.ControllerWithButtons;

public class RecipeLockingSupportDumper extends MultiBlockFeatureSupportDumper {

    public RecipeLockingSupportDumper() {
        super("recipe_locking", ControllerWithButtons::supportsSingleRecipeLocking);
    }
}
