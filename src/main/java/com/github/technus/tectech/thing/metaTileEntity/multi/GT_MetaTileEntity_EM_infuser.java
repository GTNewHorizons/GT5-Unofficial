package com.github.technus.tectech.thing.metaTileEntity.multi;

import static com.github.technus.tectech.loader.TecTechConfig.DEBUG_MODE;
import static com.github.technus.tectech.thing.casing.GT_Block_CasingsTT.textureOffset;
import static com.github.technus.tectech.thing.casing.TT_Container_Casings.sBlockCasingsTT;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.ofBlock;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.transpose;
import static gregtech.api.GregTech_API.mEUtoRF;
import static gregtech.api.util.GT_StructureUtility.ofHatchAdderOptional;
import static net.minecraft.util.StatCollector.translateToLocal;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.FluidStack;

import com.github.technus.tectech.Reference;
import com.github.technus.tectech.TecTech;
import com.github.technus.tectech.thing.metaTileEntity.multi.base.GT_MetaTileEntity_MultiblockBase_EM;
import com.github.technus.tectech.util.CommonValues;
import com.gtnewhorizon.structurelib.alignment.constructable.IConstructable;
import com.gtnewhorizon.structurelib.structure.IStructureDefinition;

import cofh.api.energy.IEnergyContainerItem;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import gregtech.api.enums.Materials;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_Hatch_InputBus;
import gregtech.api.util.GT_Multiblock_Tooltip_Builder;
import ic2.api.item.ElectricItem;
import ic2.api.item.IElectricItem;

/**
 * Created by danie_000 on 17.12.2016.
 */
public class GT_MetaTileEntity_EM_infuser extends GT_MetaTileEntity_MultiblockBase_EM implements IConstructable {

    private static final int maxRepairedDamagePerOperation = 1000;
    private static final long usedEuPerDurability = 1000;
    private static final int usedUumPerDurability = 1;

    // region structure
    private static final String[] description = new String[] {
            EnumChatFormatting.AQUA + translateToLocal("tt.keyphrase.Hint_Details") + ":",
            translateToLocal("gt.blockmachines.multimachine.em.infuser.hint"), // 1 - Classic Hatches or High Power
                                                                               // Casing
    };

    private static final IStructureDefinition<GT_MetaTileEntity_EM_infuser> STRUCTURE_DEFINITION = IStructureDefinition
            .<GT_MetaTileEntity_EM_infuser>builder()
            .addShape(
                    "main",
                    transpose(
                            new String[][] { { "CCC", "CCC", "CCC" }, { "BBB", "BAB", "BBB" }, { "A~A", "AAA", "AAA" },
                                    { "BBB", "BAB", "BBB" }, { "CCC", "CCC", "CCC" } }))
            .addElement('A', ofBlock(sBlockCasingsTT, 4)).addElement('B', ofBlock(sBlockCasingsTT, 7))
            .addElement(
                    'C',
                    ofHatchAdderOptional(
                            GT_MetaTileEntity_EM_infuser::addClassicToMachineList,
                            textureOffset,
                            1,
                            sBlockCasingsTT,
                            0))
            .build();
    // endregion

    public GT_MetaTileEntity_EM_infuser(int aID, String aName, String aNameRegional) {
        super(aID, aName, aNameRegional);
        minRepairStatus = (byte) getIdealStatus();
        eDismantleBoom = true;
    }

    public GT_MetaTileEntity_EM_infuser(String aName) {
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
            if (DEBUG_MODE) {
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
            if (DEBUG_MODE) {
                e.printStackTrace();
            }
        }
        return 0;
    }

