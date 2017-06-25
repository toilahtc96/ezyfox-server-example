package com.tvd12.ezyfoxserver.chat.client.controller;

import com.tvd12.ezyfoxserver.chat.client.constant.ChatEventType;
import com.tvd12.ezyfoxserver.chat.client.model.ChatConnectionModel;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@SuppressWarnings("restriction")
public class ChatConnectionController 
		extends ChatAbstractController<ChatConnectionModel> {

	@FXML
	private TextField hostTextField;

	@FXML
	private TextField portTextField;
	
	@Override
	protected void controlModelResult(ActionEvent event, ChatConnectionModel model, String result) {
	}
	
	@Override
	protected ChatConnectionModel getModel(ActionEvent event) {
		ChatConnectionModel model = super.getModel(event);
		model.setHost(hostTextField.getText());
		model.setPort(Integer.parseInt(portTextField.getText()));
		return model;
	}
	
	public void onConnectActive(ActionEvent event) {
		control(event);
	}
	
	public void onCancelActive(ActionEvent event) {
		System.exit(0);
	}
	
	@Override
	protected ChatEventType getEventType(ActionEvent event) {
		return ChatEventType.CONNECT;
	}
	
}
