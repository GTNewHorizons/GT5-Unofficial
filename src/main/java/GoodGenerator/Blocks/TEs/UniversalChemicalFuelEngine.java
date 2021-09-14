package GoodGenerator.Blocks.TEs;

import GoodGenerator.Loader.Loaders;
import GoodGenerator.util.DescTextLocalization;
import com.github.bartimaeusnek.crossmod.tectech.TecTechEnabledMulti;
import com.github.technus.tectech.thing.metaTileEntity.hatch.GT_MetaTileEntity_Hatch_EnergyMulti;
import com.github.technus.tectech.thing.metaTileEntity.hatch.GT_MetaTileEntity_Hatch_EnergyTunnel;
import com.github.technus.tectech.thing.metaTileEntity.multi.base.GT_MetaTileEntity_MultiblockBase_EM;
import com.gtnewhorizon.structurelib.alignment.constructable.IConstructable;
import com.gtnewhorizon.structurelib.structure.IStructureDefinition;
import com.gtnewhorizon.structurelib.structure.StructureDefinition;
import gregtech.api.GregTech_API;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.implementations.*;
import gregtech.api.render.TextureFactory;
import gregtech.api.util.GT_Multiblock_Tooltip_Builder;
import gregtech.api.util.GT_Recipe;
import gregtech.api.util.GT_Utility;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import org.lwjgl.input.Keyboard;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static GoodGenerator.util.DescTextLocalization.BLUE_PRINT_INFO;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.*;
import static gregtech.api.enums.Textures.BlockIcons.*;
import static gregtech.api.util.GT_StructureUtility.ofHatchAdder;

public class UniversalChemicalFuelEngine extends GT_MetaTileEntity_MultiblockBase_EM implements TecTechEnabledMulti, IConstructable {

    protected final double DIESEL_EFFICIENCY_COEFFICIENT = 0.02D;
    protected final double GAS_EFFICIENCY_COEFFICIENT = 0.01D;
    protected final double EFFICIENCY_CEILING = 1.5D;

    private IStructureDefinition<UniversalChemicalFuelEngine> multiDefinition = null;

    public UniversalChemicalFuelEngine(String name){super(name);}

    public UniversalChemicalFuelEngine(int id, String name, String nameRegional){
        super(id,name,nameRegional);
    }

    public final boolean addMaintenance(IGregTechTileEntity aTileEntity, int aBaseCasingIndex){
        if (aTileEntity == null) {
            return false;
        } else {
            IMetaTileEntity aMetaTileEntity = aTileEntity.getMetaTileEntity();
            if (aMetaTileEntity instanceof GT_MetaTileEntity_Hatch_Maintenance){
                ((GT_MetaTileEntity_Hatch)aMetaTileEntity).updateTexture(aBaseCasingIndex);
                return this.mMaintenanceHatches.add((GT_MetaTileEntity_Hatch_Maintenance)aMetaTileEntity);
            }
        }
        return false;
    }

    public final boolean addMuffler(IGregTechTileEntity aTileEntity, int aBaseCasingIndex){
        if (aTileEntity == null) {
            return false;
        } else {
            IMetaTileEntity aMetaTileEntity = aTileEntity.getMetaTileEntity();
            if (aMetaTileEntity instanceof GT_MetaTileEntity_Hatch_Muffler){
                ((GT_MetaTileEntity_Hatch)aMetaTileEntity).updateTexture(aBaseCasingIndex);
                return this.mMufflerHatches.add((GT_MetaTileEntity_Hatch_Muffler)aMetaTileEntity);
            }
        }
        return false;
    }

    public final boolean addInputHatch(IGregTechTileEntity aTileEntity, int aBaseCasingIndex){
        if (aTileEntity == null) {
            return false;
        } else {
            IMetaTileEntity aMetaTileEntity = aTileEntity.getMetaTileEntity();
            if (aMetaTileEntity instanceof GT_MetaTileEntity_Hatch_Input){
                ((GT_MetaTileEntity_Hatch)aMetaTileEntity).updateTexture(aBaseCasingIndex);
                return this.mInputHatches.add((GT_MetaTileEntity_Hatch_Input)aMetaTileEntity);
            }
        }
        return false;
    }

    public final boolean addDynamoHatch(IGregTechTileEntity aTileEntity, int aBaseCasingIndex){
        if (aTileEntity == null) {
            return false;
        } else {
            IMetaTileEntity aMetaTileEntity = aTileEntity.getMetaTileEntity();
            if (aMetaTileEntity instanceof GT_MetaTileEntity_Hatch_Dynamo){
                ((GT_MetaTileEntity_Hatch)aMetaTileEntity).updateTexture(aBaseCasingIndex);
                return this.mDynamoHatches.add((GT_MetaTileEntity_Hatch_Dynamo)aMetaTileEntity);
            }
        }
        return false;
    }

