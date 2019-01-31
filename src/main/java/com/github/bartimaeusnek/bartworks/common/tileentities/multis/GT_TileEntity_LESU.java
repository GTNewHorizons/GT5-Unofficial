/*
 * Copyright (c) 2019 bartimaeusnek
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package com.github.bartimaeusnek.bartworks.common.tileentities.multis;

import com.github.bartimaeusnek.bartworks.MainMod;
import com.github.bartimaeusnek.bartworks.client.gui.GT_GUIContainer_LESU;
import com.github.bartimaeusnek.bartworks.common.configs.ConfigHandler;
import com.github.bartimaeusnek.bartworks.common.loaders.ItemRegistry;
import com.github.bartimaeusnek.bartworks.server.container.GT_Container_LESU;
import com.github.bartimaeusnek.bartworks.util.ChatColorHelper;
import com.github.bartimaeusnek.bartworks.util.ConnectedBlocksChecker;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import gregtech.api.enums.Dyes;
import gregtech.api.enums.GT_Values;
import gregtech.api.interfaces.IIconContainer;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_MultiBlockBase;
import gregtech.api.objects.GT_RenderedTexture;
import gregtech.api.util.GT_Utility;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.IIcon;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;


public class GT_TileEntity_LESU extends GT_MetaTileEntity_MultiBlockBase {


    private static final byte TEXID_SIDE = 0;
    private static final byte TEXID_CHARGING = 1;
    private static final byte TEXID_IDLE = 2;
    private static final byte TEXID_EMPTY = 3;
    private static IIcon[] iIcons = new IIcon[4];
    private static IIconContainer[] iIconContainers = new IIconContainer[4];
    private static ITexture[][] iTextures = new ITexture[4][1];
    public ConnectedBlocksChecker connectedcells;
    public ItemStack[] circuits = new ItemStack[5];
    private long mStorage;

    public GT_TileEntity_LESU(int aID, String aName, String aNameRegional) {
        super(aID, aName, aNameRegional);
        this.mStorage = ConfigHandler.energyPerCell;
    }

    public GT_TileEntity_LESU(String aName) {
        super(aName);
    }


    @Override
    public boolean isEnetOutput() {
        return true;
    }

    @Override
    public boolean isEnetInput() {
        return true;
    }

    @Override
    public long maxEUStore() {
        return (this.mStorage >= Long.MAX_VALUE - 1 || this.mStorage < 0) ? Long.MAX_VALUE - 1 : this.mStorage;
    }

    @Override
    public long maxAmperesIn() {
        int ret = 0;
        for (int i = 0; i < 5; ++i)
            if (this.circuits[i] != null && this.circuits[i].getItem().equals(GT_Utility.getIntegratedCircuit(0).getItem()))
                ret += this.circuits[i].getItemDamage();
        return ret > 0 ? ret : 1;
    }

    @Override
    public long maxAmperesOut() {
        return maxAmperesIn();
    }

    @Override
    public long maxEUInput() {

        for (int i = 1; i < GT_Values.V.length; i++) {
            if (maxEUOutput() <= GT_Values.V[i] && maxEUOutput() > GT_Values.V[i - 1])
                return Math.min(GT_Values.V[i], 32768L);
        }

        return 8;
    }

    @Override
    public long maxEUOutput() {
        return Math.min(Math.max(this.mStorage / ConfigHandler.energyPerCell, 1L), 32768L);
    }

    @Override
    public int rechargerSlotStartIndex() {
        return 0;
    }

    @Override
    public int rechargerSlotCount() {
        return 1;
    }

    @Override
    public int dechargerSlotStartIndex() {
        return 1;
    }

    @Override
    public int dechargerSlotCount() {
        return 1;
    }

    @Override
    public boolean isTeleporterCompatible() {
        return true;
    }

    @Override
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity iGregTechTileEntity) {
        return new GT_TileEntity_LESU(mName);
    }

    @Override
    public String[] getDescription() {
        return new String[]{
                "Controller Block for the GT2-Styled L.E.S.U.",
                "Size: ANY",
                "Storage per LESU Casing: " + ConfigHandler.energyPerCell + "EU",
                "Output EU: LESU Casings amount" +
                        "Input EU: Next Voltage Tier to Output EU",
                "Input/Output Amps can be configured via 4 Circuits in GUI",
                "Output Side has a dot on it.",
                ChatColorHelper.RED + "Only one Controller allowed, no Wallsharing!",
                "Added by bartimaeusnek via " + ChatColorHelper.DARKGREEN + "BartWorks"
        };
    }

    @SideOnly(Side.CLIENT)
    public void registerIcons(IIconRegister aBlockIconRegister) {

        for (int i = 0; i < iTextures.length; i++) {
            iIcons[i] = aBlockIconRegister.registerIcon(MainMod.MOD_ID + ":LESU_CASING_" + i);
            final int finalI = i;
            iIconContainers[i] = new IIconContainer() {
                @Override
                public IIcon getIcon() {
                    return iIcons[finalI];
                }

                @Override
                public IIcon getOverlayIcon() {
                    return iIcons[finalI];
                }

                @Override
                public ResourceLocation getTextureFile() {
                    return new ResourceLocation(MainMod.MOD_ID + ":LESU_CASING_" + finalI);
                }
            };
        }

    }

    @Override
    public Object getClientGUI(int aID, InventoryPlayer aPlayerInventory, IGregTechTileEntity aBaseMetaTileEntity) {
        return new GT_GUIContainer_LESU(aPlayerInventory, aBaseMetaTileEntity);
    }

    @Override
    public Object getServerGUI(int aID, InventoryPlayer aPlayerInventory, IGregTechTileEntity aBaseMetaTileEntity) {
        return new GT_Container_LESU(aPlayerInventory, aBaseMetaTileEntity);
    }

    public boolean isServerSide() {
        return !isClientSide();
    }

    public boolean isClientSide() {
        if (getWorld() != null)
            return getWorld().isRemote ? FMLCommonHandler.instance().getSide() == Side.CLIENT : FMLCommonHandler.instance().getEffectiveSide() == Side.CLIENT;
        return FMLCommonHandler.instance().getEffectiveSide() == Side.CLIENT;
    }


    @Override
    public ITexture[] getTexture(IGregTechTileEntity aBaseMetaTileEntity, byte aSide, byte aFacing, byte aColorIndex, boolean aActive, boolean aRedstone) {

        ITexture[] ret = new ITexture[0];

        if (this.isClientSide()) {

            for (int i = 0; i < iTextures.length; i++) {
                iTextures[i][0] = new GT_RenderedTexture(iIconContainers[i], Dyes.getModulation(0, Dyes.MACHINE_METAL.mRGBa));
            }

            if (aSide == aFacing && this.getBaseMetaTileEntity().getUniversalEnergyStored() <= 0)
                ret = iTextures[TEXID_EMPTY];
            else if (aSide == aFacing && !aActive)
                ret = iTextures[TEXID_IDLE];
            else if (aSide == aFacing && aActive)
                ret = iTextures[TEXID_CHARGING];
            else
                ret = iTextures[TEXID_SIDE];
        }

        return ret;
    }

    @Override
    public boolean canInsertItem(int p_102007_1_, ItemStack p_102007_2_, int p_102007_3_) {
        return true;
    }

    @Override
    public boolean canExtractItem(int p_102008_1_, ItemStack p_102008_2_, int p_102008_3_) {
        return true;
    }

    @Override
    public int getSizeInventory() {
        return 6;
    }

    @Override
    public ItemStack getStackInSlot(int p_70301_1_) {
        if (p_70301_1_ > 1)
            return this.circuits[(p_70301_1_ - 2)];
        return this.mInventory[p_70301_1_];
    }

    @Override
    public void setInventorySlotContents(int p_70299_1_, ItemStack p_70299_2_) {
        if (p_70299_1_ < 2)
            this.mInventory[p_70299_1_] = p_70299_2_;
        else
            this.circuits[(p_70299_1_ - 2)] = p_70299_2_;
    }

    @Override
    public String getInventoryName() {
        return "L.E.S.U.";
    }

    @Override
    public boolean hasCustomInventoryName() {
        return true;
    }

    @Override
    public int getInventoryStackLimit() {
        return 1;
    }

    @Override
    public boolean isUseableByPlayer(EntityPlayer p_70300_1_) {
        return true;
    }

    @Override
    public boolean isItemValidForSlot(int p_94041_1_, ItemStack p_94041_2_) {

        switch (p_94041_1_) {
            case 0:
            case 1:
                return true;
            default:
                return p_94041_2_.getItem().equals(GT_Utility.getIntegratedCircuit(0).getItem());
        }
    }

    @Override
    public boolean isCorrectMachinePart(ItemStack itemStack) {
        return true;
    }

    @Override
    public boolean checkRecipe(ItemStack itemStack) {
        return true;
    }

    @Override
    public boolean isInputFacing(byte aSide) {
        return aSide != this.getBaseMetaTileEntity().getFrontFacing();
    }

    @Override
    public boolean isOutputFacing(byte aSide) {
        return aSide == this.getBaseMetaTileEntity().getFrontFacing();
    }

    @Override
    public void onFirstTick(IGregTechTileEntity aBaseMetaTileEntity) {
        checkMachine(aBaseMetaTileEntity, null);
        super.onFirstTick(aBaseMetaTileEntity);
    }

    @Override
    public void onPostTick(IGregTechTileEntity aBaseMetaTileEntity, long aTick) {
        if (aBaseMetaTileEntity.isServerSide()) {
            this.mMaxProgresstime = 1;
            if (aTick % 20 == 0)
                checkMachine(aBaseMetaTileEntity, null);
            this.mWrench = true;
            this.mScrewdriver = true;
            this.mSoftHammer = true;
            this.mHardHammer = true;
            this.mSolderingTool = true;
            this.mCrowbar = true;
        }
    }

    @Override
    public long getMinimumStoredEU() {
        return 0;
    }

    @Override
    public boolean onRunningTick(ItemStack aStack) {
        this.mMaxProgresstime = 1;
        return true;
    }

    @Override
    public void saveNBTData(NBTTagCompound aNBT) {
        aNBT.setIntArray("customCircuitInv", GT_Utility.stacksToIntArray(this.circuits));
        super.saveNBTData(aNBT);
    }

    @Override
    public void loadNBTData(NBTTagCompound aNBT) {
        int[] stacks = aNBT.getIntArray("customCircuitInv");
        for (int i = 0; i < stacks.length; i++) {
            this.circuits[i] = GT_Utility.intToStack(stacks[i]);
        }
        super.loadNBTData(aNBT);
    }

    @Override
    public boolean checkMachine(IGregTechTileEntity aBaseMetaTileEntity, ItemStack itemStack) {
        long startingTime = System.nanoTime();
        connectedcells = new ConnectedBlocksChecker();
        connectedcells.get_connected(aBaseMetaTileEntity.getWorld(), aBaseMetaTileEntity.getXCoord(), aBaseMetaTileEntity.getYCoord(), aBaseMetaTileEntity.getZCoord(), ItemRegistry.BW_BLOCKS[1]);

        if (connectedcells.get_meta_of_sideblocks(aBaseMetaTileEntity.getWorld(), this.getBaseMetaTileEntity().getMetaTileID(), new int[]{aBaseMetaTileEntity.getXCoord(), aBaseMetaTileEntity.getYCoord(), aBaseMetaTileEntity.getZCoord()}, true)) {
            this.getBaseMetaTileEntity().disableWorking();
            this.getBaseMetaTileEntity().setActive(false);
            this.mStorage = 0;
            this.mMaxProgresstime = 0;
            this.mProgresstime = 0;
            return false;
        }

        this.mEfficiency = this.getMaxEfficiency(null);
        this.mStorage = (ConfigHandler.energyPerCell * connectedcells.hashset.size() >= Long.MAX_VALUE - 1 || ConfigHandler.energyPerCell * connectedcells.hashset.size() < 0) ? Long.MAX_VALUE - 1 : ConfigHandler.energyPerCell * connectedcells.hashset.size();
        this.mMaxProgresstime = 1;
        this.mProgresstime = 0;

        this.mCrowbar = true;
        this.mHardHammer = true;
        this.mScrewdriver = true;
        this.mSoftHammer = true;
        this.mSolderingTool = true;
        this.mWrench = true;

        this.getBaseMetaTileEntity().enableWorking();
        this.getBaseMetaTileEntity().setActive(true);

        long finishedTime = System.nanoTime();
        if (finishedTime - startingTime > 5000000)
            MainMod.LOGGER.warn("LESU LookUp took longer than 5ms!(" + (finishedTime - startingTime) + "ns / " + ((finishedTime - startingTime) / 1000000) + "ms) Check at x:" + this.getBaseMetaTileEntity().getXCoord() + " y:" + this.getBaseMetaTileEntity().getYCoord() + " z:" + this.getBaseMetaTileEntity().getZCoord() + " DIM-ID: " + this.getBaseMetaTileEntity().getWorld().provider.dimensionId);
        return true;
    }

    @Override
    public int getMaxEfficiency(ItemStack itemStack) {
        return 10000;
    }

    @Override
    public int getPollutionPerTick(ItemStack itemStack) {
        return 0;
    }

    @Override
    public int getDamageToComponent(ItemStack itemStack) {
        return 0;
    }

    @Override
    public boolean explodesOnComponentBreak(ItemStack itemStack) {
        return false;
    }

    public World getWorld() {
        return this.getBaseMetaTileEntity().getWorld();
    }

}
