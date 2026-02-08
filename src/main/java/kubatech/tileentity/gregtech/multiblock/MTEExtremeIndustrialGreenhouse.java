/*
 * spotless:off
 * KubaTech - Gregtech Addon
 * Copyright (C) 2022 - 2024  kuba6000
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this library. If not, see <https://www.gnu.org/licenses/>.
 * spotless:on
 */

package kubatech.tileentity.gregtech.multiblock;

import static com.gtnewhorizon.structurelib.structure.StructureUtility.isAir;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.ofBlock;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.ofChain;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.onElementPass;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.transpose;
import static gregtech.api.enums.HatchElement.Energy;
import static gregtech.api.enums.HatchElement.InputBus;
import static gregtech.api.enums.HatchElement.InputHatch;
import static gregtech.api.enums.HatchElement.Maintenance;
import static gregtech.api.enums.HatchElement.OutputBus;
import static gregtech.api.enums.Mods.ProjectRedIllumination;
import static gregtech.api.enums.Mods.RandomThings;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_DISTILLATION_TOWER;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_DISTILLATION_TOWER_ACTIVE;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_DISTILLATION_TOWER_ACTIVE_GLOW;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_DISTILLATION_TOWER_GLOW;
import static gregtech.api.util.GTStructureUtility.buildHatchAdder;
import static gregtech.api.util.GTStructureUtility.chainAllGlasses;
import static gregtech.api.util.GTStructureUtility.ofAnyWater;
import static gregtech.api.util.GTUtility.formatShortenedLong;
import static gregtech.api.util.GTUtility.truncateText;
import static gregtech.api.util.GTUtility.validMTEList;
import static kubatech.api.utils.ItemUtils.readItemStackFromNBT;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;

import org.jetbrains.annotations.NotNull;

import com.gtnewhorizon.structurelib.alignment.IAlignmentLimits;
import com.gtnewhorizon.structurelib.alignment.constructable.ISurvivalConstructable;
import com.gtnewhorizon.structurelib.alignment.enumerable.ExtendedFacing;
import com.gtnewhorizon.structurelib.structure.IStructureDefinition;
import com.gtnewhorizon.structurelib.structure.ISurvivalBuildEnvironment;
import com.gtnewhorizon.structurelib.structure.StructureDefinition;
import com.gtnewhorizons.modularui.api.ModularUITextures;
import com.gtnewhorizons.modularui.api.drawable.ItemDrawable;
import com.gtnewhorizons.modularui.api.drawable.Text;
import com.gtnewhorizons.modularui.api.math.Alignment;
import com.gtnewhorizons.modularui.api.math.Color;
import com.gtnewhorizons.modularui.api.math.MainAxisAlignment;
import com.gtnewhorizons.modularui.api.screen.ModularUIContext;
import com.gtnewhorizons.modularui.api.screen.ModularWindow;
import com.gtnewhorizons.modularui.api.screen.UIBuildContext;
import com.gtnewhorizons.modularui.api.widget.Widget;
import com.gtnewhorizons.modularui.common.builder.UIInfo;
import com.gtnewhorizons.modularui.common.internal.wrapper.ModularUIContainer;
import com.gtnewhorizons.modularui.common.widget.ButtonWidget;
import com.gtnewhorizons.modularui.common.widget.Column;
import com.gtnewhorizons.modularui.common.widget.CycleButtonWidget;
import com.gtnewhorizons.modularui.common.widget.DrawableWidget;
import com.gtnewhorizons.modularui.common.widget.DynamicPositionedColumn;
import com.gtnewhorizons.modularui.common.widget.DynamicPositionedRow;
import com.gtnewhorizons.modularui.common.widget.FakeSyncWidget;
import com.gtnewhorizons.modularui.common.widget.MultiChildWidget;
import com.gtnewhorizons.modularui.common.widget.Scrollable;
import com.gtnewhorizons.modularui.common.widget.SlotWidget;
import com.gtnewhorizons.modularui.common.widget.TextWidget;

import cofh.asmhooks.block.BlockTickingWater;
import cofh.asmhooks.block.BlockWater;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import gregtech.api.GregTechAPI;
import gregtech.api.casing.Casings;
import gregtech.api.enums.GTValues;
import gregtech.api.enums.Materials;
import gregtech.api.enums.Mods;
import gregtech.api.enums.VoltageIndex;
import gregtech.api.gui.modularui.GTUITextures;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.GregTechTileClientEvents;
import gregtech.api.metatileentity.implementations.MTEHatchEnergy;
import gregtech.api.metatileentity.implementations.MTEHatchOutputBus;
import gregtech.api.recipe.check.CheckRecipeResult;
import gregtech.api.recipe.check.CheckRecipeResultRegistry;
import gregtech.api.recipe.check.SimpleCheckRecipeResult;
import gregtech.api.render.TextureFactory;
import gregtech.api.util.GTUtility;
import gregtech.api.util.MultiblockTooltipBuilder;
import gregtech.api.util.VoidProtectionHelper;
import gregtech.common.misc.GTStructureChannels;
import gregtech.common.tileentities.machines.MTEHatchOutputBusME;
import kubatech.api.EIGDynamicInventory;
import kubatech.api.eig.EIGBucket;
import kubatech.api.eig.EIGDropTable;
import kubatech.api.eig.EIGMode;
import kubatech.api.enums.EIGModes;
import kubatech.api.implementations.KubaTechGTMultiBlockBase;
import kubatech.client.effect.CropRenderer;
import kubatech.tileentity.gregtech.multiblock.eigbuckets.EIGIC2Bucket;

