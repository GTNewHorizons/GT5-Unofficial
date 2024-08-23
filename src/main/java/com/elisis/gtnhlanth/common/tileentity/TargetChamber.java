package com.elisis.gtnhlanth.common.tileentity;

import static com.elisis.gtnhlanth.util.DescTextLocalization.BLUEPRINT_INFO;
import static com.elisis.gtnhlanth.util.DescTextLocalization.addDotText;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.ofBlock;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.ofBlockAdder;
import static gregtech.api.enums.GT_HatchElement.Energy;
import static gregtech.api.enums.GT_HatchElement.InputBus;
import static gregtech.api.enums.GT_HatchElement.Maintenance;
import static gregtech.api.enums.GT_HatchElement.OutputBus;
import static gregtech.api.enums.GT_Values.VN;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_OIL_CRACKER;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_OIL_CRACKER_ACTIVE;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_OIL_CRACKER_ACTIVE_GLOW;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_OIL_CRACKER_GLOW;
import static gregtech.api.enums.Textures.BlockIcons.casingTexturePages;
import static gregtech.api.util.GT_StructureUtility.buildHatchAdder;

import java.util.ArrayList;

import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.StatCollector;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.FluidStack;

import com.elisis.gtnhlanth.common.beamline.BeamInformation;
import com.elisis.gtnhlanth.common.beamline.Particle;
import com.elisis.gtnhlanth.common.hatch.TileBusInputFocus;
import com.elisis.gtnhlanth.common.hatch.TileHatchInputBeamline;
import com.elisis.gtnhlanth.common.register.LanthItemList;
import com.elisis.gtnhlanth.common.tileentity.recipe.beamline.BeamlineRecipeAdder2;
import com.elisis.gtnhlanth.common.tileentity.recipe.beamline.RecipeTC;
import com.elisis.gtnhlanth.util.DescTextLocalization;
import com.github.bartimaeusnek.bartworks.common.loaders.ItemRegistry;
import com.gtnewhorizon.structurelib.alignment.constructable.ISurvivalConstructable;
import com.gtnewhorizon.structurelib.structure.IStructureDefinition;
import com.gtnewhorizon.structurelib.structure.ISurvivalBuildEnvironment;
import com.gtnewhorizon.structurelib.structure.StructureDefinition;

import gregtech.api.GregTech_API;
import gregtech.api.enums.TickTime;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_EnhancedMultiBlockBase;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_Hatch_Energy;
import gregtech.api.recipe.RecipeMap;
import gregtech.api.render.TextureFactory;
import gregtech.api.util.GT_Multiblock_Tooltip_Builder;
import gregtech.api.util.GT_Recipe;
import gregtech.api.util.GT_Utility;

