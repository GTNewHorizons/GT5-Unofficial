package gregtech.common.tileentities.machines.multi;

import static com.gtnewhorizon.structurelib.structure.StructureUtility.*;
import static gregtech.api.enums.GTValues.AuthorChrom;
import static gregtech.api.enums.HatchElement.*;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_MULTI_BREWERY;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_MULTI_BREWERY_ACTIVE;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_MULTI_BREWERY_ACTIVE_GLOW;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_MULTI_BREWERY_GLOW;
import static gregtech.api.util.GTStructureUtility.buildHatchAdder;
import static gregtech.api.util.GTStructureUtility.chainAllGlasses;
import static gregtech.api.util.GTStructureUtility.ofFrame;

import com.cleanroommc.modularui.utils.item.ItemStackHandler;
import com.cleanroommc.modularui.utils.item.LimitingItemStackHandler;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.FluidStack;

import org.jetbrains.annotations.NotNull;

import com.gtnewhorizon.structurelib.alignment.constructable.ISurvivalConstructable;
import com.gtnewhorizon.structurelib.structure.IStructureDefinition;
import com.gtnewhorizon.structurelib.structure.ISurvivalBuildEnvironment;
import com.gtnewhorizon.structurelib.structure.StructureDefinition;

import gregtech.api.GregTechAPI;
import gregtech.api.enums.Materials;
import gregtech.api.enums.MaterialsUEVplus;
import gregtech.api.enums.Textures;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.items.MetaGeneratedTool;
import gregtech.api.logic.ProcessingLogic;
import gregtech.api.metatileentity.implementations.MTEExtendedPowerMultiBlockBase;
import gregtech.api.metatileentity.implementations.MTEHatchInput;
import gregtech.api.recipe.RecipeMap;
import gregtech.api.recipe.RecipeMaps;
import gregtech.api.recipe.check.CheckRecipeResult;
import gregtech.api.recipe.check.CheckRecipeResultRegistry;
import gregtech.api.render.TextureFactory;
import gregtech.api.util.*;
import gregtech.api.util.shutdown.ShutDownReasonRegistry;
import gregtech.common.blocks.BlockCasings1;
import gregtech.common.misc.GTStructureChannels;
import gregtech.common.tileentities.machines.multi.gui.MTEChamberCentrifugeGui;

