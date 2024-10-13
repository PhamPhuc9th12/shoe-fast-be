package org.graduate.shoefastbe.common.enums;

public enum OrderStatusEnum {
    WAIT_ACCEPT("Chờ xác nhận"),
    IS_LOADING("Đang xử lí"),
    IS_DELIVERY("Đang vận chuyển"),
    DELIVERED("Đã giao"),
    CANCELED("Đã hủy");

    private final String value;
    OrderStatusEnum(String value) {
        this.value = value;
    }

    // Getter method to get the value
    public String getValue() {
        return value;
    }
}
