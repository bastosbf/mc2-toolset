package br.lncc.sinapad.cli.server;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface Command extends Remote {

	String execute(String uuid, String... params) throws RemoteException;

	String usage(String uuid, String... params) throws RemoteException;

}
