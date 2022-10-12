package goodgenerator.blocks.tileEntity;

import static com.gtnewhorizon.structurelib.structure.StructureUtility.ofBlock;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.transpose;
import static goodgenerator.util.DescTextLocalization.BLUE_PRINT_INFO;
import static gregtech.api.enums.Textures.BlockIcons.*;
import static gregtech.api.util.GT_StructureUtility.buildHatchAdder;

import com.github.technus.tectech.thing.metaTileEntity.hatch.GT_MetaTileEntity_Hatch_DynamoMulti;
import com.gtnewhorizon.structurelib.alignment.constructable.IConstructable;
import com.gtnewhorizon.structurelib.alignment.constructable.ISurvivalConstructable;
import com.gtnewhorizon.structurelib.structure.IItemSource;
import com.gtnewhorizon.structurelib.structure.IStructureDefinition;
import com.gtnewhorizon.structurelib.structure.StructureDefinition;
import goodgenerator.blocks.tileEntity.base.GT_MetaTileEntity_TooltipMultiBlockBase_EM;
import goodgenerator.crossmod.LoadedList;
import goodgenerator.loader.Loaders;
import goodgenerator.util.DescTextLocalization;
import gregtech.api.GregTech_API;
import gregtech.api.enums.GT_HatchElement;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.implementations.*;
import gregtech.api.render.TextureFactory;
import gregtech.api.util.GTPP_Recipe;
import gregtech.api.util.GT_Multiblock_Tooltip_Builder;
import gregtech.api.util.GT_Recipe;
import gregtech.api.util.GT_Utility;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;

