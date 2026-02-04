package gregtech.common.tileentities.machines.multi;

import static com.gtnewhorizon.structurelib.structure.StructureUtility.ofBlock;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.ofBlocksTiered;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.ofChain;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.onElementPass;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.transpose;
import static gregtech.api.enums.GTValues.AuthorLeon;
import static gregtech.api.enums.HatchElement.Dynamo;
import static gregtech.api.enums.HatchElement.ExoticDynamo;
import static gregtech.api.enums.HatchElement.InputBus;
import static gregtech.api.enums.HatchElement.InputHatch;
import static gregtech.api.enums.HatchElement.Maintenance;
import static gregtech.api.enums.HatchElement.Muffler;
import static gregtech.api.util.GTStructureUtility.buildHatchAdder;
import static gregtech.api.util.GTStructureUtility.ofFrame;

import javax.annotation.Nullable;

import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.util.ForgeDirection;

import org.apache.commons.lang3.tuple.Pair;

import com.google.common.collect.ImmutableList;
import com.gtnewhorizon.structurelib.alignment.constructable.ISurvivalConstructable;
import com.gtnewhorizon.structurelib.structure.IStructureDefinition;
import com.gtnewhorizon.structurelib.structure.StructureDefinition;

import gregtech.api.GregTechAPI;
import gregtech.api.casing.Casings;
import gregtech.api.enums.Materials;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.implementations.MTEEnhancedMultiBlockBase;
import gregtech.api.util.MultiblockTooltipBuilder;

public class MTELargeNeutralizationEngine extends MTEEnhancedMultiBlockBase<MTELargeNeutralizationEngine>
    implements ISurvivalConstructable {

    private static final String STRUCTURE_PIECE_MAIN = "main";

    private int structureTier;
    private int mCasing;

    private static final int HORIZONTAL_OFF_SET=5;
    private static final int VERTICAL_OFF_SET=1;
    private static final int DEPTH_OFF_SET=0;

    public MTELargeNeutralizationEngine(int aID, String aName, String aNameRegional) {
        super(aID, aName, aNameRegional);
    }

    public MTELargeNeutralizationEngine(String aName) {
        super(aName);
    }

    @Override
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new MTELargeNeutralizationEngine(this.mName);
    }

    @Nullable
    private static Integer getStructureCasingTier(Block b, int m) {
        if (b != getBlock()) return null;
        if (m <= 0 || m >= 4) return null;
        return m;
    }

    private static Block getBlock() {
        return GregTechAPI.sBlockCasings12;
    }

    private static IStructureDefinition<MTELargeNeutralizationEngine> STRUCTURE_DEFINITION = null;

    @Override
    public IStructureDefinition<MTELargeNeutralizationEngine> getStructureDefinition() {
        if (STRUCTURE_DEFINITION == null) {
            STRUCTURE_DEFINITION = StructureDefinition.<MTELargeNeutralizationEngine>builder()
                .addShape(
                    STRUCTURE_PIECE_MAIN,
                    transpose(
                        new String[][] { { "           ", "   PPPPP   ", "           " },
                            { "   CC~CC   ", "  P-----P  ", "   CCCCC   " },
                            { "  CCFFFCC  ", " P--CCC--P ", "  CCFFFCC  " },
                            { " CCF   FCC ", "P--C   C--P", " CCF   FCC " },
                            { " CF     FC ", "P-CF   FC-P", " CF     FC " },
                            { " CF     FC ", "P-CF   FC-P", " CF     FC " },
                            { " F F   F F ", " CFCF FCFC ", " F F   F F " } }))
                .addElement(
                    'C',
                    ofChain(
                        buildHatchAdder(MTELargeNeutralizationEngine.class)
                            .atLeast(Dynamo.or(ExoticDynamo), Maintenance, InputBus, InputHatch, Muffler)
                            .casingIndex(Casings.StrengthenedInanimateCasing.getTextureId())
                            .allowOnly(ForgeDirection.NORTH)
                            .hint(1)
                            .build(),
                        onElementPass(
                            m -> m.mCasing++,
                            ofBlocksTiered(
                                MTELargeNeutralizationEngine::getStructureCasingTier,
                                ImmutableList.of(
                                    Pair.of(getBlock(), Casings.StrengthenedInanimateCasing.getBlockMeta()),
                                    Pair.of(getBlock(), Casings.PreciseStationaryCasing.getBlockMeta()),
                                    Pair.of(getBlock(), Casings.UltimateStaticCasing.getBlockMeta())),
                                -1,
                                (m, t) -> m.structureTier = t,
                                m -> m.structureTier))))
                .addElement('F', ofFrame(Materials.Polytetrafluoroethylene))
                .addElement('P', ofBlock(GregTechAPI.sBlockCasings8, 1))
                .build();
        }
        return STRUCTURE_DEFINITION;
    }

    @Override
    protected MultiblockTooltipBuilder createTooltip() {
        final MultiblockTooltipBuilder tt = new MultiblockTooltipBuilder();
        tt.addMachineType("Acid Generator, LNE")
            .addInfo("(Dis)solves all your problems!")
            .addInfo("Provide an acid to burn, and a base to boost efficiency")
            .addInfo("TBD: Finish residue/base info")
            .beginStructureBlock(11, 7, 3, true)
            .addController("Top center")
            .addCasingInfoRange(
                "Tiered Casings: Strengthened Inanimate Casing, Precise Stationary Casing, Ultimate Static Casing",
                30,
                46,
                false)
            .addCasingInfoExactly("Polytetrafluoroethylene Frame Box", 34, false)
            .addCasingInfoExactly("PTFE Pipe Casing", 15, false)
            .addInputBus("Any Tiered Casing", 1)
            .addInputHatch("Any Tiered Casing", 1)
            .addMufflerHatch("Any Tiered Casing", 1)
            .addMaintenanceHatch("Any Tiered Casing", 1)
            .addDynamoHatch("Any Tiered Casing", 1)
            .toolTipFinisher(AuthorLeon);
        return tt;
    }

    @Override
    public boolean checkMachine(IGregTechTileEntity aBaseMetaTileEntity, ItemStack aStack) {
        mCasing=0;
        structureTier=-1;
        if (!checkPiece(STRUCTURE_PIECE_MAIN, HORIZONTAL_OFF_SET, VERTICAL_OFF_SET, DEPTH_OFF_SET)) return false;
        if (mCasing<30 || structureTier<1) return false;
        return (!mMufflerHatches.isEmpty() && mMaintenanceHatches.size()==1);
    }
}
