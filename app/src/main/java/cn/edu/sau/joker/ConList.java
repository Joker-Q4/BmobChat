package cn.edu.sau.joker;

import org.litepal.crud.LitePalSupport;

/**
 * 会话列表的数据
 */
public class ConList extends LitePalSupport {

    private String name;
    private String message;

    ConList(String name, String message) {
        this.name = name;
        this.message = message;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getMessage() {
        return message;
    }
    public void setMessage(String message) {
        this.message = message;
    }
}
