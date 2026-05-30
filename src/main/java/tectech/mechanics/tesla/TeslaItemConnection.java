package tectech.mechanics.tesla;

import static tectech.mechanics.tesla.ITeslaConnectable.TeslaUtil.teslaSimpleNodeSetRemoveScheduled;

import java.util.Objects;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;

import com.gtnewhorizon.gtnhlib.hash.Fnv1a32;
import com.gtnewhorizon.structurelib.util.Vec3Impl;

import ic2.api.item.ElectricItem;

public class TeslaItemConnection implements ITeslaConnectableSimple {

    private final EntityLivingBase entity;
    private final ItemStack stack;
    private final byte teslaReceptionCapability;
    private boolean once = true;

    public TeslaItemConnection(EntityLivingBase entity, ItemStack stack, byte teslaReceptionCapability) {
        this.entity = entity;
        this.stack = stack;
        this.teslaReceptionCapability = teslaReceptionCapability;
    }

    @Override
    public byte getTeslaReceptionCapability() {
        return teslaReceptionCapability;
    }

    @Override
    public float getTeslaReceptionCoefficient() {
        return 1;
    }

    @Override
    public boolean isTeslaReadyToReceive() {
        return once;
    }

    @Override
    public long getTeslaStoredEnergy() {
        return (long) ElectricItem.manager.getCharge(stack);
    }

    @Override
    public Vec3Impl getTeslaPosition() {
        return new Vec3Impl((int) entity.posX, (int) entity.posY, (int) entity.posZ);
    }

    @Override
    public Integer getTeslaDimension() {
        return entity.dimension;
    }

    @Override
    public boolean teslaInjectEnergy(long teslaVoltageInjected) {
        // Same as in the microwave transmitters, this does not account for amp limits
        boolean output = false;

        if (!entity.isDead) {
            if (ElectricItem.manager.charge(stack, teslaVoltageInjected, Integer.MAX_VALUE, true, true)
                == teslaVoltageInjected) {
                output = true;
                ElectricItem.manager.charge(stack, teslaVoltageInjected, Integer.MAX_VALUE, true, false);
            }
        } else {
            teslaSimpleNodeSetRemoveScheduled(this);
        }
        once = false;

        return output;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TeslaItemConnection that = (TeslaItemConnection) o;
        return Objects.equals(stack, that.stack); // hopefully i did this right??
    }

    @Override
    public int hashCode() {
        return Fnv1a32.hashStep(Fnv1a32.initialState(), Objects.hash(stack));
    }
}
