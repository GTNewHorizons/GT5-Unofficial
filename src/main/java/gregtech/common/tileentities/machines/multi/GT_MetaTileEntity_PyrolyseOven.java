package gregtech.common.tileentities.machines.multi;

import static com.gtnewhorizon.structurelib.structure.StructureUtility.ofBlock;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.ofBlockUnlocalizedName;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.onElementPass;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.transpose;
import static gregtech.api.enums.GT_HatchElement.Energy;
import static gregtech.api.enums.GT_HatchElement.InputBus;
import static gregtech.api.enums.GT_HatchElement.InputHatch;
import static gregtech.api.enums.GT_HatchElement.Maintenance;
import static gregtech.api.enums.GT_HatchElement.Muffler;
import static gregtech.api.enums.GT_HatchElement.OutputBus;
import static gregtech.api.enums.GT_HatchElement.OutputHatch;
import static gregtech.api.enums.Mods.NewHorizonsCoreMod;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_PYROLYSE_OVEN;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_PYROLYSE_OVEN_ACTIVE;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_PYROLYSE_OVEN_ACTIVE_GLOW;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_PYROLYSE_OVEN_GLOW;
import static gregtech.api.util.GT_StructureUtility.buildHatchAdder;
import static gregtech.api.util.GT_StructureUtility.ofCoil;

import net.minecraft.item.ItemStack;
import net.minecraftforge.common.util.ForgeDirection;

import org.jetbrains.annotations.NotNull;

import com.gtnewhorizon.structurelib.alignment.constructable.ISurvivalConstructable;
import com.gtnewhorizon.structurelib.structure.IStructureDefinition;
import com.gtnewhorizon.structurelib.structure.IStructureElement;
import com.gtnewhorizon.structurelib.structure.ISurvivalBuildEnvironment;
import com.gtnewhorizon.structurelib.structure.StructureDefinition;

import gregtech.GT_Mod;
import gregtech.api.GregTech_API;
import gregtech.api.enums.HeatingCoilLevel;
import gregtech.api.enums.Textures;
import gregtech.api.enums.Textures.BlockIcons;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.logic.ProcessingLogic;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_EnhancedMultiBlockBase;
import gregtech.api.recipe.RecipeMap;
import gregtech.api.recipe.RecipeMaps;
import gregtech.api.recipe.check.CheckRecipeResult;
import gregtech.api.render.TextureFactory;
import gregtech.api.util.GT_Multiblock_Tooltip_Builder;

