package goodgenerator.blocks.tileEntity;

import static com.gtnewhorizon.structurelib.structure.StructureUtility.lazy;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.ofBlock;
import static gregtech.api.enums.Textures.BlockIcons.*;
import static gregtech.api.metatileentity.BaseTileEntity.TOOLTIP_DELAY;
import static gregtech.api.util.GTStructureUtility.buildHatchAdder;
import static gregtech.api.util.GTStructureUtility.ofFrame;
import static gregtech.common.misc.WirelessNetworkManager.addEUToGlobalEnergyMap;
import static gregtech.common.misc.WirelessNetworkManager.strongCheckOrAddUser;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.StatCollector;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.FluidStack;

import com.gtnewhorizon.structurelib.alignment.constructable.IConstructable;
import com.gtnewhorizon.structurelib.alignment.constructable.ISurvivalConstructable;
import com.gtnewhorizon.structurelib.structure.IStructureDefinition;
import com.gtnewhorizon.structurelib.structure.ISurvivalBuildEnvironment;
import com.gtnewhorizon.structurelib.structure.StructureDefinition;
import com.gtnewhorizons.modularui.api.drawable.IDrawable;
import com.gtnewhorizons.modularui.api.drawable.UITexture;
import com.gtnewhorizons.modularui.api.screen.ModularWindow;
import com.gtnewhorizons.modularui.api.screen.UIBuildContext;
import com.gtnewhorizons.modularui.common.widget.ButtonWidget;
import com.gtnewhorizons.modularui.common.widget.FakeSyncWidget;

import bartworks.common.loaders.ItemRegistry;
import goodgenerator.blocks.structures.AntimatterStructures;
import goodgenerator.loader.Loaders;
import gregtech.api.GregTechAPI;
import gregtech.api.enums.HatchElement;
import gregtech.api.enums.Materials;
import gregtech.api.enums.MaterialsUEVplus;
import gregtech.api.enums.Textures;
import gregtech.api.gui.modularui.GTUITextures;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.implementations.MTEExtendedPowerMultiBlockBase;
import gregtech.api.metatileentity.implementations.MTEHatch;
import gregtech.api.recipe.RecipeMap;
import gregtech.api.recipe.check.CheckRecipeResult;
import gregtech.api.recipe.check.CheckRecipeResultRegistry;
import gregtech.api.render.TextureFactory;
import gregtech.api.util.GTUtility;
import gregtech.api.util.HatchElementBuilder;
import gregtech.api.util.MultiblockTooltipBuilder;
import kekztech.client.gui.KTUITextures;
import tectech.thing.metaTileEntity.hatch.MTEHatchDynamoTunnel;

