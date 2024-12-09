package tectech.thing.metaTileEntity.multi.godforge.color;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

public class ForgeOfGodsStarColorTest {

    @Test
    public void testSerializeToString() {
        ForgeOfGodsStarColor starColor = new ForgeOfGodsStarColor("Test Color").setCycleSpeed(10)
            .addColor(1, 2, 3, 4)
            .addColor(71, 82, 155, 9)
            .addColor(3, 8, 94, 20);

        String serialized = starColor.serializeToString();
        System.out.printf("Tested Star Color: %s\n", serialized);
        ForgeOfGodsStarColor deserialized = ForgeOfGodsStarColor.deserialize(serialized);
        assertEquals(starColor, deserialized);
    }
}
