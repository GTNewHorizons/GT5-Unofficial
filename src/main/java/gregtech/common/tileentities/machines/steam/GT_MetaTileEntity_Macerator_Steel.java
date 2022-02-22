package gregtech.common.tileentities.machines.steam;

import gregtech.api.GregTech_API;
import gregtech.api.gui.GT_GUIContainer_BasicMachine;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.MetaTileEntity;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_BasicMachine_Steel;
import gregtech.api.render.TextureFactory;
import gregtech.api.util.GT_Recipe;
import gregtech.api.util.GT_Recipe.GT_Recipe_Map;
import gregtech.api.util.GT_Utility;
import gregtech.api.util.WorldSpawnedEventBuilder;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

import java.util.Random;

import static gregtech.api.enums.GT_Values.V;
import static gregtech.api.enums.Textures.BlockIcons.*;

public class GT_MetaTileEntity_Macerator_Steel extends GT_MetaTileEntity_BasicMachine_Steel {
    public GT_MetaTileEntity_Macerator_Steel(int aID, String aName, String aNameRegional) {
        super(aID, aName, aNameRegional, "Macerating your Ores", 1, 1, true);
    }

    public GT_MetaTileEntity_Macerator_Steel(String aName, String aDescription, ITexture[][][] aTextures) {
        super(aName, aDescription, aTextures, 1, 1, true);
    }

    public GT_MetaTileEntity_Macerator_Steel(String aName, String[] aDescription, ITexture[][][] aTextures) {
        super(aName, aDescription, aTextures, 1, 1, true);
    }

    @Override
    public Object getClientGUI(int aID, InventoryPlayer aPlayerInventory, IGregTechTileEntity aBaseMetaTileEntity) {
        return new GT_GUIContainer_BasicMachine(aPlayerInventory, aBaseMetaTileEntity, getLocalName(), "SteelMacerator.png", GT_Recipe_Map.sMaceratorRecipes.mUnlocalizedName);
    }

