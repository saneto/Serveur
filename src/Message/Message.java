package Message;

public class Message 
{
	private String MSG;
	private boolean valide;
	public Message(String MSG,boolean valide)
	{
		this.setMSG(MSG);
		this.setValide(valide);
	}
	public boolean isValide() {
		return valide;
	}
	public void setValide(boolean valide) {
		this.valide = valide;
	}
	public String getMSG() {
		return MSG;
	}
	public void setMSG(String mSG) {
		MSG = mSG;
	}
}
