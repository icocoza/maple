package com.ccz.modules.domain.file;

import com.ccz.modules.common.utils.ImageResizeWorker;
import com.ccz.modules.common.utils.ImageUtil;
import com.ccz.modules.common.utils.StrUtils;
import com.ccz.modules.repository.db.file.UploadFileRec;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class UploadFile {
	static final float THUMB_SIZE=480;
	public static final String THUMB_PATH = "thumb";
	public static final String CROP_PATH = "crop";

	static int seq=0;

	final UploadFileRec file;
	
	public long downsize = 0;
	public FileOutputStream fos;
	
	private String scode;
	private String uploadPath, filepath;

	private String thumbName;
	private int thumbWidth;
	private int thumbHeight;

	public UploadFile(UploadFileRec file) {
		this.file = file;
	}
	
	public boolean open(String scode, String serverIp, String uploadPath) {
		try {
			String dir = uploadPath +"/"+ scode;
			filepath = dir +"/"+ file.getFileId();
			this.uploadPath = uploadPath; 
			new File(dir).mkdirs();
			fos = new FileOutputStream(filepath);
			return true;
		}catch(Exception e) {
			e.printStackTrace();
			return false;
		}
	}
	public void write(byte[] buf, int size) throws IOException {
		fos.write(buf, 0, size);
		downsize += size;
	}
	public void discard() throws IOException {
		fos.close();
		new File(filepath).delete();
	}
	
	public UploadFile commit(String scode, ImageResizeWorker imageResizeWorker) throws IOException {
		this.scode = scode;
		fos.close();
		if(StrUtils.isImageFile(file.getFileName()) == true) {
			ImageUtil.ImageSize imageSize = ImageUtil.getImageSize(new File(filepath));
			file.setWidth(imageSize.getWidth());
			file.setHeight(imageSize.getHeight());
			makeThumb(imageResizeWorker);
		}
		return this;//DbAppManager.getInst().updateFileInfo(scode, file.getFileId(), file.getWidth(), file.getHeight(), downsize);
	}
	
	public UploadFileRec getFile() {	return file;		}
	
	public boolean isOverSize() { return file.getFileSize() <= downsize; }
	
	
	
	public UploadFile makeThumb(ImageResizeWorker imageResizeWorker) {
		float rate = file.getWidth() > file.getHeight() ? THUMB_SIZE / (float)file.getWidth() : THUMB_SIZE / (float)file.getHeight();
		thumbName = newThumbFilename();
		String dir = getThumbPath(scode, uploadPath);
		
		(new File(dir)).mkdirs();
		thumbWidth = (int)(file.getWidth() * rate);
		thumbHeight = (int)(file.getHeight() * rate);
		imageResizeWorker.doResize(filepath, dir + thumbName, thumbWidth, thumbHeight, new ImageResizeWorker.ImageResizerCallback() {
			@Override
			public void onCompleted(Object dest) {
				System.out.println(dest);
			}

			@Override
			public void onFailed(Object src) {
				System.out.println(src);
			}
		});
		return this;//DbAppManager.getInst().updateThumbnail(scode, file.fileid, thumbName, thumbWidth, thumbHeight);
	}
	
	public void makeCrop(String src, String dest) {
		
	}
	
	public static int getSeq() {	return ++seq % 1000; }
	
	public static String newThumbFilename() {
		 return String.format("%s%d_%03d", THUMB_PATH, System.currentTimeMillis(), getSeq());
	}
	public static String getThumbPath(String scode, String uploadPath) {
		return uploadPath +"/"+ scode +"/" + THUMB_PATH +"/";
	}
	
	public static String newCropFilename() {
		 return String.format("%s%d_%03d", CROP_PATH, System.currentTimeMillis(), getSeq());
	}
	public static String newCropFilename(String name) {
		 return String.format("%s%s", CROP_PATH, name);
	}

	public static String getCropPath(String scode, String uploadPath) {
		return uploadPath +"/"+ scode +"/" + CROP_PATH +"/";
	}

}