    @Override
    public MetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new GT_MetaTileEntity_Macerator_Steel(this.mName, this.mDescriptionArray, this.mTextures);
    }

    @Override
    public void onPreTick(IGregTechTileEntity aBaseMetaTileEntity, long aTick) {
        super.onPreTick(aBaseMetaTileEntity, aTick);
        if ((aBaseMetaTileEntity.isClientSide()) && (aBaseMetaTileEntity.isActive()) && (aBaseMetaTileEntity.getFrontFacing() != 1) && (aBaseMetaTileEntity.getCoverIDAtSide((byte) 1) == 0) && (!aBaseMetaTileEntity.getOpacityAtSide((byte) 1))) {
            Random tRandom = getBaseMetaTileEntity().getWorld().rand;
            new WorldSpawnedEventBuilder.ParticleEventBuilder()
                    .setMotion(0D, 0.0D, 0D)
                    .setIdentifier("smoke")
                    .setPosition(
                            aBaseMetaTileEntity.getXCoord() + 0.8F - tRandom.nextFloat() * 0.6F,
                            aBaseMetaTileEntity.getYCoord() + 0.9F + tRandom.nextFloat() * 0.2F,
                            aBaseMetaTileEntity.getZCoord() + 0.8F - tRandom.nextFloat() * 0.6F
                    )
                    .setWorld(getBaseMetaTileEntity().getWorld())
                    .run();
        }
    }

    @Override
    public GT_Recipe.GT_Recipe_Map getRecipeList() {
        return GT_Recipe.GT_Recipe_Map.sMaceratorRecipes;
    }

    @Override
    public int checkRecipe() {
        GT_Recipe tRecipe = getRecipeList().findRecipe(getBaseMetaTileEntity(), mLastRecipe, false, V[mTier], null, null, getAllInputs());
        if (tRecipe == null) return DID_NOT_FIND_RECIPE;
        if (tRecipe.mCanBeBuffered) mLastRecipe = tRecipe;
        if (!canOutput(tRecipe)) {
            mOutputBlocked++;
            return FOUND_RECIPE_BUT_DID_NOT_MEET_REQUIREMENTS;
        }

        if (!tRecipe.isRecipeInputEqual(true, new FluidStack[]{getFillableStack()}, getAllInputs()))
            return FOUND_RECIPE_BUT_DID_NOT_MEET_REQUIREMENTS;
        if (tRecipe.getOutput(0) != null) mOutputItems[0] = tRecipe.getOutput(0);
        calculateOverclockedNess(tRecipe);
        return FOUND_AND_SUCCESSFULLY_USED_RECIPE;
    }

    @Override
    public boolean allowPutStackValidated(IGregTechTileEntity aBaseMetaTileEntity, int aIndex, byte aSide, ItemStack aStack) {
        return super.allowPutStackValidated(aBaseMetaTileEntity, aIndex, aSide, aStack) && GT_Recipe_Map.sMaceratorRecipes.containsInput(GT_Utility.copyAmount(64L, aStack));
    }

    @Override
    public void startSoundLoop(byte aIndex, double aX, double aY, double aZ) {
        super.startSoundLoop(aIndex, aX, aY, aZ);
        if (aIndex == 1) {
            GT_Utility.doSoundAtClient(GregTech_API.sSoundList.get(201), 10, 1.0F, aX, aY, aZ);
        }
    }

    @Override
    public void startProcess() {
        sendLoopStart((byte) 1);
    }

    @Override
    public ITexture[] getSideFacingActive(byte aColor) {
        return new ITexture[]{
                super.getSideFacingActive(aColor)[0],
                TextureFactory.of(OVERLAY_SIDE_STEAM_MACERATOR_ACTIVE),
                TextureFactory.builder().addIcon(OVERLAY_SIDE_STEAM_MACERATOR_ACTIVE_GLOW).glow().build()};
    }

    @Override
    public ITexture[] getSideFacingInactive(byte aColor) {
        return new ITexture[]{
                super.getSideFacingInactive(aColor)[0],
                TextureFactory.of(OVERLAY_SIDE_STEAM_MACERATOR),
                TextureFactory.builder().addIcon(OVERLAY_SIDE_STEAM_MACERATOR_GLOW).glow().build()};
    }

    @Override
    public ITexture[] getFrontFacingActive(byte aColor) {
        return new ITexture[]{
                super.getFrontFacingActive(aColor)[0],
                TextureFactory.of(OVERLAY_FRONT_STEAM_MACERATOR_ACTIVE),
                TextureFactory.builder().addIcon(OVERLAY_FRONT_STEAM_MACERATOR_ACTIVE_GLOW).glow().build()};
    }

    @Override
    public ITexture[] getFrontFacingInactive(byte aColor) {
        return new ITexture[]{super.getFrontFacingInactive(aColor)[0],
                TextureFactory.of(OVERLAY_FRONT_STEAM_MACERATOR),
                TextureFactory.builder().addIcon(OVERLAY_FRONT_STEAM_MACERATOR_GLOW).glow().build()};
    }

    @Override
    public ITexture[] getTopFacingActive(byte aColor) {
        return new ITexture[]{super.getTopFacingActive(aColor)[0],
                TextureFactory.of(OVERLAY_TOP_STEAM_MACERATOR_ACTIVE),
                TextureFactory.builder().addIcon(OVERLAY_TOP_STEAM_MACERATOR_ACTIVE_GLOW).glow().build()};
    }

    @Override
    public ITexture[] getTopFacingInactive(byte aColor) {
        return new ITexture[]{super.getTopFacingInactive(aColor)[0],
                TextureFactory.of(OVERLAY_TOP_STEAM_MACERATOR),
                TextureFactory.builder().addIcon(OVERLAY_TOP_STEAM_MACERATOR_GLOW).glow().build()};
    }

    @Override
    public ITexture[] getBottomFacingActive(byte aColor) {
        return new ITexture[]{super.getBottomFacingActive(aColor)[0],
                TextureFactory.of(OVERLAY_BOTTOM_STEAM_MACERATOR_ACTIVE),
                TextureFactory.builder().addIcon(OVERLAY_BOTTOM_STEAM_MACERATOR_ACTIVE_GLOW).glow().build()};
    }

    @Override
    public ITexture[] getBottomFacingInactive(byte aColor) {
        return new ITexture[]{super.getBottomFacingInactive(aColor)[0],
                TextureFactory.of(OVERLAY_BOTTOM_STEAM_MACERATOR),
                TextureFactory.builder().addIcon(OVERLAY_BOTTOM_STEAM_MACERATOR_GLOW).glow().build()};
    }
}
