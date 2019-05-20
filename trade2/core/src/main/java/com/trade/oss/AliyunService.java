package com.trade.oss;

import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;

public interface AliyunService {

	/**
	 * 上传文件
	 * @param in
	 * @param fileName
	 * @param contextType
	 * @throws Exception
     */
	void updateFile(InputStream in, String fileName, String contextType) throws Exception;

    /**
     * 上传文件
     * @param file
     * @param fileName
     * @return
     * @throws Exception
     */
	void uploadFile(MultipartFile file, String fileName) throws Exception;

	/**
	 * 删除文件
	 * @param uri
	 * @throws Exception
	 */
	void deleteFile(String uri);

}
