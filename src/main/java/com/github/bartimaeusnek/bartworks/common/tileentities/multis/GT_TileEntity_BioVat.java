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

import com.github.bartimaeusnek.bartworks.API.BioVatLogicAdder;
import com.github.bartimaeusnek.bartworks.MainMod;
import com.github.bartimaeusnek.bartworks.common.items.LabParts;
import com.github.bartimaeusnek.bartworks.common.loaders.FluidLoader;
import com.github.bartimaeusnek.bartworks.common.loaders.ItemRegistry;
import com.github.bartimaeusnek.bartworks.common.net.RendererPacket;
import com.github.bartimaeusnek.bartworks.common.tileentities.tiered.GT_MetaTileEntity_RadioHatch;
import com.github.bartimaeusnek.bartworks.util.*;
import cpw.mods.fml.common.FMLCommonHandler;
import gregtech.api.GregTech_API;
import gregtech.api.enums.Textures;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_Hatch_Input;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_Hatch_Output;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_MultiBlockBase;
import gregtech.api.objects.GT_RenderedTexture;
import gregtech.api.util.GT_Recipe;
import gregtech.api.util.GT_Utility;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;

public class GT_TileEntity_BioVat extends GT_MetaTileEntity_MultiBlockBase {

    public static final HashMap<Coords, Integer> staticColorMap = new HashMap<>();

    private static final byte MCASING_INDEX = 49;
    private static final byte TIMERDIVIDER = 20;

    private final HashSet<EntityPlayerMP> playerMPHashSet = new HashSet<EntityPlayerMP>();
    private final ArrayList<GT_MetaTileEntity_RadioHatch> mRadHatches = new ArrayList<>();
    private int height = 1;
    private GT_Recipe mLastRecipe;
    private Fluid mFluid = FluidRegistry.LAVA;
    private BioCulture mCulture;
    private ItemStack mStack;
    private boolean needsVisualUpdate = true;
    private byte mGlassTier;
    private int mSievert;
    private int mNeededSievert;

    public GT_TileEntity_BioVat(int aID, String aName, String aNameRegional) {
        super(aID, aName, aNameRegional);
    }

    public GT_TileEntity_BioVat(String aName) {
        super(aName);
    }

    public static int[] specialValueUnpack(int aSpecialValue) {
        int[] ret = new int[4];
        ret[0] = aSpecialValue & 0xF; // = glas tier
        ret[1] = aSpecialValue >>> 4 & 0b11; // = special valure
        ret[2] = aSpecialValue >>> 6 & 0b1; //boolean exact svt | 1 = true | 0 = false
        ret[3] = aSpecialValue >>> 7 & Integer.MAX_VALUE; // = sievert
        return ret;
    }

    public boolean isLiquidInput(byte aSide) {
        return false;
    }

    public boolean isLiquidOutput(byte aSide) {
        return false;
    }

    private int getInputCapacity() {
        int ret = 0;

        for (GT_MetaTileEntity_Hatch_Input fluidH : this.mInputHatches) {
            ret += fluidH.getCapacity();
        }
        return ret;
    }

    private int getOutputCapacity() {
        int ret = 0;

        for (GT_MetaTileEntity_Hatch_Output fluidH : this.mOutputHatches) {
            ret += fluidH.getCapacity();
        }
        return ret;
    }

    @Override
    public int getCapacity() {
        int ret = 0;
        ret += this.getInputCapacity();
        //ret += getOutputCapacity();
        return ret;
    }

    @Override
    public int fill(FluidStack resource, boolean doFill) {
        return super.fill(resource, doFill);
    }

    @Override
    public boolean isCorrectMachinePart(ItemStack itemStack) {
        return true;
    }

    @Override
    public GT_Recipe.GT_Recipe_Map getRecipeMap() {
        return BWRecipes.instance.getMappingsFor(BWRecipes.BACTERIALVATBYTE);
    }

    private int calcMod(double x) {
        int ret = (int) Math.ceil(100D*(-(((2*x/this.getOutputCapacity())-1)*(2*x/this.getOutputCapacity()-1D))+1D));
        return ret <= 0 ? 1 : ret > 100 ? 100 : ret;//(int) Math.ceil((-0.00000025D * x * (x - this.getOutputCapacity())));
    }

