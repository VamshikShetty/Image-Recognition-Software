//http://www.mkyong.com/java/apache-httpclient-examples/
//http://www.vogella.com/tutorials/ApacheHttpClient/article.html

//to get import orr.apache download the jar file 

package IReS;
import javax.swing.*;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.*;  
import java.awt.Graphics;
import java.awt.Image;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;




import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;



public class ires_main {
	public JFrame jframe;
	public JLabel jlab;
	
	// two textarea one to see the current result other to compare the result obtained with any/previous result
	public JTextArea narea,parea;
	public float w_jframe,h_jframe;
	public JMenuBar menuBar;
	public JMenuItem menutem;
	public JMenu file;
	private Scanner scan;
	

	
	
	Graphics g;
	String file_path="";
	public void ires_m(JFrame jframe)
	{
		// this gets the screen size of the user desktop
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		
		// create the JFrame
		jframe = new JFrame("IReS");
		
		// set the jFrame to size of user desktop in both width and height
		jframe.setExtendedState(jframe.getExtendedState() | JFrame.MAXIMIZED_BOTH);
		
		// add close function at the top left corner 
		jframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		

		// Create a Jpanel to display the preview of the selected image   
        JPanel jpanel=new JPanel();  
        jpanel.setBounds(10,50,290,290);    
        jpanel.setBackground(Color.gray); 
		jframe.add(jpanel);
        
		// label for select image button
        JLabel jlab = new JLabel("Select image file: ");
		jlab.setBounds(10,10, 100,20);  
		jframe.add(jlab);
		
		//file button (used to open the dialog box to select the image that needs to be uploaded)
		JButton jbutton=new JButton("select");  
	    jbutton.setBounds(110,10,70,20);  
	    jframe.add(jbutton);
	    
	    // Event or function that needs to run when "image select" button is pressed 
	    jbutton.addActionListener(new ActionListener()
	    {  
	    	public void actionPerformed(ActionEvent e)
	    	{  	
	    		//open file dialog  -> which is used to select the image for preview and upload
	    		JFileChooser fc=new JFileChooser();    
	    	    int i=fc.showOpenDialog(null);
	    	    
	    	    // Check if the file is uploaded
	    	    if(i==JFileChooser.APPROVE_OPTION){    
	    	    	// create the object for selected file
	    	        File f=fc.getSelectedFile();    
	    	        
	    	        // If the file is then get the path of the file in user system which can be used for http upload
	    	        file_path=f.getPath();    
	    	        try
	    	        {  
	    	        	// removeAll() is used to remove any previous image on display in Jpanel 
	    	        	jpanel.removeAll();
	    	        	
	    	        	// the file or image is selected 
	    	        	// converted to form which is possible to be displayed on jPanel
	    	        	ImageIcon img_ic =new ImageIcon(file_path);
	    	        	Image image = img_ic.getImage();
	    	        	
	    	        	// image is scaled down to 290x290 for preview as it the size of the Jpanel
	    	        	Image newimg = image.getScaledInstance(290, 290,  java.awt.Image.SCALE_SMOOTH);
	    	        	ImageIcon new_img_ic = new ImageIcon(newimg);
	    	        
	    	        	
	    	        	JLabel image_l = new JLabel("", new_img_ic, JLabel.HORIZONTAL);
	    	        	jpanel.add(image_l, BorderLayout.CENTER);
	    	        	jpanel.revalidate(); 
	    	        	jpanel.repaint();  
 
	    	        }
	    	        catch (Exception ex) 
	    	        {
	    	        	ex.printStackTrace();  
	    	        }                 
	    	    }      
	        }  
	    });  
	    
	  // Button for uploading the file to server 
	  JButton jsub=new JButton("Submit");  
	  jsub.setBounds(310,50,100,20);  
	  jframe.add(jsub);
	  
	  // event listener for the above button
	  jsub.addActionListener(new ActionListener()
	  {  
		  public void actionPerformed(ActionEvent e) 
		  {
			  
			  try 
			  {
				// path of selected image which we set in above event listener
				// post_img is user defined function which does http call to the server
				String content = post_img(file_path);
				narea.setText(content);
			
			  } 
			  catch (IOException e1) 
			  {
				e1.printStackTrace();
			}
		  }  
	  });  
	     
	  	
	    // Button to save the obtained result
	  	JButton saveButton=new JButton("Save");  
	  	saveButton.setBounds(10,350,100,25);  
	    jframe.add(saveButton);
	    
	    //Event call for the above function
	    saveButton.addActionListener(new ActionListener()
		  {  
			  public void actionPerformed(ActionEvent e) 
			  {
				  
				  JFileChooser save = new JFileChooser();
				  
				  // dialog box to select the path and file name and extension under which the result needs to be saved
				  int option = save.showSaveDialog(null);
					
				  	// check if the the path is being select ( Given there might be chance that user will cancel instead of saving it )
					if (option == JFileChooser.APPROVE_OPTION)
					{
						try 
						{	
							// Get path and file name as single string defined by user to save the result
							String temp = save.getSelectedFile().getPath();
							
							BufferedWriter out = new BufferedWriter(new FileWriter(temp));
							// write the text in the "New Area": narea where the output of classifier is obtained   
							out.write(narea.getText());
							out.close();
						} 
						catch (Exception ex) 
						{
							System.out.println(ex.getMessage());
						}
					}
			  }  
		  });
	    
	    
	    //Result TextEditior 
	    narea=new JTextArea();
	    
	    //making jtextarea scrollable both "New Area" and "Previous Area" textarea
	    jframe.add(new JScrollPane(narea),BorderLayout.CENTER);  
	    
	    // scroll option in both horizontal and vertical direction if the content of textarea in either direction 
        JScrollPane scrollableNarea = new JScrollPane(narea);  
        scrollableNarea.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);  
        scrollableNarea.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        
        
