package gtPlusPlus.xmod.thaumcraft.common.tile;

import cpw.mods.fml.common.Optional;
import cpw.mods.fml.common.registry.GameRegistry;
import gtPlusPlus.api.objects.Logger;
import gtPlusPlus.core.lib.LoadedMods;

public class TCTileEntities {	

	@Optional.Method(modid = "Thaumcraft")
		public static void init() {
			Logger.INFO("Registering TC Tile Entities.");
			if (LoadedMods.Thaumcraft){
				GameRegistry.registerTileEntity(TileFastAlchemyFurnace.class, "TileFastAlchemyFurnace");
				GameRegistry.registerTileEntity(TileFastArcaneAlembic.class, "TileFastArcaneAlembic");
			}
		}
	}