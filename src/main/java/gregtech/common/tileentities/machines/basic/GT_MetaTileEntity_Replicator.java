package gregtech.common.tileentities.machines.basic;

import gregtech.api.GregTech_API;
import gregtech.api.enums.*;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.MetaTileEntity;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_BasicMachine;
import gregtech.api.objects.GT_RenderedTexture;
import gregtech.api.util.GT_OreDictUnificator;
import gregtech.api.util.GT_Recipe;
import gregtech.api.util.GT_Utility;
import gregtech.common.items.behaviors.Behaviour_DataOrb;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;

public class GT_MetaTileEntity_Replicator
        extends GT_MetaTileEntity_BasicMachine {
    private static int sHeaviestElementMass = 0;
    public static final HashMap<Materials,Long> MASS_OVERRIDES =new HashMap<>();
    static{
        //put overrides here
        //ex.
        //MASS_OVERRIDES.put(Materials.get("cake"),Materials.get("cake").getMass());
        //MASS_OVERRIDES.put(Materials.get("otherCake"),1235234L);
        //MASS_OVERRIDES.put(Materials.Kalendrite,1235234L);
        //MASS_OVERRIDES.put(Materials.Kalendrite,Materials.Kalendrite.getMass()*2);
    }

    public GT_MetaTileEntity_Replicator(int aID, String aName, String aNameRegional, int aTier) {
        super(aID, aName, aNameRegional, aTier, 1, "Producing Elemental Matter", 1, 1, "Replicator.png", "", new GT_RenderedTexture(Textures.BlockIcons.OVERLAY_SIDE_REPLICATOR_ACTIVE), new GT_RenderedTexture(Textures.BlockIcons.OVERLAY_SIDE_REPLICATOR), new GT_RenderedTexture(Textures.BlockIcons.OVERLAY_FRONT_REPLICATOR_ACTIVE), new GT_RenderedTexture(Textures.BlockIcons.OVERLAY_FRONT_REPLICATOR), new GT_RenderedTexture(Textures.BlockIcons.OVERLAY_TOP_REPLICATOR_ACTIVE), new GT_RenderedTexture(Textures.BlockIcons.OVERLAY_TOP_REPLICATOR), new GT_RenderedTexture(Textures.BlockIcons.OVERLAY_BOTTOM_REPLICATOR_ACTIVE), new GT_RenderedTexture(Textures.BlockIcons.OVERLAY_BOTTOM_REPLICATOR));
    }

    public GT_MetaTileEntity_Replicator(String aName, int aTier, String aDescription, ITexture[][][] aTextures, String aGUIName, String aNEIName) {
        super(aName, aTier, 1, aDescription, aTextures, 1, 1, aGUIName, aNEIName);
    }

    public GT_MetaTileEntity_Replicator(String aName, int aTier, String[] aDescription, ITexture[][][] aTextures, String aGUIName, String aNEIName) {
        super(aName, aTier, 1, aDescription, aTextures, 1, 1, aGUIName, aNEIName);
    }

    public MetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new GT_MetaTileEntity_Replicator(this.mName, this.mTier, this.mDescriptionArray, this.mTextures, this.mGUIName, this.mNEIName);
    }

    public int checkRecipe() {
        FluidStack tFluid = getFillableStack();
        if ((tFluid != null) && (tFluid.isFluidEqual(Materials.UUMatter.getFluid(1L)))) {
            ItemStack tDataOrb = getSpecialSlot();
            if ((ItemList.Tool_DataOrb.isStackEqual(tDataOrb, false, true)) && (Behaviour_DataOrb.getDataTitle(tDataOrb).equals("Elemental-Scan"))) {
                Materials tMaterial = Element.get(Behaviour_DataOrb.getDataName(tDataOrb)).mLinkedMaterials.get(0);
                long tMass = MASS_OVERRIDES.getOrDefault(tMaterial,tMaterial.getMass());
                if ((tFluid.amount >= tMass) && (tMass > 0L)) {

                    this.mEUt = GT_Utility.safeInt(gregtech.api.enums.GT_Values.V[this.mTier],1);
                    this.mMaxProgresstime = GT_Utility.safeInt(tMass * 1024L / (1 << this.mTier),1);
                    if (mMaxProgresstime == Integer.MAX_VALUE - 1 || mEUt == Integer.MAX_VALUE - 1)
                        return FOUND_RECIPE_BUT_DID_NOT_MEET_REQUIREMENTS;


                    if ((this.mOutputItems[0] = GT_OreDictUnificator.get(OrePrefixes.dust, tMaterial, 1L)) == null) {
                        if ((this.mOutputItems[0] = GT_OreDictUnificator.get(OrePrefixes.cell, tMaterial, 1L)) != null) {
                            if ((this.mOutputFluid = GT_Utility.getFluidForFilledItem(this.mOutputItems[0], true)) == null) {
                                if (ItemList.Cell_Empty.isStackEqual(getInputAt(0))) {
                                    if (canOutput(this.mOutputItems[0])) {
                                        getInputAt(0).stackSize -= 1;
                                        tFluid.amount = ((int) (tFluid.amount - tMass));
                                        return 2;
                                    }
                                }
                            } else {
                                this.mOutputItems[0] = null;
                                if ((getDrainableStack() == null) || ((getDrainableStack().isFluidEqual(this.mOutputFluid)) && (getDrainableStack().amount < 16000))) {
                                    tFluid.amount = ((int) (tFluid.amount - tMass));
                                    return 2;
                                }
                            }
                        }
                    } else if (canOutput(this.mOutputItems[0])) {
                        tFluid.amount = ((int) (tFluid.amount - tMass));
                        return 2;
                    }
                }
            }
        }
        return 0;
    }

    @Override
    public GT_Recipe.GT_Recipe_Map getRecipeList() {
        return GT_Recipe.GT_Recipe_Map.sReplicatorFakeRecipes;
    }

    public boolean allowPutStack(IGregTechTileEntity aBaseMetaTileEntity, int aIndex, byte aSide, ItemStack aStack) {
        return (super.allowPutStack(aBaseMetaTileEntity, aIndex, aSide, aStack)) && (ItemList.Cell_Empty.isStackEqual(aStack));
    }

    public boolean isFluidInputAllowed(FluidStack aFluid) {
        return aFluid.isFluidEqual(Materials.UUMatter.getFluid(1L));
    }

    public int getCapacity() {
        if ((sHeaviestElementMass == 0) && (GregTech_API.sPostloadFinished)) {
            Materials tMaterial;
            for (Iterator i$ = Arrays.asList(Materials.values()).iterator(); i$.hasNext(); sHeaviestElementMass = Math.max(sHeaviestElementMass, (int) tMaterial.getMass())) {
                tMaterial = (Materials) i$.next();
            }
        }
        return sHeaviestElementMass;
    }
}
