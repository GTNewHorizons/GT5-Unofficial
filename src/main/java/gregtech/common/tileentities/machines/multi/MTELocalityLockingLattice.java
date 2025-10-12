package gregtech.common.tileentities.machines.multi;

import static com.gtnewhorizon.structurelib.structure.StructureUtility.ofBlock;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.onElementPass;
import static gregtech.api.enums.HatchElement.Energy;
import static gregtech.api.enums.HatchElement.InputBus;
import static gregtech.api.enums.HatchElement.InputHatch;
import static gregtech.api.enums.HatchElement.OutputHatch;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_MULTI_BREWERY;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_MULTI_BREWERY_ACTIVE;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_MULTI_BREWERY_ACTIVE_GLOW;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_MULTI_BREWERY_GLOW;
import static gregtech.api.util.GTStructureUtility.buildHatchAdder;
import static gregtech.api.util.GTStructureUtility.chainAllGlasses;
import static gregtech.api.util.GTStructureUtility.ofFrame;

import net.minecraft.item.ItemStack;
import net.minecraftforge.common.util.ForgeDirection;

import com.gtnewhorizon.structurelib.alignment.constructable.ISurvivalConstructable;
import com.gtnewhorizon.structurelib.structure.IStructureDefinition;
import com.gtnewhorizon.structurelib.structure.ISurvivalBuildEnvironment;
import com.gtnewhorizon.structurelib.structure.StructureDefinition;

import gregtech.api.GregTechAPI;
import gregtech.api.casing.Casings;
import gregtech.api.enums.Materials;
import gregtech.api.enums.Textures;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.logic.ProcessingLogic;
import gregtech.api.metatileentity.implementations.MTEExtendedPowerMultiBlockBase;
import gregtech.api.recipe.RecipeMap;
import gregtech.api.recipe.RecipeMaps;
import gregtech.api.render.TextureFactory;
import gregtech.api.util.GTUtility;
import gregtech.api.util.MultiblockTooltipBuilder;
import gregtech.common.blocks.BlockCasings3;
import gregtech.common.misc.GTStructureChannels;

