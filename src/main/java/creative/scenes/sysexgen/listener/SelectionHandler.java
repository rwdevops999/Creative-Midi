package creative.scenes.sysexgen.listener;

@FunctionalInterface
public interface SelectionHandler<T> {
    void handleSelection(T selectedItem);
}
