package vendor.yamaha;

import creative.scenes.midi.util.MidiWriter;
import creative.scenes.sysex.util.SysexWriter;
import util.ApplicationInfo;

import javax.sound.midi.*;
import java.io.Writer;

public class StyleTransfer {
    public StyleTransfer() {
    }

    public void firstTest() throws Exception {
        addListener();

        MidiDevice device = ApplicationInfo.getInstance().getMidiOutputDevice();

        if (device != null) {
            if (! device.isOpen()) {
                device.open();
            }

            Receiver receiver = device.getReceiver();
            YamahaTransfer transferToPSR = new YamahaTransfer(receiver);

/*            ShortMessage noteOn = new ShortMessage();
            noteOn.setMessage(
                    ShortMessage.NOTE_ON,
                    0,
                    60,
                    100
            );

            // Note off
            ShortMessage noteOff = new ShortMessage();
            noteOff.setMessage(
                    ShortMessage.NOTE_OFF,
                    0,
                    60,
                    0
            );
*/
            byte[] identityRequest = {
                    (byte) 0xF0,
                    (byte) 0x7E,
                    (byte) 0x7F,
                    (byte) 0x06,
                    (byte) 0x01,
                    (byte) 0xF7
            };

            transferToPSR.sendSysex(identityRequest);
            Thread.sleep(700);

            receiver.close();

            if (device.isOpen()) {
                device.close();
            }
        }
    }

    private void addListener() throws Exception {
        MidiDevice inputDevice = ApplicationInfo.getInstance().getMidiInputDevice();
        if (inputDevice != null) {
            Transmitter transmitter = inputDevice.getTransmitter();
            transmitter.setReceiver(new Receiver() {
                @Override
                public void send(MidiMessage message, long timeStamp) {
                    byte[] bytes = message.getMessage();
                    if ((bytes[0] & 0xFF) != 0XFE) {
                        System.out.print("RECEIVED SOMETHING FROM PSR");

                        for (byte b : bytes) {
                            System.out.printf("%02X ", b & 0xFF);
                        }

                        System.out.println();
                    }
                }

                @Override
                public void close() {
                }
            });
        }
    }
    private static final class YamahaTransfer {
        private final Receiver receiver;

        SysexWriter sysexWriter;
        YamahaTransfer(Receiver receiver) {
            this.receiver = receiver;
            this.sysexWriter = new SysexWriter();
        }

        private static String toHex(byte[] data) {

            StringBuilder sb =
                    new StringBuilder();

            for (byte b : data) {
                sb.append(
                        String.format(
                                "%02X ",
                                b & 0xFF
                        )
                );
            }

            return sb.toString().trim();
        }

        public void sendSysex(byte[] message) throws InvalidMidiDataException {
            SysexMessage sysex = new SysexMessage();

            sysex.setMessage(
                    SysexMessage.SYSTEM_EXCLUSIVE,
                    message,
                    message.length
            );

            receiver.send(sysex, -1);

            System.out.println(
                    "TX: " + toHex(message)
            );
        }
    }
}
