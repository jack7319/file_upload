package com.bizideal.mn.web.controller;

import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.io.FileUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

/**
 * @author 作者 liulq:
 * @data 创建时间：2016年12月22日 下午3:16:18
 * @version 1.0
 */
@Controller
@RequestMapping("/f")
public class MeetingMaterialController {

	@RequestMapping("/u")
	@ResponseBody
	public ObjectNode upload(@RequestParam("file") CommonsMultipartFile file, String fileMd5, String chunk,
			String chunks, HttpServletRequest request) throws UnsupportedEncodingException, FileUploadException {

		ObjectMapper mapper = new ObjectMapper();
		ObjectNode node = mapper.createObjectNode();
		String realPath = request.getSession().getServletContext().getRealPath("/upload");
		String path = realPath + File.separator + fileMd5;
		File tmpFile = new File(path);
		if (!tmpFile.exists()) {
			tmpFile.mkdir();
		}

		File chunkFile = new File(path + File.separator + chunk);
		try {
			FileUtils.copyInputStreamToFile(file.getInputStream(), chunkFile);
		} catch (IOException e) {
			e.printStackTrace();
			node.put("errcode", 1);
			node.put("errmsg", e.getMessage());
			return node;
		}
		node.put("errcode", 0);
		node.put("errmsg", "ok,chunk=" + chunk);
		return node;
	}

	@RequestMapping("/check")
	@ResponseBody
	public ObjectNode check(String fileMd5, String chunk, String chunkSize, HttpServletRequest request) {
		ObjectMapper mapper = new ObjectMapper();
		ObjectNode node = mapper.createObjectNode();
		String realPath = request.getSession().getServletContext().getRealPath("/upload");
		// 找到分块文件
		File checkFile = new File(realPath + File.separator + fileMd5 + File.separator + chunk);

		// 检查文件是否存在，且大小是否一致
		if (checkFile.exists() && checkFile.length() == Integer.parseInt(chunkSize)) {
			// 上传过了
			node.put("errcode", 1);
			node.put("errmsg", "已经存在");
		} else {
			// 没有上传过
			node.put("errcode", 0);
			node.put("errmsg", "没有上传过");
		}
		return node;
	}

	@RequestMapping("/mergeChunks")
	@ResponseBody
	public ObjectNode mergeChunks(String fileMd5, String filename, HttpServletRequest request) {
		ObjectMapper mapper = new ObjectMapper();
		ObjectNode node = mapper.createObjectNode();
		String realPath = request.getSession().getServletContext().getRealPath("/upload");
		File f = new File(realPath + "/" + fileMd5);
		File[] fileArray = f.listFiles(new FileFilter() {

			// 排除目录，只要文件
			public boolean accept(File pathname) {
				if (pathname.isDirectory()) {
					return false;
				}
				return true;
			}
		});
		// 转成集合，便于排序
		List<File> fileList = new ArrayList<File>(Arrays.asList(fileArray));
		// 从小到大排序
		Collections.sort(fileList, new Comparator<File>() {
			public int compare(File o1, File o2) {
				if (Integer.parseInt(o1.getName()) < Integer.parseInt(o2.getName())) {
					return -1;
				}
				return 1;
			}
		});
		File outputFile = new File(realPath + File.separator + filename);
		// File outputFile = new File(realPath + File.separator
		// + UUID.randomUUID().toString() + ".zip");

		// 创建文件
		try {
			outputFile.createNewFile();

			// 输出流
			FileOutputStream fos = new FileOutputStream(outputFile);
			FileChannel outChannel = fos.getChannel();

			// 合并
			FileChannel inChannel = null;
			FileInputStream fis = null;

			for (File file : fileList) {
				fis = new FileInputStream(file);
				inChannel = fis.getChannel();
				inChannel.transferTo(0, inChannel.size(), outChannel);
				inChannel.close();
				// 删除分片
				file.delete();
			}
			if (inChannel != null) {
				inChannel.close();
			}
			if (outChannel != null) {
				outChannel.close();
			}
			if (fis != null) {
				fis.close();
			}
			if (fos != null) {
				fos.close();
			}

			// 清除文件夹
			File tempFile = new File(realPath + File.separator + fileMd5);
			if (tempFile.isDirectory() && tempFile.exists()) {
				tempFile.delete();
			}

			node.put("errcode", 0);
			node.put("errmsg", "文件合并完成");
			System.out.println("合并成功");
		} catch (IOException e) {
			e.printStackTrace();
			node.put("errcode", 1);
			node.put("errmsg", "文件合并失败");
		}
		return node;
	}
}
