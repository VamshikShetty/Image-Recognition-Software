package IReS;

import javax.swing.JFrame;

public class IReS 
{
	static JFrame jf=null;
	static ires_main obj;
	
	public static void main(String[] args) 
	{
		// create a object of GUI version to upload and display result of image classification
		obj = new ires_main();
		
		// ires_m function runs creates the GUI
		obj.ires_m(jf);
	}		
}
