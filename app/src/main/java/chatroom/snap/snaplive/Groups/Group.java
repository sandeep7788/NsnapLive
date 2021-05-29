package chatroom.snap.snaplive.Groups;

import android.Manifest;
import android.app.Activity;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.media.AudioManager;
import android.media.MediaMetadataRetriever;
import android.media.MediaRecorder;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.devlomi.record_view.OnBasketAnimationEnd;
import com.devlomi.record_view.OnRecordListener;
import com.devlomi.record_view.RecordButton;
import com.devlomi.record_view.RecordView;
import com.downloader.PRDownloader;
import com.downloader.PRDownloaderConfig;
import com.fxn.pix.Options;
import com.fxn.pix.Pix;
import com.fxn.utility.ImageQuality;
import com.fxn.utility.PermUtil;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.jaiselrahman.filepicker.activity.FilePickerActivity;
import com.jaiselrahman.filepicker.config.Configurations;
import com.jaiselrahman.filepicker.model.MediaFile;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.rosberry.mediapicker.MediaPicker;
import com.rosberry.mediapicker.data.MediaResult;
import com.rosberry.mediapicker.data.PhotoParams;
import com.sandrios.sandriosCamera.internal.SandriosCamera;
import com.sandrios.sandriosCamera.internal.configuration.CameraConfiguration;
import com.sandrios.sandriosCamera.internal.ui.model.Media;
import com.squareup.picasso.Picasso;
import com.stfalcon.chatkit.commons.ImageLoader;
import com.stfalcon.chatkit.me.GroupIn;
import com.stfalcon.chatkit.me.Message;
import com.stfalcon.chatkit.me.MessageIn;
import com.stfalcon.chatkit.messages.MessageHolders;
import com.stfalcon.chatkit.messages.MessagesList;
import com.stfalcon.chatkit.messages.MessagesListAdapter;
import com.stfalcon.chatkit.utils.DateFormatter;
import com.vanniktech.emoji.EmojiEditText;
import com.vanniktech.emoji.EmojiPopup;
import com.vanniktech.emoji.EmojiTextView;
import com.vincent.filepicker.Constant;
import com.vincent.filepicker.activity.AudioPickActivity;
import com.vincent.filepicker.filter.entity.AudioFile;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

import chatroom.snap.snaplive.Forward;
import chatroom.snap.snaplive.Notification.NotificationModel;
import chatroom.snap.snaplive.Notification.NotifyManager;
import chatroom.snap.snaplive.R;
import chatroom.snap.snaplive.custom.AttachMenu;
import chatroom.snap.snaplive.datasetters.MessageData;
import chatroom.snap.snaplive.fragments.Groups;
import chatroom.snap.snaplive.global.AppBack;
import chatroom.snap.snaplive.global.Global;
import chatroom.snap.snaplive.global.encryption;
import chatroom.snap.snaplive.holders.CustomIncomingImageMessageViewHolder;
import chatroom.snap.snaplive.holders.CustomOutcomingImageMessageViewHolder;
import chatroom.snap.snaplive.holders.IncomeHolder;
import chatroom.snap.snaplive.holders.IncomeOther;
import chatroom.snap.snaplive.holders.OutcomeHolder;
import chatroom.snap.snaplive.holders.OutcomeOther;
import chatroom.snap.snaplive.lists.Tokens;
import chatroom.snap.snaplive.lists.UserData;
import chatroom.snap.snaplive.lists.myD;
import chatroom.snap.snaplive.notify.FCM;
import chatroom.snap.snaplive.story.StickerBSFragment;
import de.hdodenhof.circleimageview.CircleImageView;
import id.zelory.compressor.Compressor;
import im.delight.android.location.SimpleLocation;
import me.leolin.shortcutbadger.ShortcutBadger;
import safety.com.br.android_shake_detector.core.ShakeCallback;
import safety.com.br.android_shake_detector.core.ShakeDetector;
import safety.com.br.android_shake_detector.core.ShakeOptions;


import static com.vincent.filepicker.activity.AudioPickActivity.IS_NEED_RECORDER;
import static com.vincent.filepicker.activity.BaseActivity.IS_NEED_FOLDER_LIST;

/**
 * Created by CodeSlu
 */
public class Group extends AppCompatActivity
        implements
        MessagesListAdapter.OnLoadMoreListener, StickerBSFragment.StickerListener, MediaPicker.OnMediaListener {

    //input
    static FirebaseAuth mAuth;
    String friendId = "";
    DatabaseReference type;
    DatabaseReference mData, myData;
    ArrayList<UserData> mylist;
    GroupIn userData;
    myD data;
    //output
    protected ImageLoader imageLoader;
    protected static MessagesListAdapter<Message> messagesAdapter;
    private static final int TOTAL_MESSAGES_COUNT = 0;
    private int selectionCount;
    private Date lastLoadedDate;
    private MessagesList messagesList;
    DatabaseReference mDataget, mdatagetme, disc;
    MessagesListAdapter.HoldersConfig holdersConfig;
    //view
    RelativeLayout ly;

    ImageView add, send, emoji, bg, sticker;
    private RecordButton voice;
    RecordView recordView;
    EmojiEditText message;
    private LinearLayout messagebox, overdark;
    private boolean isHidden = true;
    ImageButton btnI, btnV, btnVideo, btnF, btnL, btnS;
    private Bitmap compressedImageFile;
    //typing
    private Timer mActivityTransitionTimer;
    private TimerTask mActivityTransitionTimerTask;
    private final long MAX_ACTIVITY_TRANSITION_TIME_MS = 1300;
    boolean typingR, recordingR = false;
    int code = 0;
    //voice record
    private MediaRecorder mRecorder;
    private long recordTime = 0;
    private File mOutputFile;
    //map
    private SimpleLocation location;
    //all
    Object currTime;
    String messidL = "", encrypM = "", encrypI = "", encrypL = "", encrypV = "", encrypF = "", encrypVideo = "", encrypMap = "";
    //Types id
    byte Voiceid = 2;
    //toolbar
    Toolbar toolbar;
    CircleImageView ava;
    EmojiTextView name, state;
    //check online
    DatabaseReference mUserDB, getAva, kick;
    //shared pref
    SharedPreferences preferences;
    SharedPreferences.Editor editor;
    //fcm
    FCM fcm;
    //thumb
    Bitmap thumb;
    //real time
    int TIMEUPDATE = 60 * 1000;
    Runnable runnable;
    //exist
    ImageView imm, downdown;
    //get Message query
    Query query;
    //local message
    MessageIn messageLocal;
    //data query lists
    ArrayList<String> fileA, imageA;
    int iq = 0;
    boolean iqb = true;
    //dialog
    AttachMenu cdd;
    CircleImageView attachmenuP;
    LinearLayout reactD;
    //screenshot
    boolean screen = true;
    //record delete on pause
    private boolean pausebreak = false;
    Boolean canScroll = false;
    //vars
    String filetype = "";
    String whoT = "", whoR = "";
    int getAvasCount = 0;
    boolean toast = true;
    //events
    ChildEventListener child;
    //handler
    Handler mHandler;
    boolean isRunning = true;
    boolean prevstate = true;
    LinearLayout connectE;
    boolean open = true;

    private StickerBSFragment mStickerBSFragment;
    private MediaPicker mediaPicker;
    PhotoParams params;
    //reply
    static Message replyM;
    static String messidNew;
    static boolean replyBollean = false;
    static LinearLayout replyLy;
    static EmojiTextView replyU, replyT;
    static ImageView replyClose;
    private ShakeDetector shakeDetector;
    static long duration = Global.SHAKE_UNDO_TIMEOUT * 1000;
    static long tick = 1000;
    static CountDownTimer counter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Global.chatactivity = this;
        Global.audiolist = new ArrayList<>();
        Global.btnid = new ArrayList<>();
        setContentView(R.layout.activity_group);
        Global.conA = this;
        Global.currentactivity = this;
        Global.stickerIcon = true;
        Global.currAva = "";
        Global.currname = "";
        //media picker
        mediaPicker = MediaPicker.from(this).to(this);

        params = new PhotoParams.Builder()
                .type(MediaPicker.Type.VIDEO)
                .duration(5)
                .facing(false)
                .highQuality(true)
                .build();
        //firebase
        mAuth = FirebaseAuth.getInstance();
        mData = FirebaseDatabase.getInstance().getReference(Global.GROUPS);
        type = FirebaseDatabase.getInstance().getReference(Global.GROUPS);
        mdatagetme = FirebaseDatabase.getInstance().getReference(Global.USERS);
        mUserDB = FirebaseDatabase.getInstance().getReference().child(Global.USERS);
        getAva = FirebaseDatabase.getInstance().getReference().child(Global.USERS);
        disc = FirebaseDatabase.getInstance().getReference(Global.GROUPS);
        //init Arrays
        fileA = new ArrayList<>();
        imageA = new ArrayList<>();
        //local db
        ly = findViewById(R.id.lyC);
        add = findViewById(R.id.attachmentButton);
        voice = findViewById(R.id.voice);
        send = findViewById(R.id.send);
        imm = findViewById(R.id.imm);
        bg = findViewById(R.id.bg);
        connectE = findViewById(R.id.connectE);
        emoji = findViewById(R.id.emoji);
        sticker = findViewById(R.id.sticker);
        messagebox = findViewById(R.id.messagebox);
        messagesList = (MessagesList) findViewById(R.id.messagesList);
        recordView = (RecordView) findViewById(R.id.record_view);
        message = findViewById(R.id.messageInput);
        overdark = findViewById(R.id.overdark);
        downdown = findViewById(R.id.downdown);
        downdown.setVisibility(View.GONE);
        overdark.setVisibility(View.GONE);

        mStickerBSFragment = new StickerBSFragment();
        mStickerBSFragment.setStickerListener(this);

        replyLy = findViewById(R.id.reply);
        replyT = findViewById(R.id.replyT);
        replyU = findViewById(R.id.replyU);
        replyClose = findViewById(R.id.closeR);
        replyLy.setVisibility(View.GONE);
        replyClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                replyBollean = false;
                replyLy.setVisibility(View.GONE);
            }
        });

//set Wallpapers
        if (!((AppBack) getApplication()).shared().getString("wall", "no").equals("no")) {
            String pathW = ((AppBack) getApplication()).shared().getString("wall", "no");
            File f = new File(getRealPathFromURI(Uri.parse(pathW)));
            Drawable d = Drawable.createFromPath(f.getAbsolutePath());
            ly.setBackground(d);
        }
        //clear all notifications
        NotificationManager notificationManager = (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);
        try {
            if (notificationManager != null) {
                notificationManager.cancelAll();
                int count = 0;
                //store it again
                ((AppBack) getApplication()).editSharePrefs().putInt("numN" + mAuth.getCurrentUser().getUid(), count);
                ((AppBack) getApplication()).editSharePrefs().apply();
                ShortcutBadger.applyCount(this, count);
            }
        } catch (NullPointerException e) {
            //nothing
        }

        //attach mnu init
        cdd = new AttachMenu(this);
        cdd.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        cdd.getWindow().getAttributes().windowAnimations = R.style.CustomDialogAnimation;
        cdd.show();
        //menu inti
        btnI = cdd.findViewById(R.id.gallery);
        btnV = cdd.findViewById(R.id.audio);
        btnS = cdd.findViewById(R.id.snap);
        btnL = cdd.findViewById(R.id.location);
        btnVideo = cdd.findViewById(R.id.video);
        btnF = cdd.findViewById(R.id.file);
        attachmenuP = cdd.findViewById(R.id.profileAttach);
        reactD = cdd.findViewById(R.id.reactD);
        cdd.dismiss();
        isHidden = true;

        prevstate = Global.check_int(this);

        if (Global.check_int(this))
            connectE.setVisibility(View.GONE);
        else
            connectE.setVisibility(View.VISIBLE);

        mHandler = new Handler();

        new Thread(new Runnable() {
            @Override
            public void run() {
                // TODO Auto-generated method stub
                while (isRunning) {
                    try {
                        Thread.sleep(500);
                        mHandler.post(new Runnable() {

                            @Override
                            public void run() {
                                if (prevstate != Global.check_int(Group.this)) {
                                    if (!Global.check_int(Group.this))
                                        connectE.setVisibility(View.VISIBLE);
                                    else
                                        connectE.setVisibility(View.GONE);

                                    prevstate = Global.check_int(Group.this);
                                }

                            }
                        });
                    } catch (Exception e) {
                    }
                }
            }
        }).start();

        DisplayMetrics displaymetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        int imagewidth = (int) Math.round(displaymetrics.widthPixels * 0.99);
        int imageheight = (int) Math.round(displaymetrics.heightPixels);

        //dark mode init
        if (mAuth.getCurrentUser() != null) {
            if (!((AppBack) getApplication()).shared().getBoolean("dark" + mAuth.getCurrentUser().getUid(), false)) {
                getDelegate().setLocalNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                message.setBackground(getResources().getDrawable(R.drawable.round_w));
                messagebox.setBackground(getResources().getDrawable(R.drawable.round_w));
                message.setTextColor(getResources().getColor(R.color.black));
                recordView.setBackground(getResources().getDrawable(R.drawable.round_w));
                add.setBackground(getResources().getDrawable(R.drawable.circle));
                downdown.setBackground(getResources().getDrawable(R.drawable.circle));
                imm.setBackground(getResources().getDrawable(R.drawable.circle));
                reactD.setBackground(getResources().getDrawable(R.drawable.react_bg));
                replyLy.setBackground(getResources().getDrawable(R.drawable.reply_bg_w));

                Global.DARKSTATE = false;
//wallpaper
                overdark.setVisibility(View.GONE);
                if (((AppBack) getApplication()).shared().getString("wall", "no").equals("no"))
                    bg.setImageResource(R.drawable.bg2);


                else {
                    Uri pathW = Uri.parse(((AppBack) getApplication()).shared().getString("wall", "no"));
                    File f = new File(getRealPathFromURI(pathW));
                    if (f.exists()) {
                        Picasso.get()
                                .load(pathW)
                                .resize(imagewidth, imageheight)
                                .error(R.drawable.bg2)
                                .into(bg);

                    } else
                        bg.setImageResource(R.drawable.bg2);


                }
            } else {
                getDelegate().setLocalNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                message.setBackground(getResources().getDrawable(R.drawable.round_d));
                messagebox.setBackground(getResources().getDrawable(R.drawable.round_d));
                message.setTextColor(getResources().getColor(R.color.white));
                recordView.setBackground(getResources().getDrawable(R.drawable.round_d));
                add.setBackground(getResources().getDrawable(R.drawable.circle_d));
                downdown.setBackground(getResources().getDrawable(R.drawable.circle_d));
                imm.setBackground(getResources().getDrawable(R.drawable.circle_d));
                reactD.setBackground(getResources().getDrawable(R.drawable.react_bg_d));
                replyLy.setBackground(getResources().getDrawable(R.drawable.reply_bg_d));

                Global.DARKSTATE = true;
                //wallpaper
                overdark.setVisibility(View.VISIBLE);
                if (((AppBack) getApplication()).shared().getString("wall", "no").equals("no")) {
                    bg.setImageResource(R.drawable.bg3);
                    overdark.setVisibility(View.GONE);
                } else {
                    Uri pathW = Uri.parse(((AppBack) getApplication()).shared().getString("wall", "no"));
                    File f = new File(getRealPathFromURI(pathW));
                    if (f.exists()) {
                        Picasso.get()
                                .load(pathW)
                                .resize(imagewidth, imageheight)
                                .error(R.drawable.bg3)
                                .into(bg);
                    } else {
                        bg.setImageResource(R.drawable.bg3);
                        overdark.setVisibility(View.GONE);
                    }


                }
            }
        }


        //fcm notify
        fcm = Global.getFCMservies();
        Global.currentactivity = this;
        //online checker
        ((AppBack) this.getApplication()).startOnline();
