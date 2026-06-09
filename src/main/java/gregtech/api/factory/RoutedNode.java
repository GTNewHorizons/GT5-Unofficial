package gregtech.api.factory;

import com.github.bsideup.jabel.Desugar;

@Desugar
public record RoutedNode<TElement, TRouteInfo> (TElement element, TRouteInfo routeInfo) {

}
