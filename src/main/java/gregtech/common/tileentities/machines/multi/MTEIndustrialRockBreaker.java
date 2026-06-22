package gregtech.common.tileentities.machines.multi;

import static com.gtnewhorizon.structurelib.structure.StructureUtility.isAir;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.ofBlockAnyMeta;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.ofChain;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.onElementPass;
import static gregtech.api.enums.HatchElement.Energy;
import static gregtech.api.enums.HatchElement.InputBus;
import static gregtech.api.enums.HatchElement.InputHatch;
import static gregtech.api.enums.HatchElement.Maintenance;
import static gregtech.api.enums.HatchElement.Muffler;
import static gregtech.api.enums.HatchElement.OutputBus;
import static gregtech.api.util.GTStructureUtility.buildHatchAdder;
import static gregtech.api.util.GTStructureUtility.chainAllGlasses;
import static gregtech.api.util.GTStructureUtility.ofAnyWater;
import static gregtech.api.util.GTStructureUtility.ofFrame;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

import com.gtnewhorizon.structurelib.alignment.constructable.ISurvivalConstructable;
import com.gtnewhorizon.structurelib.structure.IStructureDefinition;
import com.gtnewhorizon.structurelib.structure.ISurvivalBuildEnvironment;
import com.gtnewhorizon.structurelib.structure.StructureDefinition;

import gregtech.api.casing.Casings;
import gregtech.api.enums.Materials;
import gregtech.api.enums.SoundResource;
import gregtech.api.enums.Textures;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.ICasingTextureProvider;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.logic.ProcessingLogic;
import gregtech.api.metatileentity.implementations.MTEExtendedPowerMultiBlockBase;
import gregtech.api.recipe.RecipeMap;
import gregtech.api.structure.error.StructureError;
import gregtech.api.util.GTStructureUtility;
import gregtech.api.util.GTUtility;
import gregtech.api.util.MultiblockTooltipBuilder;
import gregtech.common.misc.GTStructureChannels;
import gregtech.common.pollution.PollutionConfig;
import gtPlusPlus.api.recipe.GTPPRecipeMaps;
import gtPlusPlus.xmod.gregtech.common.blocks.textures.TexturesGtBlock;

