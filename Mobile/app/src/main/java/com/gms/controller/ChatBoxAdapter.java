package com.gms.controller;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;


public class ChatBoxAdapter extends RecyclerView.Adapter<ChatBoxAdapter.MyViewHolder>
{
    private List<ChatMessage> MessageList;



    public ChatBoxAdapter(List<ChatMessage> MessagesList)
    {
        this.MessageList = MessagesList;
    }

    @Override
    public int getItemCount() {
        return MessageList.size();
    }

    @Override
    public ChatBoxAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.message_item, parent, false);

        return new ChatBoxAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final ChatBoxAdapter.MyViewHolder holder, final int position) {
        final ChatMessage message = MessageList.get(position);
        holder.setMessage(message.getNickname(), message.getMessage());
    }


    public class MyViewHolder extends RecyclerView.ViewHolder
    {
        public TextView nickname;
//        public TextView message;
        public TextView mMessageViewTheir;
        public TextView mMessageViewMe;


        public MyViewHolder(View view) {
            super(view);

            nickname = (TextView) view.findViewById(R.id.editTextNickname);
            mMessageViewTheir = (TextView) itemView.findViewById(R.id.textViewMessageTheir);
            mMessageViewMe = (TextView) itemView.findViewById(R.id.textViewMessageMe);
        }

        public void setMessage(String nick, String message)
        {
            if (mMessageViewTheir == null) return;
            if (mMessageViewMe == null) return;

            String formattedDate = new SimpleDateFormat("mm:ss:a", Locale.getDefault()).format(new Date());

            if(message.substring(0,1).equals("S")) {

                message = message.substring(1);
                if(message.equals("")) return;
                mMessageViewMe.setVisibility(View.VISIBLE);
                mMessageViewMe.setText("" + message);
                mMessageViewTheir.setVisibility(View.GONE);
                nickname.setVisibility(View.GONE);

            }
            else
            {
                if(message.equals("")) return;

                nickname.setText(nick);
                mMessageViewTheir.setVisibility(View.VISIBLE);
                mMessageViewTheir.setText("" + message);
                mMessageViewMe.setVisibility(View.GONE);
            }
        }

    }

}