package com.gtnewhorizons.gtnhintergalactic.tile.multi;

import static net.minecraft.util.EnumChatFormatting.*;
import static net.minecraft.util.EnumChatFormatting.RESET;

import java.util.Arrays;
import java.util.Map;
import java.util.Objects;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.util.MathHelper;
import net.minecraft.world.ChunkCoordIntPair;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;

import com.github.bartimaeusnek.bartworks.client.textures.PrefixTextureLinker;
import com.github.bartimaeusnek.bartworks.system.material.BW_MetaGeneratedBlocks_CasingAdvanced_TE;
import com.github.bartimaeusnek.bartworks.system.material.WerkstoffLoader;
import com.gtnewhorizon.structurelib.StructureLibAPI;
import com.gtnewhorizon.structurelib.structure.IStructureDefinition;
import com.gtnewhorizon.structurelib.structure.IStructureElement;
import com.gtnewhorizon.structurelib.structure.StructureDefinition;
import com.gtnewhorizon.structurelib.structure.StructureUtility;
import com.gtnewhorizons.gtnhintergalactic.Tags;
import com.gtnewhorizons.gtnhintergalactic.client.IGTextures;
import com.gtnewhorizons.gtnhintergalactic.client.lore.LoreHolder;
import com.gtnewhorizons.gtnhintergalactic.recipe.GasSiphonRecipes;

import cpw.mods.fml.common.Loader;
import galaxyspace.core.register.GSBlocks;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.Materials;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.enums.Textures;
import gregtech.api.interfaces.IChunkLoader;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_EnhancedMultiBlockBase;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_Hatch_InputBus;
import gregtech.api.objects.GT_ChunkManager;
import gregtech.api.render.TextureFactory;
import gregtech.api.util.GT_ModHandler;
import gregtech.api.util.GT_Multiblock_Tooltip_Builder;
import gregtech.api.util.GT_StructureUtility;
import gregtech.api.util.GT_Utility;
import gregtech.common.blocks.GT_Block_Casings1;
import micdoodle8.mods.galacticraft.api.world.IOrbitDimension;
import micdoodle8.mods.galacticraft.core.util.GCCoreUtil;

/**
 * Gas Siphon, used to extract gas from gas planets, when placed on a space station in their orbit
 *
 * @author glowredman
 */
