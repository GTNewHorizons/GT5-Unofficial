package gregtech.api.metatileentity.implementations;

import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_AUTOMAINTENANCE;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_AUTOMAINTENANCE_GLOW;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_AUTOMAINTENANCE_IDLE;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_AUTOMAINTENANCE_IDLE_GLOW;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_DUCTTAPE;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_MAINTENANCE;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.common.util.ForgeDirection;

import com.gtnewhorizon.structurelib.StructureLibAPI;
import com.gtnewhorizon.structurelib.alignment.IAlignment;
import com.gtnewhorizon.structurelib.alignment.IAlignmentLimits;
import com.gtnewhorizon.structurelib.alignment.IAlignmentProvider;
import com.gtnewhorizon.structurelib.alignment.enumerable.ExtendedFacing;
import com.gtnewhorizon.structurelib.alignment.enumerable.Flip;
import com.gtnewhorizon.structurelib.alignment.enumerable.Rotation;
import com.gtnewhorizons.modularui.api.screen.ModularWindow;
import com.gtnewhorizons.modularui.api.screen.UIBuildContext;
import com.gtnewhorizons.modularui.common.internal.wrapper.BaseSlot;
import com.gtnewhorizons.modularui.common.widget.DrawableWidget;
import com.gtnewhorizons.modularui.common.widget.SlotWidget;
import com.gtnewhorizons.modularui.common.widget.TextWidget;

import cpw.mods.fml.common.network.NetworkRegistry;
import gregtech.GT_Mod;
import gregtech.api.GregTech_API;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.Materials;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.gui.modularui.GT_UIInfos;
import gregtech.api.gui.modularui.GT_UITextures;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.modularui.IAddUIWidgets;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.MetaTileEntity;
import gregtech.api.render.TextureFactory;
import gregtech.api.util.GT_ModHandler;
import gregtech.api.util.GT_OreDictUnificator;
import gregtech.api.util.GT_Utility;
import ic2.core.IHasGui;
import ic2.core.item.ItemToolbox;

public class GT_MetaTileEntity_Hatch_Maintenance extends GT_MetaTileEntity_Hatch implements IAddUIWidgets, IAlignment {

    private Rotation rotation = Rotation.NORMAL;

    private static ItemStack[] sAutoMaintenanceInputs;
    public boolean mWrench = false, mScrewdriver = false, mSoftHammer = false, mHardHammer = false,
        mSolderingTool = false, mCrowbar = false, mAuto;

    public GT_MetaTileEntity_Hatch_Maintenance(int aID, String aName, String aNameRegional, int aTier) {
        super(aID, aName, aNameRegional, aTier, 1, "For maintaining Multiblocks");
        mAuto = false;
    }

    public GT_MetaTileEntity_Hatch_Maintenance(int aID, String aName, String aNameRegional, int aTier, boolean aAuto) {
        super(aID, aName, aNameRegional, aTier, 4, "For automatically maintaining Multiblocks");
        mAuto = aAuto;
    }

    public GT_MetaTileEntity_Hatch_Maintenance(String aName, int aTier, String aDescription, ITexture[][][] aTextures,
        boolean aAuto) {
        super(aName, aTier, aAuto ? 4 : 1, aDescription, aTextures);
        mAuto = aAuto;
    }

    public GT_MetaTileEntity_Hatch_Maintenance(String aName, int aTier, String[] aDescription, ITexture[][][] aTextures,
        boolean aAuto) {
        super(aName, aTier, aAuto ? 4 : 1, aDescription, aTextures);
        mAuto = aAuto;
    }

    private static ItemStack[] getAutoMaintenanceInputs() {
        if (sAutoMaintenanceInputs == null) sAutoMaintenanceInputs = new ItemStack[] { ItemList.Duct_Tape.get(4),
            GT_OreDictUnificator.get(OrePrefixes.cell, Materials.Lubricant, 2),
            GT_OreDictUnificator.get(OrePrefixes.screw, Materials.Steel, 4),
            GT_OreDictUnificator.get(OrePrefixes.circuit, Materials.HV, 2) };
        return sAutoMaintenanceInputs;
    }

    @Override
    public String[] getDescription() {
        String[] desc;
        if (mAuto) {
            desc = new String[mDescriptionArray.length + 3];
            System.arraycopy(mDescriptionArray, 0, desc, 0, mDescriptionArray.length);
            desc[mDescriptionArray.length] = "4 Ducttape, 2 Lubricant Cells";
            desc[mDescriptionArray.length + 1] = "4 Steel Screws, 2 HV Circuits";
            desc[mDescriptionArray.length + 2] = "For each autorepair";
        } else {
            desc = new String[mDescriptionArray.length + 1];
            System.arraycopy(mDescriptionArray, 0, desc, 0, mDescriptionArray.length);
            desc[mDescriptionArray.length] = "Cannot be shared between Multiblocks!";
        }
        return desc;
    }

