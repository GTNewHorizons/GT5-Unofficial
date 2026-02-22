package gregtech.common.tileentities.machines.multi;

import static com.gtnewhorizon.structurelib.structure.StructureUtility.ofBlock;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.ofBlockAnyMeta;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.ofChain;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.onElementPass;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.transpose;
import static gregtech.api.enums.HatchElement.Energy;
import static gregtech.api.enums.HatchElement.InputBus;
import static gregtech.api.enums.HatchElement.Maintenance;
import static gregtech.api.util.GTStructureUtility.buildHatchAdder;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.BiFunction;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.common.util.ForgeDirection;

import org.jetbrains.annotations.NotNull;

import com.gtnewhorizon.gtnhlib.util.map.ItemStackMap;
import com.gtnewhorizon.structurelib.alignment.constructable.ISurvivalConstructable;
import com.gtnewhorizon.structurelib.structure.IStructureDefinition;
import com.gtnewhorizon.structurelib.structure.IStructureElementCheckOnly;
import com.gtnewhorizon.structurelib.structure.ISurvivalBuildEnvironment;
import com.gtnewhorizon.structurelib.structure.StructureDefinition;
import com.mojang.authlib.GameProfile;

import appeng.api.AEApi;
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
import appeng.api.storage.data.IAEItemStack;
import appeng.api.storage.data.IItemList;
import appeng.api.util.DimensionalCoord;
import appeng.api.util.IInterfaceViewable;
import appeng.core.worlddata.WorldData;
import appeng.items.misc.ItemEncodedPattern;
import appeng.me.GridAccessException;
import appeng.me.GridNode;
import appeng.me.helpers.AENetworkProxy;
import appeng.me.helpers.IGridProxyable;
import appeng.util.Platform;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.network.NetworkRegistry;
import gregtech.api.GregTechAPI;
import gregtech.api.enums.GTValues;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.Textures;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.BaseMetaTileEntity;
import gregtech.api.metatileentity.implementations.MTEExtendedPowerMultiBlockBase;
import gregtech.api.metatileentity.implementations.MTEHatchInputBus;
import gregtech.api.net.GTPacketLMACraftingFX;
import gregtech.api.render.TextureFactory;
import gregtech.api.util.GTLog;
import gregtech.api.util.GTUtility;
import gregtech.api.util.MultiblockTooltipBuilder;
import gregtech.common.gui.modularui.multiblock.MTELargeMolecularAssemblerGui;
import gregtech.common.gui.modularui.multiblock.base.MTEMultiBlockBaseGui;
import gregtech.common.items.behaviors.BehaviourDataOrb;
import gregtech.common.tileentities.machines.MTEHatchCraftingInputME;
import gregtech.common.tileentities.machines.MTEHatchPatternProvider;
import gregtech.crossmod.ae2.InputBusInventoryProxy;

