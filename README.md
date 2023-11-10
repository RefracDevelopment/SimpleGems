# SimpleGems
A gems economy/currency plugin.

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
SimpleGems.getInstance().getGemsAPI().getGems(player);

SimpleGems.getInstance().getGemsAPI().getOfflineGems(player);

SimpleGems.getInstance().getGemsAPI().hasGems(player, amount);

SimpleGems.getInstance().getGemsAPI().hasOfflineGems(player, amount);

SimpleGems.getInstance().getGemsAPI().giveGemsItem(player, amount);

SimpleGems.getInstance().getGemsAPI().giveGems(player, amount);

SimpleGems.getInstance().getGemsAPI().giveOfflineGems(player, amount);

SimpleGems.getInstance().getGemsAPI().takeGems(player, amount);

SimpleGems.getInstance().getGemsAPI().takeOfflineGems(player, amount);

SimpleGems.getInstance().getGemsAPI().setGems(player, amount);

SimpleGems.getInstance().getGemsAPI().setOfflineGems(player, amount);
```

### License
Please view the [LICENSE](LICENSE) file for more information on the license of this source code.
