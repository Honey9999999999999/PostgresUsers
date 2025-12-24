package org.example.dao;

import lombok.extern.slf4j.Slf4j;
import org.example.model.Friendship;
import org.example.model.User;
import org.example.util.DataBaseServices;
import org.example.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
public class FriendShipDAO {
    public void createFriendShip(User user, User friend){
        Transaction transaction = null;
        Friendship friendship;
        String successMessage = "Заявка отправлена!";
        try(Session session = HibernateUtil.getSessionFactory().openSession()){
            transaction = session.beginTransaction();

            String hql = "From Friendship f where f.user.id = :userId AND f.friend.id = :friendId";
            Optional<Friendship> optional = session.createQuery(hql, Friendship.class)
                    .setParameter("userId", user.getId())
                    .setParameter("friendId", friend.getId())
                    .uniqueResultOptional();
            if(optional.isPresent()){
                transaction.commit();
                log.info("Вы уже отправляли заявку этому пользователю.");
                return;
            }

            hql = "From Friendship f where f.user.id = :friendId AND f.friend.id = : userId";
            optional = session.createQuery(hql, Friendship.class)
                    .setParameter("userId", user.getId())
                    .setParameter("friendId", friend.getId())
                    .uniqueResultOptional();
            if(optional.isPresent()){
                friendship = new Friendship(friend, user);
                friendship.setStatus("CONFIRMED");
                successMessage = "Этот пользователь уже отправил заявку вам. Теперь вы друзья!";
            }
            else {
                friendship = new Friendship(user, friend);
            }

            session.merge(friendship);
            transaction.commit();
            log.info(successMessage);
        } catch (Exception e){
            log.error("Ошибка при выполнении транзакции в FriendShipDAO.createFriendShip", e);
        }
    }

    public List<Friendship> getOutFriendShip(Long userId){
        try(Session session = HibernateUtil.getSessionFactory().openSession()) {
            String hql = "From Friendship f where f.user.id = :userId AND f.status = 'PENDING'";
            return session.createQuery(hql, Friendship.class).setParameter("userId", userId).list();
        }
    }

    public List<Friendship> getInFriendShip(Long userId){
        try(Session session = HibernateUtil.getSessionFactory().openSession()) {
            String hql = "From Friendship f where f.friend.id = :userId AND f.status = 'PENDING'";
            return session.createQuery(hql, Friendship.class).setParameter("userId", userId).list();
        }
    }

    public List<User> getFriends(Long userId){
        try(Session session = HibernateUtil.getSessionFactory().openSession()){
            GenericDAO<User> userDAO = DataBaseServices.getInstance().userGenericDAO;
            String hql = "From Friendship f where f.status = 'CONFIRMED' AND (f.user.id = :userId OR f.friend.id = :userId)";

            List<Friendship> friendships = session.createQuery(hql, Friendship.class).setParameter("userId", userId).list();
            List<User> friends = new ArrayList<>();

            for(Friendship friendship : friendships){
                friends.add(userId.equals(friendship.getId().getUserId())
                        ? userDAO.findById(friendship.getId().getFriendId())
                        : userDAO.findById(friendship.getId().getUserId()));
            }
            
            return friends;
        }
    }

    public void updateFriendship(Friendship friendship){
        try(Session session = HibernateUtil.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();
            session.merge(friendship);
            transaction.commit();
        }catch (Exception e){
            log.error("Ошибка при выполнении транзакции в FriendShipDAO.update", e);
        }
    }
}
