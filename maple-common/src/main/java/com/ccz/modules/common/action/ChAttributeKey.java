package com.ccz.modules.common.action;

import com.ccz.modules.common.action.ProtocolWriter.IWriteProtocol;
import com.ccz.modules.domain.file.FileSession;
import com.ccz.modules.domain.session.AuthSession;
import io.netty.util.AttributeKey;

public class ChAttributeKey {
	
	public static AttributeKey<IWriteProtocol> getWriteKey() {
		return AttributeKey.valueOf(IWriteProtocol.class.getSimpleName());
	}
	public static AttributeKey<AuthSession> getAuthSessionKey() {
		return AttributeKey.valueOf(AuthSession.class.getSimpleName());
	}
	public static AttributeKey<FileSession> getFileSessionKey() {
		return AttributeKey.valueOf(FileSession.class.getSimpleName());
	}

}
