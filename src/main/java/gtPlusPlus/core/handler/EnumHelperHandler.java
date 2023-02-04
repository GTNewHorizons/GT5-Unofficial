package gtPlusPlus.core.handler;

import gtPlusPlus.xmod.gregtech.HANDLER_GT;
import gtPlusPlus.xmod.ic2.CustomInternalName;

public class EnumHelperHandler {

    public static void init() {
        CustomInternalName.init();
        HANDLER_GT.addNewOrePrefixes();
    }
}
