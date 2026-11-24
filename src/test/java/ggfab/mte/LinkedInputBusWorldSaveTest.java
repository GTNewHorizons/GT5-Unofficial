package ggfab.mte;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mockStatic;

import net.minecraft.nbt.NBTTagCompound;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

import com.gtnewhorizons.postea.api.CustomDataReplacementManager;

import ggfab.mte.MTELinkedInputBus.WorldSave;

class LinkedInputBusWorldSaveTest {

    private MockedStatic<CustomDataReplacementManager> postea;
    private final WorldSave save = new WorldSave("LinkedInputBusses");

    @BeforeEach
    void mockPostea() {
        postea = mockStatic(CustomDataReplacementManager.class);
    }

    @AfterEach
    void closePostea() {
        postea.close();
    }

    @Test
    void failedMigrationIsNeverSavedAndRefusesTheTable() {
        postea.when(() -> CustomDataReplacementManager.transform(anyString(), any()))
            .thenThrow(new IllegalStateException("migration failed"));

        assertThrows(IllegalStateException.class, () -> save.readFromNBT(channelTable()));
        save.markDirty();

        assertFalse(save.isDirty());
        assertThrows(IllegalStateException.class, () -> save.get("channel"));
        assertThrows(IllegalStateException.class, () -> save.remove("channel"));
    }

    private static NBTTagCompound channelTable() {
        NBTTagCompound inventory = new NBTTagCompound();
        inventory.setInteger("ref", 1);
        NBTTagCompound root = new NBTTagCompound();
        root.setTag("channel", inventory);
        return root;
    }
}
