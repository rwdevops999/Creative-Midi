package creative.scenes.eventlist.data;

import entity.sysex.Sysex;

import java.util.*;

public class EventKey {
    private static List<String> keys = new ArrayList<>();

    private List<String> values = new ArrayList<>();

    // General keys
    public static String MIDI                  ="MIDI";
    public static String META                  ="META";
    public static String SYSEX                 ="SYSEX";

    // midi keys
    public static String MIDI_CHANNEL_MESSAGE           =   "MIDI_CHANNEL_MESSAGE";
    public static String MIDI_NOTE_OFF                  =   "MIDI_NOTE_OFF";
    public static String MIDI_NOTE_ON                   =   "MIDI_NOTE_ON";
    public static String MIDI_CONTROL_MODE_CHANGE       =   "MIDI_CONTROL_MODE_CHANGE";
    public static String MIDI_CONTROL_CHANGE            =   "MIDI_CONTROL_CHANGE";
    public static String MIDI_MODE_CHANGE               =   "MIDI_MODE_CHANGE";
    public static String MIDI_ALL_SOUND_OFF             =   "MIDI_ALL_SOUND_OFF";
    public static String MIDI_RESET_ALL_CONTROLLERS     =   "MIDI_RESET_ALL_CONTROLLERS";
    public static String MIDI_LOCAL_CONTROL             =   "MIDI_LOCAL_CONTROL";
    public static String MIDI_ALL_NOTE_OFF              =   "MIDI_ALL_NOTE_OFF";
    public static String MIDI_OMNI_OFF                  =   "MIDI_OMNI_OFF";
    public static String MIDI_OMNI_ON                   =   "MIDI_OMNI_ON";
    public static String MIDI_MONO                      =   "MIDI_MONO";
    public static String MIDI_POLY                      =   "MIDI_POLY";
    public static String MIDI_BANK_SELECT_MSB           =   "MIDI_BANK_SELECT_MSB";
    public static String MIDI_MODULATION                =   "MIDI_MODULATION";
    public static String MIDI_BREATH                    =   "MIDI_BREATH";
    public static String MIDI_PORTAMENTO_TIME           =   "MIDI_PORTAMENTO_TIME";
    public static String MIDI_DATA_ENTRY_MSB            =   "MIDI_DATA_ENTRY_MSB";
    public static String MIDI_MAIN_VOLUME               =   "MIDI_MAIN_VOLUME";
    public static String MIDI_PANPOT                    =   "MIDI_PANPOT";
    public static String MIDI_EXPRESSION                =   "MIDI_EXPRESSION";
    public static String MIDI_GENERAL_PURPOSE           =   "MIDI_GENERAL_PURPOSE";
    public static String MIDI_BANK_SELECT_LSB           =   "MIDI_BANK_SELECT_LSB";
    public static String MIDI_DATA_ENTRY_LSB            =   "MIDI_DATA_ENTRY_LSB";
    public static String MIDI_SUSTAIN_DAMPER            =   "MIDI_SUSTAIN_DAMPER";
    public static String MIDI_PORTAMENTO                =   "MIDI_PORTAMENTO";
    public static String MIDI_SOSTENUTO                 =   "MIDI_SOSTENUTO";
    public static String MIDI_SOFT_PEDAL                =   "MIDI_SOFT_PEDAL";
    public static String MIDI_RESONANCE                 =   "MIDI_RESONANCE";
    public static String MIDI_RELEASE_TIME              =   "MIDI_RELEASE_TIME";
    public static String MIDI_ATTACK_TIME               =   "MIDI_ATTACK_TIME";
    public static String MIDI_CUTOFF                    =   "MIDI_CUTOFF";
    public static String MIDI_DECAY_TIME                =   "MIDI_DECAY_TIME";
    public static String MIDI_VIBRATO_RATE              =   "MIDI_VIBRATO_RATE";
    public static String MIDI_VIBRATO_DEPTH             =   "MIDI_VIBRATO_DEPTH";
    public static String MIDI_VIBRATO_DELAY             =   "MIDI_VIBRATO_DELAY";
    public static String MIDI_ARTICULATION_1            =   "MIDI_ARTICULATION_1";
    public static String MIDI_ARTICULATION_2            =   "MIDI_ARTICULATION_2";
    public static String MIDI_ARTICULATION_3            =   "MIDI_ARTICULATION_3";
    public static String MIDI_PORTAMENTO_CONTROL        =   "MIDI_PORTAMENTO_CONTROL";
    public static String MIDI_REVERB_SEND_LEVEL         =   "MIDI_REVERB_SEND_LEVEL";
    public static String MIDI_CHORUS_SEND_LEVEL         =   "MIDI_CHORUS_SEND_LEVEL";
    public static String MIDI_VARIATION_SEND_LEVEL      =   "MIDI_VARIATION_SEND_LEVEL";
    public static String MIDI_RPN_INCREMENT             =   "MIDI_RPN_INCREMENT";
    public static String MIDI_RPN_DECREMENT             =   "MIDI_RPN_DECREMENT";
    public static String MIDI_NRPN_LSB                  =   "MIDI_NRPN_LSB";
    public static String MIDI_NRPN_MSB                  =   "MIDI_NRPN_MSB";
    public static String MIDI_RPN_LSB                   =   "MIDI_RPN_LSB";
    public static String MIDI_RPN_MSB                   =   "MIDI_RPN_MSB";
    public static String MIDI_PROGRAM_CHANGE            =   "MIDI_PROGRAM_CHANGE";
    public static String MIDI_CHANNEL_AFTERTOUCH        =   "MIDI_CHANNEL_AFTERTOUCH";
    public static String MIDI_KEY_AFTERTOUCH            =   "MIDI_KEY_AFTERTOUCH";
    public static String MIDI_PITCH_BEND                =   "MIDI_PITCH_BEND";
    public static String MIDI_CLOCK                     =   "MIDI_CLOCK";
    public static String MIDI_START                     =   "MIDI_START";
    public static String MIDI_CONTINUE                  =   "MIDI_CONTINUE";
    public static String MIDI_STOP                      =   "MIDI_STOP";
    public static String MIDI_ACTIVE_SENSE              =   "MIDI_ACTIVE_SENSE";
    public static String MIDI_SYSTEM_RESET              =   "MIDI_SYSTEM_RESET";

