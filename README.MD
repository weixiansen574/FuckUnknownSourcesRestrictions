## 去他妈的未知来源限制

这是一个XPosed/LSPosed模块，移除**原生安装器**的安装未知来源权限检查。启用后，`出于安全考虑，目前已禁止您的手机安装来自此来源的未知应用。您可以在“设置”中对此进行更改。`的警告就可以滚一边去了。不再需要申请，打开允许开关，直接安装！（除非你的应用主动判断并申请😅）

## 作用域

```xml
<string-array name="xposed_scope">
    <item>com.miui.packageinstaller</item>
    <item>com.android.packageinstaller</item>
</string-array>
```

说一下为啥要包含`com.miui.packageinstaller`

缘由是MIUI安装器过于恶心，安装速度慢、收集安装应用的数据并上传（泄露隐私）、爱管闲事等，然后且用其他安装器系统或应用不默认使用，又或者不支持apks，故将原生的安装器直接改包名并使用核破解覆盖com.miui.packageinstaller。

## 为啥开发此模块

2024-06-12 消息：

> （MIUI/Hyperos）安装未知应用权限，每张SIM卡拥有一定次数的限制，超限后无法申请该权限。

替换了原生安装器，但神烦的要申请这个权限，未来要是被金凡制裁了就麻烦了🤣