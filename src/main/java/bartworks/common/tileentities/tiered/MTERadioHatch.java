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

package bartworks.common.tileentities.tiered;

import static bartworks.common.loaders.RadioHatchMaterialLoader.getRadioHatchMaterialFromInput;
import static bartworks.common.loaders.RadioHatchMaterialLoader.getRadioHatchMaterialList;
import static gregtech.api.enums.GTValues.ticksBetweenSounds;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StatCollector;
import net.minecraftforge.common.util.ForgeDirection;

import com.cleanroommc.modularui.factory.PosGuiData;
import com.cleanroommc.modularui.screen.ModularPanel;
import com.cleanroommc.modularui.screen.UISettings;
import com.cleanroommc.modularui.value.sync.PanelSyncManager;

import bartworks.API.recipe.BartWorksRecipeMaps;
import bartworks.MainMod;
import bartworks.common.loaders.RadioHatchMaterialLoader.RadioHatchMaterial;
import bartworks.util.BWColorUtil;
import bartworks.util.BWTooltipReference;
import bartworks.util.MathUtils;
import gregtech.api.enums.Materials;
import gregtech.api.enums.Textures;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.interfaces.tileentity.RecipeMapWorkable;
import gregtech.api.metatileentity.BaseMetaTileEntity;
import gregtech.api.metatileentity.implementations.MTEHatch;
import gregtech.api.objects.ItemData;
import gregtech.api.recipe.RecipeMap;
import gregtech.api.render.TextureFactory;
import gregtech.api.util.GTOreDictUnificator;
import gregtech.api.util.GTUtility;
import gregtech.common.gui.modularui.hatch.MTERadioHatchGui;

public class MTERadioHatch extends MTEHatch implements RecipeMapWorkable {

    public int sievert;
    private long timer = 1;
    private long decayTime = 1;
    private short[] colorForGUI = { 0x02, 0x02, 0x02 };
    private byte mass;
    private String material;
    private byte coverage;
    private ItemStack lastUsedItem = null;
    private boolean lastFail = false;
    private RadioHatchMaterial radioHatchMaterial = null;

    public MTERadioHatch(int aID, String aName, String aNameRegional, int aTier) {
        super(
            aID,
            aName,
            aNameRegional,
            aTier,
            1,
            new String[] { StatCollector.translateToLocal("tooltip.tile.radhatch.0.name"),
                StatCollector.translateToLocal("tooltip.tile.radhatch.1.name"),
                BWTooltipReference.ADDED_BY_BARTIMAEUSNEK_VIA_BARTWORKS.get(), });
    }

    public MTERadioHatch(String aName, int aTier, String[] aDescription, ITexture[][][] aTextures) {
        super(aName, aTier, 1, aDescription, aTextures);
    }

    public int getSievert() {
        return this.sievert - MathUtils.ceilInt(this.sievert / 100f * this.coverage);
    }

    public void setSievert(int b) {
        this.sievert = b;
    }

    public short[] getColorForGUI() {
        if (this.colorForGUI != null) return this.colorForGUI;
        return this.colorForGUI = new short[] { 0xFA, 0xFA, 0xFF };
    }

    public byte getMass() {
        return this.mass;
    }

    public void setMass(byte b) {
        this.mass = b;
    }

    public short getColorForGuiAtIndex(int i) {
        i = GTUtility.clamp(i, 0, 3);
        return colorForGUI[i];
    }

    public void setColorForGuiAtIndex(short c, int i) {
        i = GTUtility.clamp(i, 0, 3);
        colorForGUI[i] = c;
    }

    public byte getCoverage() {
        return this.coverage;
    }

    public void setCoverage(short coverage) {
        byte nu;
        if (coverage > 100) nu = 100;
        else if (coverage < 0) nu = 0;
        else nu = (byte) coverage;
        this.coverage = nu;
    }

    @Override
    public ITexture[] getTexturesActive(ITexture aBaseTexture) {
        return new ITexture[] { aBaseTexture, TextureFactory.of(Textures.BlockIcons.OVERLAY_PIPE_IN) };
    }

    @Override
    public ITexture[] getTexturesInactive(ITexture aBaseTexture) {
        return new ITexture[] { aBaseTexture, TextureFactory.of(Textures.BlockIcons.OVERLAY_PIPE_IN) };
    }

