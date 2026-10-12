package entity.sysex;

import creative.scenes.sysexgen.data.ParameterType;

import java.util.ArrayList;
import java.util.HexFormat;
import java.util.Iterator;
import java.util.List;

import static util.Util.extend;

public class Root implements IRoot {
    protected static final String SYSEX_START = "F0";
    protected static final String SYSEX_END = "F7";

    protected String baseSysEx;

    public Root(String baseSysEx) {
        this.baseSysEx = baseSysEx;
    }

    @Override
    public List<String> generateSysEx(String sysex, List<InputBlock> inputList) {
        List<String> sysexList = new ArrayList<>();

        for (InputBlock inputBlock : inputList) {
            if (ParameterType.INLINE.equals(inputBlock.getParameterType())) {
                // Embedded sysex
                String rootSysex = extend(SYSEX_START) + extend(this.baseSysEx) + extend(sysex) + extend(inputBlock.getValues().asString(inputBlock.getNumbytes())) + SYSEX_END;
                sysexList.add(rootSysex);
            }
        }

        System.out.println("GENERATING SYSEXLIST");
/*
        Iterator<InputBlock> iterator = inputList.iterator();

        boolean addedValues = false;

        StringBuilder sb = new StringBuilder();
        while (iterator.hasNext()) {
            InputBlock inputBlock = iterator.next();

            setupSB(sb, inputBlock.getPosition());
            if (inputBlock.getInputValues() != null) {
                sb.append(" ").append(HexFormat.of().withUpperCase().toHexDigits(inputBlock.getInputValues().getB1()));
                if (inputBlock.getNumBytes() > 1) {
                    sb.append(" ").append(HexFormat.of().withUpperCase().toHexDigits(inputBlock.getInputValues().getB2()));
                }
                addedValues = true;
            } else {
                if (addedValues) {
                    sb.append(" ").append(SYSEX_END);
                    sysexList.add(sb.toString());
                }

                sb = new StringBuilder();
                addedValues = false;
            }
        }

        if (addedValues) {
            sb.append(" ").append(SYSEX_END);
            sysexList.add(sb.toString());
        }
*/
        return sysexList;
    }
}
