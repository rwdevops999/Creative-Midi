package entity.voice;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import lombok.Getter;

public class Patch {
    private SimpleStringProperty patch = new SimpleStringProperty();
    private SimpleIntegerProperty msb = new SimpleIntegerProperty();
    private SimpleIntegerProperty lsb = new SimpleIntegerProperty();
    private SimpleIntegerProperty bank = new SimpleIntegerProperty();
    private SimpleIntegerProperty pc = new SimpleIntegerProperty();

    @Getter
    private Group parent;

    public Patch() {
    }

    public Patch (Group group, String name, String msb, String lsb, String pc, String provider) {
        this();

        this.parent = group;
        this.patch.set(name);
        this.msb.set(Integer.parseInt(msb));
        this.lsb.set(Integer.parseInt(lsb));
        this.pc.set(Integer.parseInt(pc));
        if ("Yamaha".equals(provider)) {
            this.pc.set(Integer.parseInt(pc) + 1);
        }
        this.bank.set((this.msb.get() * 128) + this.lsb.get());
    }

    public String getPatch() {
        return patch.get();
    }
    public Integer getMsb() {
        return msb.get();
    }
    public Integer getLsb() {
        return lsb.get();
    }
    public Integer getBank() {
        return bank.get();
    }
    public Integer getPc() {
        return pc.get();
    }

    public void setPatch(String patch) {
        this.patch.set(patch);
    }
    public void setMsb(Integer msb) {
        this.msb.set(msb);
    }
    public void setLsb(Integer lsb) {
        this.lsb.set(lsb);
    }
    public void setBank(Integer bank) {
        this.bank.set(bank);
    }
    public void setPc(Integer pc) {
        this.pc.set(pc);
    }

    public String getVoiceInfo () {
        StringBuilder sb = new StringBuilder();

        sb.append(patch)
                .append("[")
                .append("BANK=").append(bank)
                .append(", MSB=").append(msb)
                .append(", LSB=").append(lsb)
                .append(", PC=").append(pc)
                .append("]");

        return sb.toString();
    }

    @Override
    public String toString () {
        StringBuilder sb = new StringBuilder();

        sb.append(patch)
                .append("[")
                .append("BANK=").append(bank)
                .append(", MSB=").append(msb)
                .append(", LSB=").append(lsb)
                .append(", PC=").append(pc)
                .append("]");

        return sb.toString();
    }

    private String getParentGroups(Group group) {
        StringBuilder result = new StringBuilder();

        if (group != null) {
            result.append(getParentGroups(group.getParent())).append("/").append(group.getGroupName());
        }

        return result.toString();
    };

    public String getPath() {
        StringBuilder sb = new StringBuilder();

        if (parent != null) {
            sb.append(getParentGroups(parent));
        }

        return sb.toString();
    }
}
