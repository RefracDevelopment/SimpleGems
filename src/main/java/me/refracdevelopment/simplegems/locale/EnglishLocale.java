package me.refracdevelopment.simplegems.locale;

import dev.rosewood.rosegarden.locale.Locale;

import java.util.LinkedHashMap;
import java.util.Map;

public class EnglishLocale implements Locale {

    @Override
    public String getLocaleName() {
        return "en_US";
    }

    @Override
    public String getTranslatorName() {
        return "Refrac";
    }

    @Override
    public Map<String, Object> getDefaultLocaleValues() {
        return new LinkedHashMap() {{
            this.put("#0", "Plugin Message Prefix");
            this.put("prefix", "<g:#8A2387:#E94057:#F27121>SimpleGems &8| &f");

            this.put("#1", "Generic Command Messages");
            this.put("no-permission", "&cYou don't have permission for that!");
            this.put("no-console", "&cOnly players may execute this command.");
            this.put("unknown-command", "Unknown command, use #00B4DB/%cmd%&f help for more info");

            this.put("#2", "Error Messages");
            this.put("invalid-gems", "&c%player% does not have enough gems for you to take.");
            this.put("invalid-amount", "&cYou provided an invalid amount.");
            this.put("invalid-player", "&cYou provided an invalid player.");
            this.put("kick-profile-not-created", "Error: Profile could not be created.");
            this.put("kick-profile-not-loaded", "Error: Player profile could not be loaded!");

            this.put("#3", "Base Command Message");
            this.put("base-command-color", "&e");
            this.put("base-command-help", "&eUse &b/%cmd% help &efor command information.");

            this.put("#4", "Gems Command Messages");
            this.put("not-enough-gems", "&cYou don't have enough gems to buy %item%&c.");
            this.put("not-enough-pay", "&cYou don't have enough gems to pay %player%.");
            this.put("not-enough-withdraw", "&cYou don't have enough gems to withdraw.");
            this.put("shop-disabled", "&cThe shop is currently disabled.");
            this.put("gems-balance", "&d%player% &ehas &c%gems% &egems.");
            this.put("gems-paid", "&eYou paid &d%player% &c%gems% &egems.");
            this.put("gems-withdrawn", "&eYou've withdrawn &cx%gems% &egems.");
            this.put("gems-deposited", "&eYou've deposited &c%gems% &egems.");
            this.put("gems-received", "&d%player% &ejust paid you &c%gems% &egems.");
            this.put("gems-given", "&eYou've given &c%gems% &egems to &d%player%&e.");
            this.put("gems-gained", "&eYou've received &c%gems% &egems.");
            this.put("gems-taken", "&eYou've taken &c%gems% &egems from &d%player%&b.");
            this.put("gems-lost", "&eYou've lost &c%gems% &egems.");
            this.put("gems-set", "&eYou've set &d%player%'s &egems to &c%gems%&e.");
            this.put("gems-setted", "&eYour gems have been set to &c%gems%&e.");

            this.put("#5", "Help Command");
            this.put("command-help-title", "&fAvailable Commands:");
            this.put("command-help-description", "Displays the help menu.");
            this.put("command-help-list-description", "&8 - &d/%cmd% %subcmd% %args% &7- %desc%");
            this.put("command-help-list-description-no-args", "&8 - &d/%cmd% %subcmd% &7- %desc%");

            this.put("#6", "Shop Command");
            this.put("command-shop-description", "Opens the gems shop.");

            this.put("#7", "Top Command");
            this.put("command-top-description", "Allows you to see the top players.");

            this.put("#8", "Deposit Command");
            this.put("command-deposit-description", "Allows you to deposit all gems in your inventory.");

            this.put("#9", "Balance Command");
            this.put("command-balance-description", "Display other players gem balances.");

            this.put("#10", "Withdraw Command");
            this.put("command-withdraw-description", "Allows you to withdraw gems in item form.");

            this.put("#11", "Pay Command");
            this.put("command-pay-description", "Allows you to pay someone gems.");

            this.put("#12", "Give Command");
            this.put("command-give-description", "Allows you to give someone gems.");

            this.put("#13", "Take Command");
            this.put("command-take-description", "Allows you to take away others' gems.");

            this.put("#14", "Set Command");
            this.put("command-set-description", "Allows you to set someone gems.");

            this.put("#15", "Reload Command");
            this.put("command-reload-description", "Reloads the plugin");
            this.put("command-reload-usage", "&cUsage: &e/%cmd% reload");
            this.put("command-reload-success", "&aConfiguration and locale files were reloaded.");

            this.put("#16", "Version Command");
            this.put("command-version-description", "Display the version info for SimpleLinks");
        }};
    }
}