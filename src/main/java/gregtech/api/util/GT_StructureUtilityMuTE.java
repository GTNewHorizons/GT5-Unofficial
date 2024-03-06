package gregtech.api.util;

import static gregtech.GT_Mod.GT_FML_LOGGER;
import static gregtech.api.multitileentity.enums.GT_MultiTileComponentCasing.*;
import static gregtech.api.multitileentity.enums.GT_MultiTileUpgradeCasing.*;
import static gregtech.loaders.preload.GT_Loader_MultiTileEntities.*;

import java.util.Arrays;

import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;

import com.gtnewhorizon.structurelib.StructureLibAPI;
import com.gtnewhorizon.structurelib.structure.IStructureElement;

import gregtech.api.enums.GT_Values;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.enums.TextureSet;
import gregtech.api.multitileentity.MultiTileEntityBlock;
import gregtech.api.multitileentity.MultiTileEntityRegistry;
import gregtech.api.multitileentity.enums.GT_MultiTileUpgradeCasing;
import gregtech.api.multitileentity.interfaces.IMultiBlockController;
import gregtech.api.multitileentity.multiblock.base.MultiBlockPart;

public class GT_StructureUtilityMuTE {

    public static final MuTEStructureCasing MOTOR_CASINGS = FunctionalCasings.Motor.getCasing();
    public static final MuTEStructureCasing PUMP_CASINGS = FunctionalCasings.Pump.getCasing();
    public static final MuTEStructureCasing CONVEYOR_CASINGS = FunctionalCasings.Conveyor.getCasing();
    public static final MuTEStructureCasing PISTON_CASINGS = FunctionalCasings.Piston.getCasing();
    public static final MuTEStructureCasing ROBOT_ARM_CASINGS = FunctionalCasings.RobotArm.getCasing();
    public static final MuTEStructureCasing EMITTER_CASINGS = FunctionalCasings.Emitter.getCasing();
    public static final MuTEStructureCasing SENSOR_CASINGS = FunctionalCasings.Sensor.getCasing();
    public static final MuTEStructureCasing FIELD_GENERATOR_CASINGS = FunctionalCasings.FieldGenerator.getCasing();
    public static final MuTEStructureCasing INVENTORY_CASINGS = UpgradeCasings.Inventory.getCasing();
    public static final MuTEStructureCasing TANK_CASINGS = UpgradeCasings.Tank.getCasing();
    public static final MuTEStructureCasing AMPERAGE_CASINGS = UpgradeCasings.Amperage.getCasing();
    public static final MuTEStructureCasing LASER_CASINGS = UpgradeCasings.Laser.getCasing();
    public static final MuTEStructureCasing WIRELESS_CASINGS = UpgradeCasings.Wireless.getCasing();
    public static final MuTEStructureCasing CLEANROOM_CASINGS = UpgradeCasings.Cleanroom.getCasing();
    public static final MuTEStructureCasing HEATER_CASINGS = UpgradeCasings.Heater.getCasing();
    public static final MuTEStructureCasing INSULATOR_CASINGS = UpgradeCasings.Insulator.getCasing();

    public enum FunctionalCasings {

        Motor(COMPONENT_CASING_REGISTRY_NAME, LV_Motor.getId(), MV_Motor.getId(), HV_Motor.getId(), EV_Motor.getId(),
            IV_Motor.getId(), LuV_Motor.getId(), ZPM_Motor.getId(), UV_Motor.getId(), UHV_Motor.getId(),
            UEV_Motor.getId(), UIV_Motor.getId(), UMV_Motor.getId(), UXV_Motor.getId(), MAX_Motor.getId()),

        Pump(COMPONENT_CASING_REGISTRY_NAME, LV_Pump.getId(), MV_Pump.getId(), HV_Pump.getId(), EV_Pump.getId(),
            IV_Pump.getId(), LuV_Pump.getId(), ZPM_Pump.getId(), UV_Pump.getId(), UHV_Pump.getId(), UEV_Pump.getId(),
            UIV_Pump.getId(), UMV_Pump.getId(), UXV_Pump.getId(), MAX_Pump.getId()),

        Conveyor(COMPONENT_CASING_REGISTRY_NAME, LV_Conveyor.getId(), MV_Conveyor.getId(), HV_Conveyor.getId(),
            EV_Conveyor.getId(), IV_Conveyor.getId(), LuV_Conveyor.getId(), ZPM_Conveyor.getId(), UV_Conveyor.getId(),
            UHV_Conveyor.getId(), UEV_Conveyor.getId(), UIV_Conveyor.getId(), UMV_Conveyor.getId(),
            UXV_Conveyor.getId(), MAX_Conveyor.getId()),

