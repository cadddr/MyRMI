/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.rpi.cogsci.leia.rmi;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

import static java.lang.String.format;

/**
 *
 * @author pb
 */
public class RmiServiceLauncher {
    public static void main(String[] args) throws RemoteException, ClassNotFoundException, InstantiationException, IllegalAccessException {
        System.out.println("Launcher " + args.length);
        if (args.length < 2) {
            System.out.println("Wrappable class name and service id are required.");
            return;
        }
        String cls = args[0];
        String id  = args[1];
        int port   = args.length > 2 ? Integer.parseInt(args[2]) : 1099;

        System.out.println(format("%s @ %s : %s", cls, id, port));
        
        launch(cls, id, port);
    }
    
    private static void launch(String cls, String id, int port) throws ClassNotFoundException, InstantiationException, RemoteException, IllegalAccessException {
        Class<?> implementationClass = Class.forName(cls);
        Remote implementation = (Remote) implementationClass.newInstance();
        
        Registry registry = LocateRegistry.createRegistry(port);
        registry.rebind(id, implementation);
        
        System.out.println(format("%s has started.", id));
    }
}
