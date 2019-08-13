package com.ccz.modules.common.form;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.Getter;

@Getter
public class FormCommon {

	private String scode;
	private String cmd;
	private String token;

	public FormCommon(JsonNode jNode) {
		if(jNode==null)
			return;
		scode = jNode.get("scode").asText();
		cmd   = jNode.get("cmd").asText();
		token   = jNode.get("token").asText();
	}

	public FormCommon(FormCommon form) {
		this.copy(form);
	}
	
	public void copy(FormCommon form) {
		this.scode = form.scode;
		this.cmd = form.cmd;
		this.token = form.token;
	}

}
