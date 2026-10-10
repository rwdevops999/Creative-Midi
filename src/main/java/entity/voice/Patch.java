package entity.voice;

import javafx.beans.property.*;
import lombok.Getter;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

public class Patch implements Serializable {
    private static final long serialVersionUID = 1L; //

    private transient StringProperty patch = new SimpleStringProperty();
    private transient IntegerProperty msb = new SimpleIntegerProperty();
    private transient IntegerProperty lsb = new SimpleIntegerProperty();
    private transient IntegerProperty bank = new SimpleIntegerProperty();
    private transient ObjectProperty<Integer> pc = new SimpleObjectProperty<>();

    @Getter
    private Group parent;

    public Patch() {
    }

    public Patch(Group group, String name, String msb, String lsb, String pc, String provider) {
        this();

        this.parent = group;
        this.patch.set(name);
        this.msb.set(Integer.parseInt(msb));
        this.lsb.set(Integer.parseInt(lsb));
        this.pc.set(Integer.parseInt(pc));
/*        if ("Yamaha".equals(provider)) {
            this.pc.set(Integer.parseInt(pc));
        } */
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

    public String getVoiceInfo() {
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
    public String toString() {
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
    }

    ;

    public String getPath() {
        StringBuilder sb = new StringBuilder();

        if (parent != null) {
            sb.append(getParentGroups(parent));
        }

        return sb.toString();
    }


    private void writeObject(ObjectOutputStream out) throws IOException {
        // 1. Schrijf de niet-transiente velden (zoals long serialVersionUID indien van toepassing)
        out.defaultWriteObject();

        // 2. Schrijf de objecten en de ruwe waarden uit de transiente properties weg
        out.writeObject(parent);               // Schrijft Group object
        out.writeObject(patch.get());          // Schrijft String object
        out.writeInt(msb.get());               // Schrijft primitieve int (efficiënter)
        out.writeInt(lsb.get());               // Schrijft primitieve int
        if (pc.get() == null) {
            out.writeInt(-1);
        } else {
            out.writeInt(pc.get());
        }
    }

    // Aangeroepen door ObjectInputStream achter de schermen
    private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
        // 1. Lees de standaard niet-transiente velden
        in.defaultReadObject();

        // 2. Lees exact in dezelfde volgorde de velden terug!
        this.parent = (Group) in.readObject();  // Cast terug naar Group

        String rawPatch = (String) in.readObject();
        this.patch = new SimpleStringProperty(rawPatch);

        int rawMsb = in.readInt();
        this.msb = new SimpleIntegerProperty(rawMsb);

        int rawLsb = in.readInt();
        this.lsb = new SimpleIntegerProperty(rawLsb);

        int rawPc = in.readInt();
        if (rawPc == -1) {
            this.pc = new SimpleObjectProperty<>(null);
        } else {
            this.pc = new SimpleObjectProperty<>(rawPc);
        }

        // 3. Vergeet niet de bank property opnieuw te berekenen en te initialiseren!
        this.bank = new SimpleIntegerProperty((rawMsb * 128) + rawLsb);
    }}