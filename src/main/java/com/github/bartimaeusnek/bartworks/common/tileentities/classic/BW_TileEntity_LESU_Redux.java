/// *
// * Copyright (c) 2019 bartimaeusnek
// *
// * Permission is hereby granted, free of charge, to any person obtaining a copy
// * of this software and associated documentation files (the "Software"), to deal
// * in the Software without restriction, including without limitation the rights
// * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
// * copies of the Software, and to permit persons to whom the Software is
// * furnished to do so, subject to the following conditions:
// *
// * The above copyright notice and this permission notice shall be included in all
// * copies or substantial portions of the Software.
// *
// * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
// * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
// * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
// * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
// * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
// * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
// * SOFTWARE.
// */
//
// package com.github.bartimaeusnek.bartworks.common.tileentities.classic;
//
// import com.github.bartimaeusnek.bartworks.API.ITileAddsInformation;
// import com.github.bartimaeusnek.bartworks.API.ITileDropsContent;
// import com.github.bartimaeusnek.bartworks.API.ITileHasDifferentTextureSides;
// import com.github.bartimaeusnek.bartworks.MainMod;
// import com.github.bartimaeusnek.bartworks.common.configs.ConfigHandler;
// import com.github.bartimaeusnek.bartworks.util.ChatColorHelper;
// import com.github.bartimaeusnek.bartworks.util.ConnectedBlocksChecker;
// import cpw.mods.fml.common.FMLCommonHandler;
// import cpw.mods.fml.relauncher.Side;
// import gregtech.api.interfaces.tileentity.IEnergyConnected;
// import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
// import gregtech.api.objects.XSTR;
// import net.minecraft.block.Block;
// import net.minecraft.client.renderer.texture.IIconRegister;
// import net.minecraft.entity.player.EntityPlayer;
// import net.minecraft.inventory.IInventory;
// import net.minecraft.item.ItemStack;
// import net.minecraft.tileentity.TileEntity;
// import net.minecraft.util.StatCollector;
// import net.minecraft.world.World;
// import net.minecraft.world.biome.BiomeGenBase;
// import net.minecraftforge.fluids.IFluidHandler;
//
// import java.nio.ByteBuffer;
// import java.util.ArrayList;
//
//
// public class BW_TileEntity_LESU_Redux extends TileEntity implements ITileHasDifferentTextureSides,
// ITileAddsInformation, ITileDropsContent, IEnergyConnected {
//
//    public ConnectedBlocksChecker connectedcells;
//    public ItemStack[] circuits = new ItemStack[5];
//    private long[] storage;
//    private long input;
//    private long output;
//    ByteBuffer eu;
//
//    @Override
//    public String[] getInfoData() {
//        ArrayList<String> e = new ArrayList<>();
//        String[] dsc = StatCollector.translateToLocal("tooltip.tile.lesu.0.name").split(";");
//        for (int i = 0; i < dsc.length; i++) {
//            e.add(dsc[i]);
//        }
//        e.add(StatCollector.translateToLocal("tooltip.tile.lesu.1.name") + " " + ConfigHandler.energyPerCell + "EU");
//        dsc = StatCollector.translateToLocal("tooltip.tile.lesu.2.name").split(";");
//        for (int i = 0; i < dsc.length; i++) {
//            e.add(dsc[i]);
//        }
//        e.add(ChatColorHelper.RED + StatCollector.translateToLocal("tooltip.tile.lesu.3.name"));
//        e.add(StatCollector.translateToLocal("tooltip.bw.1.name") + ChatColorHelper.DARKGREEN + " BartWorks");
//        return e.toArray(new String[0]);
//    }
//
//    @Override
//    public void registerBlockIcons(IIconRegister par1IconRegister) {
//        par1IconRegister.registerIcon(MainMod.MOD_ID + ":LESU_CASING_" + i);
//    }
//
//    @Override
//    public long injectEnergyUnits(byte aSide, long aVoltage, long aAmperage)  {
//        if (inputEnergyFrom(aSide)){
//
//        }
//        return 0;
//    }
//
//    @Override
//    public boolean inputEnergyFrom(byte b) {
//        return true;
//    }
//
//    @Override
//    public boolean outputsEnergyTo(byte b) {
//        return false;
//    }
//
//    @Override
//    public byte getColorization() {
//        return 0;
//    }
//
//    @Override
//    public byte setColorization(byte b) {
//        return 0;
//    }
//
//    @Override
//    public World getWorld() {
//        return null;
//    }
//
//    @Override
//    public int getXCoord() {
//        return 0;
//    }
//
//    @Override
//    public short getYCoord() {
//        return 0;
//    }
//
//    @Override
//    public int getZCoord() {
//        return 0;
//    }
//
//    @Override
//    public boolean isServerSide() {
//        return !isClientSide();
//    }
//
//    @Override
//    public boolean isClientSide() {
//        if (getWorld() != null)
//            return getWorld().isRemote;
//        return FMLCommonHandler.instance().getEffectiveSide() == Side.CLIENT;
//    }
//
//    @Override
//    public int getRandomNumber(int i) {
//        return new XSTR().nextInt(i);
//    }
//
//    @Override
//    public TileEntity getTileEntity(int i, int i1, int i2) {
//        return null;
//    }
//
//    @Override
//    public TileEntity getTileEntityOffset(int i, int i1, int i2) {
//        return null;
//    }
//
//    @Override
//    public TileEntity getTileEntityAtSide(byte b) {
//        return null;
//    }
//
//    @Override
//    public TileEntity getTileEntityAtSideAndDistance(byte b, int i) {
//        return null;
//    }
//
//    @Override
//    public IInventory getIInventory(int i, int i1, int i2) {
//        return null;
//    }
//
//    @Override
//    public IInventory getIInventoryOffset(int i, int i1, int i2) {
//        return null;
//    }
//
//    @Override
//    public IInventory getIInventoryAtSide(byte b) {
//        return null;
//    }
//
//    @Override
//    public IInventory getIInventoryAtSideAndDistance(byte b, int i) {
//        return null;
//    }
//
//    @Override
//    public IFluidHandler getITankContainer(int i, int i1, int i2) {
//        return null;
//    }
//
//    @Override
//    public IFluidHandler getITankContainerOffset(int i, int i1, int i2) {
//        return null;
//    }
//
//    @Override
//    public IFluidHandler getITankContainerAtSide(byte b) {
//        return null;
//    }
//
//    @Override
//    public IFluidHandler getITankContainerAtSideAndDistance(byte b, int i) {
//        return null;
//    }
//
//    @Override
//    public IGregTechTileEntity getIGregTechTileEntity(int i, int i1, int i2) {
//        return null;
//    }
//
//    @Override
//    public IGregTechTileEntity getIGregTechTileEntityOffset(int i, int i1, int i2) {
//        return null;
//    }
//
//    @Override
//    public IGregTechTileEntity getIGregTechTileEntityAtSide(byte b) {
//        return null;
//    }
//
//    @Override
//    public IGregTechTileEntity getIGregTechTileEntityAtSideAndDistance(byte b, int i) {
//        return null;
//    }
//
//    @Override
//    public Block getBlock(int i, int i1, int i2) {
//        return null;
//    }
//
//    @Override
//    public Block getBlockOffset(int i, int i1, int i2) {
//        return null;
//    }
//
//    @Override
//    public Block getBlockAtSide(byte b) {
//        return null;
//    }
//
//    @Override
//    public Block getBlockAtSideAndDistance(byte b, int i) {
//        return null;
//    }
//
//    @Override
//    public byte getMetaID(int i, int i1, int i2) {
//        return 0;
//    }
//
//    @Override
//    public byte getMetaIDOffset(int i, int i1, int i2) {
//        return 0;
//    }
//
//    @Override
//    public byte getMetaIDAtSide(byte b) {
//        return 0;
//    }
//
//    @Override
//    public byte getMetaIDAtSideAndDistance(byte b, int i) {
//        return 0;
//    }
//
//    @Override
//    public byte getLightLevel(int i, int i1, int i2) {
//        return 0;
//    }
//
//    @Override
//    public byte getLightLevelOffset(int i, int i1, int i2) {
//        return 0;
//    }
//
//    @Override
//    public byte getLightLevelAtSide(byte b) {
//        return 0;
//    }
//
//    @Override
//    public byte getLightLevelAtSideAndDistance(byte b, int i) {
//        return 0;
//    }
//
//    @Override
//    public boolean getOpacity(int i, int i1, int i2) {
//        return false;
//    }
//
//    @Override
//    public boolean getOpacityOffset(int i, int i1, int i2) {
//        return false;
//    }
//
//    @Override
//    public boolean getOpacityAtSide(byte b) {
//        return false;
//    }
//
//    @Override
//    public boolean getOpacityAtSideAndDistance(byte b, int i) {
//        return false;
//    }
//
//    @Override
//    public boolean getSky(int i, int i1, int i2) {
//        return false;
//    }
//
//    @Override
//    public boolean getSkyOffset(int i, int i1, int i2) {
//        return false;
//    }
//
//    @Override
//    public boolean getSkyAtSide(byte b) {
//        return false;
//    }
//
//    @Override
//    public boolean getSkyAtSideAndDistance(byte b, int i) {
//        return false;
//    }
//
//    @Override
//    public boolean getAir(int i, int i1, int i2) {
//        return false;
//    }
//
//    @Override
//    public boolean getAirOffset(int i, int i1, int i2) {
//        return false;
//    }
//
//    @Override
//    public boolean getAirAtSide(byte b) {
//        return false;
//    }
//
//    @Override
//    public boolean getAirAtSideAndDistance(byte b, int i) {
//        return false;
//    }
//
//    @Override
//    public BiomeGenBase getBiome() {
//        return null;
//    }
//
//    @Override
//    public BiomeGenBase getBiome(int i, int i1) {
//        return null;
//    }
//
//    @Override
//    public int getOffsetX(byte b, int i) {
//        return 0;
//    }
//
//    @Override
//    public short getOffsetY(byte b, int i) {
//        return 0;
//    }
//
//    @Override
//    public int getOffsetZ(byte b, int i) {
//        return 0;
//    }
//
//    @Override
//    public boolean isDead() {
//        return false;
//    }
//
//    @Override
//    public void sendBlockEvent(byte b, byte b1) {
//
//    }
//
//    @Override
//    public long getTimer() {
//        return 0;
//    }
//
//    @Override
//    public void setLightValue(byte b) {
//
//    }
//
//    @Override
//    public boolean isInvalidTileEntity() {
//        return false;
//    }
//
//    @Override
//    public boolean openGUI(EntityPlayer entityPlayer, int i) {
//        return false;
//    }
//
//    @Override
//    public boolean openGUI(EntityPlayer entityPlayer) {
//        return false;
//    }
//
//    @Override
//    public int[] getAccessibleSlotsFromSide(int p_94128_1_) {
//        return new int[0];
//    }
//
//    @Override
//    public boolean canInsertItem(int p_102007_1_, ItemStack p_102007_2_, int p_102007_3_) {
//        return false;
//    }
//
//    @Override
//    public boolean canExtractItem(int p_102008_1_, ItemStack p_102008_2_, int p_102008_3_) {
//        return false;
//    }
//
//    @Override
//    public int getSizeInventory() {
//        return 0;
//    }
//
//    @Override
//    public ItemStack getStackInSlot(int p_70301_1_) {
//        return null;
//    }
//
//    @Override
//    public ItemStack decrStackSize(int p_70298_1_, int p_70298_2_) {
//        return null;
//    }
//
//    @Override
//    public ItemStack getStackInSlotOnClosing(int p_70304_1_) {
//        return null;
//    }
//
//    @Override
//    public void setInventorySlotContents(int p_70299_1_, ItemStack p_70299_2_) {
//
//    }
//
//    @Override
//    public String getInventoryName() {
//        return null;
//    }
//
//    @Override
//    public boolean hasCustomInventoryName() {
//        return false;
//    }
//
//    @Override
//    public int getInventoryStackLimit() {
//        return 0;
//    }
//
//    @Override
//    public boolean isUseableByPlayer(EntityPlayer p_70300_1_) {
//        return false;
//    }
//
//    @Override
//    public void openInventory() {
//
//    }
//
//    @Override
//    public void closeInventory() {
//
//    }
//
//    @Override
//    public boolean isItemValidForSlot(int p_94041_1_, ItemStack p_94041_2_) {
//        return false;
//    }
//
//    @Override
//    public int[] getDropSlots() {
//        return new int[0];
//    }
// }
