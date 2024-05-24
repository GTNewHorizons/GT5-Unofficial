package gtPlusPlus.xmod.ic2;

import net.minecraftforge.common.util.EnumHelper;

import ic2.core.init.InternalName;

public class CustomInternalName {

    public static InternalName aHazmatHelmetEx;
    public static InternalName aHazmatChestEx;
    public static InternalName aHazmatLegsEx;
    public static InternalName aHazmatBootsEx;

    public static void init() {
        aHazmatHelmetEx = EnumHelper
            .addEnum(InternalName.class, "itemArmorHazmatHelmetEx", new Class[] {}, new Object[] {});
        aHazmatChestEx = EnumHelper
            .addEnum(InternalName.class, "itemArmorHazmatChestplateEx", new Class[] {}, new Object[] {});
        aHazmatLegsEx = EnumHelper
            .addEnum(InternalName.class, "itemArmorHazmatLeggingsEx", new Class[] {}, new Object[] {});
        aHazmatBootsEx = EnumHelper.addEnum(InternalName.class, "itemArmorRubBootsEx", new Class[] {}, new Object[] {});
    }
}