public class MTEChamberCentrifuge extends MTEExtendedPowerMultiBlockBase<MTEChamberCentrifuge>
    implements ISurvivalConstructable {

    public boolean tier2Fluid = false;
    private int horizontalOffset = 4; // base offset for tier 1
    private int verticalOffset = 6; // base offset for tier 2
    private final int amountToDrain = 10; // constant drain amount.
    private int mTier;
    public final LimitingItemStackHandler inventoryHandler = new LimitingItemStackHandler(8,1);
    private int[] modules = { 1, 2, 3, 4, 5 };
    private static final String STRUCTURE_TIER_1 = "t1";
    private static final String STRUCTURE_TIER_2 = "t2";
    private static final String STRUCTURE_TIER_3 = "t3";
    private static final String STRUCTURE_TIER_4 = "t4";
    private static MetaGeneratedTool toolchecker;

    private static final IStructureDefinition<MTEChamberCentrifuge> STRUCTURE_DEFINITION = StructureDefinition
        .<MTEChamberCentrifuge>builder()
        .addShape(
            STRUCTURE_TIER_1,
            // spotless:off
            transpose(new String[][]{
                {" CCCCCCC ","CCCCCCCCC","CCCCCCCCC","CCCEEECCC","CCCEEECCC","CCCEEECCC","CCCCCCCCC","CCCCCCCCC"," CCCCCCC "},
                {"  AAAAA  "," A     A ","A       A","A       A","A   BDD A","A       A","A       A"," A     A ","  AAAAA  "},
                {"         ","  AAAAA  "," A     A "," A     A "," ADDB  A "," A     A "," A     A ","  AAAAA  ","         "},
                {"         ","  AAAAA  "," A     A "," A     A "," A  BDDA "," A     A "," A     A ","  AAAAA  ","         "},
                {"         ","  AAAAA  "," A     A "," A     A "," ADDB  A "," A     A "," A     A ","  AAAAA  ","         "},
                {"  CCCCC  "," C     C ","C       C","C       C","C   BDD C","C       C","C       C"," C     C ","  CCCCC  "},
                {" CCC~CCC ","CC     CC","C       C","C       C","C DDB   C","C       C","C       C","CC     CC"," CCCCCCC "},
                {"  CCCCC  "," CCCCCCC ","CCCCCCCCC","CCCCCCCCC","CCCCCCCCC","CCCCCCCCC","CCCCCCCCC"," CCCCCCC ","  CCCCC  "}
            }))
        .addShape(
            STRUCTURE_TIER_2,
            transpose(new String[][]{
                {" CCCCCCC ","CCCCCCCCC","CCCCCCCCC","CCCEEECCC","CCCEEECCC","CCCEEECCC","CCCCCCCCC","CCCCCCCCC"," CCCCCCC "},
                {"  AAAAA  "," A     A ","A       A","A       A","A GGF   A","A       A","A       A"," A     A ","  AAAAA  "},
                {"  AAAAA  "," A     A ","A       A","A       A","A   FGG A","A       A","A       A"," A     A ","  AAAAA  "},
                {"         ","  AAAAA  "," A     A "," A     A "," AGGF  A "," A     A "," A     A ","  AAAAA  ","         "},
                {"         ","  AAAAA  "," A     A "," A     A "," A  FGGA "," A     A "," A     A ","  AAAAA  ","         "},
                {"         ","  AAAAA  "," A     A "," A     A "," AGGF  A "," A     A "," A     A ","  AAAAA  ","         "},
                {"  CCCCC  "," C     C ","C       C","C       C","C   FGG C","C       C","C       C"," C     C ","  CCCCC  "},
                {" CCC~CCC ","CC     CC","C       C","C       C","C GGF   C","C       C","C       C","CC     CC"," CCCCCCC "},
                {"  CCCCC  "," CCCCCCC ","CCCCCCCCC","CCCCCCCCC","CCCCCCCCC","CCCCCCCCC","CCCCCCCCC"," CCCCCCC ","  CCCCC  "}
            }))
        .addShape(
            STRUCTURE_TIER_3,
            transpose(new String[][]{
                {" CCCCCCC ","CCCCCCCCC","CCCCCCCCC","CCCEEECCC","CCCEEECCC","CCCEEECCC","CCCCCCCCC","CCCCCCCCC"," CCCCCCC "},
                {"  AAAAA  "," A     A ","A       A","A       A","A   HII A","A       A","A       A"," A     A ","  AAAAA  "},
                {"  AAAAA  "," A     A ","A       A","A       A","A IIH   A","A       A","A       A"," A     A ","  AAAAA  "},
                {"  AAAAA  "," A     A ","A       A","A       A","A   HII A","A       A","A       A"," A     A ","  AAAAA  "},
                {"         ","  AAAAA  "," A     A "," A     A "," AIIH  A "," A     A "," A     A ","  AAAAA  ","         "},
                {"         ","  AAAAA  "," A     A "," A     A "," A  HIIA "," A     A "," A     A ","  AAAAA  ","         "},
                {"         ","  AAAAA  "," A     A "," A     A "," AIIH  A "," A     A "," A     A ","  AAAAA  ","         "},
                {"  CCCCC  "," C     C ","C       C","C       C","C   HII C","C       C","C       C"," C     C ","  CCCCC  "},
                {" CCC~CCC ","CC     CC","C       C","C       C","C IIH   C","C       C","C       C","CC     CC"," CCCCCCC "},
                {"  CCCCC  "," CCCCCCC ","CCCCCCCCC","CCCCCCCCC","CCCCCCCCC","CCCCCCCCC","CCCCCCCCC"," CCCCCCC ","  CCCCC  "}
            }))
        .addShape(
            STRUCTURE_TIER_4,
            transpose(new String[][]{
                {" CCCCCCC ","CCCCCCCCC","CCCCCCCCC","CCCEEECCC","CCCEEECCC","CCCEEECCC","CCCCCCCCC","CCCCCCCCC"," CCCCCCC "},
                {"  AAAAA  "," A     A ","A       A","A       A","A KKD   A","A       A","A       A"," A     A ","  AAAAA  "},
                {"  AAAAA  "," A     A ","A       A","A       A","A   DKK A","A       A","A       A"," A     A ","  AAAAA  "},
                {"  AAAAA  "," A     A ","A       A","A       A","A KKD   A","A       A","A       A"," A     A ","  AAAAA  "},
                {"  AAAAA  "," A     A ","A       A","A       A","A   DKK A","A       A","A       A"," A     A ","  AAAAA  "},
                {"         ","  AAAAA  "," A     A "," A     A "," AKKD  A "," A     A "," A     A ","  AAAAA  ","         "},
                {"         ","  AAAAA  "," A     A "," A     A "," A  DKKA "," A     A "," A     A ","  AAAAA  ","         "},
                {"         ","  AAAAA  "," A     A "," A     A "," AKKD  A "," A     A "," A     A ","  AAAAA  ","         "},
                {"  CCCCC  "," C     C ","C       C","C       C","C   DKK C","C       C","C       C"," C     C ","  CCCCC  "},
                {" CCC~CCC ","CC     CC","C       C","C       C","C KKD   C","C       C","C       C","CC     CC"," CCCCCCC "},
                {"  CCCCC  "," CCCCCCC ","CCCCCCCCC","CCCCCCCCC","CCCCCCCCC","CCCCCCCCC","CCCCCCCCC"," CCCCCCC ","  CCCCC  "}
            }))
        //spotless:on
        .addElement('A', chainAllGlasses()) // tiered glasses

        .addElement(
            'C', // main casing block
            buildHatchAdder(MTEChamberCentrifuge.class)
                .atLeast(InputBus, OutputBus, InputHatch, OutputHatch, Maintenance, Energy, ExoticEnergy)
                .casingIndex(((BlockCasings1) GregTechAPI.sBlockCasings1).getTextureIndex(12))
                .dot(1)
                .buildAndChain(
                    onElementPass(MTEChamberCentrifuge::onCasingAdded, ofBlock(GregTechAPI.sBlockCasings1, 12))))
        .addElement('E', ofBlock(GregTechAPI.sBlockCasings2, 4)) // titanium turbine casings, for top section.
        .addElement('B', ofFrame(Materials.NaquadahAlloy)) // central shaft piece for tier 1
        .addElement('D', ofBlock(GregTechAPI.sBlockMetal4, 14)) // central rotor piece for tier 1
        .addElement('F', ofFrame(Materials.Neutronium)) // central shaft piece for tier 2
        .addElement('G', ofBlock(GregTechAPI.sBlockMetal5, 2)) // central rotor piece for tier 2
        .addElement('H', ofFrame(Materials.Infinity)) // central shaft piece for tier 3
        // .addElement('I', ofBlock(LudicrousBlocks.resource_block,1)) //central rotor piece for tier 3
        .addElement('I', ofBlock(GregTechAPI.sBlockMetal5, 3))
        .addElement('J', ofFrame(MaterialsUEVplus.TranscendentMetal)) // central shaft piece for tier 4, transcendent
                                                                      // metal
        .addElement('K', ofBlock(GregTechAPI.sBlockMetal9, 3)) // central rotor piece for tier 4, ~shirabon time. is
                                                               // spacetime atm

        .build();

    public MTEChamberCentrifuge(final int aID, final String aName, final String aNameRegional) {
        super(aID, aName, aNameRegional);
    }

    public MTEChamberCentrifuge(String aName) {
        super(aName);
    }

    @Override
    public IStructureDefinition<MTEChamberCentrifuge> getStructureDefinition() {
        return STRUCTURE_DEFINITION;
    }

    @Override
    public boolean isCorrectMachinePart(ItemStack aStack) {
        return true;
    }

    @Override
    public void loadNBTData(NBTTagCompound aNBT) {
        mTier = aNBT.getInteger("multiTier");
        super.loadNBTData(aNBT);
    }

    @Override
    public void saveNBTData(NBTTagCompound aNBT) {
        aNBT.setInteger("multiTier", mTier);
        super.saveNBTData(aNBT);
    }

    @Override
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new MTEChamberCentrifuge(this.mName);
    }

    @Override
    public ITexture[] getTexture(IGregTechTileEntity baseMetaTileEntity, ForgeDirection side, ForgeDirection aFacing,
        int colorIndex, boolean aActive, boolean redstoneLevel) {
        ITexture[] rTexture;
        if (side == aFacing) {
            if (aActive) {
                rTexture = new ITexture[] {
                    Textures.BlockIcons
                        .getCasingTextureForId(GTUtility.getCasingTextureIndex(GregTechAPI.sBlockCasings10, 15)),
                    TextureFactory.builder()
                        .addIcon(OVERLAY_FRONT_MULTI_BREWERY_ACTIVE)
                        .extFacing()
                        .build(),
                    TextureFactory.builder()
                        .addIcon(OVERLAY_FRONT_MULTI_BREWERY_ACTIVE_GLOW)
                        .extFacing()
                        .glow()
                        .build() };
            } else {
                rTexture = new ITexture[] {
                    Textures.BlockIcons
                        .getCasingTextureForId(GTUtility.getCasingTextureIndex(GregTechAPI.sBlockCasings10, 15)),
                    TextureFactory.builder()
                        .addIcon(OVERLAY_FRONT_MULTI_BREWERY)
                        .extFacing()
                        .build(),
                    TextureFactory.builder()
                        .addIcon(OVERLAY_FRONT_MULTI_BREWERY_GLOW)
                        .extFacing()
                        .glow()
                        .build() };
            }
        } else {
            rTexture = new ITexture[] { Textures.BlockIcons
                .getCasingTextureForId(GTUtility.getCasingTextureIndex(GregTechAPI.sBlockCasings10, 15)) };
        }
        return rTexture;
    }

    @Override
    protected MultiblockTooltipBuilder createTooltip() {
        MultiblockTooltipBuilder tt = new MultiblockTooltipBuilder();
        tt.addMachineType("Centrifuge")
            .addInfo("200% faster than singleblock machines of the same voltage")
            .addInfo("Gains 4 * (Total Rotor Tier) Parallels")
            .addInfo("Requires 10L/s of Lubricant to operate, supply {Fluid2} for a 1.25x Parallel multiplier")
            .beginStructureBlock(9, 9, 9, false)
            .addController("Front Center")
            .addCasingInfoMin("Chamber Casing", 120, false)
            .addCasingInfoMin("Any Tiered Glass", 84, true)
            .addCasingInfoMin("Central Frame", 6, false)
            .addCasingInfoMin("Central Rotor Blocks", 10, false)
            .addInputBus("Any Chamber Casing", 1)
            .addOutputBus("Any Chamber Casing", 1)
            .addInputHatch("Any Chamber Casing", 1)
            .addOutputHatch("Any Chamber Casing", 1)
            .addEnergyHatch("Any Chamber Casing", 1)
            .addMaintenanceHatch("Any Chamber Casing", 1)
            .addSubChannelUsage(GTStructureChannels.BOROGLASS)
            .toolTipFinisher(AuthorChrom);
        return tt;
    }

    @Override
    public void construct(ItemStack holoStack, boolean hintsOnly) {
        if (holoStack.stackSize == 1) {
            buildPiece(STRUCTURE_TIER_1, holoStack, hintsOnly, horizontalOffset, verticalOffset, 0);
        }
        if (holoStack.stackSize == 2) {
            buildPiece(STRUCTURE_TIER_2, holoStack, hintsOnly, horizontalOffset, verticalOffset + 1, 0);
        }
        if (holoStack.stackSize == 3) {
            buildPiece(STRUCTURE_TIER_3, holoStack, hintsOnly, horizontalOffset, verticalOffset + 2, 0);
        }
        if (holoStack.stackSize >= 4) {
            buildPiece(STRUCTURE_TIER_4, holoStack, hintsOnly, horizontalOffset, verticalOffset + 3, 0);
        }

    }

    @Override
    public int survivalConstruct(ItemStack holoStack, int elementBudget, ISurvivalBuildEnvironment env) {
        if (mMachine) return -1;
        if (holoStack.stackSize == 1) {
            return survivalBuildPiece(
                STRUCTURE_TIER_1,
                holoStack,
                horizontalOffset,
                verticalOffset,
                0,
                elementBudget,
                env,
                false,
                true);
        }
        if (holoStack.stackSize == 2) {
            return survivalBuildPiece(
                STRUCTURE_TIER_2,
                holoStack,
                horizontalOffset,
                verticalOffset + 1,
                0,
                elementBudget,
                env,
                false,
                true);
        }
        if (holoStack.stackSize == 3) {
            return survivalBuildPiece(
                STRUCTURE_TIER_3,
                holoStack,
                horizontalOffset,
                verticalOffset + 2,
                0,
                elementBudget,
                env,
                false,
                true);
        }
        if (holoStack.stackSize >= 4) {
            return survivalBuildPiece(
                STRUCTURE_TIER_4,
                holoStack,
                horizontalOffset,
                verticalOffset + 3,
                0,
                elementBudget,
                env,
                false,
                true);
        }
        return 0;
    }

    private int mCasingAmount;

    private void onCasingAdded() {
        mCasingAmount++;
    }

    @Override
    public boolean checkMachine(IGregTechTileEntity aBaseMetaTileEntity, ItemStack aStack) {
        mCasingAmount = 0;
        mTier = 0;

        boolean hasEnoughCasings = false;
        if (checkPiece(STRUCTURE_TIER_1, horizontalOffset, verticalOffset, 0)) {
            mTier = 1;
        } else if (checkPiece(STRUCTURE_TIER_2, horizontalOffset, verticalOffset + 1, 0)) {
            mTier = 2;
        } else if (checkPiece(STRUCTURE_TIER_3, horizontalOffset, verticalOffset + 2, 0)) {
            mTier = 3;
        } else if (checkPiece(STRUCTURE_TIER_4, horizontalOffset, verticalOffset + 3, 0)) {
            mTier = 4;
        }
        return mTier > 0 && mCasingAmount >= 120;
    }

    @Override
    protected void setProcessingLogicPower(ProcessingLogic logic) {
        if (mExoticEnergyHatches.isEmpty()) {
            logic.setAvailableVoltage(GTUtility.roundUpVoltage(this.getMaxInputVoltage()));
            logic.setAvailableAmperage(1L);
        } else super.setProcessingLogicPower(logic);
    }

    @Override
    protected ProcessingLogic createProcessingLogic() {
        return new ProcessingLogic() {

            @NotNull
            @Override
            protected CheckRecipeResult validateRecipe(@NotNull GTRecipe recipe) {
                if (!checkFluid(amountToDrain * 10)) return CheckRecipeResultRegistry.NO_FUEL_FOUND;
                return super.validateRecipe(recipe);
            }

            @NotNull
            @Override
            protected OverclockCalculator createOverclockCalculator(@NotNull GTRecipe recipe) { // implements Hatch+1
                                                                                                // OC, just like BHC.
                return super.createOverclockCalculator(recipe)
                    .setMaxOverclocks((GTUtility.getTier(getAverageInputVoltage()) - GTUtility.getTier(recipe.mEUt)));
            }
        }.setSpeedBonus(1F / 3F) // base speed, 200% faster
            .setMaxParallelSupplier(this::getTrueParallel);
    }

    private int getSumRotorLevels() {
        int sumRotorLevels = 100;
        /*
         * for (ItemStack item : turbines)
         * {
         * IToolStats tStats = toolchecker.getToolStats(item);
         * if( tStats instanceof ToolTurbine)
         * {
         * int level = toolchecker.getHarvestLevel(item, "");
         * sumRotorLevels += level;
         * }
         * }
         */
        return sumRotorLevels;
    }

    private boolean checkFluid(int amount) // checks if [amount] fluid is found in ANY of the machines input hatches
    {
        // checks for fluid in hatch, does not drain it.
        FluidStack tFluid = tier2Fluid ? GTModHandler.getDistilledWater(amount) : Materials.Lubricant.getFluid(amount); // t2 : t1

        for (MTEHatchInput mInputHatch : mInputHatches) {
            if (drain(mInputHatch, tFluid, false)) {
                return true;
            }
        }
        return false; // fluid was not found.
    }

    @Override
    public int getMaxParallelRecipes() {
        int parallels = 7 * getSumRotorLevels();
        if (tier2Fluid) {
            parallels = (int) Math.floor(parallels * 1.25);
        }
        return parallels > 0 ? parallels : 1; // if its 1, something messed up lol, just a failsafe in case i mess up
                                              // during testing
    }

    private int ticker = 1; // shoutout pcb fac source code, just increments and drains (amountToDrain) of the given
                            // fluid every second

    @Override
    public boolean onRunningTick(ItemStack aStack) {
        if (!super.onRunningTick(aStack)) {
            return false;
        }
        if (ticker % 21 == 0) {
            FluidStack tFluid = tier2Fluid ? GTModHandler.getDistilledWater(amountToDrain):
                Materials.Lubricant.getFluid(amountToDrain); // gets fluid to drain
            for (MTEHatchInput mInputHatch : mInputHatches) { // worst case, checks all hatches fluid not found, stops
                                                              // machine
                if (drain(mInputHatch, tFluid, true)) {
                    ticker = 1;
                    return true;
                }
            }
            stopMachine(ShutDownReasonRegistry.outOfFluid(tFluid));
            ticker = 1;
            return false;
        }
        ticker++;
        return true;
    }

    // mui2 stuff goes here

    @Override
    protected @NotNull MTEChamberCentrifugeGui getGui() {
        return new MTEChamberCentrifugeGui(this);
    }

    @Override
    protected boolean useMui2() {
        return true;
    }

    @Override
    public RecipeMap<?> getRecipeMap() {
        return RecipeMaps.centrifugeRecipes;
    }

    @Override
    public int getMaxEfficiency(ItemStack aStack) {
        return 10000;
    }

    @Override
    public int getDamageToComponent(ItemStack aStack) {
        return 0;
    }

    @Override
    public boolean supportsVoidProtection() {
        return true;
    }

    @Override
    public boolean supportsBatchMode() {
        return true;
    }

    @Override
    public boolean supportsInputSeparation() {
        return true;
    }

    @Override
    public boolean supportsSingleRecipeLocking() {
        return true;
    }
}
