package gregtech.common.tileentities.machines.steam;

import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_BOTTOM_STEAM_MACERATOR;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_BOTTOM_STEAM_MACERATOR_ACTIVE;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_BOTTOM_STEAM_MACERATOR_ACTIVE_GLOW;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_BOTTOM_STEAM_MACERATOR_GLOW;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_STEAM_MACERATOR;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_STEAM_MACERATOR_ACTIVE;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_STEAM_MACERATOR_ACTIVE_GLOW;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_STEAM_MACERATOR_GLOW;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_SIDE_STEAM_MACERATOR;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_SIDE_STEAM_MACERATOR_ACTIVE;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_SIDE_STEAM_MACERATOR_ACTIVE_GLOW;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_SIDE_STEAM_MACERATOR_GLOW;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_TOP_STEAM_MACERATOR;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_TOP_STEAM_MACERATOR_ACTIVE;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_TOP_STEAM_MACERATOR_ACTIVE_GLOW;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_TOP_STEAM_MACERATOR_GLOW;
import static gregtech.api.objects.XSTR.XSTR_INSTANCE;

import net.minecraft.item.ItemStack;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.FluidStack;

import gregtech.api.enums.ParticleFX;
import gregtech.api.enums.SoundResource;
import gregtech.api.enums.TierEU;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.MetaTileEntity;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_BasicMachine_Bronze;
import gregtech.api.recipe.RecipeMap;
import gregtech.api.recipe.RecipeMaps;
import gregtech.api.render.TextureFactory;
import gregtech.api.util.GT_Recipe;
import gregtech.api.util.GT_Utility;
import gregtech.api.util.WorldSpawnedEventBuilder.ParticleEventBuilder;

public class GT_MetaTileEntity_Macerator_Bronze extends GT_MetaTileEntity_BasicMachine_Bronze {

    public GT_MetaTileEntity_Macerator_Bronze(int aID, String aName, String aNameRegional) {
        super(aID, aName, aNameRegional, "Macerating your Ores", 1, 1, false);
    }

    public GT_MetaTileEntity_Macerator_Bronze(String aName, String[] aDescription, ITexture[][][] aTextures) {
        super(aName, aDescription, aTextures, 1, 1, false);
    }

