package tectech.thing.metaTileEntity.multi;

import static com.gtnewhorizon.structurelib.structure.StructureUtility.ofBlock;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.transpose;
import static gregtech.api.GregTechAPI.mEUtoRF;
import static gregtech.api.enums.HatchElement.Energy;
import static gregtech.api.enums.HatchElement.InputBus;
import static gregtech.api.enums.HatchElement.InputHatch;
import static gregtech.api.enums.HatchElement.Maintenance;
import static gregtech.api.enums.HatchElement.OutputBus;
import static gregtech.api.util.GTStructureUtility.buildHatchAdder;
import static net.minecraft.util.StatCollector.translateToLocal;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.fluids.FluidStack;

import org.jetbrains.annotations.NotNull;

import com.gtnewhorizon.structurelib.alignment.constructable.ISurvivalConstructable;
import com.gtnewhorizon.structurelib.structure.IItemSource;
import com.gtnewhorizon.structurelib.structure.IStructureDefinition;

import cofh.api.energy.IEnergyContainerItem;
import gregtech.api.casing.Casings;
import gregtech.api.enums.Materials;
import gregtech.api.enums.Mods;
import gregtech.api.enums.SoundResource;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.implementations.MTEHatchInputBus;
import gregtech.api.recipe.check.CheckRecipeResult;
import gregtech.api.recipe.check.SimpleCheckRecipeResult;
import gregtech.api.util.MultiblockTooltipBuilder;
import gregtech.common.tileentities.machines.MTEHatchInputBusME;
import ic2.api.item.ElectricItem;
import ic2.api.item.IElectricItem;
import tectech.loader.ConfigHandler;
import tectech.thing.casing.TTCasingsContainer;
import tectech.thing.metaTileEntity.multi.base.TTMultiblockBase;

/**
 * Created by danie_000 on 17.12.2016.
 */
public class MTEEnergyInfuser extends TTMultiblockBase implements ISurvivalConstructable {

    private static final int maxRepairedDamagePerOperation = 1000;
    private static final long usedEuPerDurability = 1000;
    private static final int usedUumPerDurability = 1;

    // region structure
    private static final String[] description = new String[] {
        EnumChatFormatting.AQUA + translateToLocal("tt.keyphrase.Hint_Details") + ":",
        // 1 - Classic Hatches or High Power Casing
        translateToLocal("gt.blockmachines.multimachine.em.infuser.hint"), };

    private static final IStructureDefinition<MTEEnergyInfuser> STRUCTURE_DEFINITION = IStructureDefinition
        .<MTEEnergyInfuser>builder()
        .addShape(
            "main",
            transpose(
                new String[][] { { "CCC", "CCC", "CCC" }, { "BBB", "BAB", "BBB" }, { "A~A", "AAA", "AAA" },
                    { "BBB", "BAB", "BBB" }, { "CCC", "CCC", "CCC" } }))
        .addElement('A', ofBlock(TTCasingsContainer.sBlockCasingsTT, 4))
        .addElement('B', ofBlock(TTCasingsContainer.sBlockCasingsTT, 7))
        .addElement(
            'C',
            buildHatchAdder(MTEEnergyInfuser.class)
                .atLeast(Energy.or(HatchElement.EnergyMulti), Maintenance, InputBus, InputHatch, OutputBus)
                .casingIndex(Casings.HighPowerCasing.getTextureId())
                .hint(1)
                .buildAndChain(Casings.HighPowerCasing.asElement()))
        .build();
    // endregion

    public MTEEnergyInfuser(int aID, String aName, String aNameRegional) {
        super(aID, aName, aNameRegional);
        eDismantleBoom = true;
    }

    public MTEEnergyInfuser(String aName) {
        super(aName);
        eDismantleBoom = true;
    }