    @Override
    public ITexture[] getTexturesActive(ITexture aBaseTexture) {
        if (mAuto) return new ITexture[] { aBaseTexture, TextureFactory.builder()
            .addIcon(OVERLAY_AUTOMAINTENANCE_IDLE)
            .extFacing()
            .build(),
            TextureFactory.builder()
                .addIcon(OVERLAY_AUTOMAINTENANCE_IDLE_GLOW)
                .extFacing()
                .glow()
                .build() };
        return new ITexture[] { aBaseTexture, TextureFactory.builder()
            .addIcon(OVERLAY_MAINTENANCE)
            .extFacing()
            .build() };
    }

    @Override
    public ITexture[] getTexturesInactive(ITexture aBaseTexture) {
        if (mAuto) return new ITexture[] { aBaseTexture, TextureFactory.builder()
            .addIcon(OVERLAY_AUTOMAINTENANCE)
            .extFacing()
            .build(),
            TextureFactory.builder()
                .addIcon(OVERLAY_AUTOMAINTENANCE_GLOW)
                .extFacing()
                .glow()
                .build() };
        return new ITexture[] { aBaseTexture, TextureFactory.builder()
            .addIcon(OVERLAY_MAINTENANCE)
            .extFacing()
            .build(),
            TextureFactory.builder()
                .addIcon(OVERLAY_DUCTTAPE)
                .extFacing()
                .build() };
    }

    @Override
    public void initDefaultModes(NBTTagCompound aNBT) {
        getBaseMetaTileEntity().setActive(true);
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
        return mAuto && GT_Mod.gregtechproxy.mAMHInteraction;
    }