    // meta keys
    public static String META_TEXT             ="META_TEXT";
    public static String META_COPYRIGHT        ="META_COPYRIGHT";
    public static String META_TRACK_NAME       ="META_TRACK_NAME";
    public static String META_INSTRUMENT_NAME  ="META_INSTRUMENT_NAME";
    public static String META_LYRIC            ="META_LYRIC";
    public static String META_MARKER           ="META_MARKER";
    public static String META_CUE_POINT        ="META_CUE_POINT";
    public static String META_MIDI_PORT        ="META_MIDI_PORT";
    public static String META_TEMPO            ="META_TEMPO";
    public static String META_SMPTE_OFFET      ="META_SMPTE_OFFET";
    public static String META_TIME_SIGNATURE   ="META_TIME_SIGNATURE";
    public static String META_KEY_SIGNATURE    ="META_KEY_SIGNATURE";
    public static String META_SEQUENCER        ="META_SEQUENCER";

    // sysex keys

    static {
        keys.add(MIDI);
        keys.add(META);
        keys.add(SYSEX);

        keys.add(MIDI_CHANNEL_MESSAGE);
        keys.add(MIDI_NOTE_OFF);
        keys.add(MIDI_NOTE_ON);
        keys.add(MIDI_CONTROL_CHANGE);
        keys.add(MIDI_MODE_CHANGE);
        keys.add(MIDI_ALL_SOUND_OFF);
        keys.add(MIDI_RESET_ALL_CONTROLLERS);
        keys.add(MIDI_LOCAL_CONTROL);
        keys.add(MIDI_ALL_NOTE_OFF);
        keys.add(MIDI_OMNI_OFF);
        keys.add(MIDI_OMNI_ON);
        keys.add(MIDI_MONO);
        keys.add(MIDI_POLY);
        keys.add(MIDI_BANK_SELECT_MSB);
        keys.add(MIDI_MODULATION);
        keys.add(MIDI_BREATH);
        keys.add(MIDI_PORTAMENTO_TIME);
        keys.add(MIDI_DATA_ENTRY_MSB);
        keys.add(MIDI_MAIN_VOLUME);
        keys.add(MIDI_PANPOT);
        keys.add(MIDI_EXPRESSION);
        keys.add(MIDI_GENERAL_PURPOSE);
        keys.add(MIDI_BANK_SELECT_LSB);
        keys.add(MIDI_DATA_ENTRY_LSB);
        keys.add(MIDI_SUSTAIN_DAMPER);
        keys.add(MIDI_PORTAMENTO);
        keys.add(MIDI_SOSTENUTO);
        keys.add(MIDI_SOFT_PEDAL);
        keys.add(MIDI_RESONANCE);
        keys.add(MIDI_RELEASE_TIME);
        keys.add(MIDI_ATTACK_TIME);
        keys.add(MIDI_CUTOFF);
        keys.add(MIDI_DECAY_TIME);
        keys.add(MIDI_VIBRATO_RATE);
        keys.add(MIDI_VIBRATO_DEPTH);
        keys.add(MIDI_VIBRATO_DELAY);
        keys.add(MIDI_ARTICULATION_1);
        keys.add(MIDI_ARTICULATION_2);
        keys.add(MIDI_ARTICULATION_3);
        keys.add(MIDI_PORTAMENTO_CONTROL);
        keys.add(MIDI_REVERB_SEND_LEVEL);
        keys.add(MIDI_CHORUS_SEND_LEVEL);
        keys.add(MIDI_VARIATION_SEND_LEVEL);
        keys.add(MIDI_RPN_INCREMENT);
        keys.add(MIDI_RPN_DECREMENT);
        keys.add(MIDI_NRPN_LSB);
        keys.add(MIDI_NRPN_MSB);
        keys.add(MIDI_RPN_LSB);
        keys.add(MIDI_RPN_MSB);
        keys.add(MIDI_PROGRAM_CHANGE);
        keys.add(MIDI_CHANNEL_AFTERTOUCH);
        keys.add(MIDI_KEY_AFTERTOUCH);
        keys.add(MIDI_PITCH_BEND);
        keys.add(MIDI_CLOCK);
        keys.add(MIDI_START);
        keys.add(MIDI_CONTINUE);
        keys.add(MIDI_STOP);
        keys.add(MIDI_ACTIVE_SENSE);
        keys.add(MIDI_SYSTEM_RESET);

        keys.add(META_TEXT);
        keys.add(META_COPYRIGHT);
        keys.add(META_TRACK_NAME);
        keys.add(META_INSTRUMENT_NAME);
        keys.add(META_LYRIC);
        keys.add(META_MARKER);
        keys.add(META_CUE_POINT);
        keys.add(META_MIDI_PORT);
        keys.add(META_TEMPO);
        keys.add(META_SMPTE_OFFET);
        keys.add(META_TIME_SIGNATURE);
        keys.add(META_KEY_SIGNATURE);
        keys.add(META_SEQUENCER);
    }

    public EventKey(String ...values) {
        Collections.addAll(this.values, values);
    }

    public boolean hasValue(String target) {
        return this.values.contains(target);
    }

    public void addValue(String value) {
        if (! hasValue(value)) {
            this.values.add(value);
        }
    }

    public String getFirstElement() {
        return this.values.get(0);
    }

    public String getLastElement() {
        return this.values.get(this.values.size() - 1);
    }

    public static List<String> getEvents(String type) {
        return keys.stream().filter(k -> (k.startsWith(type) && !k.equals(type))).toList();
    }
}
