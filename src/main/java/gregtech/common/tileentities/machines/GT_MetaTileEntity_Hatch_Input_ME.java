package gregtech.common.tileentities.machines;

import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_ME_FLUID_HATCH;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_ME_FLUID_HATCH_ACTIVE;

import java.io.IOException;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.Iterator;
import java.util.List;

import javax.annotation.Nullable;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.PacketBuffer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.FluidStack;

import com.google.common.collect.ImmutableList;
import com.gtnewhorizons.modularui.api.drawable.IDrawable;
import com.gtnewhorizons.modularui.api.drawable.UITexture;
import com.gtnewhorizons.modularui.api.math.Alignment;
import com.gtnewhorizons.modularui.api.math.Color;
import com.gtnewhorizons.modularui.api.math.Pos2d;
import com.gtnewhorizons.modularui.api.math.Size;
import com.gtnewhorizons.modularui.api.screen.ModularWindow;
import com.gtnewhorizons.modularui.api.screen.UIBuildContext;
import com.gtnewhorizons.modularui.common.fluid.FluidStackTank;
import com.gtnewhorizons.modularui.common.widget.ButtonWidget;
import com.gtnewhorizons.modularui.common.widget.DrawableWidget;
import com.gtnewhorizons.modularui.common.widget.FakeSyncWidget;
import com.gtnewhorizons.modularui.common.widget.FluidSlotWidget;
import com.gtnewhorizons.modularui.common.widget.TextWidget;
import com.gtnewhorizons.modularui.common.widget.textfield.TextFieldWidget;

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
import gregtech.api.render.TextureFactory;
import gregtech.api.util.GT_Utility;
import mcp.mobius.waila.api.IWailaConfigHandler;
import mcp.mobius.waila.api.IWailaDataAccessor;

