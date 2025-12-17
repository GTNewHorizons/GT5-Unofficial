package goodgenerator.blocks.tileEntity;

import static com.gtnewhorizon.structurelib.structure.StructureUtility.*;
import static gregtech.api.enums.Textures.BlockIcons.*;
import static gregtech.api.util.GTStructureUtility.*;
import static gregtech.api.util.GTUtility.validMTEList;

import net.minecraft.item.ItemStack;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.FluidStack;

import org.jetbrains.annotations.NotNull;

import com.gtnewhorizon.structurelib.alignment.constructable.IConstructable;
import com.gtnewhorizon.structurelib.alignment.constructable.ISurvivalConstructable;
import com.gtnewhorizon.structurelib.structure.IStructureDefinition;
import com.gtnewhorizon.structurelib.structure.ISurvivalBuildEnvironment;
import com.gtnewhorizon.structurelib.structure.StructureDefinition;

import goodgenerator.blocks.tileEntity.base.MTETooltipMultiBlockBaseEM;
import goodgenerator.util.DescTextLocalization;
import gregtech.api.GregTechAPI;
import gregtech.api.enums.Materials;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.implementations.MTEHatchInput;
import gregtech.api.metatileentity.implementations.MTEHatchMultiInput;
import gregtech.api.recipe.check.CheckRecipeResult;
import gregtech.api.recipe.check.CheckRecipeResultRegistry;
import gregtech.api.render.TextureFactory;
import gregtech.api.util.GTModHandler;
import gregtech.api.util.GTUtility;
import gregtech.api.util.MultiblockTooltipBuilder;
import tectech.thing.metaTileEntity.multi.base.TTMultiblockBase;

public class MTECoolantTower extends MTETooltipMultiBlockBaseEM implements IConstructable, ISurvivalConstructable {

    protected IStructureDefinition<MTECoolantTower> multiDefinition = null;
    private static final int CASING_INDEX = 1539;

    public MTECoolantTower(int aID, String aName, String aNameRegional) {
        super(aID, aName, aNameRegional);
    }

    public MTECoolantTower(String name) {
        super(name);
    }

    @Override
    public IStructureDefinition<? extends TTMultiblockBase> getStructure_EM() {
        if (multiDefinition == null) {
            multiDefinition = StructureDefinition.<MTECoolantTower>builder()
                .addShape(
                    mName,
                    transpose(
                        new String[][] {
                            { "           ", "           ", "    BBB    ", "   B   B   ", "  B     B  ", "  B     B  ",
                                "  B     B  ", "   B   B   ", "    BBB    ", "           ", "           " },
                            { "           ", "           ", "    BBB    ", "   BBBBB   ", "  BB   BB  ", "  BB   BB  ",
                                "  BB   BB  ", "   BBBBB   ", "    BBB    ", "           ", "           " },
                            { "           ", "           ", "           ", "    BBB    ", "   B   B   ", "   B   B   ",
                                "   B   B   ", "    BBB    ", "           ", "           ", "           " },
                            { "           ", "           ", "           ", "    BBB    ", "   B   B   ", "   B   B   ",
                                "   B   B   ", "    BBB    ", "           ", "           ", "           " },
                            { "           ", "           ", "           ", "    BBB    ", "   B   B   ", "   B   B   ",
                                "   B   B   ", "    BBB    ", "           ", "           ", "           " },
                            { "           ", "           ", "    BBB    ", "   BBBBB   ", "  BB   BB  ", "  BB   BB  ",
                                "  BB   BB  ", "   BBBBB   ", "    BBB    ", "           ", "           " },
                            { "           ", "           ", "    BBB    ", "   B   B   ", "  B     B  ", "  B     B  ",
                                "  B     B  ", "   B   B   ", "    BBB    ", "           ", "           " },
                            { "           ", "           ", "    BBB    ", "   B   B   ", "  B     B  ", "  B     B  ",
                                "  B     B  ", "   B   B   ", "    BBB    ", "           ", "           " },
                            { "           ", "    BBB    ", "   BBBBB   ", "  BB   BB  ", " BB     BB ", " BB     BB ",
                                " BB     BB ", "  BB   BB  ", "   BBBBB   ", "    BBB    ", "           " },
                            { "           ", "    BBB    ", "   B   B   ", "  B     B  ", " B       B ", " B       B ",
                                " B       B ", "  B     B  ", "   B   B   ", "    BBB    ", "           " },
                            { "           ", "   BBBBB   ", "  BB   BB  ", " BB     BB ", " B       B ", " B       B ",
                                " B       B ", " BB     BB ", "  BB   BB  ", "   BBBBB   ", "           " },
                            { "   HH~HH   ", "  HBBBBBH  ", " HB     BH ", "HB       BH", "HB       BH", "HB       BH",
                                "HB       BH", "HB       BH", " HB     BH ", "  HBBBBBH  ", "   HHHHH   " },
                            { "   CCCCC   ", "  C     C  ", " C       C ", "C         C", "C         C", "C         C",
                                "C         C", "C         C", " C       C ", "  C     C  ", "   CCCCC   " }, }))
                .addElement('B', ofBlockAnyMeta(GregTechAPI.sBlockConcretes, 8))
                .addElement('C', ofFrame(Materials.TungstenCarbide))
                .addElement(
                    'H',
                    buildHatchAdder(MTECoolantTower.class)
                        .atLeast(
                            gregtech.api.enums.HatchElement.InputHatch,
                            gregtech.api.enums.HatchElement.OutputHatch)
                        .casingIndex(CASING_INDEX)
                        .hint(1)
                        .buildAndChain(ofBlockAnyMeta(GregTechAPI.sBlockConcretes, 8)))
                .build();
        }
        return multiDefinition;
    }

