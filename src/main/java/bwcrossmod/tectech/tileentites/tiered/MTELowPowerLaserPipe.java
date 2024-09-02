/*
 * Copyright (c) 2018-2020 bartimaeusnek Permission is hereby granted, free of charge, to any person obtaining a copy of
 * this software and associated documentation files (the "Software"), to deal in the Software without restriction,
 * including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following
 * conditions: The above copyright notice and this permission notice shall be included in all copies or substantial
 * portions of the Software. THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED,
 * INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT.
 * IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN
 * ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER
 * DEALINGS IN THE SOFTWARE.
 */

package bwcrossmod.tectech.tileentites.tiered;

import java.util.ArrayList;
import java.util.HashSet;

import net.minecraft.block.Block;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.common.util.ForgeDirection;

import bartworks.util.BWTooltipReference;
import gregtech.api.enums.Materials;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.ICoverable;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.implementations.MTECable;
import gregtech.api.render.TextureFactory;
import gregtech.api.util.CoverBehavior;
import gregtech.common.GTClient;
import ic2.core.Ic2Items;

public class MTELowPowerLaserPipe extends MTECable implements LowPowerLaser {

    public MTELowPowerLaserPipe(int aID, String aName, String aNameRegional) {
        super(aID, aName, aNameRegional, 0.25f, Materials.BorosilicateGlass, 0, 0, 0, false, false);
    }

    public MTELowPowerLaserPipe(String aName, float aThickNess, Materials aMaterial, long aCableLossPerMeter,
        long aAmperage, long aVoltage, boolean aInsulated, boolean aCanShock) {
        super(aName, aThickNess, aMaterial, aCableLossPerMeter, aAmperage, aVoltage, aInsulated, aCanShock);
    }

    @Override
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity iGregTechTileEntity) {
        return new MTELowPowerLaserPipe(
            this.mName,
            this.mThickNess,
            this.mMaterial,
            this.mCableLossPerMeter,
            this.mAmperage,
            this.mVoltage,
            this.mInsulated,
            this.mCanShock);
    }

    @Override
    public void onPostTick(IGregTechTileEntity aBaseMetaTileEntity, long aTick) {
        if (aTick % 20 == 13 && aBaseMetaTileEntity.isClientSide() && GTClient.changeDetected == 4) {
            aBaseMetaTileEntity.issueTextureUpdate();
        }
    }

    public ITexture[] getTexture(IGregTechTileEntity aBaseMetaTileEntity, ForgeDirection siide, byte aConnections,
        int aColorIndex, boolean aConnected, boolean aRedstone) {
        return new ITexture[] { TextureFactory.of(
            Block.getBlockFromItem(Ic2Items.glassFiberCableBlock.getItem()),
            Ic2Items.glassFiberCableBlock.getItemDamage(),
            siide) };
    }

    @Override
    public String[] getDescription() {
        return new String[] { "Primitive Laser Cable intended for Low Power Applications", "Does not auto-connect",
            "Does not turn or bend",
            EnumChatFormatting.WHITE + "Must be "
                + EnumChatFormatting.YELLOW
                + "c"
                + EnumChatFormatting.RED
                + "o"
                + EnumChatFormatting.BLUE
                + "l"
                + EnumChatFormatting.DARK_PURPLE
                + "o"
                + EnumChatFormatting.GOLD
                + "r"
                + EnumChatFormatting.DARK_RED
                + "e"
                + EnumChatFormatting.DARK_GREEN
                + "d"
                + EnumChatFormatting.WHITE
                + " in order to work",
            BWTooltipReference.ADDED_BY_BARTIMAEUSNEK_VIA_BARTWORKS.get() };
    }

    @Override
    public boolean isSender() {
        return false;
    }

    @Override
    public boolean isReceiver() {
        return false;
    }

    @Override
    public boolean isTunnel() {
        return true;
    }

    public boolean canConnect(ForgeDirection side) {
        return true;
    }

    @Override
    public boolean isGivingInformation() {
        return false;
    }

    @Override
    public boolean isConnectedCorrectly(ForgeDirection side) {
        return this.isConnectedAtSide(side) && this.isConnectedAtSide(side.getOpposite());
    }

    @Override
    public boolean shouldJoinIc2Enet() {
        return false;
    }

    @Override
    public long injectEnergyUnits(ForgeDirection side, long aVoltage, long aAmperage) {
        return 0L;
    }

    @Override
    @Deprecated
    public long transferElectricity(ForgeDirection side, long aVoltage, long aAmperage,
        ArrayList<TileEntity> aAlreadyPassedTileEntityList) {
        return 0L;
    }

    @Override
    public long transferElectricity(ForgeDirection side, long aVoltage, long aAmperage,
        HashSet<TileEntity> aAlreadyPassedSet) {
        return 0L;
    }

    @Override
    public boolean letsIn(CoverBehavior coverBehavior, ForgeDirection side, int aCoverID, int aCoverVariable,
        ICoverable aTileEntity) {
        return true;
    }

    @Override
    public boolean letsOut(CoverBehavior coverBehavior, ForgeDirection side, int aCoverID, int aCoverVariable,
        ICoverable aTileEntity) {
        return true;
    }
}
