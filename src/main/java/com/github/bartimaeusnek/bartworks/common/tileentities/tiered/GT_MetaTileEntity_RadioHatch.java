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

import static gregtech.api.enums.GT_Values.ticksBetweenSounds;

import com.github.bartimaeusnek.bartworks.MainMod;
import com.github.bartimaeusnek.bartworks.client.gui.GT_GUIContainer_RadioHatch;
import com.github.bartimaeusnek.bartworks.server.container.GT_Container_RadioHatch;
import com.github.bartimaeusnek.bartworks.util.BWRecipes;
import com.github.bartimaeusnek.bartworks.util.BW_ColorUtil;
import com.github.bartimaeusnek.bartworks.util.BW_Tooltip_Reference;
import com.github.bartimaeusnek.bartworks.util.MathUtils;
import gregtech.api.enums.Textures;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.BaseMetaTileEntity;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_Hatch;
import gregtech.api.render.TextureFactory;
import gregtech.api.util.GT_Recipe;
import gregtech.api.util.GT_Utility;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StatCollector;

public class GT_MetaTileEntity_RadioHatch extends GT_MetaTileEntity_Hatch {

    private final int cap;
    public int sievert;
    private long timer = 1;
    private long decayTime = 1;
    private short[] colorForGUI;
    private byte mass;
    private String material;
    private byte coverage;
    private ItemStack lastUsedItem = null;
    private boolean lastFail = false;
    private GT_Recipe lastRecipe = null;

    public GT_MetaTileEntity_RadioHatch(int aID, String aName, String aNameRegional, int aTier) {
        super(aID, aName, aNameRegional, aTier, 1, new String[] {
            StatCollector.translateToLocal("tooltip.tile.radhatch.0.name"),
            StatCollector.translateToLocal("tooltip.tile.tiereddsc.3.name") + " " + (aTier - 2) + " "
                    + ((aTier - 2) >= 2
                            ? StatCollector.translateToLocal("tooltip.bw.kg.1.name")
                            : StatCollector.translateToLocal("tooltip.bw.kg.0.name")),
            StatCollector.translateToLocal("tooltip.tile.radhatch.1.name"),
            BW_Tooltip_Reference.ADDED_BY_BARTIMAEUSNEK_VIA_BARTWORKS.get()
        });
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

    public int getSievert() {
        return this.sievert - MathUtils.ceilInt((float) this.sievert / 100f * (float) this.coverage);
    }

    public short[] getColorForGUI() {
        if (this.colorForGUI != null) return this.colorForGUI;
        return new short[] {0xFA, 0xFA, 0xFF};
    }

    public byte getMass() {
        return this.mass;
    }

    public byte getCoverage() {
        return this.coverage;
    }

    public long getDecayTime() {
        return this.decayTime;
    }

    public void setCoverage(short coverage) {
        byte nu;
        if (coverage > 100) nu = 100;
        else if (coverage < 0) nu = 0;
        else nu = (byte) coverage;
        this.coverage = nu;
    }

    public ITexture[] getTexturesActive(ITexture aBaseTexture) {
        return new ITexture[] {aBaseTexture, TextureFactory.of(Textures.BlockIcons.OVERLAY_PIPE_IN)};
    }

    public ITexture[] getTexturesInactive(ITexture aBaseTexture) {
        return new ITexture[] {aBaseTexture, TextureFactory.of(Textures.BlockIcons.OVERLAY_PIPE_IN)};
    }

    @Override
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity iGregTechTileEntity) {
        return new GT_MetaTileEntity_RadioHatch(this.mName, this.mTier, this.mDescriptionArray, this.mTextures);
    }

    @Override
    public void onScrewdriverRightClick(byte aSide, EntityPlayer aPlayer, float aX, float aY, float aZ) {
        aPlayer.openGui(
                MainMod.MOD_ID,
                2,
                this.getBaseMetaTileEntity().getWorld(),
                this.getBaseMetaTileEntity().getXCoord(),
                this.getBaseMetaTileEntity().getYCoord(),
                this.getBaseMetaTileEntity().getZCoord());
    }

    public boolean onRightclick(IGregTechTileEntity aBaseMetaTileEntity, EntityPlayer aPlayer) {
        if (!aBaseMetaTileEntity.isClientSide()) {
            aBaseMetaTileEntity.openGUI(aPlayer);
        }
        return true;
    }

    public void updateSlots() {
        if (this.mInventory[0] != null && this.mInventory[0].stackSize <= 0) this.mInventory[0] = null;
    }

