[![](https://jitpack.io/v/RefracDevelopment/SimpleGems.svg)](https://jitpack.io/#RefracDevelopment/SimpleGems) [![Java CI with Maven](https://github.com/RefracDevelopment/SimpleGems/actions/workflows/maven.yml/badge.svg)](https://github.com/RefracDevelopment/SimpleGems/actions/workflows/maven.yml)

![](https://i.imgur.com/pMKvWrk.png)

SimpleGems is a plugin that adds a custom gems economy into minecraft.

[Spigot](https://www.spigotmc.org/resources/96827/) [Hangar](https://hangar.papermc.io/RefracDevelopment/SimpleGems/)

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
You can use this to access all methods found in the [SimpleGemsAPI](https://github.com/RefracDevelopment/SimpleGems/blob/main/src/main/java/me/refracdevelopment/simplegems/api/SimpleGemsAPI.java)
```JAVA
SimpleGemsAPI gemsAPI = SimpleGems.getInstance().getGemsAPI();
```

### License
Please view the [LICENSE](LICENSE) file for more information on the license of this source code.

[icon from flaticon](https://www.flaticon.com/)
