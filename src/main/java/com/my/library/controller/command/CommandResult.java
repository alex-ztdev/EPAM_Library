package com.my.library.controller.command;

import com.my.library.controller.command.constant.CommandDirection;

public class CommandResult {
    private String page;
    private CommandDirection action = CommandDirection.FORWARD; //Direction is forward by default

    public CommandResult(String page) {
        this.page = page;
    }

    public CommandResult(String page, CommandDirection action) {
        this.page = page;
        this.action = action;
    }

    public String getPage() {
        return page;
    }

    public void setPage(String page) {
        this.page = page;
    }

    public CommandDirection getAction() {
        return action;
    }

    public void setAction(CommandDirection action) {
        this.action = action;
    }

    @Override
    public String toString() {
        return "CommandResult{" +
                "page='" + page + '\'' +
                ", action=" + action +
                '}';
    }
}
