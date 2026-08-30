package tectech.voidcraft.machine;

import static com.gtnewhorizon.structurelib.structure.StructureUtility.ofBlock;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.transpose;
import static gregtech.api.enums.HatchElement.InputBus;
import static gregtech.api.enums.HatchElement.OutputBus;
import static gregtech.api.enums.HatchElement.OutputHatch;
import static gregtech.api.util.GTStructureUtility.buildHatchAdder;
import static net.minecraft.util.EnumChatFormatting.RESET;
import static net.minecraft.util.EnumChatFormatting.YELLOW;
import static net.minecraft.util.StatCollector.translateToLocal;

import java.util.List;

import javax.annotation.Nullable;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.FluidStack;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.gtnewhorizon.structurelib.alignment.constructable.ISurvivalConstructable;
import com.gtnewhorizon.structurelib.structure.IItemSource;
import com.gtnewhorizon.structurelib.structure.IStructureDefinition;

import gregtech.api.casing.Casings;
import gregtech.api.enums.Materials;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechDeviceInformation;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.implementations.MTEHatchInputBus;
import gregtech.api.recipe.check.CheckRecipeResult;
import gregtech.api.recipe.check.CheckRecipeResultRegistry;
import gregtech.api.structure.error.StructureError;
import gregtech.api.util.FluidEjectionHelper;
import gregtech.api.util.GTUtility;
import gregtech.api.util.ItemEjectionHelper;
import gregtech.api.util.MultiblockTooltipBuilder;
import tectech.thing.casing.TTCasingsContainer;
import tectech.thing.metaTileEntity.multi.base.TTMultiblockBase;
import tectech.voidcraft.uss.USSShipCargo;
import tectech.voidcraft.uss.VoidcraftCargoPool;
import tectech.voidcraft.uss.VoidcraftFluidPool;

/**
 * Voidcraft Storage Bay (the EoH rework).
 *
 * <p>
 * A 5×5×3 multiblock holding the shared {@link VoidcraftCargoPool} (16 slots) that mining ships deliver into and
 * that players pull out of.
 *
 * <p>
 * I/O model (no energy, no maintenance, no recipes — this is a buffer):
 * <ul>
 * <li>input buses push their contents into the pool (merge-aware),</li>
 * <li>the pool pushes out as much as fits into the output busses (void-protection ON — leftovers stay in the
 * pool),</li>
 * <li>ships deliver cargo straight through the output busses first (an ME output bus is the bulk sink: it takes any
 * number of items into its cache and flushes them to the ME network),</li>
 * <li>Starlifter missions also deliver <em>fluid</em> cargo (Stellar Plasma) into a parallel
 * {@link VoidcraftFluidPool}, pumped out through output <em>hatches</em> (an ME output hatch is the bulk sink) —
 * same ME-first / pool-fallback / never-silently-void contract as the item path,</li>
 * <li>both pump directions run every machine tick while the structure is valid.</li>
 * </ul>
 *
 * <p>
 * Casing: BA0 meta 10 (back planes) / meta 11 (front face ring), buses around the front face — the same 5×5×3
 * silhouette as the Voidcraft Assembler.
 *
 * <p>
 * The pool persists in NBT across chunk reloads and collapses (it belongs to the bay, not to the USS).
 */
@IMetaTileEntity.SkipGenerateDescription
public class MTEVoidcraftStorageBay extends TTMultiblockBase implements ISurvivalConstructable {

    private static final Logger LOGGER = LogManager.getLogger("Voidcraft Storage Bay");

    private static final String STRUCTURE_PIECE_MAIN = "main";

