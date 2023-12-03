package common.tileentities;

import static com.gtnewhorizon.structurelib.structure.StructureUtility.ofBlock;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.ofBlockAnyMeta;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.ofChain;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.onElementPass;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.transpose;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_HEAT_EXCHANGER;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_HEAT_EXCHANGER_ACTIVE;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_HEAT_EXCHANGER_ACTIVE_GLOW;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_HEAT_EXCHANGER_GLOW;
import static gregtech.api.util.GT_StructureUtility.ofHatchAdder;

import java.util.ArrayList;
import java.util.Collection;

import javax.annotation.Nonnull;

import net.minecraft.item.ItemStack;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;

import com.gtnewhorizon.structurelib.structure.IStructureDefinition;
import common.Blocks;

import cpw.mods.fml.common.registry.GameRegistry;
import gregtech.api.GregTech_API;
import gregtech.api.enums.Materials;
import gregtech.api.enums.Textures;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_EnhancedMultiBlockBase;
import gregtech.api.recipe.RecipeMaps;
import gregtech.api.recipe.check.CheckRecipeResult;
import gregtech.api.recipe.check.CheckRecipeResultRegistry;
import gregtech.api.render.TextureFactory;
import gregtech.api.util.GT_Multiblock_Tooltip_Builder;
import gregtech.api.util.GT_Recipe;
import gregtech.api.util.GT_Utility;

public class GTMTE_SOFuelCellMK2 extends GT_MetaTileEntity_EnhancedMultiBlockBase<GTMTE_SOFuelCellMK2> {

    private final int OXYGEN_PER_SEC = 2000;
    private final int EU_PER_TICK = 24576; // 100% Efficiency, 3A IV
    private final int STEAM_PER_SEC = 96000; // SH Steam (10,800EU/t @ 150% Efficiency)

    public GTMTE_SOFuelCellMK2(int aID, String aName, String aNameRegional) {
        super(aID, aName, aNameRegional);
    }

    public GTMTE_SOFuelCellMK2(String aName) {
        super(aName);
    }