public class MTELocalityLockingLattice extends MTEExtendedPowerMultiBlockBase<MTELocalityLockingLattice>
    implements ISurvivalConstructable {

    private static final String STRUCTURE_PIECE_MAIN = "main";
    private static final IStructureDefinition<MTELocalityLockingLattice> STRUCTURE_DEFINITION = StructureDefinition
        .<MTELocalityLockingLattice>builder()
        .addShape(
            STRUCTURE_PIECE_MAIN,
            // spotless:off
                new String[][]{{
                        "                   ",
                        "                   ",
                        "                   ",
                        "                   ",
                        "D D             D D",
                        "DDDD           DDDD",
                        "D  D           D  D",
                        "DDDD           DDDD",
                        "D D             D D",
                        "                   ",
                        "                   ",
                        "                   ",
                        "                   "
                },{
                        "                   ",
                        "                   ",
                        "D D             D D",
                        "D D             D D",
                        " AD             DA ",
                        " ADA           ADA ",
                        "DAAAA         AAAAD",
                        " ADA           ADA ",
                        " AD             DA ",
                        "D D             D D",
                        "D D             D D",
                        "                   ",
                        "                   "
                },{
                        "                   ",
                        "D D             D D",
                        "D DCCCCCCCCCCCCCD D",
                        " ADA     C     ADA ",
                        " ADAA    C    AADA ",
                        " ADAEEEEAAAEEEEADA ",
                        "DADAEEEAA~AAEEEADAD",
                        " ADAEEEEAAAEEEEADA ",
                        " ADAA    C    AADA ",
                        " ADA     C     ADA ",
                        "D DCCCCCCCCCCCCCD D",
                        "D D             D D",
                        "                   "
                },{
                        "D D             D D",
                        "D D             D D",
                        "DADA           ADAD",
                        " ADAFFFFFFFFFFFADA ",
                        " ADAFFFFFFFFFFFADA ",
                        " AB             BA ",
                        "DAB             BAD",
                        " AB             BA ",
                        " ADAFFFFFFFFFFFADA ",
                        " ADAFFFFFFFFFFFADA ",
                        "DADA           ADAD",
                        "D D             D D",
                        "D D             D D"
                },{
                        "DDD             DDD",
                        "DADA           ADAD",
                        " ADAFFFFFFFFFFFADA ",
                        "DAB             BAD",
                        " AB             BA ",
                        "DAB             BAD",
                        "DAB             BAD",
                        "DAB             BAD",
                        " AB             BA ",
                        "DAB             BAD",
                        " ADAFFFFFFFFFFFADA ",
                        "DADA           ADAD",
                        "DDD             DDD"
                },{
                        "D DD           DD D",
                        " ADAAAA     AAAADA ",
                        " ADAFFFFFFFFFFFADA ",
                        " AB             BA ",
                        "DAB             BAD",
                        "DAB             BAD",
                        "DAB             BAD",
                        "DAB             BAD",
                        "DAB             BAD",
                        " AB             BA ",
                        " ADAFFFFFFFFFFFADA ",
                        " ADAAAA     AAAADA ",
                        "D DD           DD D"
                },{
                        "D  D           D  D",
                        " AAD           DAA ",
                        " ADAFFFFFFFFFFFADA ",
                        " AB             BA ",
                        "DAB             BAD",
                        "DAB             BAD",
                        "DAB             BAD",
                        "DAB             BAD",
                        "DAB             BAD",
                        " AB             BA ",
                        " ADAFFFFFFFFFFFADA ",
                        " AAD           DAA ",
                        "D  D           D  D"
                },{
                        "D DD           DD D",
                        " ADAAAA     AAAADA ",
                        " ADAFFFFFFFFFFFADA ",
                        " AB             BA ",
                        "DAB             BAD",
                        "DAB             BAD",
                        "DAB             BAD",
                        "DAB             BAD",
                        "DAB             BAD",
                        " AB             BA ",
                        " ADAFFFFFFFFFFFADA ",
                        " ADAAAA     AAAADA ",
                        "D DD           DD D"
                },{
                        "DDD             DDD",
                        "DADA           ADAD",
                        " ADAFFFFFFFFFFFADA ",
                        "DAB             BAD",
                        " AB             BA ",
                        "DAB             BAD",
                        "DAB             BAD",
                        "DAB             BAD",
                        " AB             BA ",
                        "DAB             BAD",
                        " ADAFFFFFFFFFFFADA ",
                        "DADA           ADAD",
                        "DDD             DDD"
                },{
                        "D D             D D",
                        "D D             D D",
                        "DADA           ADAD",
                        " ADAFFFFFFFFFFFADA ",
                        " ADAFFFFFFFFFFFADA ",
                        " AB             BA ",
                        "DAB             BAD",
                        " AB             BA ",
                        " ADAFFFFFFFFFFFADA ",
                        " ADAFFFFFFFFFFFADA ",
                        "DADA           ADAD",
                        "D D             D D",
                        "D D             D D"
                },{
                        "                   ",
                        "D D             D D",
                        "D DCCCCCCCCCCCCCD D",
                        " ADA           ADA ",
                        " ADAA  EEEEE  AADA ",
                        " ADAEEEEEEEEEEEADA ",
                        "DADAEHEEEHEEEHEADAD",
                        " ADAEEEEEEEEEEEADA ",
                        " ADAA  EEEEE  AADA ",
                        " ADA           ADA ",
                        "D DCCCCCCCCCCCCCD D",
                        "D D             D D",
                        "                   "
                },{
                        "                   ",
                        "                   ",
                        "D D             D D",
                        "D D             D D",
                        " AD             DA ",
                        " ADAEEE EEE EEEADA ",
                        "DAAAEHE EHE EHEAAAD",
                        " ADAEEE EEE EEEADA ",
                        " AD             DA ",
                        "D D             D D",
                        "D D             D D",
                        "                   ",
                        "                   "
                },{
                        "                   ",
                        "                   ",
                        "                   ",
                        "                   ",
                        "D D             D D",
                        "DDDD           DDDD",
                        "D  D           D  D",
                        "DDDD           DDDD",
                        "D D             D D",
                        "                   ",
                        "                   ",
                        "                   ",
                        "                   "
                }})
        //spotless:on
        .addElement(
            'H',
            buildHatchAdder(MTELocalityLockingLattice.class).atLeast(InputBus, InputHatch, OutputHatch, Energy)
                .casingIndex(((BlockCasings3) GregTechAPI.sBlockCasings3).getTextureIndex(12))
                .dot(1)
                .buildAndChain(
                    onElementPass(MTELocalityLockingLattice::onCasingAdded, ofBlock(GregTechAPI.sBlockCasings3, 12))))
        .addElement('A', ofBlock(GregTechAPI.sBlockCasings10, 3))
        .addElement('B', ofBlock(GregTechAPI.sBlockCasings9, 7))
        .addElement('C', ofFrame(Materials.Enderium))
        .addElement('D', Casings.CyclotronOuterCasing.asElement())
        .addElement('E', Casings.MatterRoutingCasing.asElement())
        .addElement('F', chainAllGlasses())
        .build();

    public MTELocalityLockingLattice(final int aID, final String aName, final String aNameRegional) {
        super(aID, aName, aNameRegional);
    }

    public MTELocalityLockingLattice(String aName) {
        super(aName);
    }

    @Override
    public IStructureDefinition<MTELocalityLockingLattice> getStructureDefinition() {
        return STRUCTURE_DEFINITION;
    }

    @Override
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new MTELocalityLockingLattice(this.mName);
    }

    @Override
    public ITexture[] getTexture(IGregTechTileEntity baseMetaTileEntity, ForgeDirection side, ForgeDirection aFacing,
        int colorIndex, boolean aActive, boolean redstoneLevel) {
        ITexture[] rTexture;
        if (side == aFacing) {
            if (aActive) {
                rTexture = new ITexture[] {
                    Textures.BlockIcons
                        .getCasingTextureForId(GTUtility.getCasingTextureIndex(GregTechAPI.sBlockCasings10, 15)),
                    TextureFactory.builder()
                        .addIcon(OVERLAY_FRONT_MULTI_BREWERY_ACTIVE)
                        .extFacing()
                        .build(),
                    TextureFactory.builder()
                        .addIcon(OVERLAY_FRONT_MULTI_BREWERY_ACTIVE_GLOW)
                        .extFacing()
                        .glow()
                        .build() };
            } else {
                rTexture = new ITexture[] {
                    Textures.BlockIcons
                        .getCasingTextureForId(GTUtility.getCasingTextureIndex(GregTechAPI.sBlockCasings10, 15)),
                    TextureFactory.builder()
                        .addIcon(OVERLAY_FRONT_MULTI_BREWERY)
                        .extFacing()
                        .build(),
                    TextureFactory.builder()
                        .addIcon(OVERLAY_FRONT_MULTI_BREWERY_GLOW)
                        .extFacing()
                        .glow()
                        .build() };
            }
        } else {
            rTexture = new ITexture[] { Textures.BlockIcons
                .getCasingTextureForId(GTUtility.getCasingTextureIndex(GregTechAPI.sBlockCasings10, 15)) };
        }
        return rTexture;
    }

    @Override
    protected MultiblockTooltipBuilder createTooltip() {
        MultiblockTooltipBuilder tt = new MultiblockTooltipBuilder();
        tt.addMachineType("Brewery, BBB")
            .addBulkMachineInfo(4, 1.5F, 1F)
            .beginStructureBlock(3, 5, 3, true)
            .addController("Front Center")
            .addCasingInfoMin("Reinforced Wooden Casing", 14, false)
            .addCasingInfoExactly("Any Tiered Glass", 6, false)
            .addCasingInfoExactly("Steel Frame Box", 4, false)
            .addInputBus("Any Wooden Casing", 1)
            .addOutputBus("Any Wooden Casing", 1)
            .addInputHatch("Any Wooden Casing", 1)
            .addOutputHatch("Any Wooden Casing", 1)
            .addEnergyHatch("Any Wooden Casing", 1)
            .addMaintenanceHatch("Any Wooden Casing", 1)
            .addSubChannelUsage(GTStructureChannels.BOROGLASS)
            .toolTipFinisher();
        return tt;
    }

    @Override
    public void construct(ItemStack stackSize, boolean hintsOnly) {
        buildPiece(STRUCTURE_PIECE_MAIN, stackSize, hintsOnly, 9, 6, 2);
    }

    @Override
    public int survivalConstruct(ItemStack stackSize, int elementBudget, ISurvivalBuildEnvironment env) {
        if (mMachine) return -1;
        return survivalBuildPiece(STRUCTURE_PIECE_MAIN, stackSize, 9, 6, 2, elementBudget, env, false, true);
    }

    private int mCasingAmount;

    private void onCasingAdded() {
        mCasingAmount++;
    }

    @Override
    public boolean checkMachine(IGregTechTileEntity aBaseMetaTileEntity, ItemStack aStack) {
        mCasingAmount = 0;
        return checkPiece(STRUCTURE_PIECE_MAIN, 9, 6, 2);
    }

    @Override
    protected ProcessingLogic createProcessingLogic() {
        return new ProcessingLogic().setSpeedBonus(1F / 1.5F)
            .setMaxParallelSupplier(this::getTrueParallel);
    }

    @Override
    public int getMaxParallelRecipes() {
        return (4 * GTUtility.getTier(this.getMaxInputVoltage()));
    }

    @Override
    public RecipeMap<?> getRecipeMap() {
        return RecipeMaps.brewingRecipes;
    }

    @Override
    public boolean supportsVoidProtection() {
        return true;
    }

    @Override
    public boolean supportsBatchMode() {
        return true;
    }

    @Override
    public boolean supportsInputSeparation() {
        return true;
    }

    @Override
    public boolean supportsSingleRecipeLocking() {
        return true;
    }

    @Override
    public boolean getDefaultHasMaintenanceChecks() {
        return false;
    }
}
