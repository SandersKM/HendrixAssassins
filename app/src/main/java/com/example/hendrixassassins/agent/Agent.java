package com.example.hendrixassassins.agent;

import java.util.ArrayList;
import java.util.Date;

public class Agent {
    private int drawNumber, personalKills, pointsTotal;
    private String email, name;
    private Agent currentTarget;
    private AgentStatus status;
    private Date deathTime;
    private AgentList killList;

    public Agent(String email, String name){
        this.email = email;
        this.name = name;
        this.drawNumber = -1;
        this.personalKills = 0;
        this.pointsTotal = 0;
        this.status = AgentStatus.ALIVE;
        this.deathTime = null;
        this.killList = new AgentList();
        this.currentTarget = null;
    }

    public AgentStatus getStatus(){
        return status;
    }

    public void setStatus(AgentStatus status){
        this.status = status;
    }

    public String getEmail(){
        return email;
    }

    public String getName(){
        return name;
    }

    public void setDrawNumber(int number){
        drawNumber = number;
    }

    public int getDrawNumber(){
        return drawNumber;
    }

    public String getDrawNumberString(){
        if(drawNumber == -1){
            return "NA";
        }
        return String.valueOf(drawNumber);
    }

    public void setPersonalKills(int personalKills) {
        this.personalKills = personalKills;
    }

    public void incrementPersonalKills(){
        personalKills++;
    }

    public int getPersonalKills(){
        return personalKills;
    }

    public int getPointsTotal(){
        return pointsTotal;
    }

    public void setPointsTotal(int pointsTotal){
        this.pointsTotal = pointsTotal;
    }

    public void addToPointsTotal(int addedPoints){
        pointsTotal += addedPoints;
    }

    public void setCurrentTarget(Agent target){
        currentTarget = target;
    }

    public String getCurrentTargetEmail(){
        if(currentTarget == null){
            return "NA";
        }
        return currentTarget.email;
    }

    public Agent getCurrentTarget() {
        return currentTarget;
    }

    public Date getDeathTime(){
        return deathTime;
    }

    public String getDeathTimeString(){
        if(deathTime==null){
            return "NA";
        }
        return deathTime.toString();
    }

    public void setDeathTime(Date time){
        deathTime = time;
    }

    public AgentList getKillList(){
        return killList;
    }

    public void addToKillList(Agent killed){
        killList.addAgent(killed);
    }

    public void extendKillList(AgentList killed){
        for(Agent k : killed.getAllAgents()){
            killList.addAgent(k);
        }
    }

    public String getTableRow(){
        return getDrawNumberString()+"," + getName() + "," + getEmail() +"," +
                getStatus().toString()+"," +  getDeathTimeString() + "," +
                getCurrentTargetEmail() + "," + String.valueOf(getPersonalKills()) +
                "," + String.valueOf(getPointsTotal()) + "," + getKillListAsString();
    }

    private String getKillListAsString(){
        StringBuilder builder = new StringBuilder();
        for(Agent agent: killList.getAllAgents()){
            builder.append(agent.email);
            builder.append("-");
        }
        builder.deleteCharAt(builder.length() - 1);
        return builder.toString();
    }

}