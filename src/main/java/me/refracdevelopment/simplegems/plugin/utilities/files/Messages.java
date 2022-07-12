package me.refracdevelopment.simplegems.plugin.utilities.files;

import java.util.List;

/**
 * Author:  Zachary (Refrac) Baldwin
 * Created: 2021-10-8
 */
public class Messages {
    public static String PREFIX;
    public static String NO_PERMISSION;
    public static String RELOAD;
    public static String SAVE_BROADCAST;
    public static String INVALID_GEMS;
    public static String INVALID_AMOUNT;
    public static String INVALID_PLAYER;
    public static String NOT_ENOUGH_GEMS;
    public static String NOT_ENOUGH_PAY;
    public static String NOT_ENOUGH_WITHDRAW;
    public static String SHOP_DISABLED;
    public static String GEMS_PAID;
    public static String GEMS_WITHDRAWN;
    public static String GEMS_DEPOSITED;
    public static String GEMS_RECEIVED;
    public static String GEMS_GIVEN;
    public static String GEMS_GAINED;
    public static String GEMS_TAKEN;
    public static String GEMS_LOST;
    public static String GEMS_SET;
    public static String GEMS_SETTED;
    public static String GEMS_BALANCE;
    public static String GEMS_WIPE;
    public static String GEMS_WIPE_CONFIRM;
    public static List<String> HELP_PAGE;

    public static void loadMessages() {
        PREFIX = Files.getMessages().getString("messages.prefix");
        NO_PERMISSION = Files.getMessages().getString("messages.no-permission");
        RELOAD = Files.getMessages().getString("messages.reload");
        SAVE_BROADCAST = Files.getMessages().getString("messages.save-broadcast");
        INVALID_GEMS = Files.getMessages().getString("messages.invalid-gems");
        INVALID_AMOUNT = Files.getMessages().getString("messages.invalid-amount");
        INVALID_PLAYER = Files.getMessages().getString("messages.invalid-player");
        NOT_ENOUGH_GEMS = Files.getMessages().getString("messages.not-enough-gems");
        NOT_ENOUGH_PAY = Files.getMessages().getString("messages.not-enough-pay");
        NOT_ENOUGH_WITHDRAW = Files.getMessages().getString("messages.not-enough-withdraw");
        SHOP_DISABLED = Files.getMessages().getString("messages.shop-disabled");
        GEMS_PAID = Files.getMessages().getString("messages.gems-paid");
        GEMS_WITHDRAWN = Files.getMessages().getString("messages.gems-withdrawn");
        GEMS_DEPOSITED = Files.getMessages().getString("messages.gems-deposited");
        GEMS_RECEIVED = Files.getMessages().getString("messages.gems-received");
        GEMS_GIVEN = Files.getMessages().getString("messages.gems-given");
        GEMS_GAINED = Files.getMessages().getString("messages.gems-gained");
        GEMS_TAKEN = Files.getMessages().getString("messages.gems-taken");
        GEMS_LOST = Files.getMessages().getString("messages.gems-lost");
        GEMS_SET = Files.getMessages().getString("messages.gems-set");
        GEMS_SETTED = Files.getMessages().getString("messages.gems-setted");
        GEMS_BALANCE = Files.getMessages().getString("messages.gems-balance");
        GEMS_WIPE = Files.getMessages().getString("messages.gems-wipe");
        GEMS_WIPE_CONFIRM = Files.getMessages().getString("messages.gems-wipe-confirm");
        HELP_PAGE = Files.getMessages().getStringList("help-page");
    }
}