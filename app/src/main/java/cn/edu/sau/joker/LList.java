package cn.edu.sau.joker;


import org.litepal.crud.LitePalSupport;

/**
 * 好友列表
 */
public class LList extends LitePalSupport {

    private String friends;

    LList(String friends) {
        this.friends = friends;
    }
    public String getFriends() {
        return friends;
    }
    public void setFriends(String friends) {
        this.friends = friends;
    }
}
