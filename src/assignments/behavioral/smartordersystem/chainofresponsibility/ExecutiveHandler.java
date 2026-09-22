package assignments.behavioral.smartordersystem.chainofresponsibility;

import assignments.behavioral.smartordersystem.enums.SupportRequestType;

public class ExecutiveHandler extends SupportHandler {

    @Override
    public void handleRequest(SupportRequestType requestType, String query) {

        if (requestType != SupportRequestType.PAYMENT) {
            nextHandler.handleRequest(requestType, query);
        } else {
            System.out.println("Executive handler: Receives request");
            System.out.println("Executive is handling " + query);
            System.out.println("Executive Handler: Done with request");
        }
    }
}
