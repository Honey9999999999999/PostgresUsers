package org.example.service;

import org.example.dao.ContentDAO;
import org.example.dao.GenericDAO;
import org.example.model.Content;

import java.util.List;

public record ContentService(GenericDAO<Content> contentGenericDAO, ContentDAO contentDAO) {
    public void save(Content content){
        contentGenericDAO.save(content);
    }

    public void update(Content content){
        contentGenericDAO.update(content);
    }

    public Content findById(Long id){
        return contentGenericDAO.findById(id);
    }

    public <T extends Content> List<T> findContentByUserId(Class<T> type, Long id){
        return contentDAO.findContentByUserId(type, id);
    }
}
