package entity.sysex;

import java.util.ArrayList;
import java.util.HexFormat;
import java.util.Iterator;
import java.util.List;

public class Root implements IRoot {
    private static final String SYSEX_END = "F7";
    String baseSysEx;
    String baseAddress;

    public Root(String baseSysEx, String baseAddress) {
        this.baseSysEx = baseSysEx;
        this.baseAddress = baseAddress;
    }

    private void setupSB (StringBuilder sb, Integer position) {
        if (sb.isEmpty()) {
            sb.append(baseSysEx).append(" ").append(baseAddress);
            sb.append(" ").append(HexFormat.of().withUpperCase().toHexDigits(position.byteValue()));
        }
    }

    @Override
    public List<String> generateSysEx(List<InputBlock> inputList) {
        List<String> sysexList = new ArrayList<>();

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

        return sysexList;
    }

    @Override
    public List<String> generateSysEx() {
        List<String> sysexList = new ArrayList<>();

        sysexList.add(baseSysEx + " " + baseAddress);

        return sysexList;
    }
}
