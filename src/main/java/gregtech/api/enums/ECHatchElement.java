package gregtech.api.enums;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import net.minecraft.util.StatCollector;

import org.jetbrains.annotations.NotNull;

import com.google.common.collect.ImmutableList;

import gregtech.api.ec.ComponentKey;
import gregtech.api.ec.IECMultiblock;
import gregtech.api.ec.component.multiblock.CustomHatchKey;
import gregtech.api.ec.component.multiblock.CustomHatchKeys;
import gregtech.api.ec.component.multiblock.IDualInputLogicComponent;
import gregtech.api.ec.component.multiblock.IHatchContainerComponent;
import gregtech.api.ec.component.multiblock.IMaintenanceLogicComponent;
import gregtech.api.ec.component.multiblock.IMultiblockEnergyInputLogicComponent;
import gregtech.api.ec.component.multiblock.IMultiblockEnergyOutputLogicComponent;
import gregtech.api.ec.component.multiblock.IMultiblockFluidInputLogicComponent;
import gregtech.api.ec.component.multiblock.IMultiblockFluidOutputLogicComponent;
import gregtech.api.ec.component.multiblock.IMultiblockItemInputLogicComponent;
import gregtech.api.ec.component.multiblock.IMultiblockItemOutputLogicComponent;
import gregtech.api.ec.component.multiblock.IMultiblockPollutionLogicComponent;
import gregtech.api.interfaces.IHatchElement;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.implementations.MTEHatch;
import gregtech.api.metatileentity.implementations.MTEHatchDynamo;
import gregtech.api.metatileentity.implementations.MTEHatchEnergy;
import gregtech.api.metatileentity.implementations.MTEHatchInput;
import gregtech.api.metatileentity.implementations.MTEHatchInputBus;
import gregtech.api.metatileentity.implementations.MTEHatchMaintenance;
import gregtech.api.metatileentity.implementations.MTEHatchMuffler;
import gregtech.api.metatileentity.implementations.MTEHatchOutput;
import gregtech.api.metatileentity.implementations.MTEHatchOutputBus;
import gregtech.api.util.ExoticEnergyInputHelper;
import gregtech.api.util.IGTHatchAdder;
import gregtech.common.tileentities.machines.MTEHatchCraftingInputME;
import gregtech.common.tileentities.machines.MTEHatchCraftingInputSlave;
import gregtech.common.tileentities.machines.MTEHatchPatternProvider;
import gregtech.common.tileentities.machines.multi.purification.MTEHatchLensHousing;
import gtPlusPlus.xmod.gregtech.api.metatileentity.implementations.MTEHatchElementalDataOrbHolder;
import gtPlusPlus.xmod.gregtech.api.metatileentity.implementations.MTEHatchExtrusion;
import gtPlusPlus.xmod.gregtech.api.metatileentity.implementations.MTEHatchSolidifier;
import gtPlusPlus.xmod.gregtech.api.metatileentity.implementations.MTEHatchSteamBusInput;
import gtPlusPlus.xmod.gregtech.api.metatileentity.implementations.MTEHatchSteamBusOutput;
import gtPlusPlus.xmod.gregtech.api.metatileentity.implementations.base.MTEHatchCustomFluidBase;
import kubatech.tileentity.gregtech.hatch.MTEHatchElectrode;
import tectech.thing.metaTileEntity.hatch.MTEHatchDynamoTunnel;

public enum ECHatchElement implements IHatchElement<IECMultiblock> {

