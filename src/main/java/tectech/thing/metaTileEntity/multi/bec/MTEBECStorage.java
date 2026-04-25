package tectech.thing.metaTileEntity.multi.bec;

import static gregtech.api.casing.Casings.AdvancedFusionCoilII;
import static gregtech.api.casing.Casings.ElectromagneticWaveguide;
import static gregtech.api.casing.Casings.ElectromagneticallyIsolatedCasing;
import static gregtech.api.casing.Casings.FineStructureConstantManipulator;
import static gregtech.api.casing.Casings.SuperconductivePlasmaEnergyConduit;
import static gregtech.api.enums.HatchElement.Energy;
import static gregtech.api.enums.HatchElement.ExoticEnergy;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

import javax.annotation.Nonnull;

import net.minecraft.client.Minecraft;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.FluidRegistry;

import org.jetbrains.annotations.NotNull;

import com.cleanroommc.modularui.api.drawable.IKey;
import com.cleanroommc.modularui.api.widget.IWidget;
import com.cleanroommc.modularui.screen.ModularPanel;
import com.cleanroommc.modularui.value.sync.DoubleSyncValue;
import com.cleanroommc.modularui.value.sync.GenericSyncValue;
import com.cleanroommc.modularui.value.sync.PanelSyncManager;
import com.cleanroommc.modularui.widget.Widget;
import com.cleanroommc.modularui.widgets.ListWidget;
import com.cleanroommc.modularui.widgets.TextWidget;
import com.gtnewhorizon.gtnhlib.util.numberformatting.NumberFormatUtil;
import com.gtnewhorizon.structurelib.structure.IStructureDefinition;

import appeng.api.storage.data.IAEFluidStack;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import gregtech.api.enums.CondensateType;
import gregtech.api.enums.GTAuthors;
import gregtech.api.enums.SoundResource;
import gregtech.api.enums.Textures;
import gregtech.api.interfaces.IHatchElement;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.OCMethod;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.recipe.check.CheckRecipeResult;
import gregtech.api.recipe.check.CheckRecipeResultRegistry;
import gregtech.api.render.TextureFactory;
import gregtech.api.structure.StructureWrapperTooltipBuilder;
import gregtech.api.util.IGTHatchAdder;
import gregtech.api.util.IntFraction;
import gregtech.api.util.MultiblockTooltipBuilder;
import gregtech.api.util.shutdown.ShutDownReason;
import gregtech.client.GTSoundLoop;
import gregtech.client.volumetric.CircularSound;
import gregtech.client.volumetric.LinearSound;
import gregtech.common.gui.modularui.adapter.CondensateListAdapter;
import gregtech.common.gui.modularui.multiblock.base.MTEMultiBlockBaseGui;
import gregtech.common.gui.modularui.multiblock.base.TTMultiblockBaseGui;
import gregtech.common.gui.modularui.widget.settings.SettingsPanel;
import it.unimi.dsi.fastutil.Pair;
import tectech.mechanics.boseEinsteinCondensate.BECInventory;
import tectech.mechanics.boseEinsteinCondensate.CondensateList;
import tectech.thing.CustomItemList;
import tectech.thing.metaTileEntity.hatch.bec.MTEHatchCondensateDetector;
import tectech.thing.metaTileEntity.multi.base.MTEBECMultiblockBase;
import tectech.thing.metaTileEntity.multi.structures.BECStructureDefinitions;

public class MTEBECStorage extends MTEBECMultiblockBase<MTEBECStorage> implements BECInventory {

    private final CondensateList storedCondensate = new CondensateList();
    private final HashSet<MTEHatchCondensateDetector> condensateDetectors = new HashSet<>();

    private boolean contentsChanged = false;

    private long fieldStrength;
    private GTSoundLoop pillar, torus, torusFar;

    public MTEBECStorage(int aID, String aName) {
        super(aID, aName);
    }

    protected MTEBECStorage(MTEBECStorage prototype) {
        super(prototype);
    }

