package creative.scenes.midi.provider;

import entity.midi.Midi;
import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tools.jackson.core.json.JsonReadFeature;
import tools.jackson.core.type.TypeReference;
import tools.jackson.databind.ObjectMapper;
import tools.jackson.databind.json.JsonMapper;
import util.properties.PropertyContainer;
import util.properties.PropertyType;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class MidiProvider {
    private static final Logger logger = LoggerFactory.getLogger(MidiProvider.class);

    private static final ObjectMapper mapper = JsonMapper.builder()
            .enable(JsonReadFeature.ALLOW_JAVA_COMMENTS)
            .build();

    @Getter
    private String midiFileName = new String("");

    @Getter
    private static List<Midi> midis = new ArrayList<>();

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
}
