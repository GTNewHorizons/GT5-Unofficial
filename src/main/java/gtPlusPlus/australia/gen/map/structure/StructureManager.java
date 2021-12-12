package gtPlusPlus.australia.gen.map.structure;

import gtPlusPlus.australia.gen.map.MapGenExtendedVillage;
import gtPlusPlus.australia.gen.map.structure.type.ComponentVillageBank;
import gtPlusPlus.core.lib.CORE;
import net.minecraft.world.gen.structure.MapGenStructureIO;

public class StructureManager {

	public static void registerVillageComponents() {
		try {
			//Register Village
			MapGenStructureIO.registerStructure(MapGenExtendedVillage.Start.class, "ExtendedVillage");	
			
			//Register Structures within village
			MapGenStructureIO.func_143031_a(ComponentVillageBank.class, CORE.MODID+":"+"Bank");
		} catch (Throwable e) {}
	}
	
}
