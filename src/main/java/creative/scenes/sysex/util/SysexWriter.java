package creative.scenes.sysex.util;

import communication.CommunicationModel;
import creative.scenes.sysex.convertor.SysexToHexStringConvertor;
import custom.dialog.DialogFactory;
import entity.sysex.Sysex;
import entity.sysex.SysexContent;
import org.slf4j.helpers.MessageFormatter;
import util.ApplicationInfo;

import javax.sound.midi.*;
import java.util.HexFormat;

public class SysexWriter {
    public void sendSysex(Sysex sysex) {
        MidiDevice outputDevice = ApplicationInfo.getInstance().getMidiOutputDevice();
        if (outputDevice == null) {
            CommunicationModel.monitorError("No device selected");
        } else {
            try {
                Receiver receiver = outputDevice.getReceiver();

                SysexMessage message = new SysexMessage();
                for (SysexContent sysexContent : sysex.getList()) {
                    String content = sysexContent.getContent();

                    byte[] data = HexFormat.of().parseHex(content.replace(" ", ""));

                    message.setMessage(data, data.length);
                    String status = MessageFormatter.basicArrayFormat("Sending SysEx [{}]", new Object[]{sysexContent.getContent()});
                    CommunicationModel.setStatus(status);

                    receiver.send(message, -1);

                    CommunicationModel.monitorOutbound(sysexContent.getContent());
                }

                receiver.close();
            } catch (MidiUnavailableException | InvalidMidiDataException e) {
                DialogFactory.renderErrorDialog(e.getMessage());
            }
        }
    }

    public void sendSysex(byte[] data) {
        MidiDevice outputDevice = ApplicationInfo.getInstance().getMidiOutputDevice();
        if (outputDevice == null) {
            CommunicationModel.monitorError("No device selected");
        } else {
            try {
                Receiver receiver = outputDevice.getReceiver();

                SysexMessage message = new SysexMessage();
                message.setMessage(data, data.length);

                receiver.send(message, -1);

                CommunicationModel.monitorOutbound(SysexToHexStringConvertor.convertToHexString(message.getData()));
            } catch (MidiUnavailableException | InvalidMidiDataException e) {
                DialogFactory.renderErrorDialog(e.getMessage());
            }
        }
    }

    public void sendSysex(MidiDevice outputDevice, byte[] data) {
        try {
            Receiver receiver = outputDevice.getReceiver();

            SysexMessage message = new SysexMessage();
            message.setMessage(data, data.length);

            receiver.send(message, -1);

            CommunicationModel.monitorOutbound("[" + outputDevice.getDeviceInfo().getName() + "]: " + SysexToHexStringConvertor.convertToHexString(message.getMessage()));
        } catch (MidiUnavailableException | InvalidMidiDataException e) {
            DialogFactory.renderErrorDialog(e.getMessage());
        }
    }
}
