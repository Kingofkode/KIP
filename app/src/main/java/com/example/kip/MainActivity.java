package com.example.kip;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;

import com.example.kip.models.Conversation;
import com.example.kip.models.Message;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.List;

public class MainActivity extends AppCompatActivity {

  private static final String TAG = "MainActivity";

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    queryConversations();
  }

  private void queryConversations() {
    ParseQuery<Conversation> conversationQuery = ParseQuery.getQuery(Conversation.class);
    conversationQuery.include(Conversation.KEY_MEMBER_IDS);
    conversationQuery.include(Conversation.KEY_LAST_MESSAGE);

    conversationQuery.findInBackground(new FindCallback<Conversation>() {
      @Override
      public void done(List<Conversation> conversations, ParseException e) {
        if (e != null) {
          Log.e(TAG, "Issue with getting conversations", e);
          return;
        }

        for (Conversation conversation : conversations) {
          try {
            ParseUser sender = conversation.getLastMessage().getSender().fetchIfNeeded();
            Message message = conversation.getLastMessage();
            // Todo: Populate RecyclerView with data
          } catch (ParseException ex) {
            ex.printStackTrace();
          }
        }
      }
    });

  }
}