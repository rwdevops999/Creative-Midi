package util;

import communication.CommunicationModel;
import creative.panes.MainPane;
import creative.scenes.sysex.SysexContainer;
import device.DeviceScanner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.enums.ThemeType;
import util.properties.PropertyContainer;
import util.properties.PropertyLoader;
import util.properties.PropertyType;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Properties;

import static util.Util.loadVoiceFilenames;

public class Initializer {
    private static final Logger logger = LoggerFactory.getLogger(Initializer.class);

    public void initApp() {
        logger.debug("[CM_INITIALIZER] Initializing Application");

        CommunicationModel.setStatus("Initializing Creative Midi");

        PropertyLoader propertyLoader = new PropertyLoader();

        // 1. Load system properties
        Properties props = propertyLoader.loadPropertiesFromResource("creative.properties");
        logger.debug("[CM_INITIALIZER] System Properties loaded");
        PropertyContainer.setProperties(PropertyType.System, props);

        // 2. Set Theme
        ThemeType theme = ThemeType.valueOf(PropertyContainer.getPropertyAsString(PropertyType.System, PropertyContainer.THEME, "DARK"));
        Theme.setThemeType(theme);
        Theme.switchTheme(ApplicationInfo.getInstance().getRootScene());

        // 3. load paths
        String propertiesDirectory = PropertyContainer.getPropertyAsString(PropertyType.System, PropertyContainer.PROPERTIES_PATH, "./properties");
        Path path = Paths.get(propertiesDirectory, "paths.properties"); // Use your actual path
        Properties pathProperties = propertyLoader.loadPropertiesFromPath(path);
        PropertyContainer.setProperties(PropertyType.Path, pathProperties);

        // 4. Scan for devices
        boolean autoSelectDevice = PropertyContainer.getPropertyAsBoolean(PropertyType.System, PropertyContainer.AUTO_SELECT_DEVICE, false);
        String defaultDeviceName = PropertyContainer.getPropertyAsString(PropertyType.System, PropertyContainer.DEVICE_NAME, "Digital Keyboard");

        DeviceScanner task = new DeviceScanner(autoSelectDevice, defaultDeviceName, data -> {
            Registry.publish("DeviceSelector", data);
        });

        task.setOnSucceeded(e -> {
            logger.info("[CM_INIT] Device Scanning Task Completed");
        });

        Thread thread = new Thread(task);
        thread.start();
        ApplicationInfo.getInstance().setDeviceScannerThread(thread);

        // 5. Loading the voices (keyboard files)
        // loading the voices
        String voicesDirectory = PropertyContainer.getPropertyAsString(PropertyType.Path, PropertyContainer.VOICES_PATH, "./voices");
        List<String> voiceFileNames = loadVoiceFilenames(voicesDirectory);
        ApplicationInfo.getInstance().setVoiceFilenames(voiceFileNames);

        // 6. Load keyboard properties
        // TODO Solve this to properties file
        Path keyboardPath = Paths.get(propertiesDirectory, "keyboard.properties"); // Use your actual path
        Properties keyboardProperties = propertyLoader.loadPropertiesFromPath(keyboardPath);
        PropertyContainer.setProperties(PropertyType.GeneralKeyboard, keyboardProperties);

        // 7. Load midi file
        String midiPath = PropertyContainer.getPropertyAsString(PropertyType.Path, PropertyContainer.MIDI_PATH, "./midi");
        String filename = PropertyContainer.getPropertyAsString(PropertyType.Path, PropertyContainer.MIDI_DEMO_FILE, "creative.mid");

        File midiFile = new File(midiPath + "/" + filename);
        ApplicationInfo.getInstance().setMidiToTry(midiFile);

        // 8. Load Sysex events
        SysexContainer.loadSysexEvents();
    }
}
