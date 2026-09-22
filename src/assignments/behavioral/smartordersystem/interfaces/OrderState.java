package assignments.behavioral.smartordersystem.interfaces;

public interface OrderState {
    public void confirm();

    public void preparing();

    public void outForDelivery();

    public void delivered();

    public void cancelled();
}