    private boolean isItemStackFullyCharged(ItemStack stack) {
        if (stack == null) {
            return true;
        }
        Item item = stack.getItem();
        if (stack.stackSize == 1) {
            if (item instanceof IElectricItem) {
                return ElectricItem.manager.getCharge(stack) >= ((IElectricItem) item).getMaxCharge(stack);
            } else if (Mods.COFHCore.isModLoaded() && item instanceof IEnergyContainerItem) {
                return ((IEnergyContainerItem) item).getEnergyStored(stack)
                    >= ((IEnergyContainerItem) item).getMaxEnergyStored(stack);
            }
        }
        return true;
    }

    private boolean isItemStackFullyRepaired(ItemStack stack) {
        if (stack == null) {
            return true;
        }
        Item item = stack.getItem();
        return !item.isRepairable() || item.getMaxDamage(stack) <= 0 || item.getDamage(stack) <= 0;
    }

    private long doChargeItemStack(IElectricItem item, ItemStack stack) {
        try {
            final double missingItemCharge = item.getMaxCharge(stack) - ElectricItem.manager.getCharge(stack);
            final double availablePower = getAverageInputVoltage() * getMaxInputAmps();
            final double availableEnergy = getEUVar();
            final double charge = Math.min(Math.min(missingItemCharge, availablePower), availableEnergy);
            long remove = (long) Math
                .ceil(ElectricItem.manager.charge(stack, charge, item.getTier(stack), true, false));
            setEUVar(getEUVar() - remove);
            if (getEUVar() < 0) {
                setEUVar(0);
            }
            return remove;
        } catch (Exception e) {
            if (ConfigHandler.debug.DEBUG_MODE) {
                e.printStackTrace();
            }
        }
        return 0;
    }

    private long doChargeItemStackRF(IEnergyContainerItem item, ItemStack stack) {
        try {
            long RF = Math
                .min(item.getMaxEnergyStored(stack) - item.getEnergyStored(stack), getEUVar() * mEUtoRF / 100L);
            RF = item.receiveEnergy(stack, RF > Integer.MAX_VALUE ? Integer.MAX_VALUE : (int) RF, false);
            RF = RF * 100L / mEUtoRF;
            setEUVar(getEUVar() - RF);
            if (getEUVar() < 0) {
                setEUVar(0);
            }
            return RF;
        } catch (Exception e) {
            if (ConfigHandler.debug.DEBUG_MODE) {
                e.printStackTrace();
            }
        }
        return 0;
    }

