package goodgenerator.blocks.tileEntity;

import static com.gtnewhorizon.structurelib.structure.StructureUtility.*;
import static goodgenerator.util.DescTextLocalization.BLUE_PRINT_INFO;
import static gregtech.api.enums.GT_Values.V;
import static gregtech.api.enums.Textures.BlockIcons.*;
import static gregtech.api.util.GT_StructureUtility.buildHatchAdder;
import static gregtech.api.util.GT_StructureUtility.ofHatchAdder;

import com.gtnewhorizon.structurelib.alignment.constructable.IConstructable;
import com.gtnewhorizon.structurelib.alignment.constructable.ISurvivalConstructable;
import com.gtnewhorizon.structurelib.structure.IItemSource;
import com.gtnewhorizon.structurelib.structure.IStructureDefinition;
import com.gtnewhorizon.structurelib.structure.StructureDefinition;
import goodgenerator.blocks.tileEntity.base.GT_MetaTileEntity_TooltipMultiBlockBase_EM;
import goodgenerator.loader.Loaders;
import goodgenerator.util.DescTextLocalization;
import goodgenerator.util.MyRecipeAdder;
import gregtech.api.GregTech_API;
import gregtech.api.enums.GT_HatchElement;
import gregtech.api.interfaces.IHatchElement;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_Hatch;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_Hatch_Input;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_Hatch_Output;
import gregtech.api.render.TextureFactory;
import gregtech.api.util.*;
import ic2.core.Ic2Items;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.StatCollector;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;