public class UniversalChemicalFuelEngine extends GT_MetaTileEntity_TooltipMultiBlockBase_EM
        implements IConstructable, ISurvivalConstructable {

    protected final double DIESEL_EFFICIENCY_COEFFICIENT = 0.04D;
    protected final double GAS_EFFICIENCY_COEFFICIENT = 0.04D;
    protected final double ROCKET_EFFICIENCY_COEFFICIENT = 0.005D;
    protected final double EFFICIENCY_CEILING = 1.5D;

    private long lEUt;
    private long tEff;

    private IStructureDefinition<UniversalChemicalFuelEngine> multiDefinition = null;

    public UniversalChemicalFuelEngine(String name) {
        super(name);
    }

    public UniversalChemicalFuelEngine(int id, String name, String nameRegional) {
        super(id, name, nameRegional);
    }

    public final boolean addMaintenance(IGregTechTileEntity aTileEntity, int aBaseCasingIndex) {
        if (aTileEntity == null) {
            return false;
        } else {
            IMetaTileEntity aMetaTileEntity = aTileEntity.getMetaTileEntity();
            if (aMetaTileEntity instanceof GT_MetaTileEntity_Hatch_Maintenance) {
                ((GT_MetaTileEntity_Hatch) aMetaTileEntity).updateTexture(aBaseCasingIndex);
                return this.mMaintenanceHatches.add((GT_MetaTileEntity_Hatch_Maintenance) aMetaTileEntity);
            }
        }
        return false;
    }

    public final boolean addMuffler(IGregTechTileEntity aTileEntity, int aBaseCasingIndex) {
        if (aTileEntity == null) {
            return false;
        } else {
            IMetaTileEntity aMetaTileEntity = aTileEntity.getMetaTileEntity();
            if (aMetaTileEntity instanceof GT_MetaTileEntity_Hatch_Muffler) {
                ((GT_MetaTileEntity_Hatch) aMetaTileEntity).updateTexture(aBaseCasingIndex);
                return this.mMufflerHatches.add((GT_MetaTileEntity_Hatch_Muffler) aMetaTileEntity);
            }
        }
        return false;
    }

    public final boolean addInputHatch(IGregTechTileEntity aTileEntity, int aBaseCasingIndex) {
        if (aTileEntity == null) {
            return false;
        } else {
            IMetaTileEntity aMetaTileEntity = aTileEntity.getMetaTileEntity();
            if (aMetaTileEntity instanceof GT_MetaTileEntity_Hatch_Input) {
                ((GT_MetaTileEntity_Hatch) aMetaTileEntity).updateTexture(aBaseCasingIndex);
                return this.mInputHatches.add((GT_MetaTileEntity_Hatch_Input) aMetaTileEntity);
            }
        }
        return false;
    }

    public final boolean addDynamoHatch(IGregTechTileEntity aTileEntity, int aBaseCasingIndex) {
        if (aTileEntity == null) {
            return false;
        } else {
            IMetaTileEntity aMetaTileEntity = aTileEntity.getMetaTileEntity();
            if (aMetaTileEntity instanceof GT_MetaTileEntity_Hatch_Dynamo) {
                ((GT_MetaTileEntity_Hatch) aMetaTileEntity).updateTexture(aBaseCasingIndex);
                return this.mDynamoHatches.add((GT_MetaTileEntity_Hatch_Dynamo) aMetaTileEntity);
            } else if (aMetaTileEntity instanceof GT_MetaTileEntity_Hatch_DynamoMulti) {
                ((GT_MetaTileEntity_Hatch) aMetaTileEntity).updateTexture(aBaseCasingIndex);
                return this.eDynamoMulti.add((GT_MetaTileEntity_Hatch_DynamoMulti) aMetaTileEntity);
            }
        }
        return false;
    }

    @Override
    public IStructureDefinition<UniversalChemicalFuelEngine> getStructure_EM() {
        if (multiDefinition == null) {
            multiDefinition = StructureDefinition.<UniversalChemicalFuelEngine>builder()
                    .addShape(mName, transpose(new String[][] {
                        {"TTTTT", "TTMMT", "TTMMT", "TTMMT", "TTMMT", "TTMMT", "TTMMT", "TTMMT", "TTTTT"},
                        {"TTTTT", "SPCCI-", "SPCCI-", "SPCCI-", "SPCCI-", "SPCCI-", "SPCCI-", "SPCCI-", "TTTTT"},
                        {"TT~TT", "SPGGI-", "SPGGI-", "SPGGI-", "SPGGI-", "SPGGI-", "SPGGI-", "SPGGI-", "TTETT"},
                        {"TTWTT", "TTTTT", "TTTTT", "TTTTT", "TTTTT", "TTTTT", "TTTTT", "TTTTT", "TTTTT"}
                    }))
                    .addElement('T', ofBlock(GregTech_API.sBlockCasings4, 2))
                    .addElement(
                            'W',
                            buildHatchAdder(UniversalChemicalFuelEngine.class)
                                    .atLeast(GT_HatchElement.Maintenance)
                                    .casingIndex(50)
                                    .dot(1)
                                    .build())
                    .addElement(
                            'M',
                            buildHatchAdder(UniversalChemicalFuelEngine.class)
                                    .atLeast(GT_HatchElement.Muffler)
                                    .casingIndex(50)
                                    .dot(2)
                                    .build())
                    .addElement(
                            'S',
                            buildHatchAdder(UniversalChemicalFuelEngine.class)
                                    .atLeast(GT_HatchElement.InputHatch)
                                    .casingIndex(50)
                                    .dot(3)
                                    .build())
                    .addElement(
                            'E',
                            buildHatchAdder(UniversalChemicalFuelEngine.class)
                                    .atLeast(GT_HatchElement.Dynamo)
                                    .casingIndex(50)
                                    .dot(4)
                                    .build())
                    .addElement('P', ofBlock(GregTech_API.sBlockCasings2, 14))
                    .addElement('C', ofBlock(Loaders.titaniumPlatedCylinder, 0))
                    .addElement('G', ofBlock(GregTech_API.sBlockCasings2, 4))
                    .addElement('I', ofBlock(GregTech_API.sBlockCasings4, 13))
                    .build();
        }
        return multiDefinition;
    }

    @Override
    public boolean checkMachine_EM(IGregTechTileEntity aBaseMetaTileEntity, ItemStack aStack) {
        return structureCheck_EM(mName, 2, 2, 0);
    }

    @Override
    public void construct(ItemStack stackSize, boolean hintsOnly) {
        structureBuild_EM(mName, 2, 2, 0, stackSize, hintsOnly);
    }

    @Override
    public String[] getStructureDescription(ItemStack itemStack) {
        return DescTextLocalization.addText("UniversalChemicalFuelEngine.hint", 11);
    }

    @Override
    public boolean isCorrectMachinePart(ItemStack aStack) {
        return true;
    }

    @Override
    public int getPollutionPerTick(ItemStack aStack) {
        return (int) Math.sqrt(this.mEUt) / 20;
    }

    @Override
    protected GT_Multiblock_Tooltip_Builder createTooltip() {
        final GT_Multiblock_Tooltip_Builder tt = new GT_Multiblock_Tooltip_Builder();
        tt.addMachineType("Chemical Engine")
                .addInfo("Controller block for the Chemical Engine")
                .addInfo("BURNING BURNING BURNING")
                .addInfo("Use combustible liquid to generate power.")
                .addInfo("You need to supply Combustion Promoter to keep it running.")
                .addInfo("This engine will consume all the fuel and combustion promoter in the hatch every second.")
                .addInfo("The efficiency is determined by the proportion of Combustion Promoter to fuel.")
                .addInfo("The proportion is bigger, and the efficiency will be higher.")
                .addInfo("It creates sqrt(Current Output Power) pollution every second")
                .addInfo("If you forget to supply Combustion Promoter, this engine will swallow all the fuel "
                        + EnumChatFormatting.YELLOW + "without outputting energy" + EnumChatFormatting.GRAY + ".")
                .addInfo("The efficiency is up to 150%.")
                .addInfo("The structure is too complex!")
                .addInfo(BLUE_PRINT_INFO)
                .addSeparator()
                .beginStructureBlock(5, 4, 9, false)
                .addMaintenanceHatch("Hint block with dot 1")
                .addMufflerHatch("Hint block with dot 2")
                .addInputHatch("Hint block with dot 3")
                .addDynamoHatch("Hint block with dot 4")
                .toolTipFinisher("Good Generator");
        return tt;
    }

    @Override
    public boolean checkRecipe_EM(ItemStack aStack) {

        ArrayList<FluidStack> tFluids = getStoredFluids();

        Collection<GT_Recipe> tDieselFuels = GT_Recipe.GT_Recipe_Map.sDieselFuels.mRecipeList;
        Collection<GT_Recipe> tGasFuels = GT_Recipe.GT_Recipe_Map.sTurbineFuels.mRecipeList;

        int PromoterAmount = findLiquidAmount(getPromoter(), tFluids);

        for (GT_Recipe recipe : tDieselFuels) {
            FluidStack tFuel = findFuel(recipe);
            if (tFuel == null) continue;
            int FuelAmount = findLiquidAmount(tFuel, tFluids);
            if (FuelAmount == 0) continue;
            calculateEfficiency(FuelAmount, PromoterAmount, DIESEL_EFFICIENCY_COEFFICIENT);

            consumeAllLiquid(tFuel);
            consumeAllLiquid(getPromoter());

            this.mEUt = (int) (FuelAmount * recipe.mSpecialValue / 20.0D);
            this.lEUt = (long) ((long) FuelAmount * recipe.mSpecialValue / 20.0D);
            this.mMaxProgresstime = 20;
            this.updateSlots();
            return true;
        }

        for (GT_Recipe recipe : tGasFuels) {
            FluidStack tFuel = findFuel(recipe);
            if (tFuel == null) continue;
            int FuelAmount = findLiquidAmount(tFuel, tFluids);
            if (FuelAmount == 0) continue;
            calculateEfficiency(FuelAmount, PromoterAmount, GAS_EFFICIENCY_COEFFICIENT);

            consumeAllLiquid(tFuel);
            consumeAllLiquid(getPromoter());

            this.mEUt = (int) (FuelAmount * recipe.mSpecialValue / 20.0D);
            this.lEUt = (long) ((long) FuelAmount * recipe.mSpecialValue / 20.0D);
            this.mMaxProgresstime = 20;
            this.updateSlots();
            return true;
        }

        if (LoadedList.GTPP) {
            Collection<GT_Recipe> tRocketFuels = GTPP_Recipe.GTPP_Recipe_Map.sRocketFuels.mRecipeList;
            for (GT_Recipe recipe : tRocketFuels) {
                FluidStack tFuel = findFuel(recipe);
                if (tFuel == null) continue;
                int FuelAmount = findLiquidAmount(tFuel, tFluids);
                if (FuelAmount == 0) continue;
                calculateEfficiency(FuelAmount, PromoterAmount, ROCKET_EFFICIENCY_COEFFICIENT);

                consumeAllLiquid(tFuel);
                consumeAllLiquid(getPromoter());

                this.mEUt = (int) (FuelAmount * recipe.mSpecialValue * 3 / 20.0D);
                this.lEUt = (long) ((long) FuelAmount * recipe.mSpecialValue * 3 / 20.0D);
                this.mMaxProgresstime = 20;
                this.updateSlots();
                return true;
            }
        }

        return false;
    }

    @Override
    public boolean onRunningTick(ItemStack stack) {
        super.onRunningTick(stack);
        if (this.getBaseMetaTileEntity().isServerSide()) {
            addAutoEnergy();
        }
        return true;
    }

    @Override
    public String[] getInfoData() {
        String[] info = super.getInfoData();
        info[4] = "Probably makes: " + EnumChatFormatting.RED + this.lEUt + EnumChatFormatting.RESET + " EU/t";
        info[6] = "Problems: " + EnumChatFormatting.RED + (this.getIdealStatus() - this.getRepairStatus())
                + EnumChatFormatting.RESET + " Efficiency: " + EnumChatFormatting.YELLOW + tEff / 100D
                + EnumChatFormatting.RESET + " %";
        return info;
    }

    void addAutoEnergy() {
        long exEU = lEUt * tEff / 10000;
        if (!mDynamoHatches.isEmpty()) {
            GT_MetaTileEntity_Hatch_Dynamo tHatch = mDynamoHatches.get(0);
            if (tHatch.maxEUOutput() * tHatch.maxAmperesOut() >= exEU) {
                tHatch.setEUVar(Math.min(
                        tHatch.maxEUStore(), tHatch.getBaseMetaTileEntity().getStoredEU() + exEU));
            } else tHatch.doExplosion(tHatch.maxEUOutput());
        }
        if (!eDynamoMulti.isEmpty()) {
            GT_MetaTileEntity_Hatch_DynamoMulti tHatch = eDynamoMulti.get(0);
            if (tHatch.maxEUOutput() * tHatch.maxAmperesOut() >= exEU) {
                tHatch.setEUVar(Math.min(
                        tHatch.maxEUStore(), tHatch.getBaseMetaTileEntity().getStoredEU() + exEU));
            } else tHatch.doExplosion(tHatch.maxEUOutput());
        }
    }

    public FluidStack getPromoter() {
        return FluidRegistry.getFluidStack("combustionpromotor", 1);
    }

    public FluidStack findFuel(GT_Recipe aFuel) {
        if (aFuel.mInputs != null && aFuel.mInputs.length > 0)
            return GT_Utility.getFluidForFilledItem(aFuel.mInputs[0], true);
        else return aFuel.mFluidInputs[0];
    }

    public void calculateEfficiency(int aFuel, int aPromoter, double coefficient) {
        if (aPromoter == 0) {
            this.tEff = 0;
            return;
        }
        this.tEff = (int) (Math.exp(-coefficient * (double) aFuel / (double) aPromoter) * EFFICIENCY_CEILING * 10000);
    }

    public int findLiquidAmount(FluidStack liquid, List<FluidStack> input) {
        int cnt = 0;
        for (FluidStack fluid : input) {
            if (fluid.isFluidEqual(liquid)) {
                cnt += fluid.amount;
            }
        }
        if (cnt < 0) cnt = 0;
        return cnt;
    }

    public void consumeAllLiquid(FluidStack liquid) {
        for (GT_MetaTileEntity_Hatch_Input tHatch : mInputHatches) {
            if (isValidMetaTileEntity(tHatch)) {
                FluidStack tLiquid = tHatch.getFluid();
                if (tLiquid != null && tLiquid.isFluidEqual(liquid)) {
                    tHatch.drain(tLiquid.amount, true);
                }
            }
        }
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
                    casingTexturePages[0][50],
                    TextureFactory.of(OVERLAY_FRONT_DIESEL_ENGINE_ACTIVE),
                    TextureFactory.builder()
                            .addIcon(OVERLAY_FRONT_DIESEL_ENGINE_ACTIVE_GLOW)
                            .glow()
                            .build()
                };
            return new ITexture[] {
                casingTexturePages[0][50],
                TextureFactory.of(OVERLAY_FRONT_DIESEL_ENGINE),
                TextureFactory.builder()
                        .addIcon(OVERLAY_FRONT_DIESEL_ENGINE_GLOW)
                        .glow()
                        .build()
            };
        }
        return new ITexture[] {casingTexturePages[0][50]};
    }

    @Override
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new UniversalChemicalFuelEngine(this.mName);
    }

    @Override
    public int survivalConstruct(ItemStack stackSize, int elementBudget, IItemSource source, EntityPlayerMP actor) {
        if (mMachine) return -1;
        return survivialBuildPiece(mName, stackSize, 2, 2, 0, elementBudget, source, actor, false, true);
    }
}
