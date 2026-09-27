package device;

import entity.device.DeviceInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uk.co.xfactorylibrarians.coremidi4j.CoreMidiDeviceProvider;
import uk.co.xfactorylibrarians.coremidi4j.CoreMidiException;
import util.OS;

import javax.sound.midi.MidiDevice;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.MidiUnavailableException;
import java.util.ArrayList;
import java.util.List;

public class DeviceService {
    private static final Logger logger = LoggerFactory.getLogger(DeviceService.class);

    public DeviceService() {
    }

    public DeviceInfo scanMidiDevices() {
        CoreMidiDeviceProvider provider = null;

        DeviceInfo deviceInfo = new DeviceInfo();

        try {
            MidiDevice.Info[] infos;
            if (OS.isWindows()) {
                infos = MidiSystem.getMidiDeviceInfo();
            } else {
                provider = new CoreMidiDeviceProvider();
                infos = provider.getDeviceInfo();
            }

            for (MidiDevice.Info info : infos) {
                MidiDevice dev = null;
                if (OS.isWindows()) {
                    dev = MidiSystem.getMidiDevice(info);
                } else {
                    dev = provider != null ? provider.getDevice(info) : null;
                }

                if (dev != null) {
                    if (! dev.getClass().getName().equals("com.sun.media.sound.RealTimeSequencer")) {
                        // if the device is not already in the map, we add it and create a list for it
                        // (which will contain input and output)
                        if (dev.getMaxTransmitters() != 0) {
                            deviceInfo.addCandidate(dev.getDeviceInfo().getName(), dev);
                        }

                        // check from app to keyboard
                        if (dev.getMaxReceivers() != 0) {
                            deviceInfo.addCandidate(dev.getDeviceInfo().getName(), dev);
                        }
                    }
                }
            }
        } catch (CoreMidiException cme) {
            logger.error("[CM_DEVICE_SERVICE] Core Midi Exception. CAUSE: {}", cme.getMessage());
        } catch (MidiUnavailableException mue) {
            logger.error("[CM_DEVICE_SERVICE] Midi Unavailable Exception. CAUSE: {}", mue.getMessage());
        }

        return deviceInfo;
    }
}
