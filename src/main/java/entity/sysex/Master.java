package entity.sysex;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * We start with the master which contains the baseSysEx and an addressblock
 */
public class Master extends Root {
    @Getter
    String name;
    @Getter
    List<ParameterChangeTable> parameterChangeTable;
    List<TypeBlock> typeBlocks;

    public TypeBlock findTypeBlockByDefaultValues( int def1, int def2) {
        return typeBlocks.stream().filter(t -> ((t.getMsb() != null && t.getMsb() == def1) && (t.getLsb() != null && t.getLsb() == def2))).findFirst().orElse(null);
    }

    public ParameterChangeTable getDefaultParameterChangeTable() {
        return parameterChangeTable.stream().filter(p -> p.id == 0).findFirst().orElse(null);
    }

    public List<String> getCategories() {
        return typeBlocks.stream().map(TypeBlock::getCategory).distinct().filter(Objects::nonNull).toList();
    }

    public List<TypeBlock> getTypesOfCategory(String category) {
        return typeBlocks.stream().filter(t -> t.getCategory().equals(category)).toList();
    }

    public TypeBlock getDefaultTypeBlock() {
        TypeBlock result = null;

        ParameterChangeTable pct = this.parameterChangeTable.stream().filter(p -> "Type".equals(p.parameter)).findFirst().orElse(null);
        if (pct != null) {
            int msb = pct.defaultValues.b1;
            int lsb = pct.defaultValues.b2;

            result = typeBlocks.stream().filter(t -> t.getMsb() == msb && t.getLsb() == lsb).findFirst().orElse(null);
        }

/*        if (! typeBlocks.isEmpty()) {
            result = typeBlocks.get(0);
        }
*/
        return result;
    }

    public TypeBlock getTypeBlockFromCategoryAndType(String category, String type) {
        return typeBlocks.stream().filter(t -> t.getCategory().equals(category) && t.getType().equals(type)).findFirst().orElse(null);
    }

    public Master(String baseSysEx, String baseAddress) {
        super(baseSysEx, baseAddress);
    }

    public Master(MasterBuilder builder) {
        super(builder.baseSysEx, builder.address);
        this.name = builder.name;
        this.parameterChangeTable = builder.parameterChangeTableList;
        this.typeBlocks = builder.typeBlocks;
    }

    @Override
    public boolean equals(Object other) {
        if (other != null) {
            return this.getName().equals(((Master) other).name);
        }

        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getName());
    }

    public static class MasterBuilder {
        String name;
        String baseSysEx;
        String address;
        List<ParameterChangeTable> parameterChangeTableList;
        List<TypeBlock> typeBlocks;

        public MasterBuilder withName(String name) {
            this.name = name;
            return this;
        }

        public MasterBuilder withBaseSysex(String baseSysEx) {
            this.baseSysEx = baseSysEx;
            return this;
        }

        public MasterBuilder withAddress(String baseAddress) {
            this.address = baseAddress;
            return this;
        }

        public MasterBuilder withParameterChangeTableValue(ParameterChangeTable parameterChangeTable) {
            parameterChangeTableList.add(parameterChangeTable);
            return this;
        };

        public MasterBuilder withTypeBlockValue(TypeBlock typeBlock) {
            typeBlocks.add(typeBlock);
            return this;
        };

        public MasterBuilder reset() {
            this.baseSysEx = null;
            this.address = null;
            this.parameterChangeTableList = new ArrayList<ParameterChangeTable>();
            this.typeBlocks = new ArrayList<TypeBlock>();
            return this;
        }

        public Master build() {
            return new Master(this);
        }
    }
}
