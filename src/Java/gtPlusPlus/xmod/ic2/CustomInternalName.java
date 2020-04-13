package gtPlusPlus.xmod.ic2;

import gtPlusPlus.core.util.data.EnumUtils;
import ic2.core.init.InternalName;
import net.minecraftforge.common.util.EnumHelper;

public class CustomInternalName {

	public static InternalName aHazmatHelmetEx;
	public static InternalName aHazmatChestEx;
	public static InternalName aHazmatLegsEx;
	public static InternalName aHazmatBootsEx;
	
	public static void init() {
		EnumHelper.addEnum(InternalName.class, "itemArmorHazmatHelmetEx", new Class[] {}, new Object[] {});
		EnumHelper.addEnum(InternalName.class, "itemArmorHazmatChestplateEx", new Class[] {}, new Object[] {});
		EnumHelper.addEnum(InternalName.class, "itemArmorHazmatLeggingsEx", new Class[] {}, new Object[] {});
		EnumHelper.addEnum(InternalName.class, "itemArmorRubBootsEx", new Class[] {}, new Object[] {});			
		aHazmatHelmetEx = EnumUtils.getValue(InternalName.class, "itemArmorHazmatHelmetEx");
		aHazmatChestEx = EnumUtils.getValue(InternalName.class, "itemArmorHazmatChestplateEx");
		aHazmatLegsEx = EnumUtils.getValue(InternalName.class, "itemArmorHazmatLeggingsEx");
		aHazmatBootsEx = EnumUtils.getValue(InternalName.class, "itemArmorRubBootsEx");		
	}

	
	
}
