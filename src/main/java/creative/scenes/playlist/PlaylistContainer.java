package creative.scenes.playlist;

import entity.playlist.Song;
import lombok.Getter;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tools.jackson.core.type.TypeReference;
import tools.jackson.databind.ObjectMapper;
import tools.jackson.databind.annotation.JsonAppend;
import util.properties.PropertyContainer;
import util.properties.PropertyType;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

public class PlaylistContainer {
    private static final Logger logger = LoggerFactory.getLogger(PlaylistContainer.class);

    @Getter
    private static boolean fileLoaded = false;

    @Getter
    private static List<Song> playlist;

    private final static ObjectMapper mapper;

    static {
        mapper = new ObjectMapper();
        playlist = new ArrayList<>();
    }

    public static void loadPlaylist() {
        String playlistPath = PropertyContainer.getPropertyAsString(PropertyType.Path, PropertyContainer.PLAYLIST_PATH, "./playlist");
        String filename = PropertyContainer.getPropertyAsString(PropertyType.Path, PropertyContainer.PLAYLIST_FILE, "playlist.json");

        String playlistfile = playlistPath + "/" + filename;

        logger.debug("[CM_PLAYLIST_CONTAINER] Loading the playlist from file {}", playlistfile);
        try (InputStream in = Files.newInputStream(Path.of(playlistfile))) {
            playlist = mapper.readValue(in, new TypeReference<List<Song>>() {});
            fileLoaded = true;
        } catch (IOException ioe) {
            logger.error("[CM_PLAYLIST_CONTAINER] Exception: CAUSE: {}", ioe.getMessage());
        }
    }

    public static List<String> getSongnamesFromPlaylist() {
        return playlist.stream().sorted(Comparator.comparing(Song::getSongId)).map(Song::getSongName).toList();
    }
}
