package org.novamedia.novamail.jobserver.runtime;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import org.apache.log4j.Logger;
import org.novamedia.novamail.jobserver.model.DOJob;

import com.google.common.util.concurrent.AbstractIdleService;

public class DispatcherService extends AbstractIdleService {

	private static final Logger log = Logger.getLogger(DispatcherService.class);

	private JobScheduler jobScheduler;
	private EmfService emfService;
	
	public DispatcherService(JobScheduler jobScheduler, EmfService emfService) {
		super();
		this.jobScheduler = jobScheduler;
		this.emfService = emfService;
	}

	@Override
	protected void startUp() throws Exception {
		Timer startupListenerTimer = new Timer();
		startupListenerTimer.schedule(new TimerTask() {
			@Override
			public void run() {
				log.info("startUp: [jobScheduler.isRunning():" + jobScheduler.isRunning() + "] [emfService.isRunning():" + emfService.isRunning() + "]");
				if (jobScheduler.isRunning() && emfService.isRunning()) {
					startupListenerTimer.cancel();
					EntityManager em = emfService.getEm();
					TypedQuery<DOJob> query = em.createQuery("SELECT c FROM DOJob c WHERE c.jobStatus = 0 OR c.jobStatus = 1", DOJob.class);
					List<DOJob> results = query.getResultList();
					for (DOJob doJob : results) {
						log.info("doJob: " + doJob.getJobUuid());
						// TODO : deserialize job
//						jobScheduler.scheduleJob(job);
					}
				}
			}
		}, 1000, 3*1000);
	}

	@Override
	protected void shutDown() throws Exception {
	}

}
