![](https://i.imgur.com/60pxSr4.png)

SimpleGems is a plugin that adds a custom gems economy into minecraft.

[Spigot](https://www.spigotmc.org/resources/96827/) [Craftaro](https://craftaro.com/marketplace/product/simplegems.755) [BuiltByBit](https://builtbybit.com/resources/simplegems.21583/) [Hangar](https://hangar.papermc.io/RefracDevelopment/SimpleGems/)

### Issue Reporting/Support

Please report all issues/support questions to the [Issues](https://github.com/RefracDevelopment/SimpleGems/issues) tab or [Discord](https://discord.gg/EFeSKPg739).

### Suggestions

Suggestion can be posted in [Issues](https://github.com/RefracDevelopment/SimpleGems/issues) or [Discord](https://discord.gg/EFeSKPg739) which is reviewed by project maintainers, but feel free to make a pull request to add cool features without posting there!

### Developer API
#### Maven
Replace LATEST with latest version in the [Releases](https://github.com/RefracDevelopment/SimpleGems/releases/latest) tab.
```XML
<repository>
    <id>jitpack.io</id>
    <url>https://jitpack.io</url>
</repository>

<dependency>
    <groupId>com.github.RefracDevelopment</groupId>
    <artifactId>SimpleGems</artifactId>
    <version>LATEST</version>
    <scope>provided</scope>
</dependency>
```
Make sure you add SimpleGems to your depends or softdepends
```YML
depend:
  - SimpleGems

softdepend:
  - SimpleGems
```

```JAVA
SimpleGemsAPI.INSTANCE.getGems(player);

SimpleGemsAPI.INSTANCE.getOfflineGems(player);

SimpleGemsAPI.INSTANCE.hasGems(player, amount);

SimpleGemsAPI.INSTANCE.hasOfflineGems(player, amount);

SimpleGemsAPI.INSTANCE.giveGemsItem(player, amount);

SimpleGemsAPI.INSTANCE.giveGems(player, amount);

SimpleGemsAPI.INSTANCE.giveOfflineGems(player, amount);

SimpleGemsAPI.INSTANCE.takeGems(player, amount);

SimpleGemsAPI.INSTANCE.takeOfflineGems(player, amount);

SimpleGemsAPI.INSTANCE.setGems(player, amount);

SimpleGemsAPI.INSTANCE.setOfflineGems(player, amount);
```

### License
Please view the [LICENSE](LICENSE) file for more information on the license of this source code.

[icon from flaticon](https://www.flaticon.com/)