    /**
     * 5 wide × 5 tall × 3 deep. Controller at the front-center (x=2, y=2, z=0). Bus slots (C) ring the front face,
     * 'A' (BA0 casing meta 10) fills the two back planes, 'B' (BA0 casing meta 11) pads the front ring.
     */
    private static final IStructureDefinition<MTEVoidcraftStorageBay> STRUCTURE_DEFINITION = IStructureDefinition
        .<MTEVoidcraftStorageBay>builder()
        .addShape(
            STRUCTURE_PIECE_MAIN,
            transpose(
                new String[][] { { "CCCCC", "AAAAA", "AAAAA" }, { "CBBBC", "AAAAA", "AAAAA" },
                    { "CB~BC", "AAAAA", "AAAAA" }, { "CBBBC", "AAAAA", "AAAAA" }, { "CCCCC", "AAAAA", "AAAAA" } }))
        .addElement('A', ofBlock(TTCasingsContainer.sBlockCasingsBA0, 10))
        .addElement('B', ofBlock(TTCasingsContainer.sBlockCasingsBA0, 11))
        .addElement(
            'C',
            buildHatchAdder(MTEVoidcraftStorageBay.class)
                // Input/OutputBus: the item path (buses, incl. ME item buses). OutputHatch: the
                // FLUID path — output hatches (incl. ME output hatches) accept the pool's Stellar Plasma.
                .atLeast(InputBus, OutputBus, OutputHatch)
                .casingIndex(Casings.HighPowerCasing.getTextureId())
                .hint(1)
                .buildAndChain(Casings.HighPowerCasing.asElement()))
        .build();

    /** The shared cargo pool (16 slots). */
    private @Nullable VoidcraftCargoPool pool = new VoidcraftCargoPool();

    /** The shared fluid pool (the starlifter's produced star fluids). */
    private @Nullable VoidcraftFluidPool fluidPool = new VoidcraftFluidPool();

    public MTEVoidcraftStorageBay(int aID, String aName, String aNameRegional) {
        super(aID, aName, aNameRegional);
    }

    public MTEVoidcraftStorageBay(String aName) {
        super(aName);
    }

