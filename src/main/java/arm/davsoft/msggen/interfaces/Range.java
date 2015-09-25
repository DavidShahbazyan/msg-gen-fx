package arm.davsoft.msggen.interfaces;

/**
 * <b>Author:</b> David Shahbazyan <br/>
 * <b>Date:</b> 7/7/15 <br/>
 * <b>Time:</b> 5:55 PM <br/>
 */
public interface Range<T extends Number> {
    T getFrom();

    T getTo();
}
