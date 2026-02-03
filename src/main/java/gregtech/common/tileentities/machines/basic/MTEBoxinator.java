package gregtech.common.tileentities.machines.basic;

import net.minecraft.item.ItemStack;
import net.minecraftforge.common.util.ForgeDirection;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import gregtech.api.enums.GTValues;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.MachineType;
import gregtech.api.enums.SoundResource;
import gregtech.api.enums.Textures;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.MetaTileEntity;
import gregtech.api.metatileentity.implementations.MTEBasicMachine;
import gregtech.api.recipe.RecipeMap;
import gregtech.api.recipe.RecipeMaps;
import gregtech.api.render.TextureFactory;
import gregtech.api.util.GTModHandler;
import gregtech.api.util.GTUtility;

public class MTEBoxinator extends MTEBasicMachine {

    ItemStack aInputCache;
    ItemStack aOutputCache;
    int aTypeCache = 0;

    public MTEBoxinator(int aID, String aName, String aNameRegional, int aTier) {
        super(
            aID,
            aName,
            aNameRegional,
            aTier,
            1,
            MachineType.PACKAGER.tooltipDescription(),
            2,
            1,
            TextureFactory.of(
                TextureFactory.of(
                    Textures.BlockIcons.CustomIcon
                        .createOptional("basicmachines/boxinator/OVERLAY_SIDE_BOXINATOR_ACTIVE")),
                TextureFactory.builder()
                    .addIcon(
                        Textures.BlockIcons.CustomIcon
                            .createOptional("basicmachines/boxinator/OVERLAY_SIDE_BOXINATOR_ACTIVE_GLOW"))
                    .glow()
                    .build()),
            TextureFactory.of(
                TextureFactory.of(
                    Textures.BlockIcons.CustomIcon.createOptional("basicmachines/boxinator/OVERLAY_SIDE_BOXINATOR")),
                TextureFactory.builder()
                    .addIcon(
                        Textures.BlockIcons.CustomIcon
                            .createOptional("basicmachines/boxinator/OVERLAY_SIDE_BOXINATOR_GLOW"))
                    .glow()
                    .build()),
            TextureFactory.of(
                TextureFactory.of(
                    Textures.BlockIcons.CustomIcon
                        .createOptional("basicmachines/boxinator/OVERLAY_FRONT_BOXINATOR_ACTIVE")),
                TextureFactory.builder()
                    .addIcon(
                        Textures.BlockIcons.CustomIcon
                            .createOptional("basicmachines/boxinator/OVERLAY_FRONT_BOXINATOR_ACTIVE_GLOW"))
                    .glow()
                    .build()),
            TextureFactory.of(
                TextureFactory.of(
                    Textures.BlockIcons.CustomIcon.createOptional("basicmachines/boxinator/OVERLAY_FRONT_BOXINATOR")),
                TextureFactory.builder()
                    .addIcon(
                        Textures.BlockIcons.CustomIcon
                            .createOptional("basicmachines/boxinator/OVERLAY_FRONT_BOXINATOR_GLOW"))
                    .glow()
                    .build()),
            TextureFactory.of(
                TextureFactory.of(
                    Textures.BlockIcons.CustomIcon
                        .createOptional("basicmachines/boxinator/OVERLAY_TOP_BOXINATOR_ACTIVE")),
                TextureFactory.builder()
                    .addIcon(
                        Textures.BlockIcons.CustomIcon
                            .createOptional("basicmachines/boxinator/OVERLAY_TOP_BOXINATOR_ACTIVE_GLOW"))
                    .glow()
                    .build()),
            TextureFactory.of(
                TextureFactory
                    .of(Textures.BlockIcons.CustomIcon.createOptional("basicmachines/boxinator/OVERLAY_TOP_BOXINATOR")),
                TextureFactory.builder()
                    .addIcon(
                        Textures.BlockIcons.CustomIcon
                            .createOptional("basicmachines/boxinator/OVERLAY_TOP_BOXINATOR_GLOW"))
                    .glow()
                    .build()),
            TextureFactory.of(
                TextureFactory.of(
                    Textures.BlockIcons.CustomIcon
                        .createOptional("basicmachines/boxinator/OVERLAY_BOTTOM_BOXINATOR_ACTIVE")),
                TextureFactory.builder()
                    .addIcon(
                        Textures.BlockIcons.CustomIcon
                            .createOptional("basicmachines/boxinator/OVERLAY_BOTTOM_BOXINATOR_ACTIVE_GLOW"))
                    .glow()
                    .build()),
            TextureFactory.of(
                TextureFactory.of(
                    Textures.BlockIcons.CustomIcon.createOptional("basicmachines/boxinator/OVERLAY_BOTTOM_BOXINATOR")),
                TextureFactory.builder()
                    .addIcon(
                        Textures.BlockIcons.CustomIcon
                            .createOptional("basicmachines/boxinator/OVERLAY_BOTTOM_BOXINATOR_GLOW"))
                    .glow()
                    .build()));
    }

