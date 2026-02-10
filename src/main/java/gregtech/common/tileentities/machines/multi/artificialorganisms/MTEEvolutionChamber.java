package gregtech.common.tileentities.machines.multi.artificialorganisms;

import static com.gtnewhorizon.structurelib.structure.StructureUtility.ofChain;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.onElementPass;
import static gregtech.api.enums.HatchElement.Energy;
import static gregtech.api.enums.HatchElement.InputBus;
import static gregtech.api.enums.HatchElement.InputHatch;
import static gregtech.api.enums.HatchElement.Maintenance;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_BIOVAT;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_BIOVAT_EMPTY;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_BIOVAT_EMPTY_GLOW;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_BIOVAT_GLOW;
import static gregtech.api.util.GTStructureUtility.buildHatchAdder;
import static gregtech.api.util.GTStructureUtility.chainAllGlasses;
import static gregtech.api.util.GTStructureUtility.ofFrame;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;
import net.minecraftforge.fluids.FluidTankInfo;

import org.apache.commons.lang3.tuple.Pair;
import org.jetbrains.annotations.NotNull;
import org.lwjgl.opengl.GL11;

import com.cleanroommc.modularui.utils.item.LimitingItemStackHandler;
import com.google.common.collect.ImmutableList;
import com.gtnewhorizon.structurelib.alignment.IAlignmentLimits;
import com.gtnewhorizon.structurelib.alignment.constructable.ISurvivalConstructable;
import com.gtnewhorizon.structurelib.structure.IStructureDefinition;
import com.gtnewhorizon.structurelib.structure.ISurvivalBuildEnvironment;
import com.gtnewhorizon.structurelib.structure.StructureDefinition;
import com.gtnewhorizon.structurelib.structure.StructureUtility;

import gregtech.api.GregTechAPI;
import gregtech.api.enums.Materials;
import gregtech.api.enums.Textures;
import gregtech.api.factory.artificialorganisms.MTEHatchAOOutput;
import gregtech.api.gui.modularui.GUITextureSet;
import gregtech.api.interfaces.IHatchElement;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.logic.ProcessingLogic;
import gregtech.api.metatileentity.implementations.MTEExtendedPowerMultiBlockBase;
import gregtech.api.metatileentity.implementations.MTEHatch;
import gregtech.api.metatileentity.implementations.MTEHatchInput;
import gregtech.api.modularui2.GTGuiTheme;
import gregtech.api.modularui2.GTGuiThemes;
import gregtech.api.objects.ArtificialOrganism;
import gregtech.api.render.TextureFactory;
import gregtech.api.util.GTUtility;
import gregtech.api.util.IGTHatchAdder;
import gregtech.api.util.MultiblockTooltipBuilder;
import gregtech.common.blocks.BlockCasings12;
import gregtech.common.gui.modularui.multiblock.MTEEvolutionChamberGui;
import gregtech.common.gui.modularui.multiblock.base.MTEMultiBlockBaseGui;
import gregtech.common.render.IMTERenderer;
import gregtech.common.tileentities.machines.IDualInputHatch;
import mcp.mobius.waila.api.IWailaConfigHandler;
import mcp.mobius.waila.api.IWailaDataAccessor;

