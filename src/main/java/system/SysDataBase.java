package system;

public abstract class SysDataBase {
    public abstract void save();
    public abstract void load();

    protected void display() {
        System.out.println("DISPLAY");
    }
}