    @Override
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity var1) {
        return new GTMTE_SOFuelCellMK2(super.mName);
    }

    private int mCasing = 0;

    private static final int CASING_TEXTURE_ID = 48;
    private static final String STRUCTURE_PIECE_MAIN = "main";
    private static final IStructureDefinition<GTMTE_SOFuelCellMK2> STRUCTURE_DEFINITION = IStructureDefinition
            .<GTMTE_SOFuelCellMK2>builder()
            .addShape(
                    STRUCTURE_PIECE_MAIN,
                    transpose(
                            new String[][] { { "ccc", "ccc", "ccc", "ccc", "ccc" },
                                    { "c~c", "geg", "geg", "geg", "cdc" }, { "ccc", "ccc", "ccc", "ccc", "ccc" } }))
            .addElement(
                    'c',
                    ofChain(
                            onElementPass(te -> te.mCasing++, ofBlock(GregTech_API.sBlockCasings4, 0)),
                            ofHatchAdder(GTMTE_SOFuelCellMK2::addInputToMachineList, CASING_TEXTURE_ID, 1),
                            ofHatchAdder(GTMTE_SOFuelCellMK2::addMaintenanceToMachineList, CASING_TEXTURE_ID, 1),
                            ofHatchAdder(GTMTE_SOFuelCellMK2::addOutputToMachineList, CASING_TEXTURE_ID, 1)))
            .addElement('d', ofHatchAdder(GTMTE_SOFuelCellMK2::addDynamoToMachineList, CASING_TEXTURE_ID, 1))
            .addElement('g', ofBlockAnyMeta(GameRegistry.findBlock("IC2", "blockAlloyGlass")))
            .addElement('e', ofBlockAnyMeta(Blocks.gdcUnit)).build();

    @Override
    public IStructureDefinition<GTMTE_SOFuelCellMK2> getStructureDefinition() {
        return STRUCTURE_DEFINITION;
    }

    @Override
    protected GT_Multiblock_Tooltip_Builder createTooltip() {
        final GT_Multiblock_Tooltip_Builder tt = new GT_Multiblock_Tooltip_Builder();
        tt.addMachineType("Gas Turbine")
                .addInfo("Oxidizes gas fuels to generate electricity without polluting the environment")
                .addInfo(
                        "Consumes up to " + GT_Utility.formatNumbers(EU_PER_TICK * 20)
                                + "EU worth of fuel with up to 100% efficiency each second")
                .addInfo("Nitrobenzene and other gas fuels above 1M EU/bucket are more efficient")
                .addInfo("Steam production requires the SOFC to heat up completely first")
                .addInfo("Outputs " + EU_PER_TICK + "EU/t and " + STEAM_PER_SEC + "L/s Steam")
                .addInfo("Additionally, requires " + OXYGEN_PER_SEC + "L/s Oxygen gas").addSeparator()
                .beginStructureBlock(3, 3, 5, false).addController("Front center")
                .addCasingInfoMin("Robust Tungstensteel Machine Casing", 12, false)
                .addOtherStructurePart("GDC Ceramic Electrolyte Unit", "3x, Center 1x1x3")
                .addOtherStructurePart("Reinforced Glass", "6x, touching the electrolyte units on the horizontal sides")
                .addDynamoHatch("Back center", 1).addMaintenanceHatch("Any casing").addInputHatch("Fuel, any casing")
                .addInputHatch("Oxygen, any casing").addOutputHatch("Superheated Steam, any casing")
                .toolTipFinisher("KekzTech");
        return tt;
    }

    @Override
    public ITexture[] getTexture(final IGregTechTileEntity aBaseMetaTileEntity, final ForgeDirection side,
            final ForgeDirection facing, final int colorIndex, final boolean aActive, final boolean aRedstone) {
        if (side == facing) {
            if (aActive) return new ITexture[] { Textures.BlockIcons.getCasingTextureForId(CASING_TEXTURE_ID),
                    TextureFactory.builder().addIcon(OVERLAY_FRONT_HEAT_EXCHANGER_ACTIVE).extFacing().build(),
                    TextureFactory.builder().addIcon(OVERLAY_FRONT_HEAT_EXCHANGER_ACTIVE_GLOW).extFacing().glow()
                            .build() };
            return new ITexture[] { Textures.BlockIcons.getCasingTextureForId(CASING_TEXTURE_ID),
                    TextureFactory.builder().addIcon(OVERLAY_FRONT_HEAT_EXCHANGER).extFacing().build(),
                    TextureFactory.builder().addIcon(OVERLAY_FRONT_HEAT_EXCHANGER_GLOW).extFacing().glow().build() };
        }
        return new ITexture[] { Textures.BlockIcons.getCasingTextureForId(CASING_TEXTURE_ID) };
    }

    @Override
    public boolean isCorrectMachinePart(ItemStack stack) {
        return true;
    }

    @Nonnull
    @Override
    public CheckRecipeResult checkProcessing() {
        final ArrayList<FluidStack> storedFluids = super.getStoredFluids();
        Collection<GT_Recipe> recipeList = RecipeMaps.gasTurbineFuels.getAllRecipes();

        for (FluidStack hatchFluid : storedFluids) {
            for (GT_Recipe aFuel : recipeList) {
                FluidStack liquid;
                if ((liquid = GT_Utility.getFluidForFilledItem(aFuel.getRepresentativeInput(0), true)) != null
                        && hatchFluid.isFluidEqual(liquid)) {

                    liquid.amount = (EU_PER_TICK * 20) / aFuel.mSpecialValue / Math.max(1, aFuel.mSpecialValue / 1000);

                    if (super.depleteInput(liquid)) {

                        if (!super.depleteInput(Materials.Oxygen.getGas(OXYGEN_PER_SEC))) {
                            super.mEUt = 0;
                            super.mEfficiency = 0;
                            return CheckRecipeResultRegistry.NO_FUEL_FOUND;
                        }

                        super.mEUt = EU_PER_TICK;
                        super.mMaxProgresstime = 20;
                        super.mEfficiencyIncrease = 80;
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
    public int getMaxEfficiency(ItemStack stack) {
        return 10000;
    }

    @Override
    public int getPollutionPerTick(ItemStack stack) {
        return 0;
    }

    @Override
    public int getDamageToComponent(ItemStack stack) {
        return 0;
    }

    @Override
    public boolean explodesOnComponentBreak(ItemStack stack) {
        return false;
    }

    @Override
    public void construct(ItemStack itemStack, boolean b) {
        buildPiece(STRUCTURE_PIECE_MAIN, itemStack, b, 1, 1, 0);
    }
}
