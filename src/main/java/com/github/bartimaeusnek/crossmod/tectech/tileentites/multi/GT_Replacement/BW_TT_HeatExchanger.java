package com.github.bartimaeusnek.crossmod.tectech.tileentites.multi.GT_Replacement;

import com.github.technus.tectech.mechanics.structure.IStructureDefinition;
import com.github.technus.tectech.mechanics.structure.StructureDefinition;
import gregtech.api.GregTech_API;
import gregtech.api.enums.Materials;
import gregtech.api.enums.Textures;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_Hatch;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_Hatch_Input;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_Hatch_Output;
import gregtech.api.objects.GT_RenderedTexture;
import gregtech.api.util.GT_Log;
import gregtech.api.util.GT_ModHandler;
import gregtech.api.util.GT_Recipe;
import gregtech.api.util.GT_Utility;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.StatCollector;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;

import java.util.Collection;

import static com.github.bartimaeusnek.bartworks.util.BW_Tooltip_Reference.ADV_STR_CHECK;
import static com.github.bartimaeusnek.bartworks.util.BW_Tooltip_Reference.TT_BLUEPRINT;
import static com.github.technus.tectech.mechanics.structure.StructureUtility.*;

public class BW_TT_HeatExchanger extends TT_Abstract_GT_Replacement {
    public static float penalty_per_config = 0.015f;  // penalize 1.5% efficiency per circuitry level (1-25)

    private GT_MetaTileEntity_Hatch_Input mInputHotFluidHatch;
    private GT_MetaTileEntity_Hatch_Output mOutputColdFluidHatch;
    private boolean superheated = false;
    private int superheated_threshold = 0;
    private float water;
    private byte blocks = 0;

    public BW_TT_HeatExchanger(Object unused, Object unused2) {
        super(1154, "multimachine.heatexchanger", "Large Heat Exchanger");
    }

    public BW_TT_HeatExchanger(String aName) {
        super(aName);
    }

    private static final byte TEXTURE_INDEX = 50;
    private static final byte SOLID_CASING_META = 2;
    private static final byte PIPE_CASING_META = 14;
    private static final IStructureDefinition<BW_TT_HeatExchanger> STRUCTURE_DEFINITION = StructureDefinition
            .<BW_TT_HeatExchanger>builder()
            .addShape("main",
                    transpose(new String[][]{
                            {"AAA", "ACA", "AAA"},
                            {"AAA", "ABA", "AAA"},
                            {"AAA", "ABA", "AAA"},
                            {"A~A", "ADA", "AAA"}
                    })
            ).addElement(
                    'A',
                    ofChain(
                            ofHatchAdder(
                                    BW_TT_HeatExchanger::addClassicToMachineList, TEXTURE_INDEX,
                                    GregTech_API.sBlockCasings4, SOLID_CASING_META
                            ),
                            onElementPass(
                                    x -> ++x.blocks,
                                    ofBlock(
                                            GregTech_API.sBlockCasings4, SOLID_CASING_META
                                    )
                            )
                    )

            ).addElement(
                    'B',
                    ofBlock(
                            GregTech_API.sBlockCasings2, PIPE_CASING_META,
                            GregTech_API.sBlockCasings2, PIPE_CASING_META
                    )
            ).addElement(
                    'C',
                    ofHatchAdder(
                            BW_TT_HeatExchanger::addColdFluidOutputToMachineList,TEXTURE_INDEX,
                            1
                    )
            ).addElement(
                    'D',
                    ofHatchAdder(
                            BW_TT_HeatExchanger::addHotFluidInputToMachineList,TEXTURE_INDEX,
                            2
                    )
            )
            .build();

    @Override
    public IStructureDefinition<BW_TT_HeatExchanger> getStructure_EM() {
        return STRUCTURE_DEFINITION;
    }

    @Override
    protected boolean checkMachine_EM(IGregTechTileEntity iGregTechTileEntity, ItemStack itemStack) {
       this.blocks = 0;
       return this.structureCheck_EM("main", 1, 3, 0) && this.blocks >= 20;
    }

