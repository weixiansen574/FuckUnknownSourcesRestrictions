package top.weixiansen574.fuckunknownsourcesrestrictions;

import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage;

public class XposedMain implements IXposedHookLoadPackage {

    @Override
    public void handleLoadPackage(XC_LoadPackage.LoadPackageParam loadPackageParam) throws Throwable {
        String packageName = loadPackageParam.packageName;
        ClassLoader classLoader = loadPackageParam.classLoader;
        //注意：com.miui.packageinstaller是com.android.packageinstaller的改包名安装器，不是MIUI原版安装器
        //缘由：MIUI原版安装器上传应用信息并协同小米账号到反诈中心等，并且用其他安装器不认，故将原生的安装器直接改包名并使用核破解覆盖com.miui.packageinstaller
        if (packageName.equals("com.miui.packageinstaller") || packageName.equals("com.android.packageinstaller")){
            XposedHelpers.findAndHookMethod("com.android.packageinstaller.PackageInstallerActivity", classLoader, "onCreate", android.os.Bundle.class, new XC_MethodHook() {
                @Override
                protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                    super.beforeHookedMethod(param);
                    //很简单，只需要在onCreate方法执行前把mAllowUnknownSources字段设置为true即可
                    XposedHelpers.setBooleanField(param.thisObject,"mAllowUnknownSources",true);
                }
            });
        }
    }
}
