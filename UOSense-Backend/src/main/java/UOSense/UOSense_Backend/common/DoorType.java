package UOSense.UOSense_Backend.common;

public enum DoorType implements BaseEnum {
    FRONT("정문"), SIDE("쪽문"), BACK("후문");
    private final String value;

    DoorType(String name) {
        this.value = name;
    }

    @Override
    public String getValue() {
        return value;
    }
}
