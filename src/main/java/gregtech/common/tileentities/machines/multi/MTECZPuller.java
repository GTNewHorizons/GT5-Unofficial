package gregtech.common.tileentities.machines.multi;

import static com.gtnewhorizon.structurelib.structure.StructureUtility.ofBlock;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.onElementPass;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.transpose;
import static gregtech.api.enums.HatchElement.Energy;
import static gregtech.api.enums.HatchElement.InputBus;
import static gregtech.api.enums.HatchElement.InputHatch;
import static gregtech.api.enums.HatchElement.Maintenance;
import static gregtech.api.enums.HatchElement.OutputBus;
import static gregtech.api.enums.HatchElement.OutputHatch;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_LARGE_CHEMICAL_REACTOR;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_LARGE_CHEMICAL_REACTOR_ACTIVE;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_LARGE_CHEMICAL_REACTOR_ACTIVE_GLOW;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_LARGE_CHEMICAL_REACTOR_GLOW;
import static gregtech.api.enums.Textures.BlockIcons.casingTexturePages;
import static gregtech.api.util.GTStructureUtility.activeCoils;
import static gregtech.api.util.GTStructureUtility.buildHatchAdder;

import java.util.function.Consumer;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IChatComponent;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

import com.gtnewhorizon.structurelib.StructureLibAPI;
import com.gtnewhorizon.structurelib.alignment.constructable.ISurvivalConstructable;
import com.gtnewhorizon.structurelib.structure.AutoPlaceEnvironment;
import com.gtnewhorizon.structurelib.structure.IItemSource;
import com.gtnewhorizon.structurelib.structure.IStructureDefinition;
import com.gtnewhorizon.structurelib.structure.IStructureElement;
import com.gtnewhorizon.structurelib.structure.ISurvivalBuildEnvironment;
import com.gtnewhorizon.structurelib.structure.StructureDefinition;
import com.gtnewhorizon.structurelib.structure.StructureUtility;
import com.gtnewhorizon.structurelib.util.ItemStackPredicate;

import gregtech.api.GregTechAPI;
import gregtech.api.enums.HeatingCoilLevel;
import gregtech.api.interfaces.IHeatingCoil;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.logic.ProcessingLogic;
import gregtech.api.metatileentity.implementations.MTEEnhancedMultiBlockBase;
import gregtech.api.recipe.RecipeMap;
import gregtech.api.recipe.RecipeMaps;
import gregtech.api.render.TextureFactory;
import gregtech.api.util.GTUtility;
import gregtech.api.util.MultiblockTooltipBuilder;

public class MTECZPuller extends MTEEnhancedMultiBlockBase<MTECZPuller> implements ISurvivalConstructable {

    private static final int CASING_INDEX = 176;
    private static final String STRUCTURE_PIECE_MAIN = "main";
    private static final IStructureDefinition<MTECZPuller> STRUCTURE_DEFINITION = StructureDefinition
        .<MTECZPuller>builder()
        .addShape(
            STRUCTURE_PIECE_MAIN,
            transpose(new String[][] { { "ccc", "cxc", "ccc" }, { "c~c", "xPx", "cxc" }, { "ccc", "cxc", "ccc" }, }))
        .addElement('P', ofBlock(GregTechAPI.sBlockCasings8, 1))
        .addElement(
            'c',
            buildHatchAdder(MTECZPuller.class)
                .atLeast(InputHatch, OutputHatch, InputBus, OutputBus, Maintenance, Energy)
                .casingIndex(CASING_INDEX)
                .dot(1)
                .buildAndChain(onElementPass(MTECZPuller::onCasingAdded, ofBlock(GregTechAPI.sBlockCasings8, 0))))
        .addElement(
            'x',
            buildHatchAdder(MTECZPuller.class)
                .atLeast(InputHatch, OutputHatch, InputBus, OutputBus, Maintenance, Energy)
                .casingIndex(CASING_INDEX)
                .dot(1)
                .buildAndChain(
                    activeCoils(CoilStructureElement.INSTANCE),
                    onElementPass(MTECZPuller::onCasingAdded, ofBlock(GregTechAPI.sBlockCasings8, 0))))
        .build();

