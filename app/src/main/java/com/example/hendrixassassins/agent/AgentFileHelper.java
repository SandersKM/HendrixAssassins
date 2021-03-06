package com.example.hendrixassassins.agent;

import android.content.Context;
import android.util.Log;


import java.util.Date;
import java.util.ArrayList;
import java.util.GregorianCalendar;

public class AgentFileHelper {

    private final static int drawNumberIndex = 0, nameIndex = 1, emailIndex = 2, statusIndex = 3,
    deathTimeIndex = 4, currentTargetIndex = 5, personalKillsIndex = 6, pointsTotalIndex = 7,
    killListIndex = 8;

    private final static String dateSplitChar = "-", spaceSplitChar = " ", timeSplitChar = ":";

    public AgentFileHelper(){}

    public String getAgentListFileString(AgentList agentList){
        StringBuilder builder = new StringBuilder();
        for(Agent agent: agentList.getAllAgents()) builder.append(agent.getTableRow());
        return builder.toString();
    }

    public void writeToFile(String fileName, AgentList agentList, Context context){
        AgentFileInteraction interaction = new AgentFileInteraction(context);
        interaction.writeAgentFile(getAgentListFileString(agentList), fileName);
    }

    public AgentList readFromFile(String fileName, Context context){
        AgentFileInteraction interaction = new AgentFileInteraction(context);
        ArrayList<String> file = interaction.readAgentFile(fileName);
        AgentList agentList = setupAgentsFromFile(file);
        return connectAgentObjects(file, agentList);
    }

    private String[] splitLine(String line, String splitChar){
        return line.trim().split(splitChar);
    }

    private int intFromString(String string){
        if(string.equals("NA")) return 0;
        return Integer.parseInt(string.trim());
    }

    private GregorianCalendar dateFromString(String string){
        if(string.equals("NA") || string.trim().length() == 0) return null;
        String[] split = splitLine(string, spaceSplitChar);
        String[] date = splitLine(split[0], dateSplitChar);
        String[] time = splitLine(split[1], dateSplitChar);
        return new GregorianCalendar(Integer.parseInt(date[0]), Integer.parseInt(date[1]) - 1,
                Integer.parseInt(date[2]),  Integer.parseInt(time[0]), Integer.parseInt(time[1]),
                Integer.parseInt(time[2]));
    }

    private Agent setupAgent(String line){
        String[] split = splitLine(line, ",");
        Agent agent = new Agent(split[emailIndex], split[nameIndex]);
        agent.setDrawNumber(intFromString(split[drawNumberIndex]));
        agent.setStatus(AgentStatus.valueOf(split[statusIndex]));
        agent.setDeathTime(dateFromString(split[deathTimeIndex]));
        agent.setPersonalKills(intFromString(split[personalKillsIndex]));
        agent.setPointsTotal(intFromString(split[pointsTotalIndex]));
        return agent;
    }

    private AgentList getAgentKillList(String[] killed, AgentList agentList){
        AgentList killedList = new AgentList();
        for(String k: killed) {
            Log.e("ZZZ", k);
            if(!k.equals("NA")) killedList.addAgent( agentList.getAgentWithEmailAddress(k));
        }
        return killedList;
    }

    private AgentList setupAgentsFromFile(ArrayList<String> file){
        AgentList agentList = new AgentList();
        for(String line: file){
            if(line.trim().length() > 0) agentList.addAgent(setupAgent(line));
        }
        return agentList;
    }

    private AgentList connectAgentObjects(ArrayList<String> file, AgentList agentList){
        for(String line: file){
            String[] split = splitLine(line, ",");
            if(split.length > 8) {
                Agent agent = agentList.getAgentWithEmailAddress(split[emailIndex]);
                agent.setCurrentTarget(agentList.getAgentWithEmailAddress(split[currentTargetIndex]));
                Log.e("DDD", line);
                agent.extendKillList(getAgentKillList(splitLine(split[killListIndex], ":"), agentList));
            }
        }
        return agentList;
    }

    public AgentList testFileReading(ArrayList<String> file){
        return connectAgentObjects( file, setupAgentsFromFile(file));
    }
}