    public void onPostTick(IGregTechTileEntity aBaseMetaTileEntity, long aTimer) {
        BaseMetaTileEntity myMetaTileEntity = ((BaseMetaTileEntity) this.getBaseMetaTileEntity());
        if (myMetaTileEntity.isServerSide()) {

            if (this.mass > 0) {
                ++this.timer;
            }

            if (this.mass > 0) {
                if (this.decayTime == 0 || (this.decayTime > 0 && this.timer % this.decayTime == 0)) {
                    this.mass--;
                    if (this.mass == 0) {
                        this.material = StatCollector.translateToLocal("tooltip.bw.empty.name");
                        this.sievert = 0;
                    }
                    this.timer = 1;
                }
            }

            if (myMetaTileEntity.mTickTimer > (myMetaTileEntity.mLastSoundTick + ticksBetweenSounds)) {
                if (this.sievert > 0) {
                    sendLoopStart((byte) 1);
                    myMetaTileEntity.mLastSoundTick = myMetaTileEntity.mTickTimer;
                }
            }

            if (this.mass == 0) {
                ItemStack lStack = this.mInventory[0];

                if (lStack == null) {
                    return;
                }

                if (this.lastFail && GT_Utility.areStacksEqual(this.lastUsedItem, lStack, true)) {
                    return;
                }

                if (!this.lastFail && this.lastUsedItem != null && this.lastRecipe != null) {
                    if (GT_Utility.areStacksEqual(this.lastUsedItem, lStack, true)) {
                        this.mass = (byte) this.lastRecipe.mDuration;
                        this.decayTime = this.lastRecipe.mSpecialValue;
                        this.sievert = this.lastRecipe.mEUt;
                        this.material = this.lastUsedItem.getDisplayName();
                        lStack.stackSize--;
                        updateSlots();
                    } else {
                        this.lastRecipe = null;
                    }
                }

                if (this.lastRecipe == null || this.lastFail) {
                    this.lastRecipe = BWRecipes.instance
                            .getMappingsFor(BWRecipes.RADHATCH)
                            .findRecipe(
                                    this.getBaseMetaTileEntity(), false, Integer.MAX_VALUE - 7, null, mInventory[0]);
                    if (this.lastRecipe == null) {
                        this.lastFail = true;
                        this.lastUsedItem = this.mInventory[0] == null ? null : this.mInventory[0].copy();
                    } else {
                        if (this.lastRecipe.mDuration > this.cap) {
                            this.lastFail = true;
                            this.lastUsedItem = this.mInventory[0].copy();
                            return;
                        }
                        this.lastFail = false;
                        this.lastUsedItem = this.mInventory[0].copy();
                        this.mass = (byte) this.lastRecipe.mDuration;
                        this.decayTime = this.lastRecipe.mSpecialValue;
                        this.sievert = this.lastRecipe.mEUt;
                        this.colorForGUI = new short[] {
                            (short) this.lastRecipe.mChances[0],
                            (short) this.lastRecipe.mChances[1],
                            (short) this.lastRecipe.mChances[2]
                        };
                        this.material = lStack.getDisplayName();
                        lStack.stackSize--;
                        updateSlots();
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
        if (this.sievert != 0)
            return new String[] {
                StatCollector.translateToLocal("tooltip.tile.radhatch.2.name") + " "
                        + StatCollector.translateToLocal(this.material),
                StatCollector.translateToLocal("tooltip.tile.radhatch.3.name") + " " + this.sievert,
                StatCollector.translateToLocal("tooltip.tile.radhatch.4.name") + " " + this.mass,
                StatCollector.translateToLocal("tooltip.tile.radhatch.5.name") + " "
                        + (this.decayTime - this.timer % (this.decayTime * 60))
                        + StatCollector.translateToLocal("tooltip.tile.radhatch.6.name")
                        + "/"
                        + (this.decayTime - this.timer % this.decayTime) / 20
                        + StatCollector.translateToLocal("tooltip.tile.radhatch.7.name")
                        + "/"
                        + (this.decayTime - this.timer % this.decayTime) / 20 / 60
                        + StatCollector.translateToLocal("tooltip.tile.radhatch.8.name")
                        + "/"
                        + (this.decayTime - this.timer % this.decayTime) / 20 / 60 / 60
                        + StatCollector.translateToLocal("tooltip.tile.radhatch.9.name")
            };
        else
            return new String[] {
                StatCollector.translateToLocal("tooltip.tile.radhatch.2.name") + " "
                        + StatCollector.translateToLocal("tooltip.bw.empty.name"),
                StatCollector.translateToLocal("tooltip.tile.radhatch.3.name") + " " + "0",
                StatCollector.translateToLocal("tooltip.tile.radhatch.4.name") + " " + "0"
            };
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
        return aSide == this.getBaseMetaTileEntity().getFrontFacing()
                && BWRecipes.instance.getMappingsFor(BWRecipes.RADHATCH).containsInput(aStack);
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
        if (this.material != null && !this.material.isEmpty()) aNBT.setString("mMaterial", this.material);
        aNBT.setLong("timer", this.timer);
        aNBT.setLong("decay", this.decayTime);
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
        this.decayTime = aNBT.getLong("decay");
        super.loadNBTData(aNBT);
    }

    @Override
    public void startSoundLoop(byte aIndex, double aX, double aY, double aZ) {
        super.startSoundLoop(aIndex, aX, aY, aZ);
        ResourceLocation rl = new ResourceLocation(MainMod.MOD_ID, "hatch.RadOn");
        if (aIndex == 1) {
            GT_Utility.doSoundAtClient(rl, 10, 1.0F, aX, aY, aZ);
        }
    }
}