public class AntimatterGenerator extends MTEExtendedPowerMultiBlockBase
    implements IConstructable, ISurvivalConstructable {

    public static final String MAIN_NAME = "antimatterGenerator";
    protected IStructureDefinition<AntimatterGenerator> multiDefinition = null;
    protected long trueOutput = 0;
    protected int trueEff = 0;
    protected int times = 1;
    private UUID owner_uuid;
    private boolean wirelessEnabled = false;
    private boolean canUseWireless = true;

    private static final ClassValue<IStructureDefinition<AntimatterGenerator>> STRUCTURE_DEFINITION = new ClassValue<IStructureDefinition<AntimatterGenerator>>() {

        @Override
        protected IStructureDefinition<AntimatterGenerator> computeValue(Class<?> type) {
            return StructureDefinition.<AntimatterGenerator>builder()
                .addShape(MAIN_NAME, AntimatterStructures.ANTIMATTER_GENERATOR)
                .addElement('F', lazy(x -> ofFrame(Materials.Naquadria))) // Naquadria Frame Box
                .addElement('D', lazy(x -> ofBlock(x.getCasingBlock(1), x.getCasingMeta(1)))) // Black Casing
                .addElement('G', lazy(x -> ofBlock(x.getCoilBlock(1), x.getCoilMeta(1)))) // Annihilation Coil
                .addElement('B', lazy(x -> ofBlock(x.getCoilBlock(2), x.getCoilMeta(2)))) // Containment Coil
                .addElement('C', lazy(x -> ofBlock(x.getCasingBlock(2), x.getCasingMeta(2)))) // White Casing
                .addElement('A', lazy(x -> ofBlock(x.getGlassBlock(), x.getGlassMeta()))) // Glass
                .addElement('E', lazy(x -> ofBlock(GregTechAPI.sBlockCasings9, 1))) // Filter Casing
                .addElement(
                    'H',
                    lazy(
                        x -> HatchElementBuilder.<AntimatterGenerator>builder()
                            .anyOf(HatchElement.ExoticEnergy)
                            .adder(AntimatterGenerator::addLaserSource)
                            .casingIndex(x.textureIndex(2))
                            .dot(1)
                            .buildAndChain(x.getCasingBlock(2), x.getCasingMeta(2))))
                .addElement(
                    'I',
                    lazy(
                        x -> buildHatchAdder(AntimatterGenerator.class).atLeast(HatchElement.InputHatch)
                            .casingIndex(x.textureIndex(1))
                            .dot(2)
                            .buildAndChain(x.getCasingBlock(1), x.getCasingMeta(1))))
                .build();
        }
    };

    static {
        Textures.BlockIcons.setCasingTextureForId(
            53,
            TextureFactory.of(
                TextureFactory.builder()
                    .addIcon(MACHINE_CASING_ANTIMATTER)
                    .extFacing()
                    .build(),
                TextureFactory.builder()
                    .addIcon(MACHINE_CASING_ANTIMATTER_GLOW)
                    .extFacing()
                    .glow()
                    .build()));
    };

    private boolean addLaserSource(IGregTechTileEntity aBaseMetaTileEntity, int aBaseCasingIndex) {
        IMetaTileEntity aMetaTileEntity = aBaseMetaTileEntity.getMetaTileEntity();
        if (aMetaTileEntity == null) return false;
        if (aMetaTileEntity instanceof MTEHatchDynamoTunnel tHatch) {
            tHatch.updateTexture(aBaseCasingIndex);
            return mExoticEnergyHatches.add(tHatch);
        }
        return false;
    }

    public AntimatterGenerator(String name) {
        super(name);
    }

    public AntimatterGenerator(int id, String name, String nameRegional) {
        super(id, name, nameRegional);
    }

    @Override
    public boolean isCorrectMachinePart(ItemStack aStack) {
        return true;
    }

    @Override
    public RecipeMap<?> getRecipeMap() {
        return null;
    }

    @Override
    public String[] getInfoData() {
        String[] info = super.getInfoData();
        info[4] = "Probably makes: " + EnumChatFormatting.RED
            + GTUtility.formatNumbers(Math.abs(this.trueOutput))
            + EnumChatFormatting.RESET
            + EnumChatFormatting.RESET
            + " Efficiency: "
            + EnumChatFormatting.YELLOW
            + trueEff
            + EnumChatFormatting.RESET
            + " %";
        return info;
    }

    @Override
    public CheckRecipeResult checkProcessing() {
        startRecipeProcessing();
        List<FluidStack> inputFluids = getStoredFluids();
        long containedAntimatter = 0;
        FluidStack catalystFluid = null;
        int i = 0;

        while (i < inputFluids.size()) {
            FluidStack inputFluid = inputFluids.get(i);
            if (inputFluid.isFluidEqual(MaterialsUEVplus.Antimatter.getFluid(1))) {
                containedAntimatter += inputFluid.amount;
            } else {
                catalystFluid = inputFluid.copy();
            }
            // We annihilate everything, even if it was the wrong fluid
            inputFluid.amount = 0;
            i++;
        }
        // If i != 2, we iterated more than 2 times and have too many fluids.
        if (i == 2 && containedAntimatter > 0 && catalystFluid != null) {
            createEU(containedAntimatter, catalystFluid);
        }

        endRecipeProcessing();
        return CheckRecipeResultRegistry.SUCCESSFUL;
    }

    // (Antimatter^(EXP) * 1e12 )/(Math.min((Antimatter/Matter),(Matter/Antimatter)))
    public void createEU(long antimatter, FluidStack catalyst) {
        Float modifier = null;

        if (catalyst.isFluidEqual(Materials.Copper.getMolten(1L))) {
            modifier = 1.0F;
        } else if (catalyst.isFluidEqual(Materials.SuperconductorUIVBase.getMolten(1L))) {
            modifier = 1.02F;
        } else if (catalyst.isFluidEqual(Materials.SuperconductorUMVBase.getMolten(1L))) {
            modifier = 1.03F;
        } else if (catalyst.isFluidEqual(MaterialsUEVplus.BlackDwarfMatter.getMolten(1L))) {
            modifier = 1.04F;
        }
        long catalystCount = catalyst.amount;
        long generatedEU = 0;

        if (modifier != null) {
            generatedEU = (long) ((Math.pow(antimatter, modifier) * 1e12) * (Math
                .min(((float) antimatter / (float) catalystCount), ((float) catalystCount / (float) antimatter))));
        }

        if (wirelessEnabled && modifier >= 1.03F) {
            // Clamp the EU to the maximum of the hatches so wireless cannot bypass the limitations
            long euCapacity = 0;
            for (MTEHatch tHatch : getExoticEnergyHatches()) {
                if (tHatch instanceof MTEHatchDynamoTunnel tLaserSource) {
                    euCapacity += tLaserSource.maxEUStore();
                }
            }
            generatedEU = Math.min(generatedEU, euCapacity);
            addEUToGlobalEnergyMap(owner_uuid, generatedEU);
        } else {
            float invHatchCount = 1.0F / (float) mExoticEnergyHatches.size();
            for (MTEHatch tHatch : getExoticEnergyHatches()) {
                if (tHatch instanceof MTEHatchDynamoTunnel tLaserSource) {
                    tLaserSource.setEUVar(tLaserSource.getEUVar() + (long) (generatedEU * invHatchCount));
                }
            }
        }
    }

    @Override
    public boolean checkMachine(IGregTechTileEntity aBaseMetaTileEntity, ItemStack aStack) {
        return checkPiece(MAIN_NAME, 17, 41, 0);
    }

    @Override
    public int survivalConstruct(ItemStack stackSize, int elementBudget, ISurvivalBuildEnvironment env) {
        if (mMachine) return -1;
        return survivialBuildPiece(MAIN_NAME, stackSize, 17, 41, 0, elementBudget, env, false, true);
    }

    @Override
    public void construct(ItemStack itemStack, boolean hintsOnly) {
        buildPiece(MAIN_NAME, itemStack, hintsOnly, 17, 41, 0);
    }

    @Override
    public void saveNBTData(NBTTagCompound nbt) {
        nbt = (nbt == null) ? new NBTTagCompound() : nbt;
        nbt.setBoolean("wirelessEnabled", wirelessEnabled);
        super.saveNBTData(nbt);
    }

    @Override
    public void loadNBTData(NBTTagCompound nbt) {
        nbt = (nbt == null) ? new NBTTagCompound() : nbt;
        wirelessEnabled = nbt.getBoolean("wirelessEnabled");
        super.loadNBTData(nbt);
    }

    @Override
    public int getMaxEfficiency(ItemStack aStack) {
        return 10000;
    }

    @Override
    public int getPollutionPerTick(ItemStack aStack) {
        return 0;
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
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new AntimatterGenerator(this.MAIN_NAME);
    }

    @Override
    public void onScrewdriverRightClick(ForgeDirection side, EntityPlayer aPlayer, float aX, float aY, float aZ) {
        wirelessEnabled = !wirelessEnabled;
        GTUtility.sendChatToPlayer(aPlayer, "Wireless network mode " + (wirelessEnabled ? "enabled." : "disabled."));
        if (wirelessEnabled) {
            GTUtility.sendChatToPlayer(aPlayer, "Wireless only works with UMV Superconductor Base or better.");
        }
    }

    @Override
    public void onPreTick(IGregTechTileEntity aBaseMetaTileEntity, long aTick) {
        super.onPreTick(aBaseMetaTileEntity, aTick);

        if (aBaseMetaTileEntity.isServerSide()) {

            // On first tick find the player name and attempt to add them to the map.
            if (aTick == 1) {

                // UUID and username of the owner.
                owner_uuid = aBaseMetaTileEntity.getOwnerUuid();

                strongCheckOrAddUser(owner_uuid);
            }
        }
    }

    @Override
    protected MultiblockTooltipBuilder createTooltip() {
        final MultiblockTooltipBuilder tt = new MultiblockTooltipBuilder();
        tt.addMachineType("Antimatter Generator")
            .addInfo("Annihilating antimatter like it's 2205!")
            .addSeparator()
            .addInfo("Generates energy by reacting Semi-Stable Antimatter with matter")
            .addInfo("Annihilation uses an equal amount of antimatter and matter")
            .addInfo(
                "Consumes " + EnumChatFormatting.GOLD
                    + "all inputs"
                    + EnumChatFormatting.GRAY
                    + " every processing cycle")
            .addInfo(
                "Imbalance between antimatter and matter " + EnumChatFormatting.RED
                    + "will waste energy!"
                    + EnumChatFormatting.GRAY)
            .addInfo(
                "Any EU that does not fit in laser hatches will be " + EnumChatFormatting.RED
                    + "voided"
                    + EnumChatFormatting.GRAY)
            .addSeparator()
            .addInfo("Antimatter base energy value: 1,000,000,000 EU/L")
            .addInfo("Energy production is exponentially increased depending on the matter used:")
            .addInfo("Molten Copper: 1.00")
            .addInfo("Molten SC UIV Base: 1.02")
            .addInfo("Molten SC UMV Base: 1.03")
            .addInfo("Molten Black Dwarf Matter: 1.04")
            .addSeparator()
            .addInfo("Enable wireless EU mode with screwdriver")
            .addInfo("Wireless mode requires SC UMV Base or better")
            .addInfo("Wireless mode uses hatch capacity limit")
            .addDynamoHatch("1-9, Hint block with dot 4", 4)
            .addInputHatch("1-6, Hint block with dot 1", 1)
            .toolTipFinisher("Good Generator");
        return tt;
    }

    protected boolean canUseWireless() {
        return true;
    }

    @Override
    public void addUIWidgets(ModularWindow.Builder builder, UIBuildContext buildContext) {
        super.addUIWidgets(builder, buildContext);
        builder.widget(new ButtonWidget().setOnClick((clickData, widget) -> {
            if (!widget.isClient()) {
                canUseWireless = canUseWireless();
            }
            if (canUseWireless) {
                wirelessEnabled = !wirelessEnabled;
            }
        })
            .setPlayClickSound(true)
            .setBackground(() -> {
                List<UITexture> ret = new ArrayList<>();
                ret.add(GTUITextures.BUTTON_STANDARD);
                if (canUseWireless) {
                    if (wirelessEnabled) {
                        ret.add(KTUITextures.OVERLAY_BUTTON_WIRELESS_ON);
                    } else {
                        ret.add(KTUITextures.OVERLAY_BUTTON_WIRELESS_OFF);
                    }
                } else {
                    ret.add(KTUITextures.OVERLAY_BUTTON_WIRELESS_OFF_DISABLED);
                }
                return ret.toArray(new IDrawable[0]);
            })
            .setPos(80, 91)
            .setSize(16, 16)
            .addTooltip(StatCollector.translateToLocal("gui.kekztech_lapotronicenergyunit.wireless"))
            .setTooltipShowUpDelay(TOOLTIP_DELAY))
            .widget(new FakeSyncWidget.BooleanSyncer(() -> wirelessEnabled, val -> wirelessEnabled = val))
            .widget(new FakeSyncWidget.BooleanSyncer(this::canUseWireless, val -> canUseWireless = val));
    }

    @Override
    public IStructureDefinition<AntimatterGenerator> getStructureDefinition() {
        return STRUCTURE_DEFINITION.get(getClass());
    }

    private static final ITexture textureOverlay = TextureFactory.of(
        TextureFactory.builder()
            .addIcon(OVERLAY_FUSION1)
            .extFacing()
            .build(),
        TextureFactory.builder()
            .addIcon(OVERLAY_FUSION1_GLOW)
            .extFacing()
            .glow()
            .build());

    public ITexture getTextureOverlay() {
        return textureOverlay;
    }

    @Override
    @SuppressWarnings("ALL")
    public ITexture[] getTexture(IGregTechTileEntity aBaseMetaTileEntity, ForgeDirection side, ForgeDirection facing,
        int colorIndex, boolean aActive, boolean aRedstone) {
        if (side == facing) return new ITexture[] { TextureFactory.builder()
            .addIcon(MACHINE_CASING_ANTIMATTER)
            .extFacing()
            .build(), getTextureOverlay() };
        if (aActive) return new ITexture[] { Textures.BlockIcons.getCasingTextureForId(53) };
        return new ITexture[] { TextureFactory.builder()
            .addIcon(MACHINE_CASING_ANTIMATTER)
            .extFacing()
            .build() };
    }

    public Block getCoilBlock(int type) {
        switch (type) {
            case 1:
                return Loaders.antimatterAnnihilationMatrix;
            case 2:
                return Loaders.protomatterActivationCoil;
            default:
                return Loaders.antimatterAnnihilationMatrix;
        }
    }

    public int getCoilMeta(int type) {
        switch (type) {
            case 1:
                return 0;
            case 2:
                return 0;
            default:
                return 0;
        }
    }

    public Block getCasingBlock(int type) {
        switch (type) {
            case 1:
                return Loaders.magneticFluxCasing;
            case 2:
                return Loaders.gravityStabilizationCasing;
            default:
                return Loaders.magneticFluxCasing;
        }
    }

    public int getCasingMeta(int type) {
        switch (type) {
            case 1:
                return 0;
            case 2:
                return 0;
            default:
                return 0;
        }
    }

    public Block getFrameBlock() {
        return Loaders.antimatterContainmentCasing;
    }

    public int getFrameMeta() {
        return 0;
    }

    public Block getGlassBlock() {
        return ItemRegistry.bw_realglas2;
    }

    public int getGlassMeta() {
        return 0;
    }

    public int textureIndex(int type) {
        switch (type) {
            case 1:
                return (12 << 7) + 9;
            case 2:
                return (12 << 7) + 10;
            default:
                return (12 << 7) + 9;
        }
    }

    public int hatchTier() {
        return 6;
    }

    @Override
    public boolean getDefaultHasMaintenanceChecks() {
        return false;
    }

}
