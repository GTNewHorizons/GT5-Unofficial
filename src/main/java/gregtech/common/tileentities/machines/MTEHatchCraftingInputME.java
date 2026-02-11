package gregtech.common.tileentities.machines;

import static gregtech.api.enums.GTValues.TIER_COLORS;
import static gregtech.api.enums.GTValues.VN;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_ME_CRAFTING_INPUT_BUFFER;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_ME_CRAFTING_INPUT_BUS;
import static gregtech.api.metatileentity.BaseTileEntity.TOOLTIP_DELAY;
import static gregtech.api.objects.XSTR.XSTR_INSTANCE;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.WeakHashMap;
import java.util.stream.Collectors;

import javax.annotation.Nullable;

import net.minecraft.entity.item.EntityItem;
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
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.event.ForgeEventFactory;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;

import org.apache.commons.lang3.ArrayUtils;
import org.jetbrains.annotations.NotNull;

import com.cleanroommc.modularui.factory.PosGuiData;
import com.cleanroommc.modularui.screen.ModularPanel;
import com.cleanroommc.modularui.screen.UISettings;
import com.cleanroommc.modularui.value.sync.PanelSyncManager;
import com.glodblock.github.common.item.ItemFluidPacket;
import com.gtnewhorizons.modularui.api.drawable.IDrawable;
import com.gtnewhorizons.modularui.api.math.Alignment;
import com.gtnewhorizons.modularui.api.math.Size;
import com.gtnewhorizons.modularui.api.screen.ModularWindow;
import com.gtnewhorizons.modularui.api.screen.UIBuildContext;
import com.gtnewhorizons.modularui.common.widget.ButtonWidget;
import com.gtnewhorizons.modularui.common.widget.CycleButtonWidget;
import com.gtnewhorizons.modularui.common.widget.FakeSyncWidget;
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
import appeng.api.storage.data.IAEStack;
import appeng.api.util.AECableType;
import appeng.api.util.AEColor;
import appeng.api.util.DimensionalCoord;
import appeng.api.util.IInterfaceViewable;
import appeng.core.AppEng;
import appeng.core.sync.GuiBridge;
import appeng.helpers.ICustomNameObject;
import appeng.items.misc.ItemEncodedPattern;
import appeng.items.tools.quartz.ToolQuartzCuttingKnife;
import appeng.me.GridAccessException;
import appeng.me.cache.CraftingGridCache;
import appeng.me.helpers.AENetworkProxy;
import appeng.me.helpers.IGridProxyable;
import appeng.util.IWideReadableNumberConverter;
import appeng.util.PatternMultiplierHelper;
import appeng.util.Platform;
import appeng.util.ReadableNumberConverter;
import appeng.util.inv.MEInventoryCrafting;
import gregtech.GTMod;
import gregtech.api.enums.Dyes;
import gregtech.api.enums.GTValues;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.SoundResource;
import gregtech.api.gui.modularui.GTUITextures;
import gregtech.api.interfaces.IConfigurationCircuitSupport;
import gregtech.api.interfaces.IMEConnectable;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.modularui.IAddGregtechLogo;
import gregtech.api.interfaces.modularui.IAddUIWidgets;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.logic.ProcessingLogic;
import gregtech.api.metatileentity.MetaTileEntity;
import gregtech.api.metatileentity.implementations.MTEHatchInputBus;
import gregtech.api.objects.GTDualInputPattern;
import gregtech.api.render.TextureFactory;
import gregtech.api.util.GTSplit;
import gregtech.api.util.GTUtility;
import gregtech.api.util.extensions.ArrayExt;
import gregtech.common.config.Gregtech;
import gregtech.common.gui.modularui.hatch.MTEHatchCraftingInputMEGui;
import mcp.mobius.waila.api.IWailaConfigHandler;
import mcp.mobius.waila.api.IWailaDataAccessor;