        Piston(COMPONENT_CASING_REGISTRY_NAME, LV_Piston.getId(), MV_Piston.getId(), HV_Piston.getId(),
            EV_Piston.getId(), IV_Piston.getId(), LuV_Piston.getId(), ZPM_Piston.getId(), UV_Piston.getId(),
            UHV_Piston.getId(), UEV_Piston.getId(), UIV_Piston.getId(), UMV_Piston.getId(), UXV_Piston.getId(),
            MAX_Piston.getId()),

        RobotArm(COMPONENT_CASING_REGISTRY_NAME, LV_RobotArm.getId(), MV_RobotArm.getId(), HV_RobotArm.getId(),
            EV_RobotArm.getId(), IV_RobotArm.getId(), LuV_RobotArm.getId(), ZPM_RobotArm.getId(), UV_RobotArm.getId(),
            UHV_RobotArm.getId(), UEV_RobotArm.getId(), UIV_RobotArm.getId(), UMV_RobotArm.getId(),
            UXV_RobotArm.getId(), MAX_RobotArm.getId()),

        Emitter(COMPONENT_CASING_REGISTRY_NAME, LV_Emitter.getId(), MV_Emitter.getId(), HV_Emitter.getId(),
            EV_Emitter.getId(), IV_Emitter.getId(), LuV_Emitter.getId(), ZPM_Emitter.getId(), UV_Emitter.getId(),
            UHV_Emitter.getId(), UEV_Emitter.getId(), UIV_Emitter.getId(), UMV_Emitter.getId(), UXV_Emitter.getId(),
            MAX_Emitter.getId()),

        Sensor(COMPONENT_CASING_REGISTRY_NAME, LV_Sensor.getId(), MV_Sensor.getId(), HV_Sensor.getId(),
            EV_Sensor.getId(), IV_Sensor.getId(), LuV_Sensor.getId(), ZPM_Sensor.getId(), UV_Sensor.getId(),
            UHV_Sensor.getId(), UEV_Sensor.getId(), UIV_Sensor.getId(), UMV_Sensor.getId(), UXV_Sensor.getId(),
            MAX_Sensor.getId()),

        FieldGenerator(COMPONENT_CASING_REGISTRY_NAME, LV_FieldGenerator.getId(), MV_FieldGenerator.getId(),
            HV_FieldGenerator.getId(), EV_FieldGenerator.getId(), IV_FieldGenerator.getId(), LuV_FieldGenerator.getId(),
            ZPM_FieldGenerator.getId(), UV_FieldGenerator.getId(), UHV_FieldGenerator.getId(),
            UEV_FieldGenerator.getId(), UIV_FieldGenerator.getId(), UMV_FieldGenerator.getId(),
            UXV_FieldGenerator.getId(), MAX_FieldGenerator.getId());

        private final MuTEStructureCasing casing;

        FunctionalCasings(String registryName, Integer... validIds) {
            casing = createMuTEStructureCasing(registryName, validIds);
        }

        public MuTEStructureCasing getCasing() {
            return casing;
        }
    }

    public enum UpgradeCasings {

        Inventory(UPGRADE_CASING_REGISTRY_NAME, ULV_Inventory.getId(), LV_Inventory.getId(), MV_Inventory.getId(),
            HV_Inventory.getId(), EV_Inventory.getId(), IV_Inventory.getId(), LuV_Inventory.getId(),
            ZPM_Inventory.getId(), UV_Inventory.getId(), UHV_Inventory.getId(), UEV_Inventory.getId(),
            UIV_Inventory.getId(), UMV_Inventory.getId(), UXV_Inventory.getId(), MAX_Inventory.getId()),

        Tank(UPGRADE_CASING_REGISTRY_NAME, ULV_Tank.getId(), LV_Tank.getId(), MV_Tank.getId(), HV_Tank.getId(),
            EV_Tank.getId(), IV_Tank.getId(), LuV_Tank.getId(), ZPM_Tank.getId(), UV_Tank.getId(), UHV_Tank.getId(),
            UEV_Tank.getId(), UIV_Tank.getId(), UMV_Tank.getId(), UXV_Tank.getId(), MAX_Tank.getId()),

        Amperage(UPGRADE_CASING_REGISTRY_NAME, Amp_4.getId(), Amp_16.getId(), Amp_64.getId(), Amp_256.getId(),
            Amp_1_024.getId(), Amp_4_096.getId(), Amp_16_384.getId(), Amp_65_536.getId(), Amp_262_144.getId(),
            Amp_1_048_576.getId()),

        Laser(UPGRADE_CASING_REGISTRY_NAME, GT_MultiTileUpgradeCasing.Laser.getId()),

        Wireless(UPGRADE_CASING_REGISTRY_NAME, GT_MultiTileUpgradeCasing.Wireless.getId()),

        Cleanroom(UPGRADE_CASING_REGISTRY_NAME, GT_MultiTileUpgradeCasing.Cleanroom.getId()),

