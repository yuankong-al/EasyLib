package com.yuankong.easylib.command.tools;

public enum Identify {
    ALL,//含有op权限的所有，普通玩家不可用
    OP,//仅op玩家可执行，后台角色不可以
    CONSOLE,//仅后台角色，即使op角色也不可用
    PLAYER//除后台角色之外的所有可用
}
