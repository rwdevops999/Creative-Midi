package eventhandlers;

import custom.components.ColoredItem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.properties.PropertyContainer;
import util.properties.PropertyType;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;

public class MonitorExportHandler implements IEventHandler {
    private static final Logger logger = LoggerFactory.getLogger(MonitorExportHandler.class);

    @Override
    public void handle(Object o) {
        List<ColoredItem> list = (List<ColoredItem>)o;

        String dir = PropertyContainer.getPropertyAsString(PropertyType.Path, PropertyContainer.EXPORT_PATH, "./export");
        String fileName = "monitor.txt";

        try {
            Path filePath = Paths.get(dir, fileName);
            logger.debug("[CM_MONITOR_EXPORT_HANDLER] Exporting monitor to file {}", filePath.toString());
            Files.createDirectories(filePath.getParent());

            List<String> lines = list.stream().
                    filter(i -> ! i.getText().isEmpty()).
                    map(c -> {
                        return c.getColor().getRenderer().apply(c.getText());
                    }).
                    toList();

            Files.write(filePath, lines);
        } catch (IOException ioe) {
            logger.debug("[CM_MONITOR_EXPORT_HANDLER] EXCEPTION: Error exporting monitor to file CAUSE: {}", ioe.getMessage());
        }
    }
}
