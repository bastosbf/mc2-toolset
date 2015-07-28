package br.lncc.sinapad.portengin.scheduler;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import br.lncc.sinapad.portengin.utils.PortEnginUtils;

public abstract class PortEnginScheduler {

	private static Logger logger = Logger.getLogger(PortEnginScheduler.class);

	private static boolean started = Boolean.FALSE;
	private static Thread thread;
	private static File storage;

	public static void start(File storage, final int interval) {
		if (!started) {
			PortEnginScheduler.storage = storage;
			thread = new Thread(new Runnable() {
				public void run() {
					while (true) {
						check();
						try {
							Thread.sleep(interval);
						} catch (InterruptedException e) {
							PortEnginUtils.handleException(e, logger);
						}
					}
				}
			});
			thread.start();
			started = Boolean.TRUE;
		}
	}

	public static void stop() {
		if (started) {
			thread.interrupt();
			started = Boolean.FALSE;
		}
	}

	public static synchronized void addWorker(PortEnginSchedulerWorker worker) {
		try {
			List<PortEnginSchedulerWorker> workers = readWorkers();
			if (workers == null) {
				workers = new ArrayList<PortEnginSchedulerWorker>();
			}
			workers.add(worker);
			writeWorkers(workers);
		} catch (FileNotFoundException e) {
			PortEnginUtils.handleException(e, logger);
		} catch (IOException e) {
			PortEnginUtils.handleException(e, logger);
		} catch (ClassNotFoundException e) {
			PortEnginUtils.handleException(e, logger);
		}

	}

	private static synchronized void writeWorkers(List<PortEnginSchedulerWorker> data) throws IOException {
		ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(storage, false));
		oos.writeObject(data);
		oos.flush();
		oos.close();
	}

	private static synchronized List<PortEnginSchedulerWorker> readWorkers() throws FileNotFoundException, IOException, ClassNotFoundException {
		if (storage.exists()) {
			ObjectInputStream ois = new ObjectInputStream(new FileInputStream(storage));
			Object obj = ois.readObject();
			ois.close();
			if (obj != null) {
				return (List<PortEnginSchedulerWorker>) obj;
			}
		}
		return null;
	}

	private static synchronized void check() {
		try {
			boolean modified = Boolean.FALSE;
			List<PortEnginSchedulerWorker> workers = readWorkers();
			List<PortEnginSchedulerWorker> oldWorkers = new ArrayList<PortEnginSchedulerWorker>();
			if (workers != null && workers.size() > 0) {
				for (PortEnginSchedulerWorker worker : workers) {
					if (worker.canExecute()) {
						worker.execute();
						modified = Boolean.TRUE;
						oldWorkers.add(worker);
					}
				}
				if (modified) {
					workers.removeAll(oldWorkers);
					writeWorkers(workers);
				}
			}
		} catch (FileNotFoundException e) {
			PortEnginUtils.handleException(e, logger);
		} catch (IOException e) {
			PortEnginUtils.handleException(e, logger);
		} catch (ClassNotFoundException e) {
			PortEnginUtils.handleException(e, logger);
		}
	}

}
