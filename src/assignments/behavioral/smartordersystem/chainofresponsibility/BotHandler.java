package assignments.behavioral.smartordersystem.chainofresponsibility;

import assignments.behavioral.smartordersystem.enums.SupportRequestType;

public class BotHandler extends SupportHandler {

    @Override
    public void handleRequest(SupportRequestType requestType, String query) {

        if (requestType != SupportRequestType.ORDER) {
            nextHandler.handleRequest(requestType, query);
        } else {
            System.out.println("Bot handler: Receives request");
            System.out.println("Bot is handling " + query);
            System.out.println("Bot Handler: Done with request");
        }

    }
}
