package gregtech.common.tileentities.machines.multi.beamcrafting;

import static com.gtnewhorizon.structurelib.structure.StructureUtility.ofBlock;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_BEAM_MIRROR;
import static gregtech.api.util.GTStructureUtility.buildHatchAdder;
import static gregtech.api.util.GTStructureUtility.chainAllGlasses;

import java.util.ArrayList;

import javax.annotation.Nullable;

import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.common.util.ForgeDirection;

import org.jetbrains.annotations.NotNull;

import com.gtnewhorizon.structurelib.alignment.constructable.ISurvivalConstructable;
import com.gtnewhorizon.structurelib.structure.IStructureDefinition;
import com.gtnewhorizon.structurelib.structure.ISurvivalBuildEnvironment;
import com.gtnewhorizon.structurelib.structure.StructureDefinition;

import gregtech.api.GregTechAPI;
import gregtech.api.enums.Textures;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.implementations.MTEExtendedPowerMultiBlockBase;
import gregtech.api.recipe.check.CheckRecipeResult;
import gregtech.api.recipe.check.CheckRecipeResultRegistry;
import gregtech.api.render.TextureFactory;
import gregtech.api.util.GTUtility;
import gregtech.api.util.MultiblockTooltipBuilder;
import gregtech.common.misc.GTStructureChannels;
import gtnhlanth.common.beamline.BeamInformation;
import gtnhlanth.common.beamline.BeamLinePacket;
import gtnhlanth.common.hatch.MTEHatchInputBeamline;
import gtnhlanth.common.hatch.MTEHatchOutputBeamline;

