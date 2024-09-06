package gregtech.common.tileentities.machines.multi;

import static com.gtnewhorizon.structurelib.structure.StructureUtility.ofBlock;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.onElementPass;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.transpose;
import static gregtech.api.enums.GTValues.AuthorTotto;
import static gregtech.api.enums.HatchElement.*;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_METEOR_MINER;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_METEOR_MINER_ACTIVE;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_METEOR_MINER_ACTIVE_GLOW;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_METEOR_MINER_GLOW;
import static gregtech.api.util.GTStructureUtility.buildHatchAdder;
import static gregtech.api.util.GTStructureUtility.ofFrame;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;

import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.common.util.ForgeDirection;

import org.jetbrains.annotations.NotNull;

import com.gtnewhorizon.structurelib.alignment.IAlignmentLimits;
import com.gtnewhorizon.structurelib.alignment.constructable.ISurvivalConstructable;
import com.gtnewhorizon.structurelib.structure.IStructureDefinition;
import com.gtnewhorizon.structurelib.structure.ISurvivalBuildEnvironment;
import com.gtnewhorizon.structurelib.structure.StructureDefinition;

import gregtech.api.GregTechAPI;
import gregtech.api.enums.Materials;
import gregtech.api.enums.TAE;
import gregtech.api.enums.Textures;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.implementations.MTEEnhancedMultiBlockBase;
import gregtech.api.metatileentity.implementations.MTEHatchEnergy;
import gregtech.api.multitileentity.multiblock.casing.Glasses;
import gregtech.api.recipe.check.CheckRecipeResult;
import gregtech.api.recipe.check.SimpleCheckRecipeResult;
import gregtech.api.render.TextureFactory;
import gregtech.api.util.GTUtility;
import gregtech.api.util.MultiblockTooltipBuilder;
import gregtech.api.util.shutdown.ShutDownReasonRegistry;
import gtPlusPlus.core.block.ModBlocks;

public class MTEMeteorMiner extends MTEEnhancedMultiBlockBase<MTEMeteorMiner> implements ISurvivalConstructable {

    private static final String STRUCTURE_PIECE_MAIN = "main";
    private static IStructureDefinition<MTEMeteorMiner> STRUCTURE_DEFINITION = null;
    private static final int BASE_CASING_COUNT = 469;
    private static final int MAX_RADIUS = 24;
    private int currentRadius = MAX_RADIUS;
    private int xDrill, yDrill, zDrill;
    private int xStart, yStart, zStart;
    private boolean isStartInitialized = false;
    private boolean hasFinished = true;
    private boolean isObserving = true;
    Collection<ItemStack> res = new HashSet<>();

