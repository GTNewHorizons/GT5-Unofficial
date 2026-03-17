package gregtech.api.factory.routing;

import com.github.bsideup.jabel.Desugar;

@Desugar
public record NetworkStep<N, R> (N node, R route, NetworkStep<N, R> parent) implements StepLike {

    public boolean contains(N node) {
        NetworkStep<N, R> curr = this;

        while (curr != null) {
            if (curr.node == node) return true;

            curr = curr.parent;
        }

        return false;
    }
}