@IMetaTileEntity.SkipGenerateDescription
public class MTEHatchCraftingInputME extends MTEHatchInputBus
    implements IConfigurationCircuitSupport, IAddGregtechLogo, IAddUIWidgets, IPowerChannelState, ICraftingProvider,
    IGridProxyable, IDualInputHatchWithPattern, ICustomNameObject, IInterfaceViewable, IMEConnectable {

    // Each pattern slot in the crafting input hatch has its own internal inventory
    public static class PatternSlot<P extends IMetaTileEntity & IDualInputHatch>
        implements IDualInputInventoryWithPattern {

        protected final P parentMTE;
        protected final ItemStack pattern;
        @Nullable
        protected final ICraftingPatternDetails patternDetails;
        protected final GTUtility.ItemId patternItemId;

        protected final List<ItemStack> itemInventory;
        protected final List<FluidStack> fluidInventory;

        public PatternSlot(ItemStack pattern, P parent) {
            this(pattern, null, parent);
        }

        public PatternSlot(ItemStack pattern, NBTTagCompound nbt, P parent) {
            this.pattern = pattern;
            this.parentMTE = parent;
            this.patternDetails = ((ICraftingPatternItem) Objects.requireNonNull(pattern.getItem())).getPatternForItem(
                pattern,
                parent.getBaseMetaTileEntity()
                    .getWorld());
            this.itemInventory = new ArrayList<>();
            this.fluidInventory = new ArrayList<>();
            this.patternItemId = GTUtility.ItemId.create(pattern);

            if (nbt == null) return;
            NBTTagList inv = nbt.getTagList("inventory", Constants.NBT.TAG_COMPOUND);
            for (int i = 0; i < inv.tagCount(); i++) {
                NBTTagCompound tagItemStack = inv.getCompoundTagAt(i);
                ItemStack item = GTUtility.loadItem(tagItemStack);
                if (item != null) {
                    if (item.stackSize > 0) {
                        itemInventory.add(item);
                    }
                } else {
                    GTMod.GT_FML_LOGGER.warn(
                        "An error occurred while loading contents of ME Crafting Input Bus. This item has been voided: "
                            + tagItemStack);
                }
            }
            NBTTagList fluidInv = nbt.getTagList("fluidInventory", Constants.NBT.TAG_COMPOUND);
            for (int i = 0; i < fluidInv.tagCount(); i++) {
                NBTTagCompound tagFluidStack = fluidInv.getCompoundTagAt(i);
                FluidStack fluid = FluidStack.loadFluidStackFromNBT(tagFluidStack);
                if (fluid != null) {
                    if (fluid.amount > 0) {
                        fluidInventory.add(fluid);
                    }
                } else {
                    GTMod.GT_FML_LOGGER.warn(
                        "An error occurred while loading contents of ME Crafting Input Bus. This fluid has been voided: "
                            + tagFluidStack);
                }
            }
        }

        public boolean hasChanged(ItemStack newPattern, World world) {
            return newPattern == null || patternDetails == null
                || (!ItemStack.areItemStacksEqual(pattern, newPattern) && !this.patternDetails.equals(
                    ((ICraftingPatternItem) Objects.requireNonNull(pattern.getItem()))
                        .getPatternForItem(newPattern, world)));
        }

        public void updateSlotItems() {
            for (int i = itemInventory.size() - 1; i >= 0; i--) {
                ItemStack itemStack = itemInventory.get(i);
                if (itemStack == null || itemStack.stackSize <= 0) {
                    itemInventory.remove(i);
                }
            }
        }

        public void updateSlotFluids() {
            for (int i = fluidInventory.size() - 1; i >= 0; i--) {
                FluidStack fluidStack = fluidInventory.get(i);
                if (fluidStack == null || fluidStack.amount <= 0) {
                    fluidInventory.remove(i);
                }
            }
        }

        public boolean isItemEmpty() {
            updateSlotItems();
            return itemInventory.isEmpty();
        }

        public boolean isFluidEmpty() {
            updateSlotFluids();
            return fluidInventory.isEmpty();
        }

        @Override
        public boolean isEmpty() {
            return isItemEmpty() && isFluidEmpty();
        }

        @Override
        public ItemStack[] getItemInputs() {
            if (isItemEmpty()) return GTValues.emptyItemStackArray;
            return itemInventory.toArray(new ItemStack[0]);
        }

        @Override
        public FluidStack[] getFluidInputs() {
            if (isEmpty()) return GTValues.emptyFluidStackArray;
            return fluidInventory.toArray(new FluidStack[0]);
        }

        @Nullable
        public ICraftingPatternDetails getPatternDetails() {
            return patternDetails;
        }

        @Override
        public GTDualInputPattern getPatternInputs() {
            GTDualInputPattern dualInputs = new GTDualInputPattern();

            ItemStack[] inputItems = this.parentMTE.getSharedItems();
            FluidStack[] inputFluids = GTValues.emptyFluidStackArray;

            if (patternDetails != null) {
                for (IAEStack<?> singleInput : patternDetails.getAEInputs()) {
                    if (singleInput == null) continue;
                    if (singleInput instanceof IAEItemStack ais) {
                        inputItems = ArrayUtils.addAll(inputItems, ais.getItemStack());
                    } else if (singleInput instanceof IAEFluidStack ifs) {
                        inputFluids = ArrayUtils.addAll(inputFluids, ifs.getFluidStack());
                    }
                }
            }

            dualInputs.inputItems = inputItems;
            dualInputs.inputFluid = inputFluids;
            return dualInputs;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            PatternSlot<?> that = (PatternSlot<?>) o;
            return Objects.equals(pattern, that.pattern);
        }

        @Override
        public int hashCode() {
            return patternItemId.hashCode();
        }

        /**
         * Try to refund the items and fluids back.
         * <p>
         * Push all the items and fluids back to the AE network first. If shouldDrop is true, the remaining are dropped
         * to the world (the fluids are dropped as AE2FC fluid drop). Otherwise, they are still left in the inventory.
         */
        public void refund(AENetworkProxy proxy, BaseActionSource src, boolean shouldDrop) throws GridAccessException {
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

                if (Gregtech.machines.allowCribDropItems && shouldDrop && itemStack.stackSize > 0) {
                    World world = parentMTE.getBaseMetaTileEntity()
                        .getWorld();
                    EntityItem entityItem = new EntityItem(
                        world,
                        parentMTE.getBaseMetaTileEntity()
                            .getXCoord() + XSTR_INSTANCE.nextFloat() * 0.8F
                            + 0.1F,
                        parentMTE.getBaseMetaTileEntity()
                            .getYCoord() + XSTR_INSTANCE.nextFloat() * 0.8F
                            + 0.1F,
                        parentMTE.getBaseMetaTileEntity()
                            .getZCoord() + XSTR_INSTANCE.nextFloat() * 0.8F
                            + 0.1F,
                        GTUtility.copy(itemStack));
                    entityItem.motionX = XSTR_INSTANCE.nextGaussian() * 0.05;
                    entityItem.motionY = XSTR_INSTANCE.nextGaussian() * 0.25;
                    entityItem.motionZ = XSTR_INSTANCE.nextGaussian() * 0.05;
                    world.spawnEntityInWorld(entityItem);

                    itemStack.stackSize = 0;
                }
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

                if (Gregtech.machines.allowCribDropItems && shouldDrop && fluidStack.amount > 0) {
                    World world = parentMTE.getBaseMetaTileEntity()
                        .getWorld();

                    ItemStack fluidPacketItemStack = ItemFluidPacket.newStack(fluidStack);
                    if (fluidPacketItemStack == null) continue;

                    EntityItem entityItem = new EntityItem(
                        world,
                        parentMTE.getBaseMetaTileEntity()
                            .getXCoord() + XSTR_INSTANCE.nextFloat() * 0.8F
                            + 0.1F,
                        parentMTE.getBaseMetaTileEntity()
                            .getYCoord() + XSTR_INSTANCE.nextFloat() * 0.8F
                            + 0.1F,
                        parentMTE.getBaseMetaTileEntity()
                            .getZCoord() + XSTR_INSTANCE.nextFloat() * 0.8F
                            + 0.1F,
                        fluidPacketItemStack);
                    entityItem.motionX = XSTR_INSTANCE.nextGaussian() * 0.05;
                    entityItem.motionY = XSTR_INSTANCE.nextGaussian() * 0.25;
                    entityItem.motionZ = XSTR_INSTANCE.nextGaussian() * 0.05;
                    world.spawnEntityInWorld(entityItem);
                }
            }
        }

        private void insertItem(IAEItemStack inserted) {
            final List<ItemStack> temp = new ArrayList<>();
            for (ItemStack itemStack : itemInventory) {
                if (GTUtility.areStacksEqual(inserted.getItemStack(), itemStack)) {
                    if (itemStack.stackSize > Integer.MAX_VALUE - inserted.getStackSize()) {
                        inserted.decStackSize(Integer.MAX_VALUE - itemStack.stackSize);
                        itemStack.stackSize = Integer.MAX_VALUE;

                        if (inserted.getStackSize() > Integer.MAX_VALUE) {
                            inserted.decStackSize(Integer.MAX_VALUE);
                            temp.add(itemStack.copy());
                        }
                    } else {
                        itemStack.stackSize += (int) inserted.getStackSize();
                        return;
                    }
                }
            }

            while (inserted.getStackSize() > Integer.MAX_VALUE) {
                temp.add(inserted.getItemStack());
                inserted.decStackSize(Integer.MAX_VALUE);
            }

            if (inserted.getStackSize() > 0) {
                itemInventory.add(inserted.getItemStack());
            }

            if (!temp.isEmpty()) itemInventory.addAll(temp);
        }

        private void insertFluid(IAEFluidStack inserted) {
            final List<FluidStack> temp = new ArrayList<>();
            for (FluidStack fluidStack : fluidInventory) {
                if (GTUtility.areFluidsEqual(inserted.getFluidStack(), fluidStack)) {
                    if (fluidStack.amount > Integer.MAX_VALUE - inserted.getStackSize()) {
                        inserted.decStackSize(Integer.MAX_VALUE - fluidStack.amount);
                        fluidStack.amount = Integer.MAX_VALUE;

                        if (inserted.getStackSize() > Integer.MAX_VALUE) {
                            inserted.decStackSize(Integer.MAX_VALUE);
                            temp.add(fluidStack.copy());
                        }
                    } else {
                        fluidStack.amount += (int) inserted.getStackSize();
                        return;
                    }
                }
            }

            while (inserted.getStackSize() > Integer.MAX_VALUE) {
                temp.add(inserted.getFluidStack());
                inserted.decStackSize(Integer.MAX_VALUE);
            }

            if (inserted.getStackSize() > 0) {
                fluidInventory.add(inserted.getFluidStack());
            }

            if (!temp.isEmpty()) fluidInventory.addAll(temp);
        }

        public boolean insertItemsAndFluids(MEInventoryCrafting inventoryCrafting) {
            for (int i = 0; i < inventoryCrafting.getSizeInventory(); ++i) {
                final IAEStack<?> aes = inventoryCrafting.getAEStackInSlot(i);
                if (aes == null) continue;

                if (aes instanceof IAEFluidStack ifs) { // insert fluid
                    insertFluid(ifs);
                } else if (aes instanceof IAEItemStack ais) { // insert item
                    insertItem(ais);
                }
            }
            return true;
        }

        public NBTTagCompound writeToNBT(NBTTagCompound nbt) {
            nbt.setTag("pattern", pattern.writeToNBT(new NBTTagCompound()));

            NBTTagList itemInventoryNbt = new NBTTagList();
            for (ItemStack itemStack : this.itemInventory) {
                itemInventoryNbt.appendTag(GTUtility.saveItem(itemStack));
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
    public static final int SLOT_MANUAL_START = SLOT_CIRCUIT + 1;
    private static final int MANUAL_SLOT_WINDOW = 10;
    private BaseActionSource requestSource = null;
    private @Nullable AENetworkProxy gridProxy = null;
    public Set<ProcessingLogic> processingLogics = Collections.newSetFromMap(new WeakHashMap<>());
    private final List<MTEHatchCraftingInputSlave> proxyHatches = new ArrayList<>();

    // holds all internal inventories
    @SuppressWarnings("unchecked") // Java doesn't allow to create an array of a generic type.
    private final PatternSlot<MTEHatchCraftingInputME>[] internalInventory = new PatternSlot[MAX_PATTERN_COUNT];

    // a hash map for faster lookup of pattern slots, not necessarily all valid.
    private final Map<ICraftingPatternDetails, PatternSlot<MTEHatchCraftingInputME>> patternDetailsPatternSlotMap = new HashMap<>(
        MAX_PATTERN_COUNT);

    private boolean needPatternSync = true;
    private boolean justHadNewItems = false;

    private String customName = null;
    private final boolean supportFluids;
    private boolean additionalConnection = false;
    public boolean disablePatternOptimization = false;
    public boolean showPattern = true;

    public MTEHatchCraftingInputME(int aID, String aName, String aNameRegional, boolean supportFluids) {
        super(aID, aName, aNameRegional, supportFluids ? 10 : 6, MAX_INV_COUNT, null);
        disableSort = true;
        this.supportFluids = supportFluids;
    }

    public MTEHatchCraftingInputME(String aName, int aTier, String[] aDescription, ITexture[][][] aTextures,
        boolean supportFluids) {
        super(aName, aTier, MAX_INV_COUNT, aDescription, aTextures);
        this.supportFluids = supportFluids;
        disableSort = true;
    }

    @Override
    public MetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new MTEHatchCraftingInputME(mName, mTier, mDescriptionArray, mTextures, supportFluids);
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
    public void onColorChangeServer(byte aColor) {
        updateAE2ProxyColor();
    }

    public void addProxyHatch(MTEHatchCraftingInputSlave proxy) {
        if (!proxyHatches.contains(proxy)) proxyHatches.add(proxy);
    }

    public void removeProxyHatch(MTEHatchCraftingInputSlave proxy) {
        proxyHatches.remove(proxy);
    }

    public List<MTEHatchCraftingInputSlave> getProxyHatches() {
        validateProxyHatchList();
        return Collections.unmodifiableList(proxyHatches);
    }

    private long lastProxyHatchValidationTime = -1;

    private void validateProxyHatchList() {
        long currentTime = getBaseMetaTileEntity().getTimer();
        if (currentTime != lastProxyHatchValidationTime) {
            proxyHatches
                .removeIf(hatch -> hatch == null || hatch.getBaseMetaTileEntity() == null || hatch.getMaster() != this);
            lastProxyHatchValidationTime = currentTime;
        }
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
            name.append(getLocalName());
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
        return showPattern;
    }

    @Override
    public boolean allowsPatternOptimization() {
        return !disablePatternOptimization;
    }

    @Override
    public ItemStack getSelfRep() {
        return this.getStackForm(1);
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
        aNBT.setBoolean("disablePatternOptimization", disablePatternOptimization);
        aNBT.setBoolean("showPattern", showPattern);
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
                internalInventory[patternSlot] = new PatternSlot<>(pattern, patternSlotNBT, this);
            } else {
                GTMod.GT_FML_LOGGER.warn(
                    "An error occurred while loading contents of ME Crafting Input Bus. This pattern has been voided: "
                        + patternSlotNBT);
            }
        }

        // Migrate from 4x8 to 4x9 pattern inventory
        int oldPatternCount = 4 * 8;
        int oldSlotManual = oldPatternCount + 1;

        if (internalInventory[oldSlotManual] == null && mInventory[oldSlotManual] != null) {
            mInventory[SLOT_MANUAL_START] = mInventory[oldSlotManual];
            mInventory[oldSlotManual] = null;
        }
        if (internalInventory[oldPatternCount] == null && mInventory[oldPatternCount] != null) {
            mInventory[SLOT_CIRCUIT] = mInventory[oldPatternCount];
            mInventory[oldPatternCount] = null;
        }

        // reconstruct patternDetailsPatternSlotMap
        patternDetailsPatternSlotMap.clear();
        for (PatternSlot<MTEHatchCraftingInputME> patternSlot : internalInventory) {
            if (patternSlot != null && patternSlot.getPatternDetails() != null) {
                patternDetailsPatternSlotMap.put(patternSlot.getPatternDetails(), patternSlot);
            }
        }

        if (aNBT.hasKey("customName")) customName = aNBT.getString("customName");
        additionalConnection = aNBT.getBoolean("additionalConnection");
        disablePatternOptimization = aNBT.getBoolean("disablePatternOptimization");
        if (aNBT.hasKey("showPattern")) showPattern = aNBT.getBoolean("showPattern");

        getProxy().readFromNBT(aNBT);
        updateAE2ProxyColor();

        // Sync inventories to ensure that the real inventory matches what AE2 is seeing.
        for (int i = 0; i < MAX_PATTERN_COUNT; i++) {
            if (internalInventory[i] == null) continue;
            mInventory[i] = internalInventory[i].pattern;
        }
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
        List<String> ret = new ArrayList<>();
        ret.add(
            (getProxy() != null && getProxy().isActive())
                ? StatCollector.translateToLocal("GT5U.infodata.hatch.crafting_input_me.bus.online")
                : StatCollector.translateToLocalFormatted(
                    "GT5U.infodata.hatch.crafting_input_me.bus.offline",
                    getAEDiagnostics()));
        ret.add(
            StatCollector.translateToLocal(
                "GT5U.infodata.hatch.crafting_input_me.show_pattern." + (showPattern ? "enable" : "disabled")));
        ret.add(StatCollector.translateToLocal("GT5U.infodata.hatch.internal_inventory"));
        int i = 0;
        for (PatternSlot<MTEHatchCraftingInputME> slot : internalInventory) {
            if (slot == null) continue;
            if (slot.getPatternDetails() == null) continue;
            IWideReadableNumberConverter nc = ReadableNumberConverter.INSTANCE;

            i += 1;
            ret.add(
                StatCollector.translateToLocalFormatted(
                    "GT5U.infodata.hatch.internal_inventory.slot",
                    i,
                    EnumChatFormatting.BLUE + describePattern(slot.getPatternDetails()) + EnumChatFormatting.RESET));
            Map<GTUtility.ItemId, Long> itemMap = GTUtility.convertItemListToMap(slot.itemInventory);
            for (Map.Entry<GTUtility.ItemId, Long> entry : itemMap.entrySet()) {
                ItemStack item = entry.getKey()
                    .getItemStack();
                long amount = entry.getValue();
                ret.add(
                    item.getItem()
                        .getItemStackDisplayName(item) + ": "
                        + EnumChatFormatting.GOLD
                        + nc.toWideReadableForm(amount)
                        + EnumChatFormatting.RESET);
            }
            Map<Fluid, Long> fluidMap = GTUtility.convertFluidListToMap(slot.fluidInventory);
            for (Map.Entry<Fluid, Long> entry : fluidMap.entrySet()) {
                FluidStack fluid = new FluidStack(entry.getKey(), 1);
                long amount = entry.getValue();
                ret.add(
                    fluid.getLocalizedName() + ": "
                        + EnumChatFormatting.AQUA
                        + nc.toWideReadableForm(amount)
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
        return 176 + 16;
    }

    @Override
    public int getGUIHeight() {
        return 166;
    }

    @Override
    protected boolean useMui2() {
        return true;
    }

    @Override
    public ModularPanel buildUI(PosGuiData data, PanelSyncManager syncManager, UISettings uiSettings) {
        return new MTEHatchCraftingInputMEGui(this).build(data, syncManager, uiSettings);
    }

    @Override
    public void addUIWidgets(ModularWindow.@NotNull Builder builder, UIBuildContext buildContext) {
        buildContext.addSyncedWindow(MANUAL_SLOT_WINDOW, this::createSlotManualWindow);
        builder.widget(
            SlotGroup.ofItemHandler(inventoryHandler, 9)
                .startFromSlot(0)
                .endAtSlot(MAX_PATTERN_COUNT - 1)
                .phantom(false)
                .background(getGUITextureSet().getItemSlot(), GTUITextures.OVERLAY_SLOT_PATTERN_ME)
                .widgetCreator(slot -> new SlotWidget(slot) {

                    @Override
                    protected ItemStack getItemStackForRendering(Slot slotIn) {
                        ItemStack stack = slot.getStack();
                        if (stack == null || !(stack.getItem() instanceof ItemEncodedPattern patternItem)) {
                            return stack;
                        }
                        ItemStack output = patternItem.getOutput(stack);
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
                .setBackground(GTUITextures.BUTTON_STANDARD, GTUITextures.OVERLAY_BUTTON_PLUS_LARGE)
                .addTooltip(
                    StatCollector.translateToLocal("GT5U.gui.tooltip.hatch.crafting_input_me.place_manual_items"))
                .setSize(16, 16)
                .setPos(170, 46))
            .widget(new ButtonWidget().setOnClick((clickData, widget) -> {
                if (clickData.mouseButton == 0) {
                    refundAll(false);
                }
            })
                .setPlayClickSound(true)
                .setBackground(GTUITextures.BUTTON_STANDARD, GTUITextures.OVERLAY_BUTTON_EXPORT)
                .addTooltip(StatCollector.translateToLocal("GT5U.gui.tooltip.hatch.crafting_input_me.export"))
                .setSize(16, 16)
                .setPos(170, 28))
            .widget(
                new CycleButtonWidget()
                    .setToggle(() -> disablePatternOptimization, val -> disablePatternOptimization = val)
                    .setStaticTexture(GTUITextures.OVERLAY_BUTTON_PATTERN_OPTIMIZE)
                    .setVariableBackground(GTUITextures.BUTTON_STANDARD_TOGGLE)
                    .addTooltip(0, "Pattern Optimization:\n§7Allowed")
                    .addTooltip(1, "Pattern Optimization:\n§7Disabled")
                    .setPos(170, 10)
                    .setSize(16, 16))
            .widget(new ButtonWidget().setOnClick((clickData, widget) -> {
                int val = clickData.shift ? 1 : 0;
                if (clickData.mouseButton == 1) val |= 0b10;
                doublePatterns(val);
            })
                .setPlayClickSound(true)
                .setBackground(GTUITextures.BUTTON_STANDARD, GTUITextures.OVERLAY_BUTTON_X2)
                .addTooltip(StatCollector.translateToLocal("gui.tooltips.appliedenergistics2.DoublePatterns"))
                .setSize(16, 16)
                .setPos(194, 10))
            .widget(
                new ButtonWidget().setOnClick((clickData, widget) -> showPattern = !showPattern)
                    .setPlayClickSoundResource(
                        () -> showPattern ? SoundResource.GUI_BUTTON_UP.resourceLocation
                            : SoundResource.GUI_BUTTON_DOWN.resourceLocation)
                    .setBackground(() -> {
                        if (showPattern) {
                            return new IDrawable[] { GTUITextures.BUTTON_STANDARD_PRESSED,
                                GTUITextures.OVERLAY_BUTTON_WHITELIST };
                        } else {
                            return new IDrawable[] { GTUITextures.BUTTON_STANDARD,
                                GTUITextures.OVERLAY_BUTTON_BLACKLIST };
                        }
                    })
                    .attachSyncer(
                        new FakeSyncWidget.BooleanSyncer(() -> showPattern, val -> showPattern = val),
                        builder)
                    .dynamicTooltip(
                        () -> Collections.singletonList(
                            StatCollector.translateToLocal(
                                "GT5U.infodata.hatch.crafting_input_me.show_pattern."
                                    + (showPattern ? "enable" : "disabled"))))
                    .setTooltipShowUpDelay(TOOLTIP_DELAY)
                    .setUpdateTooltipEveryTick(true)
                    .setPos(194, 28)
                    .setSize(16, 16));
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

    public void onPatternChange(int index, ItemStack newItem) {
        if (!getBaseMetaTileEntity().isServerSide()) return;

        World world = getBaseMetaTileEntity().getWorld();

        // remove old if applicable
        PatternSlot<MTEHatchCraftingInputME> originalPattern = internalInventory[index];
        if (originalPattern != null) {
            if (originalPattern.hasChanged(newItem, world)) {
                try {
                    originalPattern.refund(getProxy(), getRequest(), true);
                    for (ProcessingLogic pl : processingLogics) {
                        pl.removeInventoryRecipeCache(originalPattern);
                    }
                } catch (GridAccessException ignored) {}
                internalInventory[index] = null;
                needPatternSync = true;
            } else {
                return; // nothing has changed
            }
        }

        // original does not exist or has changed
        if (newItem == null || !(newItem.getItem() instanceof ICraftingPatternItem)) return;

        PatternSlot<MTEHatchCraftingInputME> patternSlot = new PatternSlot<>(newItem, this);
        internalInventory[index] = patternSlot;
        if (patternSlot.getPatternDetails() != null) {
            patternDetailsPatternSlotMap.put(patternSlot.getPatternDetails(), patternSlot);
        }
        needPatternSync = true;
    }

    @Override
    public ItemStack[] getSharedItems() {
        ItemStack[] sharedItems = new ItemStack[SLOT_MANUAL_SIZE + 1];
        sharedItems[0] = mInventory[SLOT_CIRCUIT];
        System.arraycopy(mInventory, SLOT_MANUAL_START, sharedItems, 1, SLOT_MANUAL_SIZE);
        return ArrayExt.withoutNulls(sharedItems, ItemStack[]::new);
    }

    @Override
    public void setProcessingLogic(ProcessingLogic pl) {
        if (!processingLogics.contains(pl)) {
            processingLogics.add(Objects.requireNonNull(pl));
        }
    }

    @Override
    public void resetCraftingInputRecipeMap(ProcessingLogic pl) {
        for (PatternSlot<MTEHatchCraftingInputME> sl : internalInventory) {
            if (sl == null) continue;
            pl.removeInventoryRecipeCache(sl);
        }

    }

    @Override
    public void resetCraftingInputRecipeMap() {
        for (ProcessingLogic pl : processingLogics) {
            resetCraftingInputRecipeMap(pl);
        }
    }

    @Override
    public void getWailaBody(ItemStack itemStack, List<String> currenttip, IWailaDataAccessor accessor,
        IWailaConfigHandler config) {
        NBTTagCompound tag = accessor.getNBTData();
        if (tag.hasKey("name"))
            currenttip.add(EnumChatFormatting.AQUA + tag.getString("name") + EnumChatFormatting.RESET);
        currenttip.add(
            StatCollector.translateToLocal(
                "GT5U.infodata.hatch.crafting_input_me.show_pattern." + (showPattern ? "enable" : "disabled")));
        if (tag.hasKey("inventory")) {
            NBTTagList inventory = tag.getTagList("inventory", Constants.NBT.TAG_COMPOUND);
            for (int i = 0; i < inventory.tagCount(); ++i) {
                NBTTagCompound item = inventory.getCompoundTagAt(i);
                String name = item.getString("name");
                long amount = item.getLong("amount");
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
        tag.setBoolean("showPattern", showPattern);
        NBTTagList inventory = new NBTTagList();
        HashMap<String, Long> nameToAmount = new HashMap<>();
        for (Iterator<PatternSlot<MTEHatchCraftingInputME>> it = inventories(); it.hasNext();) {
            PatternSlot<MTEHatchCraftingInputME> i = it.next();
            for (ItemStack item : i.itemInventory) {
                if (item != null && item.stackSize > 0) {
                    String name = item.getDisplayName();
                    nameToAmount.merge(name, (long) item.stackSize, Long::sum);
                }
            }
            for (FluidStack fluid : i.fluidInventory) {
                if (fluid != null && fluid.amount > 0) {
                    String name = fluid.getLocalizedName();
                    nameToAmount.merge(name, (long) fluid.amount, Long::sum);
                }
            }
        }
        for (Map.Entry<String, Long> entry : nameToAmount.entrySet()) {
            NBTTagCompound item = new NBTTagCompound();
            item.setString("name", entry.getKey());
            item.setLong("amount", entry.getValue());
            inventory.appendTag(item);
        }

        tag.setTag("inventory", inventory);
        if (!Objects.equals(getName(), getLocalName())) {
            tag.setString("name", getName());
        }
        super.getWailaNBTData(player, tile, tag, world, x, y, z);
    }

    @Override
    public void provideCrafting(ICraftingProviderHelper craftingTracker) {
        if (!isActive()) return;

        for (PatternSlot<MTEHatchCraftingInputME> slot : internalInventory) {
            if (slot == null) continue;
            ICraftingPatternDetails details = slot.getPatternDetails();
            if (details == null) {
                GTMod.GT_FML_LOGGER.warn(
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
        if (!getBaseMetaTileEntity().isAllowedToWork()) return false;
        if (!(table instanceof MEInventoryCrafting meic)) return false;

        if (!supportFluids) {
            for (int i = 0; i < table.getSizeInventory(); ++i) {
                if (meic.getAEStackInSlot(i) instanceof IAEFluidStack) return false;
            }
        }
        if (!patternDetailsPatternSlotMap.get(patternDetails)
            .insertItemsAndFluids(meic)) {
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
    public Iterator<PatternSlot<MTEHatchCraftingInputME>> inventories() {
        return Arrays.stream(internalInventory)
            .filter(Objects::nonNull)
            .iterator();
    }

    @Override
    public void onBlockDestroyed() {
        refundAll(true);
        super.onBlockDestroyed();
    }

    public void refundAll(boolean shouldDrop) {
        for (PatternSlot<MTEHatchCraftingInputME> slot : internalInventory) {
            if (slot == null) continue;
            try {
                slot.refund(getProxy(), getRequest(), shouldDrop);
            } catch (GridAccessException ignored) {}
        }
    }

    @Override
    public boolean justUpdated() {
        boolean ret = justHadNewItems;
        justHadNewItems = false;
        return ret;
    }

    @Override
    public void onLeftclick(IGregTechTileEntity aBaseMetaTileEntity, EntityPlayer aPlayer) {
        if (!(aPlayer instanceof EntityPlayerMP)) return;

        ItemStack dataStick = aPlayer.inventory.getCurrentItem();
        if (!ItemList.Tool_DataStick.isStackEqual(dataStick, false, true)) return;

        this.saveToDataStick(aPlayer, dataStick);
    }

    public void saveToDataStick(EntityPlayer aPlayer, ItemStack dataStick) {
        IGregTechTileEntity aBaseMetaTileEntity = getBaseMetaTileEntity();
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
            IGregTechTileEntity te = getBaseMetaTileEntity();
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

    @Override
    public void markDirty() {
        super.markDirty();
        resetCraftingInputRecipeMap();
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
        builder.setBackground(GTUITextures.BACKGROUND_SINGLEBLOCK_DEFAULT);
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
                .widgetCreator(slot -> new SlotWidget(slot).setChangeListener(() -> resetCraftingInputRecipeMap()))
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
        for (PatternSlot<MTEHatchCraftingInputME> slot : internalInventory) {
            if (slot != null && !slot.isEmpty()) return Optional.of(slot);
        }
        return Optional.empty();
    }

    @Override
    public boolean supportsFluids() {
        return this.supportFluids;
    }

    @Override
    public List<ItemStack> getItemsForHoloGlasses() {
        List<ItemStack> list = new ArrayList<>();
        for (PatternSlot<MTEHatchCraftingInputME> slot : internalInventory) {
            if (slot == null) continue;
            if (slot.getPatternDetails() == null) continue;

            IAEItemStack[] outputs = slot.getPatternDetails()
                .getCondensedOutputs();
            list.add(outputs[0].getItemStack());
        }
        return list;
    }

    public void doublePatterns(int val) {
        boolean fast = (val & 1) != 0;
        boolean backwards = (val & 2) != 0;
        CraftingGridCache.pauseRebuilds();
        try {
            IInventory patterns = this.getPatterns();
            TileEntity te = this.getTileEntity();
            for (int i = 0; i < patterns.getSizeInventory(); i++) {
                ItemStack stack = patterns.getStackInSlot(i);
                if (stack != null && stack.getItem() instanceof ICraftingPatternItem cpi) {
                    ICraftingPatternDetails details = cpi.getPatternForItem(stack, te.getWorldObj());
                    if (details != null && !details.isCraftable()) {
                        int max = backwards ? PatternMultiplierHelper.getMaxBitDivider(details)
                            : PatternMultiplierHelper.getMaxBitMultiplier(details);
                        if (max > 0) {
                            ItemStack copy = stack.copy();
                            PatternMultiplierHelper
                                .applyModification(copy, (fast ? Math.min(3, max) : 1) * (backwards ? -1 : 1));
                            patterns.setInventorySlotContents(i, copy);
                        }
                    }
                }
            }
        } catch (Exception ignored) {}
        CraftingGridCache.unpauseRebuilds();
    }

    @Override
    public String[] getDescription() {
        if (supportFluids) return GTSplit.splitLocalizedFormatted(
            "gt.blockmachines.input_bus_crafting_me.desc",
            TIER_COLORS[10] + VN[10],
            StatCollector.translateToLocal("gt.blockmachines.input_bus_crafting_me.support_fluid.desc") + GTSplit.LB);
        return GTSplit.splitLocalizedFormatted(
            "gt.blockmachines.input_bus_crafting_me.desc",
            TIER_COLORS[6] + VN[6],
            StatCollector.translateToLocal("gt.blockmachines.input_bus_crafting_me.not_support_fluid.desc")
                + GTSplit.LB);
    }
}
