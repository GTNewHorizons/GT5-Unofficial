package gregtech.loaders.postload;

import static org.junit.jupiter.api.Assertions.assertEquals;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;

import org.junit.jupiter.api.Test;

class FluidNameMigrationTest {

    @Test
    void renamesFluidReferencesNestedInListsAndCompounds() {
        NBTTagCompound tank = new NBTTagCompound();
        tank.setString("FluidName", "iodine");
        tank.setInteger("Amount", 1000);
        NBTTagList tanks = new NBTTagList();
        tanks.appendTag(tank);
        NBTTagCompound machine = new NBTTagCompound();
        machine.setTag("mFluids", tanks);
        machine.setString("lockedFluidName", "iodine");

        FluidNameMigration.rename(machine);

        assertEquals(
            "molten.iodine",
            machine.getTagList("mFluids", 10)
                .getCompoundTagAt(0)
                .getString("FluidName"));
        assertEquals("molten.iodine", machine.getString("lockedFluidName"));
    }

    @Test
    void leavesStringsUnderKeysThatNameNoFluid() {
        NBTTagCompound tag = new NBTTagCompound();
        tag.setString("customName", "iodine");

        FluidNameMigration.rename(tag);

        assertEquals("iodine", tag.getString("customName"));
    }
}