public class ExtremeHeatExchanger extends GT_MetaTileEntity_TooltipMultiBlockBase_EM
        implements IConstructable, ISurvivalConstructable {

    protected IStructureDefinition<ExtremeHeatExchanger> multiDefinition = null;

    public static double penalty_per_config = 0.015d;
    protected int casingAmount = 0;
    protected GT_MetaTileEntity_Hatch_Input mHotFluidHatch;
    protected GT_MetaTileEntity_Hatch_Output mCooledFluidHatch;
    private boolean transformed = false;
    private String hotName;
    private MyRecipeAdder.ExtremeHeatExchangerRecipe tRunningRecipe;

    public ExtremeHeatExchanger(String name) {
        super(name);
    }

    public ExtremeHeatExchanger(int id, String name, String nameRegional) {
        super(id, name, nameRegional);
    }

    @Override
    public IStructureDefinition<ExtremeHeatExchanger> getStructure_EM() {
        if (multiDefinition == null) {
            multiDefinition = StructureDefinition.<ExtremeHeatExchanger>builder()
                    .addShape(mName, transpose(new String[][] {
                        {
                            " CCC ", "TTTTT", "TTTTT", "TTTTT", "TTTTT", "TTTTT", "TTTTT", "TTTTT", "TTTTT", "TTTTT",
                            " CCC "
                        },
                        {
                            " CCC ", "GPPPG", "GWWWG", "GPPPG", "GWWWG", "GPPPG", "GWWWG", "GPPPG", "GWWWG", "GPPPG",
                            " CCC "
                        },
                        {
                            " CFC ", "GPPPG", "GWWWG", "GPPPG", "GWWWG", "GPPPG", "GWWWG", "GPPPG", "GWWWG", "GPPPG",
                            " CEC "
                        },
                        {
                            " CCC ", "GPPPG", "GWWWG", "GPPPG", "GWWWG", "GPPPG", "GWWWG", "GPPPG", "GWWWG", "GPPPG",
                            " CCC "
                        },
                        {
                            " CCC ", "GPPPG", "GWWWG", "GPPPG", "GWWWG", "GPPPG", "GWWWG", "GPPPG", "GWWWG", "GPPPG",
                            " CCC "
                        },
                        {
                            " C~C ", "BBBBB", "BBBBB", "BBBBB", "BBBBB", "BBBBB", "BBBBB", "BBBBB", "BBBBB", "BBBBB",
                            " CCC "
                        },
                    }))
                    .addElement(
                            'B',
                            ofChain(
                                    buildHatchAdder(ExtremeHeatExchanger.class)
                                            .atLeast(GT_HatchElement.InputHatch, GT_HatchElement.Maintenance)
                                            .casingIndex(48)
                                            .dot(1)
                                            .build(),
                                    onElementPass(x -> x.casingAmount++, ofBlock(GregTech_API.sBlockCasings4, 0))))
                    .addElement(
                            'T',
                            ofChain(
                                    buildHatchAdder(ExtremeHeatExchanger.class)
                                            .atLeast(GT_HatchElement.OutputHatch, GT_HatchElement.Maintenance)
                                            .casingIndex(48)
                                            .dot(1)
                                            .build(),
                                    onElementPass(x -> x.casingAmount++, ofBlock(GregTech_API.sBlockCasings4, 0))))
                    .addElement(
                            'F',
                            buildHatchAdder(ExtremeHeatExchanger.class)
                                    .atLeast(EHEHatches.HotInputHatch)
                                    .casingIndex(48)
                                    .dot(3)
                                    .build())
                    .addElement(
                            'E',
                            buildHatchAdder(ExtremeHeatExchanger.class)
                                    .atLeast(EHEHatches.ColdOutputHatch)
                                    .casingIndex(48)
                                    .dot(4)
                                    .build())
                    .addElement(
                            'C',
                            ofChain(
                                    ofHatchAdder(
                                            ExtremeHeatExchanger::addMaintenanceToMachineList,
                                            48,
                                            GregTech_API.sBlockCasings4,
                                            0),
                                    onElementPass(x -> x.casingAmount++, ofBlock(GregTech_API.sBlockCasings4, 0))))
                    .addElement('G', ofBlock(Block.getBlockFromItem(Ic2Items.reinforcedGlass.getItem()), 0))
                    .addElement('P', ofBlock(GregTech_API.sBlockCasings2, 15))
                    .addElement('W', ofBlock(Loaders.pressureResistantWalls, 0))
                    .build();
        }
        return multiDefinition;
    }

    public boolean addHotFluidInputToMachineList(IGregTechTileEntity aTileEntity, int aBaseCasingIndex) {
        if (aTileEntity == null) return false;
        IMetaTileEntity aMetaTileEntity = aTileEntity.getMetaTileEntity();
        if (aMetaTileEntity == null) return false;
        if (aMetaTileEntity instanceof GT_MetaTileEntity_Hatch_Input) {
            ((GT_MetaTileEntity_Hatch) aMetaTileEntity).updateTexture(aBaseCasingIndex);
            mHotFluidHatch = (GT_MetaTileEntity_Hatch_Input) aMetaTileEntity;
            return true;
        }
        return false;
    }

    public boolean addColdFluidOutputToMachineList(IGregTechTileEntity aTileEntity, int aBaseCasingIndex) {
        if (aTileEntity == null) return false;
        IMetaTileEntity aMetaTileEntity = aTileEntity.getMetaTileEntity();
        if (aMetaTileEntity == null) return false;
        if (aMetaTileEntity instanceof GT_MetaTileEntity_Hatch_Output) {
            ((GT_MetaTileEntity_Hatch) aMetaTileEntity).updateTexture(aBaseCasingIndex);
            mCooledFluidHatch = (GT_MetaTileEntity_Hatch_Output) aMetaTileEntity;
            return true;
        }
        return false;
    }

    @Override
    public void loadNBTData(NBTTagCompound aNBT) {
        transformed = aNBT.getBoolean("transformed");
        hotName = aNBT.getString("hotName");
        if (hotName != null) {
            tRunningRecipe = MyRecipeAdder.mXHeatExchangerFuelMap.get(new Fluid(hotName));
        }
        super.loadNBTData(aNBT);
    }

    @Override
    public void saveNBTData(NBTTagCompound aNBT) {
        aNBT.setBoolean("transformed", transformed);
        aNBT.setString("hotName", hotName);
        super.saveNBTData(aNBT);
    }

    @Override
    public boolean checkMachine_EM(IGregTechTileEntity aBaseMetaTileEntity, ItemStack aStack) {
        this.casingAmount = 0;
        mCooledFluidHatch = null;
        mHotFluidHatch = null;
        return structureCheck_EM(mName, 2, 5, 0) && mMaintenanceHatches.size() == 1 && casingAmount >= 25;
    }

    @Override
    protected GT_Multiblock_Tooltip_Builder createTooltip() {
        final GT_Multiblock_Tooltip_Builder tt = new GT_Multiblock_Tooltip_Builder();
        tt.addMachineType("Heat Exchanger/Plasma Heat Exchanger")
                .addInfo("Controller block for the Extreme Heat Exchanger.")
                .addInfo("Accept Hot fluid like lava, hot coolant or plasma.")
                .addInfo("Output SC Steam/SH Steam/Steam.")
                .addInfo("Check NEI for more info.")
                .addInfo(BLUE_PRINT_INFO)
                .addSeparator()
                .addController("Front bottom")
                .addOtherStructurePart("Input Hatch: distilled water", "Hint block with dot 1")
                .addOtherStructurePart("Output Hatch: SC Steam/SH Steam/Steam", "Hint block with dot 2")
                .addOtherStructurePart("Input Hatch: Hot fluid or plasma", "Hint block with dot 3")
                .addOtherStructurePart("Output Hatch: Cold fluid", "Hint block with dot 4")
                .addMaintenanceHatch("Any Casing")
                .addCasingInfo("Robust Tungstensteel Machine Casings", 25)
                .toolTipFinisher("Good Generator");
        return tt;
    }

    @Override
    public boolean checkRecipe_EM(ItemStack aStack) {
        tRunningRecipe = null;
        if (mHotFluidHatch.getFluid() == null) return true;
        MyRecipeAdder.ExtremeHeatExchangerRecipe tRecipe = MyRecipeAdder.mXHeatExchangerFuelMap.get(
                mHotFluidHatch.getFluid().getFluid());
        if (tRecipe == null) return false;
        tRunningRecipe = tRecipe;
        this.hotName = mHotFluidHatch.getFluid().getFluid().getName();
        int tMaxConsume = tRecipe.getMaxHotFluidConsume();
        int transformed_threshold = tRecipe.mSpecialValue;
        int tRealConsume = Math.min(tMaxConsume, mHotFluidHatch.getFluid().amount);
        double penalty = 0.0d;
        double efficiency = 1d;
        int shs_reduction_per_config = 150;

        if (mInventory[1] != null && mInventory[1].getUnlocalizedName().startsWith("gt.integrated_circuit")) {
            int circuit_config = mInventory[1].getItemDamage();
            if (circuit_config >= 1 && circuit_config <= 25) {
                penalty = (circuit_config - 1) * penalty_per_config;
                transformed_threshold -= (shs_reduction_per_config * (circuit_config - 1));
            }
        }
        efficiency -= penalty;

        if (transformed_threshold <= 0) transformed_threshold = 1;

        transformed = tRealConsume >= transformed_threshold;

        this.mMaxProgresstime = 20;
        this.mEUt = (int) (tRecipe.getEUt() * efficiency * ((double) tRealConsume / (double) tMaxConsume));
        mHotFluidHatch.drain(tRealConsume, true);
        mCooledFluidHatch.fill(new FluidStack(tRecipe.getCooledFluid(), tRealConsume), true);
        this.mEfficiencyIncrease = 160;

        return true;
    }

    @Override
    public boolean onRunningTick(ItemStack aStack) {
        if (this.mEUt > 0 && tRunningRecipe != null) {
            Fluid tReadySteam = transformed ? tRunningRecipe.getHeatedSteam() : tRunningRecipe.getNormalSteam();
            int waterAmount = (int) (this.mEUt / getUnitSteamPower(tReadySteam.getName())) / 160;
            if (waterAmount < 0) return false;
            if (depleteInput(GT_ModHandler.getDistilledWater(waterAmount))) {
                addOutput(new FluidStack(tReadySteam, waterAmount * 160));
            } else {
                GT_Log.exp.println(this.mName + " had no more Distilled water!");
                mHotFluidHatch.getBaseMetaTileEntity().doExplosion(V[8]);
                return false;
            }
        }
        return true;
    }

    public double getUnitSteamPower(String steam) {
        switch (steam) {
            case "steam":
                return 0.5;
            case "ic2superheatedsteam":
                return 1;
            case "supercriticalsteam":
                return 100;
            default:
                return -1;
        }
    }

    @Override
    public void construct(ItemStack stackSize, boolean hintsOnly) {
        structureBuild_EM(mName, 2, 5, 0, stackSize, hintsOnly);
    }

    @Override
    public String[] getStructureDescription(ItemStack stackSize) {
        return DescTextLocalization.addText("ExtremeHeatExchanger.hint", 6);
    }

    @Override
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new ExtremeHeatExchanger(mName);
    }

    @Override
    public int getMaxEfficiency(ItemStack aStack) {
        return 10000;
    }

    @Override
    public String[] getInfoData() {
        int tThreshold = tRunningRecipe != null ? tRunningRecipe.mSpecialValue : 0;
        return new String[] {
            StatCollector.translateToLocal("GT5U.multiblock.Progress") + ": " + EnumChatFormatting.GREEN
                    + GT_Utility.formatNumbers(mProgresstime / 20) + EnumChatFormatting.RESET + " s / "
                    + EnumChatFormatting.YELLOW
                    + GT_Utility.formatNumbers(mMaxProgresstime / 20) + EnumChatFormatting.RESET + " s",
            StatCollector.translateToLocal("GT5U.multiblock.problems") + ": " + EnumChatFormatting.RED
                    + (getIdealStatus() - getRepairStatus()) + EnumChatFormatting.RESET + " "
                    + StatCollector.translateToLocal("GT5U.multiblock.efficiency")
                    + ": " + EnumChatFormatting.YELLOW
                    + mEfficiency / 100.0F + EnumChatFormatting.RESET + " %",
            StatCollector.translateToLocal("scanner.info.XHE.0") + " "
                    + (transformed ? EnumChatFormatting.RED : EnumChatFormatting.YELLOW)
                    + GT_Utility.formatNumbers(this.mEUt) + EnumChatFormatting.RESET + " EU/t",
            StatCollector.translateToLocal("scanner.info.XHE.1") + " " + EnumChatFormatting.GREEN
                    + GT_Utility.formatNumbers(tThreshold) + EnumChatFormatting.RESET + " L/s"
        };
    }

    @Override
    public ITexture[] getTexture(
            IGregTechTileEntity aBaseMetaTileEntity,
            byte aSide,
            byte aFacing,
            byte aColorIndex,
            boolean aActive,
            boolean aRedstone) {
        if (aSide == aFacing) {
            if (aActive)
                return new ITexture[] {
                    casingTexturePages[0][48],
                    TextureFactory.builder()
                            .addIcon(OVERLAY_FRONT_HEAT_EXCHANGER_ACTIVE)
                            .extFacing()
                            .build(),
                    TextureFactory.builder()
                            .addIcon(OVERLAY_FRONT_HEAT_EXCHANGER_ACTIVE_GLOW)
                            .extFacing()
                            .glow()
                            .build()
                };
            return new ITexture[] {
                casingTexturePages[0][48],
                TextureFactory.builder()
                        .addIcon(OVERLAY_FRONT_HEAT_EXCHANGER)
                        .extFacing()
                        .build(),
                TextureFactory.builder()
                        .addIcon(OVERLAY_FRONT_HEAT_EXCHANGER_GLOW)
                        .extFacing()
                        .glow()
                        .build()
            };
        }
        return new ITexture[] {casingTexturePages[0][48]};
    }

    @Override
    public int survivalConstruct(ItemStack stackSize, int elementBudget, IItemSource source, EntityPlayerMP actor) {
        if (mMachine) return -1;
        return survivialBuildPiece(mName, stackSize, 2, 5, 0, elementBudget, source, actor, false, true);
    }

    private enum EHEHatches implements IHatchElement<ExtremeHeatExchanger> {
        HotInputHatch(ExtremeHeatExchanger::addHotFluidInputToMachineList, GT_MetaTileEntity_Hatch_Input.class) {
            @Override
            public long count(ExtremeHeatExchanger t) {
                if (t.mHotFluidHatch == null) return 0;
                return 1;
            }
        },
        ColdOutputHatch(ExtremeHeatExchanger::addColdFluidOutputToMachineList, GT_MetaTileEntity_Hatch_Output.class) {
            @Override
            public long count(ExtremeHeatExchanger t) {
                if (t.mCooledFluidHatch == null) return 0;
                return 1;
            }
        };

        private final List<Class<? extends IMetaTileEntity>> mteClasses;
        private final IGT_HatchAdder<ExtremeHeatExchanger> adder;

        EHEHatches(IGT_HatchAdder<ExtremeHeatExchanger> adder, Class<? extends IMetaTileEntity>... mteClasses) {
            this.mteClasses = Collections.unmodifiableList(Arrays.asList(mteClasses));
            this.adder = adder;
        }

        @Override
        public List<? extends Class<? extends IMetaTileEntity>> mteClasses() {
            return mteClasses;
        }

        public IGT_HatchAdder<? super ExtremeHeatExchanger> adder() {
            return adder;
        }
    }
}
