package gtPlusPlus.xmod.gregtech.common.tileentities.machines.multi.production;

import static com.gtnewhorizon.structurelib.structure.StructureUtility.onElementPass;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.transpose;
import static gregtech.api.enums.HatchElement.Dynamo;
import static gregtech.api.enums.HatchElement.InputHatch;
import static gregtech.api.enums.HatchElement.Maintenance;
import static gregtech.api.enums.HatchElement.Muffler;
import static gregtech.api.util.GTStructureUtility.buildHatchAdder;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StatCollector;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.FluidStack;

import org.jetbrains.annotations.NotNull;

import com.gtnewhorizon.structurelib.alignment.constructable.ISurvivalConstructable;
import com.gtnewhorizon.structurelib.structure.IStructureDefinition;
import com.gtnewhorizon.structurelib.structure.ISurvivalBuildEnvironment;
import com.gtnewhorizon.structurelib.structure.StructureDefinition;

import gregtech.api.casing.Casings;
import gregtech.api.enums.Materials;
import gregtech.api.enums.Textures;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.ICasingTextureProvider;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.implementations.MTEExtendedPowerMultiBlockBase;
import gregtech.api.recipe.RecipeMap;
import gregtech.api.recipe.RecipeMaps;
import gregtech.api.recipe.check.CheckRecipeResult;
import gregtech.api.recipe.check.CheckRecipeResultRegistry;
import gregtech.api.recipe.check.SimpleCheckRecipeResult;
import gregtech.api.structure.error.StructureError;
import gregtech.api.util.GTRecipe;
import gregtech.api.util.GTUtility;
import gregtech.api.util.MultiblockTooltipBuilder;
import gregtech.common.pollution.PollutionConfig;