        Heater(UPGRADE_CASING_REGISTRY_NAME, Heater_Prototype.getId(), Heater_IndustrialGrade.getId(),
            Heater_NextGen.getId(), Heater_Omnipotent.getId(), Heater_OmegaType.getId()),

        Insulator(UPGRADE_CASING_REGISTRY_NAME, Insulator_Prototype.getId(), Insulator_IndustrialGrade.getId(),
            Insulator_NextGen.getId(), Insulator_Omnipotent.getId(), Insulator_OmegaType.getId());

        private final MuTEStructureCasing casing;

        UpgradeCasings(String registryName, Integer... validIds) {
            casing = createMuTEStructureCasing(registryName, validIds);
        }

        public MuTEStructureCasing getCasing() {
            return casing;
        }
    }

    /**
     * Specify all casing sets that are valid for a multiblock structure position. The first casing will be used as
     * default when doing auto place
     *
     * @param modes        Allowed modes on the casings
     * @param validCasings Allowed casing sets
     * @return Structure Element
     * @param <T> Multiblock class
     */
    public static <T extends IMultiBlockController> IStructureElement<T> ofMuTECasings(int modes,
        MuTEStructureCasing... validCasings) {
        if (validCasings == null || validCasings.length == 0) {
            throw new IllegalArgumentException();
        }
        return new IStructureElement<>() {

            final MuTEStructureCasing[] allowedCasings = validCasings;
            private final static short[] DEFAULT = new short[] { 255, 255, 255, 0 };
            private static IIcon[] mIcons = null;

            @Override
            public boolean check(T t, World world, int x, int y, int z) {
                final TileEntity tileEntity = world.getTileEntity(x, y, z);
                if (!(tileEntity instanceof MultiBlockPart part)) return false;

                for (MuTEStructureCasing casing : allowedCasings) {
                    if (casing.isCasingValid(part.getRegistryId(), part.getMetaId())) {
                        final IMultiBlockController tTarget = part.getTarget(false);
                        if (tTarget != null && tTarget != t) return false;

                        part.setTarget(t, modes);

                        t.registerSpecialCasings(part);
                        return true;
                    }
                }

                return false;
            }

            @Override
            public boolean spawnHint(T t, World world, int x, int y, int z, ItemStack trigger) {
                // Moved here from Controller. TODO: Proper implementation
                if (mIcons == null) {
                    mIcons = new IIcon[6];
                    Arrays.fill(mIcons, TextureSet.SET_NONE.mTextures[OrePrefixes.block.mTextureIndex].getIcon());
                }
                final short[] RGBA = DEFAULT;
                StructureLibAPI.hintParticleTinted(world, x, y, z, mIcons, RGBA);
                return true;
            }

            @Override
            public boolean placeBlock(T t, World world, int x, int y, int z, ItemStack trigger) {
                final MultiTileEntityRegistry tRegistry = MultiTileEntityRegistry
                    .getRegistry(validCasings[0].getRegistryId());
                if (tRegistry == null) {
                    GT_FML_LOGGER.error("NULL REGISTRY");
                    return false;
                }
                MultiTileEntityBlock block = tRegistry.getBlock();
                if (!world.setBlock(x, y, z, block, allowedCasings[0].getDefaultMeta(), 2)) {
                    return false;
                }

                final TileEntity te = world.getTileEntity(x, y, z);
                if (!(te instanceof MultiBlockPart part)) return false;
                part.setTarget(t, modes);
                return true;
            }
        };
    }

    public static MuTEStructureCasing createMuTEStructureCasing(String registryName, Integer... validIds) {
        return new MuTEStructureCasing(registryName, validIds);
    }

    /**
     * Object used to store a set of casings (e.g. all motor casings)
     */
    public static class MuTEStructureCasing {

        private String registryName;
        private int registryId = GT_Values.W;
        private final int defaultMeta;
        private final Integer[] validIds;

        public MuTEStructureCasing(String registryName, Integer... validIds) {
            MultiTileEntityRegistry registry = MultiTileEntityRegistry.getRegistry(registryName);
            if (validIds == null || validIds.length == 0 || registry == null) {
                throw new IllegalArgumentException();
            }
            this.registryName = registryName;
            this.validIds = validIds;
            this.defaultMeta = validIds[0];
        }

        public boolean isCasingValid(int registryId, int id) {
            if (getRegistryId() != registryId) {
                return false;
            }
            for (Integer validId : validIds) {
                if (validId == id) {
                    return true;
                }
            }
            return false;
        }

        public int getDefaultMeta() {
            return defaultMeta;
        }

        public int getRegistryId() {
            // TODO: MuTE registry seems to somehow shift, probably due to NBT shenanigans. Lazy init circumvents this
            // but it should be properly fixed in the future
            if (registryId == GT_Values.W) {
                MultiTileEntityRegistry registry = MultiTileEntityRegistry.getRegistry(registryName);
                registryId = Block.getIdFromBlock(registry.block);
            }
            return registryId;
        }
    }
}