@SuppressWarnings("unused")
public class MTEExtremeIndustrialGreenhouse extends KubaTechGTMultiBlockBase<MTEExtremeIndustrialGreenhouse>
    implements ISurvivalConstructable {

    /***
     * BALANCE OF THE IC2 MODE: (let T = EIG_BALANCE_IC2_ACCELERATOR_TIER) All IC2 crops are simulated and all drops are
     * generated based on the real crop drops. T is a tick accelerator tier for the IC2 crops, Each crop in the EIG is
     * accelerated using T tier accelerator (Accelerators in the game are defined as 2^T acceleration, 8*(4^T) voltage,
     * 6 amps) IC2 mode is unlocked at T+1 tier (glass and power) And each amp of T gives one crop slot, EIG only
     * consumes 1 AMP of a tier that it is at (EIG starts at 4 crops (T+1 tier) and each tier quadruples the amount of
     * slots) Each crop is accelerated 2^T times Summary: Accelerators in EIG are a bit cheaper than on the crop field
     * (4 amps instead of 6 amps) There are 4 crops touching the accelerator (1 AMP for 1 accelerated crop)
     * <p>
     * Changing T one number down will buff the EIG twice, as well as changing it up will nerf the EIG twice (That is
     * because accelerators are imperfectly scaled in game LV = 2x, MV = 4x, ...)
     */
    public static final int EIG_BALANCE_IC2_ACCELERATOR_TIER = VoltageIndex.IV;
    public static final int EIG_BALANCE_REGULAR_MODE_MIN_TIER = VoltageIndex.EV;
    public static final int EIG_BALANCE_IC2_MODE_MIN_TIER = EIG_BALANCE_IC2_ACCELERATOR_TIER + 1;
    public static final double EIG_BALANCE_MAX_FERTILIZER_BOOST = 4.0d;
    public static final int EIG_BALANCE_WEED_EX_USAGE_BEGINS_AT = 1000;
    public static final int EIG_BALANCE_WATER_USAGE_PER_SEED = 1000;

    private static final Fluid WEEDEX_FLUID = Materials.WeedEX9000.mFluid;
    private static final LinkedList<ItemStack> FERTILIZER_ITEM_LIST = new LinkedList<>();

    public static void addFertilizerItem(ItemStack fertilizer) {
        FERTILIZER_ITEM_LIST.addLast(fertilizer);
    }

    private static final Block DIRT_BLOCK = RandomThings.isModLoaded()
        ? Block.getBlockFromName("RandomThings:fertilizedDirt")
        : Blocks.dirt;
    private static final Block TILLED_DIRT_BLOCK = RandomThings.isModLoaded()
        ? Block.getBlockFromName("RandomThings:fertilizedDirt_tilled")
        : Blocks.farmland;

    private static final boolean debug = false;

    /***
     * Changing this variable will cause ALL EIGs in the world to regenerate their drop tables.
     */
    private static final int NBT_REVISION = 2;
    private static final int CONFIGURATION_WINDOW_ID = 999;

    public final List<EIGBucket> buckets = new LinkedList<>();
    public final EIGDropTable dropTracker = new EIGDropTable();
    public Collection<EIGMigrationHolder> toMigrate;
    public EIGDropTable guiDropTracker = new EIGDropTable();
    private HashMap<ItemStack, Double> synchedGUIDropTracker = new HashMap<>();
    private int maxSeedTypes = 0;
    private int maxSeedCount = 0;
    /**
     * The setup phase of the EIG. 0 operation. 1 input. 2 output.
     */
    private int setupPhase = 1;
    /**
     * The amount of water used per cycle.
     */
    private int waterUsage = 0;
    /**
     * The tier of the glass on the EIG.
     */
    private byte glassTier = -1;
    /**
     * The Amount of Weed-EX used per cycle.
     */
    private int weedEXUsage = 0;
    /**
     * The mode that the EIG is in.
     */
    private EIGMode mode = EIGModes.Normal;
    /**
     * Determines whether new IC2 buckets will use no humidity for their growth speed calculation.
     */
    private boolean useNoHumidity = false;
    private boolean isCheckingDirtWater = false;
    // TODO: Remove after 2.9
    private boolean isOldStructure = false;

    public boolean isInNoHumidityMode() {
        return this.useNoHumidity;
    }

    // region structure stuff

    private int mCasing = 0;
    private static final Casings CASING = Casings.SterileFarmCasing;
    private static final String STRUCTURE_PIECE_MAIN = "main";
    private static final int OFFSET_H = 3;
    private static final int OFFSET_V = 6;
    private static final int OFFSET_D = 0;
    private static final String STRUCTURE_PIECE_MAIN_OLD = "mainold";
    private static final Casings CASING_OLD = Casings.CleanStainlessSteelCasing;
    private static final IStructureDefinition<MTEExtremeIndustrialGreenhouse> STRUCTURE_DEFINITION = StructureDefinition
        .<MTEExtremeIndustrialGreenhouse>builder()
        .addShape(
            STRUCTURE_PIECE_MAIN,
            transpose(
                new String[][] { // spotless:off
                    {"  fff  ","  ggg  ","  ggg  ","  ggg  ","  fff  ","  ggg  ","  ggg  ","  ggg  ","  fff  "},
                    {" fgggf "," g   g "," g   g "," g   g "," flllf "," g   g "," g   g "," g   g "," fgggf "},
                    {"fgggggf","g     g","g     g","g     g","f     f","g     g","g     g","g     g","fgggggf"},
                    {"fgggggf","g     g","g     g","g     g","f     f","g     g","g     g","g     g","fgggggf"},
                    {"fgggggf","g     g","g     g","g     g","f     f","g     g","g     g","g     g","fgggggf"},
                    {"aaaaaaa","awdddwa","awdddwa","awdddwa","awdddwa","awdddwa","awdddwa","awdddwa","aaaaaaa"},
                    {"aaa~aaa","aaaaaaa","aaaaaaa","aaaaaaa","aaaaaaa","aaaaaaa","aaaaaaa","aaaaaaa","aaaaaaa"}
                } // spotless:on
            ))
        .addShape(
            STRUCTURE_PIECE_MAIN_OLD,
            transpose(
                new String[][] { // spotless:off
                    { "ccccc", "ccccc", "ccccc", "ccccc", "ccccc" },
                    { "ccccc", "clllc", "clllc", "clllc", "ccccc" },
                    { "ggggg", "g---g", "g---g", "g---g", "ggggg" },
                    { "ggggg", "g---g", "g---g", "g---g", "ggggg" },
                    { "ccccc", "cdddc", "cdwdc", "cdddc", "ccccc" },
                    { "cc~cc", "cCCCc", "cCCCc", "cCCCc", "ccccc" },
                })) // spotless:on
        .addElement(
            'a',
            buildHatchAdder(MTEExtremeIndustrialGreenhouse.class)
                .atLeast(InputBus, OutputBus, Energy, Maintenance, InputHatch)
                .casingIndex(CASING.textureId)
                .hint(1)
                .buildAndChain(onElementPass(t -> t.mCasing++, Casings.SterileFarmCasing.asElement())))
        .addElement('f', ofBlock(GregTechAPI.sBlockFrames, 316))
        .addElement(
            'l',
            ProjectRedIllumination.isModLoaded()
                ? ofChain(
                    ofBlock(Block.getBlockFromName("ProjRed|Illumination:projectred.illumination.lamp"), 10),
                    ofBlock(Block.getBlockFromName("ProjRed|Illumination:projectred.illumination.lamp"), 26))
                : ofChain(ofBlock(Blocks.redstone_lamp, 0)))
        .addElement('g', chainAllGlasses(-1, (te, t) -> te.glassTier = t.byteValue(), te -> (int) te.glassTier))
        .addElement('d', ofChain(ofBlock(TILLED_DIRT_BLOCK, 0), ofBlock(DIRT_BLOCK, 0)))
        .addElement('w', ofChain(isAir(), ofAnyWater(true)))
        .addElement(
            'c',
            buildHatchAdder(MTEExtremeIndustrialGreenhouse.class)
                .atLeast(InputBus, OutputBus, Energy, Maintenance, InputHatch)
                .casingIndex(CASING_OLD.textureId)
                .hint(1)
                .buildAndChain(onElementPass(t -> t.mCasing++, Casings.CleanStainlessSteelCasing.asElement())))
        .addElement('C', onElementPass(t -> t.mCasing++, Casings.CleanStainlessSteelCasing.asElement()))
        .build();

    @Override
    public IStructureDefinition<MTEExtremeIndustrialGreenhouse> getStructureDefinition() {
        return STRUCTURE_DEFINITION;
    }

    @Override
    public boolean checkMachine(IGregTechTileEntity iGregTechTileEntity, ItemStack itemStack) {
        mCasing = 0;
        glassTier = -1;
        isCheckingDirtWater = false;
        if (debug) glassTier = 8;

        if (isOldStructure && !checkPiece(STRUCTURE_PIECE_MAIN_OLD, 2, 5, 0)) return false;
        if (!isOldStructure && !checkPiece(STRUCTURE_PIECE_MAIN, OFFSET_H, OFFSET_V, OFFSET_D)) return false;

        if (this.glassTier < 8 && !this.mEnergyHatches.isEmpty())
            for (MTEHatchEnergy hatchEnergy : this.mEnergyHatches) if (this.glassTier < hatchEnergy.mTier) return false;

        boolean valid = this.mMaintenanceHatches.size() == 1 && !this.mEnergyHatches.isEmpty() && this.mCasing >= 70;

        if (valid) {
            this.updateSeedLimits();
            isCheckingDirtWater = true;
        }

        return valid;
    }

    @Override
    public void construct(ItemStack itemStack, boolean b) {
        if (isOldStructure) buildPiece(STRUCTURE_PIECE_MAIN_OLD, itemStack, b, 2, 5, 0);
        else buildPiece(STRUCTURE_PIECE_MAIN, itemStack, b, OFFSET_H, OFFSET_V, OFFSET_D);
    }

    @Override
    public int survivalConstruct(ItemStack stackSize, int elementBudget, ISurvivalBuildEnvironment env) {
        if (isOldStructure)
            return survivalBuildPiece(STRUCTURE_PIECE_MAIN_OLD, stackSize, 2, 5, 0, elementBudget, env, true);
        else return survivalBuildPiece(
            STRUCTURE_PIECE_MAIN,
            stackSize,
            OFFSET_H,
            OFFSET_V,
            OFFSET_D,
            elementBudget,
            env,
            true);
    }

    @Override
    protected IAlignmentLimits getInitialAlignmentLimits() {
        return (d, r, f) -> d.offsetY == 0 && r.isNotRotated() && f.isNotFlipped();
    }

    // endregion structure stuff

    // region tooltip

    @Override
    protected MultiblockTooltipBuilder createTooltip() {
        MultiblockTooltipBuilder tt = new MultiblockTooltipBuilder();
        String fertilizerBoostMax = String.format("%.0f", EIG_BALANCE_MAX_FERTILIZER_BOOST * 100);
        tt.addMachineType("Crop Farm, EIG")
            .addInfo("Grow your crops like a chad!")
            .addInfo("Use screwdriver to enable/change/disable setup mode")
            .addInfo("Use screwdriver while sneaking to enable/disable IC2 mode")
            .addInfo("Use wire cutters to give incoming IC2 seeds 0 humidity")
            .addInfo("Uses " + EIG_BALANCE_WATER_USAGE_PER_SEED + "L of water per seed per operation")
            .addInfo(
                "Uses 1L of " + new FluidStack(WEEDEX_FLUID, 1).getLocalizedName()
                    + " per operation per seed if it contains more than "
                    + EIG_BALANCE_WEED_EX_USAGE_BEGINS_AT
                    + " seeds")
            .addInfo("Otherwise, around 1% of seeds will be voided each operation")
            .addInfo("You can insert fertilizer each operation to get more drops (max + " + fertilizerBoostMax + ")")
            .addGlassEnergyLimitInfo()
            .addSeparator()
            .addInfo(EnumChatFormatting.GOLD + "Setup Mode:")
            .addInfo("Does not take power")
            .addInfo("There are two modes: input / output")
            .addInfo("Input mode: machine will take seeds from input bus and plant them")
            .addInfo("[IC2] You need to also input block that is required under the crop")
            .addInfo("Output mode: machine will take planted seeds and output them");
        EIGModes.addTooltipInfo(tt);
        tt.beginStructureBlock(7, 7, 9, false)
            .addController("Front bottom center")
            .addCasingInfoMin("Sterile Farm Casing", 70, false)
            .addStructureInfo("Tiered glass")
            .addStructureInfo("The glass tier limits the Energy Input tier")
            .addStructureInfo("The dirt is from RandomThings, must be tilled")
            .addStructureInfo("Regular water and IC2 Distilled Water are accepted")
            .addStructureInfo("Purple lamps are from ProjectRedIllumination. They can be powered and/or inverted")
            .addSubChannelUsage(GTStructureChannels.BOROGLASS)
            .addMaintenanceHatch("Any casing", 1)
            .addInputBus("Any casing", 1)
            .addOutputBus("Any casing", 1)
            .addInputHatch("Any casing", 1)
            .addEnergyHatch("Any casing", 1)
            .toolTipFinisher(GTValues.AuthorKuba);
        return tt;
    }

    @Override
    public String[] getStructureDescription(ItemStack stackSize) {
        List<String> info = new ArrayList<>(Arrays.asList(super.getStructureDescription(stackSize)));
        info.add("The dirt is from RandomThings, must be tilled");
        info.add("Purple lamps are from ProjectRedIllumination. They can be powered and/or inverted");
        return info.toArray(new String[] {});
    }

    // endregion tooltip

    // region (de)constructor

    public MTEExtremeIndustrialGreenhouse(int aID, String aName, String aNameRegional) {
        super(aID, aName, aNameRegional);
    }

    public MTEExtremeIndustrialGreenhouse(String aName) {
        super(aName);
    }

    @Override
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity iGregTechTileEntity) {
        return new MTEExtremeIndustrialGreenhouse(this.mName);
    }

    @Override
    public void onFirstTick(IGregTechTileEntity aBaseMetaTileEntity) {
        super.onFirstTick(aBaseMetaTileEntity);
        if (this.toMigrate != null) {
            // Create the new buckets respectively.
            if (this.mode == EIGModes.IC2) {
                for (EIGMigrationHolder holder : toMigrate) {
                    // We will have to revalidate the seeds on the next cycle.
                    this.buckets
                        .add(new EIGIC2Bucket(holder.seed, holder.count, holder.supportBlock, holder.useNoHumidity));
                }
            } else {
                this.mode = EIGModes.Normal;
                for (EIGMigrationHolder holder : toMigrate) {
                    holder.seed.stackSize = holder.count;
                    EIGBucket bucket = this.mode.tryCreateNewBucket(this, holder.seed, Integer.MAX_VALUE, false);
                    if (bucket == null) {
                        // if we somehow can't grow the seed, try ejecting it at least.
                        holder.seed.stackSize = holder.count;
                        this.addOutputPartial(holder.seed);
                        continue;
                    }
                    this.buckets.add(bucket);
                }
            }
        }
    }

    /**
     * Ejects all the seeds when the controller is broken.
     */
    @Override
    public void onRemoval() {
        super.onRemoval();

        // attempt to empty all buckets
        buckets.removeIf(this::tryEmptyBucket);
        if (buckets.isEmpty()) return;

        // attempt to drop non outputted items into the world.
        IGregTechTileEntity mte = this.getBaseMetaTileEntity();
        for (EIGBucket bucket : this.buckets) {
            for (ItemStack stack : bucket.tryRemoveSeed(bucket.getSeedCount(), false)) {
                EntityItem entityitem = new EntityItem(
                    mte.getWorld(),
                    mte.getXCoord(),
                    mte.getYCoord(),
                    mte.getZCoord(),
                    stack);
                entityitem.delayBeforeCanPickup = 10;
                mte.getWorld()
                    .spawnEntityInWorld(entityitem);
            }
        }
    }

    // endregion

    // region tool interactions

    /**
     * Right click = change setup phase Shift+Right Click = change EIG Mode
     */
    @Override
    public void onScrewdriverRightClick(ForgeDirection side, EntityPlayer aPlayer, float aX, float aY, float aZ,
        ItemStack aTool) {
        if (aPlayer.isSneaking()) {
            tryChangeMode(aPlayer);
        } else {
            tryChangeSetupPhase(aPlayer);
        }
    }

    /**
     * Right-Clicking with wire cutters toggle no hydration mode.
     */
    @Override
    public boolean onWireCutterRightClick(ForgeDirection side, ForgeDirection wrenchingSide, EntityPlayer aPlayer,
        float aX, float aY, float aZ, ItemStack aTool) {
        this.tryChangeHumidityMode(aPlayer);
        return true;
    }

    // endregion tool interactions

    // region mode change standardisation

    /**
     * Attempts to change the setup phase of the EIG to the next mode
     *
     * @param aPlayer The player to notify for success and errors
     */
    private void tryChangeSetupPhase(EntityPlayer aPlayer) {
        // TODO: Create l10n entries for the setup phase change messages.
        if (this.mMaxProgresstime > 0) {
            GTUtility.sendChatToPlayer(aPlayer, "You can't enable/disable setup if the machine is working!");
            return;
        }
        this.setupPhase++;
        if (this.setupPhase == 3) this.setupPhase = 0;
        String phaseChangeMessage = "EIG is now running in ";
        switch (this.setupPhase) {
            case 0:
                phaseChangeMessage += "operational mode.";
                break;
            case 1:
                phaseChangeMessage += "seed input mode.";
                break;
            case 2:
                phaseChangeMessage += "seed output mode.";
                break;
            default:
                phaseChangeMessage += "an invalid mode please send us a ticket!";
                break;
        }
        this.updateSeedLimits();
        GTUtility.sendChatToPlayer(aPlayer, phaseChangeMessage);
    }

    /**
     * Attempts to change the mode of the EIG to the next mode.
     *
     * @param aPlayer The player to notify of success and errors
     */
    private void tryChangeMode(EntityPlayer aPlayer) {
        // TODO: Create l10n entries for the mode change messages.
        if (this.mMaxProgresstime > 0) {
            GTUtility.sendChatToPlayer(aPlayer, "You can't change mode if the machine is working!");
            return;
        }
        if (!this.buckets.isEmpty()) {
            GTUtility.sendChatToPlayer(aPlayer, "You can't change mode if there are seeds inside!");
            return;
        }
        this.mode = EIGModes.getNextMode(this.mode);
        this.updateSeedLimits();
        GTUtility.sendChatToPlayer(aPlayer, "Changed mode to: " + this.mode.getName());
    }

    /**
     * Attempts to toggle the hydration mode of the EIG.
     *
     * @param aPlayer The player to notify for success and errors
     */
    private void tryChangeHumidityMode(EntityPlayer aPlayer) {
        // TODO: Create l10n entries for the humidity status interactions.
        this.useNoHumidity = !this.useNoHumidity;
        if (this.useNoHumidity) {
            GTUtility.sendChatToPlayer(aPlayer, "No Humidity mode enabled.");
        } else {
            GTUtility.sendChatToPlayer(aPlayer, "No Humidity mode disabled.");
        }
    }

    // endregion mode change standardisation

    // region (de)serialisations

    @Override
    public void saveNBTData(NBTTagCompound aNBT) {
        super.saveNBTData(aNBT);
        aNBT.setInteger("version", NBT_REVISION);
        aNBT.setByte("glassTier", this.glassTier);
        aNBT.setInteger("setupPhase", this.setupPhase);
        aNBT.setString("mode", this.mode.getName());
        aNBT.setBoolean("isNoHumidity", this.useNoHumidity);
        NBTTagList bucketListNBT = new NBTTagList();
        for (EIGBucket b : this.buckets) {
            bucketListNBT.appendTag(b.save());
        }
        aNBT.setTag(
            "progress",
            this.dropTracker.intersect(this.guiDropTracker)
                .save());
        aNBT.setTag("buckets", bucketListNBT);
        // TODO: Remove after 2.9
        aNBT.setBoolean("isOldStructure", this.isOldStructure);
    }

    private static class EIGMigrationHolder {

        public final ItemStack seed;
        public final ItemStack supportBlock;
        public final boolean useNoHumidity;
        public int count;
        public boolean isValid = false;

        public EIGMigrationHolder(NBTTagCompound nbt) {
            this.seed = readItemStackFromNBT(nbt.getCompoundTag("input"));
            this.count = this.seed.stackSize;
            this.seed.stackSize = 1;
            this.supportBlock = nbt.hasKey("undercrop", 10) ? readItemStackFromNBT(nbt.getCompoundTag("undercrop"))
                : null;
            this.useNoHumidity = nbt.getBoolean("noHumidity");
            this.isValid = true;
        }

        public String getKey() {
            if (this.supportBlock == null) return seed.toString();
            return "(" + this.seed.toString() + "," + this.supportBlock + ")";
        }

    }

    @Override
    public void loadNBTData(NBTTagCompound aNBT) {
        super.loadNBTData(aNBT);
        int revision = aNBT.hasKey("version", 3) ? aNBT.getInteger("version") : 0;
        if (revision <= 0) {
            // migrate old EIG with greenhouse slots to new Bucker mode and fix variable names
            this.glassTier = aNBT.getByte("glasTier");
            this.setupPhase = aNBT.getInteger("setupphase");
            this.mode = aNBT.getBoolean("isIC2Mode") ? EIGModes.IC2 : EIGModes.Normal;
            this.useNoHumidity = aNBT.getBoolean("isNoHumidity");
            // aggregate all seed types
            HashMap<String, EIGMigrationHolder> toMigrate = new HashMap<>();
            for (int i = 0; i < aNBT.getInteger("mStorageSize"); i++) {
                EIGMigrationHolder holder = new EIGMigrationHolder(aNBT.getCompoundTag("mStorage." + i));
                if (toMigrate.containsKey(holder.getKey())) {
                    toMigrate.get(holder.getKey()).count += holder.count;
                } else {
                    toMigrate.put(holder.getKey(), holder);
                }
            }

            this.toMigrate = toMigrate.values();
        } else {
            if (revision == 1) isOldStructure = true;
            else isOldStructure = aNBT.getBoolean("isOldStructure");
            this.glassTier = aNBT.getByte("glassTier");
            this.setupPhase = aNBT.getInteger("setupPhase");
            this.mode = EIGModes.getModeFromName(aNBT.getString("mode"));
            this.useNoHumidity = aNBT.getBoolean("isNoHumidity");
            this.mode.restoreBuckets(aNBT.getTagList("buckets", 10), this.buckets);
            new EIGDropTable(aNBT.getTagList("progress", 10)).addTo(this.dropTracker);
        }
    }

    // endregion

    // region crop visuals rendering

    @Override
    public byte getUpdateData() {
        return isOldStructure ? (byte) 1 : (byte) 0;
    }

    @Override
    public void receiveClientEvent(byte eventID, byte value) {
        if (eventID == GregTechTileClientEvents.CHANGE_CUSTOM_DATA) {
            isOldStructure = value == 0b1;
        }
    }

    @SideOnly(Side.CLIENT)
    public void spawnVisualCrops(World world, int x, int y, int z, ExtendedFacing facing, int age,
        boolean isOldStructure) {
        CropRenderer crop = new CropRenderer(world, x, y, z, facing, age, isOldStructure);
        Minecraft.getMinecraft().effectRenderer.addEffect(crop);
    }

    @Override
    public void onPostTick(IGregTechTileEntity aBaseMetaTileEntity, long aTick) {
        super.onPostTick(aBaseMetaTileEntity, aTick);
        if (aBaseMetaTileEntity.isClientSide()) {
            if (aBaseMetaTileEntity.isActive() && aTick % 40 == 0) {
                int[] abc = new int[] { 0, -2, isOldStructure ? 2 : 1 };
                int[] xyz = new int[] { 0, 0, 0 };
                this.getExtendedFacing()
                    .getWorldOffset(abc, xyz);
                xyz[0] += aBaseMetaTileEntity.getXCoord();
                xyz[1] += aBaseMetaTileEntity.getYCoord();
                xyz[2] += aBaseMetaTileEntity.getZCoord();
                spawnVisualCrops(
                    aBaseMetaTileEntity.getWorld(),
                    xyz[0],
                    xyz[1],
                    xyz[2],
                    this.getExtendedFacing(),
                    40,
                    isOldStructure);
            }
        } else {
            if (!isOldStructure && isCheckingDirtWater && mMachine && aTick % 20 == 0) {
                World world = aBaseMetaTileEntity.getWorld();
                int didPlace = 0;
                // try to till the dirt and place the water
                for (int x = -1; x <= 1; x++) {
                    for (int z = 1; z <= 7; z++) {
                        if (didPlace >= 3) return;
                        int[] abc = new int[] { x, -1, z };
                        int[] xyz = new int[] { 0, 0, 0 };
                        this.getExtendedFacing()
                            .getWorldOffset(abc, xyz);
                        xyz[0] += aBaseMetaTileEntity.getXCoord();
                        xyz[1] += aBaseMetaTileEntity.getYCoord();
                        xyz[2] += aBaseMetaTileEntity.getZCoord();
                        Block block = world.getBlock(xyz[0], xyz[1], xyz[2]);
                        if (block == DIRT_BLOCK) {
                            world.setBlock(xyz[0], xyz[1], xyz[2], TILLED_DIRT_BLOCK, 0, 3);
                            world.playSoundEffect(
                                xyz[0] + 0.5f,
                                xyz[1] + 0.5f,
                                xyz[2] + 0.5f,
                                Blocks.farmland.stepSound.getStepResourcePath(),
                                (Blocks.farmland.stepSound.getVolume() + 1.0F) / 2.0F,
                                Blocks.farmland.stepSound.getPitch() * 0.8F);
                            didPlace++;
                        }
                    }
                }
                for (int x = -2; x <= 2; x += 4) {
                    for (int z = 1; z <= 7; z++) {
                        if (didPlace >= 3) return;
                        int[] abc = new int[] { x, -1, z };
                        int[] xyz = new int[] { 0, 0, 0 };
                        this.getExtendedFacing()
                            .getWorldOffset(abc, xyz);
                        xyz[0] += aBaseMetaTileEntity.getXCoord();
                        xyz[1] += aBaseMetaTileEntity.getYCoord();
                        xyz[2] += aBaseMetaTileEntity.getZCoord();
                        Block block = world.getBlock(xyz[0], xyz[1], xyz[2]);
                        boolean isCOFHCore = Mods.COFHCore.isModLoaded()
                            && (block instanceof BlockWater || block instanceof BlockTickingWater);
                        boolean isWater = (block == Blocks.water) || isCOFHCore;
                        boolean isAir = block == Blocks.flowing_water || block == Blocks.air;
                        if (!isWater && isAir) {
                            world.setBlock(xyz[0], xyz[1], xyz[2], Blocks.water, 0, 3);
                            world.playSoundEffect(
                                xyz[0] + 0.5f,
                                xyz[1] + 0.5f,
                                xyz[2] + 0.5f,
                                "random.splash",
                                0.5F,
                                1.0F);
                            didPlace++;
                        }
                    }
                }
                isCheckingDirtWater = false;
            }
        }
    }

    // endregion crop visuals rendering

    /**
     * Calculates the total amount of seeds in the EIG
     *
     * @return The number of seeds in the EIG.
     */
    private int getTotalSeedCount() {
        // null check is to prevent a occasional weird NPE from MUI
        return this.buckets.parallelStream()
            .reduce(0, (b, t) -> b + t.getSeedCount(), Integer::sum);
    }

    /**
     * Updates the max seed counts of the machine
     */
    private void updateSeedLimits() {
        this.maxSeedTypes = this.mode.getSlotCount(getVoltageTier());
        this.maxSeedCount = this.maxSeedTypes * this.mode.getSeedCapacityPerSlot();
    }

    /**
     * Attempts to drain the multi of a given fluid, will only return true if all fluid is consumed.
     *
     * @param toConsume    A fluid stack of the fluid to consume.
     * @param drainPartial True to allow partial consumption.
     * @return True when all the fluid has been consumed.
     */
    private boolean tryDrain(FluidStack toConsume, boolean drainPartial) {
        // Nothing to consume = success I guess?
        if (toConsume == null || toConsume.amount <= 0) return true;
        // TODO: improve fluid draining logic.
        List<FluidStack> fluids = this.getStoredFluids();
        List<FluidStack> fluidsToUse = new ArrayList<>(fluids.size());
        int remaining = toConsume.amount;
        for (FluidStack fluid : fluids) {
            if (fluid.isFluidEqual(toConsume)) {
                remaining -= fluid.amount;
                fluidsToUse.add(fluid);
                if (remaining <= 0) break;
            }
        }
        if (!drainPartial && remaining > 0 && !debug) return false;
        boolean success = remaining <= 0;
        remaining = toConsume.amount - Math.max(0, remaining);
        for (FluidStack fluid : fluidsToUse) {
            int used = Math.min(remaining, fluid.amount);
            fluid.amount -= used;
            remaining -= used;
        }
        return success;
    }

    /**
     * Checks if a stack contains an item that can be used as fertilizer
     *
     * @param item A stack of item to validate
     * @return True if the item can be consumed as fertilizer.
     */
    public static boolean isFertilizer(ItemStack item) {
        if (item == null || item.stackSize <= 0) return false;
        for (ItemStack fert : FERTILIZER_ITEM_LIST) {
            if (GTUtility.areStacksEqual(item, fert)) return true;
        }
        return false;
    }

    private boolean tryEmptyBucket(EIGBucket bucket) {
        // check if it's already empty
        if (bucket.getSeedCount() <= 0) return true;

        // check if we have an ME output bus to output to.
        for (MTEHatchOutputBus tHatch : validMTEList(mOutputBusses)) {
            if (!(tHatch instanceof MTEHatchOutputBusME)) continue;
            for (ItemStack stack : bucket.tryRemoveSeed(bucket.getSeedCount(), false)) {
                tHatch.storePartial(stack);
            }
            return true;
        }

        // Else attempt to empty the bucket while not voiding anything.
        ItemStack[] simulated = bucket.tryRemoveSeed(1, true);
        VoidProtectionHelper helper = new VoidProtectionHelper().setMachine(this, true, false)
            .setItemOutputs(simulated)
            .setMaxParallel(bucket.getSeedCount())
            .build();
        if (helper.getMaxParallel() > 0) {
            for (ItemStack toOutput : bucket.tryRemoveSeed(helper.getMaxParallel(), false)) {
                for (MTEHatchOutputBus tHatch : validMTEList(mOutputBusses)) {
                    if (tHatch.storePartial(toOutput)) break;
                }
            }
        }
        return bucket.getSeedCount() <= 0;
    }

    @Override
    @NotNull
    public CheckRecipeResult checkProcessing() {
        int tier = getVoltageTier();
        updateSeedLimits();

        if (setupPhase > 0) {
            if ((buckets.size() >= maxSeedTypes && setupPhase == 1) || (buckets.isEmpty() && setupPhase == 2))
                return CheckRecipeResultRegistry.NO_RECIPE;

            if (setupPhase == 1) {
                List<ItemStack> inputs = getStoredInputs();
                for (ItemStack input : inputs) {
                    addCrop(input);
                    if (buckets.size() >= maxSeedTypes) break;
                }
            } else if (setupPhase == 2) {
                for (Iterator<EIGBucket> iterator = this.buckets.iterator(); iterator.hasNext();) {
                    EIGBucket bucket = iterator.next();
                    if (tryEmptyBucket(bucket)) {
                        iterator.remove();
                    } else {
                        this.mMaxProgresstime = 20;
                        this.lEUt = 0;
                        return CheckRecipeResultRegistry.ITEM_OUTPUT_FULL;
                    }
                }
            }

            this.updateSlots();
            this.mMaxProgresstime = 5;
            this.lEUt = 0;
            this.mEfficiency = (10000 - (getIdealStatus() - getRepairStatus()) * 1000);
            this.mEfficiencyIncrease = 10000;
            return CheckRecipeResultRegistry.SUCCESSFUL;
        }
        if (this.maxSeedTypes < this.buckets.size()) {
            return SimpleCheckRecipeResult.ofFailure("EIG_slotoverflow");
        }
        int seedCount = this.getTotalSeedCount();
        if (this.maxSeedCount < seedCount) {
            return SimpleCheckRecipeResult.ofFailure("EIG_seedOverflow");
        }

        // Kick out bad buckets.
        for (Iterator<EIGBucket> iterator = this.buckets.iterator(); iterator.hasNext();) {
            EIGBucket bucket = iterator.next();
            if (bucket.isValid() || bucket.revalidate(this)) continue;
            // attempt to empty the bucket
            tryEmptyBucket(bucket);
            // remove empty bucket and attempt to revalidate invalid buckets
            if (bucket.getSeedCount() <= 0) {
                iterator.remove();
            }
        }

        if (this.buckets.isEmpty()) return CheckRecipeResultRegistry.NO_RECIPE;

        // Compute the Weed-EX and water requirements,
        // TODO: We only really need to update water usage and WeedEX usage when adding seeds or when loading NBT.
        this.waterUsage = seedCount * 1000;
        this.weedEXUsage = (seedCount >= EIG_BALANCE_WEED_EX_USAGE_BEGINS_AT ? seedCount : 0)
            * this.mode.getWeedEXMultiplier();

        // Consume water, fail if we don't have enough
        if (!this.tryDrain(new FluidStack(FluidRegistry.WATER, this.waterUsage), false)) {
            return SimpleCheckRecipeResult.ofFailure("EIG_missingwater");
        }

        // Consume weed ex, if there isn't enough we consume what's there but don't fail
        if (weedEXUsage > 0 && !this.tryDrain(new FluidStack(WEEDEX_FLUID, this.weedEXUsage), true)) {
            IGregTechTileEntity baseMTE = this.getBaseMetaTileEntity();
            // Cap seed murder to the Weed EX limit, no more senseless murder of bystanders
            int killLimit = (seedCount - EIG_BALANCE_WEED_EX_USAGE_BEGINS_AT + 1);
            int toKill = Math.min(killLimit, baseMTE.getRandomNumber((int) ((double) seedCount * 0.02d) + 1));
            if (toKill > 0) {
                for (Iterator<EIGBucket> iterator = this.buckets.iterator(); iterator.hasNext();) {
                    EIGBucket bucket = iterator.next();
                    ItemStack[] removed = bucket.tryRemoveSeed(toKill, false);
                    if (removed == null || removed[0].stackSize <= 0) continue;
                    toKill -= removed[0].stackSize;
                    // if bucket is empty, yeet it out.
                    if (bucket.getSeedCount() <= 0) iterator.remove();
                    // if we are out of crops to kill we can just leave
                    if (toKill <= 0) break;
                }
            }
        }

        // OVERCLOCK
        // FERTILIZER IDEA:
        // IC2 +10% per fertilizer per crop per operation
        // NORMAL +200% per fertilizer per crop per operation

        int consumedFertilizer = 0;
        int maxFertilizerToConsume = 0;
        for (EIGBucket bucket : this.buckets)
            maxFertilizerToConsume += bucket.getSeedCount() * this.mode.getMaxFertilizerUsagePerSeed();

        ArrayList<ItemStack> inputs = getStoredInputs();
        for (ItemStack i : inputs) {
            if (isFertilizer(i)) {
                int used = Math.min(i.stackSize, maxFertilizerToConsume - consumedFertilizer);
                i.stackSize -= used;
                consumedFertilizer += used;
            }
            if (consumedFertilizer == maxFertilizerToConsume) break;
        }
        double multiplier = 1.d
            + (((double) consumedFertilizer / (double) maxFertilizerToConsume) * EIG_BALANCE_MAX_FERTILIZER_BOOST);

        // compute drops based on the drop tracker
        this.guiDropTracker = new EIGDropTable();
        if (this.mode == EIGModes.IC2) {
            if (glassTier < (EIG_BALANCE_IC2_ACCELERATOR_TIER + 1))
                return SimpleCheckRecipeResult.ofFailure("EIG_ic2glass");
            this.mMaxProgresstime = 100;
            // determine the amount of time we are simulating on the seed.
            double timeElapsed = ((double) this.mMaxProgresstime * (1 << EIG_BALANCE_IC2_ACCELERATOR_TIER));
            // Add drops to the drop tracker for each seed bucket.
            for (EIGBucket bucket : this.buckets) {
                bucket.addProgress(timeElapsed * multiplier, this.guiDropTracker);
            }
        } else if (this.mode == EIGModes.Normal) {
            this.mMaxProgresstime = Math.max(20, 100 / (tier - 3)); // Min 1 s
            for (EIGBucket bucket : this.buckets) {
                bucket.addProgress(multiplier, this.guiDropTracker);
            }
        }

        this.guiDropTracker.addTo(this.dropTracker);
        this.mOutputItems = this.dropTracker.getDrops();

        // consume power
        this.lEUt = -(long) ((double) GTValues.V[tier] * 0.99d);
        this.mEfficiency = (10000 - (getIdealStatus() - getRepairStatus()) * 1000);
        this.mEfficiencyIncrease = 10000;
        this.updateSlots();
        return CheckRecipeResultRegistry.SUCCESSFUL;
    }

    private ItemStack addCrop(ItemStack input) {
        return addCrop(input, false) ? input : null;
    }

    /**
     * Adds a seed to the EIG
     *
     * @param input    The item to add to the EIG.
     * @param simulate Set to true to not actually consume any input.
     * @return True if all items were consumed
     */
    private boolean addCrop(ItemStack input, boolean simulate) {
        // Nothing to add = success since technically nothing should have changed?
        if (input == null || input.stackSize <= 0) return true;

        // For safety's sake copy the input if we are simulating to make sure we aren't modifying it
        if (simulate) input = input.copy();

        // Cap input count to current seed max
        int addCap = Math.min(input.stackSize, this.maxSeedCount - this.getTotalSeedCount());
        if (addCap <= 0) return false;

        // Attempt to find a compatible bucket that already exists
        for (EIGBucket bucket : this.buckets) {
            int consumed = bucket.tryAddSeed(this, input, addCap, simulate);
            if (consumed <= 0) continue;
            return input.stackSize <= 0;
        }

        // Check if we have space for a new bucket
        if (this.maxSeedTypes <= this.buckets.size()) {
            return false;
        }

        // try creating a new bucket, this only returns valid buckets.
        EIGBucket bucket = this.mode.tryCreateNewBucket(this, input, addCap, simulate);
        if (bucket == null) return false;
        this.buckets.add(bucket);
        return input.stackSize <= 0;
    }

    // region ui

    private static final UIInfo<?, ?> GreenhouseUI = createKTMetaTileEntityUI(
        KT_ModulaUIContainer_ExtremeIndustrialGreenhouse::new);

    @Override
    public boolean onRightclick(IGregTechTileEntity aBaseMetaTileEntity, EntityPlayer aPlayer) {
        if (aBaseMetaTileEntity.isClientSide()) return true;
        GreenhouseUI.open(
            aPlayer,
            aBaseMetaTileEntity.getWorld(),
            aBaseMetaTileEntity.getXCoord(),
            aBaseMetaTileEntity.getYCoord(),
            aBaseMetaTileEntity.getZCoord());
        return true;
    }

    private static class KT_ModulaUIContainer_ExtremeIndustrialGreenhouse extends ModularUIContainer {

        final WeakReference<MTEExtremeIndustrialGreenhouse> parent;

        public KT_ModulaUIContainer_ExtremeIndustrialGreenhouse(ModularUIContext context, ModularWindow mainWindow,
            MTEExtremeIndustrialGreenhouse mte) {
            super(context, mainWindow);
            parent = new WeakReference<>(mte);
        }

        @Override
        public ItemStack transferStackInSlot(EntityPlayer aPlayer, int aSlotIndex) {
            if (!(aPlayer instanceof EntityPlayerMP)) return super.transferStackInSlot(aPlayer, aSlotIndex);
            final Slot s = getSlot(aSlotIndex);
            if (s == null) return super.transferStackInSlot(aPlayer, aSlotIndex);
            if (aSlotIndex >= 36) return super.transferStackInSlot(aPlayer, aSlotIndex);
            final ItemStack aStack = s.getStack();
            if (aStack == null) return super.transferStackInSlot(aPlayer, aSlotIndex);
            MTEExtremeIndustrialGreenhouse mte = parent.get();
            if (mte == null) return super.transferStackInSlot(aPlayer, aSlotIndex);
            // if (mte.buckets.size() >= mte.maxSeedTypes) return super.transferStackInSlot(aPlayer, aSlotIndex);
            if (mte.mMaxProgresstime > 0) {
                GTUtility.sendChatToPlayer(aPlayer, EnumChatFormatting.RED + "Can't insert while running !");
                return super.transferStackInSlot(aPlayer, aSlotIndex);
            }

            mte.addCrop(aStack);
            if (aStack.stackSize <= 0) s.putStack(null);
            else s.putStack(aStack);
            detectAndSendChanges();
            return null;
        }
    }

    @Override
    protected void addConfigurationWidgets(DynamicPositionedRow configurationElements, UIBuildContext buildContext) {
        buildContext.addSyncedWindow(CONFIGURATION_WINDOW_ID, this::createConfigurationWindow);
        configurationElements.setSynced(false);
        configurationElements.widget(
            new ButtonWidget().setOnClick(
                (clickData, widget) -> {
                    if (!widget.isClient()) widget.getContext()
                        .openSyncedWindow(CONFIGURATION_WINDOW_ID);
                })
                .setBackground(GTUITextures.BUTTON_STANDARD, GTUITextures.OVERLAY_BUTTON_CYCLIC)
                .addTooltip(StatCollector.translateToLocal("kubatech.gui.text.configuration"))
                .setSize(16, 16));
    }

    EIGDynamicInventory<EIGBucket> dynamicInventory = new EIGDynamicInventory<>(
        128,
        60,
        () -> this.maxSeedTypes,
        () -> this.maxSeedCount,
        this.buckets::size,
        this::getTotalSeedCount,
        this.buckets,
        EIGBucket::getSeedStack).allowInventoryInjection(this::addCrop)
            .allowInventoryExtraction((bucket, player) -> {
                if (bucket == null) return null;
                int maxRemove = bucket.getSeedStack()
                    .getMaxStackSize();
                ItemStack[] outputs = bucket.tryRemoveSeed(maxRemove, false);
                if (outputs == null || outputs.length == 0) return null;
                ItemStack ret = outputs[0];
                for (int i = 1; i < outputs.length; i++) {
                    ItemStack suppertItem = outputs[i];
                    if (!player.inventory.addItemStackToInventory(suppertItem)) {
                        player.entityDropItem(suppertItem, 0.f);
                    }
                }
                if (bucket.getSeedCount() <= 0) this.buckets.remove(bucket);
                return ret;
            })
            // TODO: re-add allow inventory replace?
            .setEnabled(() -> this.mMaxProgresstime == 0);

    @Override
    public void createInventorySlots() {

    }

    private boolean isInInventory = true;

    @Override
    public void addUIWidgets(ModularWindow.Builder builder, UIBuildContext buildContext) {
        isInInventory = !getBaseMetaTileEntity().isActive();
        builder.widget(
            new DrawableWidget().setDrawable(GTUITextures.PICTURE_SCREEN_BLACK)
                .setPos(4, 4)
                .setSize(190, 85)
                .setEnabled(w -> !isInInventory));
        builder.widget(
            dynamicInventory.asWidget(builder, buildContext)
                .setPos(10, 16)
                .setEnabled(w -> isInInventory));

        builder.widget(
            new CycleButtonWidget().setToggle(() -> isInInventory, i -> isInInventory = i)
                .setTextureGetter(
                    i -> i == 0 ? new Text(StatCollector.translateToLocal("kubatech.gui.text.inventory"))
                        : new Text(StatCollector.translateToLocal("kubatech.gui.text.status")))
                .setBackground(GTUITextures.BUTTON_STANDARD)
                .setPos(140, 91)
                .setSize(55, 16));

        final DynamicPositionedColumn screenElements = new DynamicPositionedColumn();
        drawTexts(screenElements, null);
        builder.widget(
            new Scrollable().setVerticalScroll()
                .widget(screenElements)
                .setPos(10, 7)
                .setSize(182, 79)
                .setEnabled(w -> !isInInventory));

        builder.widget(createPowerSwitchButton(builder))
            .widget(createVoidExcessButton(builder))
            .widget(createInputSeparationButton(builder))
            .widget(createBatchModeButton(builder))
            .widget(createLockToSingleRecipeButton(builder))
            .widget(createStructureUpdateButton(builder));

        DynamicPositionedRow configurationElements = new DynamicPositionedRow();
        addConfigurationWidgets(configurationElements, buildContext);

        builder.widget(
            configurationElements.setSpace(2)
                .setAlignment(MainAxisAlignment.SPACE_BETWEEN)
                .setPos(getRecipeLockingButtonPos().add(18, 0)));
    }

    protected ModularWindow createConfigurationWindow(final EntityPlayer player) {
        ModularWindow.Builder builder = ModularWindow.builder(200, 100);
        builder.setBackground(ModularUITextures.VANILLA_BACKGROUND);
        builder.widget(
            new DrawableWidget().setDrawable(GTUITextures.OVERLAY_BUTTON_CYCLIC)
                .setPos(5, 5)
                .setSize(16, 16))
            .widget(new TextWidget(StatCollector.translateToLocal("kubatech.gui.text.configuration")).setPos(25, 9))
            .widget(
                ButtonWidget.closeWindowButton(true)
                    .setPos(185, 3))
            .widget(
                new Column().widget(
                    new CycleButtonWidget().setLength(3)
                        .setGetter(() -> this.setupPhase)
                        .setSetter(val -> {
                            if (!(player instanceof EntityPlayerMP)) return;
                            tryChangeSetupPhase(player);
                        })
                        .addTooltip(
                            0,
                            new Text(StatCollector.translateToLocal("kubatech.gui.text.operating"))
                                .color(Color.GREEN.dark(3)))
                        .addTooltip(
                            1,
                            new Text(StatCollector.translateToLocal("kubatech.gui.text.input"))
                                .color(Color.YELLOW.dark(3)))
                        .addTooltip(
                            2,
                            new Text(StatCollector.translateToLocal("kubatech.gui.text.output"))
                                .color(Color.YELLOW.dark(3)))
                        .setTextureGetter(
                            i -> i == 0
                                ? new Text(StatCollector.translateToLocal("kubatech.gui.text.operating"))
                                    .color(Color.GREEN.dark(3))
                                    .withFixedSize(70 - 18, 18, 15, 0)
                                : i == 1
                                    ? new Text(StatCollector.translateToLocal("kubatech.gui.text.input"))
                                        .color(Color.YELLOW.dark(3))
                                        .withFixedSize(70 - 18, 18, 15, 0)
                                    : new Text(StatCollector.translateToLocal("kubatech.gui.text.output"))
                                        .color(Color.YELLOW.dark(3))
                                        .withFixedSize(70 - 18, 18, 15, 0))
                        .setBackground(
                            ModularUITextures.VANILLA_BACKGROUND,
                            GTUITextures.OVERLAY_BUTTON_CYCLIC.withFixedSize(18, 18))
                        .setSize(70, 18)
                        .addTooltip(StatCollector.translateToLocal("kubatech.gui.text.eig.setup_mode")))
                    .widget(
                        new CycleButtonWidget().setLength(2)
                            .setGetter(() -> this.mode.getUIIndex())
                            .setSetter(val -> {
                                if (!(player instanceof EntityPlayerMP)) return;
                                tryChangeMode(player);
                            })
                            .addTooltip(
                                0,
                                new Text(StatCollector.translateToLocal("kubatech.gui.text.eig.disabled"))
                                    .color(Color.RED.dark(3)))
                            .addTooltip(
                                1,
                                new Text(StatCollector.translateToLocal("kubatech.gui.text.eig.enabled"))
                                    .color(Color.GREEN.dark(3)))
                            .setTextureGetter(
                                i -> i == 0
                                    ? new Text(StatCollector.translateToLocal("kubatech.gui.text.eig.disabled"))
                                        .color(Color.RED.dark(3))
                                        .withFixedSize(70 - 18, 18, 15, 0)
                                    : new Text(StatCollector.translateToLocal("kubatech.gui.text.eig.enabled"))
                                        .color(Color.GREEN.dark(3))
                                        .withFixedSize(70 - 18, 18, 15, 0))
                            .setBackground(
                                ModularUITextures.VANILLA_BACKGROUND,
                                GTUITextures.OVERLAY_BUTTON_CYCLIC.withFixedSize(18, 18))
                            .setSize(70, 18)
                            .addTooltip(StatCollector.translateToLocal("kubatech.gui.text.eig.ic2_mode")))
                    .widget(
                        new CycleButtonWidget().setLength(2)
                            .setGetter(() -> useNoHumidity ? 1 : 0)
                            .setSetter(val -> {
                                if (!(player instanceof EntityPlayerMP)) return;
                                this.tryChangeHumidityMode(player);
                            })
                            .addTooltip(
                                0,
                                new Text(StatCollector.translateToLocal("kubatech.gui.text.eig.disabled"))
                                    .color(Color.RED.dark(3)))
                            .addTooltip(
                                1,
                                new Text(StatCollector.translateToLocal("kubatech.gui.text.eig.enabled"))
                                    .color(Color.GREEN.dark(3)))
                            .setTextureGetter(
                                i -> i == 0
                                    ? new Text(StatCollector.translateToLocal("kubatech.gui.text.eig.disabled"))
                                        .color(Color.RED.dark(3))
                                        .withFixedSize(70 - 18, 18, 15, 0)
                                    : new Text(StatCollector.translateToLocal("kubatech.gui.text.eig.enabled"))
                                        .color(Color.GREEN.dark(3))
                                        .withFixedSize(70 - 18, 18, 15, 0))
                            .setBackground(
                                ModularUITextures.VANILLA_BACKGROUND,
                                GTUITextures.OVERLAY_BUTTON_CYCLIC.withFixedSize(18, 18))
                            .setSize(70, 18)
                            .addTooltip(StatCollector.translateToLocal("kubatech.gui.text.eig.no_humidity_mode")))
                    .setEnabled(widget -> !getBaseMetaTileEntity().isActive())
                    .setPos(10, 30))
            .widget(
                new Column().widget(
                    new TextWidget(StatCollector.translateToLocal("kubatech.gui.text.eig.setup_mode")).setSize(100, 18))
                    .widget(
                        new TextWidget(StatCollector.translateToLocal("kubatech.gui.text.eig.ic2_mode"))
                            .setSize(100, 18))
                    .widget(
                        new TextWidget(StatCollector.translateToLocal("kubatech.gui.text.eig.no_humidity_mode"))
                            .setSize(100, 18))
                    .setEnabled(widget -> !getBaseMetaTileEntity().isActive())
                    .setPos(80, 30))
            .widget(
                new DrawableWidget().setDrawable(GTUITextures.OVERLAY_BUTTON_CROSS)
                    .setSize(18, 18)
                    .setPos(10, 30)
                    .addTooltip(
                        new Text(StatCollector.translateToLocal("GT5U.gui.text.cannot_change_when_running"))
                            .color(Color.RED.dark(3)))
                    .setEnabled(widget -> getBaseMetaTileEntity().isActive()));
        return builder.build();
    }

    @Override
    protected Widget generateCurrentRecipeInfoWidget() {
        final DynamicPositionedColumn processingDetails = new DynamicPositionedColumn();

        if (mOutputItems == null || synchedGUIDropTracker == null) return processingDetails;

        LinkedHashMap<ItemStack, Double> sortedMap = synchedGUIDropTracker.entrySet()
            .stream()
            .sorted(Comparator.comparingInt((Map.Entry<ItemStack, Double> entry) -> {
                assert mOutputItems != null;
                return Arrays.stream(mOutputItems)
                    .filter(s -> s.isItemEqual(entry.getKey()))
                    .mapToInt(i -> i.stackSize)
                    .sum();
            })
                .reversed())
            .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e1, LinkedHashMap::new));

        for (Map.Entry<ItemStack, Double> drop : sortedMap.entrySet()) {
            assert mOutputItems != null;
            int outputSize = Arrays.stream(mOutputItems)
                .filter(s -> s.isItemEqual(drop.getKey()))
                .mapToInt(i -> i.stackSize)
                .sum();
            if (outputSize != 0) {
                Long itemCount = (long) outputSize;
                String itemName = drop.getKey()
                    .getDisplayName();
                String itemAmountString = EnumChatFormatting.WHITE + " x "
                    + EnumChatFormatting.GOLD
                    + formatShortenedLong(itemCount)
                    + EnumChatFormatting.WHITE
                    + appendRate(false, itemCount, true);
                String lineText = EnumChatFormatting.AQUA + truncateText(itemName, 20) + itemAmountString;
                String lineTooltip = EnumChatFormatting.AQUA + itemName + "\n" + appendRate(false, itemCount, false);

                processingDetails.widget(
                    new MultiChildWidget().addChild(
                        new ItemDrawable(
                            drop.getKey()
                                .copy()).asWidget()
                                    .setSize(8, 8)
                                    .setPos(0, 0))
                        .addChild(
                            new TextWidget(lineText).setTextAlignment(Alignment.CenterLeft)
                                .addTooltip(lineTooltip)
                                .setPos(10, 1)));
            }
        }
        return processingDetails;
    }

    @Override
    protected void drawTexts(DynamicPositionedColumn screenElements, SlotWidget inventorySlot) {
        screenElements.widget(
            new FakeSyncWidget.BooleanSyncer(
                () -> this.mode == EIGModes.IC2,
                b -> this.mode = b ? EIGModes.IC2 : EIGModes.Normal));
        screenElements.widget(new FakeSyncWidget<>(() -> {
            HashMap<ItemStack, Double> ret = new HashMap<>();

            for (Map.Entry<ItemStack, Double> drop : this.guiDropTracker.entrySet()) {
                ret.merge(drop.getKey(), drop.getValue(), Double::sum);
            }

            return ret;
        }, h -> this.synchedGUIDropTracker = h, (buffer, h) -> {
            buffer.writeVarIntToBuffer(h.size());
            for (Map.Entry<ItemStack, Double> itemStackDoubleEntry : h.entrySet()) {
                try {
                    buffer.writeItemStackToBuffer(itemStackDoubleEntry.getKey());
                    buffer.writeDouble(itemStackDoubleEntry.getValue());
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }, buffer -> {
            int len = buffer.readVarIntFromBuffer();
            HashMap<ItemStack, Double> ret = new HashMap<>(len);
            for (int i = 0; i < len; i++) {
                try {
                    ret.put(buffer.readItemStackFromBuffer(), buffer.readDouble());
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
            return ret;
        }));
        super.drawTexts(screenElements, inventorySlot);
    }

    @Override
    public String[] getInfoData() {
        List<String> info = new ArrayList<>(
            Arrays
                .asList(
                    StatCollector.translateToLocal("kubatech.infodata.running_mode") + " "
                        + EnumChatFormatting.GREEN
                        + (this.setupPhase == 0 ? this.mode.getName()
                            : (this.setupPhase == 1
                                ? StatCollector.translateToLocal("kubatech.infodata.eig.running_mode.setup_mode.input")
                                : StatCollector
                                    .translateToLocal("kubatech.infodata.eig.running_mode.setup_mode.output")))
                        + EnumChatFormatting.RESET,
                    StatCollector.translateToLocalFormatted("kubatech.infodata.eig.uses.water", waterUsage),
                    StatCollector.translateToLocalFormatted("kubatech.infodata.eig.uses.weedex", weedEXUsage),
                    StatCollector.translateToLocal("kubatech.infodata.eig.max_slots") + EnumChatFormatting.GREEN
                        + this.maxSeedTypes
                        + EnumChatFormatting.RESET,
                    StatCollector.translateToLocal("kubatech.infodata.eig.used_slots")
                        + ((this.buckets.size() > maxSeedTypes) ? EnumChatFormatting.RED : EnumChatFormatting.GREEN)
                        + this.buckets.size()
                        + EnumChatFormatting.RESET));
        for (EIGBucket bucket : buckets) {
            info.add(bucket.getInfoData());
        }
        if (this.buckets.size() > this.maxSeedTypes) {
            info.add(
                EnumChatFormatting.DARK_RED + StatCollector.translateToLocal("kubatech.infodata.eig.too_many_types")
                    + EnumChatFormatting.RESET);
        }
        if (this.getTotalSeedCount() > this.maxSeedCount) {
            info.add(
                EnumChatFormatting.DARK_RED + StatCollector.translateToLocal("kubatech.infodata.eig.too_many_seeds")
                    + EnumChatFormatting.RESET);
        }
        info.addAll(Arrays.asList(super.getInfoData()));
        return info.toArray(new String[0]);
    }

    @Override
    public ITexture[] getTexture(IGregTechTileEntity aBaseMetaTileEntity, ForgeDirection side, ForgeDirection facing,
        int colorIndex, boolean aActive, boolean aRedstone) {
        final ITexture casingTexture = isOldStructure ? CASING_OLD.getCasingTexture() : CASING.getCasingTexture();
        if (side == facing) {
            if (aActive) return new ITexture[] { casingTexture, TextureFactory.builder()
                .addIcon(OVERLAY_FRONT_DISTILLATION_TOWER_ACTIVE)
                .extFacing()
                .build(),
                TextureFactory.builder()
                    .addIcon(OVERLAY_FRONT_DISTILLATION_TOWER_ACTIVE_GLOW)
                    .extFacing()
                    .glow()
                    .build() };
            return new ITexture[] { casingTexture, TextureFactory.builder()
                .addIcon(OVERLAY_FRONT_DISTILLATION_TOWER)
                .extFacing()
                .build(),
                TextureFactory.builder()
                    .addIcon(OVERLAY_FRONT_DISTILLATION_TOWER_GLOW)
                    .extFacing()
                    .glow()
                    .build() };
        }
        return new ITexture[] { casingTexture };
    }

    @Override
    public boolean supportsPowerPanel() {
        return false;
    }

    // endregion ui
}
