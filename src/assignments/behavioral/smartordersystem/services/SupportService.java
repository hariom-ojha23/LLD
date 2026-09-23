package assignments.behavioral.smartordersystem.services;

import assignments.behavioral.smartordersystem.chainofresponsibility.BotHandler;
import assignments.behavioral.smartordersystem.chainofresponsibility.ExecutiveHandler;
import assignments.behavioral.smartordersystem.chainofresponsibility.ManagerHandler;
import assignments.behavioral.smartordersystem.chainofresponsibility.SupportHandler;
import assignments.behavioral.smartordersystem.enums.SupportRequestType;

public class SupportService {
    private final SupportHandler firstHandler;

    public SupportService() {
        /**
         * Support request
         */
        SupportHandler botHandler = new BotHandler();
        SupportHandler managerHandler = new ManagerHandler();
        SupportHandler executivHandler = new ExecutiveHandler();

        /**
         * Setting handlers chain
         */
        botHandler.setNextSupportHandler(executivHandler);
        executivHandler.setNextSupportHandler(managerHandler);

        this.firstHandler = botHandler;
    }

    /**
     * Handle support request
     * 
     * @param type    type of support request
     * @param message message to be sent
     */
    public void handleRequest(SupportRequestType type, String message) {
        firstHandler.handleRequest(type, message);
    }
}
