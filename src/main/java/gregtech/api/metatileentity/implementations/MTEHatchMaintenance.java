package gregtech.api.metatileentity.implementations;

import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_AUTOMAINTENANCE;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_AUTOMAINTENANCE_GLOW;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_AUTOMAINTENANCE_IDLE;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_AUTOMAINTENANCE_IDLE_GLOW;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_DUCTTAPE;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_MAINTENANCE;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.cleanroommc.modularui.factory.PosGuiData;
import com.cleanroommc.modularui.screen.ModularPanel;
import com.cleanroommc.modularui.screen.UISettings;
import com.cleanroommc.modularui.value.sync.PanelSyncManager;
import gregtech.common.gui.modularui.hatch.MTEHatchMaintenanceGui;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StatCollector;
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
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import gregtech.GTMod;
import gregtech.api.GregTechAPI;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.Materials;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.enums.SoundResource;
import gregtech.api.gui.modularui.GTUITextures;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.modularui.IAddUIWidgets;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.MetaTileEntity;
import gregtech.api.render.TextureFactory;
import gregtech.api.util.GTModHandler;
import gregtech.api.util.GTOreDictUnificator;
import gregtech.api.util.GTUtility;
import ic2.core.IHasGui;
import ic2.core.item.ItemToolbox;
import org.jetbrains.annotations.NotNull;
import thaumic.tinkerer.common.block.tile.tablet.TabletFakePlayer;

public class MTEHatchMaintenance extends MTEHatch implements IAddUIWidgets, IAlignment {

    private Rotation rotation = Rotation.NORMAL;

    private static ItemStack[] sAutoMaintenanceInputs;

    protected String mMaintenanceSound = null;
    protected float mMaintenanceSoundStrength = 1.0F;
    protected float mMaintenanceSoundModulation = 1.0F;
    private final List<MTEMultiBlockBase> multiblocks = new ArrayList<>();

    public List<MTEMultiBlockBase> getMultiBlocks() {
        return multiblocks;
    }

    public void addMultiblock(MTEMultiBlockBase controller) {
            multiblocks.add(controller);
    }

    public void setMultiblocks(@NotNull List<MTEMultiBlockBase> controllers) {
        multiblocks.clear();
        controllers.stream()
            .filter(c -> c != null && c.mMachine)
            .forEach(multiblocks::add);
    }


    public boolean mWrench = false, mScrewdriver = false, mSoftMallet = false, mHardHammer = false,
        mSolderingTool = false, mCrowbar = false, mAuto;

    public MTEHatchMaintenance(int aID, String aName, String aNameRegional, int aTier) {
        super(aID, aName, aNameRegional, aTier, 1, "For maintaining multiblocks");
        mAuto = false;
    }

    public MTEHatchMaintenance(int aID, String aName, String aNameRegional, int aTier, boolean aAuto) {
        super(aID, aName, aNameRegional, aTier, 4, "For automatically maintaining multiblocks");
        mAuto = aAuto;
    }

    public MTEHatchMaintenance(String aName, int aTier, String[] aDescription, ITexture[][][] aTextures,
        boolean aAuto) {
        super(aName, aTier, aAuto ? 4 : 1, aDescription, aTextures);
        mAuto = aAuto;
    }

    private static ItemStack[] getAutoMaintenanceInputs() {
        if (sAutoMaintenanceInputs == null) sAutoMaintenanceInputs = new ItemStack[] { ItemList.Duct_Tape.get(4),
            GTOreDictUnificator.get(OrePrefixes.cell, Materials.Lubricant, 2),
            GTOreDictUnificator.get(OrePrefixes.screw, Materials.Steel, 4),
            GTOreDictUnificator.get(OrePrefixes.circuit, Materials.HV, 2) };
        return sAutoMaintenanceInputs;
    }