    private int mCasingAmount;
    private int mCoilAmount;

    public MTECZPuller(int aID, String aName, String aNameRegional) {
        super(aID, aName, aNameRegional);
    }

    public MTECZPuller(String aName) {
        super(aName);
    }

    @Override
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new MTECZPuller(this.mName);
    }

    @Override
    public MultiblockTooltipBuilder createTooltip() {
        final MultiblockTooltipBuilder tt = new MultiblockTooltipBuilder();
        tt.addMachineType("Czochralski Puller, CZ Puller")
            .addInfo("Does not lose efficiency when overclocked")
            .addInfo("Accepts fluids instead of fluid cells")
            .beginStructureBlock(3, 3, 3, false)
            .addController("Front center")
            .addCasingInfoRange("Chemically Inert Machine Casing", 8, 22, false)
            .addOtherStructurePart("PTFE Pipe Machine Casing", "Center")
            .addOtherStructurePart(
                StatCollector.translateToLocal("GT5U.tooltip.structure.heating_coil"),
                "Adjacent to the PTFE Pipe Machine Casing",
                1)
            .addEnergyHatch("Any casing", 1, 2)
            .addMaintenanceHatch("Any casing", 1, 2)
            .addInputBus("Any casing", 1, 2)
            .addInputHatch("Any casing", 1, 2)
            .addOutputBus("Any casing", 1, 2)
            .addOutputHatch("Any casing", 1, 2)
            .addStructureInfo("You can have multiple hatches/buses")
            .toolTipFinisher();
        return tt;
    }

    @Override
    public ITexture[] getTexture(IGregTechTileEntity aBaseMetaTileEntity, ForgeDirection side, ForgeDirection aFacing,
        int colorIndex, boolean aActive, boolean redstoneLevel) {
        if (side == aFacing) {
            if (aActive) return new ITexture[] { casingTexturePages[1][48], TextureFactory.builder()
                .addIcon(OVERLAY_FRONT_LARGE_CHEMICAL_REACTOR_ACTIVE)
                .extFacing()
                .build(),
                TextureFactory.builder()
                    .addIcon(OVERLAY_FRONT_LARGE_CHEMICAL_REACTOR_ACTIVE_GLOW)
                    .extFacing()
                    .glow()
                    .build() };
            return new ITexture[] { casingTexturePages[1][48], TextureFactory.builder()
                .addIcon(OVERLAY_FRONT_LARGE_CHEMICAL_REACTOR)
                .extFacing()
                .build(),
                TextureFactory.builder()
                    .addIcon(OVERLAY_FRONT_LARGE_CHEMICAL_REACTOR_GLOW)
                    .extFacing()
                    .glow()
                    .build() };
        }
        return new ITexture[] { casingTexturePages[1][48] };
    }

    @Override
    public boolean isCorrectMachinePart(ItemStack aStack) {
        return true;
    }

    @Override
    public RecipeMap<?> getRecipeMap() {
        return RecipeMaps.CZpullerRecipes;
    }

    @Override
    protected ProcessingLogic createProcessingLogic() {
        return new ProcessingLogic().enablePerfectOverclock();
    }

    @Override
    public IStructureDefinition<MTECZPuller> getStructureDefinition() {
        return STRUCTURE_DEFINITION;
    }

    private void onCasingAdded() {
        mCasingAmount++;
    }

    @Override
    public boolean checkMachine(IGregTechTileEntity aBaseMetaTileEntity, ItemStack aStack) {
        mCasingAmount = 0;
        mCoilAmount = 0;
        return checkPiece(STRUCTURE_PIECE_MAIN, 1, 1, 0) && mCasingAmount >= 8
            && mCoilAmount == 1
            && !mEnergyHatches.isEmpty()
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

    @Override
    public void construct(ItemStack stackSize, boolean hintsOnly) {
        mCoilAmount = 0;
        buildPiece(STRUCTURE_PIECE_MAIN, stackSize, hintsOnly, 1, 1, 0);
    }

    @Override
    public int survivalConstruct(ItemStack stackSize, int elementBudget, ISurvivalBuildEnvironment env) {
        if (mMachine) return -1;
        mCoilAmount = 0;
        return survivialBuildPiece(STRUCTURE_PIECE_MAIN, stackSize, 1, 1, 0, elementBudget, env, false, true);
    }

    @Override
    public boolean supportsVoidProtection() {
        return true;
    }

    private enum CoilStructureElement implements IStructureElement<MTECZPuller> {

        INSTANCE;

        @Override
        public boolean check(MTECZPuller t, World world, int x, int y, int z) {
            Block block = world.getBlock(x, y, z);
            if (block instanceof IHeatingCoil
                && ((IHeatingCoil) block).getCoilHeat(world.getBlockMetadata(x, y, z)) != HeatingCoilLevel.None) {
                return t.mCoilAmount++ == 0;
            } else {
                return false;
            }
        }

        @Override
        public boolean couldBeValid(MTECZPuller t, World world, int x, int y, int z, ItemStack trigger) {
            Block block = world.getBlock(x, y, z);
            return block instanceof IHeatingCoil
                && ((IHeatingCoil) block).getCoilHeat(world.getBlockMetadata(x, y, z)) != HeatingCoilLevel.None;
        }

        @Override
        public boolean spawnHint(MTECZPuller t, World world, int x, int y, int z, ItemStack trigger) {
            StructureLibAPI.hintParticle(world, x, y, z, GregTechAPI.sBlockCasings5, 0);
            return true;
        }

        @Override
        public boolean placeBlock(MTECZPuller t, World world, int x, int y, int z, ItemStack trigger) {
            if (t.mCoilAmount > 0) return false;
            boolean b = world.setBlock(x, y, z, GregTechAPI.sBlockCasings5, 0, 3);
            if (b) t.mCoilAmount++;
            return b;
        }

        @Override
        public PlaceResult survivalPlaceBlock(MTECZPuller t, World world, int x, int y, int z, ItemStack trigger,
            IItemSource s, EntityPlayerMP actor, Consumer<IChatComponent> chatter) {
            return survivalPlaceBlock(t, world, x, y, z, trigger, AutoPlaceEnvironment.fromLegacy(s, actor, chatter));
        }

        @Override
        public BlocksToPlace getBlocksToPlace(MTECZPuller largeChemicalReactor, World world, int x, int y, int z,
            ItemStack trigger, AutoPlaceEnvironment env) {
            return BlocksToPlace.create(
                IntStream.range(0, 8)
                    .mapToObj(i -> new ItemStack(GregTechAPI.sBlockCasings5, 1, i))
                    .collect(Collectors.toList()));
        }

        @Override
        public PlaceResult survivalPlaceBlock(MTECZPuller t, World world, int x, int y, int z, ItemStack trigger,
            AutoPlaceEnvironment env) {
            if (t.mCoilAmount > 0) return PlaceResult.SKIP;
            if (check(t, world, x, y, z)) return PlaceResult.SKIP;
            if (!StructureLibAPI.isBlockTriviallyReplaceable(world, x, y, z, env.getActor())) return PlaceResult.REJECT;
            ItemStack result = env.getSource()
                .takeOne(ItemStackPredicate.from(GregTechAPI.sBlockCasings5), true);
            if (result == null) return PlaceResult.REJECT;
            PlaceResult ret = StructureUtility.survivalPlaceBlock(
                result,
                ItemStackPredicate.NBTMode.EXACT,
                null,
                true,
                world,
                x,
                y,
                z,
                env.getSource(),
                env.getActor(),
                env.getChatter());
            if (ret == PlaceResult.ACCEPT) t.mCoilAmount++;
            return ret;
        }
    }

    @Override
    public boolean supportsBatchMode() {
        return true;
    }

    @Override
    public boolean onWireCutterRightClick(ForgeDirection side, ForgeDirection wrenchingSide, EntityPlayer aPlayer,
        float aX, float aY, float aZ, ItemStack aTool) {
        batchMode = !batchMode;
        if (batchMode) {
            GTUtility.sendChatToPlayer(aPlayer, StatCollector.translateToLocal("misc.BatchModeTextOn"));
        } else {
            GTUtility.sendChatToPlayer(aPlayer, StatCollector.translateToLocal("misc.BatchModeTextOff"));
        }
        return true;
    }
}