    @Override
    public IStructureDefinition<UniversalChemicalFuelEngine> getStructure_EM(){
        if (multiDefinition == null){
            multiDefinition = StructureDefinition
                    .<UniversalChemicalFuelEngine>builder()
                    .addShape(mName,
                            transpose(new String[][]{
                                    {"TTTTT","TTMMT","TTMMT","TTMMT","TTMMT","TTMMT","TTMMT","TTMMT","TTTTT"},
                                    {"TTTTT","SPCCI-","SPCCI-","SPCCI-","SPCCI-","SPCCI-","SPCCI-","SPCCI-","TTTTT"},
                                    {"TT~TT","SPGGI-","SPGGI-","SPGGI-","SPGGI-","SPGGI-","SPGGI-","SPGGI-","TTETT"},
                                    {"TTWTT","TTTTT","TTTTT","TTTTT","TTTTT","TTTTT","TTTTT","TTTTT","TTTTT"}
                            })
                    ).addElement(
                            'T',
                            ofBlock(
                                    GregTech_API.sBlockCasings4, 2
                            )
                    ).addElement(
                            'W',
                            ofHatchAdder(
                                    UniversalChemicalFuelEngine::addMaintenance, 50,
                                     1
                            )
                    ).addElement(
                            'M',
                            ofHatchAdder(
                                    UniversalChemicalFuelEngine::addMuffler, 50,
                                    2
                            )
                    ).addElement(
                            'S',
                            ofHatchAdder(
                                    UniversalChemicalFuelEngine::addInputHatch, 50,
                                    3
                            )
                    ).addElement(
                            'E',
                            ofHatchAdder(
                                    UniversalChemicalFuelEngine::addDynamoHatch, 50,
                                    4
                            )
                    ).addElement(
                            'P',
                            ofBlock(
                                    GregTech_API.sBlockCasings2, 14
                            )
                    ).addElement(
                            'C',
                            ofBlock(
                                    Loaders.titaniumPlatedCylinder, 0
                            )
                    ).addElement(
                            'G',
                            ofBlock(
                                    GregTech_API.sBlockCasings2, 4
                            )
                    ).addElement(
                            'I',
                            ofBlock(
                                    GregTech_API.sBlockCasings4, 13
                            )
                    )
                    .build();
        }
        return multiDefinition;
    }

    @Override
    public boolean checkMachine_EM(IGregTechTileEntity aBaseMetaTileEntity, ItemStack aStack) {
        return structureCheck_EM(mName, 2, 2, 0);
    }

    @Override
    public void construct(ItemStack itemStack, boolean b) {
        structureBuild_EM(mName, 2, 2, 0, b, itemStack);
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
        return (int)Math.sqrt(this.mEUt) / 20;
    }

    @Override
    public String[] getDescription(){
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
                .addInfo("If you forget to supply Combustion Promoter, this engine will swallow all the fuel " + EnumChatFormatting.YELLOW + "without outputting energy" + EnumChatFormatting.GRAY + ".")
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
        if (!Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)) {
            return tt.getInformation();
        } else {
            return tt.getStructureInformation();
        }
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

            this.mEUt = (int)(FuelAmount * recipe.mSpecialValue / 20.0D);
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

            this.mEUt = (int)(FuelAmount * recipe.mSpecialValue / 20.0D);
            this.mMaxProgresstime = 20;
            this.updateSlots();
            return true;
        }

        return false;
    }

    public FluidStack getPromoter() {
        return FluidRegistry.getFluidStack("combustionpromotor", 1);
    }

    public FluidStack findFuel(GT_Recipe aFuel) {
        return GT_Utility.getFluidForFilledItem(aFuel.mInputs[0], true);
    }

    public void calculateEfficiency(int aFuel, int aPromoter, double coefficient){
        if (aPromoter == 0){
            this.mEfficiency = 0;
            return;
        }
        this.mEfficiency = (int)(Math.exp(-coefficient * (double)aFuel / (double)aPromoter) * EFFICIENCY_CEILING * 10000);
    }

    public int findLiquidAmount(FluidStack liquid, List<FluidStack> input) {
        int cnt = 0;
        for (FluidStack fluid : input){
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
    public int getMaxEfficiency(ItemStack aStack) {
        return (int)(10000 * EFFICIENCY_CEILING);
    }

    @Override
    public ITexture[] getTexture(IGregTechTileEntity aBaseMetaTileEntity, byte aSide, byte aFacing, byte aColorIndex, boolean aActive, boolean aRedstone){
        if(aSide == aFacing){
            if(aActive) return new ITexture[]{
                    casingTexturePages[0][50],
                    TextureFactory.of(OVERLAY_FRONT_DIESEL_ENGINE_ACTIVE),
                    TextureFactory.builder().addIcon(OVERLAY_FRONT_DIESEL_ENGINE_ACTIVE_GLOW).glow().build()
            };
            return new ITexture[]{
                    casingTexturePages[0][50],
                    TextureFactory.of(OVERLAY_FRONT_DIESEL_ENGINE),
                    TextureFactory.builder().addIcon(OVERLAY_FRONT_DIESEL_ENGINE_GLOW).glow().build()
            };
        }
        return new ITexture[]{casingTexturePages[0][50]};
    }

    @Override
    public List<GT_MetaTileEntity_Hatch_Energy> getVanillaEnergyHatches() {
        return this.mEnergyHatches;
    }

    @Override
    public List<GT_MetaTileEntity_Hatch_EnergyTunnel> getTecTechEnergyTunnels() {
        return new ArrayList<>();
    }

    @Override
    public List<GT_MetaTileEntity_Hatch_EnergyMulti> getTecTechEnergyMultis() {
        return new ArrayList<>();
    }

    @Override
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new UniversalChemicalFuelEngine(this.mName);
    }
}
