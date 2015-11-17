package edu.hypower.gatech.phidget;

import java.util.Vector;

import com.phidgets.*;
import com.phidgets.event.AttachEvent;
import com.phidgets.event.AttachListener;
import com.phidgets.event.DetachEvent;
import com.phidgets.event.DetachListener;

/**
 * Example class using the Phidget manager to look for new phidgets attached to
 * the system.
 * 
 * @author pjmartin
 *
 */
public class PhidgetDiscovery {

	public static void main(String[] args) {

		// boolean ready = false;
		// final Vector<Phidget> attachedPhidgets = new Vector<Phidget>();
		Manager mgr;

		try {
			mgr = new Manager();
			mgr.open();
			/*
			 * With listeners...
			 */
			mgr.addAttachListener(new AttachListener() {
				@Override
				public void attached(AttachEvent ae) {
					try {
						System.out.println("Phidget attached: " + ae.getSource().getDeviceName() + " sn="
								+ ae.getSource().getSerialNumber());
					} catch (PhidgetException e) {
						e.printStackTrace();
					}
				}
			});

			mgr.addDetachListener(new DetachListener() {
				@Override
				public void detached(DetachEvent de) {
					try {
						System.out.print(de.getSource().getSerialNumber() + " detached.");
					} catch (PhidgetException e) {
						e.printStackTrace();
					}
				}
			});

			while (true) {
				try {
					Thread.sleep(100);
				} catch (InterruptedException e) {
					mgr.close();
				}
			}
			/*
			 * Without listeners...
			 */
			// while(!ready){
			// if(!mgr.getPhidgets().isEmpty()){
			// // get and create the phidgets
			// Vector currPhidgets = mgr.getPhidgets();
			// for(int i = 0; i < currPhidgets.size(); i++){
			// Phidget p = (Phidget) currPhidgets.elementAt(i);
			// int sn = p.getSerialNumber();
			// p.open(sn);
			// p.waitForAttachment();
			// attachedPhidgets.addElement(p);
			// System.out.println(p.getDeviceName() + ": " + sn + " attached.");
			// }
			// ready = true;
			// }
			//
			// try {
			// Thread.sleep(500);
			// } catch (InterruptedException e) {
			// e.printStackTrace();
			// }
			//
			// }
			// System.out.println("All done!");
			// for(Phidget ap : attachedPhidgets){
			// ap.close();
			// }
			// mgr.close();
		} catch (PhidgetException e1) {
			e1.printStackTrace();
		}

		System.exit(0);
	}

}
