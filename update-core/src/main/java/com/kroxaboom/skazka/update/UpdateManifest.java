package com.kroxaboom.skazka.update;

/**
 * RU: Проверенная метаинформация одного APK-обновления.
 * EN: Validated metadata for one APK update.
 */
public record UpdateManifest(
        int schema,
        String packageName,
        long versionCode,
        String versionName,
        int minSdk,
        long size,
        String sha256,
        String apkUrl,
        String notes
) {
    public static final long DEFAULT_MAX_APK_BYTES = 100L * 1024L * 1024L;

    public UpdateManifest validate(
            String expectedPackage,
            GitHubReleasePolicy releasePolicy,
            long maxApkBytes
    ) {
        if (schema != 1 || expectedPackage == null || !expectedPackage.equals(packageName)) {
            throw new IllegalArgumentException("Update manifest does not match the application");
        }
        if (versionCode < 1
                || size < 1
                || size > maxApkBytes
                || minSdk < 1
                || minSdk > 10000
                || versionName == null
                || !versionName.matches("[0-9A-Za-z._-]{1,64}")
                || sha256 == null
                || !sha256.matches("[a-f0-9]{64}")
                || apkUrl == null
                || releasePolicy == null
                || !releasePolicy.isAssetUrl(apkUrl)
                || (notes != null && notes.length() > 12000)) {
            throw new IllegalArgumentException("Invalid update manifest");
        }
        return this;
    }

    public boolean isNewerThan(long installedVersionCode, int deviceSdk) {
        return versionCode > installedVersionCode && minSdk <= deviceSdk;
    }
}
