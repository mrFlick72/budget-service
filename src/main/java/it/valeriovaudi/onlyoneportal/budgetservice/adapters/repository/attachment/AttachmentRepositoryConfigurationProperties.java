package it.valeriovaudi.onlyoneportal.budgetservice.adapters.repository.attachment;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;

@ConstructorBinding
@ConfigurationProperties("attachment.repository")
public record AttachmentRepositoryConfigurationProperties(String accessKey,
                                                   String secretKey,
                                                   String region,
                                                   String bucketName,
                                                   String bucketPrefix
) {
}
