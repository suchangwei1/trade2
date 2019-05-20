package com.trade.oss;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClient;
import com.aliyun.oss.model.ObjectMetadata;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.PostConstruct;
import java.io.InputStream;

@Service
public class SimpleAliyunService implements AliyunService {

    @Value("${oss.ACCESS_ID}")
    private String ACCESS_ID;
    @Value("${oss.ACCESS_KEY}")
    private String ACCESS_KEY;
    @Value("${oss.bucket}")
    private String bucket;
    @Value("${oss.endpoint}")
    private String endpoint;

    private OSS client;

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @PostConstruct
    public void init() {
        logger.debug("init oss client");
        client = new OSSClient(endpoint, ACCESS_ID, ACCESS_KEY);
        // 创建bucket，如果不存在
        if (!client.doesBucketExist(bucket)) {
            logger.debug("init oss bucket");
            client.createBucket(bucket);
        }
    }

    @Override
    public void updateFile(InputStream in, String fileName, String contextType) throws Exception {
        logger.debug("upload file {}.", fileName);
        ObjectMetadata objectMeta = new ObjectMetadata();
        objectMeta.setContentLength(in.available());
        objectMeta.setContentType(contextType);
        client.putObject(bucket, fileName, in, objectMeta);
        in.close();
        logger.debug("upload file {} success.", fileName);
    }

    @Override
    public void uploadFile(MultipartFile file, String fileName)
            throws Exception {
        updateFile(file.getInputStream(), fileName, file.getContentType());
    }

    @Override
    public void deleteFile(String fileName) {
        logger.debug("delete {} from aliyun oss", fileName);
        try {
            if (StringUtils.isNotBlank(fileName)) {
                client.deleteObject(bucket, fileName);
            }
        } catch(Exception e) {
            logger.debug("delete {} from aliyun oss fails", fileName);
            e.printStackTrace();
        }
    }


}