    @Override
    public IStructureDefinition<MTEMeteorMiner> getStructureDefinition() {
        if (STRUCTURE_DEFINITION == null) {
            STRUCTURE_DEFINITION = StructureDefinition.<MTEMeteorMiner>builder()
                .addShape(
                    STRUCTURE_PIECE_MAIN,
                    (transpose(
                        // spotless:off
                new String[][]{
                    {"               ","               ","               ","               ","               ","               ","       D       ","      D D      ","       D       ","               ","               ","               ","               ","               ","               "},
                    {"               ","               ","               ","               ","               ","       D       ","      D D      ","     D   D     ","      D D      ","       D       ","               ","               ","               ","               ","               "},
                    {"               ","               ","               ","               ","       D       ","     D   D     ","               ","    D     D    ","               ","     D   D     ","       D       ","               ","               ","               ","               "},
                    {"               ","               ","               ","       D       ","    D     D    ","               ","               ","   D   B   D   ","               ","               ","    D     D    ","       D       ","               ","               ","               "},
                    {"               ","               ","       D       ","   D       D   ","               ","               ","               ","  D    B    D  ","               ","               ","               ","   D       D   ","       D       ","               ","               "},
                    {"               ","       D       ","  D         D  ","               ","               ","               ","       C       "," D    CBC    D ","       C       ","               ","               ","               ","  D         D  ","       D       ","               "},
                    {"  DDDDDDDDDDD  "," DDFFFFFFFFFDD ","DDFF       FFDD","DFF         FFD","DF           FD","DF           FD","DF     C     FD","DF    CBC    FD","DF     C     FD","DF           FD","DF           FD","DFF         FFD","DDFF       FFDD"," DDFFFFFFFFFDD ","  DDDDDDDDDDD  "},
                    {"               ","       D       ","    FFFFFFF    ","   FF     FF   ","  FF       FF  ","  F         F  ","  F    C    F  "," DF   CBC   FD ","  F    C    F  ","  F         F  ","  FF       FF  ","   FF     FF   ","    FFFFFFF    ","       D       ","               "},
                    {"               ","               ","       D       ","     FFFFF     ","    FF   FF    ","   FF  C  FF   ","   F  CCC  F   ","  DF CCBCC FD  ","   F  CCC  F   ","   FF  C  FF   ","    FF   FF    ","     FFFFF     ","       D       ","               ","               "},
                    {"               ","               ","               ","       D       ","      FFF      ","     FFFFF     ","    FFFFFFF    ","   DFFFGFFFD   ","    FFFFFFF    ","     FFFFF     ","      FFF      ","       D       ","               ","               ","               "},
                    {"               ","               ","               ","               ","       D       ","      DDD      ","     DEEED     ","    DDEGEDD    ","     DEEED     ","      DDD      ","       D       ","               ","               ","               ","               "},
                    {"               ","               ","               ","               ","               ","               ","      EEE      ","      EGE      ","      EEE      ","               ","               ","               ","               ","               ","               "},
                    {"               ","               ","               ","               ","               ","               ","      EEE      ","      EGE      ","      EEE      ","               ","               ","               ","               ","               ","               "},
                    {"               ","               ","               ","               ","               ","               ","     EEAEE     ","     EEGEE     ","     EEEEE     ","      EEE      ","               ","               ","               ","               ","               "},
                    {"               ","               ","               ","               ","               ","               ","    EE A EE    ","    EEAGAEE    ","    EE A EE    ","      EEE      ","      EEE      ","               ","               ","               ","               "},
                    {"               ","               ","               ","               ","       J       ","       A       ","   EE  A  EE   ","   EE AGA EE   ","   EE  A  EE   ","               ","      EEE      ","      EEE      ","               ","               ","               "},
                    {"               ","               ","               ","               ","      J~J      ","      AGA      ","  EE  AGA  EE  ","  EE  AGA  EE  ","  EE   A   EE  ","               ","               ","      EEE      ","      EEE      ","               ","               "},
                    {"               ","               ","               ","               ","       I       ","       A       "," HHH   A   HHH "," HHH   A   HHH "," HHH       HHH ","               ","               ","      HHH      ","      HHH      ","      HHH      ","               "}
                // spotless:on
                        })))
                .addElement('A', Glasses.chainAllGlasses())
                .addElement('B', ofBlock(GregTechAPI.sBlockCasings1, 15))
                .addElement('C', ofBlock(GregTechAPI.sBlockCasings5, 5))
                .addElement('D', ofFrame(Materials.StainlessSteel))
                .addElement(
                    'H',
                    buildHatchAdder(MTEMeteorMiner.class).atLeast(OutputBus, Energy)
                        .casingIndex(TAE.getIndexFromPage(0, 10))
                        .dot(1)
                        .buildAndChain(
                            onElementPass(
                                MTEMeteorMiner::onCasingAdded,
                                ofBlock(ModBlocks.blockSpecialMultiCasings, 6))))
                .addElement('F', ofBlock(ModBlocks.blockSpecialMultiCasings, 8))
                .addElement('G', ofBlock(GregTechAPI.sBlockCasings1, 15))
                .addElement('E', ofBlock(ModBlocks.blockSpecialMultiCasings, 6))
                .addElement(
                    'I',
                    buildHatchAdder(MTEMeteorMiner.class).atLeast(Maintenance)
                        .casingIndex(TAE.getIndexFromPage(0, 8))
                        .dot(2)
                        .buildAndChain(
                            onElementPass(MTEMeteorMiner::onCasingAdded, ofBlock(ModBlocks.blockCasings4Misc, 2))))
                .addElement('J', ofBlock(ModBlocks.blockCasings4Misc, 2))
                .build();
        }
        return STRUCTURE_DEFINITION;
    }

    @Override
    protected IAlignmentLimits getInitialAlignmentLimits() {
        return (d, r, f) -> (d.flag & (ForgeDirection.UP.flag | ForgeDirection.DOWN.flag)) == 0 && r.isNotRotated()
            && !f.isVerticallyFliped();
    }

    public MTEMeteorMiner(int aID, String aName, String aNameRegional) {
        super(aID, aName, aNameRegional);
    }

    public MTEMeteorMiner(String aName) {
        super(aName);
    }

    protected int aCasingAmount;

    @Override
    public void clearHatches() {
        super.clearHatches();

        aCasingAmount = 0;
    }

    private void onCasingAdded() {
        aCasingAmount++;
    }

