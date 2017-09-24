package gtPlusPlus.xmod.rftools.blocks;

import cpw.mods.fml.common.registry.GameRegistry;
import mcjty.lib.container.GenericItemBlock;
import mcjty.rftools.blocks.dimlets.DimensionBuilderBlock;
import mcjty.rftools.blocks.dimlets.DimensionBuilderTileEntity;
import mcjty.rftools.blocks.teleporter.MatterTransmitterBlock;
import mcjty.rftools.blocks.teleporter.MatterTransmitterTileEntity;

public class RFT_Block_Registrator {

	public static DimensionBuilderBlock dimensionBuilderBlockAdvanced;
	public static DimensionBuilderBlock creativeDimensionBuilderBlockAdvanced;
	public static MatterTransmitterBlock matterTransmitterBlockA;

	public static void run(){
		
		matterTransmitterBlockA = new MatterTransmitterBlock();
		GameRegistry.registerBlock(matterTransmitterBlockA, GenericItemBlock.class, "matterTransmitterBlockA");
		GameRegistry.registerTileEntity(MatterTransmitterTileEntity.class, "MatterTransmitterTileEntityA");
		
		dimensionBuilderBlockAdvanced = new DimensionBuilderBlock(false, "dimensionBuilderBlockAdvanced");
		GameRegistry.registerBlock(dimensionBuilderBlockAdvanced, GenericItemBlock.class, "dimensionBuilderBlockAdvanced");
		GameRegistry.registerTileEntity(DimensionBuilderTileEntity.class, "dimensionBuilderBlockAdvanced");

		creativeDimensionBuilderBlockAdvanced = new DimensionBuilderBlock(true, "creativeDimensionBuilderBlockAdvanced");
		GameRegistry.registerBlock(creativeDimensionBuilderBlockAdvanced, GenericItemBlock.class,
				"creativeDimensionBuilderBlockAdvanced");
	}
	
}
