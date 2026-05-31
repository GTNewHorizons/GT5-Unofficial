package gregtech.common.tileentities.machines.multi.beamcrafting;

import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_BEAM_MIRROR;
import static gregtech.api.util.GTStructureUtility.chainAllGlasses;

import java.util.List;

import net.minecraft.item.ItemStack;
import net.minecraft.util.StatCollector;
import net.minecraftforge.common.util.ForgeDirection;

import org.jetbrains.annotations.NotNull;

import com.gtnewhorizon.structurelib.alignment.constructable.ISurvivalConstructable;
import com.gtnewhorizon.structurelib.structure.IStructureDefinition;
import com.gtnewhorizon.structurelib.structure.ISurvivalBuildEnvironment;
import com.gtnewhorizon.structurelib.structure.StructureDefinition;

import gregtech.api.casing.Casings;
import gregtech.api.enums.GTAuthors;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.recipe.check.CheckRecipeResult;
import gregtech.api.recipe.check.CheckRecipeResultRegistry;
import gregtech.api.render.TextureFactory;
import gregtech.api.structure.error.StructureError;
import gregtech.api.util.MultiblockTooltipBuilder;
import gregtech.common.misc.GTStructureChannels;
import gtnhlanth.common.beamline.BeamInformation;
import gtnhlanth.common.beamline.BeamLinePacket;
import gtnhlanth.common.hatch.MTEHatchInputBeamline;
import gtnhlanth.common.hatch.MTEHatchOutputBeamline;

public class MTEBeamMirror extends MTEBeamMultiBase<MTEBeamMirror> implements ISurvivalConstructable {

    private static final String STRUCTURE_PIECE_MAIN = "main";
    private static final String STRUCTURE_PIECE_TIER2 = "tier2";
    private byte mTier = 0;

    private static final int ShieldedAccCasingTextureID = Casings.ShieldedAcceleratorCasing.getTextureId();

    private static final IStructureDefinition<MTEBeamMirror> STRUCTURE_DEFINITION = StructureDefinition
        .<MTEBeamMirror>builder()
        .addShape(
            STRUCTURE_PIECE_MAIN,
            // spotless:off
            new String[][]{{
                "   ",
                "   ",
                "   ",
                "ECE",
                "E~E"
            },{
                "   ",
                "   ",
                "   ",
                "BAB",
                "BBB"
            },{
                "   ",
                "   ",
                "   ",
                "BAB",
                "BBB"
            },{
                "EDE",
                "BAB",
                "BAB",
                "BAB",
                "BBB"
            },{
                "EEE",
                "BBB",
                "BBB",
                "BBB",
                "BBB"
            }})
        //spotless:on
        .addShape(
            STRUCTURE_PIECE_TIER2,
            // spotless:off
            new String[][]{{
                "EEE",
                "EDE",
                "   ",
                "   ",
                "ECE",
                "E~E"
            },{
                "BBB",
                "BAB",
                "   ",
                "   ",
                "BAB",
                "BBB"
            },{
                "BBB",
                "BAB",
                "   ",
                "   ",
                "BAB",
                "BBB"
            },{
                "BBB",
                "BAB",
                "BAB",
                "BAB",
                "BAB",
                "BBB"
            },{
                "BBB",
                "BBB",
                "BBB",
                "BBB",
                "BBB",
                "BBB"
            }}
        )
        //spotless:on
        .addElement('B', Casings.ShieldedAcceleratorCasing.asElement())
        .addElement('A', chainAllGlasses())
        .addElement('C', buildBeamlineInputHatch(MTEBeamMirror.class, ShieldedAccCasingTextureID, 1))
        .addElement('D', buildBeamlineOutputHatch(MTEBeamMirror.class, ShieldedAccCasingTextureID, 2))
        .addElement('E', Casings.GrateMachineCasing.asElement())
        .build();

    public MTEBeamMirror(final int aID, final String aName, final String aNameRegional) {
        super(aID, aName, aNameRegional);
    }

    public MTEBeamMirror(String aName) {
        super(aName);
    }

    @Override
    public IStructureDefinition<MTEBeamMirror> getStructureDefinition() {
        return STRUCTURE_DEFINITION;
    }

    @NotNull
    @Override
    public CheckRecipeResult checkProcessing() {
        this.mMaxProgresstime = 1;
        BeamInformation inputInfo = this.getNthInputParticle(0);

        if (inputInfo == null || inputInfo.getRate() == 0) return CheckRecipeResultRegistry.NO_RECIPE;

        outputPacketAfterRecipe(inputInfo);
        return CheckRecipeResultRegistry.SUCCESSFUL;
    }

    private void outputPacketAfterRecipe(BeamInformation inputInfo) {
        if (!this.mOutputBeamline.isEmpty()) {
            BeamLinePacket packet = new BeamLinePacket(inputInfo);
            for (MTEHatchOutputBeamline o : this.mOutputBeamline) {
                o.dataPacket = packet;
            }
        }
    }

