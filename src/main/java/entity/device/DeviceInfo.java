package entity.device;

import lombok.Getter;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.ApplicationInfo;

import javax.sound.midi.MidiDevice;
import javax.sound.midi.MidiUnavailableException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Getter
@Setter
public class DeviceInfo {
    private static final Logger logger = LoggerFactory.getLogger(DeviceInfo.class);

    private String defaultDeviceName = null;
    private Boolean autoSelect = false;
    private List<String> candidates;
    private Map<String , List<MidiDevice>> devices = new HashMap<>();

    public DeviceInfo() {
        this.candidates = new ArrayList<>();
    }

    public void addCandidate(String name, MidiDevice device) {
        devices.computeIfAbsent(name, k -> new ArrayList<>());
        devices.get(name).add(device);

        if (devices.get(name).size() == 2) {
            candidates.add(name);
        }
    }

    private void handleDevice(MidiDevice device) {
        try {
            // This is from keyboard to Java
            if (device.getMaxTransmitters() != 0) {
                if (! device.isOpen()) {
                    device.open();
                    ApplicationInfo.getInstance().setMidiInputDevice(device);
                }
            }

            // This is from Java to Keyboard
            if (device.getMaxReceivers() != 0) {
                if (! device.isOpen()) {
                    device.open();
                    ApplicationInfo.getInstance().setMidiOutputDevice(device);
                }
            }
        } catch (MidiUnavailableException mue) {
            logger.error("[CM_DEVICE_INFO] EXCEPTION: Midi is unavailable. CAUSE: {}", mue.getMessage());
        }
    }

    public void selectDevice(String deviceName) {
        List<MidiDevice> devs = devices.get(deviceName);
        handleDevice(devs.get(0));
        handleDevice(devs.get(1));
    }

    public void deselectDevice(String deviceName) {
        List<MidiDevice> devs = devices.get(deviceName);

        if (devs != null) {
            MidiDevice device = devs.get(0);
            if (device != null && device.isOpen()) {
                device.close();
                ApplicationInfo.getInstance().setMidiInputDevice(null);
            }

            device = devs.get(1);
            if (device != null && device.isOpen()) {
                device.close();
                ApplicationInfo.getInstance().setMidiOutputDevice(null);
            }
        }
    }
}