public class MTEBeamMirror
    extends MTEExtendedPowerMultiBlockBase<gregtech.common.tileentities.machines.multi.beamcrafting.MTEBeamMirror>
    implements ISurvivalConstructable {

    private static final String STRUCTURE_PIECE_MAIN = "main";

    private static final int CASING_INDEX_CENTRE = 1662; // Shielded Acc.
    private final ArrayList<MTEHatchInputBeamline> mInputBeamline = new ArrayList<>();
    private final ArrayList<MTEHatchOutputBeamline> mOutputBeamline = new ArrayList<>();

    private static final IStructureDefinition<gregtech.common.tileentities.machines.multi.beamcrafting.MTEBeamMirror> STRUCTURE_DEFINITION = StructureDefinition.<gregtech.common.tileentities.machines.multi.beamcrafting.MTEBeamMirror>builder()
        .addShape(
            STRUCTURE_PIECE_MAIN,
            // spotless:off
            new String[][]{{
                "   ",
                "   ",
                "   ",
                "BCB",
                "B~B"
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
                "BDB",
                "BAB",
                "BAB",
                "BAB",
                "BBB"
            },{
                "BBB",
                "BBB",
                "BBB",
                "BBB",
                "BBB"
            }})
        //spotless:on
        .addElement(
            'B', // collider casing
            ofBlock(GregTechAPI.sBlockCasings13, 10))
        .addElement('A', chainAllGlasses()) // new glass type todo: (?)
        .addElement(
            'C',
            buildHatchAdder(MTEBeamMirror.class).hatchClass(MTEHatchInputBeamline.class)
                .casingIndex(CASING_INDEX_CENTRE)
                .hint(1)
                .adder(MTEBeamMirror::addBeamLineInputHatch)
                .build()) // beamline input hatch
        .addElement(
            'D',
            buildHatchAdder(MTEBeamMirror.class).hatchClass(MTEHatchOutputBeamline.class)
                .casingIndex(CASING_INDEX_CENTRE)
                .hint(2)
                .adder(MTEBeamMirror::addBeamLineOutputHatch)
                .build()) // beamline output hatch
        .build();

    private boolean addBeamLineInputHatch(IGregTechTileEntity te, int casingIndex) {
        if (te == null) return false;

        IMetaTileEntity mte = te.getMetaTileEntity();
        if (mte == null) return false;

        if (mte instanceof MTEHatchInputBeamline) {
            return this.mInputBeamline.add((MTEHatchInputBeamline) mte);
        }

        return false;
    }

    private boolean addBeamLineOutputHatch(IGregTechTileEntity te, int casingIndex) {
        if (te == null) return false;

        IMetaTileEntity mte = te.getMetaTileEntity();
        if (mte == null) return false;

        if (mte instanceof MTEHatchOutputBeamline) {
            return this.mOutputBeamline.add((MTEHatchOutputBeamline) mte);
        }

        return false;
    }

    public MTEBeamMirror(final int aID, final String aName, final String aNameRegional) {
        super(aID, aName, aNameRegional);
    }

    public MTEBeamMirror(String aName) {
        super(aName);
    }

    @Override
    public IStructureDefinition<gregtech.common.tileentities.machines.multi.beamcrafting.MTEBeamMirror> getStructureDefinition() {
        return STRUCTURE_DEFINITION;
    }

    @NotNull
    @Override
    public CheckRecipeResult checkProcessing() {
        BeamInformation inputInfo = this.getInputParticle();

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
    public boolean doRandomMaintenanceDamage() {
        // Cannot have maintenance issues, so do nothing.
        return true;
    }

    @Override
    public boolean getDefaultHasMaintenanceChecks() {
        return false;
    }

    @Override
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new gregtech.common.tileentities.machines.multi.beamcrafting.MTEBeamMirror(this.mName);
    }

    @Override
    public ITexture[] getTexture(IGregTechTileEntity baseMetaTileEntity, ForgeDirection side, ForgeDirection aFacing,
        int colorIndex, boolean aActive, boolean redstoneLevel) {
        ITexture[] rTexture;
        if (side == aFacing) {
            rTexture = new ITexture[] {
                Textures.BlockIcons
                    .getCasingTextureForId(GTUtility.getCasingTextureIndex(GregTechAPI.sBlockCasings13, 10)),
                TextureFactory.builder()
                    .addIcon(OVERLAY_FRONT_BEAM_MIRROR)
                    .extFacing()
                    .build() };
        } else {
            rTexture = new ITexture[] { Textures.BlockIcons
                .getCasingTextureForId(GTUtility.getCasingTextureIndex(GregTechAPI.sBlockCasings13, 10)) };
        }
        return rTexture;
    }

    @Override
    protected MultiblockTooltipBuilder createTooltip() {
        MultiblockTooltipBuilder tt = new MultiblockTooltipBuilder();
        tt.addMachineType("Beam Mirror")
            .addInfo("Takes in particle beams and sends them back out")
            .addInfo(EnumChatFormatting.GOLD + "That's literally it.")
            .beginStructureBlock(3, 5, 5, false)
            .addController("Front Center")
            .addCasingInfoExactly("Collider Casing", 40, false)
            .addCasingInfoExactly("Any Glass", 5, true)
            .addCasingInfoExactly("Beamline Input Hatch", 1, false)
            .addCasingInfoExactly("Beamline Output Hatch", 1, false)
            .addSubChannelUsage(GTStructureChannels.BOROGLASS)
            // .addTecTechHatchInfo()
            .toolTipFinisher();
        return tt;
    }

    @Override
    public void construct(ItemStack stackSize, boolean hintsOnly) {
        buildPiece(STRUCTURE_PIECE_MAIN, stackSize, hintsOnly, 1, 4, 0);
    }

    @Override
    public int survivalConstruct(ItemStack stackSize, int elementBudget, ISurvivalBuildEnvironment env) {
        if (mMachine) return -1;
        return survivalBuildPiece(STRUCTURE_PIECE_MAIN, stackSize, 1, 4, 0, elementBudget, env, false, true);
    }

    @Override
    public boolean checkMachine(IGregTechTileEntity aBaseMetaTileEntity, ItemStack aStack) {
        mInputBeamline.clear();
        mOutputBeamline.clear();
        return checkPiece(STRUCTURE_PIECE_MAIN, 1, 4, 0);
    }

    @Nullable
    private BeamInformation getInputParticle() {
        for (MTEHatchInputBeamline in : this.mInputBeamline) {
            if (in.dataPacket == null) return new BeamInformation(0, 0, 0, 0);
            return in.dataPacket.getContent();
        }
        return null;
    }

}
