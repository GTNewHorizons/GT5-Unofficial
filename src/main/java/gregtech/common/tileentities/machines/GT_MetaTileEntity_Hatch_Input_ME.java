package gregtech.common.tileentities.machines;

import static gregtech.api.enums.GT_Values.TIER_COLORS;
import static gregtech.api.enums.GT_Values.VN;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_ME_INPUT_FLUID_HATCH;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_ME_INPUT_FLUID_HATCH_ACTIVE;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.EnumSet;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import javax.annotation.Nullable;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.FluidStack;

import com.gtnewhorizons.modularui.api.ModularUITextures;
import com.gtnewhorizons.modularui.api.drawable.IDrawable;
import com.gtnewhorizons.modularui.api.drawable.Text;
import com.gtnewhorizons.modularui.api.math.Alignment;
import com.gtnewhorizons.modularui.api.math.Color;
import com.gtnewhorizons.modularui.api.math.Pos2d;
import com.gtnewhorizons.modularui.api.math.Size;
import com.gtnewhorizons.modularui.api.screen.ModularWindow;
import com.gtnewhorizons.modularui.api.screen.UIBuildContext;
import com.gtnewhorizons.modularui.api.widget.Interactable;
import com.gtnewhorizons.modularui.common.fluid.FluidStackTank;
import com.gtnewhorizons.modularui.common.widget.ButtonWidget;
import com.gtnewhorizons.modularui.common.widget.DrawableWidget;
import com.gtnewhorizons.modularui.common.widget.FakeSyncWidget;
import com.gtnewhorizons.modularui.common.widget.FluidSlotWidget;
import com.gtnewhorizons.modularui.common.widget.SlotGroup;
import com.gtnewhorizons.modularui.common.widget.TextWidget;
import com.gtnewhorizons.modularui.common.widget.textfield.NumericWidget;

import appeng.api.config.Actionable;
import appeng.api.config.PowerMultiplier;
import appeng.api.implementations.IPowerChannelState;
import appeng.api.networking.GridFlags;
import appeng.api.networking.security.BaseActionSource;
import appeng.api.networking.security.IActionHost;
import appeng.api.networking.security.MachineSource;
import appeng.api.storage.IMEMonitor;
import appeng.api.storage.data.IAEFluidStack;
import appeng.api.util.AECableType;
import appeng.core.localization.WailaText;
import appeng.me.GridAccessException;
import appeng.me.helpers.AENetworkProxy;
import appeng.me.helpers.IGridProxyable;
import appeng.util.item.AEFluidStack;
import gregtech.api.enums.ItemList;
import gregtech.api.gui.modularui.GT_UITextures;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.modularui.IAddGregtechLogo;
import gregtech.api.interfaces.modularui.IAddUIWidgets;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.MetaTileEntity;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_Hatch_Input;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_MultiBlockBase;
import gregtech.api.recipe.check.CheckRecipeResult;
import gregtech.api.recipe.check.CheckRecipeResultRegistry;
import gregtech.api.recipe.check.SimpleCheckRecipeResult;
import gregtech.api.render.TextureFactory;
import gregtech.api.util.GT_Utility;
import gregtech.api.util.shutdown.ShutDownReasonRegistry;
import mcp.mobius.waila.api.IWailaConfigHandler;
import mcp.mobius.waila.api.IWailaDataAccessor;

