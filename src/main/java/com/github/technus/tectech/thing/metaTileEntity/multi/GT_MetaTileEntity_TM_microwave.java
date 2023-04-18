package com.github.technus.tectech.thing.metaTileEntity.multi;

import static com.github.technus.tectech.loader.MainLoader.microwaving;
import static com.github.technus.tectech.recipe.TT_recipeAdder.nullItem;
import static com.github.technus.tectech.thing.metaTileEntity.multi.base.LedStatus.STATUS_OK;
import static com.github.technus.tectech.thing.metaTileEntity.multi.base.LedStatus.STATUS_TOO_HIGH;
import static com.github.technus.tectech.thing.metaTileEntity.multi.base.LedStatus.STATUS_TOO_LOW;
import static com.github.technus.tectech.thing.metaTileEntity.multi.base.LedStatus.STATUS_WRONG;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.ofBlock;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.transpose;
import static gregtech.api.GregTech_API.sBlockCasings4;
import static gregtech.api.util.GT_StructureUtility.ofHatchAdderOptional;
import static net.minecraft.util.AxisAlignedBB.getBoundingBox;
import static net.minecraft.util.StatCollector.translateToLocal;

import java.util.ArrayList;
import java.util.HashSet;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.EnumChatFormatting;

import com.github.technus.tectech.Reference;
import com.github.technus.tectech.thing.metaTileEntity.multi.base.GT_MetaTileEntity_MultiblockBase_EM;
import com.github.technus.tectech.thing.metaTileEntity.multi.base.INameFunction;
import com.github.technus.tectech.thing.metaTileEntity.multi.base.IStatusFunction;
import com.github.technus.tectech.thing.metaTileEntity.multi.base.LedStatus;
import com.github.technus.tectech.thing.metaTileEntity.multi.base.Parameters;
import com.github.technus.tectech.thing.metaTileEntity.multi.base.render.TT_RenderedExtendedFacingTexture;
import com.github.technus.tectech.util.CommonValues;
import com.gtnewhorizon.structurelib.alignment.constructable.IConstructable;
import com.gtnewhorizon.structurelib.structure.IStructureDefinition;
import com.gtnewhorizon.structurelib.util.Vec3Impl;

import gregtech.api.enums.Textures;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.util.GT_Multiblock_Tooltip_Builder;
import gregtech.api.util.GT_Recipe;
import gregtech.api.util.GT_Utility;

/**
 * Created by danie_000 on 17.12.2016.
 */
public class GT_MetaTileEntity_TM_microwave extends GT_MetaTileEntity_MultiblockBase_EM implements IConstructable {

    // region variables
    private boolean hasBeenPausedThisCycle = false;
    // endregion

    // region structure
    // use multi A energy inputs, use less power the longer it runs
    private static final String[] description = new String[] {
            EnumChatFormatting.AQUA + translateToLocal("tt.keyphrase.Hint_Details") + ":",
            translateToLocal("gt.blockmachines.multimachine.tm.microwave.hint.0"), // 1 - Classic Hatches or Clean
                                                                                   // Stainless Steel
            // Casing
            translateToLocal("gt.blockmachines.multimachine.tm.microwave.hint.1"), // Also acts like a hopper so give it
                                                                                   // an Output
            // Bus
    };

    private static final IStructureDefinition<GT_MetaTileEntity_TM_microwave> STRUCTURE_DEFINITION = IStructureDefinition
            .<GT_MetaTileEntity_TM_microwave>builder()
            .addShape(
                    "main",
                    transpose(
                            new String[][] { { "AAAAA", "A---A", "A---A", "A---A", "AAAAA" },
                                    { "AAAAA", "A---A", "A---A", "A---A", "AAAAA" },
                                    { "AA~AA", "A---A", "A---A", "A---A", "AAAAA" },
                                    { "ABBBA", "BAAAB", "BAAAB", "BAAAB", "ABBBA" } }))
            .addElement('A', ofBlock(sBlockCasings4, 1))
            .addElement(
                    'B',
                    ofHatchAdderOptional(
                            GT_MetaTileEntity_TM_microwave::addClassicToMachineList,
                            49,
                            1,
                            sBlockCasings4,
                            1))
            .build();
    // endregion

