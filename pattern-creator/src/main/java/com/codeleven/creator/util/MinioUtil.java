package com.codeleven.creator.util;

import io.minio.MinioClient;
import io.minio.errors.*;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

public class MinioUtil {
    private static MinioClient minioClient;
    private static final String MINIO_DOMAIN = "http://codeminio.nat300.top";
    private static final String MINIO_ACCESS_KEY = "AKIAIOSFODNN7EXAMPLE";
    private static final String MINIO_SECRET_KEY = "wJalrXUtnFEMI/K7MDENG/bPxRfiCYEXAMPLEKEY";
    private static final String PATTERN_SYSTEM_BUCKET = "pattern-system";
    private static final String COVER_PATH = "cover-file";
    private static final String PATTERN_PATH = "pattern-file";
    static {
        try {
            minioClient = new MinioClient(MINIO_DOMAIN, MINIO_ACCESS_KEY, MINIO_SECRET_KEY);
        } catch (InvalidEndpointException e) {
            e.printStackTrace();
        } catch (InvalidPortException e) {
            e.printStackTrace();
        }
    }

    public static InputStream getPatternData(String filePath){
        try {
            InputStream object = minioClient.getObject(PATTERN_SYSTEM_BUCKET, filePath);
            return object;
        } catch (InvalidBucketNameException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (InsufficientDataException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (NoResponseException e) {
            e.printStackTrace();
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        } catch (ErrorResponseException e) {
            e.printStackTrace();
        } catch (InternalException e) {
            e.printStackTrace();
        } catch (InvalidArgumentException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String uploadPatternCover(String fileName, InputStream is){
        String objectPath = COVER_PATH + "/" + System.currentTimeMillis() + "-" + fileName;
        try {

            minioClient.putObject(PATTERN_SYSTEM_BUCKET, objectPath, is, "application/octet-stream");
            return objectPath;
        } catch (InvalidBucketNameException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (InsufficientDataException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (NoResponseException e) {
            e.printStackTrace();
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        } catch (ErrorResponseException e) {
            e.printStackTrace();
        } catch (InternalException e) {
            e.printStackTrace();
        } catch (InvalidArgumentException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String uploadPatternData(String fileName, InputStream is){
        String objectPath = PATTERN_PATH + "/" + System.currentTimeMillis() + "-" + fileName;
        try {
            minioClient.putObject(PATTERN_SYSTEM_BUCKET, objectPath, is, "application/octet-stream");
            return objectPath;
        } catch (InvalidBucketNameException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (InsufficientDataException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (NoResponseException e) {
            e.printStackTrace();
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        } catch (ErrorResponseException e) {
            e.printStackTrace();
        } catch (InternalException e) {
            e.printStackTrace();
        } catch (InvalidArgumentException e) {
            e.printStackTrace();
        }
        return null;
    }
}