    @Override
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new MTEVoidcraftStorageBay(mName);
    }

    @Override
    public void checkMachine(IGregTechTileEntity aMetaTileEntity, ItemStack aStack, List<StructureError> errors) {
        if (!checkPiece(STRUCTURE_PIECE_MAIN, 2, 2, 0, errors)) {
            return;
        }
        checkHasInputBus(errors);
        checkHasOutputBus(errors);
    }

    @Override
    public CheckRecipeResult checkProcessing_EM() {
        // Not a recipe machine — the buffer logic runs in onPostTick.
        return CheckRecipeResultRegistry.NO_RECIPE;
    }

    @Override
    public void onPostTick(IGregTechTileEntity aBaseMetaTileEntity, long aTick) {
        super.onPostTick(aBaseMetaTileEntity, aTick);
        if (!aBaseMetaTileEntity.isServerSide()) return;
        if (!mMachine) return;
        if (pool == null) {
            pool = new VoidcraftCargoPool();
        }

        // 1) Input buses → pool (merge-aware; the remainder stays in the bus).
        for (MTEHatchInputBus inputBus : mInputBusses) {
            IGregTechTileEntity busBase = inputBus.getBaseMetaTileEntity();
            if (busBase == null) continue;
            for (int i = 0; i < busBase.getSizeInventory(); i++) {
                ItemStack inSlot = busBase.getStackInSlot(i);
                if (inSlot == null) continue;
                ItemStack toInsert = inSlot.copy();
                if (pool.insert(toInsert) > 0) {
                    // Deplete exactly what was inserted (depleteInput removes a matching stack from the buses).
                    depleteInput(toInsert);
                }
            }
        }

        // 2) Pool → output busses (as much as fits). Void protection is ON: the busses (incl. a full ME cache)
        // may not take everything, and whatever they cannot take must not be voided — it stays in the pool slot.
        boolean poolHasContent = false;
        for (int slot = 0; slot < VoidcraftCargoPool.SLOTS; slot++) {
            if (pool.get(slot) != null) {
                poolHasContent = true;
                break;
            }
        }
        if (poolHasContent) {
            ItemEjectionHelper helper = new ItemEjectionHelper(getOutputBusses(), true);
            for (int slot = 0; slot < VoidcraftCargoPool.SLOTS; slot++) {
                ItemStack inPool = pool.get(slot);
                if (inPool == null) continue;
                ItemStack toEject = inPool.copy();
                int accepted = helper.ejectStack(toEject); // accepted out; the remainder stays in toEject
                if (accepted > 0) {
                    pool.remove(slot, accepted);
                }
            }
            helper.commit();
        }

        // 3) Fluid pool → output hatches. Same contract: void protection ON → the accepted mB
        // comes back, the remainder stays in the pool slot (ME output hatches are the bulk sink for it).
        if (!fluidPool.isEmpty()) {
            FluidEjectionHelper helper = new FluidEjectionHelper(getOutputHatches(), true);
            for (int slot = 0; slot < VoidcraftFluidPool.SLOTS; slot++) {
                String materialName = fluidPool.getMaterial(slot);
                if (materialName == null) continue;
                Materials material = Materials.get(materialName);
                if (material == null || material == Materials._NULL) {
                    continue; // unresolvable (mod removed) — the entry sits until it is pulled
                }
                FluidStack toEject = material.getFluid(fluidPool.getAmount(slot));
                if (toEject == null || toEject.amount <= 0) continue;
                int accepted = helper.ejectStack(toEject); // accepted mB out; the remainder stays in toEject
                if (accepted > 0) {
                    fluidPool.remove(slot, accepted);
                }
            }
            helper.commit();
        }
    }

    /**
     * Deliver an abstract cargo (called by the USS when a ship completes a mining leg).
     *
     * <p>
     * <strong>This is the delivery boundary</strong>: the cargo arrives as abstract {@code {id, Damage, amount}}
     * entries (see {@link USSShipCargo}) and is converted to 64-chunked item stacks here. Delivery order:
     * <ol>
     * <li><strong>Output busses first</strong> — via GT's ejection helper with void protection ON, so anything the
     * busses cannot take is <em>not</em> lost. An ME output bus is the intended bulk sink: it accepts any number of
     * items into its cache (default 1600, extendable with a storage cell or void cell) and flushes them to the ME
     * network every 40 ticks, honoring the storage cell's partition filter. Regular output busses take what their
     * slots hold.</li>
     * <li><strong>The 16-slot pool</strong> for whatever the busses could not take (buffer for a later pump-out).</li>
     * <li><strong>Item entities at the bay</strong> as the last resort — never silently lost.</li>
     * </ol>
     *
     * @param items abstract cargo entry list ({@code USSShipCargo.TAG_ITEMS} format)
     * @return true if everything was accepted (no drop), false if some was dropped at the door
     */
    public boolean deliver(net.minecraft.nbt.NBTTagList items) {
        if (pool == null) {
            pool = new VoidcraftCargoPool();
        }
        List<ItemStack> stacks = USSShipCargo.toStacks(items);
        boolean allFitted = true;
        List<ItemStack> overflow = new java.util.ArrayList<>();
        // 1) Output busses (ME bus = bulk sink; regular bus = slot capacity). Void protection ON → the accepted
        // count comes back, the remainder stays in the stack.
        if (!stacks.isEmpty()) {
            ItemEjectionHelper helper = new ItemEjectionHelper(getOutputBusses(), true);
            for (ItemStack stack : stacks) {
                helper.ejectStack(stack);
            }
            helper.commit();
        }
        // 2) Whatever the busses did not take → the pool.
        for (ItemStack stack : stacks) {
            if (stack.stackSize <= 0) {
                continue;
            }
            pool.insert(stack);
            // insert() subtracts what it accepted; a remainder means the pool overflowed.
            if (stack.stackSize > 0) {
                allFitted = false;
                overflow.add(stack);
            }
        }
        // 3) Last resort: drop at the bay (never silently lost).
        if (!overflow.isEmpty()) {
            IGregTechTileEntity base = getBaseMetaTileEntity();
            if (base != null) {
                GTUtility.dropItemsOrClusters(
                    base.getWorld(),
                    base.getXCoord() + 0.5f,
                    base.getYCoord() + 0.5f,
                    base.getZCoord() + 0.5f,
                    overflow);
            }
        }
        return allFitted;
    }

    /**
     * Deliver an abstract FLUID cargo (called by the USS when a Starlifter completes its mining leg — Phase 4
     * pass 1).
     *
     * <p>
     * <strong>This is the fluid delivery boundary</strong>: the cargo arrives as abstract
     * {@code {material, mB}} entries ({@code USSShipCargo.TAG_FLUIDS} format) and is resolved to Forge fluids
     * exactly once here (the pool keeps the abstract form). Same three-tier contract as {@link #deliver}:
     * <ol>
     * <li><strong>Output hatches first</strong> (an ME output hatch is the bulk sink: it takes the full amount into
     * its cache and flushes to the ME network) — void protection ON, the accepted mB comes back,</li>
     * <li><strong>The fluid pool</strong> for whatever the hatches could not take (buffered until the pump-out
     * finds room),</li>
     * <li>Overflow that the pool could not hold is logged LOUDLY (a fluid cannot be dropped as an entity — this is
     * the one loud-loss escape hatch, and it requires a full 160k L pool to overflow).</li>
     * </ol>
     *
     * @param entries abstract fluid entry list ({@code USSShipCargo.TAG_FLUIDS} format)
     * @return true if everything was accepted (no loss), false if some was lost (and logged)
     */
    public boolean deliverFluids(NBTTagList entries) {
        if (fluidPool == null) {
            fluidPool = new VoidcraftFluidPool();
        }
        boolean allFitted = true;

        // The cargo arrives as ABSTRACT {material, mB} entries — the fluid identity is the material NAME (stable,
        // save-safe), and a Forge Fluid is only materialized for the hatch transaction itself.
        for (int i = 0; i < entries.tagCount(); i++) {
            NBTTagCompound entry = entries.getCompoundTagAt(i);
            if (entry == null) {
                continue;
            }
            String materialName = entry.getString(USSShipCargo.FLUID_ENTRY_MATERIAL);
            long amount = Math.max(0L, entry.getLong(USSShipCargo.FLUID_ENTRY_AMOUNT));
            Materials material = materialName.isEmpty() ? null : Materials.get(materialName);
            if (material == null || material == Materials._NULL) {
                continue; // unknown material — the entry sits in the cargo NBT, never in the pool
            }
            FluidStack fluid = material.getFluid(amount);
            if (fluid == null || fluid.amount <= 0) {
                continue; // material has no fluid — the entry sits in the cargo NBT, never in the pool
            }

            // 1) Output hatches (ME output hatch = bulk sink). Void protection ON → accepted mB comes back, the
            // remainder stays in the stack.
            FluidEjectionHelper helper = new FluidEjectionHelper(getOutputHatches(), true);
            helper.ejectStack(fluid);
            helper.commit();

            // 2) Whatever the hatches did not take → the fluid pool (by NAME — the pool is abstract).
            long remaining = fluid.amount;
            if (remaining > 0) {
                long accepted = fluidPool.insert(materialName, remaining);
                if (remaining - accepted > 0) {
                    allFitted = false;
                    try {
                        LOGGER.warn(
                            "[Voidcraft] Storage bay fluid pool overflow: {} mB of {} lost — the pool was full",
                            remaining - accepted,
                            materialName);
                    } catch (Throwable ignored) {}
                }
            }
        }
        return allFitted;
    }

    @Override
    public String[] getInfoData() {
        List<String> str = new java.util.ArrayList<>(java.util.Arrays.asList(super.getInfoData()));
        str.add("tt.voidcraft.bay.infodata.header");
        int used = pool == null ? 0 : pool.size();
        str.add(
            IGregTechDeviceInformation.encode(
                "tt.voidcraft.bay.infodata.slots",
                "" + YELLOW + used + RESET + " / " + VoidcraftCargoPool.SLOTS));
        if (pool != null) {
            for (int slot = 0; slot < VoidcraftCargoPool.SLOTS; slot++) {
                ItemStack stack = pool.get(slot);
                if (stack == null) continue;
                str.add(
                    IGregTechDeviceInformation.encode(
                        "tt.voidcraft.bay.infodata.item",
                        "" + stack.stackSize + " × " + stack.getDisplayName()));
            }
        }
        // The fluid pool (Starlifter Stellar Plasma).
        if (fluidPool != null && !fluidPool.isEmpty()) {
            str.add("tt.voidcraft.bay.infodata.fluids.header");
            for (int slot = 0; slot < VoidcraftFluidPool.SLOTS; slot++) {
                String materialName = fluidPool.getMaterial(slot);
                if (materialName == null) continue;
                Materials material = Materials.get(materialName);
                FluidStack fluid = material == null ? null : material.getFluid(fluidPool.getAmount(slot));
                String displayName = fluid == null ? materialName : fluid.getLocalizedName();
                str.add(
                    IGregTechDeviceInformation.encode(
                        "tt.voidcraft.bay.infodata.fluid",
                        "" + fluidPool.getAmount(slot) + " mB × " + displayName));
            }
        }
        return str.toArray(new String[0]);
    }

    // region NBT persistence

    @Override
    public void saveNBTData(NBTTagCompound aNBT) {
        super.saveNBTData(aNBT);
        NBTTagCompound poolTag = new NBTTagCompound();
        if (pool != null) {
            pool.writeToNBT(poolTag);
        }
        aNBT.setTag("vc_cargo_pool", poolTag);
        NBTTagCompound fluidPoolTag = new NBTTagCompound();
        if (fluidPool != null) {
            fluidPool.writeToNBT(fluidPoolTag);
        }
        aNBT.setTag("vc_fluid_pool", fluidPoolTag);
    }

    @Override
    public void loadNBTData(NBTTagCompound aNBT) {
        super.loadNBTData(aNBT);
        NBTTagCompound poolTag = aNBT.getCompoundTag("vc_cargo_pool");
        pool = VoidcraftCargoPool.readFromNBT(poolTag);
        NBTTagCompound fluidPoolTag = aNBT.getCompoundTag("vc_fluid_pool");
        fluidPool = VoidcraftFluidPool.readFromNBT(fluidPoolTag);
    }

    // endregion

    @Override
    public MultiblockTooltipBuilder createTooltip() {
        MultiblockTooltipBuilder tt = new MultiblockTooltipBuilder();
        // spotless:off
        tt.addMachineType(translateToLocal("gt.mbtt.machine_type.storage"))
            .addMarkdown(new ResourceLocation("gregtech", "voidcraft-storage-bay"))
            .addSupportAny()
            .beginStructureBlock(5, 5, 3, false)
            .addController(translateToLocal("tt.keyword.Structure.FrontCenter3rd"))
            .addCasing("36", new ItemStack(TTCasingsContainer.sBlockCasingsBA0, 1, 10).getDisplayName(), false)
            .addCasing("9", new ItemStack(TTCasingsContainer.sBlockCasingsBA0, 1, 11).getDisplayName(), false)
            .addInputBus("1+", translateToLocal("tt.keyword.Structure.AnyHighPowerCasing"), 1)
            .addOutputBus("1+", translateToLocal("tt.keyword.Structure.AnyHighPowerCasing"), 1)
            .toolTipFinisher();
        // spotless:on
        return tt;
    }

    @Override
    public void construct(ItemStack stackSize, boolean hintsOnly) {
        buildPiece(STRUCTURE_PIECE_MAIN, stackSize, hintsOnly, 2, 2, 0);
    }

    @Override
    public int survivalConstruct(ItemStack stackSize, int elementBudget, IItemSource source, EntityPlayerMP actor) {
        if (mMachine) {
            return -1;
        }
        return survivalBuildPiece(STRUCTURE_PIECE_MAIN, stackSize, 2, 2, 0, elementBudget, source, actor, false, true);
    }

    @Override
    public IStructureDefinition<MTEVoidcraftStorageBay> getStructure_EM() {
        return STRUCTURE_DEFINITION;
    }

    @Override
    public boolean getDefaultHasMaintenanceChecks() {
        // No energy input at all → the maintenance (damage/repair) system does not apply; it also cannot be
        // sensibly serviced through a maintenance hatch. Existing issues are auto-fixed on load.
        return false;
    }

    @Override
    public boolean isSafeVoidButtonEnabled() {
        return false;
    }

    @Override
    public boolean supportsSingleRecipeLocking() {
        return false;
    }
}
