package system;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

public abstract class SysDataBase {
    protected final static String DEFAULT_FILENAME = "./system/sysdata.dat";

    public abstract void save() throws Exception;
    public abstract void load() throws Exception;

    protected void display() {
        System.out.println("DISPLAY");
    }

    protected static BinaryFileWriter writer;
    protected static BinaryFileReader reader;
}
