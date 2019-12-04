package com.ccz.modules.domain.session;

import com.ccz.modules.common.action.SessionItem;
import com.ccz.modules.repository.db.user.UserRec;
import io.netty.channel.Channel;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class AuthSession extends SessionItem<UserRec> {
	private String scode;

	public AuthSession(Channel ch) {
		super(ch, 0);
	}
	public AuthSession(Channel ch, int methodType) {
		super(ch, methodType);
	}
	
	@Override
	public String getKey() {
		return super.item.getUserId();
	}

	@Override
	public AuthSession putSession(String scode, UserRec rec) {
		this.scode = scode;
		super.item = rec;
		return this;
	}
	
	public Channel getCh() {
		return super.channel;
	}

	public String getUserId() {
		return super.item.getUserId();
	}
	
	public String getUsername() {
		return super.item.getUserName();
	}
	
}