    @Override
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new MTEEnergyInfuser(mName);
    }

    @Override
    public boolean checkMachine_EM(IGregTechTileEntity iGregTechTileEntity, ItemStack itemStack) {
        return structureCheck_EM("main", 1, 2, 0) && mInputBusses.size() > 0
            && mOutputBusses.size() > 0
            && mMaintenanceHatches.size() == 1;
    }

    @Override
    @NotNull
    protected CheckRecipeResult checkProcessing_EM() {
        for (MTEHatchInputBus inputBus : mInputBusses) {
            if (inputBus instanceof MTEHatchInputBusME) continue;
            for (int i = 0; i < inputBus.getSizeInventory(); i++) {
                ItemStack itemStackInBus = inputBus.getStackInSlot(i);
                if (itemStackInBus == null) continue;
                Item item = itemStackInBus.getItem();
                if (itemStackInBus.stackSize != 1 || item == null) continue;
                if (isItemStackFullyCharged(itemStackInBus) && isItemStackFullyRepaired(itemStackInBus)) {
                    if (addOutputAtomic(itemStackInBus)) {
                        this.depleteInput(itemStackInBus);
                    }
                } else {
                    if (getEUVar() <= 0) {
                        return SimpleCheckRecipeResult.ofFailure("insufficient_power_no_val");
                    }
                    mEfficiencyIncrease = 10000;
                    mMaxProgresstime = 1;
                    return SimpleCheckRecipeResult.ofSuccess("charging");
                }
            }
        }
        return SimpleCheckRecipeResult.ofFailure("no_chargeable_item");
    }

    @Override
    public void outputAfterRecipe_EM() {
        boolean itemProcessed = false;
        startRecipeProcessing();
        for (MTEHatchInputBus inputBus : mInputBusses) {
            if (inputBus instanceof MTEHatchInputBusME) continue;
            for (int i = 0; i < inputBus.getSizeInventory(); i++) {
                ItemStack itemStackInBus = inputBus.getStackInSlot(i);
                if (itemStackInBus == null) continue;
                Item item = itemStackInBus.getItem();
                if (itemStackInBus.stackSize != 1 || item == null) continue;
                if (isItemStackFullyCharged(itemStackInBus) && isItemStackFullyRepaired(itemStackInBus)) {
                    itemProcessed = true;
                    if (addOutputAtomic(itemStackInBus)) {
                        this.depleteInput(itemStackInBus);
                    }
                } else {
                    if (item.isRepairable()) {
                        FluidStack uum = getStoredFluids().stream()
                            .filter(
                                fluid -> Materials.UUMatter.getFluid(1)
                                    .isFluidEqual(fluid))
                            .findAny()
                            .orElse(null);
                        if (uum != null) {
                            int repairedDamage = Math
                                .min(item.getDamage(itemStackInBus), maxRepairedDamagePerOperation);
                            long euCost = repairedDamage * usedEuPerDurability;
                            if (getEUVar() >= euCost && depleteInput(
                                new FluidStack(Materials.UUMatter.mFluid, repairedDamage * usedUumPerDurability))) {
                                item.setDamage(
                                    itemStackInBus,
                                    Math.max(item.getDamage(itemStackInBus) - repairedDamage, 0));
                                setEUVar(Math.min(getEUVar() - euCost, 0));
                            }
                        }
                    }
                }
            }
        }
        endRecipeProcessing();
        if (!itemProcessed) {
            afterRecipeCheckFailed();
        }
    }

    @Override
    public MultiblockTooltipBuilder createTooltip() {
        final MultiblockTooltipBuilder tt = new MultiblockTooltipBuilder();
        tt.addMachineType("Restorer")
            .addInfo("Simultaneously recharges and repairs equipment")
            .addInfo("Stocking input buses are not supported")
            .addInfo(EnumChatFormatting.GOLD + "Recharging" + EnumChatFormatting.GRAY + ": No max speed or energy loss")
            .addInfo(
                EnumChatFormatting.GOLD + "Repairing"
                    + EnumChatFormatting.GRAY
                    + ": Max 1k durability/t, consumes 1k EU + 1L UUM per point")
            .addTecTechHatchInfo()
            .beginStructureBlock(3, 5, 3, false)
            // Controller: Front 3rd layer center
            .addController(translateToLocal("tt.keyword.Structure.FrontCenter3rd"))
            .addOtherStructurePart(
                // High Power
                translateToLocal("gt.blockcasingsTT.0.name"),
                translateToLocal("gt.blockmachines.multimachine.em.infuser.Structure.HighPowerCasing"))
            // Casing: Layer
            // 1 and 5
            .addOtherStructurePart(
                // Molecular Coil
                translateToLocal("gt.blockcasingsTT.7.name"),
                translateToLocal("gt.blockmachines.multimachine.em.infuser.Structure.MolecularCoil"))
            // Layer 2 and 4
            .addOtherStructurePart(
                // Molecular
                translateToLocal("gt.blockcasingsTT.4.name"),
                translateToLocal("gt.blockmachines.multimachine.em.infuser.Structure.MolecularCasing"))
            // Casing: Layer
            // 3 (hollow)
            // Energy Hatch: Any High Power Casing
            .addEnergyHatch(translateToLocal("tt.keyword.Structure.AnyHighPowerCasing"), 1)
            // Maintenance Hatch: Any High Power Casing
            .addMaintenanceHatch(translateToLocal("tt.keyword.Structure.AnyHighPowerCasing"), 1)
            // Input Bus: Any High Power Casing
            .addInputBus(translateToLocal("tt.keyword.Structure.AnyHighPowerCasing"), 1)
            // Output Bus: Any High Power Casing
            .addOutputBus(translateToLocal("tt.keyword.Structure.AnyHighPowerCasing"), 1)
            .toolTipFinisher();
        return tt;
    }

    @Override
    protected SoundResource getActivitySoundLoop() {
        return SoundResource.TECTECH_MACHINES_FX_WHOOUM;
    }

    private boolean isElectricItem(Item item) {
        // Null means false as well
        return (item instanceof IElectricItem) || (Mods.COFHCore.isModLoaded() && item instanceof IEnergyContainerItem);
    }

    private boolean canChargeSomething(MTEHatchInputBus inputBus) {
        for (ItemStack stack : inputBus.mInventory) {
            if (stack == null || stack.stackSize != 1 || isItemStackFullyCharged(stack)) continue;
            if (isElectricItem(stack.getItem())) {
                return true;
            }
        }
        return false;
    }

    private ItemStack findFirstStackedChargeable(MTEHatchInputBus inputBus) {
        for (ItemStack stack : inputBus.mInventory) {
            if (stack == null || stack.stackSize <= 1) continue; // Only want stacked items
            // Fully charged items can't stack, no need to check for that
            if (isElectricItem(stack.getItem())) {
                return stack;
            }
        }
        return null;
    }

    private int findFirstFreeSlot(MTEHatchInputBus inputBus) {
        for (int i = 0; i < inputBus.mInventory.length; i++) {
            ItemStack stack = inputBus.mInventory[i];
            if (stack == null) {
                return i;
            }
        }
        return -1;
    }

    private void tryUnstackChargeable(MTEHatchInputBus inputBus) {
        // Only unstack if we otherwise can't work
        if (canChargeSomething(inputBus)) return;

        ItemStack stackOriginal = findFirstStackedChargeable(inputBus);
        int freeSlot = findFirstFreeSlot(inputBus);
        if (stackOriginal == null || freeSlot == -1) {
            return; // Nothing to do
        }

        ItemStack stackCopy = stackOriginal.copy();
        inputBus.mInventory[freeSlot] = stackCopy;
        stackOriginal.stackSize--;
        stackCopy.stackSize = 1;
    }

    @Override
    public void onPostTick(IGregTechTileEntity aBaseMetaTileEntity, long aTick) {
        super.onPostTick(aBaseMetaTileEntity, aTick);
        if (!this.isAllowedToWork()) return;
        for (MTEHatchInputBus inputBus : mInputBusses) {
            if (inputBus instanceof MTEHatchInputBusME) continue;
            tryUnstackChargeable(inputBus);
            for (ItemStack stack : inputBus.mInventory) {
                if (stack == null || stack.stackSize != 1 || isItemStackFullyCharged(stack)) continue;

                Item item = stack.getItem();
                if (item == null) continue;

                if (item instanceof IElectricItem) {
                    doChargeItemStack((IElectricItem) item, stack);
                    return;
                } else if (Mods.COFHCore.isModLoaded() && item instanceof IEnergyContainerItem) {
                    doChargeItemStackRF((IEnergyContainerItem) item, stack);
                    return;
                }
            }
        }
    }

    @Override
    public void construct(ItemStack stackSize, boolean hintsOnly) {
        structureBuild_EM("main", 1, 2, 0, stackSize, hintsOnly);
    }

    @Override
    public int survivalConstruct(ItemStack stackSize, int elementBudget, IItemSource source, EntityPlayerMP actor) {
        if (mMachine) return -1;
        return survivalBuildPiece("main", stackSize, 1, 2, 0, elementBudget, source, actor, false, true);
    }

    @Override
    public IStructureDefinition<MTEEnergyInfuser> getStructure_EM() {
        return STRUCTURE_DEFINITION;
    }

    @Override
    public String[] getStructureDescription(ItemStack stackSize) {
        return description;
    }

    @Override
    public boolean isSafeVoidButtonEnabled() {
        return false;
    }

}
