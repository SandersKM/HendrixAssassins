package com.example.hendrixassassins;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.example.hendrixassassins.UItestcompnents.CustomListViewAdapter;
import com.example.hendrixassassins.agent.Agent;
import com.example.hendrixassassins.agent.AgentFileHelper;
import com.example.hendrixassassins.agent.AgentList;
import com.example.hendrixassassins.agent.AgentStatus;
import com.example.hendrixassassins.game.Game;
import com.example.hendrixassassins.uipages.DialogBoxes.ChangeNameDialogFragment;
import com.example.hendrixassassins.uipages.DialogBoxes.PopupChangeAgentName;
import com.example.hendrixassassins.uipages.DialogBoxes.PopupChangeAgentStatus;

import java.util.ArrayList;

public class AgentProfileActivity extends AppCompatActivity implements ChangeNameDialogFragment.NoticeDialogListener,
        PopupChangeAgentName.DialogListener, PopupChangeAgentStatus.DialogListener {
    private TextView agentName, AgentTotalPoints, AgentPersonalKills, agentEmail,
            agentStatusCurrent, currentAgentTarget;
    private ImageButton changeAgentName, sendEmailAgent, removeAgent, editPlayerStatus;
    private ListView agentEmailHistory, agentKillHistory;
    private Context context;
    private Game game;
    private Agent agent;
    private AgentList agentList;
    private AgentFileHelper agentFileHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getApplicationContext();
        Intent intent = getIntent();
        String handlerEmail = intent.getExtras().getString("handlerEmail");
        String agentEmail = intent.getExtras().getString("agentEmail");
        setupGame(handlerEmail);
        setupAgentList();
        setupAgent(agentEmail);
        setContentView(R.layout.activity_agent_profile);
        setupIDs();
        personalizeAgentPage();
    }

    private void setupGame(String handlerEmail){
        game = new Game(handlerEmail);
        game.readGameFromFile(context);
    }

    private void setupAgentList(){
        agentFileHelper = new AgentFileHelper();
        agentList = agentFileHelper.readFromFile(game.getAgentFileName(), context);
    }

    private void setupAgent(String agentEmail) {
        agent = agentList.getAgentWithEmailAddress(agentEmail);
    }

    private void setupIDs() {
        agentName = findViewById(R.id.agentName);
        AgentTotalPoints = findViewById(R.id.AgentTotalPoints);
        AgentPersonalKills = findViewById(R.id.AgentPersonalKills);
        agentEmail = findViewById(R.id.agentEmail);
        // This was causing errors for some reason.
        agentStatusCurrent = findViewById(R.id.agentStatus);
        changeAgentName = findViewById(R.id.changeAgentName);
        sendEmailAgent = findViewById(R.id.sendEmailAgent);
        removeAgent = findViewById(R.id.removeAgent);
        editPlayerStatus = findViewById(R.id.editPlayerStatus);
        agentEmailHistory = findViewById(R.id.agentEmailHistory);
        agentKillHistory = findViewById(R.id.agentKillHistory);
        currentAgentTarget = findViewById(R.id.currentAgentTarget);
    }

    private void personalizeAgentPage(){
        agentName.setText(agent.getName());
        AgentTotalPoints.setText(String.valueOf(agent.getPointsTotal()));
        AgentPersonalKills.setText(String.valueOf(agent.getPersonalKills()));
        agentStatusCurrent.setText(agent.getStatus().toString());
        currentAgentTarget.setText(agent.getCurrentTarget().getName());
    }


    public void changeAgentNameButtonListener(View view) {
        changeAgentName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PopupChangeAgentName changeAgentName = new PopupChangeAgentName();
                changeAgentName.show(getSupportFragmentManager(), "changeName");
            }
        });
    }

    private void updateAgentListFile(){
        agentFileHelper.writeToFile(game.getAgentFileName(), agentList, context);
    }

    @Override
    public void onDialogPositiveClick(DialogFragment dialog, String updatedName) {
        Log.e("CCC", "posClick");
        Log.e("CCC", updatedName);
        agent.setName(updatedName);
        updateAgentListFile();
        agentName.setText(agent.getName());
    }

    @Override
    public void onDialogNegativeClick(DialogFragment dialog) {
        // User touched the dialog's negative button
    }

    public void sendEmailAgentButtonListener(View view) {
        gotoSendEmail();
    }

    private void gotoSendEmail() {
        sendEmailAgent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        //TODO: need to make sure going to the email intent won't cause crashes
    }

    public void editPlayerStatusButtonListener(View view) {
        editPlayerStatus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PopupChangeAgentStatus changeAgentStatus = new PopupChangeAgentStatus();
                changeAgentStatus.show(getSupportFragmentManager(), "changeAgentStatus");
            }
        });
        //TODO: edit player status
    }

    public void removeAgentButtonListener(View view) {
        removeAgent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        //TODO: remove agent from game
        // why is this seperate from the status changing?
    }

    @Override
    public void changeName(String updateName) {

        if(!updateName.isEmpty()){
        agentName.setText(updateName);
        }
    }


    @Override
    public void changeStatus(String updatedName) {
        agent.setStatus(AgentStatus.valueOf(updatedName));
        agentStatusCurrent.setText(agent.getStatus().toString());
        updateAgentListFile();
    }

    public void goToTargetsProfile(View view) {
        gotoAgentProfile(agent.getCurrentTarget());
    }

    private void gotoAgentProfile(Agent agent) {
        Intent userListView = new Intent(AgentProfileActivity.this, AgentProfileActivity.class);
        userListView.putExtra("handlerEmail", game.getEmail());
        userListView.putExtra("agentEmail", agent.getEmail());
        startActivity(userListView);
    }
}