//toolbar
        //Actionbar init
        toolbar = (Toolbar) findViewById(R.id.chatbar);
        toolbar.setPadding(0, 0, 0, 0);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setDisplayShowCustomEnabled(true);
        //Action bar design
        LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View viewS = inflater.inflate(R.layout.group_bar, null);
        actionBar.setCustomView(viewS);
        name = viewS.findViewById(R.id.nameC);
        state = viewS.findViewById(R.id.stateC);
        ava = viewS.findViewById(R.id.avaC);
//emoji keyboard
        final EmojiPopup emojiPopup = EmojiPopup.Builder.fromRootView(ly).build(message);

//downloader
        // Enabling database for resume support even after the application is killed:
        PRDownloaderConfig config = PRDownloaderConfig.newBuilder()
                .setDatabaseEnabled(true)
                .build();
        PRDownloader.initialize(getApplicationContext(), config);

        voice.setRecordView(recordView);
        if (getIntent() != null) {
            Intent intent = getIntent();
            friendId = intent.getExtras().getString("id");
            Global.currentpageid = friendId;


            //set users
            ((AppBack) getApplication()).getGroupUsers(friendId);
            ((AppBack) getApplication()).getGroupUsersAva(friendId);
            ((AppBack) getApplication()).getGroupAdmins(friendId);

//remove user if kiked
            kick = FirebaseDatabase.getInstance().getReference(Global.USERS).child(mAuth.getCurrentUser().getUid()).child(Global.GROUPS).child(friendId);
            Query query22 = kick.orderByChild("messDate");
            query22.keepSynced(true);
            query22.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (mAuth.getCurrentUser() != null) {

                        if (!dataSnapshot.exists()) {
                            if (toast) {
                                toast = false;
                                Groups.refreshL(friendId);
                                ((AppBack) getApplication()).getdialogdbG(mAuth.getCurrentUser().getUid());
                                ((AppBack) getApplication()).getchatsdbG(friendId);

                                Global.messGGG.clear();
                                deletebyId(friendId);

                                //local store
                                ((AppBack) getApplication()).setchatsdbG(friendId);
                                ((AppBack) getApplication()).setdialogdbG(mAuth.getCurrentUser().getUid());
                                Toast.makeText(Group.this, getString(R.string.kicked), Toast.LENGTH_SHORT).show();
                                finish();
                            }
                        }
                    }

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
            code = intent.getExtras().getInt("ccode");
            mDataget = FirebaseDatabase.getInstance().getReference(Global.GROUPS).child(friendId);
            Query query = mDataget.orderByChild("messDate");
            query.keepSynced(true);
            query.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (mAuth.getCurrentUser() != null) {
                        try {
                            ((AppBack) getApplication()).getGroupUsers(friendId);
                            ((AppBack) getApplication()).getGroupUsersAva(friendId);
                            ((AppBack) getApplication()).getGroupAdmins(friendId);

                            Global.currGAdmins.clear();
                            Global.currGUsers.clear();
                            userData = dataSnapshot.getValue(GroupIn.class);
                            Global.currname = userData.getName();
                            Global.currAva = encryption.decryptOrNull(userData.getAvatar());
                            Global.currGUsers = userData.getUsers();
                            Global.currGAdmins = userData.getAdmins();
                            ((AppBack) getApplication()).setGroupUsers(friendId);
                            ((AppBack) getApplication()).setGroupAdmins(friendId);
                            Global.currGUsersAva.clear();
                            if (code == 0) {
                                zeroCount();
                                readM();
                            }
                            getAvas();
                            editInf();
                        } catch (NullPointerException e) {

                        }
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }


        //save message
        preferences = getSharedPreferences("messagebox", Context.MODE_PRIVATE);
        editor = preferences.edit();
//retrive message
        SharedPreferences preferences = getSharedPreferences("messagebox", Context.MODE_PRIVATE);
        message.setText(preferences.getString("chatM_" + friendId + "_" + mAuth.getCurrentUser().getUid(), ""));
        //toolbar data get

        if (getIntent() != null) {
            Intent intent = getIntent();
            friendId = intent.getExtras().getString("id");
            Global.currname = intent.getExtras().getString("name");
            Global.currAva = intent.getExtras().getString("ava");
        }
        name.setText(Global.currname);
        if (String.valueOf(Global.currAva).equals("no")) {
            Picasso.get()
                    .load(R.drawable.group)
                    .placeholder(R.drawable.placeholder_gray).error(R.drawable.errorimg)

                    .into(ava);
        } else {
            Picasso.get()
                    .load(Global.currAva)
                    .placeholder(R.drawable.placeholder_gray).error(R.drawable.errorimg)

                    .into(ava);
        }
        state.setVisibility(View.GONE);

        Map<String, Object> mapp = new HashMap<>();
        mapp.put("typing", false);
        mapp.put("audio", false);
        disc.child(friendId).onDisconnect().updateChildren(mapp);
//typing
        Global.currFid = friendId;
        Query query = type.child(friendId);
        query.keepSynced(true);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (mAuth.getCurrentUser() != null) {

                    GroupIn groupIn = dataSnapshot.getValue(GroupIn.class);
                    if (groupIn != null) {
                        typingR = groupIn.isTyping();
                        recordingR = groupIn.isAudio();
                        if (typingR)
                            whoT = groupIn.getWhoT();
                        if (recordingR)
                            whoR = groupIn.getWhoR();
                        typingit();
                    } else {
                        typingR = false;
                        recordingR = false;
                        typingit();
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        mylist = new ArrayList<>();
        myData = FirebaseDatabase.getInstance().getReference(Global.USERS);
        Query query1 = myData.child(mAuth.getCurrentUser().getUid());
        query1.keepSynced(true);
        query1.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                data = dataSnapshot.getValue(myD.class);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        Query query2 = mdatagetme.child(mAuth.getCurrentUser().getUid());
        query2.keepSynced(true);
        query2.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                myD userData = dataSnapshot.getValue(myD.class);
                Global.nameLocal = userData.getName();
                Global.avaLocal = userData.getAvatar();
                Global.myscreen = userData.isScreen();
                Global.myonstate = userData.isOnline();
                //attach menu photo
                if (String.valueOf(Global.avaLocal).equals("no")) {
                    Picasso.get()
                            .load(R.drawable.group)
                            .placeholder(R.drawable.placeholder_gray).error(R.drawable.errorimg)

                            .into(attachmenuP);
                } else {
                    Picasso.get()
                            .load(Global.avaLocal)
                            .placeholder(R.drawable.placeholder_gray).error(R.drawable.errorimg)

                            .into(attachmenuP);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        downdown.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                downdown.setVisibility(View.GONE);
                messagesList.getLayoutManager().smoothScrollToPosition(messagesList, null, 0);
                canScroll = false;
            }
        });
        if (Build.VERSION.SDK_INT >= 23) {
            messagesList.setOnScrollChangeListener(new View.OnScrollChangeListener() {
                @Override
                public void onScrollChange(View v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                    if (!messagesList.canScrollVertically(5)) {
                        canScroll = false;
                        downdown.setVisibility(View.GONE);
                    } else {
                        canScroll = true;
                        downdown.setVisibility(View.VISIBLE);

                    }

                }
            });
        } else {
            messagesList.setOnScrollListener(new RecyclerView.OnScrollListener() {
                @Override
                public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                    super.onScrollStateChanged(recyclerView, newState);
                    if (!messagesList.canScrollVertically(1)) {
                        canScroll = false;
                        downdown.setVisibility(View.GONE);
                    } else {
                        canScroll = true;
                        downdown.setVisibility(View.VISIBLE);

                    }

                }
            });
        }
        //output
        imageLoader = new ImageLoader() {

            @Override
            public void loadImage(final ImageView imageView, final String url, Object payload) {
                if (String.valueOf(url).equals("no")) {
                    Picasso.get()
                            .load(R.drawable.group)
                            .placeholder(R.drawable.placeholder_gray).error(R.drawable.errorimg)

                            .into(imageView);
                } else {
                    Picasso.get()
                            .load(url)
                            .placeholder(R.drawable.placeholder_gray).error(R.drawable.errorimg)

                            .into(imageView);
                }
            }
        };

        //get messages
        getChats();
        //typing
        if (message.getText().toString().isEmpty()) {
            voice.setVisibility(View.VISIBLE);
            send.setEnabled(false);
            send.setVisibility(View.GONE);
            imm.setVisibility(View.VISIBLE);
        } else {
            voice.setVisibility(View.GONE);
            send.setEnabled(true);
            send.setVisibility(View.VISIBLE);
            imm.setVisibility(View.GONE);
        }
        message.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (Global.messGGG.size() > 0 && Global.messGGG != null) {
                    Map<String, Object> map = new HashMap<>();
                    map.put("typing", true);
                    map.put("whoT", Global.nameLocal);
                    if (Global.messGGG != null && Global.messGGG.size() != 0)
                        type.child(friendId).updateChildren(map);
                    stopTT();
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
                startTT();
                if (!TextUtils.isEmpty(editable.toString())) {
                    voice.setVisibility(View.GONE);
                    send.setEnabled(true);
                    send.setVisibility(View.VISIBLE);
                    imm.setVisibility(View.GONE);
                } else {
                    voice.setVisibility(View.VISIBLE);
                    send.setEnabled(false);
                    send.setVisibility(View.GONE);
                    imm.setVisibility(View.VISIBLE);
                }
                //resize message box
                getSize();
                editor.putString("chatM_" + friendId + "_" + mAuth.getCurrentUser().getUid(), message.getText().toString());
                editor.apply();

            }
        });
        final String[] input = new String[1];


        //send message
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                input[0] = message.getText().toString();
                if (!input[0].trim().isEmpty()) {
                    message.setText("");
                    getSize();
                    editor.putString("chatM_" + friendId + "_" + mAuth.getCurrentUser().getUid(), message.getText().toString());
                    editor.apply();
                    if (emojiPopup.isShowing()) {
                        emoji.setImageResource(R.drawable.ic_emoji);
                        emojiPopup.dismiss();
                    }

                    if (!replyBollean) {

                        encrypM = String.valueOf(input[0]).trim();
                        encrypM = encryption.encryptOrNull(encrypM);
                        currTime = ServerValue.TIMESTAMP;
                        messidL = mAuth.getCurrentUser().getUid() + "_" + friendId + "_" + String.valueOf(System.currentTimeMillis());
                        //send owner data to friend
                        mAuth = FirebaseAuth.getInstance();

                        //local message
                        messagesAdapter.clear();
                        messageLocal = new MessageIn(encrypM, "text", "..", mAuth.getCurrentUser().getUid(), System.currentTimeMillis(), false, false, messidL, "no", encryption.encryptOrNull(Global.avaLocal), false, false, false, "");
                        try {
                            Global.messGGG.add(messageLocal);
                            //local store
                            ((AppBack) getApplication()).setchatsdbG(friendId);
                        } catch (NullPointerException e) {
                            Global.messGGG = new ArrayList<>();
                            Global.messGGG.add(messageLocal);
                            //local store
                            ((AppBack) getApplication()).setchatsdbG(friendId);
                        }

                        //     update last message if dialog exist
                        Groups groups = new Groups();
                        //       update dialog if not exist
                        GroupIn dialog = new GroupIn(Global.currname, Global.currAva, friendId, messageLocal.getMessage(), mAuth.getCurrentUser().getUid(), Global.avaLocal, messageLocal.getTime(), 0);
                        ArrayList<GroupIn> tempoo = new ArrayList<>();
                        tempoo.clear();
                        tempoo.add(dialog);
                        Global.groupG = dialog;
                        Global.DialogonelistG = tempoo;
                        Global.Dialogid = friendId;
                        Global.DialogM = messageLocal;
                        groups.updatedialog(Group.this);


                        messagesAdapter.addToEnd(MessageData.getMessagesG(), true);
                        messagesAdapter.notifyDataSetChanged();
                        messagesList.getLayoutManager().smoothScrollToPosition(messagesList, null, 0);
                        ///////

                        sendM();
                    } else
                        goReply(String.valueOf(input[0]).trim());
                } else
                    Snackbar.make(ly, R.string.empty_mess, Snackbar.LENGTH_SHORT).show();
            }
        });

        message.setOnKeyListener(new View.OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_DOWN) {
                    if (((AppBack) getApplication()).shared().getBoolean("enter", false)) {
                        switch (keyCode) {
                            case KeyEvent.KEYCODE_DPAD_CENTER:
                            case KeyEvent.KEYCODE_ENTER:
                                input[0] = message.getText().toString();
                                if (!input[0].trim().isEmpty()) {
                                    message.setText("");
                                    getSize();
                                    editor.putString("chatM_" + friendId + "_" + mAuth.getCurrentUser().getUid(), message.getText().toString());
                                    editor.apply();
                                    if (emojiPopup.isShowing()) {
                                        emoji.setImageResource(R.drawable.ic_emoji);
                                        emojiPopup.dismiss();
                                    }
                                    if (!replyBollean) {

                                        encrypM = String.valueOf(input[0]).trim();
                                        encrypM = encryption.encryptOrNull(encrypM);
                                        currTime = ServerValue.TIMESTAMP;
                                        messidL = mAuth.getCurrentUser().getUid() + "_" + friendId + "_" + String.valueOf(System.currentTimeMillis());
                                        //send owner data to friend
                                        mAuth = FirebaseAuth.getInstance();
                                        //local message
                                        messagesAdapter.clear();
                                        messageLocal = new MessageIn(encrypM, "text", "..", mAuth.getCurrentUser().getUid(), System.currentTimeMillis(), false, false, messidL, "no", encryption.encryptOrNull(Global.avaLocal), false, false, false, "");
                                        try {
                                            Global.messGGG.add(messageLocal);
                                            //local store
                                            ((AppBack) getApplication()).setchatsdbG(friendId);
                                        } catch (NullPointerException e) {
                                            Global.messGGG = new ArrayList<>();
                                            Global.messGGG.add(messageLocal);
                                            //local store
                                            ((AppBack) getApplication()).setchatsdbG(friendId);
                                        }

                                        //     update last message if dialog exist
                                        Groups groups = new Groups();
                                        //       update dialog if not exist
                                        GroupIn dialog = new GroupIn(Global.currname, Global.currAva, friendId, messageLocal.getMessage(), mAuth.getCurrentUser().getUid(), Global.avaLocal, messageLocal.getTime(), 0);
                                        ArrayList<GroupIn> tempoo = new ArrayList<>();
                                        tempoo.clear();
                                        tempoo.add(dialog);
                                        Global.groupG = dialog;
                                        Global.DialogonelistG = tempoo;
                                        Global.Dialogid = friendId;
                                        Global.DialogM = messageLocal;
                                        groups.updatedialog(Group.this);


                                        messagesAdapter.addToEnd(MessageData.getMessagesG(), true);
                                        messagesAdapter.notifyDataSetChanged();
                                        messagesList.getLayoutManager().smoothScrollToPosition(messagesList, null, 0);
                                        ///////
                                        sendM();
                                    } else
                                        goReply(String.valueOf(input[0]).trim());

                                } else
                                    Snackbar.make(ly, R.string.empty_mess, Snackbar.LENGTH_SHORT).show();
                                return true;
                            default:
                                break;
                        }
                    }
                }
                return false;
            }
        });
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isHidden) {
                    cdd.show();
                    isHidden = false;
                } else {
                    cdd.dismiss();
                    isHidden = true;
                }

            }
        });
        btnI.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (iqb) {
                    cdd.dismiss();
                    isHidden = true;
                    uploadI();
                } else
                    Toast.makeText(Group.this, R.string.wait, Toast.LENGTH_SHORT).show();
            }
        });
        btnF.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cdd.dismiss();
                isHidden = true;
                Intent intent = new Intent(Group.this, FilePickerActivity.class);
                intent.putExtra(FilePickerActivity.CONFIGS, new Configurations.Builder()
                        .setCheckPermission(true)
                        .setShowImages(false)
                        .enableImageCapture(false)
                        .setMaxSelection(Global.fileS)
                        .setShowVideos(false)
                        .setShowFiles(true)
                        .setSkipZeroSizeFiles(false)
                        .build());
                startActivityForResult(intent, 1023);
            }


        });
        btnVideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cdd.dismiss();
                isHidden = true;

                mediaPicker.with(params).pick();

            }
        });
        btnV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cdd.dismiss();
                isHidden = true;
                Intent intent3 = new Intent(Group.this, AudioPickActivity.class);
                intent3.putExtra(IS_NEED_RECORDER, true);
                intent3.putExtra(IS_NEED_FOLDER_LIST, true);
                intent3.putExtra(Constant.MAX_NUMBER, Global.audioS);
                startActivityForResult(intent3, Constant.REQUEST_CODE_PICK_AUDIO);
            }
        });
        btnS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cdd.dismiss();
                isHidden = true;
                SandriosCamera
                        .with()
                        .setShowPicker(true)
                        .setMediaAction(CameraConfiguration.MEDIA_QUALITY_MEDIUM)
                        .setMediaAction(CameraConfiguration.MEDIA_ACTION_VIDEO)
                        .enableImageCropping(true)
                        .launchCamera(Group.this);
            }
        });
        emoji.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (emojiPopup.isShowing()) {
                    emoji.setImageResource(R.drawable.ic_emoji);
                    emojiPopup.dismiss();
                } else {
                    emoji.setImageResource(R.drawable.ic_keyboard);
                    emojiPopup.toggle();
                }
            }
        });
        btnL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                cdd.dismiss();
                isHidden = true;

                Dexter.withActivity(Group.this)
                        .withPermissions(Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION)
                        .withListener(new MultiplePermissionsListener() {
                            @Override
                            public void onPermissionsChecked(MultiplePermissionsReport report) {

                                if (report.areAllPermissionsGranted()) {
                                    //location
                                    location = new SimpleLocation(Group.this);
                                    // if we can't access the location yet
                                    if (!location.hasLocationEnabled()) {
                                        // ask the user to enable location access
                                        SimpleLocation.openSettings(Group.this);
                                    } else {
                                        encrypL = location.getLatitude() + "," + location.getLongitude();
                                        encrypL = encryption.encryptOrNull(encrypL);
                                        encrypMap = getString(R.string.map_location);
                                        encrypMap = encryption.encryptOrNull(encrypMap);
                                        currTime = ServerValue.TIMESTAMP;
                                        messidL = mAuth.getCurrentUser().getUid() + "_" + friendId + "_" + String.valueOf(System.currentTimeMillis());
                                        //send owner data to friend
                                        mAuth = FirebaseAuth.getInstance();
                                        //local message
                                        messagesAdapter.clear();
                                        messageLocal = new MessageIn(encrypL, "..", mAuth.getCurrentUser().getUid(), System.currentTimeMillis(), false, "no", false, messidL, "map", encryption.encryptOrNull(Global.avaLocal), false, false, false, "");
                                        try {
                                            Global.messGGG.add(messageLocal);
                                            //local store
                                            ((AppBack) getApplication()).setchatsdbG(friendId);
                                        } catch (NullPointerException e) {
                                            Global.messGGG = new ArrayList<>();
                                            Global.messGGG.add(messageLocal);
                                            //local store
                                            ((AppBack) getApplication()).setchatsdbG(friendId);
                                        }


                                        //     update last message if dialog exist
                                        Groups groups = new Groups();
                                        //       update dialog if not exist
                                        GroupIn dialog = new GroupIn(Global.currname, Global.currAva, friendId, encrypMap, mAuth.getCurrentUser().getUid(), Global.avaLocal, messageLocal.getTime(), 0);
                                        ArrayList<GroupIn> tempoo = new ArrayList<>();
                                        tempoo.clear();
                                        tempoo.add(dialog);
                                        Global.groupG = dialog;
                                        Global.DialogonelistG = tempoo;
                                        Global.Dialogid = friendId;
                                        Global.DialogM = messageLocal;
                                        groups.updatedialog(Group.this);
                                        messagesAdapter.addToEnd(MessageData.getMessagesG(), true);
                                        messagesAdapter.notifyDataSetChanged();
                                        messagesList.getLayoutManager().smoothScrollToPosition(messagesList, null, 0);
                                        ///////
                                        sendMap();


                                    }
                                } else
                                    Toast.makeText(Group.this, getString(R.string.acc_per), Toast.LENGTH_SHORT).show();


                            }

                            @Override
                            public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {

                                token.continuePermissionRequest();

                            }
                        }).check();


            }
        });
        message.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cdd.dismiss();
                isHidden = true;
                if (emojiPopup.isShowing()) {
                    emoji.setImageResource(R.drawable.ic_emoji);
                    emojiPopup.dismiss();
                }

            }
        });

        sticker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    if (Global.stickerIcon) {
                        if (iqb) {
                            Global.stickerIcon = false;
                            mStickerBSFragment.show(getSupportFragmentManager(), mStickerBSFragment.getTag());
                        } else
                            Toast.makeText(Group.this, R.string.wait, Toast.LENGTH_SHORT).show();
                    }
                } catch (IllegalStateException e) {

                }
            }
        });