public class GT_MetaTileEntity_Hatch_Input_ME extends GT_MetaTileEntity_Hatch_Input
    implements IPowerChannelState, IAddGregtechLogo, IAddUIWidgets {

    public static final int SLOT_COUNT = 16;
    public static final int ALL_SLOT_COUNT = SLOT_COUNT * 2;
    protected final FluidStack[] storedFluid;
    protected final FluidStackTank[] fluidTanks;
    protected final FluidStack[] shadowStoredFluid;

    private final int[] savedStackSizes;

    private boolean additionalConnection = false;

    protected BaseActionSource requestSource = null;

    @Nullable
    protected AENetworkProxy gridProxy = null;

    protected boolean autoPullFluidList = false;
    protected int minAutoPullStackSize = 1;
    protected boolean processingRecipe = false;

    protected static final int CONFIG_WINDOW_ID = 10;

    protected static final FluidStack[] EMPTY_FLUID_STACK = new FluidStack[0];

    public GT_MetaTileEntity_Hatch_Input_ME(int aID, String aName, String aNameRegional) {
        super(
            aID,
            aName,
            aNameRegional,
            1,
            0,
            new String[] { "Advanced fluid input for Multiblocks", "Retrieves directly from ME",
                "Keeps 16 fluid types in stock",
                "Auto-Pull from ME mode will automatically stock the first 16 fluid in the ME system, updated every 5 seconds.",
                "Toggle by right-clicking with screwdriver, or use the GUI.",
                "Use the GUI to limit the minimum stack size for Auto-Pulling.",
                "Configuration data can be copy+pasted using a data stick." });
        storedFluid = new FluidStack[ALL_SLOT_COUNT];
        fluidTanks = new FluidStackTank[ALL_SLOT_COUNT];
        shadowStoredFluid = new FluidStack[SLOT_COUNT];
        savedStackSizes = new int[SLOT_COUNT];
    }

    public GT_MetaTileEntity_Hatch_Input_ME(String aName, int aTier, String[] aDescription, ITexture[][][] aTextures) {
        super(aName, ALL_SLOT_COUNT, aTier, aDescription, aTextures);
        storedFluid = new FluidStack[ALL_SLOT_COUNT];
        fluidTanks = new FluidStackTank[ALL_SLOT_COUNT];
        for (int i = 0; i < ALL_SLOT_COUNT; i++) {
            final int index = i;
            fluidTanks[i] = new FluidStackTank(() -> storedFluid[index], fluid -> {
                if (getBaseMetaTileEntity().isServerSide()) {
                    return;
                }
                storedFluid[index] = fluid;
            }, i >= SLOT_COUNT ? Integer.MAX_VALUE : 1);
        }
        shadowStoredFluid = new FluidStack[SLOT_COUNT];
        savedStackSizes = new int[SLOT_COUNT];
    }

    @Override
    public MetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new GT_MetaTileEntity_Hatch_Input_ME(mName, mTier, mDescriptionArray, mTextures);
    }

    @Override
    public ITexture[] getTexturesActive(ITexture aBaseTexture) {
        return new ITexture[] { aBaseTexture, TextureFactory.of(OVERLAY_ME_FLUID_HATCH_ACTIVE) };
    }

    @Override
    public ITexture[] getTexturesInactive(ITexture aBaseTexture) {
        return new ITexture[] { aBaseTexture, TextureFactory.of(OVERLAY_ME_FLUID_HATCH) };
    }

    @Override
    public void onPostTick(IGregTechTileEntity aBaseMetaTileEntity, long aTimer) {
        if (aTimer % 100 == 0 && autoPullFluidList) {
            refreshFluidList();
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
                if (currItem.getStackSize() >= minAutoPullStackSize) {
                    FluidStack fluidStack = GT_Utility.copyAmount(1, currItem.getFluidStack());
                    storedFluid[index] = fluidStack;
                    index++;
                }
            }
            for (int i = index; i < SLOT_COUNT; i++) {
                storedFluid[i] = null;
            }

        } catch (final GridAccessException ignored) {}
    }

    @Override
    public boolean displaysStackSize() {
        return true;
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
            if (storedFluid[i] == null) {
                shadowStoredFluid[i] = null;
                continue;
            }
            shadowStoredFluid[i] = storedFluid[i + SLOT_COUNT];
            savedStackSizes[i] = storedFluid[i + SLOT_COUNT].amount;
        }
        return shadowStoredFluid;

    }

    @Override
    public void startRecipeProcessing() {
        processingRecipe = true;
    }

    @Override
    public void endRecipeProcessing() {
        AENetworkProxy proxy = getProxy();
        if (proxy == null || !proxy.isActive()) {
            return;
        }
        IMEMonitor<IAEFluidStack> sg;
        try {
            sg = proxy.getStorage()
                .getFluidInventory();
        } catch (GridAccessException e) {
            return;
        }
        for (int i = 0; i < SLOT_COUNT; ++i) {
            FluidStack fluidStack = storedFluid[i + SLOT_COUNT];
            if (fluidStack == null) {
                continue;
            }
            int consume = savedStackSizes[i] - shadowStoredFluid[i].amount;
            if (consume <= 0) {
                continue;
            }
            IAEFluidStack request = AEFluidStack.create(storedFluid[i]);
            request.setStackSize(consume);
            sg.extractItems(request, Actionable.MODULATE, getRequestSource());
            try {
                proxy.getEnergy()
                    .extractAEPower(consume, Actionable.MODULATE, PowerMultiplier.CONFIG);
            } catch (GridAccessException ignored) {}
        }
        for (int i = 0; i < SLOT_COUNT; i++) {
            shadowStoredFluid[i] = null;
            savedStackSizes[i] = 0;
        }
        processingRecipe = false;
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

    public void setAdditionalConnectionOption() {
        if (additionalConnection) {
            gridProxy.setValidSides(EnumSet.complementOf(EnumSet.of(ForgeDirection.UNKNOWN)));
        } else {
            gridProxy.setValidSides(EnumSet.of(getBaseMetaTileEntity().getFrontFacing()));
        }
    }

    @Override
    public boolean onWireCutterRightClick(ForgeDirection side, ForgeDirection wrenchingSide, EntityPlayer aPlayer,
        float aX, float aY, float aZ) {
        additionalConnection = !additionalConnection;
        setAdditionalConnectionOption();
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
                    ItemList.Hatch_Input_ME.get(1),
                    true);
                gridProxy.setFlags(GridFlags.REQUIRE_CHANNEL);
                setAdditionalConnectionOption();
                if (getBaseMetaTileEntity().getWorld() != null) gridProxy.setOwner(
                    getBaseMetaTileEntity().getWorld()
                        .getPlayerEntityByName(getBaseMetaTileEntity().getOwnerName()));
            }
        }
        return this.gridProxy;
    }

    @Override
    public void gridChanged() {}

    @Override
    public boolean isPowered() {
        return getProxy() != null && getProxy().isPowered();
    }

    @Override
    public boolean isActive() {
        return getProxy() != null && getProxy().isActive();
    }

    private void setAutoPullFluidList(boolean pullFluidList) {
        autoPullFluidList = pullFluidList;
        if (!autoPullFluidList) {
            for (int i = 0; i < SLOT_COUNT; i++) {
                storedFluid[i] = null;
            }
        } else {
            refreshFluidList();
        }
        updateAllInformationSlots();
    }

    private void updateAllInformationSlots() {
        for (int index = 0; index < SLOT_COUNT; index++) {
            updateInformationSlot(index, storedFluid[index]);
        }
    }

    public void updateInformationSlot(int aIndex, FluidStack fluidStack) {
        if (aIndex < 0 || aIndex >= SLOT_COUNT) {
            return;
        }
        if (fluidStack == null) {
            storedFluid[aIndex + SLOT_COUNT] = null;
            return;
        }
        AENetworkProxy proxy = getProxy();
        if (proxy == null || !proxy.isActive()) {
            storedFluid[aIndex + SLOT_COUNT] = null;
            return;
        }
        try {
            IMEMonitor<IAEFluidStack> sg = proxy.getStorage()
                .getFluidInventory();
            IAEFluidStack request = AEFluidStack.create(fluidStack);
            request.setStackSize(Integer.MAX_VALUE);
            IAEFluidStack result = sg.extractItems(request, Actionable.SIMULATE, getRequestSource());
            FluidStack s = (result != null) ? result.getFluidStack() : null;
            storedFluid[aIndex + SLOT_COUNT] = s;
        } catch (final GridAccessException ignored) {}
    }

    private BaseActionSource getRequestSource() {
        if (requestSource == null) requestSource = new MachineSource((IActionHost) getBaseMetaTileEntity());
        return requestSource;
    }

    @Override
    public int fill(FluidStack aFluid, boolean doFill) {
        return 0;
    }

    @Override
    public FluidStack drain(int maxDrain, boolean doDrain) {
        AENetworkProxy proxy = getProxy();
        if (proxy == null || !proxy.isActive()) {
            return null;
        }
        for (int i = 0; i < SLOT_COUNT; i++) {
            FluidStack fluidStack = storedFluid[i];
            if (fluidStack == null) {
                continue;
            }
            try {
                IMEMonitor<IAEFluidStack> sg = proxy.getStorage()
                    .getFluidInventory();
                IAEFluidStack request = AEFluidStack.create(fluidStack);
                IAEFluidStack result = sg
                    .extractItems(request, doDrain ? Actionable.MODULATE : Actionable.SIMULATE, getRequestSource());
                FluidStack s = (result != null) ? result.getFluidStack() : null;
                if (s == null) {
                    continue;
                }
                return s;
            } catch (GridAccessException e) {}
        }
        return null;
    }

    @Override
    public int fill(ForgeDirection from, FluidStack resource, boolean doFill) {
        return 0;
    }

    @Override
    public FluidStack drain(ForgeDirection from, FluidStack aFluid, boolean doDrain) {
        AENetworkProxy proxy = getProxy();
        if (proxy == null || !proxy.isActive()) {
            return null;
        }
        for (int i = 0; i < SLOT_COUNT; i++) {
            FluidStack fluidStack = storedFluid[i];
            if (fluidStack == null) {
                continue;
            }
            if (!GT_Utility.areFluidsEqual(aFluid, fluidStack)) {
                continue;
            }
            try {
                IMEMonitor<IAEFluidStack> sg = proxy.getStorage()
                    .getFluidInventory();
                IAEFluidStack request = AEFluidStack.create(fluidStack);
                IAEFluidStack result = sg
                    .extractItems(request, doDrain ? Actionable.MODULATE : Actionable.SIMULATE, getRequestSource());
                FluidStack s = (result != null) ? result.getFluidStack() : null;
                if (s == null) {
                    continue;
                }
                return s;
            } catch (GridAccessException e) {}
        }
        return null;
    }

    @Override
    public void saveNBTData(NBTTagCompound aNBT) {
        super.saveNBTData(aNBT);

        NBTTagList nbtTagList = new NBTTagList();
        for (int i = 0; i < SLOT_COUNT; i++) {
            FluidStack fluidStack = storedFluid[i];
            if (fluidStack == null) {
                continue;
            }
            nbtTagList.appendTag(fluidStack.writeToNBT(new NBTTagCompound()));
        }

        int[] sizes = new int[16];
        for (int i = 0; i < 16; ++i) sizes[i] = storedFluid[i + 16] == null ? 0 : storedFluid[i + 16].amount;
        aNBT.setIntArray("sizes", sizes);
        aNBT.setTag("storedFluid", nbtTagList);
        aNBT.setBoolean("autoStock", autoPullFluidList);
        aNBT.setInteger("minAutoPullStackSize", minAutoPullStackSize);
        aNBT.setBoolean("additionalConnection", additionalConnection);
        getProxy().writeToNBT(aNBT);
    }

    @Override
    public void loadNBTData(NBTTagCompound aNBT) {
        super.loadNBTData(aNBT);
        if (aNBT.hasKey("storedFluid")) {
            NBTTagList nbtTagList = aNBT.getTagList("storedFluid", 10);
            int c = Math.min(nbtTagList.tagCount(), SLOT_COUNT);
            for (int i = 0; i < c; i++) {
                NBTTagCompound nbtTagCompound = nbtTagList.getCompoundTagAt(i);
                storedFluid[i] = GT_Utility.loadFluid(nbtTagCompound);
            }
        }

        if (aNBT.hasKey("sizes")) {
            int[] sizes = aNBT.getIntArray("sizes");
            for (int i = 0; i < SLOT_COUNT; ++i) {
                if (sizes[i] != 0 && storedFluid[i] != null) {
                    FluidStack s = storedFluid[i].copy();
                    s.amount = sizes[i];
                    storedFluid[i + SLOT_COUNT] = s;
                }
            }
        }

        minAutoPullStackSize = aNBT.getInteger("minAutoPullStackSize");
        autoPullFluidList = aNBT.getBoolean("autoStock");
        additionalConnection = aNBT.getBoolean("additionalConnection");
        getProxy().readFromNBT(aNBT);
    }

    @Override
    public void onScrewdriverRightClick(ForgeDirection side, EntityPlayer aPlayer, float aX, float aY, float aZ) {
        setAutoPullFluidList(!autoPullFluidList);
        GT_Utility.sendChatToPlayer(aPlayer, "Automatic Fluid Pull " + autoPullFluidList);
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

        setAutoPullFluidList(nbt.getBoolean("autoPull"));
        minAutoPullStackSize = nbt.getInteger("minStackSize");
        additionalConnection = nbt.getBoolean("additionalConnection");
        if (!autoPullFluidList) {
            NBTTagList stockingFluids = nbt.getTagList("fluidsToStock", 10);
            for (int i = 0; i < stockingFluids.tagCount(); i++) {
                this.storedFluid[i] = GT_Utility.loadFluid(stockingFluids.getCompoundTagAt(i));
            }
        }

        setAdditionalConnectionOption();
        aPlayer.addChatMessage(new ChatComponentText("Loaded Config From Data Stick"));
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
        tag.setInteger("minStackSize", minAutoPullStackSize);
        tag.setBoolean("additionalConnection", additionalConnection);

        NBTTagList stockingFluids = new NBTTagList();
        if (!autoPullFluidList) {
            for (int index = 0; index < SLOT_COUNT; index++) {
                FluidStack fluidStack = storedFluid[index];
                if (fluidStack == null) {
                    continue;
                }
                stockingFluids.appendTag(fluidStack.writeToNBT(new NBTTagCompound()));
            }
            tag.setTag("fluidsToStock", stockingFluids);
        }
        dataStick.stackTagCompound = tag;
        dataStick.setStackDisplayName("Stocking Input Hatch Configuration");
        aPlayer.addChatMessage(new ChatComponentText("Saved Config to Data Stick"));
    }

    @Override
    public void onExplosion() {
        for (int i = 0; i < SLOT_COUNT; i++) {
            mInventory[i] = null;
        }
    }

    public boolean containsSuchStack(FluidStack tStack) {
        for (int i = 0; i < 16; ++i) {
            if (GT_Utility.areFluidsEqual(storedFluid[i], tStack, false)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void addUIWidgets(ModularWindow.Builder builder, UIBuildContext buildContext) {
        final Pos2d[] saSlotPos = new Pos2d[] { new Pos2d(97 + 18 * 0, 9 + 18 * 0), new Pos2d(97 + 18 * 1, 9 + 18 * 0),
            new Pos2d(97 + 18 * 2, 9 + 18 * 0), new Pos2d(97 + 18 * 3, 9 + 18 * 0), new Pos2d(97 + 18 * 0, 9 + 18 * 1),
            new Pos2d(97 + 18 * 1, 9 + 18 * 1), new Pos2d(97 + 18 * 2, 9 + 18 * 1), new Pos2d(97 + 18 * 3, 9 + 18 * 1),
            new Pos2d(97 + 18 * 0, 9 + 18 * 2), new Pos2d(97 + 18 * 1, 9 + 18 * 2), new Pos2d(97 + 18 * 2, 9 + 18 * 2),
            new Pos2d(97 + 18 * 3, 9 + 18 * 2), new Pos2d(97 + 18 * 0, 9 + 18 * 3), new Pos2d(97 + 18 * 1, 9 + 18 * 3),
            new Pos2d(97 + 18 * 2, 9 + 18 * 3), new Pos2d(97 + 18 * 3, 9 + 18 * 3), };
        for (int i = 0; i < SLOT_COUNT; i++) {
            FluidStackTank fluidStackTank = fluidTanks[i + SLOT_COUNT];
            FluidSlotWidget fluidSlotWidget = new FluidSlotWidget(fluidStackTank) {

                @Override
                protected void onClickServer(ClickData clickData, ItemStack clientVerifyToken) {}

                @Override
                protected void tryScrollPhantom(int direction) {}

                @Override
                public boolean onMouseScroll(int direction) {
                    return false;
                }
            };// FluidSlotWidget.phantom(fluidStackTank, true);
            fluidSlotWidget.setBackground(GT_UITextures.SLOT_DARK_GRAY);
            fluidSlotWidget.setPos(saSlotPos[i]);
            builder.widget(fluidSlotWidget);
        }

        FluidSlotWidget[] storedFluidSlotWidget = new FluidSlotWidget[16];
        final Pos2d[] storedPos = new Pos2d[] { new Pos2d(7 + 18 * 0, 9 + 18 * 0), new Pos2d(7 + 18 * 1, 9 + 18 * 0),
            new Pos2d(7 + 18 * 2, 9 + 18 * 0), new Pos2d(7 + 18 * 3, 9 + 18 * 0), new Pos2d(7 + 18 * 0, 9 + 18 * 1),
            new Pos2d(7 + 18 * 1, 9 + 18 * 1), new Pos2d(7 + 18 * 2, 9 + 18 * 1), new Pos2d(7 + 18 * 3, 9 + 18 * 1),
            new Pos2d(7 + 18 * 0, 9 + 18 * 2), new Pos2d(7 + 18 * 1, 9 + 18 * 2), new Pos2d(7 + 18 * 2, 9 + 18 * 2),
            new Pos2d(7 + 18 * 3, 9 + 18 * 2), new Pos2d(7 + 18 * 0, 9 + 18 * 3), new Pos2d(7 + 18 * 1, 9 + 18 * 3),
            new Pos2d(7 + 18 * 2, 9 + 18 * 3), new Pos2d(7 + 18 * 3, 9 + 18 * 3), };
        for (int i = 0; i < SLOT_COUNT; i++) {
            FluidStackTank fluidStackTank = fluidTanks[i];
            int finalI = i;
            FluidSlotWidget fluidSlotWidget = new FluidSlotWidget(fluidStackTank) {

                static final int PACKET_EMPTY_CLICK = 6;

                @Override
                public ClickResult onClick(int buttonId, boolean doubleClick) {
                    ItemStack cursorStack = getContext().getCursor()
                        .getItemStack();
                    if (cursorStack == null) {
                        ClickData clickData = ClickData.create(buttonId, doubleClick);
                        syncToServer(PACKET_EMPTY_CLICK, clickData::writeToPacket);
                        return ClickResult.ACCEPT;
                    }
                    return super.onClick(buttonId, doubleClick);
                }

                @Override
                public void readOnServer(int id, PacketBuffer buf) throws IOException {
                    super.readOnServer(id, buf);
                    switch (id) {
                        case PACKET_EMPTY_CLICK:
                            clearTag(ClickData.readPacket(buf));
                            break;
                    }
                    markForUpdate();
                }

                protected void clearTag(ClickData clickData) {
                    if (clickData.mouseButton != 0) {
                        return;
                    }
                    storedFluid[finalI] = null;
                    updateInformationSlot(finalI, null);
                    detectAndSendChanges(false);
                }

                @Override
                protected void onClickServer(ClickData clickData, ItemStack clientVerifyToken) {
                    EntityPlayer player = getContext().getPlayer();
                    ItemStack heldItem = player.inventory.getItemStack();
                    if (clickData.mouseButton != 0) {
                        return;
                    }
                    if (autoPullFluidList) {
                        return;
                    }
                    if (heldItem == null) {
                        storedFluid[finalI] = null;
                        updateInformationSlot(finalI, null);
                        detectAndSendChanges(false);
                        return;
                    }
                    FluidStack heldFluid = getFluidForPhantomItem(heldItem);
                    if (heldFluid == null) {
                        return;
                    }
                    if (containsSuchStack(heldFluid)) {
                        return;
                    }
                    FluidStack setFileStack = GT_Utility.copyAmount(1, heldFluid);
                    storedFluid[finalI] = setFileStack;
                    updateInformationSlot(finalI, setFileStack);
                    detectAndSendChanges(false);
                }

                @Override
                protected void tryScrollPhantom(int direction) {}
            };
            fluidSlotWidget.setBackground(getGUITextureSet().getItemSlot(), GT_UITextures.OVERLAY_SLOT_ARROW_ME);
            fluidSlotWidget.setPos(storedPos[i]);
            builder.widget(fluidSlotWidget);
        }

        buildContext.addSyncedWindow(CONFIG_WINDOW_ID, this::createStackSizeConfigurationWindow);

        builder.widget(
            new DrawableWidget().setDrawable(GT_UITextures.PICTURE_ARROW_DOUBLE)
                .setPos(82, 30)
                .setSize(12, 12))
            .widget(new ButtonWidget().setOnClick((clickData, widget) -> {
                if (clickData.mouseButton == 0) {
                    setAutoPullFluidList(!autoPullFluidList);
                } else if (clickData.mouseButton == 1 && !widget.isClient()) {
                    widget.getContext()
                        .openSyncedWindow(CONFIG_WINDOW_ID);
                }
            })
                .setPlayClickSound(true)
                .setBackground(() -> {
                    List<UITexture> ret = new ArrayList<>();
                    ret.add(GT_UITextures.BUTTON_STANDARD);
                    if (autoPullFluidList) ret.add(GT_UITextures.OVERLAY_BUTTON_AUTOPULL_ME);
                    else ret.add(GT_UITextures.OVERLAY_BUTTON_AUTOPULL_ME_DISABLED);
                    return ret.toArray(new IDrawable[0]);
                })
                .addTooltips(
                    ImmutableList.of(
                        "Click to toggle automatic fluid pulling from ME.",
                        "Right-Click to edit minimum stack size for item pulling."))
                .setSize(16, 16)
                .setPos(80, 10))
            .widget(new FakeSyncWidget.BooleanSyncer(() -> autoPullFluidList, this::setAutoPullFluidList));
    }

    protected ModularWindow createStackSizeConfigurationWindow(final EntityPlayer player) {
        final int WIDTH = 78;
        final int HEIGHT = 40;
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
            new TextWidget("Min Stack Size").setPos(3, 2)
                .setSize(74, 14))
            .widget(
                new TextFieldWidget().setSetterInt(val -> minAutoPullStackSize = val)
                    .setGetterInt(() -> minAutoPullStackSize)
                    .setNumbers(1, Integer.MAX_VALUE)
                    .setOnScrollNumbers(1, 4, 64)
                    .setTextAlignment(Alignment.Center)
                    .setTextColor(Color.WHITE.normal)
                    .setSize(36, 18)
                    .setPos(19, 18)
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
        NBTTagCompound tag = accessor.getNBTData();
        boolean autopull = tag.getBoolean("autoPull");
        int minSize = tag.getInteger("minStackSize");
        currenttip.add(String.format("Auto-Pull from ME: %s", autopull ? "Enabled" : "Disabled"));
        if (autopull) currenttip.add(String.format("Minimum Stack Size: %d", minSize));
        super.getWailaBody(itemStack, currenttip, accessor, config);
    }

    @Override
    public void getWailaNBTData(EntityPlayerMP player, TileEntity tile, NBTTagCompound tag, World world, int x, int y,
        int z) {
        tag.setBoolean("autoPull", autoPullFluidList);
        tag.setInteger("minStackSize", minAutoPullStackSize);
        super.getWailaNBTData(player, tile, tag, world, x, y, z);
    }

}
