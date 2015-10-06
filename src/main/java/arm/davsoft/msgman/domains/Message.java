package arm.davsoft.msgman.domains;

/**
 * <b>Author:</b> David Shahbazyan <br/>
 * <b>Date:</b> 6/5/15 <br/>
 * <b>Time:</b> 10:25 PM <br/>
 */
public class Message {
    private Integer id;
    private String text;

    public Message(Integer id) {
        this.id = id;
        this.text = String.valueOf(id);
    }

    public Message(Integer id, String text) {
        this.id = id;
        this.text = text;
    }

    public Integer getId() {
        return id;
    }
    public void setId(Integer id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }
    public void setText(String text) {
        this.text = text;
    }

    public boolean isEmpty() {
        return this.text == null || this.text.equals(this.id.toString());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Message message = (Message) o;

        if (id != null ? !id.equals(message.id) : message.id != null) return false;
        return !(text != null ? !text.equals(message.text) : message.text != null);
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (text != null ? text.hashCode() : 0);
        return result;
    }
}