public class TargetChamber extends GT_MetaTileEntity_EnhancedMultiBlockBase<TargetChamber>
    implements ISurvivalConstructable {

    private static final IStructureDefinition<TargetChamber> STRUCTURE_DEFINITION;

    private ArrayList<TileHatchInputBeamline> mInputBeamline = new ArrayList<>();

    private ArrayList<TileBusInputFocus> mInputFocus = new ArrayList<>();

    private static final int CASING_INDEX_FRONT = GT_Utility.getCasingTextureIndex(GregTech_API.sBlockCasings3, 10); // Grate
    private static final int CASING_INDEX_CENTRE = GT_Utility.getCasingTextureIndex(GregTech_API.sBlockCasings5, 14); // Shielded
                                                                                                                      // Acc.

    private float inputEnergy;
    private float inputRate;
    private int inputParticle;
    private float inputFocus;

    // spotless:off
    static {
    	STRUCTURE_DEFINITION = StructureDefinition.<TargetChamber>builder()
    			.addShape(
    					"base",
    					new String[][] {
    						{"ggggg", "gjjjg", "gjbjg", "gjjjg", "ff~ff"},
    						{"cslsc", "s-r-s", "srhrs", "s-r-s", "ccccc"},
    						{"csssc", "s---s", "s---s", "s---s", "ccccc"},
    						{"csssc", "s---s", "s---s", "s---s", "ccccc"},
    						{"cstsc", "s-u-s", "suius", "s-u-s", "ccccc"},
    						{"ggggg", "gjjjg", "gjojg", "gjjjg", "ggggg"}})
    			
    			.addElement('g', ofBlock(GregTech_API.sBlockCasings3, 10)) //Grate casing
    			.addElement(
    					'f', 
    					buildHatchAdder(TargetChamber.class).atLeast(Maintenance, Energy)
    					.casingIndex(CASING_INDEX_FRONT).dot(2).buildAndChain(ofBlock(GregTech_API.sBlockCasings3, 10)))
    			
    			.addElement('j', ofBlockAdder(TargetChamber::addGlass, ItemRegistry.bw_glasses[0], 1))
    			.addElement('b', buildHatchAdder(TargetChamber.class).hatchClass(TileHatchInputBeamline.class).casingIndex(CASING_INDEX_CENTRE).dot(5).adder(TargetChamber::addBeamLineInputHatch).build())
    			.addElement('c', ofBlock(LanthItemList.SHIELDED_ACCELERATOR_CASING, 0))
    			
    			.addElement('l', buildHatchAdder(TargetChamber.class).hatchClass(TileBusInputFocus.class).casingIndex(CASING_INDEX_CENTRE).dot(1).adder(TargetChamber::addFocusInputHatch).build())
    			
    			.addElement('t', buildHatchAdder(TargetChamber.class).atLeast(InputBus).casingIndex(CASING_INDEX_CENTRE).dot(3).build())
    			.addElement('s', ofBlock(LanthItemList.SHIELDED_ACCELERATOR_GLASS, 0))
    			.addElement('r', ofBlock(LanthItemList.FOCUS_MANIPULATION_CASING, 0))
    			.addElement('h', ofBlock(LanthItemList.FOCUS_HOLDER, 0))
    			.addElement('u', ofBlock(LanthItemList.TARGET_RECEPTACLE_CASING, 0))
    			.addElement('i', ofBlock(LanthItemList.TARGET_HOLDER, 0))
    			.addElement('o', buildHatchAdder(TargetChamber.class).atLeast(OutputBus).casingIndex(CASING_INDEX_CENTRE).dot(4).build())
    			
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

        if (mte instanceof TileHatchInputBeamline) {
            return this.mInputBeamline.add((TileHatchInputBeamline) mte);
        }

        return false;
    }

    private boolean addFocusInputHatch(IGregTechTileEntity te, int casingIndex) {

        if (te == null) return false;

        IMetaTileEntity mte = te.getMetaTileEntity();

        if (mte == null) return false;

        if (mte instanceof TileBusInputFocus) {
            return this.mInputFocus.add((TileBusInputFocus) mte);
        }

        return false;
    }

    public TargetChamber(int id, String name, String nameRegional) {
        super(id, name, nameRegional);
    }

    public TargetChamber(String name) {
        super(name);
    }

    @Override
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity te) {
        return new TargetChamber(this.mName);
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
    protected GT_Multiblock_Tooltip_Builder createTooltip() {
        final GT_Multiblock_Tooltip_Builder tt = new GT_Multiblock_Tooltip_Builder();
        tt.addMachineType("Collision Chamber")
            .addInfo("Controller block for the Target Chamber")
            .addInfo("Hitting things with other things")

            .addInfo(BLUEPRINT_INFO)
            .addInfo(DescTextLocalization.BEAMLINE_SCANNER_INFO)
            .addSeparator()
            .beginStructureBlock(5, 5, 6, true)
            .addController("Front bottom")
            .addCasingInfoExactly("Grate Machine Casing", 29, false)
            .addCasingInfoExactly("Shielded Accelerator Casing", 28, false)
            .addCasingInfoExactly("Borosilicate Glass", 16, true)
            .addCasingInfoExactly(LanthItemList.SHIELDED_ACCELERATOR_GLASS.getLocalizedName(), 34, false)
            .addCasingInfoExactly(LanthItemList.TARGET_RECEPTACLE_CASING.getLocalizedName(), 4, false)
            .addCasingInfoExactly(LanthItemList.FOCUS_MANIPULATION_CASING.getLocalizedName(), 4, false)
            .addCasingInfoExactly(LanthItemList.FOCUS_HOLDER.getLocalizedName(), 1, false)
            .addCasingInfoExactly(LanthItemList.TARGET_HOLDER.getLocalizedName(), 1, false)
            .addOtherStructurePart("Focus Input Bus", addDotText(1))
            .addMaintenanceHatch(addDotText(2))
            .addEnergyHatch(addDotText(2))
            .addInputBus(addDotText(3))
            .addOutputBus(addDotText(4))
            .addOtherStructurePart("Beamline Input Hatch", addDotText(5))
            .toolTipFinisher("GTNH: Lanthanides");
        return tt;
    }

    @Override
    public void construct(ItemStack stackSize, boolean hintsOnly) {
        buildPiece("base", stackSize, hintsOnly, 2, 4, 0);

    }

    @Override
    public int survivalConstruct(ItemStack stackSize, int elementBudget, ISurvivalBuildEnvironment env) {
        if (mMachine) return -1;
        return survivialBuildPiece("base", stackSize, 2, 4, 0, elementBudget, env, false, true);
    }

    @Override
    public IStructureDefinition<TargetChamber> getStructureDefinition() {
        return STRUCTURE_DEFINITION;
    }

    @Override
    public RecipeMap<?> getRecipeMap() {
        return BeamlineRecipeAdder2.instance.TargetChamberRecipes;
    }

    @Override
    public boolean checkRecipe(ItemStack itemStack) {

        inputEnergy = 0;
        inputRate = 0;
        inputParticle = 0;
        inputFocus = 0;

        ArrayList<ItemStack> tItems = this.getStoredInputs();
        ItemStack tFocusItem = this.getFocusItemStack();

        ItemStack tFocusItemZeroDamage = null;

        if (tFocusItem != null) {

            tFocusItemZeroDamage = tFocusItem.copy();
            tFocusItemZeroDamage.setItemDamage(0);
        }

        ArrayList<ItemStack> tItemsWithFocusItem = new ArrayList<>();
        tItemsWithFocusItem.add(tFocusItemZeroDamage);
        tItemsWithFocusItem.addAll(tItems);

        long tVoltage = this.getMaxInputVoltage();

        ItemStack[] tItemsArray = tItems.toArray(new ItemStack[0]);

        ItemStack[] tItemsWithFocusItemArray = tItemsWithFocusItem.toArray(new ItemStack[0]);

        RecipeTC tRecipe = (RecipeTC) BeamlineRecipeAdder2.instance.TargetChamberRecipes.findRecipeQuery()
            .items(tItemsWithFocusItemArray)
            .voltage(tVoltage)
            .filter((GT_Recipe recipe) -> {

                RecipeTC recipeTc = (RecipeTC) recipe;

                BeamInformation inputInfo = this.getInputInformation();

                int particle = recipeTc.particleId;

                return (particle == inputInfo.getParticleId()
                    && !(inputInfo.getEnergy() < recipeTc.minEnergy || inputInfo.getEnergy() > recipeTc.maxEnergy));

            })
            .find();

        if (tRecipe == null || !tRecipe.isRecipeInputEqual(true, new FluidStack[] {}, tItemsWithFocusItemArray))
            return false;

        if (tRecipe.focusItem != null) {
            if (tRecipe.focusItem.getItem() != tFocusItem.getItem()) return false;
        }

        this.mEfficiency = (10000 - (this.getIdealStatus() - this.getRepairStatus()) * 1000);
        this.mEfficiencyIncrease = 10000;

        BeamInformation inputInfo = this.getInputInformation();

        if (inputInfo == null) return false;

        inputEnergy = inputInfo.getEnergy();
        inputRate = inputInfo.getRate();
        inputParticle = inputInfo.getParticleId();
        inputFocus = inputInfo.getFocus();

        if (inputEnergy < tRecipe.minEnergy || inputEnergy > tRecipe.maxEnergy) return false;

        if (inputFocus < tRecipe.minFocus) return false;

        if (inputParticle != tRecipe.particleId) return false;

        this.mMaxProgresstime = Math.max(Math.round((tRecipe.amount / inputRate * 5 * TickTime.SECOND)), 1); // 5
                                                                                                             // seconds
                                                                                                             // per
        // integer multiple
        // over the rate. E.g., 100a, 10r
        // would equal 50 seconds
        if (this.mMaxProgresstime == Integer.MAX_VALUE - 1 && this.mEUt == Integer.MAX_VALUE - 1) return false;

        mEUt = (int) -tVoltage;
        if (this.mEUt > 0) this.mEUt = (-this.mEUt);

        this.mOutputItems = tRecipe.mOutputs;

        if (tRecipe.focusItem != null) // Recipe actually uses the mask, can also assume machine mask item is nonnull
                                       // due to above conditions
            mInputFocus.get(0)
                .depleteFocusDurability(1);

        this.updateSlots();

        return true;
    }

    private BeamInformation getInputInformation() {

        for (TileHatchInputBeamline in : this.mInputBeamline) {

            if (in.q == null) return new BeamInformation(0, 0, 0, 0);
            // if (in.q == null) return new BeamInformation(10, 10, Particle.PHOTON.ordinal(), 90); // temporary
            // for
            // testing purposes

            return in.q.getContent();
        }
        return null;
    }

    private ItemStack getFocusItemStack() {

        for (TileBusInputFocus hatch : this.mInputFocus) {
            return hatch.getContentUsageSlots()
                .get(0);
        }

        return null;

    }

    @Override
    public boolean checkMachine(IGregTechTileEntity arg0, ItemStack arg1) {

        mInputBeamline.clear();
        mInputFocus.clear();

        if (!checkPiece("base", 2, 4, 0)) return false;

        return this.mInputBeamline.size() == 1 && this.mMaintenanceHatches.size() == 1
            && this.mInputBusses.size() == 1
            && this.mOutputBusses.size() == 1
            && this.mInputFocus.size() == 1;
    }

    @Override
    public boolean explodesOnComponentBreak(ItemStack arg0) {
        return false;
    }

    @Override
    public int getDamageToComponent(ItemStack arg0) {
        return 0;
    }

    @Override
    public int getMaxEfficiency(ItemStack arg0) {
        return 10000;
    }

    @Override
    public boolean isCorrectMachinePart(ItemStack arg0) {
        return true;
    }

    @Override
    public String[] getStructureDescription(ItemStack arg0) {
        return DescTextLocalization.addText("TargetChamber.hint", 13);
    }

    @Override
    public String[] getInfoData() {

        long storedEnergy = 0;
        long maxEnergy = 0;
        for (GT_MetaTileEntity_Hatch_Energy tHatch : mEnergyHatches) {
            if (tHatch.isValid()) {
                storedEnergy += tHatch.getBaseMetaTileEntity()
                    .getStoredEU();
                maxEnergy += tHatch.getBaseMetaTileEntity()
                    .getEUCapacity();
            }
        }

        BeamInformation information = this.getInputInformation();

        return new String[] {
            /* 1 */ StatCollector.translateToLocal("GT5U.multiblock.Progress") + ": "
                + EnumChatFormatting.GREEN
                + GT_Utility.formatNumbers(mProgresstime / 20)
                + EnumChatFormatting.RESET
                + " s / "
                + EnumChatFormatting.YELLOW
                + GT_Utility.formatNumbers(mMaxProgresstime / 20)
                + EnumChatFormatting.RESET
                + " s",
            /* 2 */ StatCollector.translateToLocal("GT5U.multiblock.energy") + ": "
                + EnumChatFormatting.GREEN
                + GT_Utility.formatNumbers(storedEnergy)
                + EnumChatFormatting.RESET
                + " EU / "
                + EnumChatFormatting.YELLOW
                + GT_Utility.formatNumbers(maxEnergy)
                + EnumChatFormatting.RESET
                + " EU",
            /* 3 */ StatCollector.translateToLocal("GT5U.multiblock.usage") + ": "
                + EnumChatFormatting.RED
                + GT_Utility.formatNumbers(getActualEnergyUsage())
                + EnumChatFormatting.RESET
                + " EU/t",
            /* 4 */ StatCollector.translateToLocal("GT5U.multiblock.mei") + ": "
                + EnumChatFormatting.YELLOW
                + GT_Utility.formatNumbers(getMaxInputVoltage())
                + EnumChatFormatting.RESET
                + " EU/t(*2A) "
                + StatCollector.translateToLocal("GT5U.machines.tier")
                + ": "
                + EnumChatFormatting.YELLOW
                + VN[GT_Utility.getTier(getMaxInputVoltage())]
                + EnumChatFormatting.RESET,
            /* 5 */ StatCollector.translateToLocal("GT5U.multiblock.problems") + ": "
                + EnumChatFormatting.RED
                + (getIdealStatus() - getRepairStatus())
                + EnumChatFormatting.RESET
                + " "
                + StatCollector.translateToLocal("GT5U.multiblock.efficiency")
                + ": "
                + EnumChatFormatting.YELLOW
                + Float.toString(mEfficiency / 100.0F)
                + EnumChatFormatting.RESET
                + " %",

            /* 6 */ EnumChatFormatting.BOLD + StatCollector.translateToLocal("beamline.in_pre")
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
                + information.getRate(), };
    }

}
