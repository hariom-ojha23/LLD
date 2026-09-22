package assignments.behavioral.smartordersystem.chainofresponsibility;

import assignments.behavioral.smartordersystem.enums.SupportRequestType;

public abstract class SupportHandler {
    protected SupportHandler nextHandler;

    public void setNextSupportHandler(SupportHandler nextHandler) {
        this.nextHandler = nextHandler;
    }

    public abstract void handleRequest(SupportRequestType requestType, String query);
}
