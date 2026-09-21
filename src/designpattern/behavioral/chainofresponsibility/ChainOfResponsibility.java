package designpattern.behavioral.chainofresponsibility;

/**
 * 
 * ChainOfResponsibility
 * 
 * Chain of Responsibility pattern passes a request through
 * a chain of handlers, where each handler either can process the request
 * or pass it to the next handler in the chain.
 * 
 * 
 * Main Idea:
 * Give multiple objects a chance to process request without
 * sender knowing which object will handle it.
 * 
 * 
 * When to use:
 * - When multiple objects potentially can handle the same request
 * - To decouple the sender from the receiver
 * - To add fallback
 * - The sender should not need to know which handler will process the request
 * 
 */

/**
 * 
 * Approver
 * 
 * Approver HAS-A Approver
 * 
 * It is a self-referencing composition
 * 
 */
abstract class Approver {
    protected Approver nextApprover;

    public void setNext(Approver approver) {
        this.nextApprover = approver;
    }

    public abstract void approveRequest(double amount);
}

/**
 * 
 * TeamLead
 * 
 * TeamLead IS-A Approver
 * 
 */
class TeamLead extends Approver {
    @Override
    public void approveRequest(double amount) {
        if (amount < 10000) {
            System.out.println("Team lead approved $" + amount);
        } else {
            nextApprover.approveRequest(amount);
        }
    }
}

/**
 * 
 * Manager
 * 
 * Manager IS-A Approver
 * 
 */
class Manager extends Approver {
    @Override
    public void approveRequest(double amount) {
        if (amount < 50000) {
            System.out.println("Manager approved $" + amount);
        } else {
            nextApprover.approveRequest(amount);
        }
    }
}

/**
 * 
 * Director
 * 
 * Director IS-A Approver
 * 
 */
class Director extends Approver {
    @Override
    public void approveRequest(double amount) {
        if (amount < 100000) {
            System.out.println("Director approved $" + amount);
        } else {
            throw new RuntimeException("Request denied: Amount $" + amount + " is too large");
        }
    }
}

public class ChainOfResponsibility {
    public static void main(String[] args) {
        Approver teamLead = new TeamLead();
        Approver manager = new Manager();
        Approver director = new Director();

        teamLead.setNext(manager);
        manager.setNext(director);

        try {
            teamLead.approveRequest(5000);
            teamLead.approveRequest(20000);
            teamLead.approveRequest(80000);
            teamLead.approveRequest(150000);
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }

    }
}
