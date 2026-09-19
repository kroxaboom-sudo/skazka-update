package com.kroxaboom.skazka.update.android;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.SigningInfo;
import android.os.Build;

import com.kroxaboom.skazka.update.Hashing;
import com.kroxaboom.skazka.update.UpdateManifest;

import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * RU: Проверяет размер, SHA-256, package/version/minSdk и подпись APK до запуска PackageInstaller.
 * EN: Verifies APK size, SHA-256, package/version/minSdk, and signing identity before PackageInstaller runs.
 */
public final class ApkVerifier {
    private ApkVerifier() {}

    public static void verify(Context context, File apk, UpdateManifest update) throws Exception {
        if (context == null || apk == null || update == null || !apk.isFile()) {
            throw new IllegalArgumentException("Context, APK, and update metadata are required");
        }

        if (apk.length() != update.size()
                || !Hashing.sha256(apk.toPath()).equals(update.sha256())) {
            throw new IOException("APK integrity check failed");
        }

        PackageManager packageManager = context.getPackageManager();
        PackageManager.PackageInfoFlags flags =
                PackageManager.PackageInfoFlags.of(PackageManager.GET_SIGNING_CERTIFICATES);

        PackageInfo candidate = packageManager.getPackageArchiveInfo(apk.getAbsolutePath(), flags);
        PackageInfo current = packageManager.getPackageInfo(context.getPackageName(), flags);

        if (candidate == null
                || !context.getPackageName().equals(candidate.packageName)
                || candidate.getLongVersionCode() != update.versionCode()
                || !Objects.equals(candidate.versionName, update.versionName())
                || candidate.getLongVersionCode() <= current.getLongVersionCode()
                || candidate.applicationInfo == null
                || candidate.applicationInfo.minSdkVersion > Build.VERSION.SDK_INT) {
            throw new IOException("APK does not match the installed application");
        }

        if (candidate.signingInfo == null
                || current.signingInfo == null
                || !signers(candidate.signingInfo).equals(signers(current.signingInfo))) {
            throw new IOException("APK signing identity does not match the installed application");
        }
    }

    static Set<String> signers(SigningInfo info) {
        Set<String> values = new HashSet<>();
        for (android.content.pm.Signature signature : info.getApkContentsSigners()) {
            values.add(signature.toCharsString());
        }
        return values;
    }
}
