package chatroom.snap.snaplive.lists;

import com.stfalcon.chatkit.commons.models.IUser;


/**
 * Created by CodeSlu on 24/01/19.
 */
public class Author implements IUser {


    String name, avatar, id;

    public Author(String name, String avatar, String id) {
        this.name = name;
        this.avatar = avatar;
        this.id = id;
    }
    public Author() {
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
}