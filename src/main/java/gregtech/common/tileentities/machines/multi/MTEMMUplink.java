package gregtech.common.tileentities.machines.multi;

import static com.gtnewhorizon.structurelib.structure.StructureUtility.ofBlock;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.onElementPass;
import static gregtech.api.enums.GTValues.AuthorPineapple;
import static gregtech.api.enums.HatchElement.Energy;
import static gregtech.api.enums.HatchElement.InputHatch;
import static gregtech.api.enums.HatchElement.Maintenance;
import static gregtech.api.enums.Textures.BlockIcons.getCasingTextureForId;
import static gregtech.api.util.GTStructureUtility.buildHatchAdder;
import static gregtech.api.util.GTStructureUtility.ofFrame;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import javax.annotation.Nonnull;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTankInfo;

import org.joml.Vector3i;
import org.spongepowered.libraries.com.google.common.collect.MapMaker;

import com.gtnewhorizon.gtnhlib.util.map.ItemStackMap;
import com.gtnewhorizon.structurelib.alignment.constructable.ISurvivalConstructable;
import com.gtnewhorizon.structurelib.structure.IStructureDefinition;
import com.gtnewhorizon.structurelib.structure.ISurvivalBuildEnvironment;
import com.gtnewhorizon.structurelib.structure.StructureDefinition;

import appeng.api.config.Actionable;
import appeng.api.networking.storage.IStorageGrid;
import appeng.api.storage.IMEMonitor;
import appeng.api.storage.data.IAEFluidStack;
import appeng.api.storage.data.IAEItemStack;
import appeng.util.item.AEItemStack;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import gregtech.api.GregTechAPI;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.Materials;
import gregtech.api.enums.Textures;
import gregtech.api.enums.TierEU;
import gregtech.api.interfaces.IHatchElement;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.implementations.MTEEnhancedMultiBlockBase;
import gregtech.api.metatileentity.implementations.MTEHatchInput;
import gregtech.api.recipe.RecipeMaps;
import gregtech.api.recipe.check.CheckRecipeResult;
import gregtech.api.recipe.check.CheckRecipeResultRegistry;
import gregtech.api.recipe.maps.FuelBackend;
import gregtech.api.render.TextureFactory;
import gregtech.api.util.GTRecipe;
import gregtech.api.util.GTUtility;
import gregtech.api.util.IGTHatchAdder;
import gregtech.api.util.MultiblockTooltipBuilder;
import gregtech.api.util.shutdown.ShutDownReason;
import gregtech.common.blocks.BlockCasingsAbstract;
import gregtech.common.items.matterManipulator.ItemMatterManipulator;
import gregtech.common.items.matterManipulator.MatterManipulator;
import gregtech.common.tileentities.machines.MTEMMUplinkMEHatch;
import gtPlusPlus.core.block.ModBlocks;
import it.unimi.dsi.fastutil.chars.Char2IntArrayMap;
import mcp.mobius.waila.api.IWailaConfigHandler;
import mcp.mobius.waila.api.IWailaDataAccessor;

public class MTEMMUplink extends MTEEnhancedMultiBlockBase<MTEMMUplink> implements ISurvivalConstructable {

    public static final long BASE_PLASMA_EU_COST = 16_384;

    private long pendingPlasmaEU = 0;
    private long address = 0;

    private ArrayList<MTEMMUplinkMEHatch> uplinkHatches = new ArrayList<>();

    public MTEMMUplink(final int aID, final String aName, final String aNameRegional) {
        super(aID, aName, aNameRegional);
    }

    public MTEMMUplink(String aName) {
        super(aName);
    }