    // region parameters
    protected Parameters.Group.ParameterIn powerSetting, timerSetting;
    protected Parameters.Group.ParameterOut timerValue, remainingTime;
    private static final INameFunction<GT_MetaTileEntity_TM_microwave> POWER_SETTING_NAME = (base,
            p) -> translateToLocal("gt.blockmachines.multimachine.tm.microwave.cfgi.0"); // Power setting
    private static final INameFunction<GT_MetaTileEntity_TM_microwave> TIMER_SETTING_NAME = (base,
            p) -> translateToLocal("gt.blockmachines.multimachine.tm.microwave.cfgi.1"); // Timer setting

    private static final INameFunction<GT_MetaTileEntity_TM_microwave> TIMER_VALUE_NAME = (base,
            p) -> translateToLocal("gt.blockmachines.multimachine.tm.microwave.cfgo.0"); // Timer value
    private static final INameFunction<GT_MetaTileEntity_TM_microwave> TIMER_REMAINING_NAME = (base,
            p) -> translateToLocal("gt.blockmachines.multimachine.tm.microwave.cfgo.1"); // Timer remaining
    private static final IStatusFunction<GT_MetaTileEntity_TM_microwave> POWER_STATUS = (base, p) -> LedStatus
            .fromLimitsInclusiveOuterBoundary(p.get(), 300, 1000, 1000, Double.POSITIVE_INFINITY);
    private static final IStatusFunction<GT_MetaTileEntity_TM_microwave> TIMER_STATUS = (base, p) -> {
        double value = p.get();
        if (Double.isNaN(value)) return STATUS_WRONG;
        value = (int) value;
        if (value <= 0) return STATUS_TOO_LOW;
        if (value > 3000) return STATUS_TOO_HIGH;
        return STATUS_OK;
    };
    // endregion

    public GT_MetaTileEntity_TM_microwave(int aID, String aName, String aNameRegional) {
        super(aID, aName, aNameRegional);
    }

    public GT_MetaTileEntity_TM_microwave(String aName) {
        super(aName);
    }

