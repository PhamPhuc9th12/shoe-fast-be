package org.graduate.shoefastbe.common.enums;

public enum OfferStatusEnum {
    WAITING_FOR_APPROVAL("WAITING_FOR_APPROVAL"),
    REJECTED("REJECTED"),
    APPROVED("APPROVED"),
    WAITING_FOR_RESPONSE("WAITING_FOR_RESPONSE"),
    ACCEPTED("ACCEPTED"),
    DECLINED("DECLINED"),
    CANCELLED("CANCELLED");

    OfferStatusEnum(String waitingForApproval) {
    }
}
