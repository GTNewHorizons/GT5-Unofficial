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

package com.github.bartimaeusnek.bartworks.common.tileentities;

import com.github.bartimaeusnek.bartworks.API.BioVatLogicAdder;
import com.github.bartimaeusnek.bartworks.MainMod;
import com.github.bartimaeusnek.bartworks.client.gui.GT_GUIContainer_RadioHatch;
import com.github.bartimaeusnek.bartworks.server.container.GT_Container_RadioHatch;
import com.github.bartimaeusnek.bartworks.util.BW_Util;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.Materials;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.enums.Textures;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_Hatch;
import gregtech.api.objects.GT_RenderedTexture;
import gregtech.api.util.GT_OreDictUnificator;
import gregtech.api.util.GT_Utility;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.StatCollector;

import static com.github.bartimaeusnek.bartworks.util.BW_Util.calculateSv;

public class GT_MetaTileEntity_RadioHatch extends GT_MetaTileEntity_Hatch {

    private final int cap;
    public int sievert;
    private long timer = 1;
    private short[] colorForGUI;
    private byte mass;
    private String material;
    private byte coverage = 0;

    public GT_MetaTileEntity_RadioHatch(int aID, String aName, String aNameRegional, int aTier) {
        super(aID, aName, aNameRegional, aTier, 1, new String[]{"Radioactive Item Chamber for Multiblocks", "Capacity: " + (aTier - 2) + " kg" + ((aTier - 2) >= 2 ? "s" : ""), "Use a screwdriver to set the containment level"});
        cap = aTier - 2;
    }

    public GT_MetaTileEntity_RadioHatch(String aName, int aTier, String aDescription, ITexture[][][] aTextures) {
        super(aName, aTier, 1, aDescription, aTextures);
        cap = aTier - 2;
    }

    public GT_MetaTileEntity_RadioHatch(String aName, int aTier, String[] aDescription, ITexture[][][] aTextures) {
        super(aName, aTier, 1, aDescription, aTextures);
        cap = aTier - 2;
    }

    public static long calcDecayTicks(int x) {
        long ret = 0;
        if (x >= 83 && x <= 100)
            ret = (long) Math.ceil((8000D * Math.tanh(-x / 20D) + 8000D) * 1000D);
        else if (x == 43)
            ret = 5000;
        else if (x == 61)
            ret = 4500;
        else if (x > 100)
            ret = (long) Math.ceil(((8000D * Math.tanh(-x / 65D) + 8000D)));
        return ret;//*20;
    }

    public int getSievert() {
        return this.sievert - (int) Math.ceil((float) this.sievert / 100f * (float) coverage);
    }

    public short[] getColorForGUI() {
        if (this.colorForGUI != null)
            return this.colorForGUI;
        return new short[]{0xFA, 0xFA, 0xFF};
    }

    public byte getMass() {
        return this.mass;
    }

    public byte getCoverage() {
        return this.coverage;
    }

    public void setCoverage(short coverage) {
        byte nu = 0;
        if (coverage > 100)
            nu = 100;
        else if (coverage < 0)
            nu = 0;
        else
            nu = (byte) coverage;
        this.coverage = nu;
    }

    public ITexture[] getTexturesActive(ITexture aBaseTexture) {
        return new ITexture[]{aBaseTexture, new GT_RenderedTexture(Textures.BlockIcons.OVERLAY_PIPE_IN)};
    }

    public ITexture[] getTexturesInactive(ITexture aBaseTexture) {
        return new ITexture[]{aBaseTexture, new GT_RenderedTexture(Textures.BlockIcons.OVERLAY_PIPE_IN)};
    }