    private final static String[] desc = new String[]{
            "Heat Exchanger",
            "Controller Block for the Large Heat Exchanger",
            "Inputs are Hot Fluids and Distilled Water",
            "Outputs Cold Fluids and SH Steam/Steam",
            "Requires an additional Input and Output Hatch anywhere!",
            ADV_STR_CHECK,
            TT_BLUEPRINT
    };

    public String[] getDescription() {
        return desc;
    }

    @Override
    public void loadNBTData(NBTTagCompound aNBT) {
        superheated = aNBT.getBoolean("superheated");
        super.loadNBTData(aNBT);
    }

    @Override
    public void saveNBTData(NBTTagCompound aNBT) {
        aNBT.setBoolean("superheated", superheated);
        super.saveNBTData(aNBT);
    }

    public ITexture[] getTexture(IGregTechTileEntity aBaseMetaTileEntity, byte aSide, byte aFacing, byte aColorIndex, boolean aActive, boolean aRedstone) {
        if (aSide == aFacing) {
            return new ITexture[]{Textures.BlockIcons.casingTexturePages[0][50], new GT_RenderedTexture(aActive ? Textures.BlockIcons.OVERLAY_FRONT_HEAT_EXCHANGER_ACTIVE : Textures.BlockIcons.OVERLAY_FRONT_HEAT_EXCHANGER)};
        }
        return new ITexture[]{Textures.BlockIcons.casingTexturePages[0][50]};
    }

    private void checkRecipeClassic(float efficiency){
        int fluidAmountToConsume = mInputHotFluidHatch.getFluidAmount();
        float steam_output_multiplier = 20f; // default: multiply output by 4 * 10 (boosted x5)
        boolean do_lava = false;
        // If we're working with lava, adjust the threshold and multipliers accordingly.
        if (GT_ModHandler.isLava(mInputHotFluidHatch.getFluid())) {
            steam_output_multiplier /= 5f; // lava is not boosted
            superheated_threshold /= 4f; // unchanged
            do_lava = true;
        } else if (mInputHotFluidHatch.getFluid().isFluidEqual(FluidRegistry.getFluidStack("ic2hotcoolant", 1))) {
            steam_output_multiplier /= 2f; // was boosted x2 on top of x5 -> total x10 -> nerf with this code back to 5x
            superheated_threshold /= 5f; // 10x smaller since the Hot Things production in reactor is the same.
        }

        superheated = fluidAmountToConsume >= superheated_threshold; // set the internal superheated flag if we have enough hot fluid.  Used in the onRunningTick method.
        fluidAmountToConsume = Math.min(fluidAmountToConsume, superheated_threshold * 2); // Don't consume too much hot fluid per second
        mInputHotFluidHatch.drain(fluidAmountToConsume, true);
        this.mMaxProgresstime = 20;
        this.mEUt = (int) (fluidAmountToConsume * steam_output_multiplier * efficiency);
        if (do_lava) {
            mOutputColdFluidHatch.fill(FluidRegistry.getFluidStack("ic2pahoehoelava", fluidAmountToConsume), true);
        } else {
            mOutputColdFluidHatch.fill(FluidRegistry.getFluidStack("ic2coolant", fluidAmountToConsume), true);
        }
        this.mEfficiencyIncrease = 80;
    }

    Materials last = null;

    private void checkRecipe_Additions(float efficiency){
        Materials m = null;
        if (last != null && mInputHotFluidHatch.getFluid().isFluidEqual(last.getPlasma(1))) {
            m = last;
        } else {
            for (Materials materials : Materials.values()) {
                if (mInputHotFluidHatch.getFluid().isFluidEqual(materials.getPlasma(1))) {
                    last = m = materials;
                    break;
                }
                else if (mInputHotFluidHatch.getFluid().isFluidEqual(materials.getMolten(1))){
                    GT_Log.exp.println(this.mName + " had Molten Metal Injected!");
                    explodeMultiblock(); // Generate crater
                    return;
                }
            }
        }

        if (m == null)
            return;

        int heat = getFuelValue(mInputHotFluidHatch.getFluid(), (int) (100 * efficiency));

        superheated_threshold /= 4f;

        superheated = heat >= superheated_threshold; // set the internal superheated flag if we have enough hot fluid.  Used in the onRunningTick method.

        mInputHotFluidHatch.drain(1, true);
        this.mMaxProgresstime = 20;
        this.mEUt = heat;
        mOutputColdFluidHatch.fill(m.getMolten(1), true);
        this.mEfficiencyIncrease = 80;
    }

