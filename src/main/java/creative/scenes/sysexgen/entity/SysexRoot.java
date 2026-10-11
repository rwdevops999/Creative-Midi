package creative.scenes.sysexgen.entity;

import entity.sysex.Root;

import java.util.ArrayList;
import java.util.List;

public class SysexRoot extends Root {
    private List<SubEvent> subEvents;

    public SysexRoot(String rootSysex) {
        super(rootSysex);
        this.subEvents = new ArrayList<>();
    }

    public SysexRoot(SysexRootBuilder builder) {
        this(builder.rootSysex);

        this.subEvents.addAll(builder.subEvents);
        this.subEvents.forEach(e -> e.setParent(this));
    }

    public static class SysexRootBuilder {
        String name;
        String rootSysex;
        private List<SubEvent> subEvents;

        public SysexRootBuilder clear() {
            this.name = null;
            this.rootSysex = null;
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