    @Override
    public boolean checkMachine_EM(IGregTechTileEntity aBaseMetaTileEntity, ItemStack aStack) {
        return structureCheck_EM(mName, 5, 11, 0);
    }

    @Override
    protected MultiblockTooltipBuilder createTooltip() {
        final MultiblockTooltipBuilder tt = new MultiblockTooltipBuilder();
        tt.addMachineType("Coolant Tower")
            .addInfo("Turn Steam back to Distilled Water")
            .addController("Front middle of the second layer")
            .addCasingInfoExactly("Light Concrete", 277, false)
            .addCasingInfoExactly("Tungstencarbide Frame Box", 28, false)
            .addInputHatch("Any Light Concrete of the second layer", 1)
            .addOutputHatch("Any Light Concrete of the second layer", 1)
            .toolTipFinisher();
        return tt;
    }

    @Override
    public void construct(ItemStack stackSize, boolean hintsOnly) {
        structureBuild_EM(mName, 5, 11, 0, stackSize, hintsOnly);
    }

    @Override
    public String[] getStructureDescription(ItemStack stackSize) {
        return DescTextLocalization.addText("CoolantTower.hint", 3);
    }

    @Override
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new MTECoolantTower(mName);
    }

    @Override
    public boolean onRunningTick(ItemStack aStack) {
        return true;
    }

    @Override
    protected @NotNull CheckRecipeResult checkProcessing_EM() {
        this.mMaxProgresstime = 20;
        int steam = 0;

        for (MTEHatchInput tHatch : validMTEList(mInputHatches)) {
            steam += maybeDrainHatch(tHatch);
        }
        addOutput(GTModHandler.getDistilledWater(steam / 160));
        return CheckRecipeResultRegistry.SUCCESSFUL;
    }

    private int maybeDrainHatch(MTEHatchInput tHatch) {
        if (tHatch instanceof MTEHatchMultiInput) {
            int drained = 0;
            for (FluidStack maybeSteam : ((MTEHatchMultiInput) tHatch).getStoredFluid()) {
                drained += maybeDrainSteam(tHatch, maybeSteam);
            }
            return drained;
        }
        return maybeDrainSteam(tHatch, tHatch.getFillableStack());
    }

    private int maybeDrainSteam(MTEHatchInput tHatch, FluidStack maybeSteam) {
        if (maybeSteam == null) return 0;
        if (!GTUtility.areFluidsEqual(maybeSteam, Materials.Steam.getGas(1))) return 0;
        FluidStack defoSteam = tHatch.drain(ForgeDirection.UNKNOWN, maybeSteam, true);
        return defoSteam.amount;
    }

    @Override
    public ITexture[] getTexture(IGregTechTileEntity aBaseMetaTileEntity, ForgeDirection side, ForgeDirection facing,
        int colorIndex, boolean aActive, boolean aRedstone) {
        if (side == facing) {
            if (aActive) return new ITexture[] { casingTexturePages[12][3], TextureFactory.builder()
                .addIcon(OVERLAY_FRONT_HEAT_EXCHANGER_ACTIVE)
                .extFacing()
                .build(),
                TextureFactory.builder()
                    .addIcon(OVERLAY_FRONT_HEAT_EXCHANGER_ACTIVE_GLOW)
                    .extFacing()
                    .glow()
                    .build() };
            return new ITexture[] { casingTexturePages[12][3], TextureFactory.builder()
                .addIcon(OVERLAY_FRONT_HEAT_EXCHANGER)
                .extFacing()
                .build(),
                TextureFactory.builder()
                    .addIcon(OVERLAY_FRONT_HEAT_EXCHANGER_GLOW)
                    .extFacing()
                    .glow()
                    .build() };
        }
        return new ITexture[] { casingTexturePages[12][3] };
    }

    @Override
    public int survivalConstruct(ItemStack stackSize, int elementBudget, ISurvivalBuildEnvironment env) {
        if (mMachine) return -1;
        return survivalBuildPiece(mName, stackSize, 5, 11, 0, elementBudget, env, false, true);
    }

    @Override
    public boolean getDefaultHasMaintenanceChecks() {
        return false;
    }
}
