package com.poly.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.poly.model.Notification;

import jakarta.transaction.Transactional;
@Repository
public interface NotificationRepository extends JpaRepository<Notification, Integer>{
	
	@Query("SELECT o FROM Notification o WHERE o.user.id = :idUser")
	List<Notification> findNotification(@Param("idUser") Integer idUser);
	
	@Modifying
	@Transactional
	@Query("DELETE FROM Notification n WHERE n.user.id = :idUser")
	void deleteByUserId(@Param("idUser") Integer idUser);
}