    @Override
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new GT_MetaTileEntity_EM_infuser(mName);
    }

    @Override
    public boolean checkMachine_EM(IGregTechTileEntity iGregTechTileEntity, ItemStack itemStack) {
        return structureCheck_EM("main", 1, 2, 0);
    }

    @Override
    public boolean checkRecipe_EM(ItemStack itemStack) {
        for (GT_MetaTileEntity_Hatch_InputBus inputBus : mInputBusses) {
            if (inputBus.mInventory != null) {
                for (ItemStack itemStackInBus : inputBus.mInventory) {
                    if (itemStackInBus != null) {
                        Item item = itemStackInBus.getItem();
                        if (itemStackInBus.stackSize == 1 && item != null) {
                            if (isItemStackFullyCharged(itemStackInBus) && isItemStackFullyRepaired(itemStackInBus)) {
                                if (addOutput(itemStackInBus)) {
                                    this.depleteInput(itemStackInBus);
                                }
                            } else {
                                mEfficiencyIncrease = 10000;
                                mMaxProgresstime = 20;
                                return true;
                            }
                        }
                    }
                }
            }
        }
        return false;
    }

    @Override
    public void outputAfterRecipe_EM() {
        boolean itemProcessed = false;
        startRecipeProcessing();
        for (GT_MetaTileEntity_Hatch_InputBus inputBus : mInputBusses) {
            if (inputBus.mInventory != null) {
                for (ItemStack itemStackInBus : inputBus.mInventory) {
                    if (itemStackInBus != null) {
                        Item item = itemStackInBus.getItem();
                        if (itemStackInBus.stackSize == 1 && item != null) {
                            if (isItemStackFullyCharged(itemStackInBus) && isItemStackFullyRepaired(itemStackInBus)) {
                                itemProcessed = true;
                                if (addOutput(itemStackInBus)) {
                                    this.depleteInput(itemStackInBus);
                                }
                            } else {
                                if (item.isRepairable()) {
                                    FluidStack uum = getStoredFluids().stream()
                                            .filter(fluid -> Materials.UUMatter.getFluid(1).isFluidEqual(fluid))
                                            .findAny().orElse(null);
                                    if (uum != null) {
                                        int repairedDamage = Math
                                                .min(item.getDamage(itemStackInBus), maxRepairedDamagePerOperation);
                                        long euCost = repairedDamage * usedEuPerDurability;
                                        if (getEUVar() >= euCost && depleteInput(
                                                new FluidStack(
                                                        Materials.UUMatter.mFluid,
                                                        repairedDamage * usedUumPerDurability))) {
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
                }
            }
        }
        endRecipeProcessing();
        if (!itemProcessed) {
            afterRecipeCheckFailed();
        }
    }

    @Override
    public GT_Multiblock_Tooltip_Builder createTooltip() {
        final GT_Multiblock_Tooltip_Builder tt = new GT_Multiblock_Tooltip_Builder();
        tt.addMachineType(translateToLocal("gt.blockmachines.multimachine.em.infuser.name")) // Machine Type: Energy
                                                                                             // Infuser
                .addInfo(translateToLocal("gt.blockmachines.multimachine.em.infuser.desc.0")) // Controller block of the
                                                                                              // Energy Infuser
                .addInfo(translateToLocal("gt.blockmachines.multimachine.em.infuser.desc.1")) // Can be used to charge
                                                                                              // items (lossless)
                .addInfo(translateToLocal("gt.blockmachines.multimachine.em.infuser.desc.2")) // Can be fed with
                                                                                              // UU-Matter to repair
                                                                                              // items
                .addSeparator().beginStructureBlock(3, 5, 3, false)
                .addController(translateToLocal("tt.keyword.Structure.FrontCenter3rd")) // Controller: Front 3rd layer
                                                                                        // center
                .addOtherStructurePart(
                        translateToLocal("gt.blockcasingsTT.0.name"),
                        translateToLocal("gt.blockmachines.multimachine.em.infuser.Structure.HighPowerCasing")) // High
                                                                                                                // Power
                // Casing: Layer
                // 1 and 5
                .addOtherStructurePart(
                        translateToLocal("gt.blockcasingsTT.7.name"),
                        translateToLocal("gt.blockmachines.multimachine.em.infuser.Structure.MolecularCoil")) // Molecular
                                                                                                              // Coil:
                // Layer 2 and 4
                .addOtherStructurePart(
                        translateToLocal("gt.blockcasingsTT.4.name"),
                        translateToLocal("gt.blockmachines.multimachine.em.infuser.Structure.MolecularCasing")) // Molecular
                // Casing: Layer
                // 3 (hollow)
                .addEnergyHatch(translateToLocal("tt.keyword.Structure.AnyHighPowerCasing"), 1) // Energy Hatch: Any
                                                                                                // High Power Casing
                .addMaintenanceHatch(translateToLocal("tt.keyword.Structure.AnyHighPowerCasing"), 1) // Maintenance
                                                                                                     // Hatch: Any High
                                                                                                     // Power Casing
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
    public IStructureDefinition<GT_MetaTileEntity_EM_infuser> getStructure_EM() {
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
