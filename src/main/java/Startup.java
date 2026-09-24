import creative.CreativeApp;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.core.config.Configurator;
import org.fusesource.jansi.AnsiConsole;
import util.ApplicationInfo;

import java.net.URL;
import java.util.Locale;

import static util.constants.*;

public class Startup {
    static {
        try {
            URL configUrl = Thread.currentThread().getContextClassLoader().getResource("log4j2.xml");
            if (configUrl != null) {
                // Forceer de initialisatie direct bij het allereerste JVM-contact
                Configurator.initialize("MyAppCtx", configUrl.toURI().toString());
            } else {
                Configurator.setRootLevel(org.apache.logging.log4j.Level.OFF);
            }
        } catch (Exception e) {
            Configurator.setRootLevel(org.apache.logging.log4j.Level.OFF);
        }
    }

    /**
     * Main startup application:
     *
     * @param args: additional arguments
     *            -T = test mode
     */
    public static void main(String[] args) {
        AnsiConsole.systemInstall();

        // To Switch logging off
        System.out.println(ANSI_RED + "Running Creative Midi with '-h' displays help" + ANSI_RESET);
        for (String arg : args) {
            if (HELP.equals(arg.toLowerCase(Locale.ROOT))) {
                System.out.println("Use application with arguments");
                System.out.println("-h this screen");
                System.out.println("-t run in test mode");
                System.out.println("-l switch logging on");

                AnsiConsole.systemUninstall();
                System.exit(0);
            }

            if (TEST.equals(arg.toLowerCase(Locale.ROOT))) {
                System.out.println("Activated TEST mode");
            }

            if (DEBUG.equals(arg.toLowerCase(Locale.ROOT))) {
                ApplicationInfo.getInstance().setDebugging(true);
            }

            if (LOGGING.equals(arg.toLowerCase(Locale.ROOT))) {
                System.out.println(ANSI_RED + "LOGGING ON" + ANSI_RESET);
                Configurator.setRootLevel(Level.ALL);
            }
        }

        CreativeApp.startup(args);

        AnsiConsole.systemUninstall();
    }
}
