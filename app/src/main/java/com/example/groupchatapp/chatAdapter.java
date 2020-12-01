package com.example.groupchatapp;

import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DatabaseReference;


import java.util.List;

public class chatAdapter extends RecyclerView.Adapter<chatAdapter.ChatViewHolder> {

    List<chatData> messages;
    Context context;
    DatabaseReference messageDb;



    public chatAdapter(Context context, List<chatData> list, DatabaseReference messageDb) {
        this.context = context;
        this.messages = list;
        this.messageDb = messageDb;
    }

    @NonNull
    @Override
    public ChatViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        // Inflate the layout
        View chatView = inflater.inflate(R.layout.chat_text_layout, parent, false);
        ChatViewHolder viewHolder = new ChatViewHolder(chatView);

        return viewHolder;
    }


    @Override
    public void onBindViewHolder(@NonNull ChatViewHolder holder, int position) {

        chatData message = messages.get(position);
        if(message.getName().equals(AllMethods.name)){
            holder.title.setText("You:" + message.getMessage());
            holder.title.setGravity(Gravity.START);
            holder.linearLayout.setBackgroundColor(Color.parseColor("#EF9373"));
        }
        else {
            holder.title.setText(message.getName() + ":" +  message.getMessage());
            holder.deleteButton.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return messages.size();
    }

    public class ChatViewHolder extends RecyclerView.ViewHolder {
        TextView title;
        ImageButton deleteButton;
        LinearLayout linearLayout;

        public ChatViewHolder(@NonNull View itemView) {
            super(itemView);

            title = (TextView) itemView
                    .findViewById(R.id.tvTitle);
            deleteButton
                    = (ImageButton) itemView
                    .findViewById(R.id.ibDelete);
            linearLayout = (LinearLayout) itemView.findViewById(R.id.idMessage);

            deleteButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    messageDb.child(messages.get(getAdapterPosition()).getKey()).removeValue();
                }
            });

        }
    }
}
