package system;

import entity.voice.Patch;
import javafx.collections.FXCollections;
import javafx.collections.ObservableSet;
import lombok.Getter;
import org.openjdk.jol.vm.VM;
import util.ApplicationInfo;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class SysData extends SysDataBase {
    private static final SysData INSTANCE = new SysData();

    private SysData() {}

    public static SysData getInstance() {
        return INSTANCE;
    }


    // CHUNKS
    @Getter
    private final static ObservableSet<Patch> favorites = FXCollections.observableSet(new HashSet<>());

    // ACCESSORS
    public static void addAsFavorite(Patch patch) {
        favorites.add(patch);
    }

    public static void removeFromFavorites(Patch patch) {
        Patch found = favorites.stream().filter(p -> p.getPatch().equals(patch.getPatch())).findFirst().orElse(null);
        favorites.remove(found);
    }

    public static boolean isFavorite(Patch patch) {
        Patch found = favorites.stream().filter(p -> p.getPatch().equals(patch.getPatch())).findFirst().orElse(null);
        return found != null;
    }

    private void writeHeader(BinaryFileWriter writer) throws Exception {
        byte[] header = {'C', 'M', 'S', 'D'};
        writer.writeBytes(header);
    }

    protected void writeChunk(BinaryFileWriter writer, String name, int size, byte[] data) throws Exception {
        String result = name.substring(0, Math.min(name.length(), size));
        byte[] chunk = result.getBytes(StandardCharsets.UTF_8);

        writer.writeBytes(chunk);
        writer.write32BitInt(data.length);
        writer.writeBytes(data);
    }

    private void writeFavoritesChunk(BinaryFileWriter writer) throws Exception {
        byte[] byteArray;
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream();
             ObjectOutputStream oos = new ObjectOutputStream(baos)) {

            for (Patch patch : favorites) {
                oos.writeObject(patch);
            }
            oos.flush();
            byteArray = baos.toByteArray();
        }

        // Geef de writer mee om de chunk weg te schrijven
        writeChunk(writer, "CM_FAV", 6, byteArray);
    }

    public void save() throws Exception {
        // De try-with-resources opent en sluit de BinaryFileWriter automatisch!
        try (BinaryFileWriter writer = new BinaryFileWriter(DEFAULT_FILENAME)) {

            // 1. Schrijf de hoofdheader [C,M,S,D] via de writer
            writeHeader(writer);

            // 2. Schrijf de favorieten chunk via de writer
            writeFavoritesChunk(writer);
        }
        // Zodra het try-block verlaat, sluit de writer, flusht de buffer,
        // en staat je data gegarandeerd in het bestand.
    }

    @Override
    public void load() throws Exception {
        // Open de reader via try-with-resources
        try (BinaryFileReader reader = new BinaryFileReader(DEFAULT_FILENAME)) {

            // 1. Controleer de hoofdheader [C, M, S, D]
            byte[] expectedHeader = {'C', 'M', 'S', 'D'};
            byte[] actualHeader = reader.readBytes(4);

            if (!Arrays.equals(expectedHeader, actualHeader)) {
                throw new IOException("Ongeldig bestandstype! Header komt niet overeen.");
            }

            // 2. Lees de chunk-naam (6 bytes voor "CM_FAV")
            byte[] chunkNameBytes = reader.readBytes(6);
            String chunkName = new String(chunkNameBytes, StandardCharsets.UTF_8);

            if ("CM_FAV".equals(chunkName)) {
                // 3. Lees de 32-bit lengte van de data
                int dataLength = reader.read32BitInt();

                // 4. Lees de werkelijke byte-data van de chunk
                byte[] chunkData = reader.readBytes(dataLength);

                // 5. Deserialiseer de Patch-objecten uit de byte-array
                try (ByteArrayInputStream bais = new ByteArrayInputStream(chunkData);
                     ObjectInputStream ois = new ObjectInputStream(bais)) {

                    this.favorites.clear();
                    // Blijf lezen tot de byte-array van de chunk leeg is
                    while (bais.available() > 0) {
                        Patch patch = (Patch) ois.readObject();
                        this.favorites.add(patch);
                    }
                }
            } else {
                throw new IOException("Onverwachte chunk gevonden: " + chunkName);
            }
        }
        // De reader sluit hier automatisch, alles is netjes opgeruimd!
        System.out.println("Bestand succesvol geladen! Aantal favorieten: " + favorites.size());
    }
}
