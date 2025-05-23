package org.graduate.shoefastbe.repository;

import org.graduate.shoefastbe.entity.Notification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {
    List<Notification> getNotificationByReadEqualsAndDeliverEquals(Boolean read, Boolean deliver);
}
