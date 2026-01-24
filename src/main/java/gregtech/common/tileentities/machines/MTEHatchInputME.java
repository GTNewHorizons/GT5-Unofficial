package gregtech.common.tileentities.machines;

import static com.gtnewhorizon.gtnhlib.util.numberformatting.NumberFormatUtil.formatNumber;
import static gregtech.api.enums.GTValues.TIER_COLORS;
import static gregtech.api.enums.GTValues.VN;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_ME_INPUT_FLUID_HATCH;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_ME_INPUT_FLUID_HATCH_ACTIVE;

import java.io.IOException;
import java.text.MessageFormat;
import java.util.Arrays;
import java.util.EnumSet;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import javax.annotation.Nullable;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.PacketBuffer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTankInfo;
import net.minecraftforge.fluids.IFluidTank;

import org.jetbrains.annotations.NotNull;

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
import com.gtnewhorizons.modularui.common.widget.ButtonWidget;
import com.gtnewhorizons.modularui.common.widget.CycleButtonWidget;
import com.gtnewhorizons.modularui.common.widget.DrawableWidget;
import com.gtnewhorizons.modularui.common.widget.FakeSyncWidget;
import com.gtnewhorizons.modularui.common.widget.FluidSlotWidget;
import com.gtnewhorizons.modularui.common.widget.SlotGroup;
import com.gtnewhorizons.modularui.common.widget.TextWidget;
import com.gtnewhorizons.modularui.common.widget.textfield.NumericWidget;

import appeng.api.config.Actionable;
import appeng.api.implementations.IPowerChannelState;
import appeng.api.networking.GridFlags;
import appeng.api.networking.energy.IEnergyGrid;
import appeng.api.networking.security.BaseActionSource;
import appeng.api.networking.security.IActionHost;
import appeng.api.networking.security.MachineSource;
import appeng.api.storage.IMEMonitor;
import appeng.api.storage.data.IAEFluidStack;
import appeng.api.util.AECableType;
import appeng.api.util.AEColor;
import appeng.core.localization.WailaText;
import appeng.me.GridAccessException;
import appeng.me.helpers.AENetworkProxy;
import appeng.me.helpers.IGridProxyable;
import appeng.util.Platform;
import appeng.util.item.AEFluidStack;
import gregtech.api.GregTechAPI;
import gregtech.api.enums.Dyes;
import gregtech.api.enums.GTValues;
import gregtech.api.enums.ItemList;
import gregtech.api.gui.modularui.GTUITextures;
import gregtech.api.interfaces.IDataCopyable;
import gregtech.api.interfaces.IMEConnectable;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.modularui.IAddGregtechLogo;
import gregtech.api.interfaces.modularui.IAddUIWidgets;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.MetaTileEntity;
import gregtech.api.metatileentity.implementations.MTEHatchInput;
import gregtech.api.metatileentity.implementations.MTEMultiBlockBase;
import gregtech.api.recipe.check.CheckRecipeResult;
import gregtech.api.recipe.check.CheckRecipeResultRegistry;
import gregtech.api.recipe.check.SimpleCheckRecipeResult;
import gregtech.api.render.TextureFactory;
import gregtech.api.util.GTDataUtils;
import gregtech.api.util.GTSplit;
import gregtech.api.util.GTUtility;
import gregtech.api.util.shutdown.ShutDownReasonRegistry;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import mcp.mobius.waila.api.IWailaConfigHandler;
import mcp.mobius.waila.api.IWailaDataAccessor;

