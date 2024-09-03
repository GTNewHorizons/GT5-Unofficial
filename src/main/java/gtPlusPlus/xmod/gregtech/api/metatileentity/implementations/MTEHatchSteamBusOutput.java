package gtPlusPlus.xmod.gregtech.api.metatileentity.implementations;

import static gregtech.api.enums.Textures.BlockIcons.ITEM_OUT_SIGN;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_PIPE_OUT;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.util.ForgeDirection;

import com.gtnewhorizons.modularui.api.screen.ModularWindow;
import com.gtnewhorizons.modularui.api.screen.UIBuildContext;

import gregtech.GTMod;
import gregtech.api.enums.Textures;
import gregtech.api.gui.modularui.GTUIInfos;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.MetaTileEntity;
import gregtech.api.metatileentity.implementations.MTEHatch;
import gregtech.api.objects.GTRenderedTexture;
import gregtech.api.render.TextureFactory;
import gtPlusPlus.core.lib.GTPPCore;

public class MTEHatchSteamBusOutput extends MTEHatch {

    public MTEHatchSteamBusOutput(int aID, String aName, String aNameRegional, int aTier) {
        super(
            aID,
            aName,
            aNameRegional,
            aTier,
            4,
            new String[] { "Item Output for Steam Multiblocks", "Does not automatically export items",
                "Capacity: 4 stacks", "Does not work with non-steam multiblocks", GTPPCore.GT_Tooltip.get() });
    }

    public MTEHatchSteamBusOutput(String aName, int aTier, String aDescription, ITexture[][][] aTextures) {
        super(aName, aTier, 4, aDescription, aTextures);
    }

    public MTEHatchSteamBusOutput(String aName, int aTier, String[] aDescription, ITexture[][][] aTextures) {
        super(aName, aTier, 4, aDescription, aTextures);
    }

    @Override
    public ITexture[] getTexturesActive(ITexture aBaseTexture) {
        return GTMod.gregtechproxy.mRenderIndicatorsOnHatch
            ? new ITexture[] { aBaseTexture, TextureFactory.of(OVERLAY_PIPE_OUT), TextureFactory.of(ITEM_OUT_SIGN) }
            : new ITexture[] { aBaseTexture, TextureFactory.of(OVERLAY_PIPE_OUT) };
    }

    @Override
    public ITexture[] getTexturesInactive(ITexture aBaseTexture) {
        return GTMod.gregtechproxy.mRenderIndicatorsOnHatch
            ? new ITexture[] { aBaseTexture, TextureFactory.of(OVERLAY_PIPE_OUT), TextureFactory.of(ITEM_OUT_SIGN) }
            : new ITexture[] { aBaseTexture, TextureFactory.of(OVERLAY_PIPE_OUT) };
    }

    @Override
    public boolean isSimpleMachine() {
        return true;
    }

    @Override
    public boolean isFacingValid(ForgeDirection facing) {
        return true;
    }

    @Override
    public boolean isAccessAllowed(EntityPlayer aPlayer) {
        return true;
    }

    @Override
    public boolean isValidSlot(int aIndex) {
        return true;
    }

