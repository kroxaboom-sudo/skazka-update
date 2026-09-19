package com.kroxaboom.skazka.update;

import java.net.URI;
import java.util.Set;
import java.util.regex.Pattern;

/**
 * RU: Ограничивает обновления одним явно заданным GitHub Releases репозиторием.
 * EN: Restricts updates to one explicitly configured GitHub Releases repository.
 */
public final class GitHubReleasePolicy {
    private static final Set<String> ASSET_HOSTS = Set.of(
            "release-assets.githubusercontent.com",
            "objects.githubusercontent.com",
            "github-releases.githubusercontent.com"
    );

    private final String owner;
    private final String repository;
    private final String releasePrefix;
    private final Pattern assetPath;

    public GitHubReleasePolicy(String owner, String repository) {
        if (!safeSegment(owner) || !safeSegment(repository)) {
            throw new IllegalArgumentException("Invalid GitHub repository");
        }

        this.owner = owner;
        this.repository = repository;
        this.releasePrefix = "/" + owner + "/" + repository + "/releases/download/";
        this.assetPath = Pattern.compile(
                Pattern.quote(releasePrefix)
                        + "[A-Za-z0-9._-]+/[A-Za-z0-9._-]+\\.apk"
        );
    }

    public String repositoryUrl() {
        return "https://github.com/" + owner + "/" + repository;
    }

    public boolean isAssetUrl(String value) {
        try {
            URI uri = URI.create(value);
            return secureBase(uri)
                    && "github.com".equals(uri.getHost())
                    && uri.getRawQuery() == null
                    && uri.getPath().equals(uri.getRawPath())
                    && assetPath.matcher(uri.getPath()).matches()
                    && !uri.getPath().contains("/../");
        } catch (Exception ignored) {
            return false;
        }
    }

    public boolean isAllowedTransfer(String value) {
        try {
            URI uri = URI.create(value);
            if (!secureBase(uri)) {
                return false;
            }

            if (ASSET_HOSTS.contains(uri.getHost())) {
                return true;
            }

            if (!"github.com".equals(uri.getHost())
                    || uri.getRawQuery() != null
                    || !uri.getPath().equals(uri.getRawPath())
                    || uri.getPath().contains("/../")) {
                return false;
            }

            return uri.getPath().startsWith(releasePrefix);
        } catch (Exception ignored) {
            return false;
        }
    }

    private static boolean secureBase(URI uri) {
        return "https".equals(uri.getScheme())
                && uri.getUserInfo() == null
                && (uri.getPort() == -1 || uri.getPort() == 443)
                && uri.getFragment() == null;
    }

    private static boolean safeSegment(String value) {
        return value != null && value.matches("[A-Za-z0-9_.-]{1,100}");
    }
}
