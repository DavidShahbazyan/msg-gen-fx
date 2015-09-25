package arm.davsoft.msggen.domains;

import arm.davsoft.msggen.interfaces.Range;

/**
 * <b>Author:</b> David Shahbazyan <br/>
 * <b>Date:</b> 7/7/15 <br/>
 * <b>Time:</b> 5:57 PM <br/>
 */
public class IntegerRange implements Range<Integer> {
    private final Integer from;
    private final Integer to;

    public IntegerRange(Integer from, Integer to) {
        this.from = from;
        this.to = to;
    }

    public Integer getFrom() {
        return this.from;
    }

    public Integer getTo() {
        return this.to;
    }
}
