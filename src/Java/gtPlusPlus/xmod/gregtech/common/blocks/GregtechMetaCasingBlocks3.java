package gtPlusPlus.xmod.gregtech.common.blocks;

import gregtech.api.util.GT_Utility;
import gtPlusPlus.xmod.gregtech.common.tileentities.machines.multi.storage.GregtechMetaTileEntity_PowerSubStationController;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;

import gregtech.api.enums.TAE;
import gregtech.api.enums.Textures;
import gregtech.api.objects.GT_CopiedBlockTexture;
import gregtech.api.util.GT_LanguageManager;
import gregtech.common.blocks.GT_Material_Casings;

import gtPlusPlus.xmod.gregtech.api.enums.GregtechItemList;
import gtPlusPlus.xmod.gregtech.common.blocks.textures.CasingTextureHandler3;
import gtPlusPlus.xmod.gregtech.common.blocks.textures.TexturesGtBlock;

import java.util.List;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class GregtechMetaCasingBlocks3
extends GregtechMetaCasingBlocksAbstract {

	public static boolean mConnectedMachineTextures = false;
	CasingTextureHandler3 TextureHandler = new CasingTextureHandler3();

	public static class GregtechMetaCasingItemBlocks3 extends GregtechMetaCasingItems {

		public GregtechMetaCasingItemBlocks3(Block par1) {
			super(par1);
		}

		@Override
		public void addInformation(ItemStack aStack, EntityPlayer aPlayer, List aList, boolean aF3_H) {
			int meta = aStack.getItemDamage();
			int tier = GregtechMetaTileEntity_PowerSubStationController.getCellTier(field_150939_a, meta);
			if (tier > 0) {
				long capacity = GregtechMetaTileEntity_PowerSubStationController.getCapacityFromCellTier(tier);
				aList.add("Energy Storage: " + GT_Utility.formatNumbers(capacity));
			}
			super.addInformation(aStack, aPlayer, aList, aF3_H);
		}
	}

	public GregtechMetaCasingBlocks3() {
		super(GregtechMetaCasingItemBlocks3.class, "gtplusplus.blockcasings.3", GT_Material_Casings.INSTANCE);
		for (byte i = 0; i < 16; i = (byte) (i + 1)) {
			TAE.registerTextures(new GT_CopiedBlockTexture(this, 6, i));
		}
		GT_LanguageManager.addStringLocalization(this.getUnlocalizedName() + ".0.name", "Aquatic Casing");
		GT_LanguageManager.addStringLocalization(this.getUnlocalizedName() + ".1.name", "Inconel Reinforced Casing");
		GT_LanguageManager.addStringLocalization(this.getUnlocalizedName() + ".2.name", "Multi-Use Casing");
		GT_LanguageManager.addStringLocalization(this.getUnlocalizedName() + ".3.name", "Trinium Plated Mining Platform Casing");
		GT_LanguageManager.addStringLocalization(this.getUnlocalizedName() + ".4.name", "Vanadium Redox Power Cell (IV)");
		GT_LanguageManager.addStringLocalization(this.getUnlocalizedName() + ".5.name", "Vanadium Redox Power Cell (LuV)");
		GT_LanguageManager.addStringLocalization(this.getUnlocalizedName() + ".6.name", "Vanadium Redox Power Cell (ZPM)");
		GT_LanguageManager.addStringLocalization(this.getUnlocalizedName() + ".7.name", "Vanadium Redox Power Cell (UV)");
		GT_LanguageManager.addStringLocalization(this.getUnlocalizedName() + ".8.name", "Vanadium Redox Power Cell (MAX)");
		GT_LanguageManager.addStringLocalization(this.getUnlocalizedName() + ".9.name", "Supply Depot Casing");
		GT_LanguageManager.addStringLocalization(this.getUnlocalizedName() + ".10.name", "Advanced Cryogenic Casing");
		GT_LanguageManager.addStringLocalization(this.getUnlocalizedName() + ".11.name", "Volcanus Casing");
		GT_LanguageManager.addStringLocalization(this.getUnlocalizedName() + ".12.name", "Fusion Machine Casing MK III");
		GT_LanguageManager.addStringLocalization(this.getUnlocalizedName() + ".13.name", "Advanced Fusion Coil");
		GT_LanguageManager.addStringLocalization(this.getUnlocalizedName() + ".15.name", "Placeholder");
		GregtechItemList.Casing_FishPond.set(new ItemStack(this, 1, 0));
		GregtechItemList.Casing_Extruder.set(new ItemStack(this, 1, 1));
		GregtechItemList.Casing_Multi_Use.set(new ItemStack(this, 1, 2));
		GregtechItemList.Casing_BedrockMiner.set(new ItemStack(this, 1, 3));
		GregtechItemList.Casing_Vanadium_Redox_IV.set(new ItemStack(this, 1, 4));
		GregtechItemList.Casing_Vanadium_Redox_LuV.set(new ItemStack(this, 1, 5));
		GregtechItemList.Casing_Vanadium_Redox_ZPM.set(new ItemStack(this, 1, 6));
		GregtechItemList.Casing_Vanadium_Redox_UV.set(new ItemStack(this, 1, 7));
		GregtechItemList.Casing_Vanadium_Redox_MAX.set(new ItemStack(this, 1, 8));
		GregtechItemList.Casing_AmazonWarehouse.set(new ItemStack(this, 1, 9));
		GregtechItemList.Casing_AdvancedVacuum.set(new ItemStack(this, 1, 10));
		GregtechItemList.Casing_Adv_BlastFurnace.set(new ItemStack(this, 1, 11));
		GregtechItemList.Casing_Fusion_External.set(new ItemStack(this, 1, 12));
		GregtechItemList.Casing_Fusion_Internal.set(new ItemStack(this, 1, 13));
		//GregtechItemList.Casing_PLACEHOLDER_TreeFarmer.set(new ItemStack(this, 1, 15)); //Tree Farmer Textures
	}

	@Override
	public IIcon getIcon(final int aSide, final int aMeta) {
		return CasingTextureHandler3.getIcon(aSide, aMeta);
	}

	@SideOnly(Side.CLIENT)
	@Override
	public IIcon getIcon(final IBlockAccess aWorld, final int xCoord, final int yCoord, final int zCoord,
			final int aSide) {
		final Block thisBlock = aWorld.getBlock(xCoord, yCoord, zCoord);
		final int tMeta = aWorld.getBlockMetadata(xCoord, yCoord, zCoord);
		if ((tMeta != 12)|| !GregtechMetaCasingBlocks3.mConnectedMachineTextures) {
			return getIcon(aSide, tMeta);
		}
		final int tStartIndex = 0;
		if (tMeta == 12) {
			final boolean[] tConnectedSides = {
					aWorld.getBlock(xCoord, yCoord - 1, zCoord) == thisBlock
							&& aWorld.getBlockMetadata(xCoord, yCoord - 1, zCoord) == tMeta,
							aWorld.getBlock(xCoord, yCoord + 1, zCoord) == thisBlock
							&& aWorld.getBlockMetadata(xCoord, yCoord + 1, zCoord) == tMeta,
							aWorld.getBlock(xCoord + 1, yCoord, zCoord) == thisBlock
							&& aWorld.getBlockMetadata(xCoord + 1, yCoord, zCoord) == tMeta,
							aWorld.getBlock(xCoord, yCoord, zCoord + 1) == thisBlock
							&& aWorld.getBlockMetadata(xCoord, yCoord, zCoord + 1) == tMeta,
							aWorld.getBlock(xCoord - 1, yCoord, zCoord) == thisBlock
							&& aWorld.getBlockMetadata(xCoord - 1, yCoord, zCoord) == tMeta,
							aWorld.getBlock(xCoord, yCoord, zCoord - 1) == thisBlock
							&& aWorld.getBlockMetadata(xCoord, yCoord, zCoord - 1) == tMeta};
			switch (aSide) {
				case 0 : {
					if (tConnectedSides[0]) {
						return TexturesGtBlock.CONNECTED_FUSION_HULLS[tStartIndex + 7].getIcon();
					}
					if (tConnectedSides[4] && tConnectedSides[5] && tConnectedSides[2] && tConnectedSides[3]) {
						return TexturesGtBlock.CONNECTED_FUSION_HULLS[tStartIndex + 6].getIcon();
					}
					if (!tConnectedSides[4] && tConnectedSides[5] && tConnectedSides[2] && tConnectedSides[3]) {
						return TexturesGtBlock.CONNECTED_FUSION_HULLS[tStartIndex + 2].getIcon();
					}
					if (tConnectedSides[4] && !tConnectedSides[5] && tConnectedSides[2] && tConnectedSides[3]) {
						return TexturesGtBlock.CONNECTED_FUSION_HULLS[tStartIndex + 3].getIcon();
					}
					if (tConnectedSides[4] && tConnectedSides[5] && !tConnectedSides[2] && tConnectedSides[3]) {
						return TexturesGtBlock.CONNECTED_FUSION_HULLS[tStartIndex + 4].getIcon();
					}
					if (tConnectedSides[4] && tConnectedSides[5] && tConnectedSides[2] && !tConnectedSides[3]) {
						return TexturesGtBlock.CONNECTED_FUSION_HULLS[tStartIndex + 5].getIcon();
					}
					if (!tConnectedSides[4] && !tConnectedSides[5] && tConnectedSides[2] && tConnectedSides[3]) {
						return TexturesGtBlock.CONNECTED_FUSION_HULLS[tStartIndex + 8].getIcon();
					}
					if (tConnectedSides[4] && !tConnectedSides[5] && !tConnectedSides[2] && tConnectedSides[3]) {
						return TexturesGtBlock.CONNECTED_FUSION_HULLS[tStartIndex + 9].getIcon();
					}
					if (tConnectedSides[4] && tConnectedSides[5] && !tConnectedSides[2] && !tConnectedSides[3]) {
						return TexturesGtBlock.CONNECTED_FUSION_HULLS[tStartIndex + 10].getIcon();
					}
					if (!tConnectedSides[4] && tConnectedSides[5] && tConnectedSides[2] && !tConnectedSides[3]) {
						return TexturesGtBlock.CONNECTED_FUSION_HULLS[tStartIndex + 11].getIcon();
					}
					if (!tConnectedSides[4] && !tConnectedSides[5] && !tConnectedSides[2] && !tConnectedSides[3]) {
						return TexturesGtBlock.CONNECTED_FUSION_HULLS[tStartIndex + 7].getIcon();
					}
					if (!tConnectedSides[4] && !tConnectedSides[2]) {
						return TexturesGtBlock.CONNECTED_FUSION_HULLS[tStartIndex + 1].getIcon();
					}
					if (!tConnectedSides[5] && !tConnectedSides[3]) {
						return TexturesGtBlock.CONNECTED_FUSION_HULLS[tStartIndex + 0].getIcon();
					}
				}
				case 1 : {
					if (tConnectedSides[1]) {
						return TexturesGtBlock.CONNECTED_FUSION_HULLS[tStartIndex + 7].getIcon();
					}
					if (tConnectedSides[4] && tConnectedSides[5] && tConnectedSides[2] && tConnectedSides[3]) {
						return TexturesGtBlock.CONNECTED_FUSION_HULLS[tStartIndex + 6].getIcon();
					}
					if (!tConnectedSides[4] && tConnectedSides[5] && tConnectedSides[2] && tConnectedSides[3]) {
						return TexturesGtBlock.CONNECTED_FUSION_HULLS[tStartIndex + 2].getIcon();
					}
					if (tConnectedSides[4] && !tConnectedSides[5] && tConnectedSides[2] && tConnectedSides[3]) {
						return TexturesGtBlock.CONNECTED_FUSION_HULLS[tStartIndex + 3].getIcon();
					}
					if (tConnectedSides[4] && tConnectedSides[5] && !tConnectedSides[2] && tConnectedSides[3]) {
						return TexturesGtBlock.CONNECTED_FUSION_HULLS[tStartIndex + 4].getIcon();
					}
					if (tConnectedSides[4] && tConnectedSides[5] && tConnectedSides[2] && !tConnectedSides[3]) {
						return TexturesGtBlock.CONNECTED_FUSION_HULLS[tStartIndex + 5].getIcon();
					}
					if (!tConnectedSides[4] && !tConnectedSides[5] && tConnectedSides[2] && tConnectedSides[3]) {
						return TexturesGtBlock.CONNECTED_FUSION_HULLS[tStartIndex + 8].getIcon();
					}
					if (tConnectedSides[4] && !tConnectedSides[5] && !tConnectedSides[2] && tConnectedSides[3]) {
						return TexturesGtBlock.CONNECTED_FUSION_HULLS[tStartIndex + 9].getIcon();
					}
					if (tConnectedSides[4] && tConnectedSides[5] && !tConnectedSides[2] && !tConnectedSides[3]) {
						return TexturesGtBlock.CONNECTED_FUSION_HULLS[tStartIndex + 10].getIcon();
					}
					if (!tConnectedSides[4] && tConnectedSides[5] && tConnectedSides[2] && !tConnectedSides[3]) {
						return TexturesGtBlock.CONNECTED_FUSION_HULLS[tStartIndex + 11].getIcon();
					}
					if (!tConnectedSides[4] && !tConnectedSides[5] && !tConnectedSides[2] && !tConnectedSides[3]) {
						return TexturesGtBlock.CONNECTED_FUSION_HULLS[tStartIndex + 7].getIcon();
					}
					if (!tConnectedSides[2] && !tConnectedSides[4]) {
						return TexturesGtBlock.CONNECTED_FUSION_HULLS[tStartIndex + 1].getIcon();
					}
					if (!tConnectedSides[3] && !tConnectedSides[5]) {
						return TexturesGtBlock.CONNECTED_FUSION_HULLS[tStartIndex + 0].getIcon();
					}
				}
				case 2 : {
					if (tConnectedSides[5]) {
						return TexturesGtBlock.CONNECTED_FUSION_HULLS[tStartIndex + 7].getIcon();
					}
					if (tConnectedSides[2] && tConnectedSides[0] && tConnectedSides[4] && tConnectedSides[1]) {
						return TexturesGtBlock.CONNECTED_FUSION_HULLS[tStartIndex + 6].getIcon();
					}
					if (!tConnectedSides[2] && tConnectedSides[0] && tConnectedSides[4] && tConnectedSides[1]) {
						return TexturesGtBlock.CONNECTED_FUSION_HULLS[tStartIndex + 2].getIcon();
					}
					if (tConnectedSides[2] && !tConnectedSides[0] && tConnectedSides[4] && tConnectedSides[1]) {
						return TexturesGtBlock.CONNECTED_FUSION_HULLS[tStartIndex + 5].getIcon();
					}
					if (tConnectedSides[2] && tConnectedSides[0] && !tConnectedSides[4] && tConnectedSides[1]) {
						return TexturesGtBlock.CONNECTED_FUSION_HULLS[tStartIndex + 4].getIcon();
					}
					if (tConnectedSides[2] && tConnectedSides[0] && tConnectedSides[4] && !tConnectedSides[1]) {
						return TexturesGtBlock.CONNECTED_FUSION_HULLS[tStartIndex + 3].getIcon();
					}
					if (!tConnectedSides[2] && !tConnectedSides[0] && tConnectedSides[4] && tConnectedSides[1]) {
						return TexturesGtBlock.CONNECTED_FUSION_HULLS[tStartIndex + 11].getIcon();
					}
					if (tConnectedSides[2] && !tConnectedSides[0] && !tConnectedSides[4] && tConnectedSides[1]) {
						return TexturesGtBlock.CONNECTED_FUSION_HULLS[tStartIndex + 10].getIcon();
					}
					if (tConnectedSides[2] && tConnectedSides[0] && !tConnectedSides[4] && !tConnectedSides[1]) {
						return TexturesGtBlock.CONNECTED_FUSION_HULLS[tStartIndex + 9].getIcon();
					}
					if (!tConnectedSides[2] && tConnectedSides[0] && tConnectedSides[4] && !tConnectedSides[1]) {
						return TexturesGtBlock.CONNECTED_FUSION_HULLS[tStartIndex + 8].getIcon();
					}
					if (!tConnectedSides[2] && !tConnectedSides[0] && !tConnectedSides[4] && !tConnectedSides[1]) {
						return TexturesGtBlock.CONNECTED_FUSION_HULLS[tStartIndex + 7].getIcon();
					}
					if (!tConnectedSides[2] && !tConnectedSides[4]) {
						return TexturesGtBlock.CONNECTED_FUSION_HULLS[tStartIndex + 1].getIcon();
					}
					if (!tConnectedSides[0] && !tConnectedSides[1]) {
						return TexturesGtBlock.CONNECTED_FUSION_HULLS[tStartIndex + 0].getIcon();
					}
				}
				case 3 : {
					if (tConnectedSides[3]) {
						return TexturesGtBlock.CONNECTED_FUSION_HULLS[tStartIndex + 7].getIcon();
					}
					if (tConnectedSides[2] && tConnectedSides[0] && tConnectedSides[4] && tConnectedSides[1]) {
						return TexturesGtBlock.CONNECTED_FUSION_HULLS[tStartIndex + 6].getIcon();
					}
					if (!tConnectedSides[2] && tConnectedSides[0] && tConnectedSides[4] && tConnectedSides[1]) {
						return TexturesGtBlock.CONNECTED_FUSION_HULLS[tStartIndex + 4].getIcon();
					}
					if (tConnectedSides[2] && !tConnectedSides[0] && tConnectedSides[4] && tConnectedSides[1]) {
						return TexturesGtBlock.CONNECTED_FUSION_HULLS[tStartIndex + 5].getIcon();
					}
					if (tConnectedSides[2] && tConnectedSides[0] && !tConnectedSides[4] && tConnectedSides[1]) {
						return TexturesGtBlock.CONNECTED_FUSION_HULLS[tStartIndex + 2].getIcon();
					}
					if (tConnectedSides[2] && tConnectedSides[0] && tConnectedSides[4] && !tConnectedSides[1]) {
						return TexturesGtBlock.CONNECTED_FUSION_HULLS[tStartIndex + 3].getIcon();
					}
					if (!tConnectedSides[2] && !tConnectedSides[0] && tConnectedSides[4] && tConnectedSides[1]) {
						return TexturesGtBlock.CONNECTED_FUSION_HULLS[tStartIndex + 10].getIcon();
					}
					if (tConnectedSides[2] && !tConnectedSides[0] && !tConnectedSides[4] && tConnectedSides[1]) {
						return TexturesGtBlock.CONNECTED_FUSION_HULLS[tStartIndex + 11].getIcon();
					}
					if (tConnectedSides[2] && tConnectedSides[0] && !tConnectedSides[4] && !tConnectedSides[1]) {
						return TexturesGtBlock.CONNECTED_FUSION_HULLS[tStartIndex + 8].getIcon();
					}
					if (!tConnectedSides[2] && tConnectedSides[0] && tConnectedSides[4] && !tConnectedSides[1]) {
						return TexturesGtBlock.CONNECTED_FUSION_HULLS[tStartIndex + 9].getIcon();
					}
					if (!tConnectedSides[2] && !tConnectedSides[0] && !tConnectedSides[4] && !tConnectedSides[1]) {
						return TexturesGtBlock.CONNECTED_FUSION_HULLS[tStartIndex + 7].getIcon();
					}
					if (!tConnectedSides[2] && !tConnectedSides[4]) {
						return TexturesGtBlock.CONNECTED_FUSION_HULLS[tStartIndex + 1].getIcon();
					}
					if (!tConnectedSides[0] && !tConnectedSides[1]) {
						return TexturesGtBlock.CONNECTED_FUSION_HULLS[tStartIndex + 0].getIcon();
					}
				}
				case 4 : {
					if (tConnectedSides[4]) {
						return TexturesGtBlock.CONNECTED_FUSION_HULLS[tStartIndex + 7].getIcon();
					}
					if (tConnectedSides[0] && tConnectedSides[3] && tConnectedSides[1] && tConnectedSides[5]) {
						return TexturesGtBlock.CONNECTED_FUSION_HULLS[tStartIndex + 6].getIcon();
					}
					if (!tConnectedSides[0] && tConnectedSides[3] && tConnectedSides[1] && tConnectedSides[5]) {
						return TexturesGtBlock.CONNECTED_FUSION_HULLS[tStartIndex + 5].getIcon();
					}
					if (tConnectedSides[0] && !tConnectedSides[3] && tConnectedSides[1] && tConnectedSides[5]) {
						return TexturesGtBlock.CONNECTED_FUSION_HULLS[tStartIndex + 4].getIcon();
					}
					if (tConnectedSides[0] && tConnectedSides[3] && !tConnectedSides[1] && tConnectedSides[5]) {
						return TexturesGtBlock.CONNECTED_FUSION_HULLS[tStartIndex + 3].getIcon();
					}
					if (tConnectedSides[0] && tConnectedSides[3] && tConnectedSides[1] && !tConnectedSides[5]) {
						return TexturesGtBlock.CONNECTED_FUSION_HULLS[tStartIndex + 2].getIcon();
					}
					if (!tConnectedSides[0] && !tConnectedSides[3] && tConnectedSides[1] && tConnectedSides[5]) {
						return TexturesGtBlock.CONNECTED_FUSION_HULLS[tStartIndex + 10].getIcon();
					}
					if (tConnectedSides[0] && !tConnectedSides[3] && !tConnectedSides[1] && tConnectedSides[5]) {
						return TexturesGtBlock.CONNECTED_FUSION_HULLS[tStartIndex + 9].getIcon();
					}
					if (tConnectedSides[0] && tConnectedSides[3] && !tConnectedSides[1] && !tConnectedSides[5]) {
						return TexturesGtBlock.CONNECTED_FUSION_HULLS[tStartIndex + 8].getIcon();
					}
					if (!tConnectedSides[0] && tConnectedSides[3] && tConnectedSides[1] && !tConnectedSides[5]) {
						return TexturesGtBlock.CONNECTED_FUSION_HULLS[tStartIndex + 11].getIcon();
					}
					if (!tConnectedSides[0] && !tConnectedSides[3] && !tConnectedSides[1] && !tConnectedSides[5]) {
						return TexturesGtBlock.CONNECTED_FUSION_HULLS[tStartIndex + 7].getIcon();
					}
					if (!tConnectedSides[0] && !tConnectedSides[1]) {
						return TexturesGtBlock.CONNECTED_FUSION_HULLS[tStartIndex + 0].getIcon();
					}
					if (!tConnectedSides[3] && !tConnectedSides[5]) {
						return TexturesGtBlock.CONNECTED_FUSION_HULLS[tStartIndex + 1].getIcon();
					}
				}
				case 5 : {
					if (tConnectedSides[2]) {
						return TexturesGtBlock.CONNECTED_FUSION_HULLS[tStartIndex + 7].getIcon();
					}
					if (tConnectedSides[0] && tConnectedSides[3] && tConnectedSides[1] && tConnectedSides[5]) {
						return TexturesGtBlock.CONNECTED_FUSION_HULLS[tStartIndex + 6].getIcon();
					}
					if (!tConnectedSides[0] && tConnectedSides[3] && tConnectedSides[1] && tConnectedSides[5]) {
						return TexturesGtBlock.CONNECTED_FUSION_HULLS[tStartIndex + 5].getIcon();
					}
					if (tConnectedSides[0] && !tConnectedSides[3] && tConnectedSides[1] && tConnectedSides[5]) {
						return TexturesGtBlock.CONNECTED_FUSION_HULLS[tStartIndex + 2].getIcon();
					}
					if (tConnectedSides[0] && tConnectedSides[3] && !tConnectedSides[1] && tConnectedSides[5]) {
						return TexturesGtBlock.CONNECTED_FUSION_HULLS[tStartIndex + 3].getIcon();
					}
					if (tConnectedSides[0] && tConnectedSides[3] && tConnectedSides[1] && !tConnectedSides[5]) {
						return TexturesGtBlock.CONNECTED_FUSION_HULLS[tStartIndex + 4].getIcon();
					}
					if (!tConnectedSides[0] && !tConnectedSides[3] && tConnectedSides[1] && tConnectedSides[5]) {
						return TexturesGtBlock.CONNECTED_FUSION_HULLS[tStartIndex + 11].getIcon();
					}
					if (tConnectedSides[0] && !tConnectedSides[3] && !tConnectedSides[1] && tConnectedSides[5]) {
						return TexturesGtBlock.CONNECTED_FUSION_HULLS[tStartIndex + 8].getIcon();
					}
					if (tConnectedSides[0] && tConnectedSides[3] && !tConnectedSides[1] && !tConnectedSides[5]) {
						return TexturesGtBlock.CONNECTED_FUSION_HULLS[tStartIndex + 9].getIcon();
					}
					if (!tConnectedSides[0] && tConnectedSides[3] && tConnectedSides[1] && !tConnectedSides[5]) {
						return TexturesGtBlock.CONNECTED_FUSION_HULLS[tStartIndex + 10].getIcon();
					}
					if (!tConnectedSides[0] && !tConnectedSides[3] && !tConnectedSides[1] && !tConnectedSides[5]) {
						return TexturesGtBlock.CONNECTED_FUSION_HULLS[tStartIndex + 7].getIcon();
					}
					if (!tConnectedSides[0] && !tConnectedSides[1]) {
						return TexturesGtBlock.CONNECTED_FUSION_HULLS[tStartIndex + 0].getIcon();
					}
					if (!tConnectedSides[3] && !tConnectedSides[5]) {
						return TexturesGtBlock.CONNECTED_FUSION_HULLS[tStartIndex + 1].getIcon();
					}
					break;
				}
			}
			return TexturesGtBlock.CONNECTED_FUSION_HULLS[tStartIndex + 7].getIcon();
		}
		switch (tMeta) {
			case 9 : {
				return Textures.BlockIcons.MACHINE_CASING_SOLID_STEEL.getIcon();
			}
			case 10 : {
				return Textures.BlockIcons.MACHINE_CASING_CLEAN_STAINLESSSTEEL.getIcon();
			}
			case 11 : {
				return Textures.BlockIcons.MACHINE_CASING_STABLE_TITANIUM.getIcon();
			}
			case 12 : {
				return Textures.BlockIcons.MACHINE_CASING_ROBUST_TUNGSTENSTEEL.getIcon();
			}
			case 14 : {
				return TexturesGtBlock.TEXTURE_CASING_FUSION_CASING_ULTRA.getIcon();
			}
			default : {
				return Textures.BlockIcons.MACHINE_CASING_SOLID_STEEL.getIcon();
			}
		}
	}
}
