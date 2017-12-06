package com.famnet.famnet.Model;

import java.util.Date;
import java.util.UUID;

/**
 * Created by DungNguyen on 11/21/17.
 */

public class Task {
    private UUID id;
    private String name;
    private String description;
    private String reward;
    private Date deadline;
    private User taker;

    public Task(){
        this.id = UUID.randomUUID();
    }

    public Task(String name, String description, Date deadline){
        this.id = UUID.randomUUID();
        this.name = name;
        this.description = description;
        this.deadline = deadline;
    }

    public Task(String name, String description, String reward, Date deadline) {
        this.id = UUID.randomUUID();
        this.name = name;
        this.description = description;
        this.reward = reward;
        this.deadline = deadline;
    }

    public Task(String name, String description, String reward, Date deadline, User taker) {
        this.id = UUID.randomUUID();
        this.name = name;
        this.description = description;
        this.reward = reward;
        this.deadline = deadline;
        this.taker = taker;
    }

    public UUID getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getReward() {
        return reward;
    }

    public void setReward(String reward) {
        this.reward = reward;
    }

    public Date getDeadline() {
        return deadline;
    }

    public void setDeadline(Date deadline) {
        this.deadline = deadline;
    }

    public User getTaker() {
        return taker;
    }

    public void setTaker(User taker) {
        this.taker = taker;
    }
}
