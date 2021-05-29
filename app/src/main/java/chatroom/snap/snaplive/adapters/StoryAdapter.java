package chatroom.snap.snaplive.adapters;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;

import chatroom.snap.snaplive.R;
import chatroom.snap.snaplive.global.Global;
import chatroom.snap.snaplive.lists.StoryListRetr;
import chatroom.snap.snaplive.story.StoryView;

public class StoryAdapter extends RecyclerView.Adapter<StoryAdapter.Holder> {
Context context;
FirebaseAuth mAuth;
ArrayList<StoryListRetr> array;


    public StoryAdapter(ArrayList<StoryListRetr> array) {
        this.array = array;
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.story_raw, null, false);
        context = parent.getContext().getApplicationContext();
        mAuth = FirebaseAuth.getInstance();
        return new Holder(layoutView);
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int position) {


        if (mAuth.getCurrentUser() != null) {
            if (!Global.DARKSTATE) {
                holder.username.setTextColor(context.getResources().getColor(R.color.black));
            } else {
                holder.username.setTextColor(context.getResources().getColor(R.color.white));
            }
        }

        if(array.get(position).getListS().size()>0) {
            holder.storyView.setImageUris(array.get(position).getListS(), context);
            holder.storyView.calculateSweepAngle(array.get(position).getListS().size(),context);

        }
        String name = array.get(position).getName();
        if (name.length() > Global.STORY_NAME_LENTH)
            name = name.substring(0, Global.STORY_NAME_LENTH) + "...";

        holder.username.setText(name);



    }

    @Override
    public int getItemCount() {
        return array.size();
    }

    class  Holder extends RecyclerView.ViewHolder
    {
        StoryView storyView;
        TextView username;
        Holder(View itemView) {
            super(itemView);
            storyView = itemView.findViewById(R.id.storyView);
            username = itemView.findViewById(R.id.username);
        }
    }


}
