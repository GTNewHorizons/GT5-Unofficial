/*
 * Copyright (c) 2018-2020 bartimaeusnek
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

package com.github.bartimaeusnek.bartworks.common.tileentities.tiered;

import com.github.bartimaeusnek.bartworks.API.BioVatLogicAdder;
import com.github.bartimaeusnek.bartworks.API.IRadMaterial;
import com.github.bartimaeusnek.bartworks.API.LoaderReference;
import com.github.bartimaeusnek.bartworks.MainMod;
import com.github.bartimaeusnek.bartworks.client.gui.GT_GUIContainer_RadioHatch;
import com.github.bartimaeusnek.bartworks.server.container.GT_Container_RadioHatch;
import com.github.bartimaeusnek.bartworks.util.BW_ColorUtil;
import com.github.bartimaeusnek.bartworks.util.ChatColorHelper;
import com.github.bartimaeusnek.bartworks.util.MathUtils;
import com.github.bartimaeusnek.crossmod.GTpp.loader.RadioHatchCompat;
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
    private byte coverage;

    public GT_MetaTileEntity_RadioHatch(int aID, String aName, String aNameRegional, int aTier) {
        super(aID, aName, aNameRegional, aTier, 1, new String[]{StatCollector.translateToLocal("tooltip.tile.radhatch.0.name"), StatCollector.translateToLocal("tooltip.tile.tiereddsc.3.name") + " " + (aTier - 2) + " " + ((aTier - 2) >= 2 ? StatCollector.translateToLocal("tooltip.bw.kg.1.name") : StatCollector.translateToLocal("tooltip.bw.kg.0.name")), StatCollector.translateToLocal("tooltip.tile.radhatch.1.name"), StatCollector.translateToLocal("tooltip.bw.1.name") + ChatColorHelper.DARKGREEN + " BartWorks"});
        this.cap = aTier - 2;
    }

    public GT_MetaTileEntity_RadioHatch(String aName, int aTier, String aDescription, ITexture[][][] aTextures) {
        super(aName, aTier, 1, aDescription, aTextures);
        this.cap = aTier - 2;
    }

    public GT_MetaTileEntity_RadioHatch(String aName, int aTier, String[] aDescription, ITexture[][][] aTextures) {
        super(aName, aTier, 1, aDescription, aTextures);
        this.cap = aTier - 2;
    }

    public static long calcDecayTicks(int x) {
        long ret;
        if (x == 43)
            ret = 5000;
        else if (x == 61)
            ret = 4500;
        else if (x <= 100)
            ret = MathUtils.ceilLong((8000D * Math.tanh(-x / 20D) + 8000D) * 1000D);
        else
            ret = MathUtils.ceilLong(((8000D * Math.tanh(-x / 65D) + 8000D)));
        return ret;//*20;
    }

    public int getSievert() {
        return this.sievert - MathUtils.ceilInt((float) this.sievert / 100f * (float) this.coverage);
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
        byte nu;
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
        return new GT_MetaTileEntity_RadioHatch(this.mName, this.mTier, this.mDescriptionArray, this.mTextures);
    }

    @Override
    public void onScrewdriverRightClick(byte aSide, EntityPlayer aPlayer, float aX, float aY, float aZ) {
        aPlayer.openGui(MainMod.MOD_ID, 2, this.getBaseMetaTileEntity().getWorld(), this.getBaseMetaTileEntity().getXCoord(), this.getBaseMetaTileEntity().getYCoord(), this.getBaseMetaTileEntity().getZCoord());
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
                ++this.timer;

            if (this.mass > 0 && this.sievert > 0 && GT_MetaTileEntity_RadioHatch.calcDecayTicks(this.sievert) > 0) {
                if (this.timer % (GT_MetaTileEntity_RadioHatch.calcDecayTicks(this.sievert)) == 0) {
                    this.mass--;
                    if (this.mass == 0) {
                        this.material = StatCollector.translateToLocal("tooltip.bw.empty.name");
                        this.sievert = 0;
                    }
                    this.timer = 1;
                }
            }

            ItemStack lStack = this.mInventory[0];

            if (lStack == null)
                return;

            IRadMaterial radmat = null;
            //gt++ compat
            if (LoaderReference.miscutils)
                radmat = RadioHatchCompat.GTppRadChecker(lStack);

            if (lStack.getItem() instanceof IRadMaterial || radmat != null) {
                if (radmat == null)
                    radmat = ((IRadMaterial) lStack.getItem());
                int sv = radmat.getRadiationLevel(lStack);
                if (sv > BioVatLogicAdder.RadioHatch.getMaxSv())
                    BioVatLogicAdder.RadioHatch.MaxSV = sv;
                if (this.mass == 0 || this.sievert == sv) {
                    if (this.mass + radmat.getAmountOfMaterial(lStack) <= this.cap) {
                        this.mass += radmat.getAmountOfMaterial(lStack);
                        this.sievert = sv;
                        this.mInventory[0].stackSize--;
                        this.updateSlots();
                        this.colorForGUI = radmat.getColorForGUI(lStack);
                        this.material = radmat.getNameForGUI(lStack);
                        return;
                    }
                }
            }

            for (ItemStack varStack : BioVatLogicAdder.RadioHatch.getIsSv().keySet()) {
                if (GT_Utility.areStacksEqual(varStack, lStack, true)) {
                    if (this.mass == 0 || this.sievert == BioVatLogicAdder.RadioHatch.getIsSv().get(varStack)) {
                        int massToAdd = BioVatLogicAdder.RadioHatch.getIsKg().getOrDefault(varStack,1);
                        if (this.mass + massToAdd <= this.cap) {
                            this.mass += massToAdd;
                            this.sievert = BioVatLogicAdder.RadioHatch.getIsSv().get(varStack);
                            this.mInventory[0].stackSize--;
                            this.updateSlots();
                            this.colorForGUI = BioVatLogicAdder.RadioHatch.getIsColor().get(varStack);
                            this.material = StatCollector.translateToLocal(varStack.getUnlocalizedName());
                            return;
                        }
                    }
                }
            }

            //check material for general validity
            if (GT_OreDictUnificator.getAssociation(lStack) != null && GT_OreDictUnificator.getAssociation(lStack).mMaterial != null && GT_OreDictUnificator.getAssociation(lStack).mMaterial.mMaterial != null) {
                //check orePrefix for general validity
                if (GT_OreDictUnificator.getAssociation(lStack).mPrefix != null) {
                    OrePrefixes orePrefixes = GT_OreDictUnificator.getAssociation(lStack).mPrefix;
                    //check orePrefix for specialised validity
                    if (orePrefixes.equals(OrePrefixes.stickLong) || orePrefixes.equals(OrePrefixes.stick)) {
                        Materials materials = GT_OreDictUnificator.getAssociation(lStack).mMaterial.mMaterial;
                        //check material for specialised validity
                        if (materials.getProtons() >= 83 && materials.getProtons() != 125 || materials.getProtons() == 61 || materials.getProtons() == 43) {
                            if (this.mass == 0 || this.sievert == calculateSv(materials)) {
                                if ((this.mass + (orePrefixes.equals(OrePrefixes.stickLong) ? 2 : 1)) <= this.cap) {
                                    this.sievert = calculateSv(materials);
                                    this.mass += orePrefixes.equals(OrePrefixes.stickLong) ? 2 : 1;
                                    this.mInventory[0].stackSize--;
                                    this.updateSlots();
                                    this.colorForGUI = materials.mColor.mRGBa;
                                    this.material = materials.mName;
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
        if (GT_MetaTileEntity_RadioHatch.calcDecayTicks(this.sievert) != 0)
            return new String[]{StatCollector.translateToLocal("tooltip.tile.radhatch.2.name") + " " + this.material, StatCollector.translateToLocal("tooltip.tile.radhatch.3.name") + " " + this.sievert, StatCollector.translateToLocal("tooltip.tile.radhatch.4.name") + " " + this.mass, StatCollector.translateToLocal("tooltip.tile.radhatch.5.name") + " " + +((GT_MetaTileEntity_RadioHatch.calcDecayTicks(this.sievert)) - this.timer % (GT_MetaTileEntity_RadioHatch.calcDecayTicks(this.sievert) * 60)) + StatCollector.translateToLocal("tooltip.tile.radhatch.6.name") + "/" + ((GT_MetaTileEntity_RadioHatch.calcDecayTicks(this.sievert)) - this.timer % (GT_MetaTileEntity_RadioHatch.calcDecayTicks(this.sievert))) / 20 + StatCollector.translateToLocal("tooltip.tile.radhatch.7.name") + "/" + ((GT_MetaTileEntity_RadioHatch.calcDecayTicks(this.sievert)) - this.timer % (GT_MetaTileEntity_RadioHatch.calcDecayTicks(this.sievert))) / 20 / 60 + StatCollector.translateToLocal("tooltip.tile.radhatch.8.name") + "/" + ((GT_MetaTileEntity_RadioHatch.calcDecayTicks(this.sievert)) - this.timer % (GT_MetaTileEntity_RadioHatch.calcDecayTicks(this.sievert))) / 20 / 60 / 60 + StatCollector.translateToLocal("tooltip.tile.radhatch.9.name")};
        else
            return new String[]{StatCollector.translateToLocal("tooltip.tile.radhatch.2.name") + " " + StatCollector.translateToLocal("tooltip.bw.empty.name"), StatCollector.translateToLocal("tooltip.tile.radhatch.3.name") + " " + "0", StatCollector.translateToLocal("tooltip.tile.radhatch.4.name") + " " + "0"};
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
        aNBT.setByte("mMass", this.mass);
        aNBT.setByte("mSv", (byte) (this.sievert - 100));
        aNBT.setByte("mCoverage", this.coverage);
        aNBT.setInteger("mTextColor", BW_ColorUtil.getColorFromRGBArray(this.getColorForGUI()));
        if (this.material != null && !this.material.isEmpty())
            aNBT.setString("mMaterial", this.material);
        aNBT.setLong("timer", this.timer);
        super.saveNBTData(aNBT);
    }

    public long getTimer() {
        return this.timer;
    }

    @Override
    public void loadNBTData(NBTTagCompound aNBT) {
        this.timer = aNBT.getLong("timer");
        this.mass = aNBT.getByte("mMass");
        this.sievert = aNBT.getByte("mSv") + 100;
        this.coverage = aNBT.getByte("mCoverage");
        this.colorForGUI = BW_ColorUtil.splitColorToRBGArray(aNBT.getInteger("mTextColor"));
        this.material = aNBT.getString("mMaterial");
        super.loadNBTData(aNBT);
    }
}
