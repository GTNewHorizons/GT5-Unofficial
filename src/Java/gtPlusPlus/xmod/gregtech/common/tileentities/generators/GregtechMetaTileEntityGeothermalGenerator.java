package gtPlusPlus.xmod.gregtech.common.tileentities.generators;

import gregtech.api.GregTech_API;
import gregtech.api.enums.ConfigCategories;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.Textures;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.MetaTileEntity;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_BasicGenerator;
import gregtech.api.objects.GT_RenderedTexture;
import gregtech.api.util.GT_ModHandler;
import gregtech.api.util.GT_Recipe.GT_Recipe_Map;
import gtPlusPlus.xmod.gregtech.common.blocks.textures.TexturesGtBlock;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import cpw.mods.fml.common.registry.GameRegistry;

public class GregtechMetaTileEntityGeothermalGenerator
extends GT_MetaTileEntity_BasicGenerator
{

	public int mEfficiency;

	public GregtechMetaTileEntityGeothermalGenerator(int aID, String aName, String aNameRegional, int aTier) {
		super(aID, aName, aNameRegional, aTier, "Requires Pahoehoe Lava or Normal Lava as Fuel", new ITexture[0]);
		onConfigLoad();
	}

	public GregtechMetaTileEntityGeothermalGenerator(String aName, int aTier, String aDescription, ITexture[][][] aTextures) {
		super(aName, aTier, aDescription, aTextures);
		onConfigLoad();
	}  

	@Override
	public boolean onRightclick(IGregTechTileEntity aBaseMetaTileEntity, EntityPlayer aPlayer) {
		if (aBaseMetaTileEntity.isClientSide()){
			return true;
		}
		aBaseMetaTileEntity.openGUI(aPlayer);
		return true;
	} 

	@Override
	public int getCapacity() {
		//return MathUtils.roundToClosestMultiple(32000*(this.mTier/2), 25000);
		return 5000*this.mTier;
	}

	public void onConfigLoad() {
		this.mEfficiency = GregTech_API.sMachineFile.get(ConfigCategories.machineconfig, "ThermalGenerator.efficiency.tier." + this.mTier, (100 - this.mTier * 7));
	}

	@Override
	public int getEfficiency() {
		return this.mEfficiency;
	}

	@Override
	public int getFuelValue(ItemStack aStack) {
		int rValue = Math.max(GT_ModHandler.getFuelCanValue(aStack) * 6 / 5, super.getFuelValue(aStack));
		if (ItemList.Fuel_Can_Plastic_Filled.isStackEqual(aStack, false, true)) {
			rValue = Math.max(rValue, GameRegistry.getFuelValue(aStack) * 3);
		}
		return rValue;
	}

	@Override
	public boolean isOutputFacing(byte aSide) {
		return aSide == getBaseMetaTileEntity().getFrontFacing();
	}

	@Override
	public MetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
		return new GregtechMetaTileEntityGeothermalGenerator(this.mName, this.mTier, this.mDescription, this.mTextures);
	}

	@Override
	public ITexture[] getFront(byte aColor) {
		return new ITexture[]{super.getFront(aColor)[0], new GT_RenderedTexture(Textures.BlockIcons.DIESEL_GENERATOR_SIDE), Textures.BlockIcons.OVERLAYS_ENERGY_OUT_MULTI[this.mTier]};
	}

	@Override
	public ITexture[] getBack(byte aColor) {
		return new ITexture[]{super.getBack(aColor)[0], new GT_RenderedTexture(Textures.BlockIcons.DIESEL_GENERATOR_BACK), new GT_RenderedTexture(TexturesGtBlock.Overlay_Machine_Diesel_Vertical)};
	}

	@Override
	public ITexture[] getBottom(byte aColor) {
		return new ITexture[]{super.getBottom(aColor)[0], new GT_RenderedTexture(Textures.BlockIcons.DIESEL_GENERATOR_BOTTOM)};
	}

	@Override
	public ITexture[] getTop(byte aColor) {
		return new ITexture[]{super.getTop(aColor)[0], new GT_RenderedTexture(Textures.BlockIcons.DIESEL_GENERATOR_SIDE), new GT_RenderedTexture(Textures.BlockIcons.OVERLAY_FRONT_ROCK_BREAKER)};
	}

	@Override
	public ITexture[] getSides(byte aColor) {
		return new ITexture[]{super.getSides(aColor)[0], new GT_RenderedTexture(Textures.BlockIcons.BOILER_LAVA_FRONT)};
	}

	@Override
	public ITexture[] getFrontActive(byte aColor) {
		return new ITexture[]{super.getFrontActive(aColor)[0], new GT_RenderedTexture(Textures.BlockIcons.DIESEL_GENERATOR_SIDE_ACTIVE), Textures.BlockIcons.OVERLAYS_ENERGY_OUT_MULTI[this.mTier]};
	}

	@Override
	public ITexture[] getBackActive(byte aColor) {
		return new ITexture[]{super.getBackActive(aColor)[0], new GT_RenderedTexture(Textures.BlockIcons.DIESEL_GENERATOR_BACK_ACTIVE), new GT_RenderedTexture(TexturesGtBlock.Overlay_Machine_Diesel_Vertical_Active)};
	}

	@Override
	public ITexture[] getBottomActive(byte aColor) {
		return new ITexture[]{super.getBottomActive(aColor)[0], new GT_RenderedTexture(Textures.BlockIcons.DIESEL_GENERATOR_BOTTOM_ACTIVE)};
	}

	@Override
	public ITexture[] getTopActive(byte aColor) {
		return new ITexture[]{super.getTopActive(aColor)[0], new GT_RenderedTexture(Textures.BlockIcons.DIESEL_GENERATOR_SIDE), new GT_RenderedTexture(Textures.BlockIcons.OVERLAY_FRONT_ROCK_BREAKER_ACTIVE)};
	}

	@Override
	public ITexture[] getSidesActive(byte aColor) {
		return new ITexture[]{super.getSidesActive(aColor)[0], new GT_RenderedTexture(Textures.BlockIcons.BOILER_LAVA_FRONT_ACTIVE)};
	}


	@Override
	public String[] getDescription()
	{
		return new String[] {"Generates power from Lava/Pahoehoe at " + getEfficiency() + "% Efficiency per tick"};
	}

	@Override
	public GT_Recipe_Map getRecipes()
	{
		return GT_Recipe_Map.sHotFuels;
	}

	
	public int getPollution() {
		return 100;
	}
}