package ru.pulsecore.user_service.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.security.converter.RsaKeyConverters;
import org.springframework.stereotype.Component;

import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;

@Component
@Getter
public class JwtKeyProvider {

    private final RSAPrivateKey privateKey;
    private final RSAPublicKey publicKey;

    public JwtKeyProvider(@Value("classpath:private.pem") Resource privateKeyResource,
                          @Value("classpath:public.pem") Resource publicKeyResource) throws Exception {
        this.privateKey = RsaKeyConverters.pkcs8().convert(privateKeyResource.getInputStream());
        this.publicKey = RsaKeyConverters.x509().convert(publicKeyResource.getInputStream());
    }
}