    @Override
    public MetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        if (aTileEntity.getMetaTileID() == 111)
            return new GT_MetaTileEntity_Hatch_Maintenance(mName, mTier, mDescriptionArray, mTextures, true);
        return new GT_MetaTileEntity_Hatch_Maintenance(mName, mTier, mDescriptionArray, mTextures, false);
    }

    @Override
    public boolean onRightclick(IGregTechTileEntity aBaseMetaTileEntity, EntityPlayer aPlayer, ForgeDirection side,
        float aX, float aY, float aZ) {
        if (aBaseMetaTileEntity.isClientSide()) return true;
        if (side == aBaseMetaTileEntity.getFrontFacing()) {
            // only allow OC robot fake player
            if (aPlayer instanceof FakePlayer && !aPlayer.getGameProfile()
                .getName()
                .endsWith(".robot")) return false;
            ItemStack tStack = aPlayer.getCurrentEquippedItem();
            if (tStack != null) {
                if (tStack.getItem() instanceof ItemToolbox) {
                    applyToolbox(tStack, aPlayer);
                } else if (ItemList.Duct_Tape.isStackEqual(tStack)) {
                    mWrench = mScrewdriver = mSoftHammer = mHardHammer = mCrowbar = mSolderingTool = true;
                    getBaseMetaTileEntity().setActive(false);
                    if (--tStack.stackSize == 0) {
                        aPlayer.inventory.mainInventory[aPlayer.inventory.currentItem] = null;
                    }
                } else GT_UIInfos.openGTTileEntityUI(aBaseMetaTileEntity, aPlayer);
            } else {
                GT_UIInfos.openGTTileEntityUI(aBaseMetaTileEntity, aPlayer);
            }
            return true;
        }
        return false;
    }

    public void updateSlots() {
        for (int i = 0; i < mInventory.length; i++)
            if (mInventory[i] != null && mInventory[i].stackSize <= 0) mInventory[i] = null;
    }

    @Override
    public void onFirstTick(IGregTechTileEntity aBaseMetaTileEntity) {
        super.onFirstTick(aBaseMetaTileEntity);
        if (aBaseMetaTileEntity.isClientSide())
            StructureLibAPI.queryAlignment((IAlignmentProvider) aBaseMetaTileEntity);
    }

    @Override
    public void onPostTick(IGregTechTileEntity aBaseMetaTileEntity, long aTick) {
        super.onPostTick(aBaseMetaTileEntity, aTick);
        if (aBaseMetaTileEntity.isServerSide() && mAuto && aTick % 100L == 0L) {
            aBaseMetaTileEntity.setActive(!isRecipeInputEqual(false));
        }
    }

    @Override
    public boolean onWrenchRightClick(ForgeDirection side, ForgeDirection wrenchingSide, EntityPlayer entityPlayer,
        float aX, float aY, float aZ) {
        if (wrenchingSide != getBaseMetaTileEntity().getFrontFacing())
            return super.onWrenchRightClick(side, wrenchingSide, entityPlayer, aX, aY, aZ);
        if (!entityPlayer.isSneaking() && isRotationChangeAllowed()) {
            toolSetRotation(null);
            return true;
        }
        return false;
    }

    public boolean autoMaintainance() {
        return isRecipeInputEqual(true);
    }

    public boolean isRecipeInputEqual(boolean aDecreaseStacksizeBySuccess) {
        ItemStack[] mInputs = getAutoMaintenanceInputs();

        int amt;

        for (ItemStack tStack : mInputs) {
            if (tStack != null) {
                amt = tStack.stackSize;
                boolean temp = true;
                for (ItemStack aStack : mInventory) {
                    if ((GT_Utility.areUnificationsEqual(aStack, tStack, true)
                        || GT_Utility.areUnificationsEqual(GT_OreDictUnificator.get(false, aStack), tStack, true))) {
                        amt -= aStack.stackSize;
                        if (amt < 1) {
                            temp = false;
                            break;
                        }
                    }
                }
                if (temp) return false;
            }
        }

        if (aDecreaseStacksizeBySuccess) {
            for (ItemStack tStack : mInputs) {
                if (tStack != null) {
                    amt = tStack.stackSize;
                    for (ItemStack aStack : mInventory) {
                        if ((GT_Utility.areUnificationsEqual(aStack, tStack, true) || GT_Utility
                            .areUnificationsEqual(GT_OreDictUnificator.get(false, aStack), tStack, true))) {
                            if (aStack.stackSize < amt) {
                                amt -= aStack.stackSize;
                                aStack.stackSize = 0;
                            } else {
                                aStack.stackSize -= amt;
                                amt = 0;
                                break;
                            }
                        }
                    }
                }
            }
            mCrowbar = true;
            mHardHammer = true;
            mScrewdriver = true;
            mSoftHammer = true;
            mSolderingTool = true;
            mWrench = true;
            updateSlots();
        }
        return true;
    }

    public void onToolClick(ItemStack aStack, EntityLivingBase aPlayer, IInventory aToolboxInventory) {
        if (aStack == null || aPlayer == null) return;

        // Allow IC2 Toolbox with tools to function for maint issues.
        if (aStack.getItem() instanceof ItemToolbox && aPlayer instanceof EntityPlayer) {
            applyToolbox(aStack, (EntityPlayer) aPlayer);
            return;
        }

        if (GT_Utility.isStackInList(aStack, GregTech_API.sWrenchList) && !mWrench
            && GT_ModHandler.damageOrDechargeItem(aStack, 1, 1000, aPlayer)) mWrench = true;
        if (GT_Utility.isStackInList(aStack, GregTech_API.sScrewdriverList) && !mScrewdriver
            && GT_ModHandler.damageOrDechargeItem(aStack, 1, 1000, aPlayer)) mScrewdriver = true;
        if (GT_Utility.isStackInList(aStack, GregTech_API.sSoftHammerList) && !mSoftHammer
            && GT_ModHandler.damageOrDechargeItem(aStack, 1, 1000, aPlayer)) mSoftHammer = true;
        if (GT_Utility.isStackInList(aStack, GregTech_API.sHardHammerList) && !mHardHammer
            && GT_ModHandler.damageOrDechargeItem(aStack, 1, 1000, aPlayer)) mHardHammer = true;
        if (GT_Utility.isStackInList(aStack, GregTech_API.sCrowbarList) && !mCrowbar
            && GT_ModHandler.damageOrDechargeItem(aStack, 1, 1000, aPlayer)) mCrowbar = true;
        if (!mSolderingTool && GT_ModHandler.useSolderingIron(aStack, aPlayer, aToolboxInventory))
            mSolderingTool = true;
        if (GT_OreDictUnificator.isItemStackInstanceOf(aStack, "craftingDuctTape")) {
            mWrench = mScrewdriver = mSoftHammer = mHardHammer = mCrowbar = mSolderingTool = true;
            getBaseMetaTileEntity().setActive(false);
            aStack.stackSize--;
        }
        if (mSolderingTool && aPlayer instanceof EntityPlayerMP tPlayer) {
            try {
                GT_Mod.achievements.issueAchievement(tPlayer, "maintainance");
            } catch (Exception ignored) {}
        }
    }

    public void onToolClick(ItemStack aStack, EntityLivingBase aPlayer) {
        onToolClick(aStack, aPlayer, null);
    }

    private void applyToolbox(ItemStack aStack, EntityPlayer aPlayer) {
        final ItemToolbox aToolbox = (ItemToolbox) aStack.getItem();
        final IHasGui aToolboxGUI = aToolbox.getInventory(aPlayer, aStack);
        for (int i = 0; i < aToolboxGUI.getSizeInventory(); i++) {
            if (aToolboxGUI.getStackInSlot(i) != null) {
                onToolClick(aToolboxGUI.getStackInSlot(i), aPlayer, aToolboxGUI);
                if (aToolboxGUI.getStackInSlot(i) != null && aToolboxGUI.getStackInSlot(i).stackSize <= 0)
                    aToolboxGUI.setInventorySlotContents(i, null);
            }
        }
    }

    @Override
    public boolean allowPullStack(IGregTechTileEntity aBaseMetaTileEntity, int aIndex, ForgeDirection side,
        ItemStack aStack) {
        return mAuto && GT_Mod.gregtechproxy.mAMHInteraction;
    }

    @Override
    public boolean allowPutStack(IGregTechTileEntity aBaseMetaTileEntity, int aIndex, ForgeDirection side,
        ItemStack aStack) {
        if (mAuto && GT_Mod.gregtechproxy.mAMHInteraction) {
            for (int i = 0; i < getSizeInventory(); i++) if (GT_Utility.areStacksEqual(
                GT_OreDictUnificator.get(false, aStack),
                GT_OreDictUnificator.get(false, getStackInSlot(i)))) return i == aIndex;
            for (ItemStack tInput : getAutoMaintenanceInputs())
                if (GT_Utility.areUnificationsEqual(tInput, aStack, true)
                    || GT_Utility.areUnificationsEqual(GT_OreDictUnificator.get(false, aStack), tInput, true))
                    return true;
        }
        return false;
    }

    @Override
    public void addUIWidgets(ModularWindow.Builder builder, UIBuildContext buildContext) {
        if (mAuto) {
            getBaseMetaTileEntity().add2by2Slots(builder);
        } else {
            builder.widget(
                new DrawableWidget().setDrawable(GT_UITextures.SLOT_MAINTENANCE)
                    .setPos(78, 33)
                    .setSize(20, 20))
                .widget(new SlotWidget(BaseSlot.empty()) {

                    @Override
                    public boolean handleDragAndDrop(ItemStack draggedStack, int button) {
                        return false;
                    }

                    @Override
                    protected void phantomClick(ClickData clickData, ItemStack cursorStack) {
                        if (cursorStack == null) return;
                        onToolClick(cursorStack, getContext().getPlayer());
                        if (cursorStack.stackSize < 1) {
                            getContext().getPlayer().inventory.setItemStack(null);
                        }
                        if (getContext().getPlayer() instanceof EntityPlayerMP) {
                            ((EntityPlayerMP) getContext().getPlayer()).updateHeldItem();
                        }
                    }
                }.disableShiftInsert()
                    .setBackground(GT_UITextures.TRANSPARENT)
                    .setPos(79, 34))
                .widget(
                    new TextWidget("Click with Tool to repair.").setDefaultColor(COLOR_TEXT_GRAY.get())
                        .setPos(8, 12));
        }
    }

    @Override
    public void saveNBTData(NBTTagCompound aNBT) {
        super.saveNBTData(aNBT);
        aNBT.setByte("mRotation", (byte) rotation.getIndex());
    }

    @Override
    public void loadNBTData(NBTTagCompound aNBT) {
        rotation = Rotation.byIndex(aNBT.getByte("mRotation"));
        super.loadNBTData(aNBT);
    }

    @Override
    public ExtendedFacing getExtendedFacing() {
        return ExtendedFacing.of(getBaseMetaTileEntity().getFrontFacing(), rotation, Flip.NONE);
    }

    @Override
    public void setExtendedFacing(ExtendedFacing alignment) {
        boolean changed = false;
        final IGregTechTileEntity base = getBaseMetaTileEntity();
        if (base.getFrontFacing() != alignment.getDirection()) {
            base.setFrontFacing(alignment.getDirection());
            changed = true;
        }
        if (rotation != alignment.getRotation()) {
            rotation = alignment.getRotation();
            changed = true;
        }
        if (changed) {
            if (base.isServerSide() && !GregTech_API.isDummyWorld(base.getWorld())) {
                StructureLibAPI.sendAlignment(
                    (IAlignmentProvider) base,
                    new NetworkRegistry.TargetPoint(
                        base.getWorld().provider.dimensionId,
                        base.getXCoord(),
                        base.getYCoord(),
                        base.getZCoord(),
                        512));
            } else {
                base.issueTextureUpdate();
            }
        }
    }

    @Override
    public IAlignmentLimits getAlignmentLimits() {
        return (d, r, f) -> f.isNotFlipped();
    }

    @Override
    public boolean isFlipChangeAllowed() {
        return false;
    }

    @Override
    public boolean isRotationChangeAllowed() {
        return true;
    }
}
