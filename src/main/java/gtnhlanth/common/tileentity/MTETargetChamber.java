package gtnhlanth.common.tileentity;

import static com.gtnewhorizon.structurelib.structure.StructureUtility.ofBlock;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.ofBlockAdder;
import static gregtech.api.enums.GTValues.VN;
import static gregtech.api.enums.HatchElement.Energy;
import static gregtech.api.enums.HatchElement.InputBus;
import static gregtech.api.enums.HatchElement.Maintenance;
import static gregtech.api.enums.HatchElement.OutputBus;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_OIL_CRACKER;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_OIL_CRACKER_ACTIVE;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_OIL_CRACKER_ACTIVE_GLOW;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_OIL_CRACKER_GLOW;
import static gregtech.api.enums.Textures.BlockIcons.casingTexturePages;
import static gregtech.api.util.GTStructureUtility.buildHatchAdder;
import static gtnhlanth.api.recipe.LanthanidesRecipeMaps.TARGET_CHAMBER_METADATA;
import static gtnhlanth.util.DescTextLocalization.addHintNumber;

import java.util.ArrayList;

import javax.annotation.Nullable;

import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.StatCollector;
import net.minecraftforge.common.util.ForgeDirection;

import org.jetbrains.annotations.NotNull;

import com.gtnewhorizon.structurelib.alignment.constructable.ISurvivalConstructable;
import com.gtnewhorizon.structurelib.structure.IStructureDefinition;
import com.gtnewhorizon.structurelib.structure.ISurvivalBuildEnvironment;
import com.gtnewhorizon.structurelib.structure.StructureDefinition;

import bartworks.common.loaders.ItemRegistry;
import gregtech.api.GregTechAPI;
import gregtech.api.enums.GTValues;
import gregtech.api.enums.TickTime;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.implementations.MTEEnhancedMultiBlockBase;
import gregtech.api.metatileentity.implementations.MTEHatchEnergy;
import gregtech.api.recipe.RecipeMap;
import gregtech.api.recipe.check.CheckRecipeResult;
import gregtech.api.recipe.check.CheckRecipeResultRegistry;
import gregtech.api.render.TextureFactory;
import gregtech.api.util.GTRecipe;
import gregtech.api.util.GTUtility;
import gregtech.api.util.MultiblockTooltipBuilder;
import gregtech.api.util.extensions.ArrayExt;
import gregtech.common.misc.GTStructureChannels;
import gtnhlanth.api.recipe.LanthanidesRecipeMaps;
import gtnhlanth.common.beamline.BeamInformation;
import gtnhlanth.common.beamline.Particle;
import gtnhlanth.common.hatch.MTEBusInputFocus;
import gtnhlanth.common.hatch.MTEHatchInputBeamline;
import gtnhlanth.common.register.LanthItemList;
import gtnhlanth.common.tileentity.recipe.beamline.TargetChamberMetadata;
import gtnhlanth.util.DescTextLocalization;

public class MTETargetChamber extends MTEEnhancedMultiBlockBase<MTETargetChamber> implements ISurvivalConstructable {

    private static final IStructureDefinition<MTETargetChamber> STRUCTURE_DEFINITION;

    private final ArrayList<MTEHatchInputBeamline> mInputBeamline = new ArrayList<>();

    private final ArrayList<MTEBusInputFocus> mInputFocus = new ArrayList<>();

    private static final int CASING_INDEX_FRONT = GTUtility.getCasingTextureIndex(GregTechAPI.sBlockCasings3, 10); // Grate
    private static final int CASING_INDEX_CENTRE = 1662; // Shielded Acc.
    private GTRecipe lastRecipe;

