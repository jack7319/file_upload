package com.bizideal.mn.file.action;

import java.io.File;

import org.apache.commons.lang3.StringUtils;

public class ContentsDetailAction2 extends BaseAction {

	private static final long serialVersionUID = 593845620458229765L;

	// 属性值，单文件的情况，对应的是upload3.js中的name属性，name属性值为file，此时spring就可以获取到file的文件对象，不需要实例化，spring框架会自动注入对象值，打开调试窗口，看一下就明白了
	private File file;
	// 单文件上传的文件名，spring上传特性，文件名格式为name属性+FieName
	private String fileFileName;
	// 多文件上传的文件对象数组
	private File[] multiFile;
	// 多文件上传文件对象的文件名
	private String[] multiFileFileName;

	// 属性值，接收webupload自带的参数
	private String chunk; // 当前第几个分片
	private String chunks;// 总分片个数
	private String size;// 单个文件的总大小

	// 单文件上传后台代码
	public void ajaxAttachUpload() {
		String path = "d:\\test\\" + fileFileName;
		try {
			// 拿到文件对象
			File file = this.getFile();
			// 第一个参数是目标文件的完整路径
			// 第二参数是webupload分片传过来的文件
			// FileUtil的这个方法是把目标文件的指针，移到文件末尾，然后把分片文件追加进去，实现文件合并。简单说。就是每次最新的分片合到一个文件里面去。
//			FileUtil.randomAccessFile(path, file);
			// 如果文件小与5M的话，分片参数chunk的值是null
			// 5M的这个阈值是在upload3.js中的chunkSize属性决定的，超过chunkSize设置的大小才会进行分片，否则就不分片，不分片的话，webupload传到后台的chunk参数值就是null
			if (StringUtils.isEmpty(chunk)) {
				// 不分片的情况
				outJson("0", "success", "");
			} else {
				// 分片的情况
				// chunk 分片索引，下标从0开始
				// chunks 总分片数
				if (Integer.valueOf(chunk) == (Integer.valueOf(chunks) - 1)) {
					outJson("0", "上传成功", "");
				} else {
					outJson("2", "上传中" + fileFileName + " chunk:" + chunk, "");
				}
			}
		} catch (Exception e) {
			outJson("3", "上传失败", "");
		}
	}

	public File getFile() {
		return file;
	}

	public void setFile(File file) {
		this.file = file;
	}

	public String getFileFileName() {
		return fileFileName;
	}

	public void setFileFileName(String fileFileName) {
		this.fileFileName = fileFileName;
	}

	public File[] getMultiFile() {
		return multiFile;
	}

	public void setMultiFile(File[] multiFile) {
		this.multiFile = multiFile;
	}

	public String[] getMultiFileFileName() {
		return multiFileFileName;
	}

	public void setMultiFileFileName(String[] multiFileFileName) {
		this.multiFileFileName = multiFileFileName;
	}

	public String getChunk() {
		return chunk;
	}

	public void setChunk(String chunk) {
		this.chunk = chunk;
	}

	public String getChunks() {
		return chunks;
	}

	public void setChunks(String chunks) {
		this.chunks = chunks;
	}

	public String getSize() {
		return size;
	}

	public void setSize(String size) {
		this.size = size;
	}
}