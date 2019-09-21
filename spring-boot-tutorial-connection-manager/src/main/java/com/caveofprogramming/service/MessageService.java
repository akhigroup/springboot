package com.caveofprogramming.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.caveofprogramming.model.dto.SimpleMessage;
import com.caveofprogramming.model.entity.Message;
import com.caveofprogramming.model.entity.SiteUser;
import com.caveofprogramming.model.repository.MessageDao;

@Service
public class MessageService {
	
	@Autowired
	private MessageDao messageDao;
	
	@Async
	public void save(SiteUser fromUser, SiteUser toUser, String text) {
		messageDao.save(new Message(fromUser, toUser, text));
	}
	
	public List<SimpleMessage> fetchConversation(Long fromUserId, Long toUserId, int page) {
		
		PageRequest request = PageRequest.of(page, 12);
		
		Slice<Message> conversation =messageDao.fetchConversation(toUserId, fromUserId, request);
		
		return conversation.map(m -> new SimpleMessage(m, m.getFromUser().getId().compareTo(toUserId) == 0)).getContent();
	}
}
