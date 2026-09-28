package entity.sysex;

import lombok.Getter;

import java.util.Objects;

public class TypeBlock {
    @Getter
    String Category;
    @Getter
    String Type;
    String Description;
    @Getter
    Byte msb;
    @Getter
    Byte lsb;
    @Getter
    ParameterList parameters;

    public TypeBlock(TypeBlockBuilder builder) {
        this.Category = builder.Category;
        this.Type = builder.Type;
        this.Description = builder.Description;
        this.msb = builder.msb;
        this.lsb = builder.lsb;
        this.parameters = builder.parameters;
    }

    public boolean equals(Object other) {
        if (other != null) {
            return this.getType().equals(((TypeBlock)other).getType());
        }

        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getType());
    }

    public static class TypeBlockBuilder {
        String Category;
        String Type;
        String Description;
        Byte msb;
        Byte lsb;
        ParameterList parameters;

        public TypeBlockBuilder withCategory(String Category) {
            this.Category = Category;
            return this;
        }

        public TypeBlockBuilder withType(String Type) {
            this.Type = Type;
            return this;
        }

        public TypeBlockBuilder withDescription(String Description) {
            this.Description = Description;
            return this;
        }

        public TypeBlockBuilder withMsb(Integer msb) {
            this.msb = msb.byteValue();
            return this;
        }

        public TypeBlockBuilder withLsb(Integer lsb) {
            this.lsb = lsb.byteValue();
            return this;
        }

        public TypeBlockBuilder withMsbAndLsb(Integer msb, Integer lsb) {
            this.msb = msb.byteValue();
            this.lsb = lsb.byteValue();
            return this;
        }
        public TypeBlockBuilder withParameters(ParameterList parameters) {
            this.parameters = parameters;
            return this;
        }

        public TypeBlockBuilder reset() {
            this.Category = null;
            this.Type = null;
            this.Description = null;
            this.msb = null;
            this.lsb = null;
            this.parameters = null;
            return this;
        }

        public TypeBlock build() {
            return new TypeBlock(this);
        }
    }
}
