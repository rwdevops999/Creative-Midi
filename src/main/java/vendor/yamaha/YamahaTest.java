package vendor.yamaha;

import javax.sound.midi.*;

public class YamahaTest {

    public static void send() throws Exception {

        MidiDevice device = null;

        for (MidiDevice.Info info : MidiSystem.getMidiDeviceInfo()) {
            if (info.getName().equals("Digital Keyboard-1")) {
                device = MidiSystem.getMidiDevice(info);
                break;
            }
        }

        if (device == null) {
            throw new Exception("Digital-Keyboard-1 not found");
        }

        System.out.println("Device: " + device.getDeviceInfo().getName());
        System.out.println("Max receivers: " + device.getMaxReceivers());

        device.open();

        System.out.println("Device opened: " + device.isOpen());

        Receiver receiver = device.getReceiver();

        byte[] sysex = {
                (byte) 0xF0,
                0x43,
                0x50,
                0x00,
                0x00,
                0x00,
                0x02,
                0x01,
                0x02,
                (byte) 0xF7
        };

        SysexMessage message = new SysexMessage();
        message.setMessage(sysex, sysex.length);

        System.out.println("Sending:");
        for (byte b : sysex) {
            System.out.printf("%02X ", b & 0xFF);
        }
        System.out.println();

        receiver.send(message, -1);

        System.out.println("SysEx sent.");

        receiver.close();
        device.close();
    }

