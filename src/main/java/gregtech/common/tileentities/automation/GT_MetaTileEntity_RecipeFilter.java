package gregtech.common.tileentities.automation;

import static gregtech.api.enums.Textures.BlockIcons.AUTOMATION_RECIPEFILTER;
import static gregtech.api.enums.Textures.BlockIcons.AUTOMATION_RECIPEFILTER_GLOW;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_BasicMachine;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_SpecialFilter;
import gregtech.api.render.TextureFactory;
import gregtech.api.util.GT_Recipe;
import gregtech.common.blocks.GT_Item_Machines;

public class GT_MetaTileEntity_RecipeFilter extends GT_MetaTileEntity_SpecialFilter {

    public GT_Recipe.GT_Recipe_Map mRecipeMap;

    public GT_MetaTileEntity_RecipeFilter(int aID, String aName, String aNameRegional, int aTier) {
        super(
            aID,
            aName,
            aNameRegional,
            aTier,
            new String[] { "Filters 1 Recipe Type", "Use Screwdriver to regulate output stack size",
                "Does not consume energy to move Item" });
    }

    public GT_MetaTileEntity_RecipeFilter(String aName, int aTier, int aInvSlotCount, String aDescription,
        ITexture[][][] aTextures) {
        super(aName, aTier, aInvSlotCount, aDescription, aTextures);
    }

    public GT_MetaTileEntity_RecipeFilter(String aName, int aTier, int aInvSlotCount, String[] aDescription,
        ITexture[][][] aTextures) {
        super(aName, aTier, aInvSlotCount, aDescription, aTextures);
    }

    @Override
    public void clickTypeIcon(boolean aRightClick, ItemStack aHandStack) {
        if (!aRightClick) {
            IMetaTileEntity mte = GT_Item_Machines.getMetaTileEntity(aHandStack);
            if (mte instanceof GT_MetaTileEntity_BasicMachine machine) {
                GT_Recipe.GT_Recipe_Map recipeList = machine.getRecipeList();
                if (recipeList != null) {
                    mInventory[SPECIAL_SLOT_INDEX] = machine.getStackForm(1);
                    mRecipeMap = recipeList;
                    return;
                }
            }
            mInventory[SPECIAL_SLOT_INDEX] = null;
            mRecipeMap = null;
        }
    }

    @Override
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new GT_MetaTileEntity_RecipeFilter(
            this.mName,
            this.mTier,
            this.mInventory.length,
            this.mDescriptionArray,
            this.mTextures);
    }

    @Override
    public ITexture getOverlayIcon() {
        return TextureFactory.of(
            TextureFactory.of(AUTOMATION_RECIPEFILTER),
            TextureFactory.builder()
                .addIcon(AUTOMATION_RECIPEFILTER_GLOW)
                .glow()
                .build());
    }

    @Override
    public void saveNBTData(NBTTagCompound aNBT) {
        super.saveNBTData(aNBT);
        if (mRecipeMap != null) aNBT.setString("mRecipeMap", this.mRecipeMap.mUniqueIdentifier);
    }

    @Override
    public void loadNBTData(NBTTagCompound aNBT) {
        super.loadNBTData(aNBT);
        this.mRecipeMap = GT_Recipe.GT_Recipe_Map.sIndexedMappings.getOrDefault(aNBT.getString("mRecipeMap"), null);
    }

    @Override
    protected boolean isStackAllowed(ItemStack aStack) {
        return mRecipeMap != null && mRecipeMap.containsInput(aStack);
    }

    // @Override
    // protected List<Text> getEmptySlotTooltip() {
    // return Collections.singletonList(Text.localised("GT5U.recipe_filter.representation_slot.tooltip"));
    // }
}