public class GT_MetaTileEntity_PyrolyseOven
    extends GT_MetaTileEntity_EnhancedMultiBlockBase<GT_MetaTileEntity_PyrolyseOven> implements ISurvivalConstructable {

    private HeatingCoilLevel coilHeat;
    private static final int CASING_INDEX = 1090;
    private static final IStructureDefinition<GT_MetaTileEntity_PyrolyseOven> STRUCTURE_DEFINITION = createStructureDefinition();

    private static IStructureDefinition<GT_MetaTileEntity_PyrolyseOven> createStructureDefinition() {
        IStructureElement<GT_MetaTileEntity_PyrolyseOven> tCasingElement = NewHorizonsCoreMod.isModLoaded()
            ? ofBlockUnlocalizedName(NewHorizonsCoreMod.ID, "gt.blockcasingsNH", 2)
            : ofBlock(GregTech_API.sBlockCasings1, 0);

        return StructureDefinition.<GT_MetaTileEntity_PyrolyseOven>builder()
            .addShape(
                "main",
                transpose(
                    new String[][] { { "ccccc", "ctttc", "ctttc", "ctttc", "ccccc" },
                        { "ccccc", "c---c", "c---c", "c---c", "ccccc" },
                        { "ccccc", "c---c", "c---c", "c---c", "ccccc" },
                        { "bb~bb", "bCCCb", "bCCCb", "bCCCb", "bbbbb" }, }))
            .addElement('c', onElementPass(GT_MetaTileEntity_PyrolyseOven::onCasingAdded, tCasingElement))
            .addElement(
                'C',
                ofCoil(GT_MetaTileEntity_PyrolyseOven::setCoilLevel, GT_MetaTileEntity_PyrolyseOven::getCoilLevel))
            .addElement(
                'b',
                buildHatchAdder(GT_MetaTileEntity_PyrolyseOven.class)
                    .atLeast(OutputBus, OutputHatch, Energy, Maintenance)
                    .casingIndex(CASING_INDEX)
                    .dot(1)
                    .buildAndChain(onElementPass(GT_MetaTileEntity_PyrolyseOven::onCasingAdded, tCasingElement)))
            .addElement(
                't',
                buildHatchAdder(GT_MetaTileEntity_PyrolyseOven.class).atLeast(InputBus, InputHatch, Muffler)
                    .casingIndex(CASING_INDEX)
                    .dot(1)
                    .buildAndChain(onElementPass(GT_MetaTileEntity_PyrolyseOven::onCasingAdded, tCasingElement)))
            .build();
    }

    private int mCasingAmount;

    public GT_MetaTileEntity_PyrolyseOven(int aID, String aName, String aNameRegional) {
        super(aID, aName, aNameRegional);
    }

    public GT_MetaTileEntity_PyrolyseOven(String aName) {
        super(aName);
    }

    @Override
    protected GT_Multiblock_Tooltip_Builder createTooltip() {
        final GT_Multiblock_Tooltip_Builder tt = new GT_Multiblock_Tooltip_Builder();
        tt.addMachineType("Coke Oven")
            .addInfo("Controller block for the Pyrolyse Oven")
            .addInfo("Industrial Charcoal producer")
            .addInfo("Processing speed scales linearly with Coil tier:")
            .addInfo("CuNi: 50%, FeAlCr: 100%, Ni4Cr: 150%, TPV: 200%, etc.")
            .addInfo("EU/t is not affected by Coil tier")
            .addPollutionAmount(getPollutionPerSecond(null))
            .addSeparator()
            .beginStructureBlock(5, 4, 5, true)
            .addController("Front center")
            .addCasingInfoRange("Pyrolyse Oven Casing", 60, 80, false)
            .addOtherStructurePart("Heating Coils", "Center 3x1x3 of the bottom layer")
            .addEnergyHatch("Any bottom layer casing", 1)
            .addMaintenanceHatch("Any bottom layer casing", 1)
            .addMufflerHatch("Center 3x1x3 area in top layer", 2)
            .addInputBus("Center 3x1x3 area in top layer", 2)
            .addInputHatch("Center 3x1x3 area in top layer", 2)
            .addOutputBus("Any bottom layer casing", 1)
            .addOutputHatch("Any bottom layer casing", 1)
            .toolTipFinisher("Gregtech");
        return tt;
    }

    @Override
    public ITexture[] getTexture(IGregTechTileEntity baseMetaTileEntity, ForgeDirection sideDirection,
        ForgeDirection facingDirection, int colorIndex, boolean active, boolean redstoneLevel) {
        if (sideDirection == facingDirection) {
            if (active) return new ITexture[] { BlockIcons.casingTexturePages[8][66], TextureFactory.builder()
                .addIcon(OVERLAY_FRONT_PYROLYSE_OVEN_ACTIVE)
                .extFacing()
                .build(),
                TextureFactory.builder()
                    .addIcon(OVERLAY_FRONT_PYROLYSE_OVEN_ACTIVE_GLOW)
                    .extFacing()
                    .glow()
                    .build() };
            return new ITexture[] { BlockIcons.casingTexturePages[8][66], TextureFactory.builder()
                .addIcon(OVERLAY_FRONT_PYROLYSE_OVEN)
                .extFacing()
                .build(),
                TextureFactory.builder()
                    .addIcon(OVERLAY_FRONT_PYROLYSE_OVEN_GLOW)
                    .extFacing()
                    .glow()
                    .build() };
        }
        return new ITexture[] { Textures.BlockIcons.casingTexturePages[8][66] };
    }

    @Override
    public boolean supportsSingleRecipeLocking() {
        return true;
    }

    @Override
    public RecipeMap<?> getRecipeMap() {
        return RecipeMaps.pyrolyseRecipes;
    }

    @Override
    protected ProcessingLogic createProcessingLogic() {
        return new ProcessingLogic() {

            @NotNull
            @Override
            public CheckRecipeResult process() {
                setSpeedBonus(2f / (1 + coilHeat.getTier()));
                return super.process();
            }
        };
    }

    @Override
    public IStructureDefinition<GT_MetaTileEntity_PyrolyseOven> getStructureDefinition() {
        return STRUCTURE_DEFINITION;
    }

    private void onCasingAdded() {
        mCasingAmount++;
    }

    public HeatingCoilLevel getCoilLevel() {
        return coilHeat;
    }

    private void setCoilLevel(HeatingCoilLevel aCoilLevel) {
        coilHeat = aCoilLevel;
    }

    @Override
    public boolean checkMachine(IGregTechTileEntity aBaseMetaTileEntity, ItemStack aStack) {
        coilHeat = HeatingCoilLevel.None;
        mCasingAmount = 0;
        replaceDeprecatedCoils(aBaseMetaTileEntity);
        return checkPiece("main", 2, 3, 0) && mCasingAmount >= 60
            && mMaintenanceHatches.size() == 1
            && !mMufflerHatches.isEmpty();
    }

    @Override
    public boolean isCorrectMachinePart(ItemStack aStack) {
        return true;
    }

    @Override
    public int getMaxEfficiency(ItemStack aStack) {
        return 10000;
    }

    @Override
    public int getPollutionPerSecond(ItemStack aStack) {
        return GT_Mod.gregtechproxy.mPollutionPyrolyseOvenPerSecond;
    }

    @Override
    public int getDamageToComponent(ItemStack aStack) {
        return 0;
    }

    @Override
    public boolean explodesOnComponentBreak(ItemStack aStack) {
        return false;
    }

    @Override
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new GT_MetaTileEntity_PyrolyseOven(this.mName);
    }

    private void replaceDeprecatedCoils(IGregTechTileEntity aBaseMetaTileEntity) {
        final int xDir = aBaseMetaTileEntity.getBackFacing().offsetX;
        final int zDir = aBaseMetaTileEntity.getBackFacing().offsetZ;
        final int tX = aBaseMetaTileEntity.getXCoord() + xDir * 2;
        final int tY = aBaseMetaTileEntity.getYCoord();
        final int tZ = aBaseMetaTileEntity.getZCoord() + zDir * 2;
        for (int xPos = tX - 1; xPos <= tX + 1; xPos++) {
            for (int zPos = tZ - 1; zPos <= tZ + 1; zPos++) {
                if (aBaseMetaTileEntity.getBlock(xPos, tY, zPos) == GregTech_API.sBlockCasings1
                    && aBaseMetaTileEntity.getMetaID(xPos, tY, zPos) == 13) {
                    aBaseMetaTileEntity.getWorld()
                        .setBlock(xPos, tY, zPos, GregTech_API.sBlockCasings5, 1, 3);
                }
            }
        }
    }

    @Override
    public void construct(ItemStack stackSize, boolean hintsOnly) {
        buildPiece("main", stackSize, hintsOnly, 2, 3, 0);
    }

    @Override
    public int survivalConstruct(ItemStack stackSize, int elementBudget, ISurvivalBuildEnvironment env) {
        if (mMachine) return -1;
        return survivialBuildPiece("main", stackSize, 2, 3, 0, elementBudget, env, false, true);
    }

    @Override
    public boolean supportsVoidProtection() {
        return true;
    }
}