    @Override
    public boolean checkRecipe(ItemStack itemStack) {
        GT_Recipe.GT_Recipe_Map gtRecipeMap = this.getRecipeMap();

        if (gtRecipeMap == null)
            return false;

        ArrayList<ItemStack> tInputList = this.getStoredInputs();
        int tInputList_sS = tInputList.size();
        for (int i = 0; i < tInputList_sS - 1; i++) {
            for (int j = i + 1; j < tInputList_sS; j++) {
                if (GT_Utility.areStacksEqual(tInputList.get(i), tInputList.get(j))) {
                    if (tInputList.get(i).stackSize >= tInputList.get(j).stackSize) {
                        tInputList.remove(j--);
                        tInputList_sS = tInputList.size();
                    } else {
                        tInputList.remove(i--);
                        tInputList_sS = tInputList.size();
                        break;
                    }
                }
            }
        }
        ItemStack[] tInputs = tInputList.toArray(new ItemStack[tInputList.size()]);

        ArrayList<FluidStack> tFluidList = this.getStoredFluids();
        int tFluidList_sS = tFluidList.size();
        for (int i = 0; i < tFluidList_sS - 1; i++) {
            for (int j = i + 1; j < tFluidList_sS; j++) {
                if (GT_Utility.areFluidsEqual(tFluidList.get(i), tFluidList.get(j))) {
                    if (tFluidList.get(i).amount >= tFluidList.get(j).amount) {
                        tFluidList.remove(j--);
                        tFluidList_sS = tFluidList.size();
                    } else {
                        tFluidList.remove(i--);
                        tFluidList_sS = tFluidList.size();
                        break;
                    }
                }
            }
        }

        FluidStack[] tFluids = tFluidList.toArray(new FluidStack[tFluidList.size()]);

        if (tFluidList.size() > 0) {

            GT_Recipe gtRecipe = gtRecipeMap.findRecipe(this.getBaseMetaTileEntity(), this.mLastRecipe, false, this.getMaxInputVoltage(), tFluids, itemStack, tInputs);

            if (gtRecipe == null)
                return false;

            if (!BW_Util.areStacksEqualOrNull((ItemStack) gtRecipe.mSpecialItems, itemStack))
                return false;

            int[] conditions = GT_TileEntity_BioVat.specialValueUnpack(gtRecipe.mSpecialValue);

            this.mNeededSievert = conditions[3];

            if (conditions[2] == 0 ? (this.mSievert < this.mNeededSievert || this.mGlassTier < conditions[0]) : (this.mSievert != conditions[3] || this.mGlassTier < conditions[0]))
                return false;

            int times = 1;


            this.mEfficiency = (10000 - (this.getIdealStatus() - this.getRepairStatus()) * 1000);
            this.mEfficiencyIncrease = 10000;

            if (gtRecipe.isRecipeInputEqual(true, tFluids, tInputs)) {
                if (this.getStoredFluidOutputs().size() > 0) {
                    this.mOutputFluids = new FluidStack[gtRecipe.mFluidOutputs.length];
                    for (FluidStack storedOutputFluid : this.getStoredFluidOutputs()) {
                        if (storedOutputFluid.isFluidEqual(gtRecipe.getFluidOutput(0)))
                            for (FluidStack inputFluidStack : gtRecipe.mFluidInputs) {
                                int j = this.calcMod(storedOutputFluid.amount);
                                for (int i = 0; i < j; i++)
                                    if (this.depleteInput(inputFluidStack))
                                        times++;
                            }
                    }
                    for (FluidStack storedfluidStack : this.getStoredFluidOutputs()) {
                        for (int i = 0; i < gtRecipe.mFluidOutputs.length; i++) {
                            if (storedfluidStack.isFluidEqual(gtRecipe.getFluidOutput(i)))
                                this.mOutputFluids[i] = (new FluidStack(gtRecipe.getFluidOutput(i), times * gtRecipe.getFluidOutput(0).amount));
                        }

                    }
                } else {
                    this.mOutputFluids = gtRecipe.mFluidOutputs;
                }
            } else
                return false;

            BW_Util.calculateOverclockedNessMulti(gtRecipe.mEUt, gtRecipe.mDuration, 1, this.getMaxInputVoltage(), this);

            if (this.mEUt > 0)
                this.mEUt = -this.mEUt;
            this.mProgresstime = 0;

            if (gtRecipe.mCanBeBuffered)
                this.mLastRecipe = gtRecipe;

            this.updateSlots();
            return true;
        }
        return false;
    }

