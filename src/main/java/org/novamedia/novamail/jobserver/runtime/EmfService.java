package org.novamedia.novamail.jobserver.runtime;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import org.apache.log4j.Logger;

import com.google.common.util.concurrent.AbstractIdleService;

public class EmfService extends AbstractIdleService {

	private static final Logger log = Logger.getLogger(EmfService.class);

	private EntityManagerFactory emf;
	private EntityManager em;

	public EmfService() {
		log.debug("EmfService.init()");
	}
	
	@Override
	protected void startUp() throws Exception {
		log.debug("EmfService.startUp()");
		emf = Persistence.createEntityManagerFactory("test");
		em = emf.createEntityManager();
	}

	@Override
	protected void shutDown() throws Exception {
		log.debug("EmfService.shutDown()");
		try {
			em.close();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		try {
			emf.close();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	public EntityManager getEm() {
		return em;
	}
	
}