    Muffler("GT5U.MBTT.MufflerHatch",
        addr(IMultiblockPollutionLogicComponent.KEY, IMultiblockPollutionLogicComponent::tryAttachMuffler),
        MTEHatchMuffler.class) {

        @Override
        public long count(IECMultiblock multiblock) {
            return multiblock.getHatchCountOf(
                IMultiblockPollutionLogicComponent.KEY,
                IMultiblockPollutionLogicComponent::getPollutionHatches);
        }
    },
    Maintenance("GT5U.MBTT.MaintenanceHatch",
        addr(IMaintenanceLogicComponent.KEY, IMaintenanceLogicComponent::tryAttachMaintenance),
        MTEHatchMaintenance.class) {

        @Override
        public long count(IECMultiblock multiblock) {
            return multiblock
                .getHatchCountOf(IMaintenanceLogicComponent.KEY, IMaintenanceLogicComponent::getMaintenanceHatches);
        }
    },
    InputHatch("GT5U.MBTT.InputHatch",
        (multiblock, igte,
            casingIndex) -> multiblock.tryAttachHatchToComponent(
                IDualInputLogicComponent.KEY,
                igte,
                casingIndex,
                IDualInputLogicComponent::tryAttachDualFluidInput)
                || multiblock.tryAttachHatchToComponent(
                    IMultiblockFluidInputLogicComponent.KEY,
                    igte,
                    casingIndex,
                    IMultiblockFluidInputLogicComponent::tryAttachFluidInput),
        MTEHatchInput.class) {

        @Override
        public long count(IECMultiblock multiblock) {
            return multiblock.getHatchCountOf(
                IMultiblockFluidInputLogicComponent.KEY,
                IMultiblockFluidInputLogicComponent::getFluidInputHatches)
                + multiblock.getHatchCountOf(
                    IDualInputLogicComponent.KEY,
                    (IDualInputLogicComponent comp) -> (int) comp.getDualInputHatches()
                        .stream()
                        .filter(hatch -> hatch.supportsFluids() || hatch instanceof MTEHatchCraftingInputSlave)
                        .count());
        }

        @Override
        public List<? extends Class<? extends IMetaTileEntity>> mteClasses() {
            return ImmutableList
                .of(MTEHatchInput.class, MTEHatchCraftingInputME.class, MTEHatchCraftingInputSlave.class);
        }

        @Override
        public List<Class<? extends IMetaTileEntity>> mteBlacklist() {
            return ImmutableList.of(MTEHatchSolidifier.class);
        }
    },
    InputBus("GT5U.MBTT.InputBus",
        (multiblock, igte,
            casingIndex) -> multiblock.tryAttachHatchToComponent(
                IDualInputLogicComponent.KEY,
                igte,
                casingIndex,
                IDualInputLogicComponent::tryAttachDualItemInput)
                || multiblock.tryAttachHatchToComponent(
                    IMultiblockItemInputLogicComponent.KEY,
                    igte,
                    casingIndex,
                    IMultiblockItemInputLogicComponent::tryAttachItemInput),
        MTEHatchInputBus.class) {

        @Override
        public long count(IECMultiblock multiblock) {
            return multiblock.getHatchCountOf(
                IMultiblockItemInputLogicComponent.KEY,
                IMultiblockItemInputLogicComponent::getItemInputHatches)
                + multiblock
                    .getHatchCountOf(IDualInputLogicComponent.KEY, IDualInputLogicComponent::getDualInputHatches);
        }

        @Override
        public List<Class<? extends IMetaTileEntity>> mteBlacklist() {
            return ImmutableList.of(
                MTEHatchLensHousing.class,
                MTEHatchSteamBusInput.class,
                MTEHatchExtrusion.class,
                MTEHatchPatternProvider.class,
                MTEHatchElectrode.class);
        }
    },
    OutputHatch("GT5U.MBTT.OutputHatch",
        addr(IMultiblockFluidOutputLogicComponent.KEY, IMultiblockFluidOutputLogicComponent::tryAttachFluidOutput),
        MTEHatchOutput.class) {

        @Override
        public long count(IECMultiblock multiblock) {
            return multiblock.getHatchCountOf(
                IMultiblockFluidOutputLogicComponent.KEY,
                IMultiblockFluidOutputLogicComponent::getFluidOutputHatches);
        }
    },
    OutputBus("GT5U.MBTT.OutputBus",
        addr(IMultiblockItemOutputLogicComponent.KEY, IMultiblockItemOutputLogicComponent::tryAttachItemOutput),
        MTEHatchOutputBus.class) {

        @Override
        public long count(IECMultiblock multiblock) {
            return multiblock.getHatchCountOf(
                IMultiblockItemOutputLogicComponent.KEY,
                IMultiblockItemOutputLogicComponent::getItemOutputHatches);
        }

        @Override
        public List<Class<? extends IMetaTileEntity>> mteBlacklist() {
            return ImmutableList.of(MTEHatchSteamBusOutput.class);
        }
    },
    Energy("GT5U.MBTT.EnergyHatch",
        addr(IMultiblockEnergyInputLogicComponent.KEY, IMultiblockEnergyInputLogicComponent::tryAttachEnergyHatch),
        MTEHatchEnergy.class) {

        @Override
        public long count(IECMultiblock multiblock) {
            return multiblock.getHatchCountOf(
                IMultiblockEnergyInputLogicComponent.KEY,
                IMultiblockEnergyInputLogicComponent::getNormalEnergyHatches);
        }
    },
    Dynamo("GT5U.MBTT.DynamoHatch",
        addr(IMultiblockEnergyOutputLogicComponent.KEY, IMultiblockEnergyOutputLogicComponent::tryAttachNormalDynamo),
        MTEHatchDynamo.class) {

        @Override
        public long count(IECMultiblock multiblock) {
            return multiblock.getHatchCountOf(
                IMultiblockEnergyOutputLogicComponent.KEY,
                IMultiblockEnergyOutputLogicComponent::getNormalDynamoHatches);
        }
    },
    ExoticEnergy("GT5U.MBTT.ExoticEnergyHatch",
        addr(
            IMultiblockEnergyInputLogicComponent.KEY,
            IMultiblockEnergyInputLogicComponent::tryAttachExoticEnergyInput)) {

        @Override
        public List<? extends Class<? extends IMetaTileEntity>> mteClasses() {
            return ExoticEnergyInputHelper.getAllClasses();
        }

        @Override
        public long count(IECMultiblock multiblock) {
            return multiblock.getHatchCountOf(
                IMultiblockEnergyInputLogicComponent.KEY,
                IMultiblockEnergyInputLogicComponent::getExoticEnergyHatches);
        }
    },
    MultiAmpEnergy("GT5U.MBTT.MultiampEnergyHatch",
        addr(
            IMultiblockEnergyInputLogicComponent.KEY,
            IMultiblockEnergyInputLogicComponent::tryAttachMultiAmpEnergyInput)) {

        @Override
        public long count(IECMultiblock multiblock) {
            return multiblock.getHatchCountOf(
                IMultiblockEnergyInputLogicComponent.KEY,
                IMultiblockEnergyInputLogicComponent::getExoticEnergyHatches);
        }
    },
    ExoticDynamo("GT5U.MBTT.ExoticEnergyDynamo",
        addr(IMultiblockEnergyOutputLogicComponent.KEY, IMultiblockEnergyOutputLogicComponent::tryAttachExoticDynamo)) {

        @Override
        public long count(IECMultiblock multiblock) {
            return multiblock.getHatchCountOf(
                IMultiblockEnergyOutputLogicComponent.KEY,
                IMultiblockEnergyOutputLogicComponent::getExoticDynamoHatches);
        }
    },
    CryotheumHatch("GT5U.MBTT.CryotheumHatch", addr(CustomHatchKeys.CRYOTHEUM), MTEHatchCustomFluidBase.class) {

        @Override
        public long count(IECMultiblock multiblock) {
            return multiblock.getCustomHatchCountOf(CustomHatchKeys.CRYOTHEUM);
        }

        @Override
        public boolean matchesHatch(IMetaTileEntity mte) {
            return CustomHatchKeys.CRYOTHEUM.matches(mte);
        }
    },
    PyrotheumHatch("GT5U.MBTT.PyrotheumHatch", addr(CustomHatchKeys.PYROTHEUM), MTEHatchCustomFluidBase.class) {

        @Override
        public long count(IECMultiblock multiblock) {
            return multiblock.getCustomHatchCountOf(CustomHatchKeys.PYROTHEUM);
        }

        @Override
        public boolean matchesHatch(IMetaTileEntity mte) {
            return CustomHatchKeys.PYROTHEUM.matches(mte);
        }
    },
    LaserSource("GT5U.MBTT.LaserSourceHatch",
        addr(IMultiblockEnergyOutputLogicComponent.KEY, IMultiblockEnergyOutputLogicComponent::tryAttachLaserSource),
        MTEHatchDynamoTunnel.class) {

        @Override
        public long count(IECMultiblock multiblock) {
            return multiblock.getHatchCountOf(
                IMultiblockEnergyOutputLogicComponent.KEY,
                (IMultiblockEnergyOutputLogicComponent comp) -> (int) comp.getExoticDynamoHatches()
                    .stream()
                    .filter(MTEHatchDynamoTunnel.class::isInstance)
                    .count());
        }
    },
    SolidifierHatch("GT5U.MBTT.SolidiferHatch",
        addr(IMultiblockFluidInputLogicComponent.KEY, IMultiblockFluidInputLogicComponent::tryAttachFluidInput),
        MTEHatchSolidifier.class) {

        @Override
        public long count(IECMultiblock multiblock) {
            return multiblock.getHatchCountOf(
                IMultiblockFluidInputLogicComponent.KEY,
                (IMultiblockFluidInputLogicComponent comp) -> (int) comp.getFluidInputHatches()
                    .stream()
                    .filter(it -> it instanceof MTEHatchSolidifier)
                    .count());
        }
    },
    ElementalDataOrbHatch("GT5U.MBTT.ElementalDataOrbHatch", addr(CustomHatchKeys.ELEMENTAL_DATA_ORB_HOLDERS),
        MTEHatchElementalDataOrbHolder.class) {

        @Override
        public long count(IECMultiblock multiblock) {
            return multiblock.getCustomHatchCountOf(CustomHatchKeys.ELEMENTAL_DATA_ORB_HOLDERS);
        }

        @Override
        public boolean matchesHatch(IMetaTileEntity mte) {
            return CustomHatchKeys.ELEMENTAL_DATA_ORB_HOLDERS.matches(mte);
        }
    };

