package chatroom.snap.snaplive.custom;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.view.Window;

import chatroom.snap.snaplive.R;

/**
 * Created by CodeSlu on 05/03/19.
 */


public class AttachMenu extends Dialog {

    private Activity c;
    public Dialog d;

    public AttachMenu(Activity a) {
        super(a);
        this.c = a;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.attach_menu);
    }
}