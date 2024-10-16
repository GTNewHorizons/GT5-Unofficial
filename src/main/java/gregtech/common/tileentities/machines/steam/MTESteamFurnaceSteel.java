package gregtech.common.tileentities.machines.steam;

import net.minecraft.item.ItemStack;
import net.minecraftforge.common.util.ForgeDirection;

import gregtech.api.enums.SoundResource;
import gregtech.api.enums.Textures;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.MetaTileEntity;
import gregtech.api.metatileentity.implementations.MTEBasicMachineSteel;
import gregtech.api.recipe.RecipeMap;
import gregtech.api.recipe.RecipeMaps;
import gregtech.api.render.TextureFactory;
import gregtech.api.util.GTModHandler;
import gregtech.api.util.GTUtility;

public class MTESteamFurnaceSteel extends MTEBasicMachineSteel {

    public MTESteamFurnaceSteel(int aID, String aName, String aNameRegional) {
        super(aID, aName, aNameRegional, "Smelting things with compressed Steam", 1, 1, true);
    }

    public MTESteamFurnaceSteel(String aName, String[] aDescription, ITexture[][][] aTextures) {
        super(aName, aDescription, aTextures, 1, 1, true);
    }

    @Override
    protected boolean isBricked() {
        return true;
    }

    @Override
    public MetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new MTESteamFurnaceSteel(this.mName, this.mDescriptionArray, this.mTextures);
    }

    @Override
    public RecipeMap<?> getRecipeMap() {
        return RecipeMaps.furnaceRecipes;
    }

    @Override
    public int checkRecipe() {
        if (null != (this.mOutputItems[0] = GTModHandler.getSmeltingOutput(getInputAt(0), true, getOutputAt(0)))) {
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
            && GTModHandler.getSmeltingOutput(GTUtility.copyAmount(64, aStack), false, null) != null;
    }

    @Override
    public void startSoundLoop(byte aIndex, double aX, double aY, double aZ) {
        super.startSoundLoop(aIndex, aX, aY, aZ);
        if (aIndex == 1) {
            GTUtility.doSoundAtClient(SoundResource.IC2_MACHINES_ELECTROFURNACE_LOOP, 10, 1.0F, aX, aY, aZ);
        }
    }

    @Override
    public void startProcess() {
        sendLoopStart((byte) 1);
    }

    @Override
    public ITexture[] getSideFacingActive(byte aColor) {
        return new ITexture[] { super.getSideFacingActive(aColor)[0],
            TextureFactory.of(new Textures.BlockIcons.CustomIcon("basicmachines/furnace/OVERLAY_SIDE_ACTIVE")),
            TextureFactory.builder()
                .addIcon(new Textures.BlockIcons.CustomIcon("basicmachines/furnace/OVERLAY_SIDE_ACTIVE_GLOW"))
                .glow()
                .build() };
    }

    @Override
    public ITexture[] getSideFacingInactive(byte aColor) {
        return new ITexture[] { super.getSideFacingInactive(aColor)[0],
            TextureFactory.of(new Textures.BlockIcons.CustomIcon("basicmachines/furnace/OVERLAY_SIDE")),
            TextureFactory.builder()
                .addIcon(new Textures.BlockIcons.CustomIcon("basicmachines/furnace/OVERLAY_SIDE_GLOW"))
                .glow()
                .build() };
    }

    @Override
    public ITexture[] getFrontFacingActive(byte aColor) {
        return new ITexture[] { super.getFrontFacingActive(aColor)[0],
            TextureFactory.of(new Textures.BlockIcons.CustomIcon("basicmachines/furnace/OVERLAY_FRONT_ACTIVE")),
            TextureFactory.builder()
                .addIcon(new Textures.BlockIcons.CustomIcon("basicmachines/furnace/OVERLAY_FRONT_ACTIVE_GLOW"))
                .glow()
                .build() };
    }

    @Override
    public ITexture[] getFrontFacingInactive(byte aColor) {
        return new ITexture[] { super.getFrontFacingInactive(aColor)[0],
            TextureFactory.of(new Textures.BlockIcons.CustomIcon("basicmachines/furnace/OVERLAY_FRONT")),
            TextureFactory.builder()
                .addIcon(new Textures.BlockIcons.CustomIcon("basicmachines/furnace/OVERLAY_FRONT_GLOW"))
                .glow()
                .build() };
    }

    @Override
    public ITexture[] getTopFacingActive(byte aColor) {
        return new ITexture[] { super.getTopFacingActive(aColor)[0],
            TextureFactory.of(new Textures.BlockIcons.CustomIcon("basicmachines/furnace/OVERLAY_TOP_ACTIVE")),
            TextureFactory.builder()
                .addIcon(new Textures.BlockIcons.CustomIcon("basicmachines/furnace/OVERLAY_TOP_ACTIVE_GLOW"))
                .glow()
                .build() };
    }

    @Override
    public ITexture[] getTopFacingInactive(byte aColor) {
        return new ITexture[] { super.getTopFacingInactive(aColor)[0],
            TextureFactory.of(new Textures.BlockIcons.CustomIcon("basicmachines/furnace/OVERLAY_TOP")),
            TextureFactory.builder()
                .addIcon(new Textures.BlockIcons.CustomIcon("basicmachines/furnace/OVERLAY_TOP_GLOW"))
                .glow()
                .build() };
    }

    @Override
    public ITexture[] getBottomFacingActive(byte aColor) {
        return new ITexture[] { super.getBottomFacingActive(aColor)[0],
            TextureFactory.of(new Textures.BlockIcons.CustomIcon("basicmachines/furnace/OVERLAY_BOTTOM_ACTIVE")),
            TextureFactory.builder()
                .addIcon(new Textures.BlockIcons.CustomIcon("basicmachines/furnace/OVERLAY_BOTTOM_ACTIVE_GLOW"))
                .glow()
                .build() };
    }

    @Override
    public ITexture[] getBottomFacingInactive(byte aColor) {
        return new ITexture[] { super.getBottomFacingInactive(aColor)[0],
            TextureFactory.of(new Textures.BlockIcons.CustomIcon("basicmachines/furnace/OVERLAY_BOTTOM")),
            TextureFactory.builder()
                .addIcon(new Textures.BlockIcons.CustomIcon("basicmachines/furnace/OVERLAY_BOTTOM_GLOW"))
                .glow()
                .build() };
    }
}