@IMetaTileEntity.SkipGenerateDescription
public class MTEHatchInputME extends MTEHatchInput implements IPowerChannelState, IAddGregtechLogo, IAddUIWidgets,
    IRecipeProcessingAwareHatch, ISmartInputHatch, IDataCopyable, IMEConnectable {

    private static final int SLOT_COUNT = 16;
    public static final String COPIED_DATA_IDENTIFIER = "stockingHatch";

    protected final Slot[] slots = new Slot[SLOT_COUNT];

    private boolean additionalConnection = false;

    protected BaseActionSource requestSource = null;

    @Nullable
    protected AENetworkProxy gridProxy = null;

    private final boolean autoPullAvailable;
    protected boolean autoPullFluidList = false;
    protected int minAutoPullAmount = 1;
    private int autoPullRefreshTime = 100;
    protected boolean processingRecipe = false;
    private boolean justHadNewFluids = false;
    private boolean expediteRecipeCheck = false;
    /**
     * The cached activity for this hatch. Only valid while processing a recipe. This avoids several
     * operations.
     */
    protected boolean cachedActivity = false;

    protected static final int CONFIG_WINDOW_ID = 10;
    protected static final FluidTankInfo[] EMPTY_FLUID_TANK_INFOS = new FluidTankInfo[0];

    public MTEHatchInputME(int aID, boolean autoPullAvailable, String aName, String aNameRegional) {
        super(aID, 1, aName, aNameRegional, autoPullAvailable ? 9 : 8, null);
        this.autoPullAvailable = autoPullAvailable;
    }

    public MTEHatchInputME(String aName, boolean autoPullAvailable, int aTier, String[] aDescription,
        ITexture[][][] aTextures) {
        super(aName, 1, aTier, aDescription, aTextures);
        this.autoPullAvailable = autoPullAvailable;
    }

    @Override
    public MetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new MTEHatchInputME(mName, autoPullAvailable, mTier, mDescriptionArray, mTextures);
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
        if (aBaseMetaTileEntity.isServerSide()) {
            if (aTimer % autoPullRefreshTime == 0 && autoPullFluidList) {
                refreshFluidList();
            }
            if (aTimer % 20 == 0) {
                aBaseMetaTileEntity.setActive(isActive());
            }
        }
        super.onPostTick(aBaseMetaTileEntity, aTimer);
    }

    protected boolean isAllowedToWork() {
        if (processingRecipe) return cachedActivity;

        IGregTechTileEntity igte = getBaseMetaTileEntity();

        if (igte == null || !igte.isAllowedToWork()) return false;

        AENetworkProxy proxy = getProxy();

        if (!proxy.isActive()) return false;

        return true;
    }

    @Override
    public void onEnableWorking() {
        super.onEnableWorking();

        if (expediteRecipeCheck) {
            justHadNewFluids = true;
        }
    }

    @Override
    public void onDisableWorking() {
        super.onDisableWorking();

        if (autoPullFluidList) {
            clearSlotConfigs();
        } else {
            clearExtractedStacks();
        }
    }

    @Override
    public void onColorChangeServer(byte aColor) {
        updateAE2ProxyColor();
    }

    public void updateAE2ProxyColor() {
        AENetworkProxy proxy = getProxy();
        byte color = this.getColor();
        if (color == -1) {
            proxy.setColor(AEColor.Transparent);
        } else {
            proxy.setColor(AEColor.values()[Dyes.transformDyeIndex(color)]);
        }
        if (proxy.getNode() != null) {
            proxy.getNode()
                .updateState();
        }
    }

    private void refreshFluidList() {
        if (!isAllowedToWork()) return;

        IMEMonitor<IAEFluidStack> sg;
        Iterator<IAEFluidStack> iterator;

        try {
            sg = getProxy().getStorage()
                .getFluidInventory();
            iterator = sg.getStorageList()
                .iterator();
        } catch (final GridAccessException ignored) {
            return;
        }

        int index = 0;

        clearSlotConfigs();

        while (iterator.hasNext() && index < SLOT_COUNT) {
            IAEFluidStack curr = iterator.next();

            if (curr.getStackSize() < minAutoPullAmount) continue;

            Slot oldSlot = slots[index];

            // Prevent weird reference problems by copying the slot
            if (oldSlot != null) oldSlot = oldSlot.copy();

            setSlotConfig(index, GTUtility.copyAmount(1, curr.getFluidStack()));

            Slot newSlot = slots[index];

            if (newSlot != null) {
                newSlot.extracted = curr.getFluidStack();
                newSlot.extractedAmount = newSlot.extracted.amount;
            }

            if (!Objects.equals(oldSlot, newSlot)) {
                justHadNewFluids = true;
            }

            index++;
        }
    }

    public FluidStack[] getStoredFluids() {
        if (!isAllowedToWork()) {
            return new FluidStack[0];
        }

        if (processingRecipe) {
            List<FluidStack> fluids = new ObjectArrayList<>(GTDataUtils.countNonNulls(slots));

            for (Slot slot : slots) {
                if (slot == null) continue;

                // Must pass the reference out to the multi
                if (slot.extracted != null) fluids.add(slot.extracted);
            }

            return fluids.toArray(GTValues.emptyFluidStackArray);
        } else {
            List<FluidStack> fluids = new ObjectArrayList<>(GTDataUtils.countNonNulls(slots));

            for (Slot slot : slots) {
                if (slot == null) continue;

                // The caller should only use this to determine the configuration.
                // If it wants to know more, it can query AE itself.
                fluids.add(GTUtility.copyAmount(1, slot.config));
            }

            return fluids.toArray(GTValues.emptyFluidStackArray);
        }
    }

    @Override
    public FluidTankInfo[] getTankInfo(ForgeDirection side) {
        if (side != ForgeDirection.UNKNOWN || !isAllowedToWork()) {
            return EMPTY_FLUID_TANK_INFOS;
        }

        if (processingRecipe) {
            List<FluidTankInfo> tanks = new ObjectArrayList<>(GTDataUtils.countNonNulls(slots));

            for (Slot slot : slots) {
                if (slot == null) continue;

                if (slot.extracted != null) tanks.add(new FluidTankInfo(slot.extracted, Integer.MAX_VALUE));
            }

            return tanks.toArray(EMPTY_FLUID_TANK_INFOS);
        } else {
            IMEMonitor<IAEFluidStack> sg;

            try {
                sg = getProxy().getStorage()
                    .getFluidInventory();
            } catch (GridAccessException e) {
                return EMPTY_FLUID_TANK_INFOS;
            }

            List<FluidTankInfo> tanks = new ObjectArrayList<>(GTDataUtils.countNonNulls(slots));

            for (Slot slot : slots) {
                if (slot == null) continue;

                IAEFluidStack request = AEFluidStack.create(slot.config);
                request.setStackSize(Integer.MAX_VALUE);

                IAEFluidStack result = sg.extractItems(request, Actionable.SIMULATE, getRequestSource());

                if (result == null) continue;

                tanks.add(new FluidTankInfo(result.getFluidStack(), Integer.MAX_VALUE));
            }

            return tanks.toArray(EMPTY_FLUID_TANK_INFOS);
        }
    }

    @Override
    public boolean justUpdated() {
        if (expediteRecipeCheck && isAllowedToWork()) {
            boolean ret = justHadNewFluids;
            justHadNewFluids = false;
            return ret;
        }
        return false;
    }

    public void setRecipeCheck(boolean value) {
        expediteRecipeCheck = value;

        IGregTechTileEntity igte = getBaseMetaTileEntity();

        // Changing this field requires a structure check/update, so let's do that automatically
        if (igte.isServerSide()) {
            GregTechAPI.causeMachineUpdate(igte.getWorld(), igte.getXCoord(), igte.getYCoord(), igte.getZCoord());
        }
    }

    @Override
    public FluidStack drain(ForgeDirection side, FluidStack fluid, boolean doDrain) {
        // this is an ME input hatch. allowing draining via logistics would be very wrong (and against
        // canTankBeEmptied()) but we do need to support draining from controller, which uses the UNKNOWN direction.
        if (side != ForgeDirection.UNKNOWN) return null;

        if (processingRecipe) {
            // When processing a recipe, we just extract from the slot fake stacks
            Slot slot = getMatchingSlot(fluid, true);
            if (slot == null) return null;

            int toDrain = Math.min(slot.extracted.amount, fluid.amount);

            FluidStack drained = GTUtility.copyAmount(toDrain, slot.extracted);

            if (doDrain) {
                slot.extracted.amount -= toDrain;
            }

            return drained;
        } else {
            // Outside of processing a recipe, we need to extract everything manually
            Slot slot = getMatchingSlot(fluid, false);
            if (slot == null) return null;

            IAEFluidStack request = AEFluidStack.create(fluid);

            IMEMonitor<IAEFluidStack> sg;
            IEnergyGrid energy;

            try {
                AENetworkProxy proxy = getProxy();
                sg = proxy.getStorage()
                    .getFluidInventory();
                energy = proxy.getEnergy();
            } catch (GridAccessException e) {
                return null;
            }

            IAEFluidStack result = Platform.poweredExtraction(energy, sg, request, getRequestSource());

            return result == null ? null : result.getFluidStack();
        }
    }

    @Override
    public void startRecipeProcessing() {
        // Call isAllowedToWork before setting processingRecipe to true
        cachedActivity = isAllowedToWork();
        processingRecipe = true;
        updateAllInformationSlots();
    }

    @Override
    public CheckRecipeResult endRecipeProcessing(MTEMultiBlockBase controller) {
        CheckRecipeResult checkRecipeResult = CheckRecipeResultRegistry.SUCCESSFUL;

        IMEMonitor<IAEFluidStack> sg;
        IEnergyGrid energy;

        try {
            AENetworkProxy proxy = getProxy();

            // on some setup endRecipeProcessing() somehow runs before onFirstTick();
            // test world
            // https://discord.com/channels/181078474394566657/522098956491030558/1441490828760449124
            if (!proxy.isReady()) proxy.onReady();

            sg = proxy.getStorage()
                .getFluidInventory();
            energy = proxy.getEnergy();
        } catch (GridAccessException e) {
            controller.stopMachine(ShutDownReasonRegistry.CRITICAL_NONE);
            return SimpleCheckRecipeResult.ofFailurePersistOnShutdown("stocking_hatch_fail_extraction");
        }

        for (int i = 0; i < SLOT_COUNT; i++) {
            Slot slot = slots[i];

            if (slot == null || slot.extracted == null || slot.extractedAmount == 0) continue;

            int toExtract = slot.extractedAmount - slot.extracted.amount;

            if (toExtract <= 0) continue;

            // Reset the extracted amount to prevent double endRecipeProcessing calls from extracting twice, but keep
            // the extracted stack intact so that it looks nice.
            slot.extractedAmount = slot.extracted.amount;

            IAEFluidStack request = AEFluidStack.create(slot.extracted);
            request.setStackSize(toExtract);

            IAEFluidStack result = Platform.poweredExtraction(energy, sg, request, getRequestSource());

            if (result == null || result.getStackSize() != toExtract) {
                controller.stopMachine(ShutDownReasonRegistry.CRITICAL_NONE);
                checkRecipeResult = SimpleCheckRecipeResult
                    .ofFailurePersistOnShutdown("stocking_hatch_fail_extraction");
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
        float aX, float aY, float aZ, ItemStack aTool) {
        additionalConnection = !additionalConnection;
        updateValidGridProxySides();
        aPlayer.addChatComponentMessage(
            new ChatComponentTranslation("GT5U.hatch.additionalConnection." + additionalConnection));
        return true;
    }

    @Override
    public boolean connectsToAllSides() {
        return additionalConnection;
    }

    @Override
    public void setConnectsToAllSides(boolean connects) {
        additionalConnection = connects;
        updateValidGridProxySides();
    }

    @Override
    public @NotNull AENetworkProxy getProxy() {
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
        return getProxy().isPowered();
    }

    @Override
    public boolean isActive() {
        return getProxy().isActive();
    }

    private void setAutoPullFluidList(boolean pullFluidList) {
        if (!autoPullAvailable) {
            return;
        }

        if (autoPullFluidList != pullFluidList) {
            autoPullFluidList = pullFluidList;

            clearSlotConfigs();

            if (autoPullFluidList) {
                refreshFluidList();
            }
        }
    }

    public boolean doFastRecipeCheck() {
        return expediteRecipeCheck;
    }

    protected void updateAllInformationSlots() {
        if (isAllowedToWork()) {
            try {
                for (int index = 0; index < SLOT_COUNT; index++) {
                    updateInformationSlot(index);
                }
            } catch (GridAccessException e) {
                // :P
            }
        } else {
            clearExtractedStacks();
        }
    }

    public void setSlotConfig(int index, FluidStack config) {
        slots[index] = config == null ? null : new Slot(config.copy());
    }

    /**
     * Polls the AE network to update the available fluids for the given slot.
     */
    public void updateInformationSlot(int index) throws GridAccessException {
        Slot slot = GTDataUtils.getIndexSafe(slots, index);

        if (slot == null) return;

        if (!isAllowedToWork()) {
            slot.resetExtracted();
            return;
        }

        IMEMonitor<IAEFluidStack> sg = getProxy().getStorage()
            .getFluidInventory();

        IAEFluidStack request = AEFluidStack.create(slot.config);
        request.setStackSize(Integer.MAX_VALUE);

        IAEFluidStack result = sg.extractItems(request, Actionable.SIMULATE, getRequestSource());

        FluidStack previous = slot.extracted;

        slot.extracted = result != null ? result.getFluidStack() : null;
        slot.extractedAmount = slot.extracted == null ? 0 : slot.extracted.amount;

        // We want to track changes in any FluidStack to notify any connected controllers to make a recipe check early
        if (expediteRecipeCheck && slot.extracted != null) {
            justHadNewFluids = !GTUtility.areFluidsEqual(slot.extracted, previous);
        }
    }

    protected void clearSlotConfigs() {
        Arrays.fill(slots, null);
    }

    protected void clearExtractedStacks() {
        for (Slot slot : slots) {
            if (slot == null) continue;

            slot.resetExtracted();
        }
    }

    private BaseActionSource getRequestSource() {
        if (requestSource == null) requestSource = new MachineSource((IActionHost) getBaseMetaTileEntity());
        return requestSource;
    }

    protected Slot getMatchingSlot(FluidStack fluidStack, boolean requireExtracted) {
        if (fluidStack == null) return null;
        if (!isAllowedToWork()) return null;

        for (int i = 0; i < slots.length; i++) {
            Slot slot = slots[i];

            if (slot == null) continue;

            if (requireExtracted && (slot.extracted == null || slot.extractedAmount == 0)) continue;

            if (!GTUtility.areFluidsEqual(slot.config, fluidStack)) continue;

            return slot;
        }

        return null;
    }

    /**
     * Gets the first non-null extracted fluid stack.
     *
     * @return The first extracted fluid stack, or null if this doesn't exist.
     */
    public FluidStack getFirstValidStack() {
        return getFirstValidStack(false);
    }

    /**
     * Gets the first non-null extracted fluid stack.
     *
     * @param slotsMustMatch When true, every fluid in this input hatch must be the same (ignores amounts).
     * @return The first extracted fluid stack, or null if this doesn't exist.
     */
    public FluidStack getFirstValidStack(boolean slotsMustMatch) {
        if (slotsMustMatch) {
            FluidStack firstValid = null;

            for (Slot slot : slots) {
                if (slot == null || slot.extracted == null) continue;

                if (firstValid == null) {
                    firstValid = slot.extracted;
                } else {
                    if (!GTUtility.areFluidsEqual(firstValid, slot.extracted)) {
                        return null;
                    }
                }
            }

            return firstValid;
        } else {
            for (Slot slot : slots) {
                if (slot == null || slot.extracted == null) continue;

                return slot.extracted;
            }

            return null;
        }
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

        aNBT.setInteger("version", 1);
        aNBT.setBoolean("autoPull", autoPullFluidList);
        aNBT.setInteger("minAmount", minAutoPullAmount);
        aNBT.setBoolean("additionalConnection", additionalConnection);
        aNBT.setBoolean("expediteRecipeCheck", expediteRecipeCheck);
        aNBT.setInteger("refreshTime", autoPullRefreshTime);
        getProxy().writeToNBT(aNBT);

        NBTTagList slotList = new NBTTagList();
        aNBT.setTag("slots", slotList);

        for (int i = 0; i < slots.length; i++) {
            Slot slot = slots[i];

            if (slot == null) continue;

            NBTTagCompound tag = new NBTTagCompound();
            slot.writeToNBT(tag);
            tag.setInteger("index", i);

            slotList.appendTag(tag);
        }
    }

    @Override
    public void loadNBTData(NBTTagCompound aNBT) {
        super.loadNBTData(aNBT);
        minAutoPullAmount = aNBT.getInteger("minAmount");
        autoPullFluidList = aNBT.getBoolean("autoPull");
        additionalConnection = aNBT.getBoolean("additionalConnection");
        expediteRecipeCheck = aNBT.getBoolean("expediteRecipeCheck");
        if (aNBT.hasKey("refreshTime")) {
            autoPullRefreshTime = aNBT.getInteger("refreshTime");
        }
        getProxy().readFromNBT(aNBT);
        updateAE2ProxyColor();

        switch (aNBT.getInteger("version")) {
            case 0 -> {
                if (aNBT.hasKey("storedFluids")) {
                    NBTTagList nbtTagList = aNBT.getTagList("storedFluids", 10);
                    int c = Math.min(nbtTagList.tagCount(), SLOT_COUNT);
                    for (int i = 0; i < c; i++) {
                        NBTTagCompound nbtTagCompound = nbtTagList.getCompoundTagAt(i);
                        FluidStack fluidStack = GTUtility.loadFluid(nbtTagCompound);

                        Slot slot = new Slot(fluidStack);
                        slots[i] = slot;

                        if (nbtTagCompound.hasKey("informationAmount")) {
                            int informationAmount = nbtTagCompound.getInteger("informationAmount");
                            slot.extracted = GTUtility.copyAmount(informationAmount, fluidStack);
                            slot.extractedAmount = informationAmount;
                        }
                    }
                }
            }
            case 1 -> {
                NBTTagList slotList = aNBT.getTagList("slots", Constants.NBT.TAG_COMPOUND);

                // noinspection unchecked
                for (NBTTagCompound tag : (List<NBTTagCompound>) slotList.tagList) {
                    Slot slot = Slot.readFromNBT(tag);

                    if (slot != null) slots[tag.getInteger("index")] = slot;
                }
            }
        }
    }

    @Override
    public void onScrewdriverRightClick(ForgeDirection side, EntityPlayer aPlayer, float aX, float aY, float aZ,
        ItemStack aTool) {
        if (!autoPullAvailable) {
            return;
        }

        setAutoPullFluidList(!autoPullFluidList);
        aPlayer.addChatMessage(
            new ChatComponentTranslation(
                "GT5U.machines.stocking_hatch.auto_pull_toggle." + (autoPullFluidList ? "enabled" : "disabled")));
    }

    @Override
    public String getCopiedDataIdentifier(EntityPlayer player) {
        return COPIED_DATA_IDENTIFIER;
    }

    @Override
    public boolean pasteCopiedData(EntityPlayer player, NBTTagCompound nbt) {
        if (nbt == null || !COPIED_DATA_IDENTIFIER.equals(nbt.getString("type"))) return false;

        if (autoPullAvailable) {
            setAutoPullFluidList(nbt.getBoolean("autoPull"));
            minAutoPullAmount = nbt.getInteger("minAmount");
            autoPullRefreshTime = nbt.getInteger("refreshTime");
            expediteRecipeCheck = nbt.getBoolean("expediteRecipeCheck");
        }
        additionalConnection = nbt.getBoolean("additionalConnection");

        if (!autoPullFluidList) {
            NBTTagList stockingFluids = nbt.getTagList("fluidsToStock", 10);

            clearSlotConfigs();
            for (int i = 0; i < stockingFluids.tagCount(); i++) {
                slots[i] = new Slot(GTUtility.loadFluid(stockingFluids.getCompoundTagAt(i)));
            }
        }

        updateValidGridProxySides();
        byte color = nbt.getByte("color");
        this.getBaseMetaTileEntity()
            .setColorization(color);

        updateAllInformationSlots();
        return true;
    }

    @Override
    public NBTTagCompound getCopiedData(EntityPlayer player) {
        NBTTagCompound tag = new NBTTagCompound();
        tag.setString("type", COPIED_DATA_IDENTIFIER);
        tag.setBoolean("autoPull", autoPullFluidList);
        tag.setInteger("minAmount", minAutoPullAmount);
        tag.setBoolean("additionalConnection", additionalConnection);
        tag.setInteger("refreshTime", autoPullRefreshTime);
        tag.setBoolean("expediteRecipeCheck", expediteRecipeCheck);
        tag.setByte("color", this.getColor());

        if (!autoPullFluidList) {
            NBTTagList stockingFluids = new NBTTagList();

            for (Slot slot : slots) {
                if (slot == null) continue;

                stockingFluids.appendTag(slot.config.writeToNBT(new NBTTagCompound()));
            }

            tag.setTag("fluidsToStock", stockingFluids);
        }

        return tag;
    }

    @Override
    public boolean onRightclick(IGregTechTileEntity aBaseMetaTileEntity, EntityPlayer aPlayer, ForgeDirection side,
        float aX, float aY, float aZ) {
        if (!(aPlayer instanceof EntityPlayerMP))
            return super.onRightclick(aBaseMetaTileEntity, aPlayer, side, aX, aY, aZ);
        ItemStack dataStick = aPlayer.inventory.getCurrentItem();
        if (!ItemList.Tool_DataStick.isStackEqual(dataStick, false, true))
            return super.onRightclick(aBaseMetaTileEntity, aPlayer, side, aX, aY, aZ);

        if (!pasteCopiedData(aPlayer, dataStick.stackTagCompound)) return false;

        aPlayer.addChatMessage(new ChatComponentTranslation("GT5U.machines.stocking_bus.loaded"));
        return true;
    }

    @Override
    public void onLeftclick(IGregTechTileEntity aBaseMetaTileEntity, EntityPlayer aPlayer) {
        if (!(aPlayer instanceof EntityPlayerMP)) return;
        ItemStack dataStick = aPlayer.inventory.getCurrentItem();
        if (!ItemList.Tool_DataStick.isStackEqual(dataStick, false, true)) return;

        dataStick.stackTagCompound = getCopiedData(aPlayer);
        dataStick.setStackDisplayName("Stocking Input Hatch Configuration");
        aPlayer.addChatMessage(new ChatComponentTranslation("GT5U.machines.stocking_bus.saved"));
    }

    public boolean containsSuchStack(FluidStack tStack) {
        return Stream.of(slots)
            .filter(Objects::nonNull)
            .anyMatch(slot -> GTUtility.areFluidsEqual(slot.config, tStack));
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

        builder.widget(new SlotSyncWidget());

        builder.widget(
            SlotGroup.ofFluidTanks(
                IntStream.range(0, SLOT_COUNT)
                    .mapToObj(ConfigFluidTank::new)
                    .collect(Collectors.toList()),
                4)
                .phantom(true)
                .widgetCreator((slotIndex, h) -> (FluidSlotWidget) new FluidSlotWidget(h) {

                    @Override
                    protected void tryClickPhantom(ClickData clickData, ItemStack cursorStack) {
                        if (clickData.mouseButton != 0 || autoPullFluidList) return;

                        FluidStack heldFluid = getFluidForPhantomItem(cursorStack);

                        if (heldFluid != null && containsSuchStack(heldFluid)) return;

                        setSlotConfig(slotIndex, GTUtility.copyAmount(1, heldFluid));

                        if (getBaseMetaTileEntity().isServerSide()) {
                            try {
                                updateInformationSlot(slotIndex);
                            } catch (GridAccessException e) {
                                // :P
                            }
                        }
                    }

                    @Override
                    protected void tryScrollPhantom(int direction) {}

                    @Override
                    public IDrawable[] getBackground() {
                        IDrawable slot;
                        if (autoPullFluidList) {
                            slot = GTUITextures.SLOT_DARK_GRAY;
                        } else {
                            slot = ModularUITextures.FLUID_SLOT;
                        }
                        return new IDrawable[] { slot, GTUITextures.OVERLAY_SLOT_ARROW_ME };
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
                    .mapToObj(ExtractedFluidTank::new)
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
                .background(GTUITextures.SLOT_DARK_GRAY)
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
                        return new IDrawable[] { GTUITextures.BUTTON_STANDARD_PRESSED,
                            GTUITextures.OVERLAY_BUTTON_AUTOPULL_ME };
                    } else {
                        return new IDrawable[] { GTUITextures.BUTTON_STANDARD,
                            GTUITextures.OVERLAY_BUTTON_AUTOPULL_ME_DISABLED };
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
            new DrawableWidget().setDrawable(GTUITextures.PICTURE_ARROW_DOUBLE)
                .setPos(82, 30)
                .setSize(12, 12))
            .widget(TextWidget.dynamicString(() -> {
                boolean isActive = isActive();
                boolean isPowered = isPowered();
                boolean isBooting = isBooting();

                String state = WailaText.getPowerState(isActive, isPowered, isBooting);

                if (isActive && isPowered) {
                    return MessageFormat.format(
                        "{0}{1}§f ({2})",
                        EnumChatFormatting.GREEN,
                        state,
                        StatCollector
                            .translateToLocal(isAllowedToWork() ? "GT5U.gui.text.enabled" : "GT5U.gui.text.disabled"));
                } else {
                    return EnumChatFormatting.DARK_RED + state;
                }
            })
                .setTextAlignment(Alignment.Center)
                .setSize(130, 9)
                .setPos(23, 84));
    }

    protected ModularWindow createStackSizeConfigurationWindow(final EntityPlayer player) {
        final int WIDTH = 78;
        final int HEIGHT = 115;
        final int PARENT_WIDTH = getGUIWidth();
        final int PARENT_HEIGHT = getGUIHeight();
        ModularWindow.Builder builder = ModularWindow.builder(WIDTH, HEIGHT);
        builder.setBackground(GTUITextures.BACKGROUND_SINGLEBLOCK_DEFAULT);
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
                    .setBackground(GTUITextures.BACKGROUND_TEXT_FIELD));
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
                    .setBackground(GTUITextures.BACKGROUND_TEXT_FIELD));
        builder.widget(
            TextWidget.localised("GT5U.machines.stocking_bus.force_check")
                .setPos(3, 88)
                .setSize(50, 14))
            .widget(
                new CycleButtonWidget().setToggle(() -> expediteRecipeCheck, val -> setRecipeCheck(val))
                    .setTextureGetter(
                        state -> expediteRecipeCheck ? GTUITextures.OVERLAY_BUTTON_CHECKMARK
                            : GTUITextures.OVERLAY_BUTTON_CROSS)
                    .setBackground(GTUITextures.BUTTON_STANDARD)
                    .setPos(53, 87)
                    .setSize(16, 16));
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
                StatCollector.translateToLocalFormatted("GT5U.waila.stocking_hatch.min_amount", formatNumber(minSize)));
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

    @Override
    public String[] getDescription() {
        if (autoPullAvailable) return GTSplit.splitFormatted(
            "gt.blockmachines.input_hatch_me.desc",
            TIER_COLORS[9] + VN[9],
            StatCollector.translateToLocal("gt.blockmachines.input_hatch_me.autopull.desc") + GTSplit.LB);
        return GTSplit.splitFormatted("gt.blockmachines.input_hatch_me.desc", TIER_COLORS[8] + VN[8], "");
    }

    protected static class Slot {

        /** The fluid to pull into this slot. */
        public FluidStack config;

        /** The amount of stuff initially in the ME system when the recipe check started. */
        public int extractedAmount;
        /**
         * The extracted stack (almost certainly equal to config). This is shared as a reference to the multiblock,
         * which decrements the stored amount as it gets consumed. After the recipe check has finished, the amount in
         * this stack is compared to {@link #extractedAmount} and the difference is subtracted from the ME system. If
         * this operation fails, the machine is shut down.
         */
        public FluidStack extracted;

        public Slot(FluidStack config) {
            this.config = config;
        }

        public void resetExtracted() {
            extracted = null;
            extractedAmount = 0;
        }

        public FluidStack getOriginalExtracted() {
            return extracted == null ? null : GTUtility.copyAmount(extractedAmount, extracted);
        }

        @Override
        public final boolean equals(Object o) {
            if (!(o instanceof Slot slot)) return false;

            return extractedAmount == slot.extractedAmount && Objects.equals(config, slot.config)
                && Objects.equals(extracted, slot.extracted);
        }

        public Slot copy() {
            Slot copy = new Slot(this.config);

            copy.extracted = this.extracted;
            copy.extractedAmount = this.extractedAmount;

            return copy;
        }

        public void writeToNBT(NBTTagCompound tag) {
            tag.setTag("config", config.writeToNBT(new NBTTagCompound()));
            if (extracted != null) {
                tag.setTag("extracted", extracted.writeToNBT(new NBTTagCompound()));
                tag.setInteger("extractedAmount", extractedAmount);
            }
        }

        public static Slot readFromNBT(NBTTagCompound tag) {
            Slot slot = new Slot(FluidStack.loadFluidStackFromNBT(tag.getCompoundTag("config")));

            if (slot.config == null) return null;

            if (tag.hasKey("extracted")) {
                slot.extracted = FluidStack.loadFluidStackFromNBT(tag.getCompoundTag("extracted"));
                slot.extractedAmount = tag.getInteger("extractedAmount");
            }

            return slot;
        }
    }

    protected class ConfigFluidTank implements IFluidTank {

        private final int slotIndex;

        public ConfigFluidTank(int slotIndex) {
            this.slotIndex = slotIndex;
        }

        @Override
        public FluidStack getFluid() {
            Slot slot = GTDataUtils.getIndexSafe(slots, slotIndex);

            if (slot == null) return null;

            return slot.config;
        }

        @Override
        public int getFluidAmount() {
            Slot slot = GTDataUtils.getIndexSafe(slots, slotIndex);

            return slot == null ? 0 : 1;
        }

        @Override
        public int getCapacity() {
            return 1;
        }

        @Override
        public FluidTankInfo getInfo() {
            return new FluidTankInfo(this);
        }

        @Override
        public int fill(FluidStack resource, boolean doFill) {
            return 0;
        }

        @Override
        public FluidStack drain(int maxDrain, boolean doDrain) {
            return null;
        }
    }

    protected class ExtractedFluidTank implements IFluidTank {

        private final int slotIndex;

        public ExtractedFluidTank(int slotIndex) {
            this.slotIndex = slotIndex;
        }

        @Override
        public FluidStack getFluid() {
            Slot slot = GTDataUtils.getIndexSafe(slots, slotIndex);

            if (slot == null) return null;

            return slot.getOriginalExtracted();
        }

        @Override
        public int getFluidAmount() {
            Slot slot = GTDataUtils.getIndexSafe(slots, slotIndex);

            if (slot == null) return 0;

            return slot.extractedAmount;
        }

        @Override
        public int getCapacity() {
            return Integer.MAX_VALUE;
        }

        @Override
        public FluidTankInfo getInfo() {
            return new FluidTankInfo(this);
        }

        @Override
        public int fill(FluidStack resource, boolean doFill) {
            return 0;
        }

        @Override
        public FluidStack drain(int maxDrain, boolean doDrain) {
            return null;
        }
    }

    private class SlotSyncWidget extends FakeSyncWidget.ListSyncer<Slot> {

        public SlotSyncWidget() {
            super(
                () -> GTDataUtils.mapToList(slots, slot -> slot == null ? null : slot.copy()),
                slots2 -> System.arraycopy(slots2.toArray(new Slot[0]), 0, slots, 0, SLOT_COUNT),
                SlotSyncWidget::writeSlot,
                SlotSyncWidget::readSlot);
        }

        private static int counter = 0;
        private static final int NOT_NULL = 0b1 << counter++;
        private static final int EXTRACTED_SET = 0b1 << counter++;

        private static void writeSlot(PacketBuffer buffer, Slot slot) {
            int flags = 0;

            if (slot != null) {
                flags |= NOT_NULL;
                if (slot.extracted != null) {
                    flags |= EXTRACTED_SET;
                }
            }

            buffer.writeByte(flags);

            try {
                if (slot != null) {
                    buffer.writeNBTTagCompoundToBuffer(slot.config.writeToNBT(new NBTTagCompound()));

                    if (slot.extracted != null) {
                        buffer.writeNBTTagCompoundToBuffer(slot.extracted.writeToNBT(new NBTTagCompound()));
                        buffer.writeInt(slot.extractedAmount);
                    }
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        private static Slot readSlot(PacketBuffer buffer) {
            int flags = buffer.readByte();

            if ((flags & NOT_NULL) == 0) return null;

            try {
                Slot slot = new Slot(FluidStack.loadFluidStackFromNBT(buffer.readNBTTagCompoundFromBuffer()));

                if ((flags & EXTRACTED_SET) != 0) {
                    slot.extracted = FluidStack.loadFluidStackFromNBT(buffer.readNBTTagCompoundFromBuffer());
                    slot.extractedAmount = buffer.readInt();
                }

                return slot;
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
