package kekztech.common.tileentities;

import static com.gtnewhorizon.gtnhlib.util.numberformatting.NumberFormatUtil.formatNumber;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.ofBlock;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.ofBlockAnyMeta;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.ofChain;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.onElementPass;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.transpose;
import static gregtech.api.enums.HatchElement.Dynamo;
import static gregtech.api.enums.HatchElement.InputHatch;
import static gregtech.api.enums.HatchElement.Maintenance;
import static gregtech.api.enums.HatchElement.OutputHatch;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_HEAT_EXCHANGER;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_HEAT_EXCHANGER_ACTIVE;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_HEAT_EXCHANGER_ACTIVE_GLOW;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_HEAT_EXCHANGER_GLOW;
import static gregtech.api.util.GTStructureUtility.buildHatchAdder;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.annotation.Nonnull;

import net.minecraft.item.ItemStack;
import net.minecraft.util.StatCollector;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.FluidStack;

import com.gtnewhorizon.structurelib.alignment.constructable.ISurvivalConstructable;
import com.gtnewhorizon.structurelib.structure.IStructureDefinition;
import com.gtnewhorizon.structurelib.structure.ISurvivalBuildEnvironment;
import com.ruling_0.materiallib.api.MaterialLibAPI;

import gregtech.api.GregTechAPI;
import gregtech.api.casing.Casings;
import gregtech.api.enums.Materials;
import gregtech.api.enums.Textures;
import gregtech.api.enums.materials2.Materials2FluidShapes;
import gregtech.api.enums.materials2.Materials2Materials;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.ICasingTextureProvider;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.implementations.MTEEnhancedMultiBlockBase;
import gregtech.api.recipe.RecipeMaps;
import gregtech.api.recipe.check.CheckRecipeResult;
import gregtech.api.recipe.check.CheckRecipeResultRegistry;
import gregtech.api.structure.error.StructureError;
import gregtech.api.util.GTRecipe;
import gregtech.api.util.GTUtility;
import gregtech.api.util.MultiblockTooltipBuilder;
import kekztech.common.Blocks;

