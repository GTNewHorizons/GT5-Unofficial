package gregtech.common.tileentities.machines.multi.artificialorganisms;

import static com.gtnewhorizon.structurelib.structure.StructureUtility.ofChain;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.onElementPass;
import static gregtech.api.enums.HatchElement.Energy;
import static gregtech.api.enums.HatchElement.InputBus;
import static gregtech.api.enums.HatchElement.InputHatch;
import static gregtech.api.enums.HatchElement.Maintenance;
import static gregtech.api.enums.Materials.NutrientBroth;
import static gregtech.api.enums.Materials.PrimordialSoup;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_BIOVAT_EMPTY;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_BIOVAT_EMPTY_GLOW;
import static gregtech.api.util.GTStructureUtility.buildHatchAdder;
import static gregtech.api.util.GTStructureUtility.chainAllGlasses;
import static gregtech.api.util.GTUtility.validMTEList;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;

import org.apache.commons.lang3.tuple.Pair;
import org.jetbrains.annotations.NotNull;

import com.google.common.collect.ImmutableList;
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
import gregtech.api.metatileentity.implementations.gui.MTEMultiBlockBaseGui;
import gregtech.api.modularui2.GTGuiTheme;
import gregtech.api.modularui2.GTGuiThemes;
import gregtech.api.objects.ArtificialOrganism;
import gregtech.api.render.TextureFactory;
import gregtech.api.util.GTUtility;
import gregtech.api.util.IGTHatchAdder;
import gregtech.api.util.MultiblockTooltipBuilder;
import gregtech.common.blocks.BlockCasings12;
import gregtech.common.tileentities.machines.IDualInputHatch;
import gtPlusPlus.xmod.gregtech.common.tileentities.machines.multi.gui.MTEEvolutionChamberGui;
import mcp.mobius.waila.api.IWailaConfigHandler;
import mcp.mobius.waila.api.IWailaDataAccessor;

public class MTEEvolutionChamber extends MTEExtendedPowerMultiBlockBase<MTEEvolutionChamber>
    implements ISurvivalConstructable {

    private static final String STRUCTURE_PIECE_MAIN = "main";
    private static final IStructureDefinition<MTEEvolutionChamber> STRUCTURE_DEFINITION = StructureDefinition
        .<MTEEvolutionChamber>builder()
        .addShape(
            STRUCTURE_PIECE_MAIN,
            new String[][] { { "BBB", "BAB", "BAB", "BAB", "B~B" }, { "BBB", "A A", "A A", "A A", "BBB" },
                { "BBB", "BAB", "BAB", "BAB", "BBB" } })
        .addElement(
            'B',
            ofChain(
                buildHatchAdder(MTEEvolutionChamber.class)
                    .atLeast(InputBus, Maintenance, Energy, InputHatch, SpecialHatchElement.BioOutput)
                    .casingIndex(((BlockCasings12) GregTechAPI.sBlockCasings12).getTextureIndex(0))
                    .dot(1)
                    .build(),
                onElementPass(
                    MTEEvolutionChamber::onCasingAdded,
                    StructureUtility.ofBlocksTiered(
                        MTEEvolutionChamber::getTierFromMeta,
                        ImmutableList.of(
                            Pair.of(GregTechAPI.sBlockCasings12, 0),
                            Pair.of(GregTechAPI.sBlockCasings12, 1),
                            Pair.of(GregTechAPI.sBlockCasings12, 2)),
                        -1,
                        MTEEvolutionChamber::setCasingTier,
                        MTEEvolutionChamber::getCasingTier))))
        .addElement('A', chainAllGlasses())
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

    private int status = 0;

    public final int INTERNAL_FLUID_TANK_SIZE = 64000;

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
        if (block != GregTechAPI.sBlockCasings12) return -1;
        if (metaID < 0 || metaID > 2) return -2;
        return metaID + 1;
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
        casingTier = aValue;
    }

    @Override
    public byte getUpdateData() {
        return (byte) casingTier;
    }

    @Override
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new MTEEvolutionChamber(this.mName);
    }

    // Returns the fill level of either nutrient broth or primordial soup
    public int getFillLevel() {
        int fill = 0;

        for (MTEHatchInput tHatch : validMTEList(mInputHatches)) {
            FluidStack f = tHatch.getFluid();
            // add early return when null, if its empty the machine can crash during init for some reason
            // should be removed when the tank system works.
            if (f == null) return fill;
            if (currentSpecies.getFinalized()) {
                if (f.getFluid() == PrimordialSoup.mFluid) fill += f.amount;
            } else {
                if (f.getFluid() == NutrientBroth.mFluid) fill += f.amount;
            }
        }

        return fill;
    }

    @Override
    public ITexture[] getTexture(IGregTechTileEntity baseMetaTileEntity, ForgeDirection side, ForgeDirection aFacing,
        int colorIndex, boolean aActive, boolean redstoneLevel) {
        ITexture[] rTexture;
        int casingMeta = Math.max(casingTier - 1, 0);
        if (side == aFacing) {
            rTexture = new ITexture[] {
                Textures.BlockIcons
                    .getCasingTextureForId(GTUtility.getCasingTextureIndex(GregTechAPI.sBlockCasings12, casingMeta)),
                TextureFactory.builder()
                    .addIcon(OVERLAY_FRONT_BIOVAT_EMPTY)
                    .extFacing()
                    .build(),
                TextureFactory.builder()
                    .addIcon(OVERLAY_FRONT_BIOVAT_EMPTY_GLOW)
                    .extFacing()
                    .glow()
                    .build() };
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
        int textureID = GTUtility.getCasingTextureIndex(GregTechAPI.sBlockCasings12, casingTier - 1);
        for (MTEHatch h : mInputBusses) h.updateTexture(textureID);
        for (MTEHatch h : mInputHatches) h.updateTexture(textureID);
        for (IDualInputHatch h : mDualInputHatches) h.updateTexture(textureID);
        for (MTEHatch h : mMaintenanceHatches) h.updateTexture(textureID);
        for (MTEHatch h : mEnergyHatches) h.updateTexture(textureID);
        for (MTEHatch h : bioHatches) h.updateTexture(textureID);
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

        if (!checkPiece(STRUCTURE_PIECE_MAIN, 1, 4, 0)) return false;
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

        if (status == 1) {
            return;
        }

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

    @Override
    public void loadNBTData(NBTTagCompound aNBT) {
        super.loadNBTData(aNBT);
        currentSpecies = new ArtificialOrganism(aNBT);

        // so that the casing texture is applied on world load
        casingTier = aNBT.getInteger("casingTier");
    }

    @Override
    public void saveNBTData(NBTTagCompound aNBT) {
        super.saveNBTData(currentSpecies.saveAOToCompound(aNBT));
        aNBT.setInteger("casingTier", Math.max(0, casingTier));
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
        tag.setInteger("casingTier", Math.max(0, casingTier));
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
    protected @NotNull MTEMultiBlockBaseGui getGui() {
        return new MTEEvolutionChamberGui(this);
    }

}
