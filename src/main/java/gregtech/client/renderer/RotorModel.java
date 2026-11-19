package gregtech.client.renderer;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelBox;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;

public class RotorModel extends ModelBase {

    private static final int BEARING_TEXTURE_X = 0;
    private static final int BEARING_TEXTURE_Y = 18;
    private static final int BEARING_TEXTURE_WIDTH = 8;
    private static final int BEARING_TEXTURE_HEIGHT = 8;
    private static final int BEARING_TEXTURE_DEPTH = 3;

    private static final int BLADE_TEXTURE_X = 0;
    private static final int BLADE_TEXTURE_Y = 0;
    private static final int BLADE_TEXTURE_WIDTH = 8;
    private static final int BLADE_TEXTURE_HEIGHT = 16;
    private static final int BLADE_TEXTURE_DEPTH = 2;

    private final ModelRenderer rotor;

    public RotorModel(int rotorDiameter) {
        this.textureWidth = 32;
        this.textureHeight = 32;

        float pixelDiameter = (float) (rotorDiameter * 16);

        rotor = new ModelRenderer(this);
        rotor.setRotationPoint(0.0f, pixelDiameter / 2, 0.0f);
        rotor.cubeList.add(
            new ModelBox(
                rotor,
                BEARING_TEXTURE_X,
                BEARING_TEXTURE_Y,
                -4.0f,
                -12.0f - (float) ((rotorDiameter / 2) * 16),
                -2.0f,
                BEARING_TEXTURE_WIDTH,
                BEARING_TEXTURE_HEIGHT,
                BEARING_TEXTURE_DEPTH,
                0f));

        for (int side = 0; side < 4; side++) {
            ModelRenderer blade = createBlade(side, pixelDiameter, rotorDiameter);
            blade.offsetY = (float) -rotorDiameter;
            rotor.addChild(blade);
        }
    }

    private ModelRenderer createBlade(int side, float pixelDiameter, int rotorDiameter) {
        ModelRenderer blade = new ModelRenderer(this);

        blade.setRotationPoint(0.0f, pixelDiameter / 2, 0.0f);
        blade.rotateAngleZ = (float) ((Math.PI / 2) * side);

        blade.cubeList.add(
            new ModelBox(
                blade,
                BLADE_TEXTURE_X,
                BLADE_TEXTURE_Y,
                -4.0f,
                4.0f,
                -1.0f,
                BLADE_TEXTURE_WIDTH,
                4, // BLADE_TEXTURE_HEIGHT / 4
                BLADE_TEXTURE_DEPTH,
                0f));

        for (int extensions = 1; extensions <= (rotorDiameter / 2); extensions++) {
            blade.cubeList.add(
                new ModelBox(
                    blade,
                    BLADE_TEXTURE_X,
                    BLADE_TEXTURE_Y,
                    -4.0f,
                    (extensions * 16.0f) - 8.0f,
                    -1.0f,
                    BLADE_TEXTURE_WIDTH,
                    BLADE_TEXTURE_HEIGHT,
                    BLADE_TEXTURE_DEPTH,
                    0f));
        }

        return blade;
    }

    @Override
    public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5) {
        rotor.render(f5);
    }
}
