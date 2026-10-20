package creative.scenes.playlist.consumer;

import java.nio.charset.StandardCharsets;
import java.util.HexFormat;
import java.util.function.Function;

public class LoadSongConsumer implements Function<String, String> {
    private static final String SongPrefix = "User:/";
    private static final String SongSuffix = ".S917.RGT";

    private static final int prefixLength = SongPrefix.length() * 2;
    private static final int suffixLength = SongSuffix.length() *2;

    @Override
    public String apply(String sysexMessage) {
        String strippedString = sysexMessage.replaceAll("\\s", "");

        String input = strippedString.substring(20 + prefixLength, strippedString.length() - 4 - suffixLength);

        String songName =new String(HexFormat.of().parseHex(input), StandardCharsets.UTF_8);

        if (songName.endsWith("1") || songName.endsWith("2")) {
            songName = songName.substring(0, songName.length() - 1).trim();
        }

        // Convert hex string to byte array and then
        // convert byte array to readable String
        return songName;
    }
}
