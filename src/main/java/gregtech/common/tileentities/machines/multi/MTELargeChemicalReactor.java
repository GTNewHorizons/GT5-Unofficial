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

public class MTELargeChemicalReactor extends MTEEnhancedMultiBlockBase<MTELargeChemicalReactor>
    implements ISurvivalConstructable {

    private static final int CASING_INDEX = 176;
    private static final String STRUCTURE_PIECE_MAIN = "main";
    private static final IStructureDefinition<MTELargeChemicalReactor> STRUCTURE_DEFINITION = StructureDefinition
        .<MTELargeChemicalReactor>builder()
        .addShape(
            STRUCTURE_PIECE_MAIN,
            transpose(new String[][] { { "ccc", "cxc", "ccc" }, { "c~c", "xPx", "cxc" }, { "ccc", "cxc", "ccc" }, }))
        .addElement('P', ofBlock(GregTechAPI.sBlockCasings8, 1))
        .addElement(
            'c',
            buildHatchAdder(MTELargeChemicalReactor.class)
                .atLeast(InputHatch, OutputHatch, InputBus, OutputBus, Maintenance, Energy)
                .casingIndex(CASING_INDEX)
                .hint(1)
                .buildAndChain(
                    onElementPass(MTELargeChemicalReactor::onCasingAdded, ofBlock(GregTechAPI.sBlockCasings8, 0))))
        .addElement(
            'x',
            buildHatchAdder(MTELargeChemicalReactor.class)
                .atLeast(InputHatch, OutputHatch, InputBus, OutputBus, Maintenance, Energy)
                .casingIndex(CASING_INDEX)
                .hint(1)
                .buildAndChain(
                    activeCoils(CoilStructureElement.INSTANCE),
                    onElementPass(MTELargeChemicalReactor::onCasingAdded, ofBlock(GregTechAPI.sBlockCasings8, 0))))
        .build();

    private int mCasingAmount;
    private int mCoilAmount;

    public MTELargeChemicalReactor(int aID, String aName, String aNameRegional) {
        super(aID, aName, aNameRegional);
    }

    public MTELargeChemicalReactor(String aName) {
        super(aName);
    }

    @Override
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new MTELargeChemicalReactor(this.mName);
    }

    @Override
    public MultiblockTooltipBuilder createTooltip() {
        final MultiblockTooltipBuilder tt = new MultiblockTooltipBuilder();
        tt.addMachineType("machtype.lcr")
            .addInfo("gt.lcr.tips.1")
            .addPerfectOCInfo()
            .beginStructureBlock(3, 3, 3, false)
            .addController("front_center")
            .addCasingInfoRange("gt.blockcasings8.0.name", 8, 22, false)
            .addStructurePart("gt.blockcasings8.1.name", "gt.lcr.info.1")
            .addStructurePart("GT5U.tooltip.structure.heating_coil", "gt.lcr.info.2", 1)
            .addEnergyHatch("<casing>", 1, 2)
            .addMaintenanceHatch("<casing>", 1, 2)
            .addInputBus("<casing>", 1, 2)
            .addInputHatch("<casing>", 1, 2)
            .addOutputBus("<casing>", 1, 2)
            .addOutputHatch("<casing>", 1, 2)
            .addStructureInfo("gt.lcr.info.3")
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
    public boolean supportsSingleRecipeLocking() {
        return true;
    }

    @Override
    public RecipeMap<?> getRecipeMap() {
        return RecipeMaps.multiblockChemicalReactorRecipes;
    }

    @Override
    protected ProcessingLogic createProcessingLogic() {
        return new ProcessingLogic().enablePerfectOverclock();
    }

    @Override
    public IStructureDefinition<MTELargeChemicalReactor> getStructureDefinition() {
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
    public void construct(ItemStack stackSize, boolean hintsOnly) {
        mCoilAmount = 0;
        buildPiece(STRUCTURE_PIECE_MAIN, stackSize, hintsOnly, 1, 1, 0);
    }

    @Override
    public int survivalConstruct(ItemStack stackSize, int elementBudget, ISurvivalBuildEnvironment env) {
        if (mMachine) return -1;
        mCoilAmount = 0;
        return survivalBuildPiece(STRUCTURE_PIECE_MAIN, stackSize, 1, 1, 0, elementBudget, env, false, true);
    }

    @Override
    public boolean supportsVoidProtection() {
        return true;
    }

    private enum CoilStructureElement implements IStructureElement<MTELargeChemicalReactor> {

        INSTANCE;

        @Override
        public boolean check(MTELargeChemicalReactor t, World world, int x, int y, int z) {
            Block block = world.getBlock(x, y, z);
            if (block instanceof IHeatingCoil
                && ((IHeatingCoil) block).getCoilHeat(world.getBlockMetadata(x, y, z)) != HeatingCoilLevel.None) {
                return t.mCoilAmount++ == 0;
            } else {
                return false;
            }
        }

        @Override
        public boolean couldBeValid(MTELargeChemicalReactor t, World world, int x, int y, int z, ItemStack trigger) {
            Block block = world.getBlock(x, y, z);
            return block instanceof IHeatingCoil
                && ((IHeatingCoil) block).getCoilHeat(world.getBlockMetadata(x, y, z)) != HeatingCoilLevel.None;
        }

        @Override
        public boolean spawnHint(MTELargeChemicalReactor t, World world, int x, int y, int z, ItemStack trigger) {
            StructureLibAPI.hintParticle(world, x, y, z, GregTechAPI.sBlockCasings5, 0);
            return true;
        }

        @Override
        public boolean placeBlock(MTELargeChemicalReactor t, World world, int x, int y, int z, ItemStack trigger) {
            if (t.mCoilAmount > 0) return false;
            boolean b = world.setBlock(x, y, z, GregTechAPI.sBlockCasings5, 0, 3);
            if (b) t.mCoilAmount++;
            return b;
        }

        @Override
        public PlaceResult survivalPlaceBlock(MTELargeChemicalReactor t, World world, int x, int y, int z,
            ItemStack trigger, IItemSource s, EntityPlayerMP actor, Consumer<IChatComponent> chatter) {
            return survivalPlaceBlock(t, world, x, y, z, trigger, AutoPlaceEnvironment.fromLegacy(s, actor, chatter));
        }

        @Override
        public BlocksToPlace getBlocksToPlace(MTELargeChemicalReactor largeChemicalReactor, World world, int x, int y,
            int z, ItemStack trigger, AutoPlaceEnvironment env) {
            return BlocksToPlace.create(
                IntStream.range(0, 8)
                    .mapToObj(i -> new ItemStack(GregTechAPI.sBlockCasings5, 1, i))
                    .collect(Collectors.toList()));
        }

        @Override
        public PlaceResult survivalPlaceBlock(MTELargeChemicalReactor t, World world, int x, int y, int z,
            ItemStack trigger, AutoPlaceEnvironment env) {
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
        if (aPlayer.isSneaking()) {
            batchMode = !batchMode;
            if (batchMode) {
                GTUtility.sendChatTrans(aPlayer, "misc.BatchModeTextOn");
            } else {
                GTUtility.sendChatTrans(aPlayer, "misc.BatchModeTextOff");
            }
            return true;
        }
        return false;
    }
}