public class MTELargeMolecularAssembler extends MTEExtendedPowerMultiBlockBase<MTELargeMolecularAssembler>
    implements ICraftingProvider, IActionHost, IGridProxyable, IInterfaceViewable, ISurvivalConstructable {

    private static final String DATA_ORB_JOBS_KEY = "MX-CraftingJobs";
    private static final String DATA_ORB_JOBS_JOB_KEY = "Job";
    private static final String MACHINE_TYPE = "Molecular Assembler, LMA";
    private static final int EU_PER_TICK_BASIC = 16;
    private static final int EU_PER_TICK_CRAFTING = 64;
    private static final int CASING_INDEX = 48;
    private static final int MIN_CASING_COUNT = 24;
    private static final String DATA_ORB_TITLE = "AE-JOBS";
    private static final String NBT_KEY_CACHED_OUTPUTS = "cachedOutputs";
    private static final String NBT_KEY_CONFIG_HIDDEN_CRAFTING_FX = "config:hiddenCraftingFX";
    private static final int STRUCTURE_HORIZONTAL_OFFSET = 2;
    private static final int STRUCTURE_VERTICAL_OFFSET = 4;
    private static final int STRUCTURE_DEPTH_OFFSET = 0;
    private static final String STRUCTURE_PIECE_MAIN = "main";
    private static final IStructureDefinition<MTELargeMolecularAssembler> STRUCTURE_DEFINITION = StructureDefinition
        .<MTELargeMolecularAssembler>builder()
        .addShape(
            STRUCTURE_PIECE_MAIN,
            transpose(
                new String[][] { { "CCCCC", "CGGGC", "CGGGC", "CGGGC", "CCCCC" },
                    { "CGGGC", "G---G", "G---G", "G---G", "CGGGC" }, { "CGGGC", "G---G", "G-X-G", "G---G", "CGGGC" },
                    { "CGGGC", "G---G", "G---G", "G---G", "CGGGC" },
                    { "CC~CC", "CGGGC", "CGGGC", "CGGGC", "CCCCC" }, }))
        .addElement(
            'C',
            ofChain(
                buildHatchAdder(MTELargeMolecularAssembler.class).atLeast(Energy, InputBus, Maintenance)
                    .casingIndex(CASING_INDEX)
                    .hint(1)
                    .build(),
                onElementPass(it -> it.casing++, ofBlock(GregTechAPI.sBlockCasings4, 0))))
        .addElement(
            'G',
            ofBlockAnyMeta(
                AEApi.instance()
                    .definitions()
                    .blocks()
                    .quartzVibrantGlass()
                    .maybeBlock()
                    .get()))
        .addElement('X', (IStructureElementCheckOnly<MTELargeMolecularAssembler>) (mte, world, x, y, z) -> {
            if (world.isAirBlock(x, y, z)) {
                mte.craftingDisplayPoint = new CraftingDisplayPoint(world, x, y, z);
                return true;
            }
            return false;
        })
        .build();

    private byte casing;
    private CraftingDisplayPoint craftingDisplayPoint;

    private ItemStack cachedDataOrb;
    private List<List<ItemStack>> cachedAeJobs = new ArrayList<>();
    private boolean aeJobsDirty;

    private Map<ItemStack, ICraftingPatternDetails> cachedPatternDetail = new ItemStackMap<>(true);
    private int cachedInputBusCount = 0;

    private BaseActionSource requestSource;
    private IItemList<IAEItemStack> cachedOutputs = AEApi.instance()
        .storage()
        .createPrimitiveItemList();
    private boolean lastOutputFailed;
    private long lastOutputTick;
    private long tickCounter;

    public boolean hiddenCraftingFX;

    private AENetworkProxy gridProxy;
    private final InputBusInventoryProxy inventoryProxy = new InputBusInventoryProxy();

    public MTELargeMolecularAssembler(int aID, String aName, String aNameRegional) {
        super(aID, aName, aNameRegional);
    }

    public MTELargeMolecularAssembler(String aName) {
        super(aName);
    }

    @Override
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new MTELargeMolecularAssembler(this.mName);
    }

    @Override
    public ITexture[] getTexture(IGregTechTileEntity baseMetaTileEntity, ForgeDirection side, ForgeDirection facing,
        int colorIndex, boolean active, boolean redstoneLevel) {
        if (side == facing) {
            return new ITexture[] { Textures.BlockIcons.getCasingTextureForId(CASING_INDEX), TextureFactory.builder()
                .addIcon(Textures.BlockIcons.OVERLAY_ME_HATCH)
                .extFacing()
                .build() };
        }
        return new ITexture[] { Textures.BlockIcons.getCasingTextureForId(CASING_INDEX) };
    }

    @Override
    public boolean checkRecipe(ItemStack aStack) {
        return withAeJobs(($, aeJobs) -> {
            mMaxProgresstime = 20;
            long craftingProgressTime = 20;
            long craftingEUt = EU_PER_TICK_CRAFTING;
            lEUt = -EU_PER_TICK_BASIC;
            // Tier EU_PER_TICK_CRAFTING == 2
            int extraTier = Math.max(0, GTUtility.getTier(getMaxInputVoltage()) - 2);
            // The first two Overclocks reduce the Finish time to 0.5s and 0.25s
            for (int i = 0; i < 2; i++) {
                if (extraTier <= 0) break;
                craftingProgressTime /= 2;
                craftingEUt *= 4;
                extraTier--;
            }
            // Subsequent Overclocks Double the number of Jobs finished at once
            int parallel = 2 << extraTier;
            craftingEUt = craftingEUt << 2 * extraTier;
            List<ItemStack> outputs = new ArrayList<>();
            for (List<ItemStack> l : aeJobs.subList(0, Math.min(parallel, aeJobs.size()))) {
                outputs.addAll(l);
            }
            // Multiblock base already includes 1 parallel
            recipesDone += aeJobs.size() - 1;
            if (!outputs.isEmpty()) {
                aeJobs.subList(0, Math.min(parallel, aeJobs.size()))
                    .clear();
                aeJobsDirty = true;
                lEUt = -craftingEUt;
                mMaxProgresstime = (int) craftingProgressTime;
                mOutputItems = outputs.toArray(new ItemStack[0]);
                addCraftingFX(outputs.get(0));
            }
            mEfficiency = 10000 - (getIdealStatus() - getRepairStatus()) * 1000;
            mEfficiencyIncrease = 10000;
            return true;
        });
    }

    @Override
    public void saveNBTData(NBTTagCompound aNBT) {
        saveAeJobsIfNeeded();
        super.saveNBTData(aNBT);

        NBTTagList isList = new NBTTagList();
        for (IAEItemStack aeIS : cachedOutputs) {
            if (aeIS.getStackSize() <= 0) break;
            NBTTagCompound tag = new NBTTagCompound();
            NBTTagCompound isTag = new NBTTagCompound();
            aeIS.getItemStack()
                .writeToNBT(isTag);
            tag.setTag("itemStack", isTag);
            tag.setLong("size", aeIS.getStackSize());
            isList.appendTag(tag);
        }
        aNBT.setTag(NBT_KEY_CACHED_OUTPUTS, isList);

        aNBT.setBoolean(NBT_KEY_CONFIG_HIDDEN_CRAFTING_FX, hiddenCraftingFX);
        NBTTagCompound proxyTag = new NBTTagCompound();
        getProxy().writeToNBT(proxyTag);
        aNBT.setTag("proxy", proxyTag);
    }

    @Override
    public void loadNBTData(NBTTagCompound aNBT) {
        super.loadNBTData(aNBT);

        NBTBase nbtBase = aNBT.getTag(NBT_KEY_CACHED_OUTPUTS);
        if (nbtBase instanceof NBTTagList isList) {
            for (int i = 0; i < isList.tagCount(); i++) {
                NBTTagCompound tag = isList.getCompoundTagAt(i);
                NBTTagCompound isTag = tag.getCompoundTag("itemStack");
                long size = tag.getLong("size");
                ItemStack itemStack = GTUtility.loadItem(isTag);
                IAEItemStack aeIS = AEApi.instance()
                    .storage()
                    .createItemStack(itemStack);
                aeIS.setStackSize(size);
                cachedOutputs.add(aeIS);
            }
        }

        hiddenCraftingFX = aNBT.getBoolean(NBT_KEY_CONFIG_HIDDEN_CRAFTING_FX);
        if (aNBT.hasKey("proxy")) {
            getProxy().readFromNBT(aNBT.getCompoundTag("proxy"));
        }

        issuePatternChangeIfNeeded(0);
    }

    @Override
    protected MultiblockTooltipBuilder createTooltip() {
        return new MultiblockTooltipBuilder().addMachineType(MACHINE_TYPE)
            .addInfo("Needs a Data Orb to be placed in the controller")
            .addInfo(
                "Basic: " + EnumChatFormatting.GREEN
                    + EU_PER_TICK_BASIC
                    + EnumChatFormatting.GRAY
                    + " EU/t, unaffected by overclocking")
            .addInfo(
                "Crafting: " + EnumChatFormatting.GREEN
                    + EU_PER_TICK_CRAFTING
                    + EnumChatFormatting.GRAY
                    + " EU/t, Finish "
                    + EnumChatFormatting.WHITE
                    + "2"
                    + EnumChatFormatting.GRAY
                    + " Jobs in "
                    + EnumChatFormatting.WHITE
                    + "1"
                    + EnumChatFormatting.GRAY
                    + "s")
            .addInfo("The first two Overclocks:")
            .addInfo(
                "-Reduce the Finish time to " + EnumChatFormatting.WHITE
                    + "0.5"
                    + EnumChatFormatting.GRAY
                    + "s and "
                    + EnumChatFormatting.WHITE
                    + "0.25"
                    + EnumChatFormatting.GRAY
                    + "s")
            .addInfo("Subsequent Overclocks:")
            .addInfo("-Double the number of Jobs finished at once")
            .beginStructureBlock(5, 5, 5, true)
            .addController("Front center")
            .addCasingInfoMin("Robust Tungstensteel Machine Casing", MIN_CASING_COUNT, false)
            .addCasingInfoExactly(
                AEApi.instance()
                    .definitions()
                    .blocks()
                    .quartzVibrantGlass()
                    .maybeBlock()
                    .get()
                    .getLocalizedName(),
                54,
                false)
            .addInputBus("Any casing", 1)
            .addEnergyHatch("Any casing", 1)
            .addMaintenanceHatch("Any casing", 1)
            .toolTipFinisher();
    }

    @Override
    public boolean checkMachine(IGregTechTileEntity aBaseMetaTileEntity, ItemStack aStack) {
        casing = 0;
        if (!checkPiece(
            STRUCTURE_PIECE_MAIN,
            STRUCTURE_HORIZONTAL_OFFSET,
            STRUCTURE_VERTICAL_OFFSET,
            STRUCTURE_DEPTH_OFFSET)) {
            return false;
        }

        if (mMaintenanceHatches.size() != 1 || mEnergyHatches.isEmpty()) {
            return false;
        }

        return casing >= MIN_CASING_COUNT;
    }

    @Override
    public void construct(ItemStack stackSize, boolean hintsOnly) {
        buildPiece(
            STRUCTURE_PIECE_MAIN,
            stackSize,
            hintsOnly,
            STRUCTURE_HORIZONTAL_OFFSET,
            STRUCTURE_VERTICAL_OFFSET,
            STRUCTURE_DEPTH_OFFSET);
    }

    @Override
    public int survivalConstruct(ItemStack stackSize, int elementBudget, ISurvivalBuildEnvironment env) {
        if (mMachine) return -1;
        return survivalBuildPiece(
            STRUCTURE_PIECE_MAIN,
            stackSize,
            STRUCTURE_HORIZONTAL_OFFSET,
            STRUCTURE_VERTICAL_OFFSET,
            STRUCTURE_DEPTH_OFFSET,
            elementBudget,
            env,
            false,
            true);
    }

    @Override
    public IStructureDefinition<MTELargeMolecularAssembler> getStructureDefinition() {
        return STRUCTURE_DEFINITION;
    }

    @Override
    public boolean addItemOutputs(ItemStack[] outputItems) {
        for (ItemStack stack : outputItems) {
            cachedOutputs.add(
                AEApi.instance()
                    .storage()
                    .createItemStack(stack));
        }

        markDirty();

        return true;
    }

    @Override
    public void onPostTick(IGregTechTileEntity aBaseMetaTileEntity, long aTick) {
        super.onPostTick(aBaseMetaTileEntity, aTick);
        if (aBaseMetaTileEntity.isServerSide()) {
            flushCachedOutputsIfNeeded(aTick);
            saveAeJobsIfNeeded();
            syncAEProxyActive(aBaseMetaTileEntity);
            issuePatternChangeIfNeeded(aTick);
        }
    }

    // dataOrb, aeJobs
    private boolean withAeJobs(BiFunction<ItemStack, List<List<ItemStack>>, Boolean> action) {
        if (mInventory[1] == cachedDataOrb && cachedDataOrb != null) {
            return action.apply(cachedDataOrb, cachedAeJobs);
        }
        if (!ItemList.Tool_DataOrb.isStackEqual(mInventory[1], false, true)) {
            cachedDataOrb = null;
            cachedAeJobs = null;
            return false;
        }
        ItemStack dataOrb = mInventory[1];
        String dataTitle = BehaviourDataOrb.getDataTitle(dataOrb);
        if (dataTitle == null || dataTitle.isEmpty()) {
            dataTitle = DATA_ORB_TITLE;
            BehaviourDataOrb.setDataName(dataOrb, dataTitle);
            BehaviourDataOrb.setNBTInventory(dataOrb, GTValues.emptyItemStackArray);
        }
        if (!dataTitle.equals(DATA_ORB_TITLE)) {
            cachedDataOrb = null;
            cachedAeJobs = null;
            return false;
        }
        cachedDataOrb = dataOrb;
        NBTTagCompound tag = dataOrb.getTagCompound();
        if (tag != null && tag.hasKey("Inventory", Constants.NBT.TAG_LIST)) {
            List<List<ItemStack>> jobs = new ArrayList<>();
            ItemStack[] stacks = BehaviourDataOrb.getNBTInventory(dataOrb);
            for (ItemStack stack : stacks) {
                if (stack == null) continue;
                List<ItemStack> l = new ArrayList<>();
                l.add(stack);
                jobs.add(l);
            }
            cachedAeJobs = jobs;
        } else if (tag != null && tag.hasKey(DATA_ORB_JOBS_KEY, Constants.NBT.TAG_LIST)) {
            List<List<ItemStack>> jobs = new ArrayList<>();
            NBTTagList tagList = tag.getTagList(DATA_ORB_JOBS_KEY, Constants.NBT.TAG_COMPOUND);
            for (int i = 0; i < tagList.tagCount(); i++) {
                NBTTagCompound jobTag = tagList.getCompoundTagAt(i);
                NBTTagList jobTagList = jobTag.getTagList(DATA_ORB_JOBS_JOB_KEY, Constants.NBT.TAG_COMPOUND);
                List<ItemStack> stacks = new ArrayList<>();
                for (int j = 0; j < jobTagList.tagCount(); j++) {
                    ItemStack stack = GTUtility.loadItem(jobTagList.getCompoundTagAt(j));
                    stacks.add(stack);
                }
                jobs.add(stacks);
            }
            cachedAeJobs = jobs;
        } else {
            cachedAeJobs = new ArrayList<>();
        }
        return action.apply(cachedDataOrb, cachedAeJobs);
    }

    private void addCraftingFX(ItemStack itemStack) {
        if (hiddenCraftingFX) return;
        if (craftingDisplayPoint != null) {
            GTValues.NW.sendToAllAround(
                new GTPacketLMACraftingFX(
                    craftingDisplayPoint.x,
                    craftingDisplayPoint.y,
                    craftingDisplayPoint.z,
                    mMaxProgresstime,
                    AEApi.instance()
                        .storage()
                        .createItemStack(itemStack)),
                new NetworkRegistry.TargetPoint(
                    craftingDisplayPoint.w.provider.dimensionId,
                    craftingDisplayPoint.x,
                    craftingDisplayPoint.y,
                    craftingDisplayPoint.z,
                    64));
        }
    }

    private BaseActionSource getRequest() {
        if (requestSource == null) {
            requestSource = new MachineSource((IActionHost) getBaseMetaTileEntity());
        }
        return requestSource;
    }

    private void flushCachedOutputsIfNeeded(long tick) {
        tickCounter = tick;
        if (tickCounter <= lastOutputTick + 40) {
            return;
        }

        lastOutputFailed = true;
        try {
            AENetworkProxy proxy = getProxy();
            if (proxy == null) return;
            IMEMonitor<IAEItemStack> storage = proxy.getStorage()
                .getItemInventory();
            for (IAEItemStack s : cachedOutputs) {
                if (s.getStackSize() == 0) {
                    continue;
                }
                IAEItemStack rest = Platform.poweredInsert(proxy.getEnergy(), storage, s, getRequest());
                if (rest != null && rest.getStackSize() > 0) {
                    lastOutputFailed = true;
                    s.setStackSize(rest.getStackSize());
                    break;
                }
                s.setStackSize(0);
            }
        } catch (GridAccessException ignored) {
            lastOutputFailed = true;
        }
        lastOutputTick = tickCounter;
    }

    private void saveAeJobsIfNeeded() {
        if (!aeJobsDirty) {
            return;
        }
        withAeJobs((dataOrb, aeJobs) -> {
            NBTTagList jobList = new NBTTagList();
            for (var job : aeJobs) {
                NBTTagCompound jobTag = new NBTTagCompound();
                NBTTagList itemList = new NBTTagList();
                for (var item : job) {
                    itemList.appendTag(GTUtility.saveItem(item));
                }
                jobTag.setTag(DATA_ORB_JOBS_JOB_KEY, itemList);
            }
            dataOrb.stackTagCompound.setTag(DATA_ORB_JOBS_KEY, jobList);
            markDirty();
            aeJobsDirty = false;
            return true;
        });
    }

    private void issuePatternChangeIfNeeded(long tick) {
        if (tick % 20 != 0) {
            return;
        }

        List<MTEHatchInputBus> inputs = GTUtility.filterValidMTEs(mInputBusses)
            .stream()
            .filter(bus -> !(bus instanceof MTEHatchCraftingInputME))
            .collect(Collectors.toList());

        boolean changed = false;
        Map<ItemStack, ICraftingPatternDetails> patterns = new ItemStackMap<>(true);

        for (ItemStack is : inputs.stream()
            .flatMap(
                bus -> IntStream.range(0, bus.getSizeInventory())
                    .mapToObj(bus::getStackInSlot))
            .filter(Objects::nonNull)
            .collect(Collectors.toSet())) {
            if (!(is.getItem() instanceof ItemEncodedPattern pattern)) {
                continue;
            }

            var cachedDetail = cachedPatternDetail.get(is);
            if (cachedDetail != null) {
                patterns.put(is, cachedDetail);
            } else {
                ICraftingPatternDetails detail = pattern.getPatternForItem(is, getBaseMetaTileEntity().getWorld());
                if (detail != null && detail.isCraftable()) {
                    patterns.put(is, detail);
                    changed = true;
                }

            }
        }

        changed |= patterns.size() != cachedPatternDetail.size();
        changed |= inputs.size() != cachedInputBusCount;
        cachedPatternDetail = patterns;
        cachedInputBusCount = inputs.size();

        if (changed) {
            final var patternInputs = inputs.stream()
                .filter(mte -> mte instanceof MTEHatchPatternProvider)
                .map(mte -> (MTEHatchPatternProvider) mte)
                .collect(Collectors.toList());

            inventoryProxy.setInputs(patternInputs);
            try {
                AENetworkProxy proxy = getProxy();
                if (proxy == null) return;
                proxy.getGrid()
                    .postEvent(new MENetworkCraftingPatternChange(this, getProxy().getNode()));
            } catch (GridAccessException ignored) {}
        }
    }

    private void syncAEProxyActive(IGregTechTileEntity baseMetaTileEntity) {
        AENetworkProxy proxy = getProxy();
        if (proxy == null) return;
        if (baseMetaTileEntity.isActive()) {
            if (!proxy.isReady()) {
                proxy.onReady();
                IGridNode node = proxy.getNode();
                if (node == null) return;
                if (node.getPlayerID() == -1) {
                    GTLog.out.printf(
                        "Found a LMA at %s without valid AE playerID.\n",
                        ((BaseMetaTileEntity) baseMetaTileEntity).getLocation());
                    GTLog.out.println("Try to recover playerID with UUID: " + baseMetaTileEntity.getOwnerUuid());
                    // recover ID from old version
                    int playerAEID = WorldData.instance()
                        .playerData()
                        .getPlayerID(
                            new GameProfile(baseMetaTileEntity.getOwnerUuid(), baseMetaTileEntity.getOwnerName()));
                    node.setPlayerID(playerAEID);
                    ((GridNode) node).setLastSecurityKey(-1);
                    node.updateState(); // refresh the security connection
                    GTLog.out.println("Now it has playerID: " + playerAEID);
                }
            }

            if (proxy.getConnectableSides()
                .isEmpty()) {
                proxy.setValidSides(EnumSet.complementOf(EnumSet.of(ForgeDirection.UNKNOWN)));
            }
        } else {
            if (!proxy.getConnectableSides()
                .isEmpty()) {
                proxy.setValidSides(EnumSet.noneOf(ForgeDirection.class));
            }
        }
    }

    private boolean addToLargeMolecularAssemblerList(IGregTechTileEntity tileEntity, short baseCasingIndex) {
        if (addMaintenanceToMachineList(tileEntity, baseCasingIndex)) return true;
        if (addInputToMachineList(tileEntity, baseCasingIndex)) return true;
        return addEnergyInputToMachineList(tileEntity, baseCasingIndex);
    }

    @Override
    public boolean pushPattern(ICraftingPatternDetails patternDetails, InventoryCrafting table) {
        return withAeJobs(($, aeJobs) -> {
            World w = getBaseMetaTileEntity().getWorld();
            ItemStack mainOutput = patternDetails.getOutput(table, w);
            if (mainOutput == null) return false;

            FMLCommonHandler.instance()
                .firePlayerCraftingEvent(Platform.getPlayer((WorldServer) w), mainOutput, table);

            List<ItemStack> leftover = IntStream.range(0, table.getSizeInventory() + 1)
                .mapToObj(i -> Platform.getContainerItem(table.getStackInSlot(i)))
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
            leftover.add(mainOutput);
            aeJobs.add(leftover);
            aeJobsDirty = true;
            return true;
        });
    }

    @Override
    public boolean isBusy() {
        return withAeJobs(($, aeJobs) -> aeJobs.size() >= 256);
    }

    @Override
    public void provideCrafting(ICraftingProviderHelper craftingTracker) {
        AENetworkProxy proxy = getProxy();
        if (proxy == null) return;
        if (proxy.isReady()) {
            for (ICraftingPatternDetails detail : cachedPatternDetail.values()) {
                craftingTracker.addCraftingOption(this, detail);
            }
        }
    }

    @Override
    public AENetworkProxy getProxy() {
        if (gridProxy == null) {
            gridProxy = new AENetworkProxy(this, "proxy", getStackForm(1), true);
            gridProxy.setFlags(GridFlags.REQUIRE_CHANNEL);
            if (getBaseMetaTileEntity().getWorld() != null) {
                EntityPlayer player = getBaseMetaTileEntity().getWorld()
                    .getPlayerEntityByName(getBaseMetaTileEntity().getOwnerName());
                gridProxy.setOwner(player);
            }
        }
        return gridProxy;
    }

    @Override
    public IGridNode getGridNode(ForgeDirection dir) {
        AENetworkProxy proxy = getProxy();
        if (proxy == null) return null;
        return proxy.getNode();
    }

    @Override
    public void securityBreak() {
        getBaseMetaTileEntity().disableWorking();
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
    public ItemStack getCrafterIcon() {
        return this.getMachineCraftingIcon();
    }

    @Override
    public int rows() {
        // slots/9, rounded up
        return (inventoryProxy.getSizeInventory() + 8) / 9;
    }

    @Override
    public int rowSize() {
        return 9;
    }

    @Override
    public int numSlots() {
        return inventoryProxy.getSizeInventory();
    }

    @Override
    public IInventory getPatterns() {
        return inventoryProxy;
    }

    public String getName() {
        final var crafterIcon = getCrafterIcon();
        if (crafterIcon != null) {
            return crafterIcon.getDisplayName();
        } else {
            return getLocalName();
        }
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
    public boolean allowsPatternOptimization() {
        // crafting patterns must not be optimized (i think?)
        return false;
    }

    @Override
    public IGridNode getActionableNode() {
        return getProxy().getNode();
    }

    @Override
    protected @NotNull MTEMultiBlockBaseGui<?> getGui() {
        return new MTELargeMolecularAssemblerGui(this);
    }

    private static class CraftingDisplayPoint {

        private final World w;
        private final int x;
        private final int y;
        private final int z;

        private CraftingDisplayPoint(World w, int x, int y, int z) {
            this.w = w;
            this.x = x;
            this.y = y;
            this.z = z;
        }
    }

    public boolean isHiddenCraftingFX() {
        return hiddenCraftingFX;
    }

    public void setHiddenCraftingFX(boolean hiddenCraftingFX) {
        this.hiddenCraftingFX = hiddenCraftingFX;
    }

}
