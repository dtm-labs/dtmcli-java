package common;

public enum TransType {
    TCC("tcc");

    TransType(String value) {
        this.value = value;
    }
    private String value;

    public String getValue(){
        return this.value;
    }

}