package eventhandlers;

import creative.scenes.voice.provider.InstrumentProvider;
import creative.scenes.voice.provider.InstrumentProviderFactory;
import custom.dialog.DialogFactory;
import device.DeviceScanner;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.ComboBox;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.ApplicationInfo;
import util.Registry;
import util.properties.PropertyContainer;
import util.properties.PropertyLoader;
import util.properties.PropertyType;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Properties;

public class KeyboardHandler<T extends ActionEvent> implements EventHandler<T> {
    private static final Logger logger = LoggerFactory.getLogger(KeyboardHandler.class);

    @Override
    public void handle(T event) {
        logger.debug("[CM_KEYBOARD_HANDLER] Handling event {}", event.getClass().getSimpleName());

        ComboBox<String> combo = (ComboBox<String>)event.getTarget();

        String[] split = combo.getValue().split(" ");

        logger.debug("[CM_KEYBOARD_HANDLER] Handling selected keyboard {}", combo.getValue());
        String voicesDirectory = PropertyContainer.getPropertyAsString(PropertyType.Path, PropertyContainer.VOICES_PATH, "./voices");
        Path dirPath = Paths.get(voicesDirectory); // Use your actual path

        String provider = split[1].substring(1, split[1].length() - 1);
        String keyboard = split[0];
        String extension = provider.equalsIgnoreCase("Yamaha") ? ".txt" : ".ins";

        InstrumentProvider instrumentProvider = InstrumentProviderFactory.getProvider(dirPath + "/" + keyboard + extension);
        ApplicationInfo.getInstance().setInstrumentProvider(instrumentProvider);

        Registry.publish("VoiceGroupSelector", null);

        // Restart Device Polling task
        String propertyPath = PropertyContainer.getPropertyAsString(PropertyType.System, PropertyContainer.PROPERTIES_PATH, "./properties");
        PropertyLoader propertyLoader = new PropertyLoader();

        String filename = propertyPath + "/" + keyboard + ".properties";
        String loadedFilename = ApplicationInfo.getInstance().getKeyboardProperties();

        if (! filename.equals(loadedFilename)) {
            ApplicationInfo.getInstance().setKeyboardProperties(filename);

            Path path = Paths.get(propertyPath, keyboard + ".properties"); // Use your actual path
            Properties props = propertyLoader.loadPropertiesFromPath(path);
            PropertyContainer.setProperties(PropertyType.Keyboard, props);

            Thread thread = ApplicationInfo.getInstance().getDeviceScannerThread();
            if (thread != null) {
                thread.interrupt();
                try {
                    thread.join();
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }

            String defaultDeviceName = PropertyContainer.getPropertyAsString(PropertyType.Keyboard, PropertyContainer.DEVICE_NAME, null);
            if (defaultDeviceName == null) {
                DialogFactory.renderErrorDialog("Default device name not found in properties file");
            }

            DeviceScanner task = new DeviceScanner(false, defaultDeviceName, data -> {
                Registry.publish("DeviceSelector", data);
            });

            task.setOnSucceeded(e -> {
                logger.info("[CM_KEYBOARD_HANDLER] Device Scanning Task Completed");
            });

            thread = new Thread(task);
            thread.start();

            ApplicationInfo.getInstance().setDeviceScannerThread(thread);
        }

        logger.debug("[CM_KEYBOARD_HANDLER] Handled event {}", event.getClass().getSimpleName());
    }
}
