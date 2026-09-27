package device;

import entity.device.DeviceInfo;
import javafx.application.Platform;
import javafx.concurrent.Task;

import java.util.function.Consumer;

public class DeviceScanner extends Task<String> {
    private Boolean autoSelectDevice = false;
    private String defaultDeviceName = null;
    private final Consumer<DeviceInfo> dataConsumer;

    private DeviceService deviceService = new DeviceService();

    public DeviceScanner(Boolean autoSelect, String defaultDeviceName, Consumer<DeviceInfo> dataConsumer) {
        this.autoSelectDevice = autoSelect;
        this.defaultDeviceName = defaultDeviceName;
        this.dataConsumer = dataConsumer;
    }

    @Override
    protected String call() throws Exception {
        while (!isCancelled()) {
            try {
                DeviceInfo di = deviceService.scanMidiDevices();
                di.setDefaultDeviceName(this.defaultDeviceName);
                di.setAutoSelect(this.autoSelectDevice);

                Platform.runLater(() -> {
                    dataConsumer.accept(di);
                });
                Thread.sleep(1000);
            } catch (InterruptedException interrupted) {
                if (isCancelled()) {
                    break; // Exit the loop gracefully if interrupted while sleeping
                }
            }
        }

        return null;
    }
}
