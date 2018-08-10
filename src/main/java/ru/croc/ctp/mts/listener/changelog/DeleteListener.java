package ru.croc.ctp.mts.listener.changelog;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import ru.croc.ctp.jxfw.core.domain.DomainObject;
import ru.croc.ctp.jxfw.core.store.events.BeforeStoreEvent;
import ru.croc.ctp.jxfw.core.store.events.BeforeStoreEventListener;
import ru.croc.ctp.mts.domain.ChangeLog;
import ru.croc.ctp.mts.domain.TypeAction;
import ru.croc.ctp.mts.domain.repo.ChangeLogRepository;
import ru.croc.ctp.mts.security.CustomUserDetails;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

/**
 * Class implements catching of entities' instances deleting.
 * 
 * @author team1
 *
 */
@Component
public class DeleteListener implements BeforeStoreEventListener {

	@Autowired
	ChangeLogRepository changeLogRep;

	/**
	 * Method that catch deleting new entities' instances.
	 */
	@Override
	public void onApplicationEvent(BeforeStoreEvent event) {
		List<? extends DomainObject<?>> domainObjects = event.getStoreContext().getDomainObjects();
		domainObjects.forEach(domainObject -> {
			if (domainObject.isRemoved()) {
				ChangeLog changeLog = new ChangeLog();
				changeLog.setId(UUID.randomUUID().toString());
				LocalDateTime lDT = LocalDateTime.now();
				TypeAction tA = TypeAction.RemoveAct;
				String typeEntity = domainObject.getTypeName();
				String idEntity = domainObject.getId().toString();
				final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
				final CustomUserDetails principal = ((CustomUserDetails) authentication.getPrincipal());
				String author = principal.getUsername();
				System.out.println(lDT + "" + tA + "" + typeEntity + "" + idEntity + "" + author);
				changeLog.setChangedDate(lDT);
				changeLog.setTypeAction(tA);
				changeLog.setTypeEntity(typeEntity);
				changeLog.setEntityId(idEntity);
				changeLog.setAuthor(author);
				changeLogRep.save(changeLog);
				System.out.println(changeLog.toString());
			}
		});
	}
}
