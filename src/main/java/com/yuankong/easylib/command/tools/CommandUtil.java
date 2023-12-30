package com.yuankong.easylib.command.tools;

import com.yuankong.easylib.EasyLib;
import com.yuankong.easylib.command.CmdExecutor;
import com.yuankong.easylib.command.CommandBuilder;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.*;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.*;

public class CommandUtil implements TabExecutor {
    public static HashMap<String,CmdManager> cmds = new HashMap<>();
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        Identify identify = Identify.CONSOLE;
        if (sender instanceof Player) {
            identify = Identify.PLAYER;
        }
        CmdManager cmdManager = cmds.get(label.toLowerCase());
        if (cmdManager == null){
            cmdManager = CmdManager.forPluginName(label.toLowerCase());
            if (cmdManager == null){
                return true;
            }
        }
        String usage = cmdManager.getUsage();
        if (usage != null && !usage.equals("")){
            String str = ChatColor.GREEN + "============ "+ cmdManager.getPlugin().getName()+"指令 ============";
            usage = str+usage;
            command.setUsage(usage);
        }

        for (Map.Entry<CmdExecutor, Method> kv : cmdManager.getExecutorMap().entrySet()) {
            CmdExecutor cmdExecutor = kv.getKey();
            Method method = kv.getValue();
            if (args.length < cmdExecutor.arg().length){
                continue;
            }
            if (!compare(args,cmdExecutor.arg())){
                continue;
            }
            if (!identifyCanExecute(sender,identify,cmdExecutor.identify(),cmdManager)){
                return true;
            }

            try {
                if (Modifier.isStatic(method.getModifiers())){
                    method.invoke(CommandBuilder.class, sender, args);
                }else{
                    method.invoke(cmdManager.getCommandBuilder(), sender, args);
                }
            }catch (IllegalAccessException | InvocationTargetException ex2) {
                ex2.printStackTrace();
            }
            return true;
        }
        return !sender.isOp();
    }

    public boolean identifyCanExecute(CommandSender sender,Identify now,Identify need,CmdManager cmdManager){
        switch (need){
            default:
            case ALL:{//不是op不能执行
                if (now.equals(Identify.PLAYER)){
                    return sender.isOp();
                }
                return true;
            }
            case OP:{//控制台不能执行
                if (now.equals(Identify.CONSOLE)){
                    sender.sendMessage("["+cmdManager.plugin.getName()+"]指令不支持后台形式");
                    return false;
                }
                return sender.isOp();
            }
            case CONSOLE:{//不是后台不能执行
                if (now.equals(Identify.PLAYER)){
                    sender.sendMessage("["+cmdManager.plugin.getName()+"]指令不支持非后台形式");
                    return false;
                }
                return sender.isOp();
            }
            case PLAYER:{//控制台不能执行
                if (now.equals(Identify.CONSOLE)){
                    sender.sendMessage("["+cmdManager.plugin.getName()+"]指令不支持非玩家形式");
                    return false;
                }
                return true;
            }
        }
    }

    boolean compare(String[] args1,String[] args2){
        for(int i = 0;i < args2.length;i++){
            if (!args2[i].equalsIgnoreCase(args1[i])){
                return false;
            }
        }
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        CmdManager cmdManager = cmds.get(label.toLowerCase());
        if (cmdManager == null){
            return null;
        }
        if (args.length == 1){
            List<String> list = new ArrayList<>(cmdManager.getTabSelect());
            if (!args[0].equals("")){
                list.removeIf(s -> !s.toLowerCase().startsWith(args[0].toLowerCase()));
            }
            return list;
        }
        return null;
    }

    public static void registerCommand(Plugin plugin,String cmdName,String usage,CommandBuilder commandBuilder,boolean isMain){
        String name = cmdName.toLowerCase();
        CmdManager cmdManager = CommandUtil.cmds.get(name);
        if (cmdManager == null){
            cmdManager = new CmdManager(plugin,name,usage,commandBuilder);
        }else{
            if (!cmdManager.getPlugin().equals(plugin)){
                plugin.getLogger().warning("冲突的指令"+cmdManager.getCommand()+",指令未注册完成！");
                return;
            }
            if (!usage.equals("") && !cmdManager.getUsage().equals(usage)){
                cmdManager.setUsage(cmdManager.getUsage()+"\n"+usage);
            }
            cmdManager.setCommandBuilder(commandBuilder);
        }
        Method[] methods;
        if(isMain){
            methods = commandBuilder.getClass().getMethods();
        }else{
            methods = commandBuilder.getClass().getDeclaredMethods();
        }
        for (Method method : methods) {
            if (method.isAnnotationPresent(CmdExecutor.class)) {
                CmdExecutor cmdExecutor = method.getAnnotation(CmdExecutor.class);
                String command = cmdManager.getCommand();
                registerCommandName(plugin,command);
                registerCommandName(plugin, plugin.getName());
                cmdManager.addExecutor(cmdExecutor,method);
                if (!cmdExecutor.usage().equals("")){
                    cmdManager.addUsage(cmdExecutor.usage());
                }
                Bukkit.getPluginCommand(command).setExecutor(EasyLib.command);
                Bukkit.getPluginCommand(command).setTabCompleter((TabCompleter) EasyLib.command);
                Bukkit.getPluginCommand(plugin.getName()).setExecutor(EasyLib.command);
                Bukkit.getPluginCommand(plugin.getName()).setTabCompleter((TabCompleter) EasyLib.command);
            }
        }
        CommandUtil.cmds.put(cmdManager.getCommand(),cmdManager);
    }

    public static void registerCommandName(Plugin plugin,String command){
        if (Bukkit.getPluginCommand(command) == null) {
            try {
                Constructor<PluginCommand> constructor = PluginCommand.class.getDeclaredConstructor(String.class, Plugin.class);
                constructor.setAccessible(true);
                PluginCommand cmd = constructor.newInstance(command, EasyLib.instance);
                String packages = "org.bukkit.craftbukkit."+ EasyLib.bukkitVersion +".CraftServer";
                Class<?> craftServer = Class.forName(packages);
                Object craftServerObject = craftServer.cast(Bukkit.getServer());
                Object simpleCommandMap = craftServer.getDeclaredMethod("getCommandMap").invoke(craftServerObject);
                simpleCommandMap.getClass().getDeclaredMethod("register", String.class, Command.class).invoke(simpleCommandMap, plugin.getName(), cmd);
            } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException | ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
    }
}
