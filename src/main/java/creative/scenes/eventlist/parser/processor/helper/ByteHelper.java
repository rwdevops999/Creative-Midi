package creative.scenes.eventlist.parser.processor.helper;

public class ByteHelper {
    public static String getAndStrip(StringBuilder src, int numBytes) {
        int size = 0;
        if (numBytes > 1) {
            size = (numBytes * 3) - 1;
        } else {
            size = 2;
        }

        String data = null;
        if (size <= src.length()) {
            data = src.substring(0, size);
            src.delete(0, size+1);
        }

        return data;
    }
}
