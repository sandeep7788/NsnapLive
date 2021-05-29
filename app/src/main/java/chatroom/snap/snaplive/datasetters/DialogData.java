package chatroom.snap.snaplive.datasetters;


import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import chatroom.snap.snaplive.global.Global;
import chatroom.snap.snaplive.global.encryption;
import chatroom.snap.snaplive.models.DefaultDialog;
import com.stfalcon.chatkit.me.Message;
import com.stfalcon.chatkit.me.UserIn;


/**
 * Created by CodeSlu on 29/01/19.
 */


public final class DialogData {

    static long timeStamp;

    static ArrayList<UserIn> userList;

    public DialogData() {
        throw new AssertionError();
    }

    public  static ArrayList<DefaultDialog> getDialogs(ArrayList<UserIn> arrayList) {
        userList = new ArrayList<>();
        userList = arrayList;
        arrange();

        ArrayList<DefaultDialog> chats = new ArrayList<>();
        if(chats.size() != 0)
            chats.clear();
        for (int i = 0; i < userList.size(); i++) {
            Calendar calendar = Calendar.getInstance();
             timeStamp = userList.get(i).getMessDate();
            calendar.setTimeInMillis(timeStamp);
            chats.add(getDialog(i,calendar.getTime()));
        }

        return chats;
    }

    //put data in the list
    private static DefaultDialog getDialog(int i,Date time) {
        if(userList.get(i).getNoOfUnread() > 99)
        {

            return new DefaultDialog(
                    userList.get(i).getId(),userList.get(i).getName(),userList.get(i).getAvatar(),userList,getMessage(i,time),99,userList.get(i).getPhone(),userList.get(i).isScreen());

        }
        else
        {

            return new DefaultDialog(
                    userList.get(i).getId(),userList.get(i).getName(),userList.get(i).getAvatar(),userList,getMessage(i,time),userList.get(i).getNoOfUnread(),userList.get(i).getPhone(),userList.get(i).isScreen());

        }
    }
    //get last sender
    private static UserIn getUser(int i) {
           return new UserIn(
                   userList.get(i).getLastsender(), userList.get(i).getName(),userList.get(i).getLastsenderava(),userList.get(i).isScreen());

    }
    //get last message
    private static Message getMessage(int i,final Date date) {
        return new Message(
                userList.get(i).getLastsender(),getUser(i), encryption.decryptOrNull(userList.get(i).getLastmessage()),date,Global.avaLocal,true,false,false,"");

    }
    private static void arrange()
    {
        UserIn temp;
        for(int i=0;i<userList.size();i++) {
            if (i != userList.size() - 1)
            {
                if (userList.get(i).getMessDate() < userList.get(i + 1).getMessDate()) {
                    temp = userList.get(i);
                    userList.set(i, userList.get(i + 1));
                    userList.set(i + 1, temp);
                    arrange();
                    break;
                }
        }

        }
    }


}