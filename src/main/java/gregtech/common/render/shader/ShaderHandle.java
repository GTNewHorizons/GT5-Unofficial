package gregtech.common.render.shader;

import java.util.Arrays;

import org.joml.Matrix4fc;

import com.gtnewhorizon.gtnhlib.client.renderer.shader.ShaderProgram;
import com.gtnewhorizon.gtnhlib.client.renderer.vertex.VertexFormat;

import gregtech.common.render.shader.ShaderRecipe.ModelTransport;

public final class ShaderHandle {

    private final ShaderRecipe recipe;
    private final ShaderProfile profile;

    private final int[] slots;

    private final VertexFormat vertexFormat;

    private final ShaderConstant[] constants;
    private final int[] constantLocations;

    private final int mvpLocation;
    private final int modelLocation;

    private ShaderProgram program;
    private ShaderMesh mesh;

    ShaderHandle(ShaderRecipe recipe, ShaderProfile profile, ShaderProgram program, int[] slots, ShaderMesh mesh,
        VertexFormat vertexFormat) {
        this.recipe = recipe;
        this.profile = profile;
        this.program = program;
        this.slots = slots;
        this.mesh = mesh;
        this.vertexFormat = vertexFormat;

        this.constants = recipe.constants()
            .toArray(new ShaderConstant[0]);
        this.constantLocations = new int[constants.length];
        for (int i = 0; i < constants.length; i++) {
            constantLocations[i] = slots[recipe.slotOf(constants[i].name)];
        }

        this.mvpLocation = recipe.transport() == ModelTransport.NONE ? -1 : loc(recipe.mvpToken());
        this.modelLocation = recipe.modelUniform() == null ? -1 : loc(recipe.modelToken());
    }

    public VertexFormat vertexFormat() {
        return vertexFormat;
    }

    VertexAttribute[] layout() {
        return recipe.layout();
    }

    String id() {
        return recipe.id();
    }

    static ShaderHandle invalid(ShaderRecipe recipe) {
        final int[] slots = new int[recipe.declaredCount()];
        Arrays.fill(slots, -1);
        return new ShaderHandle(recipe, ShaderProfile.preferred(), null, slots, null, null);
    }

    public boolean isValid() {
        return program != null;
    }

    public ShaderProfile profile() {
        return profile;
    }

    public int program() {
        return program == null ? 0 : program.getProgram();
    }

    public void use() {
        if (program == null) return;

        program.use();
        for (int i = 0; i < constants.length; i++) {
            constants[i].apply(constantLocations[i]);
        }
    }

    public int loc(Uniform uniform) {
        if (uniform.owner != recipe) {
            throw new IllegalArgumentException(recipe.id() + ": " + uniform + " belongs to another shader");
        }
        return slots[uniform.slot];
    }

    public boolean has(Uniform uniform) {
        return loc(uniform) >= 0;
    }

    public void uploadModel(Matrix4fc model) {
        if (recipe.transport() != ModelTransport.UNIFORM) {
            throw new IllegalStateException(recipe.id() + ": model transform is not carried by a uniform");
        }
        if (program == null) return;

        if (profile == ShaderProfile.ANGELICA_CORE) {
            FrameMatrices.uploadMVP(mvpLocation, model);
        } else {
            FrameMatrices.upload(modelLocation, model);
        }
    }

    public void draw() {
        if (mesh != null) mesh.draw();
    }

    public void release() {
        if (program != null) {
            program.close();
            program = null;
        }
        if (mesh != null) {
            mesh.release();
            mesh = null;
        }
    }
}