    // spotless:off
    static {
    	STRUCTURE_DEFINITION = StructureDefinition.<MTETargetChamber>builder()
    			.addShape(
    					"base",
    					new String[][] {
    						{"ggggg", "gjjjg", "gjbjg", "gjjjg", "ff~ff"},
    						{"cslsc", "s-r-s", "srhrs", "s-r-s", "ccccc"},
    						{"csssc", "s---s", "s---s", "s---s", "ccccc"},
    						{"csssc", "s---s", "s---s", "s---s", "ccccc"},
    						{"cstsc", "s-u-s", "suius", "s-u-s", "ccccc"},
    						{"ggggg", "gjjjg", "gjojg", "gjjjg", "ggggg"}})

    			.addElement('g', ofBlock(GregTechAPI.sBlockCasings3, 10)) //Grate casing
    			.addElement(
    					'f',
    					buildHatchAdder(MTETargetChamber.class).atLeast(Maintenance, Energy)
    					.casingIndex(CASING_INDEX_FRONT).hint(2).buildAndChain(ofBlock(GregTechAPI.sBlockCasings3, 10)))

    			.addElement('j', ofBlockAdder(MTETargetChamber::addGlass, ItemRegistry.bw_glasses[0], 1))
    			.addElement('b', buildHatchAdder(MTETargetChamber.class).hatchClass(MTEHatchInputBeamline.class).casingIndex(CASING_INDEX_CENTRE).hint(5).adder(MTETargetChamber::addBeamLineInputHatch).build())
    			.addElement('c', ofBlock(LanthItemList.SHIELDED_ACCELERATOR_CASING, 0))

    			.addElement('l', buildHatchAdder(MTETargetChamber.class).hatchClass(MTEBusInputFocus.class).casingIndex(CASING_INDEX_CENTRE).hint(1).adder(MTETargetChamber::addFocusInputHatch).build())

    			.addElement('t', buildHatchAdder(MTETargetChamber.class).atLeast(InputBus).casingIndex(CASING_INDEX_CENTRE).hint(3).build())
    			.addElement('s', ofBlock(LanthItemList.SHIELDED_ACCELERATOR_GLASS, 0))
    			.addElement('r', ofBlock(LanthItemList.FOCUS_MANIPULATION_CASING, 0))
    			.addElement('h', ofBlock(LanthItemList.FOCUS_HOLDER, 0))
    			.addElement('u', ofBlock(LanthItemList.TARGET_RECEPTACLE_CASING, 0))
    			.addElement('i', ofBlock(LanthItemList.TARGET_HOLDER, 0))
    			.addElement('o', buildHatchAdder(MTETargetChamber.class).atLeast(OutputBus).casingIndex(CASING_INDEX_CENTRE).hint(4).build())
    			.build();
    }
    //spotless:on

    private boolean addGlass(Block block, int meta) {
        return block == ItemRegistry.bw_glasses[0];
    }

    private boolean addBeamLineInputHatch(IGregTechTileEntity te, int casingIndex) {
        if (te == null) return false;

        IMetaTileEntity mte = te.getMetaTileEntity();
        if (mte == null) return false;

        if (mte instanceof MTEHatchInputBeamline) {
            return this.mInputBeamline.add((MTEHatchInputBeamline) mte);
        }

        return false;
    }

    private boolean addFocusInputHatch(IGregTechTileEntity te, int casingIndex) {
        if (te == null) return false;

        IMetaTileEntity mte = te.getMetaTileEntity();
        if (mte == null) return false;

        if (mte instanceof MTEBusInputFocus) {
            return this.mInputFocus.add((MTEBusInputFocus) mte);
        }

        return false;
    }

    public MTETargetChamber(int id, String name, String nameRegional) {
        super(id, name, nameRegional);
    }

    public MTETargetChamber(String name) {
        super(name);
    }

    @Override
    public boolean supportsPowerPanel() {
        return false;
    }

