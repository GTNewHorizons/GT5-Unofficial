package tectech.thing.metaTileEntity.multi;

import static com.gtnewhorizon.structurelib.structure.StructureUtility.ofBlock;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.transpose;
import static gregtech.api.GregTechAPI.sBlockCasings4;
import static gregtech.api.util.GTStructureUtility.ofHatchAdderOptional;
import static net.minecraft.util.AxisAlignedBB.getBoundingBox;
import static net.minecraft.util.StatCollector.translateToLocal;

import java.util.ArrayList;
import java.util.HashSet;

import javax.annotation.Nonnull;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.common.util.ForgeDirection;

import org.jetbrains.annotations.NotNull;

import com.gtnewhorizon.structurelib.alignment.constructable.IConstructable;
import com.gtnewhorizon.structurelib.structure.IStructureDefinition;
import com.gtnewhorizon.structurelib.util.Vec3Impl;

import gregtech.api.enums.Textures;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.recipe.RecipeMaps;
import gregtech.api.recipe.check.CheckRecipeResult;
import gregtech.api.recipe.check.SimpleCheckRecipeResult;
import gregtech.api.util.GTRecipe;
import gregtech.api.util.GTUtility;
import gregtech.api.util.MultiblockTooltipBuilder;
import gregtech.api.util.shutdown.ShutDownReason;
import tectech.Reference;
import tectech.loader.MainLoader;
import tectech.recipe.TTRecipeAdder;
import tectech.thing.metaTileEntity.multi.base.INameFunction;
import tectech.thing.metaTileEntity.multi.base.IStatusFunction;
import tectech.thing.metaTileEntity.multi.base.LedStatus;
import tectech.thing.metaTileEntity.multi.base.Parameters;
import tectech.thing.metaTileEntity.multi.base.TTMultiblockBase;
import tectech.thing.metaTileEntity.multi.base.render.TTRenderedExtendedFacingTexture;
import tectech.util.CommonValues;

/**
 * Created by danie_000 on 17.12.2016.
 */
public class MTEMicrowave extends TTMultiblockBase implements IConstructable {

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

    private static final IStructureDefinition<MTEMicrowave> STRUCTURE_DEFINITION = IStructureDefinition
        .<MTEMicrowave>builder()
        .addShape(
            "main",
            transpose(
                new String[][] { { "AAAAA", "A---A", "A---A", "A---A", "AAAAA" },
                    { "AAAAA", "A---A", "A---A", "A---A", "AAAAA" }, { "AA~AA", "A---A", "A---A", "A---A", "AAAAA" },
                    { "ABBBA", "BAAAB", "BAAAB", "BAAAB", "ABBBA" } }))
        .addElement('A', ofBlock(sBlockCasings4, 1))
        .addElement('B', ofHatchAdderOptional(MTEMicrowave::addClassicToMachineList, 49, 1, sBlockCasings4, 1))
        .build();
    // endregion

    // region parameters
    protected Parameters.Group.ParameterIn powerSetting, timerSetting;
    protected Parameters.Group.ParameterOut timerValue, remainingTime;
    private static final INameFunction<MTEMicrowave> POWER_SETTING_NAME = (base,
        p) -> translateToLocal("gt.blockmachines.multimachine.tm.microwave.cfgi.0"); // Power setting
    private static final INameFunction<MTEMicrowave> TIMER_SETTING_NAME = (base,
        p) -> translateToLocal("gt.blockmachines.multimachine.tm.microwave.cfgi.1"); // Timer setting

    private static final INameFunction<MTEMicrowave> TIMER_VALUE_NAME = (base,
        p) -> translateToLocal("gt.blockmachines.multimachine.tm.microwave.cfgo.0"); // Timer value
    private static final INameFunction<MTEMicrowave> TIMER_REMAINING_NAME = (base,
        p) -> translateToLocal("gt.blockmachines.multimachine.tm.microwave.cfgo.1"); // Timer remaining
    private static final IStatusFunction<MTEMicrowave> POWER_STATUS = (base, p) -> LedStatus
        .fromLimitsInclusiveOuterBoundary(p.get(), 300, 1000, 1000, Double.POSITIVE_INFINITY);
    private static final IStatusFunction<MTEMicrowave> TIMER_STATUS = (base, p) -> {
        double value = p.get();
        if (Double.isNaN(value)) return LedStatus.STATUS_WRONG;
        value = (int) value;
        if (value <= 0) return LedStatus.STATUS_TOO_LOW;
        if (value > 3000) return LedStatus.STATUS_TOO_HIGH;
        return LedStatus.STATUS_OK;
    };
    // endregion

