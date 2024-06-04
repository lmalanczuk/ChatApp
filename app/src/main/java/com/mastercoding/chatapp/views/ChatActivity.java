package com.mastercoding.chatapp.views;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.databinding.DataBindingUtil;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.os.Bundle;
import android.view.View;
import androidx.appcompat.widget.Toolbar;
import com.google.android.material.navigation.NavigationView;
import com.mastercoding.chatapp.R;
import com.mastercoding.chatapp.databinding.ActivityChatBinding;
import com.mastercoding.chatapp.model.ChatMessage;
import com.mastercoding.chatapp.viewmodel.MyViewModel;
import com.mastercoding.chatapp.views.adapters.ChatAdapter;
import java.util.ArrayList;
import java.util.List;

public class ChatActivity extends AppCompatActivity {
    private ActivityChatBinding binding;
    private MyViewModel myViewModel;
    private RecyclerView recyclerView;
    private ChatAdapter myAdapter;
    private List<ChatMessage> messagesList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        Toolbar toolbar = findViewById(R.id.toolbar); // Moved toolbar initialization here

        DrawerLayout drawerLayout = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.navigation_view);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        navigationView.setNavigationItemSelectedListener(item -> {
            // Handle navigation view item clicks here.
            int id = item.getItemId();

            if (id == R.id.nav_item_1) {
                // Handle item 1 click
            } else if (id == R.id.nav_item_2) {
                // Handle item 2 click
            }

            drawerLayout.closeDrawer(GravityCompat.START);
            return true;
        });

        binding = DataBindingUtil.setContentView(this, R.layout.activity_chat);

        myViewModel = new ViewModelProvider(this).get(MyViewModel.class);

        // RecyclerView with DataBinding
        recyclerView = binding.recyclerView;
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);

        // Getting the Group Name from the Clicked Item in the GroupsActivity
        String groupName = getIntent().getStringExtra("GROUP_NAME");

        if (groupName != null) {
            myViewModel.getMessagesLiveData(groupName).observe(this, new Observer<List<ChatMessage>>() {
                @Override
                public void onChanged(List<ChatMessage> chatMessages) {
                    messagesList = new ArrayList<>();
                    messagesList.addAll(chatMessages);

                    myAdapter = new ChatAdapter(messagesList, getApplicationContext());

                    recyclerView.setAdapter(myAdapter);
                    myAdapter.notifyDataSetChanged();

                    // Scroll to the latest message added:
                    int latestPosition = myAdapter.getItemCount() - 1;
                    if (latestPosition > 0) {
                        recyclerView.smoothScrollToPosition(latestPosition);
                    }
                }
            });
        }

        binding.setVModel(myViewModel);

        binding.sendBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String msg = binding.edittextChatMessage.getText().toString();
                myViewModel.sendMessage(msg, groupName);

                binding.edittextChatMessage.getText().clear();
            }
        });
    }
}
