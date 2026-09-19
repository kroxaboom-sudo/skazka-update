package com.kroxaboom.skazka.update.android;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInstaller;

/**
 * RU: Общая обработка системного запроса подтверждения установки.
 * EN: Shared handling for the system install-confirmation request.
 */
public final class InstallStatus {
    private InstallStatus() {}

    public static int handlePendingUserAction(Context context, Intent intent) {
        if (context == null || intent == null) {
            return PackageInstaller.STATUS_FAILURE;
        }

        int status = intent.getIntExtra(
                PackageInstaller.EXTRA_STATUS,
                PackageInstaller.STATUS_FAILURE
        );

        if (status == PackageInstaller.STATUS_PENDING_USER_ACTION) {
            Intent confirmation = intent.getParcelableExtra(Intent.EXTRA_INTENT, Intent.class);
            if (confirmation != null) {
                confirmation.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(confirmation);
            }
        }

        return status;
    }
}
