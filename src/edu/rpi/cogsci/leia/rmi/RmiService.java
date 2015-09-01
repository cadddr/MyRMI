/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.rpi.cogsci.leia.rmi;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 *
 * @author pb
 * @param <I>
 * @param <O>
 */
public interface RmiService<I, O> extends Remote {
    public abstract O call(I arg) throws RemoteException;
}
