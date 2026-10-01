package creative.scenes.playlist.service;

import creative.panes.monitor.data.CommunicationType;
import creative.scenes.playlist.PlaylistContainer;
import creative.scenes.playlist.consumer.LoadSongConsumer;
import creative.scenes.playlist.entity.SharedEntity;
import creative.scenes.sysex.SysexContainer;
import entity.playlist.Mapping;
import entity.playlist.Song;
import entity.sysex.Sysex;
import entity.sysex.SysexContent;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.ApplicationInfo;

import javax.sound.midi.*;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

public class KeyboardService extends Service<SharedEntity> {
    private static final Logger logger = LoggerFactory.getLogger(KeyboardService.class);

    private static final long FOREVER = Long.MAX_VALUE;

    private static final byte ACTIVE_SENCE = -2;

    private boolean songPlaying = false;
    private String currentSong = null;

    private final Function<Integer, SharedEntity> midiHandler = ((status) -> {
        SharedEntity sharedEntity = null;

        if (status == ShortMessage.START) {
            sharedEntity = new SharedEntity(CommunicationType.INCOMING, "START");
//            logger.error("[CM_KEYBOARD_SERVICE] MIDI = START => SongPlaying = true");
            songPlaying = true;
        } else if (status == ShortMessage.STOP) {
            sharedEntity = new SharedEntity(CommunicationType.INCOMING, "STOP");
//            logger.error("[CM_KEYBOARD_SERVICE] MIDI = STOP => current song = null");
            currentSong = null;
        }

        return sharedEntity;
    });

    private Map<ByteBuffer, List<byte[]>> sysexTosysexMapping = new HashMap<>();
    private Map<ByteBuffer, String> sysexToHumanMapping = new HashMap<>();

    private ByteBuffer mapSysexToBytes(String name, SysexContent content) {
        ByteBuffer bb = ByteBuffer.wrap(content.getData());

        sysexToHumanMapping.put(bb, name);

        return bb;
    }

    private List<byte[]> mapSysexToBytesList(String name, List<SysexContent> contentList) {
        List<byte[]> byteBufferList = new ArrayList<>();

        for (SysexContent sysexContent : contentList) {
            mapSysexToBytes("part of " + name, sysexContent);
            byteBufferList.add(sysexContent.getData());
        }

        return byteBufferList;
    }

    private void setupMappings (List<Mapping> mappings) {
        sysexTosysexMapping = new HashMap<>();
        sysexToHumanMapping = new HashMap<>();

        mappings.forEach((mapping) -> {
            Sysex srcSysex = SysexContainer.getSysex(mapping.getReceive());
            Sysex dstSysex = SysexContainer.getSysex(mapping.getReply());

            ByteBuffer srcBytes = mapSysexToBytes(srcSysex.getName(), srcSysex.getList().get(0));
            List<byte[]> dstBytes = mapSysexToBytesList(dstSysex.getName(), dstSysex.getList());

            sysexTosysexMapping.put(srcBytes, dstBytes);
        });
    }

    private void prepareSong(String songName) {
        Song song = PlaylistContainer.getSong(songName);

        if (song != null) {
            setupMappings(song.getMappings());
        }
    }

    private final Function<String, SharedEntity> checkSongHandler = ((message) -> {
        SharedEntity sharedEntity = null;

        if (message.startsWith("F0 43 73 01 52 26")) {
            logger.error("[CM_KEYBOARD_SERVICE] Received a song name");
            LoadSongConsumer consumer = new LoadSongConsumer();
            String songName = consumer.apply(message);

            if (! songName.equals(currentSong)) {
                logger.error("[CM_KEYBOARD_SERVICE] It's a new song => SongPlaying = false");
                currentSong = songName;
                prepareSong(songName);

                sharedEntity = new SharedEntity(CommunicationType.SONG_SELECT, songName);
                songPlaying = false;
            }
        }

        return sharedEntity;
    });

