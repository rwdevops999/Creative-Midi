package creative.scenes.sysex;

import entity.sysex.Sysex;
import entity.sysex.SysexContent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

import static util.Constants.SYSEX_START_BYTE;
import static util.Constants.SYSEX_STOP_BYTE;

public class SysexContainer {
    private static final Logger logger = LoggerFactory.getLogger(SysexContainer.class);

    private final static ObjectMapper mapper;
    private static List<Sysex> sysexList;

    static {
        mapper = new ObjectMapper();
        sysexList = new ArrayList<>();
    }

    public static void loadSysexEvents() {
        String sysexPath = PropertyContainer.getPropertyAsString(PropertyType.Path, PropertyContainer.SYSEX_PATH, "./sysex");
        String filename = PropertyContainer.getPropertyAsString(PropertyType.Path, PropertyContainer.SYSEX_EVENTS_FILE, "sysexevents.json");

        String sysexfile = sysexPath + "/" + filename;

        logger.debug("[CM_SYSEX_CONTAINER] Loading SysEx events from {}", sysexfile);
        try (InputStream in = Files.newInputStream(Path.of(sysexfile))) {
            sysexList = mapper.readValue(in, new TypeReference<List<Sysex>>() {});
        } catch (IOException ioe) {
            logger.error("[CM_SYSEX_CONTAINER] Exception. CAUSE: {}", ioe.getMessage());
        }
    }

    private static String completeSysex(String sysex) {
        String result = sysex;

        if (sysex != null) {
            if (!sysex.isEmpty()) {
                if (!sysex.startsWith(SYSEX_START_BYTE)) {
                    result = SYSEX_START_BYTE + " " + sysex;
                }

                if (!sysex.endsWith(SYSEX_STOP_BYTE)) {
                    result += " " + SYSEX_STOP_BYTE;
                }

                return result;
            }
        }

        return null;
    }

    public static void addSysex(Sysex sysex) {
        for (SysexContent sysExContent : sysex.getList()) {
            sysExContent.setContent(completeSysex(sysExContent.getContent()));
        }

        sysexList.add(sysex);
    }

    public static void updateSysex(Sysex sysex) {
        for (SysexContent sysExContent : sysex.getList()) {
            sysExContent.setContent(completeSysex(sysExContent.getContent()));
        }

        sysexList.removeIf(s -> s.getName().equals(sysex.getName()));
        sysexList.add(sysex);
    }

    public static boolean containsSysex(String name) {
        return sysexList.stream().anyMatch(s -> s.getName().equals(name));
    }

    public static void deleteSysex(Sysex sysex) {
        sysexList.removeIf(s -> s.getName().equals(sysex.getName()));
    }

    private static void removeEmptyContent() {
        for (Sysex sysex : sysexList) {
            if (! sysex.getList().isEmpty()) {
                sysex.setList(sysex.getList().stream().filter(c -> (c.getContent() != null) && (! c.getContent().isEmpty())).toList());
            }
        }
    }

    public static boolean exportSysEx() {
        removeEmptyContent();
        boolean result = true;

        String sysexPath = PropertyContainer.getPropertyAsString(PropertyType.Path, PropertyContainer.SYSEX_PATH, "./sysex");
        String filename = PropertyContainer.getPropertyAsString(PropertyType.Path, PropertyContainer.SYSEX_EVENTS_FILE, "sysexevents.json");

        String sysexfile = sysexPath + "/" + filename;
        logger.debug("[CM_SYSEX_CONTAINER] Exporting SYSEX messages to {}", sysexfile);

        ObjectMapper mapperExport = JsonMapper.builder()
                .enable(SerializationFeature.INDENT_OUTPUT)
                .build();

        try {
            File file = new File(sysexfile);
            mapperExport.writeValue(file, sysexList);
        } catch (Exception e) {
            logger.error("[CM_SYSEX_CONTAINER] Exeption: CAUSE: {}", e.getMessage());
            result = false;
        }

        return result;
    }

    public static List<Sysex> getSysexList() {
        return sysexList;
    }
}