    @Override
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new MTEBeamMirror(this.mName);
    }

    @Override
    public ITexture[] getTexture(IGregTechTileEntity baseMetaTileEntity, ForgeDirection side, ForgeDirection aFacing,
        int colorIndex, boolean aActive, boolean redstoneLevel) {
        ITexture[] rTexture;
        if (side == aFacing) {
            rTexture = new ITexture[] { Casings.ShieldedAcceleratorCasing.getCasingTexture(), TextureFactory.builder()
                .addIcon(OVERLAY_FRONT_BEAM_MIRROR)
                .extFacing()
                .build() };
        } else {
            rTexture = new ITexture[] { Casings.ShieldedAcceleratorCasing.getCasingTexture() };
        }
        return rTexture;
    }

    @Override
    protected MultiblockTooltipBuilder createTooltip() {
        MultiblockTooltipBuilder tt = new MultiblockTooltipBuilder();
        tt.addMachineType(
            StatCollector
                .translateToLocalFormatted("gt.blockmachines.multimachine.beamcrafting.beammirror.machinetype"))
            .addInfo(
                StatCollector
                    .translateToLocalFormatted("gt.blockmachines.multimachine.beamcrafting.beammirror.tooltip1"))
            .addInfo(
                StatCollector
                    .translateToLocalFormatted("gt.blockmachines.multimachine.beamcrafting.beammirror.tooltip2"))
            .beginStructureBlock(3, 5, 5, false)
            .addController(
                StatCollector.translateToLocalFormatted("gt.blockmachines.multimachine.beamcrafting.ttcontroller"))
            .addCasingInfoRange(Casings.ShieldedAcceleratorCasing.getLocalizedName(), 31, 52, false)
            .addCasingInfoRange(
                StatCollector.translateToLocalFormatted("gt.blockmachines.multimachine.beamcrafting.ttanyglass"),
                5,
                8,
                true)
            .addCasingInfoExactly(
                StatCollector.translateToLocalFormatted("gt.blockmachines.multimachine.beamcrafting.ttgratecasing"),
                9,
                false)
            .addCasingInfoExactly(
                StatCollector.translateToLocalFormatted("gt.blockmachines.multimachine.beamcrafting.ttbeaminhatch"),
                1,
                false)
            .addCasingInfoExactly(
                StatCollector.translateToLocalFormatted("gt.blockmachines.multimachine.beamcrafting.ttbeamouthatch"),
                1,
                false)
            .addSubChannelUsage(GTStructureChannels.BOROGLASS)
            .toolTipFinisher(GTAuthors.AuthorHamCorp);
        return tt;
    }

    @Override
    public void construct(ItemStack stackSize, boolean hintsOnly) {
        if (stackSize.stackSize >= 2) {
            buildPiece(STRUCTURE_PIECE_TIER2, stackSize, hintsOnly, 1, 5, 0);
        } else {
            buildPiece(STRUCTURE_PIECE_MAIN, stackSize, hintsOnly, 1, 4, 0);
        }
    }

    @Override
    public int survivalConstruct(ItemStack stackSize, int elementBudget, ISurvivalBuildEnvironment env) {
        if (mMachine) return -1;

        if (stackSize.stackSize >= 2) {
            return survivalBuildPiece(STRUCTURE_PIECE_TIER2, stackSize, 1, 5, 0, elementBudget, env, false, true);
        }
        return survivalBuildPiece(STRUCTURE_PIECE_MAIN, stackSize, 1, 4, 0, elementBudget, env, false, true);
    }

    @Override
    public void checkMachine(IGregTechTileEntity aBaseMetaTileEntity, ItemStack aStack, List<StructureError> errors) {
        mInputBeamline.clear();
        mOutputBeamline.clear();
mTier = 0;
        if (checkPiece(STRUCTURE_PIECE_TIER2, 1, 5, 0, null)) {
            mTier = 2;
            return;
        }
        if (checkPiece(STRUCTURE_PIECE_MAIN, 1, 4, 0, errors)) {
            mTier = 1;
        }
    }

    @Override
    public boolean addBeamLineInputHatch(IGregTechTileEntity te, int casingIndex) {
        if (te == null) return false;

        IMetaTileEntity mte = te.getMetaTileEntity();
        if (mte == null) return false;

        if (mte instanceof MTEHatchInputBeamline beamInput) {
            beamInput.getBaseMetaTileEntity()
                .setFrontFacing(getDirection());
            return this.mInputBeamline.add(beamInput);
        }

        return false;
    }

    @Override
    public boolean addBeamLineOutputHatch(IGregTechTileEntity te, int casingIndex) {
        if (te == null) return false;

        IMetaTileEntity mte = te.getMetaTileEntity();
        if (mte == null) return false;

        if (mte instanceof MTEHatchOutputBeamline beamOutput) {
            if (this.mTier == 2) {
                beamOutput.getBaseMetaTileEntity()
                    .setFrontFacing(getDirection());
            } else if (this.mTier == 1) {
                // only flip orientation I could achieve during tests
                boolean isFlipped = getFlip().isHorizontallyFlipped();
                switch (getRotation()) {
                    case NORMAL -> beamOutput.getBaseMetaTileEntity()
                        .setFrontFacing(ForgeDirection.UP);
                    case UPSIDE_DOWN -> beamOutput.getBaseMetaTileEntity()
                        .setFrontFacing(ForgeDirection.DOWN);
                    case CLOCKWISE -> beamOutput.getBaseMetaTileEntity()
                        .setFrontFacing(
                            getDirection().getRotation(isFlipped ? ForgeDirection.UP : ForgeDirection.DOWN));
                    case COUNTER_CLOCKWISE -> beamOutput.getBaseMetaTileEntity()
                        .setFrontFacing(
                            getDirection().getRotation(isFlipped ? ForgeDirection.DOWN : ForgeDirection.UP));
                    default -> beamOutput.getBaseMetaTileEntity()
                        .setFrontFacing(getDirection().getRotation(ForgeDirection.UP));
                }
            }
            return this.mOutputBeamline.add(beamOutput);
        }

        return false;
    }
}
