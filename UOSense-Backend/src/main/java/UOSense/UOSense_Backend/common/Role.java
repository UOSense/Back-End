package UOSense.UOSense_Backend.common;

public enum Role implements BaseEnum {
    USER, ADMIN;
    // DB도 user -> USER (통일)

    /**
     * CustomUserDetails의 getAuthority 메서드에서 String role이 필요해서 추가
     */
    @Override
    public String getValue() {
        return (this == USER) ? "USER" : "ADMIN";
    }
}
