package com.ccz.modules.domain.file;

import com.ccz.modules.common.action.SessionItem;
import com.ccz.modules.common.utils.ImageResizeWorker;
import io.netty.channel.Channel;
import lombok.Getter;

import java.io.IOException;

public class FileSession extends SessionItem<UploadFile> {
	@Getter String scode;
	
	public FileSession(Channel ch, int methodType) {
		super(ch, methodType);
	}

	@Override
	public String getKey() {
		return super.item.getFile().getFileId();
	}

	@Override
	public FileSession putSession(String scode, UploadFile t) {
		super.item = t;
		this.scode = scode;
		return this;
	}
	
	public void write(byte[] buf) throws IOException {
		super.item.write(buf, buf.length);
	}
	
	public boolean isOverSize() { return item.isOverSize(); }
	
	public UploadFile commit(ImageResizeWorker imageResizeWorker) throws IOException {
		return super.item.commit(scode, imageResizeWorker);
	}
	
	public void discard() throws IOException {
		super.item.discard();
	}
}
