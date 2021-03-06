package arm.davsoft.msgman.domains;

import arm.davsoft.msgman.interfaces.Range;

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

    @Override
    public Integer getFrom() {
        return this.from;
    }

    @Override
    public Integer getTo() {
        return this.to;
    }

    @Override
    public boolean isEmpty() {
        return this.from == null || this.to == null;
    }

    @Override
    public boolean isValid() {
        return !isEmpty() && from.compareTo(to) <= 0;
    }

    @Override
    public String toString() {
        return "[" + this.from + ',' + this.to + ']';
    }
}