public class GT_MetaTileEntity_Hatch_Input_ME extends GT_MetaTileEntity_Hatch_Input
    implements IPowerChannelState, IAddGregtechLogo, IAddUIWidgets, IRecipeProcessingAwareHatch {

    private static final int SLOT_COUNT = 16;

    protected final FluidStack[] storedFluids = new FluidStack[SLOT_COUNT];
    protected final FluidStack[] storedInformationFluids = new FluidStack[SLOT_COUNT];

    // these two fields should ALWAYS be mutated simultaneously
    // in most cases, you should call setSavedFluid() instead of trying to write to the array directly
    // a desync of these two fields can lead to catastrophe
    protected final FluidStack[] shadowStoredFluids = new FluidStack[SLOT_COUNT];
    private final int[] savedStackSizes = new int[SLOT_COUNT];

    private boolean additionalConnection = false;

    protected BaseActionSource requestSource = null;

    @Nullable
    protected AENetworkProxy gridProxy = null;

    private final boolean autoPullAvailable;
    protected boolean autoPullFluidList = false;
    protected int minAutoPullAmount = 1;
    private int autoPullRefreshTime = 100;
    protected boolean processingRecipe = false;

    protected static final int CONFIG_WINDOW_ID = 10;

    protected static final FluidStack[] EMPTY_FLUID_STACK = new FluidStack[0];

    public GT_MetaTileEntity_Hatch_Input_ME(int aID, boolean autoPullAvailable, String aName, String aNameRegional) {
        super(aID, aName, aNameRegional, autoPullAvailable ? 10 : 8, 1, getDescriptionArray(autoPullAvailable));
        this.autoPullAvailable = autoPullAvailable;
    }

    public GT_MetaTileEntity_Hatch_Input_ME(String aName, boolean autoPullAvailable, int aTier, String[] aDescription,
        ITexture[][][] aTextures) {
        super(aName, 1, aTier, aDescription, aTextures);
        this.autoPullAvailable = autoPullAvailable;
    }

    @Override
    public MetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new GT_MetaTileEntity_Hatch_Input_ME(mName, autoPullAvailable, mTier, mDescriptionArray, mTextures);
    }

    @Override
    public ITexture[] getTexturesActive(ITexture aBaseTexture) {
        return new ITexture[] { aBaseTexture, TextureFactory.of(OVERLAY_ME_INPUT_FLUID_HATCH_ACTIVE) };
    }

    @Override
    public ITexture[] getTexturesInactive(ITexture aBaseTexture) {
        return new ITexture[] { aBaseTexture, TextureFactory.of(OVERLAY_ME_INPUT_FLUID_HATCH) };
    }

    @Override
    public void onPostTick(IGregTechTileEntity aBaseMetaTileEntity, long aTimer) {
        if (getBaseMetaTileEntity().isServerSide()) {
            if (aTimer % autoPullRefreshTime == 0 && autoPullFluidList) {
                refreshFluidList();
            }
            if (aTimer % 20 == 0) {
                getBaseMetaTileEntity().setActive(isActive());
            }
        }
        super.onPostTick(aBaseMetaTileEntity, aTimer);
    }

    private void refreshFluidList() {
        AENetworkProxy proxy = getProxy();
        if (proxy == null || !proxy.isActive()) {
            return;
        }

        try {
            IMEMonitor<IAEFluidStack> sg = proxy.getStorage()
                .getFluidInventory();
            Iterator<IAEFluidStack> iterator = sg.getStorageList()
                .iterator();

            int index = 0;
            while (iterator.hasNext() && index < SLOT_COUNT) {
                IAEFluidStack currItem = iterator.next();
                if (currItem.getStackSize() >= minAutoPullAmount) {
                    FluidStack fluidStack = GT_Utility.copyAmount(1, currItem.getFluidStack());
                    storedFluids[index] = fluidStack;
                    index++;
                }
            }

            for (int i = index; i < SLOT_COUNT; i++) {
                storedFluids[i] = null;
            }
        } catch (final GridAccessException ignored) {}
    }

    @Override
    public boolean displaysStackSize() {
        return true;
    }

    protected void setSavedFluid(int i, FluidStack stack) {
        shadowStoredFluids[i] = stack;
        savedStackSizes[i] = stack == null ? 0 : stack.amount;
    }

    public FluidStack[] getStoredFluids() {
        if (!processingRecipe) {
            return EMPTY_FLUID_STACK;
        }

        AENetworkProxy proxy = getProxy();
        if (proxy == null || !proxy.isActive()) {
            return EMPTY_FLUID_STACK;
        }

        updateAllInformationSlots();

        for (int i = 0; i < SLOT_COUNT; i++) {
            if (storedFluids[i] == null) {
                setSavedFluid(i, null);
                continue;
            }

            FluidStack fluidStackWithAmount = storedInformationFluids[i];

            setSavedFluid(i, fluidStackWithAmount);
        }

        return shadowStoredFluids;
    }

    @Override
    public FluidStack drain(ForgeDirection side, FluidStack aFluid, boolean doDrain) {
        // this is an ME input hatch. allowing draining via logistics would be very wrong (and against
        // canTankBeEmptied()) but we do need to support draining from controller, which uses the UNKNOWN direction.
        if (side != ForgeDirection.UNKNOWN) return null;
        FluidStack stored = getMatchingFluidStack(aFluid);
        if (stored == null) return null;
        FluidStack drained = GT_Utility.copyAmount(Math.min(stored.amount, aFluid.amount), stored);
        if (doDrain) {
            stored.amount -= drained.amount;
        }
        return drained;
    }

    @Override
    public void startRecipeProcessing() {
        processingRecipe = true;
        updateAllInformationSlots();
    }

    @Override
    public CheckRecipeResult endRecipeProcessing(GT_MetaTileEntity_MultiBlockBase controller) {
        CheckRecipeResult checkRecipeResult = CheckRecipeResultRegistry.SUCCESSFUL;
        AENetworkProxy proxy = getProxy();

        for (int i = 0; i < SLOT_COUNT; ++i) {
            FluidStack oldStack = shadowStoredFluids[i];
            int toExtract = savedStackSizes[i] - (oldStack != null ? oldStack.amount : 0);
            if (toExtract <= 0) continue;

            try {
                IMEMonitor<IAEFluidStack> sg = proxy.getStorage()
                    .getFluidInventory();

                IAEFluidStack request = AEFluidStack.create(storedFluids[i]);
                request.setStackSize(toExtract);
                IAEFluidStack extractionResult = sg.extractItems(request, Actionable.MODULATE, getRequestSource());
                proxy.getEnergy()
                    .extractAEPower(toExtract, Actionable.MODULATE, PowerMultiplier.CONFIG);

                if (extractionResult == null || extractionResult.getStackSize() != toExtract) {
                    controller.stopMachine(ShutDownReasonRegistry.CRITICAL_NONE);
                    checkRecipeResult = SimpleCheckRecipeResult
                        .ofFailurePersistOnShutdown("stocking_hatch_fail_extraction");
                }
            } catch (GridAccessException ignored) {
                controller.stopMachine(ShutDownReasonRegistry.CRITICAL_NONE);
                checkRecipeResult = SimpleCheckRecipeResult
                    .ofFailurePersistOnShutdown("stocking_hatch_fail_extraction");
            }
            setSavedFluid(i, null);
            if (storedInformationFluids[i] != null && storedInformationFluids[i].amount <= 0) {
                storedInformationFluids[i] = null;
            }
        }

        processingRecipe = false;
        return checkRecipeResult;
    }

    @Override
    public void onFirstTick(IGregTechTileEntity aBaseMetaTileEntity) {
        super.onFirstTick(aBaseMetaTileEntity);
        getProxy().onReady();
    }

    @Override
    public AECableType getCableConnectionType(ForgeDirection forgeDirection) {
        return isOutputFacing(forgeDirection) ? AECableType.SMART : AECableType.NONE;
    }

    private void updateValidGridProxySides() {
        if (additionalConnection) {
            getProxy().setValidSides(EnumSet.complementOf(EnumSet.of(ForgeDirection.UNKNOWN)));
        } else {
            getProxy().setValidSides(EnumSet.of(getBaseMetaTileEntity().getFrontFacing()));
        }
    }

    @Override
    public void onFacingChange() {
        updateValidGridProxySides();
    }

    @Override
    public boolean onWireCutterRightClick(ForgeDirection side, ForgeDirection wrenchingSide, EntityPlayer aPlayer,
        float aX, float aY, float aZ) {
        additionalConnection = !additionalConnection;
        updateValidGridProxySides();
        aPlayer.addChatComponentMessage(
            new ChatComponentTranslation("GT5U.hatch.additionalConnection." + additionalConnection));
        return true;
    }

    @Override
    public AENetworkProxy getProxy() {
        if (gridProxy == null) {
            if (getBaseMetaTileEntity() instanceof IGridProxyable) {
                gridProxy = new AENetworkProxy(
                    (IGridProxyable) getBaseMetaTileEntity(),
                    "proxy",
                    autoPullAvailable ? ItemList.Hatch_Input_ME_Advanced.get(1) : ItemList.Hatch_Input_ME.get(1),
                    true);
                gridProxy.setFlags(GridFlags.REQUIRE_CHANNEL);
                updateValidGridProxySides();
                if (getBaseMetaTileEntity().getWorld() != null) gridProxy.setOwner(
                    getBaseMetaTileEntity().getWorld()
                        .getPlayerEntityByName(getBaseMetaTileEntity().getOwnerName()));
            }
        }
        return this.gridProxy;
    }

    @Override
    public boolean isPowered() {
        return getProxy() != null && getProxy().isPowered();
    }

    @Override
    public boolean isActive() {
        return getProxy() != null && getProxy().isActive();
    }

    private void setAutoPullFluidList(boolean pullFluidList) {
        if (!autoPullAvailable) {
            return;
        }

        autoPullFluidList = pullFluidList;
        if (!autoPullFluidList) {
            Arrays.fill(storedFluids, null);
        } else {
            refreshFluidList();
        }
        updateAllInformationSlots();
    }

    private void updateAllInformationSlots() {
        for (int index = 0; index < SLOT_COUNT; index++) {
            updateInformationSlot(index);
        }
    }

    public void updateInformationSlot(int index) {
        if (index < 0 || index >= SLOT_COUNT) {
            return;
        }

        FluidStack fluidStack = storedFluids[index];
        if (fluidStack == null) {
            storedInformationFluids[index] = null;
            return;
        }

        AENetworkProxy proxy = getProxy();
        if (proxy == null || !proxy.isActive()) {
            storedInformationFluids[index] = null;
            return;
        }

        try {
            IMEMonitor<IAEFluidStack> sg = proxy.getStorage()
                .getFluidInventory();
            IAEFluidStack request = AEFluidStack.create(fluidStack);
            request.setStackSize(Integer.MAX_VALUE);
            IAEFluidStack result = sg.extractItems(request, Actionable.SIMULATE, getRequestSource());
            FluidStack resultFluid = (result != null) ? result.getFluidStack() : null;
            storedInformationFluids[index] = resultFluid;
        } catch (final GridAccessException ignored) {}
    }

    private BaseActionSource getRequestSource() {
        if (requestSource == null) requestSource = new MachineSource((IActionHost) getBaseMetaTileEntity());
        return requestSource;
    }

    public FluidStack getMatchingFluidStack(FluidStack fluidStack) {
        if (fluidStack == null) return null;

        AENetworkProxy proxy = getProxy();
        if (proxy == null || !proxy.isActive()) {
            return null;
        }

        for (int i = 0; i < storedFluids.length; i++) {
            if (storedFluids[i] == null) {
                continue;
            }

            if (GT_Utility.areFluidsEqual(fluidStack, storedFluids[i], false)) {
                updateInformationSlot(i);
                if (storedInformationFluids[i] != null) {
                    setSavedFluid(i, storedInformationFluids[i]);
                    return shadowStoredFluids[i];
                }

                setSavedFluid(i, null);
                return null;
            }
        }
        return null;
    }

    /**
     * Used to avoid slot update.
     */
    public FluidStack getShadowFluidStack(int index) {
        if (index < 0 || index >= storedFluids.length) {
            return null;
        }

        return shadowStoredFluids[index];
    }

    public int getFluidSlot(FluidStack fluidStack) {
        if (fluidStack == null) return -1;

        for (int i = 0; i < storedFluids.length; i++) {
            if (storedFluids[i] == null) {
                continue;
            }

            if (GT_Utility.areFluidsEqual(fluidStack, storedFluids[i], false)) {
                return i;
            }
        }

        return -1;
    }

    @Override
    public boolean canTankBeEmptied() {
        return false;
    }

    @Override
    public boolean canTankBeFilled() {
        return false;
    }

    @Override
    public boolean doesEmptyContainers() {
        return false;
    }

    @Override
    public boolean isValidSlot(int aIndex) {
        return false;
    }

    @Override
    public void saveNBTData(NBTTagCompound aNBT) {
        super.saveNBTData(aNBT);

        NBTTagList nbtTagList = new NBTTagList();
        for (int i = 0; i < SLOT_COUNT; i++) {
            FluidStack fluidStack = storedFluids[i];
            if (fluidStack == null) {
                continue;
            }
            NBTTagCompound fluidTag = fluidStack.writeToNBT(new NBTTagCompound());
            if (storedInformationFluids[i] != null)
                fluidTag.setInteger("informationAmount", storedInformationFluids[i].amount);
            nbtTagList.appendTag(fluidTag);
        }

        aNBT.setTag("storedFluids", nbtTagList);
        aNBT.setBoolean("autoPull", autoPullFluidList);
        aNBT.setInteger("minAmount", minAutoPullAmount);
        aNBT.setBoolean("additionalConnection", additionalConnection);
        aNBT.setInteger("refreshTime", autoPullRefreshTime);
        getProxy().writeToNBT(aNBT);
    }

    @Override
    public void loadNBTData(NBTTagCompound aNBT) {
        super.loadNBTData(aNBT);
        if (aNBT.hasKey("storedFluids")) {
            NBTTagList nbtTagList = aNBT.getTagList("storedFluids", 10);
            int c = Math.min(nbtTagList.tagCount(), SLOT_COUNT);
            for (int i = 0; i < c; i++) {
                NBTTagCompound nbtTagCompound = nbtTagList.getCompoundTagAt(i);
                FluidStack fluidStack = GT_Utility.loadFluid(nbtTagCompound);
                storedFluids[i] = fluidStack;

                if (nbtTagCompound.hasKey("informationAmount")) {
                    int informationAmount = nbtTagCompound.getInteger("informationAmount");
                    storedInformationFluids[i] = GT_Utility.copyAmount(informationAmount, fluidStack);
                }
            }
        }

        minAutoPullAmount = aNBT.getInteger("minAmount");
        autoPullFluidList = aNBT.getBoolean("autoPull");
        additionalConnection = aNBT.getBoolean("additionalConnection");
        if (aNBT.hasKey("refreshTime")) {
            autoPullRefreshTime = aNBT.getInteger("refreshTime");
        }
        getProxy().readFromNBT(aNBT);
    }

    @Override
    public void onScrewdriverRightClick(ForgeDirection side, EntityPlayer aPlayer, float aX, float aY, float aZ) {
        if (!autoPullAvailable) {
            return;
        }

        setAutoPullFluidList(!autoPullFluidList);
        aPlayer.addChatMessage(
            new ChatComponentTranslation(
                "GT5U.machines.stocking_hatch.auto_pull_toggle." + (autoPullFluidList ? "enabled" : "disabled")));
    }

    @Override
    public boolean onRightclick(IGregTechTileEntity aBaseMetaTileEntity, EntityPlayer aPlayer, ForgeDirection side,
        float aX, float aY, float aZ) {
        if (!(aPlayer instanceof EntityPlayerMP))
            return super.onRightclick(aBaseMetaTileEntity, aPlayer, side, aX, aY, aZ);
        ItemStack dataStick = aPlayer.inventory.getCurrentItem();
        if (!ItemList.Tool_DataStick.isStackEqual(dataStick, false, true))
            return super.onRightclick(aBaseMetaTileEntity, aPlayer, side, aX, aY, aZ);
        if (!dataStick.hasTagCompound() || !"stockingHatch".equals(dataStick.stackTagCompound.getString("type")))
            return false;

        NBTTagCompound nbt = dataStick.stackTagCompound;

        if (autoPullAvailable) {
            setAutoPullFluidList(nbt.getBoolean("autoPull"));
            minAutoPullAmount = nbt.getInteger("minAmount");
            autoPullRefreshTime = nbt.getInteger("refreshTime");
        }
        additionalConnection = nbt.getBoolean("additionalConnection");

        if (!autoPullFluidList) {
            NBTTagList stockingFluids = nbt.getTagList("fluidsToStock", 10);
            for (int i = 0; i < stockingFluids.tagCount(); i++) {
                storedFluids[i] = GT_Utility.loadFluid(stockingFluids.getCompoundTagAt(i));
            }
        }

        updateValidGridProxySides();
        aPlayer.addChatMessage(new ChatComponentTranslation("GT5U.machines.stocking_bus.loaded"));
        return true;
    }

    @Override
    public void onLeftclick(IGregTechTileEntity aBaseMetaTileEntity, EntityPlayer aPlayer) {
        if (!(aPlayer instanceof EntityPlayerMP)) return;

        ItemStack dataStick = aPlayer.inventory.getCurrentItem();
        if (!ItemList.Tool_DataStick.isStackEqual(dataStick, false, true)) return;

        NBTTagCompound tag = new NBTTagCompound();
        tag.setString("type", "stockingHatch");
        tag.setBoolean("autoPull", autoPullFluidList);
        tag.setInteger("minAmount", minAutoPullAmount);
        tag.setBoolean("additionalConnection", additionalConnection);
        tag.setInteger("refreshTime", autoPullRefreshTime);

        NBTTagList stockingFluids = new NBTTagList();
        if (!autoPullFluidList) {
            for (int index = 0; index < SLOT_COUNT; index++) {
                FluidStack fluidStack = storedFluids[index];
                if (fluidStack == null) {
                    continue;
                }
                stockingFluids.appendTag(fluidStack.writeToNBT(new NBTTagCompound()));
            }
            tag.setTag("fluidsToStock", stockingFluids);
        }
        dataStick.stackTagCompound = tag;
        dataStick.setStackDisplayName("Stocking Input Hatch Configuration");
        aPlayer.addChatMessage(new ChatComponentTranslation("GT5U.machines.stocking_bus.saved"));
    }

    @Override
    public void onExplosion() {
        for (int i = 0; i < SLOT_COUNT; i++) {
            mInventory[i] = null;
        }
    }

    public boolean containsSuchStack(FluidStack tStack) {
        for (int i = 0; i < 16; ++i) {
            if (GT_Utility.areFluidsEqual(storedFluids[i], tStack, false)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public int getGUIHeight() {
        return 179;
    }

    @Override
    public void addUIWidgets(ModularWindow.Builder builder, UIBuildContext buildContext) {
        if (autoPullAvailable) {
            buildContext.addSyncedWindow(CONFIG_WINDOW_ID, this::createStackSizeConfigurationWindow);
        }

        builder.widget(
            SlotGroup.ofFluidTanks(
                IntStream.range(0, SLOT_COUNT)
                    .mapToObj(index -> createTankForFluidStack(storedFluids, index, 1))
                    .collect(Collectors.toList()),
                4)
                .phantom(true)
                .widgetCreator((slotIndex, h) -> (FluidSlotWidget) new FluidSlotWidget(h) {

                    @Override
                    protected void tryClickPhantom(ClickData clickData, ItemStack cursorStack) {
                        if (clickData.mouseButton != 0 || autoPullFluidList) return;

                        FluidStack heldFluid = getFluidForPhantomItem(cursorStack);
                        if (cursorStack == null) {
                            storedFluids[slotIndex] = null;
                        } else {
                            if (containsSuchStack(heldFluid)) return;
                            storedFluids[slotIndex] = heldFluid;
                        }
                        if (getBaseMetaTileEntity().isServerSide()) {
                            updateInformationSlot(slotIndex);
                            detectAndSendChanges(false);
                        }
                    }

                    @Override
                    protected void tryScrollPhantom(int direction) {}

                    @Override
                    public IDrawable[] getBackground() {
                        IDrawable slot;
                        if (autoPullFluidList) {
                            slot = GT_UITextures.SLOT_DARK_GRAY;
                        } else {
                            slot = ModularUITextures.FLUID_SLOT;
                        }
                        return new IDrawable[] { slot, GT_UITextures.OVERLAY_SLOT_ARROW_ME };
                    }

                    @Override
                    public void buildTooltip(List<Text> tooltip) {
                        FluidStack fluid = getContent();
                        if (fluid != null) {
                            addFluidNameInfo(tooltip, fluid);

                            if (!autoPullFluidList) {
                                tooltip.add(Text.localised("modularui.phantom.single.clear"));
                            }
                        } else {
                            tooltip.add(
                                Text.localised("modularui.fluid.empty")
                                    .format(EnumChatFormatting.WHITE));
                        }

                        if (autoPullFluidList) {
                            tooltip.add(Text.localised("GT5U.machines.stocking_bus.cannot_set_slot"));
                        }
                    }
                }.setUpdateTooltipEveryTick(true))
                .build()
                .setPos(new Pos2d(7, 9)));

        builder.widget(
            SlotGroup.ofFluidTanks(
                IntStream.range(0, SLOT_COUNT)
                    .mapToObj(index -> createTankForFluidStack(storedInformationFluids, index, Integer.MAX_VALUE))
                    .collect(Collectors.toList()),
                4)
                .phantom(true)
                .widgetCreator((slotIndex, h) -> (FluidSlotWidget) new FluidSlotWidget(h) {

                    @Override
                    protected void tryClickPhantom(ClickData clickData, ItemStack cursorStack) {}

                    @Override
                    protected void tryScrollPhantom(int direction) {}

                    @Override
                    public void buildTooltip(List<Text> tooltip) {
                        FluidStack fluid = getContent();
                        if (fluid != null) {
                            addFluidNameInfo(tooltip, fluid);
                            tooltip.add(Text.localised("modularui.fluid.phantom.amount", fluid.amount));
                            addAdditionalFluidInfo(tooltip, fluid);
                            if (!Interactable.hasShiftDown()) {
                                tooltip.add(Text.EMPTY);
                                tooltip.add(Text.localised("modularui.tooltip.shift"));
                            }
                        } else {
                            tooltip.add(
                                Text.localised("modularui.fluid.empty")
                                    .format(EnumChatFormatting.WHITE));
                        }
                    }
                }.setUpdateTooltipEveryTick(true))
                .background(GT_UITextures.SLOT_DARK_GRAY)
                .controlsAmount(true)
                .build()
                .setPos(new Pos2d(97, 9)));

        if (autoPullAvailable) {
            builder.widget(new ButtonWidget().setOnClick((clickData, widget) -> {
                if (clickData.mouseButton == 0) {
                    setAutoPullFluidList(!autoPullFluidList);
                } else if (clickData.mouseButton == 1 && !widget.isClient()) {
                    widget.getContext()
                        .openSyncedWindow(CONFIG_WINDOW_ID);
                }
            })
                .setPlayClickSound(true)
                .setBackground(() -> {
                    if (autoPullFluidList) {
                        return new IDrawable[] { GT_UITextures.BUTTON_STANDARD_PRESSED,
                            GT_UITextures.OVERLAY_BUTTON_AUTOPULL_ME };
                    } else {
                        return new IDrawable[] { GT_UITextures.BUTTON_STANDARD,
                            GT_UITextures.OVERLAY_BUTTON_AUTOPULL_ME_DISABLED };
                    }
                })
                .addTooltips(
                    Arrays.asList(
                        StatCollector.translateToLocal("GT5U.machines.stocking_hatch.auto_pull.tooltip.1"),
                        StatCollector.translateToLocal("GT5U.machines.stocking_hatch.auto_pull.tooltip.2")))
                .setSize(16, 16)
                .setPos(80, 10))
                .widget(new FakeSyncWidget.BooleanSyncer(() -> autoPullFluidList, this::setAutoPullFluidList));
        }

        builder.widget(
            new DrawableWidget().setDrawable(GT_UITextures.PICTURE_ARROW_DOUBLE)
                .setPos(82, 30)
                .setSize(12, 12))
            .widget(TextWidget.dynamicString(() -> {
                boolean isActive = isActive();
                boolean isPowered = isPowered();
                boolean isBooting = isBooting();
                EnumChatFormatting color = (isActive && isPowered) ? EnumChatFormatting.GREEN
                    : EnumChatFormatting.DARK_RED;
                return color + WailaText.getPowerState(isActive, isPowered, isBooting);
            })
                .setTextAlignment(Alignment.Center)
                .setSize(90, 9)
                .setPos(43, 84));
    }

    private FluidStackTank createTankForFluidStack(FluidStack[] fluidStacks, int slotIndex, int capacity) {
        return new FluidStackTank(() -> fluidStacks[slotIndex], (stack) -> {
            if (getBaseMetaTileEntity().isServerSide()) {
                return;
            }

            fluidStacks[slotIndex] = stack;
        }, capacity);
    }

    protected ModularWindow createStackSizeConfigurationWindow(final EntityPlayer player) {
        final int WIDTH = 78;
        final int HEIGHT = 80;
        final int PARENT_WIDTH = getGUIWidth();
        final int PARENT_HEIGHT = getGUIHeight();
        ModularWindow.Builder builder = ModularWindow.builder(WIDTH, HEIGHT);
        builder.setBackground(GT_UITextures.BACKGROUND_SINGLEBLOCK_DEFAULT);
        builder.setGuiTint(getGUIColorization());
        builder.setDraggable(true);
        builder.setPos(
            (size, window) -> Alignment.Center.getAlignedPos(size, new Size(PARENT_WIDTH, PARENT_HEIGHT))
                .add(
                    Alignment.TopRight.getAlignedPos(new Size(PARENT_WIDTH, PARENT_HEIGHT), new Size(WIDTH, HEIGHT))
                        .add(WIDTH - 3, 0)));
        builder.widget(
            TextWidget.localised("GT5U.machines.stocking_hatch.min_amount")
                .setPos(3, 2)
                .setSize(74, 14))
            .widget(
                new NumericWidget().setSetter(val -> minAutoPullAmount = (int) val)
                    .setGetter(() -> minAutoPullAmount)
                    .setBounds(1, Integer.MAX_VALUE)
                    .setScrollValues(1, 4, 64)
                    .setTextAlignment(Alignment.Center)
                    .setTextColor(Color.WHITE.normal)
                    .setSize(70, 18)
                    .setPos(3, 18)
                    .setBackground(GT_UITextures.BACKGROUND_TEXT_FIELD));
        builder.widget(
            TextWidget.localised("GT5U.machines.stocking_bus.refresh_time")
                .setPos(3, 42)
                .setSize(74, 14))
            .widget(
                new NumericWidget().setSetter(val -> autoPullRefreshTime = (int) val)
                    .setGetter(() -> autoPullRefreshTime)
                    .setBounds(1, Integer.MAX_VALUE)
                    .setScrollValues(1, 4, 64)
                    .setTextAlignment(Alignment.Center)
                    .setTextColor(Color.WHITE.normal)
                    .setSize(70, 18)
                    .setPos(3, 58)
                    .setBackground(GT_UITextures.BACKGROUND_TEXT_FIELD));
        return builder.build();
    }

    @Override
    public void addGregTechLogo(ModularWindow.Builder builder) {
        builder.widget(
            new DrawableWidget().setDrawable(getGUITextureSet().getGregTechLogo())
                .setSize(17, 17)
                .setPos(80, 63));
    }

    @Override
    public void getWailaBody(ItemStack itemStack, List<String> currenttip, IWailaDataAccessor accessor,
        IWailaConfigHandler config) {
        if (!autoPullAvailable) {
            super.getWailaBody(itemStack, currenttip, accessor, config);
            return;
        }

        NBTTagCompound tag = accessor.getNBTData();
        boolean autopull = tag.getBoolean("autoPull");
        int minSize = tag.getInteger("minAmount");
        currenttip.add(
            StatCollector.translateToLocal("GT5U.waila.stocking_bus.auto_pull." + (autopull ? "enabled" : "disabled")));
        if (autopull) {
            currenttip.add(
                StatCollector.translateToLocalFormatted(
                    "GT5U.waila.stocking_hatch.min_amount",
                    GT_Utility.formatNumbers(minSize)));
        }
        super.getWailaBody(itemStack, currenttip, accessor, config);
    }

    @Override
    public void getWailaNBTData(EntityPlayerMP player, TileEntity tile, NBTTagCompound tag, World world, int x, int y,
        int z) {
        if (!autoPullAvailable) {
            super.getWailaNBTData(player, tile, tag, world, x, y, z);
            return;
        }

        tag.setBoolean("autoPull", autoPullFluidList);
        tag.setInteger("minAmount", minAutoPullAmount);
        super.getWailaNBTData(player, tile, tag, world, x, y, z);
    }

    private static String[] getDescriptionArray(boolean autoPullAvailable) {
        List<String> strings = new ArrayList<>(8);
        strings.add("Advanced fluid input for Multiblocks");
        strings.add("Hatch Tier: " + TIER_COLORS[autoPullAvailable ? 10 : 8] + VN[autoPullAvailable ? 10 : 8]);
        strings.add("Retrieves directly from ME");
        strings.add("Keeps 16 fluid types in stock");

        if (autoPullAvailable) {
            strings.add(
                "Auto-Pull from ME mode will automatically stock the first 16 fluid in the ME system, updated every 5 seconds.");
            strings.add("Toggle by right-clicking with screwdriver, or use the GUI.");
            strings
                .add("Use the GUI to limit the minimum stack size for Auto-Pulling and adjust the slot refresh timer.");
        }

        strings.add("Change ME connection behavior by right-clicking with wire cutter.");
        strings.add("Configuration data can be copy+pasted using a data stick.");
        return strings.toArray(new String[0]);
    }
}
