package gregtech.common.tileentities.machines.steam;

import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_BOTTOM_STEAM_FURNACE;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_BOTTOM_STEAM_FURNACE_ACTIVE;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_BOTTOM_STEAM_FURNACE_ACTIVE_GLOW;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_BOTTOM_STEAM_FURNACE_GLOW;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_STEAM_FURNACE;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_STEAM_FURNACE_ACTIVE;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_STEAM_FURNACE_ACTIVE_GLOW;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_STEAM_FURNACE_GLOW;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_SIDE_STEAM_FURNACE;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_SIDE_STEAM_FURNACE_ACTIVE;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_SIDE_STEAM_FURNACE_ACTIVE_GLOW;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_SIDE_STEAM_FURNACE_GLOW;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_TOP_STEAM_FURNACE;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_TOP_STEAM_FURNACE_ACTIVE;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_TOP_STEAM_FURNACE_GLOW;

import net.minecraft.item.ItemStack;
import net.minecraftforge.common.util.ForgeDirection;

import gregtech.api.enums.SoundResource;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.MetaTileEntity;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_BasicMachine_Steel;
import gregtech.api.recipe.RecipeMap;
import gregtech.api.recipe.RecipeMaps;
import gregtech.api.render.TextureFactory;
import gregtech.api.util.GT_ModHandler;
import gregtech.api.util.GT_Utility;

public class GT_MetaTileEntity_Furnace_Steel extends GT_MetaTileEntity_BasicMachine_Steel {

    public GT_MetaTileEntity_Furnace_Steel(int aID, String aName, String aNameRegional) {
        super(aID, aName, aNameRegional, "Smelting things with compressed Steam", 1, 1, true);
    }

    public GT_MetaTileEntity_Furnace_Steel(String aName, String[] aDescription, ITexture[][][] aTextures) {
        super(aName, aDescription, aTextures, 1, 1, true);
    }

    @Override
    protected boolean isBricked() {
        return true;
    }

    @Override
    public MetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new GT_MetaTileEntity_Furnace_Steel(this.mName, this.mDescriptionArray, this.mTextures);
    }

    @Override
    public RecipeMap<?> getRecipeMap() {
        return RecipeMaps.furnaceRecipes;
    }

    @Override
    public int checkRecipe() {
        if (null != (this.mOutputItems[0] = GT_ModHandler.getSmeltingOutput(getInputAt(0), true, getOutputAt(0)))) {
            this.mEUt = 8;
            this.mMaxProgresstime = 128;
            return FOUND_AND_SUCCESSFULLY_USED_RECIPE;
        }
        return DID_NOT_FIND_RECIPE;
    }

    @Override
    protected boolean allowPutStackValidated(IGregTechTileEntity aBaseMetaTileEntity, int aIndex, ForgeDirection side,
        ItemStack aStack) {
        return super.allowPutStackValidated(aBaseMetaTileEntity, aIndex, side, aStack)
            && GT_ModHandler.getSmeltingOutput(GT_Utility.copyAmount(64, aStack), false, null) != null;
    }

    @Override
    public void startSoundLoop(byte aIndex, double aX, double aY, double aZ) {
        super.startSoundLoop(aIndex, aX, aY, aZ);
        if (aIndex == 1) {
            GT_Utility.doSoundAtClient(SoundResource.IC2_MACHINES_ELECTROFURNACE_LOOP, 10, 1.0F, aX, aY, aZ);
        }
    }

    @Override
    public void startProcess() {
        sendLoopStart((byte) 1);
    }

    @Override
    public ITexture[] getSideFacingActive(byte aColor) {
        return new ITexture[] { super.getSideFacingActive(aColor)[0],
            TextureFactory.of(OVERLAY_SIDE_STEAM_FURNACE_ACTIVE), TextureFactory.builder()
                .addIcon(OVERLAY_SIDE_STEAM_FURNACE_ACTIVE_GLOW)
                .glow()
                .build() };
    }

    @Override
    public ITexture[] getSideFacingInactive(byte aColor) {
        return new ITexture[] { super.getSideFacingInactive(aColor)[0], TextureFactory.of(OVERLAY_SIDE_STEAM_FURNACE),
            TextureFactory.builder()
                .addIcon(OVERLAY_SIDE_STEAM_FURNACE_GLOW)
                .glow()
                .build() };
    }

    @Override
    public ITexture[] getFrontFacingActive(byte aColor) {
        return new ITexture[] { super.getFrontFacingActive(aColor)[0],
            TextureFactory.of(OVERLAY_FRONT_STEAM_FURNACE_ACTIVE), TextureFactory.builder()
                .addIcon(OVERLAY_FRONT_STEAM_FURNACE_ACTIVE_GLOW)
                .glow()
                .build() };
    }

    @Override
    public ITexture[] getFrontFacingInactive(byte aColor) {
        return new ITexture[] { super.getFrontFacingInactive(aColor)[0], TextureFactory.of(OVERLAY_FRONT_STEAM_FURNACE),
            TextureFactory.builder()
                .addIcon(OVERLAY_FRONT_STEAM_FURNACE_GLOW)
                .glow()
                .build() };
    }

    @Override
    public ITexture[] getTopFacingActive(byte aColor) {
        return new ITexture[] { super.getTopFacingActive(aColor)[0],
            TextureFactory.of(OVERLAY_TOP_STEAM_FURNACE_ACTIVE), TextureFactory.builder()
                .addIcon(OVERLAY_BOTTOM_STEAM_FURNACE_ACTIVE_GLOW)
                .glow()
                .build() };
    }

    @Override
    public ITexture[] getTopFacingInactive(byte aColor) {
        return new ITexture[] { super.getTopFacingInactive(aColor)[0], TextureFactory.of(OVERLAY_TOP_STEAM_FURNACE),
            TextureFactory.builder()
                .addIcon(OVERLAY_TOP_STEAM_FURNACE_GLOW)
                .glow()
                .build() };
    }

    @Override
    public ITexture[] getBottomFacingActive(byte aColor) {
        return new ITexture[] { super.getBottomFacingActive(aColor)[0],
            TextureFactory.of(OVERLAY_BOTTOM_STEAM_FURNACE_ACTIVE), TextureFactory.builder()
                .addIcon(OVERLAY_BOTTOM_STEAM_FURNACE_ACTIVE_GLOW)
                .glow()
                .build() };
    }

    @Override
    public ITexture[] getBottomFacingInactive(byte aColor) {
        return new ITexture[] { super.getBottomFacingInactive(aColor)[0],
            TextureFactory.of(OVERLAY_BOTTOM_STEAM_FURNACE), TextureFactory.builder()
                .addIcon(OVERLAY_BOTTOM_STEAM_FURNACE_GLOW)
                .glow()
                .build() };
    }
}
