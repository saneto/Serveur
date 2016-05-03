package Device;
import java.util.ArrayList;
import java.util.Date;

import Message.NbNom;

public class Device 
{
	 	public boolean connecter;
	    public String nom;
	    public String ip;
	    public int port;
	    public String mdp;
	    public String position;
	    public ArrayList<NbNom> nbContact=new ArrayList<NbNom>();
	    public Device(String name, String address) {
	        this.nom = name;
	        this.ip = address;
	    }

	    public Device(String name, String mdp,String ip,int port ,String  position,boolean connecter) {
	        this.nom = name;
	        this.mdp=mdp;
	        this.port=port;
	        this.connecter=connecter;
	        this.ip = ip;
	        this.position= position;
	    }
	    
	    @Override
	    public boolean equals(Object obj) {
	        return (nom.equals(((Device) obj).nom ));
	    }
}