    public MTEBoxinator(String aName, int aTier, String[] aDescription, ITexture[][][] aTextures) {
        super(aName, aTier, 1, aDescription, aTextures, 2, 1);
    }

    @Override
    public MetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new MTEBoxinator(this.mName, this.mTier, this.mDescriptionArray, this.mTextures);
    }

    @Override
    public RecipeMap<?> getRecipeMap() {
        return RecipeMaps.packagerRecipes;
    }

    private boolean hasValidCache(ItemStack mItem, int mType, boolean mClearOnFailure) {
        if (aInputCache != null && aOutputCache != null
            && aTypeCache == mType
            && aInputCache.isItemEqual(mItem)
            && ItemStack.areItemStackTagsEqual(mItem, aInputCache)) return true;
        // clear cache if it was invalid
        if (mClearOnFailure) {
            aInputCache = null;
            aOutputCache = null;
            aTypeCache = 0;
        }
        return false;
    }

    private void cacheItem(ItemStack mInputItem, ItemStack mOutputItem, int mType) {
        aTypeCache = mType;
        aOutputCache = mOutputItem.copy();
        aInputCache = mInputItem.copy();
    }

    @Override
    public int checkRecipe() {
        int tCheck = super.checkRecipe();
        if (tCheck != DID_NOT_FIND_RECIPE) {
            return tCheck;
        }
        ItemStack tSlot0 = getInputAt(0);
        ItemStack tSlot1 = getInputAt(1);
        if ((GTUtility.isStackValid(tSlot0)) && (GTUtility.isStackValid(tSlot1))
            && (GTUtility.getContainerItem(tSlot0, true) == null)) {
            if ((ItemList.Schematic_1by1.isStackEqual(tSlot1)) && (tSlot0.stackSize >= 1)) {
                boolean tIsCached = hasValidCache(tSlot0, 1, true);
                this.mOutputItems[0] = tIsCached ? aOutputCache.copy() : GTModHandler.getRecipeOutput(tSlot0);
                if (this.mOutputItems[0] != null) {
                    if (canOutput(this.mOutputItems[0])) {
                        tSlot0.stackSize -= 1;
                        calculateOverclockedNess(30, 16);
                        // In case recipe is too OP for that machine
                        if (mMaxProgresstime == Integer.MAX_VALUE - 1 && mEUt == Integer.MAX_VALUE - 1)
                            return FOUND_RECIPE_BUT_DID_NOT_MEET_REQUIREMENTS;
                        if (!tIsCached) cacheItem(tSlot0, this.mOutputItems[0], 1);
                        return FOUND_AND_SUCCESSFULLY_USED_RECIPE;
                    }
                }
                return DID_NOT_FIND_RECIPE;
            }
            if ((ItemList.Schematic_2by2.isStackEqual(tSlot1)) && (getInputAt(0).stackSize >= 4)) {
                boolean tIsCached = hasValidCache(tSlot0, 2, true);
                this.mOutputItems[0] = tIsCached ? aOutputCache.copy()
                    : GTModHandler.getRecipeOutput(tSlot0, tSlot0, null, tSlot0, tSlot0);
                if (this.mOutputItems[0] != null) {
                    if (canOutput(this.mOutputItems[0])) {
                        getInputAt(0).stackSize -= 4;
                        calculateOverclockedNess(30, 32);
                        // In case recipe is too OP for that machine
                        if (mMaxProgresstime == Integer.MAX_VALUE - 1 && mEUt == Integer.MAX_VALUE - 1)
                            return FOUND_RECIPE_BUT_DID_NOT_MEET_REQUIREMENTS;
                        if (!tIsCached) cacheItem(tSlot0, this.mOutputItems[0], 2);
                        return FOUND_AND_SUCCESSFULLY_USED_RECIPE;
                    }
                }
                return DID_NOT_FIND_RECIPE;
            }
            if ((ItemList.Schematic_3by3.isStackEqual(tSlot1)) && (getInputAt(0).stackSize >= 9)) {
                boolean tIsCached = hasValidCache(tSlot0, 3, true);
                this.mOutputItems[0] = tIsCached ? aOutputCache.copy()
                    : GTModHandler
                        .getRecipeOutput(tSlot0, tSlot0, tSlot0, tSlot0, tSlot0, tSlot0, tSlot0, tSlot0, tSlot0);
                if (this.mOutputItems[0] != null) {
                    if (canOutput(this.mOutputItems[0])) {
                        getInputAt(0).stackSize -= 9;
                        calculateOverclockedNess(30, 64);
                        // In case recipe is too OP for that machine
                        if (mMaxProgresstime == Integer.MAX_VALUE - 1 && mEUt == Integer.MAX_VALUE - 1)
                            return FOUND_RECIPE_BUT_DID_NOT_MEET_REQUIREMENTS;
                        if (!tIsCached) cacheItem(tSlot0, this.mOutputItems[0], 3);
                        return FOUND_AND_SUCCESSFULLY_USED_RECIPE;
                    }
                }
                return DID_NOT_FIND_RECIPE;
            }
        }
        return DID_NOT_FIND_RECIPE;
    }

    @Override
    protected boolean allowPutStackValidated(IGregTechTileEntity aBaseMetaTileEntity, int aIndex, ForgeDirection side,
        ItemStack aStack) {
        if (!super.allowPutStackValidated(aBaseMetaTileEntity, aIndex, side, aStack)) {
            return false;
        }
        ItemStack tInput1 = getInputAt(1);
        if ((ItemList.Schematic_1by1.isStackEqual(tInput1)) || (ItemList.Schematic_2by2.isStackEqual(tInput1))
            || (ItemList.Schematic_3by3.isStackEqual(tInput1))) {
            if (hasValidCache(aStack, aTypeCache, false)) return true;
            if (RecipeMaps.packagerRecipes.findRecipeQuery()
                .caching(false)
                .items(GTUtility.copyAmount(64, aStack), tInput1)
                .voltage(GTValues.V[mTier])
                .notUnificated(true)
                .find() != null) {
                return true;
            }
            if (ItemList.Schematic_1by1.isStackEqual(getInputAt(1)) && GTModHandler.getRecipeOutput(aStack) != null)
                return true;
            if (ItemList.Schematic_2by2.isStackEqual(getInputAt(1))
                && GTModHandler.getRecipeOutput(aStack, aStack, null, aStack, aStack) != null) {
                return true;
            }
            return ItemList.Schematic_3by3.isStackEqual(getInputAt(1))
                && (GTModHandler.getRecipeOutput(aStack, aStack, aStack, aStack, aStack, aStack, aStack, aStack, aStack)
                    != null);
        } else {
            return RecipeMaps.packagerRecipes.containsInput(aStack);
        }
    }

    @SideOnly(Side.CLIENT)
    @Override
    protected SoundResource getActivitySoundLoop() {
        return SoundResource.GTCEU_LOOP_ASSEMBLER;
    }
}
