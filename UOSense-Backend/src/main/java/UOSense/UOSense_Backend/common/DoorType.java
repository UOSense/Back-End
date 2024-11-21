package UOSense.UOSense_Backend.common;

public enum DoorType {
    FRONT("정문"), SIDE("쪽문"), BACK("후문");
    private final String name;

    DoorType(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public static DoorType fromValue(String value) {
        for (DoorType doorType : values()) {
            if (doorType.getName().equals(value)) {
                return doorType;
            }
        }
        throw new IllegalArgumentException("해당 DoorType은 존재하지 않습니다 : " + value);
    }
}
