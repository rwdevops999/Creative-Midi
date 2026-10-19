package util.enums;

public enum Manufacturer {
    YAMAHA("0x43");

    private String manufacturerCode;

    Manufacturer(String code) {
        this.manufacturerCode = code;
    }

    public String getManufacturerCode() {
        return manufacturerCode;
    }
}
