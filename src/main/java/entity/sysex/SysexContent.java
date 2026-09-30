package entity.sysex;

import com.fasterxml.jackson.annotation.JsonIgnore;
import communication.CommunicationModel;
import lombok.Getter;
import lombok.Setter;

import java.util.HexFormat;

@Getter
@Setter
public class SysexContent {
    private String content;      // status byte (in hex)
    private byte[] data;

    public SysexContent() {
    }

    public SysexContent(String content) {
        setContent(content);
    }

    private byte[] convertSysex() {
        if (content != null) {
            if (content.contains(" ")) {
                String cleanHex = content.replace(" ", "");

                byte[] bytes = new byte[2];
                try {
                    return HexFormat.of().parseHex(cleanHex);
                } catch (Exception e) {
                }

                // Convert to byte array
                return bytes;
            }
        }

        return new byte[0];
    }

    /**
     * ATTENTION: data can contain [0,0] if the input string is not even
     *
     * @param content
     */
    public void setContent(String content) {
        this.content = content;
        this.data = convertSysex();
    }

    @JsonIgnore
    public byte[] getData() {
        return data;
    }
}
