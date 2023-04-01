package gregtech.common.tileentities.automation;

import static gregtech.api.enums.Textures.BlockIcons.AUTOMATION_REGULATOR;
import static gregtech.api.enums.Textures.BlockIcons.AUTOMATION_REGULATOR_GLOW;

import java.util.Collections;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

import com.gtnewhorizons.modularui.api.screen.ModularWindow;
import com.gtnewhorizons.modularui.api.screen.UIBuildContext;
import com.gtnewhorizons.modularui.common.internal.wrapper.BaseSlot;
import com.gtnewhorizons.modularui.common.widget.DrawableWidget;
import com.gtnewhorizons.modularui.common.widget.SlotGroup;
import com.gtnewhorizons.modularui.common.widget.SlotWidget;
import com.gtnewhorizons.modularui.common.widget.TextWidget;

import gregtech.api.gui.modularui.GT_UITextures;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.modularui.IAddUIWidgets;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_Buffer;
import gregtech.api.render.TextureFactory;
import gregtech.api.util.GT_Utility;

public class GT_MetaTileEntity_Regulator extends GT_MetaTileEntity_Buffer implements IAddUIWidgets {

    public int[] mTargetSlots = { 0, 0, 0, 0, 0, 0, 0, 0, 0 };
    private boolean charge = false, decharge = false;

    public GT_MetaTileEntity_Regulator(int aID, String aName, String aNameRegional, int aTier) {
        super(
                aID,
                aName,
                aNameRegional,
                aTier,
                20,
                new String[] { "Filters up to 9 different Items", "Allows Item-specific output stack size",
                        "Allows Item-specific output slot", "Does not consume energy to move Item" });
    }

    public GT_MetaTileEntity_Regulator(String aName, int aTier, int aInvSlotCount, String aDescription,
            ITexture[][][] aTextures) {
        super(aName, aTier, aInvSlotCount, aDescription, aTextures);
    }

    public GT_MetaTileEntity_Regulator(String aName, int aTier, int aInvSlotCount, String[] aDescription,
            ITexture[][][] aTextures) {
        super(aName, aTier, aInvSlotCount, aDescription, aTextures);
    }

