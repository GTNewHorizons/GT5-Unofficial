package gregtech.api.metatileentity.examples;

import static gregtech.api.enums.Textures.BlockIcons.*;

import net.minecraft.item.ItemStack;

import gregtech.api.enums.SoundResource;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.MetaTileEntity;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_BasicMachine;
import gregtech.api.render.TextureFactory;
import gregtech.api.util.GT_ModHandler;
import gregtech.api.util.GT_Utility;

/**
 * This Example Implementation still works, however I use something completely different in my own Code.
 */
public class GT_MetaTileEntity_E_Furnace extends GT_MetaTileEntity_BasicMachine {

    public GT_MetaTileEntity_E_Furnace(int aID, String aName, String aNameRegional, int aTier) {
        super(
            aID,
            aName,
            aNameRegional,
            aTier,
            1,
            "Not like using a Commodore 64",
            1,
            1,
            "E_Furnace.png",
            "smelting",
            TextureFactory.of(
                TextureFactory.of(OVERLAY_SIDE_STEAM_FURNACE_ACTIVE),
                TextureFactory.builder()
                    .addIcon(OVERLAY_SIDE_STEAM_FURNACE_ACTIVE_GLOW)
                    .glow()
                    .build()),
            TextureFactory.of(
                TextureFactory.of(OVERLAY_SIDE_STEAM_FURNACE),
                TextureFactory.builder()
                    .addIcon(OVERLAY_SIDE_STEAM_FURNACE_GLOW)
                    .glow()
                    .build()),
            TextureFactory.of(
                TextureFactory.of(OVERLAY_FRONT_STEAM_FURNACE_ACTIVE),
                TextureFactory.builder()
                    .addIcon(OVERLAY_FRONT_STEAM_FURNACE_ACTIVE_GLOW)
                    .glow()
                    .build()),
            TextureFactory.of(
                TextureFactory.of(OVERLAY_FRONT_STEAM_FURNACE),
                TextureFactory.builder()
                    .addIcon(OVERLAY_FRONT_STEAM_FURNACE_GLOW)
                    .glow()
                    .build()),
            TextureFactory.of(
                TextureFactory.of(OVERLAY_TOP_STEAM_FURNACE_ACTIVE),
                TextureFactory.builder()
                    .addIcon(OVERLAY_TOP_STEAM_FURNACE_ACTIVE_GLOW)
                    .glow()
                    .build()),
            TextureFactory.of(
                TextureFactory.of(OVERLAY_TOP_STEAM_FURNACE),
                TextureFactory.builder()
                    .addIcon(OVERLAY_TOP_STEAM_FURNACE_GLOW)
                    .glow()
                    .build()),
            TextureFactory.of(
                TextureFactory.of(OVERLAY_BOTTOM_STEAM_FURNACE_ACTIVE),
                TextureFactory.builder()
                    .addIcon(OVERLAY_BOTTOM_STEAM_FURNACE_ACTIVE_GLOW)
                    .glow()
                    .build()),
            TextureFactory.of(
                TextureFactory.of(OVERLAY_BOTTOM_STEAM_FURNACE),
                TextureFactory.builder()
                    .addIcon(OVERLAY_BOTTOM_STEAM_FURNACE_GLOW)
                    .glow()
                    .build()));
    }

    public GT_MetaTileEntity_E_Furnace(String aName, int aTier, String aDescription, ITexture[][][] aTextures,
        String aGUIName, String aNEIName) {
        super(aName, aTier, 1, aDescription, aTextures, 1, 1, aGUIName, aNEIName);
    }

    public GT_MetaTileEntity_E_Furnace(String aName, int aTier, String[] aDescription, ITexture[][][] aTextures,
        String aGUIName, String aNEIName) {
        super(aName, aTier, 1, aDescription, aTextures, 1, 1, aGUIName, aNEIName);
    }

    @Override
    public MetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new GT_MetaTileEntity_E_Furnace(mName, mTier, mDescriptionArray, mTextures, mGUIName, mNEIName);
    }

    @Override
    public int checkRecipe() {
        if (null != (mOutputItems[0] = GT_ModHandler.getSmeltingOutput(getInputAt(0), true, getOutputAt(0)))) {
            calculateOverclockedNess(4, 128);
            if (mMaxProgresstime == Integer.MAX_VALUE - 1 && mEUt == Integer.MAX_VALUE - 1)
                return FOUND_RECIPE_BUT_DID_NOT_MEET_REQUIREMENTS;
            return FOUND_AND_SUCCESSFULLY_USED_RECIPE;
        }
        return DID_NOT_FIND_RECIPE;
    }

    @Override
    protected boolean allowPutStackValidated(IGregTechTileEntity aBaseMetaTileEntity, int aIndex, byte aSide,
        ItemStack aStack) {
        return super.allowPutStackValidated(aBaseMetaTileEntity, aIndex, aSide, aStack)
            && GT_ModHandler.getSmeltingOutput(GT_Utility.copyAmount(64, aStack), false, null) != null;
    }

    @Override
    public void startSoundLoop(byte aIndex, double aX, double aY, double aZ) {
        super.startSoundLoop(aIndex, aX, aY, aZ);
        if (aIndex == 1)
            GT_Utility.doSoundAtClient(SoundResource.IC2_MACHINES_ELECTROFURNACE_LOOP, 10, 1.0F, aX, aY, aZ);
    }

    @Override
    public void startProcess() {
        sendLoopStart((byte) 1);
    }
}