    @Override
    public MetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new MTEHatchSteamBusOutput(mName, mTier, mDescriptionArray, mTextures);
    }

    @Override
    public boolean onRightclick(IGregTechTileEntity aBaseMetaTileEntity, EntityPlayer aPlayer) {
        GTUIInfos.openGTTileEntityUI(aBaseMetaTileEntity, aPlayer);
        return true;
    }

    @Override
    public boolean allowPullStack(IGregTechTileEntity aBaseMetaTileEntity, int aIndex, ForgeDirection side,
        ItemStack aStack) {
        return side == aBaseMetaTileEntity.getFrontFacing();
    }

    @Override
    public boolean allowPutStack(IGregTechTileEntity aBaseMetaTileEntity, int aIndex, ForgeDirection side,
        ItemStack aStack) {
        return false;
    }

    @Override
    public ITexture[][][] getTextureSet(ITexture[] aTextures) {
        ITexture[][][] rTextures = new ITexture[14][17][];
        for (byte c = -1; c < 16; c++) {
            if (rTextures[0][c + 1] == null) rTextures[0][c + 1] = getSideFacingActive(c);
            if (rTextures[1][c + 1] == null) rTextures[1][c + 1] = getSideFacingInactive(c);
            if (rTextures[2][c + 1] == null) rTextures[2][c + 1] = getFrontFacingActive(c);
            if (rTextures[3][c + 1] == null) rTextures[3][c + 1] = getFrontFacingInactive(c);
            if (rTextures[4][c + 1] == null) rTextures[4][c + 1] = getTopFacingActive(c);
            if (rTextures[5][c + 1] == null) rTextures[5][c + 1] = getTopFacingInactive(c);
            if (rTextures[6][c + 1] == null) rTextures[6][c + 1] = getBottomFacingActive(c);
            if (rTextures[7][c + 1] == null) rTextures[7][c + 1] = getBottomFacingInactive(c);
            if (rTextures[8][c + 1] == null) rTextures[8][c + 1] = getBottomFacingPipeActive(c);
            if (rTextures[9][c + 1] == null) rTextures[9][c + 1] = getBottomFacingPipeInactive(c);
            if (rTextures[10][c + 1] == null) rTextures[10][c + 1] = getTopFacingPipeActive(c);
            if (rTextures[11][c + 1] == null) rTextures[11][c + 1] = getTopFacingPipeInactive(c);
            if (rTextures[12][c + 1] == null) rTextures[12][c + 1] = getSideFacingPipeActive(c);
            if (rTextures[13][c + 1] == null) rTextures[13][c + 1] = getSideFacingPipeInactive(c);
        }
        return rTextures;
    }

    public ITexture[] getSideFacingActive(byte aColor) {
        return new ITexture[] { new GTRenderedTexture(
            mTier == 1 ? Textures.BlockIcons.MACHINE_BRONZEBRICKS_SIDE : Textures.BlockIcons.MACHINE_BRONZE_SIDE) };
    }

    public ITexture[] getSideFacingInactive(byte aColor) {
        return new ITexture[] { new GTRenderedTexture(
            mTier == 1 ? Textures.BlockIcons.MACHINE_BRONZEBRICKS_SIDE : Textures.BlockIcons.MACHINE_BRONZE_SIDE) };
    }

    public ITexture[] getFrontFacingActive(byte aColor) {
        return new ITexture[] { new GTRenderedTexture(
            mTier == 1 ? Textures.BlockIcons.MACHINE_BRONZEBRICKS_SIDE : Textures.BlockIcons.MACHINE_BRONZE_SIDE) };
    }

    public ITexture[] getFrontFacingInactive(byte aColor) {
        return new ITexture[] { new GTRenderedTexture(
            mTier == 1 ? Textures.BlockIcons.MACHINE_BRONZEBRICKS_SIDE : Textures.BlockIcons.MACHINE_BRONZE_SIDE) };
    }

    public ITexture[] getTopFacingActive(byte aColor) {
        return new ITexture[] { new GTRenderedTexture(
            mTier == 1 ? Textures.BlockIcons.MACHINE_BRONZEBRICKS_TOP : Textures.BlockIcons.MACHINE_BRONZE_TOP) };
    }

    public ITexture[] getTopFacingInactive(byte aColor) {
        return new ITexture[] { new GTRenderedTexture(
            mTier == 1 ? Textures.BlockIcons.MACHINE_BRONZEBRICKS_TOP : Textures.BlockIcons.MACHINE_BRONZE_TOP) };
    }

    public ITexture[] getBottomFacingActive(byte aColor) {
        return new ITexture[] { new GTRenderedTexture(
            mTier == 1 ? Textures.BlockIcons.MACHINE_BRONZEBRICKS_BOTTOM : Textures.BlockIcons.MACHINE_BRONZE_BOTTOM) };
    }

    public ITexture[] getBottomFacingInactive(byte aColor) {
        return new ITexture[] { new GTRenderedTexture(
            mTier == 1 ? Textures.BlockIcons.MACHINE_BRONZEBRICKS_BOTTOM : Textures.BlockIcons.MACHINE_BRONZE_BOTTOM) };
    }

    public ITexture[] getBottomFacingPipeActive(byte aColor) {
        return new ITexture[] { new GTRenderedTexture(
            mTier == 1 ? Textures.BlockIcons.MACHINE_BRONZEBRICKS_BOTTOM : Textures.BlockIcons.MACHINE_BRONZE_BOTTOM),
            new GTRenderedTexture(Textures.BlockIcons.OVERLAY_PIPE_OUT) };
    }

    public ITexture[] getBottomFacingPipeInactive(byte aColor) {
        return new ITexture[] { new GTRenderedTexture(
            mTier == 1 ? Textures.BlockIcons.MACHINE_BRONZEBRICKS_BOTTOM : Textures.BlockIcons.MACHINE_BRONZE_BOTTOM),
            new GTRenderedTexture(Textures.BlockIcons.OVERLAY_PIPE_OUT) };
    }

    public ITexture[] getTopFacingPipeActive(byte aColor) {
        return new ITexture[] {
            new GTRenderedTexture(
                mTier == 1 ? Textures.BlockIcons.MACHINE_BRONZEBRICKS_TOP : Textures.BlockIcons.MACHINE_BRONZE_TOP),
            new GTRenderedTexture(Textures.BlockIcons.OVERLAY_PIPE_OUT) };
    }

    public ITexture[] getTopFacingPipeInactive(byte aColor) {
        return new ITexture[] {
            new GTRenderedTexture(
                mTier == 1 ? Textures.BlockIcons.MACHINE_BRONZEBRICKS_TOP : Textures.BlockIcons.MACHINE_BRONZE_TOP),
            new GTRenderedTexture(Textures.BlockIcons.OVERLAY_PIPE_OUT) };
    }

    public ITexture[] getSideFacingPipeActive(byte aColor) {
        return new ITexture[] {
            new GTRenderedTexture(
                mTier == 1 ? Textures.BlockIcons.MACHINE_BRONZEBRICKS_SIDE : Textures.BlockIcons.MACHINE_BRONZE_SIDE),
            new GTRenderedTexture(Textures.BlockIcons.OVERLAY_PIPE_OUT) };
    }

    public ITexture[] getSideFacingPipeInactive(byte aColor) {
        return new ITexture[] {
            new GTRenderedTexture(
                mTier == 1 ? Textures.BlockIcons.MACHINE_BRONZEBRICKS_SIDE : Textures.BlockIcons.MACHINE_BRONZE_SIDE),
            new GTRenderedTexture(Textures.BlockIcons.OVERLAY_PIPE_OUT) };
    }

    @Override
    public void addUIWidgets(ModularWindow.Builder builder, UIBuildContext buildContext) {
        getBaseMetaTileEntity().add2by2Slots(builder);
    }
}
