package top.weixiansen574.fuckunknownsourcesrestrictions;

import android.content.Context;
import android.content.pm.PackageManager;
import android.provider.Settings;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.Arrays;

import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage;

public class XposedMain implements IXposedHookLoadPackage {
    ThreadLocal<Integer> integerThreadLocal = new InheritableThreadLocal<>();
    @Override
    public void handleLoadPackage(XC_LoadPackage.LoadPackageParam loadPackageParam) throws Throwable {
        String packageName = loadPackageParam.packageName;
        ClassLoader classLoader = loadPackageParam.classLoader;
        //注意：com.miui.packageinstaller是com.android.packageinstaller的改包名安装器，不是MIUI原版安装器
        //缘由：MIUI原版安装器上传应用信息并协同小米账号到反诈中心等，并且用其他安装器不认，故将原生的安装器直接改包名并使用核破解覆盖com.miui.packageinstaller
        /*if (packageName.equals("com.miui.packageinstaller") || packageName.equals("com.android.packageinstaller")){
            XposedHelpers.findAndHookMethod("com.android.packageinstaller.PackageInstallerActivity", classLoader, "onCreate", android.os.Bundle.class, new XC_MethodHook() {
                @Override
                protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                    super.beforeHookedMethod(param);
                    //很简单，只需要在onCreate方法执行前把mAllowUnknownSources字段设置为true即可
                    XposedHelpers.setBooleanField(param.thisObject,"mAllowUnknownSources",true);
                    Context context;
                }
            });
        }*/
        if (packageName.equals("android")) {
            //Hook掉未知来源权限检查，一律返回有
            XposedHelpers.findAndHookMethod("com.android.server.appop.AppOpsService", classLoader, "noteOperation", int.class, int.class, java.lang.String.class, java.lang.String.class, boolean.class, java.lang.String.class, boolean.class, new XC_MethodHook() {
                @Override
                protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                    super.beforeHookedMethod(param);
                }
                @Override
                protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                    super.afterHookedMethod(param);
                    if ((Integer) param.args[0] == 66){//AppOpsManager.permissionToOpCode("android.permission.REQUEST_INSTALL_PACKAGES") == 66
                        //XposedBridge.log("com.android.server.appop.AppOpsService.noteOperation("+Arrays.toString(param.args)+") return:"+param.getResult());
                        XposedHelpers.setIntField(param.getResult(),"mOpMode",0);
                    }
                }
            });
            //以免智障程序自己检查是否有这个权限，然后跑来申请
            XposedHelpers.findAndHookMethod("com.android.server.pm.PackageManagerService", classLoader, "canRequestPackageInstalls", java.lang.String.class, int.class, new XC_MethodHook() {
                @Override
                protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                    super.beforeHookedMethod(param);
                }

                @Override
                protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                    super.afterHookedMethod(param);
                    param.setResult(true);
                }
            });
        }

    }

}