    @Override
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new MTEBECStorage(this);
    }

    @Override
    public String[][] getDefinition() {
        return BECStructureDefinitions.BEC_CONTAINMENT_FIELD;
    }

    @Override
    public IStructureDefinition<MTEBECStorage> compile(String[][] definition) {
        structure.addCasing('A', SuperconductivePlasmaEnergyConduit);
        structure.addCasing('B', ElectromagneticallyIsolatedCasing)
            .withHatches(1, 16, Arrays.asList(Energy, ExoticEnergy, DetectorHatchElement.INSTANCE));
        structure.addCasing('C', FineStructureConstantManipulator);
        structure.addCasing('D', ElectromagneticWaveguide);
        structure.addCasing('E', AdvancedFusionCoilII);
        structure.addCasing('1', FineStructureConstantManipulator)
            .withHatches(2, 4, Arrays.asList(BECHatches.Hatch));

        return structure.buildStructure(definition);
    }

    @Override
    public void clearHatches() {
        super.clearHatches();

        condensateDetectors.clear();
    }

    @Override
    protected ITexture getActiveTexture() {
        return TextureFactory.builder()
            .addIcon(Textures.BlockIcons.BEC_STORAGE_ACTIVE)
            .extFacing()
            .glow()
            .build();
    }

    @Override
    protected boolean useMui2() {
        return true;
    }

    @Override
    protected @NotNull MTEMultiBlockBaseGui<?> getGui() {
        return new Gui();
    }

    @Override
    protected MultiblockTooltipBuilder createTooltip() {
        StructureWrapperTooltipBuilder<MTEBECStorage> tt = new StructureWrapperTooltipBuilder<>(structure);

        tt.addMachineType("BEC Storage, Entangled Condensate Storage")
            .addMarkdown(new ResourceLocation("gregtech", "bec-storage"));

        tt.beginStructureBlock();
        tt.addController("Outer wall of center pillar, second layer from the bottom");
        tt.addHatchNameOverride(BECHatches.Hatch, CustomItemList.Hatch_BEC_Connector.get(1));
        tt.addAllCasingInfo(
            Arrays.asList(
                SuperconductivePlasmaEnergyConduit,
                ElectromagneticallyIsolatedCasing,
                FineStructureConstantManipulator,
                ElectromagneticWaveguide,
                AdvancedFusionCoilII),
            null);

        tt.toolTipFinisher(GTAuthors.AuthorPineapple);

        return tt;
    }

    @Override
    protected ITexture getCasingTexture() {
        return FineStructureConstantManipulator.getCasingTexture();
    }

    @Override
    public void saveNBTData(NBTTagCompound aNBT) {
        super.saveNBTData(aNBT);

        aNBT.setTag("condensate", storedCondensate.saveToNBT());
        aNBT.setLong("fieldStrength", fieldStrength);
    }

    @Override
    public void loadNBTData(NBTTagCompound aNBT) {
        super.loadNBTData(aNBT);

        storedCondensate.loadFromNBT(aNBT.getCompoundTag("condensate"));
        fieldStrength = aNBT.getLong("fieldStrength");
    }

    @Override
    protected @NotNull CheckRecipeResult checkProcessing_EM() {
        mMaxProgresstime = 20;
        mEfficiency = 10_000;
        useLongPower = true;
        lEUt = -fieldStrength;

        if (getAmountStored() > fieldStrength) {
            IntFraction decay = new IntFraction(9, 10);

            storedCondensate.replaceAll((mat, amount) -> decay.apply(amount));

            storedCondensate.values()
                .rem(0);
        }

        return CheckRecipeResultRegistry.SUCCESSFUL;
    }

    private double getAmountStored() {
        double sum = 0;

        for (long l : storedCondensate.values()) {
            sum += l;
        }

        return sum;
    }

    @Override
    public void stopMachine(@Nonnull ShutDownReason reason) {
        super.stopMachine(reason);
        storedCondensate.clear();
    }

    @Override
    public List<Pair<Class<?>, Object>> getComponents() {
        return Arrays.asList(Pair.of(BECInventory.class, this));
    }

    @Override
    public @NotNull CondensateList getContents() {
        return storedCondensate;
    }

    @Override
    public double getCondensateCapacity() {
        return fieldStrength;
    }

    @Override
    public void addCondensate(IAEFluidStack stack) {
        storedCondensate.addTo(stack.getFluid(), stack.getStackSize());
        stack.setStackSize(0);

        contentsChanged = true;
    }

    @Override
    public boolean removeCondensate(IAEFluidStack stack) {
        long stored = storedCondensate.getLong(stack.getFluid());

        if (stored <= 0) {
            return false;
        }

        long toConsume = Math.min(stored, stack.getStackSize());

        stack.decStackSize(toConsume);
        stored -= toConsume;

        if (stored == 0) {
            storedCondensate.removeLong(stack.getFluid());
        } else {
            storedCondensate.put(stack.getFluid(), stored);
        }

        contentsChanged = true;

        return stack.getStackSize() == 0;
    }

    @Override
    public void onPostTick(IGregTechTileEntity aBaseMetaTileEntity, long aTick) {
        super.onPostTick(aBaseMetaTileEntity, aTick);

        if (contentsChanged) {
            contentsChanged = false;

            for (MTEHatchCondensateDetector hatch : condensateDetectors) {
                hatch.updateAmount(this);
            }
        }
    }

    @SideOnly(Side.CLIENT)
    protected void doActivitySound(SoundResource activitySound) {
        if (getBaseMetaTileEntity().isActive()) {
            if (torus == null) {
                torus = new GTSoundLoop(
                    SoundResource.GT_MACHINES_BEC_GENERATOR.resourceLocation,
                    getBaseMetaTileEntity(),
                    false,
                    true,
                    GTSoundLoop.VOLUME_RAMP * 4);
                torus.setVolume(2);
                torus.setPosition(new CircularSound(this, 0, 8, 7, 0, 1, 0, 17, 90));

                Minecraft.getMinecraft()
                    .getSoundHandler()
                    .playSound(torus);
            }

            if (torusFar == null) {
                torusFar = new GTSoundLoop(
                    SoundResource.GT_MACHINES_BEC_GENERATOR.resourceLocation,
                    getBaseMetaTileEntity(),
                    false,
                    true,
                    GTSoundLoop.VOLUME_RAMP * 4);
                torusFar.setVolume(2);
                torus.setPosition(new CircularSound(this, 0, 8, 7, 0, 1, 0, 17, -90));

                Minecraft.getMinecraft()
                    .getSoundHandler()
                    .playSound(torusFar);
            }

            if (pillar == null) {
                pillar = new GTSoundLoop(
                    SoundResource.GT_MACHINES_ARC_FURNACE_LOOP.resourceLocation,
                    getBaseMetaTileEntity(),
                    false,
                    true,
                    GTSoundLoop.VOLUME_RAMP * 4);
                pillar.setVolume(2);
                torus.setPosition(new LinearSound(this, 0, 2, 7, 0, 14, 7, 64));

                Minecraft.getMinecraft()
                    .getSoundHandler()
                    .playSound(pillar);
            }
        } else {
            if (pillar != null) pillar.stop();
            if (torus != null) torus.stop();
            if (torusFar != null) torusFar.stop();

            pillar = null;
            torus = null;
            torusFar = null;
        }
    }

    @OCMethod
    public void setFieldStrength(long strength) {
        fieldStrength = strength;
    }

    @OCMethod
    public long getFieldStrength() {
        return fieldStrength;
    }

    @OCMethod
    public List<Pair<String, Long>> getStoredCondensate() {
        return storedCondensate.object2LongEntrySet()
            .stream()
            .map(e -> Pair.of(FluidRegistry.getFluidName(e.getKey()), e.getLongValue()))
            .collect(Collectors.toList());
    }

    private static class DetectorHatchElement implements IHatchElement<MTEBECStorage> {

        public static final DetectorHatchElement INSTANCE = new DetectorHatchElement();

        @Override
        public List<? extends Class<? extends IMetaTileEntity>> mteClasses() {
            return Collections.singletonList(MTEHatchCondensateDetector.class);
        }

        @Override
        public IGTHatchAdder<? super MTEBECStorage> adder() {
            return (self, igte, texture) -> {
                IMetaTileEntity imte = igte.getMetaTileEntity();

                if (imte instanceof MTEHatchCondensateDetector hatch) {
                    hatch.updateTexture(texture);
                    hatch.updateCraftingIcon(self.getMachineCraftingIcon());
                    self.condensateDetectors.add(hatch);
                    return true;
                } else {
                    return false;
                }
            };
        }

        @Override
        public String name() {
            return "DetectorHatchElement";
        }

        @Override
        public String getDisplayName() {
            return CustomItemList.Hatch_BEC_CondensateDetector.getDisplayName();
        }

        @Override
        public long count(MTEBECStorage storage) {
            return storage.condensateDetectors.size();
        }
    }

    private class Gui extends TTMultiblockBaseGui<MTEBECStorage> {

        public Gui() {
            super(MTEBECStorage.this);
        }

        @Override
        protected ListWidget<IWidget, ?> createTerminalTextWidget(PanelSyncManager syncManager, ModularPanel parent) {
            GenericSyncValue<CondensateList> contents = GenericSyncValue.builder(CondensateList.class)
                .getter(() -> storedCondensate)
                .adapter(new CondensateListAdapter())
                .build();

            syncManager.syncValue("contents", contents);

            TextWidget<?> contentsWidget = IKey.dynamic(() -> {
                StringBuilder ret = new StringBuilder();

                ret.append(EnumChatFormatting.GRAY)
                    .append("Stored Condensate:\n");

                if (contents.getValue()
                    .isEmpty()) {
                    ret.append(EnumChatFormatting.GRAY)
                        .append("None");
                }

                for (var e : contents.getValue()
                    .object2LongEntrySet()) {
                    ret.append("  ")
                        .append(EnumChatFormatting.AQUA)
                        .append(CondensateType.getCondensateName(e.getKey()))
                        .append(EnumChatFormatting.GRAY)
                        .append(" x ")
                        .append(EnumChatFormatting.GOLD)
                        .append(NumberFormatUtil.formatFluid(e.getLongValue()))
                        .append(EnumChatFormatting.GRAY)
                        .append('\n');
                }

                return ret.toString();
            })
                .asWidget()
                .widthRel(1);

            return super.createTerminalTextWidget(syncManager, parent).child(contentsWidget);
        }

        @Override
        protected boolean isParametrized() {
            return true;
        }

        @Override
        protected Widget<?> getParameterEditor(ModularPanel panel, PanelSyncManager syncManager) {
            return SettingsPanel.builder()
                .setDividerPosition(50)
                .addHeader(IKey.str("Parameters"))
                .addLongEditor(
                    IKey.str("Field Strength"),
                    () -> fieldStrength,
                    l -> fieldStrength = (int) l,
                    (panel1, syncManager1, widget) -> { widget.setNumbersLong(() -> 1L, () -> Long.MAX_VALUE); })
                .addReadout(
                    IKey.str("Stored:"),
                    new DoubleSyncValue(MTEBECStorage.this::getAmountStored),
                    amount -> IKey.str(NumberFormatUtil.formatFluid(amount)))
                .build(panel, syncManager)
                .size(150, 75);
        }
    }
}
