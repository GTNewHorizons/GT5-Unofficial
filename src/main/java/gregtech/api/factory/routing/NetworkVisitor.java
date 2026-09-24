package gregtech.api.factory.routing;

public interface NetworkVisitor<N, R> {

    /// Called for each node in the network.
    ///
    /// @return True when the node should be stepped into. False when the node should be skipped.
    VisitorResult visit(NetworkStep<N, R> step);
}
