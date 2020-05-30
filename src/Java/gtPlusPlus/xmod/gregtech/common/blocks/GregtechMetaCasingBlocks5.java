package gtPlusPlus.xmod.gregtech.common.blocks;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import gregtech.api.enums.TAE;
import gregtech.api.enums.Textures;
import gregtech.api.objects.GT_CopiedBlockTexture;
import gregtech.api.util.GT_LanguageManager;
import gregtech.common.blocks.GT_Material_Casings;
import gtPlusPlus.xmod.gregtech.api.enums.GregtechItemList;
import gtPlusPlus.xmod.gregtech.common.blocks.textures.TexturesGrinderMultiblock;
import gtPlusPlus.xmod.gregtech.common.blocks.textures.TexturesGtBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;


public class GregtechMetaCasingBlocks5
extends GregtechMetaCasingBlocksAbstract {

	//84, 90, 91, 92, 94, 114, 116, 117, 118, 119, 120, 121, 124, 125, 126, 127
	private static final TexturesGrinderMultiblock mGrinderOverlayHandler = new TexturesGrinderMultiblock();
	
	public GregtechMetaCasingBlocks5() {
		super(GregtechMetaCasingItems.class, "gtplusplus.blockcasings.5", GT_Material_Casings.INSTANCE);		
		GT_LanguageManager.addStringLocalization(this.getUnlocalizedName() + ".0.name", "IsaMill Exterior Casing"); // IsaMill Casing
		TAE.registerTexture(0, 2, new GT_CopiedBlockTexture(this, 6, 0));
		GT_LanguageManager.addStringLocalization(this.getUnlocalizedName() + ".1.name", "IsaMill Piping"); // IsaMill Pipe
		GT_LanguageManager.addStringLocalization(this.getUnlocalizedName() + ".2.name", "IsaMill Gearbox"); // IsaMill Gearbox
		GT_LanguageManager.addStringLocalization(this.getUnlocalizedName() + ".3.name", "Elemental Confinement Shell"); // Duplicator Casing
		TAE.registerTexture(0, 3, new GT_CopiedBlockTexture(this, 6, 3));
		GT_LanguageManager.addStringLocalization(this.getUnlocalizedName() + ".4.name", ""); // Unused
		GT_LanguageManager.addStringLocalization(this.getUnlocalizedName() + ".5.name", ""); // Unused
		GT_LanguageManager.addStringLocalization(this.getUnlocalizedName() + ".6.name", ""); // Unused
		GT_LanguageManager.addStringLocalization(this.getUnlocalizedName() + ".7.name", ""); // Unused
		GT_LanguageManager.addStringLocalization(this.getUnlocalizedName() + ".8.name", ""); // Unused
		GT_LanguageManager.addStringLocalization(this.getUnlocalizedName() + ".9.name", ""); // Unused
		GT_LanguageManager.addStringLocalization(this.getUnlocalizedName() + ".10.name", ""); // Unused
		GT_LanguageManager.addStringLocalization(this.getUnlocalizedName() + ".11.name", ""); // Unused
		GT_LanguageManager.addStringLocalization(this.getUnlocalizedName() + ".12.name", ""); // Unused
		GT_LanguageManager.addStringLocalization(this.getUnlocalizedName() + ".13.name", ""); // Unused
		GT_LanguageManager.addStringLocalization(this.getUnlocalizedName() + ".14.name", ""); // Unused
		GT_LanguageManager.addStringLocalization(this.getUnlocalizedName() + ".15.name", ""); // Unused
		
		GregtechItemList.Casing_IsaMill_Casing.set(new ItemStack(this, 1, 0));
		GregtechItemList.Casing_IsaMill_Pipe.set(new ItemStack(this, 1, 1));
		GregtechItemList.Casing_IsaMill_Gearbox.set(new ItemStack(this, 1, 2));
		GregtechItemList.Casing_ElementalDuplicator.set(new ItemStack(this, 1, 2));
	}
	
	@Override
	public IIcon getIcon(final int aSide, final int aMeta) {
		return getStaticIcon(aSide, aMeta);
	}
	

	public static IIcon getStaticIcon(final int aSide, final int aMeta) {
		if ((aMeta >= 0) && (aMeta < 16)) {
			switch (aMeta) {
				case 0:
					return TexturesGtBlock.TEXTURE_CASING_GRINDING_MILL.getIcon();
				case 1:
					return TexturesGtBlock.TEXTURE_PIPE_GRINDING_MILL.getIcon();
				case 2:
					return TexturesGtBlock.TEXTURE_GEARBOX_GRINDING_MILL.getIcon();
				case 3:
					return TexturesGtBlock.TEXTURE_TECH_PANEL_D.getIcon();
			}
		}
		return Textures.BlockIcons.RENDERING_ERROR.getIcon();		
	}

	@Override
	@SideOnly(Side.CLIENT)
	public IIcon getIcon(final IBlockAccess aWorld, final int xCoord, final int yCoord, final int zCoord, final int aSide) {
		final GregtechMetaCasingBlocks5 i = this;
		return mGrinderOverlayHandler.handleCasingsGT(aWorld, xCoord, yCoord, zCoord, aSide, i);
	}
}
