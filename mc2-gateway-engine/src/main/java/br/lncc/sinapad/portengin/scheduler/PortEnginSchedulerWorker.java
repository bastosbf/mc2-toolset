package br.lncc.sinapad.portengin.scheduler;

import java.io.Serializable;

public interface PortEnginSchedulerWorker extends Serializable {

	boolean canExecute();

	void execute();

}
