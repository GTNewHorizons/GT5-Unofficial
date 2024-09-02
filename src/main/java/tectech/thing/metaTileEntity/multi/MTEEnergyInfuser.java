package tectech.thing.metaTileEntity.multi;

import static com.gtnewhorizon.structurelib.structure.StructureUtility.ofBlock;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.transpose;
import static gregtech.api.GregTechAPI.mEUtoRF;
import static gregtech.api.util.GTStructureUtility.ofHatchAdderOptional;
import static net.minecraft.util.StatCollector.translateToLocal;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.FluidStack;

import org.jetbrains.annotations.NotNull;

import com.gtnewhorizon.structurelib.alignment.constructable.IConstructable;
import com.gtnewhorizon.structurelib.structure.IStructureDefinition;

import cofh.api.energy.IEnergyContainerItem;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import gregtech.api.enums.Materials;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.implementations.MTEHatchInputBus;
import gregtech.api.recipe.check.CheckRecipeResult;
import gregtech.api.recipe.check.SimpleCheckRecipeResult;
import gregtech.api.util.MultiblockTooltipBuilder;
import gregtech.common.tileentities.machines.MTEHatchInputBusME;
import ic2.api.item.ElectricItem;
import ic2.api.item.IElectricItem;
import tectech.Reference;
import tectech.TecTech;
import tectech.loader.TecTechConfig;
import tectech.thing.casing.BlockGTCasingsTT;
import tectech.thing.casing.TTCasingsContainer;
import tectech.thing.metaTileEntity.multi.base.TTMultiblockBase;
import tectech.util.CommonValues;

/**
 * Created by danie_000 on 17.12.2016.
 */
public class MTEEnergyInfuser extends TTMultiblockBase implements IConstructable {

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
            ofHatchAdderOptional(
                MTEEnergyInfuser::addClassicToMachineList,
                BlockGTCasingsTT.textureOffset,
                1,
                TTCasingsContainer.sBlockCasingsTT,
                0))
        .build();
    // endregion

    public MTEEnergyInfuser(int aID, String aName, String aNameRegional) {
        super(aID, aName, aNameRegional);
        minRepairStatus = (byte) getIdealStatus();
        eDismantleBoom = true;
    }

    public MTEEnergyInfuser(String aName) {
        super(aName);
        minRepairStatus = (byte) getIdealStatus();
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
            } else if (TecTech.hasCOFH && item instanceof IEnergyContainerItem) {
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
            double euDiff = item.getMaxCharge(stack) - ElectricItem.manager.getCharge(stack);
            long remove = (long) Math.ceil(
                ElectricItem.manager.charge(stack, Math.min(euDiff, getEUVar()), item.getTier(stack), true, false));
            setEUVar(getEUVar() - remove);
            if (getEUVar() < 0) {
                setEUVar(0);
            }
            return remove;
        } catch (Exception e) {
            if (TecTechConfig.DEBUG_MODE) {
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
            if (TecTechConfig.DEBUG_MODE) {
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
        return structureCheck_EM("main", 1, 2, 0);
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
                    if (addOutput(itemStackInBus)) {
                        this.depleteInput(itemStackInBus);
                    }
                } else {
                    mEfficiencyIncrease = 10000;
                    mMaxProgresstime = 20;
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
                    if (addOutput(itemStackInBus)) {
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
                    if (item instanceof IElectricItem) {
                        doChargeItemStack((IElectricItem) item, itemStackInBus);
                        return;
                    } else if (TecTech.hasCOFH && item instanceof IEnergyContainerItem) {
                        doChargeItemStackRF((IEnergyContainerItem) item, itemStackInBus);
                        return;
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
        // Machine Type: Energy Infuser
        tt.addMachineType(translateToLocal("gt.blockmachines.multimachine.em.infuser.name"))
            // Controller block of the Energy Infuser
            .addInfo(translateToLocal("gt.blockmachines.multimachine.em.infuser.desc.0"))
            // Can be used to charge items (lossless)
            .addInfo(translateToLocal("gt.blockmachines.multimachine.em.infuser.desc.1"))
            // Can be fed with UU-Matter to repair items
            .addInfo(translateToLocal("gt.blockmachines.multimachine.em.infuser.desc.2"))
            // Stocking Bus is not supported
            .addInfo(translateToLocal("gt.blockmachines.multimachine.em.infuser.desc.3"))
            .addSeparator()
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
            .toolTipFinisher(CommonValues.TEC_MARK_GENERAL);
        return tt;
    }

    public static final ResourceLocation activitySound = new ResourceLocation(Reference.MODID + ":fx_whooum");

    @Override
    @SideOnly(Side.CLIENT)
    protected ResourceLocation getActivitySound() {
        return activitySound;
    }

    @Override
    public void construct(ItemStack stackSize, boolean hintsOnly) {
        structureBuild_EM("main", 1, 2, 0, stackSize, hintsOnly);
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
    public boolean isPowerPassButtonEnabled() {
        return true;
    }

    @Override
    public boolean isSafeVoidButtonEnabled() {
        return false;
    }

    @Override
    public boolean isAllowedToWorkButtonEnabled() {
        return true;
    }
}