    @Override
    public String[] getDescription() {
        String[] desc;
        if (mAuto) {
            desc = new String[mDescriptionArray.length + 3];
            System.arraycopy(mDescriptionArray, 0, desc, 0, mDescriptionArray.length);
            desc[mDescriptionArray.length] = "Consumes 4 Duct Tape, 2 Lubricant Cells,";
            desc[mDescriptionArray.length + 1] = "4 Steel Screws, and 2 HV Circuits";
            desc[mDescriptionArray.length + 2] = "for each autorepair.";
        } else {
            desc = new String[mDescriptionArray.length + 1];
            System.arraycopy(mDescriptionArray, 0, desc, 0, mDescriptionArray.length);
            desc[mDescriptionArray.length] = "Use tools to fix issues.";
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
    public boolean isFacingValid(ForgeDirection facing) {
        return true;
    }

    @Override
    public boolean isValidSlot(int aIndex) {
        return mAuto && GTMod.proxy.mAMHInteraction;
    }

    @Override
    public MetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        if (aTileEntity.getMetaTileID() == 111)
            return new MTEHatchMaintenance(mName, mTier, mDescriptionArray, mTextures, true);
        return new MTEHatchMaintenance(mName, mTier, mDescriptionArray, mTextures, false);
    }

    @Override
    public boolean onRightclick(IGregTechTileEntity aBaseMetaTileEntity, EntityPlayer aPlayer, ForgeDirection side,
        float aX, float aY, float aZ) {
        if (side == aBaseMetaTileEntity.getFrontFacing()) {
            if (aBaseMetaTileEntity.isClientSide()) return true;
            // only allow OC robot & dynamism tablet fake player
            if (aPlayer instanceof FakePlayer) {
                if (!(aPlayer instanceof TabletFakePlayer) && !aPlayer.getGameProfile()
                    .getName()
                    .endsWith(".robot")) {
                    return false;
                }
            }
            ItemStack tStack = aPlayer.getCurrentEquippedItem();
            if (tStack != null) {
                if (tStack.getItem() instanceof ItemToolbox) {
                    applyToolbox(tStack, aPlayer);
                } else if (GTOreDictUnificator.isItemStackInstanceOf(tStack, "craftingDuctTape")) {
                    applyDuctTape();
                    if (--tStack.stackSize == 0) {
                        aPlayer.inventory.mainInventory[aPlayer.inventory.currentItem] = null;
                    }
                } else openGui(aPlayer);
            } else {
                openGui(aPlayer);
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
    public NBTTagCompound getDescriptionData() {
        NBTTagCompound data = super.getDescriptionData();
        if (data == null) data = new NBTTagCompound();
        data.setByte("mRotation", (byte) rotation.getIndex());
        return data;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void onDescriptionPacket(NBTTagCompound data) {
        super.onDescriptionPacket(data);
        rotation = Rotation.byIndex(data.getByte("mRotation"));
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
        float aX, float aY, float aZ, ItemStack aTool) {
        if (wrenchingSide == getBaseMetaTileEntity().getFrontFacing() && !entityPlayer.isSneaking()
            && isRotationChangeAllowed()) {
            toolSetRotation(null);
            return true;
        }
        return super.onWrenchRightClick(side, wrenchingSide, entityPlayer, aX, aY, aZ, aTool);
    }

    public void onMaintenancePerformed(MTEMultiBlockBase aMaintenanceTarget) {
        IGregTechTileEntity tMte = getBaseMetaTileEntity();

        if (tMte == null || tMte.isMuffled()) return;

        if (mMaintenanceSound == null) {
            setMaintenanceSound(SoundResource.GT_MAINTENANCE_TOOLBOX, 1.0F, 1.0F);
        }

        GTUtility.sendSoundToPlayers(
            tMte.getWorld(),
            mMaintenanceSound,
            mMaintenanceSoundStrength,
            mMaintenanceSoundModulation,
            tMte.getXCoord() + .5,
            tMte.getYCoord() + .5,
            tMte.getZCoord() + .5);

        setMaintenanceSound((String) null, 1.0F, 1.0F);
    }

    public boolean autoMaintainance() {
        return isRecipeInputEqual(true);
    }

    /**
     * Sets the sound resource to use in the next maintenance performed by this hatch.
     *
     * @param aSound           The {@code SoundResource} to play.
     * @param aSoundStrength   The loudness of the sound.
     * @param aSoundModulation The pitch of the sound. From 0 to 2, 1 being the default pitch.
     */
    public void setMaintenanceSound(@Nonnull SoundResource aSound, float aSoundStrength, float aSoundModulation) {
        setMaintenanceSound(aSound.resourceLocation, aSoundStrength, aSoundModulation);
    }

    /**
     * Sets the resource location of a sound to use in the next maintenance performed by this hatch.
     * Useful for playing sounds not present in {@code gregtech.api.enums.SoundResource}.
     *
     * @param aSound           The {@code ResourceLocation} of the sound to play.
     * @param aSoundStrength   The loudness of the sound.
     * @param aSoundModulation The pitch of the sound. From 0 to 2, 1 being the default pitch.
     */
    public void setMaintenanceSound(@Nonnull ResourceLocation aSound, float aSoundStrength, float aSoundModulation) {
        setMaintenanceSound(aSound.toString(), aSoundStrength, aSoundModulation);
    }

    /**
     * Sets the name of the sound resource to use in the next maintenance performed by this hatch.
     * If possible, prefer using the other overloads of this method:
     * {@link #setMaintenanceSound(SoundResource, float, float)}, if the sound has an entry in
     * {@code gregtech.api.enums.SoundResource}, or {@link #setMaintenanceSound(ResourceLocation, float, float)}
     * otherwise. (Such as calling a sound not from GT5U)
     *
     * @param aSoundName       The name of the sound to play. If null will default to the Toolbox sound.
     * @param aSoundStrength   The loudness of the sound.
     * @param aSoundModulation The pitch of the sound. From 0 to 2, 1 being the default pitch.
     */
    public void setMaintenanceSound(@Nullable String aSoundName, float aSoundStrength, float aSoundModulation) {
        mMaintenanceSound = aSoundName;
        mMaintenanceSoundStrength = aSoundStrength;
        mMaintenanceSoundModulation = aSoundModulation;
    }

    public boolean isRecipeInputEqual(boolean aDecreaseStacksizeBySuccess) {
        ItemStack[] mInputs = getAutoMaintenanceInputs();

        int amt;

        for (ItemStack tStack : mInputs) {
            if (tStack != null) {
                amt = tStack.stackSize;
                boolean temp = true;
                for (ItemStack aStack : mInventory) {
                    if ((GTUtility.areUnificationsEqual(aStack, tStack, true)
                        || GTUtility.areUnificationsEqual(GTOreDictUnificator.get(false, aStack), tStack, true))) {
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
                        if ((GTUtility.areUnificationsEqual(aStack, tStack, true)
                            || GTUtility.areUnificationsEqual(GTOreDictUnificator.get(false, aStack), tStack, true))) {
                            if (aStack.stackSize < amt) {
                                amt -= aStack.stackSize;
                                aStack.stackSize = 0;
                            } else {
                                aStack.stackSize -= amt;
                                break;
                            }
                        }
                    }
                }
            }
            setMaintenanceSound(SoundResource.GT_MAINTENANCE_AUTO_HATCH, 1.0F, 1.0F);
            mCrowbar = true;
            mHardHammer = true;
            mScrewdriver = true;
            mSoftMallet = true;
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

        if (GTUtility.isStackInList(aStack, GregTechAPI.sWrenchList) && !mWrench
            && GTModHandler.damageOrDechargeItem(aStack, 1, 1000, aPlayer)) {
            mWrench = true;
            setMaintenanceSound(SoundResource.GTCEU_OP_WRENCH, 1.0F, 1.0F);
        }
        if (GTUtility.isStackInList(aStack, GregTechAPI.sScrewdriverList) && !mScrewdriver
            && GTModHandler.damageOrDechargeItem(aStack, 1, 1000, aPlayer)) {
            mScrewdriver = true;
            setMaintenanceSound(SoundResource.GTCEU_OP_SCREWDRIVER, 1.0F, 1.0F);
        }
        if (GTUtility.isStackInList(aStack, GregTechAPI.sSoftMalletList) && !mSoftMallet
            && GTModHandler.damageOrDechargeItem(aStack, 1, 1000, aPlayer)) {
            mSoftMallet = true;
            setMaintenanceSound(SoundResource.GTCEU_OP_SOFT_HAMMER, 1.0F, 1.0F);
        }
        if (GTUtility.isStackInList(aStack, GregTechAPI.sHardHammerList) && !mHardHammer
            && GTModHandler.damageOrDechargeItem(aStack, 1, 1000, aPlayer)) {
            mHardHammer = true;
            setMaintenanceSound(SoundResource.GTCEU_LOOP_FORGE_HAMMER, 1.0F, 1.0F);
        }
        if (GTUtility.isStackInList(aStack, GregTechAPI.sCrowbarList) && !mCrowbar
            && GTModHandler.damageOrDechargeItem(aStack, 1, 1000, aPlayer)) {
            mCrowbar = true;
            setMaintenanceSound(SoundResource.RANDOM_BREAK, 1.0F, -1.0F);
        }
        if (!mSolderingTool && GTModHandler.useSolderingIron(aStack, aPlayer, aToolboxInventory)) {
            mSolderingTool = true;
            setMaintenanceSound(SoundResource.IC2_TOOLS_BATTERY_USE, 3.0F, -1.0F);
        }
        if (GTOreDictUnificator.isItemStackInstanceOf(aStack, "craftingDuctTape")) {
            applyDuctTape();
            aStack.stackSize--;
        }
        if (mSolderingTool && aPlayer instanceof EntityPlayerMP tPlayer) {
            try {
                GTMod.achievements.issueAchievement(tPlayer, "maintainance");
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
        setMaintenanceSound(SoundResource.GT_MAINTENANCE_TOOLBOX, 1.0F, 1.0F);
    }

    private void applyDuctTape() {
        mWrench = mScrewdriver = mSoftMallet = mHardHammer = mCrowbar = mSolderingTool = true;
        setMaintenanceSound(SoundResource.GT_MAINTENANCE_DUCT_TAPE, 1.0F, 1.0F);
        getBaseMetaTileEntity().setActive(false);
    }

    @Override
    public boolean allowPullStack(IGregTechTileEntity aBaseMetaTileEntity, int aIndex, ForgeDirection side,
        ItemStack aStack) {
        return mAuto && GTMod.proxy.mAMHInteraction;
    }

    @Override
    public boolean allowPutStack(IGregTechTileEntity aBaseMetaTileEntity, int aIndex, ForgeDirection side,
        ItemStack aStack) {
        if (mAuto && GTMod.proxy.mAMHInteraction) {
            for (int i = 0; i < getSizeInventory(); i++) if (GTUtility.areStacksEqual(
                GTOreDictUnificator.get(false, aStack),
                GTOreDictUnificator.get(false, getStackInSlot(i)))) return i == aIndex;
            for (ItemStack tInput : getAutoMaintenanceInputs()) if (GTUtility.areUnificationsEqual(tInput, aStack, true)
                || GTUtility.areUnificationsEqual(GTOreDictUnificator.get(false, aStack), tInput, true)) return true;
        }
        return false;
    }

    @Override
    public void addUIWidgets(ModularWindow.Builder builder, UIBuildContext buildContext) {
        if (mAuto) {
            getBaseMetaTileEntity().add2by2Slots(builder);
        } else {
            builder.widget(
                new DrawableWidget().setDrawable(GTUITextures.SLOT_MAINTENANCE)
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
                    .setBackground(GTUITextures.TRANSPARENT)
                    .setPos(79, 34))
                .widget(
                    new TextWidget(StatCollector.translateToLocal("GT5U.gui.text.repair_tip"))
                        .setDefaultColor(COLOR_TEXT_GRAY.get())
                        .setPos(8, 12))
                .widget(
                    new DrawableWidget().setDrawable(GTUITextures.PICTURE_INFORMATION)
                        .addTooltips(
                            Arrays.asList(
                                GTUtility.breakLines(StatCollector.translateToLocal("GT5U.gui.text.repair_info"))))
                        .setPos(163, 5)
                        .setSize(7, 18));
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
            if (base.isServerSide() && !GregTechAPI.isDummyWorld(base.getWorld())) {
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
    public ModularPanel buildUI(PosGuiData data, PanelSyncManager syncManager, UISettings uiSettings) {
        return new MTEHatchMaintenanceGui(this).build(data, syncManager, uiSettings);
    }

    @Override
    protected boolean useMui2() {
        return true;
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
