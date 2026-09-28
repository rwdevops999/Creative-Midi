package creative.scenes.midi.util;

import communication.CommunicationModel;
import custom.dialog.DialogFactory;
import entity.midi.Midi;
import javafx.application.Platform;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.helpers.MessageFormatter;
import util.ApplicationInfo;

import javax.sound.midi.*;

public class MidiWriter {
    private static final Logger logger = LoggerFactory.getLogger(MidiWriter.class);

    public void sendMidi (Midi midi) {
        logger.debug("[CM_MIDI_WRITER] Sending MIDI {}", midi.getStatus());

        MidiDevice outputDevice = ApplicationInfo.getInstance().getMidiOutputDevice();
        if (outputDevice == null) {
            // TODO Add Monitoring and Dialogs
            CommunicationModel.monitorError("No device selected");
//            DialogFactory.renderWarningDialog("No device selected");
        } else {
            try {
                Receiver receiver = outputDevice.getReceiver();
                ShortMessage midiMessage = new ShortMessage();

                String midiToSend = midi.toString();

                // 1. Split the string into hex tokens
                String[] hexTokens = midiToSend.trim().split("\\s+");
                if (hexTokens.length < 3) {
                    throw new IllegalArgumentException("MIDI string must contain at least 3 hex bytes.");
                }

                int status = Integer.parseInt(hexTokens[0], 16); // 0x90 (144) -> Note On, Channel 1
                int data1  = Integer.parseInt(hexTokens[1], 16); // 0x3C (60)  -> Note C4
                int data2  = Integer.parseInt(hexTokens[2], 16); // 0x3F (63)  -> Velocity

                // 3. Construct and configure the ShortMessage
                ShortMessage message = new ShortMessage();
                message.setMessage(status, data1, data2);

                String monitorMessage = MessageFormatter.basicArrayFormat("[{} {} {} {}]", new Object[]{midiMessage.getStatus(), midiMessage.getChannel(), midiMessage.getData1(), midiMessage.getData2()});
                CommunicationModel.setStatus(monitorMessage);
                CommunicationModel.monitorOutgoing("[MIDI] " + message);

                // 4. Send message
                receiver.send(midiMessage, -1);
            } catch (MidiUnavailableException mue) {
                logger.error("[CM_MIDI_WRITER] EXCEPTION: Device not ready for sending midi. CAUSE {}", mue.getMessage());
//                DialogFactory.renderErrorDialog("Device not ready");
            } catch (InvalidMidiDataException imde) {
                logger.error("[CM_MIDI_WRITER] EXCEPTION: Midi data incorrect. CAUSE {}", imde.getMessage());
//                String message = MessageFormatter.basicArrayFormat("MIDI Command is not constructed well for {}", new Object[]{midi.getStatus()});
//                DialogFactory.renderErrorDialog(message);
            }
        }
    }

    public void sendMidiMessage (ShortMessage midiMessage) {
        logger.debug("[CM_MIDI_WRITER] Sending MIDI Message {}", midiMessage);

        MidiDevice outputDevice = ApplicationInfo.getInstance().getMidiOutputDevice();
        if (outputDevice == null) {
            CommunicationModel.monitorError("No device selected");
//            DialogFactory.renderWarningDialog("No device selected");
        } else {
            try {
                Receiver receiver = outputDevice.getReceiver();

                String message;
                message = MessageFormatter.basicArrayFormat("[{} {} {} {}]", new Object[]{midiMessage.getStatus(), midiMessage.getChannel(), midiMessage.getData1(), midiMessage.getData2()});
                CommunicationModel.setStatus(message);
                CommunicationModel.monitorOutgoing("[MIDI] " + message);

                receiver.send(midiMessage, -1);
            } catch (MidiUnavailableException mue) {
                logger.error("[CM_MIDI_WRITER] EXCEPTION: Device not ready for sending midi. CAUSE {}", mue.getMessage());
                DialogFactory.renderErrorDialog("Device not ready");
            }
        }
    }

    public boolean sendMidiMessageAsString (String message) {
        MidiDevice outputDevice = ApplicationInfo.getInstance().getMidiOutputDevice();
        if (outputDevice == null) {
            // TODO Add Monitoring and Dialogs
            CommunicationModel.monitorError("No device selected");
            DialogFactory.renderWarningDialog("No device selected");
            return false;
        } else {
            try {
                Receiver receiver = outputDevice.getReceiver();

                // 1. Split the string into hex tokens
                String[] hexTokens = message.trim().split("\\s+");

                int status = Integer.parseInt(hexTokens[0], 16); // 0x90 (144) -> Note On, Channel 1
                int data1  = Integer.parseInt(hexTokens[1], 16); // 0x3C (60)  -> Note C4
                int data2  = Integer.parseInt(hexTokens[2], 16); // 0x3F (63)  -> Velocity

                // 3. Construct and configure the ShortMessage
                ShortMessage shortMessage = new ShortMessage();
                shortMessage.setMessage(status, data1, data2);

                Platform.runLater(() -> {
                    CommunicationModel.setStatus(message);
                    CommunicationModel.monitorOutgoing("[MIDI] " + message);
                });

                // 4. Send message
                receiver.send(shortMessage, -1);
            } catch (MidiUnavailableException mue) {
                logger.error("[CM_MIDI_WRITER] EXCEPTION: Device not ready for sending midi. CAUSE {}", mue.getMessage());
                DialogFactory.renderErrorDialog("Device not ready");
                return false;
            } catch (InvalidMidiDataException imde) {
                logger.error("[CM_MIDI_WRITER] EXCEPTION: Midi data incorrect. CAUSE {}", imde.getMessage());
                String strMessage = MessageFormatter.basicArrayFormat("MIDI Command is not constructed well for {}", new Object[]{message});
                DialogFactory.renderErrorDialog(strMessage);
                return false;
            }
        }

        return true;
    }
}