    @Override
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity iGregTechTileEntity) {
        return new GT_MetaTileEntity_RadioHatch(mName, mTier, mDescriptionArray, mTextures);
    }

    @Override
    public void onScrewdriverRightClick(byte aSide, EntityPlayer aPlayer, float aX, float aY, float aZ) {
        aPlayer.openGui(MainMod.modID, 2, this.getBaseMetaTileEntity().getWorld(), this.getBaseMetaTileEntity().getXCoord(), this.getBaseMetaTileEntity().getYCoord(), this.getBaseMetaTileEntity().getZCoord());
        //super.onScrewdriverRightClick(aSide, aPlayer, aX, aY, aZ);
    }

    public boolean onRightclick(IGregTechTileEntity aBaseMetaTileEntity, EntityPlayer aPlayer) {
        if (aBaseMetaTileEntity.isClientSide()) {
            return true;
        } else {
            aBaseMetaTileEntity.openGUI(aPlayer);
            return true;
        }
    }

    public void updateSlots() {
        if (this.mInventory[0] != null && this.mInventory[0].stackSize <= 0)
            this.mInventory[0] = null;
    }

    public void onPostTick(IGregTechTileEntity aBaseMetaTileEntity, long aTimer) {
        if (this.getBaseMetaTileEntity().isServerSide()) {

            if (this.mass > 0)
                ++timer;

            if (this.mass > 0 && this.sievert > 0 && calcDecayTicks(this.sievert) > 0) {
                if (timer % (calcDecayTicks(this.sievert)) == 0) {
                    this.mass--;
                    if (mass == 0) {
                        material = "Empty";
                        sievert = 0;
                    }
                    timer = 1;
                }
            }

            ItemStack lStack = this.mInventory[0];

            if (lStack == null)
                return;

            if (GT_Utility.areStacksEqual(lStack, ItemList.NaquadahCell_1.get(1)) || GT_Utility.areStacksEqual(lStack, ItemList.NaquadahCell_2.get(1)) || GT_Utility.areStacksEqual(lStack, ItemList.NaquadahCell_4.get(1)) || GT_Utility.areStacksEqual(lStack, ItemList.ThoriumCell_1.get(1)) || GT_Utility.areStacksEqual(lStack, ItemList.ThoriumCell_2.get(1)) || GT_Utility.areStacksEqual(lStack, ItemList.ThoriumCell_4.get(1)) || GT_Utility.areStacksEqual(lStack, ItemList.Uraniumcell_1.get(1)) || GT_Utility.areStacksEqual(lStack, ItemList.Uraniumcell_2.get(1)) || GT_Utility.areStacksEqual(lStack, ItemList.Uraniumcell_4.get(1)) || GT_Utility.areStacksEqual(lStack, ItemList.Moxcell_1.get(1)) || GT_Utility.areStacksEqual(lStack, ItemList.Moxcell_2.get(1)) || GT_Utility.areStacksEqual(lStack, ItemList.Moxcell_4.get(1))) {
                Materials materials = Materials.Uranium;
                byte kg = 3;

                if (GT_Utility.areStacksEqual(lStack, ItemList.Moxcell_1.get(1)) || GT_Utility.areStacksEqual(lStack, ItemList.Moxcell_2.get(1)) || GT_Utility.areStacksEqual(lStack, ItemList.Moxcell_4.get(1)))
                    materials = Materials.Plutonium;
                else if (GT_Utility.areStacksEqual(lStack, ItemList.ThoriumCell_1.get(1)) || GT_Utility.areStacksEqual(lStack, ItemList.ThoriumCell_2.get(1)) || GT_Utility.areStacksEqual(lStack, ItemList.ThoriumCell_4.get(1)))
                    materials = Materials.Thorium;
                else if (GT_Utility.areStacksEqual(lStack, ItemList.NaquadahCell_1.get(1)) || GT_Utility.areStacksEqual(lStack, ItemList.NaquadahCell_2.get(1)) || GT_Utility.areStacksEqual(lStack, ItemList.NaquadahCell_4.get(1)))
                    materials = Materials.Naquadah;
                else
                    kg = 6;

                if (GT_Utility.areStacksEqual(lStack, ItemList.NaquadahCell_2.get(1)) || GT_Utility.areStacksEqual(lStack, ItemList.Moxcell_2.get(1)) || GT_Utility.areStacksEqual(lStack, ItemList.Uraniumcell_2.get(1)) || GT_Utility.areStacksEqual(lStack, ItemList.ThoriumCell_2.get(1)))
                    kg = (byte) (2 * kg);
                else if (GT_Utility.areStacksEqual(lStack, ItemList.Moxcell_4.get(1)) || GT_Utility.areStacksEqual(lStack, ItemList.Uraniumcell_4.get(1)) || GT_Utility.areStacksEqual(lStack, ItemList.ThoriumCell_4.get(1)) || GT_Utility.areStacksEqual(lStack, ItemList.NaquadahCell_4.get(1)))
                    kg = (byte) (4 * kg);


                if (this.mass == 0 || this.sievert == calculateSv(materials)) {
                    if (this.mass + kg <= cap) {
                        this.sievert = calculateSv(materials);
                        this.mass += kg;
                        this.mInventory[0].stackSize--;
                        updateSlots();
                        colorForGUI = materials.mColor.mRGBa;
                        material = materials.mName;
                    }
                }
            }

            for (ItemStack varStack : BioVatLogicAdder.RadioHatch.getIsSv().keySet()) {
                if (GT_Utility.areStacksEqual(varStack, lStack)) {
                    if (this.mass == 0 || this.sievert == BioVatLogicAdder.RadioHatch.getIsSv().get(varStack)) {
                        if (this.mass < this.cap) {
                            this.mass++;
                            this.sievert = BioVatLogicAdder.RadioHatch.getIsSv().get(varStack);
                            this.mInventory[0].stackSize--;
                            updateSlots();
                            colorForGUI = null;
                            material = StatCollector.translateToLocal(varStack.getUnlocalizedName());
                            return;
                        }
                    }
                }
            }

            //check material for general validity
            if (lStack != null && GT_OreDictUnificator.getAssociation(lStack) != null && GT_OreDictUnificator.getAssociation(lStack).mMaterial != null && GT_OreDictUnificator.getAssociation(lStack).mMaterial.mMaterial != null) {
                //check orePrefix for general validity
                if (GT_OreDictUnificator.getAssociation(lStack).mPrefix != null) {
                    OrePrefixes orePrefixes = GT_OreDictUnificator.getAssociation(lStack).mPrefix;
                    //check orePrefix for specialised validity
                    if (orePrefixes.equals(OrePrefixes.stickLong) || orePrefixes.equals(OrePrefixes.stick)) {
                        Materials materials = GT_OreDictUnificator.getAssociation(lStack).mMaterial.mMaterial;
                        //check material for specialised validity
                        if (materials.getProtons() >= 83 && materials.getProtons() != 125 || materials.getProtons() == 61 || materials.getProtons() == 43) {
                            if (this.mass == 0 || this.sievert == calculateSv(materials)) {
                                if ((this.mass + (orePrefixes.equals(OrePrefixes.stickLong) ? 2 : 1)) <= cap) {
                                    this.sievert = calculateSv(materials);
                                    this.mass += orePrefixes.equals(OrePrefixes.stickLong) ? 2 : 1;
                                    this.mInventory[0].stackSize--;
                                    updateSlots();
                                    colorForGUI = materials.mColor.mRGBa;
                                    material = materials.mName;
                                }
                            }
                        }
                    }
                }
            }


        }
    }

    @Override
    public boolean isGivingInformation() {
        return true;
    }

    @Override
    public String[] getInfoData() {
        return new String[]{"Material: " + material, "Sievert: " + sievert, "Amount: " + mass, "Time (in t/s/m/h) to decay (1kg): " + ((calcDecayTicks(this.sievert)) - timer % (calcDecayTicks(this.sievert) * 60)) + "t/" + ((calcDecayTicks(this.sievert)) - timer % (calcDecayTicks(this.sievert))) / 20 + "s/" + ((calcDecayTicks(this.sievert)) - timer % (calcDecayTicks(this.sievert))) / 20 / 60 + "m/" + ((calcDecayTicks(this.sievert)) - timer % (calcDecayTicks(this.sievert))) / 20 / 60 / 60 + "h"};
    }

    public boolean isSimpleMachine() {
        return true;
    }

    public boolean isFacingValid(byte aFacing) {
        return true;
    }

    public boolean isAccessAllowed(EntityPlayer aPlayer) {
        return true;
    }

    public boolean isValidSlot(int aIndex) {
        return true;
    }

    public boolean allowPullStack(IGregTechTileEntity aBaseMetaTileEntity, int aIndex, byte aSide, ItemStack aStack) {
        return false;
    }

    public boolean allowPutStack(IGregTechTileEntity aBaseMetaTileEntity, int aIndex, byte aSide, ItemStack aStack) {
        return aSide == this.getBaseMetaTileEntity().getFrontFacing();
    }

    public Object getServerGUI(int aID, InventoryPlayer aPlayerInventory, IGregTechTileEntity aBaseMetaTileEntity) {
        return new GT_Container_RadioHatch(aPlayerInventory, aBaseMetaTileEntity);
    }

    public Object getClientGUI(int aID, InventoryPlayer aPlayerInventory, IGregTechTileEntity aBaseMetaTileEntity) {
        return new GT_GUIContainer_RadioHatch(aPlayerInventory, aBaseMetaTileEntity, this.mName);
    }

    @Override
    public void saveNBTData(NBTTagCompound aNBT) {
        aNBT.setByte("mMass", mass);
        aNBT.setByte("mSv", (byte) (sievert - 100));
        aNBT.setByte("mCoverage", coverage);
        aNBT.setInteger("mTextColor", BW_Util.getColorFromArray(getColorForGUI()));
        if (material != null && !material.isEmpty())
            aNBT.setString("mMaterial", material);
        aNBT.setLong("timer", timer);
        super.saveNBTData(aNBT);
    }

    public long getTimer() {
        return this.timer;
    }

    @Override
    public void loadNBTData(NBTTagCompound aNBT) {
        timer = aNBT.getLong("timer");
        mass = aNBT.getByte("mMass");
        sievert = aNBT.getByte("mSv") + 100;
        coverage = aNBT.getByte("mCoverage");
        colorForGUI = BW_Util.splitColortoArray(aNBT.getInteger("mTextColor"));
        material = aNBT.getString("mMaterial");
        super.loadNBTData(aNBT);
    }
}
