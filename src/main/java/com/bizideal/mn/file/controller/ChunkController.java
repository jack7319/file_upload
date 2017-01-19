package com.bizideal.mn.file.controller;

import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author 作者 liulq:
 * @data 创建时间：2016年12月22日 下午2:39:29
 * @version 1.0
 */
@Controller
@RequestMapping("/chunk")
public class ChunkController {

	private String serverPath = "e:/uploader/";

	@RequestMapping("/check")
	public String check(HttpServletRequest request,
			HttpServletResponse response, String action) throws IOException {

		if (action.equals("mergeChunks")) {
			// 合并文件

			// 需要合并的文件的目录标记
			String fileMd5 = request.getParameter("fileMd5");
			System.out.println(fileMd5);

			// 读取目录里面的所有文件
			File f = new File(serverPath + "/" + fileMd5);
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
					if (Integer.parseInt(o1.getName()) < Integer.parseInt(o2
							.getName())) {
						return -1;
					}
					return 1;
				}

			});

			File outputFile = new File(serverPath + "/"
					+ UUID.randomUUID().toString() + ".zip");

			// 创建文件
			outputFile.createNewFile();

			// 输出流
			FileOutputStream fos = new FileOutputStream(outputFile);
			FileChannel outChannel = fos.getChannel();

			// 合并
			FileChannel inChannel=null;
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
			File tempFile = new File(serverPath + "/" + fileMd5);
			if (tempFile.isDirectory() && tempFile.exists()) {
				tempFile.delete();
			}

			System.out.println("合并成功");
		} else if (action.equals("checkChunk")) {
			// 检查当前分块是否上传成功
			String fileMd5 = request.getParameter("fileMd5");
			String chunk = request.getParameter("chunk");
			String chunkSize = request.getParameter("chunkSize");

			// 找到分块文件
			File checkFile = new File(serverPath + "/" + fileMd5 + "/" + chunk);

			response.setContentType("text/html;charset=utf-8");
			// 检查文件是否存在，且大小是否一致
			if (checkFile.exists()
					&& checkFile.length() == Integer.parseInt(chunkSize)) {
				// 上传过了
				response.getWriter().write("{\"ifExist\":1}");
			} else {
				// 没有上传过
				response.getWriter().write("{\"ifExist\":0}");
			}

		}
		return null;
	}
}
