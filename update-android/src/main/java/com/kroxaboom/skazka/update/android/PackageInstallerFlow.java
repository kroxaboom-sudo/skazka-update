package com.kroxaboom.skazka.update.android;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.pm.PackageInstaller;

import com.kroxaboom.skazka.update.UpdateManifest;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * RU: Передаёт уже проверенный APK системному PackageInstaller с обязательным подтверждением пользователя.
 * EN: Hands an already verified APK to the system PackageInstaller with mandatory user confirmation.
 */
public final class PackageInstallerFlow {
    private PackageInstallerFlow() {}

    public static void launch(
            Activity activity,
            File apk,
            UpdateManifest update,
            Intent statusIntent
    ) throws Exception {
        if (activity == null || statusIntent == null) {
            throw new IllegalArgumentException("Activity and status intent are required");
        }

        ApkVerifier.verify(activity, apk, update);

        PackageInstaller installer = activity.getPackageManager().getPackageInstaller();
        PackageInstaller.SessionParams params =
                new PackageInstaller.SessionParams(PackageInstaller.SessionParams.MODE_FULL_INSTALL);
        params.setAppPackageName(activity.getPackageName());
        params.setRequireUserAction(PackageInstaller.SessionParams.USER_ACTION_REQUIRED);

        int sessionId = installer.createSession(params);
        try (PackageInstaller.Session session = installer.openSession(sessionId)) {
            writeApk(session, apk);

            PendingIntent pending = PendingIntent.getBroadcast(
                    activity,
                    Long.hashCode(update.versionCode()),
                    statusIntent,
                    PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_MUTABLE
            );
            session.commit(pending.getIntentSender());
        } catch (Exception error) {
            try {
                installer.abandonSession(sessionId);
            } catch (Exception ignored) {
                // RU: Исходная ошибка важнее ошибки cleanup.
                // EN: Preserve the original failure instead of replacing it with cleanup noise.
            }
            throw error;
        }
    }

    private static void writeApk(PackageInstaller.Session session, File apk) throws Exception {
        /*
         * RU:
         * Поток APK закрывается до commit(). Некоторые PackageInstaller реализации отклоняют
         * commit, если write stream всё ещё открыт.
         *
         * EN:
         * Close the APK stream before commit(). Some PackageInstaller implementations reject
         * a commit while the write stream is still open.
         */
        try (OutputStream output = session.openWrite("base.apk", 0, apk.length());
             InputStream input = new FileInputStream(apk)) {
            byte[] buffer = new byte[32 * 1024];
            int read;
            while ((read = input.read(buffer)) >= 0) {
                output.write(buffer, 0, read);
            }
            session.fsync(output);
        }
    }
}
