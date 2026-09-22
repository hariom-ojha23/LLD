package assignments.behavioral.smartordersystem.chainofresponsibility;

import assignments.behavioral.smartordersystem.enums.SupportRequestType;

public class ManagerHandler extends SupportHandler {

    @Override
    public void handleRequest(SupportRequestType requestType, String query) {

        if (requestType != SupportRequestType.REFUND) {
            throw new RuntimeException("Ivalid request");
        } else {
            System.out.println("Manager handler: Receives request");
            System.out.println("Manager is handling " + query);
            System.out.println("Manager Handler: Done with request");
        }
    }
}