    private int getFuelValue(FluidStack aLiquid, int efficency) {
        if (aLiquid == null || getRecipes() == null)
            return 0;
        FluidStack tLiquid;
        Collection<GT_Recipe> tRecipeList = getRecipes().mRecipeList;
        if (tRecipeList == null)
            return 0;
        for (GT_Recipe tFuel : tRecipeList) {
            tLiquid = GT_Utility.getFluidForFilledItem(tFuel.getRepresentativeInput(0), true);
            if (tLiquid == null) {
                continue;
            }
            if (!aLiquid.isFluidEqual(tLiquid)) {
                continue;
            }
            long val = (long) tFuel.mSpecialValue * efficency / 100;
            if (val > Integer.MAX_VALUE) {
                throw new ArithmeticException("Integer LOOPBACK!");
            }
            return (int) val;
        }
        return 0;
    }

    private GT_Recipe.GT_Recipe_Map getRecipes() {
        return GT_Recipe.GT_Recipe_Map.sPlasmaFuels;
    }

    public boolean checkRecipe_EM(ItemStack aStack) {
        if (mInputHotFluidHatch.getFluid() == null)
            return true;
        FluidStack fluid = mInputHotFluidHatch.getFluid(); // how much fluid is in hatch
        superheated_threshold = 4000;        // default: must have 4000L per second to generate superheated steam
        float efficiency = calculateEfficiency();

        if (GT_ModHandler.isLava(fluid) || fluid.isFluidEqual(FluidRegistry.getFluidStack("ic2hotcoolant", 1)))
            checkRecipeClassic(efficiency);
        else
            checkRecipe_Additions(efficiency);

        return true;
    }

    private float calculateEfficiency(){
        float defaultEff = 1f;               // default: operate at 100% efficiency with no integrated circuitry
        float penalty;                       // penalty to apply to output based on circuitry level (1-25).
        int shs_reduction_per_config = 150;  // reduce threshold 150L/s per circuitry level (1-25)

        // Do we have an integrated circuit?
        if (mInventory[1] == null || mInventory[1].getItem() != GT_Utility.getIntegratedCircuit(0).getItem())
            return defaultEff;

        //valid configuration?
        int circuit_config = mInventory[1].getItemDamage();
        if (circuit_config < 1 || circuit_config > 25)
            return defaultEff;

        // If so, apply the penalty and reduced threshold.
        penalty = (circuit_config - 1) * penalty_per_config;
        superheated_threshold -= (shs_reduction_per_config * (circuit_config - 1));
        return defaultEff - penalty;
    }

    private int useWater(float input) {
        water = water + input;
        int usage = (int) water;
        water = water - usage;
        return usage;
    }

    public boolean onRunningTick(ItemStack aStack) {
        if (this.mEUt <= 0) {
            return false;
        }
        int tGeneratedEU = (int) (this.mEUt * 2L * this.mEfficiency / 100_00L); // APPROXIMATELY how much steam to generate.
        if (tGeneratedEU <= 0) {
            return false;
        }

        if (superheated)
            tGeneratedEU /= 2; // We produce half as much superheated steam if necessary

        int distilledConsumed = useWater(tGeneratedEU / 160f); // how much distilled water to consume
        //tGeneratedEU = distilledConsumed * 160; // EXACTLY how much steam to generate, producing a perfect 1:160 ratio with distilled water consumption

        FluidStack distilledStack = GT_ModHandler.getDistilledWater(distilledConsumed);
        if (depleteInput(distilledStack)) // Consume the distilled water
        {
            if (superheated) {
                addOutput(FluidRegistry.getFluidStack("ic2superheatedsteam", tGeneratedEU)); // Generate superheated steam
            } else {
                addOutput(GT_ModHandler.getSteam(tGeneratedEU)); // Generate regular steam
            }
        } else {
            GT_Log.exp.println(this.mName + " had no more Distilled water!");
            explodeMultiblock(); // Generate crater
            return false;
        }
        return true;
    }


