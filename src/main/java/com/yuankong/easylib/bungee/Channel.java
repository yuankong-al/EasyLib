package com.yuankong.easylib.bungee;

public enum Channel {
    REGISTER("easy_lib:register"),
    GENERAL("easy_lib:general"),
    SUCCESS("success"),
    START("start");

    private final String channel;
    Channel(String channel) {
        this.channel = channel;
    }

    public String getChannel() {
        return channel;
    }
}
