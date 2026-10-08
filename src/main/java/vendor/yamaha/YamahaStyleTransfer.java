package vendor.yamaha;

import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MidiDevice;
import javax.sound.midi.Receiver;
import javax.sound.midi.SysexMessage;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;

public class YamahaStyleTransfer {
    private YamahaStyleTransfer() {
    }

    public static void sendStyleFile(
            Path styleFile,
            MidiDevice device,
            String targetFileName) throws Exception {
        if (!Files.exists(styleFile)) {
            throw new IOException("Style file does not exist: " + styleFile);
        }

        byte[] data = Files.readAllBytes(styleFile);

        if (data.length == 0) {
            throw new IOException("Style file is empty");
        }

        if (!device.isOpen()) {
            device.open();
        }

        Receiver receiver = device.getReceiver();

        YamahaTransfer transfer = new YamahaTransfer(receiver);

        System.out.println("Sending: " + styleFile);
        System.out.println("Size: " + data.length + " bytes");
        System.out.println("Target: " + targetFileName);

        transfer.start();

        transfer.selectUserStorage();

        transfer.createFile(targetFileName);

        transfer.sendFile(data);

        transfer.finish();

        System.out.println("Transfer finished.");
    }

    private static final class YamahaTransfer {

        private final Receiver receiver;

        YamahaTransfer(Receiver receiver) {
            this.receiver = receiver;
        }

        private void send(byte[] message) throws InvalidMidiDataException {
            SysexMessage sysex = new SysexMessage();

            sysex.setMessage(
                    SysexMessage.SYSTEM_EXCLUSIVE,
                    message,
                    message.length
            );

            receiver.send(sysex, -1);

            System.out.println(
                    "TX: " + hex(message)
            );
        }

        /**
         * Initial Yamaha file-system handshake.
         *
         * Observed in the user's MIDI-OX capture.
         */
        void start() throws InvalidMidiDataException {

            send(new byte[] {
                    (byte) 0xF0,
                    0x43, 0x50, 0x00,
                    0x00, 0x00, 0x02,
                    0x01, 0x02,
                    (byte) 0xF7
            });

            send(new byte[] {
                    (byte) 0xF0,
                    0x43, 0x50, 0x00,
                    0x00, 0x01, 0x02,
                    0x00,
                    (byte) 0xF7
            });
        }

        /**
         * Select USER storage.
         *
         * This packet was observed in the SX600 capture:
         *
         * F0 43 50 00 05 0B 01
         * 42 00 00 05 00
         * "USER"
         * F7
         */
        void selectUserStorage()
                throws InvalidMidiDataException {

            send(new byte[] {
                    (byte) 0xF0,
                    0x43, 0x50, 0x00,
                    0x05, 0x0B, 0x01,
                    0x42,
                    0x00, 0x00,
                    0x05, 0x00,
                    'U', 'S', 'E', 'R',
                    (byte) 0xF7
            });
        }

        /**
         * Create/select the destination filename.
         *
         * This is based on the directory-entry structure observed
         * in the user's MIDI-OX capture.
         */
        void createFile(String filename)
                throws InvalidMidiDataException {

            byte[] name = filename.getBytes(
                    java.nio.charset.StandardCharsets.US_ASCII
            );

            if (name.length > 32) {
                throw new IllegalArgumentException(
                        "Yamaha filename is too long"
                );
            }

            byte[] msg = new byte[
                    1 + 4 + 1 + 1 + 1 + 14 + 6 +
                            name.length + 3
                    ];

            int p = 0;

            msg[p++] = (byte) 0xF0;

            // Yamaha
            msg[p++] = 0x43;
            msg[p++] = 0x50;
            msg[p++] = 0x00;

            // File operation
            msg[p++] = 0x05;
            msg[p++] = 0x05;
            msg[p++] = 0x01;

            msg[p++] = 0x00;

            // Date/time fields seen in the SX600 capture.
            byte[] date = "2020 1 1 0 0 0"
                    .getBytes(
                            java.nio.charset.StandardCharsets.US_ASCII
                    );

            System.arraycopy(
                    date, 0,
                    msg, p,
                    date.length
            );

            p += date.length;

            /*
             * The following fields are provisional.
             * They are deliberately isolated here because their
             * exact meaning still needs confirmation from the SX600
             * transfer capture.
             */
            msg[p++] = 0x02;
            msg[p++] = 0x24;
            msg[p++] = 0x0B;
            msg[p++] = 0x00;
            msg[p++] = (byte) (name.length + 3);
            msg[p++] = 0x00;

            System.arraycopy(
                    name, 0,
                    msg, p,
                    name.length
            );

            p += name.length;

            msg[p++] = 0x00;
            msg[p++] = 0x00;
            msg[p++] = 0x00;

            msg[p] = (byte) 0xF7;

            send(msg);
        }