    public static void ListMidiDevices () throws Exception {
        MidiDevice.Info[] infos = MidiSystem.getMidiDeviceInfo();

        for (MidiDevice.Info info : infos) {
            try {
                MidiDevice device = MidiSystem.getMidiDevice(info);

                System.out.println("----------------------------------------");
                System.out.println("Name        : " + info.getName());
                System.out.println("Description : " + info.getDescription());
                System.out.println("Vendor      : " + info.getVendor());
                System.out.println("Version     : " + info.getVersion());
                System.out.println("Max Receivers: " + device.getMaxReceivers());
                System.out.println("Max Transmitters: " + device.getMaxTransmitters());

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static void sendTest() throws Exception {
        MidiDevice.Info selectedInfo = null;

        for (MidiDevice.Info info : MidiSystem.getMidiDeviceInfo()) {

            MidiDevice device = MidiSystem.getMidiDevice(info);

            if (info.getName().equals("Digital Keyboard-1")
                    && device.getMaxReceivers() != 0) {

                selectedInfo = info;

                System.out.println("Selected:");
                System.out.println("Name: " + info.getName());
                System.out.println("Description: " + info.getDescription());
                System.out.println("Receivers: " + device.getMaxReceivers());
                System.out.println("Transmitters: " + device.getMaxTransmitters());

                break;
            }
        }

        if (selectedInfo == null) {
            throw new Exception("MIDI output Digital Keyboard-1 not found.");
        }

        MidiDevice device = MidiSystem.getMidiDevice(selectedInfo);

        device.open();

        Receiver receiver = device.getReceiver();

        byte[] bytes = {
                (byte) 0xF0,
                0x43,
                0x50,
                0x00,
                0x00,
                0x00,
                0x02,
                0x01,
                0x02,
                (byte) 0xF7
        };

        SysexMessage sysex = new SysexMessage();
        sysex.setMessage(bytes, bytes.length);

        System.out.println("Sending SysEx:");

        for (byte b : bytes) {
            System.out.printf("%02X ", b & 0xFF);
        }

        System.out.println();

        receiver.send(sysex, -1);

        System.out.println("Sent.");

        Thread.sleep(1000);

        receiver.close();
        device.close();

        System.out.println("Closed.");
    }

    public static void syexTest() throws Exception {
        MidiDevice output = null;
        MidiDevice input = null;

        for (MidiDevice.Info info : MidiSystem.getMidiDeviceInfo()) {

            if (!info.getName().equals("Digital Keyboard-1")) {
                continue;
            }

            MidiDevice device = MidiSystem.getMidiDevice(info);

            if (device.getMaxReceivers() != 0 &&
                    device.getMaxTransmitters() == 0) {

                output = device;

                System.out.println("OUTPUT:");
                System.out.println(info.getName());
                System.out.println(info.getDescription());
            }

            if (device.getMaxReceivers() == 0 &&
                    device.getMaxTransmitters() != 0) {

                input = device;

                System.out.println("INPUT:");
                System.out.println(info.getName());
                System.out.println(info.getDescription());
            }
        }

        if (output == null)
            throw new Exception("MIDI output not found.");

        if (input == null)
            throw new Exception("MIDI input not found.");

        output.open();
        input.open();

        // Listen to PSR-SX600
        Transmitter transmitter = input.getTransmitter();

        transmitter.setReceiver(new Receiver() {

            @Override
            public void send(MidiMessage message, long timeStamp) {

                if (message instanceof SysexMessage) {

                    byte[] data = message.getMessage();

                    System.out.print("RECEIVED: ");

                    for (byte b : data) {
                        System.out.printf("%02X ", b & 0xFF);
                    }

                    System.out.println();
                }
            }

            @Override
            public void close() {
            }
        });

        // Send Yamaha command
        Receiver receiver = output.getReceiver();

        byte[] data = {
                (byte) 0xF0,
                0x43,
                0x50,
                0x00,
                0x00,
                0x01,
                0x02,
                0x00,
                (byte) 0xF7
        };

        SysexMessage message = new SysexMessage();
        message.setMessage(data, data.length);

        System.out.println("SEND:");

        for (byte b : data) {
            System.out.printf("%02X ", b & 0xFF);
        }

        System.out.println();

        receiver.send(message, -1);

        System.out.println("Waiting for response...");

        Thread.sleep(3000);

        transmitter.close();
        receiver.close();
        input.close();
        output.close();
    }

    public static void sequenceTest () throws Exception {
        MidiDevice output = null;

        for (MidiDevice.Info info : MidiSystem.getMidiDeviceInfo()) {

            if (!info.getName().equals("Digital Keyboard-1")) {
                continue;
            }

            MidiDevice device = MidiSystem.getMidiDevice(info);

            // PC -> PSR-SX600
            if (device.getMaxReceivers() != 0 &&
                    device.getMaxTransmitters() == 0) {

                output = device;
                break;
            }
        }

        if (output == null) {
            throw new Exception("Digital Keyboard-1 output not found.");
        }

        output.open();

        Receiver receiver = output.getReceiver();

        byte[][] messages = {

                {
                        (byte)0xF0, 0x43, 0x50, 0x00,
                        0x00, 0x00, 0x02, 0x01, 0x02,
                        (byte)0xF7
                },

                {
                        (byte)0xF0, 0x43, 0x50, 0x00,
                        0x00, 0x02, 0x02, 0x33,
                        0x00, 0x01, 0x00, 0x00,
                        0x01, 0x00, 0x01, 0x00,
                        0x00, 0x1B,
                        (byte)0xF7
                },

                {
                        (byte)0xF0, 0x43, 0x50, 0x00,
                        0x00, 0x01, 0x02, 0x00,
                        (byte)0xF7
                },

                {
                        (byte)0xF0, 0x43, 0x50, 0x00,
                        0x00, 0x01, 0x02, 0x01,
                        (byte)0xF7
                }
        };

        for (byte[] data : messages) {

            SysexMessage sysex = new SysexMessage();
            sysex.setMessage(data, data.length);

            System.out.print("SEND: ");

            for (byte b : data) {
                System.out.printf("%02X ", b & 0xFF);
            }

            System.out.println();

            receiver.send(sysex, -1);

            // Give the keyboard time between SysEx messages
            Thread.sleep(100);
        }

        System.out.println("Sequence sent.");

        Thread.sleep(1000);

        receiver.close();
        output.close();
    }

    public static void YamahaNoteTest () throws Exception {
        MidiDevice output = null;

        for (MidiDevice.Info info : MidiSystem.getMidiDeviceInfo()) {

            if (!info.getName().equals("Digital Keyboard-1")) {
                continue;
            }

            MidiDevice device = MidiSystem.getMidiDevice(info);

            if (device.getMaxReceivers() != 0 &&
                    device.getMaxTransmitters() == 0) {

                output = device;
                break;
            }
        }

        if (output == null) {
            throw new Exception("Digital Keyboard-1 output not found.");
        }

        output.open();

        Receiver receiver = output.getReceiver();

        // Middle C, channel 1, velocity 100
        ShortMessage noteOn = new ShortMessage();
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

        System.out.println("NOTE ON");
        receiver.send(noteOn, -1);

        Thread.sleep(1000);

        System.out.println("NOTE OFF");
        receiver.send(noteOff, -1);

        Thread.sleep(500);

        receiver.close();
        output.close();

        System.out.println("Finished.");
    }

    public static void sysexTest () throws Exception {
        MidiDevice device = null;

        for (MidiDevice.Info info : MidiSystem.getMidiDeviceInfo()) {

            if (!info.getName().equals("Digital Keyboard-1")) {
                continue;
            }

            MidiDevice candidate = MidiSystem.getMidiDevice(info);

            if (candidate.getMaxReceivers() != 0 &&
                    candidate.getMaxTransmitters() == 0) {

                device = candidate;
                break;
            }
        }

        if (device == null) {
            throw new Exception("Digital Keyboard-1 output not found.");
        }

        device.open();

        Receiver receiver = device.getReceiver();

        byte[] data = {
                (byte) 0xF0,
                0x43,
                0x50,
                0x00,
                0x05,
                0x0B,
                0x01,
                0x00,
                0x00,
                0x00,
                0x05,
                0x00,
                0x55,
                0x53,
                0x42,
                0x31,
                (byte) 0xF7
        };

        SysexMessage message = new SysexMessage();
        message.setMessage(data, data.length);

        System.out.print("Sending: ");

        for (byte b : data) {
            System.out.printf("%02X ", b & 0xFF);
        }

        System.out.println();

        receiver.send(message, -1);

        Thread.sleep(1000);

        receiver.close();
        device.close();
    }

    public static void identityTest() throws Exception {
        MidiDevice outputDevice = null;
        MidiDevice inputDevice = null;
        Receiver receiver = null;

        // Find the PC -> keyboard output
        MidiDevice.Info[] infos = MidiSystem.getMidiDeviceInfo();

        for (MidiDevice.Info info : infos) {
            MidiDevice device = MidiSystem.getMidiDevice(info);

            if (info.getName().equals("Digital Keyboard-1")
                    && info.getDescription().contains("External MIDI Port")
                    && device.getMaxReceivers() != 0) {

                outputDevice = device;
                break;
            }
        }

        // Find the keyboard -> PC input
        for (MidiDevice.Info info : infos) {
            MidiDevice device = MidiSystem.getMidiDevice(info);

            if (info.getName().equals("Digital Keyboard-1")
                    && info.getDescription().contains("No details available")
                    && device.getMaxTransmitters() != 0) {

                inputDevice = device;
                break;
            }
        }

        if (outputDevice == null) {
            throw new Exception("Could not find MIDI output: Digital Keyboard-1");
        }

        if (inputDevice == null) {
            throw new Exception("Could not find MIDI input: Digital Keyboard-1");
        }

        System.out.println("Opening MIDI devices...");

        outputDevice.open();
        inputDevice.open();

        receiver = outputDevice.getReceiver();

        // Listen for MIDI coming FROM the PSR-SX600
        Transmitter transmitter = inputDevice.getTransmitter();

        transmitter.setReceiver(new Receiver() {

            @Override
            public void send(MidiMessage message, long timeStamp) {

                byte[] bytes = message.getMessage();

                System.out.print("RECEIVED: ");

                for (byte b : bytes) {
                    System.out.printf("%02X ", b & 0xFF);
                }

                System.out.println();
            }

            @Override
            public void close() {
            }
        });

        System.out.println("Sending Yamaha Identity Request...");

        byte[] identityRequest = {
                (byte) 0xF0,
                (byte) 0x7E,
                (byte) 0x7F,
                (byte) 0x06,
                (byte) 0x01,
                (byte) 0xF7
        };

        SysexMessage sysex = new SysexMessage();
        sysex.setMessage(identityRequest, identityRequest.length);

        System.out.print("SENDING: ");

        for (byte b : identityRequest) {
            System.out.printf("%02X ", b & 0xFF);
        }

        System.out.println();

        receiver.send(sysex, -1);

        // Give the keyboard time to answer
        Thread.sleep(3000);

        System.out.println("Finished.");

        receiver.close();
        inputDevice.close();
        outputDevice.close();
    }

    public static void usbTest() throws Exception{
        MidiDevice outputDevice = null;
        MidiDevice inputDevice = null;

        for (MidiDevice.Info info : MidiSystem.getMidiDeviceInfo()) {

            MidiDevice device = MidiSystem.getMidiDevice(info);

            if (info.getName().equals("Digital Keyboard-1")
                    && info.getDescription().contains("External MIDI Port")
                    && device.getMaxReceivers() != 0) {

                outputDevice = device;
            }

            if (info.getName().equals("Digital Keyboard-1")
                    && info.getDescription().contains("No details available")
                    && device.getMaxTransmitters() != 0) {

                inputDevice = device;
            }
        }

        if (outputDevice == null)
            throw new Exception("MIDI output not found");

        if (inputDevice == null)
            throw new Exception("MIDI input not found");

        outputDevice.open();
        inputDevice.open();

        Receiver receiver = outputDevice.getReceiver();

        Transmitter transmitter = inputDevice.getTransmitter();

        transmitter.setReceiver(new Receiver() {

            @Override
            public void send(MidiMessage message, long timeStamp) {

                byte[] data = message.getMessage();

                // Ignore Active Sensing
                if (data.length == 1 && (data[0] & 0xFF) == 0xFE)
                    return;

                System.out.print("RECEIVED: ");

                for (byte b : data) {
                    System.out.printf("%02X ", b & 0xFF);
                }

                System.out.println();
            }

            @Override
            public void close() {
            }
        });

        /*
         * Exact message captured from MS2S:
         *
         * F0 43 50 00 05 0B 01 00 00 00 05 00
         * 55 53 42 31 F7
         *
         * ASCII:
         * USB1
         */

        byte[] usb1 = {
                (byte) 0xF0,
                (byte) 0x43,
                (byte) 0x50,
                (byte) 0x00,
                (byte) 0x05,
                (byte) 0x0B,
                (byte) 0x01,
                (byte) 0x00,
                (byte) 0x00,
                (byte) 0x00,
                (byte) 0x05,
                (byte) 0x00,
                (byte) 0x55,
                (byte) 0x53,
                (byte) 0x42,
                (byte) 0x31,
                (byte) 0xF7
        };

        SysexMessage message = new SysexMessage();
        message.setMessage(usb1, usb1.length);

        System.out.println("UPLOADING:");
        System.out.print("Sending: ");

        for (byte b : usb1) {
            System.out.printf("%02X ", b & 0xFF);
        }

        System.out.println();

        receiver.send(message, -1);

        // Wait for the PSR-SX600 response
        Thread.sleep(3000);

        System.out.println("Finished.");

        receiver.close();
        inputDevice.close();
        outputDevice.close();
    }

    private static Receiver receiver;
    public static void YamahaTransferHandshake() throws Exception {

        MidiDevice output = null;
        MidiDevice input = null;

        for (MidiDevice.Info info : MidiSystem.getMidiDeviceInfo()) {

            MidiDevice device = MidiSystem.getMidiDevice(info);

            if (info.getName().equals("Digital Keyboard-1")
                    && info.getDescription().contains("External MIDI Port")
                    && device.getMaxReceivers() != 0) {
                output = device;
            }

            if (info.getName().equals("Digital Keyboard-1")
                    && info.getDescription().contains("No details available")
                    && device.getMaxTransmitters() != 0) {
                input = device;
            }
        }

        if (output == null || input == null) {
            throw new Exception("Digital Keyboard-1 MIDI ports not found");
        }

        output.open();
        input.open();

        receiver = output.getReceiver();

        Transmitter transmitter = input.getTransmitter();

        transmitter.setReceiver(new Receiver() {

            @Override
            public void send(MidiMessage message, long timeStamp) {

                byte[] data = message.getMessage();

                // Ignore Active Sensing
                if (data.length == 1 && (data[0] & 0xFF) == 0xFE) {
                    return;
                }

                System.out.print("RECEIVED: ");

                for (byte b : data) {
                    System.out.printf("%02X ", b & 0xFF);
                }

                System.out.println();
            }

            @Override
            public void close() {
            }
        });

        System.out.println("Yamaha SX600 transfer handshake test");
        System.out.println();

        // --------------------------------------------------
        // STEP 1
        // --------------------------------------------------

        sendSysEx(new byte[] {
                (byte) 0xF0,
                0x43,
                0x50,
                0x00,
                0x00,
                0x07,
                0x01,
                (byte) 0xF7
        });

        Thread.sleep(1000);

        // --------------------------------------------------
        // STEP 2
        // --------------------------------------------------

        sendSysEx(new byte[] {
                (byte) 0xF0,
                0x43,
                0x50,
                0x00,
                0x00,
                0x01,
                0x00,
                0x00,
                (byte) 0xF7
        });

        Thread.sleep(2000);

        System.out.println();
        System.out.println("Handshake test finished.");

        receiver.close();
        input.close();
        output.close();
    }

    private static void sendSysEx(byte[] data) throws Exception {

        System.out.print("SENDING: ");

        for (byte b : data) {
            System.out.printf("%02X ", b & 0xFF);
        }

        System.out.println();

        SysexMessage message = new SysexMessage();
        message.setMessage(data, data.length);

        receiver.send(message, -1);
    }
}
