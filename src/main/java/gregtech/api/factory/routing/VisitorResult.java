package gregtech.api.factory.routing;

public enum VisitorResult {
    /// Node was accepted and iteration should continue
    Continue,
    /// Node was invalid and its children should be ignored, but iteration should continue
    SkipNode,
    /// Node/network was invalid or process is complete and iteration should abort
    Break,
}
