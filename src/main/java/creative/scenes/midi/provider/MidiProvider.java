package creative.scenes.midi.provider;

import entity.midi.Midi;
import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tools.jackson.core.json.JsonReadFeature;
import tools.jackson.core.type.TypeReference;
import tools.jackson.databind.ObjectMapper;
import tools.jackson.databind.SerializationFeature;
import tools.jackson.databind.json.JsonMapper;
import util.properties.PropertyContainer;
import util.properties.PropertyType;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicBoolean;

public class MidiProvider {
    private static final Logger logger = LoggerFactory.getLogger(MidiProvider.class);

    private static final ObjectMapper mapper = JsonMapper.builder()
            .enable(JsonReadFeature.ALLOW_JAVA_COMMENTS)
            .build();

    @Getter
    private String midiFileName = new String("");

    @Getter
    private List<Midi> midis = new ArrayList<>();

    public MidiProvider(String filename) {
        super();

        midiFileName = filename;

        String midiPath = PropertyContainer.getPropertyAsString(PropertyType.Path, PropertyContainer.MIDI_PATH, "./midi");
        String midiEventsFile = PropertyContainer.getPropertyAsString(PropertyType.Path, PropertyContainer.MIDI_EVENTS_FILE, midiFileName);

        String midiEventsFilePath = midiPath + "/" + midiEventsFile;

        try (InputStream in = Files.newInputStream(Path.of(midiEventsFilePath))) {
            midis = mapper.readValue(in, new TypeReference<>() {});
        } catch (IOException ioe) {
            logger.error("[CM_MIDI_PROVIDER] Exception. CAUSE: {}", ioe.getMessage());
        }
    }

    public void deleteMidi(Midi midi) {
        midis.remove(midi);
    }

    public boolean exportMidis() {
        AtomicBoolean result = new AtomicBoolean(false);

        String midiPath = PropertyContainer.getPropertyAsString(PropertyType.Path, PropertyContainer.MIDI_PATH, "./midi" );
        String filename = PropertyContainer.getPropertyAsString(PropertyType.Path, PropertyContainer.MIDI_EVENTS_FILE, "midievents.json" );

        String midifile = midiPath + "/" + filename;

        ObjectMapper mapperExport = JsonMapper.builder()
                .enable(SerializationFeature.INDENT_OUTPUT)
                .build();

        try {
            File file = new File(midifile);
            mapperExport.writeValue(file, midis);
            result.set(true);
        } catch (Exception e) {
            logger.error("[CM_MIDI_PROVIDER] Exception. CAUSE: {}", e.getMessage());
            result.set(false);
        }

        return result.get();
    }

    public boolean constainsMidi(String midiName) {
        return midis.stream().anyMatch(obj -> midiName.equals(obj.getName()));
    }

    public void addMidi(Midi midi) {
        midis.add(midi);
    }

    public void updateMidi(Midi midi) {
        Optional<Midi> foundMidi = midis.stream().filter(item -> item.getName().equals(midi.getName())).findFirst();
        if (foundMidi.isPresent()) {
            int index = midis.indexOf(foundMidi.get());
            midis.set(index, midi);
        }
    }
}
