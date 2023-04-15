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
import java.util.Iterator;

import net.minecraft.item.ItemStack;
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
import gregtech.api.render.TextureFactory;
import gregtech.api.util.GT_ModHandler;
import gregtech.api.util.GT_Multiblock_Tooltip_Builder;
import gregtech.api.util.GT_Recipe;
import gregtech.api.util.GT_Recipe.GT_Recipe_Map;
import gregtech.api.util.GT_Utility;

public class GTMTE_SOFuelCellMK1 extends GT_MetaTileEntity_EnhancedMultiBlockBase<GTMTE_SOFuelCellMK1> {

    private final int OXYGEN_PER_SEC = 100;
    private final int EU_PER_TICK = 2048;
    private final int STEAM_PER_SEC = 20000;

    public GTMTE_SOFuelCellMK1(int aID, String aName, String aNameRegional) {
        super(aID, aName, aNameRegional);
    }

    public GTMTE_SOFuelCellMK1(String aName) {
        super(aName);
    }

    @Override
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity var1) {
        return new GTMTE_SOFuelCellMK1(super.mName);
    }

    private int mCasing = 0;

    private static final int CASING_TEXTURE_ID = 49;
    private static final String STRUCTURE_PIECE_MAIN = "main";
    private static final IStructureDefinition<GTMTE_SOFuelCellMK1> STRUCTURE_DEFINITION = IStructureDefinition
            .<GTMTE_SOFuelCellMK1>builder()
            .addShape(
                    STRUCTURE_PIECE_MAIN,
                    transpose(
                            new String[][] { { "ccc", "ccc", "ccc", "ccc", "ccc" },
                                    { "c~c", "geg", "geg", "geg", "cdc" }, { "ccc", "ccc", "ccc", "ccc", "ccc" } }))
            .addElement(
                    'c',
                    ofChain(
                            onElementPass(te -> te.mCasing++, ofBlock(GregTech_API.sBlockCasings4, 1)),
                            ofHatchAdder(GTMTE_SOFuelCellMK1::addInputToMachineList, CASING_TEXTURE_ID, 1),
                            ofHatchAdder(GTMTE_SOFuelCellMK1::addMaintenanceToMachineList, CASING_TEXTURE_ID, 1),
                            ofHatchAdder(GTMTE_SOFuelCellMK1::addOutputToMachineList, CASING_TEXTURE_ID, 1)))
            .addElement('d', ofHatchAdder(GTMTE_SOFuelCellMK1::addDynamoToMachineList, CASING_TEXTURE_ID, 1))
            .addElement('g', ofBlockAnyMeta(GameRegistry.findBlock("IC2", "blockAlloyGlass")))
            .addElement('e', ofBlockAnyMeta(Blocks.yszUnit)).build();

    @Override
    public IStructureDefinition<GTMTE_SOFuelCellMK1> getStructureDefinition() {
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
                .addInfo("Steam production requires the SOFC to heat up completely first")
                .addInfo("Outputs " + EU_PER_TICK + "EU/t and " + STEAM_PER_SEC + "L/s Steam")
                .addInfo("Additionally, requires " + OXYGEN_PER_SEC + "L/s Oxygen gas").addSeparator()
                .beginStructureBlock(3, 3, 5, false).addController("Front center")
                .addCasingInfo("Clean Stainless Steel Casing", 12)
                .addOtherStructurePart("YSZ Ceramic Electrolyte Unit", "3x, Center 1x1x3")
                .addOtherStructurePart("Reinforced Glass", "6x, touching the electrolyte units on the horizontal sides")
                .addDynamoHatch("Back center", 1).addMaintenanceHatch("Any casing").addInputHatch("Fuel, any casing")
                .addInputHatch("Oxygen, any casing").addOutputHatch("Steam, any casing").toolTipFinisher("KekzTech");
        return tt;
    }

    @Override
    public ITexture[] getTexture(final IGregTechTileEntity aBaseMetaTileEntity, final byte aSide, final byte aFacing,
            final byte aColorIndex, final boolean aActive, final boolean aRedstone) {
        if (aSide == aFacing) {
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

    @Override
    public boolean checkRecipe(ItemStack stack) {
        final ArrayList<FluidStack> storedFluids = super.getStoredFluids();
        Collection<GT_Recipe> recipeList = GT_Recipe_Map.sTurbineFuels.mRecipeList;

        if ((storedFluids.size() > 0 && recipeList != null)) {

            final Iterator<FluidStack> fluidsIterator = storedFluids.iterator();
            while (fluidsIterator.hasNext()) {

                final FluidStack hatchFluid = fluidsIterator.next();
                final Iterator<GT_Recipe> recipeIterator = recipeList.iterator();
                while (recipeIterator.hasNext()) {

                    final GT_Recipe aFuel = recipeIterator.next();
                    FluidStack liquid;
                    if ((liquid = GT_Utility.getFluidForFilledItem(aFuel.getRepresentativeInput(0), true)) != null
                            && hatchFluid.isFluidEqual(liquid)) {

                        liquid.amount = (EU_PER_TICK * 20) / aFuel.mSpecialValue;

                        if (super.depleteInput(liquid)) {

                            if (!super.depleteInput(Materials.Oxygen.getGas(OXYGEN_PER_SEC))) {
                                super.mEUt = 0;
                                super.mEfficiency = 0;
                                return false;
                            }

                            super.mEUt = EU_PER_TICK;
                            super.mMaxProgresstime = 20;
                            super.mEfficiencyIncrease = 40;
                            if (super.mEfficiency == getMaxEfficiency(null)) {
                                super.addOutput(GT_ModHandler.getSteam(STEAM_PER_SEC));
                            }
                            return true;
                        }
                    }
                }
            }
        }

        super.mEUt = 0;
        super.mEfficiency = 0;
        return false;
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
