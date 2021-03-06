package ru.wtw.moreliatalkclient;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ScrollView;
import android.widget.TextView;


public class MainActivity extends AppCompatActivity {
    private Network network;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        network = new Network(MainActivity.this);
        TextView chat = findViewById(R.id.chat);
        chat.setText("");
        Bundle extras = getIntent().getExtras();
        if(extras != null) {
            Log.i("SERVER","Extra");
            network.setUsername(extras.getString("username"));
            network.setPassword(extras.getString("password"));
            network.setServername(extras.getString("servername"));
            network.connect();
        }

        final View activityRootView = findViewById(R.id.ActivityLayout);
        activityRootView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            public void onGlobalLayout() {
                int heightDiff = activityRootView.getRootView().getHeight() - activityRootView.getHeight();
                if (heightDiff > 100) {
                    final TextView chat = ((TextView) findViewById(R.id.chat));
                    final ScrollView chatScroller = findViewById(R.id.chatScroller);
                    chatScroller.post(new Runnable()
                    {
                        public void run()
                        {
                            chatScroller.smoothScrollTo(0, chat.getBottom());
                        }
                    });
                }
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        ImageButton sendButton = findViewById(R.id.sendButton);
        final TextView chat = ((TextView) findViewById(R.id.chat));
        final ScrollView chatScroller = findViewById(R.id.chatScroller);
        chatScroller.post(new Runnable()
        {
            public void run()
            {
                chatScroller.smoothScrollTo(0, chat.getBottom());
            }
        });

        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (network.isConnected()) {
                    EditText editSend = findViewById(R.id.editSend);
                    String text=editSend.getText().toString().trim();
                    if (!text.isEmpty()) {
                        network.sendMessage(text);
                    }
                    editSend.setText("");
                }
            }
        });
    }
}