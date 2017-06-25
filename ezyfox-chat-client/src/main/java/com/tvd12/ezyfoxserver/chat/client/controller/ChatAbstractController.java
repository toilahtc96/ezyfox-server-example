package com.tvd12.ezyfoxserver.chat.client.controller;

import com.tvd12.ezyfoxserver.chat.client.ChatSingleton;
import com.tvd12.ezyfoxserver.chat.client.constant.ChatEventType;
import com.tvd12.ezyfoxserver.chat.client.model.ChatModel;
import com.tvd12.ezyfoxserver.chat.client.model.ChatModelFactory;
import com.tvd12.ezyfoxserver.chat.client.view.ChatViewFactory;

import javafx.event.ActionEvent;
import javafx.scene.Node;

/**
 * Created by tavandung12 on 6/22/17.
 */
@SuppressWarnings({"restriction"})
public abstract class ChatAbstractController<M extends ChatModel> implements ChatController {

	@Override
    public void control(ActionEvent event) {
        M model = getModel(event);
        String result = model.execute();
        controlModelResult(event, model, result);
    }
	
	protected M getModel(ActionEvent event) {
		return getModelFactory().newModel(getEventType(event));
	}
	
	protected void controlModelResult(ActionEvent event, M model, String result) {
		hideCurrentView(event);
	}
	
	protected abstract ChatEventType getEventType(ActionEvent event);
	
    protected void hideCurrentView(ActionEvent event) {
        if(event != null) {
            Node source = (Node) event.getSource();
            source.getParent().getScene().getWindow().hide();
        }
    }
    
    protected ChatViewFactory getViewFactory() {
    	return ChatSingleton.getInstance().getViewFactory();
    }
    
    protected ChatModelFactory getModelFactory() {
    	return ChatSingleton.getInstance().getModelFactory();
    }
    
}
