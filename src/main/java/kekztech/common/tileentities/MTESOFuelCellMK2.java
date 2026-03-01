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

import javax.annotation.Nonnull;

import net.minecraft.item.ItemStack;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;

import com.gtnewhorizon.structurelib.alignment.constructable.ISurvivalConstructable;
import com.gtnewhorizon.structurelib.structure.IStructureDefinition;
import com.gtnewhorizon.structurelib.structure.ISurvivalBuildEnvironment;

import cpw.mods.fml.common.registry.GameRegistry;
import gregtech.api.GregTechAPI;
import gregtech.api.enums.Materials;
import gregtech.api.enums.Textures;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.implementations.MTEEnhancedMultiBlockBase;
import gregtech.api.recipe.RecipeMaps;
import gregtech.api.recipe.check.CheckRecipeResult;
import gregtech.api.recipe.check.CheckRecipeResultRegistry;
import gregtech.api.render.TextureFactory;
import gregtech.api.util.GTRecipe;
import gregtech.api.util.GTUtility;
import gregtech.api.util.MultiblockTooltipBuilder;
import kekztech.common.Blocks;

public class MTESOFuelCellMK2 extends MTEEnhancedMultiBlockBase<MTESOFuelCellMK2> implements ISurvivalConstructable {

    private final int OXYGEN_PER_SEC = 2000;
    private final int EU_PER_TICK = 24576; // 100% Efficiency, 3A IV
    private final int STEAM_PER_SEC = 96000; // SH Steam (10,800EU/t @ 150% Efficiency)

    public MTESOFuelCellMK2(int aID, String aName, String aNameRegional) {
        super(aID, aName, aNameRegional);
    }

    public MTESOFuelCellMK2(String aName) {
        super(aName);
    }

    @Override
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity var1) {
        return new MTESOFuelCellMK2(super.mName);
    }

    @Override
    public boolean supportsPowerPanel() {
        return false;
    }

    private int mCasing = 0;

    private static final int CASING_TEXTURE_ID = 48;
    private static final String STRUCTURE_PIECE_MAIN = "main";
    private static final IStructureDefinition<MTESOFuelCellMK2> STRUCTURE_DEFINITION = IStructureDefinition
        .<MTESOFuelCellMK2>builder()
        .addShape(
            STRUCTURE_PIECE_MAIN,
            transpose(
                new String[][] { { "ccc", "ccc", "ccc", "ccc", "ccc" }, { "c~c", "geg", "geg", "geg", "cdc" },
                    { "ccc", "ccc", "ccc", "ccc", "ccc" } }))
        .addElement(
            'c',
            ofChain(
                buildHatchAdder(MTESOFuelCellMK2.class).atLeast(InputHatch, InputHatch, OutputHatch, Maintenance)
                    .hint(1)
                    .casingIndex(CASING_TEXTURE_ID)
                    .build(),
                onElementPass(te -> te.mCasing++, ofBlock(GregTechAPI.sBlockCasings4, 0))))
        .addElement('d', Dynamo.newAny(CASING_TEXTURE_ID, 2))
        .addElement('g', ofBlockAnyMeta(GameRegistry.findBlock("IC2", "blockAlloyGlass")))
        .addElement('e', ofBlockAnyMeta(Blocks.gdcUnit))
        .build();

    @Override
    public IStructureDefinition<MTESOFuelCellMK2> getStructureDefinition() {
        return STRUCTURE_DEFINITION;
    }

    @Override
    protected MultiblockTooltipBuilder createTooltip() {
        final MultiblockTooltipBuilder tt = new MultiblockTooltipBuilder();
        tt.addMachineType("machtype.gas_turbine")
            .addInfo("gt.so_fuel_cell.tips.1", formatNumber(EU_PER_TICK * 20))
            .addInfo("gt.so_fuel_cell_ii.tips.1")
            .addInfo(
                "gt.so_fuel_cell.tips.2",
                EU_PER_TICK,
                STEAM_PER_SEC,
                FluidRegistry.getFluidStack("ic2superheatedsteam", 1)
                    .getLocalizedName(),
                OXYGEN_PER_SEC)
            .beginStructureBlock(3, 3, 5, false)
            .addController("front_center")
            .addCasingInfoMin("gt.blockcasings4.0.name", 12)
            .addStructurePart("tile.kekztech_gdcceramicelectrolyteunit_block.name", "gt.so_fuel_cell.info.1")
            .addStructurePart("Material.reinforcedglass", "gt.so_fuel_cell.info.2")
            .addDynamoHatch("gt.so_fuel_cell.info.3", 2)
            .addMaintenanceHatch("<casing>", 1)
            .addInputHatch("gt.so_fuel_cell.info.4", 1)
            .addInputHatch("gt.so_fuel_cell.info.5", 1)
            .addOutputHatch("gt.so_fuel_cell_ii.info.1", 1)
            .toolTipFinisher();
        return tt;
    }

    @Override
    public ITexture[] getTexture(final IGregTechTileEntity aBaseMetaTileEntity, final ForgeDirection side,
        final ForgeDirection facing, final int colorIndex, final boolean aActive, final boolean aRedstone) {
        if (side == facing) {
            if (aActive) return new ITexture[] { Textures.BlockIcons.getCasingTextureForId(CASING_TEXTURE_ID),
                TextureFactory.builder()
                    .addIcon(OVERLAY_FRONT_HEAT_EXCHANGER_ACTIVE)
                    .extFacing()
                    .build(),
                TextureFactory.builder()
                    .addIcon(OVERLAY_FRONT_HEAT_EXCHANGER_ACTIVE_GLOW)
                    .extFacing()
                    .glow()
                    .build() };
            return new ITexture[] { Textures.BlockIcons.getCasingTextureForId(CASING_TEXTURE_ID),
                TextureFactory.builder()
                    .addIcon(OVERLAY_FRONT_HEAT_EXCHANGER)
                    .extFacing()
                    .build(),
                TextureFactory.builder()
                    .addIcon(OVERLAY_FRONT_HEAT_EXCHANGER_GLOW)
                    .extFacing()
                    .glow()
                    .build() };
        }
        return new ITexture[] { Textures.BlockIcons.getCasingTextureForId(CASING_TEXTURE_ID) };
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

                    liquid.amount = (int) ((EU_PER_TICK * 20)
                        / (aFuel.mSpecialValue * Math.max(1, (float) aFuel.mSpecialValue / 1000)));

                    if (super.depleteInput(liquid)) {

                        if (!super.depleteInput(Materials.Oxygen.getGas(OXYGEN_PER_SEC))) {
                            super.mEUt = 0;
                            super.mEfficiency = 0;
                            return CheckRecipeResultRegistry.NO_FUEL_FOUND;
                        }

                        super.mEUt = EU_PER_TICK;
                        super.mMaxProgresstime = 20;
                        super.mEfficiencyIncrease = 200;
                        if (super.mEfficiency == getMaxEfficiency(null)) {
                            super.addOutput(FluidRegistry.getFluidStack("ic2superheatedsteam", STEAM_PER_SEC));
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
    public boolean checkMachine(IGregTechTileEntity thisController, ItemStack guiSlotItem) {
        this.mCasing = 0;

        if (!checkPiece(STRUCTURE_PIECE_MAIN, 1, 1, 0)) return false;

        return (this.mCasing >= 12 && this.mMaintenanceHatches.size() == 1 && this.mInputHatches.size() >= 2);
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
    public boolean showRecipeTextInGUI() {
        return false;
    }
}
