package gregtech.common.tileentities.machines.basic;

import gregtech.api.GregTech_API;
import gregtech.api.enums.*;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.MetaTileEntity;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_BasicMachine;
import gregtech.api.objects.GT_RenderedTexture;
import gregtech.api.util.GT_Config;
import gregtech.api.util.GT_Recipe;
import gregtech.api.util.GT_Utility;
import ic2.api.crops.CropCard;
import ic2.api.crops.Crops;
import ic2.core.item.ItemCropSeed;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fluids.FluidStack;

public class GT_MetaTileEntity_OrganicReplicator extends GT_MetaTileEntity_BasicMachine{

    private static short EUMultiplier = 500;
    private static float UUMatterMultiplier = 2;

    public GT_MetaTileEntity_OrganicReplicator(int aID, String aName, String aNameRegional, int aTier) {
        super(aID, aName, aNameRegional, aTier, 1, new String[]{"Copies seeds with efficiency: "+Math.min((aTier+5)*10,100)+"%","Uses UUMatter for each seed","The better crop the more UUMatter it needs","Can replicate only scanned seeds"}, 1, 1, "OrganicReplicator.png", "", new GT_RenderedTexture(new Textures.BlockIcons.CustomIcon("basicmachines/organic_replicator/OVERLAY_SIDE_ACTIVE")), new GT_RenderedTexture(new Textures.BlockIcons.CustomIcon("basicmachines/organic_replicator/OVERLAY_SIDE")), new GT_RenderedTexture(new Textures.BlockIcons.CustomIcon("basicmachines/organic_replicator/OVERLAY_FRONT_ACTIVE")), new GT_RenderedTexture(new Textures.BlockIcons.CustomIcon("basicmachines/organic_replicator/OVERLAY_FRONT")), new GT_RenderedTexture(new Textures.BlockIcons.CustomIcon("basicmachines/organic_replicator/OVERLAY_TOP_ACTIVE")), new GT_RenderedTexture(new Textures.BlockIcons.CustomIcon("basicmachines/organic_replicator/OVERLAY_TOP")), new GT_RenderedTexture(new Textures.BlockIcons.CustomIcon("basicmachines/organic_replicator/OVERLAY_BOTTOM_ACTIVE")), new GT_RenderedTexture(new Textures.BlockIcons.CustomIcon("OVERLAY_BOTTOM")));
    }

    public GT_MetaTileEntity_OrganicReplicator(String aName, int aTier, String aDescription, ITexture[][][] aTextures, String aGUIName, String aNEIName) {
        super(aName, aTier, 1, aDescription, aTextures, 1, 1, aGUIName, aNEIName);
    }

    public GT_MetaTileEntity_OrganicReplicator(String aName, int aTier, String[] aDescription, ITexture[][][] aTextures, String aGUIName, String aNEIName) {
        super(aName, aTier, 1, aDescription, aTextures, 1, 1, aGUIName, aNEIName);
    }

    public MetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new GT_MetaTileEntity_OrganicReplicator(this.mName, this.mTier, this.mDescriptionArray, this.mTextures, this.mGUIName, this.mNEIName);
    }

    public int checkRecipe(){
        FluidStack tFluid = getFillableStack();
        ItemStack aStack = getInputAt(0);
        float efficiency = Math.min((this.mTier+5)*10,100);
        if ((tFluid != null) && (tFluid.isFluidEqual(Materials.UUMatter.getFluid(1L)))&& ItemList.IC2_Crop_Seeds.isStackEqual(aStack, true, true)&&isOutputEmpty()) {
            NBTTagCompound tNBT = aStack.getTagCompound();
            if (tNBT == null) {
                tNBT = new NBTTagCompound();
            }
            if (tNBT.getByte("scan") == 4){
                int aGain = ItemCropSeed.getGainFromStack(aStack);
                int aGrowth = ItemCropSeed.getGrowthFromStack(aStack);
                int aResistance = ItemCropSeed.getResistanceFromStack(aStack);
                CropCard v= Crops.instance.getCropCard(aStack);
                int aCropTier = v.tier();
                int UUMConsume =  Math.round((aCropTier*4+aGain+aGrowth+aResistance)*UUMatterMultiplier);
                if(UUMConsume>tFluid.amount)
                    return 0;
                this.mEUt = ((int) gregtech.api.enums.GT_Values.V[this.mTier]);
                this.mMaxProgresstime = (aCropTier+1)*EUMultiplier/ (1 << this.mTier - 1);
                //setFillableStack(new FluidStack(getFillableStack().fluid,Math.round(getFillableStack().amount-(aCropTier*4+aGain+aGrowth+aResistance)*UUMatterMultiplier)));
                FluidStack f = tFluid;
                f.amount = ((int) (f.amount - UUMConsume));
                this.mOutputItems[0] = GT_Utility.copyAmount(1L, new Object[]{aStack});
                this.mOutputItems[0].setTagCompound(tNBT);
                if(getBaseMetaTileEntity().getRandomNumber(100) > efficiency){
                    this.mOutputItems[0] = null;
                }
                return 2;
            }

        }
        return 0;
    }

    public void onConfigLoad(GT_Config aConfig){
        super.onConfigLoad(aConfig);
         EUMultiplier = (short) aConfig.get(ConfigCategories.machineconfig,"organicreplicator.euMultiplier",500);
         UUMatterMultiplier = aConfig.get(ConfigCategories.machineconfig,"organicreplicator.UUMatterMultiplier",2);
    }

    @Override
    public GT_Recipe.GT_Recipe_Map getRecipeList() {
        return GT_Recipe.GT_Recipe_Map.sOrganicReplicatorFakeRecipes;
    }

    public boolean allowPutStack(IGregTechTileEntity aBaseMetaTileEntity, int aIndex, byte aSide, ItemStack aStack) {
        return (super.allowPutStack(aBaseMetaTileEntity, aIndex, aSide, aStack)) && (ItemList.IC2_Crop_Seeds.isStackEqual(aStack));
    }

    public boolean isFluidInputAllowed(FluidStack aFluid) {
        return aFluid.isFluidEqual(Materials.UUMatter.getFluid(1L));
    }

    public int getCapacity() {
        return 100000;
    }

}