    @Override
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new MTEMMUplink(mName);
    }

    // #region Structure
    // spotless:off
    private static String[][] getStructure() {
        return new String[][]{{
            "         ",
            "         ",
            "         ",
            "         ",
            "  AA~AA  ",
            "         ",
            "         ",
            "         ",
            "         "
        },{
            "         ",
            "         ",
            "  A   A  ",
            " AA   AA ",
            " A     A ",
            " AA   AA ",
            "  A   A  ",
            "         ",
            "         "
        },{
            "         ",
            "  A   A  ",
            " ACCCCCA ",
            " AD   DA ",
            "A D   D A",
            " AD   DA ",
            " ACCCCCA ",
            "  A   A  ",
            "         "
        },{
            "         ",
            " AA   AA ",
            " AD   DA ",
            "A       A",
            "A       A",
            "A       A",
            " AD   DA ",
            " AA   AA ",
            "         "
        },{
            "  A   A  ",
            " A     A ",
            "A D   D A",
            "A       A",
            "ABBE EBBA",
            "A       A",
            "A D   D A",
            " A     A ",
            "  A   A  "
        },{
            "         ",
            " AA   AA ",
            " AD   DA ",
            "A       A",
            "A       A",
            "A       A",
            " AD   DA ",
            " AA   AA ",
            "         "
        },{
            "         ",
            "  A   A  ",
            " ACCCCCA ",
            " AD   DA ",
            "A D   D A",
            " AD   DA ",
            " ACCCCCA ",
            "  A   A  ",
            "         "
        },{
            "         ",
            "         ",
            "  A   A  ",
            " AA   AA ",
            " A     A ",
            " AA   AA ",
            "  A   A  ",
            "         ",
            "         "
        },{
            "         ",
            "         ",
            "         ",
            "         ",
            "  A   A  ",
            "         ",
            "         ",
            "         ",
            "         "
        }};
    }
    // spotless:on

    private static final String STRUCTURE_PIECE_MAIN = "main";

    private BasicStructureWrapper structure = new BasicStructureWrapper();

    @Override
    public IStructureDefinition<MTEMMUplink> getStructureDefinition() {
        return structure.getStructureDefinition();
    }

    private static class UplinkHatchAdder implements IHatchElement<MTEMMUplink> {

        @Override
        public List<? extends Class<? extends IMetaTileEntity>> mteClasses() {
            return Arrays.asList(MTEMMUplinkMEHatch.class);
        }

        @Override
        public IGTHatchAdder<? super MTEMMUplink> adder() {
            return (uplink, hatchTE, aBaseCasingIndex) -> {
                if (hatchTE == null || hatchTE.isDead()) return false;

                IMetaTileEntity aMetaTileEntity = hatchTE.getMetaTileEntity();

                if (aMetaTileEntity == null) return false;

                if (!(aMetaTileEntity instanceof MTEMMUplinkMEHatch uplinkHatch)) return false;

                uplink.uplinkHatches.add(uplinkHatch);
                uplinkHatch.updateTexture(aBaseCasingIndex);
                uplinkHatch.updateCraftingIcon(uplink.getMachineCraftingIcon());

                return true;
            };
        }

        @Override
        public String name() {
            return "uplink-hatch-adder";
        }

        @Override
        public long count(MTEMMUplink t) {
            return t.uplinkHatches.size();
        }
    }

    public class BasicStructureWrapper {

        public String[][] defText;

        public int casingCount;
        public boolean badCasingCount;
        public IStructureDefinition<MTEMMUplink> structureDefinition;

        public Vector3i offset;
        public Char2IntArrayMap defCasingCounts;

        public char casingChar = 'A';
        public int maxHatches = 8;
        public int width, height, length;

        public IStructureDefinition<MTEMMUplink> getStructureDefinition() {
            String[][] structure = getStructure();

            if (!Objects.equals(structure, defText)) {
                defText = structure;
                defCasingCounts = new Char2IntArrayMap();

                width = 0;
                height = 0;
                length = defText.length;

                int z = 0;
                for (String[] a : defText) {
                    int y = 0;
                    height = Math.max(height, a.length);
                    for (String b : a) {
                        width = Math.max(width, b.length());
                        for (int x = 0; x < b.length(); x++) {
                            char c = b.charAt(x);
                            defCasingCounts.put(c, defCasingCounts.getOrDefault(c, 0) + 1);

                            if (c == '~') {
                                offset = new Vector3i(x, y, z);
                            }
                        }
                        y++;
                    }
                    z++;
                }

                structureDefinition = StructureDefinition.<MTEMMUplink>builder()
                    .addShape(STRUCTURE_PIECE_MAIN, defText)
                    .addElement(
                        'A',
                        buildHatchAdder(MTEMMUplink.class)
                            .atLeast(InputHatch, Energy, Maintenance, new UplinkHatchAdder())
                            .casingIndex(getCasingIndex())
                            .dot(1)
                            .buildAndChain(onElementPass(this::onCasingAdded, ofBlock(GregTechAPI.sBlockCasings8, 7))))
                    .addElement('B', ofFrame(Materials.NaquadahAlloy))
                    .addElement('C', ofFrame(Materials.Trinium))
                    .addElement('D', ofBlock(ModBlocks.blockCasingsMisc, 8))
                    .addElement('E', ofBlock(GregTechAPI.sBlockCasings8, 10))
                    .build();
            }

            return structureDefinition;
        }

        private void onCasingAdded(MTEMMUplink ignored) {
            casingCount++;
        }

        public boolean checkStructure() {
            getStructureDefinition();

            casingCount = 0;
            badCasingCount = false;
            uplinkHatches.clear();

            if (!checkPiece(STRUCTURE_PIECE_MAIN, offset.x, offset.y, offset.z)) {
                return false;
            }

            if (casingCount < (defCasingCounts.get('A') - maxHatches)) {
                badCasingCount = true;
                return false;
            }

            return true;
        }

        public void construct(ItemStack stackSize, boolean hintsOnly) {
            getStructureDefinition();

            buildPiece(STRUCTURE_PIECE_MAIN, stackSize, hintsOnly, offset.x, offset.y, offset.z);
        }

        public int survivalConstruct(ItemStack stackSize, int elementBudget, ISurvivalBuildEnvironment env) {
            getStructureDefinition();

            if (mMachine) return -1;
            return survivialBuildPiece(
                STRUCTURE_PIECE_MAIN,
                stackSize,
                offset.x,
                offset.y,
                offset.z,
                elementBudget,
                env,
                false,
                true);
        }
    }

    @Override
    public void construct(ItemStack stackSize, boolean hintsOnly) {
        structure.construct(stackSize, hintsOnly);
    }

    @Override
    public int survivalConstruct(ItemStack stackSize, int elementBudget, ISurvivalBuildEnvironment env) {
        return structure.survivalConstruct(stackSize, elementBudget, env);
    }

    @Override
    public boolean checkMachine(IGregTechTileEntity aBaseMetaTileEntity, ItemStack aStack) {
        return structure.checkStructure();
    }

    // #endregion

    // #region Misc boilerplate

    private int getCasingIndex() {
        return ((BlockCasingsAbstract) GregTechAPI.sBlockCasings8).getTextureIndex(7); // Advanced Iridium Plated
                                                                                       // Machine Casing
    }

    private static final Textures.BlockIcons.CustomIcon ACTIVE_GLOW = new Textures.BlockIcons.CustomIcon(
        "multitileentity/mmuplink/OVERLAY_FRONT_ACTIVE_GLOW");
    private static final Textures.BlockIcons.CustomIcon IDLE_GLOW = new Textures.BlockIcons.CustomIcon(
        "multitileentity/mmuplink/OVERLAY_FRONT_IDLE_GLOW");
    private static final Textures.BlockIcons.CustomIcon OFF = new Textures.BlockIcons.CustomIcon(
        "multitileentity/mmuplink/OVERLAY_FRONT_OFF");

    @Override
    public ITexture[] getTexture(IGregTechTileEntity baseMetaTileEntity, ForgeDirection side, ForgeDirection facing,
        int colorIndex, boolean active, boolean redstoneLevel) {
        int casing = getCasingIndex();

        List<ITexture> textures = new ArrayList<>(3);

        textures.add(getCasingTextureForId(casing));

        if (side == facing) {
            textures.add(
                TextureFactory.builder()
                    .addIcon(OFF)
                    .extFacing()
                    .build());

            switch (getState()) {
                case OFF: {
                    break;
                }
                case IDLE: {
                    textures.add(
                        TextureFactory.builder()
                            .addIcon(IDLE_GLOW)
                            .extFacing()
                            .glow()
                            .build());
                    break;
                }
                case ACTIVE: {
                    textures.add(
                        TextureFactory.builder()
                            .addIcon(ACTIVE_GLOW)
                            .extFacing()
                            .glow()
                            .build());
                    break;
                }
            }
        }
        return textures.toArray(new ITexture[textures.size()]);
    }

    public static enum UplinkState {
        OFF,
        IDLE,
        ACTIVE,
    }

    @SideOnly(Side.CLIENT)
    private UplinkState state;

    public UplinkState getState() {
        if (getBaseMetaTileEntity().isServerSide()) {
            if (getBaseMetaTileEntity().isActive()) {
                if (uplinkHatches.stream()
                    .anyMatch(hatch -> hatch.hasAnyRequests())) {
                    return UplinkState.ACTIVE;
                } else {
                    return UplinkState.IDLE;
                }
            } else {
                return UplinkState.OFF;
            }
        } else {
            if (state == null) state = UplinkState.OFF;

            return state;
        }
    }

    @SideOnly(Side.CLIENT)
    public void setState(UplinkState state) {
        this.state = state;
        getBaseMetaTileEntity().issueTextureUpdate();
    }

    @Override
    protected MultiblockTooltipBuilder createTooltip() {
        MultiblockTooltipBuilder tt = new MultiblockTooltipBuilder();

        structure.getStructureDefinition();

        // spotless:off
        tt.addMachineType("Matter Manipulator Quantum Uplink")
            .addInfo("Interdimensional and infinite range uplink for matter manipulators.")
            .addInfo("Connects directly to an ME system via a " + EnumChatFormatting.GOLD + ItemList.Hatch_MatterManipulatorUplink_ME.get(0).getDisplayName() + EnumChatFormatting.GRAY + ".")
            .addInfo("Consumes 1A ZPM while active.")
            .addInfo("Must be fed with plasma via an input hatch.")
            .addInfo("Transfers to/from the manipulator cost " + String.format("%,d", BASE_PLASMA_EU_COST) + " EU in plasma per item or per bucket.")
            .addInfo("Insert a compatible manipulator in the controller slot while the machine is running to bind it to the uplink.")
            .beginStructureBlock(structure.width, structure.height, structure.length, true)
            .addController("Front Center")
            .addCasingInfoRange("Advanced Iridium Plated Machine Casing", structure.defCasingCounts.get('A') - structure.maxHatches, structure.defCasingCounts.get('A'), false)
            .addCasingInfoExactly("Trinium Frame Box", structure.defCasingCounts.get('C'), false)
            .addCasingInfoExactly("Matter Generation Coil", structure.defCasingCounts.get('D'), false)
            .addCasingInfoExactly("Naquadah Alloy Frame Box", structure.defCasingCounts.get('B'), false)
            .addCasingInfoExactly("Radiant Naquadah Alloy Casing", structure.defCasingCounts.get('E'), false)
            .addInputHatch("Any Advanced Iridium Plated Machine Casing", 1)
            .addEnergyHatch("Any Advanced Iridium Plated Machine Casing", 1)
            .addMaintenanceHatch("Any Advanced Iridium Plated Machine Casing", 1)
            .addOtherStructurePart(ItemList.Hatch_MatterManipulatorUplink_ME.get(1).getDisplayName(), "Any Advanced Iridium Plated Machine Casing", 1)
            .toolTipFinisher(AuthorPineapple);
        // spotless:on

        return tt;
    }

    @Override
    public boolean isCorrectMachinePart(ItemStack aStack) {
        return true;
    }

    @Override
    public int getMaxEfficiency(ItemStack aStack) {
        return 10_000;
    }

    @Override
    public int getDamageToComponent(ItemStack aStack) {
        return 0;
    }

    @Override
    public boolean explodesOnComponentBreak(ItemStack aStack) {
        return false;
    }

    // #endregion

    @Override
    public void loadNBTData(NBTTagCompound aNBT) {
        super.loadNBTData(aNBT);

        address = aNBT.getLong("address");
        pendingPlasmaEU = aNBT.getLong("plasmaEU");

        if (address == 0) address = newAddress();
    }

    @Override
    public void getWailaBody(ItemStack itemStack, List<String> currentTip, IWailaDataAccessor accessor,
        IWailaConfigHandler config) {
        super.getWailaBody(itemStack, currentTip, accessor, config);
        currentTip.add(
            String.format(
                "Address: %x",
                accessor.getNBTData()
                    .getLong("address")));
    }

    @Override
    public void getWailaNBTData(EntityPlayerMP player, TileEntity tile, NBTTagCompound tag, World world, int x, int y,
        int z) {
        super.getWailaNBTData(player, tile, tag, world, x, y, z);
        tag.setLong("address", address);
    }

    @Override
    public void addAdditionalTooltipInformation(ItemStack stack, List<String> tooltip) {
        super.addAdditionalTooltipInformation(stack, tooltip);

        if (stack.getTagCompound() != null) {
            tooltip.add(
                String.format(
                    "Address: %x",
                    stack.getTagCompound()
                        .getLong("address")));
        }
    }

    @Override
    public String[] getInfoData() {
        List<String> info = new ArrayList<>(Arrays.asList(super.getInfoData()));

        info.add(
            String.format(
                "Stored Plasma: %s%,d%s EU",
                EnumChatFormatting.YELLOW,
                pendingPlasmaEU,
                EnumChatFormatting.WHITE));

        return info.toArray(new String[0]);
    }

    @Override
    public void saveNBTData(NBTTagCompound aNBT) {
        super.saveNBTData(aNBT);

        aNBT.setLong("address", address);
        aNBT.setLong("plasmaEU", pendingPlasmaEU);
    }

    @Override
    public void setItemNBT(NBTTagCompound aNBT) {
        aNBT.setLong("address", address);
    }

    @Override
    public void initDefaultModes(NBTTagCompound aNBT) {
        address = aNBT == null ? newAddress() : aNBT.getLong("address");
    }

    private static long newAddress() {
        return (long) (Long.MAX_VALUE * Math.random());
    }

    private MTEMMUplinkMEHatch getMEHatch() {
        for (MTEMMUplinkMEHatch hatch : uplinkHatches) {
            if (hatch != null && hatch.isActive() && hatch.isPowered()) {
                return hatch;
            }
        }

        return null;
    }

    public static enum UplinkStatus {

        OK,
        NO_PLASMA,
        AE_OFFLINE,
        NO_HATCH;

        @Override
        public String toString() {
            return switch (this) {
                case OK -> "ok";
                case NO_PLASMA -> "insufficient plasma";
                case AE_OFFLINE -> "could not connect to the ME system";
                case NO_HATCH -> "missing ME hatch";
            };
        }
    }

    public UplinkStatus tryConsumeItems(ItemStackMap<Long> requestedItems, boolean simulate) {
        MTEMMUplinkMEHatch hatch = getMEHatch();

        if (hatch == null) return UplinkStatus.NO_HATCH;

        IStorageGrid storage = hatch.getStorageGrid();

        if (storage == null) return UplinkStatus.AE_OFFLINE;

        IMEMonitor<IAEItemStack> itemInventory = storage.getItemInventory();

        if (itemInventory == null) return UplinkStatus.AE_OFFLINE;

        var iter = requestedItems.entrySet()
            .iterator();

        while (iter.hasNext()) {
            var e = iter.next();

            if (e.getKey() == null || e.getKey()
                .getItem() == null || e.getValue() == null || e.getValue() == 0) {
                iter.remove();
                continue;
            }

            if (!consumePlasmaEU(e.getValue())) return UplinkStatus.NO_PLASMA;

            IAEItemStack result = itemInventory.extractItems(
                Objects.requireNonNull(AEItemStack.create(e.getKey()))
                    .setStackSize(e.getValue()),
                simulate ? Actionable.SIMULATE : Actionable.MODULATE,
                hatch.getRequestSource());

            if (result != null) {
                if (result.getStackSize() == e.getValue()) {
                    iter.remove();
                } else {
                    e.setValue(e.getValue() - result.getStackSize());
                }
            }
        }

        return UplinkStatus.OK;
    }

    public UplinkStatus tryGivePlayerItems(List<IAEItemStack> items) {
        MTEMMUplinkMEHatch hatch = getMEHatch();

        if (hatch == null) return UplinkStatus.NO_HATCH;

        IStorageGrid storage = hatch.getStorageGrid();

        if (storage == null) return UplinkStatus.AE_OFFLINE;

        IMEMonitor<IAEItemStack> itemInventory = storage.getItemInventory();

        if (itemInventory == null) return UplinkStatus.AE_OFFLINE;

        for (IAEItemStack item : items) {
            if (item == null) continue;

            if (!consumePlasmaEU(item.getStackSize())) return UplinkStatus.NO_PLASMA;

            IAEItemStack result = itemInventory.injectItems(item, Actionable.MODULATE, hatch.getRequestSource());

            item.setStackSize(result == null ? 0 : result.getStackSize());
        }

        return UplinkStatus.OK;
    }

    public UplinkStatus tryGivePlayerFluids(List<IAEFluidStack> fluids) {
        MTEMMUplinkMEHatch hatch = getMEHatch();

        if (hatch == null) return UplinkStatus.NO_HATCH;

        IStorageGrid storage = hatch.getStorageGrid();

        if (storage == null) return UplinkStatus.AE_OFFLINE;

        IMEMonitor<IAEFluidStack> fluidInventory = storage.getFluidInventory();

        if (fluidInventory == null) return UplinkStatus.AE_OFFLINE;

        for (IAEFluidStack fluid : fluids) {
            if (fluid == null) continue;

            if (!consumePlasmaEU(GTUtility.ceilDiv(fluid.getStackSize(), 1000))) return UplinkStatus.NO_PLASMA;

            IAEFluidStack result = fluidInventory.injectItems(fluid, Actionable.MODULATE, hatch.getRequestSource());

            fluid.setStackSize(result == null ? 0 : result.getStackSize());
        }

        return UplinkStatus.OK;
    }

    private boolean consumePlasmaEU(long multiplier) {
        long euToConsume = multiplier * BASE_PLASMA_EU_COST;

        if (pendingPlasmaEU < euToConsume) {
            generatePlasmaEU(euToConsume);
        }

        if (pendingPlasmaEU >= euToConsume) {
            pendingPlasmaEU -= euToConsume;
            return true;
        } else {
            return false;
        }
    }

    private void generatePlasmaEU(long euToGenerate) {
        FuelBackend fuels = RecipeMaps.plasmaFuels.getBackend();

        for (MTEHatchInput input : mInputHatches) {
            for (FluidTankInfo tank : input.getTankInfo(ForgeDirection.UNKNOWN)) {
                if (tank.fluid == null) continue;

                GTRecipe fuel = fuels.findFuel(tank.fluid);

                if (fuel != null) {
                    long euPerLitre = fuel.mSpecialValue;

                    int litresToConsume = (int) Math
                        .min(Integer.MAX_VALUE, GTUtility.ceilDiv(euToGenerate, euPerLitre));

                    FluidStack toConsume = tank.fluid.copy();
                    toConsume.amount = litresToConsume;

                    FluidStack drained = input.drain(ForgeDirection.UNKNOWN, toConsume, true);

                    long generated = drained.amount * euPerLitre;
                    euToGenerate -= generated;
                    pendingPlasmaEU += generated;
                }

                if (euToGenerate <= 0) {
                    return;
                }
            }
        }
    }

    public void submitPlan(EntityPlayer submitter, String details, List<ItemStack> requiredItems, boolean autocraft) {
        MTEMMUplinkMEHatch hatch = getMEHatch();

        if (hatch != null) {
            String patternName = String.format(
                "%s's Manipulator Plan",
                submitter.getGameProfile()
                    .getName());

            if (details != null && !details.isEmpty()) {
                patternName += " (" + details + ")";
            }

            hatch.addRequest(submitter, patternName, requiredItems, autocraft);

            GTUtility.sendChatToPlayer(
                submitter,
                "Pushed a new virtual ME pattern to the uplink called '" + patternName + "'.");
        }
    }

    public void clearManualPlans(EntityPlayer player) {
        MTEMMUplinkMEHatch hatch = getMEHatch();

        if (hatch != null) {
            hatch.clearManualPlans(player);
            GTUtility.sendChatToPlayer(player, "Cleared manual plans.");
        }
    }

    private static final Map<Long, MTEMMUplink> UPLINKS = new MapMaker().weakValues()
        .makeMap();

    public static MTEMMUplink getUplink(long address) {
        return UPLINKS.get(address);
    }

    @Override
    public boolean onRunningTick(ItemStack aStack) {
        if (aStack != null && aStack.getItem() instanceof ItemMatterManipulator manipulator) {
            manipulator.setUplinkAddress(aStack, address);
        }

        return super.onRunningTick(aStack);
    }

    private UplinkState lastState;

    @Override
    @Nonnull
    public CheckRecipeResult checkProcessing() {
        mMaxProgresstime = 20;
        mEUt = (int) -TierEU.RECIPE_ZPM;
        mEfficiency = 10_000;

        UPLINKS.put(address, this);

        UplinkState state = getState();

        if (state != lastState) {
            lastState = state;
            MatterManipulator.sendUplinkStateUpdate(this);
        }

        return CheckRecipeResultRegistry.SUCCESSFUL;
    }

    @Override
    public void stopMachine(@Nonnull ShutDownReason reason) {
        super.stopMachine(reason);

        UPLINKS.remove(address);
    }

    @Override
    public void onBlockDestroyed() {
        super.onBlockDestroyed();

        UPLINKS.remove(address);
    }
}