    @Override
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity te) {
        return new MTETargetChamber(this.mName);
    }

    @Override
    public ITexture[] getTexture(IGregTechTileEntity baseMetaTileEntity, ForgeDirection side, ForgeDirection facing,
        int colorIndex, boolean active, boolean redstoneLevel) {
        // Placeholder
        if (side == facing) {
            if (active) return new ITexture[] { casingTexturePages[0][47], TextureFactory.builder()
                .addIcon(OVERLAY_FRONT_OIL_CRACKER_ACTIVE)
                .extFacing()
                .build(),
                TextureFactory.builder()
                    .addIcon(OVERLAY_FRONT_OIL_CRACKER_ACTIVE_GLOW)
                    .extFacing()
                    .glow()
                    .build() };
            return new ITexture[] { casingTexturePages[0][47], TextureFactory.builder()
                .addIcon(OVERLAY_FRONT_OIL_CRACKER)
                .extFacing()
                .build(),
                TextureFactory.builder()
                    .addIcon(OVERLAY_FRONT_OIL_CRACKER_GLOW)
                    .extFacing()
                    .glow()
                    .build() };
        }
        return new ITexture[] { casingTexturePages[0][47] };
    }

    @Override
    protected MultiblockTooltipBuilder createTooltip() {
        final MultiblockTooltipBuilder tt = new MultiblockTooltipBuilder();
        // spotless:off
        tt.addMachineType("Collision Chamber")
            .addInfo("Engraves Wafers with " + EnumChatFormatting.LIGHT_PURPLE + "Particle Beams" + EnumChatFormatting.GRAY + " and " + createMaskText("Masks"))
            .addInfo(DescTextLocalization.BEAMLINE_SCANNER_INFO)
            .addSeparator()
            .addInfo(createMaskText("Masks") + " have limited durability, degrading by 1 with each engraving")
            .addInfo("NEI shows the required " + EnumChatFormatting.LIGHT_PURPLE + "particle type" + EnumChatFormatting.GRAY + ", minimum " + EnumChatFormatting.AQUA + "EU/t")
            .addInfo("minimum " + createFocusText("Beam Focus") + ", minimum " + EnumChatFormatting.GOLD + "Beam Rate" + EnumChatFormatting.GRAY + "(Amount), and required " + EnumChatFormatting.BLUE + "Beam Energy Range")
            .addSeparator()
            .addInfo("The incoming " + EnumChatFormatting.LIGHT_PURPLE + "Beam" + EnumChatFormatting.GRAY + " must be within the required " + EnumChatFormatting.BLUE + "Energy Range" + EnumChatFormatting.GRAY + ", be the correct " + EnumChatFormatting.LIGHT_PURPLE + "particle type")
            .addInfo("and have sufficient " + createFocusText("Focus") + " and " + EnumChatFormatting.GOLD+"Rate")
            .addInfo(EnumChatFormatting.WHITE + "Processing Time" + EnumChatFormatting.GRAY + " is determined solely by the " + createRateText("Beam Rate"))
            .addInfo(EnumChatFormatting.WHITE + "Processing Time = 5 seconds * " + EnumChatFormatting.GOLD + "Minimum Required Rate" + EnumChatFormatting.WHITE + " / " + createRateText("Input Beam Rate"))
            .addInfo("Can process up to the durability of all " + createMaskText("Mask's") + " in the Focus Input Bus of one type per game tick")
            .beginStructureBlock(5, 5, 6, true)
            .addController("Front bottom")
            .addCasingInfoExactly("Grate Machine Casing", 29, false)
            .addCasingInfoExactly("Shielded Accelerator Casing", 28, false)
            .addCasingInfoExactly("Any Tiered Glass", 16, false)
            .addCasingInfoExactly(LanthItemList.SHIELDED_ACCELERATOR_GLASS.getLocalizedName(), 34, false)
            .addCasingInfoExactly(LanthItemList.TARGET_RECEPTACLE_CASING.getLocalizedName(), 4, false)
            .addCasingInfoExactly(LanthItemList.FOCUS_MANIPULATION_CASING.getLocalizedName(), 4, false)
            .addCasingInfoExactly(LanthItemList.FOCUS_HOLDER.getLocalizedName(), 1, false)
            .addCasingInfoExactly(LanthItemList.TARGET_HOLDER.getLocalizedName(), 1, false)
            .addOtherStructurePart("Focus Input Bus", addHintNumber(1))
            .addMaintenanceHatch(addHintNumber(2))
            .addEnergyHatch(addHintNumber(2))
            .addInputBus(addHintNumber(3))
            .addOutputBus(addHintNumber(4))
            .addOtherStructurePart("Beamline Input Hatch", addHintNumber(5))
            .addSubChannelUsage(GTStructureChannels.BOROGLASS)
            .toolTipFinisher();
        return tt;
        //spotless:on
    }

    @Override
    public void construct(ItemStack stackSize, boolean hintsOnly) {
        buildPiece("base", stackSize, hintsOnly, 2, 4, 0);
    }

    @Override
    public int survivalConstruct(ItemStack stackSize, int elementBudget, ISurvivalBuildEnvironment env) {
        if (this.mMachine) return -1;
        return survivalBuildPiece("base", stackSize, 2, 4, 0, elementBudget, env, false, true);
    }

    @Override
    public IStructureDefinition<MTETargetChamber> getStructureDefinition() {
        return STRUCTURE_DEFINITION;
    }

    @Override
    public RecipeMap<?> getRecipeMap() {
        return LanthanidesRecipeMaps.targetChamberRecipes;
    }

    @NotNull
    @Override
    public CheckRecipeResult checkProcessing() {
        ArrayList<ItemStack> tItems = this.getStoredInputs();
        ArrayList<ItemStack> tFocusItemArray = this.getFocusItemStack();

        ItemStack tFocusItemZeroDamage;
        ArrayList<ItemStack> tItemsWithFocusItem = new ArrayList<>();
        if (tFocusItemArray != null) {
            tFocusItemZeroDamage = tFocusItemArray.get(0)
                .copy();
            tFocusItemZeroDamage.setItemDamage(0);
            tItemsWithFocusItem.add(tFocusItemZeroDamage);
        }
        tItemsWithFocusItem.addAll(tItems);
        ItemStack[] tItemsWithFocusItemArray = tItemsWithFocusItem.toArray(new ItemStack[0]);

        long tVoltageActual = GTValues.VP[(int) this.getInputVoltageTier()];

        GTRecipe tRecipe = LanthanidesRecipeMaps.targetChamberRecipes.findRecipeQuery()
            .items(tItemsWithFocusItemArray)
            .voltage(tVoltageActual)
            .filter((GTRecipe recipe) -> {
                TargetChamberMetadata metadata = recipe.getMetadata(TARGET_CHAMBER_METADATA);
                if (metadata == null) return false;

                BeamInformation recipeInInfo = this.getInputInformation();
                int particle = metadata.particleID;

                if (recipeInInfo != null) {
                    return (particle == recipeInInfo.getParticleId() && !(recipeInInfo.getEnergy() < metadata.minEnergy
                        || recipeInInfo.getEnergy() > metadata.maxEnergy));
                }

                return false;
            })
            .cachedRecipe(this.lastRecipe)
            .find();
        if (tRecipe == null) return CheckRecipeResultRegistry.NO_RECIPE;

        TargetChamberMetadata metadata = tRecipe.getMetadata(TARGET_CHAMBER_METADATA);
        if (metadata == null) return CheckRecipeResultRegistry.NO_RECIPE;

        BeamInformation inputInfo = this.getInputInformation();
        if (inputInfo == null) return CheckRecipeResultRegistry.NO_RECIPE;
        float inputEnergy = inputInfo.getEnergy();
        float inputRate = inputInfo.getRate();
        int inputParticle = inputInfo.getParticleId();
        float inputFocus = inputInfo.getFocus();

        if (inputEnergy < metadata.minEnergy || inputEnergy > metadata.maxEnergy)
            return CheckRecipeResultRegistry.NO_RECIPE;
        if (inputFocus < metadata.minFocus) return CheckRecipeResultRegistry.NO_RECIPE;
        if (inputParticle != metadata.particleID) return CheckRecipeResultRegistry.NO_RECIPE;
        if (metadata.focusItem != null) {
            if (tFocusItemArray != null) {
                if (tFocusItemArray.get(0) != null && metadata.focusItem.getItem() != tFocusItemArray.get(0)
                    .getItem()) return CheckRecipeResultRegistry.NO_RECIPE;
            }
        }

        // 5 seconds per integer multiple over the rate
        float progressTime = metadata.amount / inputRate * 5 * TickTime.SECOND;
        int batchAmount = 1;
        if (progressTime < 1) { // Subticking
            batchAmount = (int) Math.round(1.0 / progressTime);

            if (metadata.focusItem != null) {
                int maskLimit = 0;

                if (tFocusItemArray != null) {
                    for (ItemStack focus : tFocusItemArray) {
                        maskLimit += focus.getMaxDamage() - focus.getItemDamage() + 1;
                    }
                }

                // Limited by mask durability first
                if (batchAmount > maskLimit) batchAmount = maskLimit;
                progressTime = 1;

                // If enough mask durability is present, attempt to batch up to 128 instances
                // while remaining within the mask limit. 128 chosen from normal batch mode
                if (batchAmount < maskLimit) {
                    int ratio = Math.min(maskLimit / batchAmount, 128);
                    batchAmount *= ratio;
                    progressTime = ratio;
                }
            }

        }

        this.mMaxProgresstime = (int) progressTime;

        if (this.mMaxProgresstime == Integer.MAX_VALUE - 1 && this.mEUt == Integer.MAX_VALUE - 1)
            return CheckRecipeResultRegistry.NO_RECIPE;

        double maxParallel = tRecipe
            .maxParallelCalculatedByInputs(batchAmount, GTValues.emptyFluidStackArray, tItemsWithFocusItemArray);
        if (batchAmount > maxParallel) batchAmount = (int) maxParallel;

        if (!tRecipe.equals(this.lastRecipe)) this.lastRecipe = tRecipe;

        tRecipe.consumeInput(batchAmount, GTValues.emptyFluidStackArray, tItemsWithFocusItemArray);
        ItemStack[] itemOutputArray = ArrayExt.copyItemsIfNonEmpty(tRecipe.mOutputs);
        for (ItemStack stack : itemOutputArray) {
            stack.stackSize *= batchAmount;
        }
        this.mOutputItems = itemOutputArray;

        this.mEfficiency = (10000 - (this.getIdealStatus() - this.getRepairStatus()) * 1000);
        this.mEfficiencyIncrease = 10000;

        mEUt = (int) -tVoltageActual;
        if (this.mEUt > 0) this.mEUt = (-this.mEUt);

        int focusDurabilityDepletion = batchAmount;
        if (tFocusItemArray != null) {
            for (ItemStack stack : tFocusItemArray) {
                if (focusDurabilityDepletion + stack.getItemDamage() >= stack.getMaxDamage() + 1) {
                    focusDurabilityDepletion -= stack.getMaxDamage() + 1 - stack.getItemDamage();
                    stack.stackSize--;
                } else {
                    stack.setItemDamage(stack.getItemDamage() + focusDurabilityDepletion);
                    break;
                }
            }
        }

        this.updateSlots();
        return CheckRecipeResultRegistry.SUCCESSFUL;
    }

    @Nullable
    private BeamInformation getInputInformation() {
        for (MTEHatchInputBeamline in : this.mInputBeamline) {
            if (in.dataPacket == null) return new BeamInformation(0, 0, 0, 0);
            return in.dataPacket.getContent();
        }
        return null;
    }

    @Nullable
    private ArrayList<ItemStack> getFocusItemStack() {
        if (this.mInputFocus.isEmpty()) return null;
        if (this.mInputFocus.get(0)
            .getContentUsageSlots()
            .isEmpty()) return null;
        return this.mInputFocus.get(0)
            .getContentUsageSlots();
    }

    @Override
    public boolean checkMachine(IGregTechTileEntity arg0, ItemStack arg1) {
        mInputBeamline.clear();
        mInputFocus.clear();
        this.lastRecipe = null;

        if (!checkPiece("base", 2, 4, 0)) return false;

        return this.mInputBeamline.size() == 1 && this.mMaintenanceHatches.size() == 1
            && this.mInputBusses.size() == 1
            && this.mOutputBusses.size() == 1
            && this.mInputFocus.size() == 1;
    }

    @Override
    public String[] getStructureDescription(ItemStack arg0) {
        return DescTextLocalization.addText("TargetChamber.hint", 13);
    }

    @Override
    public String[] getInfoData() {
        long storedEnergy = 0;
        long maxEnergy = 0;

        for (MTEHatchEnergy tHatch : mEnergyHatches) {
            if (tHatch.isValid()) {
                storedEnergy += tHatch.getBaseMetaTileEntity()
                    .getStoredEU();
                maxEnergy += tHatch.getBaseMetaTileEntity()
                    .getEUCapacity();
            }
        }

        BeamInformation information = this.getInputInformation();
        if (information == null) {
            information = new BeamInformation(0, 0, 0, 0);
        }

        return new String[] {
            // from super()
            /* 1 */ StatCollector.translateToLocal("GT5U.multiblock.Progress") + ": "
                + EnumChatFormatting.GREEN
                + formatNumber(mProgresstime / 20)
                + EnumChatFormatting.RESET
                + " s / "
                + EnumChatFormatting.YELLOW
                + formatNumber(mMaxProgresstime / 20)
                + EnumChatFormatting.RESET
                + " s",
            /* 2 */ StatCollector.translateToLocal("GT5U.multiblock.energy") + ": "
                + EnumChatFormatting.GREEN
                + formatNumber(storedEnergy)
                + EnumChatFormatting.RESET
                + " EU / "
                + EnumChatFormatting.YELLOW
                + formatNumber(maxEnergy)
                + EnumChatFormatting.RESET
                + " EU",
            /* 3 */ StatCollector.translateToLocal("GT5U.multiblock.usage") + ": "
                + EnumChatFormatting.RED
                + formatNumber(getActualEnergyUsage())
                + EnumChatFormatting.RESET
                + " EU/t",
            /* 4 */ StatCollector.translateToLocal("GT5U.multiblock.mei") + ": "
                + EnumChatFormatting.YELLOW
                + formatNumber(getMaxInputVoltage())
                + EnumChatFormatting.RESET
                + " EU/t(*2A) "
                + StatCollector.translateToLocal("GT5U.machines.tier")
                + ": "
                + EnumChatFormatting.YELLOW
                + VN[GTUtility.getTier(getMaxInputVoltage())]
                + EnumChatFormatting.RESET,
            /* 5 */ StatCollector.translateToLocal("GT5U.multiblock.problems") + ": "
                + EnumChatFormatting.RED
                + (getIdealStatus() - getRepairStatus())
                + EnumChatFormatting.RESET
                + " "
                + StatCollector.translateToLocal("GT5U.multiblock.efficiency")
                + ": "
                + EnumChatFormatting.YELLOW
                + mEfficiency / 100.0F
                + EnumChatFormatting.RESET
                + " %",
            /* 6 Pollution not included */
            // Beamline-specific
            EnumChatFormatting.BOLD + StatCollector.translateToLocal("beamline.in_pre")
                + ": "
                + EnumChatFormatting.RESET,
            StatCollector.translateToLocal("beamline.particle") + ": " // "Multiblock Beamline Input:"
                + EnumChatFormatting.GOLD
                + Particle.getParticleFromId(information.getParticleId())
                    .getLocalisedName() // e.g. "Electron
                                        // (e-)"
                + " "
                + EnumChatFormatting.RESET,
            StatCollector.translateToLocal("beamline.energy") + ": " // "Energy:"
                + EnumChatFormatting.DARK_RED
                + information.getEnergy() * 1000 // In line with the synchrotron's output
                + EnumChatFormatting.RESET
                + " eV", // e.g. "10240 eV"
            StatCollector.translateToLocal("beamline.focus") + ": " // "Focus:"
                + EnumChatFormatting.BLUE
                + information.getFocus()
                + " "
                + EnumChatFormatting.RESET,
            StatCollector.translateToLocal("beamline.amount") + ": " // "Amount:"
                + EnumChatFormatting.LIGHT_PURPLE
                + information.getRate() };
    }

    private String createMaskText(String text) {
        return String.format("%s%s%s", EnumChatFormatting.GREEN, text, EnumChatFormatting.GRAY);
    }

    private String createRateText(String text) {
        return String.format("%s%s%s", EnumChatFormatting.YELLOW, text, EnumChatFormatting.GRAY);
    }

    private String createFocusText(String text) {
        return String.format("%s%s%s", EnumChatFormatting.RED, text, EnumChatFormatting.GRAY);
    }

}