    @Override
    protected Task<SharedEntity> createTask() {

        return new Task<>() {
            private final MidiDevice midiInputDevice = ApplicationInfo.getInstance().getMidiInputDevice();

            private void handleSysex(SysexMessage msg) {
                ByteBuffer input = ByteBuffer.wrap(msg.getMessage());
            }

            private void handleMidi(ShortMessage msg) {
                byte[] rawBytes = msg.getMessage();

                if (rawBytes[0] != ACTIVE_SENCE) {
                    SharedEntity sharedEntity = midiHandler.apply(msg.getStatus());
                    if (sharedEntity != null) {
                        updateValue(sharedEntity);
                    }
                }
            }

            @Override
            protected SharedEntity call() throws Exception {
                if (! midiInputDevice.isOpen()) {
                    midiInputDevice.open();
                }

                // Setup the receiver for receiving messages from the keyboard
                Receiver receiver = new Receiver() {

                    @Override
                    public void send(MidiMessage message, long timeStamp) {
                        if (message instanceof SysexMessage sysexMessage) {
                            handleSysex(sysexMessage);
                        } else if (message instanceof ShortMessage shortMessage) {
                            handleMidi(shortMessage);
                        }
                    }

                    @Override
                    public void close() {
                    }
                };

                Transmitter transmitter = midiInputDevice.getTransmitter();
                transmitter.setReceiver(receiver);

                while (!isCancelled()) {
                    try {
                        Thread.sleep(FOREVER); // is about 26 million years
                    } catch (InterruptedException e) {
                        if (isCancelled()) {
                            break;
                        }
                    }
                }

                // We get here after the break in the interrupt execution
                // or when the thread awakens
                if (midiInputDevice.isOpen()) {
                    midiInputDevice.close();
                }

                songPlaying = false;

                return null;
            }
        };
    }
}
/*public class KeyboardService extends Service<SharedEntity> {
    private static final Logger logger = LoggerFactory.getLogger(KeyboardService.class);

    private final Map<Object, Communication> communicationInfo = new HashMap<>();
    private final Map<String, ByteBuffer> sysexData = new HashMap<>();

    private final SysExWriter sysexWriter = new SysExWriter();

    private boolean songPlaying = false;

    private final Function<Integer, SharedEntity> midiHandler = ((status) -> {
        SharedEntity sharedEntity = null;

        if (status == ShortMessage.START) {
            sharedEntity = new SharedEntity(CommunicationType.INCOMING, "START");
            logger.error("[CM_KEYBOARD_SERVICE] MIDI = START => SongPlaying = true");
            songPlaying = true;
        } else if (status == ShortMessage.STOP) {
            sharedEntity = new SharedEntity(CommunicationType.INCOMING, "STOP");
            logger.error("[CM_KEYBOARD_SERVICE] MIDI = STOP => current song = null");
            currentSong = null;
        }

        return sharedEntity;
    });

    private String currentSong = null;

    private final Function<String, SharedEntity> checkSongHandler = ((message) -> {
        SharedEntity sharedEntity = null;

        if (message.startsWith("F0 43 73 01 52 26")) {
            logger.error("[CM_KEYBOARD_SERVICE] Received a song name");
            LoadSongConsumer consumer = new LoadSongConsumer();
            String songName = consumer.apply(message);

            if (! songName.equals(currentSong)) {
                logger.error("[CM_KEYBOARD_SERVICE] It's a new song => SongPlaying = false");
                currentSong = songName;
                prepareSong(songName);

                sharedEntity = new SharedEntity(CommunicationType.SONG_SELECT, songName);
                songPlaying = false;
            }
        }

        return sharedEntity;
    });

    public KeyboardService() {
        setupCommunicationInfo ();
        currentSong = null;
    }

    private void setupCommunicationInfo () {
        for (SysEx s : SysExContainer.getSysExs()) {
            if (s.getList().size() == 1) {
                SysExContent content = s.getList().get(0);
                sysexData.put(s.getName(), ByteBuffer.wrap(content.getData()));

                Communication comm = new Communication(s.getName(), content.getContent());
                this.communicationInfo.put(ByteBuffer.wrap(content.getData()), comm);
            }
        }
    }

    private Map<ByteBuffer, ByteBuffer> sysexTosysexMapping = new HashMap<>();

    private void setupMappings (List<Mapping> mappings) {
        sysexTosysexMapping = new HashMap<>();

        mappings.forEach((mapping) -> {
            String src = mapping.getReceive();
            String dst = mapping.getReply();

            ByteBuffer srcBytes = sysexData.get(src);
            ByteBuffer dstBytes = sysexData.get(dst);

            sysexTosysexMapping.put(srcBytes, dstBytes);
        });
    }

    private void prepareSong(String songName) {
        Song song = PlaylistContainer.getSongByName(songName);

        if (song != null) {
            setupMappings(song.getMappings());
        }
    }

    @Override
    protected Task<SharedEntity> createTask() {
        logger.debug("[CM_KEYBOARD_SERVICE] Creating keyboard task");
        return new Task<>() {
            private void setupKeyboardCommunication() {
                midiInputDevice = DirectSingleton.getInstance().getMidiInputDevice();
                openMidiDevice();
            }

            @Override
            protected SharedEntity call() throws Exception {
                Receiver receiver = new Receiver() {
                    @Override
                    public void send(MidiMessage message, long timeStamp) {
                        if (message instanceof SysexMessage sysexMessage) {
                            logger.error("[CM_KEYBOARD_SERVICE] Received SYSEX {}", sysexMessage);
                            ByteBuffer input = ByteBuffer.wrap(sysexMessage.getMessage());
                            Communication inComm = communicationInfo.get(input);
                            if (inComm != null) {
                                logger.error("[CM_KEYBOARD_SERVICE] Mapping for this sysex found");
                                SharedEntity inEntity = new SharedEntity(CommunicationType.INCOMING, inComm.getReadableFormat());
                                updateValue(inEntity);
                                if (songPlaying) {
                                    logger.error("[CM_KEYBOARD_SERVICE] Song is playing ... Execute mapping");
                                    ByteBuffer output = sysexTosysexMapping.get(input);
                                    Communication outComm = communicationInfo.get(output);
                                    if (outComm != null) {
                                        SharedEntity outEntity = new SharedEntity(CommunicationType.OUTGOING, outComm.getReadableFormat());
                                        Platform.runLater(() -> updateValue(outEntity));
                                        sysexWriter.sendSysexData(output.array());
                                    }
                                }
                            } else {
                                SharedEntity sharedEntity = checkSongHandler.apply(SysexToHexStringConvertor.convertToHexString(message.getMessage()));
                                if (sharedEntity != null) {
                                    updateValue(sharedEntity);
                                }
                            }
                        } else {
                            ShortMessage msg = (ShortMessage) message;
                            SharedEntity sharedEntity = midiHandler.apply(msg.getStatus());
                            if (sharedEntity != null) {
                                updateValue(sharedEntity);
                            }
                        }
                    }

                    @Override
                    public void close() {
                        closeMidiDevice();
                    }
                };

                Transmitter transmitter = midiInputDevice.getTransmitter();
                transmitter.setReceiver(receiver);

                // Continuous loop
                while (!isCancelled()) {
                    try {
                        Thread.sleep(Long.MAX_VALUE);
                    } catch (InterruptedException e) {
                        if (isCancelled()) {
                            break;
                        }
                    }
                }

                songPlaying = false;

                // Task cancelled
                receiver.close();

                return null;
            }
        };
    }
}*/
