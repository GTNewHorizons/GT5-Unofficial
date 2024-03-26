package gregtech.api.net;

import static gregtech.api.enums.GT_Values.B;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import net.minecraft.world.IBlockAccess;

import com.google.common.io.ByteArrayDataInput;

import gregtech.api.net.data.CasingData;
import gregtech.api.net.data.CommonData;
import gregtech.api.net.data.CoordinateData;
import gregtech.api.net.data.MultiTileEntityProcess;
import gregtech.api.net.data.PacketData;
import io.netty.buffer.ByteBuf;

public class GT_Packet_MultiTileEntity extends GT_Packet_New {

    private final Set<PacketData<MultiTileEntityProcess>> data = new HashSet<>();
    public static final int COVERS = B[0], REDSTONE = B[1], MODES = B[2], CONTROLLER = B[3], INVENTORY_INDEX = B[4],
        INVENTORY_NAME_ID = B[5], BOOLEANS = B[6], SOUND = B[7];

    public GT_Packet_MultiTileEntity(boolean reference) {
        super(reference);
    }

    @Override
    public void encode(ByteBuf aOut) {
        Set<PacketData<MultiTileEntityProcess>> set = data.stream()
            .sorted()
            .collect(
                HashSet<PacketData<MultiTileEntityProcess>>::new,
                HashSet<PacketData<MultiTileEntityProcess>>::add,
                HashSet<PacketData<MultiTileEntityProcess>>::addAll);
        clearData();
        data.addAll(set);
        int features = 0;
        for (PacketData<MultiTileEntityProcess> data : data) {
            features |= 1 << data.getId();
        }

        aOut.writeInt(features);

        for (PacketData<MultiTileEntityProcess> data : data) {
            data.encode(aOut);
        }
        /*
         * TODO Move to new system
         * if ((features & COVERS) == COVERS) {
         * aOut.writeInt(mC0);
         * aOut.writeInt(mC1);
         * aOut.writeInt(mC2);
         * aOut.writeInt(mC3);
         * aOut.writeInt(mC4);
         * aOut.writeInt(mC5);
         * }
         * if ((features & MODES) == MODES) {
         * aOut.writeInt(mode);
         * aOut.writeInt(allowedModes);
         * }
         * if ((features & CONTROLLER) == CONTROLLER) {
         * aOut.writeInt(mTargetPos.posX);
         * aOut.writeShort(mTargetPos.posY);
         * aOut.writeInt(mTargetPos.posZ);
         * }
         * if ((features & INVENTORY_INDEX) == INVENTORY_INDEX) {
         * aOut.writeInt(mLockedInventoryIndex);
         * }
         * if ((features & INVENTORY_NAME_ID) == INVENTORY_NAME_ID) {
         * if (mInventoryName != null && mInventoryName.length() > 0) {
         * byte[] bytes = mInventoryName.getBytes();
         * aOut.writeInt(bytes.length);
         * aOut.writeBytes(bytes);
         * } else {
         * aOut.writeInt(0);
         * }
         * if (inventoryID != null && inventoryID.length() > 0) {
         * byte[] bytes = inventoryID.getBytes();
         * aOut.writeInt(bytes.length);
         * aOut.writeBytes(bytes);
         * } else {
         * aOut.writeInt(0);
         * }
         * }
         * if ((features & BOOLEANS) == BOOLEANS) {
         * aOut.writeInt(booleans);
         * }
         * if ((features & SOUND) == SOUND) {
         * aOut.writeByte(soundEvent);
         * aOut.writeInt(soundEventValue);
         * }
         */
    }