    @Override
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new GT_MetaTileEntity_Regulator(
                this.mName,
                this.mTier,
                this.mInventory.length,
                this.mDescriptionArray,
                this.mTextures);
    }

    @Override
    public ITexture getOverlayIcon() {
        return TextureFactory.of(
                TextureFactory.of(AUTOMATION_REGULATOR),
                TextureFactory.builder()
                              .addIcon(AUTOMATION_REGULATOR_GLOW)
                              .glow()
                              .build());
    }

    @Override
    public boolean isValidSlot(int aIndex) {
        return aIndex < 9 || aIndex == rechargerSlotStartIndex();
    }

    @Override
    public void saveNBTData(NBTTagCompound aNBT) {
        super.saveNBTData(aNBT);
        aNBT.setInteger("mTargetSlot1", this.mTargetSlots[0]);
        aNBT.setInteger("mTargetSlot2", this.mTargetSlots[1]);
        aNBT.setInteger("mTargetSlot3", this.mTargetSlots[2]);
        aNBT.setInteger("mTargetSlot4", this.mTargetSlots[3]);
        aNBT.setInteger("mTargetSlot5", this.mTargetSlots[4]);
        aNBT.setInteger("mTargetSlot6", this.mTargetSlots[5]);
        aNBT.setInteger("mTargetSlot7", this.mTargetSlots[6]);
        aNBT.setInteger("mTargetSlot8", this.mTargetSlots[7]);
        aNBT.setInteger("mTargetSlot9", this.mTargetSlots[8]);
    }

    @Override
    public void loadNBTData(NBTTagCompound aNBT) {
        super.loadNBTData(aNBT);
        this.mTargetSlots[0] = aNBT.getInteger("mTargetSlot1");
        this.mTargetSlots[1] = aNBT.getInteger("mTargetSlot2");
        this.mTargetSlots[2] = aNBT.getInteger("mTargetSlot3");
        this.mTargetSlots[3] = aNBT.getInteger("mTargetSlot4");
        this.mTargetSlots[4] = aNBT.getInteger("mTargetSlot5");
        this.mTargetSlots[5] = aNBT.getInteger("mTargetSlot6");
        this.mTargetSlots[6] = aNBT.getInteger("mTargetSlot7");
        this.mTargetSlots[7] = aNBT.getInteger("mTargetSlot8");
        this.mTargetSlots[8] = aNBT.getInteger("mTargetSlot9");
    }

    @Override
    public void onScrewdriverRightClick(byte aSide, EntityPlayer aPlayer, float aX, float aY, float aZ) {
        // Regulation per Screwdriver is overridden by GUI regulation.
    }

    @Override
    public void moveItems(IGregTechTileEntity aBaseMetaTileEntity, long aTimer) {
        for (int i = 0, tCosts; i < 9; i++) {
            if (this.mInventory[(i + 9)] != null) {
                tCosts = GT_Utility.moveOneItemStackIntoSlot(
                        getBaseMetaTileEntity(),
                        getBaseMetaTileEntity().getTileEntityAtSide(getBaseMetaTileEntity().getBackFacing()),
                        getBaseMetaTileEntity().getBackFacing(),
                        this.mTargetSlots[i],
                        Collections.singletonList(this.mInventory[(i + 9)]),
                        false,
                        (byte) this.mInventory[(i + 9)].stackSize,
                        (byte) this.mInventory[(i + 9)].stackSize,
                        (byte) 64,
                        (byte) 1) * 3;
                if (tCosts > 0) {
                    this.mSuccess = 50;
                    break;
                }
            }
        }
    }

    @Override
    public boolean allowPutStack(IGregTechTileEntity aBaseMetaTileEntity, int aIndex, byte aSide, ItemStack aStack) {
        return super.allowPutStack(aBaseMetaTileEntity, aIndex, aSide, aStack) && aIndex >= 0
                && aIndex <= 8
                && GT_Utility.areStacksEqual(aStack, this.mInventory[(aIndex + 9)]);
    }

    @Override
    public int rechargerSlotStartIndex() {
        return 19;
    }

    @Override
    public int dechargerSlotStartIndex() {
        return 19;
    }

    @Override
    public int rechargerSlotCount() {
        return charge ? 1 : 0;
    }

    @Override
    public int dechargerSlotCount() {
        return decharge ? 1 : 0;
    }

    @Override
    public void onPostTick(IGregTechTileEntity aBaseMetaTileEntity, long aTick) {
        super.onPostTick(aBaseMetaTileEntity, aTick);
        if (aBaseMetaTileEntity.isServerSide()) {
            charge = aBaseMetaTileEntity.getStoredEU() / 2 > aBaseMetaTileEntity.getEUCapacity() / 3;
            decharge = aBaseMetaTileEntity.getStoredEU() < aBaseMetaTileEntity.getEUCapacity() / 3;
        }
    }

    @Override
    public void addUIWidgets(ModularWindow.Builder builder, UIBuildContext buildContext) {
        addEmitEnergyButton(builder);
        builder.widget(createChargerSlot(43, 62));
        builder.widget(
                new DrawableWidget().setDrawable(GT_UITextures.PICTURE_ARROW_22_RED.apply(84, true))
                                    .setPos(65, 60)
                                    .setSize(84, 22))
               .widget(
                       SlotGroup.ofItemHandler(inventoryHandler, 3)
                                .startFromSlot(0)
                                .endAtSlot(8)
                                .build()
                                .setPos(7, 5))
               .widget(
                       new DrawableWidget().setDrawable(GT_UITextures.PICTURE_SLOTS_HOLO_3BY3)
                                           .setPos(62, 5)
                                           .setSize(54, 54))
               .widget(
                       SlotGroup.ofItemHandler(inventoryHandler, 3)
                                .phantom(true)
                                .startFromSlot(9)
                                .endAtSlot(17)
                                .applyForWidget(
                                        widget -> widget.setControlsAmount(true)
                                                        .setBackground(GT_UITextures.TRANSPARENT))
                                .build()
                                .setPos(62, 5))
               .widget(
                       new DrawableWidget().setDrawable(GT_UITextures.PICTURE_SLOTS_HOLO_3BY3)
                                           .setPos(117, 5)
                                           .setSize(54, 54));

        int xBase = 117, yBase = 5;
        for (int i = 0; i < mTargetSlots.length; i++) {
            final int index = i;
            int xPos = xBase + (i % 3) * 18, yPos = yBase + (i / 3) * 18;
            builder.widget(new SlotWidget(BaseSlot.empty()) {

                @Override
                protected void phantomClick(ClickData clickData, ItemStack cursorStack) {
                    mTargetSlots[index] = Math.min(
                            99,
                            Math.max(
                                    0,
                                    mTargetSlots[index]
                                            + (clickData.mouseButton == 0 ? -1 : 1) * (clickData.shift ? 16 : 1)));
                }
            }.setBackground(GT_UITextures.TRANSPARENT)
             .setPos(xPos, yPos))
                   .widget(
                           TextWidget.dynamicString(() -> String.valueOf(mTargetSlots[index]))
                                     .setDefaultColor(COLOR_TEXT_WHITE.get())
                                     .setPos(xPos + 2 + (i % 3 == 0 ? 1 : 0), yPos + 3 + (i / 3 == 0 ? 1 : 0)));
        }
    }
}