public class MTEIndustrialRockBreaker extends MTEExtendedPowerMultiBlockBase<MTEIndustrialRockBreaker>
    implements ISurvivalConstructable, ICasingTextureProvider {

    private static final String STRUCTURE_PIECE_MAIN = "main";
    private static final int OFFSET_X = 3;
    private static final int OFFSET_Y = 2;
    private static final int OFFSET_Z = 1;

    private int casingAmount;
    private boolean needsFluidRefill = false;

    private static final String[][] structure = new String[][] {
        { "  ACA  ", "       ", "       ", "       ", "  ACA  " },
        { "CACCCAC", "ABACABA", "ABA~ABA", "ABACABA", "CCCCCCC" },
        { "CCCCCCC", "CEC CDC", "CEC CDC", "CEC CDC", "CCCCCCC" },
        { "CACCCAC", "ABACABA", "ABACABA", "ABACABA", "CCCCCCC" },
        { "  ACA  ", "       ", "       ", "       ", "  ACA  " } };

    private static final IStructureDefinition<MTEIndustrialRockBreaker> STRUCTURE_DEFINITION = StructureDefinition
        .<MTEIndustrialRockBreaker>builder()
        .addShape(STRUCTURE_PIECE_MAIN, structure)
        .addElement('A', ofFrame(Materials.Tungsten))
        .addElement('B', chainAllGlasses())
        .addElement(
            'C',
            buildHatchAdder(MTEIndustrialRockBreaker.class)
                .atLeast(InputBus, InputHatch, OutputBus, Maintenance, Energy, Muffler)
                .casingIndex(Casings.ThermalProcessingCasing.textureId)
                .hint(1)
                .buildAndChain(onElementPass(x -> ++x.casingAmount, Casings.ThermalProcessingCasing.asElement())))
        .addElement('D', ofChain(ofBlockAnyMeta(Blocks.lava, 1), isAir()))
        .addElement('E', ofChain(ofAnyWater(false), isAir()))
        .build();

    public MTEIndustrialRockBreaker(final int aID, final String aName, final String aNameRegional) {
        super(aID, aName, aNameRegional);
    }

    public MTEIndustrialRockBreaker(final String aName) {
        super(aName);
    }

    @Override
    public IMetaTileEntity newMetaEntity(final IGregTechTileEntity aTileEntity) {
        return new MTEIndustrialRockBreaker(this.mName);
    }

    @Override
    protected MultiblockTooltipBuilder createTooltip() {
        MultiblockTooltipBuilder tt = new MultiblockTooltipBuilder();
        tt.addMachineType("Rock Breaker")
            .addBulkMachineInfo(8, 3f, 0.75f)
            .addInfo("Use Integrated Circuit to determine recipe")
            .addInfo("1 = Cobble, 2 = Stone, 3 = Obsidian, 4 = Basalt, 5 = Deepslate, 6 = Netherrack, 7 = Endstone")
            .addInfo("Needs Soul Sand and Blue Ice in input bus for basalt")
            .addInfo("Needs Soul Sand and Magma in input bus for deepslate")
            .addPollutionAmount(getPollutionPerSecond(null))
            .beginStructureBlock(7, 5, 5, false)
            .addController("Front center")
            .addCasingInfoMin("Thermal Processing Casing", 50, false)
            .addCasingInfoExactly("Tungsten Frame Box", 36, false)
            .addCasingInfoExactly("Any Tiered Glass", 12, false)
            .addInputBus("Any Thermal Processing Casing", 1)
            .addInputHatch("Any Thermal Processing Casing", 1)
            .addOutputBus("Any Thermal Processing Casing", 1)
            .addEnergyHatch("Any Thermal Processing Casing", 1)
            .addMaintenanceHatch("Any Thermal Processing Casing", 1)
            .addMufflerHatch("Any Thermal Processing Casing", 1)
            .addSubChannelUsage(GTStructureChannels.BOROGLASS)
            .addStructureAuthors(EnumChatFormatting.GOLD + "VorTex")
            .toolTipFinisher();
        return tt;
    }

    @Override
    public IStructureDefinition<MTEIndustrialRockBreaker> getStructureDefinition() {
        return STRUCTURE_DEFINITION;
    }

    @Override
    public void construct(ItemStack stackSize, boolean hintsOnly) {
        buildPiece(STRUCTURE_PIECE_MAIN, stackSize, hintsOnly, OFFSET_X, OFFSET_Y, OFFSET_Z);
    }

    @Override
    public int survivalConstruct(ItemStack stackSize, int elementBudget, ISurvivalBuildEnvironment env) {
        if (mMachine) return -1;
        return survivalBuildPiece(
            STRUCTURE_PIECE_MAIN,
            stackSize,
            OFFSET_X,
            OFFSET_Y,
            OFFSET_Z,
            elementBudget,
            env,
            false,
            true);
    }

    @Override
    public void checkMachine(IGregTechTileEntity aBaseMetaTileEntity, ItemStack aStack, List<StructureError> errors) {
        casingAmount = 0;
        if (!checkPiece(STRUCTURE_PIECE_MAIN, OFFSET_X, OFFSET_Y, OFFSET_Z, errors)) {
            needsFluidRefill = GTStructureUtility.hasWaterAtStructurePosition(
                aBaseMetaTileEntity,
                getExtendedFacing(),
                structure,
                OFFSET_X,
                OFFSET_Y,
                OFFSET_Z,
                'E');
            return;
        }
        checkCasingMin(errors, casingAmount, 50);
        checkHasEnergyHatch(errors);
        checkHasMaintenanceHatch(errors);
        checkHasMufflerHatch(errors);
        checkHasOutputBus(errors);
        if (errors.isEmpty()) needsFluidRefill = true;
    }

    @Override
    protected SoundResource getProcessStartSound() {
        return SoundResource.IC2_MACHINES_INDUCTION_LOOP;
    }

    @Override
    public ITexture[] getTexture(IGregTechTileEntity aBaseMetaTileEntity, ForgeDirection side, ForgeDirection aFacing,
        int colorIndex, boolean aActive, boolean redstoneLevel) {
        return Textures.BlockIcons.createTextureWithCasing(
            this,
            side,
            aFacing,
            aActive,
            TexturesGtBlock.oMCAIndustrialRockBreaker,
            TexturesGtBlock.oMCAIndustrialRockBreakerGlow,
            TexturesGtBlock.oMCAIndustrialRockBreakerActive,
            TexturesGtBlock.oMCAIndustrialRockBreakerActiveGlow);
    }

    @Override
    public ITexture getCasingTexture() {
        return Casings.ThermalProcessingCasing.getCasingTexture();
    }

    @Override
    public RecipeMap<?> getRecipeMap() {
        return GTPPRecipeMaps.multiblockRockBreakerRecipes;
    }

    @Override
    protected boolean filtersFluid() {
        return false;
    }

    @Override
    protected ProcessingLogic createProcessingLogic() {
        return new ProcessingLogic().setSpeedBonus(1 / 3.0)
            .setEuModifier(0.75)
            .setMaxParallelSupplier(this::getTrueParallel);

    }

    @Override
    public int getMaxParallelRecipes() {
        return 8 * GTUtility.getTier(this.getMaxInputVoltage());
    }

    @Override
    public int getPollutionPerSecond(final ItemStack aStack) {
        return PollutionConfig.pollutionPerSecondMultiIndustrialRockBreaker;
    }

    @Override
    public void getWailaNBTData(EntityPlayerMP player, TileEntity tile, NBTTagCompound tag, World world, int x, int y,
        int z) {
        super.getWailaNBTData(player, tile, tag, world, x, y, z);
        tag.setInteger("maxParallelRecipes", getMaxParallelRecipes());
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
    public boolean supportsVoidProtection() {
        return true;
    }

    @Override
    public boolean supportsBatchMode() {
        return true;
    }

    @Override
    public void onPostTick(IGregTechTileEntity aBaseMetaTileEntity, long aTick) {
        super.onPostTick(aBaseMetaTileEntity, aTick);
        if (aBaseMetaTileEntity.isServerSide() && needsFluidRefill && aTick % 20 == 0) {
            World world = aBaseMetaTileEntity.getWorld();
            boolean allFilled = true;
            int controllerX = aBaseMetaTileEntity.getXCoord();
            int controllerY = aBaseMetaTileEntity.getYCoord();
            int controllerZ = aBaseMetaTileEntity.getZCoord();

            for (int sliceZ = 0; sliceZ < structure.length; sliceZ++) {
                String[] layers = structure[sliceZ];
                for (int layerY = 0; layerY < layers.length; layerY++) {
                    String row = layers[layerY];
                    for (int charX = 0; charX < row.length(); charX++) {
                        char c = row.charAt(charX);
                        if (c != 'E' && c != 'D') continue;

                        int[] abc = new int[] { charX - OFFSET_X, layerY - OFFSET_Y, sliceZ - OFFSET_Z };
                        int[] xyz = new int[] { 0, 0, 0 };
                        this.getExtendedFacing()
                            .getWorldOffset(abc, xyz);
                        int wx = controllerX + xyz[0];
                        int wy = controllerY + xyz[1];
                        int wz = controllerZ + xyz[2];
                        Block block = world.getBlock(wx, wy, wz);

                        if (c == 'E') {
                            if (GTUtility.canReplaceBlockWithWater(world, wx, wy, wz)) {
                                world.setBlock(wx, wy, wz, Blocks.water, 0, 3);
                            } else if (!GTUtility.isSourceWater(block, world, wx, wy, wz)) {
                                allFilled = false;
                            }
                        } else {
                            if (block != Blocks.lava) {
                                if (block == Blocks.air || block == Blocks.flowing_lava
                                    || block.isReplaceable(world, wx, wy, wz)) {
                                    world.setBlock(wx, wy, wz, Blocks.lava, 0, 3);
                                } else {
                                    allFilled = false;
                                }
                            }
                        }
                    }
                }
            }
            if (allFilled) needsFluidRefill = false;
        }
    }
}
