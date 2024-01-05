package gregtech.common.tileentities.machines;

import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_ME_CRAFTING_INPUT_BUFFER;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_ME_CRAFTING_INPUT_BUS;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.annotation.Nullable;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.world.World;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.event.ForgeEventFactory;
import net.minecraftforge.fluids.FluidStack;

import org.apache.commons.lang3.ArrayUtils;
import org.jetbrains.annotations.NotNull;

import com.glodblock.github.common.item.ItemFluidPacket;
import com.google.common.collect.ImmutableList;
import com.gtnewhorizons.modularui.api.math.Alignment;
import com.gtnewhorizons.modularui.api.math.Size;
import com.gtnewhorizons.modularui.api.screen.ModularWindow;
import com.gtnewhorizons.modularui.api.screen.UIBuildContext;
import com.gtnewhorizons.modularui.common.widget.ButtonWidget;
import com.gtnewhorizons.modularui.common.widget.SlotGroup;
import com.gtnewhorizons.modularui.common.widget.SlotWidget;

import appeng.api.AEApi;
import appeng.api.implementations.ICraftingPatternItem;
import appeng.api.implementations.IPowerChannelState;
import appeng.api.networking.GridFlags;
import appeng.api.networking.IGridNode;
import appeng.api.networking.crafting.ICraftingPatternDetails;
import appeng.api.networking.crafting.ICraftingProvider;
import appeng.api.networking.crafting.ICraftingProviderHelper;
import appeng.api.networking.events.MENetworkCraftingPatternChange;
import appeng.api.networking.security.BaseActionSource;
import appeng.api.networking.security.IActionHost;
import appeng.api.networking.security.MachineSource;
import appeng.api.storage.IMEMonitor;
import appeng.api.storage.data.IAEFluidStack;
import appeng.api.storage.data.IAEItemStack;
import appeng.api.util.AECableType;
import appeng.api.util.DimensionalCoord;
import appeng.api.util.IInterfaceViewable;
import appeng.core.AppEng;
import appeng.core.sync.GuiBridge;
import appeng.helpers.ICustomNameObject;
import appeng.items.misc.ItemEncodedPattern;
import appeng.items.tools.quartz.ToolQuartzCuttingKnife;
import appeng.me.GridAccessException;
import appeng.me.helpers.AENetworkProxy;
import appeng.me.helpers.IGridProxyable;
import appeng.util.IWideReadableNumberConverter;
import appeng.util.Platform;
import appeng.util.ReadableNumberConverter;
import gregtech.GT_Mod;
import gregtech.api.enums.ItemList;
import gregtech.api.gui.modularui.GT_UITextures;
import gregtech.api.interfaces.IConfigurationCircuitSupport;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.modularui.IAddGregtechLogo;
import gregtech.api.interfaces.modularui.IAddUIWidgets;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.MetaTileEntity;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_Hatch_InputBus;
import gregtech.api.render.TextureFactory;
import gregtech.api.util.GT_Utility;
import gregtech.api.util.extensions.ArrayExt;
import mcp.mobius.waila.api.IWailaConfigHandler;
import mcp.mobius.waila.api.IWailaDataAccessor;