    @Override
    public MetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new GT_MetaTileEntity_Macerator_Bronze(this.mName, this.mDescriptionArray, this.mTextures);
    }

    @Override
    public void onPreTick(IGregTechTileEntity aBaseMetaTileEntity, long aTick) {
        super.onPreTick(aBaseMetaTileEntity, aTick);
        if (aBaseMetaTileEntity.isClientSide() && aBaseMetaTileEntity.isActive()) {

            if (aBaseMetaTileEntity.getFrontFacing() != ForgeDirection.UP
                && aBaseMetaTileEntity.getCoverIDAtSide(ForgeDirection.UP) == 0
                && !aBaseMetaTileEntity.getOpacityAtSide(ForgeDirection.UP)) {

                new ParticleEventBuilder().setMotion(0.0D, 0.0D, 0.0D)
                    .setIdentifier(ParticleFX.SMOKE)
                    .setPosition(
                        aBaseMetaTileEntity.getXCoord() + 0.8F - XSTR_INSTANCE.nextFloat() * 0.6F,
                        aBaseMetaTileEntity.getYCoord() + 0.9F + XSTR_INSTANCE.nextFloat() * 0.2F,
                        aBaseMetaTileEntity.getZCoord() + 0.8F - XSTR_INSTANCE.nextFloat() * 0.6F)
                    .setWorld(getBaseMetaTileEntity().getWorld())
                    .run();
            }
        }
    }

    @Override
    public RecipeMap getRecipeList() {
        return RecipeMaps.maceratorRecipes;
    }

    @Override
    public int checkRecipe() {
        GT_Recipe tRecipe = getRecipeList()
            .findRecipe(getBaseMetaTileEntity(), mLastRecipe, false, TierEU.LV, null, null, getAllInputs());
        if (tRecipe == null) return DID_NOT_FIND_RECIPE;
        if (tRecipe.mCanBeBuffered) mLastRecipe = tRecipe;
        if (!canOutput(tRecipe)) {
            mOutputBlocked++;
            return FOUND_RECIPE_BUT_DID_NOT_MEET_REQUIREMENTS;
        }

        if (!tRecipe.isRecipeInputEqual(true, new FluidStack[] { getFillableStack() }, getAllInputs()))
            return FOUND_RECIPE_BUT_DID_NOT_MEET_REQUIREMENTS;
        if (tRecipe.getOutput(0) != null) mOutputItems[0] = tRecipe.getOutput(0);
        calculateOverclockedNess(tRecipe);
        return FOUND_AND_SUCCESSFULLY_USED_RECIPE;
    }

    @Override
    protected boolean allowPutStackValidated(IGregTechTileEntity aBaseMetaTileEntity, int aIndex, ForgeDirection side,
        ItemStack aStack) {
        return super.allowPutStackValidated(aBaseMetaTileEntity, aIndex, side, aStack)
            && RecipeMaps.maceratorRecipes.containsInput(GT_Utility.copyAmount(64L, aStack));
    }

    @Override
    public void startSoundLoop(byte aIndex, double aX, double aY, double aZ) {
        super.startSoundLoop(aIndex, aX, aY, aZ);
        if (aIndex == 1) {
            GT_Utility.doSoundAtClient(SoundResource.IC2_MACHINES_MACERATOR_OP, 10, 1.0F, aX, aY, aZ);
        }
    }

    @Override
    public void startProcess() {
        sendLoopStart((byte) 1);
    }

    @Override
    public ITexture[] getSideFacingActive(byte aColor) {
        return new ITexture[] { super.getSideFacingActive(aColor)[0],
            TextureFactory.of(OVERLAY_SIDE_STEAM_MACERATOR_ACTIVE), TextureFactory.builder()
                .addIcon(OVERLAY_SIDE_STEAM_MACERATOR_ACTIVE_GLOW)
                .glow()
                .build() };
    }

    @Override
    public ITexture[] getSideFacingInactive(byte aColor) {
        return new ITexture[] { super.getSideFacingInactive(aColor)[0], TextureFactory.of(OVERLAY_SIDE_STEAM_MACERATOR),
            TextureFactory.builder()
                .addIcon(OVERLAY_SIDE_STEAM_MACERATOR_GLOW)
                .glow()
                .build() };
    }

    @Override
    public ITexture[] getFrontFacingActive(byte aColor) {
        return new ITexture[] { super.getFrontFacingActive(aColor)[0],
            TextureFactory.of(OVERLAY_FRONT_STEAM_MACERATOR_ACTIVE), TextureFactory.builder()
                .addIcon(OVERLAY_FRONT_STEAM_MACERATOR_ACTIVE_GLOW)
                .glow()
                .build() };
    }

    @Override
    public ITexture[] getFrontFacingInactive(byte aColor) {
        return new ITexture[] { super.getFrontFacingInactive(aColor)[0],
            TextureFactory.of(OVERLAY_FRONT_STEAM_MACERATOR), TextureFactory.builder()
                .addIcon(OVERLAY_FRONT_STEAM_MACERATOR_GLOW)
                .glow()
                .build() };
    }

    @Override
    public ITexture[] getTopFacingActive(byte aColor) {
        return new ITexture[] { super.getTopFacingActive(aColor)[0],
            TextureFactory.of(OVERLAY_TOP_STEAM_MACERATOR_ACTIVE), TextureFactory.builder()
                .addIcon(OVERLAY_TOP_STEAM_MACERATOR_ACTIVE_GLOW)
                .glow()
                .build() };
    }

    @Override
    public ITexture[] getTopFacingInactive(byte aColor) {
        return new ITexture[] { super.getTopFacingInactive(aColor)[0], TextureFactory.of(OVERLAY_TOP_STEAM_MACERATOR),
            TextureFactory.builder()
                .addIcon(OVERLAY_TOP_STEAM_MACERATOR_GLOW)
                .glow()
                .build() };
    }

    @Override
    public ITexture[] getBottomFacingActive(byte aColor) {
        return new ITexture[] { super.getBottomFacingActive(aColor)[0],
            TextureFactory.of(OVERLAY_BOTTOM_STEAM_MACERATOR_ACTIVE), TextureFactory.builder()
                .addIcon(OVERLAY_BOTTOM_STEAM_MACERATOR_ACTIVE_GLOW)
                .glow()
                .build() };
    }

    @Override
    public ITexture[] getBottomFacingInactive(byte aColor) {
        return new ITexture[] { super.getBottomFacingInactive(aColor)[0],
            TextureFactory.of(OVERLAY_BOTTOM_STEAM_MACERATOR), TextureFactory.builder()
                .addIcon(OVERLAY_BOTTOM_STEAM_MACERATOR_GLOW)
                .glow()
                .build() };
    }
}
