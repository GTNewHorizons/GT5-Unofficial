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
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.common.util.ForgeDirection;

import org.jetbrains.annotations.NotNull;

import com.gtnewhorizon.structurelib.alignment.constructable.ISurvivalConstructable;
import com.gtnewhorizon.structurelib.structure.IItemSource;
import com.gtnewhorizon.structurelib.structure.IStructureDefinition;
import com.gtnewhorizon.structurelib.util.Vec3Impl;

import gregtech.api.enums.GTValues;
import gregtech.api.enums.Textures;
import gregtech.api.hazards.HazardProtection;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.implementations.gui.MTEMultiBlockBaseGui;
import gregtech.api.recipe.RecipeMaps;
import gregtech.api.recipe.check.CheckRecipeResult;
import gregtech.api.recipe.check.SimpleCheckRecipeResult;
import gregtech.api.util.GTRecipe;
import gregtech.api.util.MultiblockTooltipBuilder;
import gregtech.api.util.shutdown.ShutDownReason;
import gregtech.api.util.shutdown.ShutDownReasonRegistry;
import tectech.Reference;
import tectech.loader.MainLoader;
import tectech.recipe.TTRecipeAdder;
import tectech.thing.metaTileEntity.multi.base.Parameter;
import tectech.thing.metaTileEntity.multi.base.TTMultiblockBase;
import tectech.thing.metaTileEntity.multi.base.gui.MTEMicrowaveGui;
import tectech.thing.metaTileEntity.multi.base.render.TTRenderedExtendedFacingTexture;

/**
 * Created by danie_000 on 17.12.2016.
 */
public class MTEMicrowave extends TTMultiblockBase implements ISurvivalConstructable {

    // region variables
    private boolean hasBeenPausedThisCycle = false;
    private int currentTime;
    public int remainingTime;
    public int maxDamagePerSecond;
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
    Parameter.IntegerParameter powerParameter;
    Parameter.IntegerParameter timerParameter;
    // endregion

    public MTEMicrowave(int aID, String aName, String aNameRegional) {
        super(aID, aName, aNameRegional);
    }

    public MTEMicrowave(String aName) {
        super(aName);
    }

    @Override
    protected void initParameters() {
        powerParameter = new Parameter.IntegerParameter(
            1000,
            () -> 128,
            () -> Integer.MAX_VALUE,
            "microwave_power",
            "gt.blockmachines.multimachine.tm.microwave.power");
        timerParameter = new Parameter.IntegerParameter(
            360,
            () -> 1,
            () -> 3000,
            "microwave_timer",
            "gt.blockmachines.multimachine.tm.microwave.timer");
        parameterList.add(powerParameter);
        parameterList.add(timerParameter);
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
        if (remainingTime <= 0) {
            remainingTime = timerParameter.getValue();
            currentTime = 0;
        }
        mEUt = -powerParameter.getValue();
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
        currentTime++;
        remainingTime = (timerParameter.getValue() - currentTime);
        IGregTechTileEntity mte = getBaseMetaTileEntity();
        Vec3Impl xyzOffsets = getExtendedFacing().getWorldOffset(new Vec3Impl(0, -1, 2));
        double xPos = mte.getXCoord() + 0.5f + xyzOffsets.get0();
        double yPos = mte.getYCoord() + 0.5f + xyzOffsets.get1();
        double zPos = mte.getZCoord() + 0.5f + xyzOffsets.get2();
        AxisAlignedBB aabb = getBoundingBox(-2, -2, -2, 2, 2, 2).offset(xPos, yPos, zPos);
        xyzOffsets = getExtendedFacing().getWorldOffset(new Vec3Impl(0, -4, 0));
        Vec3Impl xyzExpansion = getExtendedFacing().getWorldOffset(new Vec3Impl(1, 0, 1))
            .abs();
        int power = powerParameter.getValue();
        int damagingFactor = (int) (Math.min(power / GTValues.V[1], 8) + Math.min(power / GTValues.V[2], 24)
            + Math.min(power / GTValues.V[4], 48)
            + (power / GTValues.V[7]));
        maxDamagePerSecond = damagingFactor;

        ArrayList<ItemStack> itemsToOutput = new ArrayList<>();
        HashSet<Entity> tickedStuff = new HashSet<>();

        boolean inside = true;
        do {
            for (Entity entity : mte.getWorld()
                .getEntitiesWithinAABBExcludingEntity(null, aabb)) {

                if (tickedStuff.add(entity)) {
                    if (inside && entity instanceof EntityItem) {
                        GTRecipe tRecipe = RecipeMaps.microwaveRecipes.findRecipeQuery()
                            .items(((EntityItem) entity).getEntityItem())
                            .voltage(128)
                            .notUnificated(true)
                            .find();
                        if (tRecipe == null || tRecipe.mInputs.length == 0 || tRecipe.mInputs[0].stackSize != 1) {
                            itemsToOutput.add(((EntityItem) entity).getEntityItem());
                        } else {
                            ItemStack newStuff = tRecipe.getOutput(0)
                                .copy();
                            newStuff.stackSize = ((EntityItem) entity).getEntityItem().stackSize;
                            itemsToOutput.add(newStuff);
                        }
                        ((EntityItem) entity).delayBeforeCanPickup = 2;
                        entity.setDead();
                    } else if (entity instanceof EntityLivingBase) {
                        if (!HazardProtection.isWearingFullElectroHazmat((EntityLivingBase) entity)) {
                            entity.attackEntityFrom(MainLoader.microwaving, damagingFactor);
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

        if (remainingTime <= 0) {
            mte.getWorld()
                .playSoundEffect(xPos, yPos, zPos, Reference.MODID + ":microwave_ding", 1, 1);
            stopMachine(ShutDownReasonRegistry.NONE);
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
            .addEnergyHatch(translateToLocal("tt.keyword.Structure.AnyOuterCasingOnBottom"), 1) // Energy Hatch: Any
                                                                                                // outer casing on
                                                                                                // the bottom layer
            .addMaintenanceHatch(translateToLocal("tt.keyword.Structure.AnyOuterCasingOnBottom"), 1) // Maintenance
                                                                                                     // Hatch: Any
                                                                                                     // outer casing
                                                                                                     // on the
                                                                                                     // bottom layer
            .toolTipFinisher();
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
    public void stopMachine(@Nonnull ShutDownReason reason) {
        super.stopMachine(reason);
        remainingTime = timerParameter.getValue();
        currentTime = 0;
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
    public int survivalConstruct(ItemStack stackSize, int elementBudget, IItemSource source, EntityPlayerMP actor) {
        if (mMachine) return -1;
        return survivalBuildPiece("main", stackSize, 2, 2, 0, elementBudget, source, actor, false, true);
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
    public boolean isSafeVoidButtonEnabled() {
        return false;
    }

    @Override
    public boolean forceUseMui2() {
        return true;
    }

    @Override
    protected @NotNull MTEMultiBlockBaseGui getGui() {
        return new MTEMicrowaveGui(this);
    }
}
