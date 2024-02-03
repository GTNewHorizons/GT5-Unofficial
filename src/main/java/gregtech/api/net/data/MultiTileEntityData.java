package gregtech.api.net.data;

import javax.annotation.Nonnull;

import com.google.common.io.ByteArrayDataInput;

import io.netty.buffer.ByteBuf;

public class MultiTileEntityData extends PacketData<MultiTileEntityProcess> {

    public static final int MULTI_TILE_ENTITY_DATA_ID = 1;

    private int registryId;
    private int metaId;

    public MultiTileEntityData() {}

    public MultiTileEntityData(int registryId, int metaId) {
        this.registryId = registryId;
        this.metaId = metaId;
    }

    @Override
    public int getId() {
        return MULTI_TILE_ENTITY_DATA_ID;
    }

    @Override
    public void encode(@Nonnull ByteBuf out) {}

    @Override
    public void decode(@Nonnull ByteArrayDataInput in) {}

    @Override
    public void process(MultiTileEntityProcess processData) {
        processData.giveMultiTileEntityData(registryId, metaId);
    }

}
