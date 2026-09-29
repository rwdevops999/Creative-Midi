package entity.sysex;

import com.fasterxml.jackson.annotation.JsonIgnore;
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
            String cleanHex = content.replace(" ", "");

            // Convert to byte array
            return HexFormat.of().parseHex(cleanHex);
        }

        return new byte[0];
    }

    public void setContent(String content) {
        this.content = content;
        this.data = convertSysex();
    }

    @JsonIgnore
    public byte[] getData() {
        return data;
    }
}
