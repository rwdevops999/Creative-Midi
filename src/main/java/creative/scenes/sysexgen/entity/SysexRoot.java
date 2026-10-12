package creative.scenes.sysexgen.entity;

import entity.sysex.Root;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

import static util.Util.extend;

public class SysexRoot extends Root {
    @Getter
    private String name;
    @Getter
    private List<SubEvent> subEvents;

    public SysexRoot(String rootSysex) {
        super(rootSysex);
        this.name = null;
        this.subEvents = new ArrayList<>();
    }

    public SysexRoot(SysexRootBuilder builder) {
        this(builder.rootSysex);

        this.name = builder.name;
        this.subEvents.clear();
        if (builder.subEvents != null) {
            this.subEvents.addAll(builder.subEvents);
            this.subEvents.forEach(e -> e.setParent(this));
        }
    }

    public String buildSysex(String nextSysex) {
        String result = "";
        result += extend(SYSEX_START) + extend(baseSysEx) + extend(nextSysex) + SYSEX_END;
        return result;
    }

    public static class SysexRootBuilder {
        String name;
        String rootSysex;
        private List<SubEvent> subEvents;

        public SysexRootBuilder clear() {
            this.name = null;
            this.rootSysex = null;
            if (subEvents != null) {
                subEvents.clear();
                subEvents.clear();
            }
            return this;
        }

        public SysexRootBuilder withName(String value) {
            this.name = value;
            return this;
        }

        public SysexRootBuilder withRootSysex(String value) {
            this.rootSysex = value;
            return this;
        }

        public SysexRootBuilder add(SubEvent subEvent) {
            if (this.subEvents == null) {
                this.subEvents = new ArrayList<>();
            }

            this.subEvents.add(subEvent);

            return this;
        }

        public SysexRoot build() {
            return new SysexRoot(this);
        }

    }
}
