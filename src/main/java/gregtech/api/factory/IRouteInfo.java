package gregtech.api.factory;

public interface IRouteInfo<TSelf extends IRouteInfo<TSelf>> extends Comparable<TSelf> {

    /** Merges the distances/etc of two routing infos and returns a copy. */
    TSelf merge(TSelf other);

    TSelf copy();
}
