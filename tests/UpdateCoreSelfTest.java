import com.kroxaboom.skazka.update.GitHubReleasePolicy;
import com.kroxaboom.skazka.update.UpdateManifest;

public final class UpdateCoreSelfTest {
    public static void main(String[] args) {
        GitHubReleasePolicy releases = new GitHubReleasePolicy("example-owner", "example-releases");
        String apk = releases.repositoryUrl() + "/releases/download/v1.2.3/app.apk";

        check(releases.isAssetUrl(apk), "canonical APK asset");
        check(!releases.isAssetUrl("http://github.com/example-owner/example-releases/releases/download/v1/app.apk"), "HTTPS only");
        check(!releases.isAssetUrl(releases.repositoryUrl() + "/releases/download/../app.apk"), "path traversal");
        check(releases.isAllowedTransfer("https://release-assets.githubusercontent.com/asset?signature=abc"), "GitHub asset host");
        check(!releases.isAllowedTransfer("https://evil.example/app.apk"), "unknown transfer host");

        UpdateManifest manifest = new UpdateManifest(
                1,
                "com.example.app",
                42,
                "1.2.3-preview",
                33,
                1024,
                "a".repeat(64),
                apk,
                "notes"
        ).validate("com.example.app", releases, UpdateManifest.DEFAULT_MAX_APK_BYTES);

        check(manifest.isNewerThan(41, 33), "newer compatible version");
        check(!manifest.isNewerThan(42, 33), "same version");
        check(!manifest.isNewerThan(41, 32), "minSdk compatibility");

        boolean rejected = false;
        try {
            new UpdateManifest(
                    1,
                    "com.other.app",
                    42,
                    "1.2.3",
                    33,
                    1024,
                    "a".repeat(64),
                    apk,
                    ""
            ).validate("com.example.app", releases, UpdateManifest.DEFAULT_MAX_APK_BYTES);
        } catch (IllegalArgumentException expected) {
            rejected = true;
        }
        check(rejected, "package mismatch");

        System.out.println("PASS: Skazka Update Core manifest and release URL policy");
    }

    private static void check(boolean condition, String message) {
        if (!condition) {
            throw new AssertionError(message);
        }
    }
}
