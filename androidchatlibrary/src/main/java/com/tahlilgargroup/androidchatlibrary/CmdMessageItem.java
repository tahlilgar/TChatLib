package com.tahlilgargroup.androidchatlibrary;

public class CmdMessageItem {
    private CmdMessageData cmdMessageData;
    private String CommandName;

    public CmdMessageItem(CmdMessageData cmdMessageData, String commandName) {
        this.cmdMessageData = cmdMessageData;
        CommandName = commandName;
    }

    public CmdMessageData getCmdMessageData() {
        return cmdMessageData;
    }

    public void setCmdMessageData(CmdMessageData cmdMessageData) {
        this.cmdMessageData = cmdMessageData;
    }

    public String getCommandName() {
        return CommandName;
    }

    public void setCommandName(String commandName) {
        CommandName = commandName;
    }
}