    @Override
    public GT_Packet_New decode(ByteArrayDataInput in) {
        Objects.requireNonNull(in);
        final int packetFeatures = in.readInt();

        final GT_Packet_MultiTileEntity packet = new GT_Packet_MultiTileEntity(false);

        if (containsBit(packetFeatures, CoordinateData.COORDINATE_DATA_ID)) {
            packet.addData(new CoordinateData());
        }
        if (containsBit(packetFeatures, CommonData.COMMON_DATA_ID)) {
            packet.addData(new CommonData());
        }
        if (containsBit(packetFeatures, CasingData.CASING_DATA_ID)) {
            packet.addData(new CasingData());
        }

        Set<PacketData<MultiTileEntityProcess>> set = packet.data.stream()
            .sorted()
            .collect(
                HashSet<PacketData<MultiTileEntityProcess>>::new,
                HashSet<PacketData<MultiTileEntityProcess>>::add,
                HashSet<PacketData<MultiTileEntityProcess>>::addAll);
        packet.clearData();
        packet.data.addAll(set);
        for (PacketData<MultiTileEntityProcess> data : packet.data) {
            data.decode(in);
        }
        /*
         * if ((packetFeatures & COVERS) == COVERS) {
         * packet.setCoverData(
         * in.readInt(),
         * in.readInt(),
         * in.readInt(),
         * in.readInt(),
         * in.readInt(),
         * in.readInt());
         * }
         * if ((packetFeatures & INVENTORY_INDEX) == INVENTORY_INDEX) {
         * packet.setInventoryIndex(aData.readInt());
         * }
         * if ((packetFeatures & INVENTORY_NAME_ID) == INVENTORY_NAME_ID) {
         * int nameLength = aData.readInt();
         * String inventoryName;
         * if (nameLength > 0) {
         * byte[] bytes = new byte[nameLength];
         * for (int i = 0; i < nameLength; i++) {
         * bytes[i] = aData.readByte();
         * }
         * inventoryName = new String(bytes);
         * } else {
         * inventoryName = null;
         * }
         * int idLength = aData.readInt();
         * String inventoryID;
         * if (idLength > 0) {
         * byte[] bytes = new byte[idLength];
         * for (int i = 0; i < idLength; i++) {
         * bytes[i] = aData.readByte();
         * }
         * inventoryID = new String(bytes);
         * } else {
         * inventoryID = null;
         * }
         * packet.setInventoryName(inventoryName, inventoryID);
         * }
         * if ((packetFeatures & BOOLEANS) == BOOLEANS) {
         * packet.setBooleans(aData.readInt());
         * }
         * if ((packetFeatures & SOUND) == SOUND) {
         * packet.setSoundEvent(aData.readByte(), aData.readInt());
         * }
         */
        return packet;
    }

    @Override
    public void process(IBlockAccess aWorld) {
        if (aWorld == null) return;
        MultiTileEntityProcess process = new MultiTileEntityProcess(aWorld);
        for (PacketData<MultiTileEntityProcess> data : data) {
            data.process(process);
        }
        process.process();
        /*
         * final TileEntity tTileEntity = aWorld.getTileEntity(mX, mY, mZ);
         * try {
         * final Block tBlock = aWorld.getBlock(mX, mY, mZ);
         * if (tBlock instanceof MultiTileEntityBlock mteBlock) {
         * final IMultiTileEntity mte = mteBlock.receiveMultiTileEntityData(aWorld, mX, mY, mZ, mRID, mID);
         * if (mte == null) return;
         * mte.receiveClientData(GregTechTileClientEvents.CHANGE_COMMON_DATA, mCommonData);
         * mte.receiveClientData(GregTechTileClientEvents.CHANGE_COLOR, mColor);
         * if ((features & COVERS) == COVERS) {
         * mteBlock.receiveCoverData(mte, mC0, mC1, mC2, mC3, mC4, mC5);
         * }
         * if ((features & REDSTONE) == REDSTONE) {
         * mte.receiveClientData(GregTechTileClientEvents.CHANGE_REDSTONE_OUTPUT, mRedstone);
         * }
         * if ((features & MODES) == MODES && mte instanceof IMultiTileEntity.IMTE_HasModes mteModes) {
         * mteModes.setMode(mode);
         * mteModes.setAllowedModes(allowedModes);
         * }
         * if ((features & INVENTORY_NAME_ID) == INVENTORY_NAME_ID && mte instanceof Inventory invUpg) {
         * invUpg.setInventoryName(mInventoryName);
         * invUpg.setInventoryId(inventoryID);
         * }
         * if ((features & CONTROLLER) == CONTROLLER && mte instanceof IMultiBlockPart) {
         * final IMultiBlockPart mtePart = (IMultiBlockPart) mte;
         * mtePart.setTargetPos(mTargetPos);
         * }
         * if ((features & INVENTORY_INDEX) == INVENTORY_INDEX && mte instanceof IMultiBlockPart) {
         * final IMultiBlockPart mtePart = (IMultiBlockPart) mte;
         * mtePart.setLockedInventoryIndex(mLockedInventoryIndex);
         * }
         * if ((features & BOOLEANS) == BOOLEANS && mte instanceof IMultiTileMachine) {
         * final IMultiTileMachine machine = (IMultiTileMachine) mte;
         * machine.setBooleans(booleans);
         * }
         * if ((features & SOUND) == SOUND && mte instanceof IMultiTileMachine) {
         * final IMultiTileMachine machine = (IMultiTileMachine) mte;
         * machine.setSound(soundEvent, soundEventValue);
         * }
         * }
         * } catch (Exception e) {
         * e.printStackTrace();
         * GT_Mod.GT_FML_LOGGER.error(
         * "Exception setting tile entity data for tile entity {} at ({}, {}, {})",
         * tTileEntity,
         * mX,
         * mY,
         * mZ);
         * }
         */
    }

    @Override
    public byte getPacketID() {
        return 18;
    }

    public void clearData() {
        data.clear();
    }

    public void addData(PacketData<MultiTileEntityProcess> data) {
        this.data.add(data);
    }

    private static boolean containsBit(int toCheck, int bit) {
        return (toCheck & (1 << bit)) > 0;
    }
}
