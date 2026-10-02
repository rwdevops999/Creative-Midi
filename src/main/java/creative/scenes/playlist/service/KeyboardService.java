package creative.scenes.playlist.service;

import creative.panes.monitor.data.CommunicationType;
import creative.scenes.playlist.PlaylistContainer;
import creative.scenes.playlist.consumer.LoadSongConsumer;
import creative.scenes.playlist.entity.CommunicationInfo;
import creative.scenes.playlist.entity.SharedEntity;
import creative.scenes.sysex.SysexContainer;
import creative.scenes.sysex.convertor.SysexToHexStringConvertor;
import creative.scenes.sysex.util.SysexWriter;
import entity.playlist.Mapping;
import entity.playlist.Song;
import entity.sysex.Sysex;
import entity.sysex.SysexContent;
import javafx.application.Platform;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.ApplicationInfo;

import javax.sound.midi.*;
import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

public class KeyboardService extends Service<SharedEntity> {
    private static final Logger logger = LoggerFactory.getLogger(KeyboardService.class);

    private final SysexWriter sysexWriter = new SysexWriter();

    private static final long FOREVER = Long.MAX_VALUE;

    private static final byte ACTIVE_SENSING = -2;

    private boolean songPlaying = false;
    private String currentSong = null;

    private Map<ByteBuffer, CommunicationInfo> communicationInfoMap = new HashMap<>();

    private final Function<Integer, SharedEntity> midiHandler = ((status) -> {
        SharedEntity sharedEntity = null;

        if (status == ShortMessage.START) {
            sharedEntity = new SharedEntity(CommunicationType.INCOMING, "START");
            songPlaying = true;
        } else if (status == ShortMessage.STOP) {
            sharedEntity = new SharedEntity(CommunicationType.INCOMING, "STOP");
            currentSong = null;
        }

        return sharedEntity;
    });

    private ByteBuffer mapSysexToBytes(SysexContent content) {
        return ByteBuffer.wrap(content.getData());
    }

    private void setupMappings (List<Mapping> mappings) {
        communicationInfoMap = new HashMap<>();

        mappings.forEach((mapping) -> {
            Sysex srcSysex = SysexContainer.getSysex(mapping.getReceive());
            Sysex dstSysex = SysexContainer.getSysex(mapping.getReply());

            ByteBuffer srcBytes = mapSysexToBytes(srcSysex.getList().get(0));

            CommunicationInfo ci = new CommunicationInfo(mapping.getReceive());
            String name = dstSysex.getList().size() > 1 ? "part of " : "";
            for (SysexContent content : dstSysex.getList()) {
                CommunicationInfo sysexCi = new CommunicationInfo(name + dstSysex.getName());
                sysexCi.setData(content.getData());

                ci.getReplies().add(sysexCi);
            }

            communicationInfoMap.put(srcBytes, ci);
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
                CommunicationInfo ci = communicationInfoMap.get(input);
                if (ci != null) {
                    Platform.runLater(() -> {
                        SharedEntity inEntity = new SharedEntity(CommunicationType.INCOMING, ci.getName());
                        // TODO Send to Monitor INBOUND ci.getName();
                        updateValue(inEntity);
                    });

                    if (songPlaying) {
                        for (CommunicationInfo communicationInfo : ci.getReplies()) {
                            Platform.runLater(() -> {
                                SharedEntity outEntity = new SharedEntity(CommunicationType.OUTGOING, communicationInfo.getName());
                                updateValue(outEntity);
                            });
                            sysexWriter.sendSysex(communicationInfo.getData());
                        }
                    }
                } else {
                    SharedEntity sharedEntity = checkSongHandler.apply(SysexToHexStringConvertor.convertToHexString(msg.getMessage()));
                    if (sharedEntity != null) {
                        Platform.runLater(() -> updateValue(sharedEntity));
                    }
                }
            }

            private void handleMidi(ShortMessage msg) {
                byte[] rawBytes = msg.getMessage();

                if (rawBytes[0] != ACTIVE_SENSING) {
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

                // Set up the receiver for receiving messages from the keyboard
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
