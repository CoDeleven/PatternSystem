package com.codeleven.web.config;

import cn.hutool.core.util.StrUtil;
import io.minio.MinioClient;
import io.minio.errors.*;
import lombok.Data;
import org.apache.http.entity.ContentType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.xmlpull.v1.XmlPullParserException;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

@Component
@ConfigurationProperties(prefix = "minio")
@PropertySource("classpath:minio.properties")
@Scope("singleton")
@Data
public class MinioConfig {
    private static final Logger LOGGER = LoggerFactory.getLogger(MinioConfig.class);

    private int port;
    private String host;
    private String bucket;
    private String patternPath;
    private String coverPath;
    private String accessKey;
    private String accessSecret;

    private final ThreadLocal<MinioClient> threadLocal = new ThreadLocal<>();

    private static final String OPERATION_GET_STREAM = "getInputStream";
    private static final String OPERATION_PUT_OBJECT = "putObject";

    /**
     * 上产封面文件，只需要文件名不需要路径
     * @param filename 文件名
     * @param is 输入流
     */
    public void uploadCoverData(String filename, InputStream is) {
        String objectPath = COVER_PATH + "/" + System.currentTimeMillis() + "-" + filename;
        this.putObject(coverPath, objectPath, is, ContentType.APPLICATION_OCTET_STREAM.getMimeType());
    }

    /**
     * 上传花样数据，只需要文件名不需要路径
     * @param filename 文件名
     * @param is 输入流
     */
    public void uploadPatternData(String filename, InputStream is){
        String objectPath = PATTERN_PATH + "/" + System.currentTimeMillis() + "-" + filename;
        this.putObject(patternPath, objectPath, is, ContentType.APPLICATION_OCTET_STREAM.getMimeType());
    }

    /**
     * 通过 filepath 获取对象的字节数据
     *
     * @param filepath 对象路径
     * @return 字节数组
     */
    public byte[] getObjectBytes(String filepath) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        int len = -1;
        byte[] bytes = new byte[1024];
        try(InputStream objectInputStream = this.getObjectInputStream(filepath)){
            while( (len = objectInputStream.read(bytes)) != -1) {
                byteArrayOutputStream.write(bytes, 0, len);
            }
            return byteArrayOutputStream.toByteArray();
        }catch (IOException e){
            LOGGER.error("读取IO错误...", e);
        }
        return new byte[0];
    }

    public String getObjectUrl(String filepath){
        MinioClient client = this.getInstance();
        try {
            String url = client.presignedGetObject(bucket, filepath, 3600 * 24);
            LOGGER.debug("路径: {} 转换至 URL: {}", filepath, url);

            return url;
        } catch (InvalidBucketNameException e) {
            LOGGER.error("无效的Bucket名称...", e);
        } catch (NoSuchAlgorithmException e) {
            LOGGER.error("未知的算法...", e);
        } catch (InsufficientDataException e) {
            LOGGER.error("读取的数据量不符合数据长度...", e);
        } catch (IOException e) {
            LOGGER.error("IO读取错误...", e);
        } catch (InvalidKeyException e) {
            LOGGER.error("非法的Key...", e);
        } catch (NoResponseException e) {
            LOGGER.error("没有响应...", e);
        } catch (XmlPullParserException e) {
            LOGGER.error("XML解析错误...", e);
        } catch (ErrorResponseException e) {
            LOGGER.error("响应错误...", e);
        } catch (InternalException e) {
            LOGGER.error("内部错误...", e);
        } catch (InvalidExpiresRangeException e) {
            LOGGER.error("无效的过期时间...", e);
        }
        return StrUtil.EMPTY;
    }

    private void putObject(String bucket, String filepath, InputStream is, String contentType){
        MinioClient client = this.getInstance();
        try {
            client.putObject(bucket, filepath, is, contentType);
        } catch (InvalidBucketNameException e) {
            LOGGER.error("无效的Bucket名称...", e);
        } catch (NoSuchAlgorithmException e) {
            LOGGER.error("未知的算法...", e);
        } catch (InsufficientDataException e) {
            LOGGER.error("读取的数据量不符合数据长度...", e);
        } catch (IOException e) {
            LOGGER.error("IO读取错误...", e);
        } catch (InvalidKeyException e) {
            LOGGER.error("非法的Key...", e);
        } catch (NoResponseException e) {
            LOGGER.error("没有响应...", e);
        } catch (XmlPullParserException e) {
            LOGGER.error("XML解析错误...", e);
        } catch (ErrorResponseException e) {
            LOGGER.error("响应错误...", e);
        } catch (InternalException e) {
            LOGGER.error("内部错误...", e);
        } catch (InvalidArgumentException e) {
            LOGGER.error("内部错误...", e);
        }

    }

    public InputStream getObjectInputStream(String filepath) {
        MinioClient client = this.getInstance();
        try {
            return client.getObject(bucket, filepath);
        } catch (InvalidBucketNameException e) {
            LOGGER.error("无效的Bucket名称...", e);
        } catch (NoSuchAlgorithmException e) {
            LOGGER.error("未知的算法...", e);
        } catch (InsufficientDataException e) {
            LOGGER.error("读取的数据量不符合数据长度...", e);
        } catch (IOException e) {
            LOGGER.error("IO读取错误...", e);
        } catch (InvalidKeyException e) {
            LOGGER.error("非法的Key...", e);
        } catch (NoResponseException e) {
            LOGGER.error("没有响应...", e);
        } catch (XmlPullParserException e) {
            LOGGER.error("XML解析错误...", e);
        } catch (ErrorResponseException e) {
            LOGGER.error("响应错误...", e);
        } catch (InternalException e) {
            LOGGER.error("内部错误...", e);
        } catch (InvalidArgumentException e) {
            LOGGER.error("内部错误...", e);
        }
        return null;
    }

    /**
     * 获取实体
     * @return
     */
    public MinioClient getInstance() {
        // 如果当前线程没有 MinioClient，就构建一个
        if(threadLocal.get() == null){
            String domain = "http://" + this.host + ":" + this.port;
            try {
                MinioClient client = new MinioClient(domain, accessKey, accessSecret);
                threadLocal.set(client);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return threadLocal.get();
    }


    public static final int MINIO_PORT = 80;
    public static final String MINIO_IP = "http://codeminio.nat300.top";
    public static final String MINIO_DOMAIN = MINIO_IP + ":" + MINIO_PORT;
    public static final String PATTERN_SYSTEM_BUCKET = "pattern-system";
    public static final String PATTERN_PATH = "pattern-file";
    public static final String COVER_PATH = "cover-file";
    public static final String MINIO_ACCESS_KEY = "AKIAIOSFODNN7EXAMPLE";
    public static final String MINIO_SECRET_KEY = "wJalrXUtnFEMI/K7MDENG/bPxRfiCYEXAMPLEKEY";
}