    public ArrayList<FluidStack> getStoredFluidOutputs() {
        ArrayList<FluidStack> rList = new ArrayList();
        Iterator var2 = this.mOutputHatches.iterator();

        while (var2.hasNext()) {
            GT_MetaTileEntity_Hatch_Output tHatch = (GT_MetaTileEntity_Hatch_Output) var2.next();
            if (tHatch.getFluid() != null)
                rList.add(tHatch.getFluid());
        }
        return rList;
    }

    private boolean addRadiationInputToMachineList(IGregTechTileEntity aTileEntity, int aBaseCasingIndex) {
        if (aTileEntity == null) {
            return false;
        } else {
            IMetaTileEntity aMetaTileEntity = aTileEntity.getMetaTileEntity();
            if (aMetaTileEntity == null) {
                return false;
            } else if (aMetaTileEntity instanceof GT_MetaTileEntity_RadioHatch) {
                ((GT_MetaTileEntity_RadioHatch) aMetaTileEntity).updateTexture(aBaseCasingIndex);
                return this.mRadHatches.add((GT_MetaTileEntity_RadioHatch) aMetaTileEntity);
            } else {
                return false;
            }
        }
    }

    @Override
    public boolean checkMachine(IGregTechTileEntity aBaseMetaTileEntity, ItemStack itemStack) {
        int xDir = ForgeDirection.getOrientation(aBaseMetaTileEntity.getBackFacing()).offsetX * 2;
        int zDir = ForgeDirection.getOrientation(aBaseMetaTileEntity.getBackFacing()).offsetZ * 2;
        int blockcounter = 0;
        this.mRadHatches.clear();

        for (int x = -2; x <= 2; x++)
            for (int z = -2; z <= 2; z++)
                for (int y = 0; y < 4; y++) {
                    IGregTechTileEntity tileEntity = aBaseMetaTileEntity.getIGregTechTileEntityOffset(xDir + x, y, zDir + z);
                    if (y == 0 || y == 3) {
                        //controller
                        if (y == 0 && xDir + x == 0 && zDir + z == 0)
                            continue;
                        if (!(this.addOutputToMachineList(tileEntity, GT_TileEntity_BioVat.MCASING_INDEX) || this.addRadiationInputToMachineList(tileEntity, GT_TileEntity_BioVat.MCASING_INDEX) || this.addInputToMachineList(tileEntity, GT_TileEntity_BioVat.MCASING_INDEX) || this.addMaintenanceToMachineList(tileEntity, GT_TileEntity_BioVat.MCASING_INDEX) || this.addEnergyInputToMachineList(tileEntity, GT_TileEntity_BioVat.MCASING_INDEX))) {
                            if (BW_Util.addBlockToMachine(x, y, z, 2, aBaseMetaTileEntity, GregTech_API.sBlockCasings4, 1)) {
                                ++blockcounter;
                                continue;
                            }
                            return false;
                        }
                    } else {
                        if (Math.abs(x) < 2 && Math.abs(z) != 2) {
                            if (!(BW_Util.addBlockToMachine(x, y, z, 2, aBaseMetaTileEntity, Blocks.air) || (BW_Util.addBlockToMachine(x, y, z, 2, aBaseMetaTileEntity, FluidLoader.bioFluidBlock)))) {
                                return false;
                            }
                        } else {
                            if (x == -2 && z == -2 && y == 1)
                                this.mGlassTier = this.calculateGlassTier(aBaseMetaTileEntity.getBlockOffset(xDir + -2, y, zDir + z), aBaseMetaTileEntity.getMetaIDOffset(xDir + x, y, zDir + z));
                            if (0 == this.mGlassTier || this.mGlassTier != this.calculateGlassTier(aBaseMetaTileEntity.getBlockOffset(xDir + x, y, zDir + z), aBaseMetaTileEntity.getMetaIDOffset(xDir + x, y, zDir + z))) {
                                return false;
                            }
                        }
                    }
                }

        if (blockcounter > 18)
            if (this.mRadHatches.size() < 2)
                if (this.mOutputHatches.size() == 1)
                    if (this.mMaintenanceHatches.size() == 1)
                        if (this.mInputHatches.size() > 0)
                            return this.mEnergyHatches.size() > 0;

        return false;
    }