    @Override
    public void construct(ItemStack stackSize, boolean hintsOnly) {
        buildPiece(STRUCTURE_PIECE_MAIN, stackSize, hintsOnly, 7, 16, 4);
    }

    @Override
    public int survivalConstruct(ItemStack stackSize, int elementBudget, ISurvivalBuildEnvironment env) {
        if (mMachine) return -1;
        return survivialBuildPiece(STRUCTURE_PIECE_MAIN, stackSize, 7, 16, 4, elementBudget, env, false, true);
    }

    @Override
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new MTEMeteorMiner(this.mName);
    }

    @Override
    public ITexture[] getTexture(IGregTechTileEntity baseMetaTileEntity, ForgeDirection side, ForgeDirection aFacing,
        int colorIndex, boolean aActive, boolean redstoneLevel) {
        ITexture[] rTexture;
        if (side == aFacing) {
            if (aActive) {
                rTexture = new ITexture[] { Textures.BlockIcons.getCasingTextureForId(TAE.getIndexFromPage(0, 8)),
                    TextureFactory.builder()
                        .addIcon(OVERLAY_FRONT_METEOR_MINER_ACTIVE)
                        .extFacing()
                        .build(),
                    TextureFactory.builder()
                        .addIcon(OVERLAY_FRONT_METEOR_MINER_ACTIVE_GLOW)
                        .extFacing()
                        .glow()
                        .build() };
            } else {
                rTexture = new ITexture[] { Textures.BlockIcons.getCasingTextureForId(TAE.getIndexFromPage(0, 8)),
                    TextureFactory.builder()
                        .addIcon(OVERLAY_FRONT_METEOR_MINER)
                        .extFacing()
                        .build(),
                    TextureFactory.builder()
                        .addIcon(OVERLAY_FRONT_METEOR_MINER_GLOW)
                        .extFacing()
                        .glow()
                        .build() };
            }
        } else {
            rTexture = new ITexture[] { Textures.BlockIcons.getCasingTextureForId(TAE.getIndexFromPage(0, 8)) };
        }
        return rTexture;
    }

    @Override
    protected MultiblockTooltipBuilder createTooltip() {
        MultiblockTooltipBuilder tt = new MultiblockTooltipBuilder();
        tt.addMachineType("Miner")
            .addInfo("Controller Block for the Meteor Miner!")
            .addInfo(
                "To work properly the Superconducting Coils must be placed 32 blocks below the center of the meteor,")
            .addInfo("it will mine in a radius of 24 blocks in each direction from the center of the meteor.")
            .addInfo("All the chunks involved must be chunkloaded.")
            .addInfo("" + EnumChatFormatting.BLUE + EnumChatFormatting.BOLD + "Finally some good Meteors!")
            .addInfo(AuthorTotto)
            .addSeparator()
            .beginStructureBlock(15, 18, 15, false)
            .addController("Second Layer Center")
            .addOutputBus("Any Structural Solar Casing", 1)
            .addEnergyHatch("Any Structural Solar Casing", 1)
            .addMaintenanceHatch("Below the Controller", 2)
            .toolTipFinisher("GregTech");
        return tt;
    }

    @Override
    public boolean isCorrectMachinePart(ItemStack aStack) {
        return true;
    }

    @Override
    public boolean checkMachine(IGregTechTileEntity aBaseMetaTileEntity, ItemStack aStack) {
        aCasingAmount = 0;
        return checkPiece(STRUCTURE_PIECE_MAIN, 7, 16, 4) && !mEnergyHatches.isEmpty()
            && mMaintenanceHatches.size() == 1;
    }

    @Override
    public int getMaxEfficiency(ItemStack aStack) {
        return 10000;
    }

    @Override
    public int getDamageToComponent(ItemStack aStack) {
        return 0;
    }

    @Override
    public boolean explodesOnComponentBreak(ItemStack aStack) {
        return false;
    }

    protected int getBaseProgressTime() {
        return 480;
    };

    protected int getXDrill() {
        return xDrill;
    }

    protected int getYDrill() {
        return yDrill;
    }

    protected int getZDrill() {
        return zDrill;
    }

    /**
     * Sets the coordinates of the center to the max range meteor center
     * 
     */
    private void setStartCoords() {
        xStart = 0 * getExtendedFacing().getRelativeBackInWorld().offsetX + getBaseMetaTileEntity().getXCoord();
        zStart = 3 * getExtendedFacing().getRelativeBackInWorld().offsetZ + getBaseMetaTileEntity().getZCoord();
        yStart = 48 + getBaseMetaTileEntity().getYCoord();
    }

    private void initializeDrillPos() {
        this.xDrill = this.xStart - currentRadius;
        this.yDrill = this.yStart;
        this.zDrill = this.zStart - currentRadius;

        this.isStartInitialized = true;
        this.hasFinished = false;
    }

    @Override
    @NotNull
    public CheckRecipeResult checkProcessing() {
        setElectricityStats();
        if (!isEnergyEnough()) {
            stopMachine(ShutDownReasonRegistry.NONE);
            return SimpleCheckRecipeResult.ofFailure("not_enough_energy");
        }

        if (!isStartInitialized) {
            this.setStartCoords();
            this.initializeDrillPos();
        }

        if (!hasFinished) {
            this.startMining(this.xDrill, this.zDrill);
            mOutputItems = res.toArray(new ItemStack[0]);
            res.clear();
            this.moveToNextColumn();
        } else {
            this.isStartInitialized = false;
            stopMachine(ShutDownReasonRegistry.NONE);
            return SimpleCheckRecipeResult.ofFailure("drill_exhausted");
        }

        return SimpleCheckRecipeResult.ofSuccess("meteor_mining");
    }

    /*
     * TODO
     * Drop blocks instead of ores UNDOABLE
     * Check if block is breakable
     * Redstone compatibility
     * Restart Button
     * Parallels
     * Drones in input
     */
    private void startMining(int currentX, int currentZ) {
        if (getBaseMetaTileEntity().getWorld()
            .isAirBlock(currentX, this.yStart, currentZ)) {
            return;
        }

        for (int y = -currentRadius; y <= currentRadius; y++) {

            int currentY = this.yStart + y;

            if (!getBaseMetaTileEntity().getWorld()
                .isAirBlock(currentX, currentY, currentZ)) {

                Block target = getBaseMetaTileEntity().getBlock(currentX, currentY, currentZ);

                if (target.getBlockHardness(getBaseMetaTileEntity().getWorld(), currentX, currentY, currentZ) > 0) {

                    final int blockMeta = getBaseMetaTileEntity().getMetaID(currentX, currentY, currentZ);

                    addToOutput(
                        target
                            .getDrops(getBaseMetaTileEntity().getWorld(), currentX, currentY, currentZ, blockMeta, 3));

                    getBaseMetaTileEntity().getWorld()
                        .setBlockToAir(currentX, currentY, currentZ);
                }

            }
        }
    }

    private void moveToNextColumn() {
        if (this.xDrill <= this.xStart + currentRadius) {
            this.xDrill++;
        } else if (this.zDrill <= this.zStart + currentRadius) {
            this.xDrill = this.xStart - currentRadius;
            this.zDrill++;
        } else {
            this.hasFinished = true;
        }
    }

    protected void setElectricityStats() {
        this.mOutputItems = new ItemStack[0];

        this.mEfficiency = getCurrentEfficiency(null);
        this.mEfficiencyIncrease = 10000;

        int tier = Math.max(1, GTUtility.getTier(getMaxInputVoltage()));
        this.mEUt = -3 * (1 << (tier << 1));
        this.mMaxProgresstime = isObserving ? 800 : calculateMaxProgressTime(tier);
    }

    private void reset() {
        this.isStartInitialized = false;
    }

    private int calculateMaxProgressTime(int tier) {
        return Math.max(1, getBaseProgressTime() / (2 << tier));
    }

    private boolean isEnergyEnough() {
        long requiredEnergy = 512 + getMaxInputVoltage() * 4;
        for (MTEHatchEnergy energyHatch : mEnergyHatches) {
            requiredEnergy -= energyHatch.getEUVar();
            if (requiredEnergy <= 0) return true;
        }
        return false;
    }

    private void addToOutput(ArrayList<ItemStack> drops) {
        ArrayList<ItemStack> newItems = new ArrayList<>();

        for (ItemStack d : drops) {
            boolean found = false;

            for (ItemStack r : res) {
                if (d.isItemEqual(r)) {
                    r.stackSize += d.stackSize; // Combine stack sizes
                    found = true;
                }
            }

            if (!found) {
                newItems.add(d);
            }
        }

        res.addAll(newItems);
    }

    private void findBestRadius() {
        currentRadius = MAX_RADIUS;
        int delta = 0;
        for (int zCoord = zDrill; zCoord > 0; zCoord++) {
            if (!getBaseMetaTileEntity().getWorld()
                .isAirBlock(xStart, yStart, zCoord)) {
                break;
            }
            delta++;
        }
        currentRadius -= delta;
        System.out.println("New Radius: " + currentRadius);
    }
}
