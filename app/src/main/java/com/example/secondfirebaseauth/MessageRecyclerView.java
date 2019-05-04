package com.example.secondfirebaseauth;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import org.w3c.dom.Text;

import java.util.List;

public class MessageRecyclerView extends RecyclerView.Adapter<MessageRecyclerView.MyViewHolder> {

    Context context;
    List<MessageContainer> messageList;
    FirebaseUser user;
    ListItemClickListener listItemClickListener;
    View view;

    public interface ListItemClickListener {
        void onListItemClick(int clickedItemIndex);
    }

    public MessageRecyclerView(Context context, List<MessageContainer> messageList,ListItemClickListener listItemClickListener) {
        this.context = context;
        this.messageList = messageList;
        this.listItemClickListener = listItemClickListener;
        user = FirebaseAuth.getInstance().getCurrentUser();
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        view = LayoutInflater.from(context).inflate(R.layout.message_activity, viewGroup, false);
        MyViewHolder myViewHolder = new MyViewHolder(view);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int i) {
        MessageContainer messageContainer = messageList.get(i);

        if (user.getEmail().equals(messageContainer.getChatName())) {
            myViewHolder.r1.setVisibility(View.GONE);
            myViewHolder.r2.setVisibility(View.VISIBLE);
            myViewHolder.name2.setText(messageContainer.getChatName());

            if(messageContainer.chatImage != null) {
                myViewHolder.sendImage.setVisibility(View.VISIBLE);
                myViewHolder.message2.setVisibility(View.GONE);
                Glide.with(context).load(messageContainer.chatImage).into(myViewHolder.sendImage);
            }else{
                myViewHolder.sendImage.setVisibility(View.GONE);
                myViewHolder.message2.setVisibility(View.VISIBLE);
                myViewHolder.message2.setText(messageContainer.getChatMessage());
            }
        } else {
            myViewHolder.r1.setVisibility(View.VISIBLE);
            myViewHolder.r2.setVisibility(View.GONE);
            myViewHolder.name.setText(messageContainer.getChatName());

            if(messageContainer.getChatImage() != null) {
                myViewHolder.receivedImage.setVisibility(View.VISIBLE);
                myViewHolder.message.setVisibility(View.GONE);
                Glide.with(context).load(messageContainer.chatImage).into(myViewHolder.receivedImage);
            }else{
                myViewHolder.receivedImage.setVisibility(View.GONE);
                myViewHolder.message.setVisibility(View.VISIBLE);
                myViewHolder.message.setText(messageContainer.getChatMessage());
            }
        }
    }

    @Override
    public int getItemCount() {
        return messageList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView name, name2;
        TextView message, message2;
        LinearLayout r1, r2;
        ImageView receivedImage,sendImage;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            name = (TextView) itemView.findViewById(R.id.user_name_in_message);
            name2 = (TextView) itemView.findViewById(R.id.user_name_in_message2);
            message = (TextView) itemView.findViewById(R.id.user_message);
            message2 = (TextView) itemView.findViewById(R.id.user_message2);
            r1 = itemView.findViewById(R.id.linearLayout1);
            r2 = itemView.findViewById(R.id.linearLayout2);
            receivedImage = itemView.findViewById(R.id.received_user_image);
            sendImage = itemView.findViewById(R.id.send_user_image);

            sendImage.setOnClickListener(this);
            receivedImage.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            listItemClickListener.onListItemClick(getAdapterPosition());
        }
    }

}