public class GT_MetaTileEntity_Hatch_CraftingInput_ME extends GT_MetaTileEntity_Hatch_InputBus
    implements IConfigurationCircuitSupport, IAddGregtechLogo, IAddUIWidgets, IPowerChannelState, ICraftingProvider,
    IGridProxyable, IDualInputHatch, ICustomNameObject, IInterfaceViewable {

    // Each pattern slot in the crafting input hatch has its own internal inventory
    public static class PatternSlot implements IDualInputInventory {

        public interface SharedItemGetter {

            ItemStack[] getSharedItem();
        }

        private final ItemStack pattern;
        private final ICraftingPatternDetails patternDetails;
        private final List<ItemStack> itemInventory;
        private final List<FluidStack> fluidInventory;
        private final SharedItemGetter sharedItemGetter;

        public PatternSlot(ItemStack pattern, World world, SharedItemGetter getter) {
            this.pattern = pattern;
            this.patternDetails = ((ICraftingPatternItem) Objects.requireNonNull(pattern.getItem()))
                .getPatternForItem(pattern, world);
            this.itemInventory = new ArrayList<>();
            this.fluidInventory = new ArrayList<>();
            this.sharedItemGetter = getter;
        }

        public PatternSlot(ItemStack pattern, NBTTagCompound nbt, World world, SharedItemGetter getter) {
            this.pattern = pattern;
            this.patternDetails = ((ICraftingPatternItem) Objects.requireNonNull(pattern.getItem()))
                .getPatternForItem(pattern, world);
            this.itemInventory = new ArrayList<>();
            this.fluidInventory = new ArrayList<>();
            this.sharedItemGetter = getter;
            NBTTagList inv = nbt.getTagList("inventory", Constants.NBT.TAG_COMPOUND);
            for (int i = 0; i < inv.tagCount(); i++) {
                NBTTagCompound tagItemStack = inv.getCompoundTagAt(i);
                var item = GT_Utility.loadItem(tagItemStack);
                if (item != null) {
                    if (item.stackSize > 0) {
                        itemInventory.add(item);
                    }
                } else {
                    GT_Mod.GT_FML_LOGGER.warn(
                        "An error occurred while loading contents of ME Crafting Input Bus. This item has been voided: "
                            + tagItemStack);
                }
            }
            NBTTagList fluidInv = nbt.getTagList("fluidInventory", Constants.NBT.TAG_COMPOUND);
            for (int i = 0; i < fluidInv.tagCount(); i++) {
                NBTTagCompound tagFluidStack = fluidInv.getCompoundTagAt(i);
                var fluid = FluidStack.loadFluidStackFromNBT(tagFluidStack);
                if (fluid != null) {
                    if (fluid.amount > 0) {
                        fluidInventory.add(fluid);
                    }
                } else {
                    GT_Mod.GT_FML_LOGGER.warn(
                        "An error occurred while loading contents of ME Crafting Input Bus. This fluid has been voided: "
                            + tagFluidStack);
                }
            }
        }

        public boolean hasChanged(ItemStack newPattern, World world) {
            return newPattern == null
                || (!ItemStack.areItemStacksEqual(pattern, newPattern) && !this.patternDetails.equals(
                    ((ICraftingPatternItem) Objects.requireNonNull(pattern.getItem()))
                        .getPatternForItem(pattern, world)));
        }

        private boolean isEmpty() {
            // if one item / fluid is empty then it should be safe to assume all other is empty,
            // or at least won't require a recipe check, as long as the pattern is sane
            if (!itemInventory.isEmpty()) {
                return itemInventory.get(0) == null || itemInventory.get(0).stackSize <= 0;
            }

            if (!fluidInventory.isEmpty()) {
                return fluidInventory.get(0) == null || fluidInventory.get(0).amount <= 0;
            }
            return true;
        }

        @Override
        public ItemStack[] getItemInputs() {
            if (isEmpty()) return new ItemStack[0];
            return ArrayUtils.addAll(itemInventory.toArray(new ItemStack[0]), sharedItemGetter.getSharedItem());
        }

        @Override
        public FluidStack[] getFluidInputs() {
            if (isEmpty()) return new FluidStack[0];
            return fluidInventory.toArray(new FluidStack[0]);
        }

        public ICraftingPatternDetails getPatternDetails() {
            return patternDetails;
        }

        public void refund(AENetworkProxy proxy, BaseActionSource src) throws GridAccessException {
            IMEMonitor<IAEItemStack> sg = proxy.getStorage()
                .getItemInventory();
            for (ItemStack itemStack : itemInventory) {
                if (itemStack == null || itemStack.stackSize == 0) continue;
                IAEItemStack rest = Platform.poweredInsert(
                    proxy.getEnergy(),
                    sg,
                    AEApi.instance()
                        .storage()
                        .createItemStack(itemStack),
                    src);
                itemStack.stackSize = rest != null && rest.getStackSize() > 0 ? (int) rest.getStackSize() : 0;
            }
            IMEMonitor<IAEFluidStack> fsg = proxy.getStorage()
                .getFluidInventory();
            for (FluidStack fluidStack : fluidInventory) {
                if (fluidStack == null || fluidStack.amount == 0) continue;
                IAEFluidStack rest = Platform.poweredInsert(
                    proxy.getEnergy(),
                    fsg,
                    AEApi.instance()
                        .storage()
                        .createFluidStack(fluidStack),
                    src);
                fluidStack.amount = rest != null && rest.getStackSize() > 0 ? (int) rest.getStackSize() : 0;
            }
        }

        public boolean insertItemsAndFluids(InventoryCrafting inventoryCrafting) {
            int errorIndex = -1; // overflow may occur at this index
            for (int i = 0; i < inventoryCrafting.getSizeInventory(); ++i) {
                ItemStack itemStack = inventoryCrafting.getStackInSlot(i);
                if (itemStack == null) continue;

                boolean inserted = false;
                if (itemStack.getItem() instanceof ItemFluidPacket) { // insert fluid
                    var fluidStack = ItemFluidPacket.getFluidStack(itemStack);
                    if (fluidStack == null) continue;
                    for (var fluid : fluidInventory) {
                        if (!fluid.isFluidEqual(fluidStack)) continue;
                        if (Integer.MAX_VALUE - fluidStack.amount < fluid.amount) {
                            // Overflow detected
                            errorIndex = i;
                            break;
                        }
                        fluid.amount += fluidStack.amount;
                        inserted = true;
                        break;
                    }
                    if (errorIndex != -1) break;
                    if (!inserted) {
                        fluidInventory.add(fluidStack);
                    }
                } else { // insert item
                    for (var item : itemInventory) {
                        if (!itemStack.isItemEqual(item)) continue;
                        if (Integer.MAX_VALUE - itemStack.stackSize < item.stackSize) {
                            // Overflow detected
                            errorIndex = i;
                            break;
                        }
                        item.stackSize += itemStack.stackSize;
                        inserted = true;
                        break;
                    }
                    if (errorIndex != -1) break;
                    if (!inserted) {
                        itemInventory.add(itemStack);
                    }
                }
            }
            if (errorIndex != -1) { // need to rollback
                // Clean up the inserted items/liquids
                for (int i = 0; i < errorIndex; ++i) {
                    var itemStack = inventoryCrafting.getStackInSlot(i);
                    if (itemStack == null) continue;
                    if (itemStack.getItem() instanceof ItemFluidPacket) { // remove fluid
                        var fluidStack = ItemFluidPacket.getFluidStack(itemStack);
                        if (fluidStack == null) continue;
                        for (var fluid : fluidInventory) {
                            if (fluid.isFluidEqual(fluidStack)) {
                                fluid.amount -= fluidStack.amount;
                                break;
                            }
                        }
                    } else { // remove item
                        for (var item : itemInventory) {
                            if (item.isItemEqual(itemStack)) {
                                item.stackSize -= itemStack.stackSize;
                                break;
                            }
                        }
                    }
                }
                return false;
            }
            return true;
        }

        public NBTTagCompound writeToNBT(NBTTagCompound nbt) {
            nbt.setTag("pattern", pattern.writeToNBT(new NBTTagCompound()));

            NBTTagList itemInventoryNbt = new NBTTagList();
            for (ItemStack itemStack : this.itemInventory) {
                itemInventoryNbt.appendTag(GT_Utility.saveItem(itemStack));
            }
            nbt.setTag("inventory", itemInventoryNbt);

            NBTTagList fluidInventoryNbt = new NBTTagList();
            for (FluidStack fluidStack : fluidInventory) {
                fluidInventoryNbt.appendTag(fluidStack.writeToNBT(new NBTTagCompound()));
            }
            nbt.setTag("fluidInventory", fluidInventoryNbt);

            return nbt;
        }
    }

    // mInventory is used for storing patterns, circuit and manual slot (typically NC items)
    private static final int MAX_PATTERN_COUNT = 4 * 9;
    private static final int SLOT_MANUAL_SIZE = 9;
    private static final int MAX_INV_COUNT = MAX_PATTERN_COUNT + SLOT_MANUAL_SIZE + 1;
    private static final int SLOT_CIRCUIT = MAX_PATTERN_COUNT;
    private static final int SLOT_MANUAL_START = SLOT_CIRCUIT + 1;
    private static final int MANUAL_SLOT_WINDOW = 10;
    private BaseActionSource requestSource = null;
    private @Nullable AENetworkProxy gridProxy = null;

    // holds all internal inventories
    private PatternSlot[] internalInventory = new PatternSlot[MAX_PATTERN_COUNT];

    // a hash map for faster lookup of pattern slots, not necessarily all valid.
    private Map<ICraftingPatternDetails, PatternSlot> patternDetailsPatternSlotMap = new HashMap<>(MAX_PATTERN_COUNT);

    private boolean needPatternSync = true;
    private boolean justHadNewItems = false;

    private String customName = null;
    private boolean supportFluids;
    private boolean additionalConnection = false;

    public GT_MetaTileEntity_Hatch_CraftingInput_ME(int aID, String aName, String aNameRegional,
        boolean supportFluids) {
        super(
            aID,
            aName,
            aNameRegional,
            6,
            MAX_INV_COUNT,
            new String[] { "Advanced item input for Multiblocks", "Processes patterns directly from ME",
                supportFluids ? "It supports patterns including fluids"
                    : "It does not support patterns including fluids",
                "Change ME connection behavior by right-clicking with wire cutter" });
        disableSort = true;
        this.supportFluids = supportFluids;
    }

    public GT_MetaTileEntity_Hatch_CraftingInput_ME(String aName, int aTier, String[] aDescription,
        ITexture[][][] aTextures, boolean supportFluids) {
        super(aName, aTier, MAX_INV_COUNT, aDescription, aTextures);
        this.supportFluids = supportFluids;
        disableSort = true;
    }

    @Override
    public MetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new GT_MetaTileEntity_Hatch_CraftingInput_ME(mName, mTier, mDescriptionArray, mTextures, supportFluids);
    }

    @Override
    public ITexture[] getTexturesActive(ITexture aBaseTexture) {
        return getTexturesInactive(aBaseTexture);
    }

    @Override
    public ITexture[] getTexturesInactive(ITexture aBaseTexture) {
        return new ITexture[] { aBaseTexture,
            TextureFactory.of(supportFluids ? OVERLAY_ME_CRAFTING_INPUT_BUFFER : OVERLAY_ME_CRAFTING_INPUT_BUS) };
    }

    @Override
    public void onPostTick(IGregTechTileEntity aBaseMetaTileEntity, long aTimer) {
        super.onPostTick(aBaseMetaTileEntity, aTimer);

        if (getBaseMetaTileEntity().isServerSide()) {
            if (needPatternSync && aTimer % 10 == 0) {
                needPatternSync = !postMEPatternChange();
            }
            if (aTimer % 20 == 0) {
                getBaseMetaTileEntity().setActive(isActive());
            }
        }
    }

    @Override
    public void onFirstTick(IGregTechTileEntity aBaseMetaTileEntity) {
        super.onFirstTick(aBaseMetaTileEntity);
        getProxy().onReady();
    }

    @Override
    public IGridNode getGridNode(ForgeDirection dir) {
        return getProxy().getNode();
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
    public void securityBreak() {}

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
            gridProxy = new AENetworkProxy(this, "proxy", ItemList.Hatch_CraftingInput_Bus_ME.get(1), true);
            gridProxy.setFlags(GridFlags.REQUIRE_CHANNEL);
            updateValidGridProxySides();
            if (getBaseMetaTileEntity().getWorld() != null) gridProxy.setOwner(
                getBaseMetaTileEntity().getWorld()
                    .getPlayerEntityByName(getBaseMetaTileEntity().getOwnerName()));
        }

        return this.gridProxy;
    }

    @Override
    public DimensionalCoord getLocation() {
        return new DimensionalCoord(
            getBaseMetaTileEntity().getWorld(),
            getBaseMetaTileEntity().getXCoord(),
            getBaseMetaTileEntity().getYCoord(),
            getBaseMetaTileEntity().getZCoord());
    }

    @Override
    public int rows() {
        return 4;
    }

    @Override
    public int rowSize() {
        return 9;
    }

    @Override
    public IInventory getPatterns() {
        return this;
    }

    @Override
    public String getName() {
        if (hasCustomName()) {
            return getCustomName();
        }
        StringBuilder name = new StringBuilder();
        if (getCrafterIcon() != null) {
            name.append(getCrafterIcon().getDisplayName());
        } else {
            name.append(getInventoryName());
        }

        if (mInventory[SLOT_CIRCUIT] != null) {
            name.append(" - ");
            name.append(mInventory[SLOT_CIRCUIT].getItemDamage());
        }
        if (mInventory[SLOT_MANUAL_START] != null) {
            name.append(" - ");
            name.append(mInventory[SLOT_MANUAL_START].getDisplayName());
        }
        return name.toString();
    }

    @Override
    public TileEntity getTileEntity() {
        return (TileEntity) getBaseMetaTileEntity();
    }

    @Override
    public boolean shouldDisplay() {
        return true;
    }

    @Override
    public void gridChanged() {
        needPatternSync = true;
    }

    @Override
    public boolean isPowered() {
        return getProxy() != null && getProxy().isPowered();
    }

    @Override
    public boolean isActive() {
        return getProxy() != null && getProxy().isActive();
    }

    @Override
    public void saveNBTData(NBTTagCompound aNBT) {
        super.saveNBTData(aNBT);

        // save internalInventory
        NBTTagList internalInventoryNBT = new NBTTagList();
        for (int i = 0; i < internalInventory.length; i++) {
            if (internalInventory[i] != null) {
                NBTTagCompound internalInventorySlotNBT = new NBTTagCompound();
                internalInventorySlotNBT.setInteger("patternSlot", i);
                internalInventorySlotNBT
                    .setTag("patternSlotNBT", internalInventory[i].writeToNBT(new NBTTagCompound()));
                internalInventoryNBT.appendTag(internalInventorySlotNBT);
            }
        }
        aNBT.setTag("internalInventory", internalInventoryNBT);
        if (customName != null) aNBT.setString("customName", customName);
        aNBT.setBoolean("additionalConnection", additionalConnection);
        getProxy().writeToNBT(aNBT);
    }

    @Override
    public void loadNBTData(NBTTagCompound aNBT) {
        super.loadNBTData(aNBT);
        // load internalInventory
        NBTTagList internalInventoryNBT = aNBT.getTagList("internalInventory", Constants.NBT.TAG_COMPOUND);
        for (int i = 0; i < internalInventoryNBT.tagCount(); i++) {
            NBTTagCompound internalInventorySlotNBT = internalInventoryNBT.getCompoundTagAt(i);
            int patternSlot = internalInventorySlotNBT.getInteger("patternSlot");
            NBTTagCompound patternSlotNBT = internalInventorySlotNBT.getCompoundTag("patternSlotNBT");
            ItemStack pattern = ItemStack.loadItemStackFromNBT(patternSlotNBT.getCompoundTag("pattern"));
            if (pattern != null) {
                internalInventory[patternSlot] = new PatternSlot(
                    pattern,
                    patternSlotNBT,
                    getBaseMetaTileEntity().getWorld(),
                    this::getSharedItems);
            } else {
                GT_Mod.GT_FML_LOGGER.warn(
                    "An error occurred while loading contents of ME Crafting Input Bus. This pattern has been voided: "
                        + patternSlotNBT);
            }
        }

        // Migrate from 4x8 to 4x9 pattern inventory
        int oldPatternCount = 4 * 8;
        int oldSlotManual = oldPatternCount + 1;
        int oldSlotCircuit = oldPatternCount;

        if (internalInventory[oldSlotManual] == null && mInventory[oldSlotManual] != null) {
            mInventory[SLOT_MANUAL_START] = mInventory[oldSlotManual];
            mInventory[oldSlotManual] = null;
        }
        if (internalInventory[oldSlotCircuit] == null && mInventory[oldSlotCircuit] != null) {
            mInventory[SLOT_CIRCUIT] = mInventory[oldSlotCircuit];
            mInventory[oldSlotCircuit] = null;
        }

        // reconstruct patternDetailsPatternSlotMap
        patternDetailsPatternSlotMap.clear();
        for (PatternSlot patternSlot : internalInventory) {
            if (patternSlot != null) {
                patternDetailsPatternSlotMap.put(patternSlot.getPatternDetails(), patternSlot);
            }
        }

        if (aNBT.hasKey("customName")) customName = aNBT.getString("customName");
        additionalConnection = aNBT.getBoolean("additionalConnection");

        getProxy().readFromNBT(aNBT);
    }

    @Override
    public boolean isGivingInformation() {
        return true;
    }

    private String describePattern(ICraftingPatternDetails patternDetails) {
        return Arrays.stream(patternDetails.getCondensedOutputs())
            .map(
                aeItemStack -> aeItemStack.getItem()
                    .getItemStackDisplayName(aeItemStack.getItemStack()))
            .collect(Collectors.joining(", "));
    }

    @Override
    public String[] getInfoData() {
        var ret = new ArrayList<String>();
        ret.add(
            "The bus is " + ((getProxy() != null && getProxy().isActive()) ? EnumChatFormatting.GREEN + "online"
                : EnumChatFormatting.RED + "offline" + getAEDiagnostics()) + EnumChatFormatting.RESET);
        ret.add("Internal Inventory: ");
        var i = 0;
        for (var slot : internalInventory) {
            if (slot == null) continue;
            IWideReadableNumberConverter nc = ReadableNumberConverter.INSTANCE;

            i += 1;
            ret.add(
                "Slot " + i
                    + " "
                    + EnumChatFormatting.BLUE
                    + describePattern(slot.patternDetails)
                    + EnumChatFormatting.RESET);
            for (var item : slot.itemInventory) {
                if (item == null || item.stackSize == 0) continue;
                ret.add(
                    item.getItem()
                        .getItemStackDisplayName(item) + ": "
                        + EnumChatFormatting.GOLD
                        + nc.toWideReadableForm(item.stackSize)
                        + EnumChatFormatting.RESET);
            }
            for (var fluid : slot.fluidInventory) {
                if (fluid == null || fluid.amount == 0) continue;
                ret.add(
                    fluid.getLocalizedName() + ": "
                        + EnumChatFormatting.AQUA
                        + nc.toWideReadableForm(fluid.amount)
                        + EnumChatFormatting.RESET);
            }
        }
        return ret.toArray(new String[0]);
    }

    @Override
    public boolean allowPullStack(IGregTechTileEntity aBaseMetaTileEntity, int aIndex, ForgeDirection side,
        ItemStack aStack) {
        return false;
    }

    @Override
    public boolean allowPutStack(IGregTechTileEntity aBaseMetaTileEntity, int aIndex, ForgeDirection side,
        ItemStack aStack) {
        return false;
    }

    @Override
    public int getCircuitSlot() {
        return SLOT_CIRCUIT;
    }

    @Override
    public int getCircuitSlotX() {
        return 170;
    }

    @Override
    public int getCircuitSlotY() {
        return 64;
    }

    @Override
    public boolean isValidSlot(int aIndex) {
        return true;
    }

    @Override
    public int getGUIWidth() {
        return super.getGUIWidth() + 16;
    }

    @Override
    public void addUIWidgets(ModularWindow.@NotNull Builder builder, UIBuildContext buildContext) {
        buildContext.addSyncedWindow(MANUAL_SLOT_WINDOW, this::createSlotManualWindow);
        builder.widget(
            SlotGroup.ofItemHandler(inventoryHandler, 9)
                .startFromSlot(0)
                .endAtSlot(MAX_PATTERN_COUNT - 1)
                .phantom(false)
                .background(getGUITextureSet().getItemSlot(), GT_UITextures.OVERLAY_SLOT_PATTERN_ME)
                .widgetCreator(slot -> new SlotWidget(slot) {

                    @Override
                    protected ItemStack getItemStackForRendering(Slot slotIn) {
                        var stack = slot.getStack();
                        if (stack == null || !(stack.getItem() instanceof ItemEncodedPattern patternItem)) {
                            return stack;
                        }
                        var output = patternItem.getOutput(stack);
                        return output != null ? output : stack;
                    }
                }.setFilter(itemStack -> itemStack.getItem() instanceof ICraftingPatternItem)
                    .setChangeListener(() -> onPatternChange(slot.getSlotIndex(), slot.getStack())))
                .build()
                .setPos(7, 9))
            .widget(new ButtonWidget().setOnClick((clickData, widget) -> {
                if (clickData.mouseButton == 0) {
                    widget.getContext()
                        .openSyncedWindow(MANUAL_SLOT_WINDOW);
                }
            })
                .setPlayClickSound(true)
                .setBackground(GT_UITextures.BUTTON_STANDARD, GT_UITextures.OVERLAY_BUTTON_PLUS_LARGE)
                .addTooltips(ImmutableList.of("Place manual items"))
                .setSize(16, 16)
                .setPos(170, 45))
            .widget(new ButtonWidget().setOnClick((clickData, widget) -> {
                if (clickData.mouseButton == 0) {
                    refundAll();
                }
            })
                .setPlayClickSound(true)
                .setBackground(GT_UITextures.BUTTON_STANDARD, GT_UITextures.OVERLAY_BUTTON_EXPORT)
                .addTooltips(ImmutableList.of("Return all internally stored items back to AE"))
                .setSize(16, 16)
                .setPos(170, 28));
    }

    @Override
    public void updateSlots() {
        for (int slotId = SLOT_MANUAL_START; slotId < SLOT_MANUAL_START + SLOT_MANUAL_SIZE; ++slotId) {
            if (mInventory[slotId] != null && mInventory[slotId].stackSize <= 0) mInventory[slotId] = null;
        }
    }

    private BaseActionSource getRequest() {
        if (requestSource == null) requestSource = new MachineSource((IActionHost) getBaseMetaTileEntity());
        return requestSource;
    }

    private void onPatternChange(int index, ItemStack newItem) {
        if (!getBaseMetaTileEntity().isServerSide()) return;

        var world = getBaseMetaTileEntity().getWorld();

        // remove old if applicable
        var originalPattern = internalInventory[index];
        if (originalPattern != null) {
            if (originalPattern.hasChanged(newItem, world)) {
                try {
                    originalPattern.refund(getProxy(), getRequest());
                } catch (GridAccessException ignored) {}
                internalInventory[index] = null;
                needPatternSync = true;
            } else {
                return; // nothing has changed
            }
        }

        // original does not exist or has changed
        if (newItem == null || !(newItem.getItem() instanceof ICraftingPatternItem)) return;

        var patternSlot = new PatternSlot(newItem, world, this::getSharedItems);
        internalInventory[index] = patternSlot;
        patternDetailsPatternSlotMap.put(patternSlot.getPatternDetails(), patternSlot);

        needPatternSync = true;
    }

    public ItemStack[] getSharedItems() {
        ItemStack[] sharedItems = new ItemStack[SLOT_MANUAL_SIZE + 1];
        sharedItems[0] = mInventory[SLOT_CIRCUIT];
        System.arraycopy(mInventory, SLOT_MANUAL_START, sharedItems, 1, SLOT_MANUAL_SIZE);
        return ArrayExt.withoutNulls(sharedItems, ItemStack[]::new);
    }

    @Override
    public void getWailaBody(ItemStack itemStack, List<String> currenttip, IWailaDataAccessor accessor,
        IWailaConfigHandler config) {
        NBTTagCompound tag = accessor.getNBTData();
        if (tag.hasKey("inventory")) {
            var inventory = tag.getTagList("inventory", Constants.NBT.TAG_COMPOUND);
            for (int i = 0; i < inventory.tagCount(); ++i) {
                var item = inventory.getCompoundTagAt(i);
                var name = item.getString("name");
                var amount = item.getLong("amount");
                currenttip.add(
                    name + ": "
                        + EnumChatFormatting.GOLD
                        + ReadableNumberConverter.INSTANCE.toWideReadableForm(amount)
                        + EnumChatFormatting.RESET);
            }
        }
        super.getWailaBody(itemStack, currenttip, accessor, config);
    }

    @Override
    public void getWailaNBTData(EntityPlayerMP player, TileEntity tile, NBTTagCompound tag, World world, int x, int y,
        int z) {

        NBTTagList inventory = new NBTTagList();
        HashMap<String, Long> nameToAmount = new HashMap<>();
        for (Iterator<PatternSlot> it = inventories(); it.hasNext();) {
            var i = it.next();
            for (var item : i.itemInventory) {
                if (item != null && item.stackSize > 0) {
                    var name = item.getDisplayName();
                    var amount = nameToAmount.getOrDefault(name, 0L);
                    nameToAmount.put(name, amount + item.stackSize);
                }
            }
            for (var fluid : i.fluidInventory) {
                if (fluid != null && fluid.amount > 0) {
                    var name = fluid.getLocalizedName();
                    var amount = nameToAmount.getOrDefault(name, 0L);
                    nameToAmount.put(name, amount + fluid.amount);
                }
            }
        }
        for (var entry : nameToAmount.entrySet()) {
            var item = new NBTTagCompound();
            item.setString("name", entry.getKey());
            item.setLong("amount", entry.getValue());
            inventory.appendTag(item);
        }

        tag.setTag("inventory", inventory);
        tag.setString("name", getName());
        super.getWailaNBTData(player, tile, tag, world, x, y, z);
    }

    @Override
    public void provideCrafting(ICraftingProviderHelper craftingTracker) {
        if (!isActive()) return;

        for (PatternSlot slot : internalInventory) {
            if (slot == null) continue;
            ICraftingPatternDetails details = slot.getPatternDetails();
            if (details == null) {
                GT_Mod.GT_FML_LOGGER.warn(
                    "Found an invalid pattern at " + getBaseMetaTileEntity().getCoords()
                        + " in dim "
                        + getBaseMetaTileEntity().getWorld().provider.dimensionId);
                continue;
            }
            craftingTracker.addCraftingOption(this, details);
        }
    }

    @Override
    public boolean pushPattern(ICraftingPatternDetails patternDetails, InventoryCrafting table) {
        if (!isActive()) return false;

        if (!supportFluids) {
            for (int i = 0; i < table.getSizeInventory(); ++i) {
                ItemStack itemStack = table.getStackInSlot(i);
                if (itemStack == null) continue;
                if (itemStack.getItem() instanceof ItemFluidPacket) return false;
            }
        }
        if (!patternDetailsPatternSlotMap.get(patternDetails)
            .insertItemsAndFluids(table)) {
            return false;
        }
        justHadNewItems = true;
        return true;
    }

    @Override
    public boolean isBusy() {
        return false;
    }

    @Override
    public Iterator<PatternSlot> inventories() {
        return Arrays.stream(internalInventory)
            .filter(Objects::nonNull)
            .iterator();
    }

    @Override
    public void onBlockDestroyed() {
        refundAll();
        super.onBlockDestroyed();
    }

    private void refundAll() {
        for (var slot : internalInventory) {
            if (slot == null) continue;
            try {
                slot.refund(getProxy(), getRequest());
            } catch (GridAccessException ignored) {}
        }
    }

    @Override
    public boolean justUpdated() {
        var ret = justHadNewItems;
        justHadNewItems = false;
        return ret;
    }

    @Override
    public void onLeftclick(IGregTechTileEntity aBaseMetaTileEntity, EntityPlayer aPlayer) {
        if (!(aPlayer instanceof EntityPlayerMP)) return;

        ItemStack dataStick = aPlayer.inventory.getCurrentItem();
        if (!ItemList.Tool_DataStick.isStackEqual(dataStick, false, true)) return;

        NBTTagCompound tag = new NBTTagCompound();
        tag.setString("type", "CraftingInputBuffer");
        tag.setInteger("x", aBaseMetaTileEntity.getXCoord());
        tag.setInteger("y", aBaseMetaTileEntity.getYCoord());
        tag.setInteger("z", aBaseMetaTileEntity.getZCoord());

        dataStick.stackTagCompound = tag;
        dataStick.setStackDisplayName(
            "Crafting Input Buffer Link Data Stick (" + aBaseMetaTileEntity
                .getXCoord() + ", " + aBaseMetaTileEntity.getYCoord() + ", " + aBaseMetaTileEntity.getZCoord() + ")");
        aPlayer.addChatMessage(new ChatComponentText("Saved Link Data to Data Stick"));
    }

    @Override
    public boolean onRightclick(IGregTechTileEntity aBaseMetaTileEntity, EntityPlayer aPlayer, ForgeDirection side,
        float aX, float aY, float aZ) {
        final ItemStack is = aPlayer.inventory.getCurrentItem();
        if (is != null && is.getItem() instanceof ToolQuartzCuttingKnife) {
            if (ForgeEventFactory.onItemUseStart(aPlayer, is, 1) <= 0) return false;
            var te = getBaseMetaTileEntity();
            aPlayer.openGui(
                AppEng.instance(),
                GuiBridge.GUI_RENAMER.ordinal() << 5 | (side.ordinal()),
                te.getWorld(),
                te.getXCoord(),
                te.getYCoord(),
                te.getZCoord());
            return true;
        }
        return super.onRightclick(aBaseMetaTileEntity, aPlayer, side, aX, aY, aZ);
    }

    @Override
    public ItemStack getCrafterIcon() {
        return getMachineCraftingIcon();
    }

    private boolean postMEPatternChange() {
        // don't post until it's active
        if (!getProxy().isActive()) return false;
        try {
            getProxy().getGrid()
                .postEvent(new MENetworkCraftingPatternChange(this, getProxy().getNode()));
        } catch (GridAccessException ignored) {
            return false;
        }
        return true;
    }

    protected ModularWindow createSlotManualWindow(final EntityPlayer player) {
        final int WIDTH = 68;
        final int HEIGHT = 68;
        final int PARENT_WIDTH = getGUIWidth();
        final int PARENT_HEIGHT = getGUIHeight();
        ModularWindow.Builder builder = ModularWindow.builder(WIDTH, HEIGHT);
        builder.setBackground(GT_UITextures.BACKGROUND_SINGLEBLOCK_DEFAULT);
        builder.setGuiTint(getGUIColorization());
        builder.setDraggable(true);
        // make sure the manual window is within the parent window
        // otherwise picking up manual items would toss them
        // See GuiContainer.java flag1
        builder.setPos(
            (size, window) -> Alignment.Center.getAlignedPos(size, new Size(PARENT_WIDTH, PARENT_HEIGHT))
                .add(Alignment.TopRight.getAlignedPos(new Size(PARENT_WIDTH, PARENT_HEIGHT), new Size(WIDTH, HEIGHT))));
        builder.widget(
            SlotGroup.ofItemHandler(inventoryHandler, 3)
                .startFromSlot(SLOT_MANUAL_START)
                .endAtSlot(SLOT_MANUAL_START + SLOT_MANUAL_SIZE - 1)
                .phantom(false)
                .background(getGUITextureSet().getItemSlot())
                .build()
                .setPos(7, 7));
        return builder.build();
    }

    @Override
    public void setInventorySlotContents(int aIndex, ItemStack aStack) {
        super.setInventorySlotContents(aIndex, aStack);
        if (aIndex >= MAX_PATTERN_COUNT) return;
        onPatternChange(aIndex, aStack);
        needPatternSync = true;
    }

    @Override
    public String getCustomName() {
        return customName;
    }

    @Override
    public boolean hasCustomName() {
        return customName != null;
    }

    @Override
    public void setCustomName(String name) {
        customName = name;
    }

    @Override
    public Optional<IDualInputInventory> getFirstNonEmptyInventory() {
        for (PatternSlot slot : internalInventory) {
            if (slot != null && !slot.isEmpty()) return Optional.of(slot);
        }
        return Optional.empty();
    }

    @Override
    public boolean supportsFluids() {
        return this.supportFluids;
    }
}