public class TileEntityPlanetaryGasSiphon extends GT_MetaTileEntity_EnhancedMultiBlockBase<TileEntityPlanetaryGasSiphon>
        implements IChunkLoader {

    /** Lore string, which will be randomly picked from a selection each time the resources are reloaded */
    @LoreHolder("gt.blockmachines.multimachine.ig.siphon.lore")
    private static String loreTooltip;
    /** Main structure of the machine */
    private static final String STRUCTURE_PIECE_MAIN = "main";
    /** Cached value of log10(4) */
    private static final double LOG4 = Math.log10(4);
    /** Structure definition of the machine */
    private static final IStructureDefinition<TileEntityPlanetaryGasSiphon> STRUCTURE_DEFINITION = StructureDefinition
            .<TileEntityPlanetaryGasSiphon>builder()
            .addShape(
                    STRUCTURE_PIECE_MAIN,
                    StructureUtility.transpose(
                            new String[][] { { "   ", " f ", "   " }, { "   ", " f ", "   " }, { "   ", " f ", "   " },
                                    { " f ", "fcf", " f " }, { " f ", "fcf", " f " }, { " f ", "fcf", " f " },
                                    { "b~b", "bcb", "bbb" } }))
            .addElement('f', GT_StructureUtility.ofFrame(Materials.TungstenSteel)).addElement('c', ofReboltedCasing())
            .addElement(
                    'b',
                    StructureUtility.ofChain(
                            GT_StructureUtility.ofHatchAdder(
                                    TileEntityPlanetaryGasSiphon::addMaintenanceToMachineList,
                                    IGTextures.ADVANCED_MACHINE_FRAME_INDEX,
                                    1),
                            GT_StructureUtility.ofHatchAdder(
                                    TileEntityPlanetaryGasSiphon::addEnergyInputToMachineList,
                                    IGTextures.ADVANCED_MACHINE_FRAME_INDEX,
                                    1),
                            GT_StructureUtility.ofHatchAdder(
                                    TileEntityPlanetaryGasSiphon::addInputToMachineList,
                                    IGTextures.ADVANCED_MACHINE_FRAME_INDEX,
                                    1),
                            GT_StructureUtility.ofHatchAdder(
                                    TileEntityPlanetaryGasSiphon::addOutputToMachineList,
                                    IGTextures.ADVANCED_MACHINE_FRAME_INDEX,
                                    1),
                            StructureUtility.ofBlock(GSBlocks.MachineFrames, 0)))
            .build();

    /**
     * Create a new planetary gas siphon
     *
     * @param id           ID of the multiblock
     * @param name         Unlocalized name of the multiblock
     * @param regionalName Localized (english) name of the multiblock
     */
    public TileEntityPlanetaryGasSiphon(int id, String name, String regionalName) {
        super(id, name, regionalName);
    }

    /**
     * Create a new planetary gas siphon
     *
     * @param name Unlocalized name of the multiblock
     */
    public TileEntityPlanetaryGasSiphon(String name) {
        super(name);
    }

    /** Current pumping depth */
    private int depth;
    /** Cached fluid stack using for displaying the pumped fluid */
    private FluidStack fluid = new FluidStack(FluidRegistry.WATER, 0) {

        public String getLocalizedName() {
            return "None";
        }
    };
    /** Flag if chunk loading is enabled */
    private boolean mChunkLoadingEnabled = true;
    /** Chunk in which the multi is build */
    private ChunkCoordIntPair mCurrentChunk = null;
    /** Flag if the chunk of the multi needs to be reloaded */
    private boolean mWorkChunkNeedsReload = true;

    /**
     * Construct this machine using the blueprint in creative
     *
     * @param stackSize Blueprint stack
     * @param hintsOnly Whether only hints should be displayed, or it should be build
     */
    @Override
    public void construct(ItemStack stackSize, boolean hintsOnly) {
        buildPiece(STRUCTURE_PIECE_MAIN, stackSize, hintsOnly, 1, 6, 0);
    }

    /**
     * Get a new meta entity for this controller
     *
     * @param tileEntity is just because the internal Variable "mBaseMetaTileEntity" is set after this Call.
     * @return New meta entity
     */
    @Override
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity tileEntity) {
        return new TileEntityPlanetaryGasSiphon(this.mName);
    }

    /**
     * @return Tooltip builder for this machine
     */
    @Override
    protected GT_Multiblock_Tooltip_Builder createTooltip() {
        final GT_Multiblock_Tooltip_Builder tt = new GT_Multiblock_Tooltip_Builder();
        tt.addMachineType(GCCoreUtil.translate("gt.blockmachines.multimachine.ig.siphon.type"))
                .addInfo(loreTooltip != null ? ITALIC + loreTooltip : "")
                .addInfo(GCCoreUtil.translate("gt.blockmachines.multimachine.ig.siphon.desc1"))
                .addInfo(GCCoreUtil.translate("gt.blockmachines.multimachine.ig.siphon.desc2"))
                .addInfo(GCCoreUtil.translate("gt.blockmachines.multimachine.ig.siphon.desc3"))
                .addInfo(GCCoreUtil.translate("gt.blockmachines.multimachine.ig.siphon.desc4"))
                .addInfo(GCCoreUtil.translate("gt.blockmachines.multimachine.ig.siphon.desc5")).addSeparator()
                .beginStructureBlock(3, 7, 3, false)
                .addController(GCCoreUtil.translate("ig.siphon.structure.ControllerPos"))
                .addOtherStructurePart(
                        GCCoreUtil.translate("ig.siphon.structure.AdvMachineFrame"),
                        GCCoreUtil.translate("ig.siphon.structure.Base"))
                .addOtherStructurePart(
                        GCCoreUtil.translate("ig.siphon.structure.ReboltedRhodiumPalladiumCasing"),
                        GCCoreUtil.translate("ig.siphon.structure.PillarMiddle"))
                .addOtherStructurePart(
                        GCCoreUtil.translate("ig.siphon.structure.FrameTungstensteel"),
                        GCCoreUtil.translate("ig.siphon.structure.Sides"))
                .addEnergyHatch(GCCoreUtil.translate("ig.siphon.structure.AnyAdvMachineFrame"), 1)
                .addMaintenanceHatch(GCCoreUtil.translate("ig.siphon.structure.AnyAdvMachineFrame"), 1)
                .addInputBus(GCCoreUtil.translate("ig.siphon.structure.AnyAdvMachineFrame"), 1)
                .addOutputHatch(GCCoreUtil.translate("ig.siphon.structure.AnyAdvMachineFrame"), 1)
                .toolTipFinisher(DARK_PURPLE + Tags.MODNAME);
        return tt;
    }

    /**
     * @return Structure definition for this machine
     */
    @Override
    public IStructureDefinition<TileEntityPlanetaryGasSiphon> getStructureDefinition() {
        return STRUCTURE_DEFINITION;
    }

    /**
     * @return Chunk of this machine
     */
    @Override
    public ChunkCoordIntPair getActiveChunk() {
        return mCurrentChunk;
    }

    /**
     * Get the texture for this controller
     *
     * @param baseMetaTileEntity MTE of this controller
     * @param side               is the Side of the Block
     * @param facing             is the direction the Block is facing (or a Bitmask of all Connections in case of Pipes)
     * @param colorIndex         The Minecraft Color the Block is having
     * @param active             if the Machine is currently active (use this instead of calling
     *                           mBaseMetaTileEntity.mActive!!!). Note: In case of Pipes this means if this Side is
     *                           connected to something or not.
     * @param redstone           if the Machine is currently outputting a RedstoneSignal (use this instead of calling
     *                           mBaseMetaTileEntity.mRedstone!!!)
     * @return Texture of this controller for the input conditions
     */
    @Override
    public ITexture[] getTexture(IGregTechTileEntity baseMetaTileEntity, ForgeDirection side, ForgeDirection facing,
            int colorIndex, boolean active, boolean redstone) {
        if (side == facing) {
            if (active) return new ITexture[] {
                    Textures.BlockIcons.getCasingTextureForId(IGTextures.ADVANCED_MACHINE_FRAME_INDEX),
                    TextureFactory.of(IGTextures.SIPHON_OVERLAY_FRONT),
                    TextureFactory.builder().addIcon(IGTextures.SIPHON_OVERLAY_FRONT_ACTIVE_GLOW).glow().build() };
            return new ITexture[] { Textures.BlockIcons.getCasingTextureForId(IGTextures.ADVANCED_MACHINE_FRAME_INDEX),
                    TextureFactory.of(IGTextures.SIPHON_OVERLAY_FRONT),
                    TextureFactory.builder().addIcon(IGTextures.SIPHON_OVERLAY_FRONT_GLOW).glow().build() };
        }
        return new ITexture[] { Textures.BlockIcons.getCasingTextureForId(IGTextures.ADVANCED_MACHINE_FRAME_INDEX) };
    }

    /**
     * Check if the input item is the correct machine part
     *
     * @param stack Input item
     * @return True if correct machine part, else false
     */
    @Override
    public boolean isCorrectMachinePart(ItemStack stack) {
        return true;
    }

    /**
     * Check if this machine can perform a recipe
     *
     * @param controllerStack Item in the controller
     * @return True if recipe was started, else false
     */
    @Override
    public boolean checkRecipe(ItemStack controllerStack) {
        // return early if no input busses are present, the first bus is invalid or the TE is not on a space station
        if (mInputBusses.isEmpty() || !isValidMetaTileEntity(mInputBusses.get(0))
                || !(this.getBaseMetaTileEntity().getWorld().provider instanceof IOrbitDimension)) {
            depth = 0;
            mEUt = 0;
            mEfficiency = 0;
            return false;
        }

        IOrbitDimension provider = (IOrbitDimension) this.getBaseMetaTileEntity().getWorld().provider;
        Map<Integer, FluidStack> planetRecipes = GasSiphonRecipes.RECIPES.get(provider.getPlanetToOrbit());

        // return early if there are no recipes for the planet the station is orbiting
        if (planetRecipes == null) {
            depth = 0;
            mEUt = 0;
            mEfficiency = 0;
            return false;
        }

        GT_MetaTileEntity_Hatch_InputBus bus = mInputBusses.get(0);
        int numPipes = 0;

        // count mining pipes, get depth
        for (int i = 0; i < bus.getBaseMetaTileEntity().getSizeInventory(); i++) {
            ItemStack stack = bus.getBaseMetaTileEntity().getStackInSlot(i);
            if (stack == null) {
                continue;
            }
            if (stack.getItem() == ItemList.Circuit_Integrated.getItem()) {
                depth = stack.getItemDamage();
                continue;
            }
            if (Objects.equals(stack.getItem(), GT_ModHandler.getIC2Item("miningPipe", 0).getItem())) {
                numPipes += stack.stackSize;
            }
        }

        // return early if not enough mining pipes are in the input bus
        if (depth == 0 || numPipes < depth * 64) {
            mEUt = 0;
            mEfficiency = 0;
            return false;
        }

        FluidStack recipeFluid = planetRecipes.get(depth);

        // return early if invalid depth
        if (recipeFluid == null) {
            depth = 0;
            mEUt = 0;
            mEfficiency = 0;
            return false;
        }
        if (!canOutputAll(new FluidStack[] { recipeFluid })) {
            return false;
        }

        // calculate overclockedness
        int recipeEUt = depth * (4 << (2 * provider.getCelestialBody().getTierRequirement() + 2));
        int ocLevel = MathHelper
                .floor_double(Math.log10((double) this.getMaxInputVoltage() / (double) recipeEUt) / LOG4);

        // apply recipe
        if (ocLevel < 0) {
            // no underclocking allowed
            mEUt = 0;
            mEfficiency = 0;
            return false;
        }
        fluid = recipeFluid.copy();
        if (ocLevel == 0) {
            mEUt = -recipeEUt;
        } else {
            ocLevel--;
            fluid.amount *= 2 << ocLevel;
            mEUt = -recipeEUt * (4 << (2 * ocLevel));
        }

        // success - check again in 20 ticks
        mOutputFluids = new FluidStack[] { fluid };
        mEfficiency = 10000 - (getIdealStatus() - getRepairStatus()) * 1000;
        mEfficiencyIncrease = 10000;
        mMaxProgresstime = 20;
        return true;
    }

    /**
     * Check if the machine has a valid structure
     *
     * @param aBaseMetaTileEntity MTE of this controller
     * @param stack               Item in the controller
     * @return True if machine is valid, else false
     */
    @Override
    public boolean checkMachine(IGregTechTileEntity aBaseMetaTileEntity, ItemStack stack) {
        return checkPiece(STRUCTURE_PIECE_MAIN, 1, 6, 0) && mMaintenanceHatches.size() == 1
                && mInputBusses.size() == 1
                && mOutputHatches.size() == 1
                && mEnergyHatches.size() == 1;
    }

    /**
     * Get the maximum efficiency of this machine
     *
     * @param stack Item in the controller
     * @return Maximum efficiency
     */
    @Override
    public int getMaxEfficiency(ItemStack stack) {
        return 10000;
    }

    /**
     * Get the damage that will be dealt to the item in the controller
     *
     * @param stack Item in the controller
     * @return Damage that is applied to the item in the controller
     */
    @Override
    public int getDamageToComponent(ItemStack stack) {
        return 0;
    }

    /**
     * Whether this machine should explode, when the structure is broken while running
     *
     * @param stack Item in the controller
     * @return True if it should explode, else false
     */
    @Override
    public boolean explodesOnComponentBreak(ItemStack stack) {
        return false;
    }

    /**
     * Save additional nbt data to this controller
     *
     * @param aNBT Tag to which will be saved
     */
    @Override
    public void saveNBTData(NBTTagCompound aNBT) {
        super.saveNBTData(aNBT);
        aNBT.setBoolean("chunkLoadingEnabled", mChunkLoadingEnabled);
        aNBT.setBoolean("isChunkloading", mCurrentChunk != null);
        if (mCurrentChunk != null) {
            aNBT.setInteger("loadedChunkXPos", mCurrentChunk.chunkXPos);
            aNBT.setInteger("loadedChunkZPos", mCurrentChunk.chunkZPos);
        }
    }

    /**
     * Read additional nbt data from this controller
     *
     * @param aNBT Tag which will be read
     */
    @Override
    public void loadNBTData(NBTTagCompound aNBT) {
        super.loadNBTData(aNBT);
        if (aNBT.hasKey("chunkLoadingEnabled")) mChunkLoadingEnabled = aNBT.getBoolean("chunkLoadingEnabled");
        if (aNBT.getBoolean("isChunkloading")) {
            mCurrentChunk = new ChunkCoordIntPair(
                    aNBT.getInteger("loadedChunkXPos"),
                    aNBT.getInteger("loadedChunkZPos"));
        }
    }

    /**
     * Callback that wil be invoked when the controller is right-clicked with a soldering tool
     *
     * @param side          Clicked side of the controller
     * @param wrenchingSide Clicked grid side (the grid that gets displayed, when holding a tool while looking at it)
     * @param player        Player that clicked
     * @param x             X coordinate of the machine
     * @param y             Y coordinate of the machine
     * @param z             Z coordinate of the machine
     * @return True if event was processed, else false
     */
    @Override
    public boolean onSolderingToolRightClick(ForgeDirection side, ForgeDirection wrenchingSide, EntityPlayer player,
            float x, float y, float z) {
        if (side == getBaseMetaTileEntity().getFrontFacing()) {
            mChunkLoadingEnabled = !mChunkLoadingEnabled;
            GT_Utility.sendChatToPlayer(
                    player,
                    mChunkLoadingEnabled ? GT_Utility.trans("502", "Mining chunk loading enabled")
                            : GT_Utility.trans("503", "Mining chunk loading disabled"));
            return true;
        }
        return super.onSolderingToolRightClick(side, wrenchingSide, player, x, y, z);
    }

    /**
     * Callback that will be invoked when the machine is broken
     */
    @Override
    public void onRemoval() {
        if (mChunkLoadingEnabled) GT_ChunkManager.releaseTicket((TileEntity) getBaseMetaTileEntity());
        super.onRemoval();
    }

    /**
     * Callback that will be invoked after executing a tick
     *
     * @param baseMetaTileEntity MTE of this controller
     * @param tick               Current tick
     */
    @Override
    public void onPostTick(IGregTechTileEntity baseMetaTileEntity, long tick) {
        super.onPostTick(baseMetaTileEntity, tick);
        if (baseMetaTileEntity.isServerSide() && mCurrentChunk != null
                && !mWorkChunkNeedsReload
                && !baseMetaTileEntity.isAllowedToWork()) {
            // if machine has stopped, stop chunk loading
            GT_ChunkManager.releaseTicket((TileEntity) baseMetaTileEntity);
            mWorkChunkNeedsReload = true;
        }
    }

    /**
     * @return Info data stick of this controller
     */
    @Override
    public String[] getInfoData() {
        return new String[] { LIGHT_PURPLE + "Operational Data:" + RESET, "Depth: " + YELLOW + depth + RESET,
                "Fluid: " + YELLOW + fluid.amount + RESET + "L/s " + BLUE + fluid.getLocalizedName() + RESET,
                "EU/t required: " + YELLOW + GT_Utility.formatNumbers(-mEUt) + RESET + " EU/t",
                "Maintenance Status: " + (getRepairStatus() == getIdealStatus() ? GREEN + "Working perfectly" + RESET
                        : RED + "Has problems" + RESET),
                "---------------------------------------------" };
    }

    @Override
    public boolean supportsVoidProtection() {
        return true;
    }

    private static <T> IStructureElement<T> ofReboltedCasing() {
        return new IStructureElement<T>() {

            private final boolean isBartworksLoaded = Loader.isModLoaded("bartworks");
            private final Block fallBackBlock = GT_Block_Casings1.getBlockById(0);
            private final int fallbackBlockMeta = 0;
            private IIcon[] icons;

            @Override
            public boolean spawnHint(T t, World world, int x, int y, int z, ItemStack trigger) {
                if (isBartworksLoaded) {
                    if (icons == null) {
                        icons = new IIcon[6];
                        Arrays.fill(
                                icons,
                                PrefixTextureLinker.texMapBlocks.get(OrePrefixes.blockCasingAdvanced)
                                        .get(WerkstoffLoader.LuVTierMaterial.getTexSet()).getIcon());
                    }
                    StructureLibAPI
                            .hintParticleTinted(world, x, y, z, icons, WerkstoffLoader.LuVTierMaterial.getRGBA());
                } else {
                    StructureLibAPI.hintParticle(world, x, y, z, fallBackBlock, fallbackBlockMeta);
                }
                return true;
            }

            @Override
            public boolean placeBlock(T t, World world, int x, int y, int z, ItemStack trigger) {
                if (isBartworksLoaded) {
                    ItemStack stack = WerkstoffLoader.LuVTierMaterial.get(OrePrefixes.blockCasingAdvanced);
                    if (stack.getItem() instanceof ItemBlock) {
                        ItemBlock item = (ItemBlock) stack.getItem();
                        return item.placeBlockAt(
                                stack,
                                null,
                                world,
                                x,
                                y,
                                z,
                                6,
                                0,
                                0,
                                0,
                                WerkstoffLoader.LuVTierMaterial.getmID());
                    }
                } else {
                    world.setBlock(x, y, z, fallBackBlock, fallbackBlockMeta, 2);
                }
                return false;
            }

            @Override
            public boolean check(T t, World world, int x, int y, int z) {
                if (isBartworksLoaded) {
                    TileEntity tile = world.getTileEntity(x, y, z);
                    if (tile instanceof BW_MetaGeneratedBlocks_CasingAdvanced_TE) {
                        BW_MetaGeneratedBlocks_CasingAdvanced_TE tileCasingAdvanced = (BW_MetaGeneratedBlocks_CasingAdvanced_TE) tile;
                        if (tileCasingAdvanced.isInvalid()) return false;
                        return tileCasingAdvanced.mMetaData == WerkstoffLoader.LuVTierMaterial.getmID();
                    }
                } else {
                    Block worldBlock = world.getBlock(x, y, z);
                    return worldBlock == fallBackBlock;
                }
                return false;
            }
        };
    }
}