    @Override
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity iGregTechTileEntity) {
        return new MTERadioHatch(this.mName, this.mTier, this.mDescriptionArray, this.mTextures);
    }

    @Override
    public boolean onRightclick(IGregTechTileEntity aBaseMetaTileEntity, EntityPlayer aPlayer) {
        openGui(aPlayer);
        return true;
    }

    public void updateSlots() {
        if (this.mInventory[0] != null && this.mInventory[0].stackSize <= 0) this.mInventory[0] = null;
    }

    @Override
    public void onPostTick(IGregTechTileEntity aBaseMetaTileEntity, long aTimer) {
        BaseMetaTileEntity myMetaTileEntity = (BaseMetaTileEntity) this.getBaseMetaTileEntity();
        if (myMetaTileEntity.isServerSide()) {

            if (this.mass > 0) {
                ++this.timer;
            }

            if (this.mass > 0 && (this.decayTime == 0 || this.decayTime > 0 && this.timer % this.decayTime == 0)) {
                this.mass--;
                if (this.mass == 0) {
                    this.material = StatCollector.translateToLocal("tooltip.bw.empty.name");
                    this.sievert = 0;
                }
                this.timer = 1;
            }

            if (myMetaTileEntity.mTickTimer > myMetaTileEntity.mLastSoundTick + ticksBetweenSounds
                && this.sievert > 0) {
                this.sendLoopStart((byte) 1);
                myMetaTileEntity.mLastSoundTick = myMetaTileEntity.mTickTimer;
            }

            if (this.mass == 0) {
                ItemStack lStack = this.mInventory[0];

                if (lStack == null) {
                    this.colorForGUI = new short[] { 0x37, 0x37, 0x37 };
                    return;
                }

                ItemData itemData = GTOreDictUnificator.getAssociation(lStack);
                if (itemData != null) {
                    Materials mat = itemData.mMaterial.mMaterial;
                    this.colorForGUI = new short[] { mat.getRGBA()[0], mat.getRGBA()[1], mat.getRGBA()[2] };
                } else {
                    this.colorForGUI = new short[] { 0x37, 0x37, 0x37 };
                }
                if (this.lastFail && GTUtility.areStacksEqual(this.lastUsedItem, lStack, true)) {
                    return;
                }
                if (!this.lastFail && this.lastUsedItem != null && this.radioHatchMaterial != null) {
                    if (GTUtility.areStacksEqual(this.lastUsedItem, lStack, true)) {
                        for (RadioHatchMaterial recipes : getRadioHatchMaterialList()) {
                            this.radioHatchMaterial = getRadioHatchMaterialFromInput(recipes, this.lastUsedItem);
                            if (radioHatchMaterial != null) {
                                break;
                            }
                        }
                        if (radioHatchMaterial != null && getBaseMetaTileEntity().isAllowedToWork()) {
                            this.sievert = this.radioHatchMaterial.recipeSievert;
                            this.mass = this.radioHatchMaterial.recipeMass;
                            this.decayTime = calcDecayTicks(radioHatchMaterial.recipeSievert);
                            this.material = this.lastUsedItem.getDisplayName();
                            lStack.stackSize--;
                            this.updateSlots();
                        }
                    } else {
                        this.radioHatchMaterial = null;
                    }
                }

                if (this.radioHatchMaterial == null || this.lastFail || this.radioHatchMaterial.recipeSievert == 0) {
                    for (RadioHatchMaterial recipes : getRadioHatchMaterialList()) {
                        this.radioHatchMaterial = getRadioHatchMaterialFromInput(recipes, this.mInventory[0]);
                        if (radioHatchMaterial != null) {
                            break;
                        }
                    }
                    if (this.radioHatchMaterial == null) {
                        this.lastFail = true;
                        this.lastUsedItem = this.mInventory[0] == null ? null : this.mInventory[0].copy();
                    } else {
                        this.lastFail = false;
                        this.lastUsedItem = this.mInventory[0].copy();
                        this.sievert = radioHatchMaterial.recipeSievert;
                        this.mass = radioHatchMaterial.recipeMass;
                        this.decayTime = calcDecayTicks(radioHatchMaterial.recipeSievert);
                        this.material = lStack.getDisplayName();
                        lStack.stackSize--;
                        this.updateSlots();
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
        if (this.sievert != 0) return new String[] {
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
                + StatCollector.translateToLocal("tooltip.tile.radhatch.9.name") };
        return new String[] {
            StatCollector.translateToLocal("tooltip.tile.radhatch.2.name") + " "
                + StatCollector.translateToLocal("tooltip.bw.empty.name"),
            StatCollector.translateToLocal("tooltip.tile.radhatch.3.name") + " " + "0",
            StatCollector.translateToLocal("tooltip.tile.radhatch.4.name") + " " + "0" };
    }

    @Override
    public boolean isFacingValid(ForgeDirection facing) {
        return true;
    }

    @Override
    public boolean isValidSlot(int aIndex) {
        return true;
    }

    @Override
    public boolean allowPullStack(IGregTechTileEntity aBaseMetaTileEntity, int aIndex, ForgeDirection side,
        ItemStack aStack) {
        return false;
    }

    @Override
    public boolean allowPutStack(IGregTechTileEntity aBaseMetaTileEntity, int aIndex, ForgeDirection side,
        ItemStack aStack) {
        return side == this.getBaseMetaTileEntity()
            .getFrontFacing() && BartWorksRecipeMaps.radioHatchFakeRecipes.containsInput(aStack);
    }

    @Override
    public void saveNBTData(NBTTagCompound aNBT) {
        aNBT.setByte("mMass", this.mass);
        aNBT.setInteger("mSievert", this.sievert);
        aNBT.setByte("mCoverage", this.coverage);
        aNBT.setInteger("mTextColor", BWColorUtil.getColorFromRGBArray(this.getColorForGUI()));
        if (this.material != null && !this.material.isEmpty()) aNBT.setString("mMaterial", this.material);
        aNBT.setLong("timer", this.timer);
        aNBT.setLong("decay", this.decayTime);
        super.saveNBTData(aNBT);
    }

    @Override
    public void loadNBTData(NBTTagCompound aNBT) {
        this.timer = aNBT.getLong("timer");
        this.mass = aNBT.getByte("mMass");
        this.sievert = (aNBT.getByte("mSv") != 0) ? aNBT.getByte("mSv") + 100 : aNBT.getInteger("mSievert");
        this.coverage = aNBT.getByte("mCoverage");
        this.colorForGUI = BWColorUtil.splitColorToRBGArray(aNBT.getInteger("mTextColor"));
        this.material = aNBT.getString("mMaterial");
        this.decayTime = aNBT.getLong("decay");
        super.loadNBTData(aNBT);
    }

    @Override
    public void startSoundLoop(byte aIndex, double aX, double aY, double aZ) {
        super.startSoundLoop(aIndex, aX, aY, aZ);
        ResourceLocation rl = new ResourceLocation(MainMod.MOD_ID, "hatch.RadOn");
        if (aIndex == 1) {
            GTUtility.doSoundAtClient(rl, 10, 1.0F, aX, aY, aZ);
        }
    }

    @Override
    public RecipeMap<?> getRecipeMap() {
        // Only for visual
        return BartWorksRecipeMaps.radioHatchFakeRecipes;
    }

    private static final int RADIATION_SHUTTER_WINDOW_ID = 999;

    public long getDecayTime() {
        return decayTime;
    }

    public void setDecayTime(long decayTime) {
        this.decayTime = decayTime;
    }

    public long getTimer() {
        return timer;
    }

    public void setTimer(long timer) {
        this.timer = timer;
    }

    @Override
    protected boolean useMui2() {
        return true;
    }

    @Override
    public ModularPanel buildUI(PosGuiData data, PanelSyncManager syncManager, UISettings uiSettings) {
        return new MTERadioHatchGui(this).build(data, syncManager, uiSettings);
    }

    public static long calcDecayTicks(int x) {
        long ret;
        if (x == 43) ret = 5000;
        else if (x == 61) ret = 4500;
        else if (x <= 100) ret = MathUtils.ceilLong((8000D * Math.tanh(-x / 20D) + 8000D) * 1000D);
        else ret = MathUtils.ceilLong(8000D * Math.tanh(-x / 65D) + 8000D);
        return ret;
    }
}
