package gtPlusPlus.xmod.gregtech.common.tileentities.generators;

import gregtech.api.GregTech_API;
import gregtech.api.enums.ConfigCategories;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.Textures;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.MetaTileEntity;
import gregtech.api.objects.GT_RenderedTexture;
import gregtech.api.util.GT_ModHandler;
import gregtech.api.util.GT_Recipe;
import gtPlusPlus.xmod.gregtech.api.metatileentity.implementations.base.generators.GregtechRocketFuelGeneratorBase;
import gtPlusPlus.xmod.gregtech.common.blocks.textures.TexturesGtBlock;
import net.minecraft.item.ItemStack;
import cpw.mods.fml.common.registry.GameRegistry;

public class GregtechMetaTileEntityDoubleFuelGeneratorBase
        extends GregtechRocketFuelGeneratorBase {

    public int mEfficiency;

    public GregtechMetaTileEntityDoubleFuelGeneratorBase(int aID, String aName, String aNameRegional, int aTier) {
        super(aID, aName, aNameRegional, aTier, "Requires two liquid Fuels. Fuel A is Fastburn, Fuel B is slowburn.", new ITexture[0]);
        onConfigLoad();
    }

    public GregtechMetaTileEntityDoubleFuelGeneratorBase(String aName, int aTier, String aDescription, ITexture[][][] aTextures) {
        super(aName, aTier, aDescription, aTextures);
        onConfigLoad();
    }

    @Override
	public boolean isOutputFacing(byte aSide) {
        return aSide == getBaseMetaTileEntity().getFrontFacing();
    }

    @Override
	public MetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new GregtechMetaTileEntityDoubleFuelGeneratorBase(this.mName, this.mTier, this.mDescription, this.mTextures);
    }

    @Override
	public GT_Recipe.GT_Recipe_Map getRecipes() {
        return GT_Recipe.GT_Recipe_Map.sDieselFuels;
    }

    @Override
	public int getCapacity() {
        return 32000;
    }

    public void onConfigLoad() {
        this.mEfficiency = GregTech_API.sMachineFile.get(ConfigCategories.machineconfig, "RocketEngine.efficiency.tier." + this.mTier, (100 - this.mTier * 8));
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
    
    private GT_RenderedTexture getCasingTexture(){
    	if (this.mTier <= 4){
    		return  new GT_RenderedTexture(TexturesGtBlock.Casing_Machine_Simple_Top);
    	}
    	else if (this.mTier == 5){

    		return  new GT_RenderedTexture(TexturesGtBlock.Casing_Machine_Advanced);
    	}
    	else if (this.mTier >= 6){

    		return  new GT_RenderedTexture(TexturesGtBlock.Casing_Machine_Ultra);
    	}
		return  new GT_RenderedTexture(TexturesGtBlock.Casing_Machine_Simple_Top);
    }
    

    @Override
	public ITexture[] getFront(byte aColor) {
        return new ITexture[]{super.getFront(aColor)[0], getCasingTexture(), Textures.BlockIcons.OVERLAYS_ENERGY_OUT_MULTI[this.mTier]};
    }

    @Override
	public ITexture[] getBack(byte aColor) {
        return new ITexture[]{super.getBack(aColor)[0], getCasingTexture(), new GT_RenderedTexture(TexturesGtBlock.Overlay_Machine_Vent)};
    }

    @Override
	public ITexture[] getBottom(byte aColor) {
        return new ITexture[]{super.getBottom(aColor)[0], new GT_RenderedTexture(TexturesGtBlock.Casing_Machine_Simple_Bottom)};
    }

    @Override
	public ITexture[] getTop(byte aColor) {
        return new ITexture[]{super.getTop(aColor)[0], new GT_RenderedTexture(TexturesGtBlock.Casing_Machine_Redstone_Off)};
    }

    @Override
	public ITexture[] getSides(byte aColor) {
        return new ITexture[]{super.getSides(aColor)[0], getCasingTexture(), new GT_RenderedTexture(TexturesGtBlock.Overlay_Machine_Diesel_Horizontal)};
    }

    @Override
	public ITexture[] getFrontActive(byte aColor) {
        return new ITexture[]{super.getFrontActive(aColor)[0], getCasingTexture(), Textures.BlockIcons.OVERLAYS_ENERGY_OUT_MULTI[this.mTier]};
    }

    @Override
	public ITexture[] getBackActive(byte aColor) {
        return new ITexture[]{super.getBackActive(aColor)[0], getCasingTexture(), new GT_RenderedTexture(TexturesGtBlock.Overlay_Machine_Vent_Fast)};
    }

    @Override
	public ITexture[] getBottomActive(byte aColor) {
        return new ITexture[]{super.getBottomActive(aColor)[0], new GT_RenderedTexture(TexturesGtBlock.Casing_Machine_Simple_Bottom)};
    }

    @Override
	public ITexture[] getTopActive(byte aColor) {
        return new ITexture[]{super.getTopActive(aColor)[0], new GT_RenderedTexture(TexturesGtBlock.Casing_Machine_Redstone_On)};
    }

    @Override
	public ITexture[] getSidesActive(byte aColor) {
        return new ITexture[]{super.getSidesActive(aColor)[0], getCasingTexture(), new GT_RenderedTexture(TexturesGtBlock.Overlay_Machine_Diesel_Horizontal_Active)};
    }
}
