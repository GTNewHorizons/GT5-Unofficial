package gregtech.common.render.shader;

public final class Uniform {

    final ShaderRecipe owner;
    final String name;
    final int slot;

    Uniform(ShaderRecipe owner, String name, int slot) {
        this.owner = owner;
        this.name = name;
        this.slot = slot;
    }

    @Override
    public String toString() {
        return owner.id() + ":" + name;
    }
}
