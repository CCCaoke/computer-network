package bean;

import java.io.Serializable;
import java.util.Date;

public class Item implements Serializable {
    private Date date;
    private String note;

    public Item(Date date, String note) {
        this.date = date;
        this.note = note;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date time) {
        this.date = time;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }
}
