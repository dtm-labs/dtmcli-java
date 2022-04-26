package enums;

public enum TransTypeEnum {
    TCC("tcc"),
    XA("xa"),
    MSG("msg"),
    SAGA("saga")
    ;

    TransTypeEnum(String value) {
        this.value = value;
    }

    private String value;

    @Override
    public String toString() {
        return this.value;
    }

    public static TransTypeEnum parseString(String value) {
        for (TransTypeEnum transType : TransTypeEnum.values()) {
            if (transType.value.equals(value)) {
                return transType;
            }
        }
        return null;
    }
}