    private final String name;
    private final List<Class<? extends IMetaTileEntity>> mteClasses;
    private final IGTHatchAdder<IECMultiblock> adder;
    private static final ECHatchElement[] elements = ECHatchElement.values();

    @SafeVarargs
    ECHatchElement(String name, IGTHatchAdder<IECMultiblock> adder, Class<? extends IMetaTileEntity>... mteClasses) {
        this.name = name;
        this.mteClasses = Collections.unmodifiableList(Arrays.asList(mteClasses));
        this.adder = adder;
    }

    @Override
    public List<? extends Class<? extends IMetaTileEntity>> mteClasses() {
        return mteClasses;
    }

    @Override
    public String getDisplayName() {
        return StatCollector.translateToLocal(name);
    }

    @Override
    public String getDescriptionLangKey() {
        return name;
    }

    @Override
    public IGTHatchAdder<? super IECMultiblock> adder() {
        return adder;
    }

    public boolean tryAttach(@NotNull IECMultiblock multiblock, @NotNull IGregTechTileEntity igte, short casingIndex) {
        return adder.apply(multiblock, igte, casingIndex);
    }

    public static ECHatchElement fromOrdinal(int ord) {
        return elements[ord];
    }

    private static <T extends IHatchContainerComponent> IGTHatchAdder<IECMultiblock> addr(@NotNull ComponentKey<T> key,
        @NotNull IECMultiblock.HatchAttacher<T> attacher) {
        return (multiblock, igte, casingIndex) -> multiblock
            .tryAttachHatchToComponent(key, igte, casingIndex, attacher);
    }

    private static <T extends MTEHatch> IGTHatchAdder<IECMultiblock> addr(@NotNull CustomHatchKey<T> key) {
        return (multiblock, igte, casingIndex) -> multiblock.tryAttachCustomHatch(key, igte, casingIndex);
    }
}