    private byte calculateGlassTier(@Nonnull Block block, @Nonnegative Byte meta) {

        if (block.equals(ItemRegistry.bw_glasses[0]))
            return meta == 12 ? 5 : meta > 1 && meta < 6 ? (byte) (meta + 3) : 4;

        if (block.getUnlocalizedName().equals("blockAlloyGlass"))
            return 4;

        if (block.equals(Blocks.glass))
            return 3;

        for (BioVatLogicAdder.BlockMetaPair B : BioVatLogicAdder.BioVatGlass.getGlassMap().keySet())
            if (B.getBlock().equals(block) && B.getaByte().equals(meta))
                return BioVatLogicAdder.BioVatGlass.getGlassMap().get(B);

        if (block.getMaterial().equals(Material.glass))
            return 3;

        return 0;
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

    private void sendAllRequiredRendererPackets() {
        int height = this.reCalculateHeight();
        if (this.mFluid != null && height > 1 && this.reCalculateFluidAmmount() > 0) {
            for (int x = -1; x < 2; x++)
                for (int y = 1; y < height; y++)
                    for (int z = -1; z < 2; z++)
                        this.sendPackagesOrRenewRenderer(x, y, z, this.mCulture);
        }
    }

    private void sendPackagesOrRenewRenderer(int x, int y, int z, BioCulture lCulture) {
        int xDir = ForgeDirection.getOrientation(this.getBaseMetaTileEntity().getBackFacing()).offsetX * 2;
        int zDir = ForgeDirection.getOrientation(this.getBaseMetaTileEntity().getBackFacing()).offsetZ * 2;

        GT_TileEntity_BioVat.staticColorMap.remove(new Coords(xDir + x + this.getBaseMetaTileEntity().getXCoord(), y + this.getBaseMetaTileEntity().getYCoord(), zDir + z + this.getBaseMetaTileEntity().getZCoord(), this.getBaseMetaTileEntity().getWorld().provider.dimensionId));
        GT_TileEntity_BioVat.staticColorMap.put(new Coords(xDir + x + this.getBaseMetaTileEntity().getXCoord(), y + this.getBaseMetaTileEntity().getYCoord(), zDir + z + this.getBaseMetaTileEntity().getZCoord(), this.getBaseMetaTileEntity().getWorld().provider.dimensionId), lCulture == null ? BioCulture.NULLCULTURE.getColorRGB() : lCulture.getColorRGB());

        if (FMLCommonHandler.instance().getSide().isServer()) {
            MainMod.BW_Network_instance.sendPacketToAllPlayersInRange(
                    this.getBaseMetaTileEntity().getWorld(),
                    new RendererPacket(
                            new Coords(
                                    xDir + x + this.getBaseMetaTileEntity().getXCoord(),
                                    y + this.getBaseMetaTileEntity().getYCoord(),
                                    zDir + z + this.getBaseMetaTileEntity().getZCoord(),
                                    this.getBaseMetaTileEntity().getWorld().provider.dimensionId
                            ),
                            lCulture == null ? BioCulture.NULLCULTURE.getColorRGB() : lCulture.getColorRGB(),
                            true
                    ),
                    this.getBaseMetaTileEntity().getXCoord(),
                    this.getBaseMetaTileEntity().getZCoord()
            );
            MainMod.BW_Network_instance.sendPacketToAllPlayersInRange(
                    this.getBaseMetaTileEntity().getWorld(),
                    new RendererPacket(
                            new Coords(
                                    xDir + x + this.getBaseMetaTileEntity().getXCoord(),
                                    y + this.getBaseMetaTileEntity().getYCoord(),
                                    zDir + z + this.getBaseMetaTileEntity().getZCoord(),
                                    this.getBaseMetaTileEntity().getWorld().provider.dimensionId
                            ),
                            lCulture == null ? BioCulture.NULLCULTURE.getColorRGB() : lCulture.getColorRGB(),
                            false
                    ),
                    this.getBaseMetaTileEntity().getXCoord(),
                    this.getBaseMetaTileEntity().getZCoord()
            );
        }
        this.needsVisualUpdate = true;
    }

    private void check_Chunk() {
        World aWorld = this.getBaseMetaTileEntity().getWorld();
        if (!aWorld.isRemote) {
            Iterator var5 = aWorld.playerEntities.iterator();

            while (var5.hasNext()) {
                Object tObject = var5.next();
                if (!(tObject instanceof EntityPlayerMP)) {
                    break;
                }
                EntityPlayerMP tPlayer = (EntityPlayerMP) tObject;
                Chunk tChunk = aWorld.getChunkFromBlockCoords(this.getBaseMetaTileEntity().getXCoord(), this.getBaseMetaTileEntity().getZCoord());
                if (tPlayer.getServerForPlayer().getPlayerManager().isPlayerWatchingChunk(tPlayer, tChunk.xPosition, tChunk.zPosition)) {
                    if (!this.playerMPHashSet.contains(tPlayer)) {
                        this.playerMPHashSet.add(tPlayer);
                        this.sendAllRequiredRendererPackets();
                    }
                } else {
                    this.playerMPHashSet.remove(tPlayer);
                }

            }
        }
    }

    private void placeFluid() {
        int xDir = ForgeDirection.getOrientation(this.getBaseMetaTileEntity().getBackFacing()).offsetX * 2;
        int zDir = ForgeDirection.getOrientation(this.getBaseMetaTileEntity().getBackFacing()).offsetZ * 2;
        this.height = this.reCalculateHeight();
        if (this.mFluid != null && this.height > 1 && this.reCalculateFluidAmmount() > 0)
            for (int x = -1; x < 2; x++) {
                for (int y = 0; y < this.height; y++) {
                    for (int z = -1; z < 2; z++) {
                        if (this.getBaseMetaTileEntity().getWorld().getBlock(xDir + x + this.getBaseMetaTileEntity().getXCoord(), y + this.getBaseMetaTileEntity().getYCoord(), zDir + z + this.getBaseMetaTileEntity().getZCoord()).equals(Blocks.air))
                            this.getBaseMetaTileEntity().getWorld().setBlock(xDir + x + this.getBaseMetaTileEntity().getXCoord(), y + this.getBaseMetaTileEntity().getYCoord(), zDir + z + this.getBaseMetaTileEntity().getZCoord(), FluidLoader.bioFluidBlock);
                    }
                }
            }
    }

    private int reCalculateFluidAmmount() {
        int lFluidAmount = 0;
        for (int i = 0; i < this.getStoredFluids().size(); i++) {
            lFluidAmount += this.getStoredFluids().get(i).amount;
        }
        return lFluidAmount;
    }

    private int reCalculateHeight() {
        return (this.reCalculateFluidAmmount() > ((this.getCapacity() / 4) - 1) ? (this.reCalculateFluidAmmount() >= this.getCapacity() / 2 ? 3 : 2) : 1);
    }

    public void doAllVisualThings() {
        if (this.getBaseMetaTileEntity().isServerSide()) {
            if (this.mMachine) {
                ItemStack aStack = this.mInventory[1];
                BioCulture lCulture = null;
                int xDir = ForgeDirection.getOrientation(this.getBaseMetaTileEntity().getBackFacing()).offsetX * 2;
                int zDir = ForgeDirection.getOrientation(this.getBaseMetaTileEntity().getBackFacing()).offsetZ * 2;

                if (this.getBaseMetaTileEntity().getTimer() % 200 == 0) {
                    this.check_Chunk();
                }

                if (this.needsVisualUpdate && this.getBaseMetaTileEntity().getTimer() % GT_TileEntity_BioVat.TIMERDIVIDER == 0) {
                    for (int x = -1; x < 2; x++)
                        for (int y = 1; y < 3; y++)
                            for (int z = -1; z < 2; z++)
                                this.getBaseMetaTileEntity().getWorld().setBlockToAir(xDir + x + this.getBaseMetaTileEntity().getXCoord(), y + this.getBaseMetaTileEntity().getYCoord(), zDir + z + this.getBaseMetaTileEntity().getZCoord());
                }

                this.height = this.reCalculateHeight();
                if (this.mFluid != null && this.height > 1 && this.reCalculateFluidAmmount() > 0) {
                    if ((!(BW_Util.areStacksEqualOrNull(aStack, this.mStack))) || (this.needsVisualUpdate && this.getBaseMetaTileEntity().getTimer() % GT_TileEntity_BioVat.TIMERDIVIDER == 1)) {
                        for (int x = -1; x < 2; x++) {
                            for (int y = 1; y < this.height; y++) {
                                for (int z = -1; z < 2; z++) {
                                    if (aStack == null || (aStack != null && aStack.getItem() instanceof LabParts && aStack.getItemDamage() == 0)) {
                                        if (this.mCulture == null || aStack == null || aStack.getTagCompound() == null || this.mCulture.getID() != aStack.getTagCompound().getInteger("ID")) {
                                            lCulture = aStack == null || aStack.getTagCompound() == null ? null : BioCulture.getBioCulture(aStack.getTagCompound().getString("Name"));
                                            this.sendPackagesOrRenewRenderer(x, y, z, lCulture);
                                        }
                                    }
                                }
                            }
                        }
                        this.mStack = aStack;
                        this.mCulture = lCulture;
                    }
                    if (this.needsVisualUpdate && this.getBaseMetaTileEntity().getTimer() % GT_TileEntity_BioVat.TIMERDIVIDER == 1) {
                        if (this.getBaseMetaTileEntity().isClientSide())
                            new Throwable().printStackTrace();
                        this.placeFluid();
                        this.needsVisualUpdate = false;
                    }
                }
            } else {
                this.onRemoval();
            }
        }
    }

    @Override
    public void onPostTick(IGregTechTileEntity aBaseMetaTileEntity, long aTick) {
        super.onPostTick(aBaseMetaTileEntity, aTick);
        if (this.height != this.reCalculateHeight())
            this.needsVisualUpdate = true;
        this.doAllVisualThings();
        if (this.getBaseMetaTileEntity().isServerSide() && this.mRadHatches.size() == 1) {
            this.mSievert = this.mRadHatches.get(0).getSievert();
            if (this.getBaseMetaTileEntity().isActive() && this.mNeededSievert > this.mSievert)
                this.mOutputFluids = null;
        }
    }

    @Override
    public void saveNBTData(NBTTagCompound aNBT) {
        aNBT.setInteger("mFluidHeight", this.height);
        if (this.mCulture != null && !this.mCulture.getName().isEmpty())
            aNBT.setString("mCulture", this.mCulture.getName());
        else if ((this.mCulture == null || this.mCulture.getName().isEmpty()) && !aNBT.getString("mCulture").isEmpty()) {
            aNBT.removeTag("mCulture");
        }
        if (this.mFluid != null)
            aNBT.setString("mFluid", this.mFluid.getName());
        aNBT.setInteger("mSievert",this.mSievert);
        aNBT.setInteger("mNeededSievert",this.mNeededSievert);
        super.saveNBTData(aNBT);
    }

    @Override
    public void onRemoval() {
        int xDir = ForgeDirection.getOrientation(this.getBaseMetaTileEntity().getBackFacing()).offsetX * 2;
        int zDir = ForgeDirection.getOrientation(this.getBaseMetaTileEntity().getBackFacing()).offsetZ * 2;
        for (int x = -1; x < 2; x++) {
            for (int y = 1; y < 3; y++) {
                for (int z = -1; z < 2; z++) {
                    if (this.getBaseMetaTileEntity().getWorld().getBlock(xDir + x + this.getBaseMetaTileEntity().getXCoord(), y + this.getBaseMetaTileEntity().getYCoord(), zDir + z + this.getBaseMetaTileEntity().getZCoord()).equals(FluidLoader.bioFluidBlock))
                        this.getBaseMetaTileEntity().getWorld().setBlockToAir(xDir + x + this.getBaseMetaTileEntity().getXCoord(), y + this.getBaseMetaTileEntity().getYCoord(), zDir + z + this.getBaseMetaTileEntity().getZCoord());
                    GT_TileEntity_BioVat.staticColorMap.remove(new Coords(xDir + x + this.getBaseMetaTileEntity().getXCoord(), y + this.getBaseMetaTileEntity().getYCoord(), zDir + z + this.getBaseMetaTileEntity().getZCoord()), this.getBaseMetaTileEntity().getWorld().provider.dimensionId);
                    if (FMLCommonHandler.instance().getSide().isServer())
                        MainMod.BW_Network_instance.sendPacketToAllPlayersInRange(
                                this.getBaseMetaTileEntity().getWorld(),
                                new RendererPacket(
                                        new Coords(
                                                xDir + x + this.getBaseMetaTileEntity().getXCoord(),
                                                y + this.getBaseMetaTileEntity().getYCoord(),
                                                zDir + z + this.getBaseMetaTileEntity().getZCoord(),
                                                this.getBaseMetaTileEntity().getWorld().provider.dimensionId
                                        ),
                                        this.mCulture == null ? BioCulture.NULLCULTURE.getColorRGB() : this.mCulture.getColorRGB(),
                                        true
                                ),
                                this.getBaseMetaTileEntity().getXCoord(),
                                this.getBaseMetaTileEntity().getZCoord()
                        );
                }
            }
        }
        super.onRemoval();
    }

    @Override
    public void loadNBTData(NBTTagCompound aNBT) {
        this.height = aNBT.getInteger("mFluidHeight");
        this.mCulture = BioCulture.getBioCulture(aNBT.getString("mCulture"));
        if (!aNBT.getString("mFluid").isEmpty())
            this.mFluid = FluidRegistry.getFluid(aNBT.getString("mFluid"));
        this.mSievert = aNBT.getInteger("mSievert");
        this.mNeededSievert = aNBT.getInteger("mNeededSievert");
        super.loadNBTData(aNBT);
    }

    @Override
    public boolean explodesOnComponentBreak(ItemStack itemStack) {
        return false;
    }

    @Override
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity iGregTechTileEntity) {
        return new GT_TileEntity_BioVat(this.mName);
    }

    @Override
    public String[] getDescription() {
        String[] dsc = StatCollector.translateToLocal("tooltip.tile.bvat.0.name").split(";");
        String[] fdsc = new String[dsc.length + 1];
        for (int i = 0; i < dsc.length; i++) {
            fdsc[i] = dsc[i];
            fdsc[dsc.length] = StatCollector.translateToLocal("tooltip.bw.1.name") + ChatColorHelper.DARKGREEN + " BartWorks";
        }
        return fdsc;
    }

    @Override
    public ITexture[] getTexture(IGregTechTileEntity aBaseMetaTileEntity, byte aSide, byte aFacing, byte aColorIndex, boolean aActive, boolean aRedstone) {
        return aSide == aFacing ? new ITexture[]{Textures.BlockIcons.CASING_BLOCKS[GT_TileEntity_BioVat.MCASING_INDEX], new GT_RenderedTexture(aActive ? Textures.BlockIcons.OVERLAY_FRONT_DISTILLATION_TOWER_ACTIVE : Textures.BlockIcons.OVERLAY_FRONT_DISTILLATION_TOWER)} : new ITexture[]{Textures.BlockIcons.CASING_BLOCKS[GT_TileEntity_BioVat.MCASING_INDEX]};
    }
}