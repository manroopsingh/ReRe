package com.example.manroop.ReRe.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;
import android.widget.ListView;

import com.example.manroop.ReRe.R;

public class InviteFriendsActivity extends AppCompatActivity {

    private ListView listViewContacts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_invite_friends);

        listViewContacts = (ListView) findViewById(R.id.listViewContacts);


    }
}
