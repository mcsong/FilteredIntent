package net.sjava.filteredintent.app;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import net.sjava.appstore.PlayAppStore;
import net.sjava.filteredintent.FilteredIntent;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private Button mTextShareButton;
    private Button mImageShareButton;
    private Button mImageShareWithoudAppButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mTextShareButton = (Button)findViewById(R.id.main_test01_btn);
        mImageShareButton = (Button)findViewById(R.id.main_test02_btn);
        mImageShareWithoudAppButton = (Button)findViewById(R.id.main_test03_btn);


        mTextShareButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent shareIntent = new Intent();
                shareIntent.setAction(Intent.ACTION_SEND);
                shareIntent.putExtra(Intent.EXTRA_SUBJECT, "share intent subject");
                shareIntent.putExtra(Intent.EXTRA_TEXT, "share intent value");
                shareIntent.setType("text/plain");

                // dummy
                //String[] filters = new String[]{"1222222222222222222222"};

                // real
                String[] filters = new String[]{"twitter", "facebook", "kakao.talk", "com.facebook.orca", "com.tencent.mm"};

                FilteredIntent filteredIntent = FilteredIntent.newInstance(MainActivity.this, shareIntent);
                filteredIntent.startIntent("Share sns", filters);

                //FilteredIntent. .create(MainActivity.this, shareIntent).startIntent("share title", filters);
            }
        });

        mImageShareButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent shareIntent = new Intent();
                shareIntent.setAction(Intent.ACTION_SEND);
                shareIntent.setType("image/*");

                String[] filters = new String[]{"dropbox", "com.microsoft.skydrive", "com.google.android.apps.docs", "com.box.android", "com.amazon.drive"};
                FilteredIntent filteredIntent = FilteredIntent.newInstance(MainActivity.this, shareIntent);
                filteredIntent.startIntent("Share file to clouds", filters);

                //FilteredIntent. .create(MainActivity.this, shareIntent).startIntent("share title", filters);
            }
        });



        mImageShareWithoudAppButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent shareIntent = new Intent();
                shareIntent.setAction(Intent.ACTION_SEND);
                shareIntent.setType("image/*");

                String[] filters = new String[]{"tv.twitch.android.app"};
                FilteredIntent filteredIntent = FilteredIntent.newInstance(MainActivity.this, shareIntent);
                List<Intent> intents = filteredIntent.getFilteredIntents();

                if(intents == null || intents.size() ==0) {
                    PlayAppStore.newInstance().openApp(MainActivity.this, "tv.twitch.android.app");

                    return;
                }

                filteredIntent.startIntent("Share file to clouds", filters);


                //FilteredIntent. .create(MainActivity.this, shareIntent).startIntent("share title", filters);
            }
        });


    }
}
