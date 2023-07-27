package es.project.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Properties specific to Project.
 * <p>
 * Properties are configured in the {@code application.yml} file.
 * See {@link tech.jhipster.config.JHipsterProperties} for a good example.
 */
@ConfigurationProperties(prefix = "application", ignoreUnknownFields = false)
public class ApplicationProperties {
    // jhipster-needle-application-properties-property
    // jhipster-needle-application-properties-property-getter
    // jhipster-needle-application-properties-property-class

    private final Media media= new Media();

    public Media getMedia() {
        return media;
    }

    public static class Media{
        private long maxChunkSize;

        public long getMaxChunkSize() {
            return maxChunkSize;
        }

        public void setMaxChunkSize(long maxChunkSize) {
            this.maxChunkSize = maxChunkSize;
        }
    }
}
