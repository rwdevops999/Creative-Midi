package creative.scenes.sysexgen2.listener;

@FunctionalInterface
public interface SelectionHandler<T> {
    void handleSelection(T selectedItem);
}
