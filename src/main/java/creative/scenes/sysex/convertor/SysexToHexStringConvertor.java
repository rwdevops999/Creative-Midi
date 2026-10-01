package creative.scenes.sysex.convertor;

public class SysexToHexStringConvertor {
    public static String convertToHexString(byte[] data) {
        StringBuilder sb = new StringBuilder();
        for (byte b : data) {
            sb.append(String.format("%02x ", b));
        }

        return sb.toString().trim().toUpperCase();
    }
}
