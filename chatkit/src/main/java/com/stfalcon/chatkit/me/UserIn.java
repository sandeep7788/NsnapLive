package com.stfalcon.chatkit.me;

import com.stfalcon.chatkit.commons.models.IUser;

import java.util.Date;

/**
 * Created by mostafa on 28/10/18.
 */

public class UserIn  implements IUser {
    String name, statue, avatar, phone, lastOn,id,lastmessage,lastsender,lastsenderava;
    boolean onstatue,typing,audio,screen;
    long timeC,messDate;
    int noOfUnread;


    public UserIn() {
    }


    public UserIn(String id, String name, String avatar,boolean screen) {
        this.name = name;
        this.avatar = avatar;
        this.id = id;
        this.screen = screen;
    }
    public UserIn(String id, String avatar,boolean screen) {
        this.name = name;
        this.avatar = avatar;
        this.id = id;
        this.screen = screen;
    }

    public UserIn(String name, String statue, String avatar, String phone, String id, String lastmessage, String lastsender, String lastsenderava, long messDate, int noOfUnread,boolean screen) {
        this.name = name;
        this.statue = statue;
        this.avatar = avatar;
        this.phone = phone;
        this.id = id;
        this.lastmessage = lastmessage;
        this.lastsender = lastsender;
        this.lastsenderava = lastsenderava;
        this.messDate = messDate;
        this.noOfUnread = noOfUnread;
        this.screen = screen;
    }

    public boolean isAudio() {
        return audio;
    }

    public void setAudio(boolean audio) {
        this.audio = audio;
    }

    public boolean isTyping() {
        return typing;
    }

    public void setTyping(boolean typing) {
        this.typing = typing;
    }

    public String getLastmessage() {
        return lastmessage;
    }

    public String getLastsenderava() {
        return lastsenderava;
    }

    public void setLastsenderava(String lastsenderava) {
        this.lastsenderava = lastsenderava;
    }

    public void setLastmessage(String lastmessage) {
        this.lastmessage = lastmessage;
    }

    public String getLastsender() {
        return lastsender;
    }

    public void setLastsender(String lastsender) {
        this.lastsender = lastsender;
    }

    public int getNoOfUnread() {
        return noOfUnread;
    }

    public void setNoOfUnread(int noOfUnread) {
        this.noOfUnread = noOfUnread;
    }

    public void setName(String name) {
        this.name = name;
    }


    public String getStatue() {
        return statue;
    }

    public void setStatue(String statue) {
        this.statue = statue;
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


    public void setId(String id) {
        this.id = id;
    }

    public boolean isOnstatue() {
        return onstatue;
    }

    public void setOnstatue(boolean onstatue) {
        onstatue = onstatue;
    }


    public long getTimeC() {
        return timeC;
    }

    public void setTimeC(long timeC) {
        this.timeC = timeC;
    }
    @Override
    public String getId() {
        return id;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getAvatar() {
        return avatar;
    }

    public long getMessDate() {
        return messDate;
    }

    public void setMessDate(long messDate) {
        this.messDate = messDate;
    }

    public boolean isScreen() {
        return screen;
    }

    public void setScreen(boolean screen) {
        this.screen = screen;
    }
}