//voice record
        recordView.setVisibility(View.GONE);
        messagebox.setVisibility(View.VISIBLE);
        add.setVisibility(View.VISIBLE);
        recordView.setOnRecordListener(new OnRecordListener() {
            @Override
            public void onStart() {

                Dexter.withActivity(Group.this)
                        .withPermissions(Manifest.permission.RECORD_AUDIO, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE)
                        .withListener(new MultiplePermissionsListener() {
                            @Override
                            public void onPermissionsChecked(MultiplePermissionsReport report) {

                                if (report.areAllPermissionsGranted()) {
                                    //Start Recording..
                                    recordView.setVisibility(View.VISIBLE);
                                    messagebox.setVisibility(View.GONE);
                                    add.setVisibility(View.GONE);
                                    startRecording();
                                } else
                                    Toast.makeText(Group.this, getString(R.string.acc_per), Toast.LENGTH_SHORT).show();


                            }

                            @Override
                            public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {

                                token.continuePermissionRequest();

                            }
                        }).check();

            }

            @Override
            public void onCancel() {
                try {
                    stopRecording(false);
                } catch (RuntimeException e) {
                    stopAT();
                }
                setResult(RESULT_CANCELED);
            }

            @Override
            public void onFinish(long recordTime) {
                recordView.setVisibility(View.GONE);
                messagebox.setVisibility(View.VISIBLE);
                add.setVisibility(View.VISIBLE);
                if (!pausebreak) {
                    try {
                        stopRecording(true);
                        Uri uri = Uri.parse("file://" + mOutputFile.getAbsolutePath());
                        setResult(Activity.RESULT_OK, new Intent().setData(uri));

                        uploadV(uri, recordTime);
                    } catch (NullPointerException e) {
                        stopAT();

                    }
                }
            }

            @Override
            public void onLessThanSecond() {
                try {
                    stopAT();
                    recordView.setVisibility(View.GONE);
                    messagebox.setVisibility(View.VISIBLE);
                    add.setVisibility(View.VISIBLE);
                    try {
                        stopRecording(false);
                    } catch (RuntimeException e) {
                        stopAT();
                    }
                    setResult(RESULT_CANCELED);
                } catch (NullPointerException e) {
                    stopAT();
                }

            }
        });
        voice.setListenForRecord(true);
        recordView.setOnBasketAnimationEndListener(new OnBasketAnimationEnd() {
            @Override
            public void onAnimationEnd() {
                recordView.setVisibility(View.GONE);
                messagebox.setVisibility(View.VISIBLE);
                add.setVisibility(View.VISIBLE);
            }
        });

        imm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (iqb) {
                    uploadI();
                } else
                    Toast.makeText(Group.this, R.string.wait, Toast.LENGTH_SHORT).show();

            }
        });

        //  deleteTodoItem();

        if (((AppBack) getApplication()).shared().getBoolean("shake", true))
            onShakeDelete();


    }

    @Override
    protected void onStop() {
        super.onStop();
        try {
            stopRecording(false);
        } catch (RuntimeException e) {
        }
        setResult(RESULT_CANCELED);
    }

    @Override
    public void onBackPressed() {
        if (selectionCount == 0) {
            super.onBackPressed();
        } else {
            messagesAdapter.unselectAllItems();
        }
    }

    @Override
    public void onLoadMore(int page, int totalItemsCount) {
        if (totalItemsCount < TOTAL_MESSAGES_COUNT) {
            loadMessages();
        }
    }


    protected void loadMessages() {
        new Handler().postDelayed(new Runnable() { //imitation of internet connection
            @Override
            public void run() {
                ArrayList<Message> messages = MessageData.getMessagesG();
//                lastLoadedDate = messages.get(messages.size() - 1).getCreatedAt();
                messagesAdapter.addToEnd(messages, false);
            }
        }, 1000);
    }

    private void initAdapter() {
        //todo
        holdersConfig = new MessagesListAdapter.HoldersConfig();
        holdersConfig.setIncoming(IncomeHolder.class, R.layout.item_incoming_text_message);
        holdersConfig.setOutcoming(OutcomeHolder.class, R.layout.item_outcoming_text_message);
        holdersConfig.setIncomingImageHolder(CustomIncomingImageMessageViewHolder.class, R.layout.item_incoming_image_message);
        holdersConfig.setOutcomingImageHolder(CustomOutcomingImageMessageViewHolder.class, R.layout.item_outcoming_image_message);
        holdersConfig.setDateHeaderLayout(R.layout.item_date_header);
        holdersConfig.registerContentType(Voiceid, IncomeOther.class, R.layout.item_incoming_other_message,
                OutcomeOther.class, R.layout.item_outcoming_other_message, new MessageHolders.ContentChecker() {
                    @Override
                    public boolean hasContentFor(Message message, byte type) {
                        if (type == 2) {
                            try {
                                return message.getVoice().getUrl() != null;
                            } catch (NullPointerException e) {
                                try {
                                    return message.getFile().getUrl() != null;

                                } catch (NullPointerException e1) {
                                    try {
                                        return message.getVideo().getUrl() != null;

                                    } catch (NullPointerException e2) {
                                        try {
                                            return message.getMap().getLocation() != null;

                                        } catch (NullPointerException e3) {
                                            return false;

                                        }
                                    }

                                }

                            }
                        } else
                            return false;
                    }
                });
        messagesAdapter = new MessagesListAdapter<>(mAuth.getCurrentUser().getUid(), holdersConfig, imageLoader);
        messagesAdapter.setLoadMoreListener(this);
        messagesAdapter.setDateHeadersFormatter(new DateFormatter.Formatter() {
            @Override
            public String format(Date date) {
                if (DateFormatter.isToday(date)) {
                    return getString(R.string.date_header_today);
                } else if (DateFormatter.isYesterday(date)) {
                    return getString(R.string.date_header_yesterday);
                } else {
                    return DateFormatter.format(date, DateFormatter.Template.STRING_DAY_MONTH_YEAR);
                }


            }
        });
        messagesAdapter.registerViewClickListener(R.id.messageUserAvatar,
                new MessagesListAdapter.OnMessageViewClickListener<Message>() {
                    @Override
                    public void onMessageViewClick(View view, Message message) {
//todo
                    }
                });
        messagesList.setAdapter(messagesAdapter);
        messagesList.setNestedScrollingEnabled(true);

    }


    private void sendM() {
        final Map<String, Object> map = new HashMap<>();
        map.put("message", encrypM);
        map.put("time", currTime);
        map.put("messId", messidL);
        map.put("react", "no");
        map.put("avatar", encryption.encryptOrNull(Global.avaLocal));
        map.put("chat", false);
        map.put("seen", false);
        map.put("type", "text");
        map.put("deleted", false);
        map.put("statue", "✔");
        map.put("reply", encryption.encryptOrNull(""));
        map.put("forw", false);
        map.put("call", false);
        map.put("from", mAuth.getCurrentUser().getUid());

        mData.child(friendId).child(Global.Messages).child(messidL)
                .updateChildren(map).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Map<String, Object> map2 = new HashMap<>();
                map2.put("lastmessage", encrypM);
                map2.put("lastsender", mAuth.getCurrentUser().getUid());
                map2.put("lastsenderava", Global.avaLocal);
                map2.put("messDate", currTime);

                mData.child(friendId).updateChildren(map2).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        mUserDB.child(mAuth.getCurrentUser().getUid()).child(Global.GROUPS).child(friendId).updateChildren(map2).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                sendMessNotify(encrypM, messidL);
                                for (int i = 0; i < Global.currGUsers.size(); i++) {
                                    int j = i;
                                    if (!Global.currGUsers.get(i).equals(mAuth.getCurrentUser().getUid())) {
                                        mUserDB.child(Global.currGUsers.get(i)).child(Global.GROUPS).child(friendId).updateChildren(map2).addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                            }
                                        });
                                    }
                                }
                            }
                        });
                    }
                });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });
    }

    private void sendMap() {
        final Map<String, Object> map = new HashMap<>();
        map.put("location", encrypL);
        map.put("time", currTime);
        map.put("messId", messidL);
        map.put("react", "no");
        map.put("avatar", encryption.encryptOrNull(Global.avaLocal));
        map.put("chat", false);
        map.put("seen", false);
        map.put("type", "map");
        map.put("deleted", false);
        map.put("statue", "✔");
        map.put("reply", encryption.encryptOrNull(""));
        map.put("forw", false);
        map.put("call", false);
        map.put("from", mAuth.getCurrentUser().getUid());
        mData.child(friendId).child(Global.Messages).child(messidL)
                .updateChildren(map).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Map<String, Object> map2 = new HashMap<>();
                map2.put("lastmessage", encrypMap);
                map2.put("lastsender", mAuth.getCurrentUser().getUid());
                map2.put("lastsenderava", Global.avaLocal);
                map2.put("messDate", currTime);

                mData.child(friendId).updateChildren(map2).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        mUserDB.child(mAuth.getCurrentUser().getUid()).child(Global.GROUPS).child(friendId).updateChildren(map2).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                sendMessNotify(encrypMap, messidL);
                                for (int i = 0; i < Global.currGUsers.size(); i++) {
                                    int j = i;
                                    if (!Global.currGUsers.get(i).equals(mAuth.getCurrentUser().getUid())) {
                                        mUserDB.child(Global.currGUsers.get(i)).child(Global.GROUPS).child(friendId).updateChildren(map2).addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                            }
                                        });
                                    }
                                }
                            }
                        });
                    }
                });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });
    }

    private void getChats() {

        final long[] keyOnce = {0};
        final int[] i = {0};
        final int[] onceOnce = {0};
        //getride of old data
        ((AppBack) getApplication()).getchatsdbG(friendId);
        query = mData.child(friendId).child(Global.Messages).orderByChild("time");
        query.keepSynced(true);
        initAdapter();
        //just init because of the first time offline started chat
        messagesAdapter.clear();
        messagesAdapter.addToEnd(MessageData.getMessagesG(), true);
        messagesAdapter.notifyDataSetChanged();

        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                keyOnce[0] = dataSnapshot.getChildrenCount();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        if (!Global.check_int(Group.this)) {
            ((AppBack) getApplication()).getchatsdbG(friendId);
            //update the list
            messagesAdapter.clear();
            messagesAdapter.addToEnd(MessageData.getMessagesG(), true);
            messagesAdapter.notifyDataSetChanged();
        }


        child = query.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                if (dataSnapshot.exists()) {
                    try {

                        MessageIn message = dataSnapshot.getValue(MessageIn.class);

                        if (messagesAdapter.halbine(Global.messGGG, message.getMessId()) == -1)
                            Global.messGGG.add(message);
                        else
                            Global.messGGG.set(messagesAdapter.halbine(Global.messGGG, message.getMessId()), message);

                        if (!message.getFrom().equals(mAuth.getCurrentUser().getUid()) && canScroll) {
                            downdown.setVisibility(View.VISIBLE);


                        }
                        //check only in global list range
                        if (i[0] >= keyOnce[0] - 1) {
                            //local store
                            ((AppBack) getApplication()).setchatsdbG(friendId);

                            //update the list
                            messagesAdapter.clear();
                            messagesAdapter.addToEnd(MessageData.getMessagesG(), true);
                            messagesAdapter.notifyDataSetChanged();
                            messagesList.scrollBy(0, 0);


                            if (onceOnce[0] == 0) {
                                keyOnce[0]++;
                            }
                        }
                        i[0]++;
                    } catch (NullPointerException e) {

                    }
                }
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                MessageIn message = dataSnapshot.getValue(MessageIn.class);
                if (messagesAdapter.halbine(Global.messGGG, message.getMessId()) != -1)
                    Global.messGGG.set(messagesAdapter.halbine(Global.messGGG, message.getMessId()), message);

                //local store
                ((AppBack) getApplication()).setchatsdbG(friendId);

                messagesAdapter.notifyDataSetChanged();
                messagesAdapter.clear();
                messagesAdapter.addToEnd(MessageData.getMessagesG(), true);
                messagesAdapter.notifyDataSetChanged();
                messagesList.scrollBy(0, 0);


            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                MessageIn message = dataSnapshot.getValue(MessageIn.class);

                if (messagesAdapter.halbine(Global.messGGG, message.getMessId()) != -1)
                    Global.messGGG.remove(messagesAdapter.halbine(Global.messGGG, message.getMessId()));

                //local store
                ((AppBack) getApplication()).setchatsdbG(friendId);

                messagesAdapter.clear();
                messagesAdapter.addToEnd(MessageData.getMessagesG(), true);
                messagesAdapter.notifyDataSetChanged();
                messagesList.scrollBy(0, 0);


            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }


    private void readM() {
//        Query query33 =  mData.child(friendId).child(mAuth.getCurrentUser().getUid()).child(Global.Messages);
//        query33.keepSynced(true);
//        query33.addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                for (DataSnapshot ds : dataSnapshot.getChildren()) {
//                    OnlineGetter getter = ds.getValue(OnlineGetter.class);
//                    try {
//                        if (getter != null && getter.getStatue().equals("D✔"))
//                            ds.child("statue").getRef().setValue("R✔");
//                    } catch (NullPointerException e) {
//                    }
//                }
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//
//            }
//        });
    }

    private void zeroCount() {
        try {
            if (open) {
                DatabaseReference count = FirebaseDatabase.getInstance().getReference(Global.USERS);
                if (Global.currGUsers.contains(mAuth.getCurrentUser().getUid())) {
                    Map<String, Object> map = new HashMap<>();
                    map.put("noOfUnread", 0);
                    count.child(mAuth.getCurrentUser().getUid()).child(Global.GROUPS).child(friendId).updateChildren(map).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {

                        }
                    });
                }
            }
        }
        catch (NullPointerException e)
        {

        }

    }

    public void startTT() {
        this.mActivityTransitionTimer = new Timer();
        this.mActivityTransitionTimerTask = new TimerTask() {
            public void run() {
                if (Global.messGGG.size() > 0 && Global.messGGG != null) {
                    Map<String, Object> map = new HashMap<>();
                    map.put("typing", false);
                    if (Global.messGGG != null && Global.messGGG.size() != 0)
                        type.child(friendId).updateChildren(map);
                }
            }
        };

        this.mActivityTransitionTimer.schedule(mActivityTransitionTimerTask,
                MAX_ACTIVITY_TRANSITION_TIME_MS);
    }


    public void stopTT() {
        if (this.mActivityTransitionTimerTask != null) {
            this.mActivityTransitionTimerTask.cancel();
        }

        if (this.mActivityTransitionTimer != null) {
            this.mActivityTransitionTimer.cancel();
        }

    }

    //stop and begin (recording audio)
    public void startAT() {
        if (mAuth.getCurrentUser() != null) {

            if (Global.messGGG.size() > 0 && Global.messGGG != null) {
                Map<String, Object> map = new HashMap<>();
                map.put("audio", true);
                map.put("whoR", Global.nameLocal);
                if (Global.messGGG != null && Global.messGGG.size() != 0)
                    type.child(friendId).updateChildren(map);
            }
        }
    }

    public void stopAT() {
        if (mAuth.getCurrentUser() != null) {

            if (Global.messGGG.size() > 0 && Global.messGGG != null) {
                Map<String, Object> map = new HashMap<>();
                map.put("audio", false);
                if (Global.messGGG != null && Global.messGGG.size() != 0)
                    type.child(friendId).updateChildren(map);
            }
        }
    }

    public void uploadI() {

        Options options = Options.init()
                .setRequestCode(100)                                                 //Request code for activity results
                .setCount(Global.photoS)                                                         //Number of images to restict selection count
                .setFrontfacing(false)                                                //Front Facing camera on start
                .setImageQuality(ImageQuality.REGULAR)                                  //Image Quality
                .setScreenOrientation(Options.SCREEN_ORIENTATION_PORTRAIT);        //Orientaion

        Pix.start(Group.this, options);


    }

    public void uploadF(Uri linkL, final String filename, String filett) {
        //for local
        String fileL = filename;

        Dexter.withActivity(Group.this)
                .withPermissions(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.WAKE_LOCK)
                .withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport report) {

                        if (report.areAllPermissionsGranted()) {
                            filename.replace(" ", "_");
                            if (filett.contains("/xlsx")) {
                                filetype = ".xlsx";
                            } else if (filett.contains("/xls")) {
                                filetype = ".xls";

                            } else if (filett.contains("/docx")) {
                                filetype = ".docx";

                            } else if (filett.contains("/doc")) {
                                filetype = ".doc";

                            } else if (filett.contains("/pptx")) {
                                filetype = ".pptx";
                            } else if (filett.contains("/ppt")) {
                                filetype = ".ppt";

                            } else if (filett.contains("/pdf")) {
                                filetype = ".pdf";

                            } else if (filett.contains("/txt")) {
                                filetype = ".txt";
                            } else if (filett.contains("/plain")) {
                                filetype = ".txt";
                            } else if (filett.contains("/binary")) {
                                filetype = "";
                            } else if (filett.contains("/zip")) {
                                filetype = ".zip";
                            } else if (filett.contains("/rar")) {
                                filetype = ".rar";
                            } else if (filett.contains("/apk")) {
                                filetype = ".apk";
                            } else {
                                filetype = filett;
                            }
                            //local message
                            messidL = mAuth.getCurrentUser().getUid() + "_" + friendId + "_" + String.valueOf(System.currentTimeMillis());
                            fileA.add(messidL);
                            String locall = encryption.encryptOrNull(String.valueOf(linkL));
                            messagesAdapter.clear();
                            messageLocal = new MessageIn(locall, "..", System.currentTimeMillis(), false, false, messidL, "file", fileL + filetype, mAuth.getCurrentUser().getUid(), "no", encryption.encryptOrNull(Global.avaLocal), false, false, false, "");
                            try {
                                Global.messGGG.add(messageLocal);
                                //local store
                                ((AppBack) getApplication()).setchatsdbG(friendId);
                            } catch (NullPointerException e) {
                                Global.messGGG = new ArrayList<>();
                                Global.messGGG.add(messageLocal);
                                //local store
                                ((AppBack) getApplication()).setchatsdbG(friendId);
                            }

                            //update dialog if not exist
                            encrypF = "File " + filename + filetype;
                            encrypF = encryption.encryptOrNull(encrypF);
                            //     update last message if dialog exist
                            Groups groups = new Groups();
                            //       update dialog if not exist
                            GroupIn dialog = new GroupIn(Global.currname, Global.currAva, friendId, encrypF, mAuth.getCurrentUser().getUid(), Global.avaLocal, messageLocal.getTime(), 0);
                            ArrayList<GroupIn> tempoo = new ArrayList<>();
                            tempoo.clear();
                            tempoo.add(dialog);
                            Global.groupG = dialog;
                            Global.DialogonelistG = tempoo;
                            Global.Dialogid = friendId;
                            Global.DialogM = messageLocal;
                            groups.updatedialog(Group.this);
                            messagesAdapter.addToEnd(MessageData.getMessagesG(), true);
                            messagesAdapter.notifyDataSetChanged();
                            messagesList.getLayoutManager().smoothScrollToPosition(messagesList, null, 0);
                            ///////
                            //upload on server
                            StorageReference mStorageRef = FirebaseStorage.getInstance().getReference();
                            StorageReference riversRef = mStorageRef.child(Global.GROUPS + "/" + friendId + "/" + mAuth.getCurrentUser().getUid() + "/Files/" + mAuth.getCurrentUser().getUid() + friendId + System.currentTimeMillis() + filetype);
                            final String finalFiletype = filetype;
                            UploadTask uploadTask = riversRef.putFile(linkL);

                            Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                                @Override
                                public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                                    if (!task.isSuccessful()) {
                                        throw task.getException();
                                    }

                                    // Continue with the task to get the download URL
                                    return riversRef.getDownloadUrl();
                                }
                            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                                @Override
                                public void onComplete(@NonNull Task<Uri> task) {
                                    if (task.isSuccessful()) {
                                        Uri downloadUrl = task.getResult();
                                        message.setText("");
                                        currTime = ServerValue.TIMESTAMP;
                                        //send owner data to friend
                                        mAuth = FirebaseAuth.getInstance();
                                        sendFpre(String.valueOf(downloadUrl), filename + finalFiletype);


                                    }
                                }
                            });
                        } else
                            Toast.makeText(Group.this, getString(R.string.acc_per), Toast.LENGTH_SHORT).show();


                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {

                        token.continuePermissionRequest();

                    }
                }).check();


    }


    public void uploadV(Uri linkL, final long time) {
        messidL = mAuth.getCurrentUser().getUid() + "_" + friendId + "_" + String.valueOf(System.currentTimeMillis());
        String locall = encryption.encryptOrNull(String.valueOf(linkL));
        //local message
        messagesAdapter.clear();
        messageLocal = new MessageIn(locall, "..", mAuth.getCurrentUser().getUid(), System.currentTimeMillis(), false, false, messidL, "voice", "no", getHumanTimeText(time), encryption.encryptOrNull(Global.avaLocal), false, false, false, "");
        try {
            Global.messGGG.add(messageLocal);
            //local store
            ((AppBack) getApplication()).setchatsdbG(friendId);
        } catch (NullPointerException e) {
            Global.messGGG = new ArrayList<>();
            Global.messGGG.add(messageLocal);
            //local store
            ((AppBack) getApplication()).setchatsdbG(friendId);
        }
        //update dialog if not exist
        encrypV = "Voice " + getHumanTimeText(time);
        encrypV = encryption.encryptOrNull(encrypV);
        //     update last message if dialog exist
        Groups groups = new Groups();
        //       update dialog if not exist
        GroupIn dialog = new GroupIn(Global.currname, Global.currAva, friendId, encrypV, mAuth.getCurrentUser().getUid(), Global.avaLocal, messageLocal.getTime(), 0);
        ArrayList<GroupIn> tempoo = new ArrayList<>();
        tempoo.clear();
        tempoo.add(dialog);
        Global.groupG = dialog;
        Global.DialogonelistG = tempoo;
        Global.Dialogid = friendId;
        Global.DialogM = messageLocal;
        groups.updatedialog(Group.this);
        messagesAdapter.addToEnd(MessageData.getMessagesG(), true);
        messagesAdapter.notifyDataSetChanged();
        messagesList.getLayoutManager().smoothScrollToPosition(messagesList, null, 0);
        ///////

        StorageReference mStorageRef = FirebaseStorage.getInstance().getReference();
        StorageReference riversRef = mStorageRef.child(Global.GROUPS + "/" + friendId + "/" + mAuth.getCurrentUser().getUid() + "/Audio/" + mAuth.getCurrentUser().getUid() + friendId + System.currentTimeMillis() + ".m4a");
        UploadTask uploadTask = riversRef.putFile(linkL);

        Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
            @Override
            public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                if (!task.isSuccessful()) {
                    throw task.getException();
                }

                // Continue with the task to get the download URL
                return riversRef.getDownloadUrl();
            }
        }).addOnCompleteListener(new OnCompleteListener<Uri>() {
            @Override
            public void onComplete(@NonNull Task<Uri> task) {
                if (task.isSuccessful()) {
                    Uri downloadUrl = task.getResult();
                    try {
                        mOutputFile.delete();

                    } catch (NullPointerException e) {

                    }
                    message.setText("");
                    currTime = ServerValue.TIMESTAMP;
                    //send owner data to friend
                    mAuth = FirebaseAuth.getInstance();
                    sendVpre(String.valueOf(downloadUrl), time);


                }

            }
        });

    }

    public void uploadVideo(final Uri linkL, final long time, final String local) {
        messidL = mAuth.getCurrentUser().getUid() + "_" + friendId + "_" + String.valueOf(System.currentTimeMillis());
        String locall = encryption.encryptOrNull(String.valueOf(linkL));
        //local message
        messagesAdapter.clear();
        messageLocal = new MessageIn(locall, "..", mAuth.getCurrentUser().getUid(), System.currentTimeMillis(), false, false, messidL, "video", getHumanTimeText(time), "file:///android_asset/loading.jpg", "no", encryption.encryptOrNull(Global.avaLocal), false, false, false, "");
        try {
            Global.messGGG.add(messageLocal);
            //local store
            ((AppBack) getApplication()).setchatsdbG(friendId);
        } catch (NullPointerException e) {
            Global.messGGG = new ArrayList<>();
            Global.messGGG.add(messageLocal);
            //local store
            ((AppBack) getApplication()).setchatsdbG(friendId);
        }

        //update dialog if not exist
        encrypVideo = "Video " + getHumanTimeText(time);
        encrypVideo = encryption.encryptOrNull(encrypVideo);
        //     update last message if dialog exist
        Groups groups = new Groups();
        //       update dialog if not exist
        GroupIn dialog = new GroupIn(Global.currname, Global.currAva, friendId, encrypVideo, mAuth.getCurrentUser().getUid(), Global.avaLocal, messageLocal.getTime(), 0);
        ArrayList<GroupIn> tempoo = new ArrayList<>();
        tempoo.clear();
        tempoo.add(dialog);
        Global.groupG = dialog;
        Global.DialogonelistG = tempoo;
        Global.Dialogid = friendId;
        Global.DialogM = messageLocal;
        groups.updatedialog(Group.this);
        messagesAdapter.addToEnd(MessageData.getMessagesG(), true);
        messagesAdapter.notifyDataSetChanged();
        messagesList.getLayoutManager().smoothScrollToPosition(messagesList, null, 0);
        ///////
        final String videoidtemp = System.currentTimeMillis() + "";
        StorageReference mStorageRef = FirebaseStorage.getInstance().getReference();
        StorageReference riversRef = mStorageRef.child(Global.GROUPS + "/" + friendId + "/" + mAuth.getCurrentUser().getUid() + "/Video/" + mAuth.getCurrentUser().getUid() + friendId + videoidtemp + ".mp4");
        UploadTask uploadTask = riversRef.putFile(linkL);

        Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
            @Override
            public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                if (!task.isSuccessful()) {
                    throw task.getException();
                }

                // Continue with the task to get the download URL
                return riversRef.getDownloadUrl();
            }
        }).addOnCompleteListener(new OnCompleteListener<Uri>() {
            @Override
            public void onComplete(@NonNull Task<Uri> task) {
                if (task.isSuccessful()) {
                    Uri downloadUrl = task.getResult();
                    message.setText("");
                    currTime = ServerValue.TIMESTAMP;
                    //send owner data to friend
                    mAuth = FirebaseAuth.getInstance();
                    thumb = ThumbnailUtils.createVideoThumbnail(local, MediaStore.Video.Thumbnails.MINI_KIND);
                    ByteArrayOutputStream bao = new ByteArrayOutputStream();
                    thumb.compress(Bitmap.CompressFormat.PNG, 100, bao);
                    byte[] byteArray = bao.toByteArray();
                    StorageReference mStorageRef = FirebaseStorage.getInstance().getReference();
                    StorageReference riversRef = mStorageRef.child(Global.GROUPS + "/" + friendId + "/" + mAuth.getCurrentUser().getUid() + "/Video/" + "Thumb/" + mAuth.getCurrentUser().getUid() + friendId + videoidtemp + ".png");
                    UploadTask uploadTask = riversRef.putBytes(byteArray);

                    Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                        @Override
                        public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                            if (!task.isSuccessful()) {
                                throw task.getException();
                            }

                            // Continue with the task to get the download URL
                            return riversRef.getDownloadUrl();
                        }
                    }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                        @Override
                        public void onComplete(@NonNull Task<Uri> task) {
                            if (task.isSuccessful()) {
                                Uri thumbD = task.getResult();
                                sendVideopre(String.valueOf(downloadUrl), time, String.valueOf(thumbD));
                            }
                        }
                    });

                }
            }
        });


    }

    @Override
    protected void onActivityResult(int requestCode, final int resultCode, Intent data) {
        switch (requestCode) {
            case 100:
                if (resultCode == RESULT_OK) {
                    if (Global.check_int(Group.this)) {
                        iqb = false;
                        imageA.clear();
                        iq = 0;
                    }
                    ArrayList<String> list = data.getStringArrayListExtra(Pix.IMAGE_RESULTS);
                    for (int i = 0; i < list.size(); i++) {
                        String path = list.get(i);
                        afterCompress(path);
                    }
                }
                break;
            case Constant.REQUEST_CODE_PICK_AUDIO:
                if (resultCode == RESULT_OK) {
                    ArrayList<AudioFile> list = data.getParcelableArrayListExtra(Constant.RESULT_PICK_AUDIO);
                    for (int i = 0; i < list.size(); i++) {
                        uploadV(Uri.parse("file:///" + list.get(i).getPath()), list.get(i).getDuration());
                    }

                }
                break;
            case 1023:
                if (resultCode == RESULT_OK) {

                    ArrayList<MediaFile> files = data.getParcelableArrayListExtra(FilePickerActivity.MEDIA_FILES);
                    if (Global.check_int(Group.this)) {
                        fileA.clear();
                    }
                    for (int i = 0; i < files.size(); i++) {
                        uploadF(Uri.parse("file:///" + files.get(i).getPath()), files.get(i).getName(), files.get(i).getMimeType());
                    }
                }
                break;
        }
        try {
            mediaPicker.process(requestCode, resultCode, data);

        } catch (NullPointerException e) {

        }
        if (resultCode == Activity.RESULT_OK
                && requestCode == SandriosCamera.RESULT_CODE
                && data != null) {
            if (data.getSerializableExtra(SandriosCamera.MEDIA) instanceof Media) {
                Media media = (Media) data.getSerializableExtra(SandriosCamera.MEDIA);
                MediaMetadataRetriever retriever = new MediaMetadataRetriever();
                retriever.setDataSource(Group.this, Uri.parse(media.getPath()));
                String time = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION);
                long timeInMillisec = Long.parseLong(time);
                retriever.release();
                uploadVideo(Uri.parse("file:///" + media.getPath()), timeInMillisec, media.getPath());
            }

        }


        super.onActivityResult(requestCode, resultCode, data);
    }

    public void afterCompress(String path) {
        //local message
        messidL = mAuth.getCurrentUser().getUid() + "_" + friendId + "_" + String.valueOf(System.currentTimeMillis());
        imageA.add(messidL);
        String locall = encryption.encryptOrNull("file://" + path);
        messagesAdapter.clear();
        messageLocal = new MessageIn(locall, "image", messidL, "..", mAuth.getCurrentUser().getUid(), System.currentTimeMillis(), false, false, "no", encryption.encryptOrNull(Global.avaLocal), false, false, false, "");
        try {
            Global.messGGG.add(messageLocal);
            //local store
            ((AppBack) getApplication()).setchatsdbG(friendId);
        } catch (NullPointerException e) {
            Global.messGGG = new ArrayList<>();
            Global.messGGG.add(messageLocal);
            //local store
            ((AppBack) getApplication()).setchatsdbG(friendId);
        }


        encrypI = "Image";
        encrypI = encryption.encryptOrNull(encrypI);

        //     update last message if dialog exist
        Groups groups = new Groups();
        //       update dialog if not exist
        GroupIn dialog = new GroupIn(Global.currname, Global.currAva, friendId, encrypI, mAuth.getCurrentUser().getUid(), Global.avaLocal, messageLocal.getTime(), 0);
        ArrayList<GroupIn> tempoo = new ArrayList<>();
        tempoo.clear();
        tempoo.add(dialog);
        Global.groupG = dialog;
        Global.DialogonelistG = tempoo;
        Global.Dialogid = friendId;
        Global.DialogM = messageLocal;
        groups.updatedialog(Group.this);
        messagesAdapter.addToEnd(MessageData.getMessagesG(), true);
        messagesAdapter.notifyDataSetChanged();
        messagesList.getLayoutManager().smoothScrollToPosition(messagesList, null, 0);
        ///////
        //      //compress the photo
        File newImageFile = new File(path);
        try {
            compressedImageFile = new Compressor(Group.this)
                    .setMaxHeight(500)
                    .setMaxWidth(500)
                    .setQuality(50)
                    .compressToBitmap(newImageFile);

        } catch (IOException e) {
            e.printStackTrace();
        }
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        compressedImageFile.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] thumbData = baos.toByteArray();
        StorageReference mStorageRef = FirebaseStorage.getInstance().getReference();
        StorageReference riversRef = mStorageRef.child(Global.GROUPS + "/" + friendId + "/" + mAuth.getCurrentUser().getUid() + "/Images/" + mAuth.getCurrentUser().getUid() + friendId + System.currentTimeMillis() + ".jpg");
        UploadTask uploadTask = riversRef.putBytes(thumbData);

        Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
            @Override
            public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                if (!task.isSuccessful()) {
                    throw task.getException();
                }

                // Continue with the task to get the download URL
                return riversRef.getDownloadUrl();
            }
        }).addOnCompleteListener(new OnCompleteListener<Uri>() {
            @Override
            public void onComplete(@NonNull Task<Uri> task) {
                if (task.isSuccessful()) {
                    Uri downloadUrl = task.getResult();
                    message.setText("");
                    currTime = ServerValue.TIMESTAMP;
                    //send owner data to friend
                    mAuth = FirebaseAuth.getInstance();
                    sendIpre(String.valueOf(downloadUrl));
                }
            }
        });
    }

    private void sendIpre(String link) {
        encrypL = link;
        encrypL = encryption.encryptOrNull(encrypL);
        final Map<String, Object> map = new HashMap<>();
        map.put("linkI", encrypL);
        map.put("time", currTime);
        map.put("messId", imageA.get(iq));
        map.put("react", "no");
        map.put("avatar", encryption.encryptOrNull(Global.avaLocal));
        map.put("chat", false);
        map.put("seen", false);
        map.put("type", "image");
        map.put("deleted", false);
        map.put("statue", "✔");
        map.put("reply", encryption.encryptOrNull(""));
        map.put("forw", false);
        map.put("call", false);
        map.put("from", mAuth.getCurrentUser().getUid());
        final String mssgid = imageA.get(iq);
        mData.child(friendId).child(Global.Messages).child(mssgid)
                .updateChildren(map).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Map<String, Object> map2 = new HashMap<>();
                map2.put("lastmessage", encrypI);
                map2.put("lastsender", mAuth.getCurrentUser().getUid());
                map2.put("lastsenderava", Global.avaLocal);
                map2.put("messDate", currTime);

                mData.child(friendId).updateChildren(map2).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        mUserDB.child(mAuth.getCurrentUser().getUid()).child(Global.GROUPS).child(friendId).updateChildren(map2).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                sendMessNotify(encrypI, mssgid);
                                for (int i = 0; i < Global.currGUsers.size(); i++) {
                                    int j = i;
                                    if (!Global.currGUsers.get(i).equals(mAuth.getCurrentUser().getUid())) {
                                        mUserDB.child(Global.currGUsers.get(i)).child(Global.GROUPS).child(friendId).updateChildren(map2).addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                            }
                                        });
                                    }
                                }
                            }
                        });
                    }
                });


                if (iq <= imageA.size() - 2) {
                    //nothing
                } else {
                    iqb = true;

                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                iqb = true;
            }
        });

        if (iq <= imageA.size() - 2)
            iq++;

    }

    private void sendFpre(String link, String filename) {
        encrypL = link;
        encrypL = encryption.encryptOrNull(encrypL);
        final Map<String, Object> map = new HashMap<>();
        map.put("linkF", encrypL);
        map.put("time", currTime);
        map.put("messId", fileA.get(0));
        map.put("react", "no");
        map.put("avatar", encryption.encryptOrNull(Global.avaLocal));
        map.put("chat", false);
        map.put("filename", filename);
        map.put("seen", false);
        map.put("type", "file");
        map.put("deleted", false);
        map.put("statue", "✔");
        map.put("reply", encryption.encryptOrNull(""));
        map.put("forw", false);
        map.put("call", false);
        map.put("from", mAuth.getCurrentUser().getUid());
        final String mssgid = fileA.get(0);
        mData.child(friendId).child(Global.Messages).child(mssgid)
                .updateChildren(map).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {

                Map<String, Object> map2 = new HashMap<>();
                map2.put("lastmessage", encrypF);
                map2.put("lastsender", mAuth.getCurrentUser().getUid());
                map2.put("lastsenderava", Global.avaLocal);
                map2.put("messDate", currTime);

                mData.child(friendId).updateChildren(map2).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        mUserDB.child(mAuth.getCurrentUser().getUid()).child(Global.GROUPS).child(friendId).updateChildren(map2).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                sendMessNotify(encrypF, mssgid);
                                for (int i = 0; i < Global.currGUsers.size(); i++) {
                                    int j = i;
                                    if (!Global.currGUsers.get(i).equals(mAuth.getCurrentUser().getUid())) {
                                        mUserDB.child(Global.currGUsers.get(i)).child(Global.GROUPS).child(friendId).updateChildren(map2).addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                            }
                                        });
                                    }
                                }
                            }
                        });
                    }
                });

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
            }
        });
    }

    private void sendVpre(String link, long time) {
        encrypL = link;
        encrypL = encryption.encryptOrNull(encrypL);
        final Map<String, Object> map = new HashMap<>();
        map.put("linkV", encrypL);
        map.put("time", currTime);
        map.put("messId", messidL);
        map.put("react", "no");
        map.put("avatar", encryption.encryptOrNull(Global.avaLocal));
        map.put("chat", false);
        map.put("duration", getHumanTimeText(time));
        map.put("seen", false);
        map.put("type", "voice");
        map.put("deleted", false);
        map.put("statue", "✔");
        map.put("reply", encryption.encryptOrNull(""));
        map.put("forw", false);
        map.put("call", false);
        map.put("from", mAuth.getCurrentUser().getUid());
        mData.child(friendId).child(Global.Messages).child(messidL)
                .updateChildren(map).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Map<String, Object> map2 = new HashMap<>();
                map2.put("lastmessage", encrypV);
                map2.put("lastsender", mAuth.getCurrentUser().getUid());
                map2.put("lastsenderava", Global.avaLocal);
                map2.put("messDate", currTime);

                mData.child(friendId).updateChildren(map2).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        mUserDB.child(mAuth.getCurrentUser().getUid()).child(Global.GROUPS).child(friendId).updateChildren(map2).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                sendMessNotify(encrypV, messidL);
                                for (int i = 0; i < Global.currGUsers.size(); i++) {
                                    int j = i;
                                    if (!Global.currGUsers.get(i).equals(mAuth.getCurrentUser().getUid())) {
                                        mUserDB.child(Global.currGUsers.get(i)).child(Global.GROUPS).child(friendId).updateChildren(map2).addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                            }
                                        });
                                    }
                                }
                            }
                        });
                    }
                });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });
    }

    private void sendVideopre(String link, long time, String thumbL) {
        encrypL = link;
        encrypL = encryption.encryptOrNull(encrypL);
        final Map<String, Object> map = new HashMap<>();
        map.put("linkVideo", encrypL);
        map.put("time", currTime);
        map.put("messId", messidL);
        map.put("react", "no");
        map.put("avatar", encryption.encryptOrNull(Global.avaLocal));
        map.put("chat", false);
        map.put("thumb", thumbL);
        map.put("duration", getHumanTimeText(time));
        map.put("seen", false);
        map.put("type", "video");
        map.put("deleted", false);
        map.put("statue", "✔");
        map.put("reply", encryption.encryptOrNull(""));
        map.put("forw", false);
        map.put("call", false);
        map.put("from", mAuth.getCurrentUser().getUid());
        mData.child(friendId).child(Global.Messages).child(messidL)
                .updateChildren(map).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Map<String, Object> map2 = new HashMap<>();
                map2.put("lastmessage", encrypVideo);
                map2.put("lastsender", mAuth.getCurrentUser().getUid());
                map2.put("lastsenderava", Global.avaLocal);
                map2.put("messDate", currTime);

                mData.child(friendId).updateChildren(map2).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        mUserDB.child(mAuth.getCurrentUser().getUid()).child(Global.GROUPS).child(friendId).updateChildren(map2).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                sendMessNotify(encrypVideo, messidL);
                                for (int i = 0; i < Global.currGUsers.size(); i++) {
                                    int j = i;
                                    if (!Global.currGUsers.get(i).equals(mAuth.getCurrentUser().getUid())) {
                                        mUserDB.child(Global.currGUsers.get(i)).child(Global.GROUPS).child(friendId).updateChildren(map2).addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                            }
                                        });
                                    }
                                }
                            }
                        });
                    }
                });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });

    }

    private boolean startRecording() {
        mRecorder = new MediaRecorder();
        mRecorder.setAudioChannels(2);
        mRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mRecorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
        mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.HE_AAC);
        mRecorder.setAudioEncodingBitRate(48000);

        mRecorder.setAudioSamplingRate(16000);
        mOutputFile = getOutputFile();
        mOutputFile.getParentFile().mkdirs();
        mRecorder.setOutputFile(mOutputFile.getAbsolutePath());

        try {
            mRecorder.prepare();
            mRecorder.start();
            startAT();
            return true;
        } catch (Exception e) {
            return false;
        }
    }


    protected void stopRecording(boolean saveFile) {
        try {
            mRecorder.stop();
            mRecorder.release();
            mRecorder = null;
            stopAT();
            if (!saveFile && mOutputFile != null) {
                mOutputFile.delete();

            }
        } catch (NullPointerException e) {
            stopAT();
        } catch (RuntimeException e) {
            stopAT();
        }

    }

    private File getOutputFile() {
        return new File(Environment.getExternalStorageDirectory().getAbsolutePath().toString()
                + "/" + getResources().getString(R.string.app_name) + "/Voice Notes/" + "rec_voice_" + String.valueOf(System.currentTimeMillis())
                + ".m4a");
    }

    private String getHumanTimeText(long milliseconds) {
        return String.format("%02d:%02d",
                TimeUnit.MILLISECONDS.toMinutes(milliseconds),
                TimeUnit.MILLISECONDS.toSeconds(milliseconds) -
                        TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(milliseconds)));
    }

    private void editInf() {
        name.setText(Global.currname);
        if (String.valueOf(Global.currAva).equals("no")) {
            Picasso.get()
                    .load(R.drawable.group)
                    .placeholder(R.drawable.placeholder_gray).error(R.drawable.errorimg)

                    .into(ava);
        } else {
            Picasso.get()
                    .load(Global.currAva)
                    .placeholder(R.drawable.placeholder_gray).error(R.drawable.errorimg)

                    .into(ava);
        }
        state.setVisibility(View.GONE);


        typingit();

    }

    public void typingit() {
        if (typingR && !whoT.equals(Global.nameLocal)) {
            state.setVisibility(View.VISIBLE);
            state.setText(whoT + " " + getResources().getString(R.string.typing));
        } else if (recordingR && !whoR.equals(Global.nameLocal)) {
            state.setVisibility(View.VISIBLE);
            state.setText(whoR + " " + getResources().getString(R.string.recording));
        } else {
            state.setVisibility(View.GONE);
        }
    }

    @Override
    public void onResume() {
        open = true;
        pausebreak = false;
        Global.stickerIcon = true;


        try {
            if (shakeDetector != null)
                shakeDetector.start(getBaseContext());
        } catch (NullPointerException e) {

        }

        //resume
        super.onResume();

        AppBack myApp = (AppBack) this.getApplication();
        if (myApp.wasInBackground) {
            //init data
            Map<String, Object> map = new HashMap<>();
            map.put(Global.Online, true);
            if (mAuth.getCurrentUser() != null)
                myData.child(mAuth.getCurrentUser().getUid()).updateChildren(map);
            Global.local_on = true;
            //lock screen
            ((AppBack) getApplication()).lockscreen(((AppBack) getApplication()).shared().getBoolean("lock", false));
        }
        myApp.stopActivityTransitionTimer();
        Global.currentpageid = friendId;
        //clear all notifications
        NotificationManager notificationManager = (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);
        try {
            messagesAdapter.notifyDataSetChanged();
            if (notificationManager != null) {
                notificationManager.cancelAll();
                int count = 0;
                //store it again
                ((AppBack) getApplication()).editSharePrefs().putInt("numN" + mAuth.getCurrentUser().getUid(), count);
                ((AppBack) getApplication()).editSharePrefs().apply();
                ShortcutBadger.applyCount(this, count);
            }
        } catch (NullPointerException e) {
            //nothing
        }
        //read messages
        if (code == 0) {
            zeroCount();
            readM();
        }
        Global.currentactivity = this;
    }

    @Override
    public void onPause() {
        super.onPause();
        open = false;
        Global.currentactivity = null;
        ((AppBack) this.getApplication()).startActivityTransitionTimer();
        pausebreak = true;
        try {
            AudioManager audioManager = (AudioManager) this.getSystemService(Context.AUDIO_SERVICE);
            audioManager.setMode(AudioManager.MODE_NORMAL);
            audioManager.setRingerMode(AudioManager.USE_DEFAULT_STREAM_TYPE );
            audioManager.setSpeakerphoneOn(false);
        }catch (NullPointerException e)
        {

        }
        try {
            if(shakeDetector !=null)
                shakeDetector.stopShakeDetector(getBaseContext());
            timerStopDeleted();
        }
        catch (NullPointerException e)
        {

        }
        try {
            stopRecording(false);
        } catch (RuntimeException e) {
        }

        try {
            for (int i = 0; i < Global.audiolist.size(); i++)
                Global.audiolist.get(i).pause();
        } catch (NullPointerException e) {

        }
        setResult(RESULT_CANCELED);
        Global.currentpageid = "";
    }

    private void getSize() {
        if (message.getLineCount() == message.getMaxLines()) {
            message.setMaxLines((message.getLineCount() + 1));
        }

    }

    private void sendMessNotify(final String message, final String Mid) {
try{
        for (int i = 0; i < Global.currGUsers.size(); i++) {
            if (!Global.currGUsers.get(i).equals(mAuth.getCurrentUser().getUid())) {
                DatabaseReference mTokenget = FirebaseDatabase.getInstance().getReference(Global.tokens);
                int finalI = i;
                mTokenget.child(Global.currGUsers.get(i)).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        Tokens tokens = dataSnapshot.getValue(Tokens.class);

                      //Message maybe not encrypted
                        NotificationModel notificationModel2 = new NotificationModel(Global.currname,encryption.decryptOrNull(message));
                        chatroom.snap.snaplive.Notification.Message message2 = new chatroom.snap.snaplive.Notification.Message(Global.currname,Global.currAva,
                                Global.currGUsers.get(finalI)  ,message,null,friendId,Mid       );
                        FirebaseDatabase.getInstance().getReference(Global.USERS).child( Global.currGUsers.get(finalI)).child(Global.device).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                if (dataSnapshot.exists()){
                                    String isAndroid = dataSnapshot.getValue(String.class);
                                    NotifyManager.MessageGroupNotificationToUser(tokens.getTokens(),isAndroid,message2);
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });


                    }


                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
        }


    } catch (NullPointerException e){


}

    }

    public void goPG(View view) {
        Intent intent = new Intent(this, ProfileGroup.class);
        intent.putExtra("idP", friendId);
        startActivity(intent);
    }


    private String getRealPathFromURI(Uri contentURI) {
        Cursor cursor = getContentResolver().query(contentURI, null, null, null, null);
        if (cursor == null) {
            return contentURI.getPath();
        } else {
            cursor.moveToFirst();
            int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
            return cursor.getString(idx);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case PermUtil.REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Pix.start(this, Options.init().setRequestCode(100));
                } else {
                    Toast.makeText(Group.this, getResources().getString(R.string.approve_upload), Toast.LENGTH_LONG).show();
                }
                return;
            }
        }
    }

    @Override
    protected void onDestroy() {
        if (mAuth.getCurrentUser() != null) {
            query.removeEventListener(child);


            try {
                if(shakeDetector !=null)
                    shakeDetector.destroy(getBaseContext());
                timerStopDeleted();
            }
            catch (NullPointerException e)
            {

            }

            try {
                for (int i = 0; i < Global.audiolist.size(); i++)
                    Global.audiolist.get(i).pause();
            } catch (NullPointerException e) {

            }


            Global.btnid.clear();
            Global.audiolist.clear();
            for (int i = 0; i < Global.messGGG.size(); i++) {
                //check all failed messages
                if (Global.messGGG.get(i).getStatue().equals("..")) {
                    //make it false
                    Global.messGGG.get(i).setStatue("X");
                    ((AppBack) getApplication()).setchatsdbG(friendId);

                    //todo when add retry after online
                    //put to retry
//                    ((AppBack) getApplication()).getRetry(friendId);
//                    Global.messGGG.get(i).setStatue("..");
//                    Global.retryM.add(Global.messGGG.get(i));
//                    ((AppBack) getApplication()).setRetry(friendId);
                }
            }
            messagesAdapter.clear();
            messagesAdapter.addToEnd(MessageData.getMessagesG(), true);
            messagesAdapter.notifyDataSetChanged();
        }
        super.onDestroy();
    }

    void getAvas() {
        if (Global.currGUsers.size() > 0) {
            getAva.child(Global.currGUsers.get(getAvasCount)).child("avatar").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    Global.currGUsersAva.add(dataSnapshot.getValue(String.class));
                    if (getAvasCount == Global.currGUsers.size() - 1) {
                        ((AppBack) getApplication()).setGroupUsersAva(friendId);
                        getAvasCount = 0;
                    } else {
                        getAvasCount += 1;
                        getAvas();
                    }

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }
    }
    public int halbine(ArrayList<GroupIn> ml, String id) {
        int j = 0, i = 0;
        for (i = 0; i < ml.size(); i++) {
            if (ml.get(i).getId().equals(id)) {
                j = 1;
                break;
            }

        }
        if (j == 1)
            return i;
        else
            return -1;

    }

    public void deletebyId(String Fid) {
        int i = 0;
        for (i = 0; i < Global.diaGGG.size(); i++) {
            if (Global.diaGGG.get(i).getId().equals(Fid)) {
                Global.diaGGG.remove(i);
                break;
            }

        }
    }

    @Override
    public void onStickerClick(Bitmap bitmap, int position) {
        if (Global.check_int(Group.this)) {
            iqb = false;
            imageA.clear();
            iq = 0;
        }
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
        byte[] data = baos.toByteArray();
        stickerUpload(data, position);
    }

    public void stickerUpload(byte[] thumbData, int position) {
        //local message
        messidL = mAuth.getCurrentUser().getUid() + "_" + friendId + "_" + String.valueOf(System.currentTimeMillis());
        imageA.add(messidL);
        String locall = encryption.encryptOrNull(getURLForResource(position));
        messagesAdapter.clear();
        messageLocal = new MessageIn(locall, "image", messidL, "..", mAuth.getCurrentUser().getUid(), System.currentTimeMillis(), false, false, "no", encryption.encryptOrNull(Global.avaLocal), false, false, false, "");
        try {
            Global.messGGG.add(messageLocal);
            //local store
            ((AppBack) getApplication()).setchatsdbG(friendId);
        } catch (NullPointerException e) {
            Global.messGGG = new ArrayList<>();
            Global.messGGG.add(messageLocal);
            //local store
            ((AppBack) getApplication()).setchatsdbG(friendId);
        }


        encrypI = "Sticker";
        encrypI = encryption.encryptOrNull(encrypI);

        //     update last message if dialog exist
        Groups groups = new Groups();
        //       update dialog if not exist
        GroupIn dialog = new GroupIn(Global.currname, Global.currAva, friendId, encrypI, mAuth.getCurrentUser().getUid(), Global.avaLocal, messageLocal.getTime(), 0);
        ArrayList<GroupIn> tempoo = new ArrayList<>();
        tempoo.clear();
        tempoo.add(dialog);
        Global.groupG = dialog;
        Global.DialogonelistG = tempoo;
        Global.Dialogid = friendId;
        Global.DialogM = messageLocal;
        groups.updatedialog(Group.this);
        messagesAdapter.addToEnd(MessageData.getMessagesG(), true);
        messagesAdapter.notifyDataSetChanged();
        messagesList.getLayoutManager().smoothScrollToPosition(messagesList, null, 0);


        StorageReference mStorageRef = FirebaseStorage.getInstance().getReference();
        StorageReference riversRef = mStorageRef.child(Global.GROUPS + "/" + friendId + "/" + mAuth.getCurrentUser().getUid() + "/Images/" + mAuth.getCurrentUser().getUid() + friendId + System.currentTimeMillis() + ".png");
        UploadTask uploadTask = riversRef.putBytes(thumbData);

        Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
            @Override
            public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                if (!task.isSuccessful()) {
                    throw task.getException();
                }

                // Continue with the task to get the download URL
                return riversRef.getDownloadUrl();
            }
        }).addOnCompleteListener(new OnCompleteListener<Uri>() {
            @Override
            public void onComplete(@NonNull Task<Uri> task) {
                if (task.isSuccessful()) {
                    Uri downloadUrl = task.getResult();
                    message.setText("");
                    currTime = ServerValue.TIMESTAMP;
                    //send owner data to friend
                    mAuth = FirebaseAuth.getInstance();
                    sendIpre(String.valueOf(downloadUrl));
                }
            }
        });
    }


    public String getURLForResource(int resourceId) {
        return Uri.parse("android.resource://" + R.class.getPackage().getName() + "/" + resourceId).toString();
    }

    public static void refreshAdapter() {
        try {
            messagesAdapter.notifyDataSetChanged();

        } catch (NullPointerException e) {

        }

    }

    @Override
    public void onPickMediaStateChanged(boolean inProgress) {

    }

    @Override
    public void onPickMediaResult(@NonNull MediaResult result, @Nullable CharSequence errorMsg) {
        MediaMetadataRetriever retriever = new MediaMetadataRetriever();
        retriever.setDataSource(Group.this, Uri.parse(result.getPath()));
        String time = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION);
        long timeInMillisec = Long.parseLong(time);
        retriever.release();
        uploadVideo(Uri.parse("file:///" + result.getPath()), timeInMillisec, result.getPath());

    }

    private void deleteTodoItem() {
        //Swipe to delete currentTodo Item
        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0,
                ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {

            private Drawable replyI = ContextCompat.getDrawable(getApplicationContext(), R.drawable.reply);
            private Drawable forwardI = ContextCompat.getDrawable(getApplicationContext(), R.drawable.forward);


            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                int position = (Global.messGGG.size() - viewHolder.getAdapterPosition()) - 1;
                messagesAdapter.notifyDataSetChanged();

                if (direction == 8) {
                    try {
                        if (messagesAdapter.halbins2(Global.messGGG.get(position).getMessId()) != null && !messagesAdapter.halbins2(Global.messGGG.get(position).getMessId()).getStatus().equals("..") && !messagesAdapter.halbins2(Global.messGGG.get(position).getMessId()).getStatus().equals("X")) {
                            replyM = messagesAdapter.halbins2(Global.messGGG.get(position).getMessId());
                            replyBollean = true;
                            replyLy.setVisibility(View.VISIBLE);
                            if (messagesAdapter.halbins2(Global.messGGG.get(position).getMessId()).getReply().isEmpty()) {
                                switch (messagesAdapter.halbins2(Global.messGGG.get(position).getMessId()).getType()) {
                                    case "text":
                                        replyT.setText(messagesAdapter.halbins2(Global.messGGG.get(position).getMessId()).getText());
                                        break;
                                    case "image":
                                        replyT.setText(getResources().getString(R.string.image));
                                        break;
                                    case "video":
                                        replyT.setText(getResources().getString(R.string.video));
                                        break;
                                    case "voice":
                                        replyT.setText(getResources().getString(R.string.voice));
                                        break;
                                    case "file":
                                        replyT.setText(getResources().getString(R.string.file));
                                        break;
                                    case "map":
                                        replyT.setText(getResources().getString(R.string.map_location));
                                        break;
                                    case "gif":
                                        replyT.setText("GIF");
                                        break;
                                }
                            } else {
                                replyT.setText(messagesAdapter.halbins2(Global.messGGG.get(position).getMessId()).getReply());
                            }


                            if (Global.messGGG.get(position).getFrom().equals(mAuth.getCurrentUser().getUid()))
                                replyU.setText(getResources().getString(R.string.you));
                            else
                                replyU.setText(Global.currname);

                        } else {
                            Toast.makeText(Group.this, getString(R.string.cant_reply), Toast.LENGTH_SHORT).show();
                            replyBollean = false;
                            replyLy.setVisibility(View.GONE);
                        }
                    } catch (NullPointerException e) {
                        Toast.makeText(Group.this, getString(R.string.cant_reply), Toast.LENGTH_SHORT).show();
                        replyBollean = false;
                        replyLy.setVisibility(View.GONE);
                    }


                } else if (direction == 4) {
                    try {
                        if (messagesAdapter.halbins2(Global.messGGG.get(position).getMessId()) != null && !messagesAdapter.halbins2(Global.messGGG.get(position).getMessId()).getStatus().equals("..") && !messagesAdapter.halbins2(Global.messGGG.get(position).getMessId()).getStatus().equals("X")) {
                            Global.forwardMessage = messagesAdapter.halbins2(Global.messGGG.get(position).getMessId());
                            startActivity(new Intent(Group.this, Forward.class));
                        } else
                            Toast.makeText(Group.this, getString(R.string.cant_forw), Toast.LENGTH_SHORT).show();
                    } catch (NullPointerException e) {
                        Toast.makeText(Group.this, getString(R.string.cant_forw), Toast.LENGTH_SHORT).show();

                    }

                }
            }

            @Override
            public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView recyclerView,
                                    @NonNull RecyclerView.ViewHolder viewHolder, float dX, float dY,
                                    int actionState, boolean isCurrentlyActive) {
                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
                View itemView = viewHolder.itemView;

                if (dX > 0) { // Swiping to the right
                    replyI.setBounds(0, 0, 0, 0);
                    forwardI.setBounds(0, 0, 0, 0);
                } else if (dX < 0) { // Swiping to the left
                    replyI.setBounds(0, 0, 0, 0);
                    forwardI.setBounds(0, 0, 0, 0);
                } else { // view is unSwiped
                    replyI.setBounds(0, 0, 0, 0);
                    forwardI.setBounds(0, 0, 0, 0);
                }
                replyI.draw(c);
                forwardI.draw(c);
            }
        }).attachToRecyclerView(messagesList);
    }

    public void goReply(String messageReply) {
        {
            replyBollean = false;
            replyLy.setVisibility(View.GONE);
            Map<String, Object> map = new HashMap<>();

            Object currT = ServerValue.TIMESTAMP;
            messidNew = mAuth.getCurrentUser().getUid() + "_" + friendId + "_" + System.currentTimeMillis();


            if (replyM.getReply().isEmpty()) {
                switch (replyM.getType()) {
                    case "text":
                        messageLocal = new MessageIn(encryption.encryptOrNull(replyM.getText()), "text", "..", mAuth.getCurrentUser().getUid(), System.currentTimeMillis(), false, false, messidNew, "no", encryption.encryptOrNull(Global.avaLocal), false, false, false, encryption.encryptOrNull(messageReply));

                        map.put("message", encryption.encryptOrNull(replyM.getText()));
                        map.put("time", currT);
                        map.put("react", "no");
                        map.put("avatar", encryption.encryptOrNull(Global.avaLocal));
                        map.put("seen", false);
                        map.put("type", "text");
                        map.put("deleted", false);
                        map.put("statue", "✔");
                        map.put("reply", encryption.encryptOrNull(messageReply));
                        map.put("forw", false);
                        map.put("call", false);
                        map.put("from", mAuth.getCurrentUser().getUid());
                        map.put("chat", false);
                        map.put("messId", messidNew);
                        updateData(encryption.encryptOrNull(messageReply), messidNew, currT, map);
                        break;
                    case "map":
                        messageLocal = new MessageIn(encryption.encryptOrNull(replyM.getMap().getLocation()), "..", mAuth.getCurrentUser().getUid(), System.currentTimeMillis(), false, "no", false, messidNew, "map", encryption.encryptOrNull(Global.avaLocal), false, false, false, encryption.encryptOrNull(messageReply));

                        map.put("location", encryption.encryptOrNull(replyM.getMap().getLocation()));
                        map.put("time", currT);
                        map.put("react", "no");
                        map.put("avatar", encryption.encryptOrNull(Global.avaLocal));
                        map.put("seen", false);
                        map.put("type", "map");
                        map.put("deleted", false);
                        map.put("statue", "✔");
                        map.put("reply", encryption.encryptOrNull(messageReply));
                        map.put("forw", false);
                        map.put("call", false);
                        map.put("from", mAuth.getCurrentUser().getUid());
                        map.put("chat", false);
                        map.put("messId", messidNew);
                        updateData(encryption.encryptOrNull(messageReply), messidNew, currT, map);
                        break;
                    case "voice":
                        messageLocal = new MessageIn(encryption.encryptOrNull(replyM.getVoice().getUrl()), "..", mAuth.getCurrentUser().getUid(), System.currentTimeMillis(), false, false, messidNew, "voice", "no", replyM.getVoice().getDuration(), encryption.encryptOrNull(Global.avaLocal), false, false, false, encryption.encryptOrNull(messageReply));

                        map.put("linkV", encryption.encryptOrNull(replyM.getVoice().getUrl()));
                        map.put("time", currT);
                        map.put("react", "no");
                        map.put("avatar", encryption.encryptOrNull(Global.avaLocal));
                        map.put("duration", replyM.getVoice().getDuration());
                        map.put("seen", false);
                        map.put("type", "voice");
                        map.put("deleted", false);
                        map.put("statue", "✔");
                        map.put("reply", encryption.encryptOrNull(messageReply));
                        map.put("forw", false);
                        map.put("call", false);
                        map.put("from", mAuth.getCurrentUser().getUid());
                        map.put("chat", false);
                        map.put("messId", messidNew);
                        updateData(encryption.encryptOrNull(messageReply), messidNew, currT, map);
                        break;
                    case "video":
                        messageLocal = new MessageIn(encryption.encryptOrNull(replyM.getVideo().getUrl()), "..", mAuth.getCurrentUser().getUid(), System.currentTimeMillis(), false, false, messidNew, replyM.getVideo().getDuration(), replyM.getVideo().getThumb(), "no", encryption.encryptOrNull(Global.avaLocal), false, false, false, encryption.encryptOrNull(messageReply));

                        map.put("linkVideo", encryption.encryptOrNull(replyM.getVideo().getUrl()));
                        map.put("time", currT);
                        map.put("react", "no");
                        map.put("avatar", encryption.encryptOrNull(Global.avaLocal));
                        map.put("thumb", replyM.getVideo().getThumb());
                        map.put("duration", replyM.getVideo().getDuration());
                        map.put("seen", false);
                        map.put("type", "video");
                        map.put("deleted", false);
                        map.put("statue", "✔");
                        map.put("reply", encryption.encryptOrNull(messageReply));
                        map.put("forw", false);
                        map.put("call", false);
                        map.put("from", mAuth.getCurrentUser().getUid());
                        map.put("chat", false);
                        map.put("messId", messidNew);
                        updateData(encryption.encryptOrNull(messageReply), messidNew, currT, map);
                        break;
                    case "file":
                        messageLocal = new MessageIn(encryption.encryptOrNull(replyM.getFile().getUrl()), "..", System.currentTimeMillis(), false, false, messidNew, "file", replyM.getFile().getFilename(), mAuth.getCurrentUser().getUid(), "no", encryption.encryptOrNull(Global.avaLocal), false, false, false, encryption.encryptOrNull(messageReply));

                        map.put("linkF", encryption.encryptOrNull(replyM.getFile().getUrl()));
                        map.put("time", currT);
                        map.put("react", "no");
                        map.put("avatar", encryption.encryptOrNull(Global.avaLocal));
                        map.put("filename", replyM.getFile().getFilename());
                        map.put("seen", false);
                        map.put("type", "file");
                        map.put("deleted", false);
                        map.put("statue", "✔");
                        map.put("reply", encryption.encryptOrNull(messageReply));
                        map.put("forw", false);
                        map.put("call", false);
                        map.put("from", mAuth.getCurrentUser().getUid());
                        map.put("chat", false);
                        map.put("messId", messidNew);
                        updateData(encryption.encryptOrNull(messageReply), messidNew, currT, map);


                        break;
                    case "image":
                        messageLocal = new MessageIn(encryption.encryptOrNull(replyM.getImageUrl()), "image", messidNew, "..", mAuth.getCurrentUser().getUid(), System.currentTimeMillis(), false, false, "no", encryption.encryptOrNull(Global.avaLocal), false, false, false, encryption.encryptOrNull(messageReply));

                        map.put("linkI", encryption.encryptOrNull(replyM.getImageUrl()));
                        map.put("time", currT);
                        map.put("react", "no");
                        map.put("avatar", encryption.encryptOrNull(Global.avaLocal));
                        map.put("seen", false);
                        map.put("type", "image");
                        map.put("deleted", false);
                        map.put("statue", "✔");
                        map.put("reply", encryption.encryptOrNull(messageReply));
                        map.put("forw", false);
                        map.put("call", false);
                        map.put("from", mAuth.getCurrentUser().getUid());
                        map.put("chat", false);
                        map.put("messId", messidNew);
                        if (!replyM.getImageUrl().contains(".png"))
                            updateData(encryption.encryptOrNull(messageReply), messidNew, currT, map);
                        else
                            updateData(encryption.encryptOrNull(messageReply), messidNew, currT, map);
                        break;


                }
            } else {
                messageLocal = new MessageIn(encryption.encryptOrNull(replyM.getReply()), "text", "..", mAuth.getCurrentUser().getUid(), System.currentTimeMillis(), false, false, messidNew, "no", encryption.encryptOrNull(Global.avaLocal), false, false, false, encryption.encryptOrNull(messageReply));

                map.put("message", encryption.encryptOrNull(replyM.getReply()));
                map.put("time", currT);
                map.put("react", "no");
                map.put("avatar", encryption.encryptOrNull(Global.avaLocal));
                map.put("seen", false);
                map.put("type", "text");
                map.put("deleted", false);
                map.put("statue", "✔");
                map.put("reply", encryption.encryptOrNull(messageReply));
                map.put("forw", false);
                map.put("call", false);
                map.put("from", mAuth.getCurrentUser().getUid());
                map.put("chat", true);
                map.put("messId", messidNew);
                updateData(encryption.encryptOrNull(messageReply), messidNew, currT, map);
            }


        }

    }

    public void updateData(String mess, String messid, Object currTime, Map<String, Object> MessMap) {
        if (mAuth.getCurrentUser() != null) {


            //local message
            messagesAdapter.clear();
            try {
                Global.messGGG.add(messageLocal);
                //local store
                ((AppBack) getApplication()).setchatsdbG(friendId);
            } catch (NullPointerException e) {
                Global.messGGG = new ArrayList<>();
                Global.messGGG.add(messageLocal);
                //local store
                ((AppBack) getApplication()).setchatsdbG(friendId);
            }

            //     update last message if dialog exist
            Groups groups = new Groups();
            //       update dialog if not exist
            GroupIn dialog = new GroupIn(Global.currname, Global.currAva, friendId, mess, mAuth.getCurrentUser().getUid(), Global.avaLocal, messageLocal.getTime(), 0);
            ArrayList<GroupIn> tempoo = new ArrayList<>();
            tempoo.clear();
            tempoo.add(dialog);
            Global.groupG = dialog;
            Global.DialogonelistG = tempoo;
            Global.Dialogid = friendId;
            Global.DialogM = messageLocal;
            groups.updatedialog(Group.this);


            messagesAdapter.addToEnd(MessageData.getMessagesG(), true);
            messagesAdapter.notifyDataSetChanged();
            messagesList.getLayoutManager().smoothScrollToPosition(messagesList, null, 0);
            ///////


            mData.child(friendId).child(Global.Messages).child(messid)
                    .updateChildren(MessMap).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    Map<String, Object> map2 = new HashMap<>();
                    map2.put("lastmessage", mess);
                    map2.put("lastsender", mAuth.getCurrentUser().getUid());
                    map2.put("lastsenderava", Global.avaLocal);
                    map2.put("messDate", currTime);

                    mData.child(friendId).updateChildren(map2).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {

                            mUserDB.child(mAuth.getCurrentUser().getUid()).child(Global.GROUPS).child(friendId).updateChildren(map2).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    sendMessNotify(mess, messid);
                                    for (int i = 0; i < Global.currGUsers.size(); i++) {
                                        int j = i;
                                        if (!Global.currGUsers.get(i).equals(mAuth.getCurrentUser().getUid())) {

                                            mUserDB.child(Global.currGUsers.get(i)).child(Global.GROUPS).child(friendId).updateChildren(map2).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void aVoid) {

                                                }
                                            });
                                        }

                                    }
                                }
                            });
                        }
                    });
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {

                }
            });


        }
    }

    public static void replyMOut(Message message, Context con) {
        try {
            if (message != null && !message.getStatus().equals("..") && !message.getStatus().equals("X")) {
                replyM = message;
                replyBollean = true;
                replyLy.setVisibility(View.VISIBLE);
                if (message.getReply().isEmpty()) {
                    switch (message.getType()) {
                        case "text":
                            replyT.setText(message.getText());
                            break;
                        case "image":
                            replyT.setText(con.getResources().getString(R.string.image));
                            break;
                        case "video":
                            replyT.setText(con.getResources().getString(R.string.video));
                            break;
                        case "voice":
                            replyT.setText(con.getResources().getString(R.string.voice));
                            break;
                        case "file":
                            replyT.setText(con.getResources().getString(R.string.file));
                            break;
                        case "map":
                            replyT.setText(con.getResources().getString(R.string.map_location));
                            break;
                        case "gif":
                            replyT.setText("GIF");
                            break;
                    }
                } else {
                    replyT.setText(message.getReply());
                }


                if (message.getId().equals(mAuth.getCurrentUser().getUid()))
                    replyU.setText(con.getResources().getString(R.string.you));
                else
                    replyU.setText(Global.currname);

            } else {
                Toast.makeText(con, con.getString(R.string.cant_reply), Toast.LENGTH_SHORT).show();
                replyBollean = false;
                replyLy.setVisibility(View.GONE);

            }
        } catch (NullPointerException e) {
            Toast.makeText(con, con.getString(R.string.cant_reply), Toast.LENGTH_SHORT).show();
            replyBollean = false;
            replyLy.setVisibility(View.GONE);
        }
    }

    public static void timerForDeleted() {

        timerStopDeleted();

        counter = new CountDownTimer(duration, tick) {

            public void onTick(long millisUntilFinished) {
            }

            public void onFinish() {
                Global.lastDeletedMessage = null;


            }
        }.start();
    }

    public static void timerStopDeleted() {
        try {
            if (counter != null)
                counter.cancel();
        } catch (NullPointerException e) {

        }
    }

    public void onShakeDelete() {
        ShakeOptions options = new ShakeOptions()
                .background(false)
                .interval(1000)
                .shakeCount(2)
                .sensibility(2.0f);

        shakeDetector = new ShakeDetector(options).start(this, new ShakeCallback() {
            @Override
            public void onShake() {
                if (Global.lastDeletedMessage != null && Global.check_int(Group.this)) {
                    if (Global.lastDeletedMessage.getMessid().contains(friendId)) {

                        try {
                            Map<String, Object> map = new HashMap<>();
                            map.put("deleted", false);
                            map.put("message", encryption.encryptOrNull(Global.lastDeletedMessage.getText()));
                            map.put("type", Global.lastDeletedMessage.getType());
                            mData.child(friendId).child(Global.Messages).child(Global.lastDeletedMessage.getMessid()).updateChildren(map).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    try {
                                        if (messagesAdapter.halbine(Global.messGGG, Global.lastDeletedMessage.getMessid()) == Global.messGGG.size() - 1)
                                            kameDeleteGG();
                                        else {
                                            Global.lastDeletedMessage = null;
                                            timerForDeleted();
                                        }
                                    }catch (NullPointerException e)
                                    {

                                    }

                                }
                            });
                        } catch (NullPointerException e) {

                        }

                    }
                }


            }
        });
    }

    private void kameDeleteGG() {
        Map<String, Object> map = new HashMap<>();
        map.put("lastmessage", encryption.encryptOrNull(getString(R.string.mess_edited)));
        map.put("lastsender", mAuth.getCurrentUser().getUid());
        map.put("lastsenderava", Global.avaLocal);
        for (int i = 0; i < Global.currGUsers.size(); i++) {
            int finalI = i;
            mUserDB.child(Global.currGUsers.get(i)).child(Global.GROUPS).child(friendId).updateChildren(map).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {

                    if (finalI == Global.currGUsers.size() - 1) {
                        Global.lastDeletedMessage = null;
                        timerForDeleted();
                    }
                }
            });
        }


    }

}