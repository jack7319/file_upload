package com.bizideal.mn.file.controller;

import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.io.FileUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author 作者 liulq:
 * @data 创建时间：2016年12月22日 下午2:35:46
 * @version 1.0
 */
@Controller
@RequestMapping("/file")
public class UploadController {

	// 服务器的目录
	private String serverPath = "e:/uploader/";

	@RequestMapping("/upload")
	public String upload(HttpServletRequest request,
			HttpServletResponse response) throws IOException {
		// 1.创建DiskFileItemFactory对象,配置缓存信息
		DiskFileItemFactory factory = new DiskFileItemFactory();

		// 2.创建ServletFileUpload对象
		ServletFileUpload sfu = new ServletFileUpload(factory);

		// 3.设置文件名称的编码
		sfu.setHeaderEncoding("utf-8");

		// 4.开始解析文件
		String fileMd5 = null;
		// 分块索引
		String chunk = null;
		try {
			List<FileItem> items = sfu.parseRequest(request);

			// 5.获取文件信息
			for (FileItem item : items) {

				// 6.判断是文件还是普通数据
				if (item.isFormField()) {
					// 普通数据

					String fieldName = item.getFieldName();

					if (fieldName.equals("info")) {
						// 获取文件信息
						String info = item.getString("utf-8");
						System.out.println(info);
					}

					if (fieldName.equals("fileMd5")) {
						// 获取文件信息
						fileMd5 = item.getString("utf-8");
						System.out.println(fileMd5);
					}

					if (fieldName.equals("chunk")) {
						// 获取文件信息
						chunk = item.getString("utf-8");
						System.out.println(chunk);
					}

				} else {
					// 文件

					/*
					 * //获取文件名称 String name = item.getName();
					 * 
					 * //获取文件的实际内容 InputStream is = item.getInputStream();
					 * 
					 * //保存文件 FileUtils.copyInputStreamToFile(is, new
					 * File(serverPath+"/"+name));
					 */

					// 保存分块文件

					// 1.创建一个唯一目录，保存分块文件
					File file = new File(serverPath + "/" + fileMd5);
					if (!file.exists()) {
						// 创建目录
						file.mkdir();
					}

					// 2.保存文件
					File chunkFile = new File(serverPath + "/" + fileMd5 + "/"
							+ chunk);

					FileUtils.copyInputStreamToFile(item.getInputStream(),
							chunkFile);
				}
			}

		} catch (FileUploadException e) {
			e.printStackTrace();
		}
		return null;
	}
}
