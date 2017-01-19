package com.bizideal.mn.file.controller;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Random;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.disk.DiskFileItem;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import com.bizideal.mn.file.action.AjaxResultDto;
import com.bizideal.mn.file.action.BaseAction;
import com.bizideal.mn.file.action.FileUtil;

@Controller
@RequestMapping("/t")
public class FileController extends BaseAction {

	@RequestMapping("/t")
	public String t() {
		return "index";
	}

	/**
	 * 实现文件上传
	 * 
	 * @param fileUpload
	 * @param request
	 * @return
	 */
	@RequestMapping("/upload")
	public void fileUpload(
			@RequestParam("file") CommonsMultipartFile fileUpload,
			HttpServletRequest request, HttpServletResponse response) {

		long startTime = System.currentTimeMillis();

		System.out.println("===========");
		System.out.println(fileUpload.getSize());

		SimpleDateFormat sFormat = new SimpleDateFormat("yyyyMMddhhmmss");
		String fileName = sFormat.format(Calendar.getInstance().getTime())
				+ new Random().nextInt(1000);
		String originalFilename = fileUpload.getOriginalFilename();
		fileName += originalFilename.substring(originalFilename
				.lastIndexOf("."));
		String dirName = request.getSession().getServletContext()
				.getRealPath("/")
				+ "upload";

		double originalFilesize = request.getContentLength();// 获取源文件大小

		File file = new File(dirName);
		InputStream inputStream = null;
		FileOutputStream outputStream = null;
		if (!file.exists()) {
			file.mkdir();
		}
		try {
			inputStream = fileUpload.getInputStream();
			if (!inputStream.equals(null)) {
				try {
					// String table_type = request.getParameter("table_type");
					// DocManagement docManagement = new DocManagement();
					// docManagement.setFilename(originalFilename);
					// docManagement.setFileurl("/fileUpload/" + fileName);
					// docManagement.setFilesize(originalFilesize);
					// docManagement.setTable_type(table_type);
					// dManagementService.addDocFile(readCurrentOperator(),
					// docManagement);// 直接存入数据库表
				} catch (Exception e) {

					e.printStackTrace();
				}
			}
			outputStream = new FileOutputStream(dirName + "/" + fileName);
			byte[] buffer = new byte[1024 * 1024];
			int len = 0;
			while ((len = inputStream.read(buffer)) != -1) {
				outputStream.write(buffer, 0, len);
				outputStream.flush();
			}
			outputStream.close();
			inputStream.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		long endTime = System.currentTimeMillis();
		System.out.println((endTime - startTime) + "ms");

	}

	// 单文件上传后台代码
	@RequestMapping("/ajax")
	@ResponseBody
	public AjaxResultDto ajaxAttachUpload(
			@RequestParam("file") CommonsMultipartFile file, String chunk, // 当前第几个分片
			String chunks,// 总分片个数
			HttpServletRequest request, HttpServletResponse response) {

		DiskFileItem filetemp = (DiskFileItem) file.getFileItem();
		File file_material = filetemp.getStoreLocation();
		System.out.println("文件大小=" + file.getSize() / 1024);
		System.out.println(file_material.getAbsolutePath());
		System.out.println("chunk=" + chunk);
		System.out.println("chunks=" + chunks);
		System.out.println(file.getOriginalFilename());
		System.out.println("file.getName()="+file.getName());
		// 获取上传文件的路径
		String realPath = request.getSession().getServletContext()
				.getRealPath("/upload");
		System.out.println(realPath + File.separator
				+ file.getOriginalFilename());
		String filename = file.getOriginalFilename();
		try {
			FileUtils.copyFile(
					file_material,
					new File(file_material.getParent() + File.separator
							+ "233" + chunk + ".tmp"));
			if (Integer.valueOf(chunk) == (Integer.valueOf(chunks) - 1)) {
				System.out.println("开始合并文件。。。");
				FileUtil.randomAccessFile(
						realPath + File.separator + file.getOriginalFilename(),
						Integer.valueOf(chunks), file_material.getParent()
								+ File.separator + "233");
			}

			// 拿到文件对象
			// 第一个参数是目标文件的完整路径
			// 第二参数是webupload分片传过来的文件
			// FileUtil的这个方法是把目标文件的指针，移到文件末尾，然后把分片文件追加进去，实现文件合并。简单说。就是每次最新的分片合到一个文件里面去。

			// int a = FileUtil.randomAccessFile(realPath + File.separator
			// + filename, file_material);
			// System.out.println("a=" + a);

			// 如果文件小与5M的话，分片参数chunk的值是null
			// 5M的这个阈值是在upload3.js中的chunkSize属性决定的，超过chunkSize设置的大小才会进行分片，否则就不分片，不分片的话，webupload传到后台的chunk参数值就是null
			if (StringUtils.isEmpty(chunk)) {
				// 不分片的情况
				return outJson("0", "success", "");
			} else {
				// 分片的情况
				// chunk 分片索引，下标从0开始
				// chunks 总分片数
				if (Integer.valueOf(chunk) == (Integer.valueOf(chunks) - 1)) {

					return outJson("0", "上传成功", "");
				} else {
					return outJson("2", "上传中" + file_material.getName()
							+ " chunk:" + chunk, "");
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			return outJson("3", "上传失败", "");
		}
	}

	@RequestMapping("/down/attachment")
	public void attachment(String url, HttpServletResponse response) {
		response.addHeader("Content-Disposition", "attachment; filename="
				+ response.encodeURL(url));
	}

	@RequestMapping("/down/inline")
	public void inline(String url, HttpServletResponse response) {
		response.addHeader("Content-Disposition", "inline; filename="
				+ response.encodeURL(url));
	}
}
