package com.bizideal.mn.file.action;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.channels.FileChannel;

/**
 * 文件上传工具类
 */
public class FileUtil {

	/**
	 * 指定位置开始写入文件
	 * 
	 * @param tempFile
	 *            输入文件
	 * @param outPath
	 *            输出文件的路径(路径+文件名)
	 * @throws IOException
	 */
	public static int randomAccessFile(String outPath, int chunks,
			String tempPath) {
		FileInputStream fis = null;
		FileOutputStream fos = null; 
		FileChannel outChannel =null;
		FileChannel inChannel = null;
		try {
			File outputFile = new File(outPath);
			// 创建文件
			if (!outputFile.exists()) {
				System.out.println("文件不存在");
				outputFile.createNewFile();
			} else {
				System.out.println("文件存在");
			}

			System.out.println(outputFile);
			// 输出流
			fos = new FileOutputStream(outputFile);
			outChannel = fos.getChannel();

			for (int i = 0; i < chunks; i++) {
				File tempFile = new File(tempPath + i + ".tmp");
				fis = new FileInputStream(tempFile);
				inChannel = fis.getChannel();
				inChannel.transferTo(0, inChannel.size(), outChannel);
				inChannel.close();
				tempFile.delete();
			}
			return 0;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
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
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return 1;
	}

}
