package gregtech.common.tileentities.machines.basic;

import gregtech.api.enums.*;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.MetaTileEntity;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_BasicMachine;
import gregtech.api.objects.GT_RenderedTexture;
import gregtech.api.util.GT_Config;
import gregtech.api.util.GT_Recipe;
import net.minecraftforge.fluids.FluidStack;

import static gregtech.api.enums.GT_Values.V;

public class GT_MetaTileEntity_Massfabricator
        extends GT_MetaTileEntity_BasicMachine {
    public static int sUUAperUUM = 1;
    public static int sUUASpeedBonus = 4;
    public static int sDurationMultiplier = 3215;
    public static boolean sRequiresUUA = false;

    public GT_MetaTileEntity_Massfabricator(int aID, String aName, String aNameRegional, int aTier) {
        super(aID, aName, aNameRegional, aTier, 1, "UUM = Matter * Fabrication Squared", 1, 1, "Massfabricator.png", "", new ITexture[]{new GT_RenderedTexture(Textures.BlockIcons.OVERLAY_SIDE_MASSFAB_ACTIVE), new GT_RenderedTexture(Textures.BlockIcons.OVERLAY_SIDE_MASSFAB), new GT_RenderedTexture(Textures.BlockIcons.OVERLAY_FRONT_MASSFAB_ACTIVE), new GT_RenderedTexture(Textures.BlockIcons.OVERLAY_FRONT_MASSFAB), new GT_RenderedTexture(Textures.BlockIcons.OVERLAY_TOP_MASSFAB_ACTIVE), new GT_RenderedTexture(Textures.BlockIcons.OVERLAY_TOP_MASSFAB), new GT_RenderedTexture(Textures.BlockIcons.OVERLAY_BOTTOM_MASSFAB_ACTIVE), new GT_RenderedTexture(Textures.BlockIcons.OVERLAY_BOTTOM_MASSFAB)});
    }

    public GT_MetaTileEntity_Massfabricator(String aName, int aTier, String aDescription, ITexture[][][] aTextures, String aGUIName, String aNEIName) {
        super(aName, aTier, 1, aDescription, aTextures, 1, 1, aGUIName, aNEIName);
    }

    public GT_MetaTileEntity_Massfabricator(String aName, int aTier, String[] aDescription, ITexture[][][] aTextures, String aGUIName, String aNEIName) {
        super(aName, aTier, 1, aDescription, aTextures, 1, 1, aGUIName, aNEIName);
    }

    public MetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new GT_MetaTileEntity_Massfabricator(this.mName, this.mTier, this.mDescriptionArray, this.mTextures, this.mGUIName, this.mNEIName);
    }

    public void onConfigLoad(GT_Config aConfig) {
        super.onConfigLoad(aConfig);
        sDurationMultiplier = aConfig.get(ConfigCategories.machineconfig, "Massfabricator.UUM_Duration_Multiplier", sDurationMultiplier);
        sUUAperUUM = aConfig.get(ConfigCategories.machineconfig, "Massfabricator.UUA_per_UUM", sUUAperUUM);
        sUUASpeedBonus = aConfig.get(ConfigCategories.machineconfig, "Massfabricator.UUA_Speed_Bonus", sUUASpeedBonus);
        sRequiresUUA = aConfig.get(ConfigCategories.machineconfig, "Massfabricator.UUA_Requirement", sRequiresUUA);
        Materials.UUAmplifier.mChemicalFormula = ("Mass Fabricator Eff/Speed Bonus: x" + sUUASpeedBonus);
    }

    @Override
    public long maxAmperesIn() {
        return 10;
    }

    @Override
    public long maxEUStore() {
        return V[mTier] * 512L;
    }

    public int checkRecipe() {
        FluidStack tFluid = getDrainableStack();
        if ((tFluid == null) || (tFluid.amount < getCapacity())) {
            this.mOutputFluid = Materials.UUMatter.getFluid(1L);
            calculateOverclockedNessMassFabricator();
            //In case recipe is too OP for that machine
            if (mMaxProgresstime == Integer.MAX_VALUE - 1 && mEUt == Integer.MAX_VALUE - 1)
                return FOUND_RECIPE_BUT_DID_NOT_MEET_REQUIREMENTS;
            if (((tFluid = getFillableStack()) != null) && (tFluid.amount >= sUUAperUUM) && (tFluid.isFluidEqual(Materials.UUAmplifier.getFluid(1L)))) {
                tFluid.amount -= sUUAperUUM;
                this.mMaxProgresstime /= sUUASpeedBonus;
                return 2;
            }
            return (sRequiresUUA) || (ItemList.Circuit_Integrated.isStackEqual(getInputAt(0), true, true)) ? 1 : 2;
        }
        return 0;
    }
	@Override
    public GT_Recipe.GT_Recipe_Map getRecipeList() {
        return GT_Recipe.GT_Recipe_Map.sMassFabFakeRecipes;
    }

    private void calculateOverclockedNessMassFabricator() {
        if(mTier==0){
            //Long time calculation
            long xMaxProgresstime = ((long)sDurationMultiplier)<<1;
            if(xMaxProgresstime>Integer.MAX_VALUE-1){
                //make impossible if too long
                mEUt=Integer.MAX_VALUE-1;
                mMaxProgresstime=Integer.MAX_VALUE-1;
            }else{
                mEUt= (int)(GT_Values.V[1]<<2);//2^2=4  so shift <<2
                mMaxProgresstime=(int)xMaxProgresstime;
            }
        }else{
            //Long EUt calculation
            long xEUt=GT_Values.V[1] * (long)Math.pow(2,mTier+2);

            long tempEUt = GT_Values.V[1];

            mMaxProgresstime = sDurationMultiplier;

            while (tempEUt <= V[mTier -1]) {
                tempEUt<<=2;//this actually controls overclocking
                mMaxProgresstime>>=1;//this is effect of overclocking
                if(mMaxProgresstime==0)
                    xEUt = (long)(xEUt/1.1D);//U know, if the time is less than 1 tick make the machine use less power
            }
            if(xEUt>Integer.MAX_VALUE-1){
                mEUt = Integer.MAX_VALUE-1;
                mMaxProgresstime = Integer.MAX_VALUE-1;
            }else{
                mEUt = (int)xEUt;
                if(mEUt==0)
                    mEUt = 1;
                if(mMaxProgresstime==0)
                    mMaxProgresstime = 1;//set time to 1 tick
            }
        }
    }

    public boolean isFluidInputAllowed(FluidStack aFluid) {
        return aFluid.isFluidEqual(Materials.UUAmplifier.getFluid(1L));
    }

    public int getCapacity() {
        return Math.max(sUUAperUUM, 1000);
    }
}