public class MTESOFuelCellMK1 extends MTEEnhancedMultiBlockBase<MTESOFuelCellMK1>
    implements ISurvivalConstructable, ICasingTextureProvider {

    private final int OXYGEN_PER_SEC = 100;
    private final int EU_PER_TICK = 2048;
    private final int STEAM_PER_SEC = 20000;

    public MTESOFuelCellMK1(int aID, String aName, String aNameRegional) {
        super(aID, aName, aNameRegional);
    }

    public MTESOFuelCellMK1(String aName) {
        super(aName);
    }

    @Override
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity var1) {
        return new MTESOFuelCellMK1(super.mName);
    }

    @Override
    public boolean supportsPowerPanel() {
        return false;
    }

    private int mCasing = 0;

    private static final int CASING_TEXTURE_ID = 49;
    private static final String STRUCTURE_PIECE_MAIN = "main";
    private static final IStructureDefinition<MTESOFuelCellMK1> STRUCTURE_DEFINITION = IStructureDefinition
        .<MTESOFuelCellMK1>builder()
        .addShape(
            STRUCTURE_PIECE_MAIN,
            transpose(
                new String[][] { { "ccc", "ccc", "ccc", "ccc", "ccc" }, { "c~c", "geg", "geg", "geg", "cdc" },
                    { "ccc", "ccc", "ccc", "ccc", "ccc" } }))
        .addElement(
            'c',
            ofChain(
                buildHatchAdder(MTESOFuelCellMK1.class).atLeast(InputHatch, InputHatch, OutputHatch, Maintenance)
                    .hint(1)
                    .casingIndex(CASING_TEXTURE_ID)
                    .build(),
                onElementPass(te -> te.mCasing++, ofBlock(GregTechAPI.sBlockCasings4, 1))))
        .addElement('d', Dynamo.newAny(CASING_TEXTURE_ID, 2))
        .addElement('g', Casings.ReinforcedGlass.asElement())
        .addElement('e', ofBlockAnyMeta(Blocks.yszUnit))
        .build();

    @Override
    public IStructureDefinition<MTESOFuelCellMK1> getStructureDefinition() {
        return STRUCTURE_DEFINITION;
    }

    @Override
    protected MultiblockTooltipBuilder createTooltip() {
        final MultiblockTooltipBuilder tt = new MultiblockTooltipBuilder();
        tt.addMachineType("Gas Turbine, SOFC")
            .addInfo("Oxidizes gas fuels to generate electricity without polluting the environment")
            .addInfo(
                "Consumes up to " + formatNumber(EU_PER_TICK * 20)
                    + "EU worth of fuel with up to 100% efficiency each second")
            .addInfo("Steam production requires the SOFC to heat up completely first")
            .addInfo("Outputs " + EU_PER_TICK + "EU/t and " + STEAM_PER_SEC + "L/s Steam")
            .addInfo("Additionally, requires " + OXYGEN_PER_SEC + "L/s Oxygen gas")
            .beginStructureBlock(5, 3, 3, false)
            .addController("Front center")
            .addCasing("12-31", "Clean Stainless Steel Machine Casing", false)
            .addCasing("6", "Reinforced Glass", false)
            .addCasing("3", "YSZ Ceramic Electrolyte Unit", false)
            .addDynamoHatch("1", "Back center casing", 2)
            .addMaintenanceHatch("1", "Any casing", 1)
            .addInputHatch("1+", "Any casing", 1)
            .addOutputHatch("1+", "Any casing", 1)
            .addStructureInfo("")
            .addStructureFooter(StatCollector.translateToLocal("GT5U.MBTT.Structure.DynamoLimit"))
            .toolTipFinisher();
        return tt;
    }

    @Override
    public ITexture[] getTexture(IGregTechTileEntity aBaseMetaTileEntity, ForgeDirection side, ForgeDirection aFacing,
        int colorIndex, boolean aActive, boolean redstoneLevel) {
        return Textures.BlockIcons.createTextureWithCasing(
            this,
            side,
            aFacing,
            aActive,
            OVERLAY_FRONT_HEAT_EXCHANGER,
            OVERLAY_FRONT_HEAT_EXCHANGER_GLOW,
            OVERLAY_FRONT_HEAT_EXCHANGER_ACTIVE,
            OVERLAY_FRONT_HEAT_EXCHANGER_ACTIVE_GLOW);
    }

    @Override
    public ITexture getCasingTexture() {
        return Textures.BlockIcons.getCasingTextureForId(CASING_TEXTURE_ID);
    }

    @Nonnull
    @Override
    public CheckRecipeResult checkProcessing() {
        final ArrayList<FluidStack> storedFluids = super.getStoredFluids();
        Collection<GTRecipe> recipeList = RecipeMaps.gasTurbineFuels.getAllRecipes();

        for (FluidStack hatchFluid : storedFluids) {
            for (GTRecipe aFuel : recipeList) {
                FluidStack liquid;
                if ((liquid = GTUtility.getFluidForFilledItem(aFuel.getRepresentativeInput(0), true)) != null
                    && hatchFluid.isFluidEqual(liquid)) {

                    liquid.amount = (EU_PER_TICK * 20) / aFuel.mSpecialValue;

                    if (super.depleteInput(liquid)) {

                        if (!super.depleteInput(
                            MaterialLibAPI.getFluidStack(
                                Materials2Materials.Oxygen,
                                Materials2FluidShapes.shapeFluidGas,
                                (int) (OXYGEN_PER_SEC)))) {
                            super.mEUt = 0;
                            super.mEfficiency = 0;
                            return CheckRecipeResultRegistry.NO_FUEL_FOUND;
                        }

                        super.mEUt = EU_PER_TICK;
                        super.mMaxProgresstime = 20;
                        super.mEfficiencyIncrease = 40;
                        if (super.mEfficiency == getMaxEfficiency(null)) {
                            super.addOutputPartial(Materials.Steam.getGas(STEAM_PER_SEC));
                        }
                        return CheckRecipeResultRegistry.GENERATING;
                    }
                }
            }
        }

        super.mEUt = 0;
        super.mEfficiency = 0;
        return CheckRecipeResultRegistry.NO_FUEL_FOUND;
    }

    @Override
    public void checkMachine(IGregTechTileEntity thisController, ItemStack guiSlotItem, List<StructureError> errors) {
        this.mCasing = 0;
        if (!checkPiece(STRUCTURE_PIECE_MAIN, 1, 1, 0, errors)) return;
        checkCasingMin(errors, this.mCasing, 12);
        checkHasMaintenanceHatch(errors);
        checkHasInputHatch(errors);
        checkHasOutputHatch(errors);
    }

    @Override
    public void construct(ItemStack itemStack, boolean b) {
        buildPiece(STRUCTURE_PIECE_MAIN, itemStack, b, 1, 1, 0);
    }

    @Override
    public int survivalConstruct(ItemStack stackSize, int elementBudget, ISurvivalBuildEnvironment env) {
        return survivalBuildPiece(STRUCTURE_PIECE_MAIN, stackSize, 1, 1, 0, elementBudget, env, false, true);
    }

    @Override
    public boolean supportsSingleRecipeLocking() {
        return false;
    }

    @Override
    public boolean showRecipeTextInGUI() {
        return false;
    }
}
