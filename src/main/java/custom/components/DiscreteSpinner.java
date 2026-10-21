package custom.components;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;

public class DiscreteSpinner extends Spinner<Integer> {
    public DiscreteSpinner() {
        super();
    }

    public DiscreteSpinner(int min, int max, int initialValue) {
        this();

        ObservableList<Integer> allowedValues = FXCollections.observableArrayList(min, max);

        // 2. Koppel de lijst aan de spinner via de ListSpinnerValueFactory
        this.setValueFactory(new SpinnerValueFactory.ListSpinnerValueFactory<>(allowedValues));

        this.getValueFactory().setValue(initialValue);

        // 3. Standaard configuratie voor gebruiksgemak
        this.getValueFactory().setWrapAround(true);
        this.setEditable(false);
    }
}