public class MTEEvolutionChamber extends MTEExtendedPowerMultiBlockBase<MTEEvolutionChamber>
    implements ISurvivalConstructable, IMTERenderer {

    private static final String STRUCTURE_PIECE_MAIN = "main";
    private static final IStructureDefinition<MTEEvolutionChamber> STRUCTURE_DEFINITION = StructureDefinition
        .<MTEEvolutionChamber>builder()
        .addShape(
            STRUCTURE_PIECE_MAIN,
            new String[][] {
                { "           ", " BBBCCCBBB ", " AAC   CAA ", " AAC   CAA ", " AAC   CAA ", " AAC   CAA ",
                    " AAC   CAA ", " AAC   CAA ", " AAC   CAA ", " BBB   BBB " },
                { "  CCCCCCC  ", "BBBBBBBBBBB", "A--BAAAB--A", "A--BAAAB--A", "A--BCCCB--A", "A--BAAAB--A",
                    "A--BCCCB--A", "A--BAAAB--A", "A--BAAAB--A", "BBBBB~BBBBB" },
                { " CBBBBBBBC ", "BBB-----BBB", "A---------A", "A---------A", "A---------A", "A---------A",
                    "A---------A", "A---------A", "A---------A", "BBBBBBBBBBB" },
                { " CBBBBBBBC ", "BB-------BB", "CB-------BC", "CB-------BC", "CB-------BC", "CB-------BC",
                    "CB-------BC", "CB-------BC", "CB-------BC", "BBBBBBBBBBB" },
                { " CBBBBBBBC ", "CB-------BC", " A-------A ", " A-------A ", " C-------C ", " A-------A ",
                    " C-------C ", " A-------A ", " A-------A ", " BBBBBBBBB " },
                { " CBBBBBBBC ", "CB-------BC", " A-------A ", " A-------A ", " C-------C ", " A-------A ",
                    " C-------C ", " A-------A ", " A-------A ", " BBBBBBBBB " },
                { " CBBBBBBBC ", "CB-------BC", " A-------A ", " A-------A ", " C-------C ", " A-------A ",
                    " C-------C ", " A-------A ", " A-------A ", " BBBBBBBBB " },
                { " CBBBBBBBC ", "BB-------BB", "CB-------BC", "CB-------BC", "CB-------BC", "CB-------BC",
                    "CB-------BC", "CB-------BC", "CB-------BC", "BBBBBBBBBBB" },
                { " CBBBBBBBC ", "BBB-----BBB", "A---------A", "A---------A", "A---------A", "A---------A",
                    "A---------A", "A---------A", "A---------A", "BBBBBBBBBBB" },
                { "  CCCCCCC  ", "BBBBBBBBBBB", "A--BAAAB--A", "A--BAAAB--A", "A--BCCCB--A", "A--BAAAB--A",
                    "A--BCCCB--A", "A--BAAAB--A", "A--BAAAB--A", "BBBBBBBBBBB" },
                { "           ", " BBBCCCBBB ", " AAC   CAA ", " AAC   CAA ", " AAC   CAA ", " AAC   CAA ",
                    " AAC   CAA ", " AAC   CAA ", " AAC   CAA ", " BBB   BBB " } })
        .addElement('A', chainAllGlasses())
        .addElement(
            'B',
            ofChain(
                buildHatchAdder(MTEEvolutionChamber.class)
                    .atLeast(InputBus, Maintenance, Energy, InputHatch, SpecialHatchElement.BioOutput)
                    .casingIndex(((BlockCasings12) GregTechAPI.sBlockCasings12).getTextureIndex(61))
                    .hint(1)
                    .build(),
                onElementPass(
                    MTEEvolutionChamber::onCasingAdded,
                    StructureUtility.ofBlocksTiered(
                        MTEEvolutionChamber::getTierFromMeta,
                        ImmutableList.of(
                            Pair.of(GregTechAPI.sBlockCasings12, 6),
                            Pair.of(GregTechAPI.sBlockCasings12, 7),
                            Pair.of(GregTechAPI.sBlockCasings12, 8)),
                        -1,
                        MTEEvolutionChamber::setCasingTier,
                        MTEEvolutionChamber::getCasingTier))))
        .addElement('C', ofFrame(Materials.Netherite))
        .build();

    private enum SpecialHatchElement implements IHatchElement<MTEEvolutionChamber> {

        BioOutput(MTEEvolutionChamber::addBioHatch, MTEHatchAOOutput.class) {

            @Override
            public long count(MTEEvolutionChamber gtMetaTileEntityEvolutionChamber) {
                return gtMetaTileEntityEvolutionChamber.bioHatches.size();
            }
        };

        private final List<Class<? extends IMetaTileEntity>> mteClasses;
        private final IGTHatchAdder<MTEEvolutionChamber> adder;

        @SafeVarargs
        SpecialHatchElement(IGTHatchAdder<MTEEvolutionChamber> adder, Class<? extends IMetaTileEntity>... mteClasses) {
            this.mteClasses = Collections.unmodifiableList(Arrays.asList(mteClasses));
            this.adder = adder;
        }

        @Override
        public List<? extends Class<? extends IMetaTileEntity>> mteClasses() {
            return mteClasses;
        }

        public IGTHatchAdder<? super MTEEvolutionChamber> adder() {
            return adder;
        }
    }

    private final ArrayList<MTEHatchAOOutput> bioHatches = new ArrayList<>();

    public ArtificialOrganism currentSpecies = new ArtificialOrganism();

    private long powerUsage = 0;
    private FluidStack nutrientUsage;

    private int casingTier;
    public int maxAOs;

    public final int INTERNAL_FLUID_TANK_SIZE = 64000;

    boolean isFinalized = false;

    public MTEEvolutionChamber(final int aID, final String aName, final String aNameRegional) {
        super(aID, aName, aNameRegional);
    }

    public MTEEvolutionChamber(String aName) {
        super(aName);
    }

    public int getCasingTier() {
        return casingTier;
    }

    public void setCasingTier(int i) {
        casingTier = i;
    }

    private static Integer getTierFromMeta(Block block, Integer metaID) {
        if (block != GregTechAPI.sBlockCasings12) return null;
        if (metaID < 6 || metaID > 8) return null;
        return metaID;
    }

    @Override
    public IStructureDefinition<MTEEvolutionChamber> getStructureDefinition() {
        return STRUCTURE_DEFINITION;
    }

    @Override
    public boolean isCorrectMachinePart(ItemStack aStack) {
        return true;
    }

    @Override
    public void onValueUpdate(byte aValue) {
        if (aValue >= 10) {
            isFinalized = true;
            aValue -= 10;
        }
        casingTier = aValue;
    }

    @Override
    public byte getUpdateData() {
        byte update = (byte) casingTier;
        if (currentSpecies != null && currentSpecies.getFinalized()) {
            update += 10;
        }
        return update;
    }

    @Override
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new MTEEvolutionChamber(this.mName);
    }

    // Returns the fill level of either nutrient broth or primordial soup
    public int getFillLevel() {
        return tank.getFluidAmount();
    }

    @Override
    public ITexture[] getTexture(IGregTechTileEntity baseMetaTileEntity, ForgeDirection side, ForgeDirection aFacing,
        int colorIndex, boolean aActive, boolean redstoneLevel) {
        ITexture[] rTexture;
        // stupid fix because casing tier was 117 if machine was not formed ???
        int casingMeta = mMachine ? Math.max(6, getCasingTier()) : 1;
        if (side == aFacing) {
            if (currentSpecies != null && currentSpecies.getFinalized()) {
                rTexture = new ITexture[] {
                    Textures.BlockIcons.getCasingTextureForId(
                        GTUtility.getCasingTextureIndex(GregTechAPI.sBlockCasings12, casingMeta)),
                    TextureFactory.builder()
                        .addIcon(OVERLAY_FRONT_BIOVAT)
                        .extFacing()
                        .build(),
                    TextureFactory.builder()
                        .addIcon(OVERLAY_FRONT_BIOVAT_GLOW)
                        .extFacing()
                        .glow()
                        .build() };
            } else {
                rTexture = new ITexture[] {
                    Textures.BlockIcons.getCasingTextureForId(
                        GTUtility.getCasingTextureIndex(GregTechAPI.sBlockCasings12, casingMeta)),
                    TextureFactory.builder()
                        .addIcon(OVERLAY_FRONT_BIOVAT_EMPTY)
                        .extFacing()
                        .build(),
                    TextureFactory.builder()
                        .addIcon(OVERLAY_FRONT_BIOVAT_EMPTY_GLOW)
                        .extFacing()
                        .glow()
                        .build() };
            }
        } else {
            rTexture = new ITexture[] { Textures.BlockIcons
                .getCasingTextureForId(GTUtility.getCasingTextureIndex(GregTechAPI.sBlockCasings12, casingMeta)) };
        }
        return rTexture;
    }

    @Override
    protected MultiblockTooltipBuilder createTooltip() {
        MultiblockTooltipBuilder tt = new MultiblockTooltipBuilder();
        tt.addMachineType("Artificial Organism Source")
            .addInfo("Used to create and maintain Artificial Organisms")
            .addInfo("Use higher tier vat casings to get more AO culture slots")
            .addInfo("Maximum tank capacity is 500000 * casing tier")
            .addSeparator()
            .beginStructureBlock(3, 5, 3, true)
            .addController("Front Center")
            .addCasingInfoMin("Solid Steel Machine Casing", 85, false)
            .addCasingInfoExactly("Steel Pipe Casing", 24, false)
            .addInputBus("Any Solid Steel Casing", 1)
            .addOutputBus("Any Solid Steel Casing", 1)
            .addInputHatch("Any Solid Steel Casing", 1)
            .addOutputHatch("Any Solid Steel Casing", 1)
            .addEnergyHatch("Any Solid Steel Casing", 1)
            .addMaintenanceHatch("Any Solid Steel Casing", 1)
            .toolTipFinisher("GregTech");
        return tt;
    }

    private void updateTextures() {
        getBaseMetaTileEntity().issueTextureUpdate();

        int textureID = GTUtility.getCasingTextureIndex(GregTechAPI.sBlockCasings12, Math.max(6, casingTier));
        for (MTEHatch h : mInputBusses) h.updateTexture(textureID);
        for (MTEHatch h : mInputHatches) h.updateTexture(textureID);
        for (IDualInputHatch h : mDualInputHatches) h.updateTexture(textureID);
        for (MTEHatch h : mMaintenanceHatches) h.updateTexture(textureID);
        for (MTEHatch h : mEnergyHatches) h.updateTexture(textureID);
        for (MTEHatch h : bioHatches) h.updateTexture(textureID);
    }

    @Override
    public void construct(ItemStack stackSize, boolean hintsOnly) {
        buildPiece(STRUCTURE_PIECE_MAIN, stackSize, hintsOnly, 5, 9, 1);
    }

    @Override
    public int survivalConstruct(ItemStack stackSize, int elementBudget, ISurvivalBuildEnvironment env) {
        if (mMachine) return -1;
        return survivalBuildPiece(STRUCTURE_PIECE_MAIN, stackSize, 5, 9, 1, elementBudget, env, false, true);
    }

    private int mCasingAmount;

    private void onCasingAdded() {
        mCasingAmount++;
    }

    @Override
    public boolean checkMachine(IGregTechTileEntity aBaseMetaTileEntity, ItemStack aStack) {
        mCasingAmount = 0;
        casingTier = -1;
        bioHatches.clear();
        mEnergyHatches.clear();

        if (!checkPiece(STRUCTURE_PIECE_MAIN, 5, 9, 1)) return false;
        if (casingTier < 1) return false;
        updateTextures();
        maxAOs = 500000 * casingTier;
        return mCasingAmount >= 0;
    }

    private boolean useNutrients(FluidStack fluid) {
        if (fluid == null) {
            return false;
        }

        for (MTEHatchInput hatch : mInputHatches) {
            if (drain(hatch, fluid, true)) {
                return true;
            }
        }
        return false;
    }

    private void triggerNutrientLoss() {
        currentSpecies.consumeAOs(currentSpecies.getCount() / 4);
    }

    private void triggerElectricityLoss() {
        currentSpecies.increaseSentience(1);
    }

    @Override
    public void onPostTick(IGregTechTileEntity aBaseMetaTileEntity, long aTick) {
        super.onPostTick(aBaseMetaTileEntity, aTick);

        if (!mMachine || !aBaseMetaTileEntity.isServerSide()
            || aTick % 5 != 0
            || currentSpecies == null
            || !currentSpecies.getFinalized()) return;

        boolean fluidChanged = false;
        if (tank.getFluidAmount() < tank.getCapacity()) {
            for (MTEHatchInput hatch : mInputHatches) {
                int remaining = tank.getCapacity() - tank.getFluidAmount();
                FluidStack drain = hatch.drain(remaining, true);
                if (drain.amount > 0) fluidChanged = true;
                tank.fill(drain, true);
            }
        }

        if (fluidChanged) aBaseMetaTileEntity.issueTileUpdate();

        currentSpecies.setMaxAOs(maxAOs);
        // TODO: REMOVE THIS
        currentSpecies.doReproduction();
        currentSpecies.increaseSentience(1);
        return;
        /*
         * if (currentSpecies.photosynthetic) {
         * if (!aBaseMetaTileEntity.getSkyAtSideAndDistance(ForgeDirection.UP, 5)) {
         * triggerElectricityLoss();
         * triggerNutrientLoss();
         * }
         * }
         * if (currentSpecies.cooperative) currentSpecies.increaseSentience(1);
         * if (!drainEnergyInput(powerUsage)) triggerElectricityLoss();
         * if (!useNutrients(nutrientUsage)) {
         * triggerNutrientLoss();
         * } else if (currentSpecies.getCount() < maxAOs) currentSpecies.doReproduction();
         */
    }

    FluidTank tank = new FluidTank(INTERNAL_FLUID_TANK_SIZE);

    @Override
    public NBTTagCompound getDescriptionData() {
        NBTTagCompound tag = new NBTTagCompound();
        if (tank.getFluid() != null) {
            NBTTagCompound fluidTag = new NBTTagCompound();
            tank.getFluid()
                .writeToNBT(fluidTag);
            tag.setTag("fluidTank", fluidTag);
        }
        return tag;
    }

    @Override
    public void onDescriptionPacket(NBTTagCompound tag) {
        if (tag.hasKey("fluidTank")) {
            FluidStack fluid = FluidStack.loadFluidStackFromNBT(tag.getCompoundTag("fluidTank"));
            tank.setFluid(fluid);
        } else {
            tank.setFluid(null);
        }
    }

    public LimitingItemStackHandler limitedHandler = new LimitingItemStackHandler(1, 1);

    @Override
    public void loadNBTData(NBTTagCompound aNBT) {
        super.loadNBTData(aNBT);
        currentSpecies = new ArtificialOrganism(aNBT);

        // so that the casing texture is applied on world load
        casingTier = aNBT.getInteger("casingTier");

        if (aNBT.hasKey("fluidTank")) {
            FluidStack fluid = FluidStack.loadFluidStackFromNBT(aNBT.getCompoundTag("fluidTank"));
            tank.setFluid(fluid);
        } else {
            tank.setFluid(null);
        }
        if (limitedHandler != null) {
            limitedHandler.deserializeNBT(aNBT.getCompoundTag("inventory"));
        }
    }

    @Override
    public void saveNBTData(NBTTagCompound aNBT) {
        super.saveNBTData(currentSpecies.saveAOToCompound(aNBT));
        aNBT.setInteger("casingTier", Math.max(1, casingTier));

        if (tank.getFluid() != null) {
            NBTTagCompound fluidTag = new NBTTagCompound();
            tank.getFluid()
                .writeToNBT(fluidTag);
            aNBT.setTag("fluidTank", fluidTag);
        }
        if (limitedHandler != null) {
            aNBT.setTag("inventory", limitedHandler.serializeNBT());
        }
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
    protected void setProcessingLogicPower(ProcessingLogic logic) {
        logic.setAvailableVoltage(GTUtility.roundUpVoltage(this.getMaxInputVoltage()));
        logic.setAvailableAmperage(1L);
    }

    private boolean addBioHatch(IGregTechTileEntity aTileEntity, int aBaseCasingIndex) {
        if (aTileEntity != null) {
            final IMetaTileEntity aMetaTileEntity = aTileEntity.getMetaTileEntity();
            if (aMetaTileEntity instanceof MTEHatchAOOutput hatch) {
                if (currentSpecies != null) hatch.setSpecies(currentSpecies);
                return bioHatches.add(hatch);
            }
        }
        return false;
    }

    @Override
    public boolean isRotationChangeAllowed() {
        return false;
    }

    @Override
    protected IAlignmentLimits getInitialAlignmentLimits() {
        return (d, r, f) -> d != ForgeDirection.UP && d != ForgeDirection.DOWN;
    }

    public void createNewAOs() {

        // Generate the nutrient cost for this species

        int amount = 16;
        Fluid type = Materials.NutrientBroth.mFluid;

        if (currentSpecies.immortal) amount = 0;
        else {
            if (currentSpecies.hiveMind) {
                type = Materials.NeuralFluid.mFluid;
                amount /= 4;
            }
            if (currentSpecies.photosynthetic) amount /= 4;
            if (currentSpecies.cancerous) amount *= 64;
        }

        nutrientUsage = new FluidStack(type, amount);

        currentSpecies.finalize(maxAOs);
        for (MTEHatchAOOutput hatch : bioHatches) hatch.setSpecies(currentSpecies);
    }

    @Override
    public boolean shouldCheckMaintenance() {
        return false;
    }

    @Override
    public void getWailaNBTData(EntityPlayerMP player, TileEntity tile, NBTTagCompound tag, World world, int x, int y,
        int z) {
        super.getWailaNBTData(player, tile, tag, world, x, y, z);
        tag.setInteger("casingTier", Math.max(1, casingTier));
    }

    @Override
    public void getWailaBody(ItemStack itemStack, List<String> currentTip, IWailaDataAccessor accessor,
        IWailaConfigHandler config) {
        super.getWailaBody(itemStack, currentTip, accessor, config);
        NBTTagCompound tag = accessor.getNBTData();
        currentTip.add("Tier: " + EnumChatFormatting.WHITE + tag.getInteger("casingTier"));
    }

    @Override
    public GUITextureSet getGUITextureSet() {
        return GUITextureSet.ORGANIC;
    }

    public boolean isValidCulture(ItemStack input) {
        return ArtificialOrganism.getTraitFromItem(input) != null;
    }

    public boolean canAddTrait() {
        return !currentSpecies.getFinalized() && currentSpecies.traits.size() < casingTier;
    }

    @Override
    protected boolean forceUseMui2() {
        return true;
    }

    @Override
    protected GTGuiTheme getGuiTheme() {
        return GTGuiThemes.ORGANIC;
    }

    @Override
    protected @NotNull MTEMultiBlockBaseGui<MTEEvolutionChamber> getGui() {
        return new MTEEvolutionChamberGui(this);
    }

    @Override
    public void renderTESR(double x, double y, double z, float partialTicks) {
        FluidTankInfo info = tank.getInfo();
        FluidStack fluid = info.fluid;
        if (fluid == null || fluid.amount <= 0) return;

        float fillRatio = (float) fluid.amount / INTERNAL_FLUID_TANK_SIZE;
        float fluidHeight = fillRatio * 7;

        // Get fluid icon and color
        IIcon icon = fluid.getFluid()
            .getIcon();
        int color = fluid.getFluid()
            .getColor(fluid);
        float r = ((color >> 16) & 0xFF) / 255.0f;
        float g = ((color >> 8) & 0xFF) / 255.0f;
        float b = (color & 0xFF) / 255.0f;

        // Bind texture
        Minecraft.getMinecraft()
            .getTextureManager()
            .bindTexture(TextureMap.locationBlocksTexture);

        GL11.glPushMatrix();
        GL11.glPushAttrib(GL11.GL_ALL_ATTRIB_BITS);

        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        ForgeDirection direction = getDirection();
        GL11.glTranslated(x - 3 + (direction.offsetX * -4), y + 1, z - 3 + (direction.offsetZ * -4));
        GL11.glColor4f(r, g, b, 0.99f);

        Tessellator tess = Tessellator.instance;
        tess.startDrawingQuads();

        // front
        tess.addVertexWithUV(0, fluidHeight, 0, icon.getMinU(), icon.getMaxV());
        tess.addVertexWithUV(7, fluidHeight, 0, icon.getMaxU(), icon.getMaxV());
        tess.addVertexWithUV(7, 0, 0, icon.getMaxU(), icon.getMinV());
        tess.addVertexWithUV(0, 0, 0, icon.getMinU(), icon.getMinV());

        // Back face (z = 7)
        tess.addVertexWithUV(0, fluidHeight, 7, icon.getMinU(), icon.getMaxV());
        tess.addVertexWithUV(0, 0, 7, icon.getMinU(), icon.getMinV());
        tess.addVertexWithUV(7, 0, 7, icon.getMaxU(), icon.getMinV());
        tess.addVertexWithUV(7, fluidHeight, 7, icon.getMaxU(), icon.getMaxV());

        // Left face (x = 0)
        tess.addVertexWithUV(0, 0, 0, icon.getMinU(), icon.getMinV());
        tess.addVertexWithUV(0, 0, 7, icon.getMaxU(), icon.getMinV());
        tess.addVertexWithUV(0, fluidHeight, 7, icon.getMaxU(), icon.getMaxV());
        tess.addVertexWithUV(0, fluidHeight, 0, icon.getMinU(), icon.getMaxV());

        // Right face (x = 7)
        tess.addVertexWithUV(7, fluidHeight, 0, icon.getMinU(), icon.getMaxV());
        tess.addVertexWithUV(7, fluidHeight, 7, icon.getMaxU(), icon.getMaxV());
        tess.addVertexWithUV(7, 0, 7, icon.getMaxU(), icon.getMinV());
        tess.addVertexWithUV(7, 0, 0, icon.getMinU(), icon.getMinV());

        // Top face (y = fluidHeight)
        tess.addVertexWithUV(0, fluidHeight, 7, icon.getMinU(), icon.getMaxV());
        tess.addVertexWithUV(7, fluidHeight, 7, icon.getMaxU(), icon.getMaxV());
        tess.addVertexWithUV(7, fluidHeight, 0, icon.getMaxU(), icon.getMinV());
        tess.addVertexWithUV(0, fluidHeight, 0, icon.getMinU(), icon.getMinV());

        // Bottom face (y = 0)
        tess.addVertexWithUV(0, 0, 0, icon.getMinU(), icon.getMinV());
        tess.addVertexWithUV(7, 0, 0, icon.getMaxU(), icon.getMinV());
        tess.addVertexWithUV(7, 0, 7, icon.getMaxU(), icon.getMaxV());
        tess.addVertexWithUV(0, 0, 7, icon.getMinU(), icon.getMaxV());

        tess.draw();
        GL11.glPopMatrix();
        GL11.glPopAttrib();
    }

}
