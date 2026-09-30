package device;

import entity.device.DeviceInfo;
import javafx.application.Platform;
import javafx.concurrent.Task;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.function.Consumer;

public class DeviceScanner extends Task<String> {
    private static final Logger logger = LoggerFactory.getLogger(DeviceScanner.class);

    private Boolean autoSelectDevice = false;
    private String defaultDeviceName = null;
    private final Consumer<DeviceInfo> dataConsumer;

    private DeviceService deviceService = new DeviceService();

    public DeviceScanner(Boolean autoSelect, String defaultDeviceName, Consumer<DeviceInfo> dataConsumer) {
        this.autoSelectDevice = autoSelect;
        this.defaultDeviceName = defaultDeviceName;
        this.dataConsumer = dataConsumer;
    }

    private int counter = 0;
    @Override
    protected String call() throws Exception {
        while (!isCancelled()) {
            try {
                if (counter++ == 0) {
                    logger.info("Scanning for " + this.defaultDeviceName);
                }
                DeviceInfo di = deviceService.scanMidiDevices();
                di.setDefaultDeviceName(this.defaultDeviceName);
                di.setAutoSelect(this.autoSelectDevice);

                Platform.runLater(() -> {
                    dataConsumer.accept(di);
                });
                Thread.sleep(1000);
            } catch (InterruptedException interrupted) {
                break; // Exit the loop gracefully if interrupted while sleeping
            }
        }

        return null;
    }
}