@IMetaTileEntity.SkipGenerateDescription
public class MTELargeSemifluidGenerator extends MTEExtendedPowerMultiBlockBase<MTELargeSemifluidGenerator>
    implements ISurvivalConstructable, ICasingTextureProvider {

    private int fuelConsumption = 0;
    private int fuelValue = 0;
    private long fuelRemaining = 0;
    private boolean boostEu = false;
    private FluidStack lubricant;
    private FluidStack oxygen;

    // spotless:off
    private static final String[][] structure = {
        { "III", "CCC", "CCC", "CCC" },
        { "I~I", "CGC", "CGC", "CMC" },
        { "III", "CCC", "CCC", "CCC" }, };
    // spotless:on
    private int casingAmount;
    private static final String STRUCTURE_PIECE_MAIN = "main";
    private static IStructureDefinition<MTELargeSemifluidGenerator> STRUCTURE_DEFINITION = null;

    public MTELargeSemifluidGenerator(int aID, String aName, String aNameRegional) {
        super(aID, aName, aNameRegional);
        setFluids();
    }

    public MTELargeSemifluidGenerator(String aName) {
        super(aName);
        setFluids();
    }

    private void setFluids() {
        if (lubricant == null) {
            lubricant = Materials.Lubricant.getFluid(0L);
        }
        if (oxygen == null) {
            oxygen = Materials.Oxygen.getGas(0L);
        }
    }

    @Override
    protected MultiblockTooltipBuilder createTooltip() {
        MultiblockTooltipBuilder tt = new MultiblockTooltipBuilder();
        tt.addMachineType(StatCollector.translateToLocal("gt.mbtt.machine_type.semifluid_generator"))
            .addMarkdown(new ResourceLocation("gregtech", "large-semifluid-generator"))
            .addPollutionAmount(getPollutionPerSecond(null))
            .beginStructureBlock(3, 3, 4, false)
            .addController(StatCollector.translateToLocal("gt.mbtt.structure.front_center_2nd_layer"))
            .addCasing("16-21", Casings.StableTitaniumMachineCasing.getLocalizedName(), false)
            .addCasing("8", Casings.EngineIntakeCasing.getLocalizedName(), false)
            .addCasing("2", Casings.SteelGearBoxCasing.getLocalizedName(), false)
            .addDynamoHatch("1", StatCollector.translateToLocal("gt.mbtt.structure.back_center_casing"), 2)
            .addMaintenanceHatch("1", StatCollector.translateToLocal("gt.mbtt.structure.any_machine_casing"), 1)
            .addMufflerHatch("1", StatCollector.translateToLocal("gt.mbtt.structure.any_machine_casing"), 1)
            .addInputHatch("1+", StatCollector.translateToLocal("gt.mbtt.structure.any_machine_casing"), 1)
            .addStructureInfo("")
            .addStructureFooter(StatCollector.translateToLocal("GT5U.MBTT.Structure.DynamoLimit"))
            .toolTipFinisher();
        return tt;
    }

    @Override
    public ITexture[] getTexture(IGregTechTileEntity baseMetaTileEntity, ForgeDirection side, ForgeDirection facing,
        int colorIndex, boolean active, boolean redstoneLevel) {
        return Textures.BlockIcons.createTextureWithCasing(
            this,
            side,
            facing,
            active,
            Textures.BlockIcons.OVERLAY_FRONT_COMBUSTION_ENGINE,
            Textures.BlockIcons.OVERLAY_FRONT_COMBUSTION_ENGINE_GLOW,
            Textures.BlockIcons.OVERLAY_FRONT_COMBUSTION_ENGINE_ACTIVE,
            Textures.BlockIcons.OVERLAY_FRONT_COMBUSTION_ENGINE_ACTIVE_GLOW);
    }

    @Override
    public ITexture getCasingTexture() {
        return Casings.StableTitaniumMachineCasing.getCasingTexture();
    }

    @Override
    public boolean supportsPowerPanel() {
        return false;
    }

    @Override
    public boolean isCorrectMachinePart(ItemStack aStack) {
        return getMaxEfficiency(aStack) > 0;
    }

    @Override
    public @NotNull Collection<RecipeMap<?>> getAvailableRecipeMaps() {
        return Collections.singletonList(RecipeMaps.semiFluidFuels);
    }

    @Override
    public @NotNull CheckRecipeResult checkProcessing() {
        ArrayList<FluidStack> fluids = getStoredFluids();

        // Check for lubricant and oxygen first, so we can compute costs ahead of time.
        // This will allow us to check costs without needing to actually try to deplete fluids
        // (wasting earlier fluids in the check if later fluids turn out to be insufficient).
        GTUtility.setFluidAmount(lubricant, 0);
        GTUtility.setFluidAmount(oxygen, 0);
        for (FluidStack hatchFluid : fluids) {
            if (hatchFluid.isFluidEqual(lubricant)) {
                GTUtility.setFluidAmount(
                    lubricant,
                    Math.max(GTUtility.getFluidAmount(lubricant), GTUtility.getFluidAmount(hatchFluid)));
            } else if (hatchFluid.isFluidEqual(oxygen)) {
                GTUtility.setFluidAmount(
                    oxygen,
                    Math.max(GTUtility.getFluidAmount(oxygen), GTUtility.getFluidAmount(hatchFluid)));
            }
        }
        boostEu = GTUtility.getFluidAmount(oxygen) >= 4L;
        long lubricantCost = boostEu ? 2L : 1L;
        if (GTUtility.getFluidAmount(lubricant) < lubricantCost) {
            return SimpleCheckRecipeResult.ofFailure("no_lubricant");
        }

        for (FluidStack hatchFluid : fluids) { // Loops through hatches
            GTRecipe fuel = RecipeMaps.semiFluidFuels.getBackend()
                .findFuel(hatchFluid);
            if (fuel == null) {
                // Not a valid semifluid fuel.
                continue;
            }

            int newEUt = boostEu ? 4096 : 2048;
            fuelConsumption = newEUt / fuel.mSpecialValue; // Calc fuel consumption
            FluidStack fuelStack = new FluidStack(hatchFluid.getFluid(), fuelConsumption);
            if (depleteInput(fuelStack)) { // Deplete that amount
                // We checked beforehand, so both of these depletions should succeed.
                // But check the return values anyway just to be safe.
                if (boostEu) {
                    if (!depleteInput(Materials.Oxygen.getGas(4L))) {
                        return SimpleCheckRecipeResult.ofFailure("no_oxygen");
                    }
                }
                // Deplete Lubricant. 2000L should = 1 hour of runtime (if baseEU = 2048)
                if (mRuntime % 72 == 0 || mRuntime == 0) {
                    if (!depleteInput(Materials.Lubricant.getFluid(lubricantCost))) {
                        return SimpleCheckRecipeResult.ofFailure("no_lubricant");
                    }
                }

                fuelValue = fuel.mSpecialValue;
                fuelRemaining = GTUtility.getFluidAmount(hatchFluid); // Record available fuel
                this.lEUt = mEfficiency < 2000 ? 0 : newEUt; // Output 0 if startup is less than 20%
                this.mProgresstime = 1;
                this.mMaxProgresstime = 1;
                this.mEfficiencyIncrease = 15;
                return CheckRecipeResultRegistry.GENERATING;
            }
        }

        this.lEUt = 0;
        this.mEfficiency = 0;
        return CheckRecipeResultRegistry.NO_FUEL_FOUND;
    }

    @Override
    public IStructureDefinition<MTELargeSemifluidGenerator> getStructureDefinition() {
        if (STRUCTURE_DEFINITION == null) {
            STRUCTURE_DEFINITION = StructureDefinition.<MTELargeSemifluidGenerator>builder()
                .addShape(STRUCTURE_PIECE_MAIN, transpose(structure))
                .addElement(
                    'C',
                    buildHatchAdder(MTELargeSemifluidGenerator.class).atLeast(Muffler, InputHatch, Maintenance)
                        .casingIndex(Casings.StableTitaniumMachineCasing.textureId)
                        .hint(1)
                        .buildAndChain(
                            onElementPass(x -> ++x.casingAmount, Casings.StableTitaniumMachineCasing.asElement())))
                .addElement('G', Casings.SteelGearBoxCasing.asElement())
                .addElement('I', Casings.EngineIntakeCasing.asElement())
                .addElement('M', Dynamo.newAny(Casings.StableTitaniumMachineCasing.textureId, 2))
                .build();
        }
        return STRUCTURE_DEFINITION;
    }

    @Override
    public void construct(ItemStack stackSize, boolean hintsOnly) {
        buildPiece(STRUCTURE_PIECE_MAIN, stackSize, hintsOnly, 1, 1, 0);
    }

    @Override
    public int survivalConstruct(ItemStack stackSize, int elementBudget, ISurvivalBuildEnvironment env) {
        if (mMachine) return -1;
        return survivalBuildPiece(STRUCTURE_PIECE_MAIN, stackSize, 1, 1, 0, elementBudget, env, false, true);
    }

    @Override
    public void checkMachine(IGregTechTileEntity aBaseMetaTileEntity, ItemStack aStack, List<StructureError> errors) {
        casingAmount = 0;
        if (!checkPiece(STRUCTURE_PIECE_MAIN, 1, 1, 0, errors)) return;
        checkCasingMin(errors, casingAmount, 16);
        checkHasMaintenanceHatch(errors);
        checkHasMufflerHatch(errors);
        checkHasInputHatch(errors);
    }

    @Override
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new MTELargeSemifluidGenerator(this.mName);
    }

    @Override
    public void saveNBTData(NBTTagCompound aNBT) {
        super.saveNBTData(aNBT);
        aNBT.setInteger("mEfficiency", mEfficiency);
        aNBT.setBoolean("boostEu", boostEu);
        aNBT.setInteger("fuelConsumption", fuelConsumption);
        aNBT.setInteger("fuelValue", fuelValue);
        aNBT.setLong("fuelRemaining", fuelRemaining);
    }

    @Override
    public void loadNBTData(NBTTagCompound aNBT) {
        super.loadNBTData(aNBT);
        mEfficiency = aNBT.getInteger("mEfficiency");
        boostEu = aNBT.getBoolean("boostEu");
        fuelConsumption = aNBT.getInteger("fuelConsumption");
        fuelValue = aNBT.getInteger("fuelValue");
        fuelRemaining = aNBT.getLong("fuelRemaining");
    }

    @Override
    public int getMaxEfficiency(ItemStack aStack) {
        return boostEu ? 15000 : 10000;
    }

    @Override
    public int getPollutionPerSecond(ItemStack aStack) {
        return PollutionConfig.pollutionPerSecondMultiLargeSemiFluidGenerator;
    }

    @Override
    public int getMaxParallelRecipes() {
        return 0;
    }

    @Override
    public boolean showRecipeTextInGUI() {
        return false;
    }

    @Override
    public boolean supportsSingleRecipeLocking() {
        return false;
    }
}