        /**
         * Send the actual file.
         *
         * Yamaha's reverse-engineered protocol uses
         *
         * F0 43 50 01 ...
         *
         * for data packets.
         */
        void sendFile(byte[] file)
                throws InvalidMidiDataException {

            final int CHUNK_SIZE = 64;

            int offset = 0;

            while (offset < file.length) {

                int length = Math.min(
                        CHUNK_SIZE,
                        file.length - offset
                );

                byte[] chunk = Arrays.copyOfRange(
                        file,
                        offset,
                        offset + length
                );

                sendDataPacket(chunk);

                offset += length;

                System.out.printf(
                        "Progress: %d / %d bytes%n",
                        offset,
                        file.length
                );
            }
        }

        /**
         * Construct a Yamaha 43 50 01 data packet.
         *
         * NOTE:
         * Header fields here are based on the reverse-engineered
         * protocol and must be matched against the SX600 capture.
         */
        private void sendDataPacket(byte[] data)
                throws InvalidMidiDataException {

            byte[] packet = new byte[
                    1 +
                            4 +
                            8 +
                            data.length +
                            2
                    ];

            int p = 0;

            packet[p++] = (byte) 0xF0;

            packet[p++] = 0x43;
            packet[p++] = 0x50;
            packet[p++] = 0x01;

            /*
             * Transfer header.
             *
             * These values are provisional.
             */
            packet[p++] = 0x00;
            packet[p++] = 0x23;
            packet[p++] = 0x02;
            packet[p++] = 0x02;
            packet[p++] = 0x04;
            packet[p++] = 0x00;
            packet[p++] = (byte) (data.length & 0x7F);
            packet[p++] = (byte) ((data.length >> 7) & 0x7F);

            System.arraycopy(
                    data,
                    0,
                    packet,
                    p,
                    data.length
            );

            p += data.length;

            /*
             * Placeholder checksum.
             *
             * The exact checksum for the SX600 transfer still
             * needs to be derived from the captured packets.
             */
            packet[p++] = 0x00;

            packet[p] = (byte) 0xF7;

            send(packet);
        }

        /**
         * Complete the transfer.
         *
         * The 03 00 / 03 02 / 03 03 sequence has been observed
         * in Yamaha style-transfer implementations.
         */
        void finish() throws InvalidMidiDataException {

            send(new byte[] {
                    (byte) 0xF0,
                    0x43, 0x50, 0x00,
                    0x03, 0x00,
                    (byte) 0xF7
            });

            send(new byte[] {
                    (byte) 0xF0,
                    0x43, 0x50, 0x00,
                    0x03, 0x02,
                    (byte) 0xF7
            });

            send(new byte[] {
                    (byte) 0xF0,
                    0x43, 0x50, 0x00,
                    0x03, 0x03,
                    (byte) 0xF7
            });

            send(new byte[] {
                    (byte) 0xF0,
                    0x43, 0x50, 0x00,
                    0x00, 0x01,
                    0x00, 0x00,
                    (byte) 0xF7
            });
        }

        private static String hex(byte[] data) {

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
    }
}