    public MTEMicrowave(int aID, String aName, String aNameRegional) {
        super(aID, aName, aNameRegional);
    }

    public MTEMicrowave(String aName) {
        super(aName);
    }

    @Override
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new MTEMicrowave(mName);
    }

    @Override
    public boolean checkMachine_EM(IGregTechTileEntity iGregTechTileEntity, ItemStack itemStack) {
        return structureCheck_EM("main", 2, 2, 0);
    }

    @Override
    @NotNull
    public CheckRecipeResult checkProcessing_EM() {
        hasBeenPausedThisCycle = false;
        if ((int) powerSetting.get() < 300 || timerSetting.get() <= 0 || timerSetting.get() > 3000) {
            return SimpleCheckRecipeResult.ofFailure("invalid_timer");
        }
        if (remainingTime.get() <= 0) {
            remainingTime.set(timerSetting.get());
            timerValue.set(0);
        }
        mEUt = -((int) powerSetting.get() >> 1);
        eAmpereFlow = 1;
        mMaxProgresstime = 20;
        mEfficiencyIncrease = 10000;
        return SimpleCheckRecipeResult.ofSuccess("microwaving");
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
        Vec3Impl xyzExpansion = getExtendedFacing().getWorldOffset(new Vec3Impl(1, 0, 1))
            .abs();
        int power = (int) powerSetting.get();
        int damagingFactor = Math.min(power >> 6, 8) + Math.min(power >> 8, 24)
            + Math.min(power >> 12, 48)
            + (power >> 18);

        ArrayList<ItemStack> itemsToOutput = new ArrayList<>();
        HashSet<Entity> tickedStuff = new HashSet<>();

        boolean inside = true;
        do {
            for (Object entity : mte.getWorld()
                .getEntitiesWithinAABBExcludingEntity(null, aabb)) {
                if (entity instanceof Entity) {
                    if (tickedStuff.add((Entity) entity)) {
                        if (inside && entity instanceof EntityItem) {
                            GTRecipe tRecipe = RecipeMaps.microwaveRecipes.findRecipe(
                                mte,
                                null,
                                true,
                                128,
                                null,
                                null,
                                new ItemStack[] { ((EntityItem) entity).getEntityItem() });
                            if (tRecipe == null || tRecipe.mInputs.length == 0 || tRecipe.mInputs[0].stackSize != 1) {
                                itemsToOutput.add(((EntityItem) entity).getEntityItem());
                            } else {
                                ItemStack newStuff = tRecipe.getOutput(0)
                                    .copy();
                                newStuff.stackSize = ((EntityItem) entity).getEntityItem().stackSize;
                                itemsToOutput.add(newStuff);
                            }
                            ((EntityItem) entity).delayBeforeCanPickup = 2;
                            ((EntityItem) entity).setDead();
                        } else if (entity instanceof EntityLivingBase) {
                            if (!GTUtility.isWearingFullElectroHazmat((EntityLivingBase) entity)) {
                                ((EntityLivingBase) entity).attackEntityFrom(MainLoader.microwaving, damagingFactor);
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

        mOutputItems = itemsToOutput.toArray(TTRecipeAdder.nullItem);

        if (remainingTime.get() <= 0) {
            mte.getWorld()
                .playSoundEffect(xPos, yPos, zPos, Reference.MODID + ":microwave_ding", 1, 1);
            stopMachine();
        }
    }

    @Override
    public MultiblockTooltipBuilder createTooltip() {
        final MultiblockTooltipBuilder tt = new MultiblockTooltipBuilder();
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
            .addSeparator()
            .beginStructureBlock(5, 4, 5, true)
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
    public ITexture[] getTexture(IGregTechTileEntity aBaseMetaTileEntity, ForgeDirection side, ForgeDirection facing,
        int colorIndex, boolean aActive, boolean aRedstone) {
        if (side == facing) {
            return new ITexture[] { Textures.BlockIcons.casingTexturePages[0][49],
                new TTRenderedExtendedFacingTexture(
                    aActive ? Textures.BlockIcons.OVERLAY_FRONT_ELECTRIC_BLAST_FURNACE_ACTIVE
                        : Textures.BlockIcons.OVERLAY_FRONT_ELECTRIC_BLAST_FURNACE) };
        } else if (side == facing.getOpposite()) {
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
    public void stopMachine(@Nonnull ShutDownReason reason) {
        super.stopMachine(reason);
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
    public IStructureDefinition<MTEMicrowave> getStructure_EM() {
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