        scrollableNarea.setBounds(10,380,screenSize.width/2-15,screenSize.height/2-95);
	    jframe.add(scrollableNarea);
	    
	    
	    // some basic function for users
	    //menu for both open and save
		menuBar = new JMenuBar();
		menutem = new JMenuItem();

		file = new JMenu("File");
		JMenuItem newOption = new JMenuItem("New");
		JMenuItem open = new JMenuItem("Open");
		JMenuItem save = new JMenuItem("Save");
		
		menuBar.add(file);
		
		newOption.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				parea.setText("");
			}
		});
		
		open.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				JFileChooser open = new JFileChooser();
				int option = open.showOpenDialog(null);
				if (option == JFileChooser.APPROVE_OPTION)
				{	
					try
					{
						String temp = open.getSelectedFile().getPath();
						scan = new Scanner(new FileReader(temp));
						parea.setText("");
						while (scan.hasNext())
							parea.append(scan.nextLine()+"\n");
					} 
					catch (Exception ex)
					{
						System.out.println(ex.getMessage());
					}
				}
			}
		});
		
		
		save.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				JFileChooser save = new JFileChooser();
				int option = save.showSaveDialog(null);
				
				if (option == JFileChooser.APPROVE_OPTION)
				{
					try 
					{
						String temp = save.getSelectedFile().getPath();
						BufferedWriter out = new BufferedWriter(new FileWriter(temp));
						out.write(parea.getText());
						out.close();
					} catch (Exception ex) {
						System.out.println(ex.getMessage());
					}
				}
			}
		});
 

		file.add(newOption);
		file.add(open);
		file.add(save);
		
		menuBar.setBounds(screenSize.width/2,0,50,20);
	    jframe.add(menuBar);
	    
		
	    //Previous TextEditior/Textarea   
	    //
	    //   Previous means --> the Text area which can be used to hold the content of previous results 
	    //
	    parea=new JTextArea();
	    
	    //Making Previous JTextArea Scrollable
	    jframe.add(new JScrollPane(parea),BorderLayout.CENTER);  
	    
        JScrollPane scrollableParea = new JScrollPane(parea);  
        scrollableParea.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);  
        scrollableParea.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        
        scrollableParea.setBounds(screenSize.width/2,20,screenSize.width/2-10,screenSize.height-120);
	    jframe.add(scrollableParea);
	    
		
		jframe.setLayout(null);
		jframe.setVisible(true);
		
		
	}
	
	
public String post_img(String path) throws ClientProtocolException, IOException
{
		 String content;
		 CloseableHttpClient client = HttpClients.createDefault();
		 
		 // check if the image path is selected 
		 if(path=="") 
		 {
			 // if the path is null then show warning to user to select the image 
			 JOptionPane.showMessageDialog(jframe,"Please Select a file","WARNING",JOptionPane.WARNING_MESSAGE);
			 // return null to be displayed on new Area
			 return "";
		 }
		 else 
		 {
		 
			 try
			{
		     // link to which we will making the post request 
			 // on which our classifier is running
			 HttpPost httpPost = new HttpPost("http://127.0.0.1:5000/inceptionv3/");
	    
			 MultipartEntityBuilder builder = MultipartEntityBuilder.create();
	    
			 // the input is given in form pair as defined by Http protocal
			 // pic : holds the image file that is going to be streamed
			 builder.addBinaryBody("pic", new File(path),
			 ContentType.APPLICATION_OCTET_STREAM, "file.ext");
	    
			 HttpEntity multipart = builder.build();
			 // its is set dict or entity in name value pair --> { pic: ImageFile }
			 httpPost.setEntity(multipart);
			 // excute the post function on the link with this pair
			 HttpResponse response = client.execute(httpPost);
			 
			 // wait it returns a result 
			 HttpEntity entity = response.getEntity();
	    
			 // extract the content of the response
			 content = EntityUtils.toString(entity);
		 }
	    catch(Exception e) 
		 {
	    	content = "";
		 }
		 finally 
		 {
			 client.close();
		 }
	    return content;
		 }
	}
	
}