    @Override
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new GT_MetaTileEntity_TM_microwave(mName);
    }

    @Override
    public boolean checkMachine_EM(IGregTechTileEntity iGregTechTileEntity, ItemStack itemStack) {
        return structureCheck_EM("main", 2, 2, 0);
    }

    @Override
    public boolean checkRecipe_EM(ItemStack itemStack) {
        hasBeenPausedThisCycle = false;
        if ((int) powerSetting.get() < 300 || timerSetting.get() <= 0 || timerSetting.get() > 3000) {
            return false;
        }
        if (remainingTime.get() <= 0) {
            remainingTime.set(timerSetting.get());
            timerValue.set(0);
        }
        mEUt = -((int) powerSetting.get() >> 1);
        eAmpereFlow = 1;
        mMaxProgresstime = 20;
        mEfficiencyIncrease = 10000;
        return true;
    }

    @Override
    public void outputAfterRecipe_EM() {
        if (hasBeenPausedThisCycle) {
            return; // skip timer and actions if paused
        }
        timerValue.set(timerValue.get() + 1);
        remainingTime.set(timerSetting.get() - timerValue.get());
        IGregTechTileEntity mte = getBaseMetaTileEntity();
        Vec3Impl xyzOffsets = getExtendedFacing().getWorldOffset(new Vec3Impl(0, -1, 2));
        double xPos = mte.getXCoord() + 0.5f + xyzOffsets.get0();
        double yPos = mte.getYCoord() + 0.5f + xyzOffsets.get1();
        double zPos = mte.getZCoord() + 0.5f + xyzOffsets.get2();
        AxisAlignedBB aabb = getBoundingBox(-2, -2, -2, 2, 2, 2).offset(xPos, yPos, zPos);
        xyzOffsets = getExtendedFacing().getWorldOffset(new Vec3Impl(0, -4, 0));
        Vec3Impl xyzExpansion = getExtendedFacing().getWorldOffset(new Vec3Impl(1, 0, 1)).abs();
        int power = (int) powerSetting.get();
        int damagingFactor = Math.min(power >> 6, 8) + Math.min(power >> 8, 24)
                + Math.min(power >> 12, 48)
                + (power >> 18);

        ArrayList<ItemStack> itemsToOutput = new ArrayList<>();
        HashSet<Entity> tickedStuff = new HashSet<>();

        boolean inside = true;
        do {
            for (Object entity : mte.getWorld().getEntitiesWithinAABBExcludingEntity(null, aabb)) {
                if (entity instanceof Entity) {
                    if (tickedStuff.add((Entity) entity)) {
                        if (inside && entity instanceof EntityItem) {
                            GT_Recipe tRecipe = GT_Recipe.GT_Recipe_Map.sMicrowaveRecipes.findRecipe(
                                    mte,
                                    null,
                                    true,
                                    128,
                                    null,
                                    null,
                                    new ItemStack[] { ((EntityItem) entity).getEntityItem() });
                            if (tRecipe == null || tRecipe.mInputs[0].stackSize != 1) {
                                itemsToOutput.add(((EntityItem) entity).getEntityItem());
                            } else {
                                ItemStack newStuff = tRecipe.getOutput(0).copy();
                                newStuff.stackSize = ((EntityItem) entity).getEntityItem().stackSize;
                                itemsToOutput.add(newStuff);
                            }
                            ((EntityItem) entity).delayBeforeCanPickup = 2;
                            ((EntityItem) entity).setDead();
                        } else if (entity instanceof EntityLivingBase) {
                            if (!GT_Utility.isWearingFullElectroHazmat((EntityLivingBase) entity)) {
                                ((EntityLivingBase) entity).attackEntityFrom(microwaving, damagingFactor);
                            }
                        }
                    }
                }
            }
            aabb.offset(xyzOffsets.get0(), xyzOffsets.get1(), xyzOffsets.get2());
            aabb = aabb.expand(xyzExpansion.get0() * 1.5, xyzExpansion.get1() * 1.5, xyzExpansion.get2() * 1.5);
            inside = false;
            damagingFactor >>= 1;
        } while (damagingFactor > 0);

        mOutputItems = itemsToOutput.toArray(nullItem);

        if (remainingTime.get() <= 0) {
            mte.getWorld().playSoundEffect(xPos, yPos, zPos, Reference.MODID + ":microwave_ding", 1, 1);
            stopMachine();
        }
    }

    @Override
    public GT_Multiblock_Tooltip_Builder createTooltip() {
        final GT_Multiblock_Tooltip_Builder tt = new GT_Multiblock_Tooltip_Builder();
        tt.addMachineType(translateToLocal("gt.blockmachines.multimachine.tm.microwave.name")) // Machine Type:
                                                                                               // Microwave Grinder
                .addInfo(translateToLocal("gt.blockmachines.multimachine.tm.microwave.desc.0")) // Controller block of
                                                                                                // the
                // Microwave Grinder
                .addInfo(translateToLocal("gt.blockmachines.multimachine.tm.microwave.desc.1")) // Starts a timer when
                                                                                                // enabled
                .addInfo(translateToLocal("gt.blockmachines.multimachine.tm.microwave.desc.2")) // While the timer is
                                                                                                // running
                // anything inside the machine
                // will take damage
                .addInfo(translateToLocal("gt.blockmachines.multimachine.tm.microwave.desc.3")) // The machine will also
                                                                                                // collect
                // any items inside of it
                .addInfo(translateToLocal("gt.blockmachines.multimachine.tm.microwave.desc.4")) // Can be configured
                                                                                                // with a Parametrizer
                .addInfo(translateToLocal("gt.blockmachines.multimachine.tm.microwave.desc.5")) // (Do not insert a
                                                                                                // Wither)
                .addSeparator().beginStructureBlock(5, 4, 5, true)
                .addController(translateToLocal("tt.keyword.Structure.FrontCenter")) // Controller: Front center
                .addCasingInfoMin(translateToLocal("tt.keyword.Structure.StainlessSteelCasing"), 60, false) // 60x
                                                                                                            // Stainless
                                                                                                            // Steel
                // Casing (minimum)
                .addOtherStructurePart(
                        translateToLocal("tt.keyword.Structure.DataConnector"),
                        translateToLocal("tt.keyword.Structure.AnyOuterCasingOnBottom"),
                        2) // Output Bus: Any outer casing on the bottom layer
                .addOtherStructurePart(
                        translateToLocal("gt.blockmachines.hatch.param.tier.05.name"),
                        translateToLocal("tt.keyword.Structure.Optional") + " "
                                + translateToLocal("tt.keyword.Structure.AnyOuterCasingOnBottom"),
                        2) // Parametrizer: (optional) Any outer casing on the bottom layer
                .addEnergyHatch(translateToLocal("tt.keyword.Structure.AnyOuterCasingOnBottom"), 1) // Energy Hatch: Any
                                                                                                    // outer casing on
                                                                                                    // the bottom layer
                .addMaintenanceHatch(translateToLocal("tt.keyword.Structure.AnyOuterCasingOnBottom"), 1) // Maintenance
                                                                                                         // Hatch: Any
                                                                                                         // outer casing
                                                                                                         // on the
                                                                                                         // bottom layer
                .toolTipFinisher(CommonValues.THETA_MOVEMENT);
        return tt;
    }

    @Override
    public ITexture[] getTexture(IGregTechTileEntity aBaseMetaTileEntity, byte aSide, byte aFacing, byte aColorIndex,
            boolean aActive, boolean aRedstone) {
        if (aSide == aFacing) {
            return new ITexture[] { Textures.BlockIcons.casingTexturePages[0][49],
                    new TT_RenderedExtendedFacingTexture(
                            aActive ? Textures.BlockIcons.OVERLAY_FRONT_ELECTRIC_BLAST_FURNACE_ACTIVE
                                    : Textures.BlockIcons.OVERLAY_FRONT_ELECTRIC_BLAST_FURNACE) };
        } else if (aSide == GT_Utility.getOppositeSide(aFacing)) {
            return new ITexture[] { Textures.BlockIcons.casingTexturePages[0][49],
                    aActive ? Textures.BlockIcons.casingTexturePages[0][52]
                            : Textures.BlockIcons.casingTexturePages[0][53] };
        }
        return new ITexture[] { Textures.BlockIcons.casingTexturePages[0][49] };
    }

    @Override
    protected void parametersInstantiation_EM() {
        Parameters.Group hatch_0 = parametrization.getGroup(0, true);
        powerSetting = hatch_0.makeInParameter(0, 1000, POWER_SETTING_NAME, POWER_STATUS);
        timerSetting = hatch_0.makeInParameter(1, 360, TIMER_SETTING_NAME, TIMER_STATUS);

        timerValue = hatch_0.makeOutParameter(0, 0, TIMER_VALUE_NAME, TIMER_STATUS);
        remainingTime = hatch_0.makeOutParameter(1, 360, TIMER_REMAINING_NAME, TIMER_STATUS);
    }

    @Override
    public void stopMachine() {
        super.stopMachine();
        remainingTime.set(timerSetting.get());
        timerValue.set(0);
    }

    @Override
    public boolean onRunningTick(ItemStack aStack) {
        if (eSafeVoid) {
            hasBeenPausedThisCycle = true;
        }
        return hasBeenPausedThisCycle || super.onRunningTick(aStack); // consume eu and other resources if not paused
    }

    // TODO Why is the basetype 1??
    @Override
    public byte getTileEntityBaseType() {
        return 1;
    }

    @Override
    public void construct(ItemStack stackSize, boolean hintsOnly) {
        structureBuild_EM("main", 2, 2, 0, stackSize, hintsOnly);
    }

    @Override
    public IStructureDefinition<GT_MetaTileEntity_TM_microwave> getStructure_EM() {
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