    public boolean addColdFluidOutputToMachineList(IGregTechTileEntity aTileEntity, int aBaseCasingIndex) {
        if (aTileEntity == null)
            return false;
        IMetaTileEntity aMetaTileEntity = aTileEntity.getMetaTileEntity();
        if (!(aMetaTileEntity instanceof GT_MetaTileEntity_Hatch_Output)) {
            return false;
        }
        ((GT_MetaTileEntity_Hatch) aMetaTileEntity).updateTexture(aBaseCasingIndex);
        mOutputColdFluidHatch = (GT_MetaTileEntity_Hatch_Output) aMetaTileEntity;
        return true;
    }

    public boolean addHotFluidInputToMachineList(IGregTechTileEntity aTileEntity, int aBaseCasingIndex) {
        if (aTileEntity == null) return false;
        IMetaTileEntity aMetaTileEntity = aTileEntity.getMetaTileEntity();
        if (!(aMetaTileEntity instanceof GT_MetaTileEntity_Hatch_Input)) {
            return false;
        }
        ((GT_MetaTileEntity_Hatch) aMetaTileEntity).updateTexture(aBaseCasingIndex);
        mInputHotFluidHatch = (GT_MetaTileEntity_Hatch_Input) aMetaTileEntity;
        mInputHotFluidHatch.mRecipeMap = null;
        return true;
    }

    public IMetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new BW_TT_HeatExchanger(this.mName);
    }

    @Override
    public String[] getInfoData() {
        return new String[]{
                StatCollector.translateToLocal("GT5U.multiblock.Progress") + ": " +
                        EnumChatFormatting.GREEN + mProgresstime / 20 + EnumChatFormatting.RESET + " s / " +
                        EnumChatFormatting.YELLOW + mMaxProgresstime / 20 + EnumChatFormatting.RESET + " s",
                StatCollector.translateToLocal("GT5U.multiblock.usage") + " " + StatCollector.translateToLocal("GT5U.LHE.steam") + ": " +
                        (superheated ? EnumChatFormatting.RED : EnumChatFormatting.YELLOW) + (superheated ? -2 * mEUt : -mEUt) + EnumChatFormatting.RESET + " EU/t",
                StatCollector.translateToLocal("GT5U.multiblock.problems") + ": " +
                        EnumChatFormatting.RED + (getIdealStatus() - getRepairStatus()) + EnumChatFormatting.RESET +
                        " " + StatCollector.translateToLocal("GT5U.multiblock.efficiency") + ": " +
                        EnumChatFormatting.YELLOW + mEfficiency / 100.0F + EnumChatFormatting.RESET + " %",
                StatCollector.translateToLocal("GT5U.LHE.superheated") + ": " + (superheated ? EnumChatFormatting.RED : EnumChatFormatting.BLUE) + superheated + EnumChatFormatting.RESET,
                StatCollector.translateToLocal("GT5U.LHE.superheated") + " " + StatCollector.translateToLocal("GT5U.LHE.threshold") + ": " + EnumChatFormatting.GREEN + superheated_threshold + EnumChatFormatting.RESET
        };
    }

    @Override
    public void construct(ItemStack itemStack, boolean b) {
        this.structureBuild_EM("main", 1,3,0, b, itemStack);
    }

    private static final String[] sfStructureDescription = new String[] {
            "1 - Cold Fluid Output",
            "2 - Hot Fluid Input",
            "Needs an additional Output Hatch",
            "Needs an additional Input Hatch",
            "20 Casings at least!"
    };

    @Override
    public String[] getStructureDescription(ItemStack itemStack) {
        return sfStructureDescription;
    }
}