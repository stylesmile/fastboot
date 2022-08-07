package com.example;

import io.github.stylesmile.annotation.AutoWired;
import io.github.stylesmile.annotation.Controller;
import io.github.stylesmile.annotation.RequestMapping;
import io.minio.MinioClient;
import io.minio.ObjectWriteResponse;
import io.minio.PutObjectArgs;
import io.minio.errors.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

@Controller
public class TestController {
    private static final Logger LOGGER = LoggerFactory.getLogger(TestController.class);

    @AutoWired
    MinioClient minioClient;

    @RequestMapping("/")
    public int hello2() {
        PutObjectArgs putObjectArgs = PutObjectArgs
                .builder().build();
        try {
            ObjectWriteResponse objectWriteResponse = minioClient.putObject(putObjectArgs);
        } catch (ErrorResponseException | InsufficientDataException | InvalidKeyException | InternalException |
                 IOException | InvalidResponseException | ServerException | NoSuchAlgorithmException |
                 XmlParserException e) {
            throw new RuntimeException(e);
        }
        return 1;
    }


}
