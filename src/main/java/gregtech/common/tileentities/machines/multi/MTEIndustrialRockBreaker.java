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
import static gregtech.api.util.GTStructureUtility.ofAnyWater;
import static gregtech.api.util.GTStructureUtility.ofFrame;

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

import cofh.asmhooks.block.BlockTickingWater;
import cofh.asmhooks.block.BlockWater;
import gregtech.api.GregTechAPI;
import gregtech.api.casing.Casings;
import gregtech.api.enums.Materials;
import gregtech.api.enums.Mods;
import gregtech.api.enums.SoundResource;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.logic.ProcessingLogic;
import gregtech.api.metatileentity.implementations.MTEExtendedPowerMultiBlockBase;
import gregtech.api.recipe.RecipeMap;
import gregtech.api.render.TextureFactory;
import gregtech.api.util.GTUtility;
import gregtech.api.util.MultiblockTooltipBuilder;
import gregtech.common.pollution.PollutionConfig;
import gtPlusPlus.api.recipe.GTPPRecipeMaps;
import gtPlusPlus.xmod.gregtech.common.blocks.textures.TexturesGtBlock;

public class MTEIndustrialRockBreaker extends MTEExtendedPowerMultiBlockBase<MTEIndustrialRockBreaker>
    implements ISurvivalConstructable {

    private static final String STRUCTURE_PIECE_MAIN = "main";
    private static final int OFFSET_X = 3;
    private static final int OFFSET_Y = 2;
    private static final int OFFSET_Z = 1;

    private int casingAmount;
    private static IStructureDefinition<MTEIndustrialRockBreaker> STRUCTURE_DEFINITION = null;
    private boolean needsFluidRefill = false;

    private static final String[][] structure = new String[][] {
        { "  ACA  ", "       ", "       ", "       ", "  ACA  " },
        { "CACCCAC", "ABACABA", "ABA~ABA", "ABACABA", "CCCCCCC" },
        { "CCCCCCC", "CEC CDC", "CEC CDC", "CEC CDC", "CCCCCCC" },
        { "CACCCAC", "ABACABA", "ABACABA", "ABACABA", "CCCCCCC" },
        { "  ACA  ", "       ", "       ", "       ", "  ACA  " } };

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
            .addInfo("1 = Cobble, 2 = Stone, 3 = Obsidian, 4 = Basalt, 5 = Deepslate, 6 = Netherrack")
            .addInfo("Needs Soul Sand and Blue Ice in input bus for basalt")
            .addInfo("Needs Soul Sand and Magma in input bus for deepslate")
            .addPollutionAmount(getPollutionPerSecond(null))
            .beginStructureBlock(7, 5, 5, false)
            .addController("Front center")
            .addCasingInfoMin("Thermal Processing Casing", 50, false)
            .addCasingInfoExactly("Tungsten Frame Box", 36, false)
            .addCasingInfoExactly("Any Tinted Industrial Glass", 12, false)
            .addInputBus("Any Thermal Processing Casing", 1)
            .addInputHatch("Any Thermal Processing Casing", 1)
            .addOutputBus("Any Thermal Processing Casing", 1)
            .addEnergyHatch("Any Thermal Processing Casing", 1)
            .addMaintenanceHatch("Any Thermal Processing Casing", 1)
            .addMufflerHatch("Any Thermal Processing Casing", 1)
            .addStructureAuthors(EnumChatFormatting.GOLD + "VorTex")
            .toolTipFinisher();
        return tt;
    }

    @Override
    public IStructureDefinition<MTEIndustrialRockBreaker> getStructureDefinition() {
        if (STRUCTURE_DEFINITION == null) {
            STRUCTURE_DEFINITION = StructureDefinition.<MTEIndustrialRockBreaker>builder()
                .addShape(STRUCTURE_PIECE_MAIN, structure)
                .addElement('A', ofFrame(Materials.Tungsten))
                .addElement('B', ofBlockAnyMeta(GregTechAPI.sBlockTintedGlass))
                .addElement(
                    'C',
                    buildHatchAdder(MTEIndustrialRockBreaker.class)
                        .atLeast(InputBus, InputHatch, OutputBus, Maintenance, Energy, Muffler)
                        .casingIndex(Casings.ThermalProcessingCasing.textureId)
                        .hint(1)
                        .buildAndChain(
                            onElementPass(x -> ++x.casingAmount, Casings.ThermalProcessingCasing.asElement())))
                .addElement('D', ofChain(isAir(), ofBlockAnyMeta(Blocks.lava, 1)))
                .addElement('E', ofChain(isAir(), ofAnyWater(false)))
                .build();
        }
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
    public boolean checkMachine(IGregTechTileEntity aBaseMetaTileEntity, ItemStack aStack) {
        casingAmount = 0;
        boolean valid = checkPiece(STRUCTURE_PIECE_MAIN, OFFSET_X, OFFSET_Y, OFFSET_Z) && casingAmount >= 50
            && checkHatch();
        if (valid) needsFluidRefill = true;
        return valid;
    }

    public boolean checkHatch() {
        return mEnergyHatches.size() >= 1 && mMufflerHatches.size() == 1 && mOutputBusses.size() >= 1;
    }

    @Override
    protected SoundResource getProcessStartSound() {
        return SoundResource.IC2_MACHINES_INDUCTION_LOOP;
    }

    @Override
    public ITexture[] getTexture(IGregTechTileEntity baseMetaTileEntity, ForgeDirection sideDirection,
        ForgeDirection facingDirection, int colorIndex, boolean active, boolean redstoneLevel) {
        if (sideDirection == facingDirection) {
            if (active) return new ITexture[] { Casings.ThermalProcessingCasing.getCasingTexture(),
                TextureFactory.builder()
                    .addIcon(TexturesGtBlock.oMCAIndustrialRockBreakerActive)
                    .extFacing()
                    .build(),
                TextureFactory.builder()
                    .addIcon(TexturesGtBlock.oMCAIndustrialRockBreakerActiveGlow)
                    .extFacing()
                    .glow()
                    .build() };
            return new ITexture[] { Casings.ThermalProcessingCasing.getCasingTexture(), TextureFactory.builder()
                .addIcon(TexturesGtBlock.oMCAIndustrialRockBreaker)
                .extFacing()
                .build(),
                TextureFactory.builder()
                    .addIcon(TexturesGtBlock.oMCAIndustrialRockBreakerGlow)
                    .extFacing()
                    .glow()
                    .build() };
        }
        return new ITexture[] { Casings.ThermalProcessingCasing.getCasingTexture() };
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
        if (aBaseMetaTileEntity.isServerSide() && needsFluidRefill && mMachine && aTick % 20 == 0) {
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

                        Block existing = world.getBlock(wx, wy, wz);
                        boolean isReplaceable;

                        if (c == 'E') {
                            boolean isCOFHCore = Mods.COFHCore.isModLoaded()
                                && (existing instanceof BlockWater || existing instanceof BlockTickingWater);
                            boolean isFlowing = existing == Blocks.flowing_water;
                            boolean isWater = isFlowing || existing == Blocks.water || isCOFHCore;
                            isFlowing = isFlowing || (isWater && world.getBlockMetadata(wx, wy, wz) > 0);
                            isReplaceable = isFlowing || existing == Blocks.air;

                            if (existing != Blocks.water && !isCOFHCore) {
                                if (isReplaceable) {
                                    world.setBlock(wx, wy, wz, Blocks.water, 0, 3);
                                } else allFilled = false;
                            }
                        } else {
                            isReplaceable = existing == Blocks.air || existing == Blocks.flowing_lava
                                || existing.isReplaceable(world, wx, wy, wz);

                            if (existing != Blocks.lava) {
                                if (isReplaceable) {
                                    world.setBlock(wx, wy, wz, Blocks.lava, 0, 3);
                                } else allFilled = false;
                            }
                        }
                    }
                }
            }
            if (allFilled) needsFluidRefill = false;
        }

    }
}
