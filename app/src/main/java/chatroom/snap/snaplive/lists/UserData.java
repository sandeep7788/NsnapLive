package chatroom.snap.snaplive.lists;

/**
 * Created by CodeSlu on 28/10/18.
 */

public class UserData  {
    private  String name, statue, avatar, phone, lastOn,id,device;
    private boolean online,screen;
    private long time,timeC;

    public String getDevice() {
        return device;
    }

    public void setDevice(String device) {
        this.device = device;
    }

    public UserData() {
    }

    public UserData(String id,String ava, String name,String statue, String phone,boolean online,boolean screen,String nameL) {
        this.name = name;
        this.name = name;
        this.statue = statue;
        this.phone = phone;
        this.id = id;
        this.avatar = ava;
        this.online = online;
        this.screen = screen;

    }



    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }


    public String getStatue() {
        return statue;
    }

    public void setStatue(String statue) {
        this.statue = statue;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getLastOn() {
        return lastOn;
    }

    public void setLastOn(String lastOn) {
        this.lastOn = lastOn;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public boolean isOnline() {
        return online;
    }

    public void setOnline(boolean online) {
        this.online = online;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }
    public long getTimeC() {
        return timeC;
    }

    public void setTimeC(long timeC) {
        this.timeC = timeC;
    }

    public boolean isScreen() {
        return screen;
    }

    public void setScreen(boolean screen) {
        this.screen = screen;